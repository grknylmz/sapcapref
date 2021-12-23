sap.ui.define([
	"sap/ui/base/Object",
	"sap/m/MessageBox",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator"
], function (UI5Object, MessageBox, Filter, FilterOperator) {
	"use strict";

	return UI5Object.extend("ui.controller.ErrorHandler", {

		constructor : function (oComponent) {
			var	oMessageManager = sap.ui.getCore().getMessageManager(),
				oMessageModel = oMessageManager.getMessageModel(),
				oResourceBundle = oComponent.getModel("i18n").getResourceBundle(),
				sErrorText = oResourceBundle.getText("errorText"),
				sMultipleErrors = oResourceBundle.getText("multipleErrorsText");

			this._oComponent = oComponent;
			this._bMessageOpen = false;

			this.oMessageModelBinding = oMessageModel.bindList("/", undefined,
				[], new Filter("technical", FilterOperator.EQ, true));

			this.oMessageModelBinding.attachChange(function (oEvent) {
				var aContexts = oEvent.getSource().getContexts(),
					aMessages = [],
					sErrorTitle;

				if (this._bMessageOpen || !aContexts.length) {
					return;
				}

				aContexts.forEach(function (oContext) {
					aMessages.push(oContext.getObject());
				});
				oMessageManager.removeMessages(aMessages);

				sErrorTitle = aMessages.length === 1 ? sErrorText : sMultipleErrors;
				this._showServiceError(sErrorTitle, aMessages[0].message);
			}, this);
		},

		_showServiceError : function (sErrorTitle, sDetails) {
            if(sDetails.includes("Deserialization Error")) sErrorTitle = this._getText("ErrorTextLongUsername");
            else if(sDetails.includes("Bad Gateway")) sErrorTitle = this._getText("ErrorTextDatabaseError");
            else if(sDetails.includes("Gateway Timeout")) sErrorTitle = this._getText("ErrorTextDatabaseTimeoutError");
            else if(sDetails.includes("Not Found")) sErrorTitle = this._getText("ErrorTextNotFound");
            else if(sDetails.includes("Service Unavailable")) sErrorTitle = this._getText("ErrorTextServiceNotFound");
            else if(sDetails.includes("401")) sErrorTitle = this._getText("ErrorTextUnauthorized");
            else if(sDetails.includes("Must not change a property before it has been read")) return;
			this._bMessageOpen = true;
			MessageBox.error(
				sErrorTitle,
				{
					id : "serviceErrorMessageBox",
					details: sDetails,
					styleClass: this._oComponent.getContentDensityClass(),
					actions: [MessageBox.Action.CLOSE],
					onClose: function () {
						this._bMessageOpen = false;
					}.bind(this)
				}
			);
        },
        
        _getText: function(i18nStr) {
            return this._oComponent.getModel("i18n").getResourceBundle().getText(i18nStr);
        } 
	});
});