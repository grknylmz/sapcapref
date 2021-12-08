sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/ui/core/ws/WebSocket"
], function (Controller,WebSocket) {
	"use strict";

	return Controller.extend("inicial.controller.Gannt", {
		onInit: function () {
            
		},

		onPress: function(){
			var connection = new WebSocket("/NodeWS");
			console.log("test");
		}
	});
});