<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1.0">
<link rel="stylesheet" type="text/css"
	href="/todo/resources/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="/todo/resources/css/datepicker.css">
<link rel="stylesheet" type="text/css" href="/todo/resources/css/main.css">
<link rel="stylesheet" type="text/css" href="/todo/resources/css/desk.css">
<title>About Secretary</title>
	<script type="text/javascript" src="/todo/resources/js/angular.min.js"></script>
	<script type="text/javascript" src="/todo/resources/js/ui-bootstrap.min.js"></script>
	<script type="text/javascript" src="/todo/resources/js/services.js"></script>
	<script type="text/javascript" src="/todo/resources/js/ui-lib.js"></script>
	<script type="text/javascript" src="/todo/resources/js/test.js"></script>
	
</head>
<body ng-app="testApp" ng-controller="testCtrl as test">
<button>{{td}}</button>
<modal-datepicker model="td" set-null="true" invalid-text="Schedule"></modal-datepicker>
</body>
</html>