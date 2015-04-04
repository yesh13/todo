var app=angular.module("testApp",["ui.bootstrap","ui-lib","services"]);
console.log("testApp");
app.controller("testCtrl",['$scope','$http',function($scope,$http){
	$scope.title="Hello World";
	$scope.parent=0;
	$scope.setWidth=function(){
		$scope.width=0;
	}
	$scope.td=new Date();
	$scope.td.setDate(7);
	$scope.open=false;
	$scope.o=[{data:"aa"},{data:"bb"}]
	
}]);


app.directive("detectOpen", function() {
	return {
		scope:{
			model:"=?"
		},
	link:link,
	template:"{{model.data}}{{model.open}}"
	}
	function link(scope,elem,attr){
		scope.$watch("model.open",function(nv,ov){
			console.log(scope.model.open);
		})
	}
})
