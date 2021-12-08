sap.ui.define(["inicial/controller/BaseController",
	"sap/ui/core/mvc/Controller"
], function (BaseController,Controller) {
	"use strict";

	return BaseController.extend("inicial.controller.View1", {
		onInit: function () {
            this.getRouter().getRoute("RouteView1").attachPatternMatched(this._onObjectMatched, this);
        },
        
        		/**
		 * Binds the view to the object path.
		 * @function
		 * @param {sap.ui.base.Event} oEvent pattern match event in route 'object'
		 * @private
		 */
		_onObjectMatched: function(oEvent) {
			
			
			// this.getRouter().navTo("mappingmonitor/details", {
			// 	from: "Init",
			// 	entity: "mappingmonitor",
			// 	tab: null
			// });		
			
			
			this.getRouter().navTo("Gannt", {
				from: "Init",
				entity: "Gannt",
				tab: null
			});		
			
		}
	});
});