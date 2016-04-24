<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html>
<html lang="en">
  <head>
	<title><fmt:message key="common.index.title.anotherName"/>-<fmt:message key="common.index.title.escience"/></title>
	<meta name="description" content="<fmt:message key="index.help"/>" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
	<jsp:include page="commonheader.jsp"></jsp:include>
</head>

<body style='display:none' class="dHome-body indexPage" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container index-content">
		<div class="row-fluid">
			<div class="index-top-short" >
				<h1><fmt:message key="common.index.title.anotherName"/></h1>
				<p><b><fmt:message key="index.subtitle"/></b></p>
			</div>
			<div class="index-top-long">
				<form id="loginForm" action="<dhome:url value='/system/login'/>" method="POST">
					<div class="index-login small-font" id="logined">
	             		<fmt:message key="index.form.username"/><input name="email" class="i-small" id="inputIcon" type="text" placeholder="<fmt:message key="index.placeholder.email"/>">
	            		<fmt:message key="index.form.password"/><input name="password" class="i-small" id="inputIcon" type="password" placeholder="<fmt:message key="index.placeholderpassword"/>">
	           			<button type="submit" class="btn"><fmt:message key="common.login"/></button>
	           			<a href="<dhome:url value='/system/login?func=forgetPsw'/>" target="_blank"><fmt:message key="login.forgetPassword"/></a>
	           		</div>
			    </form>
			</div>
		</div>
		<div class="row-fluid indexbg">
			<h2><fmt:message key="index.ads"/></h2>
			<div id="register_div" class="index-register">
				<form id="registForm" method="post" class="registForm small-font" action="<dhome:url value="/system/regist"/>?func=stepOne">
					<h4><fmt:message key="index.createHome"/></h4>
		            	<div>
		             		<div class="login-left"><fmt:message key="index.form.zhName"/></div>
		             		<div class="login-right"><input id="zhName" name="zhName" class="d-small" id="zhName" type="text" placeholder="<fmt:message key="createIndex.placeholder.zhName"/>"><span></span></div>
		           		</div>
		            	<div class="x-top">
		             		<div class="login-left"><fmt:message key="index.form.email"/></div>
		             		<div class="login-right"><input id="emailToRegist" class="d-small" name="emailToRegist" type="text" placeholder="<fmt:message key="createIndex.placeholder.email"/>"><span></span><span id="umtMessage"></span></div>
		           		</div>
		           		<div class="x-top">
		           			<div class="login-left">&nbsp;</div>
		           			<div class="login-right"><button type="submit" class="btn btn-warning" style="width:162px;"><fmt:message key="index.form.beginCreate"/></button></div>
			            </div>
			            <div class="clear"></div>
			      </form>
			</div>
		</div>
		<div class="row-fluid">
			<div class="index-bot-long small-font">
				<p><fmt:message key="index.help"/>
				</p>
				<!-- <span>官方微博：<a href="http://weibo.com/dcloud" target="_blank" class="weibo-sina"></a></span> -->
			</div>
			<div class="index-bot-short">
				<div class="small-font"><a href="<dhome:url value='/system/discover'/>" target="_blank"><span id="homeCount"></span></a>&nbsp;<span id="loading"><fmt:message key='common.loading'/></span></div>
				<form class="navbar-form" method="get" action="<dhome:url value="/system/discover/search"/>" id="searchForm">
                  <input type="text" name="keyword" class="d-small" placeholder="<fmt:message key="index.searchPage"/>">
                  <button type="submit" class="btn"><fmt:message key="common.search"/></button>
                </form>
			</div>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
