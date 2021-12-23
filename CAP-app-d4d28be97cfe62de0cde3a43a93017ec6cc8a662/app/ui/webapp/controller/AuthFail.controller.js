sap.ui.define([
	"sap/ui/core/mvc/Controller"
], function (Controller) {
	"use strict";

	return Controller.extend("ui.controller.AuthFail", {
		onLogoutPress: function() {
            window.location.replace('/my/logout');
        }
	});

});