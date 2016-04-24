<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
	<title><fmt:message key="createIndex.title"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>
<body class="dHome-boarding" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="commonBanner.jsp" flush="true">
		<jsp:param value="true" name="index" />
		<jsp:param value="${umtExist }" name="b_umtExist"/>
	</jsp:include>
	<div class="navbar boarding container">
		<div class="navbar-inner" style="filter:none;">      
			<%-- <a class="dhome-logo-black" href="<dhome:url value=""/><dhome:key value="deploy_subfix"/>"></a> --%>    
			<div class="progress-bar one"> 
			    <span><fmt:message key="config.common.step1"/></span>
			    <i></i> <i></i> <i></i> <i></i> <i></i> 
			    <span><fmt:message key="config.common.step5"/></span> 
			</div>   
		</div><!-- /navbar-inner -->  
	</div>
	<div class="container narrow">
		<div class="row-fluid">
			<div class="span12">
				<div class="dhome-layout">
					<div class="page-header x-left">
						<h2><fmt:message key="createIndex.subTitle"/></h2>
					</div>
					<form id="registForm" class="form-horizontal" method="post" action="<dhome:url value="/system/regist?func=stepTwo"/>">
				        <fieldset>
				          <div class="control-group">
				            <div class="controls">
				              <input id="zhName" name="zhName" value="${zhName }" class="register-xlarge focused" type="text" placeholder="<fmt:message key="createIndex.placeholder.zhName"/>">
				              <span  class="help-inline gray-text"><fmt:message key="createIndex.form.zhName"/></span>
				            </div>
				          </div>
				          <div class="control-group">
				            <div class="controls">
				              <input id="enName" name="enName" value="${enName }" class="register-xlarge focused" type="text" placeholder="<fmt:message key="createIndex.placeholder.enName"/>">
				              <span class="help-inline gray-text"><fmt:message key="createIndex.form.enName"/></span>
				            </div>
				          </div>
				          <div class="control-group">
				            <div class="controls">
				              <input id="emailToRegist" <c:if test="${!empty umtExist &&umtExist eq true }">readonly="true"</c:if> class="register-xlarge focused"   id="emailToRegist" name="emailToRegist" type="text" value="${emailToRegist }" placeholder="<fmt:message key="createIndex.placeholder.email"/>">
				              <span class="help-inline gray-text" style="width:auto;"><fmt:message key="createIndex.form.email"/></span>
				            </div>
				          </div>
				          <c:choose>
				          <c:when test="${umtExist eq null || umtExist eq false }">
				          <div class="control-group">
				            <div class="controls">
				              <input class="register-xlarge focused" id="password" name="password"  type="password" placeholder="<fmt:message key="createIndex.placeholder.password"/>">
				              <span class="help-inline gray-text"><fmt:message key="createIndex.form.password"/></span>
				            </div>
				          </div>
				          <div class="control-group">
				            <div class="controls">
				              <input class="register-xlarge focused" name="repeatPassword" type="password" placeholder="<fmt:message key="createIndex.placeholder.repeatPassword"/>">
				              <span class="help-inline gray-text"><fmt:message key="createIndex.form.repeatPassword"/></span>
				            </div>
				          </div>
					      </c:when>
					      <c:otherwise>
					      	<input type="hidden" name="umtExist" value="${umtExist}"/>
					      </c:otherwise>
					      </c:choose>
				          <div class="control-group">
				            <div class="controls">
				              <select name="firstClassDiscipline" id="firstClassDiscipline" class="register-xmiddle">
				              	<option value="0"><fmt:message key="createIndex.placeholder.firstClassDiscipline"/></option>
				              	<c:forEach items="${rootDisciplines }" var ="item">
				              		 <option value=${item.id }>${item.name }</option>
				              	</c:forEach>
				              </select>
				              <select name="secondClassDiscipline" id="secondClassDiscipline" class="register-xmiddle">
				                <option value="0"><fmt:message key="createIndex.placeholder.secondClassDiscipline"/></option>
				              </select>
				              <span class="help-inline gray-text"><fmt:message key="createIndex.form.discipline"/></span>
				            </div>
				          </div>
				           <div class="control-group">
				            <div class="controls">
								http://www.escience.cn/people/
				              	<input class="register-xsmall focused" id="domain"  name="domain" value="${defaultDomain}" type="text">
				              	<span class="help-inline gray-text"><fmt:message key="createIndex.form.domain"/></span>
				            </div>
				          </div>
				          <div class="control-group">
				            <div class="controls">
				              <input class="register-xlarge focused" name="interests" type="text" placeholder="<fmt:message key="createIndex.placeholder.interest"/>">
				              <span class="help-inline gray-text"><fmt:message key="createIndex.form.interest"/></span>
				            </div>
				          </div>
				           <div class="control-group">
				            <div class="controls">
								<textarea name="introduction" rows="3" cols="10" class="register-xlarge"></textarea>
				              	<span class="help-inline gray-text"><fmt:message key="createIndex.form.introduction"/></span>
				            </div>
				          </div>
				           <div class="control-group">
				            <div class="controls">
								<input type="checkbox" id="read" name="read" style="margin-bottom:5px; margin-right:5px;" /><fmt:message key="createIndex.check.agreement.msg"/><a target="_blank"  href="<dhome:url value="/system/regist/protocal.html"/>"><fmt:message key="createIndex.check.agreement.msg.a"/></a>
				           		<span id="read_msg"></span>
				            </div>
				          </div>
				          <div class="form-actions clear-b">
				          
				          	<button type="submit" class="btn btn-primary btn-large btn-set-large"><fmt:message key="createIndex.form.submit"/></button>
				          </div>
				        </fieldset>
				      </form>
				</div>
			</div>
		</div>
		<jsp:include page="commonheader.jsp"></jsp:include>
		<jsp:include page="commonfooter.jsp"></jsp:include>
	</div>

