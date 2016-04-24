<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html>
<html lang="en">
<head>
	<title><fmt:message key="common.dhome.anotherName"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	
	<div class="container canedit">
		<div class="row-fluid mini-layout center">
			<span><fmt:message key="${key }"/></span>
			<a href="${redirectURL }"><fmt:message key="common.redirect"/>&nbsp;<fmt:message key="${urlDescription }"/></a>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(function(){
		//leftMenu
		$(".icon-file").addClass("icon-white");
		$(".icon-file").parent().parent("li").addClass("active");
	})
</script>
</html>