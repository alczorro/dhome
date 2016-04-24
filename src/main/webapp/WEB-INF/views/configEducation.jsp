<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
	<title><fmt:message key="config.common.title"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-boarding" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="commonBanner.jsp" flush="true"/>
	<div class="navbar boarding">
		<div class="navbar-inner" style="filter:none;">      
			<div class="container">        
				<!-- <a class="dhome-logo-black" href="<dhome:url value=""/><dhome:key value="deploy_subfix"/>"> </a>   -->     
				<div class="progress-bar two"> 
				    <span><fmt:message key="config.common.step1"/></span>
				    <i></i> <i></i> <i></i> <i></i> <i></i> 
				    <span><fmt:message key="config.common.step5"/></span> 
				</div>     
			</div>   
		</div><!-- /navbar-inner -->  
	</div>
	<div class="container narrow">
		<div class="row-fluid">
			<div class="span12 dhome-layout" style="margin-left:0;">
				<div class="page-header x-left">
					<h2><fmt:message key="configEducation.title"/><a href="<dhome:url value="/system/regist?func=stepThreeWork"/>" class="btn btn-normal d-mleft"><fmt:message key="configEducation.changeToWork"/></a></h2>
				</div>	
				<form method="post" id="registForm" action="<dhome:url value="/system/regist?func=stepThree"/>" class="form-horizontal">
			        <fieldset>
			        	<input type="hidden" name="preOper" value="education">
			           <div class="control-group">
			            <div class="controls">
			            	<select name="degree" id="degree" class="register-xlarge">
				                <option><fmt:message key="personalEducationInfo.banchelor"/></option>
				                <option><fmt:message key="personalEducationInfo.master"/></option>
				                <option><fmt:message key="personalEducationInfo.doctor"/></option>

				             </select>
				             <span class="help-inline gray-text"><fmt:message key="configEducation.form.degree"/></span>
			            </div>
			          </div>
			          <div class="control-group">
			            <div class="controls">
			              <input name="institution" id="institution" maxlength="255" class="focused register-xlarge"  type="text">
			              <span class="help-inline gray-text"><fmt:message key="configEducation.form.institution"/></span>
			            </div>
			          </div>
			          <div class="control-group">
			            <div class="controls">
			               <input name="department" maxlength="255" class="focused register-xlarge" id="department" type="text">
			               <span class="help-inline gray-text"><fmt:message key="configEducation.form.department"/></span>
			            </div>
			          </div>
			          <div class="form-actions clear-b">
			          	<button type="submit" class="btn btn-primary btn-large btn-set-large"><fmt:message key="common.saveNext"/></button>
			          </div>
			        </fieldset>
			      </form>
			</div>
		</div>
		<jsp:include page="commonfooter.jsp"></jsp:include>
	</div>

</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script>
$(function() {
	var url = "<dhome:url value='/system/regist'/>";
	$("#institution").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : url,
				type : "POST",
				data : "func=stepTwoLoadInstituion&prefixName=" + request.term,
				success : function(data) {
					response($.map(data, function(item) {
						return {
							label : item.zhName,
							value : item.zhName
							
						}
					}));
				}
			});
		},
		minLength : 2,
		open : function() {
		},
		close : function() {
		}
	});
	$("#registForm").validate({
		  rules: {
			institution: {required:true}
		  },
		   messages: {
			  institution: {required:"<fmt:message key='configEducation.check.required.institution'/>"}
			 },
			 success:function(label){
				 label.parent().parent().parent().removeClass("error");
				 label.parent().parent().parent().addClass("success");
				// if(label.parent().prev().val()){console.log(label.parent().prev().val().length);}
				 label.html("<font color='green'>√</font>");
			 },
			 errorPlacement: function(error, element){
				 element.parent().parent().removeClass("success");
				 element.parent().parent().addClass("error");
				 element.next().html("");
				 error.appendTo(element.next());
			 }
		  });
});
	function submitForm(){
		$('#registForm').submit();
	}
</script>
</html>