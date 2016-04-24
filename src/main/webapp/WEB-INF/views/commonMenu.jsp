<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<div class="float-right">
	<span class="config-link <c:if test="${param.activeItem=='content' }">active</c:if>"><a href="<dhome:url value="/people/${domain}/admin/p/index"/>"><fmt:message key="admin.common.link.content"/></a></span>
<%-- 	<c:if test="${titleUser.institutionId!=null }"> --%>
		<span class="config-link <c:if test="${param.activeItem=='achievement' }">active</c:if>"><a  id="paperList" href="<dhome:url value="/people/${domain}/admin/commonPaper?func=achievement"/>"><fmt:message key="admin.common.link.achievement"/></a></span>
<%-- 	</c:if> --%>
	<span class="config-link <c:if test="${param.activeItem=='theme' }">active</c:if>"><a href="<dhome:url value="/people/${domain}/admin/custom/theme"/>"><fmt:message key="admin.common.link.theme"/></a></span>
	<span class="config-link <c:if test="${param.activeItem=='setting' }">active</c:if>"><a href="<dhome:url value="/people/${domain}/admin/domain"/>"><fmt:message key="admin.common.link.setting"/></a></span>
	<span class="config-link <c:if test="${param.activeItem=='personInfo' }">active</c:if>"><a href="<dhome:url value='/people/${domain}/admin/personal/baseinfo'/>"><fmt:message key="admin.common.link.personInfo"/></a></span>
	<span><a class='btn btn-warning' id="viewMyPage" href="<dhome:url value="/people/${domain}"/>"><fmt:message key="admin.common.link.viewMyHome"/></a></span>
</div>