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
				    <h3 class="dark-green abs-top">
				    	<c:if test="${isMember}">
				    		<a class="btn btn-warning float-right" href="<dhome:url value='/institution/${home.domain }/scholarevent.html?func=add'/>"><fmt:message key="instituteActivity.event.add"/></a>
				    	</c:if>
				    	<fmt:message key="institute.common.scholarEvent"/>
				    	<a target="_blank" href='<dhome:url value="/institution/${home.domain }/rss_scholarevent.xml"/>' class="rss"></a>
				    </h3>
				    <ul class="nav nav-pills">
				      <li class="active"><a id="all"><fmt:message key="instituteActivity.sort.all"/></a></li>
		              <li><a id="upcoming"><fmt:message key="instituteActivity.sort.upcoming"/></a></li>
		              <li><a id="ongoing"><fmt:message key="instituteActivity.sort.ongoing"/></a></li>
		              <li><a id="expired"><fmt:message key="instituteActivity.sort.expired"/></a></li>
		            </ul>
				    <ul id="event-list" class="person-list activity-list"></ul>
				    <p id="notice" class="more-page clear"><fmt:message key="addSearchPaper.loading"/></p>
				    <p id="loadmore" class="more-page clear" style="display:none"><a><fmt:message key="instituteActivity.load.more"/></a></p>				</div>
				<div class="clear"></div>
			</div>
			
			<div class="span3">
		    	<div class="insti abs-top">
		    		<div class="insti-img">
		    			<a href="<dhome:url value='/institution/${domain}/index.html'/>"><img src="<dhome:img imgId="${home.logoId }" imgName="dhome-institute.png"/>" /></a>
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
	        <h3><fmt:message key="instituteActivity.delete.title"/></h3>
        </div>
        <form class="form-horizontal no-bmargin">
			<input type="hidden" name="durl" value="">
			<fieldset>
				<div class="modal-body">
					<span><fmt:message key="instituteActivity.delete.content"/></span>
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
<script type="text/javascript"	src="<dhome:url value="/resources/scripts/upload/fileuploader.js"/>"></script>
<script	src="<dhome:url value="/resources/scripts/jquery/jquery.imgareaselect.pack.js"/>"	type="text/javascript"></script>

