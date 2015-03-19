<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="min-width:1200px">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Todo</title>
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="/resources/css/datepicker.css">
<link rel="stylesheet" type="text/css" href="/resources/css/main.css">
</head>
<body ng-app="mainApp">
	<script type="text/javascript" src="/resources/js/angular.min.js"></script>
	<script type="text/javascript"
		src="/resources/js/angular-ui-router.min.js"></script>
	<script type="text/javascript" src="/resources/js/datepicker.js"></script>
	<script type="text/javascript" src="/resources/js/ui-bootstrap.min.js"></script>
	<script type="text/javascript" src="/resources/js/main.js"></script>
	<script type="text/ng-template" id="activity.html" ng-include="'/resources/views/activity-desktop.html'">
<div class="col-xs-4 full-height-flow" ng-include="'/resources/views/activity.list.html'">
</div>
<div class="col-xs-5 full-height-flow" ng-include="'/resources/views/activity.detail.html'">
</div>
</script>
	<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">${msg}</a>
		</div>
		<div>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#"><span class="glyphicon glyphicon-user"></span>
						Sign Up</a></li>
				<li><a href="#"><span class="glyphicon glyphicon-log-in"></span>
						Login</a></li>
			</ul>
		</div>
	</div>
	</nav>
	<div class="container-fluid full-height"
		ng-controller="sideTabCtrl as sideTab">
		<div class="row full-height">
			<div class="col-xs-2 side-nav full-height-flow">
				<ul class="nav nav-stacked">
					<li><a ui-sref="activity({aid:0})">Activity</a></li>
					<li><a ui-sref="today">Today</a></li>
					<li><a ui-sref="today">Calendar</a></li>
				</ul>
			</div>
			<div class="full-height" ui-view></div>
		</div>
	</div>
</body>
</html>