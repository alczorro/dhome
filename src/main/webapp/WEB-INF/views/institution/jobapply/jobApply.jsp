<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
<dhome:InitLanuage useBrowserLanguage="true"/>
<title>${user.zhName }<fmt:message key="common.dhome"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${user.zhName }<fmt:message key="common.dhome"/>
			<jsp:include page="../../showAudit.jsp"/>
		</div>
		<jsp:include page="../../commonMenu.jsp">
			<jsp:param name="activeItem" value="achievement" />
		</jsp:include>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title">
			<h3>岗位评定管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="jobapply" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
					<h4 class="detail">${applicationMap[applicationId].title }</h4>
					<form id="editForm" class="form-horizontal" method="post" action="<dhome:url value="/people/admin/training/creat"/>">
					<input type="hidden" name="id" id="id" value="${empty jobApply.id?0:jobApply.id }"/>
					<input type="hidden" name="applicationId" id="applicationId" value="${applicationId }"/>
					
					<div class="control-group">
		         			<strong>${user.zhName},${member.birth}<c:if test="${member.sex=='male'}">，男</c:if><c:if test="${member.sex=='female'}">，女</c:if></strong>
		       		</div>
		       		<div class="control-group">
		       			应聘岗位：
	         			<select id="jobId" name="jobId">
         			         <option value="1" <c:if test="${jobApply.jobId==1 }">selected="selected"</c:if>>研究员</option>
         			         <option value="2" <c:if test="${jobApply.jobId==2 }">selected="selected"</c:if>>副研究员</option>
         			         <option value="3" <c:if test="${jobApply.jobId==3 }">selected="selected"</c:if>>正研级高工</option>
         			         <option value="4" <c:if test="${jobApply.jobId==4 }">selected="selected"</c:if>>高级工程师</option>
         			         <option value="5" <c:if test="${jobApply.jobId==5 }">selected="selected"</c:if>>编审</option>
         			         <option value="6" <c:if test="${jobApply.jobId==6 }">selected="selected"</c:if>>副编审</option>
         			         <option value="7" <c:if test="${jobApply.jobId==7 }">selected="selected"</c:if>>高级实验师</option>
						</select>
		       		</div>
		       		<div class="control-group">
		       			本人学习工作经历（从大学或大专写起）：
		       			<div class="applyDiv applyAuto">
		         			<ul class="x-list work no-border">
								<c:choose>
									<c:when test="${works.size()>0}">
								    	<c:forEach items="${works }" var="work">
								    		<li id="${work.id}">
								    			<span class="institutionZhName" title="${work.institutionName }">
									    			<c:if test="${!empty work.domain}">
										    			<a class="aliasLink" target='_blank' href="<dhome:url  value='/institution/${work.domain }/index.html'/>">${work.aliasInstitutionName}</a>
										    		</c:if>
										    		<c:if test="${empty work.domain }">
										    			${work.aliasInstitutionName}
										    		</c:if>
								    			</span>
								    			<span class="department">${work.department}</span>
								    			<c:if test="${!empty work.department}"> ,</c:if>
								    			
								    			<span class="position">${work.position}</span> | 
								    			<span class="beginTime"><fmt:formatDate value="${work.beginTime}" pattern="yyyy.MM"/></span>-
								    			<span class="endTime">
									    			<c:choose>
									    				<c:when test="${work.endTime == '3000-01-01 00:00:00.0'}">
									    					<fmt:message key='personalWorkInfo.untilnow'/>
									    				</c:when>
									    				<c:otherwise>
									    					<fmt:formatDate value="${work.endTime}" pattern="yyyy.MM"/>
									    				</c:otherwise>
									    			</c:choose>
								    			</span>
								    			<c:if test="${currentUser.salutation eq work.position }">
								    				<span class="salutation" style="display:none"></span>
								    			</c:if>
								    		</li>
								    	</c:forEach>
								    </c:when>
								    
								</c:choose>
						    <c:if test="${educations.size()>0}">
					    		<c:forEach items="${educations }" var="education">
					    		<li id="${education.id}">
					    			<span class="institutionZhName" title="${education.institutionName }">
					    				<c:if test="${!empty education.domain}">
							    			<a class="aliasLink" target='_blank' href="<dhome:url value='/institution/${education.domain }/index.html'/>">
							    				${education.aliasInstitutionName}
							    			</a>
							    		</c:if>
							    		<c:if test="${empty education.domain }">
							    			${education.aliasInstitutionName}
							    		</c:if>
							    	</span>
					    			<span class="department">${education.department}</span>,
					    			<span class="degree">
					    			<c:if test="${(education.degree eq '本科')||(education.degree eq 'Banchelor')}">
					    				<fmt:message key="personalEducationInfo.banchelor"/>
					    			</c:if>
					    			<c:if test="${(education.degree eq '硕士研究生')||(education.degree eq 'Master')}">
					    				<fmt:message key="personalEducationInfo.master"/>
					    			</c:if>
					    			<c:if test="${(education.degree eq '博士研究生')||(education.degree eq 'Doctor')}">
					    				<fmt:message key="personalEducationInfo.doctor"/>
					    			</c:if>
					    			</span> | 
					    			<span class="beginTime"><fmt:formatDate value="${education.beginTime}" pattern="yyyy.MM"/></span>-
					    			<span class="endTime">
						    			<c:choose>
						    				<c:when test="${education.endTime == '3000-01-01 00:00:00.0' }">
						    					<fmt:message key='personalWorkInfo.untilnow'/>
						    				</c:when>
						    				<c:otherwise>
						    					<fmt:formatDate value="${education.endTime}" pattern="yyyy.MM"/>
						    				</c:otherwise>
						    			</c:choose>
					    			</span>
					    		</li>
					    		</c:forEach>
					    	</c:if>
						</ul>
		       			</div>
		       		</div>
		       		<div class="control-group">
		       			任现岗位以来的主要工作业绩（不超过1500字）：
		         		<div class="applyDiv">
		         			<textarea rows="6" id="jobPerformance" name="jobPerformance"  style="width:98%">${jobApply.jobPerformance }</textarea>
		         		</div>
		       		</div>
		       		<div class="control-group">
		       			近五年以来第一作者论文论著发表清单：（通讯作者、本所研究生在读期间和导师互为第一、第二作者的论文，均作为第一作者论文；按AAS参考文献格式填写；详细要求请参见附件）
		       			<div class="applyDiv applyAuto">
		       				<ul>
	       				<c:choose>
							<c:when test="${empty firstPapers}">
								暂无
							</c:when>
							<c:otherwise>
		       				<%  int No1=0; %>
		         			<c:forEach items="${firstPapers }" var="data" varStatus="status">
		         			<c:if test="${pubsMap[data.publicationId].publicationType eq 'SCI' }">
								<li>
									<span class="article">
										<%
										No1+=1;
										  request.setAttribute("No1",No1);
										%>
										(${No1})
		<!-- 								作者    					-->
										<c:choose>
											<c:when test="${empty authorMap[data.id]}">
												--
											</c:when>
											<c:otherwise>
												<c:forEach items="${ authorMap[data.id]}" var="author">
													<c:if test="${empty authors[author.id]}">
														<c:out value="${author.authorName }"/>，
													</c:if>
													<c:if test="${!empty authors[author.id]}">
														<b><c:out value="${author.authorName }"/></b>，
													</c:if>
												</c:forEach>
											</c:otherwise>
										</c:choose>
										<!-- 发表时间 年.月 -->
										${data.publicationYear}<c:if test="${data.publicationMonth!=0 }">.${data.publicationMonth}</c:if>: 
										<c:out value="${data.title }"/>.
										<!-- 刊物 -->
										<c:if test="${data.publicationId!=0 }">
											<c:out value="${pubsMap[data.publicationId].pubName }"/>,
										</c:if>
										<br/>
										<!-- 卷号 -->
										<c:if test="${!empty data.volumeNumber }">
											<c:out value="${data.volumeNumber }"/> 
										</c:if>
										<!-- 期号 -->
										<c:if test="${!empty data.series }">
											(<c:out value="${data.series }"/>), 
										</c:if>
										<!-- 开始页~结束页 -->
										<c:if test="${!empty data.publicationPage }">
											${data.publicationPage}, 
										</c:if>
										<!-- doi -->
										<c:if test="${!empty data.doi }">
											doi:<c:out value="${data.doi }"/>. &nbsp;
										</c:if>
										<c:if test="${pubsMap[data.publicationId].publicationType eq 'SCI' }">
											【SCI】
										</c:if>
										<c:forEach items="${ authorMap[data.id]}" var="author">
											<c:if test="${!empty authors[author.id]}">
												<c:if test="${ author.order==2}">
												<c:if test="${ author.authorStudent==true}">【学生在读】</c:if>
												<c:if test="${ author.authorTeacher==true}">【学生在读】</c:if>
											</c:if>
											<c:if test="${ author.communicationAuthor==true}">【通讯作者】</c:if>
											</c:if>
										</c:forEach>
										
										
									</span>
								</li>
								</c:if>
							</c:forEach>
							
							<c:forEach items="${firstPapers }" var="data" varStatus="status">
		         			<c:if test="${!(pubsMap[data.publicationId].publicationType eq 'SCI') }">
								<li>
									<span class="article">
										<%
											No1+=1;
										    request.setAttribute("No1",No1);
										%>
										(${No1})
		<!-- 								作者    					-->
										<c:choose>
											<c:when test="${empty authorMap[data.id]}">
												--
											</c:when>
											<c:otherwise>
												<c:forEach items="${ authorMap[data.id]}" var="author">
													<c:if test="${empty authors[author.id]}">
														<c:out value="${author.authorName }"/>，
													</c:if>
													<c:if test="${!empty authors[author.id]}">
														<b><c:out value="${author.authorName }"/></b>，
													</c:if>
												</c:forEach>
											</c:otherwise>
										</c:choose>
										<!-- 发表时间 年.月 -->
										${data.publicationYear}<c:if test="${data.publicationMonth!=0 }">.${data.publicationMonth}</c:if>: 
										<c:out value="${data.title }"/>.
										<!-- 刊物 -->
										<c:if test="${data.publicationId!=0 }">
											<c:out value="${pubsMap[data.publicationId].pubName }"/>,
										</c:if>
										<br/>
										<!-- 卷号 -->
										<c:if test="${!empty data.volumeNumber }">
											<c:out value="${data.volumeNumber }"/> 
										</c:if>
										<!-- 期号 -->
										<c:if test="${!empty data.series }">
											(<c:out value="${data.series }"/>), 
										</c:if>
										<!-- 开始页~结束页 -->
										<c:if test="${!empty data.publicationPage }">
											${data.publicationPage}, 
										</c:if>
										<!-- doi -->
										<c:if test="${!empty data.doi }">
											doi:<c:out value="${data.doi }"/>. &nbsp;
										</c:if>
										<c:if test="${pubsMap[data.publicationId].publicationType eq 'SCI' }">
											【SCI】
										</c:if>
										<c:forEach items="${ authorMap[data.id]}" var="author">
											<c:if test="${!empty authors[author.id]}">
												<c:if test="${ author.order==2}">
												<c:if test="${ author.authorStudent==true}">【学生在读】</c:if>
												<c:if test="${ author.authorTeacher==true}">【学生在读】</c:if>
											</c:if>
											<c:if test="${ author.communicationAuthor==true}">【通讯作者】</c:if>
											</c:if>
										</c:forEach>
										
									</span>
								</li>
								</c:if>
							</c:forEach>
						</c:otherwise>
						</c:choose>
							</ul>
		       			</div>
		       		</div>
