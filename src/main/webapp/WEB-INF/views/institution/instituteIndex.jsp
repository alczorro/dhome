<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
<title><fmt:message key="institute.common.home"/>-${home.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<h1 class="dark-green"><a href='<dhome:url value="/institution/${home.domain }/index.html"/>'>${home.name }</a></h1>
	</div>
	<div class="container">
		<div class="row-fluid">
			<div class="span9">
				<div class="panel abs-top">
					<ul class="top-menu">
						<li><a href='<dhome:url value="/institution/${home.domain }/members.html"/>'><strong>${memberSize}</strong><br><fmt:message key="institueIndex.statistic.scholars"/></a></li>
						<li><a href='<dhome:url value="/institution/${home.domain }/publications.html"/>'><strong>${paperSize}</strong><br><fmt:message key="institueIndex.statistic.papers"/></a></li>
						<li><a href='<dhome:url value="/institution/${home.domain }/publications.html"/>'><strong>${hIndex }</strong><br>H-Index</a></li>
						<li><a href='<dhome:url value="/institution/${home.domain }/publications.html"/>'><strong>${gIndex }</strong><br>G-Index</a></li>
						<li><a href='<dhome:url value="/institution/${home.domain }/scholarevent.html"/>'><strong>${activityCount }</strong><br><fmt:message key="institute.common.scholarEvent"/></a></li>
						<div class="clear"></div>
					</ul>
				</div>
				<div class="panel abs-top some-ipad min600">
				    <h3 class="dark-green abs-top"><fmt:message key="common.paper.title"/> <fmt:message key="institueIndex.paper.topFive"/></h3>
				    <ol class="paper-list paper-list-show">
				    	<c:forEach items="${papers }" var="paper">
				        <li paper_id="${paper.id }">
				        	<span class="count icount"></span>
				        	<span>
				        		<span class="paper-title">
							        <c:choose>
							        	<c:when test="${paper.paperURL != null && paper.paperURL !='' }">
							        		<a target="_blank" href="${paper.paperURL }">${paper.title }</a>
							        	</c:when>
							        	<c:otherwise>
							        		${paper.title }
							        	</c:otherwise>
							        </c:choose>
							    </span>
			        			<span class="paper-author">${paper.authors }</span>
			        			<c:if test="${paper.source!=null&&paper.source!=''}">
					        		<span class="paper-source">${paper.source }</span>
					        	</c:if>
			        			<span class="paper-time">${paper.publishedTime }</span>
			        			<c:if test="${paper.volumeIssue !=null && paper.volumeIssue.trim() != ''}">
							        <span class="paper-volume">,${paper.volumeIssue }</span>
							    </c:if>
							    <c:if test="${paper.pages !=null && paper.pages.trim() != ''}">
							        <span class="paper-pages">,${paper.pages }</span>
							    </c:if>
			        			<div class="clear"></div>
			        			<c:if test="${paper.summary!=null && paper.summary!='' }">
							        <a class="summary link"><fmt:message key="common.summary"/>&nbsp;</a>
						        </c:if>
						        <c:if test="${ paper.clbId>0 }">
						        	<span class="bdownfull download"><a href="<dhome:url value='/system/download/${paper.clbId  }'/>"><fmt:message key='addNewPaper.downloadFulltext'/></a></span>
					        	</c:if>
					        	<c:if test="${paper.summary!=null && paper.summary!='' }">
							        <span class="summary-content" style="display:none">${paper.summary}</span>
						        </c:if>
					        </span>
				        </li>
				        </c:forEach>
				    </ol>
				    <a class="show-all" href='<dhome:url value="/institution/${home.domain }/publications.html"/>'><fmt:message key="institueIndex.viewAll"/></a>
				    <h3 class="dark-green abs-top"><fmt:message key="institute.common.members"/></h3>
				    <ul class="person-list">
				    <c:forEach items="${users }" var="user"> 
				    	<li>
				    		<div class="person-img"><a target="_blank" href="<dhome:url value='/people/${user.domain }'/>"><img src='<dhome:img imgId="${user.image}" />'/></a></div>
				    		<div class="person-info">
				    			<div><a target="_blank" href="<dhome:url value='/people/${user.domain }'/>"><strong>${user.zhName }</strong></a></div>
				    			<div>${user.salutation }</div>
				    			<div><fmt:message key="instituteMember.researchInterests"/>
				    			<c:if test="${empty user.interest}"><fmt:message key="common.none"/></c:if>
				    			<c:forEach items="${user.interest }" varStatus="index" var="interest">
				    				<c:if test="${index.index !=0}">
				    					&nbsp;,&nbsp;
				    				</c:if>
				    				<a target="_blank" href="<dhome:url value="/system/discover?type=interest&keyword=${interest }"/>"><dhome:decode value="${interest }"/></a>
				    			</c:forEach>
				    			</div>
				    		</div>
				    		<div class="float-right">
				    			<a target="_blank" href="<dhome:url value='/people/${user.domain }'/>" class="btn btn-small"><fmt:message key="instituteMember.homepage"/></a>
				    		</div>
				    		<div class="clear"></div>
				    	</li>
				    </c:forEach>
				    </ul>
				     <a class="show-all" href='<dhome:url value="/institution/${home.domain }/members.html"/>'><fmt:message key="institueIndex.viewAll"/></a>
				</div>
				<div class="clear"></div>
			</div>
			
			<div class="span3">
		    	<div class="insti abs-top">
		    		<div class="insti-img">
		    			<a href='<dhome:url value="/institution/${home.domain }/index.html"/>'><img src="<dhome:img imgId="${home.logoId }" imgName="dhome-institute.png"/>" /></a>	
		    		</div>	
		   			<p class="insti-info">${home.introduction }</p>
		   			<c:if test="${isMember || currentUser.isAdmin}">
		   				<p><a class="show-all"  href='<dhome:url value="/institution/${home.domain }/edit/index.html"/>'><fmt:message key="institute.common.edit"/></a></p>
		   			</c:if>
		   		</div>
		   		
		   		<ul class="insti-nav">
		   			<li class="active"><a><fmt:message key="institute.common.home"/></a></li>
		   			<li><a href='<dhome:url value="/institution/${home.domain }/publications.html"/>'><fmt:message key="institute.common.paper"/></a></li>
		   			<li><a href='<dhome:url value="/institution/${home.domain }/members.html"/>'><fmt:message key="institute.common.members"/></a></li>
		   			<li><a href='<dhome:url value="/institution/${home.domain }/scholarevent.html"/>'><fmt:message key="institute.common.scholarEvent"/></a></li>
		   		</ul>
			</div>
		</div>
	</div>
	<jsp:include page="../commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="../commonheader.jsp"></jsp:include>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/base.js" />"></script>
<script type="text/javascript">
	$(document).ready(
			function() {
				var pageHeight = $("html").height();
				var documentHeight = window.screen.height;
				var maxHeight = pageHeight > documentHeight ? pageHeight
						: documentHeight;
				$(".cover-wholepage").css({
					"height" : maxHeight
				});
				$(".cover-wholepage").click(function() {
					$(this).hide();
				});
				
				//auditStatus
				$('#auditStatus').hover(function(){
					$(this).tooltip('toggle');
				});
				
				$("a.summary").toggle(function(event){
					$(this).parent().children(".summary-content").slideDown();
					event.preventDefault();
				}, function(event){
					$(this).parent().children(".summary-content").slideUp();
					event.preventDefault();
				});
			});
</script>
</html>