<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<html lang="en">
	<head>
		<title>机构成员</title>
		<meta name="description" content="dHome" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/css/dp.css'/>"/>
	</head>

	<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
		<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
		
		<div class="container">
			<jsp:include page="../leftMenu.jsp"></jsp:include>
			<div class="ins_backend_rightContent">
				<ul class="nav nav-tabs">
					<jsp:include page="../../commonMemberBackend.jsp"><jsp:param name="activeItem" value="job" /> </jsp:include>
<!-- 				     <li class="search"> -->
<!-- 				    	<form class="bs-docs-example form-search"> -->
<%-- 				            <input id='memberKeyword' type="text" class="input-medium search-query" placeholder="请输入员工姓名" value="<c:out value="${condition.keyword }"/>"> --%>
<!-- 				            <button id="searchMemberBtn" type="button" class="btn">搜索</button> -->
<!-- 				        </form> -->
<!-- 				    </li> -->
 			 </ul> 
				<h4 class="detail"  style="margin-top:20px;">高级专业技术岗位应聘申请表</h4>
				
				
			<form id="editForm" class="form-horizontal">
				 <div class="control-group">
	       			<label class="control-label">请选择年份：</label>
	       			<div class="controls">
	         			<select id="year" name="year" class="register-xsmall">
						</select>&nbsp;年&nbsp;&nbsp;
	       			</div>
	       		</div>
				<div class="control-group">
	       			<label class="control-label">数据来源时间段： </label>
	       			<div class="controls">
	         			<input readonly="readonly" type="text" id="startTime" name="startTime"  value="" />&nbsp;  至  &nbsp;
	         			<input readonly="readonly" type="text" id="endTime" name="endTime"  value="" /><br>
	         			<span id="startTime_error_place" class="error"></span>
	         			<span id="endTime_error_place" class="error"></span> 
	         			<p class="hint">数据来源时间段请选择近五年时间段。</p>
	       			</div>
	       			
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">截止时间： </label>
	       			<div class="controls">
	         			<input readonly="readonly" type="text" id="deadline" name="deadline"  value="" />
	         			<span id="deadline_error_place" class="error"></span>
	       			</div>
	       		</div>

	       		
	       		<div class="control-group">
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls">
	         			<input type="button" id="saveBtn" value="提交" class="btn btn-primary"/>
	         			<a class="btn btn-link" href="/institution/${domain}/backend/job/list/1">取消</a>
	       			</div>
	       		</div>
			</form>
				
			</div>
		</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	
<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
	<script>
		$(document).ready(function(){
			
			$("#startTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});
			$("#endTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});
			$("#deadline").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});
			
			//设置开始年份
			(function(before,defaultYear){
				var year=new Date().getFullYear();
				for(var i=year-before-1;i<=year;i++){
					$('#year').append('<option  value="'+i+'">'+i+'</option')
				}
				if(!defaultYear){
					defaultYear=year;
				}
				$('#year').val(defaultYear);
			})(15,parseInt('${patent.year}')); 
			
			$('#saveBtn').on('click',function(){
// 				if($('input:radio[name="checkItem"]:checked').val()==null){
// 					alert("请选择学生！");
// 					return false;
// 				}
				if($('#editForm').valid()){
					var data=$('#editForm').serialize();
// 					var tid=$('input:radio[name="checkItem"]:checked').val();
// 					data +='&studentId='+tid;
					
					$.post('/institution/${domain}/backend/job/save',data).done(function(data){ 
						if(data.success){
							window.location.href='/institution/${domain}/backend/job/list/1';
						}else{
							alert(data.desc);
						}
					});
				}
			});
			$('#editForm').validate({
				submitHandler:function(form){
					form.submit();
				},
			    rules:{
			    	startTime:{required:true},
			    	endTime:{required:true},
			    	deadline:{required:true}
			    },
				messages:{
			    	startTime:{required:'请选择发放开始时间.'},
			    	endTime:{required:'请选择发放结束时间.'},
			    	deadline:{required:'请选择截止时间.'}
				},
			    errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			
			
			$('#memberMenu').addClass('active');
			
			
		
			
		}); 
	</script>
	
</html>