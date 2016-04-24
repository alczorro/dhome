<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<dhome:InitLanuage useBrowserLanguage="true"/>
<head>
<title><fmt:message key="customTheme.title"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav"
	data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container page-title">

		<div class="sub-title">${titleUser.zhName}<fmt:message key="common.dhome"/></div>

		<jsp:include page="./commonMenu.jsp">
			<jsp:param name="activeItem" value="theme" />
		</jsp:include>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout d-bottom">
			<div class="config-title" style="margin-top:0.5em;"><h3><fmt:message key="customTheme.subTitle"/></h3></div>
			<ul class="theme-list">
				<c:forEach var="theme" items="${themes}" varStatus="stat">
				<li them_id="${theme.id}" class="cover">
						<div class="info">
						    <div class="d-ibottom"><b>${theme.title}</b></div>
						</div>
						<div class="theme_image">				
							<c:if test="${theme.id==1}">
								 <img src="<dhome:url value='/resources/images/theme-s-simple-size330.png'/>"
									alt="${theme.title}" title="${theme.title}" >
									</c:if>
							<c:if test="${theme.id==2}">
								<img src="<dhome:url value='/resources/images/theme-m-tradition-size330.png'/>"
									alt="${theme.title}" title="${theme.title}" >
							</c:if>
							<c:if test="${theme.id==3}">
								<img src="<dhome:url value='/resources/images/theme-m-namecard_size330.png'/>"
									alt="${theme.title}" title="${theme.title}" >
							</c:if>
							<c:if test="${theme.id==4}">
								<img src="<dhome:url value='/resources/images/theme-m-trend_size330.png'/>"
									alt="${theme.title}" title="${theme.title}" >
							</c:if>
							<c:if test="${theme.id==5}">
								<img src="<dhome:url value='/resources/images/theme-m-steady_size330.png'/>"
									alt="${theme.title}" title="${theme.title}" >
							</c:if>
							<c:if test="${theme.id==6}">
								<img src="<dhome:url value='/resources/images/theme-s-fashion-size330.png'/>"
									alt="${theme.title}" title="${theme.title}" >
							</c:if>
							<c:if test="${theme.id==7}">
								<img src="<dhome:url value='/resources/images/theme-m-iap_size330.png'/>"
									alt="${theme.title}" title="${theme.title}" >
							</c:if>
							<c:if test="${theme.id==8}">
								<img src="<dhome:url value='/resources/images/theme-m-iap-science_size330.png'/>"
									alt="${theme.title}" title="${theme.title}" >
							</c:if>
						</div>
						<div class="clear"></div>
						<div class="item-fooltip">
						<div class="float-left">${theme.descript}</div>
						<div rid="${stat.index+1}" class="operate float-right">
						 <c:choose><c:when test="${myThemeId!=theme.id}">
						 <!-- 如果不是选中的主题 -->
					     <button id="${theme.id}" onclick="changeTheme('${theme.id}')"
									class="btn btn-primary"><fmt:message key="customTheme.changeTheme"/></button>
									</c:when>
							<c:otherwise>
						    <img src="<dhome:url value='/resources/images/ok_small.png'/>"/>
							</c:otherwise>
						</c:choose>
						</div>
						<div class="clear"></div>
				</div>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/base.js" />"></script>

<script type="text/javascript">
function changeTheme(themeId) {
	if('${flag}'=='noIAP'){
		if(themeId==7||themeId==8){
			alert("您不是IAP的用户，不能用这个主题。");
			return false;
		}
	}
	var requestURL = "<dhome:url value='/system/custom/theme/changeTheme'/>";
	var params = "themeId=" + themeId;
	$.ajax({
		url:requestURL,
		type: "POST",
		data: params,
		error: function (xhr, ajaxOptions, thrownError) {
	    },
	    success: function(data){
	    	if(data=="success"){
	    		window.location.href = "<dhome:url value='/people/${domain}/admin/custom/theme'/>";
	    		
			}
		}
    });
}

</script>
</html>