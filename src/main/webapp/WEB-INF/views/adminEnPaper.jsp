<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title><fmt:message key='common.paper.title'/>-${titleUser.zhName }<fmt:message key='common.index.title'/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">

  <jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${name}<fmt:message key='common.index.title'/><jsp:include page="showAudit.jsp"/></div>
		<jsp:include page="./commonMenu.jsp">
			<jsp:param name="activeItem" value="content" />
		</jsp:include>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title"><h3><fmt:message key='admin.common.content.title'/></h3></div>
			<jsp:include page="adminCommonLeft.jsp"></jsp:include>
		    <div class="span9 left-b">
		    	<div id="mainSpan">
				    <div class="page-header">
					    <h3><fmt:message key='common.en.paper.title'/>
					    <span class="publish">							   		
					    	<c:if test="${status=='hide' }">
							(<fmt:message key='common.publish.notdo'/> <a class="btn btn-mini" href="<dhome:url value="/people/${domain}/admin/en_paper/changeStatus"/>"><fmt:message key='common.publish.do'/></a>)
							</c:if>
							<c:if test="${status=='show' }">
							(<fmt:message key='common.publish.done'/> <a class="btn btn-mini" href="<dhome:url value="/people/${domain}/admin/en_paper/changeStatus"/>"><fmt:message key='common.publish.undo'/></a>)
							</c:if>
						</span>
					    <span class="publish source">
					    	论文来源:							   		
					    	<input type="radio" name="paperLib" value="1" <c:if test="${flag!=2 }">checked="checked"</c:if>/> 通用论文库
							<input type="radio" name="paperLib" value="2" <c:if test="${flag==2 }">checked="checked"</c:if>/> IAP专用论文库
						</span>
<%-- 					    <a id="editPapers" href="<dhome:url value='/people/${domain}/admin/commonPaper'/>" class="float-right"><i class="icon-edit abs-top"></i><span class="common-link">编辑</span></a></h3> --%>
					</div>
			   
				    <div class="page-container" id="commonPapers">
<%-- 				    <c:when test="${papers!=null && papers.size()>0}"> --%>
				    <%-- <span class="small-font gray-text"><fmt:message key="adminPaper.sortable.tooltip" /></span> --%>
				    <ol id ="sortablePaper" class="commonPaper">
				    	<c:forEach items="${papers }" var="paper">
				    		<c:if test="${paper.title !=null && paper.title!='' }">
					        <li paper_id="${paper.id }">
					        	<span class="d-long"><span class="paper-title"><c:choose><c:when test="${paper.paperURL != null && paper.paperURL !='' }">
					        	<a target="_blank" href="${paper.paperURL }">${paper.title }</a></c:when><c:otherwise>${paper.title }</c:otherwise></c:choose>,</span>
					        	<div class="clear"></div>
					        		<span class="paper-author" title="${paper.authors }">${paper.shortauthors }.</span>
					        		<c:if test="${paper.source !=null && paper.source.trim()!='' }">
					        			<span class="paper-source">${paper.source }:</span>
					        			</c:if>
					        		<c:if test="${paper.publishedTime !=null && paper.publishedTime.trim()!='' }">
					        			<span class="paper-time">${paper.publishedTime}</span>
					        		</c:if>
					        		<c:if test="${paper.volumeIssue !=null && paper.volumeIssue.trim() != ''}">
					        			<span>, </span>
					        			<span>${paper.volumeIssue }</span>
					        		</c:if>
					        		<c:if test="${paper.pages !=null && paper.pages.trim() != ''}">
									    <span class="paper-pages">,${paper.pages }</span>
									</c:if>
					        		<c:choose>
						        		<c:when test="${paper.clbId >0}">
									        <c:choose>
							        			<c:when test="${paper.summary!=null && paper.summary!='' }">
										        	<a class="summary btn btn-mini btn-success" style="font-size:12px;"><fmt:message key='adminPaper.viewSummary'/></a>
									        	</c:when>
									        	<c:otherwise>
									        		<a class="summary btn btn-mini btn-success add-summary" style="background:#6bdc6b;font-size:12px;"><fmt:message key='adminPaper.addSummary'/></a>
									        	</c:otherwise>
								        	</c:choose>
						        			<a href="<dhome:url value='${paper.localFulltextURL}'/>" class="btn btn-mini btn-info downfull" style="font-size:12px; margin-top:3px;"><fmt:message key='addNewPaper.downloadFulltext'/></a>
						        			<c:if test="${paper.summary!=null && paper.summary!='' }">
									        	<span class="summary-content" style="display:none">${paper.summary}</span>
								        	</c:if>
						        		</c:when>
						        		<c:otherwise>
						        			<span>
						        				<c:choose>
							        				<c:when test="${paper.summary!=null && paper.summary!='' }">
										        		<a class="summary btn btn-mini btn-success" ><fmt:message key='adminPaper.viewSummary'/></a>
									        		</c:when>
									        		<c:otherwise>
									        			<a class="summary btn btn-mini btn-success add-summary" style="float:left; background:#6bdc6b;font-size:12px; margin-right:10px;"><fmt:message key='adminPaper.addSummary'/></a>
									        		</c:otherwise>
								        		</c:choose>
								        		<%-- <div class="upload-fulltext maintain" style="float:left;" >
													<div class="qq-upload-button" style="cursor:pointer;"><fmt:message key='addNewPaper.uploadFulltext'/></div>					
								        		</div> --%>
								        		<c:if test="${paper.summary!=null && paper.summary!='' }">
									        		<span class="summary-content" style="display:none">${paper.summary}</span>
								        		</c:if>
								        	</span>
						        		</c:otherwise>
					        		</c:choose>
					        	</span>
				        		<div class="clear"></div>
					        </li>
					        </c:if>
				        </c:forEach>
				        <c:if test="${empty papers}">
					 	<p><fmt:message key='adminPaper.youHaveNoPaper'/></p>
					 </c:if>
				    </ol>
