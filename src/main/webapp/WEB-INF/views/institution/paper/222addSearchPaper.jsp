<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
<dhome:InitLanuage useBrowserLanguage="true"/>
<title>${titleUser.zhName }<fmt:message key="common.dhome"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${titleUser.zhName }<fmt:message key="common.dhome"/>
			<jsp:include page="../../showAudit.jsp"/>
		</div>
		<jsp:include page="../../commonMenu.jsp">
			<jsp:param name="activeItem" value="achievement" />
		</jsp:include>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title">
			<h3>论文管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="paper" /> </jsp:include>
		    <div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<ul class="nav nav-tabs">
					<li>&nbsp;&nbsp;&nbsp;</li>
					<li>
					    <a href="<dhome:url value="/people/${domain}/admin/paper/list/1"/>">论文 </a>
					</li>
					<li>
					     <a href="<dhome:url value="/people/${domain }/admin/paper/create"/>">+ 手动添加论文</a>
				    </li>
				   	<li class="active">
					    <a href="#">+ 检索添加论文</a>
				    </li>
				</ul>
				    <form name="search-paper-form" class="form-search" action="<dhome:url value='/people/${domain}/admin/paper?func=saveSearchPaper'/>" method="POST">
				      <input type="hidden" name="paperIds" value="">
				        <fieldset>
				           <div class="control-group">
					          <ul class="striped eighty-per no-border small-font" id="paper-up" style="margin-bottom:0;"></ul>
					          <div id="ctrlBtn" class="form-actions clear-b" style="display:none;padding-left:0; margin-top:0;">
					          	<button id="save-search" class="btn btn-primary btn-set-middle"><fmt:message key='common.save'/></button>
					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/paper'/>"><fmt:message key='common.cancel'/></a>
					          </div>
				              <input type="text" name="paper-keyword" class="input-medium input-xlarge" placeholder="<fmt:message key='addSearchPaper.searchPaperKeyword'/>">
	      					  <a id="search-paper" class="btn" type="button" class="btn"><fmt:message key='common.search'/></a>
	      					  <div class="small-font"><fmt:message key='addSearchPaper.bySearchKeyword'/><b><span id="defaultKeywords"></span></b><fmt:message key='addSearchPaper.searchResultForYou'/><b class="dark-orange"><fmt:message key='addSearchPaper.searchResultConfirm'/></b></div>
				           </div>
				          <ul id="paper-down" class="striped eighty-per small-font">
				          	<li class="more-artical" id="loadmore"><a><fmt:message key='addSearchPaper.morePaper'/></a></li>
				          </ul>
				          <p id="notice" class="more-artical"><fmt:message key='addSearchPaper.loading'/></p>
				      </fieldset>
				    </form>
				   </div>
			    </div>
		    </div>
		</div>
		<jsp:include page="../../commonfooter.jsp"></jsp:include>

