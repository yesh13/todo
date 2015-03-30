{"defaultUrl":"/activity/0",
	"states":
		[
{"name":"focus","data":
	[{"name":"activity","state":{
		"url" : "/activity/:aid",
		"templateUrl" : "activity.html",
		"controller" : "actListCtrl as actList",
		"data":{
			"type":"list"
		}
		}},
		{"name":"today","state":{
			"url" : "/today/:aid",
			"templateUrl" : "activity.html",
			"controller" : "actListCtrl as actList",
			"data":{
				"type":"leaves",
				"date":{
					"type":"day",
					"shift":0
				}
			}
			}
			},
		{"name":"tomorrow","state":{
			"url" : "/tomorrow/:aid",
			"templateUrl" : "activity.html",
			"controller" : "actListCtrl as actList",
			"data":{
				"type":"leaves",
				"date":{
					"type":"day",
					"shift":1
				}
			}
			}
			},
			{"name":"wait","state":{
				"url" : "/wait/:aid",
				"templateUrl" : "activity.html",
				"controller" : "actListCtrl as actList",
				"data":{
					"type":"leaves",
					"unscheduled":"1"
				}
				}
				}

		]
 }
		 ]
}



