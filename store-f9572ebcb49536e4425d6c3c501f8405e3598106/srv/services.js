const cds = require('@sap/cds');
require('./workarounds')
const db = cds.connect.to ('db')
 
 //module.exports = function(service) {
class ProductService extends cds.ApplicationService {
init() {
 let { Products, Markets, Orders } = this.entities;

//  const _calculateButtonAvailability = any => {
//     const marketStatus = any.marketStatus || {}
//     any.confirmMarketEnabled = marketStatus != true
//   }
//   this.after ('each', 'Markets', _calculateButtonAvailability)
//   this.after ('EDIT', 'Markets', _calculateButtonAvailability)
this.after('each','Markets', any => {
    const {marketStatus}  = any || {}
    any.confirmMarketEnabled = marketStatus != true
})

this.before (['CREATE','UPDATE'], 'Products', async (request) => {
       const product  = request.data;
       if (!product.price || product.price <= 0)  //Enforce "mandatory" annotation for the "price" field to validate positive numbers only 
       return request.error (400, 'Understated price. Should be higher than 0 as we suppose.');
       if (product.taxrate < 0)  //Validate positive numbers only for tax rate
       return request.error (400, 'Wrong tax rate input. Please clarify.');
               })

this.before ('NEW', 'Products', async (req) => {
    if(!req.data.Phase_ID){
        req.data.Phase_ID = '1'}
})

 this.after ('PATCH', 'Orders',async (_,req) => { if ('quantity' in req.data) {
     const {orderUUID} = req.data
    //return this._update_totals4 (orderUUID)
    //return this._update_market_totals(orderUUID)
    //return this._update_product_totals(orderUUID)
    return  [await this._update_totals4 (orderUUID), 
             await this._update_market_totals (orderUUID),
             await this._update_product_totals(orderUUID)] 
  }})

  this._update_market_totals = async function (order){
       const {toMarket_marketUUID} = await SELECT.one `toMarket_marketUUID` .from(Orders.drafts,order)
      const { quantities } = await SELECT.one `sum(quantity) as quantities` .from (Orders.drafts) .where({toMarket_marketUUID:toMarket_marketUUID})
      const {netAmounts} = await SELECT.one `sum(orderNetAmount) as netAmounts` .from(Orders.drafts) .where({toMarket_marketUUID:toMarket_marketUUID})
    const {taxAmounts} = await SELECT.one `sum(orderTaxAmount) as taxAmounts` .from(Orders.drafts) .where({toMarket_marketUUID:toMarket_marketUUID})
      const {grossAmounts} = await SELECT.one `sum(oderGrossAmount) as grossAmounts` .from(Orders.drafts) .where({toMarket_marketUUID:toMarket_marketUUID})
      return UPDATE (Markets.drafts, toMarket_marketUUID) .with ({marketTotalQuantity: quantities,
                                                                    marketNetAmount: netAmounts,
                                                                marketGrossAmount : grossAmounts,
                                                            marketTaxAmount:taxAmounts})
  }

  this._update_product_totals = async function (order){
      const {toMarket_marketUUID} = await SELECT.one `toMarket_marketUUID` .from(Orders.drafts,order)
     const {toProduct_productUUID} = await SELECT.one `toProduct_productUUID` .from(Markets.drafts, toMarket_marketUUID)
      const { quantities } = await SELECT.one `sum(marketTotalQuantity) as quantities` .from (Markets.drafts) .where({toProduct_productUUID:toProduct_productUUID})
      const {netAmounts} = await SELECT.one `sum(marketNetAmount) as netAmounts` .from(Markets.drafts) .where({toProduct_productUUID:toProduct_productUUID})
      const {taxAmounts} = await SELECT.one `sum(marketTaxAmount) as taxAmounts` .from(Markets.drafts) .where({toProduct_productUUID:toProduct_productUUID})
    const {grossAmounts} = await SELECT.one `sum(marketGrossAmount) as grossAmounts` .from(Markets.drafts) .where({toProduct_productUUID:toProduct_productUUID})
      return UPDATE (Products.drafts, toProduct_productUUID) .with ({productTotalQuantity: quantities,
                                                                    productNetAmount:  netAmounts,
                                                                productTaxAmount:taxAmounts,
                                                            productGrossAmount: grossAmounts})
  }

 this._update_totals4 = async function (order) {
    const{ quantity } = await SELECT.one `quantity` .from (Orders.drafts,order)
   const {toMarket_marketUUID} = await SELECT.one `toMarket_marketUUID` .from(Orders.drafts,order)
   const {toProduct_productUUID} = await SELECT.one `toProduct_productUUID` .from(Markets.drafts, toMarket_marketUUID)
   const {price} = await SELECT.one `price` .from (Products.drafts, toProduct_productUUID)
   const {taxrate} = await SELECT.one `taxrate` .from (Products.drafts, toProduct_productUUID)
      //SELECT.one `price as price, taxrate as taxrate` .from (Products.drafts) .where ({ toMarket_marketUUID: /* in: */
      //  SELECT `marketUUID` .from (Markets.drafts) .where ({ orderUUID: order })
    return UPDATE (Orders.drafts,order) .with ({ orderNetAmount:  quantity * price ,
                                             orderTaxAmount: price*quantity*(taxrate/100),
                                                 oderGrossAmount: (quantity * price) + (price*quantity*taxrate/100)})   
  }

this.before ('NEW', 'Orders', async (request) => {
    const { maxID } = await SELECT.one `max(orderID) as maxID` .from (Orders.drafts)
    request.data.orderID = maxID + 1
    const {CurrencyCode} = await SELECT.one `CurrencyCode_code as CurrencyCode`.from (Markets.drafts, request.data.toMarket_marketUUID)
    request.data.CurrencyCode_code = CurrencyCode
  })
  

  this.before('NEW', 'Markets',async (req) =>{
const {CurrencyCode}   = await SELECT.one `CurrencyCode_code as CurrencyCode` .from (Products.drafts,  req.data.toProduct_productUUID)
  req.data.CurrencyCode_code = CurrencyCode
})



this.after ('each', 'Products', any =>{
    const {ID} = any ||{}
    any.moveToNextEnabled = ID != 4
})

// this.before ('CREATE','Products', async (req) => {
//             const { product_ID,toProductGroup_ID } = req.data
//             const ID  = await SELECT.one `product_ID as ID` .from(Products) .where({toProductGroup_ID,product_ID})
//             if(ID != null) req.error (400, `Current Product ID ${product_ID} for ${toProductGroup_ID} already exists.`)            
//         })


this.after ('PATCH', 'Orders', async (_,req) => { if ('deliveryDate' in req.data){
    const {deliveryDate,orderUUID} = req.data
     return this._update_year (deliveryDate,orderUUID)
}

})

 this._update_year = async function (deliveryDate,orderUUID) {
    const date = deliveryDate.toString().substring(0,4)
    return UPDATE (Orders.drafts,orderUUID) .with ({ calendarYear: date })
  }

//   this.before ('CREATE', 'Orders', async function(req) {
//       const {date} = req.data.deliveryDate.getFullYear();
//     if(request.data.deliveryDate !== 0){
//     const {date} = request.data.deliveryDate.getFullYear();
//     alert (date)
// request.data.calendarYear = date
// }
//   })

//   this.before ('SAVE', 'Orders', async(request) => {
// const { toMarket_MarketUUID } = req.data
//     const { beginDate } = await SELECT `startDate as beginDate` .from (Markets.drafts, toMarket_MarketUUID)
//     const { endDate } = await SELECT `endDate as endDate` .from (Markets.drafts, toMarket_MarketUUID)
//     if(request.data.deliveryDate < beginDate && request.data.deliveryDate > endDate)
//     return request.error(400, 'Delivery date should be between starting date and ending date')
//   })
  this.before ('SAVE', 'Products', async (req) => {
      if (req.data.market.length != 0){
          const curMarketID = req.data.market[req.data.market.length-1].toMarketInfos_ID
          let i =0
          while(i < req.data.market.length - 1){
              if(req.data.market[i].toMarketInfos_ID === curMarketID && curMarketID != null && curMarketID != undefined)
              req.error (400, `Current Market ${req.data.market[i].toMarketInfos_ID} already exists.`)
              i++
          }

          const marketItems = req.data.market
          const today = (new Date).toISOString().slice(0,10)
          if(marketItems) marketItems.forEach(async (marketItem) => {
            const {startDate, endDate} = marketItem
            let i = 0
            while (i < marketItem.order.length)
            {
                if (marketItem.order[i].deliveryDate && marketItem.order[i].deliveryDate != null && marketItem.order[i].deliveryDate != undefined){
                    if(marketItem.order[i].deliveryDate < startDate) req.error(400, `Delivery date ${marketItem.order[i].deliveryDate} must be after starting date ${startDate}.`, 'in/startDate')
                    if(marketItem.order[i].deliveryDate > endDate) req.error(400, `Delivery date ${marketItem.order[i].deliveryDate} must be before ending date ${endDate}.`, 'in/endDate')
                    i++
                }
            }
        if (endDate < today) req.error (400, `Ending Date ${endDate} must not be before today ${today}.`, 'in/startDate');
    if (startDate > endDate) req.error (400, `Starting Date ${startDate} must be before End Date ${endDate}.`, 'in/startDate');
          })
              
          }
        })


this.on ('confirmMarket', req => UPDATE (req._target) .with ({marketStatus:'1'}))

this.on ('moveToNext','Products', async (req) => {
const productUUID = req._target.ref[0].where[2].val
const{phase} = await SELECT.one `phase_ID as phase`.from (Products). where({productUUID: productUUID})
const{market} = await SELECT.one `marketStatus as market` .from(Markets) .where({toProduct_productUUID:productUUID})
let marketStatus = await SELECT.one `marketStatus` .from(Markets) .where({toProduct_productUUID:productUUID, marketStatus:true})
let marketItems = await SELECT `marketStatus as marketStatus, endDate as endDate` .from(Markets) .where({toProduct_productUUID:productUUID})
switch(phase){
    case '1':
        if(market != null && market !=  undefined){
           // UPDATE (req._target) .where({productUUID:productUUID}).with ({Phase_ID: '2'})
        //    let secondPhase = await UPDATE (req._target).where `productUUID = productUUID` .with (`Phase_ID = 2`)
        //    const [{ productUUID, IsActiveEntity }] = req.params
        //  return this.read(Products, { productUUID, IsActiveEntity })
        // }else{
        //     req.error(400,'Cant move to the Development, because markets were not assigned to the product')
        // }
        { let secondPhaseSucceeded = await UPDATE (req._target) .where `productUUID = productUUID` .with (`phase_ID = '2'`)
      if (!secondPhaseSucceeded) {  
        //if (product.status === 2) req.reject (400, `Product "${product.toProductGroup_ID}" "${product.productID}" has been changed already.`)
      } else { const [{ productUUID, IsActiveEntity }] = req.params
      return this.read(Products, { productUUID, IsActiveEntity })}
    } req.reject (400, `Can't move to the Development, because markets were not assigned to the product.`);
        }
        break;
    case '2':
            //const marketItems = req.data.market
            let i=0
            if(marketItems && i==0) marketItems.forEach(async (marketItem) => {
            const {marketStatus} = marketItem
            if (marketStatus == true){
            i++;
           let thirdPhaseSucceeded = await UPDATE (req._target) .where `productUUID = productUUID` .with (`phase_ID = '3'`)
      if (!thirdPhaseSucceeded) {  
      } else { const [{ productUUID, IsActiveEntity }] = req.params
      return this.read(Products, { productUUID, IsActiveEntity })}       
            }
            })
            if(i==0 || !marketItems){
           req.error(400,'Cant move to the Production, because no one  market was confirmed')
            }
        break;           
    case '3': 
    //const marketItems = req.data.market
    const today = (new Date).toISOString().slice(0,10)
            let y=0
            if(marketItems) marketItems.forEach(async (marketItem) => {
            const {marketStatus, endDate} = marketItem
            if (endDate != null && endDate<= today){
            y++;
            }
            })
            if(y==marketItems.length){
                { let PhaseSucceeded = await UPDATE (req._target) .where `productUUID = productUUID` .with (`phase_ID = '4'`)
      if (!PhaseSucceeded) {  
        //if (product.status === 2) req.reject (400, `Product "${product.toProductGroup_ID}" "${product.productID}" has been changed already.`)
      } else { const [{ productUUID, IsActiveEntity }] = req.params
      return this.read(Products, { productUUID, IsActiveEntity })}}
            }
            else{
           req.error(400,'Cant move to Out Phase, because not all markets complited production')
            }
    break;
    case '4': 
    req.error(400,'This is the latest phase')
    break;
}
})
return super.init()}}
module.exports = {ProductService}