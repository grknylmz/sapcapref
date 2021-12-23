sap.ui.define([
	"sap/ui/core/mvc/Controller",
	'sap/ui/core/Fragment',
    "ui/libs/uuid",
    "sap/m/MessageBox"
], function (Controller, Fragment, uuid, MessageBox) {
	"use strict";

	return Controller.extend("ui.controller.FacultyCourseEdit", {

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
        },

        onListItemPress: function (oEvent, sType) {
            var oBindingContext = oEvent.getSource().getBindingContext();
            this.getView().bindElement("/" + sType + "(" + oBindingContext.getProperty("ID") + ")");
            this._showEditDialog(sType);
        },

        onCloseEditDialog: function (oEven, sType) {
            this.byId(sType + "EditDialog").close();
            this.getView().getModel().refresh();
        },

        onNewItemPress: function(oEvent, sType) {
            MessageBox.warning(this._getText("AddNew" + sType + "ConfirmText"), {
                actions: [MessageBox.Action.YES, MessageBox.Action.NO],
                onClose: function (oAction) {
					if (oAction === MessageBox.Action.YES) {
						this._addNewItem(sType);
					}
				}.bind(this)
            });
            
        },

        _showEditDialog: function(sType) {
            var oView = this.getView();
            if(sType === "Courses") {
                if (!this._editCourseFragment) {
                    this._editCourseFragment = Fragment.load({
                        id: oView.getId(),
                        name:"ui.fragment.EditCourseDialog",
                        controller: this
                    }).then(function (oValueHelpDialog) {
                        oView.addDependent(oValueHelpDialog);
                        return oValueHelpDialog;
                    });
                }
                this._editCourseFragment.then(function (oValueHelpDialog) {
                    oValueHelpDialog.open();
                });
            }
            if(sType === "Faculties") {
                if (!this._editFacultyFragment) {
                    this._editFacultyFragment = Fragment.load({
                        id: oView.getId(),
                        name: "ui.fragment.EditFacultyDialog",
                        controller: this
                    }).then(function (oValueHelpDialog) {
                        oView.addDependent(oValueHelpDialog);
                        return oValueHelpDialog;
                    });
                }
                this._editFacultyFragment.then(function (oValueHelpDialog) {
                    oValueHelpDialog.open();
                });
            }
        },

        _addNewItem: function(sType) {
            var oModel = this.getView().getModel(),
                binding = oModel.bindList(sType === "Course" ? "/Courses" : "/Faculties");
            binding.requestContexts().then(function(aContexts) {
                var data_ID = uuidv4(),
                    dataToInsert = {
                        "ID" : data_ID
                    };

                if(sType === "Course") {
                    $.extend(dataToInsert, {"name" : "Új szak",
                                            "faculty_ID" : null});
                }
                if(sType === "Faculty") {
                    $.extend(dataToInsert, {"name" : "Új kar"});
                }
                var oNewContext = binding.create(dataToInsert);
                oNewContext.created().then(function () {
                    oModel.refresh();
                    MessageBox.alert(this._getText("AddNew" + sType + "SuccessMessage"),
                        {icon : MessageBox.Icon.SUCCESS, 
                        title : this._getText("appSuccessMsgTitle"),
                        onClose: function (oAction) {
                            this.getView().bindElement((sType === "Course" ? "/Courses" : "/Faculties") + "(" + data_ID + ")");
                            this._showEditDialog(sType === "Course" ? "Courses" : "Faculties");
                        }.bind(this)});
                }.bind(this), function (oError) {
                    MessageBox.alert(this._getText("DetailApplyErrorMessage")
                        + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                }.bind(this));
            }.bind(this), function(oError) {
                MessageBox.alert(this._getText("appDatabaseErrorMessageText")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
            }.bind(this));
        },

        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        },

	});
});