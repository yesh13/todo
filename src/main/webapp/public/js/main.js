var app = angular.module("mainApp", [ "ui.router", "ui.bootstrap",
	"ngQuickDate" ]);

app.directive('myDatePicker',function(){
	return {
		templateUrl:"/resources/views/my-date-picker.html",
			scope:{
				ngModel:"="
			},
			link:function(scope,element,attr){
				scope.td=new Date();
				scope.opened=false;
				scope.open=function($event){
					$event.preventDefault();
					$event.stopPropagation();
					scope.opened=!scope.opened;
				}
			}
	}
});

app.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state('activity', {
		url : '/activity/:aid',
		templateUrl : 'activity.html',
		controller : 'actListCtrl as actList'
	});
	$stateProvider.state('leaves', {
		url : '/leaves/:aid',
		templateUrl : 'activity.html',
		controller : 'leavesCtrl as actList'
	});
	$urlRouterProvider.otherwise('/activity/0');
});

app.controller("sideTabCtrl", [ '$scope', '$stateParams',
	function($scope, $stateParams) {
		$scope.$stateParams = $stateParams;
		this.setSideTab = function(item) {
			this.sideTabSelected = item;
		};
		this.sideTabUrl = function() {
			return "/resources/views/" + this.sideTabSelected + ".html";
		};
		this.setSideTab("activity");
	} ]);

app
	.controller(
		"actListCtrl",
		[
			'$scope',
			'$http',
			'$stateParams',
			function($scope, $http, $stateParams) {
				$scope.td=new Date();
				console.log("initActCtrl");
				var actListCtrl = this;
				this.activeActNum = -1;
				this.activities = [];
				this.isDragging = false;
				this.setIsDragging = function(b) {
					this.isDragging = b;
					$scope.$digest();// trigger data-binding
				}
				this.digestAll = function() {
					$scope.$digest();
				}
				this.pushNew=function(){
					actListCtrl.activities.push({
						name : "New"
					});
				}
				this.buildList = function() {
					this.detailLoaded=[];
					console.log("build list");
					// stoer active activity
					if (this.activeActNum >= 0) {
						var activeAid = this.activities[this.activeActNum].aid;
						var activeAct = this.activities[this.activeActNum];
					}
					$http
						.get("/api/activity/list/" + $stateParams.aid)
						.success(
							function(response) {
								actListCtrl.activities = response;
								actListCtrl.pushNew();
								$http.get(
									"/api/activity/path/" + $stateParams.aid)
									.success(function(response) {
										actListCtrl.actPath = response;
									});
								// recover active activity
								if(typeof(activeAct)!="undefined"){
									console.log("recover:"+activeAid);
									var exist=false;
								for (i = 0; i < actListCtrl.activities.length; i++) {
									if (actListCtrl.activities[i].aid === activeAid) {
										actListCtrl.activeActNum = i;
										actListCtrl.activities[i] = activeAct;
										//recover new
										if(i==actListCtrl.activities.length-1){
											actListCtrl.pushNew();
										}
										actListCtrl.detailLoaded[i]=true;
										exist=true;
										break;
									}
								}
								if(!exist){
									actListCtrl.activeActNum =-1;
								}
								}
							});
				};
				this.buildList();
				this.loadActivity = function(n) {
					this.activeActNum = n;
					// new item
					if (typeof (this.activities[n].aid) === "undefined") {
						console.log("load new:"+n);
						//detail=true means it has been loaded, must be submited
						actListCtrl.detailLoaded[n]=true;
						this.activities[n] = {};
						actListCtrl.pushNew();
					} else {
						console.log("load exist:"+n);
						var aid = this.activities[n].aid;
						$http
							.get("/api/activity/detail/" + aid)
							.success(
								function(response) {
									//detail=true means it has been loaded, must be submited
									actListCtrl.detailLoaded[n]=true;
									
									actListCtrl.activities[n].note = response.note;
									actListCtrl.activities[n].endTime = actListCtrl.activities[n].endTime ? new Date(
										actListCtrl.activities[n].endTime)
										: null;
									actListCtrl.activities[n].startTime = actListCtrl.activities[n].startTime ? new Date(
										actListCtrl.activities[n].startTime)
										: null;
									// read ISO string
								});
					}
				};
				this.setActivity=function(n){
					if(n!=this.activeActNum){
					this.submit(this.activeActNum);
					this.activeActNum=n;
					this.loadActivity(n);
					}
					
				}
				this.submit = function(n) {
					console.log("submittest:"+this.detailLoaded[n]);
					if(this.detailLoaded[n]){
						this.detailLoaded[n]=false;
					
					if (typeof (this.activities[n].aid) === "undefined") {
						this.activities[n].parent = $stateParams.aid;
						console.log("new: "
							+ JSON.stringify(this.activities[n]));
						$http.post("/api/activity", this.activities[n])
							.success(function() {
								actListCtrl.buildList();
							});
					} else {
						console.log("edit: "
							+ JSON.stringify(this.activities[n]));
						$http.post("/api/activity/" + this.activities[n].aid,
							this.activities[n]).success(function() {
							actListCtrl.buildList();
						});
					}
					}
				};
				$scope.setBeingDragged = function(n) {
					$scope.beingDragged = n;
					$scope.$digest();
				}
				this.moveToTrash = function(aid) {
					console.log("move " + aid + " to trash");
					$http.post("/api/activity/" + aid, {
						parent : 1
					}).success(function() {
						actListCtrl.buildList();
					});
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
				$http.post("/api/activity/" + scope.beingDragged, move)
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

app.directive('activityDetail', function() {
	return {
		templateUrl : "/resources/views/activity.detail.html",
		controller : "activityDetailCtrl as detailCtrl",
		link : function(scope, element, attr) {
			// console.log(scope.$eval(attr.activityIndex));
			scope.$watch(attr.activityIndex, function(value) {
				// console.log(scope.$eval(attr.activityIndex));
				scope.activeActNum = value;
			});
		}
	}
});
app.controller('activityDetailCtrl', [ '$scope', '$http',
	function($scope, $http) {
		var ctrl = this;

	} ]);
app.directive('activityAccordion', function() {
	function link(scope, element, attr) {
		scope.open = false;
		scope.$watch('open', function(nv, ov) {
			// close from hand or buildList
			if (nv == false) {
				if (scope.actList.activeActNum == scope.$index) {
					scope.actList.activeActNum = -1;
				}
					scope.actList.submit(scope.$index)
				// open
			} else {
				console.log("load:" + scope.$index);
				scope.actList.loadActivity(scope.$index);
			}
		})
		scope.$watch('actList.activeActNum', function(nv, ov) {
			scope.open=(scope.actList.activeActNum==scope.$index);
		})
		scope.moveToTrash=function(aid,$event){
			$event.preventDefault();
			$event.stopPropagation();
			scope.actList.moveToTrash(aid);
		}
	}
	return {
		link : link
	}
})

app.directive('signOut', [ "$http", "$location", "$window",
	function($http, $location, $window) {
		return function(scope, element, attr) {
			element.on("click", function() {
				// $http.post("/j_spring_security_logout").success(function() {
				$window.location.href = "/j_spring_security_logout";
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

app.directive('datepickerPopup', function (){
	  return {
	    restrict: 'EAC',
	    require: 'ngModel',
	    link: function(scope, element, attr, controller) {
	      //remove the default formatter from the input directive to prevent conflict
	      controller.$formatters.shift();
	    }
	  }
	});

