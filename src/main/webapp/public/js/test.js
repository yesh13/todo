$("document").ready(function(){
	listRefresh();
})
function listRefresh(){
	$.getJSON("/api/todo",function(data){
		$("ul").text("");
		data.acts.forEach(function(data){
			$("ul").append("<li><a noref onClick='javascript:rmTodo(this)'>"+data+"</a></li>");
		})
	})
}
function addTodo(){
	$.post("api/todo","action=add&"+$("form").serialize(),function(){
		listRefresh();
	})
	$("form")[0].reset();
}
function rmTodo(ele){
	console.log("rm instruction")
	$.post("api/todo","action=rm&name="+$(ele).text(),function(){
		listRefresh();
	})
}