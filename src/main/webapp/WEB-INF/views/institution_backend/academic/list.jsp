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
				<jsp:include page="../../commonAcademicBackend.jsp"><jsp:param name="activeItem" value="academics" /> </jsp:include>
				 <li class="search">
			    	<form class="bs-docs-example form-search">
			            <input id='academicKeyword' type="text" placeholder="请输入成果名称" class="input-medium search-query" value="<c:out value="${condition.keyword }"/>"/>
			            <button id="searchAcademicBtn" type="button" class="btn">搜索</button>
			        </form>
			    </li>
			</ul>
			
		
		  <div class="batch">
		  	<ul class="years" id="positionsCond">
					<li style="text-align: center;" <c:if test="${condition.publisher== -1}">class="active"</c:if> data-positions="-1">
						<a><strong>全部</strong></a>
					</li>
					<c:forEach items="${positionsMap.entrySet() }" var="entry">
						<li data-positions="${entry.key }" <c:if test="${condition.grade ==entry.key}">class="active"</c:if>>
							<a >  
								<span class="year">${positions[entry.key] }</span> 
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
						<span class="orgname">组织名称</span>
						<span class="position">职位</span> 
						<span class="author">任职人员</span> 
						<span class="time">任职时间</span>
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="check"><input data-umt-id="${data.id }" type="checkbox" class="checkUser"/></span>
							<span class="orgname">
						    	<c:choose>
									<c:when test="${ data.name==0 }">
											--   
									</c:when>
									<c:otherwise>
										${organizationNames[data.name].val}
									</c:otherwise>
								</c:choose>
							
							<span class="manage">
									<a class="label label-success" href="<dhome:url value="/institution/${domain }/backend/academic/detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a>
									<a class="label label-success" href="<dhome:url value="/institution/${domain }/backend/academic/update/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
									<a class="label label-danger deleteAcademic" data-url="<dhome:url value="/institution/${domain }/backend/academic/delete?acmId[]=${data.id }"/>">删除</a>
								</span>
							</span>
							<span class="position">
								<c:choose>
									<c:when test="${ data.position==0 }">
											--   
									</c:when>
									<c:otherwise>
										${positions[data.position].val}
									</c:otherwise>
								</c:choose>
							</span>
							
							<span class="author">
							    <c:choose>
									<c:when test="${empty authorMap[data.id]}">
										--
									</c:when>
									<c:otherwise>
										<ul>
										<c:forEach items="${ authorMap[data.id]}" var="author">
											<li >
											<a class="authorDetailPopover"  data-per-id="${data.id }" data-author-id="${author.umtId }" ><c:out value="${author.trueName }"/></a>
<%-- 												<sup>[${author.subscriptIndex }]</sup> --%>
											</li>
										</c:forEach>
										</ul>
									</c:otherwise>
								</c:choose>
							</span>
							
							<span class="time">
								<c:choose>
								 	<c:when test="${data.startYear== 0 && data.endYear==0}">
										--
									</c:when>
									<c:otherwise>
										<c:if test="${data.endYear==3000 }">
											${data.startYear }<c:if test="${data.startMonth!=0 }">.${data.startMonth }</c:if>-至今
										</c:if>
										<c:if test="${data.endYear!=3000 }">
											${data.startYear }<c:if test="${data.startMonth!=0 }">.${data.startMonth }</c:if>
											 - ${data.endYear }<c:if test="${data.endMonth!=0 }">.${data.endMonth }</c:if>
										</c:if>
									</c:otherwise>
								</c:choose>
							</span>
<!-- 							<span class="status">在职</span>
 -->						</li> 
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
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
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
			
			var delMsg = "学术任职删除以后不可恢复，确认删除吗？";
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='';
					$('.checkUser:checked').each(function(i,n){
						url+="&acmId[]="+$(n).data('umtId');
					});
					if(url==''){
						alert('请选择要删除的学术任职');
						return;
					}
					if(confirm(delMsg)){
						$.get("../delete?page=${page.currentPage}"+url).done(function(data){
							if(data.success){
								window.location.reload();
							}else{
								alert(data.desc);
							}
						});
					}
					//window.location.href="../delete?page=${page.currentPage}"+url;
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
			
			$('.deleteAcademic').on('click',function(){
				if(confirm(delMsg)){
					$.get($(this).data('url')).done(function(data){
						if(data.success){
							window.location.reload();
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
				baseUrl+="&position="+$('#positionsCond li.active').data('positions');
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#academicKeyword').val()));
				alert(baseUrl);
				window.location.href=baseUrl;
			}
			
			$('#positionsCond li').on('click',function(){
				$('#positionsCond li').removeClass('active');
				$(this).addClass('active');
				search(1);
			});
			
			$('#searchAcademicBtn').on('click',function(){
				search(1);
			});
			
			$('#academicKeyword').enter(function(){
				search(1);
			});
			
			//点击作者查看详情
			$('.authorDetailPopover').on('click',function(){
				$('.authorDetailPopover').popover('destroy');
				var authorId=$(this).data('authorId').toString();
				if($('#popover_'+authorId).size()==0){
					var $self=$(this);
					var data=$(this).data();
					var perId=data.perId;
					var authorId=data.authorId;
					$.get('<dhome:url value="/institution/${domain}/backend/academic/author/"/>'+perId+"/"+authorId).done(function(result){
						if(result.success){
							$self.popover({
								content:$('#authorTemplate').render(result.data),
								html:true,
								placement:'right',
								template:'<div id="popover_'+authorId+'" class="popover"><div class="arrow"></div><div class="popover-inner"><div class="popover-content" id="popover_content_'+authorId+'"><ul class="popAuthor""></ul></div></div></div>',
								trigger: "manual"	
							});
							$self.popover('show');
						
						}else{
							alert(result.desc);
						}
					});
				}else{
					$(this).popover('hide'); 
				}
			});
			
			$('#academicMenu').addClass('active');
		}); 
	</script>
	
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>姓名：{{= author.trueName}}</li>
		<li>邮箱：{{= author.cstnetId}}</li>
		{{if author.communicationAuthor}}
			<li>通讯作者：是</li>
		{{/if}}
		{{if author.authorStudent}}
			<li>学生在读：是</li>
		{{/if}}
		{{if author.authorTeacher}}
			<li>第一作者导师：是</li>
		{{/if}}
		{{if home}}
			<li><a href="<dhome:url value="/people/{{= home}}"/>" target="_blank">个人主页</a></li>
		{{/if}}
	</script>
</html>