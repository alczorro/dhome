<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<ul class="nav nav-tabs">
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='copyrights' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain }/admin/copyright/list/1"/>">软件著作权 </a>
	</li>
	<c:if test="${param.activeItem=='modifyCopyright' }">
		<li class="active">
		     <a href="<dhome:url value="/people/${domain }/admin/copyright/create"/>">
			    <c:choose>
					<c:when test="${op=='create' }">
						+ 手动添加软件著作权
					</c:when>
					<c:otherwise>
						更改软件著作权
					</c:otherwise>
				</c:choose>
			</a>
	    </li>
	 </c:if>
	 <c:if test="${param.activeItem!='modifyCopyright' }">
		 <li>
		     <a href="<dhome:url value="/people/${domain }/admin/copyright/create"/>">+ 手动添加软件著作权</a>
	    </li>
	 </c:if>
   	<li <c:if test="${param.activeItem=='addCopyright' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain}/admin/copyright/search"/>">+ 检索添加软件著作权</a>
    </li>
</ul>