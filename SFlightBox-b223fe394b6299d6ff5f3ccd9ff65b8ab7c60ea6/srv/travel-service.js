const cds = require ('@sap/cds'); require('./workarounds')

class BoxService extends cds.ApplicationService {
init() {

  /**
   * Reflect definitions from the service's CDS model
   */
  const { Box, Geraete } = this.entities


  /**
   * Fill in virtual elements to control status of UI elements.
   */
  const _calculateButtonAvailability = any => {
    const status = any.BoxStatus && any.BoxStatus.code || any.BoxStatus_code
    any.acceptEnabled = status !== 'A'
    any.rejectEnabled = status !== 'X'
    //any.deductDiscountEnabled = status === 'O'
  }
  this.after ('each', 'Box', _calculateButtonAvailability)
  this.after ('EDIT', 'Box', _calculateButtonAvailability)


  /**
   * Fill in primary keys for new Travels.
   * Note: In contrast to Geraetes and GeraeteSupplements that has to happen
   * upon SAVE, as multiple users could create new Travels concurrently.
   */
  this.before ('CREATE', 'Box', async req => {
    const { maxID } = await SELECT.one `max(BoxID) as maxID` .from (Travel)
    req.data.BoxID = maxID + 1
  })


  /**
   * Fill in defaults for new Geraetes when editing Travels.
   */
  this.before ('NEW', 'Geraete', async (req) => {
    const { to_Box_BoxUUID } = req.data
    const { status } = await SELECT `BoxStatus_code as status` .from (Box.drafts, to_Box_BoxUUID)
    if (status === 'X') throw req.reject (400, 'Cannot add new Geraetes to rejected travels.')
    const { maxID } = await SELECT.one `max(GeraeteID) as maxID` .from (Geraete.drafts) .where ({to_Box_BoxUUID})
    req.data.GeraeteID = maxID + 1
    req.data.GeraeteStatus_code = 'N'
   // req.data.GeraeteDate = (new Date).toISOString().slice(0,10) // today
  })


  /**
   * Fill in defaults for new GeraeteSupplements when editing Travels.
   */
/*  this.before ('NEW', 'GeraeteSupplement', async (req) => {
    const { to_Geraete_GeraeteUUID } = req.data
    const { maxID } = await SELECT.one `max(GeraeteSupplementID) as maxID` .from (GeraeteSupplement.drafts) .where ({to_Geraete_GeraeteUUID})
    req.data.GeraeteSupplementID = maxID + 1
  })*/


  /**
   * Changing Geraete Fees is only allowed for not yet accapted Travels.
   */
/*  this.before ('PATCH', 'Travel', async (req) => { if ('GeraeteFee' in req.data) {
    const { status } = await SELECT `TravelStatus_code as status` .from (req._target)
    if (status === 'A') req.reject(400, 'Geraete fee can not be updated for accepted travels.', 'GeraeteFee')
  }})*/


  /**
   * Update the Travel's TotalPrice when its GeraeteFee is modified.
   */
/*  this.after ('PATCH', 'Travel', (_,req) => { if ('GeraeteFee' in req.data) {
    return this._update_totals4 (req.data.TravelUUID)
  }})*/


  /**
   * Update the Travel's TotalPrice when a Geraete's FlightPrice is modified.
   */
 /* this.after ('PATCH', 'Geraete', async (_,req) => { if ('FlightPrice' in req.data) {
    // We need to fetch the Travel's UUID for the given Geraete target
    const { travel } = await SELECT.one `to_Travel_TravelUUID as travel` .from (req._target)
    return this._update_totals4 (travel)
  }})*/


  /**
   * Update the Travel's TotalPrice when a Supplement's Price is modified.
   */
/*  this.after ('PATCH', 'GeraeteSupplement', async (_,req) => { if ('Price' in req.data) {
    // We need to fetch the Travel's UUID for the given Supplement target
    const { travel } = await SELECT.one `to_Travel_TravelUUID as travel` .from (Geraete.drafts)
      .where `GeraeteUUID = ${ SELECT.one `to_Geraete_GeraeteUUID` .from (GeraeteSupplement.drafts).where({BookSupplUUID:req.data.BookSupplUUID}) }`
      // .where `GeraeteUUID = ${ SELECT.one `to_Geraete_GeraeteUUID` .from (req._target) }`
      //> REVISIT: req._target not supported for subselects -> see tests
    return this._update_totals4 (travel)
  }})*/


  /**
   * Helper to re-calculate a Travel's TotalPrice from GeraeteFees, FlightPrices and Supplement Prices.
   */
/*  this._update_totals4 = function (travel) {
    return UPDATE (Travel.drafts, travel) .with ({ TotalPrice: CXL `coalesce (GeraeteFee, 0) + ${
      SELECT `coalesce (sum (FlightPrice + ${
        SELECT `coalesce (sum (Price),0)` .from (GeraeteSupplement.drafts) .where `to_Geraete_GeraeteUUID = GeraeteUUID`
      }),0)` .from (Geraete.drafts) .where `to_Travel_TravelUUID = TravelUUID`
    }` })
  }*/


  /**
   * Validate a Travel's edited data before save.
   */
  this.before ('SAVE', 'Box', req => {
    const { BeginDateAusleihe, EndDate } = req.data, today = (new Date).toISOString().slice(0,10)
    if (BeginDateAusleihe < today) req.error (400, `Begin Date ${BeginDateAusleihe} must not be before today ${today}.`, 'in/BeginDateAusleihe')
    if (BeginDateAusleihe > EndDate) req.error (400, `Begin Date ${BeginDateAusleihe} must be before End Date ${EndDateAusleihe}.`, 'in/BeginDateAusleihe')
  })


  //
  // Action Implementations...
  //

  this.on ('acceptBox', req => UPDATE (req._target) .with ({BoxStatus_code:'A'}))
  this.on ('rejectBox', req => UPDATE (req._target) .with ({BoxStatus_code:'X'}))
  /*this.on ('deductDiscount', async req => {
    let discount = req.data.percent / 100
    let succeeded = await UPDATE (req._target)
      .where `BoxStatus_code != 'A'`
      /*.and `GeraeteFee is not null`
      .with (`
        TotalPrice = round (TotalPrice - GeraeteFee * ${discount}, 3),
        GeraeteFee = round (GeraeteFee - GeraeteFee * ${discount}, 3)
      `)*/
   /* if (!succeeded) { //> let's find out why...
      //let travel = await SELECT.one `TravelID as ID, TravelStatus_code as status, GeraeteFee` .from (req._target)
      if (!box) throw req.reject (404, `Box "${box.ID}" does not exist; may have been deleted meanwhile.`)
      if (box.status === 'A') req.reject (400, `Box "${box.ID}" has been approved already.`)
      //if (travel.GeraeteFee == null) throw req.reject (404, `No discount possible, as travel "${travel.ID}" does not yet have a Geraete fee added.`)
    } else {
      // Note: it is important to read from this, not db to include draft handling
      // REVISIT: through req._target workaround, IsActiveEntity is non-enumerable, which breaks this.read(Travel, req.params[0])
      const [{ BoxUUID, IsActiveEntity }] = req.params
      return this.read(Box, { BoxUUID, IsActiveEntity })
    }*/
  //})


  // Add base class's handlers. Handlers registered above go first.
  return super.init()

}}
module.exports = {BoxService}
