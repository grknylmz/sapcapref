sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/core/routing/History"
], function (Controller,History) {
    "use strict";

    return Controller.extend("inicial.controller.BaseController", {
        /**
         * Convenience method for accessing the router.
         * @public
         * @returns {sap.ui.core.routing.Router} the router for this component
         */
        getRouter : function () {
            return sap.ui.core.UIComponent.getRouterFor(this);
        },

        /**
         * Convenience method for getting the view model by name.
         * @public
         * @param {string} [sName] the model name
         * @returns {sap.ui.model.Model} the model instance
         */
        getModel : function (sName) {
            return this.getView().getModel(sName);
        },

        /**
         * Convenience method for setting the view model.
         * @public
         * @param {sap.ui.model.Model} oModel the model instance
         * @param {string} sName the model name
         * @returns {sap.ui.mvc.View} the view instance
         */
        setModel : function (oModel, sName) {
            return this.getView().setModel(oModel, sName);
        },

        /**
         * Getter for the resource bundle.
         * @public
         * @returns {sap.ui.model.resource.ResourceModel} the resourceModel of the component
         */
        getResourceBundle : function () {
            return this.getOwnerComponent().getModel("i18n").getResourceBundle();
        },

        pressLogout: function(evt) {
            this._oComponent.logout();
        },

        // Convenience method for calling function imports. Provides error handling.
        callFunctionImport: function(sFunctionName, oURLParameters, fnAfterFunctionExecuted, fnErrorFunction, batchGroupId) {
            this.getModel().callFunction(sFunctionName, {
                method: "POST",
                urlParameters: oURLParameters,
                success: jQuery.proxy(fnAfterFunctionExecuted, this),
                error: jQuery.proxy(fnErrorFunction, this),
                batchGroupId: batchGroupId
            });
        },
        
        // Convenience method for calling function imports. Provides error handling.
        callGetFunctionImport: function(sFunctionName, oURLParameters, fnAfterFunctionExecuted, fnErrorFunction, batchGroupId) {
            this.getModel().callFunction(sFunctionName, {
                method: "GET",
                urlParameters: oURLParameters,
                success: jQuery.proxy(fnAfterFunctionExecuted, this),
                error: jQuery.proxy(fnErrorFunction, this),
                batchGroupId: batchGroupId
            });
        },
        
        /** 
         * Event handler  for navigating back.
         * It there is a history entry we go one step back in the browser history
         * If not, it will replace the current entry of the browser history with the worklist route.
         * @public
         */
        onNavBack: function() {
            var sPreviousHash = History.getInstance().getPreviousHash();

            if (sPreviousHash !== undefined) {
                if (sPreviousHash !== "") {
                    history.go(-1);
                }else{
                    this.getRouter().navTo("manageForms", {}, true);
                }
            } else {
                this.getRouter().navTo("manageForms", {}, true);
            }
        },
        
        convertDepToInternal: function(sDep) {
            //convert e.g. 003.2017 to 2017003
            var aSplit = sDep.split(".");
            var sDepInternal = aSplit[1].concat(aSplit[0]);
            return sDepInternal;
        },
        
        getParametersFromURL: function(){
            
            var oComponent = sap.ui.component(sap.ui.core.Component.getOwnerIdFor(this._oView));
            var oApplicationController = oComponent._oApplicationController;
            var area = jQuery.sap.getUriParameters().get("area");
            var company = jQuery.sap.getUriParameters().get("company");
            var period = jQuery.sap.getUriParameters().get("period");
            var reportingpurpose = jQuery.sap.getUriParameters().get("reportingpurpose");
            var workinglevel = jQuery.sap.getUriParameters().get("workinglevel");
            var restatement = jQuery.sap.getUriParameters().get("restatement");
            


            oApplicationController.setArea(area);
            oApplicationController.setCompany(company);
            oApplicationController.setFiscper(period);
            oApplicationController.setReportingPurpose(reportingpurpose);
            oApplicationController.setWorkingLevel(workinglevel);
            oApplicationController.setRestatement(restatement);

        },
        
        getSessionDataFromPortalModel: function(){
            
            var oComponent = sap.ui.component(sap.ui.core.Component.getOwnerIdFor(this._oView));
            var oApplicationController = oComponent._oApplicationController;
            
            var appProperties = oComponent.oPropagatedProperties.oModels.appProperties;
            if (appProperties) {
                
                var sessionData = oComponent.oPropagatedProperties.oModels["undefined"].getProperty( "/globalParameterSet('" + appProperties.getProperty("/SessionId") + "')" );	
            

                oApplicationController.setArea(sessionData.Area);
                oApplicationController.setCompany(sessionData.CompanyCode);
                oApplicationController.setFiscper(sessionData.Fiscper);
                oApplicationController.setReportingPurpose(sessionData.ReportingPurpose);
                oApplicationController.setWorkingLevel(sessionData.WorkingLevel);
                oApplicationController.setRestatement(sessionData.Restatement);				
            
            }
            
        },
        

        /* ------------------------------------------------------------------------------------------ */
        /*      								  custom model types       							  */
        /* ------------------------------------------------------------------------------------------ */
        typePeriod: sap.ui.model.SimpleType.extend("period", {
            formatValue: function(oValue) {
                
                if(oValue){
                    if(oValue.indexOf('.') === -1){
                        oValue = oValue.substr(4,3) + "." + oValue.substr(0,4)
                        //oValue = convertDepToInternal(oValue);
                    }
                }
                return oValue;

            },
            
            parseValue: function(oValue) {
                //parsing step takes place before validating step, value can be altered

                // Modify when we have M.YYYY -> 00M.YYYY
                // Modify when we have 0M.YYYY -> 00M.YYYY

                if (oValue.length < 8 && oValue !== "") {
                    var parts = oValue.split(".");

                    var yearregex = /\d/;

                    if (parts[0].length == 1) {
                        if (parts[0].match(yearregex)) {
                            parts[0] = "00".concat(parts[0]);
                        } else {
                            return oValue;
                        }
                    }
                    if (parts[0].length == 2) {
                        if (parts[0].replace(/^0+/, '').match(yearregex)) {
                            parts[0] = "0".concat(parts[0]);
                        } else {
                            return oValue;
                        }
                    }
                    oValue = parts[0].concat(".").concat(parts[1]);
                }
                return oValue;
            },
            validateValue: function(oValue) {

            }
        })
    });

}
);