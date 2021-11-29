/**
 * Implementation for CatalogService defined in ./book-service.cds
 */
module.exports = (srv)=>{

    // Use reflection to get the csn definition of Books
    // const { Books } = cds.entities
    const Books = cds.entities('com.playground.bookshop')
    // Intercept generic handler to read books programmatically
    // (actually not required, just as a showcase)
    //srv.on ('READ','Books', SELECT.from(Books))
  
    // Add some discount for overstocked books
    srv.after ('READ','Books', (each)=>{
      if (each.stock > 111) each.title += ' -- 11% discount!'
    })
  
    // Reduce stock of books upon incoming orders
    srv.before ('CREATE','Orders', async (req)=>{
      const tx = cds.transaction(req), order = req.data
      const affectedRows = await tx.run (
        UPDATE(Books) .where ({ID:order.book_ID})
        .and (`stock >=`,order.amount)
        .set (`stock -=`,order.amount)
      )
      if (!affectedRows) req.error (409,'Sold out, sorry')
    })
  
  }