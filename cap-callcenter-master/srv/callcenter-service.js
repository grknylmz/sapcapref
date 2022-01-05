const cds = require('@sap/cds')

module.exports = async function () {
    const { Inquiries } = cds.entities
    //const { Inquiries } = this.entities
    const db = await cds.connect.to('db')
    
    db.after('each', Inquiries, (each) => {
        if ('hoursBefoerStart' in each) {
            if(each.startedAt) {
            let difference = new Date(each.startedAt).getTime() -new Date(each.createdAt).getTime()
            let hoursBefoerStart =  Math.floor(difference/1000/60/60)
            each.hoursBefoerStart = hoursBefoerStart
            }
        }
    })    

    this.before('CREATE', 'Inquiries', async(req) => {
        console.log('Create handloer called')
        req.data.status_code = '1'
    })    

    this.after('READ', 'Inquiries', (each) => {
        if (each.status_code === '1' ) {
            each.startEnabled = true
        }
        if (each.status_code !== '3') {
            each.closeEnabled = true
        }
    })    

    this.on ('start', async (req)=> {
        console.log('data: ' + req.data)
        const id = req.params[0]

        // //Get createdAt
        const createdAt = await SELECT.from(Inquiries).columns(['createdAt']).where({ID:id})

        //Get startedAt
        const startedAt = Date.now()

        const difference = startedAt - new Date(createdAt).getTime()
        const hoursBefoerStart =  Math.floor(difference/1000/60/60)        

        const n = await UPDATE(Inquiries).set({ 
            status_code:'2',
            startedAt: Date.now(),
            hoursBefoerStart: hoursBefoerStart
        }).where ({ID:id})//.and({status_code:'1'})
        n > 0 || req.error (404) 
    })

    this.on('close', async (req) => {
        const id = req.params[0]
        const { satisfaction } = req.data
        const n = await UPDATE(Inquiries).set({ 
            satisfaction: satisfaction,
            status_code: '3'
        }).where ({ID:id}).and({status_code:{'<>':'3'}})
        n > 0 || req.error (404)        
    })

}