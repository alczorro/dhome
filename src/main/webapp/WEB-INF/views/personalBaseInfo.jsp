<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%@ page import="java.util.*" %>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><fmt:message key='personalBaseInfo.title'/></title>
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
					<li class="active">
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
			    
			    <table class="personal-modify">
			    	<tr>
			    		<th><fmt:message key='personalBaseInfo.zhName'/></th>
			    		<td><c:out value="${user.zhName }"/></td>
			    	</tr>
			    	<tr>
			    		<th><fmt:message key='personalBaseInfo.enName'/></th>
			    		<td><c:out value="${user.enName }"/></td>
			    	</tr>
			    	<tr>
			    		<th><fmt:message key='personalBaseInfo.Email'/></th>
			    		<td><c:out value="${user.email }"/></td>
			    	</tr>
			    	<tr>
			    		<th><fmt:message key='personalBaseInfo.currentPosition'/></th>
			    		<td><c:out value="${user.salutation }"/></td>
			    	</tr>
			    	
			    	<tr>
			    		<th><fmt:message key='personalBaseInfo.instituation'/></th>
			    		<td>${user.firstClassDisciplineName}<c:if test="${!empty user.secondClassDisciplineName}">，</c:if> ${user.secondClassDisciplineName}</td>
			    	</tr>
			    	<tr>
			    		<th><fmt:message key='personalBaseInfo.interest'/><input type="hidden" id="interests-ids" value="${interestIds }"></th>
			    		<td id="display-interests"><c:out value="${interests}"/></td>
			    	</tr>
			    	<tr>
			    		<th><fmt:message key='personalBaseInfo.introduction'/></th>
			    		<td><c:out value="${user.introduction}"/></td>
			    	</tr>
			    	<tr>
			    		<th></th>
			    		<td><button id="popup-edit" data-toggle="modal" class="btn btn-primary"><fmt:message key='personal.common.edit'/></button></td>
			    	</tr>
			    </table>				
			</div>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
	<div id="edit-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3><fmt:message key='personal.common.basicInfo'/></h3>
        </div>
		<form method="post" id="editBasic" name="edit-baseinfo" class="form-horizontal no-bmargin" action="<dhome:url value='/people/${domain}/admin/personal/edit/baseinfo'/>">
			<fieldset>
			<div class="modal-body">
				<input type="hidden" name="func" value="save" /> 
				<div class="control-group">
         			<label class="control-label"><fmt:message key='personalBaseInfo.zhName'/></label>
          			<div class="controls">
            			<input maxlength="254" type="text" name="zhName" value='<c:out value="${user.zhName }"/>' class="register-xlarge"/>
            			<p class="help-inline"><fmt:message key='personalBaseInfo.please.zhName'/></p>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label"><fmt:message key='personalBaseInfo.enName'/></label>
          			<div class="controls">
            			<input maxlength="250" type="text" name="enName" value='<c:out value="${user.enName }"/>' class="register-xlarge"/>
            			<p class="help-inline"><fmt:message key='personalBaseInfo.please.enName'/></p>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label"><fmt:message key='personalWorkInfo.position'/></label>
          			<div class="controls">
            			<input maxlength="250" type="text" name="salutation" value='<c:out value="${user.salutation }"/>' class="register-xlarge"/>
            			<p class="help-inline"><fmt:message key='personalBaseInfo.please.salutation'/></p>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label"><fmt:message key='personalBaseInfo.instituation'/></label>
          			<div class="controls">
            			<select name="firstClassDiscipline" id="firstClassDiscipline" class="register-xmiddle">
							<option value="0"><fmt:message key='personalBaseInfo.firstDiscipline'/></option>
				            <c:forEach items="${firstClass }" var ="item">
				              <option <c:if test="${user.firstClassDiscipline eq item.id}">selected</c:if> value=${item.id }>${item.name }</option>
				            </c:forEach>
						</select> 
						<select name="secondClassDiscipline" id="secondClassDiscipline" class="register-xmiddle">
							<option value="0"><fmt:message key='personalBaseInfo.secondDiscipline'/></option>
				            <c:forEach items="${secondClass }" var ="item">
				              <option <c:if test="${user.secondClassDiscipline eq item.id}">selected</c:if> value=${item.id }>${item.name }</option>
				            </c:forEach>
						</select>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label"><fmt:message key='personalBaseInfo.interest'/></label>
          			<div class="controls">
            			<input maxlength="250" type="text" name="interests" class="register-xmiddle"/>
            			<p class="help-inline"><fmt:message key='personalBaseInfo.please.interest'/></p>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label"><fmt:message key='personalBaseInfo.introduction'/></label>
          			<div class="controls">
            			<textarea  name="introduction" rows="5" cols="70" class="register-xlarge"><c:out value="${user.introduction}"/></textarea>
            			<p class="help-inline"></p>
          			</div>
        		</div>
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="submit" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </fieldset>
        </form>
	</div>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/css/tokenInput.css"/>"/>
