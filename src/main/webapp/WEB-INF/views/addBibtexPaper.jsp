<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
				       <h3><fmt:message key='admin.common.addPaper.title'/></h3>
				    </div>
				    <div class="page-container">
					    <p class="small-font gray-text"><fmt:message key='admin.common.addPaper.hint'/></p>
					    <ul class="nav nav-tabs">
							<li>
							    <a href="<dhome:url value='/people/${domain}/admin/paper'/>"><i class="dicon-return">&nbsp;</i></a>
							</li>
							<li>
							    <a href="<dhome:url value='/people/${domain}/admin/paper/edit?func=paperSearch'/>"><fmt:message key='admin.common.addPaper.search'/></a>
						    </li>
							<li>
								<a href="<dhome:url value='/people/${domain}/admin/paper/edit?func=paperNew'/>"><fmt:message key='admin.common.addPaper.addMyPaper'/></a>
						    </li>
						    <li class="active">
							    <a class="common-blue" href="<dhome:url value='/people/${domain}/admin/paper/edit?func=importBibtex'/>"><b><fmt:message key='admin.common.addPaper.importBib'/></b></a>
						    </li>
					    </ul>
					    <form name="bibpaper-form" method="POST" action="<dhome:url value='/people/${domain}/admin/paper/edit?func=saveBibPaper'/>">
					    	<input type="hidden" name="papers" value="">
					      <div class="control-group">
				            <div class="controls">
				            	<div id="file-uploader-demo1" >
									<div class="qq-uploader" >
										<div class="qq-upload-drop-area"><span><fmt:message key='addBibtexPaper.dropBib'/></span></div>
										<div class="qq-upload-button" >
											<fmt:message key='addBibtexPaper.upLoadBib'/>
										</div>					
										<ul class="qq-upload-list fileList"></ul>
									</div>
								</div>
								
								<label id="remind1" style="display:none"><fmt:message key='addBibtexPaper.upLoadBibError'/></label>
								<div id="files-data-area"></div>
				            </div>
				          </div>
				          <div><ul id="bib-paper-list" class="paper-list striped" style="width:100%;"></ul></div>
					      <div class="d-top">
					          	<button name="save-bibpaper" type="button" class="btn btn-primary btn-set-middle"><fmt:message key='common.save'/></button>
					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/paper'/>"><fmt:message key='common.cancel'/></a>
					          </div>
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
	    var uploader = new qq.FileUploader({
	        element: document.getElementById('file-uploader-demo1'),
	        template: document.getElementById('file-uploader-demo1').innerHTML,
	        action: "<dhome:url value='/people/${domain}/admin/paper/edit?func=uploadBibtex'/>",
	        allowedExtensions:['bib'],
	        onComplete:function(id, fileName, data){
	        	$("#bib-paper-list").html("");
	        	$("ul.qq-upload-list.fileList li:last").prevAll().remove();
	        	if(data.status){
	        		$(".qq-upload-failed-text:last").remove();
	        		$("#paper-selectall-template").render({}).appendTo("#bib-paper-list");
		       	   	$("#paper-template").render(data.result).appendTo("#bib-paper-list");
		       	   	$("#bib-paper-list li:even").css("background","#f5f5f5");
	        	}else{
	        		var message = data.errMessage + "<a class='help_quote' title='<fmt:message key='commonBanner.help'/>' target='_blank' href='<dhome:url value='/system/index/help.html#a_14'/>'></a>";
	        		$(".qq-upload-failed-text:last").html(message);
	        	}
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
	    
	   $("#bib-paper-list li.single").live("click", function(){
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
		   $("#bib-paper-list li.single").each(function(index, element){
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
	   
	   $("#bib-paper-list li input[type=checkbox]").live("click", function(){
		   var ischecked = $(this).attr("checked");
		   if(ischecked=="checked" || ischecked){
			   $(this).removeAttr("checked");
		   }else{
			   $(this).attr("checked","checked");
		   }
	   });
	   
	   $("button[name=save-bibpaper]").click(function(){
		   var json = new Array();
		   $("#bib-paper-list li.single").each(function(){
			   var ischecked = $(this).children("input[type=checkbox]").attr("checked");
			   if(ischecked=="checked" || ischecked){
				   var paper = {
				   		uid : $(this).children("input[type=checkbox]").val(),
				   		title : $.trim($(this).children("span.title").text()),
				   		authors : $.trim($(this).children("span.authors").text()),
				   		source : $.trim($(this).children("span.source").text()),
				   		volumeIssue : $.trim($(this).children("span.volume").text()),
				   		pages : $.trim($(this).children("span.pages").text()),
				   		publishedTime : $.trim($(this).children("span.published").text()),
				   		paperURL : $.trim($(this).children("span.paper-url").text())
				   };
				   json.push(paper);
			   }
		   });
		   $("input[name=papers]").val(JSON.stringify(json));
		   $("form[name=bibpaper-form]").submit();
	   });
	});
</script>
<script type="text/html" id="paper-template">
<li class="single">
		<input type="checkbox" value="{{= uid}}" style="margin:6px 5px 0 0; float:left;">
		<span class="paper-title title">{{= title}}</span>
		<span class="paper-author authors">{{= authors}}</span>
		<span class="paper-source source">{{= source}}</span>
		<span class="paper-volume volume">{{= volumeIssue}}</span>
		<span class="paper-pages pages">{{= pages}}</span>
		<span class="paper-time published">{{= publishedTime}}</span>
		<span class="paper-source paper-url">{{= paperURL}}</span>
		<div class="clear"></div>
</li>
</script>
<script type="text/html" id="paper-selectall-template">
<li class="select-all">
		<input type="checkbox" style="margin:6px 5px 0 0; float:left;">
		<span>全选</span>
		<div class="clear"></div>
</li>
</script>
</html>