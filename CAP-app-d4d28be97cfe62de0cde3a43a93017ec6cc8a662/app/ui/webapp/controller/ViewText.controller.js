sap.ui.define([
		'sap/ui/core/mvc/Controller',
        'sap/ui/model/json/JSONModel',
        "sap/ui/model/Filter",
        "sap/m/MessageBox",
        "ui/libs/uuid"
	], function(Controller, JSONModel, Filter, MessageBox, uuid) {
	"use strict";

	return Controller.extend("ui.controller.ViewText", {

		onInit: function (oEvent) {
            var oTextModel = new JSONModel({
                "text": "",
                "modifiedBy": "",
                "modifiedAt": ""
            });
            this.getView().setModel(oTextModel, "Text");
            this.oUserInfoModel = this.getOwnerComponent().getModel("UserInfo");
            this.oUserAttributesModel = this.getOwnerComponent().getModel("UserAttributes");
            

            this.getView().addEventDelegate({
                onBeforeShow: function(oEvent) {
                    this.getView().setBusy(true);
                }.bind(this),
                onAfterShow: function(oEvent){
                    if(this.oUserInfoModel.getProperty("/familyName") === undefined || this.oUserAttributesModel.getProperty("/Rank") === undefined) {
                        this.getOwnerComponent().getRouter().navTo("authFail", { bReplace : true});
                        return;
                    }
                    if(this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Student" ||
                        this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Teacher") {
                            this.getView().setBusy(false);
                            this._bindItemsToMasterList();
                    } else {
                        this.getOwnerComponent().getRouter().navTo("permissionFail");
                        return;
                    }
                    
                }.bind(this)
            },  this.getView());

        },

        onBackToMasterPress: function() {
            this.byId("splitApp").toMaster(this.createId("masterPage"));
        },

        onListItemPress: function(oEvent) {
            this.byId("splitApp").toDetail(this.createId("detailPage"));
            var oBindingContext = oEvent.getSource().getBindingContext();
            this.getView().bindElement("/Theses(" + oBindingContext.getProperty("thesis_ID") + ")");
            var binding = this.getView().getModel().bindContext("/ThesisText?$filter=thesis_ID eq " + oBindingContext.getProperty("thesis_ID"));

            binding.requestObject().then(function (result) {
                this.getView().getModel("Text").setData(result.value[0]);
                if(result.value.length < 1) {
                    MessageBox.warning(this._getText("WizardEditTypeError"), {icon : MessageBox.Icon.WARNING, title : this._getText("appWarningMsgTitle")});
                }
            }.bind(this));
        },

        _bindItemsToMasterList: function() {
            var prefix = this.oUserInfoModel.getProperty("/neptun");
            var oThesisList = this.byId("thesisList");
            var rankList = this.oUserAttributesModel.getProperty("/Rank");
            var oPath = `/${rankList[0]}s2Theses`;
            var oParam = `${rankList[0].toLowerCase()}_neptun`;
            oThesisList.bindItems({
                path: oPath,
                template: new sap.m.StandardListItem({
                    title: "{thesis/title}",
                    type : sap.m.ListType.Active,
                    press: this.onListItemPress.bind(this)
                }),
                templateShareable: false,
                filters: [
                    new Filter(oParam, "EQ", prefix)
                ],
                parameters: {
                    $expand: 'thesis'
                }
            });
        },

        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        }
        
	});
});