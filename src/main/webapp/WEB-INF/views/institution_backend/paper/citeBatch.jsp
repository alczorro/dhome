<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>抓取论文引用频次</title>
	<style>
		.container{width:50%; margin:5% auto; background:#eef; padding:2em; border-radius:4px; border:1px solid #ccc; text-align:center;}
		p.sorry {color:#333}
	</style>
</head>
<body class="dHome-body gray">
	<div class="container">
		<p class="sorry" id="content">正在抓取论文引用频次，剩余抓取数量:${remainingCount}</p>
	</div>
</body>

</html>
