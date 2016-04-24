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
	<jsp:include page="../commonheaderCss.jsp"></jsp:include>
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
			<div class="span4">
				<ul class="nav nav-pills">
					<li id="list-zero" class="list-title active"><a>待审核机构名称</a></li>
					<li id="list-nonzero" class="list-title"><a>已审核机构名称</a></li>
				</ul>
				<ul class="homepageList striped max500" id="list-zero-content">
		          	<c:forEach items = "${zeroNames}" var="name">
			          <li class="source-element" data-id="${name.id}" data-type="${name.type }">
			          <input type="hidden" name="sourceInsId" value="0">
			          	<span><strong>${name.name}</strong></span>
			          	<span></span>
			          	<div class="clear"></div>
			          </li>
			        </c:forEach>
	        	</ul>
				<ul class="homepageList striped max500" id="list-nonzero-content" style="display:none;">
		          	<c:forEach items = "${nonZeroNames}" var="name">
			          <li class="source-element" data-id="${name.id}" data-type="${name.type }">
			          	<input type="hidden" name="sourceInsId" value="${name.insId }">
			          	<span><strong>${name.name}</strong></span>
			          	<span class="gray-text">（${name.officalName}）</span>
			          	<div class="clear"></div>
			          </li>
			        </c:forEach>
	        	</ul>
		    </div>
			
			<div class="span8">
				<h3 style="margin-top:0">关联官方机构</h3>
				<form id="referInsId" class="form-horizontal">
					<input type="hidden" name="sourceType" value="">
					<input type="hidden" name="sourceInsId" value="">
					<div class="control-group">
				    	<label class="control-label">用户输入的名称：</label>
				    	<div class="controls">
				        	<input name="sourceId" type="hidden" value="">
				        	<span class="gray-text"></span>
				    	</div>
				    </div>
				    <div class="control-group">
				    	<label class="control-label">官方名称：</label>
				    	<div class="controls">
				        	<input name="targetInsId" type="hidden" value="">
				        	<span class="gray-text"></span>
				    	</div>
				    </div>
				    <div class="control-group">
				    	<label class="control-label"></label>
				    	<div class="controls">
				        	<input id="searchInsName" type="text">
				        	<a id="searchInsNameButton" class="btn btn-primary btn-small">搜索</a>
				        	<div class="float-right">没有找到官方机构？
				        	<a id="newIns" class="btn btn-warning btn-small" 
				        	target="_blank" href="<dhome:url value='/system/admin/name?func=new'/>&insName=">创建一个</a></div>
				    	</div>
				    	<div><ul id="searchInsNameResult"></ul>
				    	</div>
				    </div>
				    <div class="control-group">
				    	<label class="control-label"></label>
				    	<div class="controls">
				    		<span id="submit-message" style="display:none;"></span>
				        	
				    		<button id="submitNew" class="btn btn-primary">提交</button>
				    	</div>
				    </div>
				</form>
			</div>
		</div>
	</div>
	<jsp:include page="../commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="../commonheader.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	
	$("li#list-zero").click(function(){
		$("li#list-nonzero").removeClass("active");
		$(this).addClass("active");
		$("ul#list-zero-content").show();
		$("ul#list-nonzero-content").hide();
		clickFirstLi($("ul#list-zero-content li"));
	});
	
	$("li#list-nonzero").click(function(){
		$("li#list-zero").removeClass("active");
		$(this).addClass("active");
		$(this).addClass("active");
		$("ul#list-nonzero-content").show();
		$("ul#list-zero-content").hide();
		clickFirstLi($("ul#list-nonzero-content li"));
	});
	
	$("li#list-nonzero").toggle(function(){
		$(this).nextAll().hide();
	}, function(){
		$(this).nextAll().show();
	});
	
	$("li.targetName").live("click", function(){
		var $obj = $("form#referInsId input[name=targetInsId]");
		$obj.val($(this).attr("data-id"));
		$obj.next().text($.trim($(this).children("a:eq(0)").children("span").text()));
	});
	
	$("li.source-element").bind("click", function(){
		var insName = $(this).children("span:eq(0)").children("strong").text();
		var officalName = $.trim($(this).children("span:eq(1)").text());
		officalName = (""!=officalName)?officalName.substring(1,officalName.length-1):"";
		var insId = $(this).attr("data-id");
		var insType = $(this).attr("data-type");
		var sourceInsId = $(this).children("input[name=sourceInsId]").val();
		$("#referInsId input[name=sourceType]").val(insType);
		$("#referInsId input[name=sourceId]").val(insId);
		$("#referInsId input[name=sourceId]").next().text(insName);
		$("#referInsId input[name=targetInsId]").val(sourceInsId);
		$("#referInsId input[name=sourceInsId]").val(sourceInsId);
		$("#referInsId input[name=targetInsId]").next().text(officalName);
		var hrefText = $("#newIns").attr("href");
		hrefText = hrefText.substring(0, hrefText.indexOf("insName=")+"insName=".length);
		$("#newIns").attr("href",hrefText+encodeURIComponent($.trim(insName)));
		//search(insName);
	});
	
	$("#searchInsNameButton").click(function(){
		$("#searchInsNameResult").html("");
		var keyword = $.trim($(this).prev().val());
		if(keyword !=''){
			search(keyword);
		}
	});
	
	$("#searchInsName").keyup(function(event){
		if(event.which == 13){
			$("#searchInsNameButton").click();
			event.preventDefault();
			event.stopPropagation();
		}
	}).keydown(function(event){
		if(event.which == 13){
			event.preventDefault();
			event.stopPropagation();
		}
	});
	
	$("#submitNew").click(function(event){
		var $form = $("#referInsId");
		var sourceId = $form.find("input[name=sourceId]").val();
		var sourceType = $form.find("input[name=sourceType]").val();
		var targetInsId = $form.find("input[name=targetInsId]").val();
		var sourceInsId = $form.find("input[name=sourceInsId]").val();
		var $noInsLi = $("li.targetName[data-id="+targetInsId+"]").children("span");
		if(!checkParam(sourceId, sourceType, targetInsId)){
			$("#submit-message").text("提交内容有误，请核实后提交！").show();
			setTimeout(function(){
				$("#submit-message").hide();
			},2000);
		}else if($noInsLi.length>0){
			$("#submit-message").text("要关联的目标机构尚未创建主页，请先创建然后关联！").show();
			setTimeout(function(){
				$("#submit-message").hide();
			},2000);
		}else{
			$.ajax({
				url : "<dhome:url value='/system/admin/name'/>",
				data:{"func":"referTo","sourceId":sourceId,"sourceType":sourceType,"targetInsId":targetInsId, "sourceInsId":sourceInsId},
				type:'POST',
				success:afterSubmitReturn,
				error:error
			});
		}
		event.stopPropagation();
		event.preventDefault();
	});
	
	function checkParam(sourceId, sourceType, targetInsId){
		if(""==sourceId || sourceType=="" || targetInsId=="" 
				|| targetInsId=="0"){
			return false;
		}
		return true;
	}
	
	function search(insName){
		$.ajax({
			url : "<dhome:url value='/system/admin/name'/>",
			data:{"func":"search","insName":$.trim(insName)},
			type:'POST',
			success:function(data){
				if(data.length>0){
					$("#search-result-template").render(data).appendTo("#searchInsNameResult");
					clearNoHomeLink();
				}else{
					$("#searchInsNameResult").html("没有搜索到机构！");
				}
			},
			error:error
		});
	}
	
	function clearNoHomeLink(){
		$("li.targetName").each(function(index, element){
			var $a = $(element).children("a[target='_blank']");
			if(""== $a.attr("href")){
				var createURL= "<dhome:url value='/system/admin/name?func=new'/>&insName="+$(element).children("a:eq(0)").children("span").text();
				$a.before("<span>（尚未创建主页）</span>")
				  .before("<a target='_blank' href="+createURL+">马上创建</a>")
				  .remove();
			}
		});
	}
	
	function error(){
		alert("Error while load data!");
	}
	
	function afterSubmitReturn(data){
		var message;
		if(data.status == 'success'){
			var sourceIds = data.sourceId.split(",");
			var sourceTypes = data.sourceType.split(",");
			if(sourceIds.length != sourceTypes.length){
				message = "部分关联成功！但id和type长度不一";
			}else{
				for(var i=0; i<sourceIds.length; i++){
					var $target = $("li[data-id='"+sourceIds[i]+"'][data-type='"+sourceTypes[i]+"']");
					if($target.length>0){
						var index = $("#list-zero-content").index($target);
						if(index>0){
							$("#list-nonzero-content").prepend($target.clone(true));
							$target.remove();
						}
					}
				}
				message="关联成功！";
			}
			var $li = $("li[data-id='"+sourceIds+"'][data-type='"+sourceTypes+"']");
			var $formtargetInsId = $("#referInsId input[name=targetInsId]");
			$li.children("input[name=targetInsId]").val($formtargetInsId.val());
			$li.children("span:eq(1)").text("（"+$formtargetInsId.next().text()+"）");
			$("#referInsId input[name=sourceInsId]").val(data.targetInsId);
			$("li.source-element input[name=sourceInsId]").val(data.targetInsId);
		}else{
			message="关联失败！";
		}
		$("#submit-message").text(message).show();
		setTimeout(function(){
			$("#submit-message").hide();
		},2000);
	}
	
	function clickFirstLi($li){
		if($li.length>0){
			$li.eq(0).click();
		}
	}
	
	function init(){
		clickFirstLi($("ul#list-zero-content li"));
	}
	
	init();
	
});
</script>
<script type="text/html" id="search-result-template">
	<li class="targetName" data-id="{{= id}}">
		<a><span>{{= name}}</span></a>
		<a target="_blank" href="{{= url}}">查看</a>
	</li>
</script>
</html>