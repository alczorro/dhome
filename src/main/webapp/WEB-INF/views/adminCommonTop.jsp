<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="true"/>
	<div class="personal-info">
		<div class="info-header">
			<div class="name-show">
				<h1>${name}
					<a href="<dhome:url value='/people/${domain}/admin/personal/baseinfo'/>"><i class="icon-edit"></i></a>
				</h1>
				<p class="d-mwhole"> <dhome:StrFormat value="${nearestInstitution.institutionName}，${currentUser.salutation }"/></p>
			</div>
			<p class="self-intro">${detailUser.introduction }</p>
			<div class="header-container">
				<img class="header-img" src="<dhome:img imgId="${titleUser.image}"/>"/>		
				<div class="edit-image">
					<a href="<dhome:url value='/people/${domain}/admin/personal/photo'/>"><i class="float-right icon-edit"></i></a>
				</div>
			</div>
		</div>	
		<div class="info-container">
		    <c:forEach var="work" items="${works }">
		    	<div class="info">
				    <div class="info-left"><dhome:StrFormat value="${work.institutionName }，${work.department }"/>
				    	<div class="info-year">
				    	<fmt:formatDate value="${work.beginTime}" pattern="yyyy.MM"/>
				    	<c:if test="${work.endTime != null }">
				    		 - <fmt:formatDate value="${work.endTime }" pattern="yyyy.MM"/>
				    	</c:if>
				    	</div>
				    </div>
				    <div class="info-right">${work.position }</div>
			    </div>
		    </c:forEach>
		    <c:forEach var="edu" items="${edus }">
		    	<div class="info">
				    <div class="info-left">${edu.institutionName }
				    	<div class="info-year"><fmt:formatDate value="${edu.beginTime}" pattern="yyyy.MM"/>
				    	<c:if test="${edu.endTime != null }">
					    	 - <fmt:formatDate value="${edu.endTime }" pattern="yyyy.MM"/>
					    	，
				    	</c:if>
				    	${edu.degree }</div>
				    </div>
				    <div class="info-right">${edu.department }</div>
			    </div>
		    </c:forEach>
		    <div class="clear"></div>
		</div>
		
		<div class="clear"></div>
	</div>
	<div class="clear"></div>
		    