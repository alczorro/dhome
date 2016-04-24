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
				<h3>奖励管理</h3>
			</div>
			<jsp:include page="../menu.jsp"><jsp:param name="activeItem" value="award" /></jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonAward.jsp"><jsp:param name="activeItem" value="modifyAward" /> </jsp:include>
<!-- 					<ul class="nav nav-tabs"> -->
<!-- 						<li>&nbsp;&nbsp;&nbsp;</li> -->
<!-- 						<li> -->
<%-- 						    <a href="<dhome:url value="/people/${domain }/admin/award/list/1"/>">奖励 </a> --%>
<!-- 						</li> -->
<!-- 						<li class="active"> -->
<!-- 						    <a href="#"> -->
<%-- 							    <c:choose> --%>
<%-- 									<c:when test="${op=='add' }"> --%>
<!-- 										+ 手动添加奖励 -->
<%-- 									</c:when> --%>
<%-- 									<c:otherwise> --%>
<!-- 										更改奖励 -->
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>
<!-- 							</a> -->
<!-- 					    </li> -->
					   
					    
<!-- 					   	<li> -->
<!-- 						    <a href="#">+检索添加奖励</a> -->
<!-- 					    </li> -->
<!-- 					</ul> -->
					<form id="editForm" class="form-horizontal">
						<input type="hidden" name="id" id="id" value="${empty award.id?0:award.id }"/>
						
						<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>项目名称：</label>
			       			<div class="controls">
			         			<input type="text" name="name" id="name" value="<c:out value="${award.name }"/>" class="register-xlarge"/>
								<span id="name_error_place" class="error"></span>
			       			</div>
			       		</div>
			       		
			       		  <div class="control-group">
			       			<label class="control-label"><span class="red">*</span>获奖名称：</label>
			       			<div class="controls">
			         			<select id="awardName" name="awardName">
			         				<c:forEach items="${awardNames.entrySet() }" var="data">
			         			         <option value="${data.key }" <c:if test="${data.key==award.awardName }">selected="selected"</c:if>>${data.value.val}</option>
			         			   </c:forEach>
								</select>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>部门：</label>
			       			<div class="controls">
			         			<select id="departId" name="departId">
			         				<option value="0" <c:if test="${award.departId==0 }">selected="selected"</c:if>>--</option>
			         				<c:forEach items="${deptMap.entrySet() }" var="data">
			         			         <option value="${data.key }" <c:if test="${data.key==award.departId }">selected="selected"</c:if>>${data.value.shortName}</option>
			         			 	</c:forEach>
								</select>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>授予机构：</label>
			       			<div class="controls">
			         			<input type="text" name="grantBody" id="grantBody" value="<c:out value="${award.grantBody }"/>" class="register-xlarge"/>
								<span id="grantBody_error_place" class="error"></span>
			       			</div>
			       		</div>
			       		
			       		 <div class="control-group">
			       			<label class="control-label"><span class="red">*</span>类别：</label>
			       			<div class="controls">
			         			<select id="type" name="type">
			         				<c:forEach items="${types.entrySet() }" var="data">
			         			         <option value="${data.key }" <c:if test="${data.key==award.type }">selected="selected"</c:if>>${data.value.val}</option>
			         			 	</c:forEach>
								</select>
			       			</div>
			       		</div>
			       		
			       		 <div class="control-group">
			       			<label class="control-label"><span class="red">*</span>等级：</label>
			       			<div class="controls">
			         			<select id="grade" name="grade">
			         				<c:forEach items="${grades.entrySet() }" var="data">
			         			         <option value="${data.key }" <c:if test="${data.key==award.grade }">selected="selected"</c:if>>${data.value.val}</option>
			         			   </c:forEach>
								</select>
			       			</div>
			       		</div>
			       		
			       		 <div class="control-group">
			       			<label class="control-label"><span class="red">*</span>年度：</label>
			       			<div class="controls">
			         			<select id="year" name="year">
								</select>
			       			</div>
			       		</div>
			       		
			       		<div class="control-group">
			       			<label class="control-label"><span class="red">*</span>获奖人：</label>
			       			<div class="controls">
			         			<input type="text" name="authorSearch" class="autoCompleteSearch register-xlarge" id="authorSearch"/>
								<p class="hint">请输入获奖人的姓名、邮箱或者单位，按回车确认添加。</p>
								<p class="notFind">没有找到您要选择的作者？点击这里<a id="addAuthor">添加获奖人</a></p>
			       			</div>
			       			<ul id="authorTable"></ul>
			       		</div>
			       		
			       		  <div class="control-group">
			       			<label class="control-label"><span class="red">*</span>单位排序：</label>
			       			<div class="controls">
			         			<select id="companyOrder" name="companyOrder">
								</select>
			       			</div>
			       		</div>
			       		
			       		<div class="control-group">
			       			<label class="control-label">上传附件：</label>
			       			<div class="controls">
								<div id="fileUploader">
									<div class="qq-uploader">
										<div class="qq-upload-button">
											<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
										</div>
										<ul class="qq-upload-list fileList"></ul>
									</div>
								</div>
								
								<table id="clbTable">
									<tbody id="clbContent">
									
									</tbody>
								</table>
								
								<%-- <span id="fileNameSpan"><c:out value="${paper.originalFileName }"/></span>
								<span id="fileUploadProcess"></span>
								<input type="hidden" name="clbId" id="clbId" value="${empty paper?0:paper.clbId }"/>
								<input type="hidden" name="originalFileName" id="fileName" value="<c:out value="${paper.originalFileName }"/>"/>
								<a id="fileRemove" style="display:${empty paper||paper.clbId==0?'none':'inline' }">删除</a> --%>
			       			</div>
			       		</div>
			       		
			       		<div class="control-group">
			       			<label class="control-label">&nbsp;</label>
			       			<div class="controls">
			         			<input type="button" id="awardSaveBtn" value="提交" class="btn btn-primary"/>
			         			<a class="btn btn-link" href="<dhome:url value="/people/${domain }/admin/award/list/1"/>">取消</a>
			       			</div>
			       		</div>
						
					</form>
				</div>	
			</div>
	</div>
