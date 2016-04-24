<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<ul class="nav nav-tabs">
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='papers' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain }/admin/paper/list/1"/>">论文 ${paperCount }</a>
	</li>
	<c:if test="${param.activeItem=='modifyPaper' }">
		<li class="active">
		     <a href="<dhome:url value="/people/${domain }/admin/paper/create"/>">
			    <c:choose>
					<c:when test="${op=='create' }">
						+ 手动添加论文
					</c:when>
					<c:otherwise>
						更改论文
					</c:otherwise>
				</c:choose>
			</a>
	    </li>
	 </c:if>
	 <c:if test="${param.activeItem!='modifyPaper' }">
		 <li>
		     <a href="<dhome:url value="/people/${domain }/admin/paper/create"/>">+ 手动添加论文</a>
	    </li>
	 </c:if>
   	<li <c:if test="${param.activeItem=='addPaper' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain}/admin/paper/search"/>">+ 检索添加论文</a>
    </li>
    <li <c:if test="${param.activeItem=='propelling' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain}/admin/paper/propelling"/>">论文推送</a>
    </li>
</ul>