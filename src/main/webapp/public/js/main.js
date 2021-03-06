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
app.run(['$q', '$rootScope', '$state', '$http',
         function ($q, $rootScope, $state, $http) {
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
		$state.go("today",{aid:0})
	})
}]);


app.controller("sideTabCtrl", [ '$scope', '$stateParams', "$http",
	function($scope, $stateParams, $http) {

		$scope.$stateParams = $stateParams;
		this.setSideTab = function(item) {
			this.sideTabSelected = item;
		};
		this.sideTabUrl = function() {
			return "/todo/resources/views/" + this.sideTabSelected + ".html";
		};
		this.setSideTab("activity");
	} ]);

app.controller("actListCtrl",
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
			this.lastUpdate=1;
			this.poll=setInterval(function(){
				$http.get("/todo/api/account/lastupdate").success(function(data){
					console.log(actListCtrl);
					if(actListCtrl.lastUpdate!=data){
						actListCtrl.buildList();
						actListCtrl.lastUpdate=data;
					}
				})
			},5000);
			$scope.stateInfo=$state.current.data;
			$scope.stateName = $state.current.name;
			$scope.listUpdated = false;
			$scope.filter = {
				date:new Date()
			};
			if($scope.stateInfo.date){
				$scope.filter.date=dateService.shift(new Date(),$scope.stateInfo.date.shift,$scope.stateInfo.date.type);
			}
			$scope.defaultDate=function(){
				return $scope.filter.date;
			}
			$scope.$watch('listUpdated', function(nv, ov) {
				if (nv == false) {
					$scope.listUpdated = true;
					console.log("listUpdate");
					actListCtrl.buildList();
				}
			});
			this.enter = function(aid, $event) {
				$event.preventDefault();
				$event.stopPropagation();
				$state.go("activity", {
					aid : aid
				})
			}

			//
			this.buildList = function() {
				var req={url:"/todo/api/activity/"+$scope.stateInfo.type+"/" + $stateParams.aid,params:{}};
				if($scope.stateInfo.date){
					var t=dateService.empty($scope.filter.date,$scope.stateInfo.date.type);
					console.log(t[0]);
					console.log(t[1]);
					req.params={
						t1 : t[0],
						t2 : t[1]}
				}
				if($scope.stateInfo.unscheduled){
					req.params.unscheduled=$scope.stateInfo.unscheduled;
				}
				
				activityService.buildList(actListCtrl, req, $stateParams.aid,
					false, function() {
						$scope.$watch("actList.activities[0].open",
							function(nv) {
								if (nv == true) {
									activityService
										.pushNew(actListCtrl.activities);
								}
							})
					});
			};
			this.pushNew = function() {
				activityService.pushNew(actListCtrl.activities);
			}

			this.setActivity = function(n) {
				console.log("set activity " + n + "in")
				console.log(actListCtrl.activities[n].loaded);
				if (n != this.activeActNum) {
					console.log(this.activeActNum);
					if (this.activeActNum >= 0) {
						console.log(this.activeActNum);
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
				for (i in actListCtrl.activities) {
					activityService.submit(actListCtrl.activities[i],
						function() {
						}, $stateParams.aid);
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

app.directive('activityDetail', function(dateService,activityService) {
	return {
		templateUrl : "/todo/resources/views/activity.detail.html",
		controller : "activityDetailCtrl as detailCtrl",
		scope : {
			model : "=model",
			defaultDate:"=?"
		},
		link : function(scope, element, attr) {
			scope.$watch("model",function(nv,ov){
				console.log("detail change");
				console.log(JSON.stringify(ov));
				console.log(JSON.stringify(nv));
				if(nv.data.aid==ov.data.aid&&ov.loaded){
					scope.model.dirty=true;
				}
				scope.model.description=activityService.getDescription(scope.model.data);
			},true);
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
				scope.actList.buildList();
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
