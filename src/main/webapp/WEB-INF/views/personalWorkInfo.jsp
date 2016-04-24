<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%@ page import="java.util.*" %>
<dhome:InitLanuage useBrowserLanguage="true"/>
<% 
	request.setAttribute("now", new Date().getYear()+1900);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><fmt:message key='personalWorkInfo.title'/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${titleUser}<fmt:message key='common.index.title'/></div>
		<jsp:include page="./commonMenu.jsp">
			<jsp:param name="activeItem" value="personInfo" />
		</jsp:include>
	</div>
	
	<div class="container canedit">
		<div class="row-fluid mini-layout">
			<div class="config-title" style="margin-top:0.5em;"><h3><fmt:message key='admin.common.link.personInfo'/></h3></div>
			<div class="span11">
				<ul class="nav nav-tabs">
					<li><li>&nbsp;&nbsp;&nbsp;</li></li>
						<li>
						    <a href="<dhome:url value='/people/${domain}/admin/personal'/>">
						    	<strong><fmt:message key='personal.common.basicInfo'/></strong>
						    </a>
					    </li>
					    <li>
					    <a href="http://passport.escience.cn/user/password.do?act=showChangePassword" target="_blank">
				        <strong><fmt:message key='personal.common.updatePsw'/></strong></a>
				    </li>
					    <li class="active">
						    <a href="<dhome:url value='/people/${domain}/admin/personal/work'/>">
						    	<strong><fmt:message key='personal.common.work'/></strong>
						    </a>
					    </li>
					    <li>
						    <a href="<dhome:url value='/people/${domain}/admin/personal/education'/>">
						    	<strong><fmt:message key='personal.common.education'/></strong>
						    </a>
					    </li>
					    <li>
						    <a href="<dhome:url value='/people/${domain}/admin/personal/photo'/>">
						    	<strong><fmt:message key='personal.common.photo'/></strong>
						    </a>
					    </li>
					    <li>
						    <a href="<dhome:url value='/people/${domain}/admin/personal/favorite'/>">
						    	<strong><fmt:message key="personalSocialMediaInfo.title"/></strong>
						    </a>
					    </li>
				    </ul>
				<div class="edit-mode no-ptop abs-left">
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
						    			<a data-toggle="modal" class="float-right popup-edit"><i class="icon-edit"></i><span class="icon-link"><fmt:message key='personal.common.edit'/></span></a>
					    				<a data-toggle="modal" class="float-right popup-remove"><i class="icon-trash"></i><span class="icon-link"><fmt:message key='personal.common.delete'/></span></a>
						    		</li>
						    	</c:forEach>
						    </c:when>
						    <c:otherwise>
						    	<span><fmt:message key='personalWorkInfo.nowork'/></span>
						    </c:otherwise>
						</c:choose>
						</ul>
						<div class="d-center">
							<a id="popup-add" data-toggle="modal" class="btn btn-primary"><fmt:message key='personal.common.add'/></a>
						</div>
				</div>
		    </div>
		</div>
		
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
	<div id="remove-work-popup" tabindex="-1" class="modal hide fade">
		<div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">×</button>
	        <h3><fmt:message key='personalWorkInfo.deletework'/></h3>
        </div>
        <form method="post" class="form-horizontal no-bmargin" name="remove-workinfo" action="<dhome:url value='/people/${domain}/admin/personal/edit/work'/>">
			<input type="hidden" name="func" value="delete">
			<input type="hidden" name="id" value="">
			<fieldset>
				<div class="modal-body">
					<span><fmt:message key='personalWorkInfo.confirmDelete'/></span>
					<ul class="x-list work no-border"></ul>
				</div>
	        	<div class="modal-footer">
					<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
					<button type="submit" class="btn btn-primary"><fmt:message key='common.confirm.delete'/></button>
		        </div>
	        </fieldset>
        </form>
	</div>
	<div tabindex="-1" id="edit-work-popup" class="modal hide fade">
			<div class="modal-header">
	           <button type="button" class="close" data-dismiss="modal">×</button>
	           <h3><fmt:message key='personal.common.work'/></h3>
         	</div>
         	
         	<form method="post"  id="editWork" class="form-horizontal no-bmargin" name="edit-workinfo" action="<dhome:url value='/people/${domain}/admin/personal/edit/work'/>">
			<fieldset>
				<div class="modal-body">
					<input type="hidden" name="func" value="save" /> 
					<input type="hidden" name="id" value="" /> 
					<div class="control-group">
	         			<label class="control-label"><fmt:message key='personalWorkInfo.institution'/></label>
	          			<div class="controls">
	            			<input maxlength="255" id="institutionZhName" type="text" name="institutionZhName" value="" />
	            			<span class="help-inline gray-text message"><fmt:message key='personalWorkInfo.warning.institution'/></span>
	          			</div>
	        		</div>
					<div class="control-group">
	         			<label class="control-label"><fmt:message key='personalWorkInfo.department'/></label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="department" value="" />
	            			<span class="help-inline gray-text message"><fmt:message key='personalWorkInfo.warning.department'/></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key='personalWorkInfo.position'/></label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="position" value="" />
	            			<span class="help-inline gray-text message"><fmt:message key='personalWorkInfo.warning.position'/></span>
	            			<div class="x-top"><input type="checkbox" name="salutation" class="d-sbottom"><fmt:message key='personalWorkInfo.warning.salutation'/></div>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<input type="hidden" name="beginTime" value="">
	         			<label class="control-label"><fmt:message key='personalWorkInfo.beginTime'/></label>
	          			<div class="controls">
	            			<select name="beginTimeYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>
	            			<select name="beginTimeMonth" class="i-small">
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
	            			</select>
	            			<span class="help-inline gray-text message"><fmt:message key='personalWorkInfo.warning.beginTime'/></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<input type="hidden" name="endTime" value="">
	         			<label class="control-label"><fmt:message key='personalWorkInfo.endTime'/></label>
	          			<div class="controls">
	            			<select name="endTimeYear" class="i-small">
	            				<option value="">- <fmt:message key='personalWorkInfo.year'/> -</option>
	            				<c:forEach var="tmpyear" begin="${now-100}" end="${now}" step="1">
	            					<option value="${now-tmpyear+(now-100)}">${now-tmpyear+(now-100)}</option>
	            				</c:forEach>
	            			</select>
	            			<select name="endTimeMonth" class="i-small">
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
	            			</select>
	            			<span class="help-inline gray-text message"><fmt:message key='personalWorkInfo.warning.endTime'/></span>
	            			<div class="x-top"><input type="checkbox" name="now" class="d-sbottom"><fmt:message key='personalWorkInfo.untilnow'/></div>
	          			</div>
	        		</div>
	        	</div>
	        	<div class="modal-footer">
					<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
					<button type="submit" id="save" class="btn btn-primary"><fmt:message key='common.save'/></button>
		        </div>
	        </fieldset>
        </form>
	</div>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	$("#institutionZhName").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : "<dhome:url value='/system/regist'/>",
				type : "POST",
				data : "func=stepTwoLoadInstituion&prefixName=" + request.term,
				success : function(data) {
					response($.map(data, function(item) {
						return {
							label : item.zhName,
							value : item.zhName
							
						}
					}));
				}
			});
		},
		minLength : 2,
		open : function() {
		},
		close : function() {
		}
	});

	$("form[name=edit-workinfo]").validate({
		submitHandler:function(form){
			$(form).find("#save").attr("disabled",true).attr("value","<fmt:message key='personalWorkInfo.submitting'/>");
			form.submit();
		},
		rules:{
			institutionZhName:{required:true, maxlength:255},
			//department:{required:true, maxlength:255},
			position:{required:true, maxlength:255},
			beginTimeYear:{required:true},
			beginTimeMonth:{required:true},
			endTimeYear:{required:true},
			endTimeMonth:{required:true}
		},
		messages:{
			institutionZhName:{
				required:"<fmt:message key='personalWorkInfo.warning.empty.institution'/>",
				maxlength:"<fmt:message key='common.inputTooLong'/>"
			},
			//department:{
			//	required:"<fmt:message key='personalWorkInfo.warning.empty.department'/>",
			//	maxlength:"<fmt:message key='common.inputTooLong'/>"
			//},
			position:{
				required:"<fmt:message key='personalWorkInfo.warning.empty.position'/>",
				maxlength:"<fmt:message key='common.inputTooLong'/>"
			},
			beginTimeYear:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
			beginTimeMonth:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
			endTimeYear:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"},
			endTimeMonth:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"}
		},
		success:function(label){
			var $parent = label.parents("div.control-group");
			if(label.attr("for")=="beginTimeYear" && $("select[name=beginTimeMonth]").val() == ''){
				$parent.removeClass("success");
				$parent.addClass("error");
				$parent.find("span.message").html("").append("<fmt:message key='personalWorkInfo.warning.empty.beginTimeMonth'/>");
			}else if(label.attr("for")=="beginTimeMonth" && $("select[name=beginTimeYear]").val() == ''){
				$parent.removeClass("success");
				$parent.addClass("error");
				$parent.find("span.message").html("").append("<fmt:message key='personalWorkInfo.warning.empty.beginTimeYear'/>");
			}else if(label.attr("disabled")){
				if(label.attr("for")=="endTimeYear" && $("select[name=endTimeMonth]").val() == ''){
					$parent.removeClass("success");
					$parent.addClass("error");
					$parent.find("span.message").html("").append("<fmt:message key='personalWorkInfo.warning.empty.endTimeMonth'/>");
				}else if(label.attr("for")=="endTimeMonth" && $("select[name=endTimeYear]").val() == ''){
					$parent.removeClass("success");
					$parent.addClass("error");
					$parent.find("span.message").html("").append("<fmt:message key='personalWorkInfo.warning.empty.endTimeYear'/>");
				}
			}else if(label.attr("for")=="endTimeYear" && $("select[name=endTimeMonth]").val() == ''){
				$parent.removeClass("success");
				$parent.addClass("error");
				$parent.find("span.message").html("").append("<fmt:message key='personalWorkInfo.warning.empty.endTimeMonth'/>");
				$("select[name=endTimeMonth]").click();
			}else{
				$parent.removeClass("error");
				$parent.addClass("success");
				label.html("<font color='green'>√</font>");
			}
		},
		errorPlacement:function(error, element){
			var $parent = element.parents("div.control-group");
			$parent.removeClass("success");
			$parent.addClass("error");
			$parent.find("span.message").html("").append(error);
		},
		onclick:false
	});
	
	$("a.aliasLink").hover(function(){
		var title = $.trim($(this).parent().attr("title"));
		if(title!="" && title!="null"){
			$(this).parent().tooltip('toggle');
		}
	});
	
	$("input[type=checkbox][name=now]").click(function(){
		var checked = $(this).attr("checked");
		if(checked || checked == 'checked'){
			$("select[name=endTimeYear]").val("0000").attr("disabled",true);
			$("select[name=endTimeMonth]").val("00").attr("disabled",true);
			$("input[name=endTime]").val("");
		}else{
			$("select[name=endTimeYear]").val("").attr("disabled",false);
			$("select[name=endTimeMonth]").val("").attr("disabled",false);
		}
	});
	
	$("a#popup-add").live("click",function(){
		$("#edit-work-popup input[name=id]").val("");
		$("#edit-work-popup select[name=position]").val("");
		$("#edit-work-popup input[name=salutation]").attr("checked",false);
		$("#edit-work-popup input[name=department]").val("");
		$("#edit-work-popup input[name=institutionZhName]").val("");
		$("#edit-work-popup input[name=endTime]").val("");
		$("#edit-work-popup input[name=beginTime]").val("");
		$(".success").removeClass("success");
		$(".error").removeClass("error");
		$("#edit-work-popup").modal("show");
	});
	
	$("a.popup-edit").click(function(){
		var $parent = $(this).parent(); 
		$("#edit-work-popup input[name=id]").val($parent.attr("id"));
		$("#edit-work-popup input[name=position]").val($parent.children("span.position").text());
		$("#edit-work-popup input[name=department]").val($parent.children("span.department").text());
		$("#edit-work-popup input[name=institutionZhName]").val($.trim($parent.children("span.institutionZhName").text()));
		$("input[name=salutation]").attr("checked",true);
		if($parent.children("span.salutation").length>0){
			$("#edit-work-popup input[name=salutation]").attr("checked",true);
		}else{
			$("#edit-work-popup input[name=salutation]").attr("checked",false);
		}
		selectDateTime($parent);
		$(".success").removeClass("success");
		$(".error").removeClass("error");
		$("#edit-work-popup").modal("show");
	});
	
	$("a.popup-remove").click(function(){
		var workId = $(this).parent().attr("id");
		$("input[type=hidden][name=id]").val(workId);
		var $content = $(this).parent().clone();
		$content.children("a").remove();
		$("#remove-work-popup ul").html("").append($content);
		$("#remove-work-popup").modal("show");
	});
	
	function selectDateTime($parent){
		var beginTime = $parent.children("span.beginTime").text();
		var endTime = $parent.children("span.endTime").text();
		var itemB = beginTime.split(".");
		$("#edit-work-popup select[name=beginTimeYear] option[value="+itemB[0]+"]").attr("selected","selected");
		$("#edit-work-popup select[name=beginTimeMonth] option[value="+itemB[1]+"]").attr("selected","selected");
		if($.trim(endTime) != "<fmt:message key='personalWorkInfo.untilnow'/>"){
			$("#edit-work-popup select[name=endTimeYear]").attr("disabled", false);
			$("#edit-work-popup select[name=endTimeMonth]").attr("disabled", false);
			$("#edit-work-popup input[type=checkbox][name=now]").attr("checked", false);
			var itemE = endTime.split(".");
			$("#edit-work-popup select[name=endTimeYear] option[value="+itemE[0]+"]").attr("selected","selected");
			$("#edit-work-popup select[name=endTimeMonth] option[value="+itemE[1]+"]").attr("selected","selected");
		}else{
			$("#edit-work-popup select[name=endTimeYear]").attr("disabled", true);
			$("#edit-work-popup select[name=endTimeMonth]").attr("disabled", true);
			$("#edit-work-popup input[type=checkbox][name=now]").attr("checked", true);
		}
	}
	
	$("#save").click(function(){
		if(!$("input[type=checkbox][name=now]").attr("checked")){
			if(!checkDateTime()){
				var $parent = $("select[name=endTimeYear]").parents("div.control-group");
				$parent.removeClass("success");
				$parent.addClass("error");
				$parent.find("span.message").html("").append("<fmt:message key='personalWorkInfo.warning.endAfterBegin'/>");
				return false;
			}
			$("input[name=endTime]").val($("select[name=endTimeYear]").val()+"-"+$("select[name=endTimeMonth]").val()+"-01");
		}else{
			$("input[name=endTime]").val("");
		}
		$("input[name=beginTime]").val($("select[name=beginTimeYear]").val()+"-"+$("select[name=beginTimeMonth]").val()+"-01");
		
		//$("form[name=edit-workinfo]").submit();
	});
	
	$("a.cancel").live("click", function(){
		$("#edit-work-popup").modal("hide");
	});
	
	function checkDateTime(){
		var beginTimeYear = $("select[name=beginTimeYear]").val();
		var beginTimeMonth = $("select[name=beginTimeMonth]").val();
		var endTimeYear = $("select[name=endTimeYear]").val();
		var endTimeMonth = $("select[name=endTimeMonth]").val();
		if(endTimeYear<beginTimeYear){
			return false;
		}else if(endTimeYear == beginTimeYear){
			return (endTimeMonth<beginTimeMonth)?false:true;
		}else{
			return true;
		}
	}
});
</script>
</html>