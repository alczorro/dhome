<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
	<title>${currentUser.zhName }<fmt:message key='common.dhome'/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${currentUser.zhName }<fmt:message key='common.index.title'/><jsp:include page="showAudit.jsp"/></div>
		<jsp:include page="./commonMenu.jsp">
			<jsp:param name="activeItem" value="content" />
		</jsp:include>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout p-top center">
			<div class="config-title"><h3><fmt:message key='admin.common.content.title'/></h3></div>
			<jsp:include page="adminCommonLeft.jsp"></jsp:include>
		    <div class="span9 left-b">
		       <div id="mainSpan">
			    <div class="page-header" style="margin-bottom:15px;">
			       <h3>
			       	<c:choose>
			       		<c:when test="${paper == null }">
			       			<fmt:message key='admin.common.addPaper.title'/>
			       		</c:when>
			       		<c:otherwise>
			       			<fmt:message key='addNewPaper.editPaper'/>
			       		</c:otherwise>
			       </c:choose>
			      </h3>
			    </div>
			    <div class="page-container">
			    	<p id="addPaperHintP" class="small-font gray-text"><fmt:message key='admin.common.addPaper.hint'/></p>
				    <ul class="nav nav-tabs">
						<li>
						    <a id="backToPaperList"><i class="dicon-return">&nbsp;</i></a>
						</li>
					   	<li>
						    <a id="paperSearchBtn" href="<dhome:url value='/people/${domain}/admin/paper/edit?func=paperSearch'/>"><fmt:message key='admin.common.addPaper.search'/></a>
					    </li>
					    <li class="active">
							<a class="common-blue" href="#">
			          			<b>
								    <c:choose>
							       		<c:when test="${paper == null }">
							       			<fmt:message key='admin.common.addPaper.addMyPaper'/>
							       		</c:when>
							       		<c:otherwise>
							       			<fmt:message key='addNewPaper.editMyPaper'/>
							       		</c:otherwise>
						       		</c:choose>
						       	</b>
						    </a>
					    </li>
					    <li>
						    <a id="importBibBtn" href="<dhome:url value='/people/${domain}/admin/paper/edit?func=importBibtex'/>"><fmt:message key='admin.common.addPaper.importBib'/></a>
					    </li>
				    </ul>
				    <form class="d-top form-horizontal no-bmargin" id="editPaperForm" action="<dhome:url value="/people/${domain}/admin/paper/edit"/>" method="POST">
				        <fieldset>
				        	<input name="func" type="hidden" value="save">
				        	<input name="paperId" value="${paper.id}" type="hidden">
				        	<input name="localFulltextURL" type="hidden">
				          <div class="control-group">
				          	<label class="control-label"><fmt:message key='addNewPaper.title'/></label>
				            <div class="controls">
				              <input name="title" value="${paper.title }" class="focused register-xlarge" type="text">
				              <span class="help-inline gray-text"><fmt:message key='addNewPaper.please.addPaperTitle'/></span>
				            </div>
				          </div>
				          <div class="control-group">
				            <label class="control-label"><fmt:message key='addNewPaper.authors'/></label>
				            <div class="controls">
				              <input name="authors" value="${paper.authors }" class="focused register-xlarge" type="text">
				              <span class="help-inline gray-text"><fmt:message key='addNewPaper.please.addAuthors'/></span>
				            </div>
				          </div>
				          <div class="control-group">
				            <label class="control-label"><fmt:message key='addNewPaper.source'/></label>
				            <div class="controls">
				              <input name="source" value="${paper.source }" class="focused register-xlarge" type="text">
				              <span class="help-inline gray-text"><fmt:message key='addNewPaper.please.addSource'/></span>
				            </div>
				          </div>
				          <div class="control-group">
				             <label class="control-label"><fmt:message key='addNewPaper.volume'/></label>
				            <div class="controls">
				              <input name="volumeIssue" value="${paper.volumeIssue }" class="focused register-xlarge" type="text">
				              <span class="help-inline gray-text"><fmt:message key='addNewPaper.please.volume'/></span>
				            </div>
				          </div>
				          <div class="control-group">
				             <label class="control-label"><fmt:message key='addNewPaper.pages'/></label>
				            <div class="controls">
				              <input name="pages" value="${paper.pages }" class="focused register-xlarge" type="text">
				              <span class="help-inline gray-text"><fmt:message key='addNewPaper.please.pages'/></span>
				            </div>
				          </div>
				          <div class="control-group">
				            <label class="control-label"><fmt:message key='addNewPaper.publishedTime'/></label>
				            <div class="controls">
				              <input name="publishedTime" value="${paper.publishedTime }" class="focused register-xlarge" type="text">
				              <span class="help-inline gray-text"><fmt:message key='addNewPaper.please.publishedTime'/></span>
				            </div>
				          </div>
				          <div class="control-group">
				            <label class="control-label"><fmt:message key='addNewPaper.paperURL'/></label>
				            <div class="controls">
				              <input name="paperURL" value="${paper.paperURL }" class="focused register-xlarge" type="text">
				              <span class="help-inline gray-text"><fmt:message key='addNewPaper.please.paperURL'/></span>
				            </div>
				          </div>
				          <div class="control-group">
				            <label class="control-label"><fmt:message key='addNewPaper.summary'/></label>
				            <div class="controls">
				              <textarea name="summary" class="focused register-xlarge" rows="3" cols="10">${paper.summary }</textarea>
				              <span class="help-inline gray-text"><fmt:message key='addNewPaper.please.summary'/></span>
				            </div>
				          </div>

				          <div class="control-group">
				            <label class="control-label"></label>
				            <div class="controls">
				            	<input type="hidden" name="localFulltextURL" value="">
					          <input type="hidden" name="clbId" value="0">
					          <div class="upload-fulltext maintain">
								<div class="qq-upload-button fulltext">
									<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
								</div>			
							  </div>
				            </div>
				          </div>
				          
				          <div class="control-group">
				            <label class="control-label"></label>
				            <div class="controls">
						          	<button type="submit" class="btn btn-primary btn-set-middle"><fmt:message key='common.save'/></button>
						          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/paper'/>"><fmt:message key='common.cancel'/></a>
				            </div>
				          </div>
				          
				        </fieldset>
				      </form>
				     </div>
			     </div>
		    </div>
		</div>
		<jsp:include page="commonfooter.jsp"></jsp:include>
	</div>

