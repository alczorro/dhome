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
				<%-- <a class="dhome-logo-black" href="<dhome:url value=""/><dhome:key value="deploy_subfix"/>"> </a>    --%>      
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
					<h2><fmt:message key="configCareer.title"/><a href="<dhome:url value="/system/regist?func=stepThreeEdu"/>" class="btn btn-normal d-mleft"><fmt:message key="configCareer.changeToEducation"/></a></h2>
				</div>	
			    <form id="registForm" action="<dhome:url value="/system/regist?func=stepThree"/>" method="POST" class="form-horizontal">
			        <fieldset>
			        <input name="preOper" type="hidden" value="work"/>
			          <div class="control-group">
			            <div class="controls">
			              <input maxlength="255" class="focused register-xlarge" name="institution" id ="institution" type="text" placeholder="<fmt:message key="configCareer.placeholder.institution"/>">
			              <span class="help-inline gray-text"><fmt:message key="configCareer.form.institution"/></span>
			            </div>
			          </div>
			          <div class="control-group">
			            <div class="controls">
			              <input maxlength="255" class="focused register-xlarge" name="department" id="department" type="text" placeholder="<fmt:message key="configCareer.placeholder.department"/>">
			              <span class="help-inline gray-text"><fmt:message key="configCareer.form.department"/></span>
			            </div>
			          </div>
			           <div class="control-group">
			            <div class="controls">
			             <input maxlength="255" class="focused register-xlarge" name="position" id="position" type="text" placeholder="<fmt:message key="configCareer.placeholder.position"/>">
			            
				             <span  class="help-inline gray-text"><fmt:message key="configCareer.form.position"/></span>
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
<script type="text/javascript">
$(function() {
	 $("#registForm").validate({
		  rules: {
			institution: {required:true},
			position:{required:true}
		  },
		   messages: {
			  institution: {required:"<fmt:message key='configCareer.check.required.institution'/>"},
			  position:{required:"<fmt:message key='configCareer.check.required.position'/>"}
			 },
			 success:function(label){
				 label.parent().parent().parent().removeClass("error");
				 label.parent().parent().parent().addClass("success");
				 //if(label.parent().prev().val()){console.log(label.parent().prev().val().length);}
				 label.html("<font color='green'>√</font>");
			 },
			 errorPlacement: function(error, element){
				 element.parent().parent().removeClass("success");
				 element.parent().parent().addClass("error");
				 element.next().html("");
				 error.appendTo(element.next());
			 }
		  });
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
});

</script>
</html>