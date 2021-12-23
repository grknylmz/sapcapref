sap.ui.define([
	"sap/ui/core/mvc/Controller",
	'sap/ui/core/Fragment',
    "ui/libs/uuid",
    "sap/m/MessageBox"
], function (Controller) {
	"use strict";

	return Controller.extend("ui.controller.EditDeadlines", {

		onInit : function () {
            this.oRouter = this.getOwnerComponent().getRouter();
            this.oView = this.getView();
            this.getView().addEventDelegate({
                onBeforeShow: function(oEvent) {
                    this.oView.setBusy(true);
                }.bind(this),
                onAfterShow: function(oEvent){
                    if(this.getOwnerComponent().getModel("UserInfo").getProperty("/familyName") === undefined || this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank") === undefined) {
                        this.oRouter.navTo("authFail", { bReplace : true});
                        return;
                    }
                    if(this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Admin") {
                        this.oView.setBusy(false);
                    } else {
                        this.oRouter.navTo("permissionFail");
                        return;
                    }
                }.bind(this)
            },  this.oView);
        }

	});
});