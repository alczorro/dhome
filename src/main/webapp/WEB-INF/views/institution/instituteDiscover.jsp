<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
<title><fmt:message key="discover.title"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="../commonheaderCss.jsp"></jsp:include>
</head>
<body style="overflow-y: scroll;" class="dHome-body" data-offset="50" data-target=".subnav"
	data-spy="scroll">
   <jsp:include page="../commonBanner.jsp"></jsp:include>
	<div class="container" style="min-width:1000px;">
		<ul class="nav nav-tabs discoverNav">
            <li><a href="<dhome:url value="/system/discover"/>"><fmt:message key='institute.discover.member'/></a></li>
            <li class="active"><a  data-toggle="tab"><fmt:message key='institute.discover.institute'/></a></li>
        </ul>
        <ul class="nav nav-pills some-lrpad d-nobottom">
        	<li class="some-ssspad"><fmt:message key="institute.discover.oderBy"/></li>
        	<li class="active">
				<a id="memberCount"><fmt:message key='institute.discover.memberCount'/></a>
			</li>
			<li>
				<a id="paperCount"><fmt:message key='institute.discover.paparCount'/></a>
			</li>
        	<li >
        		<a id="lastest"><fmt:message key='institute.discover.new'/></a>
        	</li>

		
		</ul>
		<ul id="instituteList" class="discover">
		</ul> 
		<p id="notice" class="more-page clear"><fmt:message key="discover.loading"/></p>
		<p id="loadmore" class="more-page clear" style="display:none"><a><fmt:message key="institute.discover.findMore"/></a></p>
	</div>
	<jsp:include page="../commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="../commonheader.jsp"></jsp:include>
<script>
$(document).ready(function(){
	
	/**load more start*/
	var usersURL = "<dhome:url value='/system/discover/institute/getInstitute.json'/>";
	var key = "memberCount";
	var offset = 0;
	var size = 9;
	var autoLoadMore; //自动加载
	var checkInfoTime;//审核提示
	var curTopLiPos = 0;//当前处于顶部的li的位置
	var isLoading=false;//当前是否存在一个请求没有相应完成
	$("ul.nav.nav-pills > li > a").click(function(){
		//onclick
		$("ul.nav.nav-pills > li").removeClass("active");
		$(this).parent().addClass("active");
		key=$(this).attr("id");
		offset=0;
		$("#instituteList").html("");
		loadMore();
	});
	var notice_handler = {
			loading: function() {
				$('#loadmore').hide();
				$('#notice').addClass('large').text("<fmt:message key='discover.loading'/>").fadeIn();
			},
			noMatch: function() {
				$('#loadmore').hide();
				$('#notice').addClass('large').text("<fmt:message key='discover.noMatching'/>").show();
			},
			noMore: function() {
				$('#loadmore').hide();
				setTimeout(function(){
					$('#notice').removeClass('large').text("<fmt:message key='discover.noMore'/>").show();
				}, 500);
			},
			readyToLoad: function() {
				$('#loadmore').show();
				$('#notice').hide();
			},
			error : function(){
				$('#loadmore').hide();
				$('#notice').removeClass('large').text("<fmt:message key='common.error.requestFailure'/>").fadeIn();
			}
	};
	
	function loadMore(){
		var params = {keyWord: $.trim(key), offset:offset, size:size};
		notice_handler.loading();
		ajaxRequest(usersURL,params,renderData,fail);
		
	};
	loadMore();
	
	function ajaxRequest(url, params){
		//如果别的正在载入中，那么就别再加入了
		if(isLoading){
			return;
		}
		isLoading=true;
		$.ajax({
			url : url,
			type : 'POST',
			data : params,
			success : renderData,
			error : function(){
				notice_handler.error();
			}
		});
	}
	
	function renderData(data){
		if(data.size==0){
			if(offset==0){
				notice_handler.noMatch();
			}else{
				notice_handler.noMore();
			}
		}else{
			$(data.data).each(function (n,i){
				var logoImg='<dhome:img imgId="999"/>';
				var defaultImg='<dhome:img imgId="0" imgName="dhome-institute.png" />';
				if(i.logoId=='0'){
					i.imgUrl=defaultImg;
				}else{
					i.imgUrl=logoImg.replace('999',i.logoId);
				}
				if(i.introduction==null||i.introduction==''||i.introduction.replace(/ /g,'')==''){
					i.introduction='<fmt:message key="institute.discover.noneIntroduction"/>'
				}
			});
			$("#institution-template").render(data.data).appendTo("#instituteList");
			if(data.size<size){
				notice_handler.noMore();
			}else{
				notice_handler.readyToLoad();
			}
			offset += data.size;
		}
		//请求完成
		isLoading=false;
	}
	$("#loadmore").click(function(){
		loadMore();
	});
	
	function fail(){
		notice_handler.error();
	}
	
	$(window).scroll(function(){
		if ($(window).scrollTop() + $(window).height() > $('#loadmore').offset().top
			&& $('#loadmore').is(':visible')) {
			clearTimeout(autoLoadMore);
			autoLoadMore = setTimeout(function(){
				$('#loadmore:visible').click();
			}, 700);
		}
	});
	/**load more end*/
	
});
</script>
<script id="institution-template" type="text/html">
<li class="ins">
	<div class="ins-img">
		<a href="<dhome:url value="/institution/{{= domain}}/index.html"/>" target="_blank">
			<img src="{{= imgUrl}}"/>
		</a>
	</div>
	<div class="ins-info">
		<p><a target="_blank" href="<dhome:url value="/institution/{{= domain}}/index.html"/>" class="ins-title" title="{{= name}}">{{= name}}</a></p>
		<p title="{{= introduction}}">{{= introduction}}</p>
		<p><fmt:message key='institute.discover.member2'/><span class="count"><a target="_blank" href="<dhome:url value="/institution/{{= domain}}/members.html"/>">{{= memberCount}}</a></span>
	   		<fmt:message key='institute.discover.paper'/><span class="count"><a target="_blank" href="<dhome:url value="/institution/{{= domain}}/publications.html"/>">{{= paperCount}}</a></span> 
	   		<fmt:message key='institute.discover.activity'/><span class="count"><a target="_blank" href="<dhome:url value="/institution/{{= domain}}/scholarevent.html"/>">{{= activityCount}}</a></span>
	</p>
	</div>
</li>
</script>
</html>
