<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
<title><fmt:message key="institute.common.edit"/>-${home.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../commonheaderCss.jsp"></jsp:include>
	<link href="<dhome:url value="/resources/css/fileuploader.css"/>"
	rel="stylesheet" type="text/css">
<link href="<dhome:url value="/resources/css/imgareaselect-default.css"/>"
	rel="stylesheet" type="text/css">
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<h1 class="dark-green">${home.name }</h1>
	</div>
	<div class="container">
		<div class="row-fluid">
			<div class="span12">
				<div class="panel abs-top some-ipad">
					<h3 class="dark-green abs-top"><fmt:message key="instituteActivity.index.setting"/></h3>
					<form id="configForm" class="form-horizontal abs-top" action="<dhome:url value="/institution/${home.domain }/submit/index.html"/>" method="post">
					    <fieldset>
					    	<div class="control-group">
				              <label class="control-label"><strong><fmt:message key="instituteActivity.index.setting.institutionName"/>：</strong></label>
				              <div class="controls">
				                ${home.name }
				              </div>
				            </div>
				            <div class="control-group">
				              <label class="control-label"><strong><fmt:message key="instituteActivity.index.setting.introduction"/>：</strong></label>
				              <div class="controls">
				                <textarea name="introduction" class="big-large" rows="6">${home.introduction }</textarea>
				              </div>
				            </div>
				            <div class="control-group">
				              <label class="control-label"><strong><fmt:message key="instituteActivity.index.setting.uploadLogo"/>：</strong></label>
				              <div class="controls">
				              		<div>
									    <div class="institute-header-container">
									    	<div class="institute-header">
												<img id="headPhoto" src="<dhome:img imgId="${home.logoId }" imgName="dhome-institute.png"/>" /> 
											</div>
											<div class="d-center d-top">
												<a id='editPhoto' href="#myPhotoModal" data-toggle="modal" style="display:none"><fmt:message key='personalPhotoInfo.cutPhoto'/></a>
											</div>
										</div>
										
										<div class="float-left x-ileft d-large-top">
										<dhome:IsMainTain>
											<font color="red"><fmt:message key='common.clb.image.ismaintain'/></font>
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
										<input type="hidden" name="x" id="x" value="0"><br /> 
										<input type="hidden" name="y" id="y" value="0"><br /> 
										<input type="hidden" name="height" value="150" id="h"><br /> 
										<input type="hidden" name="width" value="200" id="w"> 
										<input type="hidden" value="${fileName }" id="fileName" name="fileName" /> 
										<input type="hidden" id="isCut" name="isCut" value="false"> 
										<input type="hidden" id="isDefault" name="isDefault" value="true">
										<input type="hidden" id="proportion">
										<input type="hidden" name="scale" id="scale">
									</div>
				              </div>
				            </div>
				            
				            <div class="control-group">
				              <label class="control-label"></label>
				              <div class="controls">
				              		<div>
										<button id=completeBtn type="submit" class="btn btn-primary"><fmt:message key='common.save'/></button>
										<input type="button" onclick="cancel();" value="<fmt:message key='common.cancel'/>"  class="btn">
										<span style="display:none" id="message"><font color='red'><fmt:message key='personalPhotoInfo.saveSuccess'/></font></span>
									</div>
				              </div>
				            </div>
				            
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
									<img src='<dhome:img imgId="${home.logoId }"/>'  height="390" width="550" id="headPhotoEdit" />
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
	<jsp:include page="../commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="../commonheader.jsp"></jsp:include>
<script type="text/javascript"	src="<dhome:url value="/resources/scripts/upload/fileuploader.js"/>"></script>
<script	src="<dhome:url value="/resources/scripts/jquery/jquery.imgareaselect.pack.js"/>"	type="text/javascript"></script>
<script type="text/javascript"	src="<dhome:url value="/resources/scripts/base.js" />"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#configForm").validate({
			 rules: {
				 introduction:{maxlength:200} 
			 },
			 messages:{
				 introduction:"<fmt:message key='instituteActivity.index.setting.introductionMaxLength'/>"
			 }
		});

		//初始化文件上传对象
		var first=true;
		new qq.FileUploader({
			element : document.getElementById("imageUploader"),
			action : '<dhome:url value="/system/regist?func=uploadImage"/>',
			sizeLimit : 1024 * 1024*5,
			allowedExtensions:['jpg', 'jpeg', 'png', 'gif'],
			onComplete : function(id, fileName, responseJSON) {
				$('#headPhotoEdit').attr("src", responseJSON.imgPath);
				$('#headPhoto').attr("src", responseJSON.imgPath);
				$('#fileName').val(responseJSON.fileName);
				$('#proportion').val(responseJSON.proportion);
				$('#scale').attr("orgHeight",responseJSON.height);
				$('#scale').attr("orgWidth",responseJSON.width);
				$('#headPhotoEdit').attr("src", $('#headPhoto').attr("src"));
				$('#isCut').val("false");
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
			setImgSelector(500);
		});
		function setImgSelector(mils){
			var height=($('#headPhotoEdit').height());
			var width=($('#headPhotoEdit').width());
			var smaller=height<width?height:width;
			$('#x').val(0);
			$('#y').val(0);
			$('#w').val(smaller*0.8);
			$('#h').val(smaller*0.6);
			setTimeout(function(){
				$('#headPhotoEdit').imgAreaSelect({
					x1 : 0,
					y1 : 0,
					x2 : smaller*0.8,
					y2 : smaller*0.6,
					aspectRatio : '4:3',
					handles : true,
					onSelectChange : function(img, selection) {
						$('#x').val(selection.x1);
						$('#y').val(selection.y1);
						$('#w').val(selection.width);
						$('#h').val(selection.height);
					}
				});
			},mils);
		}
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

	function cutFinish() {
		$("#myPhotoModal").modal('hide');
		$.ajax({
			type : "post",
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
	function cancel(){
		window.location.href="<dhome:url value='/institution/${home.domain}/index.html'/>"
	}
</script>
</html>