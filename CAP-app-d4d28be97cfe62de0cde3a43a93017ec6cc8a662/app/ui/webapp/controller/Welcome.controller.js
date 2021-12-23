sap.ui.define([
    "sap/ui/core/mvc/Controller",
    'sap/ui/model/json/JSONModel'
], function (Controller, JSONModel) {
	"use strict";

	return Controller.extend("ui.controller.Welcome", {
        onInit : function () {
            this.oRouter = this.getOwnerComponent().getRouter();
            this.oView = this.getView();
            var siteModel = new JSONModel({
                    "first_name": "",
                    "last_name": "",
                    "faculty_name":"",
                    "course_name": "",
                    "welcome_text":"",
                    "rank_name":""
                })
            this.getView().setModel(siteModel, "Welcome");
            this.oUserInfoModel = this.getOwnerComponent().getModel("UserInfo");
            this.oUserAttributesModel = this.getOwnerComponent().getModel("UserAttributes");
            this._changeLogoTheme(sap.ui.getCore().getConfiguration().getTheme());
            sap.ui.getCore().attachThemeChanged(function(oEvent) {
                this._changeLogoTheme(oEvent.getParameter("theme"));
            }.bind(this));

            this.getView().addEventDelegate({
                onBeforeShow: function(oEvent) {
                    this.oView.setBusy(true);
                }.bind(this),
                onAfterShow: function(oEvent) {
                    if(this.oUserInfoModel.getProperty("/familyName") === undefined || this.oUserAttributesModel.getProperty("/Rank") === undefined) {
                        this.oRouter.navTo("authFail", { bReplace : true});
                        return;
                    }
                    this.oView.setBusy(false);
                    this._loadUserInfo();
                }.bind(this)
            },  this.oView);
        },

        _loadUserInfo: function() {
            var oModel = this.getView().getModel(),
                oWelcomeModel = this.getView().getModel("Welcome"),
                rankList = this.oUserAttributesModel.getProperty("/Rank"),
                facultyList = this.oUserAttributesModel.getProperty("/Faculty"),
                courseList = this.oUserAttributesModel.getProperty("/Course");
            oWelcomeModel.setProperty("/first_name", this.oUserInfoModel.getProperty("/givenName"));
            oWelcomeModel.setProperty("/last_name", this.oUserInfoModel.getProperty("/familyName"));
            oWelcomeModel.setProperty("/faculty_name", this._getText("RankNameUndefined"));
            oWelcomeModel.setProperty("/course_name", this._getText("RankNameUndefined"));
            oWelcomeModel.setProperty("/rank_name", this._getText("RankNameUndefined"));
            if(rankList[0] === "Student" || rankList[0] === "Teacher") {
                var facultyBinding = oModel.bindContext("/Faculties(" + facultyList[0] + ")?$select=name");
                oWelcomeModel.setProperty("/welcome_text", this._getText("WelcomeTextTeacher"));
                oWelcomeModel.setProperty("/rank_name", this._getText("RankNameTeacher"));
                
                facultyBinding.requestObject().then(function (result) {
                    oWelcomeModel.setProperty("/faculty_name", result.name);
                }.bind(this), function (oError) {
                    //oWelcomeModel.setProperty("/faculty_name", this._getText("RankNameUndefined"));
                }.bind(this));
            }
            if(rankList[0] === "Student") {
                var courseBinding = oModel.bindContext("/Courses(" + courseList[0] + ")?$select=name");
                oWelcomeModel.setProperty("/welcome_text", this._getText("WelcomeTextStudent"));
                oWelcomeModel.setProperty("/rank_name", this._getText("RankNameStudent"));
                courseBinding.requestObject().then(function (result) {
                    oWelcomeModel.setProperty("/course_name", result.name);
                }.bind(this), function (oError) {
                    //oWelcomeModel.setProperty("/course_name", this._getText("RankNameUndefined"));
                }.bind(this));
            }
            if(rankList[0] === "Admin") {
                oWelcomeModel.setProperty("/rank_name", this._getText("RankNameAdmin"));
                oWelcomeModel.setProperty("/welcome_text", this._getText("WelcomeTextAdmin"));
            }
        },

        _changeLogoTheme: function(sTheme) {
            if(sTheme === "sap_fiori_3") {
                this.byId("logo").setSrc("css/resources/elte_cimer_ff.svg");
            } else if(sTheme === "sap_fiori_3_dark") {
                this.byId("logo").setSrc("css/resources/elte_cimer_ff_dark.svg");
            }
        },

        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        },
    });
});