@Analytics.hidden: true
context erpDataTypes{
    type BUKRS : String(4);
    type LAND1 : String(3); //ToDo: Reuse service or replication for T005?
    type WERKS_D : String(4);
    type LGORT_D : String(4);
    type UNIT: String(3); // Todo: Align with UnitOfMeasure Service
    type MATNR: String(40); // MATNR in S/4 is Char 40
    type KUNNR: String(10);
    type BWART: String(3);
    type KZBEW: String(1);
    type WAERS: String(5);
    type MJAHR: String(4); //ToDo: Type for NUMC?
    type MBLNR: String(10);
    type MBLPO: String(4);
    type BUDAT: Date;
    type CHARG_D: String(10);
    type STORN: Boolean;
    type MENGE_D: Decimal(13,3);
    type MEINS: String(3); // Todo: Align with UnitOfMeasure Service
    type AUFNR: String(12);
    type POSNR: String(6);
    type UMREZ: Decimal(5, 0);
    type UMREN: Decimal(5, 0);
    type NAME: String(35);
    type TEXT40: String(40);
    type KDGRP: String(2);
}