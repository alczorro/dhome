<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<dhome:InitLanuage useBrowserLanguage="true"/>
<head>
<title><fmt:message key="discover.title"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>
<body class="dHome-body" style="overflow-y: scroll;" data-offset="50" data-target=".subnav"
	data-spy="scroll">
   <jsp:include page="commonBanner.jsp"></jsp:include>
   <div class="container">
   		<ul class="nav nav-tabs discoverNav">
            <li class="active"><a data-toggle="tab"><fmt:message key="discover.scholars"/></a></li>
            <li><a href="<dhome:url value="/system/discover/institute"/>"><fmt:message key="discover.institution"/></a></li>
        </ul>
        <ul id="navbar" class="nav nav-pills scholar d-nobottom">
          	<li class="active">
          		<a id="all"><fmt:message key="discover.all"/></a>
          	</li>
		    <li>
				<a id="latest"><fmt:message key="discover.new"/></a>
			</li>
			<li class="dropdown" id="menu1">
				<a class="dropdown-toggle"	data-toggle="dropdown" href="#menu1" style="display:inline-block; float:left;"><fmt:message key="discover.subject"/><b class="caret"></b>
				</a>
				<ul id="discip-breadcrumb" class="breadcrumb" style="float:left;"><fmt:message key="discover.subject.all"/></ul>
				<ul id="menu11" class="dropdown-menu">
					<c:forEach items="${discipline.sons }" var="item">
						<li class="dropdown-submenu">
							<ul class="dropdown-menu">
							<c:forEach items="${item.sons }" var="secondItem">
								<li><a href="javascript:selectDiscipline('${item.value }','${secondItem.value }')">${secondItem.name }</a></li>
							</c:forEach>
							</ul>
						<a href="javascript:selectDiscipline('${item.value }','0')">${item.name }</a>
						</li>
					</c:forEach>
				</ul>
			</li>
			<li class="dropdown" id="menu2">
				<a class="dropdown-toggle"	data-toggle="dropdown" href="#menu2" style="display:inline-block; float:left;"><fmt:message key="discover.interest"/><b class="caret"></b>
				</a>
				<ul id="interest-breadcrumb" class="breadcrumb" style="float:left;"><fmt:message key="discover.interest.all"/></ul>
				<ul id="menu11" class="dropdown-menu intrest">
				<c:forEach items="${ interest}" var="item">
					<li><a href="javascript:searchByInterest('<dhome:encode value="${item.keyWord}"/>')">${item.keyWord}(${item.count })</a></li>
				</c:forEach>
				</ul>
			</li>
		</ul>
		<ul class="discover scholar-list"></ul>
		<p id="notice" class="more-page clear"><fmt:message key="discover.loading"/></p>
		<p id="loadmore" class="more-page clear" style="display:none"><a><fmt:message key="discover.findMore"/></a></p>
	</div>
	<jsp:include page="commonheader.jsp"></jsp:include>
	<jsp:include page="commonfooter.jsp"></jsp:include>

</body>

<script>
$(document).ready(function(){
	$("ul.nav.nav-pills > li > a").click(function(){
		$("#navbar .active").removeClass("active");
		$(this).parent().addClass("active");
	});
});
</script>

