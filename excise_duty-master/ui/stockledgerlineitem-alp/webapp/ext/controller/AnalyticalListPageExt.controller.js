sap.ui.controller("com.sap.icd.edpoc.ExciseDutyStockLedgerLineItemALP.ext.controller.AnalyticalListPageExt", {
	
	onInit : function() {
		'use strict';
	},
	
	onBeforeRendering: function() {
		'use strict';
		var sTable = this.getView().byId("com.sap.icd.edpoc.ExciseDutyStockLedgerLineItemALP::sap.suite.ui.generic.template.AnalyticalListPage.view.AnalyticalListPage::StockLedgerLineItem--table");
		sTable.setUseExportToExcel(true);
		//To get an xls rendering on client side use the export type UI5Client
		sTable.setExportType("UI5Client");
	},
	
	onClickActionB_requiresSelection: function(oCustomData) {
		'use strict';
		var table = this.getView().byId("com.sap.icd.edpoc.ExciseDutyStockLedgerLineItemALP::sap.suite.ui.generic.template.AnalyticalListPage.view.AnalyticalListPage::StockLedgerLineItem--table").getTable();
		var selectedIndex = table.getSelectedIndex();
		var context = table.getContextByIndex(selectedIndex);
		var model = table.getModel();
		var selectedData = model.getData(context.getPath());
		
		var url = "https://uyr900-er8070.wdf.sap.corp/sap/bc/ui5_ui5/ui2/ushell/shells/abap/FioriLaunchpad.html?sap-client=900&sap-language=EN#Material-display?Material="+encodeURIComponent(selectedData.materialNumber)+"&sap-ui-tech-hint=GUI";
		var win = window.open(url, '_blank');
		win.focus();
	}
});