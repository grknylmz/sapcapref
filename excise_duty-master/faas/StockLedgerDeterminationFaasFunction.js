function doStockLedgerDetermination(input) {

	var ORIGIN_TAXED = "taxed";
	var ORIGIN_TAXED_PARTNER = "taxed partner";
	var ORIGIN_TAXFREE_PARTNER = "taxfree partner";
	var ORIGIN_THIRD_COUNTRY = "third country";
	var ORIGIN_THIRD_COUNTRY_EXPORT = "third country export";
	var ORIGIN_TAX_WAREHOUSE = "tax warehouse";
	var ORIGIN_TAX_WAREHOUSE_EU = "tax warehouse EU";
	var ORIGIN_EU = "EU";
	var ORIGIN_EU_THIRD_COUNTRY = "EU third country";

	var TO_BE_CLARIFIED_ORIGIN_GLEICHES_SLAGER = "1";
	var TO_BE_CLARIFIED_ORIGIN_SL_HERSTELLUNG = "2";
	var TO_BE_CLARIFIED_ORIGIN_SL_FERTIGUNG = "3";
	var TO_BE_CLARIFIED_ORIGIN_FREITRUNK = "4";
	var TO_BE_CLARIFIED_ORIGIN_ARMEE = "5";
	var TO_BE_CLARIFIED_ORIGIN_KUNDE_SONST = "6";
	var TO_BE_CLARIFIED_ORIGIN_HAUSTRUNK = "7";

	var getOriginCommon = function(input) {

		// Step 22:
		// if special partner type and Ship-To Party Master Data in Excise
		// Duties
		// and no taxation for special partner,
		// then origin = 'taxfree partner', otherwise origin = 'taxed partner'
		try {
			
			return sldi.ShipToMasterExtension.exciseDutySpecialPartnerType.taxationRelevantIndicator === 'X' ? ORIGIN_TAXED_PARTNER : ORIGIN_TAXFREE_PARTNER;
			
		} catch (err) {
		}

		/**
		 * INFO: is obsolete // Step 23: // if special partner type via customer
		 * group and no Ship-To Party Master Data in Excise Duties and // no
		 * taxation for special partner, then origin = 'taxfree partner',
		 * otherwise origin = 'taxed partner' try {
		 * 
		 * return sldi.KNVV.ED963.ED961.EDTAXATION.equals("X") ?
		 * ORIGIN_TAXED_PARTNER : ORIGIN_TAXFREE_PARTNER; } catch
		 * (NullPointerException ex) { }
		 */

		// Step 24:
		// if ship-to-party has export indicator, then origin = 'third country
		// export'
		try {

			if (sldi.ShipToMasterExtension.thirdCountryIndicator === 'X') {
				return ORIGIN_THIRD_COUNTRY_EXPORT;
			}

		} catch (err) {
		}

		// Step 25:
		// if ship-to-party is registered consignee and KNA1 counry equal to tax
		// warehouse country, then origin = 'tax
		// warehouse', otherwise origin = 'third country'
		try {

			if (sldi.ShipToMasterExtension.registeredConsignee === 'X') {
				return (sldi.ShipToMasterExtension.customerData.countryKey === sldi.TaxWarehouse.country.countryKey) ? ORIGIN_TAX_WAREHOUSE : ORIGIN_THIRD_COUNTRY;
			}

		} catch (err) {
		}

		// Step 26:
		// if External Excise Duty Warehouse Number or External Excise Duty
		// Number
		// and KNA1 counry equal to tax
		// warehouse country, then origin = 'taxed', otherwise origin = 'third
		// country'
		try {

			if (sldi.TaxWarehouse.taxWarehouseRegistration === sldi.ShipToMasterExtension.externalTaxWarehouseRegistration) {
				return (sldi.TaxWarehouse.country.countryKey === sldi.ShipToMasterExtension.customerData.countryKey) ? ORIGIN_TAXED : ORIGIN_THIRD_COUNTRY;
			}

		} catch (err) {
		}

		// Step 17:
		// set origin to 'taxed' as default and country of ship-to-party
		return ORIGIN_TAXED;

	};

	var getOriginEU = function (sldi) {
		var origin = getOriginCommon(sldi);
		// TBD
		return origin;
	};

	var createStockLedger = function(origin, group, division, subdivision) {
		var stockLedger = {
			group : null,
			division : null,
			subdivision : null
		};
		stockLedger.group = group;
		stockLedger.division = division;
		stockLedger.subdivision = subdivision;
		return stockLedger;
	};

	var deriveStockLedger = function(sldi) {

		var origin = getOriginEU(sldi);

		switch (origin) {

		case TO_BE_CLARIFIED_ORIGIN_GLEICHES_SLAGER: // TODO: clarify
			return createStockLedger(origin, "02070", "3", "d");

		case TO_BE_CLARIFIED_ORIGIN_SL_HERSTELLUNG:// TODO: clarify
			return createStockLedger(origin, "02080", "3", "d");

		case TO_BE_CLARIFIED_ORIGIN_SL_FERTIGUNG:// TODO: clarify
			return createStockLedger(origin, "02090", "3", "d");

		case ORIGIN_TAXED:
			return createStockLedger(origin, "02010", "2", " ");

		case ORIGIN_TAX_WAREHOUSE:
			return createStockLedger(origin, "02040", "3", "a");

		case ORIGIN_THIRD_COUNTRY:
			return createStockLedger(origin, "02030", "3", "c");

		case ORIGIN_TAXED_PARTNER:
			return createStockLedger(origin, "02015", "2", " ");

		case TO_BE_CLARIFIED_ORIGIN_FREITRUNK:// TODO: clarify
			return createStockLedger(origin, "02015", "2", " ");

		case ORIGIN_TAXFREE_PARTNER:
			return createStockLedger(origin, "02016", "3", "c");

		case TO_BE_CLARIFIED_ORIGIN_ARMEE:// TODO: clarify
			return createStockLedger(origin, "03050", "3", "c");

		case TO_BE_CLARIFIED_ORIGIN_KUNDE_SONST:// TODO: clarify
			return createStockLedger(origin, "03080", "3", "c");

		case TO_BE_CLARIFIED_ORIGIN_HAUSTRUNK:// TODO: clarify
			return createStockLedger(origin, "03010", "2", " ");

			// special cases EU
		case ORIGIN_TAX_WAREHOUSE_EU:
			return createStockLedger(origin, "02060", "3", "b");

		case ORIGIN_EU_THIRD_COUNTRY:
			return createStockLedger(origin, "02020", "3", "c");

		case ORIGIN_EU:
			return createStockLedger(origin, "02000", "2", " ");

		default:
			return createStockLedger(origin, "xxx", "x", "x");
			// TODO: define a result which triggers the 'classical' exit:
			// PERFORM ungeplante_herkunft USING origin
		}

		return origin;
	};


	// entry point
	var sldi = input; //JSON.parse(input);
	var stockLedger = deriveStockLedger(sldi);
 
	sldi.StockLedgerLineItem.stockLedgerGroupId = stockLedger.group;
	sldi.StockLedgerLineItem.stockLedgerDivision = stockLedger.division;
	sldi.StockLedgerLineItem.stockLedgerSubdivision = stockLedger.subdivision;
	
	var output = sldi.StockLedgerLineItem; //JSON.stringify(sldi);
	return output;
}



module.exports = { 
		
    handler: function (event, context) { 
        console.log('event data ' + JSON.stringify(event.data)); 
        console.log('event type ' + event['event-type']); 
        console.log('event time ' + event['event-time']); 
        
        return doStockLedgerDetermination(event.data);
    }
}