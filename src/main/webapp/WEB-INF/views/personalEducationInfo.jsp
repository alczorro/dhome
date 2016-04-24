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
	<title><fmt:message key='personalEducationInfo.title'/></title>
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
					<li>&nbsp;&nbsp;&nbsp;</li>
					<li>
					    <a href="<dhome:url value='/people/${domain}/admin/personal'/>">
					    	<strong><fmt:message key='personal.common.basicInfo'/></strong>
					    </a>
				    </li>
				    <li>
				       <a href="http://passport.escience.cn/user/password.do?act=showChangePassword" target="_blank">
				       <strong><fmt:message key='personal.common.updatePsw'/></strong></a>
				    </li>
				    <li>
					    <a href="<dhome:url value='/people/${domain}/admin/personal/work'/>">
					    	<strong><fmt:message key='personal.common.work'/></strong>
					    </a>
				    </li>
				    <li class="active">
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
					    	<c:when test="${educations.size()>0}">
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
					    			<a data-toggle="modal" class="float-right popup-edit"><i class="icon-edit"></i><span class="icon-link"><fmt:message key='personal.common.edit'/></span></a>
						    		<a data-toggle="modal" class="float-right popup-remove"><i class="icon-trash"></i><span class="icon-link"><fmt:message key='personal.common.delete'/></span></a>
					    		</li>
					    		</c:forEach>
					    	</c:when>
					    	<c:otherwise>
					    		<span><fmt:message key='personalEducationInfo.warning.noEdu'/></span>
					    	</c:otherwise>
					    </c:choose>
				    </ul>
				    <div class="d-center">
				    	<a id="popup-add" class="btn btn-primary" data-toggle="modal"><fmt:message key='personal.common.add'/></a>
				    </div>
			    </div>
		    </div>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
	<div tabindex="-1" id="remove-education-popup" class="modal hide fade">
		<div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">×</button>
	        <h3><fmt:message key='personalEducationInfo.deleteEdu'/></h3>
        </div>
        <form method="post" class="form-horizontal no-bmargin" name="remove-education" action="<dhome:url value='/people/${domain}/admin/personal/edit/education/'/>">
			<input type="hidden" name="func" value="delete">
			<input type="hidden" name="id" value="">
			<fieldset>
				<div class="modal-body">
					<span><fmt:message key='personalEducationInfo.deleteEduConfirm'/></span>
					<ul class="x-list work no-border"></ul>
				</div>
	        	<div class="modal-footer">
					<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
					<button type="submit" class="btn btn-primary"><fmt:message key='common.confirm.delete'/></button>
		        </div>
	        </fieldset>
        </form>
	</div>
	<div tabindex="-1" id="popup-education" class="modal hide fade">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3><fmt:message key='personal.common.education'/></h3>
        </div>
        <form method="post" name="edit-workinfo" id="editEdu" class="form-horizontal no-bmargin" action="<dhome:url value='/people/${domain}/admin/personal/edit/education'/>">
			<fieldset>
				<div class="modal-body">
					<input type="hidden" name="func" value="save" /> 
					<input type="hidden" name="id" value=""/>
					<div class="control-group">
	         			<label class="control-label"><fmt:message key='personalEducationInfo.degree'/></label>
	          			<div class="controls">
	            			<select name="degree">
								<option value="<fmt:message key='personalEducationInfo.banchelor'/>"><fmt:message key='personalEducationInfo.banchelor'/></option>
								<option value="<fmt:message key='personalEducationInfo.master'/>"><fmt:message key='personalEducationInfo.master'/></option>
								<option value="<fmt:message key='personalEducationInfo.doctor'/>"><fmt:message key='personalEducationInfo.doctor'/></option>
							</select>
	            			<span class="help-inline gray-text message"><fmt:message key='personalEducationInfo.please.degree'/></span>
	          			</div>
	        		</div>
					<div class="control-group">
	         			<label class="control-label"><fmt:message key='personalEducationInfo.university'/></label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="institutionZhName" id="institutionZhName" value="" />
	            			<span class="help-inline gray-text message"><fmt:message key='personalEducationInfo.please.university'/></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key='personalEducationInfo.school'/></label>
	          			<div class="controls">
	            			<input maxlength="255" type="text" name="department" value="" />
	            			<span class="help-inline gray-text message"><fmt:message key='personalEducationInfo.please.school'/></span>
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
			degree:{required:true},
			institutionZhName:{required:true},
			beginTimeYear:{required:true},
			beginTimeMonth:{required:true},
			endTimeYear:{required:true},
			endTimeMonth:{required:true}
		},
		messages:{
			degree:"<fmt:message key='personalEducationInfo.warning.degree'/>",
			institutionZhName:"<fmt:message key='personalEducationInfo.warning.university'/>",
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
			}
			else{
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
	
	$("a.popup-edit").live("click",function(){
		var $parent = $(this).parent();
		$("#popup-education input[name=id]").val($parent.attr("id"));
		$("#popup-education select[name=degree]").val($parent.children("span.degree").text().toString().replace(/\s/g, ""));
		$("#popup-education input[name=institutionZhName]").val($.trim($parent.children("span.institutionZhName").text()));
		$("#popup-education input[name=department]").val($parent.children("span.department").text());
		$("#popup-education input[name=beginTime]").val($parent.children("span.beginTime").text());
		$("#popup-education input[name=endTime]").val($parent.children("span.endTime").text());
		selectDateTime($parent);
		$(".success").removeClass("success");
		$(".error").removeClass("error");
		$("#popup-education").modal("show");
	});
	
	function selectDateTime($parent){
		var beginTime = $parent.children("span.beginTime").text();
		var endTime = $parent.children("span.endTime").text();
		var itemB = beginTime.split(".");
		$("#popup-education select[name=beginTimeYear]").val(itemB[0]);
		$("#popup-education select[name=beginTimeMonth]").val(itemB[1]);
		if($.trim(endTime) != "<fmt:message key='personalWorkInfo.untilnow'/>"){
			$("#popup-education select[name=endTimeYear]").attr("disabled", false);
			$("#popup-education select[name=endTimeMonth]").attr("disabled", false);
			$("#popup-education input[type=checkbox][name=now]").attr("checked", false);
			var itemE = endTime.split(".");
			$("#popup-education select[name=endTimeYear] option[value="+itemE[0]+"]").attr("selected","selected");
			$("#popup-education select[name=endTimeMonth] option[value="+itemE[1]+"]").attr("selected","selected");
		}else{
			$("#popup-education select[name=endTimeYear]").attr("disabled", true);
			$("#popup-education select[name=endTimeMonth]").attr("disabled", true);
			$("#popup-education input[type=checkbox][name=now]").attr("checked", true);
		}
	}
	
	$("a.popup-remove").click(function(){
		var eduId = $(this).parent().attr("id");
		$("input[type=hidden][name=id]").val(eduId);
		var $content = $(this).parent().clone();
		$content.children("a").remove();
		$("#remove-education-popup ul").html("").append($content);
		$("#remove-education-popup").modal("show");
	});
	
	$("#popup-add").live("click",function(){
		$("#popup-education input[name=id]").val("");
		$("#popup-education select[name=degree]").val("");
		$("#popup-education input[name=institutionZhName]").val("");
		$("#popup-education input[name=department]").val("");
		$("#popup-education input[name=beginTime]").val("");
		$("#popup-education input[name=endTime]").val("");
		$(".success").removeClass("success");
		$(".error").removeClass("error");
		$("#popup-education").modal("show");
	});
	
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
		$("#popup-education").modal("hide");
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