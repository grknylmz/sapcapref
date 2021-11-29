namespace com.playground.bookshop;

using com.playground.bookshop from '../db/bookshop-models';

service CatalogService {
  entity Books as projection on bookshop.Books;
  entity Authors as projection on bookshop.Authors;
  //entity Orders @insertonly as projection on bookshop.Orders;
  entity Orders as projection on bookshop.Orders;
}
  @Capabilities.SearchRestrictions.Searchable: true
  @Aggregation.ApplySupported.PropertyRestrictions: true
  // entity Books2 @(
    
	// 	title: '{i18n>book}',
	// 	Capabilities: {
	// 		InsertRestrictions: {Insertable: false},
	// 		UpdateRestrictions: {Updatable: false},
	// 		DeleteRestrictions: {Deletable: false}
	// 	}) as projection on bookshop.Books {
  //     ID,
  //     title,
  //     author,
  //     @Analytics.Measure: true
	//    	@Aggregation.default: #SUM
  //     stock
  //   } excluding {title};

  //       @Capabilities.SearchRestrictions.Searchable: true
  // entity Authors2 as select from bookshop.Authors {
  //   *,
  //   null as numberOfBooks: Integer
  // };


  // entity Authors3 as select from bookshop.Authors, bookshop.Books {
	// 	key Authors.ID,
  //   @Search.defaultSearchElement: true 
  //   name,
	// 	// computed is not persistent in db
  //   @Core.Computed
	//   count(Books.ID) as numberOfBooks: Integer
	// } where Authors.books.ID = Books.ID
  //   group by Authors.ID;

  // @(insertonly, requires:'authenticated-user')
//   entity Orders2 as projection on bookshop.Orders {
//     *,
//     @odata.etag
//     firstCreatedAt
//   };



  annotate bookshop.Authors with @(
      title: 'Authors'
    ){
			@Search.defaultSearchElement: true
			name;
		};

// service CustomersService @(requires:'authenticated-user') {
//   //@readonly entity Authors as select from my.Authors where buyer = $user;
//   //action cancelOrder (ID:String);
//     action cancelOrder ( orderID:Integer, reason:String ) returns cancelOrderRet;
// }

// service AdminService @(requires:'admin') {
//   entity Books as projection on my.Books;
//   entity Authors as projection on my.Authors;
//   entity Orders as projection on my.Orders
// }