service say {

    function hello(to : String) returns String;

    entity Books as projection on db.Books excluding {
        price
    };
}


entity db.Books {
    key id     : Integer;
        title  : String;
        author : String;
        price  : Decimal(9, 2);

}
