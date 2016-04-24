<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<html lang="en">
	<head>
		<title><fmt:message key="institute.common.scholarEvent"/>-${institution.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
		<meta name="description" content="dHome" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
	</head>

	<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
		<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
		<div class="container">
			<jsp:include page="../leftMenu.jsp"></jsp:include>
			<div class="ins_backend_rightContent">
				<ul class="nav nav-tabs">
				<jsp:include page="../../commonTopicBackend.jsp"><jsp:param name="activeItem" value="topics" /> </jsp:include>
<!-- 					<li>&nbsp;&nbsp;&nbsp;</li> -->
<!-- 					<li class="active"> -->
<%-- 					    <a href="#">课题 ${page.allSize }</a> --%>
<!-- 					</li> -->
<!-- 					<li> -->
<%-- 					    <a href="<dhome:url value="/institution/${domain }/backend/topic/create"/>">+ 添加课题</a> --%>
<!-- 				    </li> -->
<!-- 				   	<li> -->
<!-- 					    <a href="#">批量导入</a> -->
<!-- 				    </li> -->
				    <li class="search">
				    	<form class="bs-docs-example form-search">
				            <input id='topicKeyword' type="text" placeholder="请输入课题名称" class="input-medium search-query" value="<c:out value="${condition.keyword }"/>"/>
				            <button id="searchTopicBtn" type="button" class="btn">搜索</button>
				        </form>
				    </li>
				    
				</ul>
				<div class="batch">
					 <ul class="funds" id="fundsCond">
					<li data-funds="0" <c:if test="${condition.fundsFrom==0 }">class="active"</c:if>>
						<a><strong>全部</strong></a>
					</li>
					<c:forEach items="${fundsFromMap.entrySet() }" var="entry">
						<li data-funds="${entry.key }" <c:if test="${condition.fundsFrom == entry.key}">class="active"</c:if>>
							<a >  
								<span class="fundsFrom">
								${fundsFroms[entry.key].val}
								</span> 
								<span class="count">(${entry.value })</span>
							</a>
						</li>
					</c:forEach>
					</ul>
					
					<select id="batchOperation">
						<option>--批量操作--</option>
						<option value="batchDelete">删除</option>
					</select>
					<input type="button" id="batchBtn" value="确认" class="btn btn-primary"/>
<%-- 					<a href="<dhome:url value="/institution/${domain }/backend/topic/create"/>">添加课题</a> --%>
				</div>
				<ul class="listShow topic">
					<li class="title">
						<span class="check"><input type="checkbox"  id="checkAll"/></span>
<!-- 						<span class="num">编号</span>  -->
						<span class="name">课题名称</span>
<!-- 						<span class="beginTime">开始<br>时间</span> -->
<!-- 						<span class="endTime">结束<br>时间</span>  -->
						<span class="source">资金来源</span>
						<span class="type">类别</span>
						<span class="member">课题组成员</span>
<!-- 						<span class="proFunds">项目经费</span> -->
<!-- 						<span class="selfFunds">个人经费</span> -->
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<span class="check"><input data-id="${data.id }" type="checkbox" class="checkUser"/></span>
<!-- 							<span class="num"> -->
<%-- 								<c:choose> --%>
<%-- 									<c:when test="${ empty data.topic_no }"> --%>
<!-- 											--    -->
<%-- 									</c:when> --%>
<%-- 									<c:otherwise> --%>
<%-- 										<c:out value="${data.topic_no }"/> --%>
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>
<!-- 							</span> -->
							<span class="name">
								<c:choose>
									<c:when test="${empty data.name }">
											--   
									</c:when>
									<c:otherwise>
										<c:out value="${data.name }"/>
									</c:otherwise>
								</c:choose>
								<span class="manage">
									<a class="label label-success" href="<dhome:url value="../detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a>
									<a class="label label-success" href="<dhome:url value="/institution/${domain}/backend/topic/edit/${data.id }?returnPage=${page.currentPage }"/>">编辑</a>
									<a class="label label-danger deleteTopic" href="<dhome:url value="../delete?id[]=${data.id }&page=${page.currentPage }"/>">删除</a>
								</span>
							</span> 
<!-- 							<span class="beginTime"> -->
<%-- 										<c:out value="${data.start_year }-${data.start_month }"/> --%>
<!-- 							</span> -->
<!-- 							<span class="endTime"> -->
<%-- 										<c:out value="${ data.end_year }-${data.end_month }"/> --%>
<!-- 							</span>  -->
							
							<span class="source">
								<c:choose>
									<c:when test="${data.funds_from==0 }">
											--   
									</c:when>
									<c:otherwise>
										${fundsFroms[data.funds_from].val}
									</c:otherwise>
								</c:choose>
							</span>
							<span class="type">
								<c:choose>
									<c:when test="${data.type==0 }">
											--   
									</c:when>
									<c:otherwise>
										${types[data.type].val}
									</c:otherwise>
								</c:choose>
							</span>
							
							<span class="member">
								<c:choose>
									<c:when test="${empty authorMap[data.id]}">
									--
									</c:when>
									<c:otherwise>
										<ul>
										<c:forEach items="${ authorMap[data.id]}" var="author">
											<c:choose>
												<c:when test="${author.authorType eq 'admin' }">
													<li >
													<a class="authorDetailPopover"  data-topic-id="${data.id }" data-author-id="${author.id }" ><c:out value="${author.authorName }"/></a>
														<sup class="main">[负责人]</sup>
													</li>
												</c:when>
											</c:choose>
										</c:forEach>
										<c:forEach items="${ authorMap[data.id]}" var="author">
											<c:choose>
												<c:when test="${author.authorType eq 'member' }">
													<li >
													<a class="authorDetailPopover"  data-topic-id="${data.id }" data-author-id="${author.id }" ><c:out value="${author.authorName }"/></a>
<!-- 														<sup>[参与者]</sup> -->
													</li>
												</c:when>
											</c:choose>
										</c:forEach>
										</ul>
									</c:otherwise>
								</c:choose>
							</span> 
<!-- 							<span class="proFunds"> -->
<%-- 								<c:choose> --%>
<%-- 									<c:when test="${data.project_cost ==0 }"> --%>
<!-- 											--    -->
<%-- 									</c:when> --%>
<%-- 									<c:otherwise> --%>
<%-- 										<c:out value="${data.project_cost }"/> --%>
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>
<!-- 							</span> -->
<!-- 							<span class="selfFunds"> -->
<%-- 								<c:choose> --%>
<%-- 									<c:when test="${data.personal_cost ==0 }"> --%>
<!-- 											--    -->
<%-- 									</c:when> --%>
<%-- 									<c:otherwise> --%>
<%-- 										<c:out value="${data.personal_cost }"/> --%>
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>
<!-- 							</span>  -->
									
						</li> 
					</c:forEach>
				</ul>
				<c:choose>
					<c:when test="${page.maxPage!=0 }">	
						<div class="pages">
							<c:choose>
							   <c:when test="${page.currentPage== 1 }">
							      <a class="disable" >首页</a>
							   </c:when>
							   <c:otherwise>
						          <a class="first">首页</a>
					           </c:otherwise>
							</c:choose>
							
							<c:if test="${page.currentPage!=1 }">
								<a class="prev"><i class="prev-triggle"></i>上一页</a>
							</c:if>
							<span id="pageNav"> </span>
							<c:if test="${page.currentPage!=page.maxPage }">
								<a class="next" >下一页<i class="next-triggle"></i></a>
							</c:if>
							
							<c:choose>
							   <c:when test="${page.currentPage==page.maxPage }">
							      <a class="disable" >尾页</a>
							   </c:when>
							   <c:otherwise>
						          <a class="last">尾页</a>
					           </c:otherwise>
							</c:choose>
						</div>
					</c:when>
					<c:otherwise>
						<p class="msg">没有匹配记录</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
<!-- <!-- 		编辑信息框   			--> -->
<!-- 		<div id="edit-popup" tabindex="-1" class="modal hide fade" style="width:750px;"> -->
<!-- 		<div class="modal-header"> -->
<!--            <button type="button" class="close" data-dismiss="modal">×</button> -->
<!--            <h3>更改课题信息</h3> -->
<!--         </div> -->
<%--        	<form id="editForm" class="form-horizontal nomargin" method="post" action="<dhome:url value="/institution/backend/topic/update/${topic.id }"/>"> --%>
<!-- 		<fieldset> -->
<!-- 			<input type="hidden" id="id" name="id" value=""/> -->
<!-- 			<div class="modal-body"> -->
<!-- 				<div class="control-group"> -->
<!--          			<label class="control-label">开始年份：</label> -->
<!--           			<div class="controls"> -->
<!--             			<select id="start_year" name="start_year"> -->
<!-- 						</select> -->
<!--           			</div> -->
<!--         		</div> -->
<!-- 				<div class="control-group"> -->
<!--          			<label class="control-label">开始月份：</label> -->
<!--           			<div class="controls"> -->
<!--             			<select id="start_month" name="start_month"> -->
<!-- 						<option value="0">--</option> -->
<!-- 						</select> -->
<!--           			</div> -->
<!--           		</div> -->
<!--           		<div class="control-group"> -->
<!--          			<label class="control-label">结束年份：</label> -->
<!--           			<div class="controls"> -->
<!--             			<select id="end_year" name="end_year"> -->
<!-- 						</select> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">结束月份：</label> -->
<!--           			<div class="controls"> -->
<!--             			<select id="end_month" name="end_month"> -->
<!-- 						<option value="0">--</option> -->
<!-- 						</select> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">课题名称：</label> -->
<!--           			<div class="controls"> -->
<!--             			<input maxlength="254" type="text" id="name" name="name" value='' /> -->
<!--             			<span id="name_error_place" class="error"></span> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">资金来源：</label> -->
<!--           			<div class="controls"> -->
<!--             			<input maxlength="254" type="text" id="funds_from" name="funds_from" value='' /> -->
<!--           				<span id="funds_from_error_place" class="error"></span> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">类别：</label> -->
<!--           			<div class="controls"> -->
<!--           				<select name="type" id="type"> -->
<!--           					<option value="provincial">省级</option> -->
<!--           					<option value="ministerial">部级</option> -->
<!--           					<option value="other">其他</option> -->
<!--           				</select> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">课题编号：</label> -->
<!--           			<div class="controls"> -->
<!--             			<input maxlength="250" type="text" name="topic_no" id="topic_no" value='' /> -->
<!--           				<span id="topic_no_error_place" class="error"></span> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">本人角色：</label> -->
<!--           			<div class="controls"> -->
<!--           				<select name="role" id="role"> -->
<!--           					<option value="admin">负责人</option> -->
<!--           					<option value="member">参与人</option> -->
<!--           				</select> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">项目经费：</label> -->
<!--           			<div class="controls"> -->
<!--           				<input maxlength="250" type="text" name="project_cost" id="project_cost" value='' /> -->
<!--           				<span id="project_cost_error_place" class="error"></span> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">个人经费：</label> -->
<!--           			<div class="controls"> -->
<!--             			<input maxlength="250" type="text" name="personal_cost" id="personal_cost" value='' /> -->
<!--           				<span id="personal_cost_error_place" class="error"></span>	 -->
<!--           			</div> -->
<!--         		</div> -->
<!-- 			</div> -->
<!-- 			<div class="modal-footer"> -->
<%-- 				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a> --%>
<%-- 				<button type="button" id="saveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button> --%>
<!-- 	        </div> -->
<!-- 	        </div> -->
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> <script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){
			$.fn.extend({ 
				enter: function (callBack) {
				    $(this).keydown(function(event){
				    	if(event.keyCode=='13'){
				    		callBack.apply(event.currentTarget,arguments);
				    		event.preventDefault();
							event.stopPropagation();
				    	}
				    });
				}
			});

			//初始化时间控件
			$("#citationQueryTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});
			//设置开始年份
			(function(before){
				var year=new Date().getFullYear();
				for(var i=year-before-1;i<=year;i++){
					$('#start_year').append('<option value="'+i+'">'+i+'</option')
				}
				$('#start_year').val(year);
			})(15);
			//开始月份
			(function(loop){
				for(var i=1;i<loop+1;i++){
					$('#start_month').append('<option value="'+i+'">'+i+'</option');
				}
			})(12); 
			//设置结束年份
			(function(before){
				var year=new Date().getFullYear();
				for(var i=year-before-1;i<=year;i++){
					$('#end_year').append('<option value="'+i+'">'+i+'</option')
				}
				$('#end_year').val(year);
			})(15);
			//结束月份
			(function(loop){
				for(var i=1;i<loop+1;i++){
					$('#end_month').append('<option value="'+i+'">'+i+'</option');
				}
			})(12); 
			
			$('.deleteTopic').on('click',function(){
				return (confirm("课题删除以后不可恢复，确认删除吗？"));
			});
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='';
					$('.checkUser:checked').each(function(i,n){
						url+="&id[]="+$(n).data('id');
					});
					if(url==''){
						alert('请选择要删除的课题');
						return;
					}
					if(confirm("课题删除以后不可恢复，确认删除吗？")){
						window.location.href="../delete?page=${page.currentPage}"+url;
					}
					
				}else{
					alert('请选择批量操作'); 
				}
			});
			
			checkAllBox('checkAll','checkUser');
			
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				/* window.location.href=page; */
				search(page);
			});
			
			$('#topicMenu').addClass('active');
			//编辑课题Dialog，前台验证
			$('#editForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 topic_no:{
						 required:true
						 },
					name:{
						 required:true
						 },
				 	
				 	funds_from:{
				 		required:true
				 	},
				 	project_cost:{
				 		required:true,
				 		min:0,
				 		digits:true,
				 		max:99999999
				 	},
				 	personal_cost:{
				 		required:true,
				 		min:0,
				 		digits:true,
				 		max:99999999
				 	}
				 },
				 messages:{
					 topic_no:{
						 required:'编号不允许为空'
						 },
						 name:{
							 required:'名字不允许为空'
						 },
						 funds_from:{
					 		required:'资金来源不允许为空'
					 	},
					 	project_cost:{
					 		required:'项目经费不允许为空',
					 		min:'超出允许值',
					 		digits:'必须为正整数',
					 		max:'超出允许值'
					 	},
					 	personal_cost:{
					 		required:'个人经费不允许为空',
					 		min:'超出允许值',
					 		digits:'必须为正整数',
					 		max:'超出允许值'
					 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
				baseUrl+="&fundsFrom="+$('#fundsCond li.active').data('funds');
				baseUrl+="&keyword="+encodeURIComponent($.trim($('#topicKeyword').val()));
				window.location.href=baseUrl;
			}
			
			$('#fundsCond li').on('click',function(){
				$('#fundsCond li').removeClass('active');
				$(this).addClass('active');
				search(1);
			});
			
			$('#searchTopicBtn').on('click',function(){
				search(1);
			});
			$('#topicKeyword').enter(function(){
				search(1);
			});
			
			//点击参与者查看详情
			$('.authorDetailPopover').on('click',function(){
				$('.authorDetailPopover').popover('destroy');
				var authorId=$(this).data('authorId');
				if($('#popover_'+authorId).size()==0){
					var $self=$(this);
					var data=$(this).data();
					var topicId=data.topicId;
					var authorId=data.authorId;
					$.get('<dhome:url value="/institution/${domain}/backend/topic/author/"/>'+topicId+"/"+authorId).done(function(result){
						if(result.success){
							$self.popover({
								content:$('#authorTemplate').render(result.data),
								html:true,
								placement:'right',
								template:'<div id="popover_'+authorId+'" class="popover"><div class="arrow"></div><div class="popover-inner"><div class="popover-content" id="popover_content_'+authorId+'"><ul class="popAuthor""></ul></div></div></div>',
								trigger: "manual"	
							});
							$self.popover('show');
						
						}else{
							alert(result.desc);
						}
					});
				}else{
					$(this).popover('hide'); 
				}
			});
			
		});
	</script>
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>姓名：{{= author.authorName}}</li>
		<li>邮箱：{{= author.authorEmail}}</li>
		<li>单位：{{= author.authorCompany}}</li>
		<li>参与者角色：{{if author.authorType=='admin'}}负责人{{/if}}
				{{if author.authorType=='member'}}参与者{{/if}}</li>
	</script>
</html>