</body>
<jsp:include page="../../commonheader.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(function(){
		var url = "<dhome:url value='/system/searchPaper/paper.json'/>";
		var offset = 0;
		var size = 10;
		var curKeyword = "";
		
		var notice_handler = {
				loading: function() {
					$('#loadmore').hide();
					$('#notice').addClass('large').text("<fmt:message key='addSearchPaper.loading'/>").fadeIn();
				},
				noMatch: function() {
					$('#loadmore').hide();
					$('#notice').addClass('large').text("<fmt:message key='addSearchPaper.noResult'/>").show();
				},
				noMore: function() {
					$('#loadmore').hide();
					setTimeout(function(){
						$('#notice').removeClass('large').text("<fmt:message key='addSearchPaper.noMore'/>").show();
					}, 500);
				},
				readyToLoad: function() {
					$('#loadmore').show();
					$('#notice').hide();
				},
				error : function(){
					$('#loadmore').hide();
					$('#notice').removeClass('large').text("<fmt:message key='addSearchPaper.noAuth'/>").fadeIn();
				}
		};
		
		initPaper();
		function initPaper(){
			var params = {"offset":offset, "size":size, "existPapers":getSelectedPaperIds()};
			notice_handler.loading();
			ajaxRequest(url, params, renderData, errorHandler);
		}
		
		function ajaxRequest(url, params, successCall, errorCall){
			$.ajax({
				url:url,
				type:'POST',
				data: params,
				success:successCall,
				error:errorCall
			});
		}
		
		function renderData(data){
			var len = data.result.length;
			$("#defaultKeywords").text(data.searchKeywords);
			if(len>0){
				$("#paper-template").render(data.result).insertBefore("#loadmore");
				offset += len;
				if(len<size){
					notice_handler.noMore();
				}else{
					notice_handler.readyToLoad();
				}
			}else{
				if(offset ==0){
					notice_handler.noMatch();
				}else{
					notice_handler.noMore();
				}
			}
		}
		
		function errorHandler(){
			notice_handler.error();
		}
		
		$("button.btn-success").live("click",function(){
			$(this).parent().hide();
			$(this).parent().prev().show();
			$(this).parents("li").clone().appendTo("#paper-up");
			$(this).parents("li").hide();
			$("#paper-up").removeClass("no-border");
			$("#ctrlBtn").show();
			
		});
		
		$("a.not-my-paper").live("click",function(){
			$(this).hide();
			$(this).prev().hide();
			$(this).prev().prev().show();
			$(this).parents("li").find("span").css({"color":"#999","text-decoration":"line-through"});
		});
		
		$("button.undo").live("click",function(){
			$(this).hide();
			$(this).next().show();
			$(this).next().next().show();
			$(this).parents("li").find("span").css({"color":"#000","text-decoration":"none"});
		});
		
		$("button.close").live("click", function(){
			var paperId = $(this).attr("paper_id");
			$(this).parents("li").remove();
			var $liBtn = $("li button[paper_id="+paperId+"]");
			$liBtn.parent().hide();
			$liBtn.parent().next().show();
			$liBtn.parents("li").show();
			if($("#paper-up").html()==''){
				$("#ctrlBtn").hide();
			}
		});
		
		$("#loadmore").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=paper-keyword]").val();
			if(curKeyword!=keyword){
				curKeyword = keyword;
				offset = 0;
			}
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existPapers":getSelectedPaperIds()};
			ajaxRequest(url, params, renderData, errorHandler);
		});
		
		$("#search-paper").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=paper-keyword]").val();
			curKeyword = keyword;
			offset = 0;
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existPapers":getSelectedPaperIds()};
			ajaxRequest(url, params, renderNewResearch, errorHandler);
		});
		
		$("input[name=paper-keyword]").keyup(function(event){
			if(event.which == 13){
				$("#search-paper").click();
				event.preventDefault();
				event.stopPropagation();
			}
		}).keydown(function(event){
			if(event.which == 13){
				event.preventDefault();
				event.stopPropagation();
			}
		});
		
		function renderNewResearch(data){
			$("#paper-down li:not(#loadmore)").remove();
			renderData(data);
		}
		
		function getSelectedPaperIds(){
			var paperIds = new Array();
			$("#paper-up li:not(#loadmore)").each(function(){
				var paperId = $(this).find("button.close[paper_id]").attr("paper_id");
				paperIds.push(paperId);
			});
			return paperIds.toString();
		}
		
		$("#save-search").click(function(){
			$("input[name=paperIds]").val(getSelectedPaperIds());
			$("form[name=search-paper-form]").submit();
		});
	});
</script>
<script type="text/html" id="paper-template">
<li>
	<span class="d-long">
		<span style="display:inline-block; margin-right:1em"><strong>{{= title}}</strong></span>
		<span style="display:inline-block; margin-right:1em" title="{{= authors}}">{{= shortauthors}}</span>
		<span style="display:inline-block; margin-right:1em">{{= source}}</span>
		<span style="display:inline-block; margin-right:1em">{{= publishedTime}}</span>
	</span>
	<span class="d-short paper-up" style="display:none">
		<button type="button" class="close" paper_id="{{= dsnPaperId}}">×</button>
	</span>
	<span class="d-short paper-down">
		<button type="button" style="display:none" class="btn d-sbtn undo" paper_id="{{= dsnPaperId}}"><fmt:message key="addSearchPaper.undo"/></button>
		<button type="button" class="btn btn-success d-sbtn" paper_id="{{= dsnPaperId}}"><fmt:message key="addSearchPaper.yes"/></button>
		<a class="not-my-paper" paper_id="{{= dsnPaperId}}"><fmt:message key="addSearchPaper.no"/></a>
	</span>
	<div class="clear"></div>
</li>
</script>
</html>