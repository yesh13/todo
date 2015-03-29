var module=angular.module("ui-lib", ["services"]);

module.controller("parentSelectCtrl", function(activityService,$scope,$modal) {
	$scope.$watch("model.loaded",function(nv){
		console.log("modelloaded"+nv);
		if(nv==true){
			if($scope.model.data.parent==0){
				$scope.title="root";
			}else{
				var p={data:{aid:$scope.model.data.parent}}
				activityService.loadActivity(p,function(context){
					$scope.title=context.data.name!=null?context.data.name:"untitled";
				});
			}
		}
	});

	  $scope.open = function (size) {

	    var modalInstance = $modal.open({
	      templateUrl: '/resources/views/parent-select-modal.html',
	      controller: 'parentSelectModalCtrl',
	      size: size,
	      resolve: {
	        parent: function () {
	          return $scope.model;
	        }
	      }
	    });

	    modalInstance.result.then(function (selectedItem) {
	      $scope.model.data.parent = selectedItem.aid;
	      $scope.title=selectedItem.name;
	    }, function () {
	    });
	  };
})

module.directive("parentSelect", function() {
	return {
		templateUrl:"/resources/views/parent-select.html",
		scope:{
			model:"=?"
		},
		controller:"parentSelectCtrl"
		
	}
})



module.controller('parentSelectModalCtrl', function ($scope, $modalInstance,activityService, parent) {

	$scope.rootParent={aid:0,name:"root"};
	$scope.currentParent=$scope.rootParent;
	$scope.lastParent=null;
	  $scope.buildList=function(){
		  var req={
			  url:"/api/activity/list/"+$scope.currentParent.aid
		  }
		  console.log(req.url);
		  activityService.buildList($scope,req,$scope.currentParent.aid,true);
	  }
	  $scope.buildList();
	  $scope.choose=function(item){
		  $scope.lastParent=$scope.currentParent;
		  $scope.currentParent=item;
		  $scope.buildList();
		
	  }

  $scope.ok = function () {
    $modalInstance.close($scope.currentParent);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});