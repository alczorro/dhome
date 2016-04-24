<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title><fmt:message key='common.paper.title'/>-${titleUser.zhName }<fmt:message key='common.index.title'/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">

  <jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${name}<fmt:message key='common.index.title'/><jsp:include page="showAudit.jsp"/></div>
		<jsp:include page="./commonMenu.jsp">
			<jsp:param name="activeItem" value="content" />
		</jsp:include>
	</div>
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title"><h3><fmt:message key='admin.common.content.title'/></h3></div>
			<jsp:include page="adminCommonLeft.jsp"></jsp:include>
		    <div class="span9 left-b">
		    	<div id="mainSpan">
				    <div class="page-header">
					    <h3><fmt:message key='msg.board'/>
					    <span class="publish">							   		
					    	<c:if test="${status=='hide' }">
							（<fmt:message key='common.publish.notdo'/><a class="btn btn-mini" href="<dhome:url value="/people/${domain}/admin/msgboard/changeStatus"/>"><fmt:message key='common.publish.do'/></a>）
							</c:if>
							<c:if test="${status=='show' }">
							（<fmt:message key='common.publish.done'/><a class="btn btn-mini" href="<dhome:url value="/people/${domain}/admin/msgboard/changeStatus"/>"><fmt:message key='common.publish.undo'/></a>）
							</c:if>
							<span class="msgInfo"><fmt:message key='msg.board.hint'/>${currentUser.email }</span>
						</span>
						</h3>
					</div>
					<p class="allMsg">
						<fmt:message key='msg.board.all'/>
			    		<span class="openMsg"><input type="checkbox" id="changeOpenOrClose" ${boardSetting.isMsgBoardOpen()?'checked="checked"':'' }> <fmt:message key='msg.board.open'/></span>
					</p>			    		
				    <c:choose>
				    <c:when test="${!empty msgs}">
				    <div class="page-container">
					    <ul class="msgListShow">
					    	<c:forEach items="${msgs}" var="msg">
					    		<li id="comment_${msg.comment.id }">
					    			<div class="left">
							    		<a target="_blank" href="${context}/people/${msg.comment.domain}" >
							    			<img src="<dhome:img imgId="${msg.comment.image}"/>" class="msgHeader">
							    		</a>
							    	</div>
							    	<div class="right">
						    			<p class="msgTitle">
						    				<a href="${context}/people/${msg.comment.domain}" target="_blank" class="msgUser">${msg.comment.name}</a>
						    				<span class="msgTime"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${msg.comment.commentTime }" /></span>
							    			<a class="deleteComment" data-comment-id="${msg.comment.id }"><fmt:message key="common.delete"/></a>
							    		</p>
						    			<p class="msgContent">
						    			 	${msg.comment.content } 
						    			</p>
						    			<i class="reply-content"></i>
				    			 		<ul id="replays_${msg.comment.id }" class="msgReply">
				    			 			<c:forEach items="${msg.replys }" var="reply">
				    			 				<li id="reply_${reply.id }">
				    			 					<p class="msgTitle">
						    			 				<a target="_blank" href="${context}/people/${reply.replyDomain }">${reply.replyUserName }</a>
						    			 				<span class="msgTime"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${reply.replyTime }" /></span>
						    			 				<a class="deleteReply" data-comment-id="${msg.comment.id }" data-reply-id="${reply.id }"><fmt:message key="common.delete"/></a>
						    			 			</p>
						    			 			<p class="msgContent">
						    			 				${reply.replyContent}
						    			 			</p>
						    			 		</li>
				    			 			</c:forEach>
				    			 			<li class="msgReplyArea">
							    			 	<textarea maxlength="200" id="replyContent_${msg.comment.id }" class="replyText"></textarea>
							    			 	<input data-host-uid="${msg.comment.commentHostUid }"  data-comment-id="${msg.comment.id }" type="button" class="replyBtn btn btn-small btn-primary" value="<fmt:message key='msg.board.reply'/>"/>
							    			 	<span style="color:red;display:none"><fmt:message key='msg.validate.frequent'/></span>
							    			</li>
				    			 		</ul>
				    				</div>
				    			</li>
				        	</c:forEach>
				    	</ul>  
				    </div>
				    </c:when>
				    <c:otherwise>
				    	<div class="page-container">
				    		<p class="msgNotLogin">
				       			<fmt:message key="msg.board.no.message"/>
				       		</p>
				      	</div>
				    </c:otherwise>
				    </c:choose>
		   	 	</div>
		    </div>
		</div>
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<%
	request.setAttribute("context",request.getContextPath());
%>
<script type="text/javascript">
	$(document).ready(function(){
		//格式化日期
		function formatDate(longTime){
			var a=new Date(longTime);
			function paddingZero(length,num){
				var need=length-(num+'').length
				var result='';
				for(var i=0;i<need;i++){
					result+='0';
				}
				return result+num;
			}
			return paddingZero(4,1900+a.getYear())+'-'+paddingZero(2,(a.getMonth()+1))+'-'+paddingZero(2,a.getDate())+' '+paddingZero(2,a.getHours())+':'+paddingZero(2,a.getMinutes())+':'+paddingZero(2,a.getSeconds());
		}
		//提交回复
		$('.replyBtn').on('click',function(){
			var data=$(this).data();
			var textarea=$('#replyContent_'+data.commentId);
			var content=$.trim(textarea.val());
			var $self=$(this)
			data.content=content;
			$.post('${context}/people/${domain}/admin/msgboard/commentReply',data).done(function(obj){
				if(obj.id<1){
					$self.next().show().delay(1000).fadeOut(1000);
					return;
				}
				obj.replyTime=formatDate(obj.replyTime);
				$('#replays_'+data.commentId+' .msgReplyArea').before($('#replyCommentTmpl').render(obj));
				textarea.val('');
			});
		});
		//删除回复
		$('.deleteReply').live('click',function(){
			var self=this;
			var data=$(this).data();
			if(confirm('<fmt:message key="msg.board.delete"/>')){
			$.post('${context}/people/${domain}/admin/msgboard/deleteReply',data).done(function(){
				$(self).parent().parent().remove();
			});
			}
		});
		//删除留言
		$('.deleteComment').live('click',function(){
			var self=this;
			var data=$(this).data();
			if(confirm('<fmt:message key="msg.board.delete"/>')){
			$.post('${context}/people/${domain}/admin/msgboard/deleteComment',data).done(function(){
				$(self).parent().parent().parent().remove();  
			});
			}
		});
		//是否公开所有留言
		$('#changeOpenOrClose').on('click',function(){
			var data;
			if($(this).attr('checked')){
				data=true;
			}else{
				data=false;  
			}
			$.post('${context}/people/${domain}/admin/msgboard/changeOpenOrClose',{flag:data});
		});
	});
</script>
<script id="replyCommentTmpl" type="text/html">
<li id="reply_{{= id }}">
	<p class="msgTitle">
		<a href="${context}/people/{{= replyDomain}}" target="_blank">{{= replyUserName }}</a>
		<span class="msgTime">{{= replyTime }}</span>
		<a class="deleteReply" data-comment-id="{{= commentId }}" data-reply-id="{{= id }}"><fmt:message key="common.delete"/></a>
	</p>
	<p class="msgContent">
		{{= replyContent }}
	</p>
</li>
</script>
</html>