<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<html lang="en">
	<head>
		<title>机构成员</title>
		<meta name="description" content="dHome" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/css/dp.css'/>"/>
	</head>

	<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
		<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
		
		<div class="container">
			<jsp:include page="../leftMenu.jsp"></jsp:include>
			<div class="ins_backend_rightContent">
				<ul class="nav nav-tabs">
					<jsp:include page="../../commonMemberBackend.jsp"><jsp:param name="activeItem" value="job" /> </jsp:include>
<!-- 				     <li class="search"> -->
<!-- 				    	<form class="bs-docs-example form-search"> -->
<%-- 				            <input id='memberKeyword' type="text" class="input-medium search-query" placeholder="请输入员工姓名" value="<c:out value="${condition.keyword }"/>"> --%>
<!-- 				            <button id="searchMemberBtn" type="button" class="btn">搜索</button> -->
<!-- 				        </form> -->
<!-- 				    </li> -->
 			 </ul> 
				<h4 class="detail"  style="margin-top:20px;">更改申请表模板</h4>
				
				
			<form id="editForm" class="form-horizontal">
				 <div class="control-group">
	       			<label class="control-label">上传模板：</label>
	       			<div class="controls">
						<div id="fileUploader">
							<div class="qq-uploader">
								<div class="qq-upload-button">
									<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
								</div>
								<ul class="qq-upload-list fileList"></ul>
							</div>
						</div>
						
<!-- 						<table id="clbTable"> -->
<!-- 							<tbody id="clbContent"> -->
							
<!-- 							</tbody> -->
<!-- 						</table> -->
						
						<span id="fileNameSpan"></span>
						<span id="fileUploadProcess"></span>
<%-- 						<input type="hidden" name="clbId" id="clbId" value="${empty paper?0:paper.clbId }"/> --%>
<%-- 						<input type="hidden" name="originalFileName" id="fileName" value="<c:out value="${paper.originalFileName }"/>"/> --%>
<%-- 						<a id="fileRemove" style="display:${empty paper||paper.clbId==0?'none':'inline' }">删除</a> --%>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls">
<!-- 	         			<input type="button" id="saveBtn" value="提交" class="btn btn-primary"/> -->
	         			<a class="btn" href="/institution/${domain}/backend/job/list/1">取消</a>
	       			</div>
	       		</div>
			</form>
				
			</div>
		</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	
<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
	<script>
		$(document).ready(function(){
			
			//上传文件
			new qq.FileUploader({
				element : document.getElementById('fileUploader'),
				action :'<dhome:url value="/institution/${domain}/backend/job/upload"/>',
				sizeLimit : 20*1024 * 1024,
				allowedExtensions:['ftl'],
				onComplete : function(id, fileName, data) {
					if(!data){
						return false;
					}
					$('#fileUploadProcess').empty();
					if(data.success){
						$('#fileNameSpan').html(fileName);
						alert("模板上传成功！");
// 						$('#fileName').val(fileName)
// 						$('#clbId').val(data.data);
// 						$('#fileRemove').show();
					}else{
						alert("系统维护中，暂不能添加附件");
					}
					
				},
				messages:{
		        	typeError:"请上传ftl文件",
		        	emptyError:"请不要上传空文件",
		        	sizeError:"大小超过20M限制"
		        },
		        showMessage: function(message){
		        	alert(message);
		        },
		        onProgress: function(id, fileName, loaded, total){
		        	$('#fileNameSpan').html(fileName);
		        	$('#fileUploadProcess').html("("+Math.round((loaded/total)*100)+"%)");
		        },
		        multiple:false
			});
			
// 			$('#saveBtn').on('click',function(){
// // 				if($('input:radio[name="checkItem"]:checked').val()==null){
// // 					alert("请选择学生！");
// // 					return false;
// // 				}
// 				if($('#editForm').valid()){
// 					var data=$('#editForm').serialize();
// // 					var tid=$('input:radio[name="checkItem"]:checked').val();
// // 					data +='&studentId='+tid;
					
// 					$.post('/institution/${domain}/backend/job/save',data).done(function(data){ 
// 						if(data.success){
// 							window.location.href='/institution/${domain}/backend/job/list/1';
// 						}else{
// 							alert(data.desc);
// 						}
// 					});
// 				}
// 			});
// 			//删除文件 
// 			$('#fileRemove').on('click',function(){
// 				$('#fileName').val('');
// 				$('#clbId').val('0');
// 				$('#fileNameSpan').empty();
// 				$(this).hide();
// 			})
// 			$('#editForm').validate({
// 				submitHandler:function(form){
// 					form.submit();
// 				},
// 			    rules:{
// 			    	startTime:{required:true},
// 			    	endTime:{required:true},
// 			    },
// 				messages:{
// 			    	startTime:{required:'请选择发放开始时间.'},
// 			    	endTime:{required:'请选择发放结束时间.'}
// 				},
// 			    errorPlacement: function(error, element){
// 					 var sub="_error_place";
// 					 var errorPlaceId="#"+$(element).attr("name")+sub;
// 					 	$(errorPlaceId).html("");
// 					 	error.appendTo($(errorPlaceId));
// 				}
// 			});
			
			
			$('#memberMenu').addClass('active');
			
			
		
			
		}); 
	</script>
	
</html>