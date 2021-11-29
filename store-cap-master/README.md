# Getting Started

Welcome to your new project.

It contains these folders and files, following our recommended project layout:

File / Folder | Purpose
---------|----------
`app/` | content for UI frontends go here
`db/` | your domain models and data go here
`srv/` | your service models and code go here
`package.json` | project metadata and configuration
`readme.md` | this getting started guide


## Next Steps...

- Open a new terminal and run  `cds watch`
- ( in VSCode simply choose _**Terminal** > Run Task > cds watch_ )
- Start adding content, e.g. a [db/schema.cds](db/schema.cds), ...


## Learn more...

Learn more at https://cap.cloud.sap/docs/get-started/

## Important Commands...
Deploy data model to the database instance:
- $ cds deploy --to sqlite:db/my-company.db

Run application and watch for any changes:
- $ cds watch

## Tips and tricks...
Node version: 8.11.3 (Use n or nvm to manage node versions)
- cds init store-cap (To create the project)
- npm i --save sqlite3 (Use on the project folder to install sqlite3 for local/persistent tests)
- npm i (Use on the project folder to install the dependencies)
- cds watch (To run the project locally)
- cds deploy --to sqlite:db/my-company.db (Deploy to sqlite for a persistent database)
- sqlite3 db/my-company.db -cmd .dump (Show executed commands)
- sqlite3 db/my-company.db (Connect to the local environment database)

## Cloud deployment...
For Deploy on a SCP environment:
- npm add hdb (Add the SAP HANA driver as a dependency to the project)

Remember to add the following settings to the package.json file:
```
"cds":{
  "requires": {
      "db": {
      "kind": "hana",
      "model": ["db","srv"]
      }
  }
}
```

- Note: If your .cdsrc.json file contains a "target" entry, remove it or set it to: "target": "gen". This causes deployment files to be written to this folder. Otherwise, the deployment files would be written to the source folders.

To create a service on the SCP trial:
- cf create-service hanatrial hdi-shared store-cap-db-hdi-container

To check the status of the created service:
- cf service store-cap-db-hdi-container

- cds build/all (Build application)
- cf push -f gen/db (Push the database models to the SCP)
- cf push -f gen/srv --random-route (Push services to the SCP)
