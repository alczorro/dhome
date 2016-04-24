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
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.css"/>" />
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/css/tokenInput.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/sample-css/page.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/css/dp.css'/>"/>
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
	<div class="container">
		<jsp:include page="../leftMenu.jsp"></jsp:include>
		<div class="ins_backend_rightContent">
			<ul class="nav nav-tabs">
				<jsp:include page="../../commonTrainingBackend.jsp"><jsp:param name="activeItem" value="modifyTraining" /> </jsp:include>
			</ul>
			<form id="editForm" class="form-horizontal" method="post" action="<dhome:url value="/institution/backend/training/creat"/>">
				<input type="hidden" name="id" id="id" value="${empty training.id?0:training.id }"/>
				
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
	         				<c:forEach items="${degrees.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==training.degree }">selected="selected"</c:if>>${data.value.val}</option>
	         			 	</c:forEach>
					</select>
					<span id="degree_error_place" class="error"></span>
	       			</div>
	       		</div>
				
	       		  <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>入学时间：</label>
	       			<div class="controls">
	         			<select id="enrollmentYear" name="enrollmentYear" class="register-xsmall">
						</select>&nbsp;年&nbsp;&nbsp;
						
						<select id="enrollmentMonth" name="enrollmentMonth" class="register-xsmall">
<!-- 						   <option value="0">--</option> -->
					    </select>&nbsp;月
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>毕业时间：</label>
	       			<div class="controls">
	         			<select id="graduationYear" name="graduationYear" class="register-xsmall">
						</select>&nbsp;年&nbsp;&nbsp;
	         			<select id="graduationMonth" name="graduationMonth" class="register-xsmall">
<!-- 							<option value="0">--</option> -->
						</select>&nbsp;月&nbsp;&nbsp;
	         			 <span class="check"><input type="checkbox" name="now" id="checkThis"/> 至今</span>
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
	       			<label class="control-label"><span class="red">*</span>班级：</label>
	       			<div class="controls">
	         			<input type="text" name="className" id="className" value="<c:out value="${training.className }"/>" />
						<span id="className_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">导师：</label>
	       			<div class="controls">
	         			<input type="text" name="authorSearch" class="autoCompleteSearch register-xlarge" id="authorSearch"/>
						<p class="hint">请输入导师的姓名、邮箱或者单位，按回车确认添加。</p>
						<span id="userNameSpan" class="alert" style="padding:3px; display:none;" data-uid="${training.umtId }">
	         				<c:if test="${!empty memberMap[training.umtId].trueName }">
	         				${memberMap[training.umtId].trueName }
	         				<c:if test="${!empty memberMap[training.umtId].cstnetId }">(${memberMap[training.umtId].cstnetId })
								</c:if>
	         				</c:if>
