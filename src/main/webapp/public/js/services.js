var app = angular.module("services", [])

app
	.service(
		"dateService",
		function() {
			this.formatInt = function(value, size) {
				var s = value + "";
				while (s.length < size)
					s = "0" + s;
				return s;
			}
			this.week = [ "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" ];
			this.sameDate = function(d1, d2) {
				if (d1 == null || d2 == null)
					return false;
				return d1.getFullYear() == d2.getFullYear()
					&& d1.getDate() == d2.getDate()
					&& d1.getMonth() == d2.getMonth();
			};
			this.diffInDays = function(d1, d2) {
				if (d1 == null || d2 == null)
					return 0;
				return Math
					.round((this.emptyTime(d1)[0] - this.emptyTime(d2)[0]) / 86400000);

			}
			this.setSameDate = function(d1, d2) {
				console.log(d1);
				d1.setFullYear(d2.getFullYear());
				d1.setMonth(d2.getMonth());
				d1.setDate(d2.getDate());
				return new Date(d1.getTime());
			};
			this.stringToDate = function(act) {
				act.endTime = act.endTime != null ? new Date(act.endTime)
					: null;
				act.startTime = act.startTime != null ? new Date(act.startTime)
					: null;
				act.finishTime = act.finishTime != null ? new Date(
					act.finishTime) : null;
			};
			this.shift = function(date, n, type) {
				date = new Date(date);
				switch (type) {
				case "day":
					return d = new Date(date.setDate(date.getDate() + n));
				case "week":
					return new Date(date.setDate(date.getDate() + 7 * n));
				case "month":
					return new Date(date.setMonth(date.getMonth() + n));
				}
			}
			this.empty = function(date, type) {
				switch (type) {
				case "day":
					return this.emptyTime(date);
				case "week":
					return this.emptyDay(date);
				case "month":
					return this.emptyDate(date);
				}
			}
			this.emptyTime = function(date) {
				date = new Date(date);
				date.setHours(0);
				date.setMinutes(0);
				date.setSeconds(0);
				date.setMilliseconds(0);
				return [ new Date(date),
					new Date(date.setDate(date.getDate() + 1)) ];
			}
			this.emptyDate = function(date) {
				date = new Date(date);
				date = this.emptyTime(date)[0];
				date.setDate(0);
				return [ new Date(date),
					new Date(date.setMonth(date.getMonth() + 1)) ];
			}
			this.emptyDay = function(date) {
				date = new Date(date);
				date = this.emptyTime(date)[0];
				date.setDate(date.getDate() - (date.getDay() + 6) % 7);
				return [ new Date(date),
					new Date(date.setDate(date.getDate() + 7)) ];
			}
			this.abbreviate = function(date, forbitTime) {
				console.log(date instanceof Date);
				if (date == null)
					return "";
				today = new Date();
				if (this.sameDate(date, today)) {
					if (forbitTime) {
						return "today";
					} else {
						return date.getHours() + ":"
							+ this.formatInt(date.getMinutes(), 2);
					}
				} else {
					var diff = this.diffInDays(date, today);
					var absdiff = diff > 0 ? diff : -diff;
					if (absdiff < 10) {
						if (diff > 0) {
							return absdiff + " days later";
						} else {
							return absdiff + " days before";
						}
					} else if (absdiff < 60) {
						return (date.getMonth() + 1) + "/" + date.getDate();
					} else {
						return date.getFullYear() + "/" + (date.getMonth() + 1)
							+ "/" + date.getDate();
					}
				}
			}

		})

app.service("activityService",
	[
		'$http',
		'dateService',
		function($http, dateService) {
			var service = this;
			this.getDescription = function(act) {
				switch (act.type) {
				case "0":
					if (act.endTime != null) {
						return "Finished "
							+ dateService.abbreviate(act.endTime, true);
					} else if (act.startTime != null) {
						return dateService.abbreviate(act.startTime, true);
					} else {
						return "";
					}
				case "1":
					if (dateService.sameDate(act.startTime, act.endTime)) {
						return dateService.abbreviate(act.startTime);
					}
					return "from " + dateService.abbreviate(act.startTime)
						+ " to " + dateService.abbreviate(act.endTime);
				case "2":
					if (act.finishTime != null) {
						return "Finished "
							+ dateService.abbreviate(act.finishTime);
					} else {
						return "Deadline "
							+ dateService.abbreviate(act.endTime);
					}
				}
			}
			this.moveToTrash = function(context, aid) {
				console.log("move " + aid + " to trash");
				if (aid != null) {
					$http.get("/todo/api/activity/remove/" + aid).success(function(data) {
						if(data=="Successful")
						context.buildActivity();
					})
				} 
			}
			this.submit = function(context, callback) {
				console.log("edit: " + JSON.stringify(context.data));
				$http.post("/todo/api/activity/update/" + context.data.aid,
					context.data).success(function() {
					console.log("submit success");
					callback();
				});
			}

			// context: activity
			this.loadActivity = function(context, callback) {
				console.log(JSON.stringify(context));
				var aidString;
				// new item
				if (typeof (context.data.aid) === "undefined"
					|| context.data.aid == null) {
					console.log("load new");
					aidString = "new";
				} else {
					console.log("load exist:" + context.data.aid);
					aidString = context.data.aid;
				}
				$http.get("/todo/api/activity/detail/" + aidString).success(
					function(response) {
						// detail=true means it has been loaded, must be
						// submited
						console.log("load success");
						context.data.note = response.note;
						context.data.name = response.name;
						console.log(JSON.stringify(context.data));
						context.loaded = true;
						if (aidString == "new") {
							dateService.stringToDate(response);
							context.data = response;
						}
						if (typeof (callback) != "undefined") {
							callback(context);
						}
					});
			}
			this.pushNew = function(alist) {
				console.log("pushNew alist");
				alist.unshift({
					data : {
						name : "New"
					},
					loaded : false,
					open : false
				});
			}
			// context: actListCtrl
			this.buildActivity = function(req, callback) {
				req.method = "POST";
				$http(req).success(function(response) {

					/*
					 * // read ISO string for(iter in context.activities){
					 * dateService.stringToDate(context.activities[iter].data); }
					 *  // recover active activity //loaded item
					 * exist var exist = false; for (iter in actStore) {
					 * console.log("recover:" + actStore[iter].data.aid); for (i =
					 * 0; i < context.activities.length; i++) { if
					 * (context.activities[i].data.aid ==
					 * actStore[iter].data.aid) { context.activeActNum = i;
					 * context.activities[i] = actStore[iter]; // recover new if
					 * (i == 0) { console.log("recover new");
					 * service.pushNew(context.activities);
					 * context.activeActNum=1; } exist = true; break; } } } if
					 * (!exist) { context.activeActNum = -1; } for (i in
					 * context.activities){
					 * context.activities[i].description=service.getDescription(context.activities[i].data); }
					 * console.log("build success");
					 */
					if (callback instanceof Function) {
						callback(response);
					}
				});
			}
		} ])
