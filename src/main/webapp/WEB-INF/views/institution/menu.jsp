<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<dhome:InitLanuage useBrowserLanguage="true"/>
<div class="span3 span-left left-menu" style="margin-left:0;">
    <div class="tabbable tabs-left">
		<ul class="nav nav-tabs span11 ui-sortable" id="sortableMenu">
<%-- 				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='commonPaper' }">active</c:if>"> --%>
<%-- 					<a href="<dhome:url value="/people/${domain}/admin/commonPaper"/>" > --%>
<%--           				<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span> --%>
<!--           				论文(通用数据库) -->
<!-- 					</a> -->
<!-- 				</li> -->
				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='paper' }">active</c:if>">
					<c:if test="${param.moveItem!=1 }">
						<a href="<dhome:url value="/people/${domain}/admin/commonPaper?func=achievement"/>" >
          				<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
          				论文
						</a>
					</c:if>
					<c:if test="${param.moveItem==1 }">
						<a href="<dhome:url value="/people/${domain}/admin/paper/list/1"/>" >
          				<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
          				论文
						</a>
					</c:if>
					
				</li>
				<c:if test="${param.memberItem!='noIAP' }">
				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='treatise' }">active</c:if>">
					<a href="<dhome:url value="/people/${domain}/admin/treatise/list/1"/>" >
          				<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
          				论著
					</a>
				</li>
			<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='award' }">active</c:if>">
					<a href="<dhome:url value="/people/${domain}/admin/award/list/1"/>" >
	          			<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
	          			奖励
					</a>
				</li>
				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='copyright' }">active</c:if>">
					<a href="<dhome:url value="/people/${domain}/admin/copyright/list/1"/>" >
	          			<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
	          			软件著作权
					</a>
				</li>
				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='patent' }">active</c:if>">
					<a href="<dhome:url value="/people/${domain}/admin/patent/list/1"/>" >
	          			<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
	          			专利
					</a>
				</li>
				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='topic' }">active</c:if>">
					<a href="<dhome:url value="/people/${domain}/admin/topic/list/1"/>" >
	          			<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
	          			课题
					</a>
				</li>
				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='academic' }">active</c:if>">
					<a href="<dhome:url value="/people/${domain}/admin/academic/list/1"/>" >
	          			<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
	          			学术任职
					</a>
				</li>
				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='periodical' }">active</c:if>">
					<a href="<dhome:url value="/people/${domain}/admin/periodical/list/1"/>" >
	          			<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
	          			期刊任职
					</a>
				</li>
				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='training' }">active</c:if>">
					<a href="<dhome:url value="/people/${domain}/admin/training/list/1"/>" >
	          			<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
	          			人才培养
					</a>
				</li>
				<li class="li-item readyHighLight2 <c:if test="${param.activeItem=='jobapply' }">active</c:if>">
					<a href="<dhome:url value="/people/${domain}/admin/job/list/1"/>" >
	          			<span title="<div style='max-width:100px;'><fmt:message key="adminCommonLeft.reOrder"/></div>" data-placement="left" class="sort" ></span>
	          			岗位评定
					</a>
				</li>
				</c:if>
		</ul>
	</div>
</div>