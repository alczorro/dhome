<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
<title><fmt:message key="instituteActivity.title"/>-<c:out value="${home.name }"/>-<fmt:message key="institueIndex.common.title.suffix"/></title>

<link href="<dhome:url value="/resources/third-party/datepicker/sample-css/page.css"/>" rel="stylesheet" type="text/css" />
<link href="<dhome:url value="/resources/third-party/datepicker/css/dp.css"/>" rel="stylesheet" type="text/css" />

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
		<h1 class="dark-green"><c:out value="${home.name }"/></h1>
	</div>
	<div class="container">
		<div class="row-fluid">
			<div class="span12">
				<div class="panel abs-top some-ipad">
				    <h3 class="dark-green abs-top"><fmt:message key="instituteActivity.event.add"/></h3>
				    <form id="activityForm" class="form-horizontal abs-top" action="<dhome:url value='/institution/${home.domain }/scholarevent.html?func=submit'/>" method="post">
				    	<div class="control-group">
				    	  <input type="hidden" name="eventId" value="${event.id }"/>
				    	  <input type="hidden" name="institutionId" value="${home.institutionId }"/>
			              <label class="control-label"><strong><fmt:message key="instituteActivity.event.add.activityTitle"/>：</strong></label>
			              <div class="controls">
			                <input name="title" value="<c:out value="${event.title }"/>" id="title" type="text" class="register-xlarge" placeholder="<fmt:message key="instituteActivity.event.add.titlePlaceHolder"/>">
			              </div>
			            </div>
			            <div class="control-group">
			              <label class="control-label"><strong><fmt:message key="instituteActivity.event.add.activityTime"/>：</strong></label>
			              <div class="controls">
			                 	<input  value="${startDate }" id="startTime" name="startTime" type="text" class="i-small" placeholder="<fmt:message key="instituteActivity.event.add.startTime"/>">
			                 	<select  class= "i-small abs-left" id="startCurrentTime" name='startCurrentTime'></select>
			                 	<fmt:message key="instituteActivity.event.add.to"/><input  value="${endDate }" id="endTime" name="endTime" type="text" class="i-small abs-left" placeholder="<fmt:message key="instituteActivity.event.add.endTime"/>">
			                 	<select class= "i-small abs-left" id="endCurrentTime" name='endCurrentTime'></select>
			              </div>
			              <div class="controls">
			                 	<span id="startTimeMessage" class="d-ismall error"></span>
			                 	<span id="startCurrentTimeMessage" class="error d-ismall abs-left"></span> &nbsp;&nbsp;&nbsp;&nbsp;
			                 	<span id="endTimeMessage"class="d-ismall error"></span>
			                 	<span id="endCurrentTimeMessage" class="error d-ismall abs-left"></span>
			              </div>
			            </div>
			            <div class="control-group">
			              <label class="control-label"><strong><fmt:message key="instituteActivity.event.add.reporter"/>：</strong></label>
			              <div class="controls">
			                 <input type="text" value="<c:out value="${event.reporter }"/>" name="reporter" class="register-xlarge" placeholder="<fmt:message key="instituteActivity.event.add.reporter"/>">
			              </div>
			            </div>
			            <div class="control-group">
			              <label class="control-label"><strong><fmt:message key="instituteActivity.event.add.place"/>：</strong></label>
			              <div class="controls">
			                 <input name="place" id="place" type="text" value="<c:out value="${event.place }"/>" class="register-xlarge" placeholder="<fmt:message key="instituteActivity.event.add.place"/>">
			              </div>
			            </div>
			            <div class="control-group" style="width:95%;">
			              <label class="control-label"><strong><fmt:message key="instituteActivity.event.add.introduction"/>：</strong></label>
			              <div class="controls">
			                 <textarea id="introduction" ><c:out value="${event.introduction }"/></textarea>
			              	<input type="hidden" id="finalIntroduction" name="introduction"/>
			              </div>
			            </div>
			            <div class="control-group">
			              <label class="control-label"><strong><fmt:message key="instituteActivity.event.add.uploadActivityImage"/>：</strong></label>
			             <div class="controls">
				              		<div>
									    <div class="institute-header-container">
									    	<div class="institute-header">
												<img id="headPhoto" src="<dhome:img imgId="${event.logoId }" imgName="dhome-activity.png"/>" /> 
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
										<input type="hidden" name="height" value="200" id="h"><br /> 
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
			              <div class="controls">
			                 <button type="submit" class="btn btn-primary"><fmt:message key="common.save"/></button>
			                 <input type="button" onclick="cancel()" class="btn" value="<fmt:message key="common.cancel"/>"/>
			              </div>
			            </div>
					</form>
				</div>
			</div>
		</div>
	</div>
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
	<input type="hidden" id="contextPath" value="<%=request.getContextPath()%>"/>
	<jsp:include page="../commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="../commonheader.jsp"></jsp:include>
