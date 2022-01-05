package com.sap.exciseduty.utility;

import java.util.Locale;

import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import com.sap.xs2.security.commons.SAPOfflineTokenServicesCloud;
import com.sap.xs2.security.commons.SAPPropertyPlaceholderConfigurer;

@Configuration
@EnableWebSecurity
@EnableResourceServer
@Profile(value = { "default" })
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    public static final String CONFDISP_SCOPE = "ConfDisp";
    public static final String CONFEDIT_SCOPE = "ConfEdit";
    public static final String MDDISP_SCOPE = "MDDisp";
    public static final String MDEDIT_SCOPE = "MDEdit";
    public static final String SLDISP_SCOPE = "SLDisp";
    public static final String SLEDIT_SCOPE = "SLEdit";
    public static final String EXT_SCOPE = "Ext";
    public static final String TAXCALC_SCOPE = "ExecuteTaxCalc";
    public static final String OAUTH2_HAS_SCOPE = "#oauth2.hasScope('";
    public static final String OAUTH2_HAS_ANY_SCOPE = "#oauth2.hasAnyScope('";

    @Override
    public void configure(HttpSecurity http) throws Exception {

        String xsAppNameAppl = getXSAppName("application");
        String xsAppNameBroker = getXSAppName("broker");

        String hasConfScope = OAUTH2_HAS_ANY_SCOPE + xsAppNameAppl + "." + CONFDISP_SCOPE + "', '" + xsAppNameAppl + "." + CONFEDIT_SCOPE + "')";
        String hasMDScope = OAUTH2_HAS_ANY_SCOPE + xsAppNameAppl + "." + MDDISP_SCOPE + "', '" + xsAppNameAppl + "." + MDEDIT_SCOPE + "')";
        String hasSLScope = OAUTH2_HAS_ANY_SCOPE + xsAppNameAppl + "." + SLDISP_SCOPE + "', '" + xsAppNameAppl + "." + SLEDIT_SCOPE + "')";
        String hasExtScope = OAUTH2_HAS_SCOPE + xsAppNameAppl + "." + EXT_SCOPE + "')";
        String hasTaxCalcScope = OAUTH2_HAS_SCOPE + xsAppNameBroker + "." + TAXCALC_SCOPE + "')";

        http.sessionManagement()
                // session is created by approuter
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .authorizeRequests()

                .antMatchers(HttpMethod.POST, "/odata/v2/StockLedgerService/ExciseDutyCalculation*/**").access(hasTaxCalcScope)
                .antMatchers("/odata/v2/ExciseDutyTypeService*/**").access(hasConfScope)
                .antMatchers("/odata/v2/TaxRateService*/**").access(hasConfScope)
                .antMatchers("/odata/v2/MovementCategoryService*/**").access(hasConfScope)
                .antMatchers("/odata/v2/StockLedgerService*/**").access(hasSLScope)
                .antMatchers("/odata/v2/StockLedgerCharacteristicsService*/**").access(hasConfScope)
                .antMatchers("/odata/v2/ShipToMasterService*/**").access(hasMDScope)
                .antMatchers("/odata/v2/StockLedgerProcessingErrorService*/**").access(hasSLScope)
                .antMatchers("/odata/v2/ExtensionConfigurationService*/**").access(hasExtScope)
                .antMatchers("/odata/v2/StockLedgerAlpService*/**").access(hasSLScope)
                .antMatchers("/odata/v2/TaxWarehouseService*/**").access(hasConfScope)
                .antMatchers("/odata/v2/MaterialMasterService*/**").access(hasMDScope)

                .antMatchers("/health").permitAll()
                .antMatchers("/**").authenticated()
                .anyRequest().denyAll();
    }

    @Bean
    protected SAPOfflineTokenServicesCloud offlineTokenServices() {
        return new SAPOfflineTokenServicesCloud();
    }

    private String getXSAppName(String plan) throws ODataApplicationException {
        SAPPropertyPlaceholderConfigurer propertyHolder = new SAPPropertyPlaceholderConfigurer();
        String xsAppName = propertyHolder.readVcapServices("xs.appname", plan);
        if (xsAppName == null || xsAppName.isEmpty()) {
            String m = "Could not get property xs.appname from environment for plan" + plan + ".";
            logger.error(m);
            throw new ODataApplicationException(m, Locale.ENGLISH);
        }
        logger.info("GET xsAppName: {}", xsAppName);
        return xsAppName;
    }

}
