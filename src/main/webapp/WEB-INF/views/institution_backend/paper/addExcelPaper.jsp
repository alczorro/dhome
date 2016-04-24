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

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">

	<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
		<div class="container">
			<jsp:include page="../leftMenu.jsp"></jsp:include>
			<div class="ins_backend_rightContent">
				<ul class="nav nav-tabs">
					<li>&nbsp;&nbsp;&nbsp;</li>
					<li>
					    <a href="#">论文 ${page.allSize }</a>
					</li>
					<li>
					    <a href="<dhome:url value="/institution/${domain }/backend/paper/create?returnPage=${page.currentPage }"/>">+ 添加论文</a>
				    </li>
				   	<li class="active">
					    <a href="#">批量导入</a>
				    </li>
				    <li class="search">
				    	<form class="bs-docs-example form-search">
				            <input id='paperKeyword' type="text" class="input-medium search-query" value="<c:out value="${condition.keyword }"/>">
				            <button id="searchPaperBtn" type="button" class="btn">搜索</button>
				        </form>
				    </li>
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
				          <div><ul id="excel-paper-list" class="paper-list striped" style="width:100%;"></ul></div>
					      <div class="d-top">
					          	<button name="save-excelpaper" type="button" class="btn btn-primary btn-set-middle"><fmt:message key='common.save'/></button>
					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/paper'/>"><fmt:message key='common.cancel'/></a>
					          </div>
					    </form>
					   </div>
				  </div>
<%-- 		<jsp:include page="commonfooter.jsp"></jsp:include> --%>
</body>
<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
<script type="text/javascript">
	$(document).ready(function(){
	    var uploader = new qq.FileUploader({
	        element: document.getElementById('file-uploader-demo1'),
	        template: document.getElementById('file-uploader-demo1').innerHTML,
	        action: "<dhome:url value='/institution/${domain}/backend/paper?func=uploadExceltex'/>",
	        allowedExtensions:['xls'],
	        onComplete:function(id, fileName, data){
	        	$("#excel-paper-list").html("");
	        	$("ul.qq-upload-list.fileList li:last").prevAll().remove();
// 	        	if(data.status){
	        		$(".qq-upload-failed-text:last").remove();
// 	        		$("#paper-selectall-template").render({}).appendTo("#excel-paper-list");
		       	   	$("#paper-template").render(data.result).appendTo("#excel-paper-list");
		       	   	$("#excel-paper-list li:even").css("background","#f5f5f5");
// 	        	}else{
// 	        		var message = data.errMessage + "<a class='help_quote' title='<fmt:message key='commonBanner.help'/>' target='_blank' href='<dhome:url value='/system/index/help.html#a_14'/>'></a>";
// 	        		$(".qq-upload-failed-text:last").html(message);
// 	        	}
	        	var $selectAll = $("li.select-all");
	        	if($selectAll.length>0){
	        		$selectAll.eq(0).click();
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
	    
	   $("#excel-paper-list li.single").live("click", function(){
		   var ischecked = $(this).children("input[type=checkbox]").attr("checked");
		   if(ischecked=="checked" || ischecked){
			   $(this).children("input[type=checkbox]").removeAttr("checked");
			   $(this).css({"background":getOrgBackground($(this))});
			   $("li.select-all").find("input[type=checkbox]").removeAttr("checked");
		   }else{
			   $(this).children("input[type=checkbox]").attr("checked","checked");
			   $(this).css({"background":"#ffffbb"});
			   checkIfAllSelected();
		   }
	   }).live("mouseover", function(){
		   $(this).css({"background":"#f5f5ff"});
	   }).live("mouseout", function(){
		   $(this).css({"background":getOrgBackground($(this))});
	   });
	   
	   $("li.select-all").live("click",function(){
		  var ischecked = $(this).find("input[type=checkbox]").attr("checked");
		  var $lilist = $("li.single").find("input[type=checkbox]");
		  if(ischecked=="checked" || ischecked){
			  $(this).find("input[type=checkbox]").removeAttr("checked");
			  $lilist.removeAttr("checked");
		  }else{
			  $(this).find("input[type=checkbox]").attr("checked","checked");
			  $lilist.attr("checked","checked");
		  }
	   });
	   
	   function checkIfAllSelected(){
		   var selectall = true;
		   $("#excel-paper-list li.single").each(function(index, element){
			   var ischecked = $(element).find("input[type=checkbox]").attr("checked");
			   if(!(ischecked=="checked" || ischecked)){
				   selectall = false;
				   return;
			   }
		   });
		   
		   if(selectall){
			   $("li.select-all").find("input[type=checkbox]").attr("checked","checked");
		   }else{
			   $("li.select-all").find("input[type=checkbox]").removeAttr("checked");
		   }
	   }
	   
	   function getOrgBackground($target){
		   var checked = $target.children("input[type=checkbox]").attr("checked");
		   if(checked=="checked" || checked){
			   return "#ffffbb";
		   }
		   var index = $target.index();
		   return (index%2==0)?"#f5f5f5":"#FFFFFF";
	   }
	   
	   $("#excel-paper-list li input[type=checkbox]").live("click", function(){
		   var ischecked = $(this).attr("checked");
		   if(ischecked=="checked" || ischecked){
			   $(this).removeAttr("checked");
		   }else{
			   $(this).attr("checked","checked");
		   }
	   });
	   
	   $("button[name=save-excelpaper]").click(function(){
		   var json = new Array();
		   $("#excel-paper-list li.single").each(function(){
			   var ischecked = $(this).children("input[type=checkbox]").attr("checked");
			   if(ischecked=="checked" || ischecked){
				   var paper = {
				   		title : $.trim($(this).children("span.title").text()),
				   		author : $.trim($(this).children("span.author").text()),
				   		departName : $.trim($(this).children("span.departName").text()),
				   		publicationYear : $.trim($(this).children("span.published").text()),
				   		pages : $.trim($(this).children("span.pages").text()),
				   };
				   json.push(paper);
			   }
		   });
		   $("input[name=papers]").val(JSON.stringify(json));
		   $("form[name=excelpaper-form]").submit();
	   });
	});
</script>
<script type="text/html" id="paper-template">
<li class="single">
		<input type="checkbox" value="{{= id}}" style="margin:6px 5px 0 0; float:left;">
		<span class="paper-title title">{{= title}}</span>
		<span class="paper-title departName">{{= departName}}</span>
		<span class="paper-author author">{{= author}}</span>
		<span class="paper-pages pages">{{= startPage}}-{{= endPage}}</span>
		<span class="paper-time published">{{= publicationYear}}</span>
		<div class="clear"></div>
</li>
</html>