namespace com.playground.products;

using { Country } from '@sap/cds/common';

entity Products {
  key ID: String(36);
  country: Country;
  Reviews: Association to many Reviews on Reviews.product = $self @title: '{i18n>review}';
}

entity Reviews {
  key Review: UUID;
  product: Association to Products @title: '{i18n>product}';
  title: String(60) @title: '{i18n>reviewTitle}';
  message: String(1024) @title: '{i18n>reviewText}';
  rating: Decimal(4, 2) @title: '{i18n>rating}';
  helpfulCount: Integer @title: '{i18n>ratedHelpful}';
  helpfulTotal: Integer @title: '{i18n>ratedTotal}';
}