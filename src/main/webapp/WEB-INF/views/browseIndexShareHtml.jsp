<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
	<div class="bottom-bar">
		 <div class="content side-pannel" id="friend-pad">
			 <div id="showorhide">
			 	<div class="panelTitle"><fmt:message key='browseIndex.recommendScholars.title'/></div>
				<div id="recommending_scholar_list" class="scholar-box">
					<div id="friendList">
						<div id="accessDiv">
						<p><i class="icon-chevron-down"></i><fmt:message key='browseIndex.recommendScholars.access'/> <span style="display:none" id="accessSize">0</span> </p>
						 	<ul class="person-list" id="accessFriends" >	
								
						 	</ul>
					 	</div>
					 	<div id="disciplineDiv">
						 	<p><i class="icon-chevron-down"></i><fmt:message key='browseIndex.recommendScholars.discipline'/> <span  style="display:none"  id="disciplineSize">0</span> </p>
						 	<ul class="person-list" id="disciplineFriends" >
						 	
						 	</ul>
					 	</div>
					 	<div id="institutionDiv">
						 	<p><i class="icon-chevron-down"></i><fmt:message key='browseIndex.recommendScholars.institution'/> <span  style="display:none" id="institutionSize">0</span> </p>
						 	<ul class="person-list" id="institutionFriends" >
						 	
						 	</ul>
					 	</div>
					 	<div id="interestDiv">
						 	<p><i class="icon-chevron-down"></i><fmt:message key='browseIndex.recommendScholars.interest'/><span  style="display:none" id="interestSize">0</span> </p>
						 	<ul class="person-list" id="interestFriends" >
						 	
						 	</ul>
					 	</div>
				 	</div>
				</div>
				<!-- <div id="pdiv" class="scrollbar" style="display:none;">
					<div id="div1"></div>
				</div> -->
				<div class="clear"></div>
			</div>
			<div class="popupwindow panelTitle"> 
			  	<a><fmt:message key='browseIndex.recommendScholars.close'/></a>	
			 </div>
		</div>
		 
	 </div>
	 <script type="text/javascript">
	$(document).ready(
			function() {
				//wunaing
				/* $('#recommending_scholar_list>div').html("<fmt:message key='browseIndex.recommendScholars.title'/>"); */
				
				var pageHeight = document.body.clientHeight;
				var documentHeight = window.screen.height;
				var maxHeight = pageHeight > documentHeight ? pageHeight
						: documentHeight;
				$(".cover-wholepage").css({
					"height" : maxHeight
				});
				$(".cover-wholepage").click(function() {
					$(this).hide();
				});
				$(".popupwindow").click(function() {
					$("#showorhide").toggle();
					if($(".popupwindow a").text() == "<fmt:message key='browseIndex.recommendScholars.close'/>"){
						$(".popupwindow a").text("<fmt:message key='browseIndex.recommendScholars.title'/>");
						$(".popupwindow").css({"background":"#333","box-shadow":"2px 0px 2px #ccc"});
						$(".popupwindow a").css({"color":"#fff"});
					}
					else{
						$(".popupwindow a").text("<fmt:message key='browseIndex.recommendScholars.close'/>");
						$(".popupwindow").css({"background":"#fff"});
						$(".popupwindow a").css({"color":"#666"});
					}
				});
				
				$("a.summary").toggle(function(event){
					$(this).parent().children(".summary-content").slideDown();
					event.preventDefault();
				}, function(event){
					$(this).parent().children(".summary-content").slideUp();
					event.preventDefault();
				});
				function ifSizeZeroThenHide(id){
					if($("#"+id).html()=='0'){
						$("#"+id).parent().parent().hide();
					}
				}
				//获取相似的人
				function getFriends(){
					$.ajax({
						url : '<dhome:url value="/people/${domain}/friend.json"/>?ram='+Math.random(),
						type : 'GET',
						success : function(data){
							if(data!=null&&data!=''){
								$("#institutionSize").html((data.institutionFriends||'').length);
								$("#disciplineSize").html((data.disciplineFriends||'').length);
								$("#interestSize").html((data.interestFriends||'').length);
								$("#accessSize").html(data.accessCount);
								setImage(data.institutionFriends);
								setImage(data.disciplineFriends);
								setImage(data.interestFriends);
								setImage(data.accessFriends);
								ifSizeZeroThenHide("disciplineSize");
								ifSizeZeroThenHide("interestSize");
								ifSizeZeroThenHide("institutionSize");
								if(data.institutionFriends!=null){
									$("#friend-template").render(data.institutionFriends).appendTo("#institutionFriends");
								}if(data.disciplineFriends!=null){
									$("#friend-template").render(data.disciplineFriends).appendTo("#disciplineFriends");
								}if(data.interestFriends!=null){
									$("#friend-template").render(data.interestFriends).appendTo("#interestFriends");
								}if(data.accessFriends){
									$("#friend-template").render(data.accessFriends).appendTo("#accessFriends");
								}
								if($.trim($("#accessFriends").html())==''){
									$("#accessDiv").hide();
								}
							}else{
								$("#friendList").html("<fmt:message key='browseIndex.recommendScholars.none'/>");
							}
						}
					});
				}
				getFriends();
				function setImage(data){
					$(data).each(function(i,n){
						var logoImg='<dhome:img imgId="-999"/>';
						var defaultImg='<dhome:img imgId="0"/>';
						if(n.simpleUser.image=='0'){
							n.imgUrl=defaultImg;
						}else{
							n.imgUrl=logoImg.replace('-999',n.simpleUser.image);
						}
					})
				}
				
				//打开相似的人的边栏
				function openPanel(){
					$("#showorhide").show();
					$(".popupwindow a").text("<fmt:message key='browseIndex.recommendScholars.close'/>");
					$(".popupwindow").css({"background":"#fff"});
					$(".popupwindow a").css({"color":"#666"});
				}
				//关闭相似的人的边栏
				function closePanel(){
					$("#showorhide").hide();
					$(".popupwindow a").text("<fmt:message key='browseIndex.recommendScholars.title'/>");
					$(".popupwindow").css({"background":"#333","box-shadow":"2px 0px 2px #ccc"});
					$(".popupwindow a").css({"color":"#fff"});
				}
				var winWidth=0;
				var winHeight=0;
				var divWidth=$("#homepagebody").width();
				var friendWidth=$("#friend-pad").width();
				
				//计算是否需要关闭边栏
				function needOpen(){
					var clientHeight = document.documentElement.clientHeight;
					$(".side-pannel #friendList").css({"max-height":clientHeight-130,"overflow-y":"auto","top":0,"width":"150px"});
					$(".scholar-box").css({"max-height":clientHeight - 140,"top":0});
					//获取窗口宽度
					if (window.innerWidth){
						winWidth = window.innerWidth;
					}else if ((document.body) && (document.body.clientWidth)){
						winWidth = document.body.clientWidth;
					}
					//获取窗口高度
					if (window.innerHeight){
						winHeight = window.innerHeight;
					}else if ((document.body) && (document.body.clientHeight)){
						winHeight = document.body.clientHeight;
					}
					//通过深入Document内部对body进行检测，获取窗口大小
					if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth){
						winHeight = document.documentElement.clientHeight;
						winWidth = document.documentElement.clientWidth;
					}
					//调用开/关
					if((winWidth-divWidth)/2>friendWidth){
						openPanel();
					}else{
						closePanel();
					}
					/* scrollBar(); */
				}
				needOpen();
				window.onresize=needOpen;
				
				$("span.tooltipName").hover(function(){
					$(this).tooltip('toggle');
				});
				
				var cjq = setInterval(function(){
					var clientHeight = document.documentElement.clientHeight;
					var listHeight = $("#friendList").outerHeight();
					var h = 0.2 * listHeight;
				    $("#div1").css({"height":h});
					$(".scrollbar").css({"height":listHeight,"top":"0px"});
					$(".scholar-box").css({"height":listHeight});
					var oheight = $(".scholar-box").outerHeight();
					var omaxheight = clientHeight - 140;
					if(oheight >= omaxheight){
						$("#pdiv").show();
					}
					else {
						$("#pdiv").hide();
					}
				}, 20);
				
				/* window.onload = scrollBar;
				function scrollBar(){
				    var oDiv=document.getElementById("div1");
				    var oPDiv=document.getElementById("pdiv");
				    var oDivp=document.getElementById("friendList");
				    var startY=startoDivTop=0;
				    oDiv.style.top= "0px";
				    oDiv.onmousedown=function (e)
				    {
				        var e=e||window.event;
				        startY=e.clientY;
				        startoDivTop=oDiv.offsetTop;
				        if (oDiv.setCapture)
				        {
				            oDiv.onmousemove=doDarg;
				            oDiv.onmouseup=stopDarg;
				            oDiv.setCapture();
				        }
				        else
				        {
				            document.addEventListener("mousemove",doDarg,true);
				            document.addEventListener("mouseup",stopDarg,true);
				        }
				        function doDarg(e)
				        {
				            var e=e||window.event;
				            var t=e.clientY-startY+startoDivTop;
				            if (t<0)
				            {
				                t=0;
				            }
				            else if (t>oPDiv.offsetHeight-oDiv.offsetHeight)
				            {
				                t=oPDiv.offsetHeight-oDiv.offsetHeight;
				            }
				            var per = Math.ceil(t/(oPDiv.offsetHeight-oDiv.offsetHeight)*100)+"%";
				            var n = Math.ceil(t/(oPDiv.offsetHeight-oDiv.offsetHeight) * 100);
				            var c= n/100;
				            var num = $("#friendList").find("li").length;
				            var height = num * 40;
				            var bake = height - $("#friendList").outerHeight();
				            var m = bake > c *height ? c *height : bake;
				            oDiv.style.top= t + "px";
				            oDivp.style.top= - m +"px";
				        }
				        function stopDarg()
				        {
				            if (oDiv.releaseCapture)
				            {
				                oDiv.onmousemove=doDarg;
				                oDiv.onmouseup=stopDarg;
				                oDiv.releaseCapture();
				            }
				            else
				            {
				                document.removeEventListener("mousemove",doDarg,true);
				                document.removeEventListener("mouseup",stopDarg,true);
				            }
				            oDiv.onmousemove=null;
				            oDiv.onmouseup=null;
				        }
				    }
			} */
			
			// open or close the list
			$("#friendList p").click(function(){
				$(this).children("i").toggleClass("icon-chevron-right");
				$(this).next("ul.person-list").toggle();
			})
			});
</script>
<script type="text/html" id="friend-template">
		<li>
		<div class="person-img">
		<a target="_blank" href="<dhome:url value='/people/{{= domain}}'/>"><img src="{{= imgUrl}}"/></a>
		</div>
		<div class="person-info">
		<a target="_blank" href="<dhome:url value='/people/{{= domain}}'/>" title="{{= simpleUser.zhName}}">{{= simpleUser.zhName}}</a>
		</div>
		<div class="clear"></div>
		</li>
</script>