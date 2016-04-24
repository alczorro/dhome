<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>权限受限</title>
	<style>
		.container{width:50%; margin:5% auto; background:#eef; padding:2em; border-radius:4px; border:1px solid #ccc; text-align:center;}
		p.sorry {color:#333}
	</style>
</head>
<body class="dHome-body gray">
	<div class="container">
		<p class="sorry">对不起，您不是中国科学院大气物理研究所·人力资源管理系统的管理员，没有权限访问该页面。</p>
	</div>
</body>
</html>