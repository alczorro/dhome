<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<dhome:LeftMenuLoader domain="${domain }" isAdmin="false"/>
<dhome:InitLanuage useBrowserLanguage="false"/>
<div class="span3 span-left left-menu">
	<div class="header-container">
		<div class="header">
			<div class="header-img">
				<img src='<dhome:img imgId="${titleUser.image}"/>'/>	
			</div>
			<c:if test="${isSelf }">
				<div class="header-edit">
					<a href="<dhome:url value='/people/${domain}/admin/personal/photo'/>"><i class="icon-edit"></i><fmt:message key="personalPhotoInfo.title"/></a>
				</div>	
			</c:if>
		</div>
		
		<h3 class="steady_title">
			${titleUser.zhName}
			<c:if test="${isSelf }">
				<a class="edit-personal" href="<dhome:url value='/people/${domain}/admin/personal'/>"><i class="icon-edit"></i><fmt:message key="personal.common.editSelf"/></a>
			</c:if>
		</h3>
		<p>
			<c:if test="${!empty homeDomain }">
				<a class="needToolTip" target="_blank" href='<dhome:url value="/institution/${homeDomain }/index.html"/>'>
					<span class="tooltipName" title="${nearestInstitution.institutionName }">${nearestInstitution.aliasInstitutionName}</span>
				</a>
				<c:if test="${!empty titleUser.salutation }">
					 , ${titleUser.salutation }
				</c:if>
			</c:if>
			<c:if test="${empty homeDomain }">
				<dhome:StrFormat value="${nearestInstitution.aliasInstitutionName}， ${titleUser.salutation }"/>
			</c:if>	
			<c:if test="${!empty interest }">
				<br><fmt:message key="common.interest"/>
				<c:forEach varStatus="index" items="${interest }" var='ins'>
					<c:if test="${index.index!=0 }">,&nbsp;</c:if>
					<a target="_blank" href="<dhome:url value="/system/discover?type=interest&keyword="/><dhome:encode value="${ins.keyword }"/>">${ins.keyword }</a>
				</c:forEach>
			</c:if>
			<span class="self-intro">${detailUser.introduction }</span>
		</p>
		<jsp:include page="../showSocialMedia.jsp"/>
	</div>
	
    <div class="tabbable tabs-left span12">
     <ul class="nav nav-tabs">
	    <c:forEach var="menuItem" items="${menu.menuItems}">
	      <li class="li-item" menu_item_id="${menuItem.id}">
	          	<a href="<dhome:url value="${menuItem.url}"/>">
	          		<span>
	          		<c:choose>
	          			<c:when test="${fn:endsWith(menuItem.url,'paper.dhome') }">
	          				<fmt:message key="common.paper.title"/>
	          			</c:when>
	          			<c:when test="${fn:endsWith(menuItem.url,'msgboard.dhome') }">
	          				<fmt:message key='msg.board'/>
	          			</c:when>
	          			<c:when test="${fn:endsWith(menuItem.url,'index.html') && page.keyWord=='index' && page.title=='首页'}">
	          				<fmt:message key='adminIndex.recentNews'/>
	          			</c:when>
	          			<c:otherwise>
	          				${menuItem.title}
	          			</c:otherwise>
	          		</c:choose>
	          		</span>
	          	</a>
	      </li>
	    </c:forEach>
        </ul>
      <input id="activeUrl" type="hidden" value='${active}'/>
      </div>
   </div>
  
<script src="<dhome:url value="/resources/scripts/jquery/1.7.2/jquery-1.7.2.min.js"/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/scripts/jquery/jquery-ui-1.8.16.custom.min.js"/>" type="text/javascript"></script>
<script>
$(document).ready(function(){	
	$('.li-item a').each(function(){

		var currUrl = $(this).attr('href').replace('<%=request.getContextPath()%>','');
		var clickUrl = $("#activeUrl").val();
		if(clickUrl==""){
			if(currUrl.indexOf("index.html")>0){
				$(this).parent().addClass("active");
			}
		}
		else if(currUrl==clickUrl){
			$(this).parent(".active").removeClass("active");
			$(this).parent().addClass("active");
		}else if(endWiths(clickUrl,currUrl)){
			$(this).parent(".active").removeClass("active");
			$(this).parent().addClass("active");
		}
	});
	function endWiths(str,ends){
		var length=ends.length;
		var index=str.indexOf(ends);
		if(index>-1&&str.length-length){
			return true;
		}		
		return false;
	}

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