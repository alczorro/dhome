<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<html lang="en">
	<head>
		<title>机构论文</title>
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
					<jsp:include page="../../commonPaperBackend.jsp"><jsp:param name="activeItem" value="relates" /> </jsp:include>
				 	<li class="search">
				    	<form class="bs-docs-example form-search">
				            <input id='memberKeyword' type="text" class="input-medium search-query" placeholder="请输入作者姓名" value="<c:out value="${condition.keyword }"/>">
				            <button id="searchMemberBtn" type="button" class="btn">搜索</button>
				        </form>
				    </li>
				</ul>
				<div class="batch">
					<ul class="years" id="pubYearsCond">
						<li data-status="0" <c:if test="${condition.status==0 }">class="active"</c:if> >
							<a><strong>全部</strong></a>
						</li>
						<li data-status="1" <c:if test="${condition.status==1 }">class="active"</c:if> >
							<a><strong><i class="icon icon-user"></i>已认证作者</strong></a>
						</li>
						<li data-status="-1" <c:if test="${condition.status==-1}">class="active"</c:if>>
							<a><strong>未认证(有候选)</strong></a>
						</li>
						<li data-status="2" <c:if test="${condition.status==2}">class="active"</c:if>>
							<a><strong>未认证(无候选)</strong></a>
						</li>
					</ul>
				</div>
				<ul class="listShow">
					<li class="title">
						<!-- <span class="check"><input type="checkbox"  id="checkAll"/></span> -->
						<span class="author">作者姓名</span> 
						<span class="article">认证状态</span>
						<span class="quot">操作</span>
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<%-- <span class="check"><input data-author-id="${data.id }" type="checkbox" class="checkItem"/></span> --%>
							<span class="author">
								<a class="authorDetailPopover"  data-author-id="${data.id }"><c:out value="${data.authorName }"/></a>
							</span>
							<span class="article">
								<c:if test="${data.status!=1 }">未认证<c:if test="${!empty userMap[data.cstnetId].trueName }">(${userMap[data.cstnetId].trueName } - ${userMap[data.cstnetId].cstnetId })</c:if></c:if>
								<c:if test="${data.status==1 }"><b class="color-ok">已认证(${userMap[data.cstnetId].trueName } - ${userMap[data.cstnetId].cstnetId })</b></c:if>
							</span>
							<span class="quot">
								<c:if test="${data.status!=1 }"><a class="label label-info" href="<dhome:url value="/institution/${domain }/backend/paper/relate/1?authorId=${data.id }"/>">认证</a></c:if>
<%-- 								<c:if test="${data.status==0 }"> --%>
<%-- 								<a class="label label-success" href="<dhome:url value="/institution/${domain }/backend/paper/relate/1?authorId=${data.id }"/>">去认证</a> --%>
<%-- 								<a class="label label-success" href="<dhome:url value="/institution/${domain }/backend/paper/relate/1?authorId=${data.id }"/>">确认</a> --%>
<%-- 								</c:if> --%>
								<c:if test="${data.status==1 }"><a class="label label-success" href="<dhome:url value="/institution/${domain }/backend/paper/relate/1?authorId=${data.id }"/>">修改</a></c:if>
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
			//点击作者查看详情
			$('.authorDetailPopover').on('click',function(){
				$('.authorDetailPopover').popover('destroy');
				var authorId=$(this).data('authorId');
				if($('#popover_'+authorId).size()==0){
					var $self=$(this);
					var data=$(this).data();
					var authorId=data.authorId;
					$.get('<dhome:url value="/institution/${domain}/backend/paper/author/"/>'+authorId).done(function(result){
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
			
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
				baseUrl+="&status="+$('#pubYearsCond li.active').data('status');
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#memberKeyword').val()));
				window.location.href=baseUrl;
			}
			
			checkAllBox('checkAll','checkItem');
			
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				search(page);
			});
			
			$('#pubYearsCond li').on('click',function(){
				$('#pubYearsCond li').removeClass('active');
				$(this).addClass('active');
				search(1);
			});
			$('#orderCondition li').on('click',function(){
				$('#orderCondition li').removeClass('active');
				$(this).addClass('active');
				search(1);
			});
			$('#searchMemberBtn').on('click',function(){
				search(1);
			});
			$('#memberKeyword').enter(function(){
				search(1);
			});
			
			$('#paperMenu').addClass('active');
		}); 
	</script>
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>姓名：{{= author.authorName}}</li>
		<li>邮箱：{{= author.authorEmail}}</li>
		<li>单位：{{= author.authorCompany}}</li>
		{{if home}}
			<li><a href="<dhome:url value="/people/{{= home}}"/>" target="_blank">个人主页</a></li>
		{{/if}}
	</script>
</html>