<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/base.js" />"></script>
<script type="text/javascript">
$(document).ready(function() {
	var baseURL = "<dhome:url value='/institution/${domain}/scholarevent.html'/>";
	var context = "eventAll";
	var offset = 0;
	var size = 10;
	var autoLoadMore; //自动加载
	var isLoading=false;//当前时候有其他请求没有处理完
	
	var notice_handler = {
			loading: function() {
				$('#loadmore').hide();
				$('#notice').addClass('large').text("<fmt:message key='addSearchPaper.loading'/>").fadeIn();
			},
			noMatch: function() {
				$('#loadmore').hide();
				$('#notice').addClass('large').text("<fmt:message key='instituteActivity.load.noResult'/>").show();
			},
			noMore: function() {
				$('#loadmore').hide();
				setTimeout(function(){
					$('#notice').removeClass('large').text("<fmt:message key='instituteActivity.load.noMore'/>").show();
				}, 500);
			},
			readyToLoad: function() {
				$('#loadmore').show();
				$('#notice').hide();
			},
			error : function(){
				$('#loadmore').hide();
				$('#notice').removeClass('large').text("<fmt:message key='instituteActivity.load.fail'/>").fadeIn();
			}
	};
	
	/**      公用方法       **/
	function ajaxRequest(url, params, success, fail){
		
		//如果别的正在载入中，那么就别再加入了
		if(isLoading){
			return;
		}
		isLoading=true;
		
		$.ajax({
			url : url,
			type : 'POST',
			data : params,
			success : success,
			error : fail
		});
	}
	
	function renderData(data){
		if(data.length<=0){
			if(offset==0){
				notice_handler.noMatch();
			}else{
				notice_handler.noMore();
			}
		}else{
			$("#event-template").render(data).appendTo("#event-list");
			clearEditOption(data);
			if(data.length<size){
				notice_handler.noMore();
			}else{
				notice_handler.readyToLoad();
			}
			offset += data.length;
		}
		isLoading=false;
	}
	
	function clearEditOption(data){
		if(data.length >0){
			var size = data.length;
			for(var i=0; i<size; i++){
				if(!((data[i].creator == "${currentUser.id}" && "${isMember}"=="true") || "${currentUser.isAdmin}" == "true")){
					$("#edit-"+data[i].creator).remove();
				}
			}
		}
	}
	
	function resetContext(con, off){
		context = con;
		offset = off;
		$("#event-list").html("");
		$("#all").closest("ul").children().removeClass("active");
		if(context == "eventAll"){
			$("#all").parent().addClass("active");
		}else if(context == "eventUpcoming"){
			$("#upcoming").parent().addClass("active");
		}else if(context == "eventOngoing"){
			$("#ongoing").parent().addClass("active");
		}else{
			$("#expired").parent().addClass("active");
		}
	}
	
	function fail(){
		notice_handler.error();
		isLoading = false;
	}
	
	$("#loadmore").click(function(){
		var params = {func:context, offset:offset, size:size};
		notice_handler.loading();
		ajaxRequest(baseURL,params,renderData,fail);
	});
	
	$(window).scroll(function(){
		if ($(window).scrollTop() + $(window).height() > $('#loadmore').offset().top
			&& $('#loadmore').is(':visible')) {
			clearTimeout(autoLoadMore);
			autoLoadMore = setTimeout(function(){
				$('#loadmore:visible').click();
			}, 700);
		}
	});
	
	/******       tab事件     *************/
	
	$("#upcoming").click(function(){
		resetContext("eventUpcoming", 0);
		var params = {func:context, offset:offset, size:size};
		notice_handler.loading();
		ajaxRequest(baseURL, params, renderData, fail);
	});
	
	$("#ongoing").click(function(){
		resetContext("eventOngoing",0);
		var params = {func:context, offset:offset, size:size};
		notice_handler.loading();
		ajaxRequest(baseURL, params, renderData, fail);
	});
	
	$("#all").click(function(){
		resetContext("eventAll", 0);
		var params = {func:context, offset:offset, size:size};
		notice_handler.loading();
		ajaxRequest(baseURL, params, renderData, fail);
	});
	
	$("#expired").click(function(){
		resetContext("eventExpired", 0);
		var params = {func:context, offset:offset, size:size};
		notice_handler.loading();
		ajaxRequest(baseURL, params, renderData, fail);
	});
	
	$("a.deleteevent").live("click", function(){
		var durl = $(this).attr("data-durl");
		$("#remove-event-popup").find("input[type=hidden][name=durl]").val(durl);
		$("#remove-event-popup").modal("show");
	});
	
	$("#remove-popup-submit").click(function(event){
		var durl = $("#remove-event-popup").find("input[type=hidden][name=durl]").val();
		ajaxRequest(durl,{},deleteSuccess, fail);
		$("#remove-event-popup").modal("hide");
		event.preventDefault();
		event.stopPropagation();
	});
	
	function deleteSuccess(data){
		if(typeof(data.status) !="undefined" && data.status){
			$("li[id="+data.activityId+"]").remove();
		}
		isLoading = false;
	}
	
	/*********     初始化      **************/
	function init(){
		resetContext("eventAll", 0);
		var params = {func:context, offset:offset, size:size};
		notice_handler.loading();
		ajaxRequest(baseURL, params, renderData, fail);
	}
	
	init();
});
</script>
<script type="text/html" id="event-template">
<li id="{{= id}}">
	<div class="person-img"><a target="_blank" href="{{= eventurl}}"><img src="{{= imgurl}}"/></a></div>
	<div class="person-info">
		<div class="person-name"><a target="_blank" href="{{= eventurl}}"><strong>{{= title}}</strong></a></div>
		<div><span class="activity-title"><fmt:message key="instituteActivity.event.reporter"/></span>{{= reporter}}</div>
		<div><span class="activity-title"><fmt:message key="instituteActivity.event.time"/></span>{{= startTime}}<fmt:message key="instituteActivity.event.add.to"/>{{= endTime}}</div>
		<div><span class="activity-title"><fmt:message key="instituteActivity.event.place"/></span>{{= place}}</div>
		<div><span class="activity-title"><fmt:message key="instituteActivity.event.creator"/></span>{{= creatorName}}</div>
	</div>
	<div id="edit-{{= creator}}" class="float-right">
		<a href="{{= editurl}}" class="btn btn-small"><fmt:message key="common.edit"/></a>
		<a class="btn btn-small deleteevent" data-durl="{{= deleteurl}}"><fmt:message key="common.delete"/></a>
	</div>
	<div class="clear"></div>
</li>
</script>
<!-- 
<div><fmt:message key="instituteActivity.event.createTime"/>{{= createTime}}</div>
<div><fmt:message key="instituteActivity.event.introduction"/>{{= introduction}}</div>
 -->
</html>