sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/m/MessageBox",
    "sap/ui/core/Core",
    'sap/ui/model/Filter',
], function (Controller, MessageBox, Core, Filter) {
    "use strict";

    var Theme = Object.create(null);
    Theme["light"] = "sap_fiori_3";
    Theme["dark"] = "sap_fiori_3_dark";
    Theme["auto"] = "sap_fiori_3";
    return Controller.extend("ui.controller.ToolPage", {

        onInit: function () {
            //this.getView().addStyleClass(this.getOwnerComponent().getContentDensityClass());
            this.oRouter = this.getOwnerComponent().getRouter();
            this.oUserInfoModel = this.getOwnerComponent().getModel("UserInfo");
            this.oUserAttributesModel = this.getOwnerComponent().getModel("UserAttributes");
            Promise.all([this.oUserInfoModel.loadData("/user/userinfo"),
                        this.oUserAttributesModel.loadData("/user/userattributes")]).then(function() {
                if(this.oUserInfoModel.getProperty("/familyName") === undefined || this.oUserAttributesModel.getProperty("/Rank") === undefined) {
                    
                    this.oRouter.navTo("authFail", { bReplace : true});
                    return;
                }
            
            this._browserSupportPrefColorSch = !!(window.matchMedia && (window.matchMedia('(prefers-color-scheme: dark)').matches || window.matchMedia('(prefers-color-scheme: light)').matches));
            this.getView().addEventDelegate({
                onAfterShow: function(oEvent){
                    if(this.getOwnerComponent().getModel("UserInfo").getProperty("/familyName") === undefined || this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank") === undefined) {
                
                        this.oRouter.navTo("authFail", { bReplace : true});
                        //MessageBox.alert(this._getText("InfoPopupErrorLoadingRank"), {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                        return;
                    } else {
                        this._loadMenusAndCheckUser();
                    }
                }.bind(this)
            },  this.getView());
            
            this._loadMenusAndCheckUser();
        }.bind(this));
        },

        onSideNavButtonPress: function () {
            var oToolPage = this.byId("toolPage");
            var bSideExpanded = oToolPage.getSideExpanded();

            this._setToggleButtonTooltip(bSideExpanded);

            oToolPage.setSideExpanded(!oToolPage.getSideExpanded());
        },

        handleMenuItemClick : function(oEvent) {
            var key = oEvent.getParameter("item").getKey();
            
            switch(key) {
                case "info":
                    this.showInfoDialog();
                    break;
                case "light":
                case "dark":
                case "auto":
                    this.updateTheme(key);
                    break;
                case "logout":
                    this.onLogoutPress();
                    break;
            }

        },

        onItemSelect: function (oEvent) {
            var item = oEvent.getParameter('item');
            var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
            oRouter.navTo(item.getKey());
        },

        updateTheme: function (selectedTheme) {
            if(selectedTheme === "auto" && this._browserSupportPrefColorSch) {
                Core.applyTheme(Theme[(window.matchMedia('(prefers-color-scheme: dark)').matches) ? "dark" : "light"]);
            } else {
                Core.applyTheme(Theme[selectedTheme])
            }
        },

        onLogoutPress: function (event) {
            window.location.replace('/my/logout');
        },

        showInfoDialog: function(oEvent) {
            var rankString = this._getText("RankNameUndefined"),
                facultyString = this._getText("RankNameUndefined"),
                courseString = this._getText("RankNameUndefined"),
                rankList = this.oUserAttributesModel.getProperty("/Rank"),
                facultyList = this.oUserAttributesModel.getProperty("/Faculty"),
                courseList = this.oUserAttributesModel.getProperty("/Course"),
                addExtra = false;

            if(rankList === undefined || facultyList === undefined || courseList === undefined) {
                MessageBox.alert(this._getText("InfoPopupErrorLoadingRank"), {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                return;
            }
            switch(rankList[0]) {
                case "Student":
                    rankString = this._getText("RankNameStudent");
                    facultyString = facultyList.length === 0 ? facultyString : facultyList[0];
                    courseString = courseList.length === 0 ? courseString : courseList[0];
                    addExtra = true;
                    break;
                case "Teacher":
                    rankString = this._getText("RankNameTeacher");
                    facultyString = facultyList.length === 0 ? facultyString : facultyList[0];
                    break;
                case "Admin":
                    rankString = this._getText("RankNameAdmin");
                    break;
                case undefined:
                    rankString = this._getText("RankNameIssue");
                    break;
                default:
                    rankString = this._getText("RankNameOther");
                    break;
            }
            MessageBox.information(`${this._getText("InfoPopupInitialText")}:\n
                                    ${this._getText("InfoPopupLastname")}: ${this.oUserInfoModel.getProperty("/familyName")}
                                    ${this._getText("InfoPopupFirstname")}: ${this.oUserInfoModel.getProperty("/givenName")}
                                    ${this._getText("InfoPopupNeptun")}: ${this.oUserInfoModel.getProperty("/neptun")}
                                    ${this._getText("InfoPopupEmail")}: ${this.oUserInfoModel.getProperty("/email")}
                                    ${this._getText("InfoPopupRank")}: ${rankString}
                                    ${this._getText("InfoPopupFaculty")}: ${facultyString}
                                    ${ addExtra ? `${this._getText("InfoPopupCourse")}: ${courseString}` : '' }`);
        },

        _loadMenusAndCheckUser: function() {
            var rankList = this.oUserAttributesModel.getProperty("/Rank");
            if(rankList != undefined) {
                switch(rankList[0]) {
                    case "Student":
                        this.byId("thesisList").setVisible(true);
                        this.byId("thesisView").setVisible(true);
                        this.byId("thesisSubmit").setVisible(true);
                        this.byId("thesisKeywords").setVisible(true);
                        break;
                    case "Teacher":
                        this.byId("thesisList").setVisible(true);
                        this.byId("thesisView").setVisible(true);
                        this.byId("thesisEdit").setVisible(true);
                        this.byId("thesisKeywords").setVisible(true);

                        break;
                    case "Admin":
                        this.byId("viewUsers").setVisible(true);
                        this.byId("editFaculties").setVisible(true);
                        this.byId("editCourses").setVisible(true);
                        this.byId("editDeadlines").setVisible(true);
                        this.byId("viewStats").setVisible(true);
                        break;
                }
            }
            var neptun = this.oUserInfoModel.getProperty("/neptun"),
                dataToInsert = {
                    "neptun" : neptun,
                    "first_name" : this.oUserInfoModel.getProperty("/givenName"),
                    "last_name" : this.oUserInfoModel.getProperty("/familyName"),
                    "email" : this.oUserInfoModel.getProperty("/email"),
                },
                binding = this.getView().getModel().bindList("/" + rankList[0] + "s", undefined, [], new Filter("neptun", sap.ui.model.FilterOperator.EQ, neptun));
            
            if(rankList[0] === "Student") $.extend(dataToInsert, {"faculty_ID" : this.oUserAttributesModel.getProperty("/Faculty")[0],
                                                                    "course_ID" : this.oUserAttributesModel.getProperty("/Course")[0]})
            if(rankList[0] === "Teacher") $.extend(dataToInsert, {"faculty_ID" : this.oUserAttributesModel.getProperty("/Faculty")[0]})
            
            binding.requestContexts().then(function (aContexts) {
                var found = false;
                //jobb esetben csak egy elem van az oContext-ben
                aContexts.forEach(function (oContext) {
                    if(oContext.getProperty("neptun") === neptun) {
                        found = true;
                        if(oContext.getProperty("first_name") != dataToInsert.first_name) oContext.setProperty("first_name", dataToInsert.first_name);
                        if(oContext.getProperty("last_name") != dataToInsert.last_name) oContext.setProperty("last_name", dataToInsert.last_name);
                        if(rankList[0] === "Teacher" || rankList[0] === "Student") {
                            if(oContext.getProperty("faculty_ID") != dataToInsert.faculty_ID) oContext.setProperty("faculty_ID", dataToInsert.faculty_ID);
                        }
                        if(rankList[0] === "Student") {
                            if(oContext.getProperty("course_ID") != dataToInsert.course_ID) oContext.setProperty("course_ID", dataToInsert.course_ID);
                        }
                    }
                }.bind(this));
                if(found === false) {
                    var oNewUserCreate = binding.create(dataToInsert);
                    oNewUserCreate.created().then(function () {
                        this.getView().getModel().refresh(); 
                    }.bind(this), function (oError) {
                        MessageBox.alert(this._getText("appUserAddErrorMessageText")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                    }.bind(this));
                }
            }.bind(this), function(oError) {
                MessageBox.alert(this._getText("appDatabaseErrorMessageText")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
            }.bind(this));
        },
        
        _setToggleButtonTooltip: function (bLarge) {
            var oToggleButton = this.byId('sideNavigationToggleButton');
            if (bLarge) {
                oToggleButton.setTooltip(this._getText("toolHeaderHamburgerTooltipBig"));
            } else {
                oToggleButton.setTooltip(this._getText("toolHeaderHamburgerTooltipSmall"));
            }
        },

        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        }
    });
});