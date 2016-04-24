<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
<dhome:InitLanuage useBrowserLanguage="true"/>
<title>${titleUser.zhName }<fmt:message key="common.dhome"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
</head>

	<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${titleUser.zhName }<fmt:message key="common.dhome"/>
			<jsp:include page="../../showAudit.jsp"/>
		</div>
		<jsp:include page="../../commonMenu.jsp">
			<jsp:param name="activeItem" value="achievement" />
		</jsp:include>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title">
			<h3>奖励管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="award" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonAward.jsp"><jsp:param name="activeItem" value="awards" /> </jsp:include>
<!-- 				<ul class="nav nav-tabs"> -->
<!-- 					<li>&nbsp;&nbsp;&nbsp;</li> -->
<!-- 					<li class="active"> -->
<!-- 					    <a href="#">奖励 </a> -->
<!-- 					</li> -->
<!-- 					<li> -->
<%-- 					     <a href="<dhome:url value="/people/${domain }/admin/award/add"/>">+ 手动添加奖励</a> --%>
<!-- 				    </li> -->
<!-- 				   	<li> -->
<%-- 					    <a href="<dhome:url value="/people/${domain}/admin/award/search"/>">+ 检索添加奖励</a> --%>
<!-- 				    </li> -->
<!-- 				</ul> -->
				<ul class="listShow">
					<li class="title">
						<span class="achievementName">项目名称</span>
						<span class="awardName">获奖名称</span> 
						<span class="institutions">授予单位</span> 
						<span class="category">类别</span> 
						<span class="level">等级</span> 
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
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
							    		<a class="label label-success" href="<dhome:url value="/people/${domain }/admin/award/detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a>
										<a class="label label-success" href="<dhome:url value="/people/${domain }/admin/award/update/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
										<a class="label label-danger deleteaward" href="<dhome:url value="/people/${domain }/admin/award/delete?awardId[]=${data.id }&page=${page.currentPage }"/>">删除</a>
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
								    <c:when test="${data.type==0 }">
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
								    <c:when test="${data.grade==0 }">
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
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){
			$('.deleteMember').on('click',function(){
				return (confirm("用户删除以后不可恢复，确认删除吗？"));
			});
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='';
					$('.checkUser:checked').each(function(i,n){
						url+="&awardId[]="+$(n).data('umtId');
					});
					if(url==''){
						alert('请选择要删除的用户');
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
				window.location.href=page;
			});
			
			$('.deleteaward').on('click',function(){
				return confirm("奖励删除以后不可恢复，确认删除吗？");
			});
			
			//baseUrl+="&keyword="+encodeURIComponent($.trim($('#paperKeyword').val()));
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
				baseUrl+="&year="+$('#yearsCond li.active').data('years');
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#awardKeyword').val()));
				window.location.href=baseUrl;
			}
			
			$('#yearsCond li').on('click',function(){
				$('#yearsCond li').removeClass('active');
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
					$.get('<dhome:url value="/people/${domain}/admin/award/author/"/>'+perId+"/"+authorId).done(function(result){
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