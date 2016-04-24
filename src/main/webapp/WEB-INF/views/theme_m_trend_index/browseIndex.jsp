<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
	<title>${page.title }-${titleUser.zhName }<fmt:message key="common.index.title"/></title>
	<jsp:include page="../meta-content.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../commonheaderCss.jsp"></jsp:include>
	<link href="<dhome:url value='/resources/css/theme_m_trend.css'/>" rel="stylesheet" type="text/css"/>
</head>

<body class="dHome-body trend" data-offset="50" data-target=".subnav"
	data-spy="scroll">
	<c:if test="${fromRegist}">
		<div class="cover-wholepage">
			<div class="show-text">
				<fmt:message key="common.createSuccess"/><br><br>
				<button class="btn btn-primary btn-large btn-set-large btn-slarge"><fmt:message key="common.clickToHome"/></button>
			</div>
		</div>
	</c:if>
	<jsp:include page="../commonBanner.jsp"></jsp:include>
	<div class="container trend-skin" id="homepagebody">
		<div class="row-fluid center">
			<jsp:include page="browseCommonLeft.jsp"></jsp:include>
			<div class="span9">
			<!-- 这是首页 -->
			<c:if test="${!empty isIndex}">
				<jsp:include page="browseCommonTop.jsp"></jsp:include>
			</c:if>
			<c:choose>
				<c:when test="${tab=='showPage' }">
					<div id="mainSpan">
					<c:choose>
						<c:when test="${!empty isIndex}">
						<div class="homepage">
							<div>
								<div class="page-header"><h4>
								<c:choose>
							 	<c:when test="${page.keyWord=='index'}">
							 		<c:choose>
									 		<c:when test="${page.title=='首页' }">
									 			<fmt:message key="adminIndex.recentNews"/>
									 		</c:when>
									 		<c:otherwise>
									 			${page.title }
									 		</c:otherwise>
								 		</c:choose>
							 	</c:when>
							 	<c:otherwise>${page.title }</c:otherwise>
							 </c:choose>
							 <c:if test="${isSelf }">
								 <a class="edit-personal" href='<dhome:url value="/people/${domain}/admin/p/${page.keyWord }?func=edit&pid=${page.id }"/>'><fmt:message key="common.edit"/></a>
							 </c:if>	
							 	</h4></div>
								<div class="page-container">
									${page.content}
								</div>
							</div>
							<c:if test="${paperStatus == 'show' }">
							<div>
								<div class="page-header"><h4><fmt:message key="common.paper.title"/>
								<c:if test="${isSelf }">
									<a class="edit-personal" <c:if test="${flag==1 }"> href='<dhome:url value='/people/${domain}/admin/commonPaper'/>' </c:if> <c:if test="${flag==2 }">href='<dhome:url value='/people/${domain}/admin/paper/list/1'/>'</c:if>>
									<fmt:message key="common.edit"/></a>
								</c:if>
								</h4></div>
								<div class="page-container">
									<c:if test="${flag==1 }">
									<ol class="paper-list paper-list-show">
								    	<c:forEach items="${fivePapers }" var="paper">
								        <li paper_id="${paper.id }">
							        		<span class="paper-title">
							        			<c:choose>
											        <c:when test="${paper.paperURL != null && paper.paperURL !='' }">
											        	<a target="_blank" href="${paper.paperURL }">${paper.title }</a>
											        </c:when>
											        <c:otherwise>
											        	${paper.title }
											        </c:otherwise>
										        </c:choose>
										    	<span class="count"></span>
										    </span>
										    <div class="clear"></div>
							        		<span class="paper-author">${paper.authors }.</span>
							        		<c:choose>
								        		<c:when test="${paper.source!=null && paper.source !=''}">
								        			<span class="paper-source">${paper.source }:</span>
								        		</c:when>
							        		</c:choose>
							        		<c:if test="${paper.publishedTime != null && paper.publishedTime !='' }">
							        			<span class="paper-time">${paper.publishedTime}</span>
							        		</c:if>
							        		<c:if test="${paper.volumeIssue !=null && paper.volumeIssue.trim() != ''}">
							        			<span class="paper-volume">,${paper.volumeIssue }</span>
							        		</c:if>
							        		<c:if test="${paper.pages !=null && paper.pages.trim() != ''}">
							        			<span class="paper-pages">,${paper.pages }</span>
							        		</c:if>
							        		<c:if test="${paper.summary!=null && paper.summary!='' }">
										        <a class="summary link"><fmt:message key="common.summary"/></a>&nbsp;
									        </c:if>
							        		<c:if test="${paper.clbId > 0 }">
							        		<span class="bdownfull"><a href="<dhome:url value='/system/download/${paper.clbId}'/>"><fmt:message key='addNewPaper.downloadFulltext'/></a></span>
								        	</c:if>
								        	<c:if test="${paper.summary!=null && paper.summary!='' }">
							       				<span class="summary-content" style="display:none">${paper.summary}</span>
									        </c:if>
								        </li>
								        </c:forEach>
								    </ol>
								    <c:if test="${totalPaperCount>5 }">
								        <a class="small-font" href="<dhome:url value='/people/${domain}/paper.dhome'/>"><fmt:message key="addSearchPaper.morePaper"/></a>
								    </c:if>
								    <c:if test="${totalPaperCount == 0 }">
								    	<p><fmt:message key="theme.browsePaper.noPaper"/></p>
								    </c:if>
								    </c:if>
								    <c:if test="${flag==2 }">
								    	 <ol id="IAPPapers" class="listShow">
											<c:forEach items="${IAPPaper }" var="data">
												<li>
												<span class="title"><c:out value="${data.title }"/></span>
												<div class="clear"></div>
												<c:choose>
													<c:when test="${empty authorMap[data.id]}">
														--
													</c:when>
													<c:otherwise>
														<c:forEach items="${ authorMap[data.id]}" var="author">
															<a class="authorDetailPopover"  data-paper-id="${data.id }" data-author-id="${author.id }" ><c:out value="${author.authorName }"/></a>
																<sup>[${author.subscriptIndex }]</sup>
														</c:forEach>
													</c:otherwise>
												</c:choose>
												<div class="clear"></div>
												<span class="detail">
													<!-- doi -->
													<c:if test="${!empty data.doi }">
														dio:<c:out value="${data.doi }"/>. 
													</c:if>
													<!-- issn -->
													<c:if test="${!empty data.issn }">
														issn:<c:out value="${data.issn }"/>. 
													</c:if>
													<!-- 刊物 -->
													<c:if test="${data.publicationId!=0 }">
														<c:out value="${pubMap[data.publicationId].pubName }"/>
													</c:if>
													<!-- 卷号 -->
													<c:if test="${!empty data.volumeNumber }">
														卷:<c:out value="${data.volumeNumber }"/>. 
													</c:if>
													<!-- 期号 -->
													<c:if test="${!empty data.series }">
														期:<c:out value="${data.series }"/>. 
													</c:if>
													<!-- 开始页~结束页 -->
													<c:if test="${!empty data.publicationPage }">
														页:${data.publicationPage}. 
													</c:if>
						<!-- 									开始页~结束页 -->
						<%-- 									<c:if test="${!empty data.startPage||!empty data.endPage }"> --%>
						<%-- 										页:${data.startPage}~${data.endPage==0?'无':data.endPage}.  --%>
						<%-- 									</c:if> --%>
													<!-- 发表时间 年.月 -->
													出版年:${data.publicationYear}<c:if test="${data.publicationMonth!=0 }">.${data.publicationMonth}</c:if>. 
												</span>
													<!-- 原文链接 -->
													<c:if test="${!empty data.originalLink }">
														<a href="<c:out value="${data.originalLink }"/>" target="_blank">原文链接</a>
													</c:if>
													<!-- 原文下载 -->
													<c:if test="${data.clbId!=0 }">
														<a href="<dhome:url value="/system/file?fileId=${data.clbId }"/>" target="_blank">原文下载</a>
													</c:if>
													<!-- 摘要 -->
													<c:if test="${!empty data.summary}">
