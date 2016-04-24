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
					<li>&nbsp;&nbsp;&nbsp;</li>
					<li class="active">
					    <a href="#">课题 ${page.allSize }</a>
					</li>
					<li>
					    <a href="<dhome:url value="/institution/${domain }/backend/topic/create"/>">+ 添加课题</a>
				    </li>
				   	<li>
					    <a href="#">批量导入</a>
				    </li>
				    <li class="search">
				    	<form class="bs-docs-example form-search">
				            <input type="text" class="input-medium search-query">
				            <button type="submit" class="btn">搜索</button>
				        </form>
				    </li>
				    
				</ul>
				<div class="batch">
					<ul class="years">
						<li>
							<a>
								<span class="year">2014</span>
								<span class="count">87</span>
							</a>
						</li>
						<li>
							<a>
								<span class="year">2013</span>
								<span class="count">23</span>
							</a>
						</li>
						<li>
							<a>
								<span class="year">2012</span>
								<span class="count">56</span>
							</a>
						</li>
					</ul>
					<select id="batchOperation">
						<option>--批量操作--</option>
						<option value="batchDelete">删除</option>
					</select>
					<input type="button" id="batchBtn" value="确认" class="btn btn-primary"/>
<%-- 					<a href="<dhome:url value="/institution/${domain }/backend/topic/create"/>">添加课题</a> --%>
				</div>
				<ul class="listShow">
					<li class="title">
						<span class="check"><input type="checkbox"  id="checkAll"/></span>
						<span class="employee">成果名称</span>
						<span class="department">类别</span>
						<span class="department">等级</span> 
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="check"><input data-id="${data.id }" type="checkbox" class="checkUser"/></span>
									<span class="employee">
										<c:choose>
											<c:when test="${empty data.name }">
													--   
											</c:when>
											<c:otherwise>
												<c:out value="${data.name }"/>
											</c:otherwise>
										</c:choose>
										<span class="manage">
<%-- 										<a class="editInfo btn btn-primary" data-id="${data.id }">编辑</a> --%>
											<a class="label label-success" href="<dhome:url value="/institution/${domain}/backend/topic/edit/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
											<a class="label label-danger deleteTopic" href="<dhome:url value="../delete?id[]=${data.id }&page=${page.currentPage }"/>">删除</a>
										</span>
									</span> 
									<span class="department">
										<c:choose>
											<c:when test="${empty data.type }">
													--   
											</c:when>
											<c:otherwise>
												<c:out value="${data.type }"/>
											</c:otherwise>
										</c:choose>
									</span>
									<span class="department">
										<c:choose>
											<c:when test="${ empty data.topic_no }">
													--   
											</c:when>
											<c:otherwise>
												<c:out value="${data.topic_no }"/>
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
				/* window.location.href=page; */
				search(page);
			});
			
			$('#topicMenu').addClass('active');
			//编辑课题Dialog，前台验证
			$('#editForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 topic_no:{
						 required:true
						 },
					name:{
						 required:true
						 },
				 	
				 	funds_from:{
				 		required:true
				 	},
				 	project_cost:{
				 		required:true,
				 		min:0,
				 		digits:true,
				 		max:99999999
				 	},
				 	personal_cost:{
				 		required:true,
				 		min:0,
				 		digits:true,
				 		max:99999999
				 	}
				 },
				 messages:{
					 topic_no:{
						 required:'编号不允许为空'
						 },
						 name:{
							 required:'名字不允许为空'
						 },
						 funds_from:{
					 		required:'资金来源不允许为空'
					 	},
					 	project_cost:{
					 		required:'项目经费不允许为空',
					 		min:'超出允许值',
					 		digits:'必须为正整数',
					 		max:'超出允许值'
					 	},
					 	personal_cost:{
					 		required:'个人经费不允许为空',
					 		min:'超出允许值',
					 		digits:'必须为正整数',
					 		max:'超出允许值'
					 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
		});
	</script>
</html>