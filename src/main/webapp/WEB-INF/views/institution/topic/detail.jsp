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
					<h4 class="detail">${topic.name }</h4>
					<div class="form-horizontal">
						<div class="control-group">
			       			<label class="control-label">编号：</label>
			       			<div class="controls padding">
			         			<c:out value="${topic.topic_no }"/>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">部门：</label>
			       			<div class="controls padding">
			         			<c:out value="${deptMap[topic.departId].shortName }"/>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">开始时间：</label>
			       			<div class="controls padding">
			         			${topic.start_year }<c:if test="${topic.start_month!=0 }">-${topic.start_month }</c:if>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">结束时间：</label>
			       			<div class="controls padding">
			       				<c:if test="${topic.end_year!=3000 }">
			         				${topic.end_year }<c:if test="${topic.end_month!=0 }">-${topic.end_month }</c:if>
			         			</c:if>
			         			<c:if test="${topic.end_year==3000 }">
			         			至今
			         			</c:if>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">资金来源：</label>
			       			<div class="controls padding">
			       				${fundsFroms[topic.funds_from].val}
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">类别：</label>
			       			<div class="controls padding">
			         			<c:choose>
									<c:when test="${topic.type==0 }">
										未知
									</c:when>
									<c:otherwise>
										${types[topic.type].val}
									</c:otherwise>
								</c:choose>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">课题组成员：</label>
			       			<div class="controls">
			         			<table id="authorTable" class="table" style="margin-left:0; font-size:13px;">
									<tbody id="authorContent">
									
									</tbody>
								</table>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">项目经费：</label>
			       			<div class="controls padding">
			         			<c:out value="${topic.project_cost }"/>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">个人经费：</label>
			       			<div class="controls padding">
			         			<c:out value="${topic.personal_cost}"/>
			       			</div>
			       		</div>
						<div class="control-group">
			       			<label class="control-label">&nbsp;</label>
			       			<div class="controls padding">
			         			<a class="btn btn-primary" href="<dhome:url value="/people/${domain}/admin/topic/edit/${topic.id }?returnPage=${returnPage }"/>">编辑</a>
								<a id="deleteTopic" class="btn" href="<dhome:url value="../delete?id[]=${topic.id }&page=${returnPage }"/>">删除</a>
								<a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a>
			       			</div>
			       		</div>
						
					</div>
			  </div>
			 </div>
			</div>
	</div>  
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script>
		$(document).ready(function(){
			$('#deleteTopic').on('click',function(){
				return (confirm("课题删除以后不可恢复，确认删除吗？"));
			});
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
			
			//渲染参与者
			$.get('<dhome:url value="/people/${domain}/admin/topic/authors/${topic.id}"/>').done(function(data){
				if(data.success){
					author.data=data.data;
				}else{
					alert(data.desc);
				}
				author.render();
			});
			//左栏置为选中
			$('#topicMenu').addClass('active');
		});
	</script>
	<!-- 参与者表格模板 -->
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<tr>
			<td class="order">
				{{if authorType=='member'}}参与人{{/if}}
				{{if authorType=='admin'}}<b>负责人</b>{{/if}}
			</td>
			<td class="author">
				{{= authorName}}
				<span class="mail">({{= authorEmail}})</span>
				<span class="company">-[{{= authorCompany}}]</span>
			</td>
		</tr>
	</script>
</html>