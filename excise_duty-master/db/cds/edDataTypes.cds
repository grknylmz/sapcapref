@Analytics.hidden: true
context edDataTypes{

    type edTypeId: String (3); // /BEV2/ADART
    type taxWarehouseRegistration: String(15); // /BEV2/EDSLAGER
    type stockLedgerWarehouseType: String(1); // /BEV2/EDSLTYP Todo
    type edDescription: String(25); // /BEV2/EDBESCH
    type edLongDescription: String(40); // /BEV2/EDBESCHL
    type exciseDutyNumber: String(15); // /BEV2/EDSNR
    type exciseDutyTypeIndependentMaterialGroup : String(6); // /BEV2/EDMATGRP_S
    type alcoholicStrength: Decimal(5,2); // /BEV2/EDALKO
    type exciseDutySpecialPartnerTypeId: String(3); 
    type movementCategory: String(3); // /BEV2/EDBEWTYP
    type stockLedgerGroupId: String(5); // /BEV2/EDLBGRP
    type stockLedgerDivision: String(2); // /BEV2/EDLBSPAL  
    type stockLedgerSubdivision: String(2); // /BEV2/EDLBUSP
    type movementEntryBehavior: String(1); // /BEV2/EDKLGBUCH
    type stockLedgerNumber: Integer; // /BEV2/EDLGBUCH  
    type containerContentCalculationIndicator: Integer;  
    type taxRate: Decimal(14,5); //ToDo: Clarify actual length
    type edPositionNumber: Integer; // /BEV2/EDPOSNR //ToDo: Equivalent for NUMC(3)? 
    type noEntryInExtendedStockLedger: Boolean; // /BEV2/EDNRWLAGB
    type taxValue: Decimal(17,5); // /BEV2/EDPREISP
    type procurementType: String(1); // /BEV2/EDBESKZ
    type extensionPoint: String(20);
    type extensionDestination: String(200);
    type registeredConsignee: String(15); // /BEV2/EDREG_RECEIVER
    type outsideEUTaxTerritoryIndicator: Boolean; // /BEV2/EDEUNOTAXTERR
    type euCountryIndicator: String(3); // /BEV2/EDEUDOM4COUNTRY
}