<%-- 				    </c:when> --%>
<%-- 				    <c:when test="${IAPPaper!=null && IAPPaper.size()>0}"> --%>
				    <ol id="IAPPapers" class="listShow" style="display:none">
					<c:forEach items="${IAPPaper }" var="data">
						<li>
						<span class="title"><c:out value="${data.title }"/></span>
						<div class="clear"></div>
						<c:choose>
							<c:when test="${empty authorMap[data.id]}">
								--
							</c:when>
							<c:otherwise>
								<c:forEach items="${ authorMap[data.id]}" var="author">
									<a class="authorDetailPopover"  data-paper-id="${data.id }" data-author-id="${author.id }" ><c:out value="${author.authorName }"/></a>
										<sup>[${author.subscriptIndex }]</sup>
								</c:forEach>
							</c:otherwise>
						</c:choose>
						<div class="clear"></div>
						<span class="detail">
							<!-- doi -->
							<c:if test="${!empty data.doi }">
								dio:<c:out value="${data.doi }"/>. 
							</c:if>
							<!-- issn -->
							<c:if test="${!empty data.issn }">
								issn:<c:out value="${data.issn }"/>. 
							</c:if>
							<!-- 刊物 -->
							<c:if test="${data.publicationId!=0 }">
								<c:out value="${pubMap[data.publicationId].pubName }"/>
							</c:if>
							<!-- 卷号 -->
							<c:if test="${!empty data.volumeNumber }">
								卷:<c:out value="${data.volumeNumber }"/>. 
							</c:if>
							<!-- 期号 -->
							<c:if test="${!empty data.series }">
								期:<c:out value="${data.series }"/>. 
							</c:if>
							<!-- 开始页~结束页 -->
							<c:if test="${!empty data.publicationPage }">
								页:${data.publicationPage}. 
							</c:if>
