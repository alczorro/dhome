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
					<jsp:include page="../../commonPatentBackend.jsp"><jsp:param name="activeItem" value="patents" /> </jsp:include>
				    <li class="search">
				    	<form class="bs-docs-example form-search">
				            <input id='patentKeyword' type="text" placeholder="请输入成果名称" class="input-medium search-query" value="<c:out value="${condition.keyword }"/>"/>
				            <button id="searchPatentBtn" type="button" class="btn">搜索</button>
				        </form>
				    </li>
				    </ul>
				    <div class="batch">
				    <ul class="grades" id="gradesCond">
					<li data-grades="-1" <c:if test="${condition.grade==-1 }">class="active"</c:if>>
						<a><strong>全部</strong></a>
					</li>
					<c:forEach items="${gradeMap.entrySet() }" var="entry">
						<li data-grades="${entry.key }" <c:if test="${condition.grade == entry.key}">class="active"</c:if>>
							<a >  
								<span class="grade">${grades[entry.key].val}
								</span> 
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
				<ul class="listShow">
					<li class="title">
						<span class="check"><input type="checkbox"  id="checkAll"/></span>
						<span class="article">成果名称</span>
						<span class="author">类别</span>
						<span class="quot">等级</span> 
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="check"><input data-id="${data.id }" type="checkbox" class="checkUser"/></span>
									<span class="article">
										<c:choose>
											<c:when test="${empty data.name }">
													--   
											</c:when>
											<c:otherwise>
												<c:out value="${data.name }"/>
											</c:otherwise>
										</c:choose>
										<span class="manage">
											<a class="label label-success" href="<dhome:url value="../detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a>
											<a class="label label-success" href="<dhome:url value="/institution/${domain}/backend/patent/edit/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
											<a class="label label-danger deletePatent" href="<dhome:url value="../delete?id[]=${data.id }&page=${page.currentPage }"/>">删除</a>
										</span>
									</span> 
									<span class="author">
										<c:choose>
											<c:when test="${data.type==0 }">
													--   
											</c:when>
											<c:otherwise>
												${types[data.type].val}
											</c:otherwise>
											
										</c:choose>
									</span>
									<span class="quot">
										<c:choose>
											<c:when test="${ data.grade==0 }">
													--   
											</c:when>
											<c:otherwise>
												${grades[data.grade].val}
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
			
			$('.deletePatent').on('click',function(){
				return (confirm("专利删除以后不可恢复，确认删除吗？"));
			});
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='';
					$('.checkUser:checked').each(function(i,n){
						url+="&id[]="+$(n).data('id');
					});
					if(url==''){
						alert('请选择要删除的专利');
						return;
					}
					if(confirm("专利删除以后不可恢复，确认删除吗？")){
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
				baseUrl+="&grade="+$('#gradesCond li.active').data('grades');
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#patentKeyword').val()));
				window.location.href=baseUrl;
			}
			
			$('#gradesCond li').on('click',function(){
				$('#gradesCond li').removeClass('active');
				$(this).addClass('active');
				search(1);
			});
			
			$('#searchPatentBtn').on('click',function(){
				search(1);
			});
			$('#patentKeyword').enter(function(){
				search(1);
			});
			
			$('#patentMenu').addClass('active');
		
		});
	</script>
</html>