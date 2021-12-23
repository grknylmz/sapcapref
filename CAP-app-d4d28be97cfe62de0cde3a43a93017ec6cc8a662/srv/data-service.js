const cds = require('@sap/cds');
const xssec = require('@sap/xssec');
const { ThesisText, Students2Theses, Teachers2Theses, Theses, Courses, Courses2Theses } = cds.entities ('app.data')

class DataService extends cds.ApplicationService { init() {

    this.reject(['DELETE'], ['Theses', 'Courses', 'Faculties', 'Prerequisites', 'Literatures', 'Tags', 'Students', 'Teachers', 'Admins', 'ThesisText']);
    this.reject(['UPDATE'], ['Students2Theses', 'Teachers2Theses', 'Prerequisites2Theses', 'Literatures2Theses', 'Tags2Theses', 'Courses2Theses'])
    
    this.before (['UPDATE', 'CREATE'], 'ThesisText', async req => {
        var neptun = req.user.id.substr(0, req.user.id.indexOf('@'));
        var rank = req.user.attr.Rank[0];
        var course = req.user.attr.Course[0];

        if(rank != "Student") {
            req.reject(403);
        }

        var thesisID = undefined;
        if(req.query.INSERT != undefined && 
            req.query.INSERT.entries != undefined && 
            req.query.INSERT.entries[0] != undefined) thesisID = req.query.INSERT.entries[0].thesis_ID;
        if(req.query.UPDATE != undefined &&
            req.query.UPDATE.entity != undefined &&
            req.query.UPDATE.entity.ref != undefined &&
            req.query.UPDATE.entity.ref[0] != undefined &&
            req.query.UPDATE.entity.ref[0].where != undefined &&
            req.query.UPDATE.entity.ref[0].where[2] != undefined) thesisID = req.query.UPDATE.entity.ref[0].where[2].val;

        var condition = {student_neptun : neptun};
        if(thesisID != undefined) Object.assign(condition, {thesis_ID : thesisID});

        const deadline = await SELECT.from(Courses).where({ID : course}).columns("deadline");
        const enabled = await SELECT.from(Students2Theses).where(condition).columns("thesis_ID");
        if(new Date(deadline[0].deadline).getTime() < new Date().getTime()) {
            req.reject(467,`A szakon lejárt a beadási határidő.`);
        }
        if(thesisID != undefined) {
            const archived = await SELECT.from(Theses).where({ID : thesisID}).columns("archived");
            if(archived != undefined && archived[0] != undefined && archived[0].archived === true && req.data.archived === undefined) {
                req.reject(468,`A témaszöveg nem szerkeszthető, mert a téma archivált.`);
            }
            else if(enabled === undefined || enabled[0] === undefined || enabled[0].thesis_ID === undefined) {
                req.reject(466,`Nincs hozzárendelve ${neptun} a témához.`);
            }
            else {
                await UPDATE(Students2Theses, {student_neptun : neptun, thesis_ID : thesisID}) .with ({mod_count: {'+=': 1}});
            }
        }
    });

    this.on (['READ'], 'ThesisText', async req => {
        var neptun = req.user.id.substr(0, req.user.id.indexOf('@'));
        var rank = req.user.attr.Rank[0];
        var thesisID = req.data.thesis_ID;
        if(req.query.SELECT.where != undefined &&
            req.query.SELECT.where[3] != undefined) thesisID = req.query.SELECT.where[3].val;
        if(req.query.SELECT != undefined &&
            req.query.SELECT.from != undefined &&
            req.query.SELECT.from.ref != undefined &&
            req.query.SELECT.from.ref[0] != undefined &&
            req.query.SELECT.from.ref[0].where != undefined &&
            req.query.SELECT.from.ref[0].where[2] != undefined) thesisID = req.query.SELECT.from.ref[0].where[2].val;
        var condition = rank === "Student" ? {student_neptun : neptun} : {teacher_neptun : neptun};
        var table = rank === "Student" ? Students2Theses : Teachers2Theses;
        if(thesisID != undefined) Object.assign(condition, {thesis_ID : thesisID});
        const result = await SELECT.from(ThesisText).where({thesis_ID : SELECT('thesis_ID').from(table).where(condition)});
        //if(result.length === 0) result.push({message:"Nincs jogosultsága megtekinteni"});
        return result;
    });