<!-- 									开始页~结束页 -->
<%-- 									<c:if test="${!empty data.startPage||!empty data.endPage }"> --%>
<%-- 										页:${data.startPage}~${data.endPage==0?'无':data.endPage}.  --%>
<%-- 									</c:if> --%>
							<!-- 发表时间 年.月 -->
							出版年:${data.publicationYear}<c:if test="${data.publicationMonth!=0 }">.${data.publicationMonth}</c:if>. 
						</span>
							<!-- 原文链接 -->
							<c:if test="${!empty data.originalLink }">
								<a href="<c:out value="${data.originalLink }"/>" target="_blank">原文链接</a>
							</c:if>
							<!-- 原文下载 -->
							<c:if test="${data.clbId!=0 }">
								<a href="<dhome:url value="/system/file?fileId=${data.clbId }"/>" target="_blank">原文下载</a>
							</c:if>
							<!-- 摘要 -->
							<c:if test="${!empty data.summary}">
								<a data-paper-id="${data.id }" class="showSummary">查看摘要</a>
							</c:if>
							
							<div id="summary_${data.id }" class="summaryDiv" style="display:none"><c:out value="${data.summary }"/></div>
						</li> 
					</c:forEach>
					<c:if test="${empty IAPPaper}">
					 	<p><fmt:message key='adminPaper.youHaveNoPaper'/></p>
					 </c:if>
				</ol>
<%-- 				    </c:when> --%>
				    </div>
