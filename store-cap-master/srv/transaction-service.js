module.exports = async function() {

  // Get Item entity for the deletion operation of the Transaction Header entity
  // Take notice that the context of the data model was also informed
  const {Item} = cds.entities ('my.company.Transaction')  
  const {Product} = cds.entities ('my.company')  
  
  // Test to update item entity through a common deletion operation
  this.before ('DELETE', 'Headers', async (req) => {
    if (req.data.ID == 4) {
      const tx = cds.transaction(req)
      // Check ORM operations through https://cap.cloud.sap/docs/node.js/api#srv-entities
      const affectedRows = await tx.run (
        UPDATE (Item)
          .set   ({ price: {'-=': 500}})
          .where ({ price: {'>=': 900}})
      )
      if (affectedRows === 0)  req.error (409, "Item 4 already removed.")
    }
  })

  // This is a custom insert operation for example purpose
  this.on ('addItem', async req => {
    const {transaction, product} = req.data
    // Get product data
    const selectedProduct = await SELECT
      .from (Product)
      .where ({ID: product})
    // Insert new item based on the informed transaction and product
    await INSERT
      .into(Item)
      .columns('header_id', 'product_id', 'price', 'currency')
      .values(transaction, product, selectedProduct[0].price, selectedProduct[0].currency)
  })

}