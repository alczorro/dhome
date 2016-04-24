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
					<jsp:include page="../../commonTrainingBackend.jsp"><jsp:param name="activeItem" value="trainings" /> </jsp:include>
				    <li class="search">
				    	<form class="bs-docs-example form-search">
				            <input id='trainingKeyword' type="text" placeholder="请输入学生姓名" class="input-medium search-query" value="<c:out value="${condition.keyword }"/>"/>
				            <button id="searchTrainingBtn" type="button" class="btn">搜索</button>
				        </form>
				    </li>
				    </ul>
				    <div class="batch">
				    <ul class="degrees" id="degreesCond">
					<li data-degrees="0" <c:if test="${condition.degree==0 }">class="active"</c:if>>
						<a><strong>全部</strong></a>
					</li>
					<c:forEach items="${degreeMap.entrySet() }" var="entry">
						<li data-degrees="${entry.key }" <c:if test="${condition.degree == entry.key}">class="active"</c:if>>
							<a >  
								<span class="grade">${degrees[entry.key].val}</span> 
								<span class="count">(${entry.value })</span>
							</a>
						</li>
					</c:forEach>
					</ul>
					<select id="batchOperation">
						<option>--批量操作--</option>
						<option value="batchDelete">删除</option>
					</select>
					<input type="button" id="batchBtn" value="确认" class="btn btn-primary"/>
				</div>
				<ul class="listShow student">
					<li class="title">
						<span class="check"><input type="checkbox"  id="checkAll"/></span>
						<span class="name">姓名</span>
						<span class="inTime">入学时间</span>
						<span class="supervisor">导师</span> 
						<span class="degree">学位</span>
						<span class="status">在读情况</span> 
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="check"><input data-id="${data.id }" type="checkbox" class="checkUser"/></span>
									<span class="name">
										<img src="<dhome:img imgId="${userMap[data.cstnetId].image }"/>"/>  
										<span class="name"><c:out value="${data.studentName }"/><span class="country">(中国)</span></span>
										<span class="manage">
											<a class="label label-success" href="<dhome:url value="../detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a>
											<a class="label label-success" href="<dhome:url value="/institution/${domain}/backend/training/edit/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
											<a class="label label-danger deleteCopyright" href="<dhome:url value="../delete?id[]=${data.id }&page=${page.currentPage }"/>">删除</a>
										</span>
										</span>
									</span> 
									<span class="inTime">
										<c:choose>
											<c:when test="${empty data.enrollmentDate }">
													--   
											</c:when>
											<c:otherwise>
												<c:out value="${data.enrollmentDate }"/>
											</c:otherwise>
										</c:choose>
									</span>
									<span class="supervisor">
										<c:choose>
											<c:when test="${ empty memberMap[data.umtId] }">
													--   
											</c:when>
											<c:otherwise>
												<c:out value="${memberMap[data.umtId].trueName }"/>
											</c:otherwise>
										</c:choose>
									</span>
									<span class="degree">
										<c:choose>
											<c:when test="${data.degree==0 }">
													--   
											</c:when>
											<c:otherwise>
												${degrees[data.degree].val}
											</c:otherwise>
										</c:choose>
									</span>
									<span class="status">
										<c:choose>
											<c:when test="${ empty data.major }">
													--   
											</c:when>
											<c:otherwise>
												<c:out value="${data.major }"/>
											</c:otherwise>
										</c:choose>
									</span>
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
			$.fn.extend({ 
				enter: function (callBack) {
				    $(this).keydown(function(event){
				    	if(event.keyCode=='13'){
				    		callBack.apply(event.currentTarget,arguments);
				    		event.preventDefault();
							event.stopPropagation();
				    	}
				    });
				}
			});
			
			$('.deleteTraining').on('click',function(){
				return (confirm("学生删除以后不可恢复，确认删除吗？"));
			});
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='';
					$('.checkUser:checked').each(function(i,n){
						url+="&id[]="+$(n).data('id');
					});
					if(url==''){
						alert('请选择要删除的学生');
						return;
					}
					if(confirm("学生删除以后不可恢复，确认删除吗？")){
						window.location.href="../delete?page=${page.currentPage}"+url;
					}
					
				}else{
					alert('请选择批量操作'); 
				}
			});
			
			checkAllBox('checkAll','checkUser');
			
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				/* window.location.href=page; */
				search(page);
			});
			
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
				baseUrl+="&degree="+$('#degreesCond li.active').data('degrees');
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#trainingKeyword').val()));
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
			$('#trainingKeyword').enter(function(){
				search(1);
			});
			
			$('#trainingMenu').addClass('active');
		
		});
	</script>
</html>