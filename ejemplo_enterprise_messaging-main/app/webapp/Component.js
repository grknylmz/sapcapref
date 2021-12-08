//sap.ui.define(["sap/fe/AppComponent"], ac => ac.extend("inicial.Component", {
 //   metadata:{ manifest:'json' }
//}))

 sap.ui.define (["sap/ui/core/UIComponent"], ui5 => ui5.extend("inicial.Component", {
	 metadata: { manifest: "json" },

	 init : function () {
		 
		sap.ui.core.UIComponent.prototype.init.apply(this, arguments);

		this.doRouting();
	 },

	 doRouting: function(changeToNotFound){
				
		if(changeToNotFound){
			var hash = sap.ui.core.routing.HashChanger.getInstance();
			hash.setHash("requestNotFound");
		}
		
		// initialize router and navigate to the first page
		this.getRouter().initialize(); 

		
	}

 }))
// sap.ui.define (["sap/ui/generic/app/AppComponent"], ui5 => ui5.extend("bookshop.Component", {
//     metadata: { manifest: "json" }
// }))

// jQuery.sap.declare("bookshop.Component");
// sap.ui.getCore().loadLibrary("sap.ui.generic.app");
// jQuery.sap.require("sap.ui.generic.app.AppComponent");

 /*sap.ui.generic.app.AppComponent.extend("inicial.Component", {
 	metadata: {
 		manifest: "json"
 	}
 });*/

/* eslint no-undef:0 */