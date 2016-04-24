<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
<dhome:InitLanuage useBrowserLanguage="true"/>
<title>${titleUser.zhName }<fmt:message key="common.dhome"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.css"/>" />
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/css/tokenInput.css"/>"/>
<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/css/dp.css'/>"/>

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
			<h3><fmt:message key="institution.paper.title"/></h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="training" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonTraining.jsp"><jsp:param name="activeItem" value="grants" /> </jsp:include>
				<form id="editForm" class="form-horizontal grant">
				<h4 class="member_title">新增奖助学金</h4>
				<input type="hidden" name="id" id="id" value="${empty grants.id?0:grants.id }"/>
	       		
	       		<c:if test="${!empty trainings }">
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>学生姓名：</label>
	       			<div class="controls padding">
	       				<p class="students">
		         			<c:forEach items="${trainings }" var="data">
			         			<span class="check">
			         				<input value="${data.id }" type="radio" class="checkItem" name="checkItem"/><c:out value="${data.studentName }"/>
			         			</span>
		         			</c:forEach>
	         			<span id="studentId_error_place" class="error"></span>
	         			</p>
						<p class="hint">请点击要添加的学生。</p>
	       			</div>
	       		</div>
	       		
	       		  <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>课题号：</label>
	       			<div class="controls">
	         			<input type="text" name="topicNo" id="topicNo" value="<c:out value="${grants.topicNo }"/>" maxlength="250"/>
	         			<span id="topicNo_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>研究所奖学金（研究所支付）：</label>
	       			<div class="controls">
	         			<input type="text" name="scholarship1" id="scholarship1" value="<c:out value="${grants.scholarship1 }"/>" maxlength="10"/> 元/月
						<span id="scholarship1_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>研究所奖学金（课题支付）：</label>
	       			<div class="controls">
	         			<input type="text" name="scholarship2" id="scholarship2" value="<c:out value="${grants.scholarship2 }"/>" maxlength="10"/> 元/月
						<span id="scholarship2_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>助研酬金（课题支付）：</label>
	       			<div class="controls">
	         			<input type="text" name="assistantFee" id="assistantFee" value="<c:out value="${grants.assistantFee }"/>" maxlength="10"/> 元/月
						<span id="assistantFee_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">发放开始时间： </label>
	       			<div class="controls">
	         			<input readonly="readonly" type="text" id="startTime" name="startTime"  value="${grants.startTime }" />
	         			<span id="startTime_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">发放结束时间： </label>
	       			<div class="controls">
	         			<input readonly="readonly" type="text" id="endTime" name="endTime"  value="${grants.endTime }" />
	         			<span id="endTime_error_place" class="error"></span>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls">
	         			<input type="button" id="saveBtn" value="提交" class="btn btn-primary"/>
	         			<a class="btn btn-link" href="<dhome:url value='/people/${domain}/admin/grants/list/1'/>">取消</a>
	       			</div>
	       		</div>
				</c:if>
				<c:if test="${empty trainings }">
					<p class="hint">您还没有学生！</p>
				</c:if>
			</form>
				
			</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<jsp:include page="../../commonfooter.jsp"></jsp:include>

</body>
<jsp:include page="../../commonheader.jsp"></jsp:include>
<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
<script type="text/javascript">
$(function(){
	$("#startTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});
	$("#endTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});
	
	$('#saveBtn').on('click',function(){
		if($('input:radio[name="checkItem"]:checked').val()==null){
			alert("请选择学生！");
			return false;
		}
		if($('#editForm').valid()){
			var data=$('#editForm').serialize();
			var tid=$('input:radio[name="checkItem"]:checked').val();
			data +='&studentId='+tid;
			
			$.post('/people/${domain}/admin/grants/save',data).done(function(data){ 
				if(data.success){
					window.location.href='/people/${domain}/admin/grants/list/1';
				}else{
					alert(data.desc);
				}
			});
		}
	});
	
	$.validator.addMethod("money",function(value, element){
		var reg= /^[0-9]+(.[0-9]{1,2})?$/;
		return reg.test(value); 
	},"请输入正确的金额.");
	$('#editForm').validate({
		submitHandler:function(form){
			form.submit();
		},
	    rules:{
	    	studentId:{ required:true },
	    	topicNo:{ required:true },
	    	scholarship1:{ money:true, },
	    	scholarship2:{ money:true, },
	    	assistantFee:{ money:true, },
	    	startTime:{required:true},
	    	endTime:{required:true},
	    },
		messages:{
			studentId:{required: "请选择一个学生."},
			topicNo:{required: "请输入课题号."},
	    	startTime:{required:'请选择发放开始时间.'},
	    	endTime:{required:'请选择发放结束时间.'}
		},
	    errorPlacement: function(error, element){
			 var sub="_error_place";
			 var errorPlaceId="#"+$(element).attr("name")+sub;
			 	$(errorPlaceId).html("");
			 	error.appendTo($(errorPlaceId));
		}
	});

	$("#studentName").autocomplete('/people/${domain}/admin/grants/search/student',{
  		width:400,
		parse:function(data){
				return $.map(data, function(item) {
					return {
						data : item,
						result : '',
						value:item.studentName
					};
				});
		},
		formatItem:function(row, i, max) {
  				return  row.studentName+ "- [" + row.major+"]";
			},
		formatMatch:function(row, i, max) {
  				return row.studentName + " " + row.major;
			},
		formatResult: function(row) {
  				return row.studentName; 
			}
	}).result(function(event,item){
		$("#studentName").val(item.studentName);
		$("#studentId").val(item.id);
	});
	
});
</script>
</html>