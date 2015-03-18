var app = angular.module("mainApp", [ "ui.router", "ui.bootstrap",
		"ngQuickDate" ]);
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
app.service("dragService", function() {
});
app.controller("sideTabCtrl", ['$scope','$stateParams', function($scope,$stateParams) {
	$scope.$stateParams=$stateParams;
	this.setSideTab = function(item) {
		this.sideTabSelected = item;
	};
	this.sideTabUrl = function() {
		return "/resources/views/" + this.sideTabSelected + ".html";
	};
	this.setSideTab("activity");
} ]);

app.controller("actListCtrl",
				['dragService',
						'$scope',
						'$http',
						'$stateParams',
						function(dragService, $scope, $http, $stateParams) {
							var actListCtrl = this;
							$scope.activity = {};
							$scope.actNum = 0;
							$scope.isDragging = false;
							$scope.setIsDragging = function(b) {
								$scope.isDragging = b;
								$scope.$digest();// trigger data-binding
							}
							$scope.digestAll = function() {
								$scope.$digest();
							}
							$scope.initList = function() {
								console.log("build list");
								$http
										.get(
												"/api/activity/list/"
														+ $stateParams.aid)
										.success(
												function(response) {
													$scope.actList = response;
													dragService.list = $scope.actList;
													if ($scope.actList.length > 0) {
														$scope.setActivity(-1);
													}
													$http
															.get(
																	"/api/activity/path/"
																			+ $stateParams.aid)
															.success(
																	function(
																			response) {
																		$scope.actPath = response;
																	});
												});
							};
							$scope.initList();
							$scope.setActivity = function(n) {
								if (n < 0) {
									$scope.activity = {};
								} else {
									$scope.actNum = n;
									var aid = $scope.actList[n].aid;
									$http
											.get("/api/activity/detail/" + aid)
											.success(
													function(response) {
														$scope.activity = response;
														$scope.activity.endTime = $scope.activity.endTime ? new Date(
																$scope.activity.endTime)
																: null;
														$scope.activity.startTime = $scope.activity.startTime ? new Date(
																$scope.activity.startTime)
																: null;
														// read ISO string
													});
								}
							};
							$scope.sumbit = function() {
								if ($scope.activity.aid == null) {
									$scope.activity.parent = $stateParams.aid;
									console.log("new: "
											+ JSON.stringify($scope.activity));
									$http
											.post("/api/activity",
													$scope.activity).success(
													function() {
														$scope.initList();
													});
								} else {
									console.log("edit: "
											+ JSON.stringify($scope.activity));
									$http.post(
											"/api/activity/"
													+ $scope.activity.aid,
											$scope.activity).success(
											function() {
												$scope.initList();
											});
								}
							};
							$scope.setBeingDragged = function(n) {
								$scope.beingDragged = n;
								$scope.$digest();
							}
						} ]);


app.controller("leavesCtrl",
		['dragService',
				'$scope',
				'$http',
				'$stateParams',
				function(dragService, $scope, $http, $stateParams) {
					var actListCtrl = this;
					$scope.activity = {};
					$scope.actNum = 0;
					$scope.isDragging = false;
					$scope.setIsDragging = function(b) {
						$scope.isDragging = b;
						$scope.$digest();// trigger data-binding
					}
					$scope.digestAll = function() {
						$scope.$digest();
					}
					$scope.initList = function() {
						console.log("build list");
						var year=(new Date()).getFullYear();
						var month=(new Date()).getMonth();
						var date=(new Date()).getDate();
						var early=new Date(year,month,date);
						$http
								.get(
										"/api/activity/leaves/"
												+ $stateParams.aid,{params:{
													early:early.toISOString(),
													span:24
												}})
								.success(
										function(response) {
											$scope.actList = response;
											dragService.list = $scope.actList;
											if ($scope.actList.length > 0) {
												$scope.setActivity(-1);
											}
											$http
													.get(
															"/api/activity/path/"
																	+ $stateParams.aid)
													.success(
															function(
																	response) {
																$scope.actPath = response;
															});
										});
					};
					$scope.initList();
					$scope.setActivity = function(n) {
						if (n < 0) {
							$scope.activity = {};
						} else {
							$scope.actNum = n;
							var aid = $scope.actList[n].aid;
							$http
									.get("/api/activity/detail/" + aid)
									.success(
											function(response) {
												$scope.activity = response;
												$scope.activity.endTime = $scope.activity.endTime ? new Date(
														$scope.activity.endTime)
														: null;
												$scope.activity.startTime = $scope.activity.startTime ? new Date(
														$scope.activity.startTime)
														: null;
												// read ISO string
											});
						}
					};
					$scope.sumbit = function() {
						if ($scope.activity.aid == null) {
							$scope.activity.parent = $stateParams.aid;
							console.log("new: "
									+ JSON.stringify($scope.activity));
							$http
									.post("/api/activity",
											$scope.activity).success(
											function() {
												$scope.initList();
											});
						} else {
							console.log("edit: "
									+ JSON.stringify($scope.activity));
							$http.post(
									"/api/activity/"
											+ $scope.activity.aid,
									$scope.activity).success(
									function() {
										$scope.initList();
									});
						}
					};
					$scope.setBeingDragged = function(n) {
						$scope.beingDragged = n;
						$scope.$digest();
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

// for test
app.directive('showSize', function() {
	return function(scope, element, attr) {
		window.alert("height:" + element[0].offsetHeight + ", width: "
				+ element[0].offsetWidth);
	};
});