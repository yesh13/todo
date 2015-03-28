var app = angular.module("mainApp", [ "ui.router", "ui.bootstrap",
	"ngQuickDate" ]);

app.service("dateService",function(){
	this.sameDate=function(d1,d2){
		return d1.getFullYear()==d2.getFullYear()
		&&d1.getDate()==d2.getDate()
		&&d1.getMonth()==d2.getMonth();
	};
	this.setSameDate=function(d1,d2){
		d1.setFullYear(d2.getFullYear());
		d1.setMonth(d2.getMonth());
		d1.setDate(d2.getDate());
		return new Date(d1.getTime());
	};
	this.stringToDate=function(act){
		act.endTime = act.endTime!=null ? new Date(
			act.endTime)
			: null;
		act.startTime = act.startTime!=null ? new Date(
			act.startTime)
			: null;
	};
	this.emptyTime=function(date){
		date=new Date(date);
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.setMilliseconds(0);
		return [new Date(date),new Date(date.setDate(date.getDate()+1))];
	}
	this.emptyDate=function(date){
		date=new Date(date);
		date=this.emptyTime(date);
		date.setDate(0);
		return [new Date(date),new Date(date.setMonth(date.getMonth()+1))];
	}
	this.emptyDay=function(date){
		date=new Date(date);
		date=this.emptyTime(date);
		date.setDate(date.getDate()-date.getDay());
		return [new Date(date),new Date(date.setDate(date.getDate()+7))];
	}
	
})


app.service("activityService",[ '$http','$stateParams','dateService',function($http,$stateParams,dateService){
	var service=this;
	this.moveToTrash=function(context,aid){
		console.log("move " + aid + " to trash");
		if(aid!=null){
		$http.post("/api/activity/" + aid, {
			parent : 1
		}).success(function() {
			context.buildList();
		})}else{
			context.activities[context.activities.length-2].loaded=false;
			context.buildList();
		}
	}
	this.submit=function(context,callback){
		console.log("submittest:" + context.loaded+"from "+context.data.aid);
		if (context.loaded) {
			context.loaded = false;

			if (context.data.aid == null) {
				context.data.parent = $stateParams.aid;
				console.log("new: "
					+ JSON.stringify(context.data));
				$http.post("/api/activity", context.data)
					.success(function() {
						callback();
					});
			} else {
				console.log("edit: "
					+ JSON.stringify(context.data));
				$http.post(
					"/api/activity/" + context.data.aid,
					context.data).success(function() {
						console.log("submit success");
					callback();
				});
			}
		}
	}
	
	//context: activity
	this.loadActivity=function(context){
		console.log(JSON.stringify(context));
		var aidString;
		// new item
		if (typeof(context.data.aid)==="undefined"||context.data.aid==null) {
			console.log("load new");
			aidString = "new";
		} else {
			console.log("load exist:" + context.data.aid);
			aidString = context.data.aid;
		}
		$http
			.get("/api/activity/detail/" + aidString)
			.success(
				function(response) {
					// detail=true means it has been loaded, must be
					// submited
					console.log("load success");
					context.loaded = true;
					context.data.note = response.note;
					if(aidString=="new"){
						dateService.stringToDate(response);
						context.data=response;
					}
				});
	}
	this.pushNew=function(alist){
		console.log("pushNew alist");
		alist.push({data:{
			name : "New"
		},loaded:false});
	}
	//context: actListCtrl
	this.buildList=function(context,req){
		req.method="GET";
		$http(req)
			.success(
				function(response) {
					// store active activity
					var actStore=[];
					for(iter in context.activities){
						if(context.activities[iter].loaded==true){
							actStore.push(context.activities[iter]);
						}
					}
					console.log("activity stored "+actStore.length);
					context.activities=[];
					for(iter in response){
						context.activities.push({data:response[iter],loaded:false});
					}
					service.pushNew(context.activities);
					// read ISO string
					for(iter in context.activities){
						dateService.stringToDate(context.activities[iter].data);
					}
					$http.get(
						"/api/activity/path/" + $stateParams.aid)
						.success(function(response) {
							context.actPath = response;
						});
					// recover active activity
					//loaded item exist
					var exist = false;
					for (iter in actStore) {
						console.log("recover:" + actStore[iter].data.aid);
						for (i = 0; i < context.activities.length; i++) {
							if (context.activities[i].data.aid == actStore[iter].data.aid) {
								context.activeActNum = i;
								context.activities[i] = actStore[iter];
								// recover new
								if (i == context.activities.length - 1) {
									service.pushNew(context.activities);
								}
								exist = true;
								break;
							}
						}
					}
					if (!exist) {
						context.activeActNum = -1;
					}
					console.log("build success");
				});
	}
}])


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
					if(nv[1]==ov[1]){
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
					if(nv[1]==ov[1]){
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
					if(nv[1]==ov[1]){
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
		controller : 'actListCtrl as actList'
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
						actListCtrl.buildList();
					}
				});
				$scope.ashow=function(){
					console.log(actListCtrl.activities);
				}
				this.isDragging = false;
				this.setIsDragging = function(b) {
					this.isDragging = b;
					$scope.$digest();// trigger data-binding
				}
				this.digestAll = function() {
					$scope.$digest();
				}

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
					activityService.buildList(actListCtrl, req);
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
							activityService.submit(actListCtrl.activities[this.activeActNum],actListCtrl.buildList);
						}
						activityService.loadActivity(actListCtrl.activities[n]);
						this.activeActNum = n;
					}
				}
				this.submit = function(n) {
					activityService.submit(actListCtrl.activities[n],null);
				};
				$scope.setBeingDragged = function(n) {
					$scope.beingDragged = n;
					$scope.$digest();
				}
				this.moveToTrash = function(aid,$event) {
					$event.preventDefault();
					$event.stopPropagation();
					activityService.moveToTrash(this,aid);
				}
			} ]);


