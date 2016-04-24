<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<html lang="en">
	<head>
		<title>机构成员</title>
		<meta name="description" content="dHome" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
	</head>

	<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
		<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
		
		<div class="container">
			<jsp:include page="../leftMenu.jsp"></jsp:include>
			<div class="ins_backend_rightContent">
				<ul class="nav nav-tabs">
					<jsp:include page="../../commonMemberBackend.jsp"><jsp:param name="activeItem" value="job" /> </jsp:include>
<!-- 				     <li class="search"> -->
<!-- 				    	<form class="bs-docs-example form-search"> -->
<%-- 				            <input id='memberKeyword' type="text" class="input-medium search-query" placeholder="请输入员工姓名" value="<c:out value="${condition.keyword }"/>"> --%>
<!-- 				            <button id="searchMemberBtn" type="button" class="btn">搜索</button> -->
<!-- 				        </form> -->
<!-- 				    </li> -->
 			 </ul> 
				<h4 class="detail" style="margin-top:20px;">高级专业技术岗位应聘申请表</h4>
				
				
				
				<ul class="listShow">
					<li class="title">
						<span class="article">申请表</span>
						<span class="inTime">申请职称</span>
						<span class="supervisor">提交时间</span> 
						<span class="degree">提交人</span>
						<span class="status">操作</span> 
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="article">
								${applicationMap[data.applicationId].title }  
								
							</span>
							<span class="inTime">
								<c:choose>
									<c:when test="${data.jobId==1 }">
										研究员  
									</c:when>
									<c:when test="${data.jobId==2 }">
										副研究员  
									</c:when>
									<c:when test="${data.jobId==3 }">
										正研级高工  
									</c:when>
									<c:when test="${data.jobId==4 }">
										高级工程师  
									</c:when>
									<c:when test="${data.jobId==5 }">
										编审  
									</c:when>
									<c:when test="${data.jobId==6 }">
										副编审  
									</c:when>
									<c:when test="${data.jobId==7 }">
										高级实验师  
									</c:when>
									<c:otherwise>
										未知
									</c:otherwise>
								</c:choose>
							</span>
							<span class="supervisor">
								<c:choose>
									<c:when test="${empty data.applyTime }">
										--
									</c:when>
									<c:otherwise>
										<c:out value="${data.applyTime }"/>
									</c:otherwise>
								</c:choose>
							</span>
							<span class="degree">
								<c:choose>
									<c:when test="${empty userMap[data.userId] }">
										--
									</c:when>
									<c:otherwise>
										<c:out value="${userMap[data.userId].zhName }"/>
									</c:otherwise>
								</c:choose>
							</span>
							<span class="status">
<%-- 								<a href="<dhome:url value='/people/${domain}/admin/grants/handed/${data}/1'/>" class="label label-success">查看</a> --%>
								<a href="<dhome:url value='/institution/${domain}/backend/job/report?applyId=${data.id}'/>" target="_blank" title="批次号：${data}" class="label label-success">申请表下载</a>
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
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				window.location.href=page;
			});
			
			$('#memberMenu').addClass('active');
			
			
		
			
		}); 
	</script>
	
</html>