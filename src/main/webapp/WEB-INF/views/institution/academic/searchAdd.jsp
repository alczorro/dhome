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
			<h3>学术任职管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="academic" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonAcademic.jsp"><jsp:param name="activeItem" value="addAcademic" /> </jsp:include>
				<ul class="listShow">
					<form name="search-academic-form" class="form-search" action="<dhome:url value='/people/${domain}/admin/academic/search/submit'/>" method="POST">
				      <input type="hidden" name="academicIds" value="">
				        <fieldset>
				           <div class="control-group">
					          <ul class="striped eighty-per no-border small-font" id="academic-up" style="margin-bottom:0;"></ul>
					          <div id="ctrlBtn" class="form-actions clear-b" style="display:none;padding-left:0; margin-top:0;">
					          	<button id="save-search" class="btn btn-primary btn-set-middle">保存</button>
					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/academic/search'/>">取消</a>
					          </div>
				              <input type="text" name="academic-keyword" class="input-medium input-xlarge" placeholder="请输入任职人员姓名">
	      					  <a id="search-academic" class="btn" type="button" class="btn">搜索</a>
	      					  <div class="small-font subText">通过搜索关键词：<b><span id="defaultKeywords"></span></b>，为您找到以下学术任职，<b class="dark-orange">请确认哪些是您担任过的学术任职。</b></div>
				           </div>
				          <ul id="academic-down" class="striped eighty-per small-font">
				          	<li class="more-artical" id="loadmore"><a>更多学术任职</a></li>
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
		var url = "<dhome:url value='/people/${domain}/admin/academic/search/academic'/>";
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
					$('#notice').addClass('large').text("系统没有找到您的学术任职，您可以更改关键词再次搜索！").show();
				},
				noMore: function() {
					$('#loadmore').hide();
					setTimeout(function(){
						$('#notice').removeClass('large').text("没有更多学术任职了").show();
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
		
		initAcademic();
		function initAcademic(){
			var params = {"offset":offset, "size":size, "existAcademics":getSelectedAcademicIds()};
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
				$("#academic-template").render(data.result).insertBefore("#loadmore");
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
			$(this).parents("li").clone().appendTo("#academic-up");
			$(this).parents("li").hide();
			$("#academic-up").removeClass("no-border");
			$("#ctrlBtn").show();
			
		});
		
		$("a.not-my-academic").live("click",function(){
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
			var academicId = $(this).attr("academic_id");
			$(this).parents("li").remove();
			var $liBtn = $("li button[academic_id="+academicId+"]");
			$liBtn.parent().hide();
			$liBtn.parent().next().show();
			$liBtn.parents("li").show();
			if($("#academic-up").html()==''){
				$("#ctrlBtn").hide();
			}
		});
		
		$("#loadmore").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=academic-keyword]").val();
			if(curKeyword!=keyword){
				curKeyword = keyword;
				offset = 0;
			}
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existAcademics":getSelectedAcademicIds()};
			ajaxRequest(url, params, renderData, errorHandler);
		});
		
		$("#search-academic").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=academic-keyword]").val();
			curKeyword = keyword;
			offset = 0;
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existAcademics":getSelectedAcademicIds()};
			ajaxRequest(url, params, renderNewResearch, errorHandler);
		});
		
		$("input[name=academic-keyword]").keyup(function(event){
			if(event.which == 13){
				$("#search-academic").click();
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
			$("#academic-down li:not(#loadmore)").remove();
			renderData(data);
		}
		
		function getSelectedAcademicIds(){
			var academicIds = new Array();
			$("#academic-up li:not(#loadmore)").each(function(){
				var academicId = $(this).find("button.close[academic_id]").attr("academic_id");
				academicIds.push(academicId);
			});
			return academicIds.toString();
		}
		
		$("#save-search").click(function(){
			$("input[name=academicIds]").val(getSelectedAcademicIds());
			$("form[name=search-academic-form]").submit();
		});
		$.extend({
			judiceName:function(index){
				var names = {
				     <c:forEach items="${organizationNames.entrySet() }" var="entry">
					 "${entry.key }" : "${entry.value.val }",
				     </c:forEach>};
					return names[index];
			},
			judicePosition:function(index){
				var positions = {
				     <c:forEach items="${positions.entrySet() }" var="entry">
					 "${entry.key }" : "${entry.value.val }",
				     </c:forEach>};
					return positions[index];
			}
		});
		
	});
</script>
<script type="text/html" id="academic-template">
<li>
	<span class="d-long">
		<span style="display:inline-block; margin-right:1em"><strong>
			{{= $.judiceName(name)}}
</strong></span>
		<span style="display:inline-block; margin-right:1em" title="{{= author}}">{{= author}}</span>
		<span style="display:inline-block; margin-right:1em">
			{{= $.judicePosition(position)}}
		</span>
		<span style="display:inline-block; margin-right:1em">{{= startYear}}{{if startMonth!=0}}.{{= startMonth}}{{/if}} - 
		{{if endYear==3000}}至今{{/if}}
		{{if endYear!=3000}}{{= endYear}}{{if endMonth!=0}}.{{= endMonth}}{{/if}}{{/if}}</span>
	</span>
	<span class="d-short academic-up" style="display:none">
		<button type="button" class="close" academic_id="{{= id}}">×</button>
	</span>
	<span class="d-short academic-down">
		<button type="button" style="display:none" class="btn d-sbtn undo" academic_id="{{= id}}">撤销</button>
		<button type="button" class="btn btn-success d-sbtn" academic_id="{{= id}}">是</button>
		<a class="not-my-academic" academic_id="{{= id}}">不是</a>
	</span>
	<div class="clear"></div>
</li>
</script>

</html>