</body>
<script>
$(document).ready(function(){
	function deleteUmtMessage(){
		$('#umtMessage').html("");
	}
	$('#emailToRegist').focus(function(){
		deleteUmtMessage();
	})
	$('#zhName').focus(function(){
		deleteUmtMessage()
	})
	 $("#registForm").validate({
		 invalidHandler:function(form,validator){
				$('#umtMessage').html("");
		 },
		 submitHandler:function(form){
			 $.ajax({
				 url : "<dhome:url value='/system/index/isUmtUser'/>",
				 type : "POST",
				 data :{email:$("#emailToRegist").val()},
				 success : function(data){
					if(data){
						var prefix='<fmt:message key="index.umt.info.prefix"><fmt:param value="<sup>[<a target=\"_blank\" href=\"http://passport.escience.cn/help.jsp\">?</a>]</sup>"></fmt:param></fmt:message>';
						var suffix='<fmt:message key="index.umt.info.suffix"/>';
						$('#umtMessage').html(prefix+"<a href='<dhome:url value='/system/login'/>?umt=true&email="+$('#emailToRegist').val()+"'>"+suffix+"</a>")
					}else{
						form.submit();
					}
				 }
			 });
	        },
	  rules: {
	  	zhName: {required:true},
	  	emailToRegist: {
	   		 required: true,
	   		 email: true,
	   		 remote:{
	   			type: "GET",
				url: '<dhome:url value="/system/index/isDhomeUser"/>',
				data:{ 
					"email":function(){
							return $("#emailToRegist").attr("value");
						}	   		
	  			}
	   		 }
	   	  }
	  },
	   messages: {
		   zhName: {
			   required:"<fmt:message key='createIndex.check.required.zhName'/>"},
		   emailToRegist: {
		  	   required: "<fmt:message key='createIndex.check.required.email'/>",
		       email: "<fmt:message key='createIndex.check.email'/>",
		       remote:"<fmt:message key='createIndex.check.emailInUse'/>"
		   }
		 },
		 errorPlacement: function(error, element){
			 element.next().html("");
			 error.appendTo(element.next());
		 }
	  });	 
	 initLoginStatus();
	 initDiscover();
	 initHomeCount();
	 
	 function initHomeCount(){
		 $.ajax({
			 url : "<dhome:url value='/system/index/homeCount'/>",
			 type : "GET",
			 success : function(data){
					$('#homeCount').html(data);
					$('#loading').html('<fmt:message key="index.totalPages"/>');
			 }
		 });
	 }
	 
	 function initLoginStatus(){
		 var $prevSibling = $("#logined");
		 $.ajax({
			 url : "<dhome:url value='/system/login/isLogin.json'/>",
			 type : "POST",
			 success : function(rtnData){
				 if (typeof(rtnData)=='object' && rtnData.isLogin){
					var html = "<div class='float-right m-right'>"+
							   "	<span><fmt:message key='index.loginSuccess'/></span>"+
							   "	<a href='"+"<dhome:url value='/people/${domain}/admin'/>"+"'><fmt:message key='index.intoManagement'/></a> |"+
							   "	<a href='"+"<dhome:url value='/people/${domain}'/>"+"'><fmt:message key='index.intoPerson'/></a>"+
							   "<div>";
					/* var $prevSibling = $("div.span5.forIE:first").prev();
					$prevSibling.next().remove();
					$prevSibling.next().remove();
					$prevSibling.after(html); */
					$prevSibling.html(html);
					insertImage ();
					$('body').show();
				 }else{
					 var html = '<iframe height="55px" marginwidth="0" marginheight="0" scrolling="no" border="0" frameBorder="no" width="100%" id="umtLogin" src="<dhome:UmtOAuthUrl/>">';
					 $prevSibling.html(html);
					 $.getScript('<dhome:UmtBase/>/js/isLogin.do', function(){
						 if(!data.result){
							$('body').show();
						 }
					 });
					setTimeout(function(){
						$('body').show();
					},2000);
				 }
			 },
			 error : function(){
				 //console.log("error");
			 }
		 });
	 }
	 function insertImage(){
		 var imgId='${currentUser.image}';
		 var imgsrc="";
		 if(imgId=='0'||imgId==''){
			imgsrc='<dhome:url value="/resources/images/dhome-head.png"/>';
		 }else{
			imgsrc='<dhome:url value="/system/img?imgId="/>'+imgId; 
		 }
		 var homeUrl='<dhome:url value="/people/${domain}"/>';
		 var html="<a href='"+homeUrl+"'><img src="+imgsrc+" style='max-height:100%; max-width:100%;'/></a>";
		 $('#register_div').html(html);
		 $('#register_div').css({"height":"200px","width":"200px"});
	 }
	 function initDiscover(){
		 $.ajax({
			 url : "<dhome:url value='/system/discover/four.json'/>",
			 type : "POST",
			 success : function(data){
				 if (typeof(data)=='object'){
					 $("#user-template").render(data.users).insertBefore("ul.discover li#discoverMore");
				 }
			 },
			 error : function(){
				// console.log("error");
			 }
		 });
	 }
});
	</script>
	<script type="text/html" id="user-template">
		<li>
			<a href="{{= userPageURL}}"><img class="header-img" src="{{= imgURL}}"/></a>
			<p><a href="{{= userPageURL}}">{{= zhName}}</a></p>
		</li>
	</script>
</html>