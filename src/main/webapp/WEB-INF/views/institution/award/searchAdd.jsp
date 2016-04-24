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
			<h3>奖励管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="award" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonAward.jsp"><jsp:param name="activeItem" value="addAward" /> </jsp:include>
				<ul class="listShow">
					<form name="search-award-form" class="form-search" action="<dhome:url value='/people/${domain}/admin/award/search/submit'/>" method="POST">
				      <input type="hidden" name="awardIds" value="">
				        <fieldset>
				           <div class="control-group">
					          <ul class="striped eighty-per no-border small-font" id="award-up" style="margin-bottom:0;"></ul>
					          <div id="ctrlBtn" class="form-actions clear-b" style="display:none;padding-left:0; margin-top:0;">
					          	<button id="save-search" class="btn btn-primary btn-set-middle">保存</button>
					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/award/search'/>">取消</a>
					          </div>
				              <input type="text" name="award-keyword" class="input-medium input-xlarge" placeholder="请输入奖励的名称或者获奖人姓名">
	      					  <a id="search-award" class="btn" type="button" class="btn">搜索</a>
	      					  <div class="small-font subText">通过搜索关键词：<b><span id="defaultKeywords"></span></b>，为您找到以下奖励，<b class="dark-orange">请确认哪些是您获得过的奖励。</b></div>
				           </div>
				          <ul id="award-down" class="striped eighty-per small-font">
				          	<li class="more-artical" id="loadmore"><a>更多奖励</a></li>
				          </ul>
				          <p id="notice" class="more-artical">正在载入...</p>
				          
				      </fieldset>
				    </form>
				</ul>
				
		</div>
	</div>
	</div>
</div>
	<jsp:include page="../../commonfooter.jsp"></jsp:include>

</body>
<jsp:include page="../../commonheader.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(function(){
		var url = "<dhome:url value='/people/${domain}/admin/award/search/award'/>";
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
					$('#notice').addClass('large').text("系统没有找到您的奖励，您可以更改关键词再次搜索。").show();
				},
				noMore: function() {
					$('#loadmore').hide();
					setTimeout(function(){
						$('#notice').removeClass('large').text("没有更多奖励了").show();
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
		
		initAward();
		function initAward(){
			var params = {"offset":offset, "size":size, "existAwards":getSelectedAwardIds()};
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
				$("#award-template").render(data.result).insertBefore("#loadmore");
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
			$(this).parents("li").clone().appendTo("#award-up");
			$(this).parents("li").hide();
			$("#award-up").removeClass("no-border");
			$("#ctrlBtn").show();
			
		});
		
		$("a.not-my-award").live("click",function(){
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
			var awardId = $(this).attr("award_id");
			$(this).parents("li").remove();
			var $liBtn = $("li button[award_id="+awardId+"]");
			$liBtn.parent().hide();
			$liBtn.parent().next().show();
			$liBtn.parents("li").show();
			if($("#award-up").html()==''){
				$("#ctrlBtn").hide();
			}
		});
		
		$("#loadmore").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=award-keyword]").val();
			if(curKeyword!=keyword){
				curKeyword = keyword;
				offset = 0;
			}
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existAwards":getSelectedAwardIds()};
			ajaxRequest(url, params, renderData, errorHandler);
		});
		
		$("#search-award").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=award-keyword]").val();
			curKeyword = keyword;
			offset = 0;
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existAwards":getSelectedAwardIds()};
			ajaxRequest(url, params, renderNewResearch, errorHandler);
		});
		
		$("input[name=award-keyword]").keyup(function(event){
			if(event.which == 13){
				$("#search-award").click();
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
			$("#award-down li:not(#loadmore)").remove();
			renderData(data);
		}
		
		function getSelectedAwardIds(){
			var awardIds = new Array();
			$("#award-up li:not(#loadmore)").each(function(){
				var awardId = $(this).find("button.close[award_id]").attr("award_id");
				awardIds.push(awardId);
			});
			return awardIds.toString();
		}
		
		$("#save-search").click(function(){
			$("input[name=awardIds]").val(getSelectedAwardIds());
			$("form[name=search-award-form]").submit();
		});
		$.extend({
			judiceName:function(index){
				var awardNames = {
				     <c:forEach items="${awardNames.entrySet() }" var="entry">
					 "${entry.key }" : "${entry.value.val }",
				     </c:forEach>};
					return awardNames[index];
			},
			judiceType:function(index){
				var types = {
				     <c:forEach items="${types.entrySet() }" var="entry">
					 "${entry.key }" : "${entry.value.val }",
				     </c:forEach>};
					return types[index];
			},
			judiceGrade:function(index){
				var grades = {
				     <c:forEach items="${grades.entrySet() }" var="entry">
					 "${entry.key }" : "${entry.value.val }",
				     </c:forEach>};
					return grades[index];
			}
		});
		
	});
</script>
<script type="text/html" id="award-template">
<li>
	<span class="d-long">
		<span style="display:inline-block; margin-right:1em"><strong>{{= name}}</strong></span>
		<span style="display:inline-block; margin-right:1em">{{= $.judiceName(awardName)}}</span>
		<span style="display:inline-block; margin-right:1em">{{= author}}</span>
		<span style="display:inline-block; margin-right:1em">
			{{= $.judiceType(type)}}
		</span>
		<span style="display:inline-block; margin-right:1em">
			{{= $.judiceGrade(grade)}}
		</span>
	</span>
	<span class="d-short award-up" style="display:none">
		<button type="button" class="close" award_id="{{= id}}">×</button>
	</span>
	<span class="d-short award-down">
		<button type="button" style="display:none" class="btn d-sbtn undo" award_id="{{= id}}">撤销</button>
		<button type="button" class="btn btn-success d-sbtn" award_id="{{= id}}">是</button>
		<a class="not-my-award" award_id="{{= id}}">不是</a>
	</span>
	<div class="clear"></div>
</li>
</script>

</html>