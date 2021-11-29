# SAP CAP UI deployment sample - Fiori Launchpad on Clound Foundry

This project created for better understanding simple UI deployment requirements for *very basic* SAP CAP project.  


### Steps followed
 - ```books``` model created with three properties (`db/model.cds`)
 - CSV files added, localized text maintained for en,de,fr (`cap-flp-sample.Books_texts.csv`)
 - CatalogService created and exposed Books entity only (`srv/service.cds`)
 - SAP Fiori Tools used for app generation List Report oData v4 (FE Elements - LROP)
 - A few line annotation added for the List view ```UI.LineItem``` (`app/browse/books/annotation.cds`)
 - cf_mta folder created for approuter, appdeployer and portalsite
 - mta.yaml configuration arranged accordingly for FE-SAMPLES repo.
 - mbt build && cf deploy to cloud foundry trial account


It contains these folders and files, following our recommended project layout:

File / Folder | Purpose
---------|----------
`app/` | content for UI frontends go here
`db/` | your domain models and data go here
`srv/` | your service models and code go here
`cf_mta` | approuter, appdeployer and portalsite directories go here
`package.json` | project metadata and configuration
`readme.md` | this getting started guide


## Learn more...

 - [SAP CAP Public API](https://cap.cloud.sap/docs/)

 - [Developing Multitenant Applications in the Cloud Foundry Environment](https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/5e8a2b74e4f2442b8257c850ed912f48.html)

- [Developing HTML5 Applications in the Cloud Foundry Environment](https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/11d77aa154f64c2e83cc9652a78bb985.html)




