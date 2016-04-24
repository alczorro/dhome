<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<title><fmt:message key="institute.common.scholarEvent"/>-${institution.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.css"/>" />
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/css/tokenInput.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/sample-css/page.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/css/dp.css'/>"/>
	<link rel="stylesheet" href="<dhome:url value="/resources/css/lightbox.css"/>" type="text/css" media="screen" />
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${titleUser.zhName }<fmt:message key="common.dhome"/>
			<jsp:include page="../../showAudit.jsp"/>
		</div>
		<jsp:include page="../../commonMenu.jsp">
			<jsp:param name="activeItem" value="achievement" />
		</jsp:include>
	</div> 
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title">
				<h3>课题管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="topic" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
					<jsp:include page="../../commonTopic.jsp"><jsp:param name="activeItem" value="modifyTopic" /> </jsp:include>
					<form id="editForm" class="form-horizontal" method="post" action="<dhome:url value="/people/admin/topic/creat"/>">
						<input type="hidden" name="id" id="id" value="${empty topic.id?0:topic.id }"/>
						
						<div class="control-group">
			       			<label class="control-label">课题编号：</label>
			       			<div class="controls">
			         			<input type="text" name="topic_no" id="topic_no" value="<c:out value="${topic.topic_no }"/>" />
								<span id="topic_no_error_place" class="error"></span>
			       			</div>
			       		</div>
						<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>课题名称：</label>
			       			<div class="controls">
			         			<input type="text" name="name" id="name" value="<c:out value="${topic.name }"/>" maxlength="250" class="register-xlarge"/>
								<span id="name_error_place" class="error"></span>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>部门：</label>
			       			<div class="controls">
			         			<select id="departId" name="departId">
			         				<option value="0" <c:if test="${topic.departId==0 }">selected="selected"</c:if>>--</option>
			         				<c:forEach items="${deptMap.entrySet() }" var="data">
			         			         <option value="${data.key }" <c:if test="${data.key==topic.departId }">selected="selected"</c:if>>${data.value.shortName}</option>
			         			 	</c:forEach>
								</select>
			       			</div>
			       		</div>
						<div class="control-group">
			       			<label class="control-label">开始时间：</label>
			       			<div class="controls">
			         			<select id="start_year" name="start_year" class="register-xsmall">
								</select>&nbsp;年&nbsp;&nbsp;
			         			<select id="start_month" name="start_month" class="register-xsmall">
									<option value="0">--</option>
								</select>&nbsp;月
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">结束时间：</label>
			       			<div class="controls">
			         			<select id="end_year" name="end_year"  class="register-xsmall">
								</select>&nbsp;年&nbsp;&nbsp;
			         			<select id="end_month" name="end_month"  class="register-xsmall"> 
<!-- 									<option value="0">--</option> -->
								</select>&nbsp;月
								<span class="check"><input type="checkbox" name="now"  id="checkThis"/> 至今</span>
								<span class="error"></span>
			       			</div>
			       		</div>
						<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>资金来源：</label>
			       			<div class="controls">
			         			<select id="funds_from" name="funds_from">
			         				<c:forEach items="${fundsFroms.entrySet() }" var="data">
			         			         <option value="${data.key }" <c:if test="${data.key==topic.funds_from }">selected="selected"</c:if>>${data.value.val}</option>
			         			 	</c:forEach>
							</select>
								<span id="funds_from_error_place" class="error"></span>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>类别：</label>
			       			<div class="controls">
			         			<select id="type" name="type">
			         				<c:forEach items="${types.entrySet() }" var="data">
			         			         <option value="${data.key }" <c:if test="${data.key==topic.type }">selected="selected"</c:if>>${data.value.val}</option>
			         			 	</c:forEach>
							</select>
							<span id="type_error_place" class="error"></span>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>本人角色：</label>
			       			<div class="controls">
			         			<select id="role" name="role">
								<option value="admin" <c:if test="${topic.role eq 'admin' }">selected="selected"</c:if>>负责人</option>
		          				<option value="member" <c:if test="${topic.role eq 'member' }">selected="selected"</c:if>>参与人</option>
							</select>
							<span id="role_error_place" class="error"></span>
			       			</div>
			       		</div>
			       		<hr>
			       		<div class="control-group">
			       			<label class="control-label">参与者：</label>
			       			<div class="controls">
			         			<input type="text" name="authorSearch" class="autoCompleteSearch register-xlarge" id="authorSearch"/>
								<span class="check"><input type="checkbox"  id="checkAuthor"/> 是否负责人</span>
								<p class="hint">请输入参与者的姓名、邮箱或者单位，按回车确认添加。</p>
								<p class="notFind">没有找到您要选择的参与者？点击这里<a id="addAuthor">添加参与者</a></p>
			       			</div>
			       			<table id="authorTable" class="table table-bordered fiveCol">
								<tbody id="authorContent">
								</tbody>
							</table>
			       		</div>
			       			
			       		<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>项目经费：</label>
			       			<div class="controls">
			         			<input type="text" name="project_cost" id="project_cost" maxlength="250" value="<c:out value="${topic.project_cost }"/>"/>
			       				<span id="project_cost_error_place" class="error"></span>
			       			</div>
			       		</div>
						<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>个人经费：</label>
			       			<div class="controls">
			         			<input type="text"  name="personal_cost" id="personal_cost"  value="<c:out value="${topic.personal_cost }"/>"/>
								<span id="personal_cost_error_place" class="error"></span>
			       			</div>
			       		</div>
						
			       		<div class="control-group">
			       			<label class="control-label">&nbsp;</label>
			       			<div class="controls">
			         			<input type="button" id="topicSaveBtn" value="提交" class="btn btn-primary"/>
			         			<a class="btn btn-link" href="<dhome:url value="/people/${domain }/admin/topic/list/1"/>">取消</a>
			       			</div>
			       		</div>
					</form>
				</div>	
			</div>
		<!-- 添加参与者 -->
	<div id="add-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>添加参与者</h3>
        </div>
        	<form id="addAuthorForm" class="form-horizontal" method="post">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">姓名：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorName" name="authorName" value='' class="register-xlarge"/>
          				<span id="authorName_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">邮箱：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorEmail" name="authorEmail" value='' class="register-xlarge"/>
          				<span id="authorEmail_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">单位：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorCompany" name="authorCompany" value='' class="register-xlarge"/>
          				<span id="authorCompany_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">参与者类型：</label>
          			<div class="controls">
            			<select id="role" name="role">
							<option value="admin">负责人</option>
	          				<option value="member">参与人</option>
						</select>
          				<span id="authorCompany_error_place" class="error"></span>
          			</div>
        		</div>
        		
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="authorSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div>
	
	<!-- 编辑参与者 -->
	<div id="edit-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>编辑参与者</h3>
        </div>
        	<form id="editAuthorForm" class="form-horizontal" method="post">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">姓名：</label>
          			<div class="controls padding">
          				<span id="editTrueName"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">邮箱：</label>
          			<div class="controls padding">
          				<span id="editEmail"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">单位：</label>
          			<div class="controls padding">
            			<span id="editAuthorCompany" ></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">参与者类型：</label>
          			<div class="controls padding">
            			<select id="editAuthorType" name="editAuthorType">
							<option value="admin" >负责人</option>
	          				<option value="member">参与人</option>
						</select>
          			</div>
          			
        		</div>
        		
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="editAuthorSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div>
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
	<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script>
		$(document).ready(function(){
			
			//初始化时间控件
			$("#citationQueryTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});

// 			//token控件初始化，关键字
// 			var $tokenObj = $("#keywordDisplay").tokenInput("<dhome:url value='/people/${domain}/admin/topic/search/keyword'/>", {
// 				theme:"facebook",
// 				hintText: "请输入关键字",
// 				searchingText: "正在查询...",
// 				noResultsText: "未查询到结果",
// 				preventDuplicates: true,
// 				queryParam:"q"
// 			});
			/* 检查课题的开始结束时间 */
			function checkTime(popup){
				var beginTimeYear = $(popup+" select[id=start_year]").val();
				var beginTimeMonth = $(popup+" select[id=start_month]").val();
				var endTimeYear = $(popup+" select[id=end_year]").val();
				var endTimeMonth = $(popup+" select[id=end_month]").val();
				$(popup+" input[name=endTime]").val(endTimeYear+"-"+endTimeMonth+"-01");
				
				if($(popup+" input[type=checkbox][name=now]").attr("checked")){
					endTimeYear = '3000'; 
					endTimeMonth = '1';
					$(popup+" input[name=endTime]").val("3000-01-01");
				}
				$(popup+" input[name=beginTime]").val(beginTimeYear+"-"+beginTimeMonth+"-01");
				if(endTimeYear<beginTimeYear || (endTimeYear == beginTimeYear && endTimeMonth<beginTimeMonth )){
					var $parent = $(popup+" select[id=end_year]").parents("div.control-group");
					$parent.find("span.error").html("").append("结束时间必须在开始时间之后");
					return false;
				}
				
				return true;
			}
			
			//添加课题Dialog，前台验证
			$('#editForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
// 					 topic_no:{
// 						 required:true
// 						 },
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
// 					 topic_no:{
// 						 required:'编号不允许为空'
// 						 },
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
			//添加参与者Dialog，验证
			$('#addAuthorForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 authorName:{
						 required:true
						 },
					authorEmail:{
						 required:true,
						 email:true
						 },
				 	
				 	authorCompany:{
				 		required:true
				 	}
				 },
				 messages:{
					 authorName:{
						 required:'姓名不允许为空'
						 },
						 authorEmail:{
							 required:'邮箱不允许为空',
							 email:'非法的邮箱格式'
						 },
					 	authorCompany:{
					 		required:'单位不允许为空'
					 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
// 			//提交添加课题
// 			$('#topicSaveBtn').on('click',function(){
// 				if(!$('#editForm').valid()){
// 					return;
// 				}
// 				$.ajax({
// 					  type: 'POST',
// 					  url: '<dhome:url value="/people/${domain}/admin/topic/submit"/>',
// 					  data: $('form').serialize(),
// 					  success: function(data){window.location.href='<dhome:url value="/people/${domain}/admin/topic/list/1"/>'},
// 					  error:function(){alert("添加错误")},
// 					  dataType: "JSON"
// 					});
				
// 			});
			//设置开始年份
			(function(before,defaultYear){
				var year=new Date().getFullYear();
				for(var i=year-before-1;i<=year;i++){
					$('#start_year').append('<option  value="'+i+'">'+i+'</option')
				}
				if(!defaultYear){
					defaultYear=year;
				}
				$('#start_year').val(defaultYear);
			})(15,parseInt('${topic.start_year}'));
			//开始月份
			(function(loop,defaultMonth){
				for(var i=1;i<loop+1;i++){
					$('#start_month').append('<option value="'+i+'">'+i+'</option');
				}
				if(!defaultMonth){
					defaultMonth=1;
				}
				$('#start_month').val(defaultMonth);
			})(12,parseInt('${topic.start_month}'));
			//设置结束年份
			(function(before,defaultYear){
				var year=new Date().getFullYear();
				for(var i=year-before-1;i<=year;i++){
					$('#end_year').append('<option  value="'+i+'">'+i+'</option')
				}
// 				if(!defaultYear){
// 					defaultYear=year;
// 				}
				$('#end_year').val(defaultYear);
			})(15,parseInt('${topic.end_year}'));
			//结束月份
			(function(loop,defaultMonth){
				for(var i=1;i<loop+1;i++){
					$('#end_month').append('<option value="'+i+'">'+i+'</option');
				}
// 				if(!defaultMonth){
// 					defaultMonth=1;
// 				}
				$('#end_month').val(defaultMonth);
			})(12,parseInt('${topic.end_month}'));
			
			//参与者队列
			window.author={
					//数据
					data:[],
					//重新排序
					sort:function(){
					},
					remove:function(id){
						for(var i in this.data){
							if(this.data[i].id==id){
								this.data.splice(i,1);
								this.render();
								return;
							}
						}
					},
					//获得最后一个order
					newOrder:function(){
						if(this.data.length==0){
							return 1;
						}
						return this.data[this.data.length-1].order+1;
					},
					//替换
					replace:function(item){
						for(var i in this.data){
							if(this.data[i].id==item.id){
								this.data[i]=item;
								return;
							}
						}
					},
					//插入到末尾
					append:function(item){
// 						if(this.isContain(item)){
// 							return false;
// 						}
// 						if(!item.order){
// 							item.order=this.newOrder();
// 						}
						if($('#checkAuthor').attr("checked")){
							item.authorType='admin';  
						}else{
							item.authorType='member'; 
						}
						this.data.push(item);
						this.render();
						return true;
					},
					//是否已存在
					isContain:function(item){
						if(this.data.length==0){
							return false;
						}
						for(var i in this.data){
							if(this.data[i].id==item.id){
								return true;
							}
						}
						return false;
					},
					//排序是否已存在
					hasOrder:function(order){
						if(this.data.length==0){
							return false;
						}
						for(var i in this.data){
							if(this.data[i].order==order){
								return true;
							}
						}
						return false;
					},
					//控制表格显示
					render:function(){
						if(this.data.length==0){
							/* alert("不显示"); */
							$('#authorTable').hide();
							return;
						}else{
							this.data.sort(function(a,b){
								if(a.order>b.order){
									return 1;
								}else{
									return -1;
								}
							});
							$('#authorTable').show();
							$('#authorContent').html($('#authorTemplate').tmpl(this.data,{
								judiceBool:function(bool){
									return bool?'是':'否';
								}
							})); 
						}
					}
			};
			
			//删除参与者
			$('.removeAuthor').live('click',function(){
				if(confirm("确定移除该参与者？")){
					var email=$(this).data('uid');
					author.remove(email);
				}
			});
			//查询参与者
			$("#authorSearch").autocomplete('<dhome:url value="/people/${domain}/admin/paper/search/author"/>',
			            {
					  		width:400,
							parse:function(data){
									return $.map(data, function(item) {
										return {
											data : item,
											result : '',
											value:item.authorName
										};
									});
							},
							formatItem:function(row, i, max) {
			    				return  row.authorName+"("+row.authorEmail+")" + "- [" + row.authorCompany+"]";
			 				},
							formatMatch:function(row, i, max) {
			    				return row.authorName + " " + row.authorEmail+""+row.authorCompany;
			 				},
							formatResult: function(row) {
			    				return row.authorName; 
			 				}
						}).result(function(event,item){
							var success=author.append(item);
							if(!success){
								alert('请勿重复添加');
							}
						});
			//添加参与者
			$('#addAuthor').on('click',function(){
				$('#addAuthorForm').get(0).reset();
				$('#add-author-popup').modal('show');
// 				$('#order').val(author.newOrder());
// 				judiceCheckbox('create');
			});
			//编辑-保存参与者
			$('#editAuthorSaveBtn').on('click',function(){
				if(!$('#editAuthorForm').valid()){
					return;
				}
				var $data=$(this).closest('form').data('author');
				$data.authorType=$('#editAuthorType').val();
// 				var order=parseInt($('#editOrder').val());
// 				if($data.order!=order&&author.hasOrder(order)){
// 					alert('第'+order+'参与者已存在');
// 					return;
// 				}
// 				$data.order=order;
// 				$data.communicationAuthor=$('#editCommunicateAuthor input[type=checkbox]').is(':checked');
// 				$data.authorStudent=$('#editAuthorStudentL input[type=checkbox]').is(':checked');
// 				$data.authorTeacher=$('#editAuthorTeacherL input[type=checkbox]').is(':checked');

				author.replace($data);
				author.render();
				$('#edit-author-popup').modal('hide');
			});
			//保存参与者
			$('#authorSaveBtn').on('click',function(){
				if(!$('#addAuthorForm').valid()){
					return;
				}
// 				var order=parseInt($('#order').val());
// 				if(author.hasOrder(order)){
// 					alert('第'+order+'参与者已存在');
// 					return;
// 				}
				$.post('<dhome:url value="/people/${domain}/admin/topic/author/create"/>',$('#addAuthorForm').serialize()).done(function(data){
					if(data.success){
						author.append(data.data);
						$('#add-author-popup').modal('hide');
					}else{
						alert(data.desc);
					}
				});
			});
			//编辑参与者
			$('.editAuthor').live('click',function(){
				$('#edit-author-popup').modal('show');
				var $data=$(this).closest('tr').data('tmplItem').data;
				$('#editAuthorForm').data('author',$data).get(0).reset();
				$('#editTrueName').html($data.authorName);
				$('#editEmail').html($data.authorEmail);
				$('#editAuthorCompany').html($data.authorCompany); 
				$('#editAuthorType').val($data.authorType);
			});
			//左栏置为选中
			$('#topicMenu').addClass('active');
			
			$('#checkThis').click(function(){
				if($('#checkThis').attr("checked")){
					$("#end_year").attr("disabled","disabled");  
					$("#end_month").attr("disabled","disabled");  
				}else{
					$("#end_year").removeAttr("disabled");  
					$("#end_month").removeAttr("disabled");  
				}
			}); 
			
			//提交课题
			$('#topicSaveBtn').on('click',function(){
				if($('#editForm').valid()){
					if(!checkTime('#editForm')){
						return false;
					}
					var data=$('#editForm').serialize();
					if($('#checkThis').attr("checked")){
						data +='&end_year=' + 3000;
						data +='&end_month='+ new Date().getMonth() + 1;
					}
					if(author.data.length!=0){
						for(var i in author.data){
							var auth=author.data[i];
							data+='&uid[]='+auth.id;
							data+='&authorType[]='+auth.authorType;
						}
					}else{
						data+="&uid[]=&authorType[]=";
					}
					$.ajax({
	 					  type: 'POST',
	 					  url: '<dhome:url value="/people/${domain}/admin/topic/submit"/>',
	 					  data: data,
	 					  success: function(data){window.location.href='<dhome:url value="/people/${domain}/admin/topic/list/${empty returnPage?1:returnPage}"/>'},
	 					  error:function(){alert(data.desc)},
	 					  dataType: "JSON"
	 					});
// 					$.post('<dhome:url value="/people/${domain}/admin/topic/submit"/>',data).done(function(data){ 
// 						if(data.success){
// 							alert();
// 							window.location.href='<dhome:url value="/people/${domain}/admin/topic/list/${returnPage}"/>'
// 						}
//						else{
// 							alert(data.desc);
// 						}
// 					});
				}
			});
			//如果为更新操作
			if('${op}'=='update'){
				//渲染参与者
				$.get('<dhome:url value="/people/${domain}/admin/topic/authors/${topic.id}"/>').done(function(data){
					if(data.success){
						author.data=data.data;
					}else{
						alert(data.desc);
					}
					author.render();
				});
				if('${topic.end_year}' == 3000){
					$("#end_year").attr("disabled", true);
					$("#end_month").attr("disabled", true);
					$("#checkThis").attr("checked", true);
				}
// 				//渲染关键字
// 				var keywords='${paper.keywordDisplay}'
// 				if(keywords!=''){
// 					$tokenObj.tokenInput("clear");
// 					var array=keywords.split(',');
// 					for(var i=0; i<array.length; i++){
// 						$tokenObj.tokenInput("add", {id:$.trim(array[i]), name: $.trim(array[i])});
// 					}
// 				}
			}else{
				author.render();
			}
		});
		
	</script>
	<!-- 参与者表格模板 -->
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<tr>
			<td class="order">{{if authorType=='admin'}}负责人：{{/if}}{{if authorType=='member'}}参与者：{{/if}}</td>
			<td class="author">
				{{= authorName}}
				<span class="mail">({{= authorEmail}})</span>
				<span class="company">-[{{= authorCompany}}]</span>
			</td>
			<td>
				<a class="editAuthor" data-uid="{{= id}}"><i class="icon icon-edit"></i> </a>
				<a class="removeAuthor" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
</html>