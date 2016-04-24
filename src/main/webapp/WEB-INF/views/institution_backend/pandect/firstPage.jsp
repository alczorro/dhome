<%@ page language="java" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%@ page import="java.util.*" %>
<dhome:institutionLeftMenuCount domain="${domain }" />

<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true" />
<% 
	request.setAttribute("now", new Date().getYear()+1900); 
    response.addHeader("Cache-Control", "no-store, must-revalidate"); 
    response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");
%>
<html lang="en">
<head>
<title>中国科学院大气物理研究所 · 人力资源管理系统</title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv=“X-UA-Compatible” content=“IE=8″ />
<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body institu iap" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
	<form id="loginAdminForm" action="${baseUrl }<dhome:url value='/system/login'/>" method="post">
		<input type="hidden" name="state" value="<dhome:url value="/institution/${domain}/backend/pandect/index"/>"/>
	</form>
	<form id="loginMemberForm" action="${baseUrl }<dhome:url value='/system/login'/>" method="post">
		<input type="hidden" name="state" value="<dhome:url value="/institution/${domain}/backend/readOnly/index"/>"/>
	</form>
	<script type="text/javascript">
	function adminLogin(){ document.getElementById('loginAdminForm').submit(); }
	function memberLogin(){ document.getElementById('loginMemberForm').submit(); }
	</script>
	
	<div class="container">
		<div class="ins_backend_iap">
    		<div class="insti-img">
    			<a><img src="<dhome:img imgId="57863"/>" /></a>
    		</div>	
    		<p class="admin">
    			<c:choose>
    			   <c:when test="${!empty user}">
					    <a href="<dhome:url value='/people/${domain}/admin/personal/baseinfo'/>">${user.getZhName()}</a>
	                    <c:if test="${isAdmin }">
	                       <a href="<dhome:url value="/institution/${domain}/backend/pandect/index"/>">管理</a>
	                    </c:if>
	                    <c:if test="${!isAdmin && isMember }">
	                       <a href="<dhome:url value="/institution/${domain}/backend/readOnly/index"/>">查看</a>
	                    </c:if>
						<a href="<dhome:url value='/system/logout'/>">注销</a>  
    			   </c:when>
    			   <c:otherwise>
    			      <a href="javascript:adminLogin();" >管理员登录</a>
    			      <a href="javascript:memberLogin();" >普通登录</a>
    			   </c:otherwise>
    			</c:choose>
    		</p>
   			<p class="insti-info">中国科学院大气物理研究所（简称大气所）的前身是1928年成立的原国立中央研究院气象研究所，是中国现代史上第一个研究气象科学的最高学术机构，目前已发展成为涵盖大气科学领域各分支学科的大气科学综合研究机构。

    大气所致力于研究和探索地球大气中和大气与周边环境相互作用的物理、化学、生物、人文过程的新规律；提供天气、气候和环境监测、预测和调控的先进理论、方法和技术；造就本领域的一流人才；服务于经济和社会的可持续发展和国家安全。

    截止2015年，大气所在册职工533人，其中，中国科学院院士7人，国家特支计划（”万人计划“）入选者4人，”千人计划“入选者8人，国家杰出青年基金获得者16人，科学院”百人计划“入选者23人。

    目前，大气所荣获各类成果奖150余项，其中，国家最高科学技术奖1项，国家自然科学奖12项，国家科技进步奖11项。</p>
        </div>
		<div class="ins_backend_rightContent">
			 
