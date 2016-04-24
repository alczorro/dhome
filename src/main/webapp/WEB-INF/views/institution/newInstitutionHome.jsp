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
		<div class="page-header">
            <h1>添加新的机构名称</h1>
        </div>
		<form id="newInstitution" class="form-horizontal d-top" action="<dhome:url value='/system/admin/name?func=submitNew'/>" method="POST">
			<input type="hidden" name="sourceType" value="">
			<div class="control-group">
				<label class="control-label">机构名称：</label>
				<div class="controls">
				    <input id="institutionName" name="institutionName" type="text" value="${institutionName}">
				    <span class="help-inline gray-text">请输入官方的机构名称。</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">机构域名：</label>
				<div class="controls">
					http://www.escience.cn/institution/
				    <input id="institutionDomain" name="institutionDomain" type="text" value="${institutionDomain }">
				    /index.html
				    <span class="help-inline gray-text">请输入机构域名</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">机构简介：</label>
				<div class="controls">
					<textarea name="institutionIntroduction" rows="3" cols="10" class="register-xlarge"></textarea>
					<span class="help-inline gray-text">在这里输入机构简介</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls">
					<button name="submitNew" class="btn btn-primary">创建</button>
					<button id="cancel" class="btn btn-primary">返回</button>
				</div>
			</div>
			
		</form>
	</div>
	<jsp:include page="../commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="../commonheader.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	$.validator.addMethod("urlRegex", function(value, element){
    	var regex = /^[a-zA-Z0-9\-]+$/;
    	return this.optional(element)||(regex.test(value));
    }, "机构域名必须是合法的url");
	$("#newInstitution").validate({
		rules:{
			institutionName:{
				required:true,
				maxlength:200,
				remote:{ 
		   			   type: "POST",
					   url: "<dhome:url value='/system/admin/name'/>",
					   data: { 
						    "func":"checkName",
							"name":function(){
								return $("#institutionName").attr("value");
							}
					   }
		   		}
			},
			institutionDomain:{
				required:true,
				urlRegex:true,
				maxlength:50,
		   		remote:{ 
		   			   type: "POST",
					   url: "<dhome:url value='/system/admin/name'/>",
					   data: { 
						    "func":"checkDomain",
							"domain":function(){
								return $("#institutionDomain").attr("value");
							}
					   }
		   		}
			},
			institutionIntroduction:{
				maxlength:200
			}
		},
		messages:{
			institutionName:{
				required:"机构名称不能为空",
				maxlength:"输入内容过长",
				remote:"机构名已存在"
			},
			institutionDomain:{
				required:"机构域名不能为空",
				urlRegex:"机构域名必须是合法url",
				maxlength:"输入内容过长",
				remote:"域名已存在"
			},
			institutionIntroduction:{
				maxlength:"输入内容过长"
			}
		},
		success:function(label){
			label.parent().parent().parent().removeClass("error");
			label.parent().parent().parent().addClass("success");
			label.html("<font color='green'>√</font>");
		},
		errorPlacement: function(error, element){
			if(element.attr('id')=='read'){
				error.appendTo(element.next().next());
			}else{
				element.parent().parent().removeClass("success");
				element.parent().parent().addClass("error");
				element.next().html("");
				error.appendTo(element.next());
			}
		}
	});
	
	$("#cancel").click(function(){
		window.close();
	});
});	
</script>
</html>