<script type="text/javascript">
$(function(){
	$("#edit-popup").on("shown",function(){
		$("body").css("overflow","hidden");
		$(".token-input-dropdown-facebook").css("overflow","auto");
	});
	$("#edit-popup").on("hidden",function(){
		$("body").css("overflow","auto");
		$(".modal-backdrop").remove();
	});
	$("#firstClassDiscipline").change(function(){  
		var id=$("#firstClassDiscipline option:selected").val();
		var target=$("#secondClassDiscipline")[0];
		$.ajax({
			url:"<dhome:url value='/system/regist'/>?func=getDisciplineChild",
			type:'GET',
			data: "parentId="+id,
			success:function(data){
				$(target).html('<option value="0"><fmt:message key="personalBaseInfo.secondDiscipline"/></option>');
				$(data).each(function(i,n){
					target.options.add(new Option(n.name,n.id,false,false)); 
				});
			}
		});
	});
	$("form[name=edit-baseinfo]").validate({
		submitHandler:function(form){
			var texts = new Array();
			$("li[class^=token]").each(function(index, element){
				var $p = $(element).find("p");
				if($p.length>0){
					texts.push($.trim($p.text()));
				}
			});
			$("input[name=interests]").val(texts);
			form.submit();
		},
		rules:{
			zhName:{required:true},
			enName:{required:true},
			introduction:{maxlength:600}
		},
		messages:{
			zhName:"<fmt:message key='personalBaseInfo.warning.zhName'/>",
			enName:"<fmt:message key='personalBaseInfo.warning.enName'/>",
			introduction:"<fmt:message key='personalBaseInfo.warning.introduction'/>"
		},
		success:function(label){
			label.parents("div.control-group").removeClass("error");
			label.parents("div.control-group").addClass("success");
			label.html("<font color='green'>√</font>");
		},
		errorPlacement:function(error, element){
			element.parents("div.control-group").removeClass("success");
			element.parents("div.control-group").addClass("error");
			element.next().html("").append(error);
		}
	});
	
	var $tokenObj = $("input[name=interests]").tokenInput("<dhome:url value='/system/index/interest'/>", {
		theme:"facebook",
		hintText: "<fmt:message key='personalBaseInfo.tokeninput.hintText'/>",
		searchingText: "<fmt:message key='personalBaseInfo.tokeninput.searchingText'/>",
		noResultsText: "<fmt:message key='personalBaseInfo.tokeninput.noResultsText'/>",
		preventDuplicates: true,
		queryParam:"keyword"
	});
	
	$("button#popup-edit").click(function(){
		var interests = $("#display-interests").text();
		var interestIds = $("#interests-ids").val();
		if(""!=interests){
			var array = interests.split(",");
			var arrayIds = interestIds.split(",");
			$tokenObj.tokenInput("clear");
			for(var i=0; i<array.length; i++){
				$tokenObj.tokenInput("add", {id:$.trim(arrayIds[i]), name: $.trim(array[i])});
			}
		}
		$("#edit-popup").modal("show");
	});
	
	$("a.cancel").click(function(){
		$("#edit-popup").modal("hide");
	});
});
</script>
</html>