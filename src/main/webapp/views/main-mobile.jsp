<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<title>Todo</title>
<link rel="stylesheet" type="text/css"
	href="/todo/resources/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="/todo/resources/css/datepicker.css">
<link rel="stylesheet" type="text/css" href="/todo/resources/css/main.css">
<link rel="stylesheet" type="text/css" href="/todo/resources/css/mobile.css">
</head>
<body ng-app="mainApp">
	<script type="text/javascript" src="/todo/resources/js/angular.min.js"></script>
	<script type="text/javascript"
		src="/todo/resources/js/angular-ui-router.min.js"></script>
	<script type="text/javascript"
		src="/todo/resources/js/datepicker.js"></script>
			<script type="text/javascript"
		src="/todo/resources/js/ui-bootstrap.min.js"></script>
	<script type="text/javascript" src="/todo/resources/js/services.js"></script>
	<script type="text/javascript" src="/todo/resources/js/ui-lib.js"></script>
	<script type="text/javascript" src="/todo/resources/js/main.js"></script>
		<script type="text/ng-template" id="activity.html">
<div ng-if="actList.activity&&actList.activity.data.aid!=0">
<list-header></list-header>
<activity-detail model="actList.activity.data" default-date="filter.date"></activity-detail>
</div>
<activity-list ng-if="actList.activity.data.subTask" model="actList.activity.data.subTask" title="Task" method="actList" new-method="actList.newTask(event)"></activity-list>
<activity-list ng-if="actList.activity.data.subAppt" model="actList.activity.data.subAppt" title="Appointment" method="actList" new-method="actList.newAppt(event)"></activity-list>
<activity-list ng-if="actList.activity.data.subNote" model="actList.activity.data.subNote" title="Finished" method="actList" new-method="actList.newNote(event)"></activity-list>
<activity-list ng-if="actList.activity.data.subPend" model="actList.activity.data.subPend" title="Pend" method="actList" new-method="actList.newPend(event)"></activity-list>

</script>
	<nav class="navbar navbar-inverse">
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
<div  ng-controller="sideTabCtrl">
	<div ui-view></div>
	<tabset class="bottom-tab" ng-repeat="group in routerState.states">
    <tab heading="{{item.name}}"  ng-repeat="item in group.data" ui-sref-active="active" ui-sref="{{item.ref}}"></tab>
  </tabset>
  </div>
<div style="height:50px"></div>
		</div>

</body>
</html>