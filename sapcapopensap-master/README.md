# sapcapopensap
SAP Cloud Application Programming Model

https://open.sap.com/courses/cp7 

>npm set @sap:registry=https://npm.sap.com

>npm i -g @sap/cds

>npm i -g @sap/cds-dk

https://account.hanatrial.ondemand.com/cockpit/ 


cds help compile


cds compile say.cds --to json


cds compile say.cds --to sql  

// do we also have from ??????????? sql



cds compile say.cds --to edmx



# Build the bookshop

use the studio : https://31a167b5trial.eu10cf.trial.applicationstudio.cloud.sap/index.html#ws-bdrgd


# Getting Started

Welcome to your new project.

It contains these folders and files, following our recommended project layout:

File or Folder | Purpose
---------|----------
`app/` | content for UI frontends goes here
`db/` | your domain models and data go here
`srv/` | your service models and code go here
`package.json` | project metadata and configuration
`readme.md` | this getting started guide


## Next Steps

- Open a new terminal and run `cds watch` 
- (in VS Code simply choose _**Terminal** > Run Task > cds watch_)
- Start adding content, for example, a [db/schema.cds](db/schema.cds).


## Learn More

Learn more at https://cap.cloud.sap/docs/get-started/.


cds init bookshop

cds watch










## some queries

http://localhost:4004/browse/Books?$top=2&$skip=1
http://localhost:4004/browse/Books(207)









# Interesting stuff

https://cap.cloud.sap/docs/node.js/authentication#mocked


# Questions - Open Issues

## How to handle DB life cycle, versioning

db life cycle tools:  flyway, liquibase, ...?  https://flywaydb.org/getstarted/why

## Best way to come close to close to DB statement execution

Connectin Query ??     this.connection.query(sql);      ??    typORM


  // typORM ???  
await this.queryBuilderService                           
                    .getDeleteQueryBuilder(tableName)
                    .where(`"FS_PER_GUID_" IN (:guids)`, { guids: toDelete.map((b) => b.guid) })
                    .execute();


