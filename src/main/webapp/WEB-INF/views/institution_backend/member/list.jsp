<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<html lang="en">
	<head>
		<title>机构成员</title>
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
					<jsp:include page="../../commonMemberBackend.jsp"><jsp:param name="activeItem" value="members" /> </jsp:include>
				     <li class="search">
				    	<form class="bs-docs-example form-search">
				            <input id='memberKeyword' type="text" class="input-medium search-query" placeholder="请输入员工姓名" value="<c:out value="${condition.keyword }"/>">
				            <button id="searchMemberBtn" type="button" class="btn">搜索</button>
				        </form>
				    </li>
 			 </ul> 
				 <ul id="memberNavbar" class="nav nav-pills scholar d-nobottom" style="margin:10px 0px 10px 5px;float:left">
						 <li class="dropdown <c:if test="${!empty keywordDept.shortName }">active</c:if>" id="memberDept" data-depts="${keywordDept.id }">
							<a id="member-dept" class="dropdown-toggle" data-toggle="dropdown" href="#dept2" style="display:inline-block; float:left;">
							<c:if test="${empty keywordDept }">按部门</c:if><c:if test="${!empty keywordDept.shortName }">${keywordDept.shortName }</c:if><b class="caret"></b>
							</a>
							<ul id="menu11" class="dropdown-menu">
								<c:forEach items="${dept }" var="item">
								  <li><a href="javascript:selectMemberDept('${item.id }','${item.shortName }')">${item.shortName}</a></li>
							   </c:forEach>
							</ul>
						 </li>
						 <li class="dropdown <c:if test="${!empty keywordTitle }">active</c:if>" id="technicalTitle" data-titles="${keywordTitle }">
							<a id="technical_title" class="dropdown-toggle"	data-toggle="dropdown" href="#dept2" style="display:inline-block; float:left;">
							<c:if test="${empty keywordTitle }">按职称</c:if><c:if test="${!empty keywordTitle }">${keywordTitle }</c:if><b class="caret"></b>
							</a>
							<ul class="dropdown-menu">
								<c:forEach items="${ titles.entrySet() }" var="entry">
								  <li><a href="javascript:selectTechnicalTitle('${entry.key }')">${entry.key}</a></li>
							   </c:forEach>
							</ul>
						 </li>
						 
<%-- 						  <li class="dropdown <c:if test="${!empty keywordStatus }">active</c:if>" id="jobStatus" data-titles="${keywordStatus }"> --%>
<!-- 							<a id="job_status" class="dropdown-toggle"	data-toggle="dropdown" href="#dept2" style="display:inline-block; float:left;"> -->
<%-- 							<c:if test="${empty keywordStatus }">按状态</c:if><c:if test="${!empty keywordStatus }">${keywordStatus }</c:if><b class="caret"></b> --%>
<!-- 							</a> -->
<!-- 							<ul class="dropdown-menu"> -->
<!-- 								 <li><a href="javascript:selectJobStatus('在岗')">在岗</a></li> -->
<!-- 								 <li><a href="javascript:selectJobStatus('内退')">内退</a></li> -->
<!-- 								 <li><a href="javascript:selectJobStatus('长期出国')">长期出国</a></li> -->
<!-- 								 <li><a href="javascript:selectJobStatus('暂停分配')">暂停分配</a></li> -->
<!-- 							</ul> -->
<!-- 						 </li> -->
		       		</ul>
				<div class="batch clear">
					<select id="batchOperation">
						<option>--批量操作--</option>
						<option value="batchDelete">删除</option>
					</select>
					<input type="button" id="batchBtn" value="确认" class="btn btn-primary"/>
					<span class="rightPage">
						在职状态：
						<select id="jStatus" onChange="search(1)">
			       			<option value="" ${empty condition.jobStatus?'selected="selected"':''}>全部</option>
							<option value="在岗" ${condition.jobStatus eq '在岗'?'selected="selected"':''}>在岗</option>
							<option value="内退" ${condition.jobStatus eq '内退'?'selected="selected"':''}>内退</option>
							<option value="长期出国" ${condition.jobStatus eq '长期出国'?'selected="selected"':''}>长期出国</option>
							<option value="暂停分配" ${condition.jobStatus eq '暂停分配'?'selected="selected"':''}>暂停分配</option>
						</select> &nbsp;&nbsp;&nbsp;&nbsp;
						每页显示：
						<select id="paging" onChange="search(1)">
							<option value="10" ${condition.paging==10?'selected="selected"':''}>10条</option>
							<option value="5" ${condition.paging==5?'selected="selected"':''}>5条</option>
							<option value="20" ${condition.paging==20?'selected="selected"':''}>20条</option>
							<option value="50" ${condition.paging==50?'selected="selected"':''}>50条</option>
						</select>
					</span>
				</div>
				<ul class="listShow">
					<li class="title">
						<span class="check"><input type="checkbox"  id="checkAll"/></span>
						<span class="employee">姓名</span>
						<span class="department">所属部门</span> 
						<span class="pro-title">职称</span>
						<span class="status">在职状态</span>
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="check"><input data-umt-id="${data.umtId }" type="checkbox" class="checkUser"/></span>
							<span class="employee">
								<a class="left" href="<dhome:url value="/institution/${domain}/backend/member/detail/${data.umtId }"/>">
									<img src="<dhome:img imgId="${userMap[data.cstnetId].image }"/>"/>  
								</a>
								<span class="right">
									<a href="<dhome:url value="/institution/${domain}/backend/member/detail/${data.umtId }"/>" class="name"><c:out value="${data.trueName }"/></a>
									<span class="mail"><c:out value="${data.cstnetId }"/></span> 
									<c:if test="${! empty homeMap[userMap[data.cstnetId].id] }">
										<a class="dhome" target="_blank" href="<dhome:url value="/people/${homeMap[userMap[data.cstnetId].id].url }"/>">个人主页</a>
									</c:if> 
									<span class="manage">
										<a class="label label-success" href="<dhome:url value="/institution/${domain}/backend/member/detail/${data.umtId }"/>">查看</a>
										<a class="label label-danger deleteMember" href="<dhome:url value="/institution/${domain}/backend/member/delete?umtId[]=${data.umtId }&page=${page.currentPage }"/>">删除</a>
									</span>
								</span>
							</span>
							<span class="department">
								<c:choose>
									<c:when test="${data.departId==0 }">
											--   
									</c:when>
									<c:otherwise>
										<c:out value="${deptMap[data.departId].shortName }"/>
									</c:otherwise>
								</c:choose>
							</span>
							<span class="pro-title">
								<c:choose>
									<c:when test="${empty data.technicalTitle }">
										--
									</c:when>
									<c:otherwise>
										<c:out value="${data.technicalTitle }"/>
									</c:otherwise>
								</c:choose>
							</span>
							<span class="status">
								<c:choose>
									<c:when test="${empty data.jobStatus }">
										--
									</c:when>
									<c:otherwise>
										<c:out value="${data.jobStatus }"/>
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
			
			$("ul.nav.nav-pills > li > a").click(function(){
				$("#navbar .active").removeClass("active");
				$(this).parent().addClass("active");
			});
			
			$("#memberNavbar > li > a").click(function(){
				$("#memberNavbar .active").removeClass("active");
				$(this).parent().addClass("active");
			});
			
			var delMsg = "用户删除以后不可恢复，确认删除吗？";
			$('.deleteMember').on('click',function(){
				return (confirm(delMsg));
			});
			
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='';
					$('.checkUser:checked').each(function(i,n){
						url+="&umtId[]="+$(n).data('umtId');
					});
					if(url==''){
						alert('请选择要删除的用户');
						return;
					}
					if(confirm(delMsg)){
						window.location.href="../delete?page=${page.currentPage}"+url;
					}
				}else{
					alert('请选择批量操作'); 
				}
			});
			
