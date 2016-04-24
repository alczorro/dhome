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
				<ul class="nav nav-tabs">
					<li>&nbsp;&nbsp;&nbsp;</li>
					<li class="active">
					    <a href="<dhome:url value="/institution/${domain }/backend/setting/1"/>">基本设置</a>
					</li>
<!-- 				   	<li> -->
<!-- 					    <a href="#">报表定制</a> -->
<!-- 				    </li> -->
				   	<li>
					    <a href="<dhome:url value="/institution/${domain }/backend/setting/deptlist"/>">部门简称设置</a>
				    </li>
				</ul>
				<div class="subNav">
				    <ul class="nav nav-pills">
					    <c:forEach items="${settingList }" var="item">
							<li <c:if test="${ item.id  == id}">class="active"</c:if>>
							    <a href="<dhome:url value="/institution/${domain }/backend/setting/${item.id }"/>"><strong>${item.title }</strong></a>
							</li>
						</c:forEach>
					</ul>
				</div>
				<c:forEach items="${optionList }" var="data">
				<h4 class="member_title">${data.title }</h3>
				<ul class="listShow setting">
					<c:forEach items="${data.valList }" var="val">
						<li>
							<span class="oper">
								<a class="label label-mini label-info edit-js" data-id="${val.id }" href="javascript:;"><i class="icon icon-edit icon-white"></i>&nbsp;修改</a> 
								<a class="label label-mini del-js" href="javascript:;" data-id="${val.id }"><i class="icon icon-trash icon-white"></i>&nbsp;删除</a></span>
							<span class="title">${val.val }</span>
						</li> 
					</c:forEach>
				</ul>
				<p class="p_add"><input type="button" class="btn btn-success edit-js" data-id="0"  data-optionid="${data.id}" value="添加${data.title}" /></p>
				</c:forEach>
			</div>
		</div>
		
	<div id="settingPopup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>添加</h3>
        </div>
        	<form id="settingForm" class="form-horizontal" method="post">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">名称：</label>
          			<div class="controls">
            			<input maxlength="50" type="text" id="val" name="val" value='' class="register-xlarge"/>
          				<span id="val_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">顺序：</label>
          			<div class="controls">
            			<input maxlength="4" type="text" id="rank" name="rank" value='' class="register-xlarge"/>
          				<span id="rank_error_place" class="error"></span>
          			</div>
        		</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="optionId" name="optionId" value="0" />
				<input type="hidden" name="id" value="0" />
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="settingSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
<script>
	$(document).ready(function(){
		$('#settingMenu').addClass('active');
		$("#settingForm").keypress(function(e) {
			  if (e.which == 13) {
			    return false;
			  }
			});

		
		$.validator.addMethod("positiveInt",function(value, element){
				var reg= /^[0-9]*[1-9][0-9]*$/;
				return this.optional(element)|| (reg.test(value)); 
			},"请输入一个正整数.");
		$('#settingForm').validate({
			 submitHandler :function(form){
				 form.submit();
			 },
			 rules: {
				 val:{required:true},
			 	 rank:{ positiveInt:true }
			 },
			 messages:{
				 val:{required:'名称不允许为空'}
			 },
			 errorPlacement: function(error, element){
				 var sub="_error_place";
				 var errorPlaceId="#"+$(element).attr("name")+sub;
				 	$(errorPlaceId).html("");
				 	error.appendTo($(errorPlaceId));
			}
		});
		
		$('.del-js').live('click',function(){
			var id = $(this).data("id");
			if(!confirm("确认要删除吗？")){
				return;
			}
			$.ajax({
			   url: '<dhome:url value="/institution/${domain }/backend/setting/delete"/>',
			   data: {"id":id},
			   dataType: "JSON",
			   success: function(resp){
				   if(resp.success){
					   window.location.reload();
				   }
			   }
			});
		});
		
		$('.edit-js').live('click',function(){
			var id = $(this).data("id");
			$("span.error").html("");
			if(id!=0){
				$('#settingPopup').find('h3').html('修改');
			}else{
				$('#settingPopup').find('h3').html($(this).val());
				$('#optionId').val($(this).data("optionid"));
			}
			$.ajax({
			   url: '<dhome:url value="/institution/${domain }/backend/setting/edit"/>',
			   data: {"id":id},
			   dataType: "JSON",
			   success: function(resp){
				   if(resp.success){
					   var data = resp.data;
					   $('#settingForm').find('input[name=id]').val(data.id);
					   $('#settingForm').find('input[name=val]').val(data.val);
				   	   if(data.rank>0){$('#settingForm').find('input[name=rank]').val(data.rank);}
					   $('#settingPopup').modal('show');
				   }
			   }
			});
		});
		$("#settingSaveBtn").click(function(){
			if(!$('#settingForm').valid()){
				return;
			}
			var params = $('#settingForm').serialize();
			$.ajax({
			   type: "POST",
			   url: '<dhome:url value="/institution/${domain }/backend/setting/submit"/>',
			   data: params,
			   dataType: "JSON",
			   success: function(resp){
				   if(resp.success){
					   window.location.reload();
				   }
			   }
			});
		});
// 		$("#settingSaveBtn").enter(function(){
// 			if(!$('#settingForm').valid()){
// 				return;
// 			}
// 			var params = $('#settingForm').serialize();
// 			$.ajax({
// 			   type: "POST",
// 			   url: '<dhome:url value="/institution/${domain }/backend/setting/submit"/>',
// 			   data: params,
// 			   dataType: "JSON",
// 			   success: function(resp){
// 				   if(resp.success){
// 					   window.location.reload();
// 				   }
// 			   }
// 			});
// 		});
	})
</script>
</html>