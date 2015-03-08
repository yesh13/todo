<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Todo</title>
<link rel="stylesheet" type="text/css" href="/resources/css/main.css">
</head>
<body>
	<script type="text/javascript" src="/resources/js/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="/resources/js/test.js"></script>
	<div id="page">
		<div id="edit">
			<form id="add_form">
				name:<input name="name" /><br> location:<input name="location" /><br>
				note:<input name="note" /><br> <input type="submit" value="Add" />
			</form>
			<form id="edit_form">
				name:<input name="name" /><br> location:<input name="location" /><br>
				note:<input name="note" /><br> <input type="submit"
					value="Edit" />
			</form>
		</div>
		<div class="actList" id="list_div">
			<ul>
			</ul>
		</div>
		<div id="content_div"></div>
	</div>
</body>
</html>