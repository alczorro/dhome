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
		<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.css"/>" />
	</head>

	<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
		<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
		
		<div class="container">
			<jsp:include page="../leftMenu.jsp"></jsp:include>
			<div class="ins_backend_rightContent">
				<ul class="nav nav-tabs">
					<jsp:include page="../../commonPaperBackend.jsp"><jsp:param name="activeItem" value="relates" /> </jsp:include>
				</ul>
				<p class="authorHere">
					作者&nbsp;<span class="alert alert-info" style="padding:3px">${author.authorName } - ${author.authorEmail } - ${author.authorCompany }</span>&nbsp;的论文如下：
				</p>
				<ul class="listShow">
					<li class="title">
						<span class="check"><input type="checkbox"  id="checkAll"/></span>
						<span class="article">标题</span>
						<span class="author">作者</span> 
<!-- 						<span class="quot">引用频次</span> -->
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="check"><input data-paper-id="${data.id }" type="checkbox" class="checkItem"/></span>
							<span class="article">
								<span class="title"><c:out value="${data.title }"/></span>
								<span><c:if test="${data.status==1 }"><i class="icon icon-ok"></i></c:if></span>
								<span class="detail">
									<!-- doi -->
									<c:if test="${!empty data.doi }">
										dio:<c:out value="${data.doi }"/>. 
									</c:if>
									<!-- issn -->
									<c:if test="${!empty data.issn }">
										issn:<c:out value="${data.issn }"/>. 
									</c:if>
									<!-- 刊物 -->
									<c:if test="${data.publicationId!=0 }">
										<c:out value="${pubMap[data.publicationId].pubName }"/>
									</c:if>
									<!-- 卷号 -->
									<c:if test="${!empty data.volumeNumber }">
										卷:<c:out value="${data.volumeNumber }"/>. 
									</c:if>
									<!-- 期号 -->
									<c:if test="${!empty data.series }">
										期:<c:out value="${data.series }"/>. 
									</c:if>
									<!-- 开始页~结束页 -->
									<c:if test="${!empty data.publicationPage }">
										页:${data.publicationPage}. 
									</c:if>
									<!-- 发表时间 年.月 -->
									出版年:${data.publicationYear}<c:if test="${data.publicationMonth!=0 }">.${data.publicationMonth}</c:if>. 
								</span>
									<div id="summary_${data.id }" style="display:none"><c:out value="${data.summary }"/></div>
									<!-- 原文链接 -->
									<c:if test="${!empty data.originalLink }">
										<a href="<c:out value="${data.originalLink }"/>" target="_blank">原文链接</a>
									</c:if>
									<!-- 原文下载 -->
									<c:if test="${data.clbId!=0 }">
										<a href="<dhome:url value="/system/file?fileId=${data.clbId }"/>" target="_blank">原文下载</a>
									</c:if>
									<!-- 摘要 -->
									<c:if test="${!empty data.summary}">
										<a data-paper-id="${data.id }" class="showSummary">查看摘要</a>
									</c:if>
<!-- 									<span class="manage"> -->
<%-- 										<a class="label label-success" href="<dhome:url value="/institution/${domain }/backend/paper/detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a> --%>
<%-- 										<a class="label label-success" href="<dhome:url value="/institution/${domain }/backend/paper/update/${data.id }?returnPage=${page.currentPage }"/>">编辑</a> --%>
<%-- 										<a class="label label-danger deletePaper" data-url="<dhome:url value="/institution/${domain }/backend/paper/delete?paperId[]=${data.id }&page=${page.currentPage }"/>">删除</a> --%>
<!-- 									</span> -->
								
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
											<a class="authorDetailPopover"  data-paper-id="${data.id }" data-author-id="${author.id }" ><c:out value="${author.authorName }"/></a>
												<sup>[${author.subscriptIndex }]</sup>
											</li>
										</c:forEach>
										</ul>
									</c:otherwise>
								</c:choose>
							</span>
