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
				<li class="li-item active">
					<a href="<dhome:url value="/people/${domain}/admin/domain"/>">
						<fmt:message key='edit.common.domainSetting'/>
	          		</a>
	          	</li>
	          	<li class="li-item">
	          		<a href="<dhome:url value="/people/${domain}/admin/language"/>">
						<fmt:message key="edit.common.languageSetting"/>
	          		</a>
				</li>
				<li class="li-item">
	          		<a href="<dhome:url value="/people/${domain}/admin/loginEmail"/>">
						<fmt:message key="edit.common.loginEmailSetting"/>
	          		</a>
				</li>
		</ul>
	</div>   
   </div>
		<div class="span9 left-b">
			<form id="editDomain" class="form-horizontal" method="post" action="<dhome:url value="/people/${domain }/admin/domain/submit"/>">
				<div id="mainSpan">
					<h4><fmt:message key='domainEdit.defautDomain.title'/></h4>
					<div class="control-group d-top">
		              <label class="control-label" style="width:90px;"><b><fmt:message key='domainEdit.defautDomain'/></b></label>
		              <div class="controls" style="margin-left:100px;">
			                http://www.escience.cn<%=request.getContextPath() %>/people/
							<input maxlength="20" type="text"  value="${domain }"  name="domain" id="domain" class="d-ss"/>
							<input type="hidden"  value="${domain }"  name="oldDomain"/>
		              </div>
		            </div>
		            <div class="control-group">
		              <label class="control-label" style="width:90px;"></label>
		              <div class="controls" style="margin-left:100px;">
			                <button type="submit" class="btn btn-primary"><fmt:message key='common.save'/></button>
			               <span style="display:none" id="message"><font color='red'><fmt:message key='edit.common.settingSaved'/></font></span>
		              </div>
		            </div>
				</div>
				</form>
				<div id="mainSpan">
					<h4><fmt:message key="urlmapping.title"/></h4>
					<form id="editUrl" class="form-horizontal" method="post" action="<dhome:url value="/people/${domain }/admin/url/submit"/>">
					<input type="hidden" name="urlId" value="${urlMapping.id }">
					<div class="control-group d-top">
					  <div class="controls" style="margin-left:100px;">
			               <input type="checkbox" <c:if test="${urlMapping.status=='valid' }">checked="checked"</c:if> id="status" name="status" style="margin-bottom:5px; margin-right:5px;" /><fmt:message key="urlmapping.open"/>
		              </div>
		              <div class="controls" style="margin-left:100px;">
			                http://<input maxlength="60" type="text"  value="${urlMapping.url}"  name="url" id="url" class="input-xxlarge"/>
		              </div>
		            </div>
		            <div class="control-group">
		              <label class="control-label" style="width:90px;"></label>
		              <div class="controls" style="margin-left:100px;">
			                <button type="submit" class="btn btn-primary"><fmt:message key='common.save'/></button>
			                <span style="display:none" id="messageUrl"><font color='red'><fmt:message key='edit.common.settingSaved'/></font></span>
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
		$.validator.addMethod("urlRegex", function(value, element){
	    	var regex = /^[a-zA-Z0-9\-]+$/;
	    	return this.optional(element)||(regex.test(value));
	    }, "<fmt:message key='createIndex.check.onlyLetterNum'/>");
		$.validator.addMethod("urlRegex2", function(value, element){
	    	var regex = /^[a-zA-Z0-9\-\.]+$/;
	    	return this.optional(element)||(regex.test(value));
	    }, "请输入符合URL规则的地址");
		$("#editDomain").validate({
				  rules: {
				  	domain: {
				  		urlRegex:true,
				  		required:true,
				  		remote:{
				   			type: "GET",
							url: '<dhome:url value="/people"/>/'+$("#domain").val()+"/admin/domain/isDomainUsed",
							data:{
								"domain":function(){
									return $("#domain").val();
								}		
							}	
				  		}
				  	}
				  },
				   messages: {
					   domain:{
						   required:"<fmt:message key='domainEdit.check.notEmpty'/>",
						   remote:"域名已被注册"
					   }
					 }
		 })
		 $("#editUrl").validate({
				  rules: {
				  	url: {
				  		required:true,
				  		urlRegex2:true,
				  		remote:{
				   			type: "POST",
							url: '<dhome:url value="/people"/>/'+$("#domain").val()+"/admin/url/isUrlUsed",
							data:{
								"url":function(){
									return $("#url").val();
								}		
							}	
				  		}
				  	}
				  },
				   messages: {
					   url:{
						   required:"<fmt:message key='domainEdit.check.notEmpty'/>",
						   remote:"<fmt:message key='domainEdit.check.domainInUse'/>"
					   }
					 }
		 })
		 if('${changed}'=='true'){
			 $('#message').show('slow')
				setTimeout(function(){
					$('#message').hide('slow');		
				},2000);
		}
		if('${changedUrl}'=='true'){
			 $('#messageUrl').show('slow')
				setTimeout(function(){
					$('#messageUrl').hide('slow');		
				},2000);
		}
	});
</script>
</html>