<!-- 添加作者 -->
	<div id="add-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>添加作者</h3>
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
         			<label class="control-label">作者排序：</label>
          			<div class="controls">
          				第
            			<input maxlength="254" type="text" id="order" name="order" value='0' style="width:20px;"/>
          				作者
          				<span id="order_error_place" class="error"></span>
          			</div>
        		</div>
        		
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="authorSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div>
	<!-- 编辑作者 -->
	 <div id="edit-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>编辑作者</h3>
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
         			<label class="control-label">作者排序：</label>
          			<div class="controls padding">
          				第
            			<input maxlength="254" type="text" id="editOrder" name="editOrder" value='0' style="width:20px;"/>
          				作者
          				<span id="editOrder_error_place" class="error"></span>
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
	<script src="<dhome:url value="/resources/scripts/jquery/jquery.lightbox.js"/>" type="text/javascript"></script>
	
	<script>
		$(document).ready(function(){
			//初始化时间控件
			$("#citationQueryTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});

			//token控件初始化，关键字
			var $tokenObj = $("#keywordDisplay").tokenInput("<dhome:url value='/people/${domain}/admin/paper/search/keyword'/>", {
				theme:"facebook",
				hintText: "请输入关键字",
				searchingText: "正在查询...",
				noResultsText: "未查询到结果",
				preventDuplicates: true,
				queryParam:"q"
			});
			//奖励保存验证
			$('#editForm').validate({
				submitHandler:function(form){
					form.submit();
				},
			    rules:{
			    	name:{
			    		required:true,
				        maxlength:250
			    	},
			    	grantBody:{
			    		required:true,
			    		maxlength:250
			    	}
			    },
				
				messages:{
					name:{
						required:'项目名称不能为空',
						maxlength:'项目名称过长'
					},
					grantBody:{
						required:'授予机构不能为空',
						maxlength:'授予机构名称过长'
					}
				},
			
			    errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});  
			
			
			//作者队列
			var author={
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
						if(this.isContain(item)){
							return false;
						}
						if(!item.order){
							item.order=this.newOrder();
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
							$('#authorTable').html($('#authorTemplate').tmpl(this.data,{
								judiceBool:function(bool){
									return bool?'是':'否';
								}
							})); 
						}
					}
			};
			
			//删除作者
			$('.removeAuthor').live('click',function(){
				if(confirm("确认移除该获奖人？")){
					var email=$(this).data('uid');
					author.remove(email);
				}
			});
			//查询作者
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
			//添加作者
			$('#addAuthor').on('click',function(){
				$('#addAuthorForm').get(0).reset();
				$('#add-author-popup').modal('show');
				$('#order').val(author.newOrder());
				judiceCheckbox('create');
			});
			//编辑-保存作者
			$('#editAuthorSaveBtn').on('click',function(){
				if(!$('#editAuthorForm').valid()){
					return;
				}
				var $data=$(this).closest('form').data('author');
				var order=parseInt($('#editOrder').val());
				if($data.order!=order&&author.hasOrder(order)){
					alert('第'+order+'作者已存在');
					return;
				}
				$data.order=order;
				$data.communicationAuthor=$('#editCommunicateAuthor input[type=checkbox]').is(':checked');
				$data.authorStudent=$('#editAuthorStudentL input[type=checkbox]').is(':checked');
				$data.authorTeacher=$('#editAuthorTeacherL input[type=checkbox]').is(':checked');

				author.replace($data);
				author.render();
				$('#edit-author-popup').modal('hide');
			});
			//保存作者
			$('#authorSaveBtn').on('click',function(){
				if(!$('#addAuthorForm').valid()){
					return;
				}
				var order=parseInt($('#order').val());
				if(author.hasOrder(order)){
					alert('第'+order+'作者已存在');
					return;
				}
				$.post('<dhome:url value="/people/${domain}/admin/award/author/create"/>',$('#addAuthorForm').serialize()).done(function(data){
					if(data.success){
						author.append(data.data);
						$('#add-author-popup').modal('hide');
					}else{
						alert(data.desc);
					}
				});
			});
			$('#order').on('keyup',function(){
				judiceCheckbox('create');
			});
			$('#editOrder').on('keyup',function(){
				judiceCheckbox('update');
			});
			
			//判定添加作者，checkbox显隐逻辑
			function judiceCheckbox(oper){
				function hideAndUnchecked($0){
					$0.hide().find('input[type=checkbox]').removeAttr('checked');
				}
				var orderStr='';
				var studentLabel='';
				var teacherLabel='';
				if(oper=='create'){
					orderStr=$('#order').val();
					studentLabel='authorStudentL';
					teacherLabel='authorTeacherL'
					
				}else{
					orderStr=$('#editOrder').val();
					studentLabel='editAuthorStudentL';
					teacherLabel='editAuthorTeacherL'
				}
				if(orderStr==''){
					hideAndUnchecked($('#'+studentLabel));
					hideAndUnchecked($('#'+teacherLabel));
					return;
				}
				var order=parseInt(orderStr);
				//非正常的数字
				if(!order){
					hideAndUnchecked($('#'+studentLabel));
					hideAndUnchecked($('#'+teacherLabel));
					return;
				}
				if(order==1){
					$('#'+studentLabel).show();
					hideAndUnchecked($('#'+teacherLabel));
					return;
				} 
				if(order==2){
					$('#'+teacherLabel).show();
					hideAndUnchecked($('#'+studentLabel));
					return;
				}
				hideAndUnchecked($('#'+studentLabel));
				hideAndUnchecked($('#'+teacherLabel));
			}
			//编辑作者
			$('.editAuthor').live('click',function(){
				$('#edit-author-popup').modal('show');
				var $data=$(this).closest('li').data('tmplItem').data;
				$('#editAuthorForm').data('author',$data).get(0).reset();
				$('#editTrueName').html($data.authorName);
				$('#editEmail').html($data.authorEmail);
				$('#editAuthorCompany').html($data.authorCompany); 
				$('#editOrder').val($data.order);
				judiceCheckbox('editAuthorStudentL','editAuthorTeacherL');
				if($data.communicationAuthor){
					$('#editCommunicateAuthor input[type="checkbox"]').click();
				} 
				if($data.authorStudent){
					$('#editAuthorStudentL input[type=checkbox]').click();
				}
				if($data.authorTeacher){
					$('#editAuthorTeacherL input[type=checkbox]').click();
				}
			});
			//编辑作者验证
			$('#editAuthorForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 editOrder:{
				 		required:true,
				 		min:1,
				 		digits:true,
				 		max:99999999
				 	}
				 },
				 messages:{
					 editOrder:{
					 		required:'作者排序不允许为空',
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
			//添加作者Dialog，验证
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
				 	},
				 	order:{
				 		required:true,
				 		min:1,
				 		digits:true,
				 		max:99999999
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
					 	},
					 	order:{
					 		required:'作者排序不允许为空',
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
			
			//年度
			(function(loop,defaultYear){
				var year=new Date().getFullYear();
				for(var i=year-loop+2;i<=year;i++){
					$('#year').append('<option value="'+i+'">'+i+'</option')
				}
				if(!defaultYear){
					defaultYear = year;
				}
				
				$('#year').val(defaultYear);
			})(15,parseInt('${award.year}')); 
			
			
			//单位排序
			(function(loop,order){
				for(var i=1;i<loop+1;i++){
					$('#companyOrder').append('<option value="'+i+'">'+i+'</option');
				}
				
				if(!order){
					order = 1;
				}
				
				$('#companyOrder').val(order);
			})(6,parseInt('${award.companyOrder}')); 
			
		   //提交论文
			$('#awardSaveBtn').on('click',function(){
				if($('#editForm').valid()){
					var data=$('#editForm').serialize();
					if(author.data.length!=0){
						for(var i in author.data){
							var auth=author.data[i];
							data+='&uid[]='+auth.id;
							data+='&order[]='+auth.order;
							data+='&communicationAuthor[]='+auth.communicationAuthor;
							data+='&authorStudent[]='+auth.authorStudent;
							data+='&authorTeacher[]='+auth.authorTeacher;
						}
					}else{
						data+="&uid[]=&order[]=&communicationAuthor[]=&authorStudent[]=&authorTeacher[]=";
					}
					
					if(attachment.data.length!=0){
						for(var i in attachment.data){
							var attach =attachment.data[i];
							data+='&clbId[]='+attach.clbId;
							data+='&fileName[]='+attach.fileName;
						}
					}else{
						data+="&clbId[]=&fileName[]=";
					}
					
					$.post('<dhome:url value="/people/${domain}/admin/award/submit"/>',data).done(function(data){ 
						if(data.success){
							window.location.href='<dhome:url value="/people/${domain}/admin/award/list/1"/>'
						}else{
							alert(data.desc);
						}
					});
				}
			});
		   
			
			//奖励保存验证
			$('#editForm').validate({
				submitHandler:function(form){
					form.submit();
				},
			    rules:{
			    	perName:{
			    		required:true,
				        maxlength:250
			    	},
			    	position:{
			    		required:true,
			    		maxlength:250
			    	}
			    },
				
				messages:{
					perName:{
						required:'期刊名称不能为空',
						maxlength:'期刊名过长'
					},
					position:{
						required:'职位不能为空',
						maxlength:'职位名称过长'
					}
				},
			
			    errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});  
			
			//附件队列
			var attachment ={
					//数据
					data:[],
					
					remove:function(clbId){
						for(var i in this.data){
							if(this.data[i].clbId==clbId){
								this.data.splice(i,1);
								this.render();
								return;
							}
						}
					},
					
					//插入到末尾
					append:function(item){
						if(this.isContain(item)){
							return false;
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
							if(this.data[i].clbId==item.clbId){
								return true;
							}
						}
						return false;
					},
					//控制表格显示
					render:function(){
						var item = $('#clbTemplate').tmpl(this.data,{
							judiceBool:function(bool){
								return bool?'是':'否';
							}
						});
						item.find('.previewBtn').lightbox({
						    fitToScreen: true,
						    fileLoadingImage: "<dhome:url value='/resources/images/loading.gif'/>",
				            fileBottomNavCloseImage: "<dhome:url value='/resources/images/closelabel.gif'/>",
						    imageClickClose: false
					    });
						
						$('#clbContent').html(item); 
					}
			};
			
			$('.removeAttachment').live('click',function(){
				var clbId=$(this).data('uid');
				attachment.remove(clbId);
			});
			
			//上传文件
			new qq.FileUploader({
				element : document.getElementById('fileUploader'),
				action :'<dhome:url value="/people/${domain}/admin/award/upload"/>',
				sizeLimit : 20*1024 * 1024,
				//allowedExtensions:['doc','docx','pdf'],
				onComplete : function(id, fileName, data) {
					if(data.success){
						data.data.fileName = fileName;
						attachment.append(data.data);
					}else{
						alert("系统维护中，暂不能添加附件");
					}
					
					/* if(!data){
						return false;
					}
					$('#fileUploadProcess').empty();
					if(data.success){
						$('#fileNameSpan').html(fileName);
						$('#fileName').val(fileName)
						$('#clbId').val(data.data);
						$('#fileRemove').show();
					}else{
						alert(data.desc);
					} */
					
				},
				messages:{
		        	//typeError:"请上传doc,docx,pdf文件",
		        	emptyError:"请不要上传空文件",
		        	sizeError:"大小超过20M限制"
		        },
		        showMessage: function(message){
		        	alert(message);
		        },
		        onProgress: function(id, fileName, loaded, total){
		        	/* $('#fileNameSpan').html(fileName);
		        	$('#fileUploadProcess').html("("+Math.round((loaded/total)*100)+"%)"); */
		        },
		        multiple:false
			});
			
			//左栏置为选中
			$('#awardMenu').addClass('active');
			
			
			//编辑奖励加载作者
			if('${op}'=='update'){
				//渲染作者
				$.get('<dhome:url value="/people/${domain}/admin/award/authors/${award.id}"/>').done(function(data){
					if(data.success){
						author.data=data.data;
					}else{
						alert(data.desc);
					}
					author.render();
				});
				
				$.get('<dhome:url value="/people/${domain}/admin/award/attchments/${award.id}"/>').done(function(data){
					if(data.success){
						attachment.data=data.data;
					}else{
						alert(data.desc);
					}
					attachment.render();
				});
			}else{
				author.render();
			}
		});
	</script>
	
	<!-- 作者表格模板 -->
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>
			<span class="order">第{{= order}}获奖人：</span>
			<span class="author large">
				{{= authorName}}
				<span class="mail">({{= authorEmail}})</span>
				<span class="company">-[{{= authorCompany}}]</span>
			</span>
			<span>
				<a class="editAuthor" data-uid="{{= id}}"><i class="icon icon-edit"></i> </a>
				<a class="removeAuthor" data-uid="{{= id}}"><i class="icon icon-trash"></i></a>
			</span>
		</li>
	</script>
<%-- 	style="display:${empty paper||paper.clbId==0?'none':'inline' }"
              	<a class="removeAuthor" data-uid="{{= clbId}}">预览</a>
 --%>	
    <script type="text/x-jquery-tmpl" id="clbTemplate">
        <tr>
           <td>
            {{= fileName}}
            {{if fileType == 1}}
               <a href="<dhome:url value="/system/img?imgId={{= clbId}}"/>" class="previewBtn" title="{{= fileName}}" data-uid="{{= clbId}}">预览</a>
            {{/if}}
		    <a class="removeAttachment" data-uid="{{= clbId}}">删除</a>
          </td>
        </tr>
    </script>
	
	
</html>