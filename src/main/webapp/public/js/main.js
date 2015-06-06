var app = angular.module("mainApp", [ "ui.router", "ui.bootstrap", "services",
		"ui-lib" ]);

app.directive('myDatePicker', function() {
	return {
		templateUrl : "/todo/resources/views/my-date-picker.html",
	scope : {
		model : "=model"
	},
	link : function(scope, element, attr) {
			   scope.opened = false;
			   scope.open = function($event) {
				   $event.preventDefault();
				   $event.stopPropagation();
				   scope.opened = true;
				   console.log("opendate " + scope.opened);
			   }
		   }
	}
});

app
.directive(
		'schedulePicker',
		function(dateService) {
			return {
				templateUrl : "/todo/resources/views/schedule-picker.html",
	scope : {
		model : "=ngModel",
	open : "=isOpen",
	defaultDate:"=?",
	description:"=?"
	},
	link : function(scope, element, attr) {
			   scope.disableSameDate = function() {
				   scope.sameDate = false;
			   }
			   scope
	.$watch(
		"[model.type,model.aid,open]",
		function(nv, ov) {
			if (nv[2] == true) {
				console.log("schedule type from " + ov
					+ " to " + nv);
				switch (nv[0]) {
					case '0':
						if (nv[1] == ov[1] && nv[2] == ov[2]) {
							scope.model.endTime = null;
							scope.model.startTime = null;
						}
						scope.sameDate = false;
						// if(typeof(ov[0])!="undefined"){
						// scope.model.startTime=null;
						// }
						break;
					case '1':
						if (nv[1] == ov[1] && nv[2] == ov[2]) {
							scope.model.startTime = scope.defaultDate;
							scope.model.endTime = scope.defaultDate;
						} else {
							scope.model.startTime = (scope.model.startTime != null) ? scope.model.startTime
								: scope.defaultDate;
							scope.model.endTime = (scope.model.endTime != null) ? scope.model.endTime
								: scope.defaultDate;
						}
						scope.sameDate = dateService.sameDate(
								scope.model.startTime,
								scope.model.endTime);
						if (scope.sameDate) {
							var watchSameDate = scope
								.$watch(
										"[model.startTime,model.aid]",
										function(nv, ov) {
											if (nv != ov) {
												if (scope.sameDate
													&& scope.model.type == "1"
													&& ov[1] == nv[1]) {
													console
									.log("setSame|"
										+ nv
										+ "|"
										+ ov);
								scope.model.endTime = dateService
									.setSameDate(
										scope.model.endTime,
										nv[0]);
												} else {
													console
									.log("unbind");
								watchSameDate();
												}
											}
										})
						}
						break;
					case '2':
						if (nv[1] == ov[1] && nv[2] == ov[2]) {
							scope.model.finishTime = null;
							scope.model.startTime = null;
							var x = new Date(scope.defaultDate);
							x.setDate(x.getDate() + 1);
							scope.model.endTime = x;
						} else {
							scope.model.endTime = (scope.model.endTime != null) ? scope.model.endTime
								: scope.defaultDate;
						}
						scope.sameDate = false;
						break;
				}
			}
		});
scope.finish = function() {
	switch (scope.model.type) {
		case "0":
			if (scope.model.endTime == null) {
				scope.model.endTime = new Date();
			}
			break;
		case "2":
			if (scope.model.finishTime == null) {
				scope.model.finishTime = new Date();
			}
			break;
	}
}
scope.schedule=function(){
	scope.model.startTime = scope.defaultDate;
}

}
}
});

var stateProviderRef = null;
var urlRouterProviderRef = null;

app.config(function($stateProvider, $urlRouterProvider) {
	stateProviderRef=$stateProvider;
	urlRouterProviderRef=$urlRouterProvider;
});
app.run(['$q', '$rootScope', '$state', '$stateParams', '$http',
		function ($q, $rootScope, $state,$stateParams, $http) {
			$http.get("/todo/resources/json/router.js").success(function(data) {
				urlRouterProviderRef.otherwise(data.defaultUrl);
				for (i in data.states) {
					for (j in data.states[i].data) {
						var state=data.states[i].data[j];
						state.ref=state.name+"({aid:$stateParams.aid})"
				stateProviderRef.state(state.name,state.state);
					}
				}
				$rootScope.routerState=data;
				if($state.current.views==null)
				$state.go("today",{aid:0})
			})
		}]);


app.controller("sideTabCtrl", [ '$scope', '$stateParams', "$http","$state",
		function($scope, $stateParams, $http,$state) {

			$scope.$stateParams = $stateParams;
			$scope.filter = {
				date:new Date()
			};
			this.setSideTab = function(item) {
				this.sideTabSelected = item;
			};
			this.sideTabUrl = function() {
				return "/todo/resources/views/" + this.sideTabSelected + ".html";
			};
		} ]);

