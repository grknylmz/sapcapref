sap.ui.define([
		"sap/ui/core/mvc/Controller",
        "sap/ui/core/library",
	], function(Controller, coreLib) {
	"use strict";

	return Controller.extend("ui.controller.ViewText", {

		onInit: function (oEvent) {
            var oVizFrame = this.getView().byId("vizFrame");
            oVizFrame.setVizProperties({
                title: {
                    visible: false
                }
            });
            var oPopover = this.getView().byId("chartPopover");
            oPopover.connect(oVizFrame.getVizUid());

            this.getView().addEventDelegate({
                onBeforeShow: function(oEvent) {
                    this.getView().setBusy(true);
                }.bind(this),
                onAfterShow: function(oEvent){
                    if(this.getOwnerComponent().getModel("UserInfo").getProperty("/familyName") === undefined || this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank") === undefined) {
                        this.getOwnerComponent().getRouter().navTo("authFail", { bReplace : true});
                        return;
                    }
                    if(this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Admin") {
                            this.getView().setBusy(false);
                    } else {
                        this.getOwnerComponent().getRouter().navTo("permissionFail");
                        return;
                    }
                    
                }.bind(this)
            },  this.getView());
        },

        onQueryButtonPress: function(oEvent, sType) {
            var date = this.byId(sType + "QueryDatePicker").getValue();
            var operator = this.byId(sType + "sSelect").getSelectedKey();
            if(date != null && date != undefined && this.byId(sType + "QueryDatePicker").isValidValue() === true) {
                var queryPath = date === "" ? `/${sType}sStats()` : `/${sType}sStatsWithDate(date=${date},operator='${operator}')`;
                if(sType === "Student") {
                    this.byId(sType + "sTable").bindItems({
                        path: queryPath,
                        template: new sap.m.ColumnListItem({
                            type : sap.m.ListType.Inactive,
                            cells: [
                                new sap.m.Text({text: "{LAST_NAME} {FIRST_NAME} ({NEPTUN})"}),
                                new sap.m.Text({text: "{VALUE}"})
                            ]
                        }),
                        templateShareable: false
                    });
                } else if(sType === "Teacher") {
                    var oVizFrame = this.getView().byId("vizFrame");
                    oVizFrame.setDataset(new sap.viz.ui5.data.FlattenedDataset({
                        dimensions : [{
                            name: "Oktatói NEPTUN",
                            value: "{NEPTUN}"
                        }],
                        measures : [{
                            name: 'Kapcsolódó témák száma',
                            value: '{VALUE}'
                        }],
                        data : {
                            path: queryPath
                        }})
                    );
                }
            }
        },

        onDatePickerChange: function(oEvent) {
            var bValid = oEvent.getParameter("valid");
            if(bValid) {
                oEvent.getSource().setValueState(coreLib.ValueState.None);
            } else {
                oEvent.getSource().setValueState(coreLib.ValueState.Error);
                oEvent.getSource().setValueStateText(this._getText("appEnterValidValue"));
            }
        },

        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        },
	});
});