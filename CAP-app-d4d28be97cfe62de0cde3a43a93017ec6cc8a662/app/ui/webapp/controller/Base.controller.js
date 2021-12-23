sap.ui.define([
	"sap/ui/core/mvc/Controller",
	'sap/f/library'
], function (Controller, fioriLibrary) {
	"use strict";

	return Controller.extend("ui.controller.Base", {

		onInit: function () {
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
            oRouter.getRoute("base").attachPatternMatched(this._onObjectMatched, this);
		},
		
		_onObjectMatched: function () {
            this.getView().byId("fcl").setLayout(fioriLibrary.LayoutType.OneColumn);
		}
	});

});