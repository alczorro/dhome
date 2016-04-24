<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
	<title><fmt:message key='config.common.title'/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-boarding" data-offset="50" data-target=".subnav" data-spy="scroll">
<jsp:include page="commonBanner.jsp" flush="true"/>
	<div class="navbar boarding">
		<div class="navbar-inner" style="filter:none;">      
			<div class="container">        
				<!-- <a class="dhome-logo-black" href="<dhome:url value=""/><dhome:key value="deploy_subfix"/>"> </a>  -->     
				<div class="progress-bar three"> 
				    <span><fmt:message key='config.common.step1'/></span>
				    <i></i> <i></i> <i></i> <i></i> <i></i> 
				    <span><fmt:message key='config.common.step5'/></span> 
				</div>     
			</div>   
		</div><!-- /navbar-inner -->  
	</div>
	<div class="container narrow">
		<div class="row-fluid">
			<div class="span12 dhome-layout" style="margin-left:0;">
				<div class="page-header x-left">
					<h2><fmt:message key='configPaper.selectPaper'/></h2>
				</div>
				<form name="configPaper" class="form-horizontal x-left" action="<dhome:url value='/system/regist?func=stepFour'/>" method="POST">
					<input type="hidden" name="paperIds" value="">
					<input type="hidden" name="commonPaperIds" value="">
			        <fieldset>
			           <div class="control-group">
				          <ul class="striped eighty-per no-border small-font" id="paper-up"></ul>
			              <input type="text" name="keyWord" class="input-medium input-xlarge" placeholder="<fmt:message key='addSearchPaper.searchPaperKeyword'/>">
      					  <button id="search-paper" type="button" class="btn"><fmt:message key='common.search'/></button>
      					  <p class="help-block small-font"><fmt:message key='addSearchPaper.bySearchKeyword'/><b><span id="defaultKeywords"></span></b>
      					  <fmt:message key='addSearchPaper.searchResultForYou'/>
      					  <b class="dark-orange"><fmt:message key='addSearchPaper.searchResultConfirm'/></b></p>
			           </div>
			          <ul id="paper-down" class="striped eighty-per small-font">
			          	<li class="more-artical" id="loadmore"><a><fmt:message key='addSearchPaper.morePaper'/></a></li>
			          </ul>
			          <p id="notice" class="more-artical"><fmt:message key='addSearchPaper.loading'/></p>
			          <div class="form-actions clear-b" style="padding-left:0;">
			          	<a id="save" class="btn btn-primary btn-large btn-set-large"><fmt:message key='configPaper.nextStep'/></a>
			          </div>
			      </fieldset>
			   </form>
			</div>
		</div>
		<jsp:include page="commonfooter.jsp"></jsp:include>
	</div>
