<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
<title><fmt:message key="institute.common.members"/>-${home.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<h1 class="dark-green"><a href='<dhome:url value="/institution/${home.domain }/index.html"/>'>${home.name }</a></h1>
	</div>
	<div class="container">
		<div class="row-fluid">
			<div class="span9">
				<div class="panel abs-top some-ipad min600">
				    <h3 class="dark-green abs-top">
				    	<c:if test="${empty currentUser }">
					    	<a href="<dhome:url value='/system/regist?func=stepOne'/>" target="_blank" class="btn btn-warning float-right"><fmt:message key="instituteMember.createMyHomepage"/></a>
					    </c:if>
					    <fmt:message key="institute.common.members"/>
					    <a target="_blank" href='<dhome:url value="/institution/${home.domain }/rss_members.xml"/>' class="rss"></a>
				    </h3>
				    <ul id="memberList" class="person-list">
				    	
				    </ul>
				    <p id="notice" class="more-page clear"><fmt:message key="instituteMember.load.more"/></p>
				    <p id="loadmore" class="more-page clear" style="display:none"><a><fmt:message key="discover.findMore"/></a></p>
				</div>
			</div>
			
			<div class="span3">
		    	<div class="insti abs-top">
		    		<div class="insti-img">
			    		<a href="<dhome:url value='/institution/${home.domain}/index.html'/>"><img src='<dhome:img imgId="${home.logoId }"  imgName="dhome-institute.png"/>'/></a>
			    	</div>		
		   			<p class="insti-info">${home.introduction }</p>
		   			<c:if test="${isMember || currentUser.isAdmin}">
		   				<p><a class="show-all"  href='<dhome:url value="/institution/${home.domain }/edit/index.html"/>'><fmt:message key="institute.common.edit"/></a></p>
		   			</c:if>
		   		</div>
		   		<ul class="insti-nav">
		   			<li><a href='<dhome:url value="/institution/${home.domain }/index.html"/>'><fmt:message key="institute.common.home"/></a></li>
		   			<li><a href='<dhome:url value="/institution/${home.domain }/publications.html"/>'><fmt:message key="institute.common.paper"/></a></li>
		   			<li class="active" ><a><fmt:message key="institute.common.members"/></a></li>
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
<script type="text/javascript">
	$(document).ready(
			function() {
				/**load more start*/
				var usersURL = "<dhome:url value='/institution/${home.domain}/getMembers.json'/>";
				var keyword = "";
				var navStatus = "";
				var offset = 0;
				var size = 6;
				var autoLoadMore; //自动加载
				var checkInfoTime;//审核提示
				var curTopLiPos = 0;//当前处于顶部的li的位置
				var isLoading=false;//当前是否有请求没有相应
				
				var notice_handler = {
						noResult:function(){
							$('#loadmore').hide();
							$('#notice').removeClass('large').text("<fmt:message key='instituteMember.load.noResult'/>").show();
						},
						loading: function() {
							$('#loadmore').hide();
							$('#notice').addClass('large').text("<fmt:message key='discover.loading'/>").fadeIn();
						},
						noMatch: function() {
							$('#loadmore').hide();
							$('#notice').addClass('large').text("<fmt:message key='instituteMember.load.noMatching'/>").show();
						},
						noMore: function() {
							$('#loadmore').hide();
							setTimeout(function(){
								$('#notice').removeClass('large').text("<fmt:message key='instituteMember.load.noMore'/>").show();
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
				init();
				
				function init(){
					$("input[type=radio][name=check][value=success]").attr("checked", true);
					var params = {status: navStatus, keyword: keyword, offset:offset, size:size};
					notice_handler.loading();
					ajaxRequest(usersURL,params,renderData,fail);
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
				
				function renderData(data){
					if(typeof(data)=='undefined' ||data==''|| data.length<0){
						if(offset==0){
							notice_handler.noResult();
						}else{
							notice_handler.noMore();
						}
					}else{
						var imgBase='<dhome:img imgId="999"/>';
						var imgDefaultBase='<dhome:img imgId=""/>';
						$(data).each(function (i,n){
							if(n.image==0){
								n.imgUrl=imgDefaultBase;
							}else{
								n.imgUrl=imgBase.replace('999',n.image);
							}
							var interestHtml="";
							if(n.interest==null||n.interest==''){
								n.interest='<fmt:message key="common.none"/>'
							}else{
								$(n.interest).each(function (i,n){
									var keyDecode=decodeURIComponent(decodeURIComponent(n.keyword).replace(/\+/g,'%20'));
									if(i!=0){
										interestHtml+="&nbsp;,&nbsp;";
									}
									interestHtml+='<a target="_blank" href="<dhome:url value="/system/discover?type=interest&keyword='+n.keyword+'"/>">'+keyDecode+'</a>'
								});
								n.interest=interestHtml;
							}
						})
						$("#member-template").render(data).appendTo("#memberList");
						if(data.length<size){
							notice_handler.noMore();
						}else{
							notice_handler.readyToLoad();
						}
						offset += data.length;
					}
					isLoading=false;
				}
				
				function fail(){
					notice_handler.error();
				}
				
				$("#loadmore").click(function(){
					var params = {status: navStatus, keyword: keyword, offset:offset, size:size};
					notice_handler.loading();
					ajaxRequest(usersURL,params,renderData,fail);
					
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
				
				function setContext(sta, key, off){
					keyword = key;
					navStatus = sta;
					offset = off;
					$("ul.homepageList li:not(.th)").remove();
				}
				/**load more end*/
				var pageHeight = $("html").height();
				var documentHeight = window.screen.height;
				var maxHeight = pageHeight > documentHeight ? pageHeight
						: documentHeight;
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
			});
	
</script>
<script id="member-template" type="text/html">
<li>
	<div class="person-img"><a target="_blank" href="<dhome:url value='/people/{{= domain}}'/>"><img src="{{= imgUrl}}"/></a></div>
		<div class="person-info">
		<div class="person-name"><a target="_blank" href="<dhome:url value='/people/{{= domain}}'/>"><strong>{{= zhName}}</strong></a></div>
		<div>{{= salutation}} </div>
		<div><fmt:message key="instituteMember.researchInterests"/>{{= interest}}</div>
	</div>
	<div class="float-right"><a class="btn btn-small" target="_blank" href="<dhome:url value='/people/{{= domain}}'/>"><fmt:message key="instituteMember.homepage"/></a></div>
	<div class="clear"></div>
</li>
</script>
</html>