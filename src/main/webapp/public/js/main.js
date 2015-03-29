var app = angular.module("mainApp", [ "ui.router", "ui.bootstrap","services","ui-lib"]);




app.directive('myDatePicker', function() {
	return {
		templateUrl : "/resources/views/my-date-picker.html",
		scope : {
			model : "=model"
		},
		link : function(scope, element, attr) {
			scope.opened = false;
			scope.open = function($event) {
				$event.preventDefault();
				$event.stopPropagation();
				scope.opened = true;
				console.log("opendate "+scope.opened);
			}
		}
	}
});

app.directive('schedulePicker', function(dateService) {
	return {
		templateUrl : "/resources/views/schedule-picker.html",
		scope : {
			model : "=ngModel",
				open:"=isOpen"
		},
		link : function(scope, element, attr) {
			scope.disableSameDate=function(){
				scope.sameDate=false;
			}
			scope.$watch("[model.type,model.aid,open]",function(nv,ov){
				if(nv[2]==true){
				console.log("schedule type from "+ov+" to "+nv);
				switch(nv[0]){
				case '0':
					if(nv[1]==ov[1]&&nv[2]==ov[2]){
						scope.model.endTime=null;
						scope.model.startTime=new Date();
					}else{
						scope.model.startTime=(scope.model.startTime!=null)?scope.model.startTime:new Date();
					}
					scope.sameDate=false;
//					if(typeof(ov[0])!="undefined"){
//						scope.model.startTime=null;
//					}
					break;
				case '1':
					if(nv[1]==ov[1]&&nv[2]==ov[2]){
						scope.model.startTime=new Date();
						scope.model.endTime=new Date();
					}else{
						scope.model.startTime=(scope.model.startTime!=null)?scope.model.startTime:new Date();
						scope.model.endTime=(scope.model.endTime!=null)?scope.model.endTime:new Date();
					}
					scope.sameDate=dateService.sameDate(scope.model.startTime,scope.model.endTime);
					if(scope.sameDate){
						var watchSameDate=scope.$watch("[model.startTime,model.aid]",function(nv,ov){
							if(nv!=ov){
							if(scope.sameDate&&scope.model.type=="1"&&ov[1]==nv[1]){
								console.log("setSame|"+nv+"|"+ov);
								scope.model.endTime=dateService.setSameDate(scope.model.endTime,nv[0]);
							}else{
								console.log("unbind");
								watchSameDate();
							}}
						})
					}
					break;
				case '2':
					if(nv[1]==ov[1]&&nv[2]==ov[2]){
						scope.model.startTime=null;
						var x=new Date();
						x.setDate(x.getDate()+1)
						scope.model.endTime=new Date(x);
					}else{
						scope.model.endTime=(scope.model.endTime!=null)?scope.model.endTime:new Date();
					}
					scope.sameDate=false;
					if(typeof(ov[0])!="undefined"){
						scope.model.startTime=null;
					}
					break;
				}}
			});
			scope.finish= function(){
				switch(scope.model.type){
				case "0":
					if(scope.model.endTime==null){
						scope.model.endTime=new Date();
					}
					break;
				case "2":
					if(scope.model.startTime==null){
						scope.model.startTime=new Date();
					}
					break;
				}
			}
			
		}
	}
});



