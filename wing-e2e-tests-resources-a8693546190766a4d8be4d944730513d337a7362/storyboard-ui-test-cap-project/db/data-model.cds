namespace my.bookshop;

entity Books {
  key ID      : Integer;
      title   : String;
      stock   : Integer;
      address : type of title;
}

entity Author {
  name : type of Books : title
}

context Categories {
  entity Category {
    key ID   : Integer;
        name : String;
  }
}
