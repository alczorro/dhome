<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><fmt:message key='pageNotFound.title'/></title>
	<style>
		.container{width:50%; margin:5% auto; background:#f5f5f5; padding:2em; border-radius:4px; border:1px solid #ccc; }
		ol li {margin:1em 0;}
	</style>
</head>
<body class="dHome-body gray">
	<div class="container">
		<h1><fmt:message key="validate.error.msg"/></h1>
		<ol>
		
		<c:forEach items="${result.keys}" var="key">
			<li><fmt:message key="${key}"/></li>
		</c:forEach>
		</ol>
	
	</div>
</body>
<script>
	setTimeout(function(){
		window.history.back();			
	},7000);

</script>
</html>