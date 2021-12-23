sap.ui.define([
		'sap/ui/core/Fragment',
		'sap/ui/core/mvc/Controller',
        'sap/ui/model/json/JSONModel',
        "sap/ui/model/Filter",
        "sap/m/MessageBox"
	], function(Fragment, Controller, JSONModel, Filter, MessageBox) {
	"use strict";

	return Controller.extend("ui.controller.Keywords", {

		onInit: function (oEvent) {
            var oTextModel = new JSONModel({
                    "text": ""
                }),
                oKeywordsModel = new JSONModel(),
                oWordCloudSettings = new JSONModel({
                    "sizeH":500,
                    "sizeW":500,
                    "maxWords":20,
                    "enableSizeChange":true,
                    "font":"Impact"
                });
            this.getView().setModel(oTextModel, "Text");
            this.getView().setModel(oKeywordsModel, "Keyword");
            this.getView().setModel(oWordCloudSettings, "CloudSettings");
            this.oUserInfoModel = this.getOwnerComponent().getModel("UserInfo");
            this.oUserAttributesModel = this.getOwnerComponent().getModel("UserAttributes");
            

            this.getView().addEventDelegate({
                onBeforeShow: function(oEvent) {
                    this.getView().setBusy(true);
                    this.myWordCloud = undefined;
                }.bind(this),
                onAfterShow: function(oEvent){
                    if(this.oUserInfoModel.getProperty("/familyName") === undefined || this.oUserAttributesModel.getProperty("/Rank") === undefined) {
                        this.getOwnerComponent().getRouter().navTo("authFail", { bReplace : true});
                        return;
                    }
                    if(this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Student" ||
                        this.getOwnerComponent().getModel("UserAttributes").getProperty("/Rank")[0] === "Teacher") {
                            this.getView().setBusy(false);
                            this._bindItemsToMasterList();
                    } else {
                        this.getOwnerComponent().getRouter().navTo("permissionFail");
                        return;
                    }
                    
                }.bind(this)
            },  this.getView());

        },

        onWordcloudSelect: function(oEvent) {
            var oItem = oEvent.getParameter("item");
            if(oItem.getKey() === "keyword") {
                this._selectKeywords();
                this._generateWordCloud(this.getView().getModel("Keyword").getProperty("/data"));
            } else {
                var sText = this._prepareText(' ' + this.getView().getModel("Text").getProperty("/text") + ' ')
                var aWords = new Array ();
                aWords = sText.split(" ");
                aWords = aWords.filter(x => x != '' && isNaN(x));
                let shuffled = aWords
                    .map((a) => ({sort: Math.random(), value: a}))
                    .sort((a, b) => a.sort - b.sort)
                    .map((a) => a.value);
                this._generateWordCloud(shuffled.slice(0, Math.min(aWords.length, this.getView().getModel("CloudSettings").getProperty("/maxWords"))).map(function(word) {
                    return {text: word, count: 1 + Math.random() * 3};
                }))
            }
        },

        onShowSettingsDialog: function(oEvent) {
            var oView = this.getView();
            if (!this._oSettingsFragment) {
                this._oSettingsFragment = Fragment.load({
                    id: oView.getId(),
                    name:"ui.fragment.WordCloudSettings",
                    controller: this
                }).then(function (oSettingsDialog) {
                    oView.addDependent(oSettingsDialog);
                    return oSettingsDialog;
                });
            }
            this._oSettingsFragment.then(function (oSettingsDialog) {
                oSettingsDialog.open();
            });
        },

        onCloseSettingsDialog: function (oEven) {
            this.byId("wordCloudSettingsDialog").close();
            this.getView().getModel("CloudSettings").refresh();
        },

        onDefaultWordcloudSelected: function(oEvent) {
            this._selectKeywords();
            this._generateWordCloud(this.getView().getModel("Keyword").getProperty("/data"));
        },

        onBackToMasterPress: function() {
            this.byId("splitApp").toMaster(this.createId("masterPage"));
        },

        onListItemPress: function(oEvent) {
            this.byId("thesisTitle").setVisible(true);
            this.byId("splitApp").toDetail(this.createId("detailPage"));
            var oBindingContext = oEvent.getSource().getBindingContext();
            this.getView().bindElement("/Theses(" + oBindingContext.getProperty("thesis_ID") + ")");
            var binding = this.getView().getModel().bindContext("/ThesisText?$filter=thesis_ID eq " + oBindingContext.getProperty("thesis_ID"));

            binding.requestObject().then(function (result) {
                this.getView().getModel("Text").setData(result.value[0]);
                if(result.value.length < 1) {
                    MessageBox.warning(this._getText("WizardEditTypeError"), {icon : MessageBox.Icon.WARNING, title : this._getText("appWarningMsgTitle")});
                    this.byId("generate").setVisible(false);
                    this.getView().getModel("Keyword").setData({"data":[]});
                    this.onCloudDelete();
                } else {
                    this._selectKeywords();
                    this.byId("generate").setVisible(true);
                }
            }.bind(this));
        },

        onCloudDelete: function(oEvent) {
            this.myWordCloud = undefined;
            this.getView().getModel("CloudSettings").setProperty("/enableSizeChange", true);
            this.byId("vboxid").destroy();
            var box = new sap.m.VBox({id:this.createId("vboxid")});
            box.placeAt(this.byId("scrollContainer"));
        },

        _bindItemsToMasterList: function() {
            var prefix = this.oUserInfoModel.getProperty("/neptun");
            var oThesisList = this.byId("thesisList");
            var rankList = this.oUserAttributesModel.getProperty("/Rank");
            var oPath = `/${rankList[0]}s2Theses`;
            var oParam = `${rankList[0].toLowerCase()}_neptun`;
            oThesisList.bindItems({
                path: oPath,
                template: new sap.m.StandardListItem({
                    title: "{thesis/title}",
                    type : sap.m.ListType.Active,
                    press: this.onListItemPress.bind(this)
                }),
                templateShareable: false,
                filters: [
                    new Filter(oParam, "EQ", prefix)
                ],
                parameters: {
                    $expand: 'thesis'
                }
            });
        },

        _prepareText: function(sText) {
            var aStopWords = new Array('a','abban','ahhoz','ahogy','ahol','aki','akik','akkor','alatt','amely','amelyek','amelyekben','amelyeket','amelyet',
            'amelynek','ami','amikor','amit','amolyan','amíg','annak','arra','arról','az','azok','azon','azonban','azt','aztán','azután','azzal','azért','be',
            'belül','benne','bár','cikk','cikkek','cikkeket','csak','de','e','ebben','eddig','egy','egyes','egyetlen','egyik','egyre','egyéb','egész','ehhez',
            'ekkor','el','ellen','elsõ','elég','elõ','elõször','elõtt','emilyen','ennek','erre','ez','ezek','ezen','ezt','ezzel','ezért','fel','felé','ha','hanem',
            'hiszen','hogy','hogyan','igen','ill','ill.','illetve','ilyen','ilyenkor','is','ismét','ison','itt','kell','kellett',
            'keresztül','ki','kívül','között','közül','legalább','legyen','lehet','lehetett','lenne','lenni','lesz','lett','maga','magát','majd','majd','meg',
            'mellett','mely','melyek','mert','mi','mikor','milyen','minden','mindenki','mindent','mindig','mint','mintha','mit','mivel','miért','már',
            'más','másik','még','míg','nagy','nagyobb','nagyon','ne','nekem','neki','nem','nincs','néha','néhány','nélkül','olyan','ott','pedig','rá',
            's','saját','sem','semmi','sok','sokat','sokkal','szemben','szerint','szinte','számára','talán','tehát','teljes','tovább','továbbá','ugyanis',
            'utolsó','után','utána','vagy','vagyis','vagyok','valamint','való','van','vannak','vele','vissza','viszont','volna','volt','voltak',
            'voltam','voltunk','által','általában','át','én','éppen','és','így','õ','õk','õket','össze','úgy','új','újabb','újra');
            
            sText = sText.replace(/\s/g, ' ');
            sText = sText.toLowerCase();
            sText = sText.replace(/[^a-zA-Z0-9äöüáíűúóőéß$%+]/g, ' ');
            for (var m = 0; m < aStopWords.length; m++) {
                sText = sText.replaceAll(' ' + aStopWords[m] + ' ', ' ');
            }
            return sText;
        },

        _selectKeywords: function() {
            var aWords = new Array ();
            var aKeywords = new Object ();

            var sText = this._prepareText(' ' + this.getView().getModel("Text").getProperty("/text") + ' ');      
            aWords = sText.split(" ").filter(x => x != '');
            var iWordCount = aWords.length;
            aWords = new Array();

            aWords = sText.split(" ");

            for (var x = 0; x < aWords.length; x++) {
                var s = aWords[x].replace (/^\s+/, '').replace (/\s+$/, '');
                
                if (aKeywords[s] != undefined) {
                    aKeywords[s]++;
                } else {
                    if (s != '' && isNaN(s)) {
                        aKeywords[s] = 1;
                    }
                }
            }

            var sortable = new Object();
            sortable = Object.fromEntries(
                Object.entries(aKeywords).sort(([,a],[,b]) => b-a)
            );

            var modelData = {"data":[]};
            var n = 0;
            for(var a in sortable) {
                modelData.data.push({"text" : a, "count" : sortable[a], "ratio": Math.round(100 * (sortable[a] / iWordCount), 2)});
                n++;
                if(n > this.getView().getModel("CloudSettings").getProperty("/maxWords")) break;
            }
            this.getView().getModel("Keyword").setData(modelData);
            this.getView().getModel("Keyword").refresh();
        },

        _generateWordCloud: function(oData) {
            new sap.m.Text({
               text : "Hello World"
            }).placeAt(this.byId("vboxid"));
            var id = this.createId("vboxid");
            if(this.myWordCloud === undefined) this.myWordCloud = this._wordCloud("#"+id);
            this.myWordCloud.update(oData);
        },

        _wordCloud: function(selector) {
            var fill = d3.scale.category20();
            var settingsModel = this.getView().getModel("CloudSettings");
            settingsModel.setProperty("/enableSizeChange", false)
            var svg = d3.select(selector).append("svg")
                .attr("width", settingsModel.getProperty("/sizeW"))
                .attr("height", settingsModel.getProperty("/sizeH"))
                .append("g")
                .attr("transform", "translate(" + settingsModel.getProperty("/sizeW")/2 + "," + settingsModel.getProperty("/sizeH")/2 + ")");

            function draw(words) {
                var cloud = svg.selectAll("g text")
                                .data(words, function(d) { return d.text; })

                cloud.enter()
                    .append("text")
                    .style("font-family", settingsModel.getProperty("/font"))
                    .style("fill", function(d, i) { return fill(i); })
                    .attr("text-anchor", "middle")
                    .attr('font-size', 1)
                    .text(function(d) { return d.text; });

                cloud
                    .transition()
                        .duration(600)
                        .style("font-size", function(d) { return d.size + "px"; })
                        .style("font-family", settingsModel.getProperty("/font"))
                        .attr("transform", function(d) {
                            return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                        })
                        .style("fill-opacity", 1);

                cloud.exit()
                    .transition()
                        .duration(200)
                        .style('fill-opacity', 1e-6)
                        .attr('font-size', 1)
                        .remove();
            }
            return {
                update: function(words) {
                    d3.layout.cloud().size([settingsModel.getProperty("/sizeW"), settingsModel.getProperty("/sizeH")])
                        .words(words)
                        .padding(5)
                        .rotate(function() { return ~~(Math.random() * 2) * 90; })
                        .font(settingsModel.getProperty("/font"))
                        .fontSize(function(d) { return 15 + d.count * 10 * Math.random(); })
                        .on("end", draw)
                        .start();
                }
            }

        },

        _getText: function(i18nStr) {
            return this.getView().getModel("i18n").getResourceBundle().getText(i18nStr);
        },
	});
});