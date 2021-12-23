sap.ui.define([
	"sap/ui/core/mvc/Controller",
	'sap/f/library',
    'sap/ui/model/Filter',
    "sap/ui/core/library",
    'sap/ui/core/Fragment',
], function (Controller, fioriLibrary, Filter, coreLib, Fragment) {
	"use strict";

	return Controller.extend("ui.controller.Master", {

		onInit: function () {
			this.oRouter = this.getOwnerComponent().getRouter();
            this.oView = this.getView();
            this.byId("teacherComboBox").setFilterFunction(function(sTerm, oItem) {
				return oItem.getText().match(new RegExp(sTerm, "i")) || oItem.getKey().match(new RegExp(sTerm, "i"));
			});
            this.getView().addEventDelegate({
                onBeforeShow: function(oEvent) {
                    this.oView.setBusy(true);
                }.bind(this),
                onAfterShow: function(oEvent){
                    if(this.getOwnerComponent().getModel("UserInfo").getProperty("/familyName") === undefined || this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank") === undefined) {
                        this.oRouter.navTo("authFail", { bReplace : true});
                        return;
                    }
                    if(this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Student" ||
                        this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Teacher") {
                            this.oView.setBusy(false);
                    } else {
                        this.oRouter.navTo("permissionFail");
                        return;
                    }
                }.bind(this)
            },  this.oView);
		},

		onListItemPress: function (oEvent) {
			var oFCL = this.oView.getParent().getParent();
			oFCL.setLayout(fioriLibrary.LayoutType.TwoColumnsMidExpanded);

			var oBindingContext = oEvent.getSource().getBindingContext();
			this.oRouter.navTo("detail", {
				id: oBindingContext.getProperty("ID")
			});

        },
        
        onComboBoxChange: function (oEvent) {
			var oValidatedComboBox = oEvent.getSource(),
				sSelectedKey = oValidatedComboBox.getSelectedKey(),
				sValue = oValidatedComboBox.getValue();

			if (!sSelectedKey && sValue) {
				oValidatedComboBox.setValueState(coreLib.ValueState.Error);
                oValidatedComboBox.setValueStateText(this._getText("appEnterValidValue"));
                this.byId("thesisFilterBar").setShowGoOnFB(false);
			} else {
                oValidatedComboBox.setValueState(coreLib.ValueState.None);
                this.byId("thesisFilterBar").setShowGoOnFB(true);
			}
        },

        onDatePickerChange: function(oEvent) {
            var bValid = oEvent.getParameter("valid");
            if(bValid) {
                oEvent.getSource().setValueState(coreLib.ValueState.None);
                this.byId("thesisFilterBar").setShowGoOnFB(true);
            } else {
                oEvent.getSource().setValueState(coreLib.ValueState.Error);
                oEvent.getSource().setValueStateText(this._getText("appEnterValidValue"));
                this.byId("thesisFilterBar").setShowGoOnFB(false);
            }
        },

        onReset: function (oEvent) {
			var oItems = this.byId("thesisFilterBar").getAllFilterItems(true);
			for (var i = 0; i < oItems.length; i++) {
				var oControl = this.byId("thesisFilterBar").determineControlByFilterItem(oItems[i]);
				if (oControl) {
					oControl.setValue("");
				}
            }
            this.byId("thesisTags").destroyTokens();
            this.byId("openSelect").setSelectedKey("None");
		},

        onSearch: function (oEvent) {
            var oItems = this.byId("thesisFilterBar").getAllFilterItems(true),
                oFilters = [];
			for (var i = 0; i < oItems.length; i++) {
                var oControl = this.byId("thesisFilterBar").determineControlByFilterItem(oItems[i]);
				if (oControl) {
                    var sType = oControl.getMetadata().getName();
                    switch(sType) {
                        case "sap.m.Input":
                        case "sap.m.DateTimePicker":
                            var oValue = oControl.getValue();
                            if(oValue.length > 0) {
                                if(oItems[i].getName() === "Date") {
                                    oFilters.push(new Filter({
                                        path: "fromdate",
                                        operator: sap.ui.model.FilterOperator.LT,
                                        value1: oValue
                                    }));
                                    oFilters.push(new Filter({
                                        path: "todate",
                                        operator: sap.ui.model.FilterOperator.GT,
                                        value1: oValue
                                    }));
                                }
                                if(oItems[i].getName() === "Title") {
                                    oFilters.push(new Filter({
                                        path: "title",
                                        operator: sap.ui.model.FilterOperator.Contains,
                                        value1: oValue,
                                        caseSensitive: false
                                    }));
                                }
                            }
                            break;
                        case "sap.m.MultiInput":
                            if(oItems[i].getName() === "Tag") {
                                var oTokens = oControl.getTokens();
                                if(oTokens.length < 1) break;
                                oTokens.forEach(function (oToken) {
                                    oFilters.push(new Filter({
                                        path : "tags",
                                        operator : sap.ui.model.FilterOperator.Any,    
                                        variable : "tag",
                                        condition : new sap.ui.model.Filter("tag/tag_ID", sap.ui.model.FilterOperator.EQ, oToken.getKey())  
                                    }));
                                }.bind(this));
                                break;
                            }
                        default: 
                            var oValue = oControl.getSelectedKey();
                            if(!oValue) break;
                            if(oItems[i].getName() === "Teacher") {
                                oFilters.push(new Filter({
                                    path: "teachers",
                                    operator : sap.ui.model.FilterOperator.Any,    
                                    variable : "teacher",
                                    condition : new sap.ui.model.Filter("teacher/teacher_neptun", sap.ui.model.FilterOperator.EQ, oValue)  
                                }));
                            }
                            if(oItems[i].getName() === "Course") {
                                oFilters.push(new Filter({
                                    path: "courses",
                                    operator : sap.ui.model.FilterOperator.Any,    
                                    variable : "course",
                                    condition : new sap.ui.model.Filter("course/course_ID", sap.ui.model.FilterOperator.EQ, oValue)  
                                }));
                            }
                            if(oItems[i].getName() === "Open" && oValue != "None") {
                                oFilters.push(new Filter({
                                    path: "open",
                                    operator : sap.ui.model.FilterOperator.EQ,    
                                    value1: oValue
                                }));
                            }
                            if(oItems[i].getName() === "Faculty") {
                                oFilters.push(new Filter({
                                    path: "faculty_ID",
                                    operator : sap.ui.model.FilterOperator.EQ,    
                                    value1: oValue
                                }));
                            }
                            break;
                    }
				}
            }
            var list = this.getView().byId("thesesTable");
			var binding = list.getBinding("items");
			binding.filter(oFilters, "Application");
        },
        handleValueHelp: function (oEvent) {
			var sInputValue = oEvent.getSource().getValue(),
				oView = this.getView();

			if (!this._pValueHelpDialog) {
				this._pValueHelpDialog = Fragment.load({
					id: oView.getId(),
					name: "ui.fragment.SearchValueHelpDialog",
					controller: this
				}).then(function (oValueHelpDialog) {
					oView.addDependent(oValueHelpDialog);
					return oValueHelpDialog;
				});
			}

			this._pValueHelpDialog.then(function (oValueHelpDialog) {
				oValueHelpDialog.getBinding("items").filter([new Filter(
					"name",
					sap.ui.model.FilterOperator.Contains,
					sInputValue
				)], "Application");
				oValueHelpDialog.open(sInputValue);
			});
		},

		_handleValueHelpSearch: function (evt) {
			var sValue = evt.getParameter("value");
			var oFilter = new Filter(
				"name",
				sap.ui.model.FilterOperator.Contains,
				sValue
			);
			evt.getSource().getBinding("items").filter([oFilter]);
		},

		_handleValueHelpClose: function (evt) {
			var aSelectedItems = evt.getParameter("selectedItems"),
				oMultiInput = this.byId("thesisTags");

			if (aSelectedItems && aSelectedItems.length > 0) {
				aSelectedItems.forEach(function (oItem) {
					oMultiInput.addToken(new sap.m.Token({
                        text: oItem.getTitle(),
                        key: oItem.getCustomData()[0].getKey()
					}));
				});
			}
		},
        
        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        },
	});

});