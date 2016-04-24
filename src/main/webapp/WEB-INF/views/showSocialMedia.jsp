<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%@ page import="java.util.*" %>
<c:if test="${!empty urls }">
	<p class="mediaLinks">
	<fmt:message key="showSocialMedial.socialMedia"/>
		<c:forEach items="${urls}" var="url">
			<a title="<c:choose><c:when test="${'other' eq url.selectMedia}">${url.title}</c:when><c:otherwise><fmt:message key='${url.selectMedia }'/></c:otherwise></c:choose>" target="_blank" href="${url.url }" >
				<img width="16px" height="16px" src="<dhome:imgIdOrName img="${url.img }"/>"/>
				<c:if test="${'other' eq url.selectMedia}">${url.title}</c:if>
			</a>&nbsp;
		</c:forEach>
	</p>
</c:if>