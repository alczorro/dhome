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
			<h4 class="detail" style="margin-top:20px;">
				
				<span style="float:right; margin-bottom:5px;">
					<input type="button" id="createBtn" value=" +新增岗位应聘申请表 " class="btn btn-mini btn-success"/>
				</span>
				高级专业技术岗位应聘申请表
				<span style="margin-left:200px; margin-bottom:5px;">
					<input type="button" id="templBtn" value=" +更换申请表模板 " class="btn btn-mini btn-success"/>
				</span>
			</h4>
				
				
				
				<ul class="applyList">
					
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="institutionZhName" title="中国科学院大气物理研究所">
		    				<a class="aliasLink" target='_blank' href="/institution/${domain}/backend/job/details/1/${data.id}">
				    				${data.title }
				    				</a>
				    		</span>
				    		<span class="time">
				    			${data.startTime }  至    ${data.endTime }
				    		</span>
				    		<span class="time">
				    			${data.deadline } 截止
				    		</span>
				    		<span class="position">
								<a class="deleteJob" data-toggle="modal" href="/institution/${domain}/backend/job/delete/${data.id }" class="float-right popup-edit btn btn-mini"><i class="icon-trash"></i>删除</a>
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
			$("#createBtn").click(function(){
				window.location="/institution/${domain}/backend/job/creat";
			});
			$("#templBtn").click(function(){
				window.location="/institution/${domain}/backend/job/addTempl";
			});
			
			$('.deleteJob').on('click',function(){
				return (confirm("删除以后不可恢复，确认删除吗？"));
			});
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				window.location.href=page;
			});

			
			$('#memberMenu').addClass('active');
			
			
		
			
		}); 
	</script>
	
</html>