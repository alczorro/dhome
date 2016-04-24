<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<% 
	request.setAttribute("now", new Date().getYear()+1900); 
%>
<html lang="en">
<head>
	<title>查看成员信息</title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.css"/>" />
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/css/tokenInput.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/sample-css/page.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/css/dp.css'/>"/>
	<style>ul.token-input-list-facebook {clear:right}</style>
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
	
	<div class="container">
		<jsp:include page="../leftMenu.jsp"></jsp:include>
		<div class="ins_backend_rightContent">
			<h4 class="detail" style="border-bottom:none">
				<c:out value="${member.trueName }"/>的个人资料
				<a class="btn btn-mini rightFloat" onclick="javascript:history.go(-1);"><i class="icon icon-arrow-left"></i>&nbsp;返回</a>	
			</h4>
			<ul class="nav nav-tabs member" id="myTab">
				<li>&nbsp;</li>
				<li class="active">
				    <a href="#baseInfo" data-toggle="tab">
				    	基本信息
				    </a>
				</li>
				<li>
				    <a href="#educationist" data-toggle="tab">教育经历</a>
			    </li>
			   	<li>
				    <a href="#worklist" data-toggle="tab">工作经历</a>
			    </li>
			    <li>
				    <a href="#paper" data-toggle="tab">论文</a>
			    </li>
			    <li>
				    <a href="#treatise" data-toggle="tab">论著</a>
			    </li>
			    <li>
				    <a href="#award" data-toggle="tab">奖励</a>
			    </li>
			    <li>
				    <a href="#copyright" data-toggle="tab">软件著作权</a>
			    </li>
			    <li>
				    <a href="#patent" data-toggle="tab">专利</a>
			    </li>
			    <li>
				    <a href="#topic" data-toggle="tab">课题</a>
			    </li>
			    <li>
				    <a href="#academic" data-toggle="tab">学术任职</a>
			    </li>
			    <li>
				    <a href="#periodical" data-toggle="tab">期刊任职</a>
			    </li>
			    <li>
				    <a href="#training" data-toggle="tab">人才培养</a>
			    </li>
			</ul>
			
			<div class="tab-content">
			    <!-- 基本信息 -->
				<div class="tab-pane fade in active" id="baseInfo">
					<h4 class="member_title">基本信息</h4>
				    <table class="table member">
				    	<tr>
							<th>姓名：</th>
			       			<td><c:out value="${member.trueName }"/></td>
			       			<th>性别：</th>
			       			<td>
			         			<c:choose>
									<c:when test="${empty member.sex }">
										未知
									</c:when>
									<c:when test="${member.sex=='male' }">
										男
									</c:when>
									<c:when test="${member.sex=='female' }">
										女
									</c:when>
								</c:choose>
			       			</td>
			       		</tr>
			       		<tr>
			       			
			       			<th>部门：</th>
			       			<td><c:out value="${dept.name }"/></td>
			       			<th>职称：</th>
			       			<td>
			         			<c:out value="${member.technicalTitle }"/> 
			       			</td>
			       		</tr>
			       		<tr>
			       			<th>办公室地址：</th>
			       			<td>
			         			<c:out value="${member.officeAddress }"/>
			       			</td>
			       			<th>办公室电话：</th>
			       			<td>
			         			<c:out value="${member. officeTelephone}"/>
			       			</td>
			       		</tr>
			       		<tr>
			       			<th>手机：</th>
			       			<td>
			         			<c:out value="${member.mobilePhone}"/> 
			       			</td>
			       			<th>邮箱：</th>
			       			<td>${member.cstnetId }</td>
			       		</tr>
			       		<tr>
			       			<td colSpan="4" style="text-align:center">
			         			<a class="editInfo btn btn-primary">编辑</a> 
			       			</td>
			       		</tr>
					</table>	
					<h4 class="member_title">ARP导出信息</h4>
					<table class="table member">
				    	<tr>
							<th>姓名：</th>
			       			<td><c:out value="${member.trueName }"/></td>
			       			<th>性别：</th>
			       			<td>
			         			<c:choose>
									<c:when test="${empty member.sex }">
										未知
									</c:when>
									<c:when test="${member.sex=='male' }">
										男
									</c:when>
									<c:when test="${member.sex=='female' }">
										女
									</c:when>
								</c:choose>
			       			</td>
			       		</tr>
			       		<tr>
			       			<th>编号：</th>
			       			<td><c:out value="${member.sn }"/></td>
			       			<th>生日：</th>
			       			<td><c:out value="${member.birth }"/></td>
			       		</tr>
			       		<tr>
			       			<th>出生地：</th>
			       			<td>
			         			<c:out value="${member.birthPlace }"/> 
			       			</td>
			       			<th>年龄：</th>
			       			<td><c:out value="${member.age }"/></td>
			       		</tr>
			       		<tr>
			       			<th>职称：</th>
			       			<td>
			         			<c:out value="${member.technicalTitle }"/> 
			       			</td>
			       			<th>办公室地址：</th>
			       			<td>
			         			<c:out value="${member.officeAddress }"/>
			       			</td>
			       		</tr>
			       		<tr>
			       			<th>办公室电话：</th>
			       			<td>
			         			<c:out value="${member. officeTelephone}"/>
			       			</td>
			       			<th>手机：</th>
			       			<td>
			         			<c:out value="${member.mobilePhone}"/> 
			       			</td>
			       		</tr>
			       		<tr>
			       			<th>部门：</th>
			       			<td><c:out value="${dept.name }"/></td>
			       			<th>邮箱：</th>
			       			<td>${member.cstnetId }</td>
			       		</tr>
			       		<tr>
			       			<th>工龄：</th>
			       			<td><c:out value="${member.jobAge}"/></td>
			       			<th>员工类型：</th>
			       			<td><c:out value="${member.jobType }"/></td>
			       		</tr>
			       		<tr>
			       			<th>级别代码：</th>
			       			<td>${member.levelCode }</td>
			       			<th>岗位等级：</th>
			       			<td><c:out value="${member.jobLevel}"/></td>
			       		</tr>
			       		<tr>
			       			<th>工资薪级：</th>
			       			<td><c:out value="${member.salaryLevel }"/></td>
			       			<th>职工地址：</th>
			       			<td>${member.homeAddress }</td>
			       		</tr>
			       		<tr>
			       			<th>参加工作日期：</th>
			       			<td><c:out value="${member.jobDate}"/></td>
			       			<th>政治面貌：</th>
			       			<td><c:out value="${member.politicalStatus }"/></td>
			       		</tr>
			       		<tr>
			       			<th>职务类型：</th>
			       			<td>${member.dutyType }</td>
			       			<th>分配状态：</th>
			       			<td><c:out value="${member.jobStatus}"/></td>
			       		</tr>
			       		<tr>
			       			<th>职等：</th>
			       			<td><c:out value="${member.dutyGrade }"/></td>
			       			<th>薪级工资：</th>
			       			<td>${member.salaryBase }</td>
			       		</tr>
			       		<tr>
			       			<th>岗位工资：</th>
			       			<td><c:out value="${member.salaryJob}"/></td>
			       			<th>退休时间：</th>
			       			<td><c:out value="${member.retireDate }"/></td>
			       		</tr>
			       		<tr>
			       			<th>离休时间：</th>
			       			<td>${member.leaveDate }</td>
			       			<th>参加党派时间：</th>
			       			<td><c:out value="${member.partyDate}"/></td>
			       		</tr>
			       		<tr>
			       			<th>毕业学校：</th>
			       			<td><c:out value="${member.graduateSchool }"/></td>
			       			<th>毕业日期：</th>
			       			<td>${member.graduateDate }</td>
			       		</tr>
			       		<tr>
			       			<th>所学专业：</th>
			       			<td><c:out value="${member.major}"/></td>
			       			<th>最高学历代码：</th>
			       			<td><c:out value="${member.highestGraduateCode }"/></td>
			       		</tr>
			       		<tr>
			       			<th>最高学历：</th>
			       			<td>${member.highestGraduate }</td>
			       			<th>最高学位代码：</th>
			       			<td><c:out value="${member.highestDegreeCode}"/></td>
			       		</tr>
			       		<tr>
			       			<th>最高学位：</th>
			       			<td><c:out value="${member.highestDegree }"/></td>
			       			<th>学位授予日期：</th>
			       			<td>${member.degreeDate }</td>
			       		</tr>
			       		<tr>
			       			<th>学位授予单位：</th>
			       			<td><c:out value="${member.degreeCompany}"/></td>
			       			<th>进入本单位日期：</th>
			       			<td><c:out value="${member.companyJoinDate }"/></td>
			       		</tr>
			       		<tr>
			       			<th>进入单位方式：</th>
			       			<td>${member.companyJoinWay }</td>
			       			<th>进入本行业日期：</th>
			       			<td><c:out value="${member.industryJoinDate}"/></td>
			       		</tr>
			       		<tr>
			       			<th>进入本地区日期：</th>
			       			<td><c:out value="${member.districtJoinDate }"/></td>
			       			<th>进入本单位前个人身份：</th>
			       			<td>${member.companyJoinStatus }</td>
			       		</tr>
			       		<tr>
			       			<th>进入本单位变动类别：</th>
			       			<td><c:out value="${member.companyJoinType}"/></td>
			       			<th>进入本单位前工作单位名称：</th>
			       			<td><c:out value="${member.companyBefore }"/></td>
			       		</tr>
			       		<tr>
			       			<th>进入本单位前工作单位隶属关系：</th>
			       			<td>${member.companyBeforeRelation }</td>
			       			<th>进入本单位前工作单位性质类别：</th>
			       			<td><c:out value="${member.companyBeforeType}"/></td>
			       		</tr>
			       		<tr>
			       			<th>进入本单位前所在地区类别：</th>
			       			<td><c:out value="${member.districtBefore }"/></td>
			       			<th>行政职务名称：</th>
			       			<td>${member.administrationDuty }</td>
			       		</tr>
			       		<tr>
			       			<th>行政职务说明：</th>
			       			<td><c:out value="${member.administrationDutyDesc}"/></td>
			       			<th>行政职务任职状态：</th>
			       			<td><c:out value="${member.administrationStatus }"/></td>
			       		</tr>
			       		<tr>
			       			<th>行政职务任职机构：</th>
			       			<td>${member.administrationInstitution }</td>
			       			<th>行政职务批准任职的机关：</th>
			       			<td><c:out value="${member.administrationAprovedBy}"/></td>
			       		</tr>
			       		<tr>
			       			<th>行政职务批准任职的日期：</th>
			       			<td><c:out value="${member.administrationAprovedDate }"/></td>
			       			<th>行政职务批准任职的文号：</th>
			       			<td>${member.administrationAprovedFile }</td>
			       		</tr>
			       		<tr>
			       			<th>行政职务连续任该职起始日期：</th>
			       			<td><c:out value="${member.administrationRetainDate}"/></td>
			       			<th>职员职级：</th>
			       			<td><c:out value="${member.staffLevel }"/></td>
			       		</tr>
			       		<tr>
			       			<th>职员职级批准日期：</th>
			       			<td>${member.staffLevedAprovedDate }</td>
			       			<th>职员职级批准文号：</th>
			       			<td><c:out value="${member.staffLevedAprovedFile}"/></td>
			       		</tr>
			       		<tr>
			       			<th>职员职级批准机关名称：</th>
			       			<td><c:out value="${member.staffLevedAprovedCompany }"/></td>
			       			<th>专业技术职务名称：</th>
			       			<td>${member.technicalDuty }</td>
			       		</tr>
			       		<tr>
			       			<th>职称：</th>
			       			<td><c:out value="${member.technicalTitle}"/></td>
			       			<th>专业技术职务任职机构：</th>
			       			<td><c:out value="${member.technicalDutyCompany }"/></td>
			       		</tr>
			       		<tr>
			       			<th>专业技术职务最初评定日期：</th>
			       			<td>${member.technicalDutyEvaluateDate }</td>
			       			<th>专业技术职务任职终止日期：</th>
			       			<td><c:out value="${member.technicalDutyEndDate}"/></td>
			       		</tr>
			       		<tr>
			       			<th>工人职务等级：</th>
			       			<td><c:out value="${member.technicalDutyLevel }"/></td>
			       			<th>工人职务聘用日期：</th>
			       			<td>${member.technicalDutyBeginDate }</td>
			       		</tr>
			       		<tr>
			       			<th>主要分配职称：</th>
			       			<td><c:out value="${member.technicalTitleMajor}"/></td>
			       			<th>工人职务聘用单位：</th>
			       			<td><c:out value="${member.dutyCompany }"/></td>
			       		</tr>
			       		<tr>
			       			<th>工人职务变动类别：</th>
			       			<td>${member.dutyChangeType }</td>
			       			<th>工人职务变动文号：</th>
			       			<td><c:out value="${member.dutyChangeFile}"/></td>
			       		</tr>
			       		<tr>
			       			<th>是否有知识创新信息：</th>
			       			<td><c:out value="${member.hasKnowledgeInnovation }"/></td>
			       			<th>是否有档案信息：</th>
			       			<td>${member.hasArchive }</td>
			       		</tr>
			       		<tr>
			       			<th>是否有合同：</th>
			       			<td><c:out value="${member.hasContract}"/></td>
			       			<th>职务名称：</th>
			       			<td><c:out value="${member.dutyName }"/></td>
			       		</tr>
			       		<tr>
			       			<th>等级：</th>
			       			<td>${member.dutyLevel }</td>
			       			<th>任职时间：</th>
			       			<td><c:out value="${member.dutyDate}"/></td>
			       		</tr>
					</table>	
	            </div>
	            
	            
	            <!-- 教育经历 -->
	            <div class="tab-pane fade" id="educationist" >
	                  <ul class="memberDetail" id="eduContent">
					  </ul>
					
					<p class="msg" style="display: none;" id="eduNoData">没有教育经历</p>
					
	       			<p class="p_add">
	         			<a class="addEducation btn btn-primary">添加教育经历</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	            </div>
	            
	            <!-- 工作经历 -->
			    <div class="tab-pane fade" id="worklist" >
	                  <!-- <table id="table" class="table table-bordered fiveCol" style="margin-top:15px">
							<thead>
								<tr>
									<th class="time">开始时间</th>
									<th class="time">结束时间</th> 
									<th class="institution">工作单位</th> 
									<th class="position">职称</th> 
									<th class="department">职务</th> 
									<th class="operate">操作</th> 
								</tr>
							</thead>
							<tbody id="workContent">
							
							</tbody>
					</table> -->
					<ul class="memberDetail" id="workContent"></ul>
					
					<p class="msg" style="display: none;" id="workNoData">没有工作经历</p>
					
	       			<p class="p_add">
	         			<a class="addWork btn btn-primary">添加工作经历</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	           </div>
	            
	              <!-- 学术任职 -->
			    <div class="tab-pane fade" id="academic" >
	                  <table id="table" class="table table-bordered fiveCol" style="margin-top:15px">
							<thead>
								<tr>
									<th class="orgname">组织名称</th>
									<th class="position">任职</th>
									<th class="category">时间</th>
									<th class="operate">操作</th> 
								</tr>
							</thead>
							<tbody id="academicContent">
							</tbody>
					</table>
					
					<p class="msg" style="display: none;" id="acmNoData">没有学术任职</p>
					
	       			<p class="p_add">
	         			<a class="addAcademic btn btn-primary">添加学术任职</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	           </div>
	            
	              <!--  期刊任职 -->
			    <div class="tab-pane fade" id="periodical" >
	                  <table id="table" class="table table-bordered fiveCol" style="margin-top:15px">
							<thead>
								<tr>
									<th class="pername">期刊名称</th>
									<th class="position">任职</th>
									<th class="category">时间</th>
									<th class="operate">操作</th> 
								</tr>
							</thead>
							<tbody id="perContent">
							</tbody>
					</table>
					
					<p class="msg" style="display: none;" id="perNoData">没有期刊任职</p>
					
	       			<p class="p_add">
	         			<a class="addPeriodical btn btn-primary">添加期刊任职</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	           </div>
	            
	            
	              <!-- 论著 -->
			   <div class="tab-pane fade" id="treatise" >
	                  <table id="table" class="table table-bordered fiveCol" style="margin-top:15px">
							<thead>
								<tr>
								    <th class="treatiseName">论著名称</th>
						            <th class="publisher">出版社</th>
									<th class="degree">语言</th>
									<th class="time">部门</th> 
									<th class="time">单位排序</th>
									<th class="degree">年度</th>
									<th class="operate">操作</th> 
								</tr>
							</thead>
							<tbody id="treatiseContent">
							</tbody>
					</table>
					
					<p class="msg" style="display: none;" id="treaNoData">没有论著</p>
					
	       			<p class="p_add">
	         			<a class="addTreatise btn btn-primary">添加论著</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	           </div>
	           
	           
	           	<!-- 奖励 -->
			   <div class="tab-pane fade" id="award" >
	                  <table id="table" class="table table-bordered fiveCol" style="margin-top:15px">
							<thead>
								<tr>
								    <th class="achievementName">成果名称</th>
									<th class="awardName">奖励名称</th>
									<th class="institutions">授予单位</th> 
									<th class="category">类别</th> 
									<th class="level">等级</th> 
									<th class="time">部门</th> 
									<th class="time">单位排序</th>
									<th class="degree">年度</th>
									<th class="operate">操作</th> 
								</tr>
							</thead>
							<tbody id="awardContent">
							</tbody>
					</table>
					<p class="msg" style="display: none;" id="awardNoData">没有奖励</p>
	       			<p class="p_add">
	         			<a class="addAward btn btn-primary">添加奖励</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	           </div>
	           
	           
	           <!-- 专利 -->
			   <div class="tab-pane fade" id="patent" >
	                  <table id="table" class="table table-bordered fiveCol" style="margin-top:15px">
							<thead>
								<tr>
								    <th class="achievementName">成果名称</th>
									<th class="category">类别</th> 
									<th class="level">等级</th> 
									<th class="time">个人排序</th> 
									<th class="time">单位排序</th>
									<th class="degree">年度</th>
									<th class="operate">操作</th> 
								</tr>
							</thead>
							<tbody id="patentContent">
							
							</tbody>
					</table>
					<p class="msg" style="display: none;" id="patentNoData">没有专利</p>
	       			<p class="p_add">
	         			<a class="addPatent btn btn-primary">添加专利</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	           </div>
	           
	             <!-- 人才培养 -->
			   <div class="tab-pane fade" id="training" >
	                  <table id="table" class="table table-bordered fiveCol" style="margin-top:15px">
							<thead>
								<tr>
								    <th class="time">姓名</th>
									<th class="time">国籍</th> 
									<th class="degree">学位</th> 
									<th class="department">专业</th> 
									<th class="department">时间</th>
									<th>备注</th>
									<th class="operate">操作</th> 
								</tr>
							</thead>
							<tbody id="trainContent">
							
							</tbody>
					</table>
					<p class="msg" style="display: none;" id="trainNoData">没有人才培养</p>
	       			<p class="p_add">
	         			<a class="addTrain btn btn-primary">添加人才培养</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	           </div>
	           
	           
	           <!-- 软件著作权 -->
	            <div class="tab-pane fade" id="copyright" >
	                  <table id="table" class="table table-bordered fiveCol" style="margin-top:15px">
							<thead>
								<tr>
								    <th class="achievementName">成果名称</th>
									<th class="category">类别</th> 
									<th class="level">等级</th> 
									<th class="time">个人排序</th> 
									<th class="time">单位排序</th>
									<th class="degree">年度</th>
									<th class="operate">操作</th> 
								</tr>
							</thead>
							<tbody id="copyrightContent">
							
							</tbody>
					</table>
					<p class="msg" style="display: none;" id="crNoData">没有软件著作权</p>
	       			<p class="p_add">
	         			<a class="addCopyright btn btn-primary">添加软件著作权</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	           </div>
	           
	           
	           <!-- 课题 -->
	            <div class="tab-pane fade" id="topic" >
	                  <table id="table" class="table table-bordered fiveCol" style="margin-top:15px">
							<thead>
								<tr>
									<th class="achievementName">项目名称</th> 
									<th class="time">时间</th>
									<th class="degree">资金来源</th> 
									<th class="num">类别</th> 
									<th class="num">编号</th> 
									<th class="time">本人角色</th> 
									<th class="time">项目经费</th> 
									<th class="time">个人经费</th> 
									<th class="operate">操作</th> 
								</tr>
							</thead>
							<tbody id="topicContent">
							
							</tbody>
					</table>
					<p class="msg" style="display: none;" id="topicNoData">没有课题</p>
	       			<p class="p_add">
	         			<a class="addTopic btn btn-primary">添加课题</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	            </div>
	            
	            
	           <!-- 论文 -->
	            <div class="tab-pane fade" id="paper" >
	                  <ul id="paperContent" class="listShow" style="margin-top:15px">
					  </ul>
					<p class="msg" style="display: none;" id="paperNoData">没有论文</p>
	       			<p class="p_add">
	         			<a class="addPaper btn btn-primary">添加论文</a> 
	         			<!-- <a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a> -->
	       			</p>
	            </div>
	           
	           
		  </div>
		</div>
	</div>
	
	<!-- 基本信息编辑 -->
