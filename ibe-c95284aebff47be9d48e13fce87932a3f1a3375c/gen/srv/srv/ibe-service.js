const cds = require ('@sap/cds')
class ibeService extends cds.ApplicationService {
  /** register custom handlers */
  init(){
    const { ibeReq_Items: ibeReq_Items } = this.entities

    this.before ('UPDATE', 'Requirements', async function(req) {
      const { ID, Items } = req.data
      if (Items) for (let { fetaureName, price} of Items) {
        const { price:before } = await cds.tx(req).run (
          SELECT.one.from (ibeReq_Items, oi => oi.price) .where ({up__ID:ID})
        )
        if (price != before) await this.reqChanged (fetaureName, price-before)
      }
    })

    this.before ('DELETE', 'Requirements', async function(req) {
      const { ID } = req.data
      const Items = await cds.tx(req).run (
        SELECT.from (ibeReq_Items, oi => { oi.fetaureName, oi.price }) .where ({up__ID:ID})
      )
      if (Items) await Promise.all (Items.map(it => this.reqChanged (it.fetaureName, -it.price)))
    })

    return super.init()
  }

  /** requirement changed -> broadcast event */
  reqChanged (fetaureName, deltaPrice) {
    // Emit events to inform subscribers about changes in requirements
    console.log ('> emitting:', 'OrderChanged', { fetaureName, deltaPrice })
    return this.emit ('OrderChanged', { fetaureName, deltaPrice })
  }

}
module.exports = ibeService
