// Declared with arrow function and self referenced through "srv"
module.exports = (srv) => {

    // Set creation date for the Customer entity
    srv.before ('CREATE', 'Customers', async (req) => {
        // Get and set the current date. Not necessary if using "managed"
        req.data.creationDate = new Date().toISOString().slice(0, 10)
    })
    
    // Change attribute value for presentation
    srv.after ('READ', 'Customers', each => {
        each.forename += ' (Owner: Nick)'
    })
    
    // Implement CDS function through NodeJs
    srv.on ('hello', req => `Hello ${req.data.to}!`)

  }