app.controller("activityCtrl",
		[
		'$scope',
		'$http',
		'$stateParams',
		'$state',
		'dateService',
		'activityService',
		function($scope, $http, $stateParams, $state, dateService,
			activityService) {
			console.log("initActCtrl");
			var actListCtrl = this;
			this.activeActNum = -1;
			this.activities = [];
			this.updateQueue= [];
			this.lastUpdate=1;
			$scope.stateInfo=$state.current.data;
			$scope.stateName = $state.current.name;
			if($scope.filter.name!=$scope.stateName){
				$scope.filter.name=$scope.stateName;
				if($scope.stateInfo.date){
					$scope.filter.date=dateService.shift(new Date(),$scope.stateInfo.date.shift,$scope.stateInfo.date.type);
				}
				if($scope.stateInfo&&typeof($scope.stateInfo.sub)!='undefined'){
					$scope.filter.sub=$scope.stateInfo.sub;
				}
			}
			this.poll=setInterval(function(){
				$http.get("/todo/api/account/lastupdate").success(function(data){
					console.log("update activity");
					if(actListCtrl.lastUpdate!=data&&actListCtrl.lastUpdate!=1){
						actListCtrl.buildActivity();
						actListCtrl.lastUpdate=data;
					}
				})
				for (i in actListCtrl.updateQueue) {
					act=actListCtrl.updateQueue[i];
					if(new Date()-act.inQueue>5000)
				activityService.submit(act,
					function() {
						act.inQueue=null;
						actListCtrl.updateQueue.splice(i,1);
					});
				}
			},5000);
			$scope.$watch("actList.activity.data",
					function(nv,ov) {
						if(ov==null||nv.aid!=ov.aid) return;
						if(actListCtrl.activity.inQueue==null)
				actListCtrl.updateQueue.push(actListCtrl.activity);
			actListCtrl.activity.inQueue=new Date();
					},true)
			$scope.listUpdated = false;
			$scope.defaultDate=function(){
				return $scope.filter.date;
			}
			$scope.$watch('listUpdated', function(nv, ov) {
				if (nv == false) {
					$scope.listUpdated = true;
					console.log("listUpdate");
					actListCtrl.buildActivity();
				}
			});
			this.enter = function(aid,$event) {
				$event.preventDefault();
				$event.stopPropagation();
				$state.go($scope.stateName, {
					aid : aid
				})
			}
			this.newNote=function($event){
				$http.get("/todo/api/activity/newnote/"+actListCtrl.activity.data.aid).success(function(data){
					actListCtrl.enter(data.aid,$event);
				}) 
			}
			this.newAppt=function($event){
				$http.get("/todo/api/activity/newappt/"+actListCtrl.activity.data.aid).success(function(data){
					actListCtrl.enter(data.aid,$event);
				}) 
			}
			this.newTask=function($event){
				$http.get("/todo/api/activity/newtask/"+actListCtrl.activity.data.aid).success(function(data){
					actListCtrl.enter(data.aid,$event);
				}) 
			}
			this.newPend=function($event){
				$http.get("/todo/api/activity/newpend/"+actListCtrl.activity.data.aid).success(function(data){
					actListCtrl.enter(data.aid,$event);
				}) 
			}

			//
			this.buildActivity = function() {
				var req={url:"/todo/api/activity/fetch/" + $stateParams.aid,params:{}};
				req.data={sub:$scope.filter.sub}
				if($scope.stateInfo.date){
					var t=dateService.empty($scope.filter.date,$scope.stateInfo.date.type);
					console.log("build from "+t[0]+" to "+t[1]);

					req.data.t1=t[0]-0;
					req.data.t2=t[1]-0;
				}

				activityService.buildActivity(req, function(data) {
					actListCtrl.activity={"data":data,"dirty":false,"inQueue":null};
					console.log("build: "+JSON.stringify(actListCtrl.activity.data));
					$http.get( "/todo/api/activity/path/" + data.aid)
					.success(function(response) {
						actListCtrl.activity.actPath =response; });
				});
			};
			this.pushNew = function() {
				activityService.pushNew(actListCtrl.activities);
			}

			this.setActivity = function(n) {
				console.log("set activity " + n + "in")
					if (n != this.activeActNum) {
						if (this.activeActNum >= 0) {
							activityService.submit(
									actListCtrl.activities[this.activeActNum],
									actListCtrl.buildList, $stateParams.aid);
						}
						activityService.loadActivity(actListCtrl.activities[n]);
						this.activeActNum = n;
						if (this.activities[n].data.aid == null) {
							console.log("push new plus active");
							activityService.pushNew(this.activities);
							this.activeActNum = n + 1;
							console.log(this.activeActNum);
						}
					}
			}
			this.submit = function(n) {
				activityService.submit(actListCtrl.activities[n], null,
						$stateParams.aid);
			};
			this.moveToTrash = function(aid, $event) {
				$event.preventDefault();
				$event.stopPropagation();
				activityService.moveToTrash(this, aid);
			}
			$scope.$on("$destroy", function() {
				clearInterval(actListCtrl.poll);
				for (i in actListCtrl.updateQueue) {
					act=actListCtrl.updateQueue[i];
					activityService.submit(act,
						function() {
							act.inQueue=null;
							console.log("submit: "+act.data.aid);
							actListCtrl.updateQueue.splice(i,1);
						});
				}
			})
			window.onbeforeunload = function() {
				console.log("submitall");
				var needSubmit = false;
				for (i in actListCtrl.activities) {
					if (actListCtrl.activities[i].dirty == true) {
						needSubmit = true;
					}
					activityService.submit(actListCtrl.activities[i],
							function() {
							}, $stateParams.aid);
				}
				if (needSubmit) {
					return "Please wait a second for saving.";
				}
			}
		} ]);

