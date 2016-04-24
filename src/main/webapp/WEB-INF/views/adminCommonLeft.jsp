<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<dhome:InitLanuage useBrowserLanguage="true"/>
<dhome:LeftMenuLoader domain="${domain }" isAdmin="true"/>
<div class="span3 span-left left-menu" style="margin-left:0;">
    <div class="tabbable tabs-left">
		<ul class="nav nav-tabs span11" id="sortableMenu">
			<c:forEach var="menuItem" items="${menu.menuItems}">
				<li class="li-item readyHighLight2" menu_item_id="${menuItem.id}">
					<a href="<dhome:url value="${menuItem.url}"/>" ><c:if test="${menuItem.status==0}"><span class="float-right">(<fmt:message key="common.publish.notdo"/>)</span></c:if><span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
					
					<c:choose>
	          			<c:when test="${fn:endsWith(menuItem.url,'admin/paper') }">
	          				<fmt:message key="common.paper.title"/>
	          			</c:when>
	          			<c:when test="${fn:endsWith(menuItem.url,'admin/msgboard') }">
	          				<fmt:message key='msg.board'/>
	          			</c:when>
	          			<c:when test="${fn:endsWith(menuItem.url,'p/index') && !empty isIndex && page.title =='首页'}">
	          				<fmt:message key='adminIndex.recentNews'/>
	          			</c:when>  
<%-- 	          			<c:when test="${!empty isIndex && page.title =='首页'}"> --%>
<%-- 	          				<fmt:message key='adminIndex.recentNews'/> --%>
<%-- 	          			</c:when>   --%>
	          			<c:otherwise>
	          				${menuItem.title}
	          			</c:otherwise>
	          		</c:choose>
					</a>
				</li>
			</c:forEach>
		</ul>
	</div>
      <input id="activeUrl" type="hidden" value='${active}'/>
      
      <div class="m-sleft">
	      <div class="btn-group" id="configStep1">
	         <button id="addNewPage" class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><fmt:message key="adminCommonLeft.addNewPage"/></button>
	         <ul class="dropdown-menu">
	            <li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?defaultMenuItemName=<fmt:message key="adminCommonLeft.addNewPage.empty.menuItemName"/>"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle0"/></a>
				</li>
	            <li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle1"/>&defaultPageName=work"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle1"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle2"/>&defaultPageName=education"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle2"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle3"/>&defaultPageName=project"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle3"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle4"/>&defaultPageName=research"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle4"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle5"/>&defaultPageName=awards"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle5"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle6"/>&defaultPageName=activity"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle6"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle7"/>&defaultPageName=tutorials"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle7"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle8"/>&defaultPageName=groupmember"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle8"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle9"/>&defaultPageName=teaching"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle9"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage?title=<fmt:message key="adminCommonLeft.addNewPage.defaultTitle10"/>&defaultPageName=englishing"><fmt:message key="adminCommonLeft.addNewPage.defaultTitle10"/></a>
				</li>
				<li>
					<a href="<%=request.getContextPath()%>/people/${domain}/admin/msgboard/createMsgBoard"><fmt:message key="msg.board"/></a>
				</li>
<!-- 				<li> -->
<%-- 					<a href="<%=request.getContextPath()%>/people/${domain}/admin/en_paper/createEnPaper">英文版论文</a> --%>
<!-- 				</li> -->
	         </ul>
	       </div>
	   </div>
	   
      <div tabindex="-1" id="myModal" class="modal hide fade">
         <div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3><fmt:message key="adminCommonLeft.addNewPage"/></h3>
         </div>
         <form id="addPageForm" class="form-horizontal" style="margin:0;" method="post" action="<%=request.getContextPath()%>/people/${domain}/admin/p/newPage">
         <div class="modal-body">
         <div class="control-group">
         			<label class="control-label" for="pageName"><fmt:message key="adminCommonLeft.pageTitle"/></label>
          			<div class="controls">
            			<input maxlength="50" type="text" class="input-xlarge" id="title" name="title">
          			</div>
        		</div>
         </div>
         <div class="modal-footer">
           <a href="#" class="btn" data-dismiss="modal"><fmt:message key="common.cancel"/></a>
           <button type="submit"  class="btn btn-primary"><fmt:message key="common.confirm"/></button>
         </div>
        </form>
      </div>
   </div>
<script src="<dhome:url value="/resources/scripts/jquery/1.7.2/jquery-1.7.2.min.js"/>" type="text/javascript" ></script>
<script>

$(document).ready(function(){
	
	$('#addNewPage').hover(
		function () {
			//$(this).parent().addClass('open');
		},
		function () {
			//$(this).parent().removeClass('open');
		}
	);
	var url = "<dhome:url value='/people/${domain}/leftMenu/edit'/>";
	$("#sortableMenu").sortable({
		update : function(event,ui){
			var params = {'func':'sortMenuItems','menuItemIds[]':[]};		
			$('.li-item').each(function(){
				params['menuItemIds[]'].push($(this).attr('menu_item_id'));			
			});
			
			ajaxRequest(url, params,function(data){
				if(data=="success"){
					showMesg("<fmt:message key='adminCommonLeft.move'/>");
				}
				
			});
		},
	});	
	$('.li-item a').each(function(){
		var currUrl = $(this).attr('href').substr($(this).attr('href').lastIndexOf("/")+1);
		var clickUrl = '${page.keyWord}';
		$(this).parent(".active").removeClass("active");
// 		alert("currUrl="+currUrl);
// 		alert("clickUrl="+clickUrl);
		if(clickUrl==''&&currUrl=='paper'){
			$(this).parent().addClass("active");
		}else if(currUrl==clickUrl){
			$(this).parent().addClass("active");
		}
	});

	$("#addPageForm").validate({
		  rules: {
		  	title: {required:true}
		  },
		   messages: {
			   title: {
				   required:"<fmt message key='adminCommonLeft.noempty' />"
				}
		 }
	});
	$("span.sort").hover(function(){
		$(this).tooltip('toggle');
	});
	$("a.addPageShow").click(function(){
		$('#myModal').modal('show');
	});
	

});
var ajaxRequest = function(requestURL,queryString,callBackFunction){
	$.ajax({
		url:requestURL,
		type: "GET",
		data: queryString,
		error: function (xhr, ajaxOptions, thrownError) {
	    },
	    success: function(data){
			callBackFunction(data);
		}
    });
};
</script>