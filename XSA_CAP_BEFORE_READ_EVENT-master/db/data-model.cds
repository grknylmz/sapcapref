
using { User, Country, managed } from '@sap/cds/common';

@cds.persistence.exists
Entity SBOOK_CV {
key MANDT: String;
key CARRID: String;
key CONNID: String;
key FLDATE: String;
key BOOKID: String;
}

@cds.persistence.exists
Entity SFLIGHT_CV {
key MANDT: String;
key CARRID: String;
key CONNID: String;
key FLDATE: String;
Bookings : association to SBOOK_CV on Bookings.MANDT = MANDT and Bookings.CARRID = CARRID and Bookings.CONNID = CONNID and Bookings.FLDATE = FLDATE;
}

