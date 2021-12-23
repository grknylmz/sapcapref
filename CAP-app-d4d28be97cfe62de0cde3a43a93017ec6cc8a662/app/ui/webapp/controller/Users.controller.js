sap.ui.define([
		'sap/ui/core/mvc/Controller',
	], function(Controller) {
	"use strict";

	return Controller.extend("ui.controller.Users", {

		onInit: function (oEvent) {
            this.oRouter = this.getOwnerComponent().getRouter();
            this.oUserInfoModel = this.getOwnerComponent().getModel("UserInfo");

            this.getView().addEventDelegate({
                onBeforeShow: function(oEvent) {
                    this.getView().setBusy(true);
                }.bind(this),
                onAfterShow: function(oEvent){
                    if(this.oUserInfoModel.getProperty("/familyName") === undefined || this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank") === undefined) {
                        this.oRouter.navTo("authFail", { bReplace : true});
                        return;
                    }
                    if(this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Admin") {
                        this.getView().setBusy(false);
                    } else {
                        this.oRouter.navTo("permissionFail");
                        return;
                    }
                }.bind(this)
            },  this.getView());

        }
    });
});