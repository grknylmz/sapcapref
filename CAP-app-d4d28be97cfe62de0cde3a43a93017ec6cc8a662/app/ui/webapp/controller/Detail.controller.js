sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/model/json/JSONModel",
    'sap/f/library',
    "../model/formatter",
    "sap/m/MessageBox",
    'sap/ui/model/Filter',
], function(Controller, JSONModel, fioriLibrary, formatter, MessageBox, Filter) {
    "use strict";
    return Controller.extend("ui.controller.Detail", {

        formatter: formatter,

        onInit: function() {
            this.oRouter = this.getOwnerComponent().getRouter();
            this.oUserAttributesModel = this.getOwnerComponent().getModel("UserAttributes"),
            this.oUserInfoModel = this.getOwnerComponent().getModel("UserInfo"),
            this.oRouter.getRoute("detail").attachPatternMatched(this._onObjectMatched, this);
            var oThesisModel = new JSONModel({
                    "studentCountValue": -1,
                    "studentApplyCreateDate":""
                }),
                oFilterModel = new JSONModel();
            this.getView().setModel(oThesisModel, "Thesis");
            this.getView().setModel(oFilterModel, "S2T");
        },

        onApplyClearButtonPress: async function(oEvent) {
            var oModel = this.getView().getModel(),
                oThesisModel = this.getView().getModel("Thesis"),
                prefix = this.oUserInfoModel.getProperty("/neptun"),
                thesisID = oThesisModel.getProperty("/ID"),
                oFilters = [
                    new Filter("student_neptun", sap.ui.model.FilterOperator.EQ, prefix),
                    new Filter("thesis_ID", sap.ui.model.FilterOperator.EQ, thesisID)
                ],
                binding = oModel.bindList("/Students2Theses", undefined, [], oFilters);

            this.byId("applyClearButton").setEnabled(false);
            binding.requestContexts().then(function (aContexts) {
                aContexts.forEach(function (oContext) {
                    oContext.delete().then(function () {
                        oModel.refresh();
                        this._refreshS2TmodelWithBtnUpdate(thesisID);
                        MessageBox.alert(this._getText("DetailApplyClearSuccessMessage"),
                            {icon : MessageBox.Icon.SUCCESS, title : this._getText("appSuccessMsgTitle")});
                    }.bind(this), function (oError) {
                        MessageBox.alert(this._getText("DetailApplyClearErrorMessage")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                        this.byId("applyButton").setEnabled(true);                     
                    }.bind(this));
                }.bind(this));
            }.bind(this), function(oError) {
                MessageBox.alert(this._getText("appDatabaseErrorMessageText")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
            }.bind(this));
        },

        onApplyButtonPress: function(oEvent) {
            if(this.getView().getModel("Thesis").getProperty("/faculty_ID") != this.oUserAttributesModel.getProperty("/Faculty")[0]) {
                MessageBox.warning(this._getText("DetailConfirmApplyFaculty"), {
                    actions: [MessageBox.Action.YES, MessageBox.Action.NO],
                    onClose: function (oAction) {
                        if (oAction === MessageBox.Action.YES) {
                            this._onApplyConfirmed();
                        }
                    }.bind(this)
                });
            } else {
                MessageBox.confirm(this._getText("DetailConfirmApply"), {
                    actions: [MessageBox.Action.YES, MessageBox.Action.NO],
                    onClose: function (oAction) {
                        if (oAction === MessageBox.Action.YES) {
                            this._onApplyConfirmed();
                        }
                    }.bind(this)
                });
            }
        },

        onHandleClose: function () {
            var oFCL = this.oView.getParent().getParent();
			this.oRouter = this.getOwnerComponent().getRouter();
			oFCL.setLayout(fioriLibrary.LayoutType.OneColumn);
			this.oRouter.navTo("master");
        },

        _onObjectMatched: function (oEvent) {
            var thesisID = oEvent.getParameter("arguments").id,
                oModel = this.getView().getModel(),
                oThesisModel = this.getView().getModel("Thesis"),
                rankList = this.oUserAttributesModel.getProperty("/Rank"),
                thesisBinding = oModel.bindContext("/Theses(" + thesisID + ")?$expand=students,faculty,teachers($expand=teacher),courses($expand=course),tags($expand=tag),literatures($expand=literature),prerequisites($expand=knowledge)");

			thesisBinding.requestObject().then(function (result) {
                if(result.achieved === true) {
                    MessageBox.alert(this._getText("DetailLoadingArchivedError"), {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                    return;
                }
                oThesisModel.setData(result);
                this._getStudentCountByThesis(thesisID);
                if(rankList === undefined) {
                    MessageBox.alert(this._getText("InfoPopupErrorLoadingRank"), {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                    return;
                } else if(rankList[0] === "Student") {
                    this.byId("courseWarningText").setVisible(false);
                    this._refreshS2TmodelWithBtnUpdate(thesisID);
                }
            }.bind(this));
        },

        _checkStudentCourseInCourses: function() {
            var oThesisModel = this.getView().getModel("Thesis"),
                courses = oThesisModel.getProperty("/courses"),
                userCourse = this.oUserAttributesModel.getProperty("/Course"),
                courseExists = false;
            if(userCourse === undefined) return false;
            courses.forEach(course => {
                if(course.course_ID === userCourse[0]) courseExists = true;
            });
            if(!courseExists) this.byId("courseWarningText").setVisible(true);
            return courseExists;     
        },

        _refreshS2TmodelWithBtnUpdate: function(thesisID) {
            var oModel = this.getView().getModel(),
                oFilterModel = this.getView().getModel("S2T"),
                prefix = this.oUserInfoModel.getProperty("/neptun"),
                s2tBinding = oModel.bindContext("/Students2Theses?$filter=student_neptun eq '" + prefix + "'");

            s2tBinding.requestObject().then(function (result2) {
                oFilterModel.setData(result2);
                this._onUpdateButton(thesisID);
            }.bind(this));
        },

        _onApplyConfirmed: function() {
            var oModel = this.getView().getModel(),
                oThesisModel = this.getView().getModel("Thesis"),
                oFilterModel = this.getView().getModel("S2T"),
                prefix = this.oUserInfoModel.getProperty("/neptun"),
                thesisID = oThesisModel.getProperty("/ID"),
                oFilters = [
                    new Filter("student_neptun", sap.ui.model.FilterOperator.EQ, prefix),
                    new Filter("thesis/open", sap.ui.model.FilterOperator.EQ, true)
                ],
                binding = oModel.bindList("/Students2Theses", undefined, [], oFilters);

            this.byId("applyButton").setEnabled(false);
            binding.requestContexts().then(function (aContexts) {
                var alreadyExists = false;
                aContexts.forEach(function (oContext) {
                    if(oContext.getProperty("student_neptun") === prefix && oContext.getProperty("thesis_ID") === thesisID) {
                        alreadyExists = true;
                        return;
                    }
                });
                if(alreadyExists === true) {
                    MessageBox.alert(this._getText("DetailApplyErrorMessage"),
                            {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                    return;
                } else {
                    var oNewContext = binding.create({
                        "student_neptun" : prefix,
                        "thesis_ID" : thesisID
                    });
                    oNewContext.created().then(function () {
                        oModel.refresh();
                        this._refreshS2TmodelWithBtnUpdate(thesisID);
                        MessageBox.alert(this._getText("DetailApplySuccessMessage"),
                            {icon : MessageBox.Icon.SUCCESS, title : this._getText("appSuccessMsgTitle")});
                    }.bind(this), function (oError) {
                        MessageBox.alert(this._getText("DetailApplyErrorMessage")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                        this.byId("applyButton").setEnabled(true);
                    }.bind(this));
                }
            }.bind(this), function(oError) {
                MessageBox.alert(this._getText("appDatabaseErrorMessageText")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
            }.bind(this));
        },
        
        _onUpdateButton: async function(thesisID) {
            this._setDetailButtons("everythingHidden");
            var oButton = this.byId("applyButton"),
                oFilterModel = this.getView().getModel("S2T"),
                oThesisModel = this.getView().getModel("Thesis"),
                filterData = oFilterModel.getProperty("/value"),
                rankList = this.oUserAttributesModel.getProperty("/Rank"),
                existsButNotThis = false;
            await this._getStudentCountByThesis(thesisID);

            if(rankList[0] === "Student") {
                oButton.setTooltip(this._getText("DetailApplyTextStudentTooltip"));
                if(filterData.length > 0) {
                    oButton.setTooltip(this._getText("DetailApplyTextAlreadyAppliedTooltip"));
                    filterData = filterData.filter(x => x.thesis_ID === thesisID);
                    if(filterData.length > 0) {
                        oThesisModel.setProperty("/studentApplyCreateDate", filterData[0].createdAt);
                        this._setDetailButtons("canCancel");                        
                    } else {
                        this._setDetailButtons("canSeeDisabledApply");
                    }
                } else if(!this._checkStudentCourseInCourses() && oThesisModel.getProperty("/only_preferred") === true) {
                    this._setDetailButtons("canSeeDisabledApply");
                    oButton.setTooltip(this._getText("DetailApplyTextOnlyPreferredCourseTooltip"));
                } else if(oThesisModel.getProperty("/open") === false) {
                    this._setDetailButtons("canSeeDisabledApply");
                    oButton.setTooltip(this._getText("DetailApplyTextClosedTooltip"));
                } else if(new Date(oThesisModel.getProperty("/todate")).getTime() < new Date().getTime()) {
                    this._setDetailButtons("canSeeDisabledApply");
                    oButton.setTooltip(this._getText("DetailApplyTextNotAvailableTooltip"));
                } else if(oThesisModel.getProperty("/studentCountValue") >= oThesisModel.getProperty("/max_students")) {
                    this._setDetailButtons("canSeeDisabledApply");
                    oButton.setTooltip(this._getText("DetailApplyTextNoPlaceTooltip"));
                } else {
                    this._setDetailButtons("canApply");
                    oButton.setTooltip(this._getText("DetailApplyTextStudentTooltip"));
                }
            }
        },

        _setDetailButtons: function(oSituation) {
            switch (oSituation) {
                case "canApply":
                    this.byId("applyButton").setVisible(true);
                    this.byId("applyClearButton").setVisible(false);
                    this.byId("applyClearText").setVisible(false);
                    this.byId("applyButton").setEnabled(true);
                    this.byId("applyClearButton").setEnabled(false);
                    break;
                case "canCancel":
                    this.byId("applyButton").setVisible(false);
                    this.byId("applyClearButton").setVisible(true);
                    this.byId("applyClearText").setVisible(true);
                    this.byId("applyButton").setEnabled(false);
                    this.byId("applyClearButton").setEnabled(true);
                    break;
                case "canSeeDisabledApply":
                    this.byId("applyButton").setVisible(true);
                    this.byId("applyClearButton").setVisible(false);
                    this.byId("applyClearText").setVisible(false);
                    this.byId("applyButton").setEnabled(false);
                    this.byId("applyClearButton").setEnabled(false);
                    break;
                case "everythingHidden":
                default:
                    this.byId("applyButton").setVisible(false);
                    this.byId("applyClearButton").setVisible(false);
                    this.byId("applyClearText").setVisible(false);
                    this.byId("applyButton").setEnabled(false);
                    this.byId("applyClearButton").setEnabled(false);
                    break;
            }
        },

        _getStudentCountByThesis: async function(thesisID) {
            var oThesisModel = this.getView().getModel("Thesis"),
                oModel = this.getView().getModel(),
                binding = oModel.bindContext("/StudentCount(" + thesisID + ")/count");
            await binding.requestObject().then(function (result) {
                
                oThesisModel.setProperty("/studentCountValue", result.value);
            });
        },

        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        }        
    });
});