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
			<h3>专利管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="patent" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonPatent.jsp"> <jsp:param name="activeItem" value="patents" /> </jsp:include>
<!-- 				<ul class="nav nav-tabs"> -->
<!-- 					<li>&nbsp;&nbsp;&nbsp;</li> -->
<!-- 					<li class="active"> -->
<!-- 					    <a href="#">专利 </a> -->
<!-- 					</li> -->
<!-- 					<li> -->
<%-- 					     <a href="<dhome:url value="/people/${domain }/admin/patent/create"/>">+ 手动添加专利</a> --%>
<!-- 				    </li> -->
<!-- 				   	<li> -->
<%-- 					    <a href="<dhome:url value="/people/${domain}/admin/patent/search"/>">+ 检索添加专利</a> --%>
<!-- 				    </li> -->
<!-- 				</ul> -->
				<ul class="listShow">
					<li class="title">
						<span class="article">成果名称</span>
						<span class="author">类别</span>
						<span class="quot">等级</span> 
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
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
											<a class="label label-success" href="<dhome:url value="/people/${domain}/admin/patent/edit/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
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
											<c:when test="${data.grade==0 }">
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
		
		//根据查询条件查询
		function search(baseUrl){
			if((baseUrl+'').indexOf('?')==-1){
				baseUrl+="?_=";
			}
			baseUrl+="&grade="+$('#gradesCond li.active').data('grades');
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
		
		$('#patentMenu').addClass('active');
	
	});
	</script>

</html>