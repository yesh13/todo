var app=angular.module("testApp",[]);
console.log("testApp");
app.controller("testCtrl",['$scope','$http',function($scope,$http){
	$scope.title="Hello World";
	$scope.setWidth=function(){
		$scope.width=0;
	}
}]);
app.directive('showSize',function() {
	return function(scope, element, attr) {
		element.on("click",function(){
			element[0].innerHTML="height:"+element[0].offsetHeight+", width: "+element[0].offsetWidth;
		})
	};
});
