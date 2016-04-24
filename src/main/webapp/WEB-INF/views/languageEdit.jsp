<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html>
<html lang="en">
<head>
<title><fmt:message key="edit.common.languageSetting"/>-${currentUser.zhName }<fmt:message key='common.dhome'/></title>
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
	          	<li class="li-item active">
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
			<form class="form-horizontal"  method="post" action="<dhome:url value="/people/${domain }/admin/language/submit"/>">
				<div id="mainSpan">
					<h4><fmt:message key='languageEdit.defaultLanguage.title'/></h4>
					<div class="control-group d-top">
		              <label class="control-label" style="width:130px; margin-top:-5px"><b><fmt:message key='languageEdit.defaultLanguage'/></b></label>
		              <div class="controls" style="margin-left:140px;">
			                <input id="cn" type="radio" checked="checked" name="language" value="zh_CN" style="margin:0 5px 3px;"><fmt:message key='languageEdit.Chinese'/>
							<input id="en" type="radio" name="language" value="en_US" style="margin:0 5px 3px;"><fmt:message key='languageEdit.English'/>
		              </div>
		              <br>
		              <div class="gray-text small-font">(<fmt:message key="languageEdit.messageTip"/>)</div>
		            </div>
		            <div class="control-group">
		              <label class="control-label" style="width:130px;"></label>
		              <div class="controls" style="margin-left:140px;">
			                <button type="submit" class="btn btn-primary" onclick="upload()"><fmt:message key='common.save'/></button>
			                <span style="display:none" id="message"><font color='red'><fmt:message key='edit.common.settingSaved'/></font></span>
		              </div>
		            </div>
				</div>
			</form>
			</div>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/base.js" />"></script>
<script>
$(document).ready(function(){
	var language='${language}';
	if(language=='zh_CN'){
		$('#cn').attr("checked","checked");
	}else if(language=="en_US"){
		$('#en').attr("checked","checked");
	}else{
		$('#cn').attr("checked","checked");
	}
	if('${changed}'=='true'){
		 $('#message').show('slow');
			setTimeout(function(){
				$('#message').hide('slow');		
			},2000);
	}
});
</script>
</html>