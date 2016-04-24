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
				<jsp:include page="../../commonAwardBackend.jsp"><jsp:param name="activeItem" value="awards" /> </jsp:include>
			    
			     <li class="search">
			    	<form class="bs-docs-example form-search">
			            <input id='awardKeyword' type="text" placeholder="请输入项目名称" class="input-medium search-query" value="<c:out value="${condition.keyword }"/>">
			            <button id="searchBtn" type="button" class="btn">搜索</button>
			        </form>
				 </li>
			</ul>
		  <div class="batch">
		  
	    	<ul class="years" id="gradesCond">
					<li style="text-align: center;" <c:if test="${condition.year== -1}">class="active"</c:if> data-grades="-1">
						<a><strong>全部</strong></a>
					</li>
					<c:forEach items="${gradeMap.entrySet() }" var="entry">
						<li data-grades="${entry.key }" <c:if test="${condition.grade ==entry.key}">class="active"</c:if>>
							<a >  
								<span class="year">${grades[entry.key].val }</span> 
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
						<span class="achievementName">项目名称</span>
						<span class="awardName">获奖名称</span> 
						<span class="institutions">授予单位</span> 
						<span class="category">类别</span> 
						<span class="level">等级</span> 
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="check"><input data-umt-id="${data.id }" type="checkbox" class="checkUser"/></span>
							<!-- 成果名称 -->
							<span class="achievementName">
						    	<c:choose>
							 		<c:when test="${empty data.name}">
											--   
									</c:when> 
									<c:otherwise>
										<c:out value="${data.name}"/>
									</c:otherwise>
								</c:choose>
							
							    <span class="manage">
							    		<a class="label label-success" href="<dhome:url value="/institution/${domain }/backend/award/detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a>
										<a class="label label-success" href="<dhome:url value="/institution/${domain }/backend/award/update/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
										<a class="label label-danger deleteaward" data-url="<dhome:url value="/institution/${domain }/backend/award/delete?awardId[]=${data.id }"/>">删除</a>
								</span>
							</span>
							<!-- 奖励名称 -->
							<span class="awardName">
								<c:choose>
								    <c:when test="${empty data.awardName }">
											--   
									</c:when> 
									<c:otherwise>
										<c:out value="${awardNames[data.awardName].val }"/>
									</c:otherwise>
								</c:choose>
							</span>
							<!-- 授予单位 -->
							<span class="institutions">
								<c:choose>
								    <c:when test="${empty data.grantBody }">
											--   
									</c:when> 
									<c:otherwise>
										<c:out value="${data.grantBody }"/>
									</c:otherwise>
								</c:choose>
							</span>
							<!-- 类别 -->
							<span class="category">
								<c:choose>
								    <c:when test="${empty data.type }">
											--   
									</c:when> 
									<c:otherwise>
										<c:out value="${types[data.type].val }"/>
									</c:otherwise>
								</c:choose>
							</span>
							<!-- 等级 -->
							<span class="level">
								<c:choose>
								    <c:when test="${empty data.grade }">
											--   
									</c:when> 
									<c:otherwise>
										<c:out value="${grades[data.grade].val}"/>
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
	<jsp:include page="../../commonheader.jsp"></jsp:include>
	<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script> 
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){
			var delMsg = "奖励删除以后不可恢复，确认删除吗？";
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='?awardId[]=';
					$('.checkUser:checked').each(function(i,n){
						url+=$(n).data('umtId')+",";
					});
					url=url.substring(0,url.length-1);
					if(url=='?awardId[]'){
						alert('请选择要删除的用户');
						return;
					}
					if(confirm(delMsg)){
						$.get("<dhome:url value='/institution/${domain}/backend/award/delete'/>"+url).done(function(data){
							if(data.success){
								window.location.reload();
							}else{
								alert(data.desc);
							}
						});
					}
				}else{
					alert('请选择批量操作'); 
				}
			});
			
			checkAllBox('checkAll','checkUser');
			
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
/* 				window.location.href=page;
 */				search(page);
			});
			
			$('.deleteaward').on('click',function(){
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
			
			//baseUrl+="&keyword="+encodeURIComponent($.trim($('#paperKeyword').val()));
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
				
				var grade = $('#gradesCond li.active').data('grades');
				if(grade != null){
					baseUrl+="&grade="+grade;
				}
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#awardKeyword').val()));
				window.location.href=baseUrl;
			}
			
			$('#gradesCond li').on('click',function(){
				$('#gradesCond li').removeClass('active');
				$(this).addClass('active');
				search(1);
			});
			
			$('#searchBtn').on('click',function(){
				search(1);
			});
			
			//点击作者查看详情
			$('.authorDetailPopover').on('click',function(){
				$('.authorDetailPopover').popover('destroy');
				var authorId=$(this).data('authorId');
				if($('#popover_'+authorId).size()==0){
					var $self=$(this);
					var data=$(this).data();
					var perId=data.perId;
					var authorId=data.authorId;
					$.get('<dhome:url value="/institution/${domain}/backend/award/author/"/>'+perId+"/"+authorId).done(function(result){
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
			
			$('#awardMenu').addClass('active');
		}); 
		
	</script>
	
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>姓名：{{= author.authorName}}</li>
		<li>邮箱：{{= author.authorEmail}}</li>
		<li>单位：{{= author.authorCompany}}</li>
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