<%-- 				    <c:choose> --%>
<%-- 				    <c:when test="${papers.size()==0 && IAPPaper.size()==0}">  --%>
<!-- 				    	<div class="page-container"> -->
<%-- 				       		<span><fmt:message key='adminPaper.youHaveNoPaper'/></span> --%>
<!-- 				      	</div> -->
<%-- 				    </c:when> --%>
<%-- 				    </c:choose> --%>
		   	 	</div>
		    </div>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
	<div id="remove-paper-popup" class="modal hide fade">
		<div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">×</button>
	        <h3><fmt:message key='adminPaper.deletePaper'/></h3>
        </div>
		<div class="modal-body">
			<span><fmt:message key='adminPaper.delete.confirm.message'/></span>
			<ul class="x-list work no-border"></ul>
			<input type="hidden" name="remove-paperId" value="">
		</div>
	    <div class="modal-footer">
			<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
			<button id="remove-paper-confirm" class="btn btn-primary"><fmt:message key='common.confirm.delete'/></button>
		</div>
	</div>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(function(){
		var flag = $('input:radio[name="paperLib"]:checked').val();
		if(flag==2){
			$("#editPapers").attr("href","<dhome:url value='/people/${domain}/admin/paper/list/1'/>");
			$("#IAPPapers").show();
			$("#sortablePaper").hide();
			$("#viewMyPage").attr("href","<dhome:url value='/people/${domain}?isIAP="+flag+"'/>");
			$("#paperList").attr("href","<dhome:url value='/people/${domain}/admin/paper/list/1'/>");
		}else{
			$("#editPapers").attr("href","<dhome:url value='/people/${domain}/admin/commonPaper'/>");
			$("#IAPPapers").hide();
			$("#sortablePaper").show();
			$("#viewMyPage").attr("href","<dhome:url value='/people/${domain}?isIAP="+flag+"'/>");
			$("#paperList").attr("href","<dhome:url value='/people/${domain}/admin/commonPaper'/>");
		}
//  		$("#IAPPapers").hide();
//  		$("#viewMyPage").attr("href","<dhome:url value='/people/${domain}?isIAP="+1+"'/>");
	   $(":radio").click(function(){
			 flag = $('input:radio[name="paperLib"]:checked').val();
			if(flag==1){
				$("#editPapers").attr("href","<dhome:url value='/people/${domain}/admin/commonPaper'/>");
				$("#IAPPapers").hide();
				$("#sortablePaper").show();
				$("#viewMyPage").attr("href","<dhome:url value='/people/${domain}?isIAP="+flag+"'/>");
				$("#paperList").attr("href","<dhome:url value='/people/${domain}/admin/commonPaper'/>");
			}
			if(flag==2){
				$("#editPapers").attr("href","<dhome:url value='/people/${domain}/admin/paper/list/1'/>");
				$("#IAPPapers").show();
				$("#sortablePaper").hide();
				$("#viewMyPage").attr("href","<dhome:url value='/people/${domain}?isIAP="+flag+"'/>");
				$("#paperList").attr("href","<dhome:url value='/people/${domain}/admin/paper/list/1'/>");
			}
	 });
	 
		$('#auditStatus').hover(function(){
			$(this).tooltip('toggle');
		});
		
		$(".nav .pull-down").click(function(){
			$(".headerBar").toggle();
		});
		$(".headerBar:not(.no-liststyle)").live("click",function(){
			$(".headerBar").hide();
		});
		//leftMenu
		$(".icon-file").addClass("icon-white");
		$(".icon-file").parent().parent("li").addClass("active");
		
		
		var sortStartPos = 0;
		var sortStopPos = 0;
		$("#sortablePaper").sortable({
			start:function(event, ui){
				var paperId = $(event.target).parents("li[paper_id]").attr("paper_id");
				$("li[paper_id]").each(function(index, element){
					if($(element).attr("paper_id")==paperId){
						sortStartPos = index;
						return;
					}
				});
			},
			stop:function(event, ui){
				var paperId = $(event.target).parents("li[paper_id]").attr("paper_id");
				$("li[paper_id]").each(function(index, element){
					if($(element).attr("paper_id")==paperId){
						sortStopPos = index;
						return;
					}
				});
				var url = "<dhome:url value='/people/${domain}/admin/paper/reorder.json'/>";
				var params = {"paperId":paperId, "sequence":(sortStopPos-sortStartPos)};
				ajaxRequest(url, params, function(){refreshArrow();}, function(){refreshArrow();});
			}
		});
		
		function ajaxRequest(url, params, successCall, errorCall){
			$.ajax({
				url:url,
				type:'GET',
				data: params,
				success:successCall,
				error:errorCall
			});
		}
		
		$("a.add-summary").click(function(event){
			var paperId = $(this).parents("li").attr("paper_id");
			editPaper(paperId, true);
			event.stopPropagation();
		});
		
		$("span.d-short a").live("click", function(event){
			var $element = $(this).children("i");
			var paperId = $(this).parents("li[paper_id]").attr("paper_id");
			if($element.hasClass("icon-edit")){
				editPaper(paperId, false);
			}else if($element.hasClass("icon-arrow-up")){
				movePaper("icon-arrow-up",paperId,-1);
			}else if($element.hasClass("icon-arrow-down")){
				movePaper("icon-arrow-down",paperId,1);
			}else{
			}
			event.stopPropagation();
		});
		
		$("a.remove-paper").live("click", function(event){
			var paperId = $(this).parents("li[paper_id]").attr("paper_id");
			var $content = $(this).parents("li").clone();
			$content.removeAttr("paper_id");
			$content.children(".d-short").remove();
			$content.children("div.clear").remove();
			$content.find("a.summary").remove();
			$content.find("div.upload-fulltext").remove();
			var $authors = $content.find("span.paper-author");
			$authors.text($authors.attr("title"));
			$authors.removeAttr("title");
			$("#remove-paper-popup ul").html("").append($content);
			$("#remove-paper-popup input[name=remove-paperId]").val(paperId);
			$("#remove-paper-popup").modal("show");
			event.stopPropagation();
		});
		
		$("#remove-paper-confirm").click(function(event){
			var paperId = $(this).parent().prev().children("input[name=remove-paperId]").val();
			removePaper(paperId);
			$("#remove-paper-popup").modal("hide");
			event.stopPropagation();
		});
		
		$("span.paper-author").hover(function(){
			$(this).tooltip('toggle');
		});
		
		$("a.summary").toggle(function(event){
			$(this).parent().children(".summary-content").slideDown();
			event.preventDefault();
		}, function(event){
			$(this).parent().children(".summary-content").slideUp();
			event.preventDefault();
		});
		
		function editPaper(paperid, needFocus){
			var url = "<dhome:url value='/people/${domain}/admin/paper/edit'/>?func=paperNew&paperId="+paperid+"&needFocus="+needFocus;
			window.location.href=url;
		}
		
		function removePaper(paperid){
			var url = "<dhome:url value='/people/${domain}/admin/paper/deletePaper.json'/>";
			var params = {"paperId":paperid};
			ajaxRequest(url, params, function(data){
				if(typeof(data.result.status)=='undefined' || !data.result.status){
					alert("<fmt:message key='adminPaper.delete.fail'/>");
				}else{
					$("li[paper_id="+data.result.paperId+"]").remove();
				}
				refreshArrow();
			}, function(){
				alert("<fmt:message key='adminPaper.delete.fail'/>");
			});
		}
		
		function movePaper(cls, paperid, sequence){
			if(($("li[paper_id]:first").attr("paper_id")==paperid && cls == "icon-arrow-up") 
				|| ($("li[paper_id]:last").attr("paper_id")==paperid && cls == "icon-arrow-down")){
				return;
			}
			var url = "<dhome:url value='/people/${domain}/admin/paper/reorder.json'/>";
			var params = {"paperId":paperid, "sequence":sequence};
			ajaxRequest(url, params, function(data){
				if(typeof(data.result.status)=='undefined' || !data.result.status){
					alert("<fmt:message key='adminPaper.reorder.fail'/>");
				}else{
					var seq = data.result.sequence;
					var $cur = $("li[paper_id="+data.result.paperId+"]");
					var $mysibling = (seq>0)?($cur.next()):($cur.prev());
					for(var i=0; i<Math.abs(seq)-1; i++){
						$mysibling = (seq>0)?($mysibling.next()):($mysibling.prev());
					}
					if(seq>0){
						$cur.detach().insertAfter($mysibling);
					}else{
						$cur.detach().insertBefore($mysibling);
					}
				}
				refreshArrow();
			}, function(data){
				alert("<fmt:message key='adminPaper.reorder.fail'/>");
			});
		}
		
		function refreshArrow(){
			$("li[paper_id] span a").show();
			$("li[paper_id]").each(function(){
				if($(this).attr("paper_id")==$("li[paper_id]:first").attr("paper_id")){
					$(this).find("a i.icon-arrow-up").parent().hide();
				}
				if($(this).attr("paper_id")==$("li[paper_id]:last").attr("paper_id")){
					$(this).find("a i.icon-arrow-down").parent().hide();
				}
			});
		}
		function createFileUploader(){
			var $object = $("div.upload-fulltext");
			var fu = new Array();
			$object.each(function(index, element){
				var uploader = new qq.FileUploader({
					paperId: $(this).parents("li[paper_id] ").attr("paper_id"),
			        element: $(this)[0],
			        action: "<dhome:url value='/people/${domain}/admin/paper/edit?func=uploadFulltext'/>",
			        params : {paperId:$(this).parents("li[paper_id] ").attr("paper_id")},
			        allowedExtensions:['pdf','doc','docx'],
			        onProgress: function(id, fileName, loaded, total){
			        	var $textBtn = $("li[paper_id="+$(this)[0].paperId+"] div.upload-fulltext").find("div#uploadText");
			        	var progress = Math.round((loaded/total)*100);
			        	$textBtn.html("<fmt:message key='addNewPaper.uploading'/>("+progress+"%)");
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
			        multiple:false,
			        debug: true
			   });
				fu.push(uploader);
			});
		}
		
		function afterUpload(data){
			if(data.status == true){
				var $uploadSpan = $("li[paper_id="+data.paperId+"] div.upload-fulltext").parent();
				$uploadSpan.before("<span class='clb' style='display:none;'>"+data.clbId+"</span>");
				$uploadSpan.children("div").remove();
				var url = "<dhome:url value='/system/download/"+data.clbId+"'/>";
				$uploadSpan.children("a.summary").after("<a href='"+url+"' class='btn btn-mini btn-info downfull'  style='font-size:12px;'><fmt:message key='addNewPaper.downloadFulltext'/></a>");
			}else{
			}
		}
		
		function init(){
			refreshArrow();
			createFileUploader();
			$("li[paper_id]").each(function(){
				$(this).find("#uploadText").text("<fmt:message key='addNewPaper.uploadFulltext'/>");
			});
		}
	
		init();
	});
</script>
</html>