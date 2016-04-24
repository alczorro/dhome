<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<html lang="en">
	<head>
		<title><fmt:message key="institute.common.scholarEvent"/>-${institution.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
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
					<jsp:include page="../../commonTrainingBackend.jsp"><jsp:param name="activeItem" value="grants" /> </jsp:include>
<!-- 					<li>&nbsp;&nbsp;&nbsp;</li> -->
<!-- 					<li> -->
<%-- 					    <a href="<dhome:url value="/institution/${domain }/backend/training/list/1"/>">学生</a> --%>
<!-- 					</li> -->
<!-- 					<li> -->
<%-- 					    <a href="<dhome:url value="/institution/${domain }/backend/training/create"/>">+ 添加学生</a> --%>
<!-- 				    </li> -->
<!-- 				   	<li> -->
<!-- 					    <a href="#">批量导入</a> -->
<!-- 				    </li> -->
<!-- 			    	<li class="active"> -->
<%-- 				    	<a href="#" >奖助学金发放 ${page.allSize }</a> --%>
<!-- 				    </li> -->
				    
				    </ul>
				    <div class="batch">
				    <ul class="degrees" id="degreesCond">
					<li data-degrees="0" <c:if test="${condition.degree==0 }">class="active"</c:if>>
						<a><strong>全部</strong></a>
					</li>
					
					<c:forEach items="${degreesCountMap.entrySet() }" var="entry">
						<li data-degrees="${entry.key }" <c:if test="${condition.degree ==entry.key}">class="active"</c:if>>
							<a >  
								<span class="grade">${degreeMap[entry.key].val }</span> 
								<span class="count">(${entry.value })</span>
							</a>
						</li>
					</c:forEach>
					</ul>
					
					<br/>
				</div>
				<ul class="listShow grant">
					<li class="title">
						<span class="name">姓名</span>
						<span class="num">课题号</span>
						<span class="class">班级</span> 
						<span class="degree">学位</span>
						<span class="bonus">研究所奖学金<br><span class="normal">（研究所支付）</span></span> 
						<span class="bonus">研究所奖学金<br><span class="normal">（课题支付）</span></span>
						<span class="bonus">助研学金<br><span class="normal">（课题支付）</span></span>  
						<span class="total">总金额</span> 
						<span class="time">发放时间</span>
						<span class="supervisor">导师</span>
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="name">
<%-- 								<a href="<dhome:url value="/institution/${domain }/backend/training/detail/${data.studentId }?returnPage=${page.currentPage }"/>">${data.studentName}</a> --%>
								${data.studentName}
							</span> 
							<span class="num"> ${data.topicNo } </span>
							<span class="class"> ${data.className } </span>
							<span class="degree"> ${degreeMap[data.degree].val } </span>
							<span class="bonus"><fmt:formatNumber value="${data.scholarship1 }" pattern="0.00"/></span>
							<span class="bonus"> <fmt:formatNumber value="${data.scholarship2 }" pattern="0.00"/>  </span>
							<span class="bonus"> <fmt:formatNumber value="${data.assistantFee }" pattern="0.00"/>  </span>
							<span class="total"> <fmt:formatNumber value="${data.sumFee }" pattern="0.00"/> </span>
							<span class="time">
							<fmt:formatDate value="${data.startTime}" pattern="yyyy.MM.dd" />
								-
							<fmt:formatDate value="${data.endTime}" pattern="yyyy.MM.dd" />
							</span>
							<span  class="supervisor">${data.tutor } </span>
						</li> 
					</c:forEach>
				</ul>
				<c:choose>
					<c:when test="${page.maxPage!=0 }">	
						<div class="pages">
							<c:choose>
							   <c:when test="${page.currentPage== 1 }">
							      <a class="disable" >首页</a>
							   </c:when>
							   <c:otherwise>
						          <a class="first">首页</a>
					           </c:otherwise>
							</c:choose>
							
							<c:if test="${page.currentPage!=1 }">
								<a class="prev"><i class="prev-triggle"></i>上一页</a>
							</c:if>
							<span id="pageNav"> </span>
							<c:if test="${page.currentPage!=page.maxPage }">
								<a class="next" >下一页<i class="next-triggle"></i></a>
							</c:if>
							
							<c:choose>
							   <c:when test="${page.currentPage==page.maxPage }">
							      <a class="disable" >尾页</a>
							   </c:when>
							   <c:otherwise>
						          <a class="last">尾页</a>
					           </c:otherwise>
							</c:choose>
						</div>
					</c:when>
					<c:otherwise>
						<p class="msg">没有匹配记录</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){

			$('.deleteTraining').on('click',function(){
				return (confirm("软件著作权删除以后不可恢复，确认删除吗？"));
			});
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='';
					$('.checkUser:checked').each(function(i,n){
						url+="&id[]="+$(n).data('id');
					});
					if(url==''){
						alert('请选择要删除的软件著作权');
						return;
					}
					window.location.href="../delete?page=${page.currentPage}"+url;
					
				}else{
					alert('请选择批量操作'); 
				}
			});
			
			checkAllBox('checkAll','checkUser');
			
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				search(page);
			});
			
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
				baseUrl+="&degree="+$('#degreesCond li.active').data('degrees');
				window.location.href=baseUrl;
			}
			
			$('#degreesCond li').on('click',function(){
				$('#degreesCond li').removeClass('active');
				$(this).addClass('active');
				search(1);
			});
			
			$('#searchBtn').on('click',function(){
				search(1);
			});
			
			$('#trainingMenu').addClass('active');
		
		});
	</script>
</html>