app.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state('activity', {
		url : '/activity/:aid',
		templateUrl : 'activity.html',
		controller : 'actListCtrl as actList',
	});
	$stateProvider.state('leaves', {
		url : '/leaves/:aid',
		templateUrl : 'activity.html',
		controller : 'actListCtrl as actList'
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
			'$state','dateService','activityService',
			function($scope, $http, $stateParams,$state,dateService,activityService) {
				console.log("initActCtrl");
				var actListCtrl = this;
				this.activeActNum = -1;
				this.activities = [];
				$scope.stateName=$state.current.name;
				$scope.listUpdated=false;
				$scope.filter={date:new Date()};
				$scope.$watch('listUpdated',function(nv,ov){
					if(nv==false){
						$scope.listUpdated=true;
						console.log("listUpdate");
						actListCtrl.buildList();
					}
				});
				this.enter=function(aid,$event){
					$event.preventDefault();
					$event.stopPropagation();
					$state.go("activity",{aid:aid})
				}
				
//
				this.buildList = function() {
					switch($scope.stateName){
					case "activity":
					req={
						url:"/api/activity/list/"+$stateParams.aid
					};
						break;
					case "leaves":
						console.log($scope.filter.date);
						t=dateService.emptyTime($scope.filter.date);
					req={
						url:"/api/activity/leaves/"+$stateParams.aid,
						params:{
							t1:t[0],
								t2:t[1]
						}
					};
						break;
					}
					activityService.buildList(actListCtrl, req,$stateParams.aid);
				};
				this.pushNew=function(){
					activityService.pushNew(actListCtrl.activities);
				}
				
				this.setActivity = function(n) {
					console.log("set activity "+n+"in")
					console.log(actListCtrl.activities[n].loaded);
					if (n != this.activeActNum) {
						if(this.activities[n].data.aid==null){
							activityService.pushNew(this.activities);
						}
						if(this.activeActNum>=0){
							activityService.submit(actListCtrl.activities[this.activeActNum],actListCtrl.buildList,$stateParams.aid);
						}
						activityService.loadActivity(actListCtrl.activities[n]);
						this.activeActNum = n;
					}
				}
				this.submit = function(n) {
					activityService.submit(actListCtrl.activities[n],null,$stateParams.aid);
				};
				this.moveToTrash = function(aid,$event) {
					$event.preventDefault();
					$event.stopPropagation();
					activityService.moveToTrash(this,aid);
				}
				$scope.$on("$destroy",function(){
					for(i in actListCtrl.activities){
						activityService.submit(actListCtrl.activities[i],function(){},$stateParams.aid);
					}
				})
				window.onbeforeunload = function(){
					console.log("submitall");
					var needSubmit=false;
					for(i in actListCtrl.activities){
						if(actListCtrl.activities[i].loaded==true){
							needSubmit=true;
						}
						activityService.submit(actListCtrl.activities[i],function(){},$stateParams.aid);
					}
					if(needSubmit){
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
		scope:{
			model:"=model"
		},
		link : function(scope, element, attr) {
		}
	}
});
app.controller('activityDetailCtrl', [ '$scope', '$http',
	function($scope, $http) {
		var ctrl = this;

	} ]);
app.directive('activityAccordion', function(activityService,$stateParams) {
	function link(scope, element, attr) {
		scope.$watch('item.open', function(nv, ov) {
			if(nv!=ov){
			console.log("open nv:"+nv+" ov:"+ov+" from "+scope.item.data.aid);
			
			// close from hand or buildList
			if (nv == false&&ov==true) {
				activityService.submit(scope.item,scope.buildList,$stateParams.aid);
				// open
			} else if(nv==true){
				console.log("load:" + scope.item.data.aid);
				if(scope.item.data.aid==null){
					console.log("pushNew "+scope.item.loaded);
					scope.pushNew();
				}
				activityService.loadActivity(scope.item);
			}
			}
		})
	}
	return {
		link : link,
		scope: {
			buildList:"&buildList",
			pushNew:"&pushNew",
			item:"=model"
		}
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


app.directive('listHeader',['$state',function($state){
	return {
		restrict :'EA',
		templateUrl:'/resources/views/list-header.html',
		link : link
	}
	function link(scope, element, attr) {
		scope.$watch("filter.date",function(nv,ov){
			if(nv!=ov){
				scope.actList.buildList();
			}
		})
	}
}])


app.directive("listTitle",function(){
	return {
		restrict :'EA',
		templateUrl:'/resources/views/list-title.html',
		link : link
	}
	function link(scope, element, attr) {

	}
})
