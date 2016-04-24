<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<ul class="nav nav-tabs">
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='treatises' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain }/admin/treatise/list/1"/>">论著 ${treatiseCount }</a>
	</li>
	<c:if test="${param.activeItem=='modifyTreatise' }">
		<li class="active">
		     <a href="<dhome:url value="/people/${domain }/admin/treatise/add"/>">
			    <c:choose>
					<c:when test="${op=='add' }">
						+ 手动添加论著
					</c:when>
					<c:otherwise>
						更改论著
					</c:otherwise>
				</c:choose>
			</a>
	    </li>
	 </c:if>
	 <c:if test="${param.activeItem!='modifyTreatise' }">
		 <li>
		     <a href="<dhome:url value="/people/${domain }/admin/treatise/add"/>">+ 手动添加论著</a>
	    </li>
	 </c:if>
   	<li <c:if test="${param.activeItem=='addTreatise' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain}/admin/treatise/search"/>">+ 检索添加论著</a>
    </li>
</ul>