package com.sap.exciseduty.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.InternalServerErrorException;

import com.sap.exciseduty.client.ClientFactory;
import com.sap.exciseduty.client.rabbitmq.WorklistEventOrchestrator;
import com.sap.exciseduty.client.rabbitmq.exception.NoRabbitMQConnectionException;

@WebListener
public class RabbitMQServletContextListener implements ServletContextListener {

    private WorklistEventOrchestrator worklistEventManger;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (System.getenv("NO_RABBIT_CONSUMER") == null) {
            try {
                this.worklistEventManger = ClientFactory.getWorklistEventOrchestrator();
                this.worklistEventManger.startEventConsumer();
            } catch (NoRabbitMQConnectionException e) {

            } catch (Exception e) {
                throw new InternalServerErrorException(e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (System.getenv("NO_RABBIT_CONSUMER") == null) {
            this.worklistEventManger.close();
        }
    }

}
