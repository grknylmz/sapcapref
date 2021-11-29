cds init 
cds run
cds --version
cds help
cds compile srv/say.cds --to json
cds compile srv/say.cds --to sql
cds compile srv/say.cds --to edmx
cds compile srv/cat-service.cds --to hana
cds watch
cds deploy --to sqlite
cds deploy --to sqlite:bookshop.db
cds deploy --to hana
CDS_ENV=production cds watch

cf login
cds add hana
cds add hana -- force 
cds build/all
cf create-service hanatrial  hdi-shared bookshop-db
cf push -f gen/db/
cf push -f gen/srv/ --random-route

cf target

cds add mta 
mbt build
cf push mta_archives/????.mtar


git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/karacasenol/CAP.git
git push -u origin main


git remote add origin https://github.com/karacasenol/CAP.git
git branch -M main
git push -u origin main

