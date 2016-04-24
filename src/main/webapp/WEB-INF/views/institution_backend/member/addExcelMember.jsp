<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
	<dhome:InitLanuage useBrowserLanguage="true"/>
	<html lang="en">
	<head>
		<title>机构论文</title>
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
					<jsp:include page="../../commonMemberBackend.jsp"><jsp:param name="activeItem" value="addMember" /> </jsp:include>
<!-- 					<li>&nbsp;&nbsp;&nbsp;</li> -->
<!-- 					<li> -->
<%-- 					    <a href="<dhome:url value="/institution/${domain }/backend/member/list/1"/>">员工列表</a> --%>
<!-- 					</li> -->
<!-- 					<li> -->
<%-- 						<a href="${memberUrl}" target="_blank">+ 新增员工</a> --%>
<!-- 				    </li> -->
<!-- 				   	<li class="active"> -->
<!-- 					    <a href="#">+ 批量导入</a> -->
<!-- 				    </li> -->
<!-- 				   	<li> -->
<!-- 					    <a href="#">绩效考核</a> -->
<!-- 				    </li> -->
<!-- 				   	<li> -->
<!-- 					    <a href="#">岗位评定</a> -->
<!-- 				    </li> -->
				</ul>
				<h3 class="detail" style="margin-top:20px;">
				
				<span style="float:right; margin-bottom:5px;">
<!-- 					<input type="button" id="createBtn" value=" +新增岗位应聘申请表 " class="btn btn-mini btn-success"/> -->
					<a href="${context }/resources/download/批量导入员工模板.xls" class="btn btn-small btn-success">下载模板</a>
				</span>
			</h3>
					    <form name="excelpaper-form" method="POST" action="<dhome:url value='/institution/${domain }/backend/paper?func=saveExcelPaper'/>">
					    	<input type="hidden" name="papers" value="">
					      <div class="addExcelMember">
					      	<p>Excel格式请参考ARP导出信息。</p>
				            	<div id="file-uploader-demo1" >
									<div class="qq-uploader" >
										<div class="qq-upload-drop-area"><span>拖动文件到此处上传</span></div>
										<div class="qq-upload-button" >
											上传Excel文件
										</div>					
										<ul class="qq-upload-list fileList"></ul>
									</div>
								</div>
								
								<label id="remind1" style="display:none">上传文件格式错误，请检查您的文件是否为正确的excel文件！</label>
								<div id="files-data-area"></div>
								<p class="subHint">
									前提操作： 请管理员确保在组织通讯录中成功导入了员工的姓名、密码（iap111111）、邮箱、部门、ARPID信息。
									<br>否则无法为员工自动建立学术主页，也不能与线上的学术主页关联。
									<br>导入成功后，已注册的用户，仍用已有的密码登录，新建的用户默认密码为：iap111111
								</p>
				          </div>
				          
				          <div class="cont">
							<table style="display:none;white-space: nowrap;" class="table table-bordered setttingTable" id="dataTable" >
								<thead>
									<tr>
										<th class="result">结果</th>
										<th>ARPID</th>
										<th class="name">姓名</th>
										<th class="email">邮箱</th>
										<th>部门</th>
										
									</tr>
								</thead>
								<tbody id="resolveResult">
								</tbody>
							</table>
<!-- 							<p style="padding:20px; text-align:center;"> -->
<!-- 								<input style="display:none;" type="button" id="saveBatch" class="btn btn-large btn-primary" value="确定导入"/> -->
<!-- 								<br> --> 
<!-- 								<span id="hasAdd" style="display:none"><b>提示</b>:该操作仅针对系统内已有成员进行批量修改。以上标记为<i class="icon-batchAdd"></i>的用户为新用户，您可以直接【批量添加】这些成员。 --> 
<!-- 							</span> --> 
<!-- 							</p> -->
						</div>
				         <div id="process" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
							<div class="modal-header">
								<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
								<h4 id="title">执行进度</h4>
							</div>
							<div class="modal-body">
								<p id="content">
									<div class="progress progress-striped active">
									  <div id="progress-bar" class="bar" style="width: 0%;"></div>
									</div>
									<ul id="taskList" style="height:200px;overflow:auto">
									</ul>
								</p>
							</div>
							<div class="modal-footer">
								<a class="btn btn-primary" id="start">开始</a>
								<a class="btn" data-dismiss="modal" aria-hidden="true" id="no">关闭</a>
							</div>
						</div>
