sap.ui.controller("com.sap.icd.edpoc.ExciseDutyConfigTaxWarehouse.ext.controller.ListReportExt", {
	
	onInit : function() {
		'use strict';
	},
	
	onBeforeRendering: function() {
		'use strict';
	},
	
	onClickActionB_requiresSelection: function(oCustomData) {
		'use strict';
		var table = this.getView().byId("com.sap.icd.edpoc.ExciseDutyConfigTaxWarehouse::sap.suite.ui.generic.template.ListReport.view.ListReport::TaxWarehouse--listReport").getTable();
		var item = table.getSelectedItem();
		var context = item.getBindingContext();
		var model = table.getModel();
		var selectedData = model.getData(context.getPath());

		var date = new Date();
		var firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1);
		var lastDayOfMonth = new Date(date.getFullYear(), date.getMonth() + 1, 0);

		var firstDayOfMonthString = firstDayOfMonth.getFullYear() + '-' + ("0" + (firstDayOfMonth.getMonth() + 1)).slice(-2) + '-' + ("0" + (firstDayOfMonth.getDate())).slice(-2);
		var lastDayOfMonthString = lastDayOfMonth.getFullYear() + '-' + ("0" + (lastDayOfMonth.getMonth() + 1)).slice(-2) + '-' + ("0" + (lastDayOfMonth.getDate())).slice(-2);
		
		var url = "https://documentrenderer.cfapps.sap.hana.ondemand.com/renderDocument?taxWarehouseRegistration="+encodeURIComponent(selectedData.taxWarehouseRegistration)+"&fromDate="+firstDayOfMonthString+"&toDate="+lastDayOfMonthString;
		var win = window.open(url, '_blank');
		win.focus();
	}
	
});