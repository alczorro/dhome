<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="false"/>
	<div class="personal-info">
		<div class="info-header">
			<div class="name-show">
				<h1>
					<span style="float:right">${name}</span>
					<c:if test="${isSelf }">
						<a class="edit-personal" href="<dhome:url value='/people/${domain}/admin/personal'/>"><i class="icon-edit"></i><fmt:message key="personal.common.editSelf"/></a>
					</c:if>
				</h1>
				<div class="clear"></div>
				<p class="d-mwhole">
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
						<c:forEach items="${interest }" varStatus="index" var='ins'>
							<c:if test="${index.index!=0 }">,&nbsp;</c:if>
							<a target="_blank" href="<dhome:url value="/system/discover?type=interest&keyword="/><dhome:encode value="${ins.keyword }"/>">${ins.keyword }</a>
						</c:forEach>
					</c:if>
				</p>
			</div>
			
			<div class="header">
				<div class="header-container">
					<img class="header-img" src='<dhome:img imgId="${titleUser.image}"/>'/>		
				</div>
				<c:if test="${isSelf }">
					<div class="header-edit">
						<a href="<dhome:url value='/people/${domain}/admin/personal/photo'/>"><i class="icon-edit"></i><fmt:message key="personalPhotoInfo.title"/></a>
					</div>	
				</c:if>
			</div>
			<p class="self-intro clear">${detailUser.introduction }</p>
			<jsp:include page="../showSocialMedia.jsp"/>
		</div>	
		<div class="clear"></div>
	</div>
	<div class="clear"></div>
		    