<!-- 				          <div> -->
<!-- 						    <div>下面是导入的excel的样式<font color = "red">[上传的excel中的内容必须按照这个顺序就行排版，否则存储内容的位置会错乱的]</font></div> -->
<!-- 						    <table class="tablefirst" id="radioSubStyle"> -->
<!-- 						        <tr> -->
<!-- 						            <th>员工编号</th><th>员工姓名</th><th>员工编号</th><th>出生日期</th><th>出生地</th><th>性别</th><th>年龄</th> -->
<!-- 						            <th>工龄</th><th>办公电话</th><th>移动电话</th><th>电子邮件</th><th>办公地点</th> -->
<!-- 						            <th>所属部门</th><th>员工类型</th><th>级别代码</th><th>岗位等级</th><th>工资薪级</th><th>职工地址</th> -->
<!-- 						            <th>参加工作日期</th><th>政治面貌</th><th>职务分类</th><th>分配状态</th><th>职等</th><th>薪级工资值</th> -->
<!-- 						            <th>岗位工资值</th><th>退休时间</th><th>离休时间</th><th>参加党派时间</th><th>毕业学校</th><th>毕业日期</th> -->
<!-- 						            <th>所学专业名称</th><th>最高学历代码</th><th>最高学历</th><th>最高学位代码</th><th>最高学位</th><th>学位授予日期</th> -->
<!-- 						            <th>学位授予单位</th><th>进入本单位日期</th><th>进入方式</th><th>进入本行业日期</th><th>进入本地区日期</th><th>进入本单位前个人身份</th> -->
<!-- 						        </tr> -->
<!-- 						        <tr> 
<!-- 						            <td>沈浪</td><td>1006010054</td><td>男</td><td>汉</td><td>眼7</td><td>7</td><td>2009-11-11</td> -->
<!-- 						            <td>2011-11-11</td><td>浙江</td><td>医院</td><td>临床</td><td>主任</td> -->
<!-- 						            <td>642</td><td>159</td><td>5449</td><td>544@qq.com</td><td>杭州</td><td>杭州</td> -->
<!-- 						        </tr> 
<!-- 						    </table> -->
<!-- 						   </div> -->
<!-- 				          <div><ul id="excel-paper-list" class="paper-list striped" style="width:100%;"></ul></div> -->
<!-- 					      <div class="d-top"> -->
<%-- 					          	<button name="save-excelpaper" type="button" class="btn btn-primary btn-set-middle"><fmt:message key='common.save'/></button> --%>
<%-- 					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/paper'/>"><fmt:message key='common.cancel'/></a> --%>
<!-- 					          </div> -->
					    </form>
					   </div>
				  </div>
