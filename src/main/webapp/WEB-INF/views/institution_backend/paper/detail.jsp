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
	<link rel="stylesheet" href="<dhome:url value="/resources/css/lightbox.css"/>" type="text/css" media="screen" />
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
	<div class="container">
		<jsp:include page="../leftMenu.jsp"></jsp:include>
		<div class="ins_backend_rightContent">
			<h4 class="detail">	
				${paper.title }<c:if test="${paper.status==1 }"><i class="icon icon-ok"></i></c:if>
				<p class="subManage">
					<c:if test="${paper.clbId!=0 }">
						<a class="btn btn-mini btn-info" href="<dhome:url value="/system/file?fileId=${paper.clbId }"/>" target="_blank">原文下载</a>
						</c:if>
						<c:if test="${!empty paper.originalLink }">
						<a class="btn btn-mini btn-link" href="<c:out value="${paper.originalLink }"/>" target="_blank">原文链接</a>
						</c:if>
					<span class="citation">【被引频次】<c:out value="${paper.citation == -1?'--':paper.citation }"/></span>
				</p>
			</h4>
			<div class="form-horizontal">
	       		<div class="control-group">
	       			<label class="control-label">doi：</label>
	       			<div class="controls padding">
						<c:out value="${paper.doi }"/> 
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">ISSN / ISDN：</label>
	       			<div class="controls padding">
	         			<c:out value="${paper.issn }"/> 
	       			</div>
	       		</div>
	       		<hr>
	       		<div class="control-group">
	       			<label class="control-label">部门：</label>
	       			<div class="controls padding">
	         			<c:out value="${deptMap[paper.departId].shortName }"/> 
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">作者：</label>
	       			<div class="controls padding">
	         			<c:forEach items="${ authorMap[paper.id]}" var="author">
								<c:out value="${author.authorName }"/>  
								<sup>[${author.subscriptIndex }]</sup>  
						</c:forEach>
						<c:if test="${!empty authorMap[paper.id] && paper.authorAmount>fn:length(authorMap[paper.id]) }">等</c:if>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">作者单位：</label>
	       			<div class="controls padding">
	         			<c:forEach items="${ authorMap[paper.id]}" var="author">
								<c:out value="${author.authorCompany }"/><br/>
						</c:forEach>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">作者总人数：</label>
	       			<div class="controls padding">
	       				<c:if test="${paper.authorAmount!=0 }">
	       					<c:out value="${paper.authorAmount }"/> 
	       				</c:if>
	       				<c:if test="${paper.authorAmount==0 }">
	       					<c:out value="${ authorMap[paper.id].size()}"/> 
	       				</c:if>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">资助单位：</label>
	       			<div class="controls padding">
<%-- 	         			<c:out value="${copyright.year }"/> --%>
	       			</div>
	       		</div>
	       		<hr>
	       		<div class="control-group">
	       			<label class="control-label">刊物名称：</label>
	       			<div class="controls padding">
		       			<c:forEach items="${ pubs}" var="pub">
							<c:choose>
								<c:when test="${paper.publicationId==pub.id }">
									<c:out value="${ pub.pubName}"/>
								</c:when>
							</c:choose>
						</c:forEach>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">影响因子：</label>
	       			<div class="controls padding">
		       			<c:forEach items="${ pubs}" var="pub">
							<c:choose>
								<c:when test="${paper.publicationId==pub.id }">
									<c:out value="${ pub.ifs}"/>
								</c:when>
							</c:choose>
						</c:forEach>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">年，卷（期）：</label>
	       			<div class="controls padding">
	         			<c:out value="${paper.publicationYear}"/>,
	         			<c:out value="${paper.volumeNumber}"/>
	         			（<c:out value="${paper.series}"/>）  
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">起止页：</label>
	       			<div class="controls padding">
<%-- 	         			<c:out value="${paper.startPage}"/>- --%>
	         			<c:out value="${paper.publicationPage}"/>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">分类号：</label>
	       			<div class="controls padding">
	         			
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">关键字：</label>
	       			<div class="controls padding">
	         			<c:out value="${paper.keywordDisplay}"/>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">摘要：</label>
	       			<div class="controls padding">
	         			<c:out value="${paper.summary }"/> 
	       			</div>
	       		</div>
	       		<hr>
	       		<div class="control-group">
	       			<label class="control-label">学科方向：</label>
	       			<div class="controls padding">
<%-- 	         			<c:out value="${paper.disciplineOrientationId}"/> --%>
	         			${disciplineOrientations[paper.disciplineOrientationId].val}
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">引用次数查询时间：</label>
	       			<div class="controls padding">
	         			<c:out value="${paper.citationQueryTime}"/>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">年度奖励标示：</label>
	       			<div class="controls padding">
	         			<c:out value="${paper.annualAwardMarks}"/>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">绩效计算年份：</label>
	       			<div class="controls padding">
	         			<c:out value="${paper.performanceCalculationYear}"/>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls padding">
	       				<c:if test="${paper.status!=1}">
	       					<a class="btn btn-success" href="<dhome:url value="/institution/${domain}/backend/paper/identify/${paper.id }?status=1"/>"><i class="icon icon-white icon-ok"></i> 认证该论文</a>
	         			</c:if>
	         			<c:if test="${paper.status==1}">
	         				<a class="btn btn-success" href="<dhome:url value="/institution/${domain}/backend/paper/identify/${paper.id }?status=-1"/>"><i class="icon icon-white icon-ok"></i> 取消认证</a>
	         			</c:if>
	         			<a class="btn btn-primary" href="<dhome:url value="/institution/${domain}/backend/paper/update/${paper.id }?returnPage=${returnPage }"/>">编辑</a>
						<a id="deletePaper" class="btn" data-url="<dhome:url value="../delete?paperId[]=${paper.id }&page=${returnPage }"/>">删除</a>
						<a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a>
	       			</div>
	       		</div>
			</div>	
				
			</div>
	</div>
	
	
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/jquery/jquery.lightbox.js"/>" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			window.name=document.referrer;
			$('#deletePaper').on('click',function(){
				if(confirm("论文删除以后不可恢复，确认删除吗？")){
					$.get($(this).data('url')).done(function(data){
						if(data.success){
							window.location.href='<dhome:url value="/institution/${domain}/backend/paper/list/${returnPage }"/>'
						}else{
							alert(data.desc);
						}
					});
				}
			});
			$('#paperMenu').addClass('active');
		});
	</script>
</html>