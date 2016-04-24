<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='papers' }">class="active"</c:if>>
	    <a href="<dhome:url value="/institution/${domain }/backend/paper/list/1"/>">论文列表 </a>
	</li>
	<c:if test="${param.activeItem=='modifyPaper' }">
		<li class="active">
		    <a href="<dhome:url value="/institution/${domain }/backend/paper/create"/>">
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
		     <a href="<dhome:url value="/institution/${domain }/backend/paper/create"/>">+ 手动添加论文</a>
	    </li>
	 </c:if>
<%--    	<li <c:if test="${param.activeItem=='addPaper' }">class="active"</c:if>> --%>
<%-- 	    <a href="<dhome:url value='/people/${domain}/admin/paper/edit?func=importBibtex'/>">+ 批量导入</a> --%>
<!--     </li> -->
    <li <c:if test="${param.activeItem=='relates' }">class="active"</c:if>>
	    <a href="<dhome:url value="/institution/${domain }/backend/paper/index/1"/>">作者认证 </a>
	</li>
