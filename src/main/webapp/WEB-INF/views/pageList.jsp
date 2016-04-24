
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>HOME</title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<ul>
	<c:forEach var="item" items="${pages}">
		<li><a href="<dhome:url value='/home/showPage?pid=${item.pid}'/>">${item.title}</a></li>
	</c:forEach>
</ul>
<form action="<dhome:url value='/system/auth/logout'/>" method="POST">
	<input type="submit" value="<fmt:message key='common.logout.anotherName'/>"/>
</form>
<form action="<dhome:url value='/system/auth/commit'/>" method="POST">
	<input type="submit" value="<fmt:message key='common.submit'/>"/>
</form>

<div id="pageapp">
	<div class="title">
		<h1>Pages</h1>
	</div>
	<div class="content">
		<div id="create-page">
			<input id="new-page" placeholder="What needs to be done?" type="text" />
			<span class="ui-tooltip-top" style="display: none;">Press Enter to save this task</span>
		</div>
		<div id="pages">
			<ul id="page-list"></ul>
		</div>
		<div id="page-stats"></div>
	</div>
</div>
</body>

<jsp:include page="commonheader.jsp"></jsp:include>


</html>
