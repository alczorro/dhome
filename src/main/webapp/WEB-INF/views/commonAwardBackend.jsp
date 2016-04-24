<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='awards' }">class="active"</c:if>>
	    <a href="<dhome:url value="/institution/${domain }/backend/award/list/1"/>">奖励列表</a>
	</li>
	<c:if test="${param.activeItem=='modifyAward' }">
		<li class="active">
		     <a href="<dhome:url value="/institution/${domain }/backend/award/add"/>">
			    <c:choose>
					<c:when test="${op=='add' }">
						+ 手动添加奖励
					</c:when>
					<c:otherwise>
						更改奖励
					</c:otherwise>
				</c:choose>
			</a>
	    </li>
	 </c:if>
	 <c:if test="${param.activeItem!='modifyAward' }">
		 <li>
		     <a href="<dhome:url value="/institution/${domain }/backend/award/add"/>">+ 手动添加奖励</a>
	    </li>
	 </c:if>
<%--    	<li <c:if test="${param.activeItem=='addAward' }">class="active"</c:if>> --%>
<!-- 	    <a href="#">+ 批量导入</a> -->
<!--     </li> -->