<!-- 							<span class="quot"> -->
<%-- 								<c:out value="${paper.citation == -1?'--':paper.citation }"/> --%>
<!-- 							</span> -->
						</li> 
					</c:forEach>
				</ul>
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
				<div class="userToAuthor">
						<p class="bigFont">
							与已选论文中的作者&nbsp;<span class="alert alert-info" style="padding:3px">${author.authorName } - ${author.authorEmail } - ${author.authorCompany }</span>&nbsp;相关的用户：
							<span id="userNameSpan" class="alert" style="padding:3px" data-uid="${user.cstnetId }">
							<c:if test="${!empty user.trueName }">${user.trueName }
								<c:if test="${!empty user.cstnetId }">(${user.cstnetId })
								</c:if>
								<a id="removeUser"><i class="icon icon-trash"></i></a>
							</c:if></span>
							<c:if test="${!empty user.trueName }">
								<a class="btn btn-link" id="modifyUser">修改</a>
							</c:if>
							<c:if test="${empty user.trueName }">
								<a class="btn btn-link" id="modifyUser">添加</a>
							</c:if>
						</p>
						<p id="chooseUser">
							<input type="text" name="authorUser" class="autoCompleteSearch register-xlarge" id="userSearch" placeHolder="请输入用户的姓名或者邮箱"/>
						</p>
						<input type="button" id="batchBtn" value="作者认证" class="btn btn-success"/>
						<c:if test="${!empty user.trueName }">
							<input type="button" id="cancelBtn" value="取消认证" class="btn btn-success"/>
						</c:if>
						<a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a>
				</div>
			</div>
		</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
	<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){
			$("#modifyUser").click(function(){
				$("p#chooseUser").toggle();
				$("#userSearch").focus();
			});
			//查询用户
			$("#userSearch").autocomplete('<dhome:url value="/institution/${domain}/backend/paper/search/user"/>',
			            {
					  		width:400,
							parse:function(data){
									return $.map(data, function(item) {
										return {
											data : item,
											result : '',
											value:item.trueName
										};
									});
							},
							formatItem:function(row, i, max) {
			    				return  row.trueName+"("+row.cstnetId+")";
			 				},
							formatMatch:function(row, i, max) {
			    				return row.trueName + " " + row.cstnetId;
			 				},
							formatResult: function(row) {
			    				return row.trueName; 
			 				}
						}).result(function(event,item){
							if(item.cstnetId==null||item.cstnetId==""){
								alert("该员工没有提供邮箱，暂无法关联!");
								return false;
							}
							$("#userNameSpan").addClass("alert-success");
							$("#userNameSpan").html(item.trueName + "(" +item.cstnetId + ")");
							$("#userNameSpan").attr("data-uid",item.cstnetId);
 							$('#modifyUser').html("修改");
						});
			//点击作者查看详情
			$('.authorDetailPopover').on('click',function(){
				$('.authorDetailPopover').popover('destroy');
				var authorId=$(this).data('authorId');
				if($('#popover_'+authorId).size()==0){
					var $self=$(this);
					var data=$(this).data();
					var paperId=data.paperId;
					var authorId=data.authorId;
					$.get('<dhome:url value="/institution/${domain}/backend/paper/author/"/>'+paperId+"/"+authorId).done(function(result){
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
			//查看摘要
			$('.showSummary').on('click',function(){
				var paperId=$(this).data('paperId');
				$('#summary_'+paperId).toggle();
			});
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
// 				baseUrl+="&publicationYear="+$('#pubYearsCond li.active').data('years');
// 				baseUrl+="&order="+getOrder();
// 				baseUrl+="&orderType="+orderType;
// 				baseUrl+="&keyword="+encodeURIComponent($.trim($('#paperKeyword').val()));
				baseUrl+="&authorId="+${author.id};
				window.location.href=baseUrl;
			}
			
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				search(page);
			});
			
			checkAllBox('checkAll','checkItem');
			$(".checkItem").attr("checked",true);
			$("#checkAll").attr("checked",true);
			
			
			$('#batchBtn').on('click',function(){
					var url='';
					var flag=false;
					if($("#userNameSpan").html().replace(/\s/g, "")==""){
						url+="uid=";
					}else{
						url+="uid="+$("#userNameSpan").data('uid');
					}
					url+="&authorId[]="+${author.id.toString()};
					$('.checkItem:checked').each(function(i,n){
						url+="&paperId[]="+$(n).data('paperId').toString();
						flag=true;
					});
					if(!flag){
						url+="&paperId[]=";
						flag=false;
					}
					
					if(confirm("确认关联吗？")){
						$.get("<dhome:url value='/institution/${domain}/backend/paper/relateSubmit?'/>"+url).done(function(data){
							if(data.success){
								alert("关联成功！");
								window.location.reload();
							}else{
								alert(data.desc);
							}
						});
					}
			});
			
			$('#cancelBtn').on('click',function(){
				var url='';
				if($("#userNameSpan").html().replace(/\s/g, "")==""){
					url+="uid=";
				}else{
					url+="uid="+$("#userNameSpan").data('uid');
				}
 				var flag=false;
// 				if($("#userNameSpan").html().replace(/\s/g, "")==""){
// 					url+="uid=";
// 				}else{
// 					url+="uid="+$("#userNameSpan").data('uid');
// 				}
				url+="&authorId[]="+${author.id.toString()};
 				$('.checkItem:checked').each(function(i,n){
				url+="&paperId[]="+$(n).data('paperId').toString();
				flag=true;
			});
			if(!flag){
				url+="&paperId[]=";
				flag=false;
			}
				
				if(confirm("确认取消关联吗？")){
					$.get("<dhome:url value='/institution/${domain}/backend/paper/relateCancel?'/>"+url).done(function(data){
						if(data.success){
							alert("取消成功！");
							window.location.reload();
						}else{
							alert(data.desc);
						}
					});
				}
		});
			$('#removeUser').on('click',function(){
				if(confirm("确认删除吗？")){
					$("#userNameSpan").html("");
					$("#userNameSpan").attr("data-uid","");
				}
			});
// 			$('#pubYearsCond li').on('click',function(){
// 				$('#pubYearsCond li').removeClass('active');
// 				$(this).addClass('active');
// 				search(1);
// 			});
// 			$('#orderCondition li').on('click',function(){
// 				$('#orderCondition li').removeClass('active');
// 				$(this).addClass('active');
// 				if(!$(this).find('a i').attr('class')){
// 					orderType=1;
// 				}else{
// 					orderType=(orderType==2?1:orderType+1);
// 				}  
// 				search(1);
// 			});
// 			$('#searchPaperBtn').on('click',function(){
// 				search(1);
// 			});
// 			$('#paperKeyword').enter(function(){
// 				search(1);
// 			});
			
			$('#paperMenu').addClass('active');
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