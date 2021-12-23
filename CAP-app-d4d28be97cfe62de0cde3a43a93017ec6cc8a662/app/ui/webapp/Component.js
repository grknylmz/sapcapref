sap.ui.define([
	"sap/ui/core/UIComponent",
	"sap/ui/Device",
	"./model/models",
    "./controller/ErrorHandler",
    "sap/ui/model/json/JSONModel"
], function (UIComponent, Device, models, ErrorHandler, JSONModel) {
	"use strict";

	return UIComponent.extend("ui.Component", {

		metadata : {
			manifest: "json"
		},

		init : function () {
			UIComponent.prototype.init.apply(this, arguments);

			this._oErrorHandler = new ErrorHandler(this);
            this.setModel(models.createDeviceModel(), "device");
            var oUserInfoModel = new JSONModel();
            var oUserAttribModel = new JSONModel();
            this.setModel(oUserInfoModel, "UserInfo");
            this.setModel(oUserAttribModel, "UserAttributes");
            oUserInfoModel.loadData("/user/userinfo");
            oUserAttribModel.loadData("/user/userattributes");
			this.getRouter().initialize();
		},

		destroy : function () {
			this._oErrorHandler.destroy();
			UIComponent.prototype.destroy.apply(this, arguments);
		},

		getContentDensityClass : function() {
			if (this._sContentDensityClass === undefined) {
				if (document.body.classList.contains("sapUiSizeCozy") || document.body.classList.contains("sapUiSizeCompact")) {
					this._sContentDensityClass = "";
				} else if (!Device.support.touch) {
					this._sContentDensityClass = "sapUiSizeCompact";
				} else {
					this._sContentDensityClass = "sapUiSizeCozy";
				}
			}
			return this._sContentDensityClass;
		}

	});

});
