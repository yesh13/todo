<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Todo</title>
</head>
<body>
<script type="text/javascript" src="/todo/resources/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="/todo/resources/js/test.js"></script>
	<h1>Todo List</h1>
	<ul>
	</ul>
	<form action="javascript:addTodo()">
	<input name="name"/>
	<input type="submit" value="Add" />
	</form>
</body>
</html>