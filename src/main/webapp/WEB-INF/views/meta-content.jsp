<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<meta name="description" content="<c:out value="${titleUser.zhName }"/>,<c:out value="${nearestInstitution.aliasInstitutionName}"/>,<c:out value="${titleUser.salutation }"/> &nbsp;<fmt:message key="common.interest" /><c:forEach items="${interest }" varStatus="index" var='ins'><c:if test="${index.index!=0 }">,</c:if><c:out value="${ins.keyword}"/></c:forEach>&nbsp;<fmt:message key="personalBaseInfo.introduction" /><c:out value="${detailUser.introduction }"/>"/>