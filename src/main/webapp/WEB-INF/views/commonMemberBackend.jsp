<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
    <li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='members' }">class="active"</c:if>>
	     <a href="<dhome:url value="/institution/${domain }/backend/member/list/1"/>">员工 </a>
	</li>
	<li <c:if test="${param.activeItem=='modifyMember' }">class="active"</c:if>>
		<a href="${memberUrl}" target="_blank">+ 新增员工</a>
    </li>
   	<li <c:if test="${param.activeItem=='addMember' }">class="active"</c:if>>
	    <a href="<dhome:url value="/institution/${domain }/backend/member/import"/>">+ 批量导入</a>
    </li>
<!--    <li> -->
<!-- 	    <a href="#">绩效考核</a> -->
<!--     </li> -->
   	<li <c:if test="${param.activeItem=='job' }">class="active"</c:if>>
	    <a href="<dhome:url value="/institution/${domain }/backend/job/list/1"/>">岗位评定</a>
    </li>
    <li <c:if test="${param.activeItem=='achieve' }">class="active"</c:if>>
	    <a href="<dhome:url value="/institution/${domain }/backend/achievements/list/1"/>">绩效考核</a>
    </li>
