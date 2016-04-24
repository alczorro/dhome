<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<html lang="en">
	<head>
		<title>机构论文</title>
		<meta name="description" content="dHome" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">

	<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
		
		<div class="container page-title">
			<h1 class="dark-green"><a href='<dhome:url value="/institution/${domain }/index.html"/>'>${institution.name }</a></h1>
		</div>
		<div class="container">
			<jsp:include page="../leftMenu.jsp"></jsp:include>
			<div class="ins_backend_rightContent">
				<ul class="nav nav-tabs">
					<jsp:include page="../../commonCopyrightBackend.jsp"><jsp:param name="activeItem" value="addTreatise" /> </jsp:include>
				</ul>
					    <form name="excelpaper-form" method="POST" action="<dhome:url value='/institution/${domain }/backend/paper?func=saveExcelPaper'/>">
					    	<input type="hidden" name="papers" value="">
					      <div class="control-group">
				            <div class="controls">
				            	<div id="file-uploader-demo1" >
									<div class="qq-uploader" >
										<div class="qq-upload-drop-area"><span>拖动文件到此处上传</span></div>
										<div class="qq-upload-button" >
											上传Excel文件
										</div>					
										<ul class="qq-upload-list fileList"></ul>
									</div>
								</div>
								
								<label id="remind1" style="display:none">上传文件格式错误，请检查您的文件是否为正确的excel文件！</label>
								<div id="files-data-area"></div>
				            </div>
				          </div>
<!-- 				          <div><ul id="excel-paper-list" class="paper-list striped" style="width:100%;"></ul></div> -->
<!-- 					      <div class="d-top"> -->
<%-- 					          	<button name="save-excelpaper" type="button" class="btn btn-primary btn-set-middle"><fmt:message key='common.save'/></button> --%>
<%-- 					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/paper'/>"><fmt:message key='common.cancel'/></a> --%>
<!-- 					          </div> -->
					    </form>
					   </div>
				  </div>
<%-- 		<jsp:include page="commonfooter.jsp"></jsp:include> --%>
</body>
<jsp:include page="../../commonheader.jsp"></jsp:include> 
<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
<script type="text/javascript">
	$(document).ready(function(){
	    var uploader = new qq.FileUploader({
	        element: document.getElementById('file-uploader-demo1'),
	        template: document.getElementById('file-uploader-demo1').innerHTML,
	        action: "<dhome:url value='/institution/${domain}/backend/copyright?func=uploadExceltex'/>",
	        allowedExtensions:['xls'],
	        onComplete:function(id, fileName, data){
	        	$("#excel-paper-list").html("");
	        	$("ul.qq-upload-list.fileList li:last").prevAll().remove();
	        	if(data.success){
	        		alert("导入成功");
	        		window.location.reload();
	        	}else{
	        		alert("导入失败");
	        	}
	        },
	        messages:{
	        	typeError:"<fmt:message key='addNewPaper.upload.error.fileTypeError'/>{extensions}<fmt:message key='addNewPaper.upload.error.fileType'/>",
	        	emptyError : "<fmt:message key='addNewPaper.upload.error.fileContentError'/>"
	        },
	        showMessage: function(message){
	        	alert(message);
	        },
	        debug: true
	   });
	    $('#copyrightMenu').addClass('active');
	});
</script>

</html>