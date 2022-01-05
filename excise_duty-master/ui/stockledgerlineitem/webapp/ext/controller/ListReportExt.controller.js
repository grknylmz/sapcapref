sap.ui.controller("com.sap.icd.edpoc.ExciseDutyStockLedgerLineItem.ext.controller.ListReportExt", {
	
	onInit : function() {
		'use strict';
	},
	
	onClickActionB_requiresSelection: function(oCustomData) {
		'use strict';
		var table = this.getView().byId("com.sap.icd.edpoc.ExciseDutyStockLedgerLineItem::sap.suite.ui.generic.template.ListReport.view.ListReport::StockLedgerLineItem--listReport").getTable();
		var selectedIndex = table.getSelectedIndex();
		var context = table.getContextByIndex(selectedIndex);
		var model = table.getModel();
		var selectedData = model.getData(context.getPath());
		
		var url = "https://uyr900-er8070.wdf.sap.corp/sap/bc/ui5_ui5/ui2/ushell/shells/abap/FioriLaunchpad.html?sap-client=900&sap-language=EN#Material-display?Material="+encodeURIComponent(selectedData.materialNumber)+"&sap-ui-tech-hint=GUI";
		var win = window.open(url, '_blank');
		win.focus();
	}
	
});