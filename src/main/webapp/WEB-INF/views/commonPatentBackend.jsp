<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='patents' }">class="active"</c:if>>
	     <a href="<dhome:url value="/institution/${domain }/backend/patent/list/1"/>">专利列表</a>
	</li>
	<c:if test="${param.activeItem=='modifyPatent' }">
		<li class="active">
		     <a href="<dhome:url value="/institution/${domain }/backend/patent/create"/>">
			    <c:choose>
					<c:when test="${op=='create' }">
						+ 手动添加专利
					</c:when>
					<c:otherwise>
						更改专利
					</c:otherwise>
				</c:choose>
			</a>
	    </li>
	 </c:if>
	 <c:if test="${param.activeItem!='modifyPatent' }">
		 <li>
		     <a href="<dhome:url value="/institution/${domain }/backend/patent/create"/>">+ 手动添加专利</a>
	    </li>
	 </c:if>
<%--    	<li <c:if test="${param.activeItem=='addPatent' }">class="active"</c:if>> --%>
<%-- 	    <a href="<dhome:url value="/institution/${domain }/backend/patent/import"/>">+ 批量导入</a> --%>
<!--     </li> -->
