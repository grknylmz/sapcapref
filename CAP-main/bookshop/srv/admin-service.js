module.exports = (srv) => {
  const { OrderItems } = srv.entities("sap.krc.bookshop");

  srv.before("*", (req) => {
    console.debug("Before >>>", req.method, req.target.name);
  });

  srv.after(["READ", "EDIT"], "Orders", _calculateTotals);

  async function _calculateTotals(orders, req) {
    console.debug("_calculateTotals", req.method);
    const ordersByID = Array.isArray(orders)
      ? orders.reduce((all, o) => {
          (all[o.ID] = o).total = 0;
          return all;
        }, {})
      : { [orders.ID]: orders };

    return cds
      .transaction(req)
      .run(
        SELECT.from(OrderItems)
          .columns("parent_ID", "netAmount")
          .where({ parent_ID: { in: Object.keys(ordersByID) } })
      )
      .then((items) =>
        items.forEach(
          (item) => (ordersByID[item.parent_ID].total += item.netAmount)
        )
      );
  }
};