<!-- 		       		其他论文 			-->
		       		<div class="control-group">
		       			其他论文论著发表清单（详细要求请参见附件）：
		       			<div class="applyDiv applyAuto">
		       				<ul>
	       				<c:choose>
							<c:when test="${empty otherPapers}">
								暂无
							</c:when>
							<c:otherwise>
		       				<%  int No2=0; %>
		         			<c:forEach items="${otherPapers }" var="data" varStatus="status">
		         			<c:if test="${pubsMap[data.publicationId].publicationType eq 'SCI' }">
								<li>
									<span class="article">
										<%
											No2+=1;
										  request.setAttribute("No2",No2);
										%>
										(${No2})
		<!-- 								作者    					-->
										<c:choose>
											<c:when test="${empty otherAuthorMap[data.id]}">
												--
											</c:when>
											<c:otherwise>
												<c:forEach items="${ otherAuthorMap[data.id]}" var="author">
													<c:if test="${empty authors[author.id]}">
														<c:out value="${author.authorName }"/>，
													</c:if>
													<c:if test="${!empty authors[author.id]}">
														<b><c:out value="${author.authorName }"/></b>，
													</c:if>
												</c:forEach>
											</c:otherwise>
										</c:choose>
										<!-- 发表时间 年.月 -->
										${data.publicationYear}<c:if test="${data.publicationMonth!=0 }">.${data.publicationMonth}</c:if>: 
										<c:out value="${data.title }"/>.
										<!-- 刊物 -->
										<c:if test="${data.publicationId!=0 }">
											<c:out value="${pubsMap[data.publicationId].pubName }"/>,
										</c:if>
										<!-- 卷号 -->
										<c:if test="${!empty data.volumeNumber }">
											<c:out value="${data.volumeNumber }"/> 
										</c:if>
										<!-- 期号 -->
										<c:if test="${!empty data.series }">
											(<c:out value="${data.series }"/>), 
										</c:if>
										<!-- 开始页~结束页 -->
										<c:if test="${!empty data.publicationPage }">
											${data.publicationPage}, 
										</c:if>
										<!-- doi -->
										<c:if test="${!empty data.doi }">
											doi:<c:out value="${data.doi }"/>. &nbsp;
										</c:if>
										<c:if test="${pubsMap[data.publicationId].publicationType eq 'SCI' }">
											【SCI】
										</c:if>
