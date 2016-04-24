<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
<dhome:InitLanuage useBrowserLanguage="true"/>
<title><c:if test="${empty page.title}"><fmt:message key="adminCommonLeft.addNewPage.empty.menuItemName"/></c:if><c:if test="${!empty page.title}">${page.title }</c:if>-${titleUser.zhName }<fmt:message key="common.dhome"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav"
	data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${titleUser.zhName }<fmt:message key="common.dhome"/>
			<jsp:include page="showAudit.jsp"/>
		</div>
		<jsp:include page="./commonMenu.jsp">
			<jsp:param name="activeItem" value="content" />
		</jsp:include>
		<%-- 			<jsp:param name="institutionItem" value="${flag }" /> --%>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title">
			<h3><fmt:message key="admin.common.content.title"/></h3>
			</div>
			<jsp:include page="adminCommonLeft.jsp"></jsp:include>
			<div class="span9 left-b">
				<c:choose>
					<c:when test="${tab=='newPage' }">
						<div id="mainSpan">
							<jsp:include page="editor.jsp"></jsp:include>
						</div>
					</c:when>
					
					<c:when test="${tab=='showPage' }">
						<div id="mainSpan">
							<div class="page-header">
					   		    <h3>
					   		    <!-- 如果是首页，并且没有更改标题，显示为最新动态 -->
					   		    <c:choose>
					   		    <c:when test="${!empty isIndex && page.title =='首页'}"><fmt:message key="adminIndex.recentNews"/></c:when>
					   		     <c:otherwise> ${page.title }</c:otherwise>
					   		    </c:choose>
					   			<span class="publish">
					   			<c:if test="${!empty isIndex }">（<fmt:message key="adminIndex.appearsTop"/>）</c:if>
					   			<c:if test="${empty isIndex }">
						   		<c:if test="${status=='hide' }">
						   		（<fmt:message key="common.publish.notdo"/> <a class="btn btn-mini" href="<dhome:url value="/people/${domain}/admin/p/${page.keyWord}/changeStatus"/>"><fmt:message key="common.publish.do"/></a>）
						   		</c:if>
						   		<c:if test="${status=='show' }">
						   		（<fmt:message key='common.publish.done'/> <a class="btn btn-mini" href="<dhome:url value="/people/${domain}/admin/p/${page.keyWord}/changeStatus"/>"><fmt:message key='common.publish.undo'/></a>）
						   		</c:if>
						   		</c:if>
						   		</span>
						    	
						    	<span class="float-right">
						    		<c:if test="${empty isIndex }">
						    			<a onclick="deletePage()" class="float-right"><i class="icon-trash abs-top"></i><span class="common-link"><fmt:message key="common.delete"/></span></a>
					    			</c:if>
					    			<a href="<dhome:url value='/people/${domain}/admin/p/${page.keyWord}'/>?func=edit&pid=${page.id}" class="float-right"><i class="icon-edit abs-top"></i><span class="common-link"><fmt:message key="common.edit"/></span></a>
					    		</span>
					    		</h3>
							</div>
							<div class="page-container">
								<!-- 这个框，不仅得是内容为空，而且得是index页面 -->
									${page.content}
							</div>
						</div>
					</c:when>
					<c:when test="${tab=='editPage' }">
						<div id="mainSpan">
							<jsp:include page="editor.jsp"></jsp:include>
						</div>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
	<%-- <div id="intro_config_1" class="intro_step">
		<div class="title"><fmt:message key='adminIndex.guide.intro_step1'/></div>
		<a class="Iknow" id="Iknow_config_1"><fmt:message key='adminIndex.guide.next'/></a>
		<a class="closeMe"><fmt:message key='adminIndex.guide.skip'/></a>
	</div> --%>
	<div id="intro_config_1" class="intro_step">
		<div class="title"><fmt:message key='adminIndex.guide.intro_step2'/></div>
		<a class="Iknow" id="Iknow_config_2"><fmt:message key='adminIndex.guide.next'/></a>
		<a class="closeMe"><fmt:message key='adminIndex.guide.skip'/></a>
	</div>
	<div id="intro_config_2" class="intro_step">
		<div class="title"><fmt:message key='adminIndex.guide.intro_step3'/></div>
		<a class="Iknow" id="Iknow_config_3"><fmt:message key='adminIndex.guide.finish'/></a>
	</div>
	<div id="mask_config_1" class="intro_mask"></div>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript"
	src="<dhome:url value="/resources/scripts/base.js" />"></script>
	