/*app
.controller(
	"leavesCtrl",
	[
		'$scope',
		'$http',
		'$stateParams','dateService','activityService',
		function($scope, $http, $stateParams,dateService,activityService) {
			$scope.td={startTime:(new Date()),type:"RANGE"};
			console.log("initLeavesCtrl");
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
			this.pushNew = function() {
				actListCtrl.activities.push({
					name : "New"
				});
			}
			this.buildList = function() {
				activityService.buildList(this, "/api/activity/leaves/"+$stateParams.aid+"?early="+(new Date()).toISOString()+"&span=24")
			};
			this.buildList();
			this.loadActivity = function(n) {
				activityService.loadActivity(this, n);

			};
			this.setActivity = function(n) {
				if (n != this.activeActNum) {
					this.submit(this.activeActNum);
					this.activeActNum = n;
					this.loadActivity(n);
				}

			}
			this.submit = function(n) {
				activityService.submit(this,n);
			};
			$scope.setBeingDragged = function(n) {
				$scope.beingDragged = n;
				$scope.$digest();
			}
			this.moveToTrash = function(aid,$event) {
				$event.preventDefault();
				$event.stopPropagation();
				activityService.moveToTrash(this,aid);
			}
		} ]);*/


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
			model:"=model",
				open:"=isOpen"
		},
		link : function(scope, element, attr) {
		}
	}
});
app.controller('activityDetailCtrl', [ '$scope', '$http',
	function($scope, $http) {
		var ctrl = this;

	} ]);
app.directive('activityAccordion', function(activityService) {
	function link(scope, element, attr) {
		scope.$watch('item.open', function(nv, ov) {
			console.log("open nv:"+nv+" ov:"+ov);
			
			// close from hand or buildList
			if (nv == false) {
				activityService.submit(scope.item,scope.buildList);
				// open
			} else if(nv==true){
				console.log("load:" + scope.item.aid);
				if(scope.item.aid==null){
					console.log("pushNew");
					scope.pushNew();
				}
				activityService.loadActivity(scope.item);
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
		scope.$watch("filter.date",function(){
			scope.actList.buildList();
		})
	}
}])
