<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html>
<html lang="en">
<head>
	<title><fmt:message key='login.title'/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>
	<body style="display:none" class="dHome-body gray">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container">
	<c:if test="${message != '' }">
		<div class="alert alert-error">
	        <button type="button" class="close" data-dismiss="alert">×</button>
	        <strong>${message}</strong> 
	    </div>
	    </c:if>
	   </div>
	<div class="container canedit">
	    <div class="dhome-layout">
		    <iframe height="500px" width="100%"  border="0" frameBorder="no" id="umtLogin" src="<dhome:UmtOAuthUrl/>&state=<dhome:encode value="${state }"/>">
			</iframe>
	    	<%-- <div class="page-header x-left">
	    		<h2><fmt:message key='common.login'/></h2>
	    	</div>
	    	<div class="x-left">
				<form action="<dhome:url value='/system/login'/>" method="POST" class="form-horizontal login-index">
					<fieldset>
						<input type="hidden" name="pageUrl" value="${pageUrl }">
						<div class="control-group">
							<div class="controls">
								<div class="input-prepend">
									<span class="add-on"><i class="icon-envelope"></i></span><input
										name="email" value="${email}" type="text" class="register-xlarge ff-hack" 
										placeholder="<fmt:message key='login.placeholder.email'/>"><span class="help-inline gray-text"><fmt:message key='login.form.email'/></span>
								</div>
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<div class="input-prepend">
									<span class="add-on"><i class="icon-lock"></i></span><input
										name="password" type="password" class="register-xlarge ff-hack"
										placeholder="<fmt:message key='login.placeholder.password'/>"><span class="help-inline gray-text"><fmt:message key='login.form.password'/></span>
								</div>
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<button type="submit" class="btn btn-large btn-primary btn-set-large"><fmt:message key='common.login'/></button>
								<br><br>
								<p>
									<a href="<dhome:url value='/system/login?func=forgetPsw'/>" target="_blank"> <fmt:message key='login.forgetPassword'/></a>
								</p>
							</div>
						</div>
					</fieldset>
				</form>
			</div> --%>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
</body>

<jsp:include page="commonheader.jsp"></jsp:include>
<script>
		$(document).ready(function(){
			$.getScript('<dhome:UmtBase/>/js/isLogin.do', function(){
				 if(!data.result){
					$('body').show();
				 }
			 });
		});
</script>
</html>