</body>

<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/css/tokenInput.css"/>"/>
<script>
$(document).ready(function(){	
	$("#firstClassDiscipline").change(function(){  
		var id=$("#firstClassDiscipline option:selected").val();
		var target=$("#secondClassDiscipline")[0];
		$.ajax({
			url:"<dhome:url value='/system/regist'/>?func=getDisciplineChild",
			type:'POST',
			data: "parentId="+id,
			success:function(data){
				$(target).html('<option value="0"><fmt:message key="createIndex.placeholder.secondClassDiscipline"/></option>');
				$(data).each(function(i,n){
					target.options.add(new Option(n.name,n.id,false,false)); 
				});
			}
		});
	});
	
	var $tokenObj = $("input[name=interests]").tokenInput("<dhome:url value='/system/index/interest'/>", {
		theme:"facebook",
		hintText: "<fmt:message key='personalBaseInfo.tokeninput.hintText'/>",
		searchingText: "<fmt:message key='personalBaseInfo.tokeninput.searchingText'/>",
		noResultsText: "<fmt:message key='personalBaseInfo.tokeninput.noResultsText'/>",
		preventDuplicates: true,
		queryParam:"keyword"
	});
	
	$("form").keypress(function(event){
		if(event.which == 13){
			event.preventDefault();
		}
	});
	
	$("input[name=interests]").keypress(function(event){
		if(event.which == 13){
			var text = $(this).val().toLowerCase();
			if(text != ""){
				$tokenObj.run_search(text);
			}
			event.preventDefault();
		}
		
	});
	
	/* $("#form-submit").click(function(event){
		/* var data = $tokenObj.tokenInput("get");
		var texts = new Array();
		for(var i=0; i<data.length;i++){
			texts.push(data[i].name);
		} 
		$("#registForm")[0].submit();
	}); */
	
	$.validator.addMethod("urlRegex", function(value, element){
    	var regex = /^[a-zA-Z0-9\-]+$/;
    	return this.optional(element)||(regex.test(value));
    }, "<fmt:message key='createIndex.check.onlyLetterNum'/>");
	 $.validator.addMethod("cnRegix", function(value, element){
		 var reg = /[\u4E00-\u9FA5\uf900-\ufa2d]/ig;
	     return this.optional(element)||(!reg.test(value));
	    }, "<fmt:message key='createIndex.check.notChinese'/>");
	 var validator=$("#registForm").validate({
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
	  rules: {
	  	zhName: {required:true},
	  	enName:{required:true,cnRegix:true},
	  	emailToRegist: {
	   		 required: true,
	   		 email: true,
	   		 remote:{
	   			type: "POST",
	   			url: '<dhome:url value="/system/regist?func=isEmailUsed"/>',
				data:{ 
					"email":function(){
							return $("#emailToRegist").attr("value");
						},
					"umtExist":function(){
						var umtExist = $("input[name=umtExist]");
						if(typeof(umtExist) == "undefined" || umtExist.length==0){
							return false;
						}else{
							return true;
						}
					}
	  			}
	   		 }
	   	  },
	   	password:{required:true,minlength:6},
	   	repeatPassword:{
	   		required:true,
	   		equalTo:"#password"
	   	},
	   	domain:{
	   		required:true,
	   		urlRegex:true,
	   		remote:{ 
	   			   type: "POST",
				   url: "<dhome:url value='/system/regist'/>?func=isDomainUsed",
				   data: { 
						"domain":function(){
							return $("#domain").attr("value");
						}
				   }
	   		}
	   	},
	   	introduction:{maxlength:600},
	   	read:{required:true}
	  },
	  validClass:"success",
	  errorClass:"error",
	   messages: {
		   zhName: {
			   required:"<fmt:message key='createIndex.check.required.zhName'/>"
			},
			enName:{
				required:"<fmt:message key='createIndex.check.required.enName'/>"
			},
			  
		   emailToRegist: {
		  	   required: "<fmt:message key='createIndex.check.required.email'/>",
		       email: "<fmt:message key='createIndex.check.email'/>",
		       remote:"<fmt:message key='createIndex.check.create.emailInUse'/><a href='<dhome:url value='/system/login'/>?umt=true&email="+$('#emailToRegist').val()+"'> <fmt:message key='index.umt.info.suffix'/></a>"
		   },
			password:{
				required:"<fmt:message key='createIndex.check.required.password'/>",
				minlength:"<fmt:message key='createIndex.check.password.minLength'/>"	
			},
			repeatPassword:{
		   		required:"<fmt:message key='createIndex.check.required.repeatPassword'/>",
		   		equalTo:"<fmt:message key='createIndex.check.repeatPassword'/>"
		   	},
		   	domain:{
		   		required:"<fmt:message key='createIndex.check.required.domain'/>",
		   		remote:"<fmt:message key='createIndex.check.domainInUse'/>"
		   	},
		    introduction:"<fmt:message key='createIndex.check.introduction.minLength'/>",
		    read:"<fmt:message key='createIndex.check.userRead.checked'/>"
		 },
		 success:function(label){
			 label.parent().parent().parent().removeClass("error");
			 label.parent().parent().parent().addClass("success");
			 label.html("<font color='green'>√</font>");
		 },
		 errorPlacement: function(error, element){
			 if(element.attr('id')=='read'){
				 error.appendTo(element.next().next());
			 }else{
				 element.parent().parent().removeClass("success");
				 element.parent().parent().addClass("error");
				 element.next().html("");
				 error.appendTo(element.next());
			 }
		 }
	});
});
</script>
</html>