<script>
	var baseURL = "<dhome:url value='/system/discover'/>";
	$('.dropdown-toggle').dropdown();
	var offset = 0; //当前偏移量
	var size = 15; //每次取的数据量
	var context = "all"; //上下文环境，包含latest(最新创建),search(搜索),all(所有)
	var curKeyword = ""; //搜索关键词
	var autoLoadMore; //自动加载
	
	var firstDescipline="";//一级学科
	var secondDescipline="";//二级学科
	
	var isLoading=false;
	
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
	
	function init(){
		var initData = JSON.parse('${initData}');
		context = initData.context;
		reset();
		if(context == 'search'){
			keyword = initData.keyword;
			$("input[name=keyword]").val(keyword);
			$("#discip-breadcrumb").html("<fmt:message key='discover.subject.all'/>");
			$("#interest-breadcrumb").html("<fmt:message key='discover.interest.all'/>");
			var params = {keyword: $.trim(keyword), offset: offset, size: size};
			ajaxRequest(baseURL+"/search.json", params, renderDataSearch, fail);
			context = "search";
		}else if(context=='interest'){
			keyword=initData.keyword;
			$("#navbar .active").removeClass("active");
			$('#menu2').addClass("active");
			searchByInterest(keyword);
		}else{
			reset();
			$("#discip-breadcrumb").html("<fmt:message key='discover.subject.all'/>");
			$("#interest-breadcrumb").html("<fmt:message key='discover.interest.all'/>");
			var params = {offset: offset, size: size};
			ajaxRequest(baseURL+"/all.json", params, renderDataAll, fail);
		}
	}
	
	function renderDataAll(data){
		renderData(data);
		context = "all";
	}
	
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
	
	function fail(){
		notice_handler.error();
	}
	
	$("a#all").click(function(){
		reset();
		$("input[name=keyword]").val("");
		$("#discip-breadcrumb").html("<fmt:message key='discover.subject.all'/>");
		$("#interest-breadcrumb").html("<fmt:message key='discover.interest.all'/>");
		var params = {offset : offset, size : size};
		ajaxRequest(baseURL+"/all.json", params, renderDataAll, fail);
	});
	
	$("a#latest").click(function(){
		reset();
		$("input[name=keyword]").val("");
		$("#discip-breadcrumb").html("<fmt:message key='discover.subject.all'/>");
		$("#interest-breadcrumb").html("<fmt:message key='discover.interest.all'/>");
		var params = {offset : offset, size : size};
		ajaxRequest(baseURL+"/latest.json", params, renderDataLatestUser, fail);
	});
	
	$("input[name=keyword]").focus(function(){
		var text = $(this).val().trim();
		if(text == "" || text == "<fmt:message key='discover.search'/>"){
			$(this).val("");
		}
	}).blur(function(){
		if($(this).val().trim() == ""){
			$(this).val("<fmt:message key='discover.search'/>");
		}
	}).keyup(function(event){
		if(event.which == 13){
			var keyword = $.trim($(this).val());
			if(keyword == ""){
				$(this).focus();
				return false;
			}
			curKeyword = keyword;
			reset();
			$("#discip-breadcrumb").html("<fmt:message key='discover.subject.all'/>");
			$("#interest-breadcrumb").html("<fmt:message key='discover.interest.all'/>");
			$(".active").removeClass("active");
			params = {keyword: $.trim(keyword), offset:offset, size:size};
			ajaxRequest(baseURL+"/search.json",params,renderDataSearch,fail);
		}
	});
	
	$("#loadmore").click(function(){
		var callback = getCallBackMethod();
		var params = {offset:offset, size:size};
		if(context == 'search'){
			params = {keyword: $.trim(curKeyword), offset:offset, size:size};
		}else if(context=='discipline'){
			params ={first:firstDescipline,second:secondDescipline,offset:offset,size:size};
		}
		ajaxRequest(baseURL+"/"+context+".json", params, callback, fail);
	});
	
	function renderDataLatestUser(data){
		renderData(data);
		if(offset >=60){
			$("#loadmore").hide();
		}
		context = "latest";
	}
	
	function getCallBackMethod(){
		var callback = '';
		if(context == 'latest'){
			callback = renderDataLatestUser;
		}else if(context == 'search'){
			callback = renderDataSearch;
		}else if(context == 'all'){
			callback = renderData;
		}else if(context == 'discipline'){
			callback = renderDataDisciplineLoadMore;
		}else if(context=='interest'){
			callback=renderDataInterestLoadMore;
		}else{
			//console.log("<fmt:message key='discover.error.method'/>");
			callback = null;
		}
		return callback;
	}
	
	function renderDataSearch(data){
		renderData(data);
		context = "search";
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


function selectDiscipline(first,second){
	$("input[name=keyword]").val("");
	$('#menu1').toggleClass("open");
	firstDescipline=first;
	secondDescipline=second;
	discipiline_breadcrumb(first, second);
	$('#menu1').toggleClass("open",false);
	ajaxRequest(baseURL+"/discipline.json",{first:first,second:second,offset:0,size:size},renderDataDiscipline,fail);
	
}
function searchByInterest(keyword){
	//close the menu
	$('#menu2').removeClass("open");
	//do sth
	$("input[name=keyword]").val("");
	//bread
	$("#discip-breadcrumb").html("<fmt:message key='discover.subject.all'/>");
	keyword=keyword.replace(/\+/g,'%20');
	$("#interest-breadcrumb").html(decodeURIComponent(keyword));
	ajaxRequest(baseURL+"/interest.json",{'keyword':$.trim(keyword),'offset':0,'size':size},renderDataInterest,fail);
	
}
function breadcrumb(){
	
}

function discipiline_breadcrumb(first, second){
	var $firstTarget = $("ul.dropdown-menu li a[href=\"javascript:selectDiscipline('"+first+"','0')\"]");
	var html="";
	if(second != 0){
		var href = "javascript:selectDiscipline('"+first+"','0')";
		var $secondTarget = $("ul.dropdown-menu li a[href=\"javascript:selectDiscipline('"+first+"','"+second+"')\"]");
		html = "<li><a href=\""+href+"\">"+$firstTarget.text()+"</a> <span class='divider'>/</span></li>" +
				   "<li class='active'>"+$secondTarget.text()+"</li>";
	}else{
		 html = "<li class='active'>"+$firstTarget.text()+"</li>";
	}
	$("#discip-breadcrumb").html("").append(html);
	$("#interest-breadcrumb").html("<fmt:message key='discover.interest.all'/>");
}

function renderDataDiscipline(data){
	reset();
	renderData(data);
	context = "discipline";
}
function renderDataInterest(data){
	reset();
	renderData(data);
	context="interest";
}
function renderDataDisciplineLoadMore(data){
	renderData(data);
	context = "discipline";
}
function renderDataInterestLoadMore(data){
	renderData(data);
	context = "interest";
}

function renderData(data){
	var curLen = 0;
	if(typeof(data.users)!='undefined' && data.users.length>0){
		curLen = data.users.length;
		$("#user-template").render(data.users).appendTo("ul.discover");
		$.each(data.users,function(index, item) {		
			$('#'+item.uid).fadeIn(1000);
		});
		
	}
	isLoading=false;
	offset += curLen;
	if(offset ==0){
		notice_handler.noMatch();
	}else if(offset >= 1000){//大于1000条记录时不显示余下内容
		$("#loadmore").hide();
	}else if(curLen < size){
		notice_handler.noMore();
	}else{
		notice_handler.readyToLoad();
	}
}
//加载不同结果时需要使用的重置函数：清空当前内容，重置offset和loadmore
	function reset(){
		offset = 0;
		notice_handler.loading();
		$("ul.discover").html("");
	}

	//页面加载完毕后，初始化数据
	init();

</script>
<script type="text/html" id="user-template">
	<li>
		<a target="_blank" href="{{= userPageURL}}">
		<div class="discover-img">
            <img id="{{= uid}}" class="header-img" style="display:none;" src="{{= imgURL}}"/>
		</div>
		</a>
		<p><a target="_blank" href="{{= userPageURL}}">{{= zhName}}</a></p>
	</li>
</script>
</html>