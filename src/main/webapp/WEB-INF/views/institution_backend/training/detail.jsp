<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
	<title><fmt:message key="institute.common.scholarEvent"/>-${institution.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
	<div class="container">
		<jsp:include page="../leftMenu.jsp"></jsp:include>
		<div class="ins_backend_rightContent">
			<h4 class="detail">${training.studentName }</h4>
			<div class="form-horizontal">
				<div class="control-group">
	       			<label class="control-label">国籍：</label>
	       			<div class="controls padding">
	         			<c:out value="${training.nationality }"/>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">学位：</label>
	       			<div class="controls padding">
	         			${degrees[training.degree].val}
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">入学时间：</label>
	       			<div class="controls padding">
	         			<c:out value="${training.enrollmentDate }"/>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">毕业时间：</label>
	       			<div class="controls padding">
	         			<c:if test="${training.graduationDate eq '3000-01-01'}">
	         				至今
	         			</c:if>
	         			<c:if test="${!(training.graduationDate eq '3000-01-01')}">
	         				${training.graduationDate}
	         			</c:if>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">专业：</label>
	       			<div class="controls padding">
	         			<c:out value="${training.major }"/> 
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">班级：</label>
	       			<div class="controls padding">
	         			<c:out value="${training.className }"/> 
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">导师：</label>
	       			<div class="controls padding">
	         			${memberMap[training.umtId].trueName }
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">导师部门：</label>
	       			<div class="controls padding">
	         			<c:out value="${deptMap[training.departId].shortName }"/>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">备注：</label>
	       			<div class="controls padding">
	         			<c:out value="${training.remark }"/> 
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls padding">
	         			<a class="btn btn-primary" href="<dhome:url value="/institution/${domain}/backend/training/edit/${training.id }?returnPage=${returnPage }"/>">编辑</a>
						<a id="deleteTraining" class="btn" href="<dhome:url value="../delete?id[]=${training.id }&page=${returnPage }"/>">删除</a>
						<a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a>
	       			</div>
	       		</div>
				
			</div>
	  </div>
	</div>  
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script>
		$(document).ready(function(){
			$('#deleteTraining').on('click',function(){
				return (confirm("学生删除以后不可恢复，确认删除吗？"));
			});
			//左栏置为选中
			$('#trainingMenu').addClass('active');
		});
	</script>
</html>

