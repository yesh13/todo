var app = angular.module("mainApp", [ "ui.router", "ui.bootstrap",
		"ngQuickDate" ]);
app.config(function($stateProvider) {
	$stateProvider.state('activity', {
		url : '/activity/:aid',
		templateUrl : '/resources/views/activity.html',
		controller : 'actListCtrl as actList'
	});
	$stateProvider.state('today', {
		url : '/today',
		templateUrl : '/resources/views/today.html'
	});
});
app.service("dragService", function() {
});
app.controller("sideTabCtrl", [ '$scope', function($scope) {
	this.setSideTab = function(item) {
		this.sideTabSelected = item;
	};
	this.sideTabUrl = function() {
		return "/resources/views/" + this.sideTabSelected + ".html";
	};
	this.setSideTab("activity");
} ]);

var actList = app
		.controller(
				"actListCtrl",
				[
						'dragService',
						'$scope',
						'$http',
						'$stateParams',
						function(dragService, $scope, $http, $stateParams) {
							var actListCtrl = this;
							$scope.activity = {};
							$scope.actNum = 0;
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
							}
						} ]);
app.directive('draggableActivity', function() {
	return function(scope, element, attr) {
		element.on('dragstart', function(event) {
			// Prevent default dragging of selected content
			scope.setBeingDragged(scope.$eval(attr.draggableActivity));
		});
	};
});
app.directive('droppableActivity', [
		'$http',
		function($http) {
			return function(scope, element, attr) {
				element.on('drop', function(event) {
					// Prevent default dragging of selected content
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