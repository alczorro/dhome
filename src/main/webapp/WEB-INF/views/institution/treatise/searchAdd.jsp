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
			<h3>论著管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="treatise" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
					<jsp:include page="../../commonTreatise.jsp"><jsp:param name="activeItem" value="addTreatise" /> </jsp:include>
				<ul class="listShow">
					<form name="search-treatise-form" class="form-search" action="<dhome:url value='/people/${domain}/admin/treatise/search/submit'/>" method="POST">
				      <input type="hidden" name="treatiseIds" value="">
				        <fieldset>
				           <div class="control-group">
					          <ul class="striped eighty-per no-border small-font" id="treatise-up" style="margin-bottom:0;"></ul>
					          <div id="ctrlBtn" class="form-actions clear-b" style="display:none;padding-left:0; margin-top:0;">
					          	<button id="save-search" class="btn btn-primary btn-set-middle">保存</button>
					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/treatise/search'/>">取消</a>
					          </div>
				              <input type="text" name="treatise-keyword" class="input-medium input-xlarge" placeholder="请输入论著的名称或者作者">
	      					  <a id="search-treatise" class="btn" type="button" class="btn">搜索</a>
	      					  <div class="small-font subText">通过搜索关键词：<b><span id="defaultKeywords"></span></b>，为您找到以下论著，<b class="dark-orange">请确认哪些是您发表过的论著。</b></div>
				           </div>
				          <ul id="treatise-down" class="striped eighty-per small-font">
				          	<li class="more-artical" id="loadmore"><a>更多论著</a></li>
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
		var url = "<dhome:url value='/people/${domain}/admin/treatise/search/treatise'/>";
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
					$('#notice').addClass('large').text("系统没有找到您的论著，您可以更改关键词再次搜索。").show();
				},
				noMore: function() {
					$('#loadmore').hide();
					setTimeout(function(){
						$('#notice').removeClass('large').text("没有更多论著了").show();
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
		
		initTreatise();
		function initTreatise(){
			var params = {"offset":offset, "size":size, "existTreatises":getSelectedTreatiseIds()};
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
//  			alert(len);
			$("#defaultKeywords").text(data.searchKeywords);
			if(len>0){
				$("#treatise-template").render(data.result).insertBefore("#loadmore");
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
			$(this).parents("li").clone().appendTo("#treatise-up");
			$(this).parents("li").hide();
			$("#treatise-up").removeClass("no-border");
			$("#ctrlBtn").show();
			
		});
		
		$("a.not-my-treatise").live("click",function(){
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
			var treatiseId = $(this).attr("treatise_id");
			$(this).parents("li").remove();
			var $liBtn = $("li button[treatise_id="+treatiseId+"]");
			$liBtn.parent().hide();
			$liBtn.parent().next().show();
			$liBtn.parents("li").show();
			if($("#treatise-up").html()==''){
				$("#ctrlBtn").hide();
			}
		});
		
		$("#loadmore").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=treatise-keyword]").val();
			if(curKeyword!=keyword){
				curKeyword = keyword;
				offset = 0;
			}
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existTreatises":getSelectedTreatiseIds()};
			ajaxRequest(url, params, renderData, errorHandler);
		});
		
		$("#search-treatise").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=treatise-keyword]").val();
			curKeyword = keyword;
			offset = 0;
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existTreatises":getSelectedTreatiseIds()};
			ajaxRequest(url, params, renderNewResearch, errorHandler);
		});
		
		$("input[name=treatise-keyword]").keyup(function(event){
			if(event.which == 13){
				$("#search-treatise").click();
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
			$("#treatise-down li:not(#loadmore)").remove();
			renderData(data);
		}
		
		function getSelectedTreatiseIds(){
			var treatiseIds = new Array();
			$("#treatise-up li:not(#loadmore)").each(function(){
				var treatiseId = $(this).find("button.close[treatise_id]").attr("treatise_id");
				treatiseIds.push(treatiseId);
			});
			return treatiseIds.toString();
		}
		
		$("#save-search").click(function(){
			$("input[name=treatiseIds]").val(getSelectedTreatiseIds());
			$("form[name=search-treatise-form]").submit();
		});
		$.extend({
			judicePublisher:function(index){
				var publishers = {
				     <c:forEach items="${publishers.entrySet() }" var="entry">
					 "${entry.key }" : "${entry.value.val }",
				     </c:forEach>};
					return publishers[index];
			}
		});
		
	});
</script>
<script type="text/html" id="treatise-template">
<li>
	<span class="d-long">
		<span style="display:inline-block; margin-right:1em"><strong>{{= name}}</strong></span>
		<span style="display:inline-block; margin-right:1em" title="{{= author}}">{{= author}}</span>
		<span style="display:inline-block; margin-right:1em">
			{{= $.judicePublisher(publisher)}}
		</span>
	</span>
	<span class="d-short treatise-up" style="display:none">
		<button type="button" class="close" treatise_id="{{= id}}">×</button>
	</span>
	<span class="d-short treatise-down">
		<button type="button" style="display:none" class="btn d-sbtn undo" treatise_id="{{= id}}">撤销</button>
		<button type="button" class="btn btn-success d-sbtn" treatise_id="{{= id}}">是</button>
		<a class="not-my-treatise" treatise_id="{{= id}}">不是</a>
	</span>
	<div class="clear"></div>
</li>
</script>

</html>