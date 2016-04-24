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
			<h4 class="detail" id="perName">${organizationNames[academic.name].val}</h4>
			<div class="form-horizontal">
				<div class="control-group">
	       			<label class="control-label">部门：</label>
	       			<div class="controls padding">
	         			<c:out value="${deptMap[academic.departId].shortName }"/>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">职位：</label>
	       			<div class="controls padding" id="position">
	       				<c:choose>
							<c:when test="${academic.position==0 }">
								未知
							</c:when>
							<c:otherwise>
								${positions[academic.position].val}
							</c:otherwise>
						</c:choose>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">任职人员：</label>
	       			<div class="controls padding">
	         			<table id="authorTable" class="table" style="margin:0; width:550px; font-size:13px;">
							<tbody id="authorContent">
							
							</tbody>
						</table>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       	    	<label class="control-label">开始时间：</label>
	       			<div class="controls">
						<span id="startYear" class="register-xsmall"> ${academic.startYear} </span>
						&nbsp;年&nbsp;&nbsp;
						<span id="startMonth" class="register-xsmall"><c:if test="${academic.startMonth!=0}">${academic.startMonth} &nbsp;月</c:if> </span>
					   
	       			</div>
	       		 </div>
	       		
	       		<div class="control-group">
	       	    	<label class="control-label">结束时间：</label>
	       			<div class="controls">
						 <c:choose>
						 	<c:when test="${academic.endYear== 3000}">
								至今
							</c:when>
							<c:otherwise>
								<span id="endYear" class="register-xsmall"> ${academic.endYear} </span>
									&nbsp;年&nbsp;&nbsp;
									<span id="endMonth" class="register-xsmall"> <c:if test="${academic.endMonth!=0}">${academic.endMonth} &nbsp;月</c:if> </span>
							</c:otherwise>
					   </c:choose>
	       			</div>
	       		 </div>
	       		
	       		
	       		<div class="control-group">
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls padding">
	         			<a class="btn btn-primary" href="<dhome:url value="/institution/${domain}/backend/academic/update/${academic.id }?returnPage=${returnPage }"/>">编辑</a>
						<a id="deleteAcademic" class="btn" data-url="<dhome:url value="/institution/${domain }/backend/academic/delete?acmId[]=${academic.id }"/>">删除</a>
						<a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a>
	       			</div>
	       		</div>
			</div>	
				
			</div>
	</div>
	
	
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script> 
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/jquery/jquery.lightbox.js"/>" type="text/javascript"></script>
	<script>
		$(document).ready(function(){
			$('#deleteAcademic').on('click',function(){
				if(confirm("学术任职删除以后不可恢复，确认删除吗？")){
					$.get($(this).data('url')).done(function(data){
						if(data.success){
							window.location.href='<dhome:url value="/institution/${domain}/backend/academic/list/${returnPage }"/>'
						}else{
							alert(data.desc);
						}
					});
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
							$('#authorContent').html($('#authorTemplate').tmpl(this.data,{
								judiceBool:function(bool){
									return bool?'是':'否';
								}
							})); 
						}
					}
			};
			//渲染作者
			$.get('<dhome:url value="/institution/${domain}/backend/academic/authors/${academic.id}"/>').done(function(data){
				if(data.success){
					author.data=data.data;
				}else{
					alert(data.desc);
				}
				author.render();
			});
			$('#academicMenu').addClass('active');
		});
	</script>
	<!-- 作者表格模板 -->
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<tr>
			<td class="author">
				{{= trueName}}
				<span class="mail">({{= cstnetId}})</span>
			</td>
		</tr>
	</script>
</html>