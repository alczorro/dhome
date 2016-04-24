<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='topics' }">class="active"</c:if>>
	    <a href="<dhome:url value="/institution/${domain }/backend/topic/list/1"/>">课题列表</a>
	</li>
	<c:if test="${param.activeItem=='modifyTopic' }">
		<li class="active">
		     <a href="<dhome:url value="/institution/${domain }/backend/topic/create"/>">
			    <c:choose>
					<c:when test="${op=='create' }">
						+ 手动添加课题
					</c:when>
					<c:otherwise>
						更改课题
					</c:otherwise>
				</c:choose>
			</a>
	    </li>
	 </c:if>
	 <c:if test="${param.activeItem!='modifyTopic' }">
		 <li>
		     <a href="<dhome:url value="/institution/${domain }/backend/topic/create"/>">+ 手动添加课题</a>
	    </li>
	 </c:if>
<%--    	<li <c:if test="${param.activeItem=='addTopic' }">class="active"</c:if>> --%>
<!-- 	    <a href="#">+ 批量导入</a> -->
<!--     </li> -->
