$("document").ready(function(){
	$("#add_form").submit(function(event){
		addActivity();
		event.preventDefault();
	})
	$("#edit_form").submit(function(event){
		editActivity();
		event.preventDefault();
	})
	listRefresh();
})
function listRefresh(){
	$.getJSON("/api/list/0",function(data){
		$("ul").text("");
		data.forEach(function(data){
			$("ul").append("<li><a noref onClick='javascript:rmTodo(this)'>"+"name:"+data.name+
					"<br>location:"+data.location+
					"<br>note:"+data.note+"</a></li>");
		})
	})
}
function rmTodo(ele){
	console.log("rm instruction")
	$.post("api/todo","action=rm&name="+$(ele).text(),function(){
		listRefresh();
	})
}
function addActivity(){
	console.log("addAct");
	$.post("api/activity",$("#add_form").serialize(),null)
}
function editActivity(){
	$.post("api/activity/2",$("#edit_form").serialize(),null)
}