<%-- 										<c:forEach items="${ otherAuthorMap[data.id]}" var="author"> --%>
<%-- 											<c:if test="${!empty authors[author.id]}"> --%>
<%-- 												<c:if test="${ author.order==2}"> --%>
<%-- 												<c:if test="${ author.authorStudent==true}">【学生在读】</c:if> --%>
<%-- 												<c:if test="${ author.authorTeacher==true}">【学生在读】</c:if> --%>
<%-- 											</c:if> --%>
<%-- 											<c:if test="${ author.communicationAuthor==true}">【通讯作者】</c:if> --%>
<%-- 											</c:if> --%>
<%-- 										</c:forEach> --%>
										
									</span>
								</li>
								</c:if>
							</c:forEach>
							
							<c:forEach items="${otherPapers }" var="data" varStatus="status">
		         			<c:if test="${!(pubsMap[data.publicationId].publicationType eq 'SCI') }">
								<li>
									<span class="article">
										<%
											No2+=1;
										  request.setAttribute("No2",No2);
										%>
										(${No2})
		<!-- 								作者    					-->
										<c:choose>
											<c:when test="${empty otherAuthorMap[data.id]}">
												--
											</c:when>
											<c:otherwise>
												<c:forEach items="${ otherAuthorMap[data.id]}" var="author">
													<c:if test="${empty authors[author.id]}">
														<c:out value="${author.authorName }"/>，
													</c:if>
													<c:if test="${!empty authors[author.id]}">
														<b><c:out value="${author.authorName }"/></b>，
													</c:if>
												</c:forEach>
											</c:otherwise>
										</c:choose>
										<!-- 发表时间 年.月 -->
										${data.publicationYear}<c:if test="${data.publicationMonth!=0 }">.${data.publicationMonth}</c:if>: 
										<c:out value="${data.title }"/>.
										<!-- 刊物 -->
										<c:if test="${data.publicationId!=0 }">
											<c:out value="${pubsMap[data.publicationId].pubName }"/>,
										</c:if>
										<!-- 卷号 -->
										<c:if test="${!empty data.volumeNumber }">
											<c:out value="${data.volumeNumber }"/> 
										</c:if>
										<!-- 期号 -->
										<c:if test="${!empty data.series }">
											(<c:out value="${data.series }"/>), 
										</c:if>
										<!-- 开始页~结束页 -->
										<c:if test="${!empty data.publicationPage }">
											${data.publicationPage}, 
										</c:if>
										<!-- doi -->
										<c:if test="${!empty data.doi }">
											doi:<c:out value="${data.doi }"/>. &nbsp;
										</c:if>
										<c:if test="${pubsMap[data.publicationId].publicationType eq 'SCI' }">
											【SCI】
										</c:if>
