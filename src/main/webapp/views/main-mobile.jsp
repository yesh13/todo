<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1.0,user-scalable=no">
<title>Todo</title>
<link rel="stylesheet" type="text/css"
	href="/resources/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="/resources/css/datepicker.css">
<link rel="stylesheet" type="text/css" href="/resources/css/main.css">
<link rel="stylesheet" type="text/css" href="/resources/css/mobile.css">
</head>
<body ng-app="mainApp">
	<script type="text/javascript" src="/resources/js/angular.min.js"></script>
	<script type="text/javascript"
		src="/resources/js/angular-ui-router.min.js"></script>
	<script type="text/javascript"
		src="/resources/js/datepicker.js"></script>
			<script type="text/javascript"
		src="/resources/js/ui-bootstrap.min.js"></script>
	<script type="text/javascript" src="/resources/js/main.js"></script>
		<script type="text/ng-template" id="activity.html">
  <tabset class="bottom-tab">
    <tab heading="Activity" ui-sref-active="active" ui-sref="activity({aid:$stateParams.aid})"><div ng-include="'/resources/views/activity.mobile.html'"></div></tab>
<tab heading="Date" ui-sref-active="active" ui-sref="leaves({aid:$stateParams.aid})"><div ng-include="'/resources/views/activity.mobile.html'"></div></tab>
  </tabset>




</script>
	<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
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
						<li><a href="/register"><span
								class="glyphicon glyphicon-user"></span> Join</a></li>
						<li><a href="/private"><span
								class="glyphicon glyphicon-log-in"></span> Sign in</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
	</nav>
	<div ng-controller="sideTabCtrl as sideTab">
<!-- 			<div class="col-xs-2 side-nav full-height-flow"> -->
<!-- 				<ul class="nav nav-stacked"> -->
<!-- 					<li><a ui-sref="activity({aid:0})">Activity</a></li> -->
<!-- 					<li><a ui-sref="today">Today</a></li> -->
<!-- 					<li><a ui-sref="today">Calendar</a></li> -->
<!-- 				</ul> -->
<!-- 			</div> -->
<div style="height:50px"></div>
	<div ui-view></div>
<div style="height:50px"></div>
		</div>
</body>
</html>