<script type="text/javascript">
	$(document).ready(function(){
		//auditStatus
		$('#auditStatus').hover(function(){
			$(this).tooltip('toggle');
		});
		
		// intro steps begin
		$("#mask_config_1").css({
			"width":$(document.body).outerWidth(),
		});
		var coverStyle = setInterval(function(){
			if($(document.body).outerHeight() > $(window).height()){ 
				$("#mask_config_1").css({
					"height":$(document.body).outerHeight(),
				});
			}
			else{
				$("#mask_config_1").css({
					"height":window.innerHeight,
				});
			}
		},20);
		
		var step;
		var totalStep = 3;
		var count = 1;
		function showTheVeryStep(step){
			$("#mask_config_1").show();
			$("#intro_config_" + count).show();
			/* $("#intro_config_1").css({
				"left":$("#configStep1").offset().left - 20,
				"top":$("#configStep1").offset().top + 20
			}); 
			$("#configStep2").css({
				"background":"#04c"
			});*/
			$("#intro_config_1").css({
				"left":$("#configStep1").offset().left - 18,
				"top":$("#configStep1").offset().top - 1
			});
			$("#intro_config_2").css({
				"left":$("#sortableMenu").offset().left + 32,
				"top":$("#sortableMenu").offset().top
			});
			$(".isHighLight").removeClass("isHighLight");
			$(".readyHighLight" + count).addClass("isHighLight");
		}
		
		$(".Iknow").click(function(){
			$("#mask_config_1").show();
			count++;
			$(this).parent().hide();
			$(this).parent().next().show();
			$(".isHighLight").removeClass("isHighLight");
			$(".readyHighLight" + count).addClass("isHighLight");
		});
		
		$("#Iknow_config_3").click(function(){
			$(this).parent().hide();
			$("#mask_config_1").hide();
			$(".isHighLight").removeClass("isHighLight");
			step = totalStep;
			updateStep("adminIndexStep",step);
		});
		
		$(".closeMe").click(function(){
			$(this).parent().hide();
			$("#mask_config_1").hide();
			$(".isHighLight").removeClass("isHighLight");
			step = totalStep;
			updateStep("adminIndexStep",step);
		})
		
		function getStep(module){
			var url='<dhome:url value="/people/${domain }/admin/p/getStep.json"/>';
			$.ajax({
				url : url,
				type:"POST",
				data:{"module":module},
				success:function(data){
					//console.log("step:"+data.step);
					if(data.step < totalStep) {
						showTheVeryStep(data.step);
					}
				},
			});
		}
		
		function updateStep(module, step){
			var url='<dhome:url value="/people/${domain }/admin/p/updateStep.json"/>';
			$.ajax({
				url : url,
				type:"POST",
				data:{"module":module, "step":step},
				success:function(data){},
				error:function(){}
			});
		}
		
		function init(){
			getStep("adminIndexStep");
		}
		if(${tab=='showPage' }){
			init();
		}
		// intro steps end
	});
	function deletePage(url){
		var url='<dhome:url value="/people/${domain }/admin/p/${page.keyWord }/deletePage"/>';
		var flag=confirm("<fmt:message key='common.deleteConfirm'/>");
		if(flag){
			window.location.href=url;
		}
	}
</script>
</html>