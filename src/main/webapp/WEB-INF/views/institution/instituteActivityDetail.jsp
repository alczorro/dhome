<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<dhome:InitLanuage useBrowserLanguage="true"/>
<head>
<title>${event.title}-${institution.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<h1 class="dark-green"><a href='<dhome:url value="/institution/${domain }/index.html"/>'>${institution.name }</a></h1>
	</div>
	<div class="container">
		<div class="row-fluid">
			<div class="span9">
				<div class="panel abs-top some-ipad min600">
				    <h3 class="dark-green abs-top"><fmt:message key="institute.common.scholarEvent"/>
				    <c:if test="${event.creator == currentUser.id}">
					    <div class="float-right">
							<a href="<dhome:url value='/institution/${domain}/scholarevent.html?func=edit&activityId=${event.id}'/>"
								 class="btn btn-small font-normal"><fmt:message key="common.edit"/></a>
							<a data-durl="<dhome:url value='/institution/${domain}/scholarevent.html?func=delete&activityId=${event.id}'/>" 
								class="btn btn-small font-normal deleteevent"><fmt:message key="common.delete"/></a>
						</div>
					</c:if>
					</h3>
				    <div class="activity-img abs-top"><img src='<dhome:img imgId="${event.logoId}" imgName="dhome-activity.png"/>'/></div>
		    		<div class="activity-info abs-top">
		    			<div class="dark-green activity-name">${event.title }</div>
		    			<div><span class="activity-title"><fmt:message key="instituteActivity.event.reporter"/></span>${event.reporter }</div>
		    			<div><span class="activity-title"><fmt:message key="instituteActivity.event.time"/></span><fmt:formatDate value="${event.startTime }" pattern="yyyy-MM-dd HH:mm"/> <fmt:message key="instituteActivity.event.add.to"/> <fmt:formatDate value="${event.endTime }" pattern="yyyy-MM-dd HH:mm"/></div>
		    			<div><span class="activity-title"><fmt:message key="instituteActivity.event.place"/></span>${event.place }</div>
		    			<div><span class="activity-title"><fmt:message key="instituteActivity.event.creator"/></span>${creatorName }</div>
		    		</div>
		    		<div class="clear"></div>
		    		<div class="activity-detail abs-top">
		    			<p><span class="activity-title"><fmt:message key="instituteActivityDetail.eventDetail"/></span></p>
		    			<c:if test ="${event.introduction ==null ||event.introduction =='' ||event.introduction =='null' }">
		    			  <fmt:message key="instituteActivityDetail.noIntro"/>
		    			</c:if>
		    			${event.introduction }
		    		</div>
				</div>
			</div>
			
			<div class="span3">
		    	<div class="insti abs-top">
		    		<div class="insti-img">
		    			<img src="<dhome:img imgId="${institution.logoId }" imgName="dhome-institute.png"/>" />	
		    		</div>	
		   			<p class="insti-info">${institution.introduction}</p>
		   			<c:if test="${isMember || currentUser.isAdmin}">
		   				<p><a class="show-all"  href='<dhome:url value="/institution/${domain }/edit/index.html"/>'><fmt:message key="institute.common.edit"/></a></p>
		   			</c:if>
		   		</div>
		   		<ul class="insti-nav">
		   			<li><a href="<dhome:url value='/institution/${domain}/index.html'/>"><fmt:message key="institute.common.home"/></a></li>
		   			<li><a href="<dhome:url value='/institution/${domain}/publications.html'/>"><fmt:message key="institute.common.paper"/></a></li>
		   			<li><a href="<dhome:url value='/institution/${domain}/members.html'/>"><fmt:message key="institute.common.members"/></a></li>
		   			<li class="active"><a href="<dhome:url value='/institution/${domain}/scholarevent.html'/>"><fmt:message key="institute.common.scholarEvent"/></a></li>
		   		</ul>
			</div>
		</div>
	</div>
	<div id="remove-event-popup" tabindex="-1" class="modal hide fade">
		<div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">×</button>
	        <h3>删除学术活动</h3>
        </div>
        <form class="form-horizontal no-bmargin">
			<input type="hidden" name="durl" value="">
			<fieldset>
				<div class="modal-body">
					<span>您确认要删除该学术活动吗？</span>
					<ul class="x-list work no-border"></ul>
				</div>
	        	<div class="modal-footer">
					<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
					<button id="remove-popup-submit" class="btn btn-primary"><fmt:message key='common.confirm.delete'/></button>
		        </div>
	        </fieldset>
        </form>
	</div>
	<jsp:include page="../commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="../commonheader.jsp"></jsp:include>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/base.js" />"></script>
<script type="text/javascript">
$(document).ready(function() {
	var pageHeight = $("html").height();
	var documentHeight = window.screen.height;
	var maxHeight = pageHeight > documentHeight ? pageHeight: documentHeight;
	$(".cover-wholepage").css({
		"height" : maxHeight
	});
	$(".cover-wholepage").click(function() {
		$(this).hide();
	});
				
	//auditStatus
	$('#auditStatus').hover(function(){
		$(this).tooltip('toggle');
	});
	
	function ajaxRequest(url, params, success, fail){
		$.ajax({
			url : url,
			type : 'POST',
			data : params,
			success : success,
			error : fail
		});
	}
	
	$("a.deleteevent").live("click", function(){
		var durl = $(this).attr("data-durl");
		$("#remove-event-popup").find("input[type=hidden][name=durl]").val(durl);
		$("#remove-event-popup").modal("show");
	});
	
	$("#remove-popup-submit").click(function(){
		var durl = $("#remove-event-popup").find("input[type=hidden][name=durl]").val();
		ajaxRequest(durl,{},deleteSuccess, fail);
		$("#remove-event-popup").modal("hide");
	});
	
	function deleteSuccess(data){
		if(typeof(data.status) !="undefined" && data.status){
			window.location.href= "<dhome:url value='/institution/${domain}/scholarevent.html'/>";
		}
	}
	
	function fail(){
		alert("fail!");
	}
});
</script>
</html>