// 			$('#titlesCond li').on('click',function(){
// 				$('#titlesCond li').removeClass('active');
// 				$(this).addClass('active');
// 				search(1);
// 			});
			
			$('#searchBtn').on('click',function(){
				search(1);
			});
			
			checkAllBox('checkAll','checkUser');
			
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				memberDpetId=$('#memberDept').data('depts');
				title=$('#technicalTitle').data('titles');
				search(page);
			});
			
			$('#memberMenu').addClass('active');
			
			
		
			
		}); 
	</script>
	<script>
	var memberDpetId=-1;
	var title="全部";
	var dept="";
	var jobStatus="全部";
	function selectMemberDept(id,name){
		$('#member-dept').html(name+'<b class="caret">');
 		$('#memberDept').toggleClass("open",false);
		memberDpetId = id;
		dept=name;
		title=$('#technicalTitle').data('titles');
		if(title==''){
			title="其他";
		}
		search(1);
	}
	
	function selectTechnicalTitle(name){
		$('#technical_title').html(name+'<b class="caret">');
 		$('#technicalTitle').toggleClass("open",false);
		
		title=name;
		memberDpetId=$('#memberDept').data('depts');
		if(memberDpetId==0){
			memberDpetId=-1;		
		}
		search(1);
	}
// 	function selectJobStatus(name){
// 		$('#job_status').html(name+'<b class="caret">');
//  		$('#jobStatus').toggleClass("open",false);
		
//  		jobStatus=name;
// 		memberDpetId=$('#memberDept').data('depts');
// 		if(memberDpetId==0){
// 			memberDpetId=-1;		
// 		}
// 		search(1);
// 	}
	//根据查询条件查询
	function search(baseUrl){
		if((baseUrl+'').indexOf('?')==-1){
			baseUrl+="?_=";
		}
//			var grade = $('#titlesCond li.active').data('titles');
		if(title != null && title!=''&& title!='全部'){
			baseUrl+="&title="+title;
		}
		if($('#paging').val()!=""){
			baseUrl+="&paging="+parseInt($('#paging').val());
		}
		if($('#jStatus').val()!=""){
			baseUrl+="&jobStatus="+$('#jStatus').val();
		}
		if(memberDpetId!=-1){
			baseUrl+="&memberDpetId="+memberDpetId;
		}
		baseUrl+="&keyword="+encodeURIComponent($.trim($('#memberKeyword').val()));
		window.location.href=baseUrl;
	}
	$('#searchMemberBtn').on('click',function(){
		search(1);
	});
	
	$('#option1').on('click',function(){
		search(1);
	});
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
	$('#memberKeyword').enter(function(){
		search(1);
	});
	</script>
</html>