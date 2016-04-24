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
			<h3>奖励管理</h3>
			</div>
			<jsp:include page="../menu.jsp"><jsp:param name="activeItem" value="award" /></jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
					<h4 class="detail">${award.name }</h4>
					<div class="form-horizontal">
			       		<div class="control-group">
			       			<label class="control-label">奖励名称：</label>
			       			<div class="controls padding" id="awardName">
			       				<c:out value="${awardNames[award.awardName].val }"/>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">部门：</label>
			       			<div class="controls padding">
			         			<c:out value="${deptMap[award.departId].shortName }"/>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">授予机构：</label>
			       			<div class="controls padding">
			         			<c:out value="${award.grantBody }"/> 
			       			</div>
			       		</div>
			       		
			       		<div class="control-group">
			       			<label class="control-label">类别：</label>
			       			<div class="controls padding" id="type">
			       				<c:out value="${types[award.type].val }"/>
			       			</div>
			       		</div>
			       		
			       		<div class="control-group">
			       			<label class="control-label">等级：</label>
			       			<div class="controls padding" id="grade">
			       				<c:out value="${grades[award.grade].val }"/>
			       			</div>
			       		</div>
			       		
			       		<div class="control-group">
			       			<label class="control-label">年度：</label>
			       			<div class="controls padding">
			         			<c:out value="${award.year }"/>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">获奖人：</label>
			       			<div class="controls padding">
			         			<ul id="authorTable"style="margin-left:0">
								</ul>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">单位排序：</label>
			       			<div class="controls padding">
			         			<c:out value="${award.companyOrder}"/> 
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">附件：</label>
			       			<div class="controls padding">
			         			<table id="clbTable">
									<tbody id="clbContent">
									
									</tbody>
								</table>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">&nbsp;</label>
			       			<div class="controls padding">
			         			<a class="btn btn-primary" href="<dhome:url value="/people/${domain}/admin/award/update/${award.id }?returnPage=${returnPage }"/>">编辑</a>
								<a id="deleteaward" class="btn" href="<dhome:url value="/people/${domain }/admin/award/delete?awardId[]=${award.id }&page=${returnPage }"/>">删除</a>
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
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/jquery/jquery.lightbox.js"/>" type="text/javascript"></script>
	
	<script>
		$(document).ready(function(){
			$('#deleteaward').on('click',function(){
				return confirm("奖励删除以后不可恢复，确认删除吗？");
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
			$('#awardMenu').addClass('active');
		});
	</script>
	<!-- 作者表格模板 -->
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>
			<span class="order">第{{= order}}作者：</span>
			<span class="author large">
				{{= authorName}}
				<span class="mail">({{= authorEmail}})</span>
				<span class="company">-[{{= authorCompany}}]</span>
			</span>
		</li>
	</script>
	<script type="text/x-jquery-tmpl" id="clbTemplate">
        <tr>
           <td>
            {{= fileName}}
            {{if fileType == 1}}
               <a href="<dhome:url value="/system/img?imgId={{= clbId}}"/>" class="previewBtn" title="{{= fileName}}" data-uid="{{= clbId}}">预览</a>
            {{/if}}
			{{if clbId != 0}}
                 <a href="<dhome:url value="/system/file?fileId={{= clbId }}"/>" target="_blank">附件下载</a>
            {{/if}}
          </td>
        </tr>
    </script>
	
</html>