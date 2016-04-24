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
<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.css"/>" />
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
				<jsp:include page="../../commonPaper.jsp"><jsp:param name="activeItem" value="addPaper" /> </jsp:include>
					<form name="search-paper-form" class="form-search form-horizontal" action="<dhome:url value='/people/${domain}/admin/paper/search/submit'/>" method="POST">
				      <input type="hidden" name="paperIds" value="">
				      <input type="hidden" name="commonPaperIds" value="">
				        <fieldset>
				           <div class="control-group">
					          <ul class="striped eighty-per no-border small-font" id="paper-up" style="margin-bottom:0;"></ul>
					          <div id="ctrlBtn" class="form-actions clear-b" style="display:none;padding-left:0; margin-top:0;">
					          	<button id="save-search" class="btn btn-primary btn-set-middle">保存</button>
					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/paper/search'/>">取消</a>
					          </div>
				              <input type="text" name="paper-keyword" class="input-medium input-xlarge" placeholder="请输入论文的名称或者作者">
	      					  <a id="search-paper" class="btn" type="button" class="btn">搜索</a>
	      					  <div class="small-font subText">通过搜索关键词：<b><span id="defaultKeywords"></span></b>，为您找到以下论文，<b class="dark-orange">请确认哪些是您发表过的论文。</b></div>
				           </div>
				          <ul id="paper-down" class="striped eighty-per small-font">
				          	<li class="more-artical" id="loadmore"><a>更多论文</a></li>
				          </ul>
				          <p id="notice" class="more-artical">正在载入...</p>
				          
				      </fieldset>
				    </form>
		</div>
	</div>
	</div>
</div>

	<jsp:include page="../../commonfooter.jsp"></jsp:include>

</body>
<jsp:include page="../../commonheader.jsp"></jsp:include>
<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
<script type="text/javascript">
	$(document).ready(function(){
		var url = "<dhome:url value='/people/${domain}/admin/paper/search/paper'/>";
		var offset = 0;
		var size = 10;
		var curKeyword = "";
		
		var notice_handler = {
				loading: function() {
					$('#loadmore').hide();
					$('#notice').addClass('large').text("正在载入...").fadeIn();
				},
				noMatch: function() {
					$('#loadmore').hide();
					$('#notice').addClass('large').text("系统没有找到您的论文，您可以更改关键词再次搜索。").show();
				},
				noMore: function() { 
					$('#loadmore').hide();
					setTimeout(function(){
						$('#notice').removeClass('large').text("没有更多论文了").show();
					}, 500);
				},
				readyToLoad: function() {
					$('#loadmore').show();
					$('#notice').hide();
				},
				error : function(){
					$('#loadmore').hide();
					$('#notice').removeClass('large').text("请求失败！可能由于以下原因导致此问题：未登录，会话过期或权限不够！").fadeIn();
				}
		};
		
		initPaper();
		function initPaper(){
			var params = {"offset":offset, "size":size, "existPapers":getSelectedPaperIds(),"existCommonPapers":getSelectedPaperIds2()};
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
 			var len2 = data.result2.length;
//  			alert(len);
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
			var keyword = $("input[name=paper-keyword]").val();
			if(curKeyword!=keyword){
				curKeyword = keyword;
				offset = 0;
			}
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existPapers":getSelectedPaperIds(),"existCommonPapers":getSelectedPaperIds2()};
			ajaxRequest(url, params, renderData, errorHandler);
		});
		
		$("#search-paper").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=paper-keyword]").val();
			curKeyword = keyword;
			offset = 0;
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existPapers":getSelectedPaperIds(),"existCommonPapers":getSelectedPaperIds2()};
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
		
		$("#save-search").click(function(){
			$("input[name=paperIds]").val(getSelectedPaperIds());
			$("input[name=commonPaperIds]").val(getSelectedPaperIds2());
			$("input[name=authorIds]").val(getSelectedAuthorIds());
			$("form[name=search-paper-form]").submit();
		});
		
	});
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
</html>