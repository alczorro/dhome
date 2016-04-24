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
			<h3><fmt:message key="institution.paper.title"/></h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="training" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonTraining.jsp"><jsp:param name="activeItem" value="grants" /> </jsp:include>
				<div class="subNav">
					<ul id="orderCondition" class="nav nav-pills">
						<li data-order="1" class="active"><a href="javascript:;">待上报（${onHandCount }）</a></li>
						<li data-order="2"><a href="<dhome:url value='/people/${domain}/admin/grants/batch/1'/>">已上报（${handedCount }）</a></li>
						<li style="float:right; margin-top:5px;">
							<input type="button" id="createBtn" value=" +新增奖助学金 " class="btn btn-mini btn-success"/>
						</li>
					</ul>
				</div>
				<ul class="listShow grant">
					<c:if test="${page.maxPage>0 }">
						<li class="title">
							<span class="check"><input type="checkbox"  id="checkAll"/></span>
							<span class="name">姓名</span>
							<span class="num">课题号</span>
							<span class="class">班级</span>
							<span class="degree">学位</span>
							<span class="bonus">研究所奖学金<br><span class="normal">（研究所支付）</span></span> 
							<span class="bonus">研究所奖学金<br><span class="normal">（课题支付）</span></span>
							<span class="bonus">助研学金<br><span class="normal">（课题支付）</span></span> 
							<span class="total">总金额</span> 
							<span class="time">发放时间</span>
							<span class="name">操作</span>
						</li>
					</c:if>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="check"><input value="${data.id }" type="checkbox" class="checkItem"/></span>
							<span class="name">
								${data.studentName}
							</span> 
							<span class="num"> ${data.topicNo } </span>
							<span class="class"> ${data.className } </span>
							<span class="degree"> ${degreeMap[data.degree].val } </span>
							<span class="bonus"><fmt:formatNumber value="${data.scholarship1 }" pattern="0.00"/></span>
							<span class="bonus"> <fmt:formatNumber value="${data.scholarship2 }" pattern="0.00"/>  </span>
							<span class="bonus"> <fmt:formatNumber value="${data.assistantFee }" pattern="0.00"/>  </span>
							<span class="total"> <fmt:formatNumber value="${data.sumFee }" pattern="0.00"/> </span>
							<span class="time">
							<fmt:formatDate value="${data.startTime}" pattern="yyyy.MM.dd" />
								-
							<fmt:formatDate value="${data.endTime}" pattern="yyyy.MM.dd" />
							</span>
							
							<span>									
							   <a class="label label-danger deleteGrants" data-url="<dhome:url value="/people/${domain}/admin/grants/delete/${data.id }"/>">删除</a>
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
				
				<p class="p_add"><input type="button" id="btnHanded" class="btn btn-large btn-primary" value="上报" /> </p>
			</div>
			</div>
		</div>
	</div>
	<jsp:include page="../../commonfooter.jsp"></jsp:include>

</body>
<jsp:include page="../../commonheader.jsp"></jsp:include>
<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
<script type="text/javascript">
$(function(){
	checkAllBox('checkAll','checkItem');
	
	$("#createBtn").click(function(){
		window.location="<dhome:url value='/people/${domain}/admin/grants/create/'/>";
	});
	
	$('.deleteGrants').on('click',function(){
		if(confirm('删除后将不可恢复')){
			$.get($(this).data('url')).done(function(data){
				if(data.success){
					window.location.reload();
				}else{
					alert(data.desc);
				}
			});
		}
	});
	
	$("#btnHanded").click(function(){
		var items = $('.checkItem:checked');
		if(items.length==0){
			alert("请至少选择一项.");
			return;
		}
		
		var grantsId = new Array();
		items.each(function(i,n){
			grantsId.push($(n).val());
		});
		
		$.ajax({
		   type: "POST",
		   url: "<dhome:url value='/people/${domain}/admin/grants/updateStatus'/>",
		   data: {"grantsId":grantsId},
		   success: function(rep){
			   if(rep.success){
					window.location.href="<dhome:url value='/people/${domain}/admin/grants/batch/1' />";
			   }
		   }
		});
	});
	
	var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
	pageNav.setToPage(function(page){
		window.location.href=page;
	});
});
</script>
</html>