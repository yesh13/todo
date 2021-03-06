<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="min-width:1200px">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Todo</title>
<link rel="stylesheet" type="text/css"
	href="/todo/resources/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="/todo/resources/css/datepicker.css">
<link rel="stylesheet" type="text/css" href="/todo/resources/css/main.css">
<link rel="stylesheet" type="text/css" href="/todo/resources/css/desk.css">
</head>
<body ng-app="mainApp"> 
	<form action="/j_spring_security_logout" method="post" id="logoutForm">
	</form>
	<script type="text/javascript" src="/todo/resources/js/angular.min.js"></script>
	<script type="text/javascript"
		src="/todo/resources/js/angular-ui-router.min.js"></script>
	<script type="text/javascript" src="/todo/resources/js/datepicker.js"></script>
	<script type="text/javascript" src="/todo/resources/js/ui-bootstrap.min.js"></script>
	<script type="text/javascript" src="/todo/resources/js/services.js"></script>
	<script type="text/javascript" src="/todo/resources/js/ui-lib.js"></script>
	<script type="text/javascript" src="/todo/resources/js/main.js"></script>
	<script type="text/ng-template" id="activity.html"
		ng-include="'/todo/resources/views/activity-desktop.html'">
<div class="act-list col-xs-4 full-height-flow" ng-include="'/todo/resources/views/activity.list.html'">
</div>
<div class="act-detail col-xs-5 full-height-flow">
<activity-detail ng-if="actList.activeActNum!=-1" model="actList.activities[actList.activeActNum]" is-open="true" default-date="filter.date"></activity-detail>
</div>
</script>


	<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">${msg}</a>
		</div>
		<div>
			<ul class="nav navbar-nav navbar-right">
				<c:choose>
					<c:when test="${account!=null}">
						<li><a href="#"><span
								class="glyphicon glyphicon-user"></span> ${account.getNickName()}</a></li>
						<li sign-out><a href="#"><span
								class="glyphicon glyphicon-log-out"></span> Sign out</a></li>
					</c:when>

					<c:otherwise>
						<li><a href="/todo/register"><span
								class="glyphicon glyphicon-user"></span> Join</a></li>
						<li><a href="/todo/private"><span
								class="glyphicon glyphicon-log-in"></span> Sign in</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
	</nav>
	<div class="container-fluid full-height"
		ng-controller="sideTabCtrl as sideTab">
		<div class="row full-height">
			<div class="col-xs-2 side-nav full-height-flow">
				<ul class="nav nav-stacked" ng-repeat="group in routerState.states">
				<li ui-sref-active="active" ng-repeat="item in group.data"><a ui-sref="{{item.ref}}">{{item.name}}</a></li>
				</ul>
			</div>
			<div class="full-height" ui-view></div>
		</div>
	</div>
</body>
</html>