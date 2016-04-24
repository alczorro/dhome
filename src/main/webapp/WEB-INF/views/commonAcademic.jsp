<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<ul class="nav nav-tabs">
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='academics' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain }/admin/academic/list/1"/>">学术任职 ${academicCount }</a>
	</li>
	<c:if test="${param.activeItem=='modifyAcademic' }">
		<li class="active">
		     <a href="<dhome:url value="/people/${domain }/admin/academic/add"/>">
			    <c:choose>
					<c:when test="${op=='add' }">
						+ 手动添加学术任职
					</c:when>
					<c:otherwise>
						更改学术任职
					</c:otherwise>
				</c:choose>
			</a>
	    </li>
	 </c:if>
	 <c:if test="${param.activeItem!='modifyAcademic' }">
		 <li>
		     <a href="<dhome:url value="/people/${domain }/admin/academic/add"/>">+ 手动添加学术任职</a>
	    </li>
	 </c:if>
   	<li <c:if test="${param.activeItem=='addAcademic' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain}/admin/academic/search"/>">+ 检索添加学术任职</a>
    </li>
</ul>