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
%>
<html lang="en">
<head>
<title><fmt:message key="institute.common.scholarEvent" />-${institution.name }-<fmt:message
		key="institueIndex.common.title.suffix" /></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
</head>
<body class="dHome-body institu" data-offset="50" data-target=".subnav"
	data-spy="scroll">
	<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>

	<div class="container">
		<jsp:include page="../leftMenu.jsp"></jsp:include>
		<div class="ins_backend_rightContent">
			 
<!-- 			<ul id="navbar" class="nav nav-pills scholar d-nobottom" style="margin-left: 5px;margin-top:10px;margin-bottom: 10px;"> -->
<!-- 				<li class="dropdown" id="menuDept"> -->
<!-- 					<a id="dept" class="dropdown-toggle" data-toggle="dropdown" href="#menu1" style="display:inline-block; float:left;">按部门<b class="caret"></b> -->
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
<%-- 					  <c:forEach begin="1999" end="${now}" var="year"> --%>
<%-- 							<li><a href="javascript:selectYear('${now - year + 1999 }')">${now - year + 1999 }</a></li> --%>
<%-- 					  </c:forEach>  --%>
<!-- 					</ul> -->
<!-- 				</li> -->
<!-- 		    </ul> -->
<!-- 		     <ul class="clearfix pandect"> -->
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
<jsp:include page="../../commonheader.jsp"></jsp:include>
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
		
		$.get('<dhome:url value="/institution/${domain}/backend/pandect/count/"/>'+deptmentId+'/'+data).done(function(result){
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
		
		$.get('<dhome:url value="/institution/${domain}/backend/pandect/count/"/>'+deptmentId+'/'+year).done(function(result){
			if(result.success){
				renderCount(result.data);
			}
		});
	}
	
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
		
		$.get('<dhome:url value="/institution/${domain}/backend/pandect/member/"/>'+memberOp+'/'+id).done(function(result){
			if(result.success){
			    pie(result.data,pieTitle);
			}
		});
	}
	
	function renderCount(data){
		for(var key in data){
			$("#"+key+" "+"span").html(data[key]);
		}
	};
	
	
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
		$.get('<dhome:url value="/institution/${domain}/backend/pandect/member/degree/"/>'+memberDpetId).done(function(result){
			if(result.success){
			    pie(result.data,educaPie);
			}
		});
	});
	
	$("a#member-title").click(function(){
		memberOp ="title";
		$.get('<dhome:url value="/institution/${domain}/backend/pandect/member/title/"/>'+memberDpetId).done(function(result){
			if(result.success){
			    pie(result.data,titlePie);
			}
		});
	});
	$("a#member-age").click(function(){
		memberOp ="age";
		$.get('<dhome:url value="/institution/${domain}/backend/pandect/member/age/"/>'+memberDpetId).done(function(result){
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