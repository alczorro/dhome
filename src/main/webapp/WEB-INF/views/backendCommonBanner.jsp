<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:TopBannerLoader/>
<dhome:InitLanuage useBrowserLanguage="true"/>
<%
request.setAttribute("b_umtExist",request.getParameter("b_umtExist"));
request.setAttribute("context",request.getContextPath());
%>
<script type="text/javascript" src="http://www.escience.cn/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
<div id="ie-version" style="display:none;color:#fff;width:100%; background:#a00; text-align:center;font-size:14px;position:fixed;z-index:99999; top:0;line-height:3em;">您当前的浏览器版本太低，可能导致网站部分功能无法正常使用，建议立即升级到 Internet Explorer 9及以上版本 或 Google Chrome。
</div>  
<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner iap">      
			<div class="container">        
				<div class="nav-collapse">
					
					<span class="iap-logo"></span>
					<a class="brand" href="#">中国科学院大气物理研究所 · 人力资源管理系统</a>   
					<ul class="nav pull-right">  
					<c:choose>   
					  <c:when test="${!empty tag_currentUser}"> <!-- /登录状态 -->  
					      <!-- /判断当前的访问的页面 --> 
					      <c:choose>
					      		<c:when test="${!empty tag_isMyProfileSetting&&tag_isMyProfileSetting}">
					              <li class="active"><a href="<dhome:url value='/people/${tag_myDomain}/admin/personal/baseinfo'/>">${tag_currentUser.getZhName()}</a></li> 
					            </c:when>
					         <c:when test="${!empty isAdminStatus&&isAdminStatus}">
					            <li><a href="<dhome:url value='/people/${tag_myDomain}/admin/personal/baseinfo'/>">${tag_currentUser.getZhName()}</a></li> 				      	
					          </c:when>
					          <c:otherwise>
					           <c:choose>
					           <c:when test="${targetDomain==tag_myDomain&&tag_isMyProfileSetting}">
					           <c:set value="true" var="visitMyDomain"/>
					           <li><a href="<dhome:url value='/people/${tag_myDomain}/admin/personal/baseinfo'/>">${tag_currentUser.getZhName()}</a></li> 				      					           
					           </c:when>
					           <c:otherwise>
					             <li><a href="<dhome:url value='/people/${tag_myDomain}/admin/personal/baseinfo'/>">${tag_currentUser.getZhName()}</a></li> 				      	 
					           </c:otherwise>
					           </c:choose>	
					          </c:otherwise>
					          </c:choose>
	                   
						<li><a href="<dhome:url value='/system/logout'/>"><fmt:message key="common.logout"/></a></li>  
					  </c:when>
					  <c:when test="${!empty b_umtExist }">
					  	<li><a href="<dhome:url value='/system/logout'/>"><fmt:message key="common.logout"/></a></li>  
					  </c:when>			
					  <c:otherwise>
					  <li><a href="javascript:login()"><fmt:message key="common.login"/></a></li>
					  <li><a href="<dhome:url value='/system/regist?func=stepOne'/>"><fmt:message key="common.regist"/></a></li>  </c:otherwise>
					  </c:choose>
					             
					</ul>        
				</div><!-- /.nav-collapse -->      
			</div>     
		</div><!-- /navbar-inner -->  
	</div>
	<form id="searchForm" action="<dhome:url value='/system/discover/search'/>" method='get'>
		<input type="hidden" name="keyword" id="keyword">
	</form>
	<div id="information" class="alert alert-success" style="display:none"></div>
	<form id="loginForm" action="${baseUrl }<dhome:url value='/system/login'/>" method="post">
		<input type="hidden" id="currentPageUrl" name="pageUrl" value=""/>
	</form>
	<script>
	function login(){
		var homeUrl='<%=request.getRequestURL()%>'+'<dhome:key value="deploy_subfix"/>';
		if(homeUrl!=window.location.href){
			document.getElementById('currentPageUrl').value=window.location.href;
		}
		document.getElementById('loginForm').submit();
	}
	var xmlHttp;    
	function createXMLHttpRequest() {    
	    if (window.ActiveXObject) {  
	        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");    
	    }    
	    else if (window.XMLHttpRequest) {  
	        xmlHttp = new XMLHttpRequest();    
	    }
	    
	}    
	var okFunc = function(){    
	    if(xmlHttp.readyState == 4) {    
	        if(xmlHttp.status == 200) {  
	            window.location.reload();
	        }    
	    }    
	}    
	var startAjax = function(){    
	    createXMLHttpRequest();    
	    if( !xmlHttp){    
	        return alert('create failed');    
	    }
	}  
// 	document.getElementById('changeLanguage').onclick=startAjax;
	</script>
	
	