<%-- 														<a data-paper-id="${data.id }" class="showSummary">查看摘要</a> --%>
														<a class="summary link"><fmt:message key="common.summary"/></a>&nbsp;
													</c:if>
													
													<div id="summary_${data.id }" class="summary-content" style="display:none"><c:out value="${data.summary }"/></div>
												</li> 
											</c:forEach>
										</ol>
										<c:if test="${empty IAPPaper}">
										 	<p><fmt:message key="theme.browsePaper.noPaper"/></p>
										</c:if>
								    </c:if>
								</div>
							</div>
							</c:if>
							<jsp:include page="../userAccessInfo.jsp"></jsp:include>
						</div>
						</c:when>
						<c:otherwise>
						<div class="otherpage">
							<div class="page-header">
							<h2>${page.title }
							<c:if test="${isSelf }">
								<a class="edit-personal" href="<dhome:url value="/people/${domain}/admin/p/${page.keyWord }?func=edit&pid=${page.id }"/>"><fmt:message key="common.edit"/></a>
							</c:if>
							</h2></div>
							<div class="page-container">
								${page.content}
							</div>
						</div>
						</c:otherwise>
					</c:choose>
					</div>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
			</div>
		</div>
	</div>
	<jsp:include page="../commonheader.jsp"></jsp:include>
	<jsp:include page="../browseIndexShareHtml.jsp"></jsp:include>
	<jsp:include page="../commonfooter.jsp"></jsp:include>
</body>
<dhome:InitLanuage useBrowserLanguage="true"/>


</html>