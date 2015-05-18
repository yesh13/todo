{"defaultUrl":"/activity/0",
	"states":
		[
{"name":"focus","data":
	[
		{"name":"today","state":{
			"url" : "/today/:aid",
			"templateUrl" : "activity.html",
			"controller" : "activityCtrl as actList",
			"data":{
				"sub":7,
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
			"controller" : "activityCtrl as actList",
			"data":{
				"sub":7,
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
				"controller" : "activityCtrl as actList",
				"data":{
					"sub":8
				}
				}
				}

		]
 }
		 ]
}



