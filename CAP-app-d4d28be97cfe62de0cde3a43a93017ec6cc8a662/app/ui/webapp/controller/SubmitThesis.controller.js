sap.ui.define([
	'sap/ui/core/mvc/Controller',
	'sap/ui/model/json/JSONModel',
    "sap/m/MessageBox",
    "sap/ui/model/Filter"
], function (Controller, JSONModel, MessageBox, Filter) {
	"use strict";

	return Controller.extend("ui.controller.SubmitThesis", {
		onInit: function () {
            
            this.oRouter = this.getOwnerComponent().getRouter();
			this._wizard = this.byId("ThesisSubmitWizard");
			this._oNavContainer = this.byId("wizardNavContainer");
			this._oWizardContentPage = this.byId("wizardContentPage");

			var siteModel = new JSONModel({
                    "editType": "",
                    "selectedThesis": ""
                }),
                formModel = new JSONModel({
                    "thesisText": " "
                });
            this.getView().setModel(siteModel, "Submit");
            this.getView().setModel(formModel, "Form");

            this.oUserInfoModel = this.getOwnerComponent().getModel("UserInfo");
            this.oUserAttributesModel = this.getOwnerComponent().getModel("UserAttributes");
            
            this.getView().addEventDelegate({
                onBeforeShow: function(oEvent) {
                    this.getView().setBusy(true);
                }.bind(this),
                onAfterShow: function(oEvent){
                    if(this.getOwnerComponent().getModel("UserInfo").getProperty("/familyName") === undefined || this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank") === undefined) {
                        this.oRouter.navTo("authFail", { bReplace : true});
                        return;
                    }
                    if(this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] != "Student") {
                        this.oRouter.navTo("permissionFail");
                        return;
                    }
                    this.getView().setBusy(false);
                    this._bindItemsToSelect();
                }.bind(this)
            },  this.getView());
        },

        onCheckReceivedData: function () {
            var oThesisSelect = this.byId("thesisSelect"),
                oView = this.getView(),
                oModel = oView.getModel(),
                oSubmitModel = oView.getModel("Submit"),
                prefix = this.oUserInfoModel.getProperty("/neptun");
            if(oThesisSelect.getSelectedItem().getKey() === null) {
                this.byId("noApplyFoundStrip").setVisible(true);
                return;
            }
            var binding = oModel.bindContext("/Students2Theses?$filter=student_neptun eq '" + prefix + "' and thesis_ID eq " + oThesisSelect.getSelectedItem().getKey() + "&$expand=thesis($select=archived)");
            binding.requestObject().then(function (result) {
                if (result.value.length < 1) {
                    this._wizard.invalidateStep(this.byId("SelectThesisStep"));
                    this.byId("noApplyFoundStrip").setVisible(true);
                } else {
                    var deadlineBinding = oModel.bindContext("/Courses(" + this.oUserAttributesModel.getProperty("/Course")[0] + ")/deadline");
                    deadlineBinding.requestObject().then(function (result2) {
                        if(result2.value != null) {
                            if(new Date(result2.value).getTime() < new Date().getTime()) {
                                this.byId("deadlineExpired").setVisible(true);
                                this.byId("noApplyFoundStrip").setVisible(false);
                                this.byId("archivedThesis").setVisible(false);
                                this._wizard.invalidateStep(this.byId("SelectThesisStep"));
                            } else {
                                if(result.value[0].thesis.archived === true) {
                                    this.byId("deadlineExpired").setVisible(false);
                                    this.byId("noApplyFoundStrip").setVisible(false);
                                    this.byId("archivedThesis").setVisible(true);
                                    this._wizard.invalidateStep(this.byId("SelectThesisStep"));
                                } else {
                                    oSubmitModel.setProperty("/selectedThesis", result.value[0].thesis_ID);
                                    oSubmitModel.refresh();
                                    this._wizard.validateStep(this.byId("SelectThesisStep"));
                                    this.byId("noApplyFoundStrip").setVisible(false);
                                    this.byId("deadlineExpired").setVisible(false);
                                    this.byId("archivedThesis").setVisible(false);
                                }
                            }
                        }
                    }.bind(this));
                }
            }.bind(this));
        },

        onEditTypeSelected: function(oEvent) {
            var oEditTypeButton = this.byId("editTypeSelect"),
                oGetThesisButton = this.byId("getThesisBtn"),
                oView = this.getView(),
                oModel = oView.getModel(),
                oSubmitModel = oView.getModel("Submit"),
                oEditTypeID = oEditTypeButton.getSelectedItem(),
                oEditType = sap.ui.core.Element.registry.get(oEditTypeID).getKey(),
                thesis_ID = oSubmitModel.getProperty("/selectedThesis");
            if(thesis_ID === undefined || thesis_ID.length < 3) {
                this._handleNavigationToStep(0);
                this._wizard.invalidateStep(this.byId("SelectThesisStep"));
                return;
            }
            var binding = oModel.bindContext("/ThesisText?$filter=thesis_ID eq " + thesis_ID);

            oGetThesisButton.setEnabled(false);
            binding.requestObject().then(function (result) {
                if(result.value.length < 1 && oEditType === "edit") {
                    MessageBox.alert(this._getText("WizardEditTypeError"), {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                    this._wizard.invalidateStep(this.byId("SelectEditType"));
                } else {
                    oSubmitModel.setProperty("/editValue", oEditType);
                    this._wizard.validateStep(this.byId("SelectEditType"));
                }
            }.bind(this));
        },

        bindDataToForm: function () {
            var oEditTypeButton = this.byId("editTypeSelect"),
                oView = this.getView(),
                oModel = oView.getModel(),
                oFormModel = oView.getModel("Form"),
                oSubmitModel = oView.getModel("Submit"),
                oForm = this.byId("inputForm"),
                thesis_ID = oSubmitModel.getProperty("/selectedThesis"),
                binding = oModel.bindContext("/Theses(" + oSubmitModel.getProperty("/selectedThesis") + ")?$expand=students($expand=student($expand=faculty,course)),teachers($expand=teacher($expand=faculty)),faculty");
            
            oEditTypeButton.setEnabled(false);
            
            binding.requestObject().then(function (result) {
                oFormModel.setData(result);
                var textBinding = oModel.bindContext("/ThesisText?$filter=thesis_ID eq " + thesis_ID);
                textBinding.requestObject().then(function (result2) {
                    if(oSubmitModel.getProperty("/editValue") === "edit" && result2.value.length > 0) {
                        oFormModel.setProperty("/thesisText", result2.value[0].text);
                    } else {
                        oFormModel.setProperty("/thesisText", " ");
                    }
                }.bind(this));
            }.bind(this));
        },

		wizardCompletedHandler: function () {
			this._oNavContainer.to(this.byId("wizardReviewPage"));
		},

		backToWizardContent: function () {
			this._oNavContainer.backToPage(this._oWizardContentPage.getId());
		},

		editThesisText: function () {
			this._handleNavigationToStep(3);
		},
        
		onWizardCancel: function () {
			MessageBox.warning(this._getText("WizardFinalConfirmCancel"), {
                actions: [MessageBox.Action.YES, MessageBox.Action.NO],
                onClose: function (oAction) {
					if (oAction === MessageBox.Action.YES) {
						this._discardWizardProgress();
					}
				}.bind(this)
            });
		},

		onWizardSubmit: function () {
			MessageBox.confirm(this._getText("WizardFinalConfirmSubmit"), {
                actions: [MessageBox.Action.YES, MessageBox.Action.NO],
                onClose: function (oAction) {
					if (oAction === MessageBox.Action.YES) {
                        var oView = this.getView(),
                            oModel = oView.getModel(),
                            oFormModel = oView.getModel("Form"),
                            oSubmitModel = oView.getModel("Submit"),
                            oText = oFormModel.getProperty("/thesisText"),
                            thesis_ID = oSubmitModel.getProperty("/selectedThesis"),
                            binding = oModel.bindList("/ThesisText", undefined, [], new Filter("thesis_ID", sap.ui.model.FilterOperator.EQ, thesis_ID)),
                            that = this;
                        
                        binding.requestContexts().then(function (aContexts) {
                            var alreadyExists = false;
                            aContexts.forEach(function (oContext) {
                                if(oContext.getProperty("thesis_ID") === thesis_ID) {
                                    alreadyExists = true;
                                    oContext.setProperty("text", oText).then(function () {
                                        oModel.refresh();
                                        MessageBox.alert(this._getText("WizardFinalSuccessMessage"),
                                            {icon : MessageBox.Icon.SUCCESS, title : this._getText("appSuccessMsgTitle")});
                                    }.bind(this), function (oError) {
                                        MessageBox.alert(this._getText("WizardFinalErrorMessage") + " " + oError.message, 
                                            {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                                    }.bind(this));
                                    oModel.refresh();
                                    return;
                                }
                            }.bind(this));
                            if(alreadyExists === false) {
                                var oNewContext = binding.create({
                                    "text" : oText,
                                    "thesis_ID" : thesis_ID
                                });
                                oNewContext.created().then(function () {
                                    oModel.refresh();
                                    MessageBox.alert(this._getText("WizardFinalSuccessMessage"),
                                        {icon : MessageBox.Icon.SUCCESS, title : this._getText("appSuccessMsgTitle")});
                                }.bind(this), function (oError) {
                                    MessageBox.alert(this._getText("WizardFinalErrorMessage") + " " + oError.message, 
                                        {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                                }.bind(this));
                            }
                        }.bind(this), function(oError) {
                            MessageBox.alert(this._getText("appDatabaseErrorMessageText") + " " + oError.message, 
                                {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                        }.bind(this));
                        this._discardWizardProgress();
					}
				}.bind(this)
            });
        },
        
        _bindItemsToSelect: function() {
            this.byId("noApplyFoundStrip").setVisible(false);
            var prefix = this.oUserInfoModel.getProperty("/neptun");
            var oThesisSelect = this.byId("thesisSelect");
            oThesisSelect.bindItems({
                path: "/Students2Theses",
                template: new sap.ui.core.Item({
                    text: "{thesis/title}",
                    key: "{thesis/ID}"
                }),
                templateShareable: false,
                filters: [
                    new Filter("student_neptun", "EQ", prefix)
                ],
                parameters: {
                    $expand: 'thesis'
                }
            });
        },

		_discardWizardProgress: function () {
            this._wizard.discardProgress(this._wizard.getSteps()[0]);
            this._handleNavigationToStep(0);
            this._wizard.invalidateStep(this.byId("SelectThesisStep"));

            this.byId("editTypeSelect").setEnabled(true);
            this.byId("getThesisBtn").setEnabled(true);
            this.byId("noApplyFoundStrip").setVisible(false);
            this.getView().getModel("Form").setProperty("/thesisText", " ");
            this.getView().getModel("Submit").setProperty("/editType", " ");
            this.getView().getModel("Submit").setProperty("/selectedThesis", " ");
            
        },

        _handleNavigationToStep: function (iStepNumber) {
			var fnAfterNavigate = function () {
				this._wizard.goToStep(this._wizard.getSteps()[iStepNumber]);
				this._oNavContainer.detachAfterNavigate(fnAfterNavigate);
			}.bind(this);

			this._oNavContainer.attachAfterNavigate(fnAfterNavigate);
			this.backToWizardContent();
        },
        
        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        },
	});
});
