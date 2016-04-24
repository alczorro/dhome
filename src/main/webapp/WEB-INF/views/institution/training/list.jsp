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
			<h3>人才培养管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="training" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
					<jsp:include page="../../commonTraining.jsp"><jsp:param name="activeItem" value="trainings" /> </jsp:include>
					<ul class="listShow student">
						<li class="title">
							<span class="name">姓名</span>
							<span class="inTime">入学时间</span>
							<span class="supervisor">导师</span> 
							<span class="degree">学位</span>
							<span class="status">在读情况</span> 
						</li>
						<c:forEach items="${page.datas }" var="data">
							<li>
										<span class="name">
											<img src="<dhome:img imgId="${userMap[data.cstnetId].image }"/>"/>  
											<span class="name"><c:out value="${data.studentName }"/><span class="country">(中国)</span></span>
											<span class="manage">
												<a class="label label-success" href="<dhome:url value="../detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a>
												<a class="label label-success" href="<dhome:url value="/people/${domain}/admin/training/edit/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
												<a class="label label-danger deleteTraining" href="<dhome:url value="../delete?id[]=${data.id }&page=${page.currentPage }"/>">删除</a>
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
												<c:when test="${ data.degree==0 }">
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
		</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){

// 			//初始化时间控件
// 			$("#citationQueryTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});
// 			//设置开始年份
// 			(function(before){
// 				var year=new Date().getFullYear();
// 				for(var i=year-before-1;i<=year;i++){
// 					$('#start_year').append('<option value="'+i+'">'+i+'</option')
// 				}
// 				$('#start_year').val(year);
// 			})(15);
// 			//开始月份
// 			(function(loop){
// 				for(var i=1;i<loop+1;i++){
// 					$('#start_month').append('<option value="'+i+'">'+i+'</option');
// 				}
// 			})(12); 
// 			//设置结束年份
// 			(function(before){
// 				var year=new Date().getFullYear();
// 				for(var i=year-before-1;i<=year;i++){
// 					$('#end_year').append('<option value="'+i+'">'+i+'</option')
// 				}
// 				$('#end_year').val(year);
// 			})(15);
// 			//结束月份
// 			(function(loop){
// 				for(var i=1;i<loop+1;i++){
// 					$('#end_month').append('<option value="'+i+'">'+i+'</option');
// 				}
// 			})(12); 
			
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
				baseUrl+="&degree="+$('#degreesCond li.active').data('degrees');
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#awardKeyword').val()));
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
			
			$('#trainingMenu').addClass('active');
		
		});
	</script>
</html>