    this.before (['CREATE', 'DELETE'], ['Students2Theses', 'Teachers2Theses', 'Tags2Theses', 'Literatures2Theses', 'Courses2Theses', 'Prerequisites2Theses'], async req => {
        var neptun = req.user.id.substr(0, req.user.id.indexOf('@'));
        var rank = req.user.attr.Rank[0];
        var queryStudent = null;
        var thesisID = undefined;
        if(req.query.INSERT != undefined &&
            req.query.INSERT.entries != undefined &&
            req.query.INSERT.entries[0] != undefined) {
                thesisID = req.query.INSERT.entries[0].thesis_ID;
                if(req.entity === 'DataService.Students2Theses' && rank === "Student") queryStudent = req.query.INSERT.entries[0].student_neptun;
            }
        if(req.query.DELETE != undefined &&
            req.query.DELETE.from != undefined &&
            req.query.DELETE.from.ref != undefined &&
            req.query.DELETE.from.ref[0] != undefined &&
            req.query.DELETE.from.ref[0].where != undefined) {
                if(req.entity === 'DataService.Students2Theses' && rank === "Student" && req.query.DELETE.from.ref[0].where[2] != undefined) queryStudent = req.query.DELETE.from.ref[0].where[2].val;
                if(req.query.DELETE.from.ref[0].where[6] != undefined) thesisID = req.query.DELETE.from.ref[0].where[6].val;
            }
        if(thesisID != undefined) {
            var archived = await SELECT.from(Theses).where({ID : thesisID}).columns("archived");
            if(archived != undefined && archived[0] != undefined && archived[0].archived === true) {
                req.reject(468,`A téma nem szerkeszthető, mert a téma archiválva lett.`);
            }
        }
        if(req.entity === 'DataService.Students2Theses' && rank === "Student") {
            if(queryStudent != neptun) {
                req.reject(469,`Küldő ${neptun} és a kérésben lévő ${queryStudent} nem egyezik.`);
            } else if(thesisID != undefined && req.event === 'CREATE') {
                var course = req.user.attr.Course[0];
                var check = await SELECT.from(Theses).where({ID : thesisID}).columns("open", "max_students", "only_preferred", "todate");
                var count = await cds.run(`SELECT COUNT(*) AS count FROM DATASERVICE_Students2Theses WHERE thesis_ID = '${thesisID}';`);

                if(check != undefined && check[0] != undefined && count != undefined && count[0] != undefined) {
                    if(check[0].open === false) {
                        req.reject(470,`A témára nincs engedélyezve feljelentkezés.`);

                    } else if(new Date(check[0].todate).getTime() < new Date().getTime()) {
                        req.reject(471,`A jelentkezési idő letelt.`);

                    } else if(check[0].only_preferred === true) {
                        var courses = await SELECT.from(Courses2Theses).where({thesis_ID : thesisID}).columns("course_ID");
                        var exists = false;
                        if(courses != undefined) {
                            courses.forEach(c => {
                                if(c.course_ID === course) exists = true;
                            })
                        }
                        if(exists === false) {
                            req.reject(472,`Csak ajánlott szakról lehet jelentkezni.`);

                        }
                    } else if(count[0].COUNT >= check[0].max_students) {
                        req.reject(473,`A szabad helyek beteltek.`);

                    }
                }
            }
        }
    });

    this.before (['UPDATE'], 'Theses', async req => {
        var thesisID = req.data.thesis_ID;
        var rank = req.user.attr.Rank[0];
        if(rank != "Teacher") {
            req.reject(403);
        }
        if(req.query.UPDATE != undefined &&
            req.query.UPDATE.entity != undefined &&
            req.query.UPDATE.entity.ref != undefined &&
            req.query.UPDATE.entity.ref[0] != undefined &&
            req.query.UPDATE.entity.ref[0].where != undefined &&
            req.query.UPDATE.entity.ref[0].where[2] != undefined) thesisID = req.query.UPDATE.entity.ref[0].where[2].val;
        if(thesisID != undefined) {
            var archived = await SELECT.from(Theses).where({ID : thesisID}).columns("archived");
            if(archived != undefined && archived[0] != undefined && archived[0].archived === true && req.data.archived === undefined) {
                req.reject(468,`A téma nem szerkeszthető, mert a téma archiválva lett.`);
            }
        }
    });