app.directive('draggableActivity', function() {
	return function(scope, element, attr) {
		element.on('dragstart', function(event) {
			console.log("dragstart");
			// Prevent default dragging of selected content
			scope.setBeingDragged(scope.$eval(attr.draggableActivity));
			scope.setIsDragging(true);
		});
		element.on('dragend', function(event) {
			console.log("dragend");
			// Prevent default dragging of selected content
			console.log(scope.isDragging);
			scope.setIsDragging(false);
		});
	};
});
app.directive('droppableActivity', [
		'$http',
		function($http) {
			return function(scope, element, attr) {
				element.on('drop', function(event) {
					// Prevent default dragging of selected content
					console.log("beDroped");
					var droped = scope.$eval(attr.droppableActivity);
					console.log(scope.beingDragged + "->" + droped);
					var move = {
						parent : droped
					};
					$http.post("/todo/api/activity/" + scope.beingDragged, move)
					.success(function() {
						scope.initList();
					});
				});
				element.on('dragover', function(event) {
					console.log("dragover");
					var droped = scope.$eval(attr.droppableActivity);
					if (droped != scope.beingDragged) {
						event.preventDefault();
					}
				});
			};
		} ]);
app.directive('activityList', function(dateService,activityService) {
	return {
		templateUrl : "/todo/resources/views/activity.list.html",
	controller : "activityListCtrl as listCtrl",
	scope : {
		model : "=model",
	title : "@title",
	method:"=method",
	newMethod:"&newMethod"
	},
	link : function(scope, element, attr) {
			   console.log(typeof(scope.newMethod));
		   }
	}
});
app.controller('activityListCtrl', [ '$scope', '$http',
		function($scope, $http) {
			var ctrl = this;

		} ]);
app.directive('activityDetail', function(dateService,activityService) {
	return {
		templateUrl : "/todo/resources/views/activity.detail.html",
	controller : "activityDetailCtrl as detailCtrl",
	scope : {
		model : "=model",
	defaultDate:"=?"
	},
	link : function(scope, element, attr) {
		   }
	}
});
app.controller('activityDetailCtrl', [ '$scope', '$http',
		function($scope, $http) {
			var ctrl = this;

		} ]);
app.directive('activityAccordion', function(activityService, $stateParams) {
	function link(scope, element, attr) {
		scope.$watch('item.open', function(nv, ov) {
			if (nv != ov) {
				console.log("open nv:" + nv + " ov:" + ov + " from "
					+ scope.item.data.aid);

				// close from hand or buildList
				if (nv == false && ov == true) {
					activityService.submit(scope.item, scope.buildList,
						$stateParams.aid);
					// open
				} else if (nv == true) {

					console.log("load:" + scope.item.data.aid);
					activityService.loadActivity(scope.item);

				}
			}
		})
	}
	return {
		link : link,
			 scope : {
				 buildList : "&buildList",
				 pushNew : "&pushNew",
				 item : "=model"
			 }
	}
})

app.directive('signOut', [ "$http", "$location", "$window",
		function($http, $location, $window) {
			return function(scope, element, attr) {
				element.on("click", function() {
					// $http.post("/j_spring_security_logout").success(function() {
					$window.location.href = "/todo/j_spring_security_logout";
					// });
				})
			};
		} ]);
// for test
app.directive('showSize', function() {
	return function(scope, element, attr) {
		window.alert("height:" + element[0].offsetHeight + ", width: "
			+ element[0].offsetWidth);
	};
});

app.directive('datepickerPopup', function() {
	return {
		restrict : 'EAC',
	require : 'ngModel',
	link : function(scope, element, attr, controller) {
		// remove the default formatter from the input directive to prevent
		// conflict
		controller.$formatters.shift();
	}
	}
});

app.directive('listHeader', [ '$state', function($state) {
	return {
		restrict : 'EA',
	templateUrl : '/todo/resources/views/list-header.html',
	link : link
	}
	function link(scope, element, attr) {
		scope.$watch("filter.date", function(nv, ov) {
			if (nv != ov) {
				scope.actList.buildActivity();
			}
		})
	}
} ])

app.directive("listTitle", function() {
	return {
		restrict : 'EA',
	templateUrl : '/todo/resources/views/list-title.html',
	link : link
	}
	function link(scope, element, attr) {

	}
})
