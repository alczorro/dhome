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
			<h3>课题管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="topic" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonTopic.jsp"><jsp:param name="activeItem" value="topics" /> </jsp:include>
				<ul class="listShow topic">
					<li class="title">
<!-- 						<span class="num">编号</span>  -->
						<span class="name">名称</span>
<!-- 						<span class="beginTime">开始<br>时间</span> -->
<!-- 						<span class="endTime">结束<br>时间</span>  -->
						<span class="source">资金来源</span>
						<span class="type">类别</span>
						<span class="member">课题组成员</span>
 						<span class="role">本人角色</span> 
<!-- 						<span class="selfFunds">个人经费</span> -->
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="name">
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
									<a class="label label-success" href="<dhome:url value="/people/${domain}/admin/topic/edit/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
									<a class="label label-danger deleteTopic" href="<dhome:url value="../delete?id[]=${data.id }&page=${page.currentPage }"/>">删除</a>
								</span>
							</span> 
							<span class="source">
								<c:choose>
									<c:when test="${data.funds_from==0 }">
											--   
									</c:when>
									<c:otherwise>
										${fundsFroms[data.funds_from].val}
									</c:otherwise>
								</c:choose>
							</span>
							<span class="type">
								<c:choose>
									<c:when test="${data.type==0 }">
											--   
									</c:when>
									<c:otherwise>
										${types[data.type].val}
									</c:otherwise>
								</c:choose>
							</span>
							
							<span class="member">
								<c:choose>
									<c:when test="${empty authorMap[data.id]}">
									--
									</c:when>
									<c:otherwise>
										<ul>
										<c:forEach items="${ authorMap[data.id]}" var="author">
											<c:choose>
												<c:when test="${author.authorType eq 'admin' }">
													<li >
													<a class="authorDetailPopover"  data-topic-id="${data.id }" data-author-id="${author.id }" ><c:out value="${author.authorName }"/></a>
														<sup class="main">[负责人]</sup>
													</li>
												</c:when>
											</c:choose>
										</c:forEach>
										<c:forEach items="${ authorMap[data.id]}" var="author">
											<c:choose>
												<c:when test="${author.authorType eq 'member' }">
													<li >
													<a class="authorDetailPopover"  data-topic-id="${data.id }" data-author-id="${author.id }" ><c:out value="${author.authorName }"/></a>
<!-- 														<sup>[参与者]</sup> -->
													</li>
												</c:when>
											</c:choose>
										</c:forEach>
										</ul>
									</c:otherwise>
								</c:choose>
							</span> 
							<span class="role">
								<c:choose>
									<c:when test="${data.role eq 'admin' }">负责人</c:when>
									<c:when test="${data.role eq 'member' }">参与人</c:when>
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
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<jsp:include page="../../commonfooter.jsp"></jsp:include>

</body>
<jsp:include page="../../commonheader.jsp"></jsp:include>
<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){

			//初始化时间控件
			$("#citationQueryTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});
			//设置开始年份
			(function(before){
				var year=new Date().getFullYear();
				for(var i=year-before-1;i<=year;i++){
					$('#start_year').append('<option value="'+i+'">'+i+'</option')
				}
				$('#start_year').val(year);
			})(15);
			//开始月份
			(function(loop){
				for(var i=1;i<loop+1;i++){
					$('#start_month').append('<option value="'+i+'">'+i+'</option');
				}
			})(12); 
			//设置结束年份
			(function(before){
				var year=new Date().getFullYear();
				for(var i=year-before-1;i<=year;i++){
					$('#end_year').append('<option value="'+i+'">'+i+'</option')
				}
				$('#end_year').val(year);
			})(15);
			//结束月份
			(function(loop){
				for(var i=1;i<loop+1;i++){
					$('#end_month').append('<option value="'+i+'">'+i+'</option');
				}
			})(12); 
			
			$('.deleteTopic').on('click',function(){
				return (confirm("课题删除以后不可恢复，确认删除吗？"));
			});
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='';
					$('.checkUser:checked').each(function(i,n){
						url+="&id[]="+$(n).data('id');
					});
					if(url==''){
						alert('请选择要删除的课题');
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
			
			$('#topicMenu').addClass('active');
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
				baseUrl+="&fundsFrom="+$('#fundsCond li.active').data('funds');
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#awardKeyword').val()));
				window.location.href=baseUrl;
			}
			
			$('#fundsCond li').on('click',function(){
				$('#fundsCond li').removeClass('active');
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
					var topicId=data.topicId;
					var authorId=data.authorId;
					$.get('<dhome:url value="/people/${domain}/admin/topic/author/"/>'+topicId+"/"+authorId).done(function(result){
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
			
		});
	</script>
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>姓名：{{= author.authorName}}</li>
		<li>邮箱：{{= author.authorEmail}}</li>
		<li>单位：{{= author.authorCompany}}</li>
		<li>作者角色：{{if author.authorType=='admin'}}负责人{{/if}}
				{{if author.authorType=='member'}}参与者{{/if}}</li>
	</script>
</html>