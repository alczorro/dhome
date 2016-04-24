
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
	<title>Home</title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

<h2>${page.title}</h2>
<a href="<dhome:url value='/${teamCode}/editPage'/>"><fmt:message key='showPage.edit'/></a>
<div>
	${page.content}${teamCode}
</div>
<a href="<dhome:url value='/${teamCode}'/>"><fmt:message key='showPage.list'/></a>
</body>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/1.7.2/jquery-1.7.2.min.js" />"></script>
</html>
