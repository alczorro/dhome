<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
<title><fmt:message key="institute.common.paper"/>-${institution.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../commonheaderCss.jsp"></jsp:include>
	<link href="<dhome:url value='/resources/css/jquery.jqplot.min.css'/>" rel="stylesheet" type="text/css"/>
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<h1 class="dark-green"><a href="<dhome:url value='/institution/${domain }/index.html'/>">${institution.name }</a></h1>
	</div>
	<div class="container">
		<div class="row-fluid">
			<div class="span9">
				<div class="panel abs-top some-ipad min600">
				    <h3 class="dark-green abs-top"><fmt:message key="common.paper.title"/>
				    <a target="_blank" href='<dhome:url value="/institution/${institution.domain }/rss_publications.xml"/>'  class="rss"></a></h3>
				    <p id="paper-desc" class="dark-green total-count">
				    	<span><fmt:message key="institutePaper.statistic.paper"/>${amount }</span> 
				    	<span><fmt:message key="institutePaper.statistic.cite"/>${citedNum }</span> 
				    	<span>H-Index：${hindex } </span> 
				    	<span>G-Index：${gindex }</span>
				    </p>
				    <div id="chartdiv">
				    	
				    </div>
		          	<ul id="sortPaper" class="nav nav-pills">
			          	<li class="active">
			          		<a id="citeAll"><fmt:message key="institutePaper.sort.cite"/></a>
			          	</li>
						<li class="dropdown" id="menu1">
							<a id="yearAll" style="display:inline-block;"><fmt:message key="institutePaper.sort.year"/></a>
							<a class="dropdown-toggle" data-toggle="dropdown" href="#menu1"  style="display:inline-block; margin-left:-20px;"><b class="caret"></b>
							</a>	
							<ul class="dropdown-menu">
								<c:forEach items="${years}" var="year">
									<li data-year="${year}">
										<a class="year">
										<c:choose>
											<c:when test="${year == 0 }"><fmt:message key="institutePaper.sort.unknowYear"/></c:when>
											<c:otherwise>${year}<fmt:message key="institutePaper.sort.nian"/></c:otherwise>
										</c:choose>
										</a>
									</li>
								</c:forEach>
							</ul>
						</li>
						<li class="dropdown" id="menu2">
							<a id="authorAll" style="display:inline-block;"><fmt:message key="institutePaper.sort.author"/></b></a>
							<a class="dropdown-toggle"	data-toggle="dropdown" href="#menu2"  style="display:inline-block; margin-left:-20px;"><b class="caret"></b>
							</a>	
							<ul class="dropdown-menu">
								<c:forEach items="${authors}" var="author">
									<li data-id="${author.id }"><a class="author">${author.zhName}</a></li>
								</c:forEach>
							</ul>
						</li>
					</ul>
					<span id="currentSort"></span>
				    <ol class="paper-list paper-list-show"></ol>
				    <p id="notice" class="more-page clear"><fmt:message key="addSearchPaper.loading"/></p>
				    <p id="loadmore" class="more-page clear" style="display:none"><a><fmt:message key="addSearchPaper.morePaper"/></a></p>
				</div>
				<div class="clear"></div>
			</div>
			
			<div class="span3">
		    	<div class="insti abs-top">
		    		<div class="insti-img">
		    			<a href="<dhome:url value='/institution/${domain}/index.html'/>"><img src="<dhome:img imgId="${institution.logoId }" imgName="dhome-institute.png"/>" /></a>
		    		</div>	
		   			<p class="insti-info">${institution.introduction}</p>
		   			<c:if test="${isMember || currentUser.isAdmin}">
		   				<p><a class="show-all"  href='<dhome:url value="/institution/${domain }/edit/index.html"/>'><fmt:message key="institute.common.edit"/></a></p>
		   			</c:if>
		   		</div>
		   		<ul class="insti-nav">
		   			<li><a href="<dhome:url value='/institution/${domain}/index.html'/>"><fmt:message key="institute.common.home"/></a></li>
		   			<li class="active"><a href="<dhome:url value='/institution/${domain}/publications.html'/>"><fmt:message key="institute.common.paper"/></a></li>
		   			<li><a href="<dhome:url value='/institution/${domain}/members.html'/>"><fmt:message key="institute.common.members"/></a></li>
		   			<li><a href="<dhome:url value='/institution/${domain}/scholarevent.html'/>"><fmt:message key="institute.common.scholarEvent"/></a></li>
		   		</ul>
			</div>
		</div>
	</div>
	<jsp:include page="../commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="../commonheader.jsp"></jsp:include>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/base.js" />"></script>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/jquery/excanvas.min.js" />"></script>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/jquery/jquery.jqplot.min.js" />"></script>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/jquery/jqplot.highlighter.min.js" />"></script>


<script type="text/javascript">
$(document).ready(function() {
	$('.dropdown-toggle').dropdown();
	var baseURL = "<dhome:url value='/institution/${domain}/publications.html'/>";
	var context = "paperCite";
	var offset = 0;
	var size = 10;
	var curAuthor = 0; //所有用户
	var curYear = -1;//所有年份
	var autoLoadMore; //自动加载
	var chartData = JSON.parse('${stats}');
	var plot1;
	var isLoading=false;//当前时候有其他请求没有处理完

	var notice_handler = {
			loading: function() {
				$('#loadmore').hide();
				$('#notice').addClass('large').text("<fmt:message key='addSearchPaper.loading'/>").fadeIn();
			},
			noMatch: function() {
				$('#loadmore').hide();
				$('#notice').addClass('large').text("<fmt:message key='addSearchPaper.noResult'/>").show();
			},
			noMore: function() {
				$('#loadmore').hide();
				setTimeout(function(){
					$('#notice').removeClass('large').text("<fmt:message key='addSearchPaper.noMore'/>").show();
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
		if(data.length<0){
			if(offset==0){
				notice_handler.noMatch();
			}else{
				notice_handler.noMore();
			}
		}else{
			$("#paper-template").render(data).appendTo("ol.paper-list");
			if(data.length<size){
				notice_handler.noMore();
			}else{
				notice_handler.readyToLoad();
			}
			offset += data.length;
			formatHtml();
		}
		//请求完成
		isLoading=false;
	}
	
	function formatHtml(){
		$("ol.paper-list>li").each(function(index, element){
			formatTitle($(this));
			formatYearSource($(this));
			formatVolume($(this));
			formatPages($(this));
			formatSummary($(this));
			formatDownloadURL($(this));
		});
	}
	
	function formatTitle($li){
		var $title = $li.find("span.paper-title");
		var $a = $title.children("a");
		var url = $.trim($a.attr("href"));
		if(url == ""&&$a.html()!=null){
			$title.html($a.html());
		}
	}
	function formatYearSource($li){
		var $source = $li.find("span.paper-source");
		var source = $.trim($source.text());
		var year = $.trim($li.find("span.paper-time").text());
		if(year == ""){
			$source.nextAll().filter(":not(.download,.summary,.clear)").remove();
		}
		if(source == ""){
			$source.remove();
		}
	}
	
	function formatVolume($li){
		var $volume = $li.find("span.paper-volume");
		var content = $.trim($volume.html());
		if(content =="" || content == ","){
			$volume.remove();
		}
	}
	
	function formatPages($li){
		var $pages = $li.find("span.paper-pages");
		var content = $.trim($pages.html());
		if(content =="" || content == ","){
			$pages.remove();
		}
	}
	
	function formatSummary($li){
		var $summary = $li.find("span.summary,.summary-content");
		var $summaryDetail = $li.find("span.summary,.link");
		var content = $.trim($summary.html());
		if(content == ""){
			$summary.remove();
			$summaryDetail.remove();
		}
	}
	
	function formatDownloadURL($li){
		var $a = $li.find("span.download").children("a");
		if($a.attr("href")==""){
			$a.parent().remove();
		}
	}
	
	function resetContext(con, off, year, author){
		context = con;
		offset = off;
		curYear = year;
		curAuthor = author;
		$("ol.paper-list").html("");
		$("#sortPaper>li").removeClass("active");
		if(context == "paperCite"){
			$("#sortPaper>li:eq(0)").addClass("active");
		}else if(context == "paperYear"){
			$("#sortPaper>li:eq(1)").addClass("active");
		}else{
			$("#sortPaper>li:eq(2)").addClass("active");
		}
	}
	
	function fail(){
		notice_handler.error();
	}
	
	$("#loadmore").click(function(){
		var params = {func:context, year: curYear, uid: curAuthor, offset:offset, size:size};
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
	
	
	/***   分类查询事件    **/
	$("#citeAll").click(function(){
		init();
	});
	
	$("#yearAll").click(function(){
		resetContext("paperYear", 0, -1, 0);
		var params = {func:context, year:curYear, offset:offset, size:size};
		$("#currentSort").text("<fmt:message key='institutePaper.sort.currentText'/>"+$("#yearAll").text());
		ajaxRequest(baseURL, params, renderData, fail);
	});
	
	$("a.year").click(function(event){
		var year = $(this).parent().attr("data-year");
		resetContext("paperYear", 0, year, 0);
		var params = {func:context, year: curYear, offset:offset, size:size};
		if(year==0){
			$("#currentSort").text("<fmt:message key='institutePaper.sort.currentText'/>"+"<fmt:message key='institutePaper.sort.unknowYear'/>");
		}else{
			$("#currentSort").text("<fmt:message key='institutePaper.sort.currentText'/>"+year+"<fmt:message key='institutePaper.sort.nian'/>");
		}
		ajaxRequest(baseURL, params, renderData, fail);
		$("li.open").removeClass("open");
	});
	
	$("#authorAll").click(function(event){
		resetContext("paperAuthor", 0, -1, 0);
		var params = {func:context, uid: 0, offset:offset, size:size};
		$("#currentSort").text("<fmt:message key='institutePaper.sort.currentText'/>"+$("#authorAll").text());
		ajaxRequest(baseURL, params, renderData, fail);
	});
	
	$("a.author").click(function(event){
		var uid = $(this).parent().attr("data-id");
		resetContext("paperAuthor", 0, -1, uid);
		var params = {func:context, uid: uid, offset:offset, size:size};
		$("#currentSort").text("<fmt:message key='institutePaper.sort.currentText'/>"+$("li[data-id="+uid+"]").children("a.author").text());
		ajaxRequest(baseURL, params, renderData, fail);
		$("li.open").removeClass("open");
	});
	
	$("a.summary").live("click", function(event){
		var $next = $(this).parent().children(".summary-content");
		if($next.is(":hidden")){
			$next.slideDown();
		}else{
			$next.slideUp();
		}
		event.preventDefault();
	});
	
	/***      初始化        **/
	function init(){
		resetContext("paperCite", 0, -1, 0);
		var params = {func:context, offset:offset, size:size};
		$("#currentSort").text("<fmt:message key='institutePaper.sort.currentText'/>"+$("#citeAll").text());
		ajaxRequest(baseURL, params, renderData, fail);
		plot();
		if(parseInt("${amount}")<=0){
			$("#chartdiv").nextAll().remove();
			if("true" == "${isMember}"){
				$("#chartdiv").after("<a target='_blank' href='${addPaperURL}'><fmt:message key='institutePaper.nopaper.member'/></a>");
			}else{
				$("#chartdiv").after("<p><fmt:message key='institutePaper.nopaper.guest'/></p>");
			}
		}
	}

	function isEmpty(){
		return (chartData.cite == "" || chartData.num == "" ||
				chartData.totalCite == "" || chartData.totalNum =="");
	}
	
	function plot(){
		var cite = evalArray(chartData.cite);
		var num = evalArray(chartData.num);
		var totalCite = evalArray(chartData.totalCite);
		var totalNum = evalArray(chartData.totalNum);
		plot1 = $.jqplot ('chartdiv', [cite, num, totalCite, totalNum],{
			axes:{
				   xaxis:{
				   	  tickOptions:{
				         showGridline: true
				   	  }
			       },
			       yaxis:{
			    	  min:0,
			          tickOptions:{
				         showGridline: true
				      }
			       }
				},
				series:[ 
					       {
					          shadow: false,
					          label: "<fmt:message key='institutePaper.chart.cite'/>",
					        
					          rendererOptions: {
					              smooth: true,
					          }
					       },
					       {
						      shadow: false,
						      label: "<fmt:message key='institutePaper.chart.num'/>",
						   
						      rendererOptions: {
						          smooth: true,
						      }
						   },
						   {
							  shadow: false,
							  label: "<fmt:message key='institutePaper.chart.totalCite'/>",
							  
							  rendererOptions: {
							      smooth: true,
							  }
						   },
						   {
							  shadow: false,
							  label: "<fmt:message key='institutePaper.chart.totalNum'/>",
						
							  rendererOptions: {
								  smooth: true,
							  }
						   }
					    ],
			    legend: {
			       show: true,
			       location: 'ne',     // compass direction, nw, n, ne, e, se, s, sw, w.
			       xoffset: 25,        // pixel offset of the legend box from the x (or x2) axis.
			       yoffset: 25,        // pixel offset of the legend box from the y (or y2) axis.
			       placement:'outsideGrid'
			    },
				grid:{  
				   borderWidth: 0.5,
			       backgroundColor: "#e4eef6"  
			    },
				highlighter: {
			       show: true,
			       sizeAdjust: 7.5,
				   tooltipAxes: 'both',  
				   tooltipSeparator: 'year,'
			    }
			});
	}
	
	function evalArray(target){
		var array = target.split("]");
		var result = [];
		for(var i=0; i< array.length; i++){
			if(array[i]!=""){
				if(array[i].substring(0,1) == ","){
					array[i] = array[i].substring(1,array[i].length);
				}
				result.push(eval("("+array[i]+"])"));
			}
		}
		return (array.length ==0)?[""]:result;
	}
	init();

});
</script>
<script type="text/html" id="paper-template">
	<li id="{{= id}}">
		<span class="count icount"></span>
		<span>
			<span class="paper-title"><a target="_blank" href="{{= paperURL}}">{{= title}}</a></span>
			<span class="paper-author">{{= authors}}</span>	     
			<span class="paper-source">{{= source}}</span>
			<span class="split">:</span>
			<span class="paper-time">{{= publishedTime}}</span>
			<span class="paper-volume">,{{= volumeIssue}}</span>
			<span class="paper-pages">,{{= pages}}</span>
			<div class="clear"></div>
			<a class="summary link"><fmt:message key="common.summary"/>&nbsp;</a>
			<span class="bdownfull download"><a href="{{= downloadURL}}"><fmt:message key='addNewPaper.downloadFulltext'/></a></span>
			<span style="display:none" class="summary summary-content">{{= summary}}</span>	
        </span>
	</li>
</script>
</html>