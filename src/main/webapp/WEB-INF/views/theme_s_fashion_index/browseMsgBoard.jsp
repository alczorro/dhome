<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

	<div class="namecard-skin" id="homepagebody">
		<div class="row-fluid mini-layout center">
		    <div id="mainSpan">
			    <div class="page-container">
				    	
				   <c:choose>
				    	<c:when test="${empty currentUser}">
				    		<p class="msgNotLogin"><fmt:message key='msg.guest.no.login'/></p>
				    	</c:when>
				    	<c:otherwise>
				    		<div id="history" style="${type=='history'?'':'display:none'}">
				    			<c:if test="${isSelf }">
					    			<p class="msgNotLogin"><fmt:message key="msg.guest.self.hint"/></p>
					    		</c:if>
					    		<c:if test="${!isSelf }">
						    		<p class="msgBoard">
					    				<a id="returnComment" href="index.html?type=comment" class="btn btn-mini btn-success"><fmt:message key='msg.guest.comment'/></a>
								    </p>
					    		</c:if>
					    		<c:choose>
					    			<c:when test="${empty msgs }">
							    		<p class="msgNotLogin"><fmt:message key="msg.board.no.message"/></p>
							    	</c:when>
							    	<c:otherwise>
							    		 <ul class="msgListShow">
									    	<c:forEach items="${msgs}" var="msg">
									    		<li id="comment_${msg.comment.id }">
									    			<div class="left">  
									    				<a target="_blank" href="${context}/people/${msg.comment.domain}" >
											    			<img src="<dhome:img imgId="${msg.comment.image}"/>" class="msgHeader" href="">
											    		</a>
											    	</div>
											    	<div class="right">
										    			<p class="msgTitle">
										    				<a target="_blank"  href="${context}/people/${msg.comment.domain}" class="msgUser">${msg.comment.name}</a>
										    				<span class="msgTime"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${msg.comment.commentTime }" /></span>
											    			<c:if test="${msg.comment.uid==currentUser.id }">
											    				<a class="deleteComment" data-comment-id="${msg.comment.id }"><fmt:message key='common.delete'/></a>
											    			</c:if>
											    		</p>
										    			<p class="msgContent">
										    			 	${msg.comment.content } 
										    			</p>
										    			<c:if test="${msg.comment.uid==currentUser.id||!empty msg.replys }">
										    			<i class="reply-content"></i>
									    			 	<ul id="replays_${msg.comment.id }" class="msgReply">
									    			 	<c:forEach items="${msg.replys }" var="reply">
									    			 		<li id="reply_${reply.id }">
									    			 			<p class="msgTitle">
									    			 				<a target="_blank" href="${context}/people/${reply.replyDomain }">${reply.replyUserName }</a>
									    			 				<span class="msgTime"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${reply.replyTime }" /></span>
									    			 				<c:if test="${reply.replyUid==currentUser.id }">
										    			 				<a class="deleteReply" data-comment-id="${msg.comment.id }" data-reply-id="${reply.id }"><fmt:message key='common.delete'/></a>
										    			 			</c:if>  
									    			 			</p>
									    			 			<p class="msgContent">
									    			 				${reply.replyContent}
									    			 			</p>
									    			 		</li>
									    			 	</c:forEach>
										    			<c:if test="${msg.comment.uid==currentUser.id }">
										    			 	<li class="msgReplyArea">
											    			 	<textarea maxlength="200"  id="replyContent_${msg.comment.id }" class="replyText"></textarea>
											    			 	<input data-host-uid="${msg.comment.commentHostUid }"  data-comment-id="${msg.comment.id }" type="button" class="replyBtn btn btn-small btn-primary" value="<fmt:message key='msg.board.reply'/>"/>
											    			 	<span style="color:red;display:none"><fmt:message key='msg.validate.frequent'/></span>
											    			</li>
										    			</c:if>
									    			 </ul>
									    			 </c:if>
									    			</div>
									    		</li>
									        </c:forEach>
							    		</ul>
							    	</c:otherwise>
					    		</c:choose>
						    </div>
						    <div id="comment" style="${empty type||type=='comment'?'':'display:none'}">
						    	<p class="msgBoard"><fmt:message key='msg.guest.comment.content'/>
							    	<a id="showHistory" href="index.html?type=history" class="btn btn-small btn-link"><fmt:message key='msg.guest.comment.history'/></a>
							    </p>
					    		<textarea maxlength="200"  id="commentContent" class="msgBoardText"></textarea>
					    		<p class="msgBoard"><fmt:message key='msg.guest.commenter'/></p>
					    		<p>${currentUser.zhName }<span class="hint"><fmt:message key='msg.guest.comment.hint'/></span></p>
					    		<input id="submitContent" type="button" value="<fmt:message key='common.submit'/>" class="btn btn-primary">
					    		<span style="color:red;display:none" id="operationMsg"></span>
				    		</div>
				    	</c:otherwise>
				   </c:choose>
				</div>
		    </div>
		</div>
	</div>
	<jsp:include page="../commonfooter.jsp"></jsp:include>
<script>
	$(document).ready(function(){
		//绑定提交事件
		$('#submitContent').on('click',function(){
			var textarea=$('#commentContent');
			var content=textarea.val();  
			$.post('comment',{'content':content}).done(function(data){
				if(data=='null'){
					showMsg('<fmt:message key="msg.validate.required"/>');
				}else if(data=='hide'){
					showMsg('<fmt:message key="msg.validate.hided"/>');
				}else if(data=='self'){
					showMsg('<fmt:message key="msg.validate.self"/>');
				}else if(data=='success'){
					showMsg('<fmt:message key="msg.validate.success"/>');
				}else if(data='frequent'){
					showMsg('<fmt:message key="msg.validate.frequent"/>');
					return;
				}else{
					showMsg(data);
				}
				textarea.val('');
			});
		});
		//操作提示消息
		function showMsg(msg){
			var $msgSpan=$('#operationMsg');
			$msgSpan.html(msg);
			$msgSpan.show();
			setTimeout(function(){
				$('#operationMsg').hide();
			},2000);
		}
		//提交回复
		$('.replyBtn').on('click',function(){
			var data=$(this).data();
			var textarea=$('#replyContent_'+data.commentId);
			var content=$.trim(textarea.val());
			var $self=$(this);
			data.content=content;
			$.post('commentReply',data).done(function(obj){
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
			$.post('deleteReply',data).done(function(){
				$(self).parent().parent().remove();
			});
			}
		});  
		//删除留言
		$('.deleteComment').live('click',function(){
			var self=this;
			var data=$(this).data();
			$.post('deleteComment',data).done(function(){
				$(self).parent().parent().parent().remove();
			});
		});
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
		if('${needHash}'=='true'){
			//定位hash
			window.location.hash='${type}_md';
		}
	});
</script>
<script id="replyCommentTmpl" type="text/html">
<li id="reply_{{= id }}">
	<p class="msgTitle">
		<a target="_blank" href="${context}/people/{{= replyDomain }}">{{= replyUserName }}</a>
		<span class="msgTime">{{= replyTime }}</span>
		<a class="deleteReply" data-comment-id="{{= commentId }}" data-reply-id="{{= id }}"><fmt:message key='common.delete'/></a>
	</p>
	<p class="msgContent">  
		{{= replyContent }}
	</p>
</li>
</script>
</html>