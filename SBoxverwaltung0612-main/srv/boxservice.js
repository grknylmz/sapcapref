const cds = require ('@sap/cds'); require('./workarounds')

class BoxService extends cds.ApplicationService {
init() {

  /** Reflect definitions from the service's CDS model */
  const { Box, Geraete } = this.entities

/** Sichtbarkeit der Buttons */
const _calculateButtonAvailability = any => {
const status = any.BoxStatus && any.BoxStatus.code || any.BoxStatus_code
//Status Rückgabe
if (any.acceptEnabled = status === 'R')
{    any.rejectEnabled = status === 'X'
    any.availableEnabled = status !== 'O'
//Status Verfügbar    
}  else if (any.availableEnabled = status === 'O')
{    any.rejectEnabled = status !== 'X'
    any.acceptEnabled = status === 'R'   
//Status Außer Haus    
} else if (any.rejectEnabled = status === 'X')
{    any.avaiableEnabled = status === 'O'
    any.acceptEnabled = status !== 'R'                          
}}
this.after ('each', 'Box', _calculateButtonAvailability)
this.after ('EDIT', 'Box', _calculateButtonAvailability)

 

/** Fill in primary keys for new Boxs. */
  this.before ('CREATE', 'Box', async req => {
    const { maxID } = await SELECT.one `max(BoxID) as maxID` .from (Box)
    req.data.BoxID = maxID + 1
  })


  /** Fill in defaults for new Geraete when editing Boxen */
  this.before ('NEW', 'Geraete', async (req) => {
    const { to_Box_BoxUUID } = req.data
    const { status } = await SELECT `BoxStatus_code as status` .from (Box.drafts, to_Box_BoxUUID)
    if (status === 'X') throw req.reject (400, 'Das Hinzufügen eines neuen Geräts ist im Status "Außer Haus" ist nicht verfügbar.')
    //const { maxID } = await SELECT.one `max(GeraeteID) as maxID` .from (Geraete.drafts) .where ({to_Box_BoxUUID})
    //req.data.GeraeteID = maxID + 1
    //req.data.GeraeteStatus_code = 'N'
   // req.data.BookingDate = (new Date).toISOString().slice(0,10) // today
  })


  /**
   * Fill in defaults for new BookingSupplements when editing Boxs.
   */
/*  this.before ('NEW', 'BookingSupplement', async (req) => {
    const { to_Booking_BookingUUID } = req.data
    const { maxID } = await SELECT.one `max(BookingSupplementID) as maxID` .from (BookingSupplement.drafts) .where ({to_Booking_BookingUUID})
    req.data.BookingSupplementID = maxID + 1
  })*/


  /**
   * Changing Booking Fees is only allowed for not yet accapted Travels.
   */
 /* this.before ('PATCH', 'Travel', async (req) => { if ('BookingFee' in req.data) {
    const { status } = await SELECT `TravelStatus_code as status` .from (req._target)
    if (status === 'A') req.reject(400, 'Booking fee can not be updated for accepted travels.', 'BookingFee')
  }})*/


  /**
   * Update the Travel's TotalPrice when its BookingFee is modified.
   */
/*  this.after ('PATCH', 'Travel', (_,req) => { if ('BookingFee' in req.data) {
    return this._update_totals4 (req.data.TravelUUID)
  }})*/


  /**
   * Update the Travel's TotalPrice when a Booking's FlightPrice is modified.
   */
  /*this.after ('PATCH', 'Booking', async (_,req) => { if ('FlightPrice' in req.data) {
    // We need to fetch the Travel's UUID for the given Booking target
    const { travel } = await SELECT.one `to_Travel_TravelUUID as travel` .from (req._target)
    return this._update_totals4 (travel)
  }})*/


  /**
   * Update the Travel's TotalPrice when a Supplement's Price is modified.
   */
 /* this.after ('PATCH', 'BookingSupplement', async (_,req) => { if ('Price' in req.data) {
    // We need to fetch the Travel's UUID for the given Supplement target
    const { travel } = await SELECT.one `to_Travel_TravelUUID as travel` .from (Booking.drafts)
      .where `BookingUUID = ${ SELECT.one `to_Booking_BookingUUID` .from (BookingSupplement.drafts).where({BookSupplUUID:req.data.BookSupplUUID}) }`
      // .where `BookingUUID = ${ SELECT.one `to_Booking_BookingUUID` .from (req._target) }`
      //> REVISIT: req._target not supported for subselects -> see tests
    return this._update_totals4 (travel)
  }})*/


  /**
   * Helper to re-calculate a Travel's TotalPrice from BookingFees, FlightPrices and Supplement Prices.
   */
  /*this._update_totals4 = function (travel) {
    return UPDATE (Travel.drafts, travel) .with ({ TotalPrice: CXL `coalesce (BookingFee, 0) + ${
      SELECT `coalesce (sum (FlightPrice + ${
        SELECT `coalesce (sum (Price),0)` .from (BookingSupplement.drafts) .where `to_Booking_BookingUUID = BookingUUID`
      }),0)` .from (Booking.drafts) .where `to_Travel_TravelUUID = TravelUUID`
    }` })
  }*/


  /**
   * Validate a Travel's edited data before save.
   */
  this.before ('SAVE', 'Box', req => {
    const { BeginDateAusleihe, EndDateAusleihe } = req.data, today = (new Date).toISOString().slice(0,10)
    if (BeginDateAusleihe < today) req.error (400, `Datem der Ausleihe ${BeginDateAusleihe} darf nicht vor dem heutigen Datum liegen ${today}.`, 'in/BeginDateAusleihe')
    if (BeginDateAusleihe > EndDateAusleihe) req.error (400, `Datum der Ausleihe ${BeginDateAusleihe} muss vor dem Rückgabedatum liegen ${EndDateAusleihe}.`, 'in/BeginDateAusleihe')
  })

  //
  // Action Implementations...
  //

  this.on ('acceptBox', req => UPDATE (req._target) .with ({BoxStatus_code:'R'}))
  this.on ('rejectBox', req => UPDATE (req._target) .with ({BoxStatus_code:'X'}))
  this.on ('availableBox', req => UPDATE (req._target) .with ({BoxStatus_code:'O'}))


  // Add base class's handlers. Handlers registered above go first.
  return super.init()

}}
module.exports = {BoxService}
