⚠️ UNDER CONSTRUCTION ⚠️

# Getting Started

## Setup

With the first steps below, you can go for a minimal local setup as follows:

  - Install [Node.js](https://nodejs.org/en/) → always use the latest LTS version.
  - Install [SQLite](https://www.npmjs.com/package/sqlite3) (only required on Windows).
  - Install @sap/cds-dk globally with:

```bash
npm i -g @sap/cds-dk
cds # test it
```

> Complementary : Install [nvm](https://github.com/nvm-sh/nvm) to be able to switch Node version 

## Next Steps
- Open a new terminal and run `npm i` to install all required dependencies
- Then run `npm run deploy:sql` to deploy model on local Sqlite database
- Finally run `cds watch` 
- (in VS Code simply choose _**Terminal** > Run Task > cds watch_)
- Start adding content, for example, a [db/schema.cds](db/schema.cds).

## App preview

![image](https://user-images.githubusercontent.com/47522598/138414246-74c2faf5-876d-40cf-80fe-4dad90118ac7.png)

Tiles :

- My wine cellar (under further improvements)
  - See list of wines in wine cellar
- List of grape varieties
  - Create/Read/Update/Delete grape varieties
  - Add superficies data over the year
  - Overview list of wines that includes the grape varieties

![image](https://user-images.githubusercontent.com/47522598/138414958-e86bf309-3877-4896-ab7d-1a801db94558.png)

- List of wines
  - Create/Read/Update/Delete wines
  - Add wines to the wine cellar
  - Add grape varieties on the wine

![image](https://user-images.githubusercontent.com/47522598/138415006-ac69fd32-9879-49db-928a-47620aa17851.png)

- Overview
  - Insight of wines list, wines per aging time, ...

![image](https://user-images.githubusercontent.com/47522598/138415058-fa5fab24-0838-474a-ada5-27970cec5b2f.png)

## Project structure


Welcome to your new project.

It contains these folders and files, following our recommended project layout:

File or Folder | Purpose
---------|----------
`app/` | content for UI frontends goes here
`db/` | your domain models and data go here
`srv/` | your service models and code go here
`package.json` | project metadata and configuration
`readme.md` | this getting started guide

## Learn More

Learn more at https://cap.cloud.sap/docs/get-started/.
