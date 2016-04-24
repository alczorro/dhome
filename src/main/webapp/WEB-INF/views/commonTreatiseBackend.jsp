<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='treatises' }">class="active"</c:if>>
	    <a href="<dhome:url value="/institution/${domain }/backend/treatise/list/1"/>">论著列表</a>
	</li>
	<c:if test="${param.activeItem=='modifyTreatise' }">
		<li class="active">
		     <a href="<dhome:url value="/institution/${domain }/backend/treatise/add"/>">
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
		     <a href="<dhome:url value="/institution/${domain }/backend/treatise/add"/>">+ 手动添加论著</a>
	    </li>
	 </c:if>
   	<li <c:if test="${param.activeItem=='addTreatise' }">class="active"</c:if>>
	    <a href="<dhome:url value="/institution/${domain }/backend/treatise/import"/>">+ 批量导入</a>
    </li>
