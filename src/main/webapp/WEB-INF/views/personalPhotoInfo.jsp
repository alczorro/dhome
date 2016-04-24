<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%@ page import="java.util.*" %>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><fmt:message key='personalPhotoInfo.title'/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
<link href="<dhome:url value="/resources/css/fileuploader.css"/>"
	rel="stylesheet" type="text/css">
<link href="<dhome:url value="/resources/css/imgareaselect-default.css"/>"
	rel="stylesheet" type="text/css">
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${titleUser}<fmt:message key='common.index.title'/></div>
		<jsp:include page="./commonMenu.jsp">
			<jsp:param name="activeItem" value="personInfo" />
		</jsp:include>
	</div>
	
	<div class="container canedit">
		<div class="row-fluid mini-layout">
			<div class="config-title" style="margin-top:0.5em;"><h3><fmt:message key='admin.common.link.personInfo'/></h3></div>
			<div class="span11">
				<ul class="nav nav-tabs">
					<li><li>&nbsp;&nbsp;&nbsp;</li></li>
					<li>
					    <a href="<dhome:url value='/people/${domain}/admin/personal'/>">
					    	<strong><fmt:message key='personal.common.basicInfo'/></strong>
					    </a>
				    </li>
				    <li>
				    <a href="http://passport.escience.cn/user/password.do?act=showChangePassword" target="_blank">
				    <strong><fmt:message key='personal.common.updatePsw'/></strong></a>
				    </li>
				    <li>
					    <a href="<dhome:url value='/people/${domain}/admin/personal/work'/>">
					    	<strong><fmt:message key='personal.common.work'/></strong>
					    </a>
				    </li>
				    <li >
					    <a href="<dhome:url value='/people/${domain}/admin/personal/education'/>">
					    	<strong><fmt:message key='personal.common.education'/></strong>
					    </a>
				    </li>
				    <li class="active">
					    <a href="<dhome:url value='/people/${domain}/admin/personal/photo'/>">
					    	<strong><fmt:message key='personal.common.photo'/></strong>
					    </a>
				    </li>
				    <li>
						    <a href="<dhome:url value='/people/${domain}/admin/personal/favorite'/>">
						    	<strong><fmt:message key="personalSocialMediaInfo.title"/></strong>
						    </a>
					    </li>
			    </ul>
			    <div class="m-sleft">
				    <div class="header-container">
				    	<div class="header-img">
				    		<span style="vertical-align:middle; display:inline-block; height:100%; width:1px; margin-left:-1px;"></span>
							<img style="margin-left:-4px;" id="headPhoto" src="${filePath }${fileName }" /> 
						</div>
						<div class="clear"></div>
						<div class="d-center x-top">
							<a id='editPhoto' href="#myPhotoModal" data-toggle="modal" style="display:none"><fmt:message key='personalPhotoInfo.cutPhoto'/></a>
						</div>
					</div>
					
					<div class="float-left x-sleft d-large-top">
						<dhome:IsMainTain>
							<font color="red"><fmt:message key='common.clb.imageinfo.ismaintain'/></font>
						</dhome:IsMainTain>
						<div id="imageUploader" class="d-large-btn maintain">
							<div class="qq-uploader">
								<div class="qq-upload-button">
									<input type="file" multiple="multiple" name="files">
								</div>
								<ul class="qq-upload-list fileList"></ul>
							</div>
						</div>
					</div>
					<div class="clear"></div>
					<form action="<dhome:url value="/system/regist?func=complete"/>"
						method="post">
						<fieldset>
							<div class="form-actions clear-b d-top" style="margin-left:-1.5em;">
								<button id=completeBtn type="submit"
									class="btn btn-primary m-left"><fmt:message key='common.save'/></button>
									<span style="display:none" id="message"><font color='red'><fmt:message key='personalPhotoInfo.saveSuccess'/></font></span>
							</div>
								<input type="hidden" name="x" id="x" value="0"><br /> 
								<input type="hidden" name="y" id="y" value="0"><br /> 
								<input type="hidden" name="height" value="200" id="h"><br /> 
								<input type="hidden" name="width" value="200" id="w"> 
								<input type="hidden" value="${fileName }" id="fileName" name="fileName" /> 
								<input type="hidden" id="isCut" name="isCut" value="false"> 
								<input type="hidden" id="isDefault" name="isDefault" value="true">
								<input type="hidden" id="proportion">
								<input type="hidden" name="scale" id="scale">
						</fieldset>
					</form>
					
					<div id="myPhotoModal" class="modal hide fade">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">×</button>
							<h3><fmt:message key='personalPhotoInfo.cutPhoto'/></h3>
						</div>
						<div class="modal-body">
							<div class="control-group">
								<div class="controls">
									<img src="${filePath }${fileName }"  height="390" width="550" id="headPhotoEdit" />
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<a href="#" class="btn" data-dismiss="modal"><fmt:message key='common.cancel'/></a> <a
								href="javascript:cutFinish()" class="btn btn-primary"><fmt:message key='common.save'/></a>
						</div>
					</div>
				
				</div>
		    </div>
		</div>
		
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/upload/fileuploader.js"/>"></script>
<script
	src="<dhome:url value="/resources/scripts/jquery/jquery.imgareaselect.pack.js"/>"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		function setImgSeletor(mils){
			var height=($('#headPhotoEdit').height());
			var width=($('#headPhotoEdit').width());
			var smaller=height<width?height:width;
			$('#x').val(0);
			$('#y').val(0);
			$('#w').val(smaller*0.5);
			$('#h').val(smaller*0.5);
			setTimeout(function(){
				$('#headPhotoEdit').imgAreaSelect({
					x1 : 0,
					y1 : 0,
					x2 : smaller*0.5,
					y2 : smaller*0.5,
					aspectRatio : '1:1',
					handles : true,
					onSelectChange : function(img, selection) {
						$('#x').val(selection.x1);
						$('#y').val(selection.y1);
						$('#w').val(selection.width);
						$('#h').val(selection.height);
						$('#isCut').val("true");
					}
				});
			},mils);
		}
		//初始化文件上传对象
		var first=true;
		new qq.FileUploader({
			element : document.getElementById("imageUploader"),
			action : '<dhome:url value="/system/regist?func=uploadImage&defaultCut=true"/>',
			sizeLimit : 1024 * 1024*5,
			allowedExtensions:['jpg', 'jpeg', 'png', 'gif'],
			onComplete : function(id, fileName, responseJSON) {
				$('#headPhoto').attr("src", responseJSON.defaultCutImgPath);
				$('#fileName').val(responseJSON.fileName);
				$('#proportion').val(responseJSON.proportion);
				$('#scale').attr("orgHeight",responseJSON.height);
				$('#scale').attr("orgWidth",responseJSON.width);
				$('#headPhotoEdit').attr("src", responseJSON.imgPath);
				$('#isCut').val("true");
				$('#editPhoto').show('normal');
				$('#completeBtn').show('normal');
				$('#isDefault').val("false");
				$('#uploadText').html("<fmt:message key='personalPhotoInfo.uploadFile'/>");
				updateInfo("<fmt:message key='personalPhotoInfo.uploadSuccess'/>");
			},
			messages:{
	        	typeError:"<fmt:message key='addNewPaper.upload.error.fileTypeError'/>{extensions}<fmt:message key='addNewPaper.upload.error.fileType'/>",
	        	emptyError : "<fmt:message key='addNewPaper.upload.error.fileContentError'/>{file}.",
	        	sizeError:"<fmt:message key='personal.common.photo.size'/>"
			},
	        showMessage: function(message){
	        	alert(message);
	        },
	        onProgress: function(id, fileName, loaded, total){
	        	//$('#upload_progress_bar').attr("style","width:"+Math.round((loaded/total)*100)+"%");
	        	$('#uploadText').html("<fmt:message key='personalPhotoInfo.uploadFile'/>("+Math.round((loaded/total)*100)+"%)")
	        },
	        multiple:false
		});
		//bootsrap和imgAreaSelect兼容有问题，手动把选择区域隐藏掉
		$('#myPhotoModal').on('hide', function() {
			$(".imgareaselect-outer").each(function(i, n) {
				$(n).hide();
			});
			$(".imgareaselect-selection").each(function(i, n) {
				$(n).parent().hide();
			});
		});
		//显示的时候，应该，初始化剪切对象，算好图片比例，放图片
		$('#myPhotoModal').on('shown', function() {
			proportion();
			setImgSeletor(500);
			
		});
		$("#uploadText").html("<fmt:message key='personalPhotoInfo.uploadFile'/>");
	});
	//计算图片大小，反正容器就那么大，图片应该放大到最大，然后放到容器里
	function proportion() {
		var proportion = parseFloat($('#proportion').val());
		var height = 390;
		var width = 550;
		//说明高比较长
		//公式:height:width=proportion
		//高比较长，故height=390
		//故 width=height/proportion
		if (proportion > (height/width)) {
			$('#headPhotoEdit').height(height);
			$('#headPhotoEdit').width(height / proportion);
		}
		//说明宽比较长
		//宽比较长，故 width=550
		//height=width*proportion
		else if (proportion < (height/width)) {
			$('#headPhotoEdit').height(width * proportion);
			$('#headPhotoEdit').width(width);
		} else {//长和宽一样
			$('#headPhotoEdit').height(390);
			$('#headPhotoEdit').width(550);
		}
		//计算缩放比例
		$('#scale').val(parseFloat($('#headPhotoEdit').height()/$('#scale').attr("orgHeight")));
	}
	(function refreshImg(){
		var path=$('#headPhoto').attr("src");
		if(path.indexOf("?ram=")>0){
			$('#headPhoto').attr("src",path.substr(0,path.indexOf('?ram='))+"?ram="+Math.random());
		}
		setTimeout(refreshImg,300);
	})();

	function cutFinish() {
		$("#myPhotoModal").modal('hide');
		$.ajax({
			type : "post",
			async:false,
			url : "<dhome:url value='/system/regist?func=cutImg'/>",
			data:{
				x:$('#x').val(),
				y:$('#y').val(),
				height:$('#h').val(),
				width:$('#w').val(),
				scale:$('#scale').val(),
				fileName:$('#fileName').val()
				},
			success:function(path){
				//兼容filefox,判定如果地址一样，就不刷新了，貌似
				$('#isCut').val("true");
				$('#headPhoto').attr("src",path+"?ram="+Math.random());
			}
		});
	}
	function updateInfo(str){
		var text=$('#uploadText').html();
		$('#uploadText').html(str);
		setTimeout(function(){
			$('#uploadText').html(text);
		},2000);
	}
	 if('${isSaved}'=='true'){
		 $('#message').show('slow')
			setTimeout(function(){
				$('#message').hide('slow');		
			},2000);
	}
</script>
</html>