<div id="edit-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>更改用户信息</h3>
        </div>
       	<form id="editForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">姓名：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="trueName" name="trueName" value='' />
          				<span id="trueName_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">性别：</label>
          			<div class="controls">
          				<select name="sex" id="sex">
          					<option value="">--</option>
          					<option value="male">男</option>
          					<option value="female">女</option>
          				</select>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">职称：</label>
          			<div class="controls">
            			<input maxlength="250" type="text" name="technicalTitle" id="technicalTitle" value='' />
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">办公室地址：</label>
          			<div class="controls">
          				<input maxlength="250" type="text" name="officeAddress" id="officeAddress" value='' />
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">办公室电话：</label>
          			<div class="controls">
            			<input maxlength="250" type="text" name="officeTelephone" id="officeTelephone" />
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">手机：</label>
          			<div class="controls">
          				<input maxlength="250" type="text" name="mobilePhone" id="mobilePhone" />
          			</div>
        		</div>
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="saveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
	
		<!-- 添加教育经历 -->
  <div id="education-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button"  id="cancelEducationBtn" class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加教育经历</h3>
        </div>
       	<form id="eduForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
		          <input type="hidden" name="func" value="save" /> 
			       <input type="hidden" name="id" value=""/>
	       			<div class="control-group">
	        			<input type="hidden" name="beginTime" value="">
	         			<label class="control-label"><span class="red">*</span>开始时间：</label>
	          			<div class="controls">
	            			<select name="beginTimeYear" id="startYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="beginTimeMonth"  id="startMonth" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<c:choose>
	            						<c:when test="${tmpmonth<10 }">
	            							<option value="0${tmpmonth}">0${tmpmonth}</option>
	            						</c:when>
	            						<c:otherwise>
	            							<option value="${tmpmonth}">${tmpmonth}</option>
	            						</c:otherwise>
	            					</c:choose>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<span class="error"></span>
	          			</div>
	          			
	        		</div>
	        		<div class="control-group">
	        			<input type="hidden" name="endTime" value="">
	         			<label class="control-label"><span class="red">*</span><fmt:message key='personalWorkInfo.endTime'/></label>
	          			<div class="controls">
	            			<select name="endTimeYear" id="endYear"  class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="endTimeMonth" id="endMonth"   class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<c:choose>
	            						<c:when test="${tmpmonth<10 }">
	            							<option value="0${tmpmonth}">0${tmpmonth}</option>
	            						</c:when>
	            						<c:otherwise>
	            							<option value="${tmpmonth}">${tmpmonth}</option>
	            						</c:otherwise>
	            					</c:choose>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<input type="checkbox" name="now" class="d-sbottom"><fmt:message key='personalWorkInfo.untilnow'/>
	          				<span class="error"></span>
	          			</div>
	          			
	        		</div>
	       		
					<div class="control-group">
	         			<label class="control-label">学历：</label>
	          			<div class="controls">
	            			<select name="degree">
								<option value="本科">本科</option>
								<option value="硕士研究生">硕士研究生</option>
								<option value="博士研究生">博士研究生</option>
							</select>
	          			</div>
	        		</div>
					<div class="control-group">
	         			<label class="control-label">学位：</label>
	          			<div class="controls">
	            			<select name="degree2">
								<option>学士</option>
								<option>硕士</option>
								<option>博士</option>
							</select>
	          			</div>
	        		</div>
	        		
					<div class="control-group">
	         			<label class="control-label"><span class="red">*</span>学校：</label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="institutionZhName" id="institutionZhName" value="" />
	            			<span id="institutionZhName_error_place" class="error"></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><span class="red">*</span>专业：</label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="department" id="department" value="" />
	            			<span id="department_error_place" class="error"></span>
	          			</div>
	            	</div>
	            		<div class="control-group">
	         			<label class="control-label"><span class="red">*</span>导师姓名：</label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="tutor" id="tutor" value="" />
	            			<span id="tutor_error_place" class="error"></span>
	            			
	          			</div>
	            	</div>
	            	
	            		
	       		<div class="control-group">
	       			<label class="control-label">毕业设计：</label>
	       			<div class="controls">
						<div id="fileUploader">
							<div class="qq-uploader">
								<div class="qq-upload-button">
									<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
								</div>
								<ul class="qq-upload-list fileList"></ul>
							</div>
						</div>
						
						 <span id="fileNameSpan"></span>
						<span id="fileUploadProcess"></span>
						<input type="hidden" name="graduationProjectCid" id="clbId" />
						<input type="hidden" name="graduationProject" id="fileName"/>
						<a id="fileRemove" style="display: none;">删除</a>
	       			</div>
	       		</div>
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelEducationBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="saveEducationBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
		<!-- 添加工作经历 -->
  <div id="work-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" id="cancelWorkBtn"  class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加工作经历</h3>
        </div>
       	<form id="workForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			       <input type="hidden" name="id" value="0"/>
	       			<div class="control-group">
	        			<input type="hidden" name="beginTime" value="">
	         			<label class="control-label"><span class="red">*</span>开始时间：</label>
	          			<div class="controls">
	            			<select name="beginTimeYear" id="startYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="beginTimeMonth" id="startMonth" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<c:choose>
	            						<c:when test="${tmpmonth<10 }">
	            							<option value="0${tmpmonth}">0${tmpmonth}</option>
	            						</c:when>
	            						<c:otherwise>
	            							<option value="${tmpmonth}">${tmpmonth}</option>
	            						</c:otherwise> 
	            					</c:choose>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<span class="error"></span>
	          			</div>
	          			
	        		</div>
	        		<div class="control-group">
	        			<input type="hidden" name="endTime" value="">
	         			<label class="control-label"><span class="red">*</span><fmt:message key='personalWorkInfo.endTime'/></label>
	          			<div class="controls">
	            			<select name="endTimeYear" id="endYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="endTimeMonth" id="endMonth" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<c:choose>
	            						<c:when test="${tmpmonth<10 }">
	            							<option value="0${tmpmonth}">0${tmpmonth}</option>
	            						</c:when>
	            						<c:otherwise>
	            							<option value="${tmpmonth}">${tmpmonth}</option>
	            						</c:otherwise>
	            					</c:choose>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<input type="checkbox" name="now" class="d-sbottom"><fmt:message key='personalWorkInfo.untilnow'/>
	          				<span class="error"></span>
	          			</div>
	          			
	        		</div>
	        		
					<div class="control-group">
	         			<label class="control-label"><span class="red">*</span>工作单位：</label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="institutionZhName" id="institutionZhName" value="" />
	            			<span id="institutionZhName_error_place" class="error"></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><span class="red">*</span>职称：</label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="position" id="position"  value="" />
	            			<span id="position_error_place" class="error"></span>
	          			</div>
	            	</div>
	            		<div class="control-group">
	         			<label class="control-label">职务：</label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="department" id="department" value="" />
	          			</div>
	            	</div>
	            	
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelWorkBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="saveWorkBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
		<!-- 添加论著 -->
    <div id="treatise-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button"  id="cancelTreatiseBtn" class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加论著</h3>
        </div>
       	<form id="treatiseForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			    <input type="hidden" name="id" value="0"/>
				
				<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>论著名称：</label>
	       			<div class="controls">
	         			<input type="text" name="name" id="name" value="<c:out value="${treatise.name }"/>" class="register-xlarge"/>
						<span id="name_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>部门：</label>
	       			<div class="controls">
	         			<select id="departId" name=departId>
	         				<c:forEach items="${deptMap.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==treatise.departId }">selected="selected"</c:if>>${data.value.shortName}</option>
	         			 	</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>出版社：</label>
	       			<div class="controls">
	         			<select id="publisher" name="publisher">
	         				<c:forEach items="${publishers.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==treatise.publisher }">selected="selected"</c:if>>${data.value.val}</option>
	         			 	</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>语言：</label>
	       			<div class="controls">
	         			<input type="text" name="language" id="language" value="<c:out value="${treatise.language }"/>" maxlength="250"/>
						<span id="language_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>出版年：</label>
	       			<div class="controls">
	         			<select id="year" name="year">
            				<c:forEach var="tmpyear" begin="${now-15}" end="${now}" step="1">
            					<option value="${now-tmpyear+(now-15)}">${now-tmpyear+(now-15)}</option>
            				</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
	       		  <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>单位排序：</label>
	       			<div class="controls">
	         			<select id="companyOrder" name="companyOrder">
            				<c:forEach var="index" begin="1" end="6" step="1">
            					<option value="${index }">${index}</option>
            				</c:forEach>
						</select>
	       			</div>
	       		</div>
	            	
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelTreatiseBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="saveTreatiseBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
		<!-- 添加奖励 -->
    <div id="award-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button"  id="cancelAwardBtn" class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加奖励</h3>
        </div>
       	<form id="awardForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			    <input type="hidden" name="id" value="0"/>
				
				<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>成果名称：</label>
	       			<div class="controls">
	         			<input type="text" name="name" id="name" value="<c:out value="${treatise.name }"/>" class="register-xlarge"/>
						<span id="name_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>获奖名称：</label>
	       			<div class="controls">
	         			<select id="awardName" name="awardName">
	         			    <c:forEach items="${awardNames.entrySet() }" var="data">
	         			         <option value="${data.key }">${data.value.val}</option>
	         			   </c:forEach>
						</select>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>部门：</label>
	       			<div class="controls">
	         			<select id="departId" name="departId">
	         				<c:forEach items="${deptMap.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==award.departId }">selected="selected"</c:if>>${data.value.shortName}</option>
	         			 	</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>授予机构：</label>
	       			<div class="controls">
	         			<input type="text" name="grantBody" id="grantBody" value="<c:out value="${award.grantBody }"/>" class="register-xlarge"/>
						<span id="grantBody_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>类别：</label>
	       			<div class="controls">
	         			<select name="type">
	         			  <c:forEach items="${awardTypes.entrySet() }" var="data">
	         			         <option value="${data.key }">${data.value.val}</option>
	         			   </c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>等级：</label>
	       			<div class="controls">
	         			<select id="grade" name="grade">
	         			   <c:forEach items="${awardGrades.entrySet() }" var="data">
	         			        <option value="${data.key }">${data.value.val}</option>
	         			   </c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>年度：</label>
	       			<div class="controls">
	         			<select id="year" name="year">
            				<c:forEach var="tmpyear" begin="${now-15}" end="${now}" step="1">
            					<option value="${now-tmpyear+(now-15)}">${now-tmpyear+(now-15)}</option>
            				</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
	       		  <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>单位排序：</label>
	       			<div class="controls">
	         			<select id="companyOrder" name="companyOrder">
            				<c:forEach var="index" begin="1" end="6" step="1">
            					<option value="${index }">${index}</option>
            				</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">上传附件：</label>
	       			<div class="controls">
						<div id="awardFileUploader">
							<div class="qq-uploader">
								<div class="qq-upload-button">
									<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
								</div>
								<ul class="qq-upload-list fileList"></ul>
							</div>
						</div>
						
						<table id="clbTable">
							<tbody id="clbContent">
							
							</tbody>
						</table>
	       			</div>
	       		</div>
	            	
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelAwardBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="saveAwardBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
	
		<!-- 添加专利 -->
    <div id="patent-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button"  id="cancelPatentBtn" class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加专利</h3>
        </div>
       	<form id="patentForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			    <input type="hidden" name="id" value="0"/>
				
				<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>成果名称：</label>
	       			<div class="controls">
	         			<input type="text" name="name" id="name" value="<c:out value="${treatise.name }"/>" class="register-xlarge"/>
						<span id="name_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>类别：</label>
	       			<div class="controls">
	         			<select id="type" name="type">
	         			    <option value="1">部级</option>
	         			    <option value="2">省级</option>
	         			    <option value="3">其他</option>
						</select>
	       			</div>
	       		</div>
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>等级：</label>
	       			<div class="controls">
	         			<select id="grade" name="grade">
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>年度：</label>
	       			<div class="controls">
	         			<select id="year" name="year">
            				<c:forEach var="tmpyear" begin="${now-15}" end="${now}" step="1">
            					<option value="${now-tmpyear+(now-15)}">${now-tmpyear+(now-15)}</option>
            				</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label">个人排序：</label>
	       			<div class="controls">
						&nbsp;第&nbsp;&nbsp;
	         			<select id="personalOrder" name="personalOrder" class="i-small">
							<c:forEach var="index" begin="1" end="6" step="1">
            					<option value="${index }">${index}</option>
            				</c:forEach>
						</select>&nbsp;作者&nbsp;&nbsp;
	       			</div>
	       		</div>
	       		
	       		  <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>单位排序：</label>
	       			<div class="controls">
	         			<select id="companyOrder" name="companyOrder">
            				<c:forEach var="index" begin="1" end="6" step="1">
            					<option value="${index }">${index}</option>
            				</c:forEach>
						</select>
	       			</div>
	       		</div>
	            	
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelPatentBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="savePatentBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
		<!-- 添加软件著作权 -->
    <div id="copyright-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" id="cancelCopyrightBtn" class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加软件著作权</h3>
        </div>
       	<form id="copyrightForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			    <input type="hidden" name="id" value="0"/>
				
				<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>成果名称：</label>
	       			<div class="controls">
	         			<input type="text" name="name" id="name" value="<c:out value="${treatise.name }"/>" class="register-xlarge"/>
						<span id="name_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>类别：</label>
	       			<div class="controls">
	         			<select id="type" name="type">
	         			    <option value="1">部级</option>
	         			    <option value="2">省级</option>
	         			    <option value="3">其他</option>
						</select>
	       			</div>
	       		</div>
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>等级：</label>
	       			<div class="controls">
	         			<select id="grade" name="grade">
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>年度：</label>
	       			<div class="controls">
	         			<select id="year" name="year">
            				<c:forEach var="tmpyear" begin="${now-15}" end="${now}" step="1">
            					<option value="${now-tmpyear+(now-15)}">${now-tmpyear+(now-15)}</option>
            				</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label">个人排序：</label>
	       			<div class="controls">
						&nbsp;第&nbsp;&nbsp;
	         			<select id="personalOrder" name="personalOrder" class="i-small">
							<c:forEach var="index" begin="1" end="6" step="1">
            					<option value="${index }">${index}</option>
            				</c:forEach>
						</select>&nbsp;作者&nbsp;&nbsp;
	       			</div>
	       		</div>
	       		
	       		  <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>单位排序：</label>
	       			<div class="controls">
	         			<select id="companyOrder" name="companyOrder">
            				<c:forEach var="index" begin="1" end="6" step="1">
            					<option value="${index }">${index}</option>
            				</c:forEach>
						</select>
	       			</div>
	       		</div>
	            	
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelCopyrightBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="saveCopyrightBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
	
		<!-- 添加学术任职 -->
  <div id="academic-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" id="cancelAcademicBtn"  class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加学术任职</h3>
        </div>
       	<form id="academicForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			       <input type="hidden" name="id" value="0"/>
	       			<div class="control-group">
	        			<input type="hidden" name="beginTime" value="">
	         			<label class="control-label"><span class="red">*</span>开始时间：</label>
	          			<div class="controls">
	            			<select name="startYear"  id="startYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="startMonth"  id="startMonth" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<option value="${tmpmonth}">${tmpmonth}</option>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<span class="error"></span>
	          			</div>
	          			
	        		</div>
	        		<div class="control-group">
	        			<input type="hidden" name="endTime" value="">
	         			<label class="control-label"><span class="red">*</span><fmt:message key='personalWorkInfo.endTime'/></label>
	          			<div class="controls">
	            			<select name="endYear"  id="endYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="endMonth" id="endMonth"  class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<option value="${tmpmonth}">${tmpmonth}</option>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<input type="checkbox" name="now" class="d-sbottom"><fmt:message key='personalWorkInfo.untilnow'/>
	          				<span class="error"></span>
	          			</div>
	          			
	        		</div>
	       		
						
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>组织名称：</label>
	       			<div class="controls">
	         			<select id="name" name="name">
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>职位：</label>
	       			<div class="controls">
	         			<select id="position" name="position">
						</select>
	       			</div>
	       		</div>
	            	
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelAcademicBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="saveAcademicBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
		<!-- 添加期刊任职 -->
  <div id="periodical-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" id="cancelPeriodicalBtn"  class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加期刊任职</h3>
        </div>
       	<form id="periodicalForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			       <input type="hidden" name="id" value="0"/>
	       			<div class="control-group">
	        			<input type="hidden" name="beginTime" value="">
	         			<label class="control-label"><span class="red">*</span>开始时间：</label>
	          			<div class="controls">
	            			<select name="startYear" id="startYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="startMonth" id="startMonth" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<option value="${tmpmonth}">${tmpmonth}</option>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<span class="error"></span>
	          			</div>
	          			
	        		</div>
	        		<div class="control-group">
	        			<input type="hidden" name="endTime" value="">
	         			<label class="control-label"><span class="red">*</span><fmt:message key='personalWorkInfo.endTime'/></label>
	          			<div class="controls">
	            			<select name="endYear" id="endYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="endMonth" id="endMonth" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<option value="${tmpmonth}">${tmpmonth}</option>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<input type="checkbox" name="now" class="d-sbottom"><fmt:message key='personalWorkInfo.untilnow'/>
	          				<span class="error"></span>
	          			</div>
	          			
	        		</div>
	       		
						
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>期刊名称：</label>
	       			<div class="controls">
	         			<select id="name" name="name">
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>职位：</label>
	       			<div class="controls">
	         			<select id="position" name="position">
						</select>
	       			</div>
	       		</div>
	            	
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelPeriodicalBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="savePeriodicalBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>


		<!-- 添加课题 -->
  <div id="topic-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" id="cancelTopicBtn"  class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加课题</h3>
        </div>
       	<form id="topicForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			       <input type="hidden" name="id" value="0"/>
	       			<div class="control-group">
	        			<input type="hidden" name="beginTime" value="">
	         			<label class="control-label"><span class="red">*</span>开始时间：</label>
	          			<div class="controls">
	            			<select name="start_year"  id="startYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="start_month"  id="startMonth"  class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<option value="${tmpmonth}">${tmpmonth}</option>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<span class="error"></span>
	          			</div>
	          			
	        		</div>
	        		
	        		<div class="control-group">
	        			<input type="hidden" name="endTime" value="">
	         			<label class="control-label"><span class="red">*</span><fmt:message key='personalWorkInfo.endTime'/></label>
	          			<div class="controls">
	            			<select name="end_year"  id="endYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="end_month" id="endMonth" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<option value="${tmpmonth}">${tmpmonth}</option>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<input type="checkbox" name="now" class="d-sbottom"><fmt:message key='personalWorkInfo.untilnow'/>
	          				<span class="error"></span>
	          			</div>
	          			
	        		</div>
	       		
						
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>项目名称：</label>
	       			<div class="controls">
	         			<input maxlength="255" type="text" name="name" id="name" value="" class="register-xlarge"/>
	         			<span id="name_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>资金来源：</label>
	       			<div class="controls">
	         			<select id="funds_from" name="funds_from">
						</select>
	       			</div>
	       		</div>
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>类别：</label>
	       			<div class="controls">
	         			<select id="type" name="type">
	         			    <option value="1">部级</option>
	         			    <option value="2">省级</option>
	         			    <option value="3">其他</option>
						</select>
					</div>
	       		</div>
	            
	            <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>编号：</label>
	       			<div class="controls">
	         			<input maxlength="255" type="text" name="topic_no" id="topic_no" value="" />
	         			<span id="topic_no_error_place" class="error"></span>
	       			</div>
	       		</div>	
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>本人角色：</label>
	       			<div class="controls">
	         			<select id="role" name="role">
	         			    <option value="admin">负责人</option>
	         			    <option value="member">参与者</option>
						</select>
					</div>
	       		</div>
	       		
	       	   <hr>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>作者：</label>
	       			<div class="controls">
	         			<input type="text" name="authorSearch" class="autoCompleteSearch register-xlarge" id="authorSearch"/>
	         			<span class="check"><input type="checkbox"  id="checkAuthor"/> 是否负责人</span>
						<p class="hint">请输入作者的姓名、邮箱或者单位，按回车确认添加。</p>
						<p class="notFind">没有找到您要选择的作者？点击这里<a id="addTopicAuthor">添加作者</a></p>
	       			</div>
	       			<table id="authorTable" class="table table-bordered fiveCol">
						<tbody id="authorContent">
						</tbody>
					</table>
	       		</div>
	       		<hr>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>项目经费：</label>
	       			<div class="controls">
	         			<input maxlength="255" type="text" name="project_cost" id="project_cost" value="" />
	         			<span id="project_cost_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>个人经费：</label>
	       			<div class="controls">
	         			<input maxlength="255" type="text" name="personal_cost" id="personal_cost" value="" />
	         			<span id="personal_cost_error_place" class="error"></span>
	       			</div>
	       		</div>
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelTopicBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="saveTopicBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	

		<!-- 添加人才培养 -->
  <div id="training-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" id="cancelTrainingBtn"  class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加人才培养</h3>
        </div>
       	<form id="trainingForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			       <input type="hidden" name="id" value="0"/>
			       
			    <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>学生姓名：</label>
	       			<div class="controls">
	         			<input type="text" name="studentName" id="studentName" value="<c:out value="${training.studentName }"/>" />
						<span id="studentName_error_place" class="error"></span>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>国籍：</label>
	       			<div class="controls">
	         			<input type="text" name="nationality" id="nationality" value="<c:out value="${training.nationality }"/>" maxlength="250"/>
						<span id="nationality_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>学位：</label>
	       			<div class="controls">
	         			<select id="degree" name="degree">
	         			        <option value="0">学士</option>
								<option value="1">硕士</option>
								<option value="2">博士</option>
					    </select>
	       			</div>
	       		</div>
			       
	       		<div class="control-group">
	        			<input type="hidden" name="beginTime" value="">
	         			<label class="control-label"><span class="red">*</span>开始时间：</label>
	          			<div class="controls">
	            			<select name="beginTimeYear" id="startYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="beginTimeMonth" id="startMonth" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<c:choose>
	            						<c:when test="${tmpmonth<10 }">
	            							<option value="0${tmpmonth}">0${tmpmonth}</option>
	            						</c:when>
	            						<c:otherwise>
	            							<option value="${tmpmonth}">${tmpmonth}</option>
	            						</c:otherwise> 
	            					</c:choose>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<span class="error"></span>
	          			</div>
	          			
	        		</div>
	        		<div class="control-group">
	        			<input type="hidden" name="endTime" value="">
	         			<label class="control-label"><span class="red">*</span><fmt:message key='personalWorkInfo.endTime'/></label>
	          			<div class="controls">
	            			<select name="endTimeYear" id="endYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>&nbsp;年&nbsp;&nbsp;
	            			<select name="endTimeMonth" id="endMonth" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.month'/> -</option>
	            				<c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<c:choose>
	            						<c:when test="${tmpmonth<10 }">
	            							<option value="0${tmpmonth}">0${tmpmonth}</option>
	            						</c:when>
	            						<c:otherwise>
	            							<option value="${tmpmonth}">${tmpmonth}</option>
	            						</c:otherwise>
	            					</c:choose>
	            				</c:forEach>
	            			</select>&nbsp;月
	            			<input type="checkbox" name="now" class="d-sbottom"><fmt:message key='personalWorkInfo.untilnow'/>
	          				<span class="error"></span>
	          			</div>
	          			
	        		</div>
	       		
						
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>专业：</label>
	       			<div class="controls">
	         			<input type="text" name="major" id="major" value="<c:out value="${training.major }"/>" />
						<span id="major_error_place" class="error"></span>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">备注：</label>
	       			<div class="controls">
	         			<textarea rows="6" cols="20" id="remark" name="remark" class="register-xlarge">${training.major }</textarea>
						<span id="remark_error_place" class="error"></span>
	       			</div>
	       		</div>
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelTrainingBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="saveTrainingBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
		<!-- 添加论文 -->
  <div id="paper-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" id="cancelPaperBtn"  class="close" data-dismiss="modal">×</button>
           <h3 id="popupTitle">添加论文</h3>
        </div>
       	<form id="paperForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<input type="hidden" name="umtId" value="${member.umtId }"/>
			<div class="modal-body">
			       <input type="hidden" name="id" value="0"/>
			       
			      <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>论文标题：</label>
	       			<div class="controls">
	         			<input type="text" name="title" value="<c:out value="${paper.title }"/>" id="title" class="register-xlarge"/>
						<span id="title_error_place" class="error"></span>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">doi：</label>
	       			<div class="controls">
	         			<input type="text" name="doi" value="<c:out value="${paper.doi }"/>" id="doi" maxlength="250"/>
						<span id="dio_error_place" class="error"></span>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">ISSN / ISDN：</label>
	       			<div class="controls">
	         			<input type="text" name="issn" id="issn" value="<c:out value="${paper.issn }"/>" maxlength="250"/>
	       			</div>
	       		</div>
	       		<hr>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>部门：</label>
	       			<div class="controls">
	         			<select id="departId" name="departId">
	         				<c:forEach items="${deptMap.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==paper.departId }">selected="selected"</c:if>>${data.value.shortName}</option>
	         			 	</c:forEach>
						</select>
	       			</div>
	       		</div>