</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(function(){
		var paperStr = '${paper}';
		//paperStr是paper的json串，为空就是新建
		var isEdit=(paperStr!='');
		if(isEdit){
			$('#importBibBtn').hide();
			$('#paperSearchBtn').hide();
			$('#addPaperHintP').hide();
		}
		var uploader = new qq.FileUploader({
	        element: $("div.upload-fulltext")[0],
	        action: "<dhome:url value='/people/${domain}/admin/paper/edit?func=uploadFulltext'/>",
	        params: {paperId:$("input[name=paperId]").val(), clbId:$("input[name=clbId]").val()},
	        allowedExtensions:['pdf','doc','docx'],
	        onProgress: function(id, fileName, loaded, total){
	        	var progress = Math.round((loaded/total)*100);
	        	$("div#uploadText").html("<fmt:message key='addNewPaper.uploading'/>("+progress+"%)");
	        },
	        onComplete:function(id, fileName, data){
	       	   	afterUpload(data);
	        },
	        messages:{
	        	typeError:"<fmt:message key='addNewPaper.upload.error.fileTypeError'/>{extensions}<fmt:message key='addNewPaper.upload.error.fileType'/>",
	        	emptyError : "<fmt:message key='addNewPaper.upload.error.fileContentError'/>"
	        },
	        showMessage: function(message){
	        	alert(message);
	        },
	        multiple:true,
	        debug: true
	    });
		
		function afterUpload(data){
			if(data.status == true){
				var $existURL = $("a.downloadFulltext");
				var downloadURL = "<dhome:url value='/system/download/"+data.clbId+"'/>";
				if($existURL.length>0){
					$("input[name=localFulltextURL]").val(downloadURL);
					$("input[name=clbId]").val(data.clbId);
					$("div#uploadText").html("<fmt:message key='addNewPaper.updateFulltext'/>");
					$existURL.attr("href",downloadURL).prev().text("<fmt:message key='addNewPaper.update.success'/>");
				}else{
					$("input[name=localFulltextURL]").val(downloadURL);
					$("input[name=clbId]").val(data.clbId);
					$("div#uploadText").html("<fmt:message key='addNewPaper.updateFulltext'/>");
					$("div.upload-fulltext").after("<span><fmt:message key='addNewPaper.uploadSuccess'/></span><a class='downloadFulltext' href='"+downloadURL+"'><fmt:message key='addNewPaper.downloadFulltext'/></a>");		
				}
			}else{
				alert('<fmt:message key="upload.error"/>');
			}
		}
		
		$("#editPaperForm").validate({
			rules:{
				title:{required:true, maxlength:255},
				authors:{required:true, maxlength:255},
				source:{maxlength:255},
				volumeIssue:{maxlength:64},
				pages:{maxlength:128},
				publishedTime:{maxlength:64},
				paperURL:{url:true},
				summary:{maxlength:3000}
			},
			messages:{
				title:{
					required:"<fmt:message key='addNewPaper.warning.title'/>",
					maxlength:"<fmt:message key='common.inputTooLong'/>"
				},
				authors:{ 
					required:"<fmt:message key='addNewPaper.warning.authors'/>",
					maxlength:"<fmt:message key='common.inputTooLong'/>"
				},
				source:{ 
					required:"<fmt:message key='addNewPaper.warning.source'/>",
					maxlength:"<fmt:message key='common.inputTooLong'/>"
				},
				volumeIssue:{ 
					maxlength:"<fmt:message key='common.inputTooLong'/>"
				},
				pages:{
					maxlength:"<fmt:message key='common.inputTooLong'/>"
				},
				publishedTime:{
					maxlength:"<fmt:message key='common.inputTooLong'/>"
				},
				paperURL:{
					url:"<fmt:message key='addNewPaper.warning.invalidUrl'/>"
				},
				summary:{
					maxlength:"论文摘要过长"
				}
			},
			success:function(label){
				label.parents("div.control-group").removeClass("error");
				label.parents("div.control-group").addClass("success");
				label.html("<font color='green'>√</font>");
			},
			errorPlacement:function(error, element){
				element.parents("div.control-group").removeClass("success");
				element.parents("div.control-group").addClass("error");
				element.next().html("").append(error);
			}
		});
		
		$("#backToPaperList").click(function(){
			var href = window.location.href;
			if(href.indexOf("paperId")>0){
				history.go(-1);
			}else{
				window.location.href="<dhome:url value='/people/${domain}/admin/paper'/>";
			}
		});
			
		function init(){
			if(paperStr != ""){
				<c:if test="${!empty paper}">
					var lurl = "<dhome:url value='/system/download/"+${paper.clbId}+"'/>";
					if(${paper.clbId}>0){
						$("#uploadText").text("<fmt:message key='addNewPaper.updateFulltext'/>");
						$("input[name=localFulltextURL]").val(lurl);
						$("input[name=clbId]").val('${paper.clbId}');
						$("div.upload-fulltext").after("<span><fmt:message key='addNewPaper.alreadyUpload'/></span><a class='downloadFulltext' href='"+lurl+"'><fmt:message key='addNewPaper.downloadFulltext'/></a>");					
					}else{
						$("#uploadText").text("<fmt:message key='addNewPaper.uploadFulltext'/>");
					}
					if("${needFocus}" == "true"){
						$("textarea[name=summary]")[0].focus();
					}
				</c:if>
			}else{
				$("#uploadText").text("<fmt:message key='addNewPaper.uploadFulltext'/>");
			}
		}
		
		init();
	});
</script>
</html>