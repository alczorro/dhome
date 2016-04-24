<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<dhome:InitLanuage useBrowserLanguage="true"/>
<head>
<title><fmt:message key="search.title"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>
<body class="dHome-body gray" data-offset="50" data-target=".subnav"
	data-spy="scroll">
   <jsp:include page="commonBanner.jsp"></jsp:include>
   <div class="container">
		<div class="navbar searchbar">
	      <div class="navbar-inner">
	        <div class="some-lpad" id="searchConfig">
	        	<input type="text" value="${keyword }" id="keyWord" class="span6" placeholder="<fmt:message key="discover.search"/>">
	        	<button onclick="search();" class="btn"><fmt:message key="search.search"/></button>
	        	<br>
	        	<label class="radio inline">
	               <input type="radio" name="type"  value="all" checked="checked"><fmt:message key="search.all"/>
	            </label>
	            <label class="radio inline">
	               <input type="radio" name="type"  value="users"><fmt:message key="search.member"/>
	            </label>
	            <label class="radio inline">
	               <input type="radio" name="type" value="institutes"><fmt:message key="search.institute"/>
	            </label>
	        </div>
	      </div>
	    </div>
	</div>
	<div class="container">    
		<div  class="some-sspad canedit">
		<h4 id='none-tips' class="gray-text"><fmt:message key="search.noRecords"/></h4>
		<div id="user-tips">
			<h4 class="gray-text"><fmt:message key="search.member"/>：<span id="usersCount">0</span><fmt:message key="search.records"/></h4>
			<ul id="userList" class="searchResult"></ul>
			<span class="label searchMoreResult" id="loadMoreUser" onclick="loadMoreUser()"><fmt:message key="search.moreRecord"/></span>
		</div>	
		<div id="institute-tips">
			<h4  class="gray-text d-top"><fmt:message key="search.institute"/>：<span id="institutesCount">0</span><fmt:message key="search.records"/></h4>
			<ul id="instituteList" class="searchResult"></ul>
			<span class="label searchMoreResult" id="loadMoreInstitute" onclick="loadMoreInstitute()"><fmt:message key="search.moreRecord"/></span>
		</div>
		</div>
	</div>
	<jsp:include page="commonheader.jsp"></jsp:include>
<jsp:include page="commonfooter.jsp"></jsp:include>
</body>

<script>
$(document).ready(function(){
	$('#keyWord').bind('keyup', function(event){
		   if (event.keyCode=="13"){
		    	search();
		   }
		});
	search();
});
var size=10;
var userOffset=0;
var instituteOffset=0;
function loadMoreUser(){
	search('more',userOffset,'users');
}
function hideTips(){
	var type=$('input:radio:checked').val();
	if(type=='all'){
		$("#institute-tips").show();
		$("#user-tips").show();
	}else if(type=='users'){
		$("#institute-tips").hide();
		$("#user-tips").show();
	}else if(type='institutes'){
		$("#institute-tips").show();
		$("#user-tips").hide();
	}
}