<%-- 										<c:forEach items="${ otherAuthorMap[data.id]}" var="author"> --%>
<%-- 											<c:if test="${ author.order==2}"> --%>
<%-- 												<c:if test="${ author.authorStudent==true}">【学生在读】</c:if> --%>
<%-- 												<c:if test="${ author.authorTeacher==true}">【学生在读】</c:if> --%>
<%-- 											</c:if> --%>
<%-- 											<c:if test="${ author.communicationAuthor==true}">【通讯作者】</c:if>	 --%>
<%-- 										</c:forEach> --%>
										
										
									</span>
								</li>
								</c:if>
							</c:forEach>
							</c:otherwise>
							</c:choose>
							</ul>
		       			</div>
		       		</div>
<!-- 		       		论文统计 			-->
					<div class="control-group">
						论文论著发表情况统计：（申请研究员、副研究员必须填写；通讯作者、本所研究生在读期间和导师互为第一、第二作者的论文，均作为第一作者论文）
						<div class="applyDiv">
								正式发表学术论文 <u class="underLine">${papersCount } </u>篇，其中SCI论文<u class="underLine">${allSciCount }</u>篇（影响因子累计<u class="underLine">${allIf }</u>）；第一作者论文<u class="underLine">${allFirstCount }</u>篇，
							其中SCI论文<u class="underLine">${allFirstSciCount }</u>篇（影响因子累计<u class="underLine">${allFirstIf }</u>）。
								<br>近五年累计发表学术论文<u class="underLine">${firstPapers.size()+otherPapers.size() }</u>篇，其中SCI论文<u class="underLine">${firstSciCount+otherSciCount }</u>篇（影响因子累计<u class="underLine">${ifs }</u>）；第一作者论文<u class="underLine">${firstPapers.size() }</u>篇，
							其中SCI论文<u class="underLine">${firstSciCount }</u>篇（影响因子累计<u class="underLine">${firstIf }</u>）。
						</div>
					</div>
					
					<div class="control-group">
						近五年来承担的主要科研项目情况
						<div class="applyDiv">
							<ul class="listShow topic">
								<li class="title">
			<!-- 						<span class="num">编号</span>  -->
									<span class="name">项目名称</span>
			<!-- 						<span class="beginTime">开始<br>时间</span> -->
			<!-- 						<span class="endTime">结束<br>时间</span>  -->
									<span class="type">类别</span>
									<span class="source">项目经费</span>
			 						<span class="role">本人角色</span> 
			 						<span class="member">起止时间</span>
			<!-- 						<span class="selfFunds">个人经费</span> -->
								</li>
								<c:forEach items="${topics }" var="data">
									<li>
										<span class="name">
											<c:choose>
												<c:when test="${empty data.name }">
														--   
												</c:when>
												<c:otherwise>
													<c:out value="${data.name }"/>
												</c:otherwise>
											</c:choose>
										</span> 
										<span class="type">
											<c:choose>
												<c:when test="${data.type==0 }">
														--   
												</c:when>
												<c:otherwise>
													${types[data.type].val}
												</c:otherwise>
											</c:choose>
										</span>
										<span class="source">
											<c:choose>
												<c:when test="${data.project_cost==0 }">
														--   
												</c:when>
												<c:otherwise>
													${data.project_cost}
												</c:otherwise>
											</c:choose>
										</span>
										<span class="role">
											<c:choose>
												<c:when test="${data.role eq 'admin' }">负责人</c:when>
												<c:when test="${data.role eq 'member' }">参与人</c:when>
											</c:choose>
										</span>
										<span class="member">
											${data.start_year }<c:if test="${data.start_month!=0 }">.${data.start_month }</c:if> -
											<c:if test="${data.end_year!=3000 }">
						         				${data.end_year }<c:if test="${data.end_month!=0 }">-${data.end_month }</c:if>
						         			</c:if>
						         			<c:if test="${data.end_year==3000 }">
						         			至今
						         			</c:if>
										</span> 
									</li> 
								</c:forEach>
							</ul>
						</div>
					</div>
					
					<div class="control-group">
						专利与成果转化情况（成果转化包括发明专利、软件著作权登记、技术标准和技术转移等）
						<div class="applyDiv">
							<ul class="listShow">
								<li class="title">
									<span class="article">专利名称</span>
								</li>
								<c:forEach items="${patents }" var="data">
									<li>
										<span class="article">
											<c:choose>
												<c:when test="${empty data.name }">
														--   
												</c:when>
												<c:otherwise>
													<c:out value="${data.name }"/>
												</c:otherwise>
											</c:choose>
										</span> 
									</li> 
								</c:forEach>
								<c:forEach items="${copyrights }" var="data">
									<li>
										<span class="article">
											<c:choose>
												<c:when test="${empty data.name }">
														--   
												</c:when>
												<c:otherwise>
													<c:out value="${data.name }"/>
												</c:otherwise>
											</c:choose>
										</span> 
									</li> 
								</c:forEach>
							</ul>
						</div>
					</div>
					
					<div class="control-group">
						获科技奖励情况
						<div class="applyDiv">
							<ul class="listShow">
								<li class="title">
									<span class="awardName">年度</span>
									<span class="category">类别</span>
									<span class="achievementName">项目名称</span>
									 <span class="level">等级</span> 
									<span class="institutions">排名</span> 
								</li>
								<c:forEach items="${awards }" var="data">
									<li>
										<!-- 奖励名称 -->
										<span class="awardName">
											<c:choose>
											    <c:when test="${empty data.year }">
														--   
												</c:when> 
												<c:otherwise>
													<c:out value="${data.year}"/>
												</c:otherwise>
											</c:choose>
										</span>
										<!-- 类别 -->
										<span class="category">
											<c:choose>
											    <c:when test="${data.type==0 }">
														--   
												</c:when> 
												<c:otherwise>
													<c:out value="${awardTypes[data.type].val }"/>
												</c:otherwise>
											</c:choose>
										</span>
										<!-- 成果名称 -->
										<span class="achievementName">
									    	<c:choose>
										 		<c:when test="${empty data.name}">
														--   
												</c:when> 
												<c:otherwise>
													<c:out value="${data.name}"/>
												</c:otherwise>
											</c:choose>
										</span>
										<!-- 等级 -->
										<span class="level">
											<c:choose>
											    <c:when test="${data.grade==0 }">
														--   
												</c:when> 
												<c:otherwise>
													<c:out value="${awardGrades[data.grade].val}"/>
												</c:otherwise>
											</c:choose>
										</span>
										<!-- 授予单位 -->
										<span class="institutions">
											<c:choose>
											    <c:when test="${empty data.authorOrder }">
													--   
												</c:when> 
												<c:otherwise>
													<c:out value="${data.authorOrder }"/>
												</c:otherwise>
											</c:choose>
										</span>
			 						</li> 
								</c:forEach>
							</ul>
						</div>
					</div>
					
					<div class="control-group">
						国内外学术任职情况
						<div class="applyDiv">
							<ul class="listShow">
								<li class="title">
									<span class="orgname">组织名称</span>
									<span class="position">职位</span> 
									<span class="time">任职时间</span>
								</li>
								<c:forEach items="${academics }" var="data">
									<li>
										<span class="orgname">
									    	<c:choose>
												<c:when test="${ data.name==0 }">
														--   
												</c:when>
												<c:otherwise>
													${organizationNames[data.name].val}
												</c:otherwise>
											</c:choose>
										</span>
										<span class="position">
											<c:choose>
												<c:when test="${ data.position==0 }">
														--   
												</c:when>
												<c:otherwise>
													${positions[data.position].val}
												</c:otherwise>
											</c:choose>
										</span>
										<span class="time">
											<c:choose>
											 	<c:when test="${data.startYear== 0 && data.endYear==0}">
													--
												</c:when>
												<c:otherwise>
													<c:if test="${data.endYear==3000 }">
														${data.startYear }<c:if test="${data.startMonth!=0 }">.${data.startMonth }</c:if>-至今
													</c:if>
													<c:if test="${data.endYear!=3000 }">
														${data.startYear }<c:if test="${data.startMonth!=0 }">.${data.startMonth }</c:if>
														 - ${data.endYear }<c:if test="${data.endMonth!=0 }">.${data.endMonth }</c:if>
													</c:if>
												</c:otherwise>
											</c:choose>
										</span>
								</li> 
								</c:forEach>
							</ul>
						</div>
					</div>
					<div class="control-group">
		       			研究生培养情况（列出历届博士生硕士生和合作培养研究生名单，研究生部主任签字确认）
		       			<div class="applyDiv">
		       				<% int count1=0; %>
		         			<c:forEach items="${trainings }" var="training">
		         				<c:if test="${degrees[training.degree].val=='博士'}">
			         				<%
			         					count1+=1;
										request.setAttribute("count1",count1);
									%>
								</c:if>
		         			</c:forEach>
		         			共培养博士生<u class="underLine">${empty count1?0:count1 } </u>名，名单：<u class="underLine">
		         			<c:forEach items="${trainings }" var="training">
		         				<c:if test="${degrees[training.degree].val=='博士'}">
									[${training.studentName }]
								</c:if>
		         			</c:forEach></u>
		         			<% int count2=0; %>
		         			<c:forEach items="${trainings }" var="training">
		         				<c:if test="${degrees[training.degree].val=='硕士'}">
			         				<%
			         					count2+=1;
										request.setAttribute("count2",count2);
									%>
								</c:if>
		         			</c:forEach>。
		         			<br/>
		         			共培养硕士生<u class="underLine"> ${empty count2?0:count2 }</u> 名，名单：<u class="underLine">
		         			<c:forEach items="${trainings }" var="training">
		         				<c:if test="${degrees[training.degree].val=='硕士'}">
		         					[${training.studentName }]
								</c:if>
		         			</c:forEach></u>。
		       			</div>
		       		</div>
					<div class="control-group">
		       			其他需要说明的问题：
		       			<div class="applyDiv">
		         			<textarea rows="6" id="remark" name="remark" style="width:98%">${jobApply.remark }</textarea>
		       			</div>
		       		</div>
		       		<div class="control-group">
		       			<label class="control-label">&nbsp;</label>
		       			<div class="controls">
		         			<input type="button" id="submitBtn" value="提交" class="btn btn-primary"/>
<!-- 		         			<input type="button" id="saveBtn" value="保存" class="btn btn-primary"/> -->
		         			<a class="btn btn-link" href="/people/${domain}/admin/job/list/1">取消</a>
		       			</div>
	       			</div>
		       	</form>
				</div>
			</div>
		</div>
	</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){

			$('#saveBtn').on('click',function(){
				if($('#editForm').valid()){
					var data=$('#editForm').serialize();
// 					var tid=$('input:radio[name="checkItem"]:checked').val();
// 					data +='&studentId='+tid;
					
					$.post('/people/${domain}/admin/job/save/0',data).done(function(data){ 
						if(data.success){
							window.location.href='/people/${domain}/admin/job/list/1';
						}else{
							alert(data.desc);
						}
					});
				}
			});
			$('#submitBtn').on('click',function(){
				if($('#editForm').valid()){
					var data=$('#editForm').serialize();
// 					var tid=$('input:radio[name="checkItem"]:checked').val();
// 					data +='&studentId='+tid;
					
					$.post('/people/${domain}/admin/job/save/1',data).done(function(data){ 
						if(data.success){
							window.location.href='/people/${domain}/admin/job/list/1';
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
		
		});
	</script>
</html>