    this.before (['UPDATE'], ['Students', 'Teachers', 'Admins'], async req => {
        var neptun = req.user.id.substr(0, req.user.id.indexOf('@'));
        var queryNeptun = undefined;
        if(req.query.UPDATE != undefined &&
            req.query.UPDATE.entity != undefined &&
            req.query.UPDATE.entity.ref != undefined &&
            req.query.UPDATE.entity.ref[0] != undefined &&
            req.query.UPDATE.entity.ref[0].where != undefined &&
            req.query.UPDATE.entity.ref[0].where[2] != undefined) queryNeptun = req.query.UPDATE.entity.ref[0].where[2].val;
        if(queryNeptun != undefined) {
            if(queryNeptun != neptun && neptun.length === 6 || queryNeptun != 'GUESTU' && neptun.length != 6) {
                req.reject(469,`Küldő ${neptun} és a kérésben lévő ${queryNeptun} nem egyezik.`);
            }
        }
    });

    this.on(['TeachersStats', 'TeachersStatsWithDate'], async req => {
        var thesisID = req.data.thesis_ID;
        var rank = req.user.attr.Rank[0];
        if(rank != "Admin") {
            req.reject(403);
        }
        if(req.data.operator != undefined) {
            if(req.data.operator != 'gt' && req.data.operator != 'lt') req.reject(400);
        }
        if(req.data.date === undefined) {
            const result = await cds.run(`SELECT FIRST_NAME, LAST_NAME, TEACHER_NEPTUN AS Neptun, COUNT(TEACHER_NEPTUN) AS Value 
                                            FROM "APP_DATA_TEACHERS2THESES" 
                                                JOIN "APP_DATA_TEACHERS" ON TEACHER_NEPTUN = NEPTUN 
                                                JOIN "APP_DATA_THESES" ON THESIS_ID = ID 
                                            GROUP BY TEACHER_NEPTUN, FIRST_NAME, LAST_NAME ORDER BY Value DESC;`);
            req.reply(result);
        } else {
            const result = await cds.run(`SELECT FIRST_NAME, LAST_NAME, TEACHER_NEPTUN AS Neptun, COUNT(TEACHER_NEPTUN) AS Value 
                                            FROM "APP_DATA_TEACHERS2THESES" 
                                                JOIN "APP_DATA_TEACHERS" ON TEACHER_NEPTUN = NEPTUN 
                                                JOIN "APP_DATA_THESES" ON THESIS_ID = ID 
                                            WHERE DATE ${req.data.operator === 'gt' ? '>' : '<'} '${req.data.date}' 
                                            GROUP BY TEACHER_NEPTUN, FIRST_NAME, LAST_NAME ORDER BY Value DESC;`);
            req.reply(result);
        }
    });

    this.on(['StudentsStats', 'StudentsStatsWithDate'], async req => {
        var thesisID = req.data.thesis_ID;
        var rank = req.user.attr.Rank[0];
        if(rank != "Admin") {
            req.reject(403);
        }
        if(req.data.operator != undefined) {
            if(req.data.operator != 'gt' && req.data.operator != 'lt') req.reject(400);
        }
        if(req.data.date === undefined) {
            const result = await cds.run(`SELECT FIRST_NAME, LAST_NAME, STUDENT_NEPTUN AS Neptun, SUM(MOD_COUNT) AS Value 
                                            FROM "CAPAPP_DB_SCHEMA"."APP_DATA_STUDENTS2THESES" 
                                                JOIN "CAPAPP_DB_SCHEMA"."APP_DATA_STUDENTS" ON STUDENT_NEPTUN = NEPTUN 
                                            GROUP BY STUDENT_NEPTUN, FIRST_NAME, LAST_NAME 
                                            ORDER BY VALUE DESC;`);
            req.reply(result);
        } else {
            const result = await cds.run(`SELECT FIRST_NAME, LAST_NAME, STUDENT_NEPTUN AS Neptun, SUM(MOD_COUNT) AS Value 
                                            FROM "CAPAPP_DB_SCHEMA"."APP_DATA_STUDENTS2THESES" 
                                                JOIN "CAPAPP_DB_SCHEMA"."APP_DATA_STUDENTS" ON STUDENT_NEPTUN = NEPTUN 
                                            WHERE CREATEDAT ${req.data.operator === 'gt' ? '>' : '<'} '${req.data.date}' 
                                            GROUP BY STUDENT_NEPTUN, FIRST_NAME, LAST_NAME 
                                            ORDER BY VALUE DESC;`);
            req.reply(result);
        }
    });

  return super.init()
}}

module.exports = { DataService }