function loadMoreInstitute(){
	search('more',instituteOffset,'institutes');
}
function search(isMore,offset,type){
	var keyword=$('#keyWord').val();
	var params={'type':type==null?$('input:radio:checked').val():type,'keyword':keyword,'offset':offset==null?0:offset,'size':size,'isMore':isMore};
	var url='<dhome:url value="/system/discover/search.json"/>';
	hideTips();
	ajaxRequest(url,params);
}
function hideUserLoadMore(length){
	if(length==null||length<size){
		$('#loadMoreUser').hide();
	}else{
		$('#loadMoreUser').show();
	}
}
function hideInstituteLoadMore(length){
	if(length==null||length<size){
		$('#loadMoreInstitute').hide();
	}else{
		$('#loadMoreInstitute').show();
	}
}
function isNull(str){
	return (typeof(str)=='undefined'||str==null||str.length==0||str=='');
}
function formatStr(str){
	var index=str.lastIndexOf('|');
	if(index>0){
		if(index==str.length-2){
			return str.substr(0,index);
		}
	}
	return str;
}
function ajaxRequest(url, params){
	$.ajax({
		url : url,
		type : 'POST',
		data : params,
		success : function(data){
			if(params.isMore!='more'){
				$('#usersCount').html(data.usersCount);
				$('#institutesCount').html(data.institutesCount);
				$('#userList').html("");
				$('#instituteList').html("");
				userOffset=0;
				instituteOffset=0;
			}
			//replace imageUrl
			$(data.institutes).each(function (n,i){
				var logoImg='<dhome:img imgId="999"/>';
				var defaultImg='<dhome:img imgId="0" imgName="dhome-institute.png" />';
				if(i.logoId=='0'){
					i.imgUrl=defaultImg;
				}else{
					i.imgUrl=logoImg.replace('999',i.logoId);
				}
			});
			$(data.users).each(function (n,i){
				i.institutionName=(i.institution||0).institutionName;
				i.department=(i.institution||0).department;
				var userInfo="";
				if(!isNull(i.institutionName)){
					userInfo+=i.institutionName+' | ';
				}
				if(!isNull(i.department)){
					userInfo+=i.department+' | ';
					
				}
				if(!isNull(i.salutation)){
					userInfo+=i.salutation+' | ';
					
				};
				i.userInfo=formatStr(userInfo);
				var interestHtml="";
				$(i.interest).each(function (i,n){
					var keyDecode=decodeURIComponent(decodeURIComponent(n.keyword).replace(/\+/g,'%20'));
					if(i!=0){
						interestHtml+="&nbsp;,&nbsp;";
					}
					interestHtml+='<a target="_blank" href="<dhome:url value="/system/discover?type=interest&keyword='+n.keyword+'"/>">'+keyDecode+'</a>'
				});
				i.interest=interestHtml;
			});
			//hide load more
			if(data.type=='institutes'){
				hideInstituteLoadMore((data.institutes||0).length);
			}else if(data.type=='users'){
				hideUserLoadMore((data.users||0).length);
			}else if(data.type=='all'){
				hideInstituteLoadMore((data.institutes||0).length);
				hideUserLoadMore((data.users||0).length);
			}
			//所有结果都是0，就显示 没有匹配的结果
			if(params.isMore!='more'){
				showNoneResult((data.users||0).length,(data.institutes||0).length);
			}
			//render
			if(data.institutes!=null){
				instituteOffset+=data.institutes.length;
				$("#institute-template").render(data.institutes).appendTo("#instituteList");
			}
			
			if(data.users!=null){
				userOffset+=data.users.length;
				$("#user-template").render(data.users).appendTo("#userList");
			}
			
		},
		error : function(){
			
		}
	});
}
function showNoneResult(userLength,instituteLength){
	if((userLength==0||userLength==null)&&(instituteLength==0||instituteLength==null)){
		$('#none-tips').show();
		$('#user-tips').hide();
		$('#institute-tips').hide();
	}else{
		$('#none-tips').hide();
	}
	
}
</script>
<script type="text/html" id="institute-template">
<li class="ins">
	<span class="listCount"></span>
		<div class="ins-img"><a href="<dhome:url value="/institution/{{= domain}}/index.html"/>" target="_blank"><img src="{{= imgUrl}}"/></a></div>
		<div class="ins-info">
			<p><a href="<dhome:url value="/institution/{{= domain}}/index.html"/>" target="_blank" class="ins-title">{{= name}}</a></p>
			<p>{{= introduction}}</p>
			<p><fmt:message key="search.member2"/><span class="count"><a target="_blank" href="<dhome:url value="/institution/{{= domain}}/members.html"/>">{{= memberCount}}</a></span>
			   <fmt:message key="search.paper"/><span class="count"><a target="_blank" href="<dhome:url value="/institution/{{= domain}}/publications.html"/>">{{= paperCount}}</a></span> 
			   <fmt:message key="search.activity"/><span class="count"><a target="_blank" href="<dhome:url value="/institution/{{= domain}}/scholarevent.html"/>">{{= activityCount}}</a></span>
			</p>
		</div>
		<div class="clear"></div>
</li>
</script>
<script type="text/html" id="user-template">
<li class="member">
	<span class="listCount"></span>
	<div class="ins-img"><a href="{{= userPageURL}}" target="_blank"><img src="{{= imgURL}}"/></a></div>
	<div class="ins-info">
		<p><a href="{{= userPageURL}}" target="_blank" class="ins-title">{{= zhName}}</a></p>
		<p>{{= userInfo}}</p>
		<p><fmt:message key="search.interest"/>：{{= interest}}</p>
	</div>
	<div class="clear"></div>
</li>
</script>
</html>