<%-- 		<jsp:include page="commonfooter.jsp"></jsp:include> --%>
</body>
<jsp:include page="../../commonheader.jsp"></jsp:include> 
<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
<script type="text/javascript">
	$(document).ready(function(){
		function doTask(index,params){
			if(params.length<=index){
				$('#progress-bar').html("任务完成["+params.length+'/'+params.length+"]").css('width',"100%");
				return;
			}
			$('#progress-bar').css('width',(index*100/(params.length-1))+"%").html('['+(index+1)+'/'+params.length+']');
			var param=params[index++];
			param.index=index;
			param.all=params.length;
			$.ajax({
				 url : 'doTask',
				 type : "POST",
				 data:param,
				 success : function(flag){
					if(flag){
// 						var $taskList=$('#taskList');
// 						var opType='';
// 						if(param.type=='import'){
// 							opType= "导入";
// 						}else if(param.type=="regist.umt"){
// 							opType= "创建通行证账号";
// 						}else if(param.type=='regist.coreMail'){
// 							opType= "创建邮箱账号";
// 						}
// 						$taskList.append('<li class="taskItem icon-batchOk" id="task_'+index+'">'+index+
// 								'.'+opType+'用户：'+param.cstnetId+(param!='import'?(',密码:'+param.password):'')+'</li>')
// 						.scrollTop(100000);
						doTask(index,params);
					}
				}
			});
		}
		
	    var uploader = new qq.FileUploader({
	        element: document.getElementById('file-uploader-demo1'),
	        template: document.getElementById('file-uploader-demo1').innerHTML,
	        action: "<dhome:url value='/institution/${domain}/backend/member?func=uploadExceltex'/>",
	        allowedExtensions:['xls','xlsx'],
	        onComplete:function(id, fileName, data){
	        	$("#excel-paper-list").html("");
	        	$("ul.qq-upload-list.fileList li:last").prevAll().remove();
	        	if(data.success){
// 	        		$("#batchRowsTmpl").render(data.data).appendTo("#resolveResult");
					$('#resolveResult').html($('#batchRowsTmpl').tmpl($.parseJSON(data.data)),{});
// 	        		if($('#resolveResult').find("i.icon-batchAdd").length>1){
// 						$('#hasAdd').show();
// 					}
					$('#saveBatch').show();
					$('#start').show();
					$('#dataTable').show();
	        	}else{
	        		alert('解析失败');
	        	}
	        },
	        messages:{
	        	typeError:"<fmt:message key='addNewPaper.upload.error.fileTypeError'/>{extensions}<fmt:message key='addNewPaper.upload.error.fileType'/>",
	        	emptyError : "<fmt:message key='addNewPaper.upload.error.fileContentError'/>"
	        },
	        showMessage: function(message){
	        	alert(message);
	        },
	        debug: true
	   });
	    $('#start').on('click',function(){
			$(this).hide();
			$('#saveBatch').hide();
			doTask(0,params);
		});
		$('#saveBatch').on('click',function(){
// 			var index=0;
// 			params=[];
// 			$item.each(function(i,n){
// 				if($(n).attr("checked")){
// 					++index;
// 					var data=$(n).parents('tr').data('tmplItem').data;
// 					var param={};
// 					param.index=i+1;
// 					param.dn=data.dn;
// 					param.type=data.custom;
// 					param.teamDn='${node.dn}';
// 					for(var i=0;i<data.cells.length;i++){
// 						var cell=data.cells[i];
// 						param[cell.key]=cell.after;
// 						param['before'+cell.key]=cell.before;
// 					}
// 					params.push(param);
// 				}
// 			});
			$('#process').modal('show');
			$('#progress-bar').empty().css('width','0%');
			$('#taskList').empty();
			
		});
		
		$.extend({
			judiceDept:function(index){
				var depts = {
				     <c:forEach items="${depts.entrySet() }" var="entry">
					 "${entry.key }" : "${entry.value.shortName }",
				     </c:forEach>};
					return depts[index];
			}
		});
	    $('#memberMenu').addClass('active');
	});
</script>
<script id="batchRowsTmpl" type="text/x-jquery-tmpl">
		<tr>
			<td >
				{{if status=='hides'}}
					<b style="color:red">已删除</b>
				{{/if}}
				{{if status=='adds'}}
					<b style="color:red">未成功</b>
				{{/if}}
			</td>
					<td>
						{{= sn }}
					</td>
					<td>
						{{= trueName }}
					</td>
					<td>
						{{= cstnetId }}
					</td>
					<td>
						{{= $.judiceDept(departId) }}
					</td>
			
		</tr>
</script>

</html>