<script type="text/javascript"	src="<dhome:url value="/resources/scripts/upload/fileuploader.js"/>"></script>
<script	src="<dhome:url value="/resources/scripts/jquery/jquery.imgareaselect.pack.js"/>"	type="text/javascript"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/base.js" />"></script>
<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
<dhome:IsMainTain reverse="true">
	<script type="text/javascript" src="<dhome:url value="/resources/third-party/ueditor/editor_config.js"/>"></script>
</dhome:IsMainTain>
<dhome:IsMainTain>
	<script type="text/javascript" src="<dhome:url value="/resources/third-party/ueditor/editor_config_maintain.js"/>"></script>
</dhome:IsMainTain><script type="text/javascript" src="<dhome:url value="/resources/third-party/ueditor/editor_all.js"/>"></script>
<link rel="stylesheet" href="<dhome:url value="/resources/third-party/ueditor//themes/default/ueditor.css"/>">

<script type="text/javascript">
function initCurrentDate(){
	function getOption(i){
		var result="";
		var time=i<10?"0"+i+":00":i+":00"
		result+="<option value='"+time+"'>"+time+"</option>";
		time=i<10?"0"+i+":30":i+":30"
		result+="<option value='"+time+"'>"+time+"</option>";
		return result;
	}
	var str="";
	for(var i=8;i<24;i++){
		str+=getOption(i);
	}
	for(var i=0;i<8;i++){
		str+=getOption(i);
	}
	$("#startCurrentTime").html(str);
	$("#startCurrentTime").val('${startTime}')
	$("#endCurrentTime").html(str);
	$("#endCurrentTime").val('${endTime}');
	if('${event.id}'==''){
		var date=new Date();
		var time=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		$("#startTime").val(time);
		$("#endTime").val(time);
		$("#startCurrentTime").val('08:00');
		$("#endCurrentTime").val('09:00');
	}
}
	$(document).ready(
			function() {
				//baiduEditor
		        var option = {
		        	maximumWords:3000 
		        };
				var editor = new baidu.editor.ui.Editor(option);
    			editor.render("introduction");
    			var isSource=false;
    		    //编辑器切换源码监听
    		    editor.addListener("sourceModeChanged",function(type,mode){
    		   		isSource=mode;
    			});
				//date
				initCurrentDate();
				
				//validate
				$.validator.addMethod("lastThan", function(value, element){
					var startTime=$('#startTime').val()+" "+$('#startCurrentTime').val();
					startTime=new Date(Date.parse(startTime.replace(/-/g, "/")));
					var endTime=$('#endTime').val()+" "+$('#endCurrentTime').val();
					endTime=new Date(Date.parse(endTime.replace(/-/g, "/")));
					return this.optional(element)||(startTime.getTime()<endTime.getTime());
    			
    			}, "<fmt:message key='instituteActivity.event.add.lastThanMessage'/>");
				$.validator.addMethod("contentLength", function(value, element){
			    	return this.optional(element)||(editor.getContentTxt().length<=3000);
			    }, "");
				function dateType(value){
					function deletePad(str){
						var regex=/^0[1-9]$/;
						if(regex.test(str)){
							return str.substr(1);
						}else{
							return str;
						}
					}
					var regex=/^[0-9]{4}-((0[1-9])|(1[0-2])|[1-9])-((0[1-9])|([1-9])|([1-2][0-9])|(3[0-1]))$/;
					if(regex.test(value)){
						var str=value.split("-");
						var year=parseInt(deletePad(str[0]));
						var month=parseInt(deletePad(str[1]));
						var day=parseInt(deletePad(str[2]));
						var actualDate=new Date(Date.parse(value.replace(/-/g, "/")));
						return (year==actualDate.getFullYear()&&month==(actualDate.getMonth()+1)&&day==(actualDate.getDate()));
					}else{
						return false;
					}
					
				}
				$.validator.addMethod("dateType", function(value, element){
						return this.optional(element)||dateType(value);
			    }, "<fmt:message key='instituteActivity.event.add.date'/>");
				$("#activityForm").validate({
					 submitHandler:function(form){
				    	 if(isSource){
				    		 editor.execCommand("source");
				    	 }
				    	$('#finalIntroduction').val(editor.getContent());
				    	form.submit();
				      },
					 rules: {
						 title:{maxlength:255,required:true},
						 reporter:{maxlength:255,required:true},
						 place:{maxlength:255,required:true},
						 startTime:{required:true,dateType:true},
						 endTime:{required:true,dateType:true,lastThan:true},
						 introduction:{contentLength:true}
					 },
					 messages:{
						 title:{
							 maxlength:"<fmt:message key='instituteActivity.event.add.titleMax'/>",
							 required:"<fmt:message key='instituteActivity.event.add.titleRequired'/>"
						 },
						 reporter:{
							 maxlength:"<fmt:message key='instituteActivity.event.add.reporterMax'/>",
							 required:"<fmt:message key='instituteActivity.event.add.reporterRequired'/>"
						 },
						 place:{
							 maxlength:"<fmt:message key='instituteActivity.event.add.placeMax'/>",
							 required:"<fmt:message key='instituteActivity.event.add.placeRequired'/>"
						 },
						 startTime:{
							 required:"<fmt:message key='instituteActivity.event.add.startTimeRequired'/>",
						},
						 endTime:{ 
							 required:"<fmt:message key='instituteActivity.event.add.endTimeRequired'/>",
						}
					 },
					 errorPlacement:function(error, element){
						var name=$(element).attr("name");
						if(name=='startTime'){
							error.appendTo($('#startTimeMessage'));
						}else if(name=='endTime'){
							error.appendTo($('#endTimeMessage'));
						}else{
							error.appendTo(element.parent());
						}
					}
				});
				//===========date=========
				var imgUrl='<dhome:url value="/resources/third-party/datepicker/sample-css/cal.gif"/>';
				imgUrl='<img class="picker" align="middle" src="'+imgUrl+'" alt="">';
				$("#startTime").datepicker({ picker: imgUrl,applyrule:rule});
				$("#endTime").datepicker({ picker: imgUrl,applyrule:rule});
				/**
					return boolean resulr?show:unshow
				*/
				function rule(id){
					var dateValue=$('#'+id).val();
					return dateType(dateValue)
				}
				//===========end==========
				var pageHeight = $("html").height();
				var documentHeight = window.screen.height;
				var maxHeight = pageHeight > documentHeight ? pageHeight
						: documentHeight;
				$(".cover-wholepage").css({
					"height" : maxHeight
				});
				$(".cover-wholepage").click(function() {
					$(this).hide();
				});
				
				//auditStatus
				$('#auditStatus').hover(function(){
					$(this).tooltip('toggle');
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
						$('#w').val(smaller*0.5);
						$('#h').val(smaller*0.5);
						setTimeout(function(){
							$('#headPhotoEdit').imgAreaSelect({
								x1 : 0,
								y1 : 0,
								x2 : smaller*0.5,
								y2 : smaller*0.5,
								aspectRatio : '200:200',
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
				$('#headPhoto').attr("src",path+"?ram="+Math.random());
				$("#isCut").val("true");
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
		window.location.href="<dhome:url value='/institution/${home.domain }/scholarevent.html'/>"
	}
</script>
</html>