<!-- 			<ul id="navbar" class="nav nav-pills scholar d-nobottom" style="margin-left: 5px;margin-top:10px;margin-bottom: 10px;"> -->
<!-- 				<li class="dropdown" id="menuDept"> -->
<!-- 					<a id="dept" class="dropdown-toggle"	data-toggle="dropdown" href="#menu1" style="display:inline-block; float:left;">按部门<b class="caret"></b> -->
<!-- 					</a> -->
<!-- 					<ul id="menu11" class="dropdown-menu"> -->
<%-- 						<c:forEach items="${ dept }" var="item"> --%>
<%-- 						  <li><a href="javascript:selectDept('${item.id }','${item.shortName }')">${item.shortName}</a></li> --%>
<%-- 					   </c:forEach> --%>
<!-- 					</ul> -->
<!-- 				</li> -->
<!-- 				<li class="dropdown" id="menuYear"> -->
<!-- 					<a id="selectedYear" class="dropdown-toggle"	data-toggle="dropdown" href="#menu2" style="display:inline-block; float:left;">按年份<b class="caret"></b> -->
<!-- 					</a> -->
<!-- 					<ul id="menu11" class="dropdown-menu"> -->
<%-- 					  <c:forEach begin="2010" end="${now}" var="year"> --%>
<%-- 							<li><a href="javascript:selectYear('${now - year + 2010 }')">${now - year + 2010 }</a></li> --%>
<%-- 					  </c:forEach>  --%>
<!-- 					</ul> -->
<!-- 				</li> -->
<!-- 		    </ul> -->
<!-- 		    <ul class="clearfix pandect"> -->
<!-- 				<li class="member" id="member"> -->
<!-- 					<h4>员工</h4> -->
<%-- 					<span class="count">${empty memberCount ? 0:memberCount}</span> --%>
<!-- 				</li> -->
<!-- 				<li class="paper" id="paper"> -->
<!-- 					<h4>论文</h4> -->
<%-- 					<span class="count">${empty dPaperCount ? 0:dPaperCount}</span> --%>
<!-- 				</li> -->
<!-- 				<li class="treatise" id="treatise"> -->
<!-- 					<h4>论著</h4> -->
<%-- 					<span class="count">${empty treatiseCount ? 0:treatiseCount}</span> --%>
<!-- 				</li> -->
<!-- 				<li class="award" id="award"> -->
<!-- 					<h4>奖励</h4> -->
<%-- 					<span class="count">${empty awardCount ? 0:awardCount}</span> --%>
<!-- 				</li> -->
<!-- 				<li class="copyright" id="copyright"> -->
<!-- 					<h4>软件著作权</h4> -->
<%-- 					<span class="count">${empty copyrightCount ? 0:copyrightCount}</span> --%>
<!-- 				</li> -->
<!-- 				<li class="patent" id="patent"> -->
<!-- 					<h4>专利</h4> -->
<%-- 					<span class="count">${empty patentCount ? 0:patentCount}</span> --%>
<!-- 				</li> -->
<!-- 				<li class="topic" id="topic"> -->
<!-- 					<h4>课题</h4> -->
<%-- 					<span class="count">${empty topicCount ? 0:topicCount}</span> --%>
<!-- 				</li> -->
<!-- 				<li class="academic" id="academic"> -->
<!-- 					<h4>学术任职</h4> -->
<%-- 					<span class="count">${empty academicCount ? 0:academicCount}</span> --%>
<!-- 				</li> -->
<!-- 				<li class="periodical" id="periodical"> -->
<!-- 					<h4>期刊任职</h4> -->
<%-- 					<span class="count">${empty periodicalCount ? 0:periodicalCount}</span> --%>
<!-- 				</li> -->
<!-- 				<li class="training" id="training"> -->
<!-- 					<h4>人才培养</h4> -->
<%-- 					<span class="count">${empty trainingCount ? 0:trainingCount}</span> --%>
<!-- 				</li> -->
<!-- 			  </ul> -->
			  
			  <h4  class="detail" >员工分布</h4>
			  
			   <ul id="memberDeptNavbar" class="nav nav-pills scholar d-nobottom" style="margin:10px 0px 10px 5px;float:left">
				 <li class="dropdown" id="memberDept">
					<a id="member-dept" class="dropdown-toggle"	data-toggle="dropdown" href="#dept2" style="display:inline-block; float:left;">按部门<b class="caret"></b>
					</a>
					<ul id="menu11" class="dropdown-menu">
						<c:forEach items="${ dept }" var="item">
						  <li><a href="javascript:selectMemberDept('${item.id }','${item.shortName }')">${item.shortName}</a></li>
					   </c:forEach>
					</ul>
				 </li>
				
				<li></li>
		       </ul>
		     
		       <ul  id="memberNavbar"  class="nav nav-pills" style="margin:10px 10px 10px 0px;float:left">
					<li class="active">
	          		  <a id="member-degree">按学历分布</a>
	            	</li>
	            	
			        <li>
					  <a id="member-title">按职称分布</a>
				    </li>
				    
			        <li>
					  <a id="member-age">按年龄分布</a>
				    </li>
			    </ul>
			    
		<!-- 	  <div class="pie">
			  	<div id="educations" ></div>
			  	<p class="pieTitle">学历分布图</p>
			  </div> -->
			  <div class="pie">
			  	<div id="pie"></div>
			  	<p id="pieTitle" class="pieTitle">职称分布图</p>
			  </div>
	  
			  <div class="clear"></div>
			  <h4  class="detail ">
			  	论文分布
			  		<span id="paper-desc" class="dark-green total-count">
				    	<span>论文总数：${empty dPaperCount ? 0:dPaperCount}</span> 
				    	<span>总引用次数：${citedNum }</span> 
				    </span>
			 </h4>
			 
			  <ul id="paperNavbar"  class="nav nav-pills" style="margin-left: 15px;margin-top:10px;margin-bottom: 10px;">
					<li class="active">
	          		  <a id="paper-dept">按部门绘制</a>
	            	</li>
	            	
			        <li>
					  <a id="paper-year">按年份绘图</a>
				    </li>
			   </ul>
			   
		       <div id="papers" style="margin-left: 25px;"></div>
		        <ul id="paperNavbar-cite"  class="nav nav-pills" style="margin-left: 15px;margin-top:10px;margin-bottom: 10px;">
					<li class="active">
	          		  <a id="paper-dept-cite">按部门绘制</a>
	            	</li>
	            	
			        <li>
					  <a id="paper-year-cite">按年份绘图</a>
				    </li>
			   </ul>
		        <div id="papers-cite" style="margin-left: 25px;"></div>
