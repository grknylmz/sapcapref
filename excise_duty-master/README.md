# Excise Duty - Proof of Concept
* [Demo System](#demo-system)
* [Links](#links)
* [Learnings](#learnings)
* [Reported Tickets](#reported-tickets)
* [Open Points](#open-points)

# Demo System

## System access
The following access info can be used to demo the end-to-end scenario:
[S/4 Fiori Launchpad](https://uyr900-er8070.wdf.sap.corp/sap/bc/ui5_ui5/ui2/ushell/shells/abap/FioriLaunchpad.html#Shell-home) (user in UYR/900 and ER8/070 required)

[Excise Duty Fiori Launchpad on SCP](https://icf-excise-duty-exciseduty-approuter.cfapps.sap.hana.ondemand.com/cp.portal/site#Shell-home) (login using your d-/i-user and GLOBAL password)
**Please be advised that we have activated security checks, therefore you first require the corresponding authorizations in the ICF org for access to the App. Please contact Michael Oemler, Michael Wachter or Daniel Zimmermann to get access.**

## Demo data
The following demo data is available in ER8:
* Customers
  * **FLORIAN**: Customer in Germany without own Tax Warehouse
  * **BACCHUS**: Customer in Germany with own Tax Warehouse
  * **TELL**: Customer in Switzerland 
  
* Materials
There are three kinds of beer with differing alcohol content: 
  * **EDBDI01**
  * **EDBDI02**
  * **EDBISM01**

## Demo flow (straightforward)
- Create Sales Order via the corresponding tile in S/4
  - Tile "Create Sales Order", Order Type OR, Sales Org M201, Distribution Channel M1, Division M1
  - Pick one of the above customers as sold-to party and don't forget the PO ref number
  - Pick one or several materials
  - Save and memorize the order number displayed at the bottom
  - Close the WebGUI screen
- Change Sales Order to Post Goods Issue in S/4
  - Tile "Change Sales Order", enter the number you memorized and click on Continue
  - Go to More-->Sales Document-->Deliver in the menu bar
  - Enter the ordered quantity into the picked quantity (to fulfill the full order)
  - Click on "Post Goods Issue". **Do not click on save instead!**
- Check the line item in the Excise Duty Line Items tile
  - _Optional_ Show Excel export, Show Tax Report PDF

# How to contribute

## Build the artifacts with MTA Build Tool

Prerequisites:
* [Maven](https://maven.apache.org/download.cgi) Installed and configured
    * [settings.xml](https://github.wdf.sap.corp/xs2/Programming-Model/blob/dev/StaticResources/settings.xml) with the right configured sources
* [Node latest](https://nodejs.org/) Installed and configured
    * ```.npmrc``` with the following configuration
    ```
    registry=http://nexus.wdf.sap.corp:8081/nexus/content/groups/build.releases.npm
    package-lock=false
    ``` 
    * ```node``` & ```npm``` is part of your $PATH envrionment variable
* Globally installed [Grunt](https://gruntjs.com/) tool
    ```
    npm install grunt-cli -g
    ```
* Download the newest version of the [MTA Build Tool](http://nexus.wdf.sap.corp:8081/nexus/content/groups/build.snapshots/com/sap/di/mta_archive_builder_core/4.100.0-SNAPSHOT/)
    * Place the file in your favourite location (it is recommended to add the location to the class path)
* Make yourself familar with the [MTA Build Tool Documentation](https://wiki.wdf.sap.corp/wiki/display/CXP/MTA+Build+Tool)
* Make sure that you have a Java Version 8 JDK installed

Execute the build command within the project's root folder.

```bash
java -jar mta_archive_builder_core.jar --build-target=CF  build
```

The result will be a ```excise_duty.mtar``` file which can be deployed to cloud foundry.


### Build simplified mta with less service dependencies
In case you do not need all features of excise duty and simply want to deploy the application for some basic checks, there is a reduced version of [mta.yaml](mta.yaml) for this. In order to use this, you have to rename [mta.simple.yaml](mta.simple.yaml) to ```mta.yaml``` and start the build process as described

### Build the CDS Model
Once you performed a change to the CDS entities, you have to execute the build in order to build and distribute the entites to the corresponding entities. You have to run the native npm process by executing ```npm install```. After the run all the generated artefacts are distributed, *.hdbcds enties are moved to the ```db``` module and the ```csn.json``` and the corresponding edmx xml files are moveed to the ```java``` module.

## Test Local

### Setup Tomcat for local test
Local testing is possible by starting the SAP Edition of a Tomcat 8 (as this version includes the HANA DB driver). Within the run configuration of the ```Java Web Tomcat 8 Server``` you can also define custom environment variables like ```VCAP_SERVICES```.

Once the server is started, you can reach the service using the url ```http://localhost:8080/exciseDuty-java/odata/v2/``` 

### Define local database connection
Create a file with the name ```connection.properties``` in folder ```java/src/main/resources``` with content

```
#DB Connection Properties		
schema=
username=
password=
connectionURL=
```
### Tunnel cloud foundry database connection 
Instead of setting up everything in your local environment it is also possible to tunnel certain service (like the HANA DB) from your cloud foundry space to your local system

Port and IP information can be extracted from the VCAP_SERVICES variable of your deployed application.

```bash
cf ssh -L 30015:10.253.65.0:30053 exciseDuty-java
```

Explaination of the command:
* **cf ssh** -> open a SSH connection to any cloud foundry application
* **30015** -> local port number where the service is exposed to
* **10.253.65.0** -> Cloud Foundry internal IP address of the to  be consumed service
* **30053** -> remote port of the to be consumed service
* **exciseDuty-java** -> name of the application where the service is bound to 

## Deploy the artifacts to cloud foundry
Deployment to cloud foundry is done using the mta deploy plugin ([download](https://github.com/SAP/cf-mta-plugin)). 

**Prerequisite**
* A cloud foundry space where you can deploy to
* The space needs at least the following entitlements (full list can be found in the [mta.yaml](mta.yaml))
    * HANA DB
    * RabbitMQ
    * dynatrace-service
    * ...

To start the deployment you have to execute the following command:
```bash
cf deploy exciseDuty.mtar
```

To start the java application in debug mode you have also to apply the mta extension descriptor:
```bash
cf deploy exciseDuty.mtar -e debugJava.mtaext.yaml
```

### Shortcut deployment
In order to speed up local development & testing it is possible to deploy single modules without the MTA deployment plugin and still not break the MTA deployed artifacts.

In order to do so, the java project has dedicated manifest.yml files for each space which can be used to only update the java application. 

Execute the following command inside the java project:
```bash
mvn clean install
cf push -f manifest-playground.yml 
```

## Remote Debugging
To enable remote debugging you have to deploy the application either using the mta toll utilizing the [debugJava.mtaext.yaml](debugJava.mtaext.yaml) mta extension descriptor or with the [shortcut deployment](#shortcut_deployment).

After the deployment the java application is running with remote debugging, now you have forward the remote debugging port to your local machine
```bash
cf ssh -L 8000:127.0.0.1:8000 exciseDuty-java
```

Now that you forwarded the port to your local machine, you can connect Eclipse to your ```Remote Java Application``` and start debugging.

# Links
* [Services](#services)
* [User Interfaces](#user-interfaces)
* [Others](#others)

## Services
* [oData Service](icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com)

### Stock Ledger Service
[Service URL](https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com/odata/v2/StockLedgerService)
* Action ExciseDutyCalculation
```bash
curl 'https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com/odata/v2/StockLedgerService/ExciseDutyCalculation' \
  -H 'Content-Type: application/xml' \
  -X POST \
  -d 'materialNumber=%27123%27&companyCode=%27asd%27&plant=%270010%27&storageLocation=%270100%27&customerNumber=%2700010%27&quantity=10&baseUnitOfMeasure=%27HL%27&postingDate=datetime%272017-08-21T00:00:00%27'
```
```bash
POST /odata/v2/StockLedgerService/ExciseDutyCalculation HTTP/1.1
Host: icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com
User-Agent: curl/7.54.0
Accept: */*
Content-Type: application/xml
Content-Length: 156
```
```xml
HTTP/1.1 200 
Cache-Control: no-store, no-cache
DataServiceVersion: 2.0
Content-Type: application/atom+xml; type=entry;charset=utf-8
Content-Length: 1300
Date: Fri, 22 Sep 2017 13:15:37 GMT
Server: SAP
 

<?xml version="1.0" encoding="utf-8"?>
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:m="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata" xmlns:d="http://schemas.microsoft.com/ado/2007/08/dataservices" xml:base="https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com/odata/v2/StockLedgerService/">
    <id>https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com/odata/v2/StockLedgerService/ExciseDutyCalculationResult</id>
    <title type="text">ExciseDutyCalculationResult</title>
    <updated>2017-09-22T15:15:37.758+02:00</updated>
    <author>
        <name></name>
    </author>
    <link href="ExciseDutyCalculationResult" rel="self" title="ExciseDutyCalculationResult"></link>
    <entry>
        <id>https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com/odata/v2/StockLedgerService/ExciseDutyCalculationResult('BI')</id>
        <title type="text">ExciseDutyCalculationResult</title>
        <updated>2017-09-22T15:15:37.758+02:00</updated>
        <category term="StockLedgerService.ExciseDutyCalculationResult" scheme="http://schemas.microsoft.com/ado/2007/08/dataservices/scheme"></category>
        <link href="ExciseDutyCalculationResult('BI')" rel="edit" title="ExciseDutyCalculationResult"></link>
        <content type="application/xml">
            <m:properties>
                <d:exciseDutyTypeId>BI</d:exciseDutyTypeId>
                <d:taxValueAmount>123</d:taxValueAmount>
                <d:taxValueCurrency>EUR</d:taxValueCurrency>
            </m:properties>
        </content>
    </entry>
</feed>
```

* Action TaxReporting
```bash
 curl 'https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com/odata/v2/StockLedgerService/TaxReporting?taxWarehouseRegistration=%27TS%20M201%27&fromDate=datetime%272017-10-01T00:00:00%27&toDate=datetime%272017-10-31T00:00:00%27' \
  -H 'Accept: application/json'
```

```json
{
    "d": {
        "results": [
            {
                "__metadata": {
                    "id": "https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com:443/odata/v2/StockLedgerService/TaxReportingResult(exciseDutyTypeId='BI',alcoholicStrength=7.5M)",
                    "uri": "https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com:443/odata/v2/StockLedgerService/TaxReportingResult(exciseDutyTypeId='BI',alcoholicStrength=7.5M)",
                    "type": "StockLedgerService.TaxReportingResult"
                },
                "exciseDutyTypeId": "BI",
                "alcoholicStrength": "7.5",
                "exciseDutyProcurementIndicator": "F",
                "taxRelevantAmount": "111.11",
                "notTaxRelevantAmount": "222.222",
                "thirdCoundryNotTaxRelevantAmount": "3.3333",
                "taxValueCurrency": "EUR"
            },
            {
                "__metadata": {
                    "id": "https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com:443/odata/v2/StockLedgerService/TaxReportingResult(exciseDutyTypeId='BI',alcoholicStrength=14.0M)",
                    "uri": "https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com:443/odata/v2/StockLedgerService/TaxReportingResult(exciseDutyTypeId='BI',alcoholicStrength=14.0M)",
                    "type": "StockLedgerService.TaxReportingResult"
                },
                "exciseDutyTypeId": "BI",
                "alcoholicStrength": "14.0",
                "exciseDutyProcurementIndicator": "E",
                "taxRelevantAmount": "444.44",
                "notTaxRelevantAmount": "555.555",
                "thirdCoundryNotTaxRelevantAmount": "6.6666",
                "taxValueCurrency": "EUR"
            }
        ]
    }
}
```

## User Interface
Central entry point via the [Fiori Launchpad](https://icf-excise-duty-exciseduty-approuter.cfapps.sap.hana.ondemand.com/cp.portal/site#Shell-home)


## Others
* [Zenhub Planning Board](https://zenhub.mo.sap.corp/app/workspace/o/icdcloudarchitcture/excise_duty/boards?repos=111791)
* System Components
    * [Cloud Connector](https://dlmscc.wdf.sap.corp:8443)
    * [Dynatrace Instance](https://oxp944.dynatrace-managed.com)

# Learnings
* [Service Broker for client credentials flow](#service-broker-for-client-credentials-flow)
* [Dynatrace](#dynatrace)
* [Cloud Service SDK - New Features](#cloud-service-sdk---new-features)

## Service Broker for client credentials flow
The relevant coding can be found in folder [serviceBroker](serviceBroker) and the [mtad.yaml](mtad.yaml). Documentation on the service broker framework can be found in the [@sap/sbf](https://github.wdf.sap.corp/xs2/node-sbf) Github.

In the ```mtad.yaml``` you have to define the the service broker module with all necessary parameters. The variable ```~{url}``` is defined by the java module and reused here:
```yaml
- name: exciseDuty-sb
type: javascript.nodejs
path: serviceBroker/
properties:
    SBF_BROKER_CREDENTIALS: >
    {
        "${generated-user}": "${generated-password}"
    }
requires:
    - name: java
    properties:
        SBF_SERVICE_CONFIG:
            ExciseDuty-ServiceBroker:
            extend_credentials:
                shared:
                url: ~{url}
    - name: ed-sb-uaa
parameters:
    memory: 128M
```

 Additionally it is necessary to create a new ```UAA``` resource specific with the service broker. This ```UAA``` has to be required by the service broker module as well as by the corresponding java module. In the ```UAA``` you have to ensure that it has the service-plan "broker" assigned. It should look like:
```yaml
- name: ed-sb-uaa
    type: org.cloudfoundry.managed-service
    parameters:
      service: xsuaa
      service-plan: broker
      config:
        xsappname: ed-sb-${space}
        tenant-mode: dedicated
        scopes:
         - name: $XSAPPNAME.Show
           description: display
         - name: $XSAPPNAME.Add
           description: create
         - name: $XSAPPNAME.Remove
           description: delete
```

Once you have deployed the service broker you have to register it as service in your local space and create a service instance of it:
```bash
cf create-service-broker exciseDuty-sb {generated-user} {generated-password} {service-broker-url} --space-scoped
cf create-service ExciseDuty-ServiceBroker default ed-sb
```

Now that we have created the service broker in our environment, we can create our credentials for it:
```bash
$ cf create-service-key ed-sb credentialsForPostman
$ cf service-key ed-sb credentialsForPostman
Getting key credentialsForPostman for service instance ed-sb as D030625...
{
 "uaa": {
  "clientid": "myclientID",
  "clientsecret": "myClientSecret=",
  "url": "https://sap-icf.authentication.sap.hana.ondemand.com",
  ...
 },
 "url": "https://icf-excise-duty-exciseduty-java.cfapps.sap.hana.ondemand.com"
}
```

The last statement prints the service key information and we can use this to get our JWT token. In order to do so, we have to use the ```UAA``` url as well as the clientid and clientsecret. The token resource is reachable under ```/oauth/token``` and the clientid and clientsecret have to be provided as basic authentication with Base64 encoded.
```bash
$ curl 'https://sap-icf.authentication.sap.hana.ondemand.com/oauth/token' -i -u 'myclientID:myClientSecret=' -X POST \
    -H 'Content-Type: application/x-www-form-urlencoded' \
    -H 'Accept: application/json' \
    -d 'grant_type=client_credentials&response_type=token'

HTTP/1.1 200 OK
Cache-Control: no-store
Content-Type: application/json;charset=UTF-8
Date: Wed, 30 Aug 2017 09:35:36 GMT
Pragma: no-cache
Server: Apache-Coyote/1.1
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Vcap-Request-Id: 2781922f-40be-40b3-64ad-2bc2ffe8a897
X-Xss-Protection: 1; mode=block
Content-Length: 1571
{"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJmNTk2YzMxY2ExOWI0ODNiOTYwZGU2OWUzNzYyMDE3MCIsImV4dF9hdHRyIjp7ImVuaGFuY2VyIjoiWFNVQUEiLCJzZXJ2aWNlaW5zdGFuY2VpZCI6ImQxZDMxNWRjLTkwNTAtNDkxYi05NThkLWE0ZWE1MzgzNjM2YSJ9LCJzdWIiOiJzYi1kMWQzMTVkYy05MDUwLTQ5MWItOTU4ZC1hNGVhNTM4MzYzNmEhYjM1fGVkLXNiLWV4Y2lzZV9kdXR5IWIzNSIsImF1dGhvcml0aWVzIjpbInVhYS5yZXNvdXJjZSJdLCJzY29wZSI6WyJ1YWEucmVzb3VyY2UiXSwiY2xpZW50X2lkIjoic2ItZDFkMzE1ZGMtOTA1MC00OTFiLTk1OGQtYTRlYTUzODM2MzZhIWIzNXxlZC1zYi1leGNpc2VfZHV0eSFiMzUiLCJjaWQiOiJzYi1kMWQzMTVkYy05MDUwLTQ5MWItOTU4ZC1hNGVhNTM4MzYzNmEhYjM1fGVkLXNiLWV4Y2lzZV9kdXR5IWIzNSIsImF6cCI6InNiLWQxZDMxNWRjLTkwNTAtNDkxYi05NThkLWE0ZWE1MzgzNjM2YSFiMzV8ZWQtc2ItZXhjaXNlX2R1dHkhYjM1IiwiZ3JhbnRfdHlwZSI6ImNsaWVudF9jcmVkZW50aWFscyIsInJldl9zaWciOiIxNjcyMWYwNSIsImlhdCI6MTUwNDA4NTczNywiZXhwIjoxNTA0MTI4OTM3LCJpc3MiOiJodHRwOi8vc2FwLWljZi5sb2NhbGhvc3Q6ODA4MC91YWEvb2F1dGgvdG9rZW4iLCJ6aWQiOiJzYXAtaWNmIiwiYXVkIjpbInVhYSIsInNiLWQxZDMxNWRjLTkwNTAtNDkxYi05NThkLWE0ZWE1MzgzNjM2YSFiMzV8ZWQtc2ItZXhjaXNlX2R1dHkhYjM1Il19.xKlCX162yhVW6S4m49Is_lzrDWWRRTpccUdAa6ohetX7QSIcdJKv7GzgBygSm-RSdeqZh4zF_C8HC4F7KkNnQPvFdNf3f7n6xeBr3MtB-ascImV4wAwLqTlFZVFfAQqvi67LxZLWfOk2xUHJ7uR7IzvodV6qYxL3stoe6FbRdcGz6C0oXoevsHUxFQrfUPu3wHlOhnAA6y4-vTrfy-U-M27C106kGK6G8SJfEzrab68A_bD8YJNRcY5TC_e2bGG1lLEo4wWDL0LsOwgMA0yVpZtW7nSHClXTbD7o9CtG99cxS96-U6yGMVRTU1G37SQ1UiQp41RI8bzn5NQDTKaO5A","token_type":"bearer","expires_in":43199,"scope":"uaa.resource","ext_attr":{"enhancer":"XSUAA","serviceinstanceid":"d1d315dc-9050-491b-958d-a4ea5383636a"},"jti":"f596c31ca19b483b960de69e37620170"}
```

## Dynatrace
A detailed documentation for cloud foundry setup can be found [here](https://help.dynatrace.com/infrastructure-monitoring/paas/how-do-i-monitor-cloud-foundry-applications/).

In order to allow your applications to report their metrics to Dynatrace you have to provide a service instance on the cloud foundry which can be assigned to your application. This can be either done by deploying the [Dynatrace Service Broker](https://github.com/dynatrace-innovationlab/dynatrace-service-broker) or with a user provided service. We decided to go for the last one, as it keeps things simple for now. You have simply to perform the following command in your cloud foundry space:
```bash
cf cups dynatrace-service -p "environmentid, apitoken, apiurl"
```

Now the system  asks you for the specific values of the parameters. afterwards you are ready to go.
* ```environmentid``` is part of your dynatrace instance and can be extracted from the url ```https://<host>.dynatrace-managed.com/e/<environmentID>````
* ```apitoken``` can be create in the web frontend via ```https://<host>.dynatrace-managed.com/e/<environmentID>/#install/paas```
* ```apiurl``` looks like ```https://<host>.dynatrace-managed.com/e/<environmentID>/api```

During MTA deployment you need to bind your application to this service instance. Therefore you have to define the service as resource in your mta.yml and let your other modules require it. The resource definition could look like (you can find a [complete mtad.yaml](mtad.yml) also in this repository):
```yaml
resources:
  - name: dynatrace-service
    type: org.cloudfoundry.managed-service
    parameters:
      service:  user-provided 
```
### For Java Applications
The SAP Java Buildpack aswell as the Java Buildpack have both an out of the box integration with the dynatrace oneagend. As soon as you bind your application to a dynatrace service instance the agend will be automatically started together with your application.

### For nodeJS Applications
First you have to add the "@dynatrace/oneagent" as dependency to your ```package.json``` and ensure that the dynatrace oneagend is started together with your application. The dynatrace should only be started on cloud foundry when the application is bound to a dynatrace service instance.

In the following code snipped you can see how an ```server.js``` could look like:
```js
try {
    // Bootstrap Dynatrace
    require('@dynatrace/oneagent')();
} catch (err) {
    console.info('Dynatrace disabled: ' + err);
}

// Start approuter
var approuter = require('@sap/approuter');

var ar = approuter();
ar.start();
```
In case the dynatrace agend does not find the necessary environment information it will break with an exception which is catched by us. With this we allow the application to run in a dynatrace enabled environment as well as in a local / non enabled cloud environment.

## RabbitMQ
For event processing we are using rabbitMQ. In order to do local testing there are two possiblities. Either you install docker or using the SSH tunnel into cloud foundry. There are two ports which are of interesst:
* ```5672```: This is the general conncetion port for RabbitMQ
* ```15672```: Access to the management console, which can be accessed via the browser

### Docker
If you want to use docker you have to run the following command. Afterwards you can access for example the management console using http://localhost:15672 (user/pass guest/guest).
```shell
docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```

### CF SSH Tunnel
For the cloud foundry cf tunnel you first have to check on which IP and port your rabbitMQ instance is listening. Therefore you have first of all have to check the VCAP_SERVICES of your application.
```shell
cf env exciseDuty-java
Getting env variables for app exciseDuty-java in org ICF_poc / space excise_duty as D030625...
OK

System-Provided:
{
 "VCAP_SERVICES": {
  ...
  "rabbitmq": [
   {
    "credentials": {
     "hostname": "10.11.241.37",
     "password": "yyy",
     "port": "47566",
     "ports": {
      "15672/tcp": "59000",
      "5672/tcp": "47566"
     },
     "uri": "amqp://xxx:yyy@10.11.241.37:47566",
     "username": "xxx"
    },
    "label": "rabbitmq",
    "name": "ed-rabbit",
    "plan": "v3.6-dev",
    "provider": null,
    "syslog_drain_url": null,
    "tags": [
     "rabbitmq",
     "mbus",
     "pubsub",
     "amqp"
    ],
    "volume_mounts": []
   }
  ],
  ...
 }
```

Here you can now get the information on which IP rabbitmq is running and which PORT mapping you have to do. In this case the port 47566 is mapped to 5972 and 5900 to 15672. Now that we have the information we can open the SSH port forwarding tunnel using:
```shell
cf ssh -L 15672:10.11.241.37:59000 -L 5672:10.11.241.37:47566 exciseDuty-java
```
In your local environment you now have the default ports of rabbitMQ available.

## Logging
https://github.wdf.sap.corp/foundation-apps/ConsentManagementDocumentation/blob/master/Architecture/Development/Logging.md

Change LogLevel on your own application, otherwise you will not see any logs.

## Analytics

For the INA exposure we have to add a new MTA Module called ```XSAHAA``` which can be downloaded from the [Nexus](https://nexusmil.wdf.sap.corp:8443/nexus/service/local/repositories/deploy.releases/content/com/sap/xsahaa/xsahaa-release/1.0.1/xsahaa-release-1.0.1-release.zip).
* Place the contained ```.war``` file to a new folder called ```xsahaa```
* Adapt your [mtad.yaml](mtad.yaml) as well as the [assemply.xml](assembly/assembly.xml) and the [pom.xml](assembly/pom.xml) within the assemply folder
* Whitelist the destination within the [approuter config](approutery/xs-app.json) of the approuter
* Define your calculation view, for example in the WebIDE, and save it in the ```db``` folder

To see the necessary changes you can search within the files for ```xsahaa``` or simply look at the [corresponding commit](https://github.wdf.sap.corp/ICDCloudArchitcture/excise_duty/commit/29d156112d10913c9236213e16450f2db542b5f5).

Now that you configured your application for the INA access your can check if it works by accessing the approuter URL:  https://icf-excise-duty-playground-exciseduty-approuter.cfapps.sap.hana.ondemand.com/sap/bc/ina/service/v2/GetServerInfo

Additional information can be found in the Apollo Analytics Quality Rollout: [Presentation](https://documents.wdf.sap.corp/share/proxy/alfresco/api/node/content/workspace/SpacesStore/93ffebbb-5f85-4ee9-98a3-54d836c92c74/LiveConnectivity.pptx) / [Recording](https://documents.wdf.sap.corp/share/proxy/alfresco/api/node/content/workspace/SpacesStore/6e4c2906-25a6-41ee-a4c4-eaa2888935a7/LiveConnectivityMultiTenantSupport.mp4)

### Workaround to get the right authorizations
At the end, we need to set the right authorizations to the analytics user which reads the data. Therefore we have to check which is the user deployed with the ```xsahaa``` application. Once you found the right ```SBSS_*``` user you can connect to your hana with the ```SYSTEM``` user and grand to the user the authorizations as mentioned in the following [wiki page](https://github.wdf.sap.corp/AnalyticsBackendTeam/HanaAnalyticsAdapter/wiki/Workaround-Missing-Privileges-(Obsolete)) in the section "USER ->Object Privileges".

You also have to grant the to the user the Role: "INA_USER".

## Fiori
### UI Adaptation & Level 0 Changes
In order to enable the UI Adaptation Editor in the WebIDE, right click the project root and go to "Project Settings". In the Project Types facet, select the "UI Adaptation" flag. To enter adaptation mode, right click on the project root and select "UI Adaptation Editor". Note: The Adaptation editor requires a live data connection (via destination), it does not work against the mock server.
### Evo Build
We have included the UI5 Evo Build. There currently is a [patched version available that also incorporates the Level0 Changes into the build here](https://github.wdf.sap.corp/D065687/Pipeline-Test-MTA-CDS/blob/master/web/Gruntfile.js). 
As the Evo Build grunt task overwrites configuration, we are calling the individual npm install scripts on the single UI modules rather than triggering the build during file aggregation.

## Authenticated Service call using Muenchhausen
The [Muenchhausen service](https://xsuaa-monitoring-idp.cfapps.sap.hana.ondemand.com/) offer the possibility to easyly generate an access token for authenticated API calls. To retrieve a token you have to use the ```gettoken``` endpoint providing the necessary information.
The generated access token has to be added to a authenticated service call as ```Authorization``` header with the value ```Bearer <<accessToken>>```

_Tip: You can use the ```Postman - Pre-request Scrip``` feature to ensure that a token is automatically generated before you perform a request to the service itself._

## Adoption of the Fiori Runtime.

The changes for the Fiori runtime have been adopted. See the materials at https://github.wdf.sap.corp/fiori-cf/support for reference.

## Cloud Service SDK - New Features
### Version 1.8
#### Execute Queries
You can build up the query using the CDSHandler. You get the handling by type casting the DataSourceHandler to a CDSHandler class.
```java
@ExtendAction(Name = "FetchWorklist")
public OperationResponse execute(OperationRequest functionRequest, ExtensionHelper extensionHelper) {
    CDSHandler handler = (CDSHanlder) extensionHelper.getHandler();
    
    CDSQuery cdsQuery = new CDSSelectQueryBuilder("ExciseDutyModel.StockLedgerLineItem")
                .top(10)
                .selectColumns("exciseDutyTypeId", "taxWarehouseRegistration", "stockLedgerNumber")
                .build();

    List<EntityData> data = handler.executeQuery(cdsQuery).getResult();
}
```
#### Get Database Connection
Typecast the DataSourceHandler to CDSHanlder class and you can access the Database connection using the method ```getConnection()```.
```java
@ExtendAction(Name = "FetchWorklist")
public OperationResponse execute(OperationRequest functionRequest, ExtensionHelper extensionHelper) {
    CDSHandler handler = (CDSHanlder) extensionHelper.getHandler();
    Conncetion con = handler.getConnection();
}
```

### Version 1.17.0
#### Intrgration of Remote oData Services
We added a live lookup of Customer and Matieral from a connected S4 System in order to offer the User a value help when he wants to maintain new Material or ShipToParty Extensions.

##### Setup in S/4 System
Open Transaction ```/iwfnd/maint_service``` to activate the necessary oData Services. In the "Excise Duty" case we need [API_PRODUCT_SRV](https://api.sap.com/shell/discover/contentpackage/SAPS4HANACloud/api/API_PRODUCT_SRV) and [API_BUSINESS_PARTNER](https://api.sap.com/shell/discover/contentpackage/SAPS4HANACloud/api/API_BUSINESS_PARTNER)

How to activate the Services:
* Select "Add Service"
* Select "System Alias" as "LOCAL"
* Click on "Get Services"
* Search for the need service, select it and click on "Add Selected Service"
* Save it to $TMP

In case the object cannot be created because it has been by accident created within a transportable package, you have to raise a ticket to componenet ```DEV-S4HANA-GEN``` following these [sample ticket](https://support.wdf.sap.corp/sap/support/message/1870204793).

##### Adding it to the SCP
* Using the ```$metadata``` file you can convert out of it a ```csn.json``` using the [edmx2csn compiler](https://github.wdf.sap.corp/cap/mashups/tree/master/edmx2csn) provided by Shiva Prasad Nayak.
* Place the ```csn.json``` within your ```srv```folder like the [API_PRODUCT_SRV.json](srv/cds/remote/API_PRODUCT_SRV.json)
* Now you can embed it like shown in the [MaterialMasterService.cds](srv/cds/MasterDataExtension/MaterialMasterService.cds)
* The corresponding implementation can be found in [S4ApiProduct.java](srv/src/main/java/com/sap/exciseduty/odata/remote/S4ApiProduct.java)

##### Current Limitations
* Only Basic filter definition is working
* Wildcard Search is not working on the Remote Service
* CustomSearch expression is not working on the Remote Service
* Value Help for Product does not return a description yet

##### Documentation for Query Manipulation
 

```java
FilterExpression existingFilter = requestWithFilter.getQueryExpression();
FilterExpression additionalFilter = new FilterExpression(“CustomerGroup”, “eq”, ODataType.of(“Privileged”));
FilterExpression updatedFilter = existingFilter.and(additionalFilter);
```

or as described in the [wiki page](https://wiki.wdf.sap.corp/wiki/display/CoCo/Filter+APIs)

```java
ExpressionHelper requestWithFilter = (ExpressionHelper) req;
Expression filterExpression = requestWithFilter.getQueryExpression();
```

## Operations

### Availability Checks
We monitor the availability of our java service using the [SAP CP Availability Service](https://availability.cfapps.sap.hana.ondemand.com) using a so called "Basic Evaluation". The service is regularly calling the ```/health``` endpoint of our java application which is running in our cloud foundry demo space (which is treated as like production). Our Evaluation Check is called [CF Canary Excise Tax Service - Java](https://availability.cfapps.sap.hana.ondemand.com/login#/evaluation/827429/)

Once the application is not reachable or returns an error HTTP code the tool triggers notifications e.g. via mail or statuspage.io. Our current internal setup is, an email is send to our SLACK channel to alert all interested parties. 

Additionally, an overall availability und response time report is send once a week. 


# Reported Tickets

Moved to [github wiki](https://github.wdf.sap.corp/ICDCloudArchitcture/excise_duty/wiki/Issues-found-during-PoC).

# Open Points

## Bugs in Cloud Service SDK

## Technical Questions
* How to create a service broker during MTA deployment to reduce manuell steps as described in the section [Service Broker for client credentials flow](#service-broker-for-client-credentials-flow).
