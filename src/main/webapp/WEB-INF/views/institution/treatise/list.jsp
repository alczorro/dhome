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
			<h3>论著管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="treatise" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonTreatise.jsp"><jsp:param name="activeItem" value="treatises" /> </jsp:include>
<!-- 				<ul class="nav nav-tabs"> -->
<!-- 					<li>&nbsp;&nbsp;&nbsp;</li> -->
<!-- 					<li class="active"> -->
<!-- 					    <a href="#">论著 </a> -->
<!-- 					</li> -->
<!-- 					<li> -->
<%-- 					     <a href="<dhome:url value="/people/${domain }/admin/treatise/add"/>">+ 手动添加论著</a> --%>
<!-- 				    </li> -->
<!-- 				   	<li> -->
<%-- 					    <a href="<dhome:url value="/people/${domain}/admin/treatise/search"/>">+ 检索添加论著</a> --%>
<!-- 				    </li> -->
<!-- 				</ul> -->
				<ul class="listShow">
					<li class="title">
						<!-- <span class="check"><input type="checkbox"  id="checkAll"/></span> -->
						<span class="treatiseName">论著名称</span>
						<span class="publisher">出版社</span> 
						<span class="author">作者</span> 
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<%-- <span class="check"><input data-umt-id="${data.id }" type="checkbox" class="checkUser"/></span> --%>
							
							<span class="treatiseName">
						    	<c:choose>
							 		<c:when test="${empty data.name}">
											--   
									</c:when> 
									<c:otherwise>
										<c:out value="${data.name}"/>
									</c:otherwise>
								</c:choose>
							
							    <span class="manage">
										<a class="label label-success" href="<dhome:url value="/people/${domain }/admin/treatise/detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a>
										<a class="label label-success" href="<dhome:url value="/people/${domain }/admin/treatise/update/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
										<a class="label label-danger deleteTreatise" href="<dhome:url value="../delete?treatiseId[]=${data.id }&page=${page.currentPage }"/>">删除</a>
								</span>
							</span>
							<span class="publisher">
								<c:choose>
								    <c:when test="${data.publisher==0 }">
											--   
									</c:when> 
									<c:otherwise>
										<c:out value="${publishers[data.publisher].val }"/>
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
											<a class="authorDetailPopover"  data-per-id="${data.id }" data-author-id="${author.id }" ><c:out value="${author.authorName }"/></a>
												<sup>[${author.subscriptIndex }]</sup>
											</li>
										</c:forEach>
										</ul>
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
						url+="&treatiseId[]="+$(n).data('umtId');
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
			
			$('.deleteTreatise').on('click',function(){
				return (confirm("论著删除以后不可恢复，确认删除吗？"));
			});
			
			/* baseUrl+="&year="+$('#yearsCond li.active').data('years'); */
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#awardKeyword').val()));
				window.location.href=baseUrl;
			}
			
			
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
					$.get('<dhome:url value="/people/${domain}/admin/treatise/author/"/>'+perId+"/"+authorId).done(function(result){
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
			
			$('#treatiseMenu').addClass('active');
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