<!-- 		       <div id="papers-year-jq" style="margin-left: 25px;"></div>
 -->			  
		</div>
	</div>
</body>
<%-- <jsp:include page="../../commonheader.jsp"></jsp:include> --%>
<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>

<!--画饼图必须引入的样式-->
<link rel="stylesheet" href="<dhome:url value="/resources/css/jquery.jqplot.min.css"/>" type="text/css" />
<script src="<dhome:url value="/resources/scripts/jquery/jquery.jqplot.min.js"/>" type="text/javascript"></script>
<script src="<dhome:url value="/resources/scripts/jquery/excanvas.js"/>" type="text/javascript"></script>
<script src="<dhome:url value="/resources/scripts/jquery/jqplot.pieRenderer.min.js"/>" type="text/javascript"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/jqplot.highlighter.min.js" />"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/jqplot.cursor.min.js" />"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/jqplot.dateAxisRenderer.min.js" />"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/jqplot.donutRenderer.min.js" />"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/jqplot.canvasTextRenderer.min.js" />"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/jqplot.canvasAxisTickRenderer.min.js" />"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/jqplot.categoryAxisRenderer.min.js" />"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/jqplot.barRenderer.js" />"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/jquery/jqplot.pointLabels.min.js" />"></script>

<style>
	.jqplot-target{color:#444;}
</style>
<script>
	var titlePie ="职称分布图";
	var educaPie ="学历分布图";
	var agePie ="年龄分布图";
	var memberDpetId = -1;
	var memberOp = "degree";
	var plot1;
	var plot2;
	var plot3;
	var year=-1;
	var deptmentId=-1;
	
	function selectYear(data){
		$('#selectedYear').html(data+'<b class="caret">');
		$('#menuYear').toggleClass("open",false);
		year=data;
		
		$.get('<dhome:url value="/institution/${domain}/count/"/>'+deptmentId+'/'+data).done(function(result){
			if(result.success){
				renderCount(result.data);
				if(deptmentId==-1){
					$("#member span").html(${memberCount});
				}
			}
		});
	}
	function selectDept(id,name){
		$('#dept').html(name+'<b class="caret">');
		$('#menuDept').toggleClass("open",false);
		deptmentId=id;
		
		$.get('<dhome:url value="/institution/${domain}/count/"/>'+deptmentId+'/'+year).done(function(result){
			if(result.success){
				renderCount(result.data);
			}
		});
	}
	function renderCount(data){
		for(var key in data){
			$("#"+key+" "+"span").html(data[key]);
		}
	};
	
	function selectMemberDept(id,name){
		$('#member-dept').html(name+'<b class="caret">');
		$('#memberDept').toggleClass("open",false);
		memberDpetId = id;
		
		var pieTitle = educaPie;
		if(memberOp =="title"){
			pieTitle = titlePie;
		}
		
		if(memberOp =="age"){
			pieTitle = agePie;
		}
		
		$.get('<dhome:url value="/institution/${domain}/member/"/>'+memberOp+'/'+id).done(function(result){
			if(result.success){
			    pie(result.data,pieTitle);
			}
		});
	}
	
	
	 function pie(data,title){
		 $('#pieTitle').html(title);
		 
		  if(plot1) {
		      plot1.destroy();
		   }
		  /* var data = ${titles}; */
		   plot1 = $.jqplot ('pie', [data], 
		    { 
		      seriesDefaults: {
		        renderer: jQuery.jqplot.DonutRenderer, 
		        rendererOptions: {
		            sliceMargin: 0,
		            showDataLabels: true,
		            dataLabels: 'value',
		            dataLabelPositionFactor: 1.4,
		            padding: 50,
 		            dataLabelThreshold:1,
 		            innerDiameter:0,
 		            dataLabels: 'both',
  		            dataLabelFormatString:"%d人(%0.1f%%)",
		        }
		      }, 
		      legend: { 
		    	  show: true, 
		    	  location: 'e',
		    	},
		      grid:{borderColor:'#ccc',borderWidth:'0.5',shadow:false,},
		    }
		  );
/* 		   plot1 = $.jqplot ('pie', [data], 
		    { 
		      seriesDefaults: {
		        renderer: jQuery.jqplot.PieRenderer, 
		        rendererOptions: {
		          diameter:230,
		          showDataLabels:true,
		          shadow:false,
 		          dataLabels: 'both',
 		          dataLabelFormatString:"%d%%(%d人)",
 		          dataLabelThreshold:1,
 		          dataLabelNudge:1
		        }
		      }, 
		      legend: { 
		    	  show: true, 
		    	  location: 'e',
		    	},
		      grid:{borderColor:'#ccc',borderWidth:'0.5',shadow:false,},
		    }
		  ); */
	  };
	
	  function highlight(counts){
		  if(plot3) {
		      plot3.destroy();
		   }
		    plot3 = $.jqplot('papers', [counts], {
// 		    	 animate: !$.jqplot.use_excanvas,
	             series:[
			       {
			    	   renderer : $.jqplot.BarRenderer,
				      shadow: false,
				      label: "发表数量",
				      pointLabels: { show: true },
				   
				      rendererOptions: {
				          smooth: true,
				      }
				   }],
				   
	             axesDefaults: {
	                 tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
	                 tickOptions: {
	                   angle: 30
	                 }
	             },
	             axes: {
	               xaxis: {
	            	   min:1,
	                 renderer: $.jqplot.CategoryAxisRenderer
	               },
	               yaxis: {
	            	 min:0,
	                 autoscale:true
	               },
	             },
	             legend: {
				       show: true,
				       location: 'ne',     // compass direction, nw, n, ne, e, se, s, sw, w.
				       xoffset: 25,        // pixel offset of the legend box from the x (or x2) axis.
				       yoffset: 25,        // pixel offset of the legend box from the y (or y2) axis.
				       placement:'outsideGrid'
				    },
				grid:{  
				   borderWidth: 0.5,
			       backgroundColor: "#e4eef6"  
			    },
				highlighter: {
			       show: false,
			       sizeAdjust: 7.5,
				   tooltipAxes: 'y',  
			    }
	           });
	  }
	  //............
	  function highlight2(cites){
		  if(plot2) {
		      plot2.destroy();
		   }
		    plot2 = $.jqplot('papers-cite', [cites], {
	             series:[{
	            	 renderer : $.jqplot.BarRenderer,
			          shadow: false,
			          label: "引用次数",
			          pointLabels: { show: true },
			          rendererOptions: {
			              smooth: true,
			          }
			       }],
				   
	             axesDefaults: {
	                 tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
	                 tickOptions: {
	                   angle: 30
	                 }
	             },
	             axes: {
	               xaxis: {
	            	   min:1,
	                 renderer: $.jqplot.CategoryAxisRenderer
	               },
	               yaxis: {
	            	 min:0,
	                 autoscale:true
	               },
	             },
	             legend: {
				       show: true,
				       location: 'ne',     // compass direction, nw, n, ne, e, se, s, sw, w.
				       xoffset: 25,        // pixel offset of the legend box from the x (or x2) axis.
				       yoffset: 25,        // pixel offset of the legend box from the y (or y2) axis.
				       placement:'outsideGrid'
				    },
				grid:{  
				   borderWidth: 0.5,
			       backgroundColor: "#e4eef6"  
			    },
				highlighter: {
			       show: false,
			       sizeAdjust: 7.5,
				   tooltipAxes: 'y',  
			    }
	           });
	  }
	  
	  
	function ajaxRequest(url, params, success, fail){
		//如果别的正在载入中，那么就别再加入了
		if(isLoading){
			return;
		}
		isLoading=true;
		$.ajax({
			url : url,
			type : 'POST',
			data : params,
			success : success,
			error : fail
		});
	}
	
	function fail(){
		notice_handler.error();
	}
	
	
	if (isSafari()) {
	    $(window).bind("pageshow", function (event) {
	        if (event.originalEvent.persisted) {
	            document.body.style.display = "none";
	            window.location.reload();
	        }
	    });
	}

	function isSafari() {
	    if (navigator.userAgent.indexOf("Safari") > -1) {
	        return true;
	    }
	    return false;
	}
</script>

<script>
$(document).ready(function(){
	 	var Sys = {};
	    var ua = navigator.userAgent.toLowerCase();
	    var s;
	    (s = ua.match(/rv:([\d.]+)\) like gecko/)) ? Sys.ie = s[1] :
	    (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
	    (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
	    (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
	    (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
	    (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
	    if (Sys.ie<9) {
	    	$("#ie-version").css("display","block");
	    };
// 	$(function () {
// 	    var Sys = {};
// 	    var ua = navigator.userAgent.toLowerCase();
// 	    var s;
// 	    (s = ua.match(/rv:([\d.]+)\) like gecko/)) ? Sys.ie = s[1] :
// 	    (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
// 	    (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
// 	    (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
// 	    (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
// 	    (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
// 	    alert("aaa");
// 	    alert(Sys.ie);
// 	    alert(Sys.ie<9);
// 	    alert(Sys.ie>9);
// 	    if (Sys.ie<9) {
// 	    	$("#ie-version").css("display","block");
// 	    };
	//	    if (Sys.chrome) $('span').text('Chrome: ' + Sys.chrome);
// 	});
// 	initLoginStatus();
// 	 function initLoginStatus(){
// 		 var $prevSibling = $("#logined");
// 		 $.ajax({
// 			 url : "<dhome:url value='/system/login/isLogin.json'/>",
// 			 type : "POST",
// 			 success : function(rtnData){
// 				 if (typeof(rtnData)=='object' && rtnData.isLogin){
// 					var html = "<div class='float-right m-right'>"+
// 							   "	<span><fmt:message key='index.loginSuccess'/></span>"+
// 							   "	<a href='"+"<dhome:url value='/people/${domain}/admin'/>"+"'><fmt:message key='index.intoManagement'/></a> |"+
// 							   "	<a href='"+"<dhome:url value='/people/${domain}'/>"+"'><fmt:message key='index.intoPerson'/></a>"+
// 							   "<div>";
// 					/* var $prevSibling = $("div.span5.forIE:first").prev();
// 					$prevSibling.next().remove();
// 					$prevSibling.next().remove();
// 					$prevSibling.after(html); */
// 					$prevSibling.html(html);
// 					insertImage ();
// 					$('body').show();
// 				 }else{
// 					 var html = '<iframe height="55px" marginwidth="0" marginheight="0" scrolling="no" border="0" frameBorder="no" width="100%" id="umtLogin" src="<dhome:UmtOAuthUrl/>">';
// 					 $prevSibling.html(html);
// 					 $.getScript('<dhome:UmtBase/>/js/isLogin.do', function(){
// 						 if(!data.result){
// 							$('body').show();
// 						 }
// 					 });
// 					setTimeout(function(){
// 						$('body').show();
// 					},2000);
// 				 }
// 			 },
// 			 error : function(){
// 				 //console.log("error");
// 			 }
// 		 });
// 	 };
	
	
    $("ul.nav.nav-pills > li > a").click(function(){
		$("#navbar .active").removeClass("active");
		$(this).parent().addClass("active");
	});
	
	$("#memberNavbar > li > a").click(function(){
		$("#memberNavbar .active").removeClass("active");
		$(this).parent().addClass("active");
	});
	
	$("#paperNavbar > li > a").click(function(){
		$("#paperNavbar .active").removeClass("active");
		$(this).parent().addClass("active");
	});
	//.......
	$("#paperNavbar-cite > li > a").click(function(){
		$("#paperNavbar-cite .active").removeClass("active");
		$(this).parent().addClass("active");
	});
	
	
	
	 pie(${educations},educaPie);
	 
	 highlight(${deptPubCounts });
	 highlight2(${deptCites });
	 
	// highlight('papers-year-jq',${yearCites },${yearPubCounts });
/* 	 $("#papers-year-jq").hide();
	 $("#papers-dept-jq").show(); */
	 
	$("a#member-degree").click(function(){
		memberOp ="degree";
		$.get('<dhome:url value="/institution/${domain}/member/degree/"/>'+memberDpetId).done(function(result){
			if(result.success){
			    pie(result.data,educaPie);
			}
		});
	});
	
	$("a#member-title").click(function(){
		memberOp ="title";
		$.get('<dhome:url value="/institution/${domain}/member/title/"/>'+memberDpetId).done(function(result){
			if(result.success){
			    pie(result.data,titlePie);
			}
		});
	});
	$("a#member-age").click(function(){
		memberOp ="age";
		$.get('<dhome:url value="/institution/${domain}/member/age/"/>'+memberDpetId).done(function(result){
			if(result.success){
			    pie(result.data,agePie);
			}
		});
	});
	
	
	 $("a#paper-dept").click(function(){
			/* $("#papers-year-jq").hide();
			$("#papers-dept-jq").show(); */
			 highlight(${deptPubCounts }); 
		});
		
		$("a#paper-year").click(function(){
	/* 		$("#papers-dept-jq").hide();
			$("#papers-year-jq").show(); */
	 		highlight(${yearPubCounts });
	 	}); 
	//...........
	 $("a#paper-dept-cite").click(function(){
		/* $("#papers-year-jq").hide();
		$("#papers-dept-jq").show(); */
		 highlight2(${deptCites }); 
	});
	
	$("a#paper-year-cite").click(function(){
/* 		$("#papers-dept-jq").hide();
		$("#papers-year-jq").show(); */
		highlight2(${yearCites });
	}); 
	
	 
		
		function evalArray(target){
			var array = target.split("]");
			var result = [];
			for(var i=0; i< array.length; i++){
				if(array[i]!=""){
					if(array[i].substring(0,1) == ","){
						array[i] = array[i].substring(1,array[i].length);
					}
					result.push(eval("("+array[i]+"])"));
				}
			}
			return (array.length ==0)?[""]:result;
		}
	$('#pandectMenu').addClass('active');
});
</script>


<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>姓名：{{= author.authorName}}</li>
		<li>邮箱：{{= author.authorEmail}}</li>
		<li>单位：{{= author.authorCompany}}</li>
		{{if author.communicationAuthor}}
			<li>通讯作者：是</li>
		{{/if}}
		{{if author.authorStudent}}
			<li>学生在读：是</li>
		{{/if}}
		{{if author.authorTeacher}}
			<li>第一作者导师：是</li>
		{{/if}}
		{{if home}}
			<li><a href="<dhome:url value="/people/{{= home}}"/>" target="_blank">个人主页</a></li>
		{{/if}}
	</script>
</html>