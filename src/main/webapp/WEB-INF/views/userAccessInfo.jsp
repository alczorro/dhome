<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<p class="updateTime">
	<c:if test="${!empty lastEditTime}">
		<fmt:message key="theme.browsePaper.updateTime" />${lastEditTime}
	</c:if>
	&nbsp;&nbsp;&nbsp;&nbsp;
	<c:if test="${!empty accessCount}">
		<fmt:message key="theme.browsePaper.accessedTime" />${accessCount}
	</c:if>
</p>