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
					<li>
					    <a href="<dhome:url value="/institution/${domain }/backend/setting/1"/>">基本设置</a>
					</li>
<!-- 				   	<li> -->
<!-- 					    <a href="#">报表定制</a> -->
<!-- 				    </li> -->
				   	<li class="active">
					    <a href="#">部门设置</a>
				    </li>
				</ul>
				
				<span id="sortInitial" style="margin:10px 0 0 20px; display:inline-block;">
				   <a class="label label-info edit-js" >按首字母排序</a> 
				</span>	
				
				
				<ul class="listShow setting">
					<li class="title">
						<span class="employee">部门全称</span>
						<span class="pro-title">部门简称</span>
						<span class="pro-title">顺序</span>
						<span>修改</span>
					</li>
					
					<c:forEach items="${dept }" var="val">
						<li>
							<span class="employee">${val.name }</span>
							<span class="pro-title">${val.shortName }</span>
							<span class="pro-title">${val.listRank }</span>
							<span>
								<a class="label label-mini label-info edit-js" href="javascript:editDept('${val.id }','${val.name }','${val.shortName }','${val.listRank }');"><i class="icon icon-edit icon-white"></i>&nbsp;修改</a> 
							</span>	
						</li> 
					</c:forEach>
				</ul>
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
         			<label class="control-label">部门名称：</label>
          			<div class="controls">
            			<span id="name" class="register-xlarge"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">部门简称：</label>
          			<div class="controls">
            			<input id="shortName" name="shortName" value='' class="register-xlarge"/>
          				<span id="shortName_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">顺序：</label>
          			<div class="controls">
            			<input id="listRank" name="listRank" value='' class="register-xlarge"/>
          				<span id="listRank_error_place" class="error"></span>
          			</div>
        		</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="optionId" name="optionId" value="0" />
				<input type="hidden" name="id" value="0" />
				<button type="button" id="settingSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
	        </div>
	        </form>
	</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
<script>
    function editDept(id,name,shortName,listRank){
		$('#settingPopup').find('h3').html('修改');
		
	    $('#settingForm').find('input[name=id]').val(id);
	    $('#settingForm').find('span[id=name]').html(name);
   	    $('#settingForm').find('input[name=shortName]').val(shortName);
   	    $('#settingForm').find('input[name=listRank]').val(listRank);
	    $('#settingPopup').modal('show');
    }
    
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
				 shortName:{ required:true }
			 },
			 messages:{
				 shortName:{required:'简称不允许为空'}
			 },
			 errorPlacement: function(error, element){
				 var sub="_error_place";
				 var errorPlaceId="#"+$(element).attr("name")+sub;
				 	$(errorPlaceId).html("");
				 	error.appendTo($(errorPlaceId));
			}
		});
		
		
		$("#sortInitial").click(function(){
			$.ajax({
			   type: "GET",
			   url: '<dhome:url value="/institution/${domain }/backend/setting/sort/initial"/>',
			   dataType: "JSON",
			   success: function(resp){
				   if(resp.success){
					   window.location.reload();
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
			   url: '<dhome:url value="/institution/${domain }/backend/setting/dept/edit"/>',
			   data: params,
			   dataType: "JSON",
			   success: function(resp){
				   if(resp.success){
					   window.location.reload();
				   }
			   }
			});
		});
	})
</script>
</html>