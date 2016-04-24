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
<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">      
			<div class="container">        
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">          
					<span class="icon-bar"></span>          
					<span class="icon-bar"></span>          
					<span class="icon-bar"></span>        
				</a>      
				<div class="nav-collapse">         
					<ul class="nav" id="dhome-nav">   
					  <li class="dropdown">
	                      <a class="dropdown-toggle" data-toggle="dropdown"><span class="brand dhome-logo"></span> <b class="caret"></b></a>
							<div class="clear"></div>
	                  </li>
	                  <c:choose>
		                  <c:when test="${!empty b_umtExist}">
		                 	 <li class="active"><a href="#"><fmt:message key='createIndex.title'/></a></li>
		                  </c:when>
		                  <c:otherwise>
		                  	 <li <c:if test="${tag_isDiscoverOrIndex=='index' }">class="active"</c:if>><a href="<dhome:url value=""/><dhome:key value="deploy_subfix"/>"><fmt:message key="commonBanner.index"/></a></li>
		                  </c:otherwise>
	                  </c:choose>
					   <li <c:if test="${tag_isDiscoverOrIndex=='discover' }">class="active"</c:if>><a href="<dhome:url value="/system/discover" />"><fmt:message key="commonBanner.discover"/></a></li>    
					   <li <c:if test="${tag_isDiscoverOrIndex=='help' }">class="active"</c:if>><a href="<dhome:url value="/system/index/help.html" />"><fmt:message key="commonBanner.help"/></a></li>    
					</ul>
					<!--input type="text" name="bannerSearch" class="search-query span2" placeholder="搜主页"-->          
					<ul class="nav pull-right">  
					
					<li class="search">
						<div id="globalSearch" class="searchBox transition">
							<%-- <input type="text" value="${keyWord }" name="keyword" class="search-query span2 standby" placeholder="<fmt:message key="discover.search"/>"tabindex="1"> --%>
							<a class="search_reset" disable="true"></a>
							<div class="search_result" style="width: 350px; "></div>
						<div id="fullScreenCover"></div>
						</div>
					</li>
					<li><a id="changeLanguage"  href="#" ><fmt:message key="common.change.language"/></a></li>
					<c:choose>   
					  <c:when test="${!empty tag_currentUser}"> <!-- /登录状态 -->  
					      <!-- /判断当前的访问的页面 --> 
					      <c:choose>
					      		<c:when test="${!empty tag_isMyProfileSetting&&tag_isMyProfileSetting}">
					          	<li><a href="<dhome:url value="/people/${tag_myDomain}"/>"><fmt:message key="commonBanner.myHome"/></a></li> 
					              <li><a href="<dhome:url value="/people/${tag_myDomain}/admin"/>"><fmt:message key="commonBanner.management"/></a></li>	
					              <li class="active"><a href="<dhome:url value='/people/${tag_myDomain}/admin/personal/baseinfo'/>">${tag_currentUser.getZhName()}</a></li> 
					            </c:when>
					         <c:when test="${!empty isAdminStatus&&isAdminStatus}">
					           <li><a href="<dhome:url value="/people/${tag_myDomain}"/>"><fmt:message key="commonBanner.myHome"/></a></li> 	
					            <li class="active"><a href="<dhome:url value="/people/${tag_myDomain}/admin"/>"><fmt:message key="commonBanner.management"/></a></li>
					            <li><a href="<dhome:url value='/people/${tag_myDomain}/admin/personal/baseinfo'/>">${tag_currentUser.getZhName()}</a></li> 				      	
					          </c:when>
					          <c:otherwise>
					           <c:choose>
					           <c:when test="${targetDomain==tag_myDomain&&tag_isMyProfileSetting}">
					           <c:set value="true" var="visitMyDomain"/>
					           <li class="active"><a href="<dhome:url value="/people/${tag_myDomain}"/>"><fmt:message key="commonBanner.myHome"/></a></li> 
					           <li><a href="<dhome:url value="/people/${tag_myDomain}/admin"/>"><fmt:message key="commonBanner.management"/></a></li>	
					           <li><a href="<dhome:url value='/people/${tag_myDomain}/admin/personal/baseinfo'/>">${tag_currentUser.getZhName()}</a></li> 				      					           
					           </c:when>
					           <c:otherwise>
					             <li><a href="<dhome:url value="/people/${tag_myDomain}"/>"><fmt:message key="commonBanner.myHome"/></a></li>
					             <li><a href="<dhome:url value="/people/${tag_myDomain}/admin"/>"><fmt:message key="commonBanner.management"/></a></li>	
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
		<c:if test="${visitMyDomain!=null&&visitMyDomain=='true'}">
		<div class="container d-config"><a class="config-btn" href="<dhome:url value="/people/${tag_myDomain}/admin"/>"><fmt:message key="commonBanner.setMyHome"/></a></div>
		</c:if><!-- /如果当前是我的主页，显示管理提示 -->
		<div  class="container d-config">
		
		</div>
		
	</div>
	<form id="searchForm" action="<dhome:url value='/system/discover/search'/>" method='get'>
		<input type="hidden" name="keyword" id="keyword">
	</form>
	<div id="information" class="alert alert-success" style="display:none"></div>
	<form id="loginForm" action="<dhome:url value='/system/login'/>" method="post">
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
	    //需要转换的语言是什么呢？
	    var language="";
	    if(${empty userLanguage}){
	    	language='<fmt:message key="common.change.language.key"/>';
	    }else{
	    	language="${userLanguage=='en'?'zh':'en'}";
	    }
	    xmlHttp.open("GET", "<dhome:url value='/system/language/change'/>?language="+language+"&random="+Math.random(), true);  
	    xmlHttp.onreadystatechange = okFunc;    
	    xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");    
	    xmlHttp.send();    
	}  
	document.getElementById('changeLanguage').onclick=startAjax;
	</script>
	
	
