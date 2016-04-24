<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<ul class="nav nav-tabs">
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='periodicals' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain }/admin/periodical/list/1"/>">期刊任职 ${periodicalCount }</a>
	</li>
	<c:if test="${param.activeItem=='modifyPeriodical' }">
		<li class="active">
		     <a href="<dhome:url value="/people/${domain }/admin/periodical/add"/>">
			    <c:choose>
					<c:when test="${op=='add' }">
						+ 手动添加期刊任职
					</c:when>
					<c:otherwise>
						更改期刊任职
					</c:otherwise>
				</c:choose>
			</a>
	    </li>
	 </c:if>
	 <c:if test="${param.activeItem!='modifyPeriodical' }">
		 <li>
		     <a href="<dhome:url value="/people/${domain }/admin/periodical/add"/>">+ 手动添加期刊任职</a>
	    </li>
	 </c:if>
   	<li <c:if test="${param.activeItem=='addPeriodical' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain}/admin/periodical/search"/>">+ 检索添加期刊任职</a>
    </li>
</ul>