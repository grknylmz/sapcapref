sap.ui.define([
		'sap/ui/core/Fragment',
		'sap/ui/core/mvc/Controller',
        "sap/ui/model/Filter",
        "sap/m/MessageBox",
        "ui/libs/uuid"
	], function(Fragment, Controller, Filter, MessageBox, uuid) {
	"use strict";

	var PageController = Controller.extend("ui.controller.EditTheses", {

		onInit: function () {
            this.oRouter = this.getOwnerComponent().getRouter();
            this.oUserInfoModel = this.getOwnerComponent().getModel("UserInfo");

			this._formFragments = {};

			this._showFormFragment("Display");

            this.getView().addEventDelegate({
                onBeforeShow: function(oEvent) {
                    this.getView().setBusy(true);
                }.bind(this),
                onAfterShow: function(oEvent){
                    if(this.oUserInfoModel.getProperty("/familyName") === undefined || this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank") === undefined) {
                        this.oRouter.navTo("authFail", { bReplace : true});
                        return;
                    }
                    if(this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Teacher") {
                        this.getView().setBusy(false);
                        this._bindItemsToMasterList();
                    } else {
                        this.oRouter.navTo("permissionFail");
                        return;
                    }
                }.bind(this)
            },  this.getView());

        },

        onListItemPress: function(oEvent) {
            this.byId("SimpleFormSplitscreen").toDetail(this.createId("detailPage"));
            this.byId('edit').setEnabled(true);
            var oBindingContext = oEvent.getSource().getBindingContext();
            this.getView().bindElement({
                path: "/Theses(" + oBindingContext.getProperty("thesis_ID") + ")",
                parameters: {
                    $expand: 'courses($expand=course($expand=faculty)),students($expand=student($expand=faculty,course)),teachers($expand=teacher($expand=faculty))'
                },
                events: {
                    dataReceived: this._validateInputsWithFaculty.bind(this)
                }
            });
        },

        onBackToMasterPress: function() {
            this.byId("SimpleFormSplitscreen").toMaster(this.createId("masterPage"));
        },

        onFacultyChanged: function(oEvent) {
            this._validateInputsWithFaculty(oEvent, oEvent.getSource().getSelectedKey());
        },

        handleNewThesisPress: function() {
            MessageBox.warning(this._getText("AddNewThesisConfirmText"), {
                actions: [MessageBox.Action.YES, MessageBox.Action.NO],
                onClose: function (oAction) {
					if (oAction === MessageBox.Action.YES) {
						this._createNewThesis();
					}
				}.bind(this)
            });
        },

        onSelectableTokenUpdate: function(oEvent) {
            var bindPath = " ",
                property = " ";
            switch(oEvent.getSource().getId()) {
                case "thesisStudents":
                    bindPath = "/Students2Theses";
                    property = "student_neptun";
                    break;
                case "thesisTeachers":
                    bindPath = "/Teachers2Theses";
                    property = "teacher_neptun";
                    break;
                case "thesisCourses":
                    bindPath = "/Courses2Theses";
                    property = "course_ID";
                    break;
            }
            var oModel = this.getView().getModel(),
                oSource = oEvent.getSource(),
                sType = oEvent.getParameter("type"),
                aAddedTokens = oEvent.getParameter("addedTokens"),
                aRemovedTokens = oEvent.getParameter("removedTokens"),
                thesis_ID = oSource.getBindingContext().getProperty("ID"),    
                binding = oModel.bindList(bindPath);
            
            binding.requestContexts().then(function (aContexts) {
                switch (sType) {
                    case "added" :
                        aAddedTokens.forEach(function (oToken) {
                            var oNewContext = binding.create({
                                [property] : oToken.getKey(),
                                "thesis_ID" : thesis_ID
                            });
                            oNewContext.created().then(function () {
                                oModel.refresh();
                            });
                        });
                        break;
                    case "removed" :
                        aRemovedTokens.forEach(function (oToken) {
                            if(this.oUserInfoModel.getProperty("/neptun") === oToken.getKey()) {
                                MessageBox.alert(this._getText("EditThesisTeacherDeleteError"), {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                                oSource.addToken(new sap.m.Token({
                                    text: oToken.getText(),
                                    key: oToken.getKey()
                                }));
                            } else {
                                aContexts.forEach(function (oContext) {
                                    if(oContext.getProperty(property) === oToken.getKey() && oContext.getProperty("thesis_ID") === thesis_ID) {
                                        oContext.delete().then(function () {
                                            oModel.refresh();
                                        });
                                    }
                                });
                            }
                        }.bind(this));
                        break;
                    default: break;
                }
            }.bind(this), function(oError) {
                MessageBox.alert(this._getText("appDatabaseErrorMessageText")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
            }.bind(this));
        },

        onTokenUpdate: function(oEvent) {
            var homeBindPath = " ",
                property = " ",
                homeProperty = " ";
            switch(oEvent.getSource().getId()) {
                case "thesisPrerequisites":
                    homeBindPath = "/Prerequisites";
                    property = "knowledge_ID";
                    homeProperty = "knowledge";
                    break;
                case "thesisLiteratures":
                    homeBindPath = "/Literatures";
                    property = "literature_ID";
                    homeProperty = "literature";
                    break;
                case "thesisTags":
                    homeBindPath = "/Tags";
                    property = "tag_ID";
                    homeProperty = "name";
                    break;
            }
            var oModel = this.getView().getModel(),
                oSource = oEvent.getSource(),
                sType = oEvent.getParameter("type"),
                aAddedTokens = oEvent.getParameter("addedTokens"),
                aRemovedTokens = oEvent.getParameter("removedTokens"),
                thesis_ID = oSource.getBindingContext().getProperty("ID"),
                binding = oModel.bindList(homeBindPath + "2Theses");
            
            binding.requestContexts().then(function (aContexts) {
                switch (sType) {
                    case "added" :
                        aAddedTokens.forEach(function (oToken) {
                            var checkBinding = oModel.bindContext(homeBindPath + "?$filter=" + homeProperty + " eq '" + oToken.getText() + "'");
                            checkBinding.requestObject().then(function (oObject) {
                                var keyID = oToken.getKey(),
                                    found = false;
                                if(oObject.value.length > 0) {
                                    keyID = oObject.value[0].ID;
                                    found = true;
                                }
                                if(found === false) {
                                    var homeBinding = oModel.bindList(homeBindPath);
                                    homeBinding.requestContexts().then(function (hContexts) {
                                        var oNewLiterature = homeBinding.create({
                                            "ID" : keyID,
                                            [homeProperty] : oToken.getText()
                                        });
                                        oNewLiterature.created().then(function() {
                                            var oNewContext = binding.create({
                                                [property] : keyID,
                                                "thesis_ID" : thesis_ID
                                            });
                                            oNewContext.created().then(function () {
                                                oModel.refresh();
                                            });
                                        }.bind(this));
                                    }.bind(this));
                                } else {
                                    var oNewContext = binding.create({
                                        [property] : keyID,
                                        "thesis_ID" : thesis_ID
                                    });
                                    oNewContext.created().then(function () {
                                        oModel.refresh();
                                    });
                                }
                                
                                oToken.setKey(keyID);
                            }.bind(this));
                        }.bind(this));
                        break;
                    case "removed" :
                        aRemovedTokens.forEach(function (oToken) {
                            aContexts.forEach(function (oContext) {
                                if(oContext.getProperty(property) === oToken.getKey() && oContext.getProperty("thesis_ID") === thesis_ID) {
                                    oContext.delete().then(function () {
                                        oModel.refresh();
                                    });
                                }
                            });
                        }.bind(this));
                        break;
                    default: break;
                }
            }.bind(this), function(oError) {
                MessageBox.alert(this._getText("appDatabaseErrorMessageText")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
            }.bind(this));
        },

        onEnableMyInput: function(oEvent, controlID) {
            var allValidator = function(args){
                var text = args.text,
                    key = uuidv4();
                if(text.includes("#") || text.includes("&") || text.includes("{") || text.includes("}") || text.split("%").length-1 > 1) 
                    return MessageBox.alert(this._getText("EditThesisForbiddenText"), {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
				return new sap.m.Token({key: key, text: text});
			}.bind(this);
			sap.ui.getCore().byId(controlID).addValidator(allValidator);
        },

		handleEditPress : function () {
            this._validateInputsWithFaculty();
			this._toggleButtonsAndView(true);

		},

		handleSavePress : function () {
            this.getView().getModel().submitBatch("$auto");
			this._toggleButtonsAndView(false);
        },
        
        _bindItemsToMasterList: function() {
            var prefix = this.oUserInfoModel.getProperty("/neptun");
            var oThesisList = this.byId("thesisList");
            oThesisList.bindItems({
                path: "/Teachers2Theses",
                template: new sap.m.StandardListItem({
                    title: "{thesis/title}",
                    type : sap.m.ListType.Active,
                    press: this.onListItemPress.bind(this)
                }),
                templateShareable: false,
                filters: [
                    new Filter("teacher_neptun", "EQ", prefix)
                ],
                parameters: {
                    $expand: 'thesis'
                }
            });
        },

        _createNewThesis: function() {
            var oModel = this.getView().getModel(),
                faculties = this.getView().getModel("UserAttributes").getProperty("/Faculty"),
                binding = oModel.bindList("/Theses");
            binding.requestContexts().then(function(aContexts) {
                var thesis_ID = uuidv4();
                var oNewContext = binding.create({
                    "ID" : thesis_ID,
                    "faculty_ID" : faculties === undefined ? null : faculties[0],
                    "title" : "Új téma",
                    "date" : new Date(),
                    "fromdate" : new Date(),
                    "todate" : new Date()
                });
               this.byId('new').setEnabled(false);
                oNewContext.created().then(function () {
                    var teacherBinding = oModel.bindList("/Teachers2Theses");
                        teacherBinding.requestContexts().then(function(aContexts) {
                            var oNewTeacherContext = teacherBinding.create({
                                "thesis_ID" : thesis_ID,
                                "teacher_neptun" : this.oUserInfoModel.getProperty("/neptun")
                            });
                            oNewTeacherContext.created().then(function () {
                                this.getView().bindElement("/Theses(" + thesis_ID + ")");
                                oModel.refresh();
                                this.byId('new').setEnabled(true);
                                this.byId('edit').setEnabled(true);
                            }.bind(this));
                        }.bind(this));
                }.bind(this), function (oError) {
                    MessageBox.alert(this._getText("DetailApplyErrorMessage")
                        + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
                    this.byId('new').setEnabled(true);
                }.bind(this));
            }.bind(this), function(oError) {
                MessageBox.alert(this._getText("appDatabaseErrorMessageText")
                            + oError.message, {icon : MessageBox.Icon.ERROR, title : this._getText("appErrorMsgTitle")});
            }.bind(this));
        },

        _validateInputsWithFaculty : function(oEvent, explicitFaculty='') {
            var oBindingContext = this.getView().getBindingContext();
            if(oBindingContext != undefined) {
                var oData = oBindingContext.getObject(),
                    faculty = explicitFaculty === '' ? oBindingContext.getProperty("faculty_ID") : explicitFaculty,
                    notValidCourse = false,
                    notValidStudentCourse = false,
                    notValidTeacher = false,
                    courseSelect = sap.ui.getCore().byId("thesisCourses"),
                    studentSelect = sap.ui.getCore().byId("thesisStudents"),
                    teacherSelect = sap.ui.getCore().byId("thesisTeachers");

                if(oData === undefined) return;

                oData.courses.forEach(courseExpand => {
                    if(courseExpand.course.faculty.ID != faculty) notValidCourse = true;
                });
                oData.students.forEach(studentExpand => {
                    var foundCourse = false;
                    oData.courses.forEach(courseExpand => {
                        if(courseExpand.course.ID === studentExpand.student.course.ID) foundCourse = true;
                    })
                    if(!foundCourse) notValidStudentCourse = true;
                });
                oData.teachers.forEach(teacherExpand => {
                    if(teacherExpand.teacher.faculty.ID != faculty) notValidTeacher = true;
                });

                if(notValidCourse === true) {
                    if(courseSelect != undefined) {
                        courseSelect.setValueState(sap.ui.core.ValueState.Warning);
                        courseSelect.setValueStateText(this._getText("EditThesisCourseFacultyWarning"));
                    }
                } else {
                    if(courseSelect != undefined) courseSelect.setValueState(sap.ui.core.ValueState.None);
                }
                if(notValidStudentCourse === true) {
                    if(studentSelect != undefined) {
                        studentSelect.setValueState(sap.ui.core.ValueState.Warning);
                        studentSelect.setValueStateText(this._getText("EditThesisStudentCourseWarning"));
                    }
                } else {
                    if(studentSelect != undefined) studentSelect.setValueState(sap.ui.core.ValueState.None);
                }
                if(notValidTeacher === true) {
                    if(teacherSelect != undefined) {
                        teacherSelect.setValueState(sap.ui.core.ValueState.Warning);
                        teacherSelect.setValueStateText(this._getText("EditThesisTeacherFacultyWarning"));
                    }
                } else {
                    if(teacherSelect != undefined) teacherSelect.setValueState(sap.ui.core.ValueState.None);
                }
            }
        },

		_toggleButtonsAndView : function (bEdit) {
			var oView = this.getView();

			oView.byId("edit").setVisible(!bEdit);
			oView.byId("save").setVisible(bEdit);

			this._showFormFragment(bEdit ? "Change" : "Display");
		},

		_getFormFragment: function (sFragmentName) {
			var pFormFragment = this._formFragments[sFragmentName],
				oView = this.getView();

			if (!pFormFragment) {
				pFormFragment = Fragment.load({
					//id: oView.getId(),
                    name: "ui.fragment." + sFragmentName,
                    controller: this
                });
                
				this._formFragments[sFragmentName] = pFormFragment;
			}

			return pFormFragment;
		},

		_showFormFragment : function (sFragmentName) {
			var oPage = this.byId("detailPage");

			oPage.removeAllContent();
			this._getFormFragment(sFragmentName).then(function(oVBox) {
                oPage.insertContent(oVBox);
                if(sFragmentName === "Change") this._validateInputsWithFaculty();
			}.bind(this));
        },

        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        }

	});

	return PageController;

});