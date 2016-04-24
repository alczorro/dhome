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
			<jsp:include page="../../commonMemberBackend.jsp"><jsp:param name="activeItem" value="modifyMember" /> </jsp:include>
			 </ul>
				 
			<form id="editForm" class="form-horizontal">
				<input type="hidden" name="id" id="id" value="${empty member.id?0:member.id }"/>
	       		
 		       <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>姓名：</label>
	       			<div class="controls">
	         			<input type="text" name="trueName" id="trueName" value="<c:out value="${member.trueName }"/>" maxlength="250"/>
						<span id="trueName_error_place" class="error"></span>
	       			</div>
	       		</div> 
	       		
 		       <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>邮箱：</label>
	       			<div class="controls">
	         			<input type="text" name="cstnetId" id="cstnetId" value="<c:out value="${member.cstnetId }"/>" maxlength="250"/>
						<span id="cstnetId_error_place" class="error"></span>
	       			</div>
	       		</div> 
	       		
 		       	<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>部门：</label>
	       			<div class="controls">
	         			<select id="departId" name="departId">
	         			   <c:forEach  items="${depts}" var="dept">
	         			      <option value="${dept.id}">${dept.name}</option>
	         			    </c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
 		       	<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>性别：</label>
	       			<div class="controls">
	         			<select id="sex" name="sex">
	         			    <option value="male">男</option>
	         			    <option value="female">女</option>
						</select>
	       			</div>
	       		</div>
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>职称：</label>
	       			<div class="controls">
	         			<input type="text" name="title" id="title" value="<c:out value="${member.title }"/>" maxlength="250"/>
						<span id="title_error_place" class="error"></span>
	       			</div>
	       		</div> 
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>办公室地址：</label>
	       			<div class="controls">
	         			<input type="text" name="officeAddress" id="officeAddress" value="<c:out value="${member.title }"/>" maxlength="250"/>
						<span id="officeAddress_error_place" class="error"></span>
	       			</div>
	       		</div> 
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>办公室电话：</label>
	       			<div class="controls">
	         			<input type="text" name="officeTelephone" id="officeTelephone" value="<c:out value="${member.title }"/>" maxlength="250"/>
						<span id="officeTelephone_error_place" class="error"></span>
	       			</div>
	       		</div> 
	       		
	       		 <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>手机：</label>
	       			<div class="controls">
	         			<input type="text" name="mobilePhone" id="mobilePhone" value="<c:out value="${member.title }"/>" maxlength="250"/>
						<span id="mobilePhone_error_place" class="error"></span>
	       			</div>
	       		</div> 
	       		
	       		
	       		<div class="control-group">
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls">
	         			<input type="button" id="memberSaveBtn" value="提交" class="btn btn-primary"/>
	         			<a class="btn btn-link" href="<dhome:url value="/institution/${domain }/backend/member/list/1"/>">取消</a>
	       			</div>
	       		</div>
				
			</form>
		</div>	
	</div>

	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
	<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script>
		$(document).ready(function(){
			
			
			
		   //提交论文
			$('#memberSaveBtn').on('click',function(){
				if($('#editForm').valid()){
					var data=$('#editForm').serialize();
					$.post('<dhome:url value="/institution/${domain}/backend/member/submit"/>',data).done(function(data){ 
						if(data.success){
							window.location.href='<dhome:url value="/institution/${domain}/backend/member/list/1"/>'
						}else{
							alert(data.desc);
						}
					});
				}
			});
		   
			
			//期刊任职保存验证
			/* $('#editForm').validate({
				submitHandler:function(form){
					form.submit();
				},
			    rules:{
			    	memberName:{
			    		required:true,
				        maxlength:250
			    	},
			    	position:{
			    		required:true,
			    		maxlength:250
			    	}
			    },
				
				messages:{
					memberName:{
						required:'期刊名称不能为空',
						maxlength:'期刊名过长'
					},
					position:{
						required:'职位不能为空',
						maxlength:'职位名称过长'
					}
				},
			
			    errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});   */
			
			//左栏置为选中
			$('#memberMenu').addClass('active');
			
		});
	</script>
	
</html>