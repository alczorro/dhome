<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
<title><fmt:message key="config.common.title"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<dhome:url value="/resources/css/fileuploader.css"/>"
	rel="stylesheet" type="text/css">
<link href="<dhome:url value="/resources/css/imgareaselect-default.css"/>"
	rel="stylesheet" type="text/css">
<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>
<body class="dHome-boarding" data-offset="50" data-target=".subnav"
	data-spy="scroll">
	<jsp:include page="commonBanner.jsp" flush="true"/>
	<div class="navbar boarding">
		<div class="navbar-inner" style="filter: none;">
			<div class="container">
				<!-- <a class="dhome-logo-black" href="<dhome:url value=""/><dhome:key value="deploy_subfix"/>"> </a>  -->
				<div class="progress-bar four">
					<span><fmt:message key="config.common.step1"/></span> <i></i> <i></i> <i></i> <i></i> <i></i> <span><fmt:message key="config.common.step5"/></span>
				</div>
			</div>

		</div>
		<!-- /navbar-inner -->
	</div>
	<div class="container narrow">
		<div class="row-fluid">
			<div class="span12 dhome-layout">
				<div class="page-header x-left">
					<h2><fmt:message key="configPhoto.title"/></h2>
				</div>
				<div class="d-left header-container">
					<div class="header-img">
						<img id="headPhoto"	src="<dhome:url value="/resources/images/dhome-head.png"/>" /> 
					</div>
					<div class="clear"></div>
					
					<div class="d-center x-top">
						<a id='editPhoto' href="#myPhotoModal" data-toggle="modal" style="display:none"><fmt:message key="configPhoto.editPhoto"/></a>
					</div>
				</div>
				
				<div class="float-left x-sleft d-large-top">
					<dhome:IsMainTain>
						<font color="red"><fmt:message key='common.clb.imageinfo.regist.ismaintain'/></font>
					</dhome:IsMainTain>
					<div id="imageUploader" class="d-large-btn maintain">
						<div class="qq-uploader">
							<div class="qq-upload-button">
								<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
							</div>
							<ul class="qq-upload-list fileList"></ul>
						</div>
					</div>
				</div>
				<div class="clear"></div>
				<form action="<dhome:url value="/system/regist?func=complete"/>" method="post">
					<fieldset>
						<input type="hidden" name="fromRegist" value="true">
						<div class="form-actions clear-b x-left d-top">
							<button id=completeBtn type="submit"
								class="btn btn-primary btn-large btn-set-large"><fmt:message key="configPhoto.save"/></button>
						</div>
							<input type="hidden" name="x" id="x" value="0"><br /> 
							<input type="hidden" name="y" id="y" value="0"><br /> 
							<input type="hidden" name="height" value="213" id="h"><br /> 
							<input type="hidden" name="width" value="201" id="w"> 
							<input type="hidden" id="fileName" name="fileName" /> 
							<input type="hidden" id="isCut" name="isCut" value="false"> 
							<input type="hidden" id="isDefault" name="isDefault" value="true">
							<input type="hidden" id="proportion">
							<input type="hidden" name="scale" id="scale">
							
					</fieldset>
				</form>
				<div id="myPhotoModal" class="modal hide fade">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">×</button>
						<h3><fmt:message key="configPhoto.editPhoto"/></h3>
					</div>
					
					<div class="modal-body">
						<div class="control-group">
							<div class="controls">
								<img height="390" width="550" id="headPhotoEdit" />
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<a href="#" class="btn" data-dismiss="modal"><fmt:message key="common.cancel"/></a> <a
							href="javascript:cutFinish()" class="btn btn-primary"><fmt:message key="common.confirm"/></a>
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="commonfooter.jsp"></jsp:include>
	</div>

</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/upload/fileuploader.js"/>"></script>
<script
	src="<dhome:url value="/resources/scripts/jquery/jquery.imgareaselect.pack.js"/>"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		(function refreshImg(){
			var path=$('#headPhoto').attr("src");
			if(path.indexOf("?ram=")>0){
				$('#headPhoto').attr("src",path.substr(0,path.indexOf('?ram='))+"?ram="+Math.random());
			}
			setTimeout(refreshImg,300);
		})();
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
					}
				});
			},mils);
		}
		//初始化文件上传对象
		new qq.FileUploader({
			element : document.getElementById("imageUploader"),
			action : '<dhome:url value="/system/regist?func=uploadImage&defaultCut=true"/>',
			sizeLimit : 1024 * 1024*5,
			allowedExtensions:['jpg', 'jpeg', 'png', 'gif'],
			onComplete : function(id, fileName, responseJSON) {
				$('#headPhotoEdit').attr("src", responseJSON.imgPath);
				$('#headPhoto').attr("src", responseJSON.defaultCutImgPath);
				$('#fileName').val(responseJSON.fileName);
				$('#proportion').val(responseJSON.proportion);
				$('#scale').attr("orgHeight",responseJSON.height);
				$('#scale').attr("orgWidth",responseJSON.width);
				$('#isCut').val("true");
				$('#editPhoto').show('normal');
				$('#completeBtn').show('normal');
				$('#isDefault').val("false");
				$('#uploadText').html("<fmt:message key='configPhoto.uploadFile'/>");
				updateInfo("<fmt:message key='configPhoto.uploadSccess'/>");
			},
			messages:{
	        	typeError:"<fmt:message key='configPhoto.upload.only'/>{extensions}<fmt:message key='configPhoto.upload.type'/>",
	        	emptyError : "<fmt:message key='configPhoto.upload.errorEmpty'/>{file}.",
	        	sizeError:"<fmt:message key='personal.common.photo.size'/>"
	        },
	        showMessage: function(message){
	        	alert(message);
	        },

	        onProgress: function(id, fileName, loaded, total){
	        	$('#uploadText').html("<fmt:message key='configPhoto.uploadFile'/>("+Math.round((loaded/total)*100)+"%)");
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
</script>
</html>