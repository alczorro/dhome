<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<dhome:InitLanuage useBrowserLanguage="true"/>
<head>
<title><fmt:message key='edit.common.domainSetting'/>-${currentUser.zhName }<fmt:message key='common.dhome'/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="commonheaderCss.jsp"></jsp:include>

</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav"
	data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${currentUser.zhName }<fmt:message key='common.dhome'/></div>
		<jsp:include page="./commonMenu.jsp">
			<jsp:param name="activeItem" value="setting" />
		</jsp:include>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title"><h3><fmt:message key="edit.common.subtitle"/></h3></div>
<div class="span3 span-left left-menu" style="margin-left:0;">
    <div class="tabbable tabs-left">
		<ul class="nav nav-tabs span12 editMode" id="sortableMenu">
				<li class="li-item">
					<a href="<dhome:url value="/people/${domain}/admin/domain"/>">
						<fmt:message key='edit.common.domainSetting'/>
	          		</a>
	          	</li>
	          	<li class="li-item">
	          		<a href="<dhome:url value="/people/${domain}/admin/language"/>">
						<fmt:message key="edit.common.languageSetting"/>
	          		</a>
				</li>
				<li class="li-item active">
	          		<a href="<dhome:url value="/people/${domain}/admin/loginEmail"/>">
						<fmt:message key="edit.common.loginEmailSetting"/>
	          		</a>
				</li>
		</ul>
	</div>   
   </div>
		<div class="span9 left-b">
				<div id="mainSpan">
					<h4>登录邮箱设置</h4><span class="error help-inline">${message }</span>
		            <p class="gray-text small-font">更改登录邮箱之后，只能通过新邮箱进行登录，请慎重操作。</p>
					<form id="editEmail" novalidate class="form-horizontal" method="post" action="<dhome:url value="/people/${domain }/admin/loginEmail/submit"/>">
					<div class="control-group d-top">
						<div class="control-group">
							<label class="control-label" for="password">当前登录邮箱：</label>
							<div class="controls padding">
								<c:out value="${user.email }"></c:out>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="password">当前登录邮箱密码：</label>
							<div class="controls">
								<input type="password" class="logininput" id="oldpassword" name="oldpassword" /> 
								<span id="oldpassword_error_place"class="error help-inline"></span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="password">新登录邮箱：</label>
							<div class="controls">
								<input type="text" name="newEmail" id="newEmail" class="logininput"/>
								<span class="help-inline gray-text message">新登录邮箱必须是科技网通行证账号</span>
								<span id="newEmail_error_place" class="error help-inline"></span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="password">新登录邮箱密码：</label>
							<div class="controls">
								<input type="password" class="logininput" id="newpassword" placeholder="" name="newpassword" />
								<span class="help-inline gray-text message">请输入该通行证账号的密码</span>
								<span id="newpassword_error_place"class="error help-inline"></span>
							</div>
						</div>
						
		            </div>
		            <div class="control-group">
		              <label class="control-label" style="width:90px;"></label>
		              <div class="controls" style="margin-left:100px;">
			                <button id="saveBtn" type="submit" class="btn btn-primary" style="margin-right:10px;"><fmt:message key='common.save'/></button>
			                <span style="display:none" id="messageEmail"><font color='green'><fmt:message key='edit.common.settingSaved'/></font></span>
		              </div>
		            </div>
		            </form>
				</div>
			
			</div>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/base.js" />"></script>
<script type="text/javascript">
	$(document).ready(function(){
// 		$.validator.addMethod("urlRegex", function(value, element){
// 	    	var regex = /^[a-zA-Z0-9\-]+$/;
// 	    	return this.optional(element)||(regex.test(value));
// 	    }, "<fmt:message key='createIndex.check.onlyLetterNum'/>");
// 		$.validator.addMethod("urlRegex2", function(value, element){
// 	    	var regex = /^[a-zA-Z0-9\-\.]+$/;
// 	    	return this.optional(element)||(regex.test(value));
// 	    }, "请输入符合URL规则的地址");
		$("#editEmail").validate({
// 		 $.validateForm('editEmail', {
			submitHandler :function(form){
				 form.submit();
			 },
				  rules: {
					  newEmail: {
				  		required:true,
				  		remote:{
				   			type: "GET",
							url: '<dhome:url value="/people/${domain}/admin/email/isEmailUsed"/>',
							data:{
								"email":function(){
									return $("#newEmail").val();
								}		
							}	
				  		}
				  	},
					oldpassword:{
						required:true,
				  		remote:{
				   			type: "GET",
							url: '<dhome:url value="/people/${domain}/admin/email/isOldPassword"/>',
							data:{
								"oldpassword":function(){
									return $("#oldpassword").val();
								},
								"email":function(){
									return '${user.email }';
								}	
							}	
				  		}
					},
					newpassword:{
						required:true,
						remote:{
				   			type: "GET",
							url: '<dhome:url value="/people/${domain}/admin/email/isNewPassword"/>',
							data:{
								"newpassword":function(){
									return $("#newpassword").val();
								},
								"email":function(){
									return $("#newEmail").val();
								}	
							}	
				  		}
					}
				  },
				   messages: {
					 newEmail:{
						   required:"邮箱不能为空",
						   remote:"该邮箱不是科技网通行证账号，或者已经被注册学术主页，请更改为其他邮箱"
					   },
				    oldpassword:{
						required:'密码不能为空',
						 remote:"请输入正确的密码"
					},
					newpassword:{
						required:'密码不能为空',
						remote:"请输入正确的密码"
					}
					 },
			 errorPlacement: function(error, element){
				 var sub="_error_place";
				 var errorPlaceId="#"+$(element).attr("name")+sub;
				 	$(errorPlaceId).html("");
				 	error.appendTo($(errorPlaceId));
			}
		 });
// 		 $("#editUrl").validate({
// 				  rules: {
// 				  	url: {
// 				  		required:true,
// 				  		urlRegex2:true,
// 				  		remote:{
// 				   			type: "POST",
// 							url: '<dhome:url value="/people"/>/'+$("#domain").val()+"/admin/url/isUrlUsed",
// 							data:{
// 								"url":function(){
// 									return $("#url").val();
// 								}		
// 							}	
// 				  		}
// 				  	}
// 				  },
// 				   messages: {
// 					   url:{
// 						   required:"<fmt:message key='domainEdit.check.notEmpty'/>",
// 						   remote:"<fmt:message key='domainEdit.check.domainInUse'/>"
// 					   }
// 					 }
// 		 })
		if('${changedEmail}'=='true'){
			 $('#messageEmail').show('slow')
				setTimeout(function(){
					$('#messageEmail').hide('slow');		
				},2000);
		}
	});
</script>
</html>