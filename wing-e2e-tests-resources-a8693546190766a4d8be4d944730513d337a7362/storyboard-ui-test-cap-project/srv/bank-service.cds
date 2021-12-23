using {BANK} from './external/BANK.csn';

service BankService {
    @readonly
    entity Banks as projection on BANK.sap.gateway.srvd_a2x.api_bank.v0001.Bank {
        key BankInternalID as ID, BankName, BankCountry, Region, CityName
    };
}
