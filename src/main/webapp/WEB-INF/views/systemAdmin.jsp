<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html>
<html lang="en">
<head>
	<title><fmt:message key="systemAdmin.pageTitle"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body admin" data-offset="50" data-target=".subnav" data-spy="scroll">
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">      
			<div class="container">        
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">          
					<span class="icon-bar"></span>          
					<span class="icon-bar"></span>          
					<span class="icon-bar"></span>        
				</a>      
				<div class="nav-collapse">         
					<ul class="nav">   
					  <li class="dropdown">
	                      <a class="dropdown-toggle" data-toggle="dropdown"><span class="brand dhome-logo"></span> <b class="caret"></b></a>
	                  </li>
					</ul>
					<ul class="nav pull-right">  
						<li>
				            <div class="input-append searchdiv">
								<input class="span2 searchinput" type="text" placeholder="<fmt:message key='systemAdmin.placeHolder'/>">
								<button class="add-on searchbtn" type="button"><fmt:message key='systemAdmin.search'/></button>
							</div>
						</li>  
					<c:choose>   
					  <c:when test="${currentUser!=null&&currentUser!=''}"> <!-- /登录状态 -->  
					      <!-- /判断当前的访问的页面 --> 
					    <li><a href="<dhome:url value='/people/${domain}/admin/p/index'/>">${currentUser.zhName }</a></li>
						<li><a href="<dhome:url value='/system/logout'/>"><fmt:message key="common.logout"/></a></li>  
					  </c:when>			
					  <c:otherwise>
					  <li><a href="<dhome:url value='/system/login'/>"><fmt:message key="common.login"/></a></li>
					  <li><a href="<dhome:url value='/system/regist?func=stepOne'/>"><fmt:message key="common.regist"/></a></li>  </c:otherwise>
					  </c:choose>
					</ul>
				</div><!-- /.nav-collapse -->      
			</div>   
		</div><!-- /navbar-inner -->  
	</div>
	
	<div class="container">
		<div class="row-fluid">
			<div class="span3 bs-docs-sidebar">
		        <ul class="nav nav-list bs-docs-sidenav affix">
		          <li class="active"><a id="all"><i class="icon-chevron-right"></i> <fmt:message key="systemAdmin.status.all"/></a></li>
		          <li><a id="waitForCheck"><i class="icon-chevron-right"></i> <fmt:message key="audit.auditNeed"/></a></li>
		          <li><a id="checkFailed"><i class="icon-chevron-right"></i> <fmt:message key="audit.auditNot"/></a></li>
		          <li><a id="checking"><i class="icon-chevron-right"></i> <fmt:message key="audit.auditIng"/></a></li>
		          <li><a id="published"><i class="icon-chevron-right"></i> <fmt:message key="systemAdmin.status.published"/></a></li>
		          <li><a id="removed"><i class="icon-chevron-right"></i> <fmt:message key="audit.auditDelete"/></a></li>
		        </ul>
		      </div>
			
			<div class="span9">
				<ul class="homepageList striped">
					<li class="th">
						<div class="title"><strong><fmt:message key="systemAdmin.homePageName"/></strong></div>
						<div class="oper"><strong><fmt:message key="systemAdmin.operation"/></strong></div>
						<div class="clear"></div>
					</li>
				</ul>
				<p id="notice" class="more-page clear"><fmt:message key="discover.loading"/></p>
				<p id="loadmore" class="more-page clear" style="display:none"><a><fmt:message key="discover.findMore"/></a></p>
			</div>
		</div>
	</div>
	
	<div id="check-popup" tabindex="-1" class="modal hide fade">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3 class="name"></h3>
        </div>
		<form name="check-form" class="form-horizontal no-bmargin">
			<input type="hidden" name="id" value="">
			<fieldset>
				<a class="modal-body m-mleft" id="homepageurl" target="_blank" href="#"></a>
				<div class="modal-body m-mleft">
					<label class="radio inline">
		              <input type="radio" name="check" id="optionsRadios1" value="auditOK">
		             	 <fmt:message key="systemAdmin.form.success"/>
		            </label>
		            <label class="radio inline">
		              <input type="radio" name="check" id="optionsRadios1" value="auditNot">
		             	 <fmt:message key="systemAdmin.form.fail"/>
		            </label>
		            <label class="radio inline">
		              <input type="radio" name="check" id="optionsRadios1" value="auditDelete">
		             	<fmt:message key="systemAdmin.form.remove"/>
		            </label>
		            <p class="abs-top"><strong><fmt:message key="systemAdmin.form.reason"/></strong></p>
		            <textarea name="reason" rows="3" class="d-middle"></textarea>
				</div>
				<div class="modal-footer">
					<span class="errorMsg small-font orange d-right"></span>
					<a id="cancel" data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
					<button id="save" type="button" class="btn btn-primary"><fmt:message key='common.save'/></button>
		        </div>
	        </fieldset>
        </form>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	var usersURL = "<dhome:url value='/system/admin/getUsers.json'/>";
	var keyword = "";
	var navStatus = "";
	var offset = 0;
	var size = 15;
	var autoLoadMore; //自动加载
	var checkInfoTime;//审核提示
	var curTopLiPos = 0;//当前处于顶部的li的位置
	
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
	
	init();
	
	function init(){
		$("input[type=radio][name=check][value=success]").attr("checked", true);
		var params = {status: navStatus, keyword: keyword, offset:offset, size:size};
		notice_handler.loading();
		ajaxRequest(usersURL,params,renderData,fail);
	}
	
	function ajaxRequest(url, params, success, fail){
		$.ajax({
			url : url,
			type : 'POST',
			data : params,
			success : success,
			error : fail
		});
	}
	
	function renderData(data){
		if(typeof(data.users)=='undefined' || data.users.length<0){
			if(offset==0){
				notice_handler.noMatch();
			}else{
				notice_handler.noMore();
			}
		}else{
			data.users = JSON.parse(data.users);
			data.users = processStatus(data.users);
			$("#page-template").render(data.users).appendTo("ul.homepageList");
			clearNoDomainUser();
			if(data.users.length<size){
				notice_handler.noMore();
			}else{
				notice_handler.readyToLoad();
			}
			//offset += data.users.length;
			offset += data.actualOffset;
		}
	}
	
	function processStatus(users){
		if(users.length>0){
			var len = users.length;
			for(var i=0; i<len; i++){
				users[i].statusZh = getStatusDescription(users[i].status);
			}
		}
		return users;
	}
	
	function getStatusDescription(status){
		var desc = "";
		if(status == "auditOK"){
			desc = "已发布";
		}else if(status == "auditNot"){
			desc = "审核未通过";
		}else if(status == "auditNeed"){
			desc = "待审核";
		}else if(status == "auditDelete"){
			desc = "已删除";
		}else if(status == "auditIng"){
			desc = "审核中";
		}else{
			desc = "待审核";
		}
		return desc;
	}
	
	function clearNoDomainUser(){
		$("ul.homepageList li").each(function(){
			var $alink = $(this).find("a.view");
			if($alink.length>0 && $alink.attr("href") ==""){
				$alink.remove();
				$(this).find("div.title a").removeAttr("href");
			}
		});
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
	
	/**      左侧边栏点击事件 ***********/
	
	function requestForData($target){
		var params = {status: navStatus, keyword: keyword, offset:offset, size:size};
		notice_handler.loading();
		$("ul.nav li").removeClass("active");
		if($target[0].tagName == "A"){
			$target.parent().addClass("active");
		}
		ajaxRequest(usersURL,params,renderData,fail);
	}
	
	$("a#all").click(function(){
		setContext("","",0);
		requestForData($(this));
	});
	
	$("a#waitForCheck").click(function(){
		setContext("auditNeed","",0);
		requestForData($(this));
	});
	
	$("a#checkFailed").click(function(){
		setContext("auditNot","",0);
		requestForData($(this));
	});
	
	$("a#checking").click(function(){
		setContext("auditIng","",0);
		requestForData($(this));
	});
	
	$("a#published").click(function(){
		setContext("auditOK","",0);
		requestForData($(this));
	});
	
	$("a#removed").click(function(){
		setContext("auditDelete","",0);
		requestForData($(this));
	});
	
	$("input.searchinput").keyup(function(event){
		if(event.which == 13){
			$(".searchbtn").click();
		}
	});
	
	$(".searchbtn").click(function(){
		setContext("",$("input.searchinput").val(),0);
		requestForData($("input.searchinput"));
	});
	
	/**      审核操作                          ***/
	$("a.check").live("click",function(){
		var $formContainer = $("#check-popup");
		var $li = $(this).parents("li");
		curTopLiPos = $(window).scrollTop();
		$formContainer.find("h3.name").text($li.find("a.name").text());
		$formContainer.find("textarea[name=reason]").val($li.find("input[type=hidden][name=reason]").val());
		$formContainer.find("input[name=id]").val($li.attr("id"));
		var $link = $formContainer.find("a#homepageurl");
		$link.attr("href",$li.children("div.title").children("a").attr("href"));
		$link.text("<fmt:message key='systemAdmin.viewHomePage'/>");
		var status = $li.find("input[type=hidden][name=status]").val();
		var $target = $("input[type=radio][name=check][value="+status+"]");
		if($target.length > 0){
			$target.attr("checked",true);
		}else{
			$("input[type=radio][name=check][value=auditOK]").attr("checked",true);
		}
		$formContainer.modal("show");
	});
	
	$("button#save").click(function(){
		//var data = validateForm();
		//if(data.status){
			var status = "auditOK";
			$("input[type=radio][name=check]").each(function(index, element){
				if($(element).attr("checked")){
					status = $(element).val();
				}
			});
			var reason = $("textarea[name=reason]").val();
			var url = "<dhome:url value='/system/admin/check.json'/>";
			var id = $("input[type=hidden][name=id]").val();
			ajaxRequest(url,{status:status, reason:reason, id:id},checkSuccess, checkFail);
			$("#check-popup").modal("hide");
		/* }else{
			$("#check-popup").find("span.errorMsg").text(data.msg);
		} */
	});
	
	$("a#cancel").click(function(){
		$("#check-popup").modal("hide");
		$('html, body').animate({ scrollTop: curTopLiPos }, 700);
	});
	
	
	
	function validateForm(){
		var status = false;
		var data = new Array();
		$("input[type=radio][name=check]").each(function(index, element){
			if($(element).attr("checked")){
				status = true;
			}
		});
		var reason = $("textarea[name=reason]").val();
		data["status"] = (status && reason != "")?true:false;
		if(!data.status){
			if(!status && reason == ""){
				data["msg"] = "<fmt:message key='systemAdmin.form.msgDouble'/>";
			}else if(!status){
				data["msg"] = "<fmt:message key='systemAdmin.form.msgStatus'/>";
			}else if(reason == ""){
				data["msg"] = "<fmt:message key='systemAdmin.form.msgReason'/>";
			}
		}
		return data;
	}
	
	function checkSuccess(data){
		$('html, body').animate({ scrollTop: curTopLiPos }, 700);
		var $target = $("li[id="+data.id+"]");
		var $checkInfo = $target.find("span.check-info");
		var statusDesc = getStatusDescription(data.status);
		$target.find("a.name").text(data.name+"<fmt:message key='systemAdmin.personalHomePage'/>"+"("+statusDesc+")");
		$target.find("input[type=hidden][name=reason]").val(data.reason);
		if(navStatus == "" || navStatus == data.status){
			$checkInfo.text("<fmt:message key='systemAdmin.success.header'/>"+statusDesc+"<fmt:message key='systemAdmin.success.tailShow'/>");
			$checkInfo.show();
			clearTimeout(checkInfoTime);
			checkInfoTime = setTimeout(function(){
				var id = $("#check-popup input[name=id]").val();
				$(window).scrollTop(curTopLiPos);
				$("li[id="+id+"]").find("span.check-info").hide();
			}, 2000);
		}else{
			$checkInfo.text("<fmt:message key='systemAdmin.success.header'/>"+statusDesc+"<fmt:message key='systemAdmin.success.tailNotShow'/>");
			$checkInfo.show();
			clearTimeout(checkInfoTime);
			checkInfoTime = setTimeout(function(){
				var id = $("#check-popup input[name=id]").val();
				var $li = $("li[id="+id+"]");
				$(window).scrollTop(curTopLiPos);
				$li.find("span.check-info").hide();
				$li.remove();
			}, 2000);
		}
	}
	
	function checkFail(){
		$('html, body').animate({ scrollTop: curTopLiPos }, 700);
		var id = $("#check-popup input[name=id]").val();
		var $checkInfo = $("li[id="+id+"]").find("span.check-info");
		$checkInfo.text("<fmt:message key='systemAdmin.fail.message'/>");
		$checkInfo.show();
		clearTimeout(checkInfoTime);
		checkInfoTime = setTimeout(function(){
			var id = $("#check-popup input[name=id]").val();
			$("li[id="+id+"]").find("span.check-info").hide();
		}, 2000);
	}
	
	
	
});
</script>
<script type="text/html" id="page-template">
	<li id="{{= id}}">
		<div class="title">
			<a target="_blank" href="{{= url}}" class="name">{{= title}}<fmt:message key='systemAdmin.personalHomePage'/>[{{= email}}] - {{= domain}}  ({{= statusZh}})</a>
			<span class="check-info small-font orange m-sleft" style="display:none; float:right;"></span>
			<input type="hidden" name="status" value="{{= status}}">
			<input type="hidden" name="reason" value="{{= reason}}">
		</div>
		<div class="oper">
			<a class="view" target="_blank" href="{{= url}}"><fmt:message key='systemAdmin.operation.view'/></a>
			<a class="check" data-toggle="modal" ><fmt:message key='systemAdmin.operation.check'/></a>
		</div>
		<div class="clear"></div>
	</li>
</script>
</html>