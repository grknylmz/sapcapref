using {
    Currency,
    User,
    managed,
    cuid
} from '@sap/cds/common';

namespace sap.capire.ibe;

type vehicleType : Integer enum {
    SUV       = 1;
    Sedan     = 2;
    Hatchback = 3;
    Truck     = 4;
}

type userType : Integer enum {
    Admin  = 1;
    Buyer  = 2;
    Seller = 3;
}

type fetaureType : Integer enum {
    Mandatory = 1;
    Optional  = 2;
}

type DocStatus : Integer enum {
    draft = 1;
    published = 2;
    archived = 3;

}

entity ibeReq : cuid, managed {
    ReqNo        : String @title : 'Ibe Order Number'; //> readable key
    Items        : Composition of many ibeReq_Items
                       on Items.up_ = $self;
    versionID    : UUID;
    vehicleType  : String;
    vehicleMake  : String;
    vehicleModel : String;
    desiredPrice : Decimal(9, 2);
    currency     : Currency;
    validToDate  : Date;
    url          : String;
    buyer        : User;
    userType     : userType;
    DocStatus    : DocStatus;
    comment      : String;
}

entity ibeReq_Items {
    key ID          : UUID;
        up_         : Association to ibeReq;
        fetaureName : String;
        fetaureType : String;
        price       : Double;
        url         : String;
        comment     : String;
}
