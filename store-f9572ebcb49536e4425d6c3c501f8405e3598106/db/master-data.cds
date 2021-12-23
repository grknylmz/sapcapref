namespace masterdata;
using {epam.sap.dev.kitchen_app.store} from './schema';
 
 
entity UnitOfMeasure {
    key msehi      : String(3);
        dimid      : String(6);
        isocode    : String(3);
        name : String(20);
}
 
entity ProductGroups {
    key ID     : String(3);
    name       : String(20);
    imageURL   : String @UI.IsImageURL;
    imageType  : String @Core.IsMediaType;
}
 
entity Phases {
    key ID     : String;
    name       : String(20);
    criticality: Integer;
}
 
entity MarketInfos {
    key ID     : String;
    name       : String(50);
    code       : String(2);
    imageURL   : String @UI.IsImageURL;
    imageType  : String @Core.IsMediaType;
}