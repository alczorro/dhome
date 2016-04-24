<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
<dhome:InitLanuage useBrowserLanguage="true"/>
<title>${user.zhName }<fmt:message key="common.dhome"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${user.zhName }<fmt:message key="common.dhome"/>
			<jsp:include page="../../showAudit.jsp"/>
		</div>
		<jsp:include page="../../commonMenu.jsp">
			<jsp:param name="activeItem" value="achievement" />
		</jsp:include>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title">
			<h3>人才培养管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="jobapply" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
					<ul class="nav nav-tabs">
						<li>&nbsp;&nbsp;&nbsp;</li>
						<li class="active">
						    <a href="<dhome:url value="/people/${domain }/admin/job/list/1"/>">大气所岗位应聘申请表 </a>
						</li>
						
					   	<li>
						    <a href="<dhome:url value="/people/${domain}/admin/job/myList/1"/>">我的岗位应聘申请表</a>
					    </li>
					   
					</ul>
					<ul class="applyList">
					
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="institutionZhName" title="中国科学院大气物理研究所">
<%-- 		    				<a class="aliasLink" target='_blank' href="/people/${domain}/admin/job/details"> --%>
				    				${data.title }
<!-- 				    				</a> -->
				    		</span>
				    		<span class="position">
							<a id="popup-add" class="btn btn-mini btn-primary" data-toggle="modal" href="/people/${domain}/admin/job/details/${data.id }">申请</a>
							</span>
						</li> 
					</c:forEach>
				</ul>
					<c:choose>
						<c:when test="${page.maxPage!=0 }">	
							<div class="pages">
								<a class="first">首页</a>
								<c:if test="${page.currentPage!=1 }">
									<a class="prev"><i class="prev-triggle"></i>上一页</a>
								</c:if>
								<span id="pageNav"> </span>
								<c:if test="${page.currentPage!=page.maxPage }">
									<a class="next" >下一页<i class="next-triggle"></i></a>
								</c:if>
								<a class="last">尾页</a>
							</div>
						</c:when>
						<c:otherwise>
							<p class="msg">没有匹配记录</p>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				window.location.href=page;
			});

			
		
		});
	</script>
</html>