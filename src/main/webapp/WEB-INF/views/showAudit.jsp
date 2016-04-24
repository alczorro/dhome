<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
		<c:choose>
			<c:when test="${titleUser.status=='auditNeed' }">
			<label id="auditStatus" class="status" rel="tooltip" data-placement="bottom" data-original-title="<fmt:message key='audit.auditNeed.tooltip'/>">
			(<fmt:message key="audit.auditNeed"/>)</label>
			</c:when>
			<c:when test="${titleUser.status=='auditIng' }">
			<label id="auditStatus" class="status" rel="tooltip" data-placement="bottom" data-original-title="<fmt:message key='audit.auditIng.tooltip'/>">
			(<fmt:message key="audit.auditIng"/>)</label>
			</c:when>
			<c:when test="${titleUser.status=='auditNot' }">
			<label id="auditStatus" class="status" rel="tooltip" data-placement="bottom" data-original-title="<fmt:message key='audit.auditNot.tooltip'/>${titleUser.auditPropose }<fmt:message key='audit.auditNotGuide.tooltip'/>">
			(<fmt:message key="audit.auditNot"/>)</label>
			<button id="submitAudit" class="btn btn-mini btn-primary" onclick="audit()"><fmt:message key="audit.submit"/></button>
			</c:when>
			<c:when test="${titleUser.status=='auditOK' }">
			<label id="auditStatus" class="status" rel="tooltip" data-placement="bottom" data-original-title="<fmt:message key='audit.auditOK.tooltip'/>">
			(<fmt:message key="audit.auditOK"/>)</label>
			</c:when>
			<c:when test="${titleUser.status=='auditDelete' }">
			<label id="auditStatus" class="status" rel="tooltip" data-placement="bottom" data-original-title="<fmt:message key="audit.auditPropose"/>:${titleUser.auditPropose }">
			(<fmt:message key="audit.auditDelete"/>)</label>
			</c:when>
			<c:otherwise>
			<label id="auditStatus" class="status" rel="tooltip" data-placement="bottom" data-original-title="<fmt:message key="audit.auditPropose"/>:${titleUser.auditPropose }">
			(<fmt:message key="audit.auditUnknown"/>)</label>
			</c:otherwise>
		</c:choose>
		<script>
		var map={
				auditNeed:"<fmt:message key='audit.auditNeed.tooltip'/>",
				auditIng:"<fmt:message key='audit.auditIng.tooltip'/>",
				auditNot:"<fmt:message key='audit.auditNot.tooltip'/>${titleUser.auditPropose }<fmt:message key='audit.auditNotGuide.tooltip'/>",
				auditOK:"<fmt:message key='audit.auditOK.tooltip'/>",
				get:function(key){
					return this[key];
				}
		}
		function audit(){
			$.ajax({
				 url : "<dhome:url value='/people/${domain}/submitAudit'/>",
				 type : "POST",
				 success : function(data){
					if(data){
						alert('<fmt:message key="audit.submitSuccess"/>');
						$('#auditStatus').html('(<fmt:message key="audit.auditIng"/>)');
						$('#auditStatus').attr('data-original-title',map.get('auditIng'));
						$('#submitAudit').hide();
					}else{
						alert('<fmt:message key="audit.submitError"/>');
					}
				 }
			 });
		}
		</script>