<!-- 				<div class="control-group"> -->
<!-- 	       			<label class="control-label"><span class="red">*</span>刊物名称：</label> -->
<!-- 	       			<div class="controls"> -->
<!-- 	         			<select id="publicationId" name="publicationId" class="register-xlarge"> -->
<!-- 							<option value="0">--</option> -->
<%-- 							<c:forEach items="${ pubs}" var="pub"> --%>
<%-- 								<option ${op=='update'&&paper.publicationId==pub.id?'selected="selected"':'' } value="${pub.id }"><c:out value="${ pub.pubName}"/></option> --%>
<%-- 							</c:forEach> --%>
<!-- 						</select> -->
<!-- 						<span id="publicationId_error_place" class="error"></span> -->
<!-- 	       			</div> -->
<!-- 	       		</div> -->
	       		<div class="control-group">
		       			<label class="control-label"><span class="red">*</span>刊物名称：</label>
		       			<div class="controls">
		       				<input type="text" value="${pubsMap[paper.publicationId].pubName}" data-pid="${paper.publicationId }" name="pubSearch" id="pubSearch" class="register-xlarge" onfocus="javascript:this.select();"/>
<!-- 		         			<select id="publicationId" name="publicationId"> -->
<!-- 								<option value="0">--</option> -->
<%-- 								<c:forEach items="${ pubs}" var="pub"> --%>
<%-- 									<option ${op=='update'&&paper.publicationId==pub.id?'selected="selected"':'' } value="${pub.id }"><c:out value="${ pub.pubName}"/></option> --%>
<%-- 								</c:forEach> --%>
<!-- 							</select> -->
							<p class="notFind">没有找到您要选择的刊物？点击这里<a id="addPubBtn">新增刊物</a></p>
							<span id="pubSearch_error_place" class="error"></span>
		       			</div>
		       		</div>
				<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>发表年份：</label>
	       			<div class="controls">
	         			<select id="publicationYear" name="publicationYear">
	         			  <c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
            				</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">发表月份：</label>
	       			<div class="controls">
	         			<select id="publicationMonth" name="publicationMonth">
						    <c:forEach var="tmpmonth" begin="1" end="12" step="1">
	            					<option value="${tmpmonth}">${tmpmonth}</option>
	            			</c:forEach>
	            			
					    </select>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>卷号：</label>
	       			<div class="controls">
	         			<input type="text" name="volumeNumber" value="<c:out value="${paper.volumeNumber }"/>" id="volumeNumber" />
						<span id="volumeNumber_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">期号：</label>
	       			<div class="controls">
	         			<input type="text" name="series" value="<c:out value="${paper.series }"/>" id="series" maxlength="250"/>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>页码：</label>
	       			<div class="controls">
	         			<input type="text" value="<c:out value="${paper.publicationPage }"/>" name="publicationPage" id="publicationPage" />
						<span id="publicationPage_error_place" class="error"></span>
						<p class="hint">请输入“起始页码-结束页码”，如“12-13”。</p>
	       			</div>
	       		</div>
	       		<hr>
	       		<div class="control-group">
	       			<label class="control-label">作者：</label>
	       			<div class="controls">
	         			<input type="text" name="authorSearch" class="autoCompleteSearch register-xlarge" id="authorSearch"/>
						<p class="hint">请输入作者的姓名、邮箱或者单位，按回车确认添加。</p>
						<p class="notFind">没有找到您要选择的作者？点击这里<a id="addAuthor">添加作者</a></p>
	       			</div>
	       			<ul id="authorTable"></ul>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">作者总人数：</label>
	       			<div class="controls">
	         			<input type="text" name="authorAmount" id="authorAmount" value="<c:out value="${(empty paper.authorAmount)?0:paper.authorAmount }"/>" maxlength="250"/>
	       				<span id="authorAmount_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<hr>
	       		<div class="control-group">
	       			<label class="control-label">资助单位：</label>
	       			<div class="controls">
	         			<input type="text" value="${paper.sponsor }" name="sponsor" id="sponsor" maxlength="250"/>
						<!-- <p class="notFind">没有找到您要选择的资助单位？点击这里<a id="addSupportor">添加资助单位</a></p> -->
	       			</div>
	       		</div>
	       		<hr>
				<div class="control-group">
	       			<label class="control-label">学科方向：</label>
	       			<div class="controls">
	         			<select id="disciplineOrientationId" name="disciplineOrientationId">
							<c:forEach items="${disciplineOrientations.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==paper.disciplineOrientationId }">selected="selected"</c:if>>${data.value.val}</option>
	         			 	</c:forEach>
						</select>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">关键字：</label>
	       			<div class="controls">
	         			<input type="text" value="${paper.keywordDisplay }" name="keywordDisplay" id="keywordDisplay" maxlength="250"/>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">摘要：</label>
	       			<div class="controls">
	         			<textarea rows="5" cols="10" id="summary" name="summary" maxlength="1000" class="register-xlarge"><c:out value="${paper.summary }"/></textarea>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">原文链接：</label>
	       			<div class="controls">
	         			<input type="text" class="register-xlarge" value="<c:out value="${paper.originalLink }"/>" name="originalLink" id="originalLink" maxlength="250"/>
						<span id="originalLink_error_place" class="error"></span>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">原文：</label>
	       			<div class="controls">
						<div id="paperfileUploader">
							<div class="qq-uploader">
								<div class="qq-upload-button">
									<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
								</div>
								<ul class="qq-upload-list fileList"></ul>
							</div>
						</div>
						<span id="paperfileNameSpan"><c:out value="${paper.originalFileName }"/></span>
						<span id="paperfileUploadProcess"></span>
						<input type="hidden" name="clbId" id="paperclbId" value="${empty paper?0:paper.clbId }"/>
						<input type="hidden" name="originalFileName" id="paperfileName" value="<c:out value="${paper.originalFileName }"/>"/>
						<a id="paperfileRemove" style="display:${empty paper||paper.clbId==0?'none':'inline' }">删除</a>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">论文引用次数：</label>
	       			<div class="controls">
	         			<input type="text" id="citation" name="citation" value="<c:out value="${paper.citation==-1?'--':paper.citation }"/>" maxlength="10"/>
						<span id="citation_error_place" class="error"></span>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">引用次数查询时间： </label>
	       			<div class="controls">
	         			<input readonly="readonly" type="text" id="citationQueryTime" value="<c:out value="${paper.citationQueryTime }"/>" name="citationQueryTime" />
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">论文年度奖励标示：</label>
	       			<div class="controls">
	         			<select id="annualAwardMarks" name="annualAwardMarks"></select>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">论文绩效计算年份：</label>
	       			<div class="controls">
	         			<select id="performanceCalculationYear" name="performanceCalculationYear"></select>
	       			</div>
	       		</div>
	       	</div>	
			<div class="modal-footer">
				<a data-dismiss="modal" id="cancelPaperBtn" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="savePaperBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
	<!-- 添加刊物 -->
	<div id="add-publication-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>新增刊物</h3>
        </div>
       	<form id="addPubForm" class="form-horizontal" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label"><span class="red">*</span>刊物名称：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="pubName" name="pubName" value='' class="register-xlarge"/>
          				<span id="pubName_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">issn：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="issn" name="issn" value='' class="register-xlarge"/>
          				<span id="issn_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">刊物简称：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="abbrTitle" name="abbrTitle" value='' class="register-xlarge"/>
          				<span id="abbrTitle_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">刊物类别：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="publicationType" name="publicationType" value='' class="register-xlarge"/>
          				<span id="publicationType_error_place" class="error"></span>
          			</div>
        		</div>
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="pubSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
        </form>
	</div>
	
	<!-- 添加作者 -->
	<div id="add-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>添加作者</h3>
        </div>
        	<form id="addAuthorForm" class="form-horizontal" method="post">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">姓名：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorName" name="authorName" value='' class="register-xlarge"/>
          				<span id="authorName_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">邮箱：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorEmail" name="authorEmail" value='' class="register-xlarge"/>
          				<span id="authorEmail_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">单位：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorCompany" name="authorCompany" value='' class="register-xlarge"/>
          				<span id="authorCompany_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">作者排序：</label>
          			<div class="controls">
          				第
            			<input maxlength="254" type="text" id="order" name="order" value='0' style="width:20px;"/>
          				作者
          				<span id="order_error_place" class="error"></span>
          				<div class="sub-author">
	          				<label style="display:inline" id="communicateAuthor">
			        			<input type="checkbox" value="true" name="communicationAuthor"/>通讯作者
			        		</label>
			        		<label style="display:none" id="authorStudentL">
			        			<!-- 第一作者时，出现 -->
			        			<input type="checkbox" value="true"  name="authorStudent"/>学生在读<span class="sub-hint">（第一作者适用）</span>
			        		</label>
			        		<label style="display:none" id="authorTeacherL">
			        			<!-- 第二作者时，出现 -->
			        			<input type="checkbox" value="true" name="authorTeacher" />第一作者导师<span class="sub-hint">（第二作者适用）</span>
			        		</label>
		        		</div>
          			</div>
        		</div>
        		
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="authorSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div>
	<!-- 编辑作者 -->
	<div id="edit-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>编辑作者</h3>
        </div>
        	<form id="editAuthorForm" class="form-horizontal" method="post">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">姓名：</label>
          			<div class="controls padding">
          				<span id="editTrueName"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">邮箱：</label>
          			<div class="controls padding">
          				<span id="editEmail"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">单位：</label>
          			<div class="controls padding">
            			<span id="editAuthorCompany" ></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">作者排序：</label>
          			<div class="controls padding">
          				第
            			<input maxlength="254" type="text" id="editOrder" name="editOrder" value='0' style="width:20px;"/>
          				作者
          				<span id="editOrder_error_place" class="error"></span>
          				<div class="sub-author">
			        		<label style="display:inline" id="editCommunicateAuthor">
			        			<input type="checkbox" value="true" name="communicationAuthor"/>通讯作者
			        		</label>
			        		<label style="display:none" id="editAuthorStudentL">
			        			<!-- 第一作者时，出现 -->
			        			<input type="checkbox" value="true"  name="authorStudent"/>学生在读
			        		</label>
			        		<label style="display:none" id="editAuthorTeacherL">
			        			<!-- 第二作者时，出现 -->
			        			<input type="checkbox" value="true" name="authorTeacher" />第一作者导师
			        		</label>
		        		</div>
          			</div>
          			
        		</div>
        		
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="editAuthorSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div>
	
		<!-- 添加课题作者 -->
	<div id="add-topic-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>添加作者</h3>
        </div>
        	<form id="addTopicAuthorForm" class="form-horizontal" method="post">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">姓名：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorName" name="authorName" value='' class="register-xlarge"/>
          				<span id="authorName_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">邮箱：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorEmail" name="authorEmail" value='' class="register-xlarge"/>
          				<span id="authorEmail_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">单位：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorCompany" name="authorCompany" value='' class="register-xlarge"/>
          				<span id="authorCompany_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">作者类型：</label>
          			<div class="controls">
            			<select id="role" name="role">
							<option value="admin">负责人</option>
	          				<option value="member">参与者</option>
						</select>
          				<span id="authorCompany_error_place" class="error"></span>
          			</div>
        		</div>
        		
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="topicAuthorSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div>
	
	<!-- 编辑课题作者 -->
	<div id="edit-topic-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>编辑作者</h3>
        </div>
        	<form id="editTopicAuthorForm" class="form-horizontal" method="post">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">姓名：</label>
          			<div class="controls padding">
          				<span id="editTrueName"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">邮箱：</label>
          			<div class="controls padding">
          				<span id="editEmail"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">单位：</label>
          			<div class="controls padding">
            			<span id="editAuthorCompany" ></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">作者类型：</label>
          			<div class="controls padding">
            			<select id="editAuthorType" name="editAuthorType">
							<option value="admin" >负责人</option>
	          				<option value="member">参与者</option>
						</select>
          			</div>
          			
        		</div>
        		
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="editTopicAuthorSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div>
	
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
	<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	
	<script>
		$(document).ready(function(){
			//初始化时间控件
			$("#citationQueryTime").datepicker({
				picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",
				applyrule:function(){return true;}}
			);
			
			//token控件初始化，关键字
			var $tokenObj = $("#keywordDisplay").tokenInput("<dhome:url value='/institution/${domain}/backend/paper/search/keyword'/>", {
				theme:"facebook",
				hintText: "请输入关键字",
				searchingText: "正在查询...",
				noResultsText: "未查询到结果",
				preventDuplicates: true,
				queryParam:"q"
			});
			
			//token控件初始化，关键字
			var $tokenSponsor = $("#sponsor").tokenInput("<dhome:url value='/institution/${domain}/backend/paper/search/sponsor'/>", {
				theme:"facebook",
				hintText: "请输入资助单位",
				searchingText: "正在查询...",
				noResultsText: "未查询到结果",
				preventDuplicates: true,
				queryParam:"q"
			});
			
			//附件队列
			var attachment ={
					//数据
					data:[],
					
					remove:function(clbId){
						for(var i in this.data){
							if(this.data[i].clbId==clbId){
								this.data.splice(i,1);
								this.render();
								return;
							}
						}
					},
					
					//插入到末尾
					append:function(item){
						if(this.isContain(item)){
							return false;
						}
						this.data.push(item);
						this.render();
						return true;
					},
					//是否已存在
					isContain:function(item){
						if(this.data.length==0){
							return false;
						}
						for(var i in this.data){
							if(this.data[i].clbId==item.clbId){
								return true;
							}
						}
						return false;
					},
					//控制表格显示
					render:function(){
						var item = $('#clbTemplate').tmpl(this.data,{
							judiceBool:function(bool){
								return bool?'是':'否';
							}
						});
// 						item.find('.previewBtn').lightbox({
// 						    fitToScreen: true,
// 						    fileLoadingImage: "<dhome:url value='/resources/images/loading.gif'/>",
// 				            fileBottomNavCloseImage: "<dhome:url value='/resources/images/closelabel.gif'/>",
// 						    imageClickClose: false
// 					    });
						
						$('#clbContent').html(item); 
					}
			};
			
			var template = {
					//数据
					data:[],
					content:'',
					template:'',
					
					remove:function(id){
						for(var i in this.data){
							if(this.data[i].id==id){
								this.data.splice(i,1);
								this.render();
								return;
							}
						}
					},
					
					findItem:function(id){
						for(var i in this.data){
							if(this.data[i].id==id){
								return this.data[i];
							}
						}
					},
					
					
					replace:function(item){
						for(var i in this.data){
							if(this.data[i].id==item.id){
								this.data[i] = item;
								this.render();
								return true;
							}
						}
					},
					
					insert:function(index,item){
						var part1 = this.data.slice( 0, index );
						var part2 = this.data.slice( index );
						part1.push( item );
						this.data = ( part1.concat( part2 ) );
						
						this.render();
					},
					
					//插入到末尾
					append:function(item){
						if(this.isContain(item)){
							return this.replace(item);
						}
						this.data.push(item);
						this.render();
						return true;
					},
					//是否已存在
					isContain:function(item){
						if(this.data.length==0){
							return false;
						}
						for(var i in this.data){
							if(this.data[i].id==item.id){
								return true;
							}
						}
						return false;
					},
					//控制表格显示
					render:function(){
						$(this.content).html($(this.template).tmpl(this.data,{
							judiceTime:function(time){
				                var time = new Date(time);
				                if(time.getFullYear() == 3000)
				                	return '至今';
				                
				                var ymdhis = "";
				                ymdhis += time.getFullYear() + ".";
				                var month = time.getMonth()+1;
				                if(month > 9)
				                    ymdhis += month;
				                else
				                	ymdhis += ("0" + month);
				                return ymdhis;
							}
						})); 
					}
			};
			
			function addDefaultAuthor(data){
				var item ={
					    id:${author.id},
						authorName:'${author.authorName}',
						authorEmail:'${author.authorEmail}',
						authorCompany:'',
						communicationAuthor:false,
						authorStudent:false,
						authorTeacher:false,
						authorType:'member',
						order:1
				}
				data.data = [];
				data.append(item);
			}
			
			function setAutocomplete(id,data){
				$(id).autocomplete('<dhome:url value="/institution/${domain}/backend/paper/search/author"/>',
			            {
					  		width:400,
							parse:function(data){
									return $.map(data, function(item) {
										return {
											data : item,
											result : '',
											value:item.authorName
										};
									});
							},
							formatItem:function(row, i, max) {
			    				return  row.authorName+"("+row.authorEmail+")" + "- [" + row.authorCompany+"]";
			 				},
							formatMatch:function(row, i, max) {
			    				return row.authorName + " " + row.authorEmail+""+row.authorCompany;
			 				},
							formatResult: function(row) {
			    				return row.authorName; 
			 				}
						}).result(function(event,item){
							var success= data.append(item);
							if(!success){
								alert('请勿重复添加');
							}
						});
			}
			
			var author={
					//数据
					data:[],
					table:'#paper-popup ul[id=authorTable]',
					template:'#authorTemplate',
					//重新排序
					sort:function(){
					},
					remove:function(id){
						for(var i in this.data){
							if(this.data[i].id==id){
								this.data.splice(i,1);
								this.render();
								return;
							}
						}
					},
					//获得最后一个order
					newOrder:function(){
						if(this.data.length==0){
							return 1;
						}
						return this.data[this.data.length-1].order+1;
					},
					//替换
					replace:function(item){
						for(var i in this.data){
							if(this.data[i].id==item.id){
								this.data[i]=item;
								return;
							}
						}
					},
					//插入到末尾
					append:function(item){
						if(this.isContain(item)){
							return false;
						}
						if(!item.order){
							item.order=this.newOrder();
						}
						this.data.push(item);
						this.render();
						return true;
					},
					//是否已存在
					isContain:function(item){
						if(this.data.length==0){
							return false;
						}
						for(var i in this.data){
							if(this.data[i].id==item.id){
								return true;
							}
						}
						return false;
					},
					//排序是否已存在
					hasOrder:function(order){
						if(this.data.length==0){
							return false;
						}
						for(var i in this.data){
							if(this.data[i].order==order){
								return true;
							}
						}
						return false;
					},
					//控制表格显示
					render:function(){
						if(this.data.length==0){
							$(this.table).hide();
							return;
						}else{
							this.data.sort(function(a,b){
								if(a.order>b.order){
									return 1;
								}else{
									return -1;
								}
							});
							$(this.table).show();
							$(this.table).html($(this.template).tmpl(this.data,{
								judiceBool:function(bool){
									return bool?'true':'false';
								}
							})); 
						}
					}
			};
			
			setAutocomplete('#paper-popup input[id=authorSearch]',author);
			
			
			/* 课题作者 */
			var topicAuthor = {};
			$.extend(topicAuthor,author,{
				table:'#topic-popup tbody[id=authorContent]',
				template:'#topicAuthorTemplate',
				append:function(item){
					if(this.isContain(item)){
						return false;
					}
					
					if($('#checkAuthor').attr("checked")){
						item.authorType='admin';  
					}else{
						item.authorType='member'; 
					}
					
					if(!item.order){
						item.order=this.newOrder();
					}
					this.data.push(item);
					this.render();
					return true;
				}
			});
			setAutocomplete('#topic-popup input[id=authorSearch]',topicAuthor);
			
			//删除课题作者
			$('.removeTopicAuthor').live('click',function(){
				var email=$(this).data('uid');
				topicAuthor.remove(email);
			});
			
			//添加课题作者
			$('#addTopicAuthor').on('click',function(){
				$('#addTopicAuthorForm').get(0).reset();
				$('#add-topic-author-popup').modal('show');
			});
			
			//编辑课题作者
			$('.editTopicAuthor').live('click',function(){
				$('#edit-topic-author-popup').modal('show');
				var $data=$(this).closest('tr').data('tmplItem').data;
				$('#editTopicAuthorForm').data('author',$data).get(0).reset();
				$('#edit-topic-author-popup span[id=editTrueName]').html($data.authorName);
				$('#edit-topic-author-popup span[id=editEmail]').html($data.authorEmail);
				$('#edit-topic-author-popup span[id=editAuthorCompany]').html($data.authorCompany); 
				$('#edit-topic-author-popup select[id=editAuthorType]').val($data.authorType);
			});
			
			
			//编辑-保存课题作者
			$('#editTopicAuthorSaveBtn').on('click',function(){
				if(!$('#editTopicAuthorForm').valid()){
					return;
				}
				var $data=$(this).closest('form').data('author');
				$data.authorType=$('#edit-topic-author-popup select[id=editAuthorType]').val();

				topicAuthor.replace($data);
				topicAuthor.render();
				$('#edit-topic-author-popup').modal('hide');
			});
			
			//保存课题作者
			$('#topicAuthorSaveBtn').on('click',function(){
				if(!$('#addTopicAuthorForm').valid()){
					return;
				}
				$.post('<dhome:url value="/institution/${domain}/backend/paper/author/create"/>',$('#addTopicAuthorForm').serialize()).done(function(data){
					if(data.success){
						topicAuthor.append(data.data);
						$('#add-topic-author-popup').modal('hide');
					}else{
						alert(data.desc);
					}
				});
			});
			
			//删除作者
			$('.removeAuthor').live('click',function(){
				var email=$(this).data('uid');
				author.remove(email);
			});
			
			//添加作者
			$('#addAuthor').on('click',function(){
				$('#addAuthorForm').get(0).reset();
				$('#add-author-popup').modal('show');
				$('#order').val(author.newOrder());
				judiceCheckbox('create');
			});
						
			//编辑-保存作者
			$('#editAuthorSaveBtn').on('click',function(){
				if(!$('#editAuthorForm').valid()){
					return;
				}
				var $data=$(this).closest('form').data('author');
				var order=parseInt($('#editOrder').val());
				if($data.order!=order&&author.hasOrder(order)){
					alert('第'+order+'作者已存在');
					return;
				}
				$data.order=order;
				$data.communicationAuthor=$('#editCommunicateAuthor input[type=checkbox]').is(':checked');
				$data.authorStudent=$('#editAuthorStudentL input[type=checkbox]').is(':checked');
				$data.authorTeacher=$('#editAuthorTeacherL input[type=checkbox]').is(':checked');

				author.replace($data);
				author.render();
				$('#edit-author-popup').modal('hide');
			});
			
			//保存作者
			$('#authorSaveBtn').on('click',function(){
				if(!$('#addAuthorForm').valid()){
					return;
				}
				var order=parseInt($('#order').val());
				if(author.hasOrder(order)){
					alert('第'+order+'作者已存在');
					return;
				}
				$.post('<dhome:url value="/institution/${domain}/backend/paper/author/create"/>',$('#addAuthorForm').serialize()).done(function(data){
					if(data.success){
						author.append(data.data);
						$('#add-author-popup').modal('hide');
					}else{
						alert(data.desc);
					}
				});
			});
			//删除文件 
			$('#fileRemove').on('click',function(){
				$('#fileName').val('');
				$('#clbId').val('0');
				$('#fileNameSpan').empty();
				$(this).hide();
			})
			//添加刊物按钮
			$('#addPubBtn').on('click',function(){
				$('#add-publication-popup').modal('show');
				$('#addPubForm').get(0).reset();
			});
			
			//查询刊物
			$("#pubSearch").autocomplete('<dhome:url value="/institution/${domain}/backend/paper/search/publication"/>',
			            {
					  		width:400,
							parse:function(data){
									return $.map(data, function(item) {
										return {
											data : item,
											result : '',
											value:item.pubName
										};
									});
							},
							formatItem:function(row, i, max) {
			    				return  row.pubName;
			 				},
							formatMatch:function(row, i, max) {
			    				return row.pubName;
			 				},
							formatResult: function(row) {
			    				return row.pubName; 
			 				}
						}).result(function(event,item){
							$("#pubSearch").val("");
							$("#pubSearch").val(item.pubName);
							$("#pubSearch").attr("data-pid",item.id);
							if(!success){
								alert('添加失败');
							}
						});
			//保存刊物按钮
			$('#pubSaveBtn').on('click',function(){
				if($('#addPubForm').valid()){
					$.post('<dhome:url value="/institution/${domain}/backend/publication/create"/>',$('#addPubForm').serialize()).done(function(data){
						if(data.success){
							$("#pubSearch").val("");
							$("#pubSearch").val(data.data.pubName);
							$("#pubSearch").attr("data-pid",data.data.id);
							$('#add-publication-popup').modal('hide');
						}else{
							alert('添加失败！');
						}
					});
				}
			});
			
			$('#order').on('keyup',function(){
				judiceCheckbox('create');
			});
			$('#editOrder').on('keyup',function(){
				judiceCheckbox('update');
			});
			
			//判定添加作者，checkbox显隐逻辑
			function judiceCheckbox(oper){
				function hideAndUnchecked($0){
					$0.hide().find('input[type=checkbox]').removeAttr('checked');
				}
				var orderStr='';
				var studentLabel='';
				var teacherLabel='';
				if(oper=='create'){
					orderStr=$('#order').val();
					studentLabel='authorStudentL';
					teacherLabel='authorTeacherL'
					
				}else{
					orderStr=$('#editOrder').val();
					studentLabel='editAuthorStudentL';
					teacherLabel='editAuthorTeacherL'
				}
				if(orderStr==''){
					hideAndUnchecked($('#'+studentLabel));
					hideAndUnchecked($('#'+teacherLabel));
					return;
				}
				var order=parseInt(orderStr);
				//非正常的数字
				if(!order){
					hideAndUnchecked($('#'+studentLabel));
					hideAndUnchecked($('#'+teacherLabel));
					return;
				}
				if(order==1){
					$('#'+studentLabel).show();
					hideAndUnchecked($('#'+teacherLabel));
					return;
				} 
				if(order==2){
					$('#'+teacherLabel).show();
					hideAndUnchecked($('#'+studentLabel));
					return;
				}
				hideAndUnchecked($('#'+studentLabel));
				hideAndUnchecked($('#'+teacherLabel));
			}
			//编辑作者
			$('.editAuthor').live('click',function(){
				$('#edit-author-popup').modal('show');
				var $data=$(this).closest('li').data('tmplItem').data;
				$('#editAuthorForm').data('author',$data).get(0).reset();
				$('#editTrueName').html($data.authorName);
				$('#editEmail').html($data.authorEmail);
				$('#editAuthorCompany').html($data.authorCompany); 
				$('#editOrder').val($data.order);
				judiceCheckbox('editAuthorStudentL','editAuthorTeacherL');
				if($data.communicationAuthor){
					$('#editCommunicateAuthor input[type="checkbox"]').click();
				} 
				if($data.authorStudent){
					$('#editAuthorStudentL input[type=checkbox]').click();
				}
				if($data.authorTeacher){
					$('#editAuthorTeacherL input[type=checkbox]').click();
				}
			});
			//编辑作者验证
			$('#editAuthorForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 editOrder:{
				 		required:true,
				 		min:1,
				 		digits:true,
				 		max:99999999
				 	}
				 },
				 messages:{
					 editOrder:{
					 		required:'作者排序不允许为空',
					 		min:'超出允许值',
					 		digits:'必须为正整数',
					 		max:'超出允许值'
					 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			
			//添加作者Dialog，验证
			$('#addTopicAuthorForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 authorName:{
						 required:true
						 },
					authorEmail:{
						 required:true,
						 email:true
						 },
				 	
				 	authorCompany:{
				 		required:true
				 	}
				 },
				 messages:{
					 authorName:{
						 required:'姓名不允许为空'
						 },
						 authorEmail:{
							 required:'邮箱不允许为空',
							 email:'非法的邮箱格式'
						 },
					 	authorCompany:{
					 		required:'单位不允许为空'
					 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#add-topic-author-popup span[id="+$(element).attr("name")+sub+"]";
 					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			
			//添加作者Dialog，验证
			$('#addAuthorForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 authorName:{
						 required:true
						 },
					authorEmail:{
						 required:true,
						 email:true
						 },
				 	
				 	authorCompany:{
				 		required:true
				 	},
				 	order:{
				 		required:true,
				 		min:1,
				 		digits:true,
				 		max:99999999
				 	}
				 },
				 messages:{
					 authorName:{
						 required:'姓名不允许为空'
						 },
						 authorEmail:{
							 required:'邮箱不允许为空',
							 email:'非法的邮箱格式'
						 },
					 	authorCompany:{
					 		required:'单位不允许为空'
					 	},
					 	order:{
					 		required:'作者排序不允许为空',
					 		min:'超出允许值',
					 		digits:'必须为正整数',
					 		max:'超出允许值'
					 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#add-author-popup span[id="+$(element).attr("name")+sub+"]";
 					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			//添加刊物Dialog，前台验证
			$('#addPubForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 pubName:{
						 required:true
						 }
				 	 }, 
				 messages:{
					 pubName:{
						 required:'刊物名称不允许为空'
						 }
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			}); 
			
			$('.editInfo').on('click',function(){
				var $edit=$('#edit-popup');
				$edit.modal('show');
				$.get('<dhome:url value="/institution/${domain}/backend/member/detail/${member.umtId}.json"/>').done(function(data){
					$('#edit-popup #trueName').val(data.trueName);
					$('#edit-popup #sex').val(data.sex);
					$('#edit-popup #technicalTitle').val(data.technicalTitle);
					$('#edit-popup #officeAddress').val(data.officeAddress);
					$('#edit-popup #officeTelephone').val(data.officeTelephone);
					$('#edit-popup #mobilePhone').val(data.mobilePhone);
				});
			});
			
			$('#saveBtn').on('click',function(){
				if(!$('#editForm').valid()){
					return;
				}
				$.post('<dhome:url value="/institution/${domain}/backend/member/update/${member.umtId }"/>',$('#edit-popup form').serialize()).done(function(data){
					if(data){
						window.location.reload();
					}else{
						alert('更新失败！');
					}
				});
			});
			
			//提交论文验证
			$('#paperForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 title:{
						 required:true,
						 maxlength:250
						 },
					 volumeNumber:{
						required:true	 
				 	 },
				 	startPage:{
				 		required:true,
				 		min:1,
				 		digits:true,
				 		max:99999999
				 	},
				 	endPage:{
				 		required:true,
				 		min:1,
				 		digits:true,
				 		max:99999999
				 	},
				 	citation:{
				 		min:0,
				 		digits:true,
				 		max:99999999
				 	},
				 	originalLink:{
				 		url:true
				 	},
				 	pubSearch:{
				 		required:true,
				 	}
				 },
				 messages:{ 
					 title:{
	    				 required:'论文名称不允许为空',
	    				 maxlength:'论文名称过长'
						 },
					 volumeNumber:{
						required:'卷号不允许为空',
						 maxlength:'卷号过长'
							},
					startPage:{
						required:'起始页不允许为空',
						min:'起始页超出范围',
						digits:'请输入整数',
				 		max:'起始页超出范围' 
					},
					endPage:{
						required:'终止页不允许为空',
						min:'终止页超出范围',
						digits:'请输入整数',
				 		max:'终止页超出范围' 
				 	},
				 	citation:{
				 		min:'论文引用次数超出范围',
				 		digits:'请输入整数',
				 		max:'论文引用次数超出范围'
				 	},
				 	originalLink:{
				 		url:'不符合URL规范'
				 	},
				 	pubSearch:{
				 		required:'刊物名称不允许为空'
				 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			}); 
			
			//提交论著验证
			$('#treatiseForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 name:{
						 required:true,
						 maxlength:250
						 }
				 },
				 messages:{ 
					 name:{
	    				 required:'论著名称不允许为空',
	    				 maxlength:'论著名称过长'
						 }
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#treatise-popup span[id="+$(element).attr("name")+sub+"]";
/* 					 var errorPlaceId="#"+$(element).attr("name")+sub;
 */					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			}); 
			//提交软件著作权验证
			$('#copyrightForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 name:{
						 required:true,
						 maxlength:250
						 }
				 },
				 messages:{ 
					 name:{
	    				 required:'软件著作权名称不允许为空',
	    				 maxlength:'软件著作权名称过长'
						 }
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
/* 					 var errorPlaceId="#"+$(element).attr("name")+sub;
 */					 var errorPlaceId="#copyright-popup span[id="+$(element).attr("name")+sub+"]";
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			}); 
			
			//提交专利验证
			$('#patentForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 name:{
						 required:true,
						 maxlength:250
						 }
				 },
				 messages:{ 
					 name:{
	    				 required:'专利名称不允许为空',
	    				 maxlength:'专利名称过长'
						 }
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#patent-popup span[id="+$(element).attr("name")+sub+"]";
 					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			}); 
			
			//提交奖励验证
			$('#awardForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 name:{
						 required:true,
						 maxlength:250
						 },
					grantBody:{
						 required:true,
						 maxlength:250
						 }
				 },
				 messages:{ 
					 name:{
	    				 required:'成果名称不允许为空',
	    				 maxlength:'成果名称过长'
						 },
				 grantBody:{
	    				 required:'授予机构不允许为空',
	    				 maxlength:'授予机构名称过长'
						 }
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#award-popup span[id="+$(element).attr("name")+sub+"]";
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			}); 
			

			/* 验证教育经历 */
			$("#eduForm").validate({
				submitHandler:function(form){
					form.submit();
				},
				rules:{
					degree:{required:true},
					degree2:{required:true},
					department:{required:true},
					tutor:{required:true},
					institutionZhName:{required:true},
					beginTimeYear:{required:true},
					beginTimeMonth:{required:true},
					endTimeYear:{required:true},
					endTimeMonth:{required:true}
				},
				messages:{
					degree:"<fmt:message key='personalEducationInfo.warning.degree'/>",
					degree2:"<fmt:message key='personalEducationInfo.warning.degree'/>",
					department:"专业不能为空",
					tutor:"导师姓名不能为空",
					institutionZhName:"<fmt:message key='personalEducationInfo.warning.university'/>",
					beginTimeYear:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					beginTimeMonth:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					endTimeYear:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"},
					endTimeMonth:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"}
				},
				success:function(label){
					checkTimeError(label);
				},
				errorPlacement:function(error, element){
						var $parent = element.parents("div.control-group");
						$parent.find("span.error").html("").append(error);
						
						var sub="_error_place";
						 var errorPlaceId="#education-popup span[id="+$(element).attr("name")+sub+"]";

					 	 $(errorPlaceId).html("");
						 	error.appendTo($(errorPlaceId));	
					
				},
				onclick:false
			});
			
			/* 验证工作经历 */
			$("#workForm").validate({
				submitHandler:function(form){
					form.submit();
				},
				rules:{
					position:{required:true},
					institutionZhName:{required:true},
					beginTimeYear:{required:true},
					beginTimeMonth:{required:true},
					endTimeYear:{required:true},
					endTimeMonth:{required:true}
				},
				messages:{
					position:"职称不能为空",
					institutionZhName:"工作单位不能为空",
					beginTimeYear:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					beginTimeMonth:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					endTimeYear:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"},
					endTimeMonth:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"}
				},
				success:function(label){
					checkTimeError(label);
				},
				errorPlacement:function(error, element){
					var $parent = element.parents("div.control-group");
					$parent.find("span.error").html("").append(error);
					
					var sub="_error_place";
					 var errorPlaceId="#work-popup span[id="+$(element).attr("name")+sub+"]";

				 	 $(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));	
				},
				onclick:false
			});
			
			/* 验证人才培养 */
			$("#trainingForm").validate({
				submitHandler:function(form){
					form.submit();
				},
				rules:{
					studentName:{required:true},
					nationality:{required:true},
					major:{required:true},
					beginTimeYear:{required:true},
					beginTimeMonth:{required:true},
					endTimeYear:{required:true},
					endTimeMonth:{required:true}
				},
				messages:{
					studentName:"学生姓名不能为空",
					nationality:"国籍不能为空",
					major:"专业不能为空",
					beginTimeYear:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					beginTimeMonth:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					endTimeYear:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"},
					endTimeMonth:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"}
				},
				success:function(label){
					checkTimeError(label);
				},
				errorPlacement:function(error, element){
					var $parent = element.parents("div.control-group");
					$parent.find("span.error").html("").append(error);
					
					var sub="_error_place";
					 var errorPlaceId="#work-popup span[id="+$(element).attr("name")+sub+"]";

				 	 $(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));	
				},
				onclick:false
			});
			
			/* 验证课题 */
			$("#topicForm").validate({
				popup:'#topic-popup',
				submitHandler:function(form){
					form.submit();
				},
				rules:{
					name:{required:true},
					topic_no:{required:true},
					project_cost:{required:true},
					personal_cost:{required:true},
					start_year:{required:true},
					start_month:{required:true},
					end_year:{required:true},
					end_month:{required:true}
				},
				messages:{
					name:"项目名称不能为空",
					topic_no:"编号不能为空",
					project_cost:"项目经费不能为空",
					personal_cost:"个人经费不能为空",
					start_year:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					start_month:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					end_year:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"},
					end_month:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"}
				},
				success:function(label){
					checkTimeError(label);
				},
				errorPlacement:function(error, element){
					var $parent = element.parents("div.control-group");
					$parent.find("span.error").html("").append(error);
					
					var sub="_error_place";
					 var errorPlaceId="#topic-popup span[id="+$(element).attr("name")+sub+"]";

				 	 $(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));	
				},
				onclick:false
			});
			
			
			/* 验证学术任职 */
			$("#academicForm").validate({
				submitHandler:function(form){
					form.submit();
				},
				rules:{
					name:{required:true},
					position:{required:true},
					startYear:{required:true},
					startMonth:{required:true},
					endYear:{required:true},
					endMonth:{required:true}
				},
				messages:{
					name:"组织名称不能为空",
					position:"职位不能为空",
					startYear:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					startMonth:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					endYear:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"},
					endMonth:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"}
				},
				success:function(label){
					checkTimeError(label);
				},
				errorPlacement:function(error, element){
					var $parent = element.parents("div.control-group");
					$parent.find("span.error").html("").append(error);
					
					var sub="_error_place";
					 var errorPlaceId="#academic-popup span[id="+$(element).attr("name")+sub+"]";

				 	 $(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));	
				},
				onclick:false
			});
			
			/* 验证期刊任职 */
			$("#periodicalForm").validate({
				submitHandler:function(form){
					form.submit();
				},
				rules:{
					name:{required:true},
					position:{required:true},
					startYear:{required:true},
					startMonth:{required:true},
					endYear:{required:true},
					endMonth:{required:true}
				},
				messages:{
					name:"期刊名称不能为空",
					position:"职位不能为空",
					startYear:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					startMonth:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					endYear:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"},
					endMonth:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"}
				},
				success:function(label){
					checkTimeError(label);
				},
				errorPlacement:function(error, element){
					var $parent = element.parents("div.control-group");
					$parent.find("span.error").html("").append(error);
					
					var sub="_error_place";
					 var errorPlaceId="#periodical-popup span[id="+$(element).attr("name")+sub+"]";

				 	 $(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));	
				},
				onclick:false
			});
			
			
			function checkTimeError(label){
				var $parent = label.parents("div.control-group");
				if(label.attr("for")=="startYear" && $parent.find("select[id=startMonth]").val() == ''){
					$parent.find("span.error").html("").append("请选择开始时间月份");
				}else if(label.attr("for")=="startMonth" && $parent.find("select[id=startYear]").val() == ''){
					$parent.find("span.error").html("").append("请选择开始时间年份");
				}else if(label.attr("disabled")){
					if(label.attr("for")=="endYear" && $parent.find("select[id=endMonth]").val() == ''){
						$parent.find("span.error").html("").append("请选择结束时间月份");
					}else if(label.attr("for")=="endMonth" && $parent.find("select[id=endYear]").val() == ''){
						$parent.find("span.error").html("").append("请选择结束时间年份");
					}
				}else if(label.attr("for")=="endYear" && $parent.find("select[id=endMonth]").val() == ''){
					$parent.find("span.error").html("").append("请选择结束时间月份");
				}else if(label.attr("for")=="endMonth" && $parent.find("select[id=endYear]").val() == ''){
					$parent.find("span.error").html("").append("请选择结束时间年份");
				}
			}
			
			
			//提交课题验证
			/* $('#topicForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 name:{
						 required:true,
						 maxlength:250
						 }
				 },
				 messages:{ 
					 name:{
	    				 required:'专利名称不允许为空',
	    				 maxlength:'专利名称过长'
						 }
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#patent-popup span[id="+$(element).attr("name")+sub+"]";

 				 	 $(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});  */
			
			$("input[type=checkbox][name=now]").click(function(){
				var checked = $(this).attr("checked");
				if(checked || checked == 'checked'){
					$("select[name=endTimeYear]").val("0000").attr("disabled",true);
					$("select[name=endTimeMonth]").val("00").attr("disabled",true);
					
					$("select[id=endYear]").val("0000").attr("disabled",true);
					$("select[id=endMonth]").val("00").attr("disabled",true);
					
					$("input[name=endTime]").val("");
				}else{
					$("select[name=endTimeYear]").val("").attr("disabled",false);
					$("select[name=endTimeMonth]").val("").attr("disabled",false);
					
					$("select[id=endYear]").val("").attr("disabled",false);
					$("select[id=endMonth]").val("").attr("disabled",false);
				}
			});
			
			function selectDateTime(popup,beginTime,endTime){
				var itemB = beginTime.split(".");
				$(popup+"  select[name=beginTimeYear]").val(itemB[0]);
				$(popup+"  select[name=beginTimeMonth]").val(itemB[1]);
				if($.trim(endTime) != "<fmt:message key='personalWorkInfo.untilnow'/>"){
					$(popup+"  select[name=endTimeYear]").attr("disabled", false);
					$(popup+"  select[name=endTimeMonth]").attr("disabled", false);
					$(popup+"  input[type=checkbox][name=now]").attr("checked", false);
					var itemE = endTime.split(".");
					$(popup+"  select[name=endTimeYear] option[value="+itemE[0]+"]").attr("selected","selected");
					$(popup+"  select[name=endTimeMonth] option[value="+itemE[1]+"]").attr("selected","selected");
				}else{
					$(popup+"  select[name=endTimeYear]").attr("disabled", true);
					$(popup+"  select[name=endTimeMonth]").attr("disabled", true);
					$(popup+"  input[type=checkbox][name=now]").attr("checked", true);
				}
			};
			
			
			function checkDateTime(popup ){
				var beginTimeYear = $(popup+" select[name=beginTimeYear]").val();
				var beginTimeMonth = $(popup+" select[name=beginTimeMonth]").val();
				var endTimeYear = $(popup+" select[name=endTimeYear]").val();
				var endTimeMonth = $(popup+" select[name=endTimeMonth]").val();
				if(endTimeYear<beginTimeYear){
					return false;
				}else if(endTimeYear == beginTimeYear){
					return (endTimeMonth<beginTimeMonth)?false:true;
				}else{
					return true;
				}
			};
			
			/* 检查学术任职、期刊任职的开始结束时间 */
			function checkTime(popup){
				var beginTimeYear = $(popup+" select[id=startYear]").val();
				var beginTimeMonth = $(popup+" select[id=startMonth]").val();
				var endTimeYear = $(popup+" select[id=endYear]").val();
				var endTimeMonth = $(popup+" select[id=endMonth]").val();
				$(popup+" input[name=endTime]").val(endTimeYear+"-"+endTimeMonth+"-01");
				
				if($(popup+" input[type=checkbox][name=now]").attr("checked")){
					endTimeYear = '3000'; 
					endTimeMonth = '1';
					$(popup+" input[name=endTime]").val("3000-01-01");
				}
				$(popup+" input[name=beginTime]").val(beginTimeYear+"-"+beginTimeMonth+"-01");
				if(endTimeYear<beginTimeYear || (endTimeYear == beginTimeYear && endTimeMonth<beginTimeMonth )){
					var $parent = $(popup+" select[id=endYear]").parents("div.control-group");
					$parent.find("span.error").html("").append("<fmt:message key='personalWorkInfo.warning.endAfterBegin'/>");
					return false;
				}
				
				return true;
			}
			
			function setTime(popup){
				var beginTimeYear = $(popup+" select[name=beginTimeYear]").val();
				var beginTimeMonth = $(popup+" select[name=beginTimeMonth]").val();
				var endTimeYear = $(popup+" select[name=endTimeYear]").val();
				var endTimeMonth = $(popup+" select[name=endTimeMonth]").val();
				$(popup+" input[name=endTime]").val(endTimeYear+"-"+endTimeMonth+"-01");
				
				if($(popup+" input[type=checkbox][name=now]").attr("checked")){
					endTimeYear = '3000'; 
					endTimeMonth = '1';
					$(popup+" input[name=endTime]").val("3000-01-01");
				}
				$(popup+" input[name=beginTime]").val(beginTimeYear+"-"+beginTimeMonth+"-01");
				
				if(endTimeYear<beginTimeYear || (endTimeYear == beginTimeYear && endTimeMonth<beginTimeMonth )){
					var $parent = $(popup+" select[name=endTimeYear]").parents("div.control-group");
					$parent.find("span.error").html("").append("<fmt:message key='personalWorkInfo.warning.endAfterBegin'/>");
					return false;
				}
				return true;
			}
			
			
			
			/* 显示表格，数据按结束时间排序 */
			function render4Time(temData,data){
				if(temData.isContain(data)){
					temData.remove(data.id);
				}
				for(var i in temData.data){
					var time = new Date(temData.data[i].endTime);
	                var year = time.getUTCFullYear();
	                var currentTime = new Date(data.endTime);
	                var currentYear = currentTime.getFullYear();
	                if(currentYear > year){
	                	temData.insert(i,data);
	                	break;
	                }else if(currentYear == year){
	                	 var month = time.getMonth();
	                	 var currentMonth = currentTime.getUTCMonth();
	                	 if(currentMonth > month){
	                		 temData.insert(i,data);
			                break;
	                	 }
	                }
				}
				
				if(!temData.isContain(data)){
					temData.append(data);
				}
			}
			
			/* 清除表单数据 */
			function clearForm(form){
				$(':input',form)  
				.not(':button, :submit, :reset, :hidden')  
				.val('')  
				.removeAttr('checked')  
				.removeAttr('selected');
				
				$(form+" span.error").empty();
				$(form+" input[name=id]").val('0')
				
				$("select[name=endTimeYear]").val("").attr("disabled",false);
				$("select[name=endTimeMonth]").val("").attr("disabled",false);
				
				$("select[id=endYear]").val("").attr("disabled",false).removeClass('error');
				$("select[id=endMonth]").val("").attr("disabled",false).removeClass('error');
				$("select[id=startYear]").removeClass('error');
				$("select[id=startMonth]").removeClass('error');
				
				$tokenObj.tokenInput("clear");
				$tokenSponsor.tokenInput("clear");
				
				$('#fileNameSpan').html('');
				$('#fileName').val('');
				$('#clbId').val('0'); 
				$('#fileRemove').hide();
				
				$('#paperfileNameSpan').html('');
				$('#paperfileName').val('')
				$('#paperclbId').val('0'); 
				$('#paperfileRemove').hide();
			}
			
			//添加刊物按钮
			$('#addPubBtn').on('click',function(){
				$('#add-publication-popup').modal('show');
				$('#addPubForm').get(0).reset();
			});
			
			$('#cancelEducationBtn').live('click',function(){
				clearForm('#eduForm');
			});
			
			$('.addEducation').on('click',function(){
				var $edit=$('#education-popup');
				$edit.modal('show');
				$("#education-popup h3[id=popupTitle]").html('添加教育经历');

			});
			/* 保存教育经历 */
			$('#saveEducationBtn').on('click',function(){
				if($('#eduForm').valid()){
					if(!checkTime('#education-popup')){
						return;
					}	
					var data=$('#eduForm').serialize();
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/education/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							//educations.append(data.data);
							educations.append4Time(data.data);
							$('#education-popup').modal('hide');
							$('#eduNoData').hide();
						    clearForm('#eduForm');
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			
			/* 添加工作经历 */
			$('.addWork').on('click',function(){
				var $edit=$('#work-popup');
				$edit.modal('show');
				$("#work-popup h3[id=popupTitle]").html('添加工作经历');

			});
			
			$('#cancelWorkBtn').live('click',function(){
				clearForm('#workForm');
			});
			
			/* 保存工作经历 */
			$('#saveWorkBtn').on('click',function(){
				if($('#workForm').valid()){
					if(!checkTime('#work-popup')){
						return;
					}
					var data=$('#workForm').serialize();
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/work/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							//works.append(data.data);
							works.append4Time(data.data);
							$('#work-popup').modal('hide');
							$('#worklist ul[id=workContent]').show();
							$('#workNoData').hide();
							clearForm('#workForm');
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			/* 添加论著 */
			$('.addTreatise').on('click',function(){
				var $edit=$('#treatise-popup');
				$edit.modal('show');
				$("#treatise-popup h3[id=popupTitle]").html('添加论著');

			});
			
			$('#cancelTreatiseBtn').live('click',function(){
				clearForm('#treatiseForm');
			});
			
			/* 保存论著 */
			$('#saveTreatiseBtn').on('click',function(){
				if($('#treatiseForm').valid()){
					var data=$('#treatiseForm').serialize();
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/treatise/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							if(treatises.isContain(data.data)){
								treatises.replace(data.data);
							}else{
								treatises.append(data.data);
							}
							
							$('#treatise-popup').modal('hide');
							clearForm('#treatiseForm');
							$('#treatise table[id=table]').show();
							$('#treaNoData').hide();
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			/* 添加奖励 */
			$('.addAward').on('click',function(){
				var $edit=$('#award-popup');
				$edit.modal('show');
				
				$("#award-popup h3[id=popupTitle]").html('添加奖励');

			});
			
			$('#cancelAwardBtn').live('click',function(){
				clearForm('#awardForm');
			});
			
			
			/* 保存奖励 */
			$('#saveAwardBtn').on('click',function(){
				if($('#awardForm').valid()){
					var data=$('#awardForm').serialize();
					if(attachment.data.length!=0){
						for(var i in attachment.data){
							var attach =attachment.data[i];
							data+='&clbId[]='+attach.clbId;
							data+='&fileName[]='+attach.fileName;
						}
					}else{
						data+="&clbId[]=&fileName[]=";
					}
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/award/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							if(awards.isContain(data.data)){
								awards.replace(data.data);
							}else{
								awards.append(data.data);
							}
							
							$('#award-popup').modal('hide');
							clearForm('#awardForm');
							$('#award table[id=table]').show();
							$('#awardNoData').hide();
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			
			/* 添加专利 */
			$('.addPatent').on('click',function(){
				var $edit=$('#patent-popup');
				$edit.modal('show');
				$("#patent-popup h3[id=popupTitle]").html('添加专利');

			});
			
			$('#cancelPatentBtn').live('click',function(){
				clearForm('#patentForm');
			});
			
			
			/* 保存专利 */
			$('#savePatentBtn').on('click',function(){
				if($('#patentForm').valid()){
					var data=$('#patentForm').serialize();
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/patent/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							if(patents.isContain(data.data)){
								patents.replace(data.data);
							}else{
								patents.append(data.data);
							}
							
							$('#patent-popup').modal('hide');
							clearForm('#patentForm');
							$('#patent table[id=table]').show();
							$('#patentNoData').hide();
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			/* 添加软件著作权 */
			$('.addCopyright').on('click',function(){
				var $edit=$('#copyright-popup');
				$edit.modal('show');
				
				$("#copyright-popup h3[id=popupTitle]").html('添加软件著作权');

			});
			
			$('#cancelCopyrightBtn').live('click',function(){
				clearForm('#copyrightForm');
			});
			
			
			/* 保存软件著作权 */
			$('#saveCopyrightBtn').on('click',function(){
				if($('#copyrightForm').valid()){
					var data=$('#copyrightForm').serialize();
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/copyright/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							if(copyrights.isContain(data.data)){
								copyrights.replace(data.data);
							}else{
								copyrights.append(data.data);
							}
							
							$('#copyright-popup').modal('hide');
							clearForm('#copyrightForm');
							$('#copyright table[id=table]').show();
							$('#crNoData').hide();
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			
			/* 添加学术任职 */
			$('.addAcademic').on('click',function(){
				var $edit=$('#academic-popup');
				$edit.modal('show');
				$("#academic-popup h3[id=popupTitle]").html('添加学术任职');

			});
			
			$('#cancelAcademicBtn').live('click',function(){
				clearForm('#academicForm');
			});
			
			
			/* 保存学术任职 */
			$('#saveAcademicBtn').on('click',function(){
				if($('#academicForm').valid()){
					if(!checkTime('#academicForm')){
						return false;
					}
					var data=$('#academicForm').serialize();
					if($("#academicForm input[type=checkbox][name=now]").attr("checked")){
						data +='&endYear=' + 3000;
						data +='&endMonth='+ new Date().getMonth() + 1;
					}
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/academic/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							if(academics.isContain(data.data)){
								academics.replace(data.data);
							}else{
								academics.append(data.data);
							}
							
							$('#academic-popup').modal('hide');
							clearForm('#academicForm');
							$('#academic table[id=table]').show();
							$('#acmNoData').hide();
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			/* 添加期刊任职 */
			$('.addPeriodical').on('click',function(){
				var $edit=$('#periodical-popup');
				$edit.modal('show');
				
				$("#periodical-popup h3[id=popupTitle]").html('添加期刊任职');

			});
			
			$('#cancelPeriodicalBtn').live('click',function(){
				clearForm('#periodicalForm');
			});
			
			
			/* 保存期刊任职 */
			$('#savePeriodicalBtn').on('click',function(){
				if($('#periodicalForm').valid()){
					if(!checkTime('#periodicalForm')){
						return false;
					}
					var data=$('#periodicalForm').serialize();
					if($("#periodicalForm input[type=checkbox][name=now]").attr("checked")){
						data +='&endYear=' + 3000;
						data +='&endMonth='+ new Date().getMonth() + 1;
					}
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/periodical/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							if(periodicals.isContain(data.data)){
								periodicals.replace(data.data);
							}else{
								periodicals.append(data.data);
							}
							
							$('#periodical-popup').modal('hide');
							clearForm('#periodicalForm');
							$('#periodical table[id=table]').show();
							$('#perNoData').hide();
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			/* 添加课题 */
			$('.addTopic').on('click',function(){
				var $edit=$('#topic-popup');
				$edit.modal('show');
				
				$("#topic-popup h3[id=popupTitle]").html('添加课题');

				topicAuthor.data =[];
				topicAuthor.render();
				//addDefaultAuthor(topicAuthor);
			});
			
			$('#cancelTopicBtn').live('click',function(){
				clearForm('#topicForm');
			});
			
			
			/* 保存课题 */
			$('#saveTopicBtn').on('click',function(){
				if($('#topicForm').valid()){
					if(!checkTime('#topicForm')){
						return false;
					}
					var data=$('#topicForm').serialize();
					if($("#topicForm input[type=checkbox][name=now]").attr("checked")){
						data +='&end_year=' + 3000;
						data +='&end_month='+ new Date().getMonth() + 1;
					}
					
					if(topicAuthor.data.length!=0){
						for(var i in topicAuthor.data){
							var auth=topicAuthor.data[i];
							data+='&uid[]='+auth.id;
							data+='&authorType[]='+auth.authorType;
						}
					}else{
						data+="&uid[]=&authorType[]=";
					}
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/topic/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							if(topics.isContain(data.data)){
								topics.replace(data.data);
							}else{
								topics.append(data.data);
							}
							
							$('#topic-popup').modal('hide');
							clearForm('#topicForm');
							$('#topic table[id=table]').show();
							$('#topicNoData').hide();
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			
			/* 添加人才培养 */
			$('.addTrain').on('click',function(){
				var $edit=$('#training-popup');
				$edit.modal('show');
				
				$("#training-popup h3[id=popupTitle]").html('添加人才培养');
			});
			
			$('#cancelTrainingBtn').live('click',function(){
				clearForm('#trainingForm');
			});
			
			/* 保存人才培养 */
			$('#saveTrainingBtn').on('click',function(){
				if($('#trainingForm').valid()){
					if(!checkTime('#training-popup')){
						return;
					}	
					var data=$('#trainingForm').serialize();
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/training/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							if(trainings.isContain(data.data)){
								trainings.replace(data.data);
							}else{
								trainings.append(data.data);
							}
							$('#training-popup').modal('hide');
							clearForm('#trainingForm');
							$('#training table[id=table]').show();
							$('#trainNoData').hide();
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			/* 添加论文 */
			$('.addPaper').on('click',function(){
				var $edit=$('#paper-popup');
				$edit.modal('show');
				$("#paper-popup h3[id=popupTitle]").html('添加论文');
				
				author.data=[];
				author.render();
				//addDefaultAuthor(author);
			});
			
			$('#cancelPaperBtn').live('click',function(){
				clearForm('#paperForm');
			});
			
			
			//提交论文
			$('#savePaperBtn').on('click',function(){
				if($('#paperForm').valid()){
					var data=$('#paperForm').serialize();
					var pid=$('#pubSearch').data("pid");
					data+='&pid='+pid;
					if(author.data.length!=0){
						for(var i in author.data){
							var auth=author.data[i];
							data+='&uid[]='+auth.id;
							data+='&order[]='+auth.order;
							data+='&communicationAuthor[]='+auth.communicationAuthor;
							data+='&authorStudent[]='+auth.authorStudent;
							data+='&authorTeacher[]='+auth.authorTeacher;
						}
					}else{
						data+="&uid[]=&order[]=&communicationAuthor[]=&authorStudent[]=&authorTeacher[]=";
					}
					data+='&pubName='+$("#paperForm select[id=publicationId]").find("option:selected").text();
					$.post('<dhome:url value="/institution/${domain}/backend/member/modify/paper/${member.umtId }"/>',data).done(function(data){ 
						if(data.success){
							if(papers.isContain(data.data)){
								papers.replace(data.data);
							}else{
								papers.append(data.data);
							}
							$('#paper-popup').modal('hide');
							$('#paperNoData').hide();
							clearForm('#paperForm');
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('#editForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 trueName:{
						 required:true
						 }
				 	 },
				 messages:{
					 trueName:{
						 required:'姓名不允许为空'
						 }
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			
			
			
			//上传文件
			new qq.FileUploader({
				element : document.getElementById('fileUploader'),
				action :'<dhome:url value="/institution/${domain}/backend/member/upload"/>',
				sizeLimit : 20*1024 * 1024,
				allowedExtensions:['doc','docx','pdf'],
				onComplete : function(id, fileName, data) {
					if(!data){
						return false;
					}
					$('#fileUploadProcess').empty();
					if(data.success){
						$('#fileNameSpan').html(fileName);
						$('#fileName').val(fileName)
						$('#clbId').val(data.data);
						$('#fileRemove').show();
					}else{
						alert(data.desc);
					}
					
				},
				messages:{
		        	typeError:"请上传doc,docx,pdf文件",
		        	emptyError:"请不要上传空文件",
		        	sizeError:"大小超过20M限制"
		        },
		        showMessage: function(message){
		        	alert(message);
		        },
		        onProgress: function(id, fileName, loaded, total){
		        	$('#fileNameSpan').html(fileName);
		        	$('#fileUploadProcess').html("("+Math.round((loaded/total)*100)+"%)");
		        },
		        multiple:false
			});
			
			new qq.FileUploader({
				element : document.getElementById('paperfileUploader'),
				action :'<dhome:url value="/institution/${domain}/backend/member/upload"/>',
				sizeLimit : 20*1024 * 1024,
				allowedExtensions:['doc','docx','pdf'],
				onComplete : function(id, fileName, data) {
					if(!data){
						return false;
					}
					$('#paperfileUploadProcess').empty();
					if(data.success){
						$('#paperfileNameSpan').html(fileName);
						$('#paperfileName').val(fileName)
						$('#paperclbId').val(data.data);
						$('#paperfileRemove').show();
					}else{
						alert(data.desc);
					}
					
				},
				messages:{
		        	typeError:"请上传doc,docx,pdf文件",
		        	emptyError:"请不要上传空文件",
		        	sizeError:"大小超过20M限制"
		        },
		        showMessage: function(message){
		        	alert(message);
		        },
		        onProgress: function(id, fileName, loaded, total){
		        	$('#paperfileNameSpan').html(fileName);
		        	$('#paperfileUploadProcess').html("("+Math.round((loaded/total)*100)+"%)");
		        },
		        multiple:false
			});
			
			//奖励上传文件
			new qq.FileUploader({
				element : document.getElementById('awardFileUploader'),
				action :'<dhome:url value="/institution/${domain}/backend/award/upload"/>',
				sizeLimit : 20*1024 * 1024,
				//allowedExtensions:['doc','docx','pdf'],
				onComplete : function(id, fileName, data) {
					if(data.success){
						data.data.fileName = fileName;
						attachment.append(data.data);
					}else{
						alert("系统维护中，暂不能添加附件");
					}
				},
				messages:{
		        	//typeError:"请上传doc,docx,pdf文件",
		        	emptyError:"请不要上传空文件",
		        	sizeError:"大小超过20M限制"
		        },
		        showMessage: function(message){
		        	alert(message);
		        },
		        onProgress: function(id, fileName, loaded, total){
		        },
		        multiple:false
			});
			
			var educations = {};
			$.extend( educations, template,{
				content:'#eduContent',
				template:'#eduTemplate',
				
				append4Time:function(item){
					render4Time(this,item);
				}
			});
			$('.removeAwardAttachment').live('click',function(){
				var clbId=$(this).data('uid');
				attachment.remove(clbId);
			});
			
			/* 加载教育经历 */
			$.get('<dhome:url value="/institution/${domain}/backend/member/educations/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null && data.data.length > 0){
					 educations.data=data.data;
				}else{
					$('#eduNoData').show();
					educations.data =[];
				}
				 educations.render();
			});
			
			/* 加载工作经历 */
			var works = {};
			$.extend( works, template,{
				content:'#workContent',
				template:'#workTemplate',
				append4Time:function(item){
					render4Time(this,item);
				}
			});
			
			$.get('<dhome:url value="/institution/${domain}/backend/member/works/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null && data.data.length > 0){
					works.data=data.data;
				}else{
					$('#worklist ul[id=workContent]').hide();
					$('#workNoData').show();
					works.data =[];
				}
				works.render();
			});
			
			
			
			/* 加载论著 */
			var treatises = {};
			$.extend( treatises, template,{
				content:'#treatiseContent',
				template:'#tratiseTemplate',
				render:function(){
					$(this.content).html($(this.template).tmpl(this.data,{
						judicePublisher:function(index){
							var publishers = {
								     <c:forEach items="${publishers.entrySet() }" var="entry">
									 "${entry.key }" : "${entry.value.val }",
								     </c:forEach>};
							return publishers[index];
						},
						judiceDept:function(index){
							var depts = {
						     <c:forEach items="${deptMap.entrySet() }" var="entry">
							 "${entry.key }" : "${entry.value.shortName }",
						     </c:forEach>};
							return depts[index];
						}
					})); 
				}	
			});
			$.get('<dhome:url value="/institution/${domain}/backend/member/treatises/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null && data.data.length > 0){
					treatises.data=data.data;
				}else{
					$('#treatise table[id=table]').hide();
					$('#treaNoData').show();	
					treatises.data =[];
				}
				treatises.render();
			});
			
			/* 加载奖励 */
			var awards = {};
			$.extend( awards, template,{
				content:'#awardContent',
				template:'#awardTemplate',
				render:function(){
					$(this.content).html($(this.template).tmpl(this.data,{
						judiceAwardName:function(index){
							var awardNames = {
								     <c:forEach items="${awardNames.entrySet() }" var="entry">
									 "${entry.key }" : "${entry.value.val }",
								     </c:forEach>};
							return awardNames[index];
						},
					
						judiceType:function(index){
							var types = {
								     <c:forEach items="${awardTypes.entrySet() }" var="entry">
									 "${entry.key }" : "${entry.value.val }",
								     </c:forEach>};
							return types[index];
						},
						
						judiceGrade:function(index){
							var grades = {
						     <c:forEach items="${awardGrades.entrySet() }" var="entry">
							 "${entry.key }" : "${entry.value.val }",
						     </c:forEach>};
							return grades[index];
						},
						
						judiceDept:function(index){
							var depts = {
						     <c:forEach items="${deptMap.entrySet() }" var="entry">
							 "${entry.key }" : "${entry.value.shortName }",
						     </c:forEach>};
							return depts[index];
						}
					})); 
				}		
			});
			
			$.get('<dhome:url value="/institution/${domain}/backend/member/awards/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null && data.data.length > 0){
					awards.data=data.data;
				}else{
					$('#award table[id=table]').hide();
					$('#awardNoData').show();
					awards.data =[];
				}
				awards.render();
			});
			
			/* 加载学术任职 */
			var academics = {};
			$.extend( academics, template,{
				content:'#academicContent',
				template:'#academicTemplate',
				render:function(){
					$(this.content).html($(this.template).tmpl(this.data,{
						judiceName:function(index){
							var names = ["国家科技部","中国科学院自然委员","国防科学技术委员会","国家自然科学奖"];
							return names[index];
						},
						judicePosition:function(index){
							var positions = ["主要负责人","项目负责人","参与者"];								
							return positions[index];
						}
					})); 
				}		
			});
			$.get('<dhome:url value="/institution/${domain}/backend/member/academics/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null  && data.data.length > 0){
					academics.data=data.data;
				}else{
					$('#academic table[id=table]').hide();
					$('#acmNoData').show();
					academics.data =[];
				}
				academics.render();
			});
			
			/* 加载期刊任职 */
			var periodicals = {};
			$.extend( periodicals, template,{
				content:'#perContent',
				template:'#periodicalTemplate',
				render:function(){
					$(this.content).html($(this.template).tmpl(this.data,{
						judiceName:function(index){
							var names = ["中国科技网在线","中国科学院自然委员","国防科学技术委员会"];
							return names[index];
						},
						judicePosition:function(index){
							var positions = ["主要负责人","项目负责人","参与者"];								
							return positions[index];
						}
					})); 
				}		
			});
			
			$.get('<dhome:url value="/institution/${domain}/backend/member/periodicals/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null  && data.data.length > 0){
					periodicals.data=data.data;
				}else{
					$('#periodical table[id=table]').hide();
					$('#perNoData').show();
					periodicals.data =[];
				}
				periodicals.render();
			});
			
	
			
			/* 加载软件著作权 */
			var copyrights = {};
			$.extend( copyrights, template,{
				content:'#copyrightContent',
				template:'#copyrightTemplate',
				render:function(){
					$(this.content).html($(this.template).tmpl(this.data,{
						judiceType:function(index){
						   var types = ["部级","省级","其他"];
							return types[index - 1];
						},
						
						judiceGrade:function(index){
							var grades = ["一等奖","二等奖","三等奖","国际奖"];
							return grades[index];
						}
					})); 
				}	
			});
			$.get('<dhome:url value="/institution/${domain}/backend/member/copyrights/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null  && data.data.length > 0){
					copyrights.data=data.data;
				}else{
					$('#copyright table[id=table]').hide();
					$('#crNoData').show();
					copyrights.data =[];
				}
				copyrights.render();
			});
			
			
			/* 加载专利 */
			var patents = {};
			$.extend( patents, copyrights,{
				content:'#patentContent',
				template:'#patentTemplate',
			});
			$.get('<dhome:url value="/institution/${domain}/backend/member/patents/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null  && data.data.length > 0){
					patents.data=data.data;
				}else{
					$('#patent table[id=table]').hide();
					$('#patentNoData').show();
					patents.data =[];
				}
				patents.render();
			});
			
			/* 加载课题 */
			var topics = {};
			$.extend( topics, template,{
				content:'#topicContent',
				template:'#topicTemplate',
				render:function(){
					$(this.content).html($(this.template).tmpl(this.data,{
						judiceType:function(index){
							 var types = ["部级","省级","其他"];
								return types[index - 1];
						},
						
						judiceRole:function(index){
							if(index == 'admin')
								return '负责人'
							
							return '参与者';
						},
						
						judiceFund:function(index){
							var fundsFroms = ["985项目","国家重点基金项目","国家基金委员会"];
							
							return fundsFroms[index];
						}
					})); 
				}	
			});
			$.get('<dhome:url value="/institution/${domain}/backend/member/topics/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null  && data.data.length > 0){
					topics.data=data.data;
				}else{
					$('#topic table[id=table]').hide();
					$('#topicNoData').show();
					topics.data =[];
				}
				topics.render();
				
			});
			
			/* 加载人才培养 */
			var trainings = {};
			$.extend( trainings, template,{
				content:'#trainContent',
				template:'#trainingTemplate',
				render:function(){
					$(this.content).html($(this.template).tmpl(this.data,{
						judiceDegree:function(index){
							var degrees = ["学士","硕士","博士"];
							return degrees[index];
						},
						
						judiceTime:function(time){
			                var time = new Date(time);
			                if(time.getFullYear() == 3000)
			                	return '至今';
			                
			                var ymdhis = "";
			                ymdhis += time.getFullYear() + ".";
			                var month = time.getMonth()+1;
			                if(month > 9)
			                    ymdhis += month;
			                else
			                	ymdhis += ("0" + month);
			                return ymdhis;
						}
					})); 
				}	
			});
			$.get('<dhome:url value="/institution/${domain}/backend/member/trainings/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null  && data.data.length > 0){
					trainings.data=data.data;
				}else{
					$('#training table[id=table]').hide();
					$('#trainNoData').show();
					trainings.data =[];
				}
				trainings.render();
			});
			
			
			/* 加载论文 */
			var papers = {};
			$.extend( papers, template,{
				content:'#paperContent',
				template:'#paperTemplate',
			});
			
			$.get('<dhome:url value="/institution/${domain}/backend/member/papers/${member.umtId }"/>').done(function(data){
				if(data.success && data.data != null  && data.data.length > 0){
					papers.data=data.data;
				}else{
					$('#paperNoData').show();
					papers.data =[];
				}
				papers.render();
			});
			
			
			//点击作者查看详情
			$('.authorDetailPopover').live('click',function(){
				$('.authorDetailPopover').popover('destroy');
				var authorId=$(this).data('authorId');
				if($('#popover_'+authorId).size()==0){
					var $self=$(this);
					var data=$(this).data();
					var paperId=data.paperId;
					var authorId=data.authorId;
					$.get('<dhome:url value="/institution/${domain}/backend/paper/author/"/>'+paperId+"/"+authorId).done(function(result){
						if(result.success){
							var author = result.data.author;
							var content = '<li>姓名：'+author.authorName+'</li><li>邮箱：'+author.authorEmail+'</li><li>单位：'+author.authorCompany+'</li>';
							if(author.communicationAuthor){
								content += '<li>通讯作者：是</li>';
							}
							
							if(author.authorStudent){
								content += '<li>学生在读：是</li>';
							}
							
							if(author.authorTeacher){
								content += '<li>第一作者导师：是</li>';
							}
							
							if(author.home){
								content += '<a href="<dhome:url value="/people/{{= home}}"/>" target="_blank">个人主页</a></li>';
							}
							
							$self.popover({
								content:content,
								html:true,
								placement:'right',
								template:'<div id="popover_'+authorId+'" class="popover"><div class="arrow"></div><div class="popover-inner"><div class="popover-content" id="popover_content_'+authorId+'"><ul class="popAuthor""></ul></div></div></div>',
								trigger: "manual"	
							});
							$self.popover('show');
						
						}else{
							alert(result.desc);
						}
					});
				}else{
					$(this).popover('hide'); 
				}
			});
			
		　 function object(o) {
		　　　　function F() {}
		　　　　F.prototype = o;
		　　　　return new F();
		　　}
			
			$('#memberMenu').addClass("active");
			
			
			
			//删除教育经历
			$('.removeEducation').live('click',function(){
				if(confirm("教育经历删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/education/'+id+'"/>').done(function(data){
						if(data.success){
							educations.remove(id);
							if(educations.data.length == 0){
								$('#eduNoData').show();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editEducation').live('click',function(){
				var $edit=$('#education-popup');
				$edit.modal('show');
				
				var id = $(this).data('uid');
				var edu = educations.findItem(id);
				var beginTime = formatTime(edu.beginTime);
				var endTime = formatTime(edu.endTime);
				selectDateTime('#education-popup',beginTime,endTime);
				
				var degree=edu.degree;
				if(degree=='Doctor'||degree=='博士研究生'){
					degree='博士研究生';
				}
				if(degree=='Master'||degree=='硕士研究生'){
					degree='硕士研究生';
				}
				if(degree=='Banchelor'||degree=='本科'){
					degree='本科';
				}
				$("#education-popup h3[id=popupTitle]").html('修改教育经历');
				$("#education-popup input[name=id]").val(edu.id);
				$("#education-popup select[name=degree]").val(degree);
				$("#education-popup select[name=degree2]").val(edu.degree2);
				$("#education-popup input[name=institutionZhName]").val(edu.aliasInstitutionName);
				$("#education-popup input[name=department]").val(edu.department);
				$("#education-popup input[name=tutor]").val(edu.tutor);
				
				if(edu.graduationProjectCid != 0){
					$('#fileNameSpan').html(edu.graduationProject);
					$('#fileName').val(edu.graduationProject)
					$('#clbId').val(edu.graduationProjectCid);
					$('#fileRemove').show();
				}
				
				//$(".success").removeClass("success");
				//$(".error").empty();
				$("#education-popup ").modal("show");
				
			});
			
			//删除工作经历
			$('.removeWork').live('click',function(){
				if(confirm("工作经历删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/work/'+id+'"/>').done(function(data){
						if(data.success){
							works.remove(id);
							if(works.data.length == 0){
								$('#workNoData').show();
								$('#worklist ul[id=workContent]').hide();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editWork').live('click',function(){
				var $edit=$('#work-popup');
				$edit.modal('show');
				
				var id = $(this).data('uid');
				var work = works.findItem(id);
				var beginTime = formatTime(work.beginTime);
				var endTime = formatTime(work.endTime);
				selectDateTime('#work-popup',beginTime,endTime);
				
				$("#work-popup h3[id=popupTitle]").html('修改工作经历');
				$("#work-popup input[name=id]").val(work.id);
				$("#work-popup input[name=position]").val(work.position);
				$("#work-popup input[name=institutionZhName]").val(work.aliasInstitutionName);
				$("#work-popup input[name=department]").val(work.department);
				
				//$(".success").removeClass("success");
				////$(".error").empty();
				$("#work-popup ").modal("show");
			});
			
			
			//删除论著
			$('.removeTreatise').live('click',function(){
				if(confirm("论著删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/treatise/${member.umtId }/'+id+'"/>').done(function(data){
						if(data.success){
							treatises.remove(id);
							if(treatises.data.length == 0){
								$('#treaNoData').show();
								$('#treatise table[id=table]').hide();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editTreatise').live('click',function(){
				var $edit=$('#treatise-popup');
				$edit.modal('show');
				
				var id = $(this).data('uid');
				var treatise = treatises.findItem(id);
				
				$("#treatise-popup h3[id=popupTitle]").html('修改论著');
				$("#treatise-popup input[name=id]").val(treatise.id);
				$("#treatise-popup input[name=name]").val(treatise.name);
				$("#treatise-popup select[name=publisher]").val(treatise.publisher);
				$("#treatise-popup input[name=language]").val(treatise.language);
				$("#treatise-popup select[name=year]").val(treatise.year);
				$("#treatise-popup select[name=authorOrder]").val(treatise.authorOrder);
				$("#treatise-popup select[name=companyOrder]").val(treatise.companyOrder);
				$("#treatise-popup select[name=departId]").val(treatise.departId);
				
				//$(".success").removeClass("success");
				//$(".error").empty();
			});
			
			//删除奖励
			$('.removeAward').live('click',function(){
				if(confirm("奖励删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/award/${member.umtId }/'+id+'"/>').done(function(data){
						if(data.success){
							awards.remove(id);
							if(awards.data.length == 0){
								$('#awardNoData').show();
								$('#award table[id=table]').hide();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editAward').live('click',function(){
				var $edit=$('#award-popup');
				$edit.modal('show');
				
				var id = $(this).data('uid');
				var award = awards.findItem(id);
				
				$("#award-popup h3[id=popupTitle]").html('修改奖励');
				$("#award-popup input[name=id]").val(award.id);
				$("#award-popup input[name=name]").val(award.name);
				$("#award-popup input[name=grantBody]").val(award.grantBody);
				$("#award-popup select[name=awardName]").val(award.awardName);
				$("#award-popup select[name=type]").val(award.type);
				$("#award-popup select[name=grade]").val(award.grade);
				$("#award-popup select[name=year]").val(award.year);
				$("#award-popup select[name=departId]").val(award.departId);
				$("#award-popup select[name=companyOrder]").val(award.companyOrder);
				
				//渲染附件
				$.get('<dhome:url value="/institution/${domain}/backend/award/attchments/"/>'+award.id).done(function(data){
					if(data.success){
						attachment.data=data.data;
					}else{
						alert(data.desc);
					}
					attachment.render();
				});
			});
			
			
			//删除专利
			$('.removePatent').live('click',function(){
				if(confirm("专利删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/patent/${member.umtId }/'+id+'"/>').done(function(data){
						if(data.success){
							patents.remove(id);
							if(patents.data.length == 0){
								$('#patentNoData').show();
								$('#patent table[id=table]').hide();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editPatent').live('click',function(){
				var $edit=$('#patent-popup');
				$edit.modal('show');
				
				var id = $(this).data('uid');
				var patent = patents.findItem(id);
				
				$("#patent-popup h3[id=popupTitle]").html('修改专利');
				$("#patent-popup input[name=id]").val(patent.id);
				$("#patent-popup input[name=name]").val(patent.name);
				$("#patent-popup select[name=type]").val(patent.type);
				$("#patent-popup select[name=grade]").val(patent.grade);
				$("#patent-popup select[name=year]").val(patent.year);
				$("#patent-popup select[name=personalOrder]").val(patent.personalOrder);
				$("#patent-popup select[name=companyOrder]").val(patent.companyOrder);
				
				//$(".success").removeClass("success");
				//$(".error").empty();
			});
			
			
			//删除软件著作权
			$('.removeCopyright').live('click',function(){
				if(confirm("软件著作权删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/copyright/${member.umtId }/'+id+'"/>').done(function(data){
						if(data.success){
							copyrights.remove(id);
							if(copyrights.data.length == 0){
								$('#crNoData').show();
								$('#copyright table[id=table]').hide();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editCopyright').live('click',function(){
				var $edit=$('#copyright-popup');
				$edit.modal('show');
				
				var id = $(this).data('uid');
				var copyright = copyrights.findItem(id);
				
				$("#copyright-popup h3[id=popupTitle]").html('修改软件著作权');
				$("#copyright-popup input[name=id]").val(copyright.id);
				$("#copyright-popup input[name=name]").val(copyright.name);
				$("#copyright-popup select[name=type]").val(copyright.type);
				$("#copyright-popup select[name=grade]").val(copyright.grade);
				$("#copyright-popup select[name=year]").val(copyright.year);
				$("#copyright-popup select[name=personalOrder]").val(copyright.personalOrder);
				$("#copyright-popup select[name=companyOrder]").val(copyright.companyOrder);
				
				//$(".success").removeClass("success");
				//$(".error").empty();
			});
			
			
			//删除学术任职
			$('.removeAcademic').live('click',function(){
				if(confirm("学术任职删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/academic/${member.umtId }/'+id+'"/>').done(function(data){
						if(data.success){
							academics.remove(id);
							if(academics.data.length == 0){
								$('#acmNoData').show();
								$('#academic table[id=table]').hide();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editAcademic').live('click',function(){
				var $edit=$('#academic-popup');
				$edit.modal('show');
				
				var id = $(this).data('uid');
				var academic = academics.findItem(id);
				
				$("#academic-popup h3[id=popupTitle]").html('修改学术任职');
				$("#academic-popup input[name=id]").val(academic.id);
				$("#academic-popup select[name=name]").val(academic.name);
				$("#academic-popup select[name=position]").val(academic.position);
				$("#academic-popup select[name=startYear]").val(academic.startYear);
				$("#academic-popup select[name=startMonth]").val(academic.startMonth);
				if(academic.endYear == 3000){
					$("#academic-popup select[id=endYear]").attr("disabled", true);
					$("#academic-popup select[id=endMonth]").attr("disabled", true);
					$("#academic-popup input[type=checkbox][name=now]").attr("checked", true);
				}else{
					$("#academic-popup select[name=endYear]").val(academic.endYear);
					$("#academic-popup select[name=endMonth]").val(academic.endMonth);
				}
				
				//$(".success").removeClass("success");
				//$(".error").empty();
			});
			
			//删除期刊任职
			$('.removePeriodical').live('click',function(){
				if(confirm("期刊任职删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/periodical/${member.umtId }/'+id+'"/>').done(function(data){
						if(data.success){
							periodicals.remove(id);
							if(periodicals.data.length == 0){
								$('#perNoData').show();
								$('#periodical table[id=table]').hide();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editPeriodical').live('click',function(){
				var $edit=$('#periodical-popup');
				$edit.modal('show');
				
				var id = $(this).data('uid');
				var periodical = periodicals.findItem(id);
				
				$("#periodical-popup h3[id=popupTitle]").html('修改期刊任职');
				$("#periodical-popup input[name=id]").val(periodical.id);
				$("#periodical-popup select[name=name]").val(periodical.name);
				$("#periodical-popup select[name=position]").val(periodical.position);
				
				if(periodical.endYear == 3000){
					$("#periodical-popup select[id=endYear]").attr("disabled", true);
					$("#periodical-popup select[id=endMonth]").attr("disabled", true);
					$("#periodical-popup input[type=checkbox][name=now]").attr("checked", true);
				}else{
					$("#periodical-popup select[name=endYear]").val(periodical.endYear);
					$("#periodical-popup select[name=endMonth]").val(periodical.endMonth);
				}
				
				$("#periodical-popup select[name=startYear]").val(periodical.startYear);
				$("#periodical-popup select[name=startMonth]").val(periodical.startMonth);
				
				
				//$(".success").removeClass("success");
				//$(".error").empty();
				
			
			});
			
			//删除课题
			$('.removeTopic').live('click',function(){
				if(confirm("课题删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/topic/${member.umtId }/'+id+'"/>').done(function(data){
						if(data.success){
							topics.remove(id);
							if(topics.data.length == 0){
								$('#topicNoData').show();
								$('#topic table[id=table]').hide();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editTopic').live('click',function(){
				var $edit=$('#topic-popup');
				$edit.modal('show');
				
				var id = $(this).data('uid');
				var topic = topics.findItem(id);
				
				$("#topic-popup h3[id=popupTitle]").html('修改课题');
				$("#topic-popup input[name=id]").val(topic.id);
				$("#topic-popup input[name=name]").val(topic.name);
				$("#topic-popup input[name=topic_no]").val(topic.topic_no);
				$("#topic-popup input[name=project_cost]").val(topic.project_cost);
				$("#topic-popup input[name=personal_cost]").val(topic.personal_cost);
				$("#topic-popup select[name=funds_from]").val(topic.funds_from);
				$("#topic-popup select[name=type]").val(topic.type);
				$("#topic-popup select[name=role]").val(topic.role);
				
				if(topic.end_year == 3000){
					$("#topic-popup select[id=endYear]").attr("disabled", true);
					$("#topic-popup select[id=endMonth]").attr("disabled", true);
					$("#topic-popup input[type=checkbox][name=now]").attr("checked", true);
				}else{
					$("#topic-popup select[name=end_year]").val(topic.end_year);
					$("#topic-popup select[name=end_month]").val(topic.end_month);	
				}
				
				$("#topic-popup select[name=start_year]").val(topic.start_year);
				$("#topic-popup select[name=start_month]").val(topic.start_month);
				
				//渲染作者
				$.get('<dhome:url value="/institution/${domain}/backend/topic/authors/"/>'+topic.id).done(function(data){
					if(data.data != null){
						topicAuthor.data=data.data;
					}else{
						topicAuthor.data =[];
					}
					topicAuthor.render();
				});
				
				//$(".success").removeClass("success");
				//$(".error").empty();
				
			});
			
			//删除人才培养
			$('.removeTraining').live('click',function(){
				if(confirm("人才培养删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/training/${member.umtId }/'+id+'"/>').done(function(data){
						if(data.success){
							trainings.remove(id);
							if(trainings.data.length == 0){
								$('#trainNoData').show();
								$('#training table[id=table]').hide();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			
			$('.editTraining').live('click',function(){
				var $edit=$('#training-popup');
				$edit.modal('show');
				var id = $(this).data('uid');
				var training = trainings.findItem(id);
				var beginTime = formatTime(training.enrollmentDate);
				var endTime = formatTime(training.graduationDate);
				selectDateTime('#training-popup',beginTime,endTime);
				
				$("#training-popup h3[id=popupTitle]").html('修改人才培养');
				$("#training-popup input[name=id]").val(training.id);
				$("#training-popup input[name=studentName]").val(training.studentName);
				$("#training-popup input[name=nationality]").val(training.nationality);
				$("#training-popup input[name=major]").val(training.major);
				$("#training-popup textarea[name=remark]").val(training.remark);
				$("#training-popup select[name=degree]").val(training.degree);
				
				//$(".success").removeClass("success");
				//$(".error").empty();
			});
			
			
			//删除论文
			$('.removePaper').live('click',function(){
				if(confirm("论文删除以后不可恢复，确认删除吗？")){
					var id = $(this).data('uid');
					$.get('<dhome:url value="/institution/${domain}/backend/member/delete/paper/${member.umtId }/'+id+'"/>').done(function(data){
						if(data.success){
							papers.remove(id);
							if(papers.data.length == 0){
								$('#paperNoData').show();
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			$('.editPaper').live('click',function(){
				var $edit=$('#paper-popup');
				$edit.modal('show');
				var id = $(this).data('uid');
				var paper = papers.findItem(id);
// 				alert($.judicePub(paper.publicationId));
				$("#paper-popup h3[id=popupTitle]").html('修改论文');
				$("#paper-popup input[name=id]").val(paper.id);
				$("#paper-popup input[name=title]").val(paper.title);
				$("#paper-popup input[name=doi]").val(paper.doi);
				$("#paper-popup input[name=issn]").val(paper.issn);
				$("#paper-popup input[name=series]").val(paper.series);
				$("#paper-popup input[name=publicationPage]").val(paper.publicationPage);
				$("#paper-popup input[name=authorAmount]").val(paper.authorAmount==null?0:paper.authorAmount);
				$("#paper-popup input[name=supportor]").val(paper.supportor);
				$("#paper-popup input[name=volumeNumber]").val(paper.volumeNumber);
				$("#paper-popup input[name=originalLink]").val(paper.originalLink);
				$("#paper-popup input[name=citation]").val(paper.citation == -1?'--':paper.citation);
				$("#paper-popup input[name=citationQueryTime]").val(paper.citationQueryTime);
				$("#paper-popup input[name=supportor]").val(paper.supportor);
// 				$("#paper-popup input[name=pubSearch]").val($.judicePub(paper.publicationId));
				$("#paper-popup select[name=publicationYear]").val(paper.publicationYear);
				$("#paper-popup select[name=publicationMonth]").val(paper.publicationMonth);
				$("#paper-popup select[name=disciplineOrientationId]").val(paper.disciplineOrientationId);
				$("#paper-popup textarea[name=summary]").val(paper.summary);
				$("#paper-popup select[name=annualAwardMarks]").val(paper.annualAwardMarks);
				$("#paper-popup select[name=departId]").val(paper.departId);
				$("#paper-popup select[name=performanceCalculationYear]").val(paper.performanceCalculationYear);
				
				//渲染刊物名称
				$.get('<dhome:url value="/institution/${domain}/backend/member/paper/pub/"/>'+paper.publicationId).done(function(data){
					if(data.data != null){
						$("#paper-popup input[name=pubSearch]").val(data.data.pubName);
					}
				});

				if(paper.authors != null){
					author.data = paper.authors;
				}else{
					author.data =[];
				}
				author.render();
				
				var keywords= paper.keywordDisplay;
				if(keywords!=''){
					$tokenObj.tokenInput("clear");
					var array=keywords.split(',');
					for(var i=0; i<array.length; i++){
						$tokenObj.tokenInput("add", {id:$.trim(array[i]), name: $.trim(array[i])});
					}
				}
				
				if(paper.clbId != 0){
					$('#paperfileNameSpan').html(paper.originalFileName);
					$('#paperfileName').val(paper.originalFileName)
					$('#paperclbId').val(paper.clbId);
					$('#paperfileRemove').show();
				}
				
				var sponsors= paper.sponsor;
				if(sponsors!=''){
					$tokenSponsor.tokenInput("clear");
					var array=sponsors.split(',');
					for(var i=0; i<array.length; i++){
						$tokenSponsor.tokenInput("add", {id:$.trim(array[i]), name: $.trim(array[i])});
					}
				}
				
				
				//$(".success").removeClass("success");
				//$(".error").empty();
			});
			
			
			//删除文件 
			$('#fileRemove').on('click',function(){
				$('#fileName').val('');
				$('#clbId').val('0');
				$('#fileNameSpan').empty();
				$(this).hide();
			})
			
				//删除文件 
			$('#paperfileRemove').on('click',function(){
				$('#paperfileName').val('');
				$('#paperclbId').val('0');
				$('#paperfileNameSpan').empty();
				$(this).hide();
			})
			
			//查看摘要
			$('.showSummary').live('click',function(){
				var paperId=$(this).data('paperId');
				$('#summary_'+paperId).toggle();
			});
			
			function formatTime(time){
                var time = new Date(time);
                if(time.getFullYear() == 3000)
                	return '至今';
                
                var ymdhis = "";
                ymdhis += time.getFullYear() + ".";
                var month = time.getMonth()+1;
                if(month > 9)
                    ymdhis += month;
                else
                	ymdhis += ("0" + month);
                return ymdhis;
			}
		});
	</script>
	
	<!-- 教育经历模板 -->
	<script type="text/x-jquery-tmpl" id="eduTemplate">
		<li>
			<span class="ins">{{= aliasInstitutionName}}</span>
			<span class="time">{{= $item.judiceTime(beginTime)}}-{{= $item.judiceTime(endTime)}}</span>
			<span class="degree">{{= degree}}<span class="degree2">({{= degree2}})</span></span><br />
			<span class="department">专业：{{= department}}</span>
			<span class="tutor">导师：{{= tutor}}</span><br />
			<span class="artical">毕业设计：{{= graduationProject}}</span>
			<span class="edit">
				<a class="editEducation btn btn-mini" data-uid="{{= id}}"><i class="icon icon-edit"></i> 编辑</a>
				<a class="removeEducation btn btn-mini" data-uid="{{= id}}"><i class="icon icon-trash"></i> 删除</a>
			</span>
		</li>
	</script>
	
	<!-- 工作经历模板 -->
	<script type="text/x-jquery-tmpl" id="workTemplate">
		<li>
			<span class="ins">{{= aliasInstitutionName}}</span>
			<span class="time">{{= $item.judiceTime(beginTime)}} - {{= $item.judiceTime(endTime)}}</span><br />
			<span class="position">职称：{{= position}}</span>
			<span class="time">职务：{{= department}}</span>
			<span class="edit">
				<a class="editWork btn btn-mini" data-uid="{{= id}}"><i class="icon icon-edit"></i> 编辑</a>
				<a class="removeWork btn btn-mini" data-uid="{{= id}}"><i class="icon icon-trash"></i> 删除</a>
			</span>
		</li>
	</script>
	
	<!-- 论著模板 -->
	<script type="text/x-jquery-tmpl" id="tratiseTemplate">
		<tr>
			<td >{{= name}}</td>
			<td >{{= $item.judicePublisher(publisher)}}</td>
			<td >{{= language}}</td>
			<td >{{= $item.judiceDept(departId)}}</td>
			<td >{{= companyOrder}}</td>
			<td >{{= year}}</td>
			<td >
				<a class="editTreatise" data-uid="{{= id}}"><i class="icon icon-edit"></i></a>
				<a class="removeTreatise" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
	
	
	
	<!-- 奖励模板 -->
	<script type="text/x-jquery-tmpl" id="awardTemplate">
		<tr>
			<td >{{= name}}</td>
			<td >{{= $item.judiceAwardName(awardName)}}</td>
			<td >{{= grantBody}}</td>
			<td >{{= $item.judiceType(type)}}</td>
			<td >{{= $item.judiceGrade(grade)}}</td>
			<td >{{= $item.judiceDept(departId)}}</td>
			<td >{{= companyOrder}}</td>
            <td >{{= year}}</td>
			<td >
				<a class="editAward" data-uid="{{= id}}"><i class="icon icon-edit"></i></a>
				<a class="removeAward" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
	
	
	<!-- 学术任职模板 -->
	<script type="text/x-jquery-tmpl" id="academicTemplate">
		<tr>
			<td >{{= $item.judiceName(name)}}</td>
			<td >{{= $item.judicePosition(position)}}</td>
			<td >{{= startYear}}.{{= startMonth}} - 
              {{if endYear == 3000}}
                   	至今
              {{else}}
                 {{= endYear}}.{{= endMonth}}
              {{/if}}
            </td>
			<td >
				<a class="editAcademic" data-uid="{{= id}}"><i class="icon icon-edit"></i></a>
				<a class="removeAcademic" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
	
	<!-- 期刊任职模板 -->
	<script type="text/x-jquery-tmpl" id="periodicalTemplate">
		<tr>
			<td >{{= $item.judiceName(name)}}</td>
			<td >{{= $item.judicePosition(position)}}</td>
			<td >{{= startYear}}.{{= startMonth}} - 
              {{if endYear == 3000}}
                   	至今
              {{else}}
                 {{= endYear}}.{{= endMonth}}
              {{/if}}
            </td>
			<td >
				<a class="editPeriodical" data-uid="{{= id}}"><i class="icon icon-edit"></i></a>
				<a class="removePeriodical" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
	
	
	<!-- 软件著作权模板 -->
	<script type="text/x-jquery-tmpl" id="copyrightTemplate">
		<tr>
			<td >{{= name}}</td>
			<td >{{= $item.judiceType(type)}}</td>
			<td >{{= $item.judiceGrade(grade)}}</td>
			<td >{{= personalOrder}}</td>
			<td >{{= companyOrder}}</td>
			<td >{{= year}}</td>
			<td >
				<a class="editCopyright" data-uid="{{= id}}"><i class="icon icon-edit"></i></a>
				<a class="removeCopyright" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
	
		<!-- 专利模板 -->
	<script type="text/x-jquery-tmpl" id="patentTemplate">
		<tr>
			<td >{{= name}}</td>
			<td >{{= $item.judiceType(type)}}</td>
			<td >{{= $item.judiceGrade(grade)}}</td>
			<td >{{= personalOrder}}</td>
			<td >{{= companyOrder}}</td>
			<td >{{= year}}</td>
			<td >
				<a class="editPatent" data-uid="{{= id}}"><i class="icon icon-edit"></i></a>
				<a class="removePatent" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
	
	
	<!-- 课题模板 -->
	<script type="text/x-jquery-tmpl" id="topicTemplate">
		<tr>
			<td >{{= name}}</td>
			<td >{{= start_year}}.{{= start_month}} - 
              {{if end_year == 3000}}
                                                   至今
              {{else}}
                 {{= end_year}}.{{= end_month}}
              {{/if}}
            </td>
			<td >{{= $item.judiceFund(funds_from)}}</td>
			<td >{{= $item.judiceType(type)}}</td>
			<td >{{= topic_no}}</td>
			<td >{{= $item.judiceRole(role)}}</td>judiceFund
			<td >{{= project_cost}}</td>
			<td >{{= personal_cost}}</td>
			<td >
				<a class="editTopic" data-uid="{{= id}}"><i class="icon icon-edit"></i></a>
				<a class="removeTopic" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
	
	
	<!-- 人才培养模板 -->
	<script type="text/x-jquery-tmpl" id="trainingTemplate">
		<tr>
            <td >{{= studentName}}</td>
            <td >{{= nationality}}</td>
            <td >{{= $item.judiceDegree(degree)}}</td>
			<td >{{= major}}</td>
			<td >{{= $item.judiceTime(enrollmentDate)}} - {{= $item.judiceTime(graduationDate)}}</td>
			<td >{{= remark}}</td>
			<td >
				<a class="editTraining" data-uid="{{= id}}"><i class="icon icon-edit"></i></a>
				<a class="removeTraining" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
	
	<!-- 论文模板 -->
	<script type="text/x-jquery-tmpl" id="paperTemplate">
		<li>
				           <span class="article">
								<span class="title">{{= title }}</span>
								<span class="detail">
									<!-- doi -->
                                    {{if doi}}
                                        doi:{{= doi}}&nbsp;&nbsp;   
                                    {{/if}}

									<!-- issn -->
                                    {{if issn != ''}}
                                        issn:{{= issn}}&nbsp;&nbsp; 
                                    {{/if}}
								</span>
                                <span class="detail">
									<!-- 刊物 -->
                                    {{if publicationId!=0}}
                                        {{= publication.pubName}}&nbsp;&nbsp; 
                                    {{/if}}

									<!-- 卷号 -->
                                    {{if volumeNumber != ''}}
                                        卷:{{= volumeNumber}}&nbsp;&nbsp;
                                    {{/if}}

									<!-- 期号 -->
                                    {{if series != ''}}
                                        期:{{= series}}&nbsp;&nbsp; 
                                    {{/if}}

									<!-- 开始页~结束页 -->
                                    {{if startPage != ''}}
                                        页:{{= startPage}}~{{= endPage}}&nbsp;&nbsp; 
                                    {{/if}}

									<!-- 发表时间 年.月 -->
									出版年:{{= publicationYear}}
                                    {{if publicationMonth != 0}}
                                        .{{= publicationMonth}}&nbsp;&nbsp; 
                                    {{/if}}
								</span>
                                    <div id="summary_{{= id }}" style="display:none"><c:out value="{{= summary }}"/></div>

									<!-- 原文链接 -->
                                    {{if originalLink != ''}}
                                        <a href="<c:out value="{{= originalLink}}"/>" target="_blank">原文链接</a>
                                    {{/if}}

									<!-- 原文下载 -->
                                    {{if clbId != 0}}
                                       <a href="<dhome:url value="/system/file?fileId={{= clbId }}"/>" target="_blank">原文下载</a>
                                    {{/if}}
									<!-- 摘要 -->
                                    {{if summary != ''}}
										<a data-paper-id="{{= id }}" class="showSummary">查看摘要</a>
									{{/if}}

                                    <a class="showSummary label label-info" href="<dhome:url value="/institution/${domain }/backend/paper/detail/{{= id}}?returnPage=1"/>">查看论文</a>
								
					</span>
            <span class="author">
				<ul>
               {{each(i,author) authors}}
                    <li >
						<a class="authorDetailPopover"  data-paper-id="{{= paperId}}" data-author-id="{{= author.id}}" >{{= author.authorName}}</a>
						<sup>[{{= author.subscriptIndex }}]</sup>
					</li>
              {{/each}}
				</ul>
            </span>
			<span class="quot" style="vertical-align:middle; text-align:center;">
				<a class="editPaper"  data-uid="{{= id}}"><i class="icon icon-edit"></i></a>
				<a class="removePaper" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</span>
		</li>
	</script>
	
	<!-- 作者表格模板 -->
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>
			<span class="order">第{{= order}}作者：</span>
			<span class="author">
				{{= authorName}}
				<span class="mail">({{= authorEmail}})</span>
				<span class="company">-[{{= authorCompany}}]</span>
			</span>
			{{if communicationAuthor}}<span class="comAuthor">通讯作者</span>{{/if}}
			{{if authorStudent}}<span class="student">学生在读</span>{{/if}}
			{{if authorTeacher}}<span class="supervisor">第一作者导师</span>{{/if}}
			<span>
				<a class="editAuthor" data-uid="{{= id}}"><i class="icon icon-edit"></i> </a>
				{{if id != ${author.id}}}<a class="removeAuthor" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>{{/if}}
			</span>
		</li>
	</script>
	
	<!-- 作者表格模板 -->
	<script type="text/x-jquery-tmpl" id="topicAuthorTemplate">
		<tr>
			<td class="order">{{if authorType=='admin'}}负责人：{{/if}}{{if authorType=='member'}}参与者：{{/if}}</td>
			<td class="author">
				{{= authorName}}
				<span class="mail">({{= authorEmail}})</span>
				<span class="company">-[{{= authorCompany}}]</span>
			</td>
			<td>
				<a class="editTopicAuthor" data-uid="{{= id}}"><i class="icon icon-edit"></i> </a>
				{{if id != ${author.id}}}<a class="removeTopicAuthor" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>{{/if}}
			</td>
		</tr>
	</script>
	<!-- 奖励附件模板 -->
	 <script type="text/x-jquery-tmpl" id="clbTemplate">
        <tr>
           <td>
            {{= fileName}}
            {{if fileType == 1}}
               <a href="<dhome:url value="/system/img?imgId={{= clbId}}"/>" class="previewBtn" title="{{= fileName}}" data-uid="{{= clbId}}">预览</a>
            {{/if}}

            {{if clbId != 0 && '${op}'=='update'}}
                 <a href="<dhome:url value="/system/file?fileId={{= clbId }}"/>" target="_blank">附件下载</a>
            {{/if}}
		    <a class="removeAwardAttachment" data-uid="{{= clbId}}">删除</a>
          </td>
        </tr>
    </script>
</html>