<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<title><fmt:message key="institute.common.scholarEvent"/>-${institution.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.css"/>" />
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/css/tokenInput.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/sample-css/page.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/css/dp.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/bootstrap/css/bootstrap-datetimepicker.min.css'/>"/>
	<link rel="stylesheet" href="<dhome:url value="/resources/css/lightbox.css"/>" type="text/css" media="screen" />
	<style>
		ul.token-input-list-facebook {width:363px;}
		.btn[disabled]{
			opacity: .2;
		}
		ul#paperTable {margin-top:30px; list-style:none; margin:0 0 0 80px; padding:0 5px; background:#eef; border-radius:4px;}
		ul#paperTable li {border-bottom:1px dotted #ccc; padding:7px 0;}
		ul#paperTable li > span {font-size:13px; display:inline-block; margin-right:5px;}
		ul#paperTable li span.doi {width:280px; font-weight:bold; color:#08a;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}
		ul#paperTable li span.name {width:100px; font-weight:bold; color:#08a;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}
		ul#paperTable li span.title {width:250px; font-weight:bold; color:#08a; overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}
	</style>

</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${titleUser.zhName }<fmt:message key="common.dhome"/>
			<jsp:include page="../../showAudit.jsp"/>
		</div>
		<jsp:include page="../../commonMenu.jsp">
			<jsp:param name="activeItem" value="achievement" />
		</jsp:include>
	</div> 
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title">
				<h3>论文管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="paper" /> </jsp:include>
			<div class="span9 left-b">
			<div class="ins_backend_rightContent">
				<jsp:include page="../../commonPaper.jsp"><jsp:param name="activeItem" value="modifyPaper" /> </jsp:include>
				<form id="editForm" class="form-horizontal">
					<input type="hidden" name="id" id="id" value="${empty paper.id?0:paper.id }"/>
					<div class="control-group">
		       			<label class="control-label"><span class="red">*</span>论文标题：</label>
		       			<div class="controls">
		         			<input type="text" name="title" value="<c:out value="${paper.title }"/>" id="title" class="register-xlarge"/>
		         			<span class="add-span" style="display:none;margin-left: -5px;">同名论文已经存在，显示如下(如果以下论文都不是您想要添加的？您可以继续补充论文的DOI来完成新论文的添加)：</span>
							<span id="title_error_place" class="error"></span>
<!-- 							<a id="btn-update" style="display:none;">编辑</a> -->
							
		       			</div>
		       			<ul id="paperTable" style="margin-top:20px;"></ul>
<!-- 		       			<span class="add-span" style="display:none;">。</span> -->
		       		</div>
					<div class="control-group">
		       			<label class="control-label">doi：</label>
		       			<div class="controls">
		         			<input type="text" name="doi" value="<c:out value="${paper.doi }"/>" id="doi" maxlength="250"/>
							<span id="dio_error_place" class="error"></span>
							<span id="doi-span" style="display:none;color:red;"></span>
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
		         				<option value="0" <c:if test="${paper.departId==0 }">selected="selected"</c:if>>--</option>
		         				<c:forEach items="${deptMap.entrySet() }" var="data">
		         			         <option value="${data.key }" <c:if test="${data.key==paper.departId }">selected="selected"</c:if>>${data.value.shortName}</option>
		         			 	</c:forEach>
							</select>
		       			</div>
		       		</div>
					<div class="control-group">
		       			<label class="control-label"><span class="red">*</span>刊物名称：</label>
		       			<div class="controls">
		       				<input type="text" value="${pubsMap[paper.publicationId].pubName}" data-pid="${paper.publicationId==null?0:paper.publicationId  }" name="pubSearch" id="pubSearch" class="register-xlarge" onfocus="javascript:this.select();"/>
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
		       				<input readonly type="text" id="publicationYear" value="<c:out value="${paper.publicationYear }"/>" name="publicationYear" />
<!-- 		         			<select id="publicationYear" name="publicationYear"> -->
<!-- 							</select> -->
		       			</div>
		       		</div>
		       		<div class="control-group">
		       			<label class="control-label">发表月份：</label>
		       			<div class="controls">
		         			<select id="publicationMonth" name="publicationMonth">
							<option value="0">--</option>
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
		         			<input type="text" value="<c:out value="${paper.publicationPage }"/>" name=publicationPage id="publicationPage" />
							<span id="publicationPage_error_place" class="error"></span>
		       			</div>
		       		</div>
<!-- 					<div class="control-group"> -->
<!-- 		       			<label class="control-label"><span class="red">*</span>终止页：</label> -->
<!-- 		       			<div class="controls"> -->
<%-- 		         			<input type="text" value="<c:out value="${paper.endPage }"/>" name="endPage" id="endPage"/> --%>
<!-- 							<span id="endPage_error_place" class="error"></span> -->
<!-- 		       			</div> -->
<!-- 		       		</div> -->
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
	       			</div>
	       		</div>
		       		<hr>
		       		<div class="control-group">
		       			<label class="control-label">资助单位：</label>
		       			<div class="controls">
		         			<input type="text" value="${paper.sponsor }" name="sponsor" id="sponsor" maxlength="250"/>
							<p class="notFind">没有找到您要选择的资助单位？点击这里<a id="addSupportor">添加资助单位</a></p>
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
		         			<textarea rows="5" cols="10" id="summary" name="summary" class="register-xlarge"><c:out value="${paper.summary }"/></textarea>
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
							<div id="fileUploader">
								<div class="qq-uploader">
									<div class="qq-upload-button">
										<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
									</div>
									<ul class="qq-upload-list fileList"></ul>
								</div>
							</div>
							<span id="fileNameSpan"><c:out value="${paper.originalFileName }"/></span>
							<span id="fileUploadProcess"></span>
							<input type="hidden" name="clbId" id="clbId" value="${empty paper?0:paper.clbId }"/>
							<input type="hidden" name="originalFileName" id="fileName" value="<c:out value="${paper.originalFileName }"/>"/>
							<a id="fileRemove" style="display:${empty paper||paper.clbId==0?'none':'inline' }">删除</a>
		       			</div>
		       		</div>
					<div class="control-group">
		       			<label class="control-label">论文引用次数：</label>
		       			<div class="controls padding">
		         			<input readonly="readonly" type="text" id="citation" name="citation" value="<c:out value="${(paper.citation=='-1'||empty paper.citation)?'--':paper.citation }"/>" />
<!-- 						<span id="citation_error_place" class="error"></span> -->
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
		       		<div class="control-group">
		       			<label class="control-label">&nbsp;</label>
		       			<div class="controls">
		         			<input type="button" id="paperSaveBtn" value="提交" class="btn btn-primary"/>
		         			<a class="btn btn-link" onclick="javascript:history.go(-2);">取消</a>
		       			</div>
		       		</div>
					
				</form>
			</div>	
			</div>
	</div>
	<!-- 添加刊物 -->
	<div id="add-publication-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>新增刊物</h3>
        </div>
       	<form id="addPubForm" class="form-horizontal" method="post" action="<dhome:url value="/people/admin/member/update/${member.umtId }"/>">
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
	
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
	<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/third-party/bootstrap/js/bootstrap-datetimepicker.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/bootstrap/js/bootstrap.min.js"/>" type="text/javascript"></script>
	
	
	<script>
		$(document).ready(function(){
			//初始化时间控件
			$("#citationQueryTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});
			 $('#publicationYear').datetimepicker({
			       autoclose: true,startView:4,minView:4,todayHighlight:true,
					 viewMode: 'years',
					format: 'yyyy'
			    });
			//token控件初始化，关键字
			var $tokenObj = $("#keywordDisplay").tokenInput("<dhome:url value='/people/${domain}/admin/paper/search/keyword'/>", {
				theme:"facebook",
				hintText: "请输入关键字",
				searchingText: "正在查询...",
				noResultsText: "未查询到结果",
				preventDuplicates: true,
				queryParam:"q"
			});
// 			//token控件初始化，刊物
// 			var $tokenSponsor = $("#publicationId").tokenInput("<dhome:url value='/institution/${domain}/backend/paper/search/publication'/>", {
// 				theme:"facebook",
// 				hintText: "请输入资助单位",
// 				searchingText: "正在查询...",
// 				noResultsText: "未查询到结果",
// 				preventDuplicates: true,
// 				queryParam:"q"
// 			});
			
			
			//作者队列
			var author={
					//数据
					data:[],
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
							$('#authorTable').hide();
							return;
						}else{
							this.data.sort(function(a,b){
								if(a.order==0){
									return 1;
								}
								if(b.order==0){
									return -1;
								}
								if(a.order>b.order){
									return 1;
								}else{
									return -1;
								}
							});
							$('#authorTable').show();
							$('#authorTable').html($('#authorTemplate').tmpl(this.data,{
								judiceBool:function(bool){
									return bool?'true':'false';
								}
							})); 
						}
					}
			};
			
			//删除作者
			$('.removeAuthor').live('click',function(){
				if(confirm("确定移除该作者？")){
					var email=$(this).data('uid');
					author.remove(email);
				}
			});
			//查询刊物
			$("#pubSearch").autocomplete('<dhome:url value="/people/${domain}/admin/paper/search/publication"/>',
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
// 							if(!success){
// 								alert('添加失败');
// 							}
						});
			//查询作者
			$("#authorSearch").autocomplete('<dhome:url value="/people/${domain}/admin/paper/search/author"/>',
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
							var success=author.append(item);
							if(!success){
								alert('请勿重复添加');
							}
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
				$.post('<dhome:url value="/people/${domain}/admin/paper/author/create"/>',$('#addAuthorForm').serialize()).done(function(data){
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
			//提交论文
			$('#paperSaveBtn').on('click',function(){
				if($('#editForm').valid()){
					var data=$('#editForm').serialize();
					var pid=$('#pubSearch').data("pid");
// 					if(pid==null||pid==""){
// 						alert("请重新添加刊物名称");
// 						return false;
// 					}
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
					$.post('<dhome:url value="/people/${domain}/admin/paper/submit"/>',data).done(function(data){ 
						if(data.success){
// 							window.location.href='<dhome:url value="/people/${domain}/admin/paper/list/${empty returnPage?1:returnPage}"/>'
							if('${op}'=='update'){
 								location.replace(window.name);
								
							}else{
								window.location.href='<dhome:url value="/people/${domain}/admin/paper/list/${empty returnPage?1:returnPage}"/>'
							}
						}else{
							alert(data.desc);
						}
					});
				}
			});
			//保存刊物按钮
			$('#pubSaveBtn').on('click',function(){
				if($('#addPubForm').valid()){
					$.post('<dhome:url value="/people/${domain}/admin/publication/create"/>',$('#addPubForm').serialize()).done(function(data){
						if(data.success){
// 							$('#publication').append('<option value="'+data.data.id+'">'+data.data.pubName+'</option>').val(data.data.id);
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
				 		min:0,
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
			$('#addAuthorForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 authorName:{
						 required:true
						 },
// 					authorEmail:{
// 						 required:true,
// 						 email:true
// 						 },
				 	
// 				 	authorCompany:{
// 				 		required:true
// 				 	},
				 	order:{
				 		required:true,
				 		min:0,
				 		digits:true,
				 		max:99999999
				 	}
				 },
				 messages:{
					 authorName:{
						 required:'姓名不允许为空'
						 },
// 						 authorEmail:{
// 							 required:'邮箱不允许为空',
// 							 email:'非法的邮箱格式'
// 						 },
// 					 	authorCompany:{
// 					 		required:'单位不允许为空'
// 					 	},
					 	order:{
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
			//提交论文验证
			$('#editForm').validate({
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
				 	publicationPage:{
				 		required:true,
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
					publicationPage:{
						required:'页码不允许为空',
					},
// 				 	citation:{
// 				 		min:'论文引用次数超出范围',
// 				 		digits:'请输入整数',
// 				 		max:'论文引用次数超出范围'
// 				 	},
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
			//上传文件
			new qq.FileUploader({
				element : document.getElementById('fileUploader'),
				action :'<dhome:url value="/people/${domain}/admin/paper/upload"/>',
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
						alert("系统维护中，暂不能添加附件");
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
			
			$("#title").on('blur',function(){
				if('${op}'!='update'){
				$("#paperTable").empty();
				$.post("<dhome:url value='/people/${domain}/admin/paper/getPaperByTitle?title="+$("#title").val()+"'/>").done(function(data){
					if(data.success){
						window.name='<dhome:url value="/people/${domain}/admin/paper/list/1"/>';
						for(var i=0;i<data.data.length;i++){
							var uid=${titleUser.id};
							
							if(data.data[i].doi==null){
								if(uid==data.data[i].creator&&data.data[i].status!=1){
									$("#paperTable").append('<li><span class="title" title="'+data.data[i].title+'">'+data.data[i].title+'</span><span class="doi">doi:空</span><span class="name" title="'+data.data[i].creatorName+'">'+data.data[i].creatorName+'</span><span><a class="paper-edit" data-pid="'+data.data[i].id+'"><i class="icon icon-edit"></i> </a></span></li>');
								}else{
									$("#paperTable").append('<li><span class="title" title="'+data.data[i].title+'">'+data.data[i].title+'</span><span class="doi">doi:空</span><span class="name" title="'+data.data[i].creatorName+'">'+data.data[i].creatorName+'</span></li>');
								}
								
							}else{
								if(uid==data.data[i].creator&&data.data[i].status!=1){
									$("#paperTable").append('<li><span class="title" title="'+data.data[i].title+'">'+data.data[i].title+'</span><span class="doi" title="'+data.data[i].doi+'">doi:'+data.data[i].doi+'</span><span class="name" title="'+data.data[i].creatorName+'">'+data.data[i].creatorName+'</span><span><a class="paper-edit" data-pid="'+data.data[i].id+'"><i class="icon icon-edit"></i> </a></span></li>');
								}else{
									$("#paperTable").append('<li><span class="title" title="'+data.data[i].title+'">'+data.data[i].title+'</span><span class="doi" title="'+data.data[i].doi+'">doi:'+data.data[i].doi+'</span><span class="name" title="'+data.data[i].creatorName+'">'+data.data[i].creatorName+'</span></li>');
								}
								
							}
							
						}
						$("#paperSaveBtn").attr("disabled",true);
						$(".add-span").show();
						$("#doi-span").html("DOI不能为空，请填写DOI");
						$("#doi-span").show();
					}else{
						$("#paperSaveBtn").attr("disabled",false);
						$(".add-span").hide();
					}
					
				});
				}
			});
			$("#doi").on('blur',function(){
				if('${op}'!='update'){
					if($("#doi").val()==null||$("#doi").val()==""){
						if($("#paperTable").html()!=""){
							$("#paperSaveBtn").attr("disabled",true);
// 							$(".add-span").show();
							$("#doi-span").html("DOI不能为空，请填写DOI");
							$("#doi-span").show();
						}else{
							$("#paperSaveBtn").attr("disabled",false);
							$(".add-span").hide();
							$("#doi-span").hide();
						}
					}else{
						$.post("<dhome:url value='/people/${domain}/admin/paper/getPaperByDoi?doi="+$("#doi").val()+"'/>").done(function(data){
							if(data.success){
								
								$("#paperSaveBtn").attr("disabled",true);
// 								$(".add-span").show();
								$("#doi-span").html("DOI已存在，请修改");
								$("#doi-span").show();
							}else{
								$("#paperSaveBtn").attr("disabled",false);
								$(".add-span").hide();
								$("#doi-span").hide();
							}
						});
					}
				
				}
			});
			$('.paper-edit').live('click',function(){
					var pid=$(this).data('pid');
					window.location.href="<dhome:url value='/people/${domain}/admin/paper/update/"+pid+"?returnPage=1'/>";
			});
			
			
			
			//设置发表年份
			(function(before,defaultYear){
				var year=new Date().getFullYear();
				for(var i=year-before-1;i<=year;i++){
					$('#publicationYear').append('<option  value="'+i+'">'+i+'</option')
				}
				if(!defaultYear){
					defaultYear=year;
				}
				$('#publicationYear').val(defaultYear);
			})(15,parseInt('${paper.publicationYear}'));
			//发表月份
			(function(loop,defaultMonth){
				for(var i=1;i<loop+1;i++){
					$('#publicationMonth').append('<option value="'+i+'">'+i+'</option');
				}
				if(!defaultMonth){
					defaultMonth=1;
				}
				$('#publicationMonth').val(defaultMonth);
			})(12,parseInt('${paper.publicationMonth}'));
			
			//论文简历引用标识
			(function(loop,defaultYear){
				var year=new Date().getFullYear();
				for(var i=year-loop+2;i<=year+1;i++){
					$('#annualAwardMarks').append('<option value="'+i+'">'+i+'</option')
				}
				if(!defaultYear){
					defaultYear=year;
				}
				$('#annualAwardMarks').val(defaultYear);
			})(5,parseInt('${paper.annualAwardMarks}')); 
			//绩效计算年份
			(function(loop,defaultYear){
				var year=new Date().getFullYear();
				for(var i=year-loop+2;i<=year+1;i++){
					$('#performanceCalculationYear').append('<option value="'+i+'">'+i+'</option')
				}
				if(!defaultYear){
					defaultYear=year;
				}
				$('#performanceCalculationYear').val(defaultYear);
			})(5,parseInt('${paper.performanceCalculationYear}'));
			
			//左栏置为选中
			$('#paperMenu').addClass('active');
			//如果为更新操作
			if('${op}'=='update'){
				//渲染作者
				$.get('<dhome:url value="/people/${domain}/admin/paper/authors/${paper.id}"/>').done(function(data){
					if(data.success){
						author.data=data.data;
						if($('#authorAmount').val()==0){
							$('#authorAmount').val(author.data.length);
						}
					}else{
						alert(data.desc);
					}
					author.render();
				});
				//渲染关键字
				var keywords='${paper.keywordDisplay}'
				if(keywords!=''){
					$tokenObj.tokenInput("clear");
					var array=keywords.split(',');
					for(var i=0; i<array.length; i++){
						$tokenObj.tokenInput("add", {id:$.trim(array[i]), name: $.trim(array[i])});
					}
				}
			}else{
				author.render();
			}
		});
	</script>
	<!-- 作者表格模板 -->
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>
			{{if order==0}}<span class="order">通讯作者：</span>{{/if}}
			{{if order!=0}}<span class="order">第{{= order}}作者：</span>{{/if}}
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
				<a class="removeAuthor" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</span>
		</li>
	</script>
	
</html>