<jsp:include page="commonheader.jsp"></jsp:include>
<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script type="text/javascript">
	$(document).ready(function(){
		var url = "<dhome:url value='/system/searchPapers/paper.json'/>";
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
			var params = {"offset":offset, "size":size, "existPapers":getSelectedPaperIds(),"existCommonPapers":getSelectedPaperIds2()};
// 			var params = {"offset":offset, "size":size, "existPapers":getSelectedPaperIds()};
			notice_handler.loading();
			ajaxRequest(url, params, renderData, errorHandler);
		}
		
		function ajaxRequest(url, params, successCall, errorCall){
			$.ajax({
				url:url,
				type:'GET',
				data: params,
				success:successCall,
				error:errorCall
			});
		}
		
		function renderData(data){
			var len = data.result.length;
			var len2 = data.result2.length;
			$("#defaultKeywords").text(data.searchKeywords);
			if(len>0||len2>0){
				$("#paper-template").tmpl(data.result).insertBefore("#loadmore");
				$("#commonPaper-template").tmpl(data.result2).insertBefore("#loadmore");
				offset += len;
				if(len<size&&len2<size){
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
		});
		$("button.close2").live("click", function(){
			var paperId = $(this).attr("commonPaper_id");
			$(this).parents("li").remove();
			var $liBtn = $("li button[commonPaper_id="+paperId+"]");
			$liBtn.parent().hide();
			$liBtn.parent().next().show();
			$liBtn.parents("li").show();
			if($("#paper-up").html()==''){
				$("#ctrlBtn").hide();
			}
		});
		
		$("#loadmore").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=keyWord]").val();
			if(curKeyword!=keyword){
				curKeyword = keyword;
				offset = 0;
			}
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existPapers":getSelectedPaperIds(),"existCommonPapers":getSelectedPaperIds2()};
			ajaxRequest(url, params, renderData, errorHandler);
		});
		
		$("#search-paper").click(function(event){
			notice_handler.loading();
			event.stopPropagation();
			var keyword = $("input[name=keyWord]").val();
			curKeyword = keyword;
			offset = 0;
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existPapers":getSelectedPaperIds(),"existCommonPapers":getSelectedPaperIds2()};
			ajaxRequest(url, params, renderNewResearch, errorHandler);
		});
		
		$("input[name=keyWord]").keyup(function(event){
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
				if(paperId!=""&&paperId!=null){
					paperIds.push(paperId);
				}
			});
			return paperIds.toString();
		}
		function getSelectedPaperIds2(){
			var commonPaperIds = new Array();
			$("#paper-up li:not(#loadmore)").each(function(){
				var commonPaperId = $(this).find("button.close[commonPaper_id]").attr("commonPaper_id");
				if(commonPaperId!=""&&commonPaperId!=null){
					commonPaperIds.push(commonPaperId);
				}
			});
			return commonPaperIds.toString();
		}
		
		$("#save").click(function(){
// 			$("input[name=paperIds]").val(getSelectedPaperIds());
// 			$("form[name=configPaper]").submit();
			
			$("input[name=paperIds]").val(getSelectedPaperIds());
			$("input[name=commonPaperIds]").val(getSelectedPaperIds2());
// 			$("input[name=authorIds]").val(getSelectedAuthorIds());
			$("form[name=configPaper]").submit();
		});

	})
	</script>
	<script type="text/html" id="paper-template">
		<li>
	<span class="d-long">
		<span style="display:inline-block; margin-right:1em"><strong>{{= title}}</strong></span>
		<span style="display:inline-block; margin-right:1em" title="{{= author}}">{{= author}}</span>
		<span style="display:inline-block; margin-right:1em">{{= publicationName}}</span>
		<span style="display:inline-block; margin-right:1em">{{= publicationYear}}</span>
		<span style="display:inline-block; margin-right:1em">--IAP论文库</span>
	</span>
	<span class="d-short paper-up" style="display:none">
		<button type="button" class="close" paper_id="{{= id}}">×</button>
	</span>
	<span class="d-short paper-down">
		<button type="button" style="display:none" class="btn d-sbtn undo" paper_id="{{= id}}">撤销</button>
		<button type="button" class="btn btn-success d-sbtn" paper_id="{{= id}}">是</button>
		<a class="not-my-paper" paper_id="{{= id}}">不是</a>
	</span>
	<div class="clear"></div>
</li>
</script>
<script type="text/html" id="commonPaper-template">
<li>
	<span class="d-long">
		<span style="display:inline-block; margin-right:1em"><strong>{{= title}}</strong></span>
		<span style="display:inline-block; margin-right:1em" title="{{= authors}}">{{= shortauthors}}</span>
		<span style="display:inline-block; margin-right:1em">{{= source}}</span>
		<span style="display:inline-block; margin-right:1em">{{= publishedTime}}</span>
		<span style="display:inline-block; margin-right:1em">--通用论文库</span>
	</span>
	<span class="d-short paper-up" style="display:none">
		<button type="button" class="close" commonPaper_id="{{= dsnPaperId}}">×</button>
	</span>
	<span class="d-short paper-down">
		<button type="button" style="display:none" class="btn d-sbtn undo" commonPaper_id="{{= dsnPaperId}}"><fmt:message key="addSearchPaper.undo"/></button>
		<button type="button" class="btn btn-success d-sbtn" commonPaper_id="{{= dsnPaperId}}"><fmt:message key="addSearchPaper.yes"/></button>
		<a class="not-my-paper" commonPaper_id="{{= dsnPaperId}}"><fmt:message key="addSearchPaper.no"/></a>
	</span>
	<div class="clear"></div>
</li>
</script>
</body>
</html>