<%-- 	         				<a id="removeUser" ><i class="icon icon-trash"></i></a> --%>
	         			</span>
	         			<span id="removeUserSpan"><c:if test="${!empty memberMap[training.umtId].trueName }"><i class="icon icon-trash"></i></c:if></span>
						<span class="error" id="authorError"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>导师部门：</label>
	       			<div class="controls">
	         			<select id="departId" name="departId">
	         				<option value="0" <c:if test="${training.departId==0 }">selected="selected"</c:if>>--</option>
	         				<c:forEach items="${deptMap.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==training.departId }">selected="selected"</c:if>>${data.value.shortName}</option>
	         			 	</c:forEach>
						</select>
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">备注：</label>
	       			<div class="controls">
	         			<textarea rows="6" cols="20" id="remark" name="remark" class="register-xlarge">${training.remark }</textarea>
						<span id="remark_error_place" class="error"></span>
	       			</div>
	       		</div>
				
	       		<div class="control-group">
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls">
	       				
	         			<input type="button" id="trainingSaveBtn" value="提交" class="btn btn-primary"/>
	         			<a class="btn btn-link" href="<dhome:url value="/institution/${domain }/backend/training/list/1"/>">取消</a>
	       			</div>
	       		</div>
			</form>
		</div>	
	
	</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
	<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script>
		$(document).ready(function(){
			
			//初始化时间控件
			$("#citationQueryTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});

// 			//token控件初始化，关键字
// 			var $tokenObj = $("#keywordDisplay").tokenInput("<dhome:url value='/institution/${domain}/backend/training/search/keyword'/>", {
// 				theme:"facebook",
// 				hintText: "请输入关键字",
// 				searchingText: "正在查询...",
// 				noResultsText: "未查询到结果",
// 				preventDuplicates: true,
// 				queryParam:"q"
// 			});
			//查询用户
			$("#authorSearch").autocomplete('<dhome:url value="/institution/${domain}/backend/academic/search/user"/>',
			            {
					  		width:400,
							parse:function(data){
									return $.map(data, function(item) {
										return {
											data : item,
											result : '',
											value:item.trueName
										};
									});
							},
							formatItem:function(row, i, max) {
			    				return  row.trueName+"("+row.cstnetId+")";
			 				},
							formatMatch:function(row, i, max) {
			    				return row.trueName + " " + row.cstnetId;
			 				},
							formatResult: function(row) {
			    				return row.trueName; 
			 				}
						}).result(function(event,item){
							$("#userNameSpan").html(item.trueName + "(" +item.cstnetId + ")").show();
							$("#userNameSpan").attr("data-uid",item.umtId);
							$('#removeUserSpan').html('<a><i class="icon icon-trash"></i></a>'); 
							$("#authorError").html("");
// 							var umtid=$('#userNameSpan').data("uid").toString();
//  							alert(umtid);
						});
			
			$('#removeUserSpan').click(function(){
				if(confirm("确认删除吗？")){
					$("#userNameSpan").html("").hide();
					$('#removeUserSpan').html("");
					$("#userNameSpan").attr("data-uid","");
				}
			});
			
			/* 检查学生的入学毕业时间 */
			function checkTime(popup){
				var beginTimeYear = $(popup+" select[id=enrollmentYear]").val();
				var beginTimeMonth = $(popup+" select[id=enrollmentMonth]").val();
				var endTimeYear = $(popup+" select[id=graduationYear]").val();
				var endTimeMonth = $(popup+" select[id=graduationMonth]").val();
				
				if($(popup+" input[type=checkbox][name=now]").attr("checked")){
					endTimeYear = '3000'; 
					endTimeMonth = '1';
				}
				$(popup+" input[name=beginTime]").val(beginTimeYear+"-"+beginTimeMonth+"-01");
				if(endTimeYear<beginTimeYear || (endTimeYear == beginTimeYear && endTimeMonth<beginTimeMonth )){
					var $parent = $(popup+" select[id=graduationYear]").parents("div.control-group");
					$parent.find("span.error").html("").append("毕业时间必须在入学时间之后");
					return false;
				}
				
				return true;
			}
			
			//添加课题Dialog，前台验证
			$('#editForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 studentName:{
						 required:true
						 },
					 nationality:{
						 required:true
						 },
				 	
				 	degree:{
				 		required:true
				 	},
				 	tutor:{
				 		required:true
				 	},
				 	major:{
				 		required:true
				 	},
				 	className:{
				 		required:true
				 	}
// 				 	project_cost:{
// 				 		required:true,
// 				 		min:0,
// 				 		digits:true,
// 				 		max:99999999
// 				 	},
// 				 	personal_cost:{
// 				 		required:true,
// 				 		min:0,
// 				 		digits:true,
// 				 		max:99999999
// 				 	}
				 },
				 messages:{
					 studentName:{
						 required:'姓名不允许为空'
						 },
						 nationality:{
					 		required:'国籍不允许为空'
					 	},
					 	degree:{
							 required:'学位不允许为空'
						 },
						 tutor:{
							 required:'导师不允许为空'
						 },
						 major:{
							 required:'专业不允许为空'
						 },
						 className:{
							 required:'班级不允许为空'
						 }
// 					 	project_cost:{
// 					 		required:'项目经费不允许为空',
// 					 		min:'超出允许值',
// 					 		digits:'必须为正整数',
// 					 		max:'超出允许值'
// 					 	},
// 					 	personal_cost:{
// 					 		required:'个人经费不允许为空',
// 					 		min:'超出允许值',
// 					 		digits:'必须为正整数',
// 					 		max:'超出允许值'
// 					 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			
			function parseDate(datestr) {  
			    var parts = datestr.split('-');  
			    return new Date(parts[0], parts[1] - 1, parts[2]);  
			}  
// 			//设置开始年份
// 			(function(before,defaultYear){
// 				var year=new Date().getFullYear();
// 				var date=parseDate('${training.enrollmentDate}').getFullYear();
// 				alert(date);
// 				for(var i=year-before-1;i<=year;i++){
// 					$('#enrollmentYear').append('<option  value="'+i+'">'+i+'</option>');
// 				}
// 				if(!defaultYear){
// 					defaultYear=year;
// 				}
// 				$('#enrollmentYear').val(defaultYear);
// 			})(15,parseInt('${training.enrollmentDate}.getFullYear()'));
			
			(function(loop,enrollmentYear,graduationYear){
				var year=new Date().getFullYear();
				for(var i=year-loop+2;i<=year;i++){
					$('#enrollmentYear').append('<option value="'+i+'">'+i+'</option')
					$('#graduationYear').append('<option value="'+i+'">'+i+'</option')
				}
				if(!enrollmentYear){
					enrollmentYear=year;
				}
				
				if(!graduationYear){
					graduationYear = year;
				}
				
				$('#enrollmentYear').val(enrollmentYear);
				$('#graduationYear').val(graduationYear);
			})(15,parseInt('${training.enrollmentDate}.getFullYear()'),parseInt('${training.graduationDate}.getFullYear()')); 
			
			//发表/结束月份
			(function(loop,enrollmentMonth,graduationMonth){
// 				var date=parseDate('${training.enrollmentDate}').getMonth();
//  				alert(new Date().getFullYear()+1);
// 				alert(date.getMonth());
				for(var i=1;i<loop+1;i++){
					$('#enrollmentMonth').append('<option value="'+i+'">'+i+'</option');
					$('#graduationMonth').append('<option value="'+i+'">'+i+'</option');
				}
				
				if(!enrollmentMonth){
					enrollmentMonth= 1;
				}
				
				if(!graduationMonth){
					graduationMonth = 1;
				}
				
				$('#enrollmentMonth').val(enrollmentMonth);
				$('#graduationMonth').val(graduationMonth);
			})(12,parseInt(parseDate('${training.enrollmentDate}').getMonth())+1,parseInt(parseDate('${training.graduationDate}').getMonth())+1); 
			//左栏置为选中
			$('#trainingMenu').addClass('active');
			
			//提交课题
			$('#trainingSaveBtn').on('click',function(){
// 				alert($("#userNameSpan").data("uid").toString());
				if($('#editForm').valid()){
					if(!checkTime('#editForm')){
						return false;
					}
					var data=$('#editForm').serialize();
					if($('#checkThis').attr("checked")){
						data +='&graduationYear=' + 3000;
						data +='&graduationMonth='+ 01;
					}
					var umtId=$("#userNameSpan").data("uid").toString();
// 					if(umtId==""||umtId==null){
// 						$("#authorError").html("").append("导师不允许为空");
// 						return;
// 					}else{
						data +='&umtId='+umtId;
// 					}
					
					$.ajax({
	 					  type: 'POST',
	 					  url: '<dhome:url value="/institution/${domain}/backend/training/submit"/>',
	 					  data: data,
	 					  success: function(data){window.location.href='<dhome:url value="/institution/${domain}/backend/training/list/${empty returnPage?1:returnPage}"/>'},
	 					  error:function(){alert(data.desc)},
	 					  dataType: "JSON"
	 					});
// 					$.post('<dhome:url value="/institution/${domain}/backend/training/submit"/>',data).done(function(data){ 
// 						if(data.success){
// 							alert();
// 							window.location.href='<dhome:url value="/institution/${domain}/backend/training/list/${returnPage}"/>'
// 						}
//						else{
// 							alert(data.desc);
// 						}
// 					});
				}
			});
			$('#checkThis').click(function(){
				if($('#checkThis').attr("checked")){
					$("#graduationYear").attr("disabled","disabled");  
					$("#graduationMonth").attr("disabled","disabled");  
				}else{
					$("#graduationYear").removeAttr("disabled");  
					$("#graduationMonth").removeAttr("disabled");  
				}
			});
			
			//如果为更新操作
			if('${op}'=='update'){
				var year=parseDate('${training.graduationDate}').getFullYear();
				if(year == 3000){
					$("#graduationYear").attr("disabled", true);
					$("#graduationMonth").attr("disabled", true);
					$("#checkThis").attr("checked", true);
				}
			}
		});
	</script>
</html>