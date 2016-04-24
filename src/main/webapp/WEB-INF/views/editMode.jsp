<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<dhome:InitLanuage useBrowserLanguage="true"/>
<head>
	<title><fmt:message key='editMode.title'/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	
	<div class="container canedit">
		<jsp:include page="adminCommonTop.jsp"></jsp:include>
		<div class="row-fluid mini-layout center">
			<jsp:include page="adminCommonLeft.jsp"></jsp:include>
		    <div class="span9">
			    <form method="post" >
			        <fieldset>
			          <div class="control-group">
			            <div class="controls">
			              <input name="institution" class="focused register-xlarge"  type="text">
			              <span class="help-inline gray-text"><fmt:message key='editMode.paperTitle'/></span>
			            </div>
			          </div>
			          <div class="control-group">
			            <div class="controls" style="width:80%; height:300px; border:1px solid #666;" >
			               	<fmt:message key='editMode.editArea'/>
			            </div>
			          </div>
			          <div class="form-actions clear-b">
			          	<a class="btn "><fmt:message key='editMode.saveAsDraft'/></a>
			          	<a class="btn btn-primary"><fmt:message key='common.publish.do'/></a>
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
	$(document).ready(function(){
		//leftMenu
		$(".icon-file").addClass("icon-white");
		$(".icon-file").parent().parent("li").addClass("active");
	});
</script>
</html>