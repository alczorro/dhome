<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%@ page import="java.util.*" %>
<dhome:InitLanuage useBrowserLanguage="true"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><fmt:message key="personalSocialMediaInfo.title"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="commonheaderCss.jsp"></jsp:include>
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="commonBanner.jsp"></jsp:include>
	<div class="container page-title">
		<div class="sub-title">${titleUser}<fmt:message key='common.index.title'/></div>
		<jsp:include page="./commonMenu.jsp">
			<jsp:param name="activeItem" value="personInfo" />
		</jsp:include>
	</div>
	
	<div class="container canedit">
		<div class="row-fluid mini-layout">
			<div class="config-title" style="margin-top:0.5em;"><h3><fmt:message key='admin.common.link.personInfo'/></h3></div>
			<div class="span11">
				<ul class="nav nav-tabs">
					<li><li>&nbsp;&nbsp;&nbsp;</li></li>
						<li>
						    <a href="<dhome:url value='/people/${domain}/admin/personal'/>">
						    	<strong><fmt:message key='personal.common.basicInfo'/></strong>
						    </a>
					    </li>
					    <li>
					    <a href="http://passport.escience.cn/user/password.do?act=showChangePassword" target="_blank">
				        <strong><fmt:message key='personal.common.updatePsw'/></strong></a>
				    </li>
					    <li>
						    <a href="<dhome:url value='/people/${domain}/admin/personal/work'/>">
						    	<strong><fmt:message key='personal.common.work'/></strong>
						    </a>
					    </li>
					    <li>
						    <a href="<dhome:url value='/people/${domain}/admin/personal/education'/>">
						    	<strong><fmt:message key='personal.common.education'/></strong>
						    </a>
					    </li>
					    <li>
						    <a href="<dhome:url value='/people/${domain}/admin/personal/photo'/>">
						    	<strong><fmt:message key='personal.common.photo'/></strong>
						    </a>
					    </li>
					     <li  class="active">
						    <a href="<dhome:url value='/people/${domain}/admin/personal/favorite'/>">
						    	<strong><fmt:message key="personalSocialMediaInfo.title"/></strong>
						    </a>
					    </li>
				    </ul>
				<div class="edit-mode no-ptop abs-left">
					<ul class="x-list work no-border">
						<c:choose>
							<c:when test="${urls.size()>0}">
						    	<c:forEach items="${urls}" var="url">
						    		<li>
						    			<a data-toggle="modal" class="float-right popup-edit" href="#editModal" onclick="document.getElementById('oper').value='edit';document.getElementById('favId').value='${url.id}'"><i class="icon-edit"></i><span class="icon-link"><fmt:message key='personal.common.edit'/></span></a>
					    				<a data-toggle="modal" class="float-right popup-remove"  href="#removeModal" onclick="document.getElementById('delFavId').value='${url.id}'"><i class="icon-trash"></i><span class="icon-link"><fmt:message key='personal.common.delete'/></span></a>
						    			<span title="${work.institutionName }">
								    		<img width="16px" height="16px" src="<dhome:imgIdOrName img="${url.img }"/>"/>
						    			</span>
						    			<span><c:choose><c:when test="${'other' eq url.selectMedia}">${url.title}</c:when><c:otherwise><fmt:message key='${url.selectMedia }'/></c:otherwise></c:choose></span>
						    			<span class="medialink-url">${url.url}</span> 
						    		</li>
						    	</c:forEach>
						    </c:when>
						    <c:otherwise>
						    	<fmt:message key="personalSocialMediaInfo.none"/>
						    </c:otherwise>
						</c:choose>
						</ul>
							<div class="d-center">
								<c:if test="${urls.size()<15 }">
									<a id="popup-add" href="#editModal" data-toggle="modal" class="btn btn-primary" onclick="document.getElementById('oper').value='add'"><fmt:message key='personal.common.add'/></a>
								</c:if>
								<c:if test="${urls.size()>=15 }">
									<font color="red"><fmt:message key="personalSocialMediaInfo.max"/></font>
								</c:if>
							</div>
						
				</div>
		    </div>
		</div>
		
	</div>
	<jsp:include page="commonfooter.jsp"></jsp:include>
	<div tabindex="-1" id="editModal" class="modal hide fade">
			<div class="modal-header">
	           <button type="button" class="close" data-dismiss="modal">×</button>
	           <h3><fmt:message key='personalSocialMediaInfo.title'/></h3>
         	</div>
         	
         	<form method="post"  id="editFavorite" class="form-horizontal no-bmargin" name="edit-workinfo" action="<dhome:url value='/people/${domain}/admin/personal/edit/favorite'/>">
			<fieldset>
				<input type="hidden" name="func" value="save">
				<input type="hidden" name="oper" id="oper" value="add">
				<input type="hidden" id="favId" name="favId" value="">
				<div class="modal-body">
					<div class="control-group">
	         			<label class="control-label"><fmt:message key="personalSocialMediaInfo.title"/>：</label>
	          			<div class="controls">
	            			<select name="selectMedia" id="favoriteSelect">
	            				<option value="weibo.com"><fmt:message key='weibo.com'/></option>
	            				<option value="t.qq.com"><fmt:message key='t.qq.com'/></option>
	            				<option value="t.sohu.com"><fmt:message key='t.sohu.com'/></option>
	            				<option value="renren.com"><fmt:message key='renren.com'/></option>
	            				<option value="blog.sina.com.cn"><fmt:message key='blog.sina.com.cn'/></option>
	            				<option value="user.qzone.qq.com"><fmt:message key='user.qzone.qq.com'/></option>
	            				<option value="www.douban.com"><fmt:message key='www.douban.com'/></option>
	            				<option value="profile.pengyou.com"><fmt:message key='profile.pengyou.com'/></option>
	            				<option value="qing.weibo.com"><fmt:message key='qing.weibo.com'/></option>
	            				<option value="blog.163.com"><fmt:message key='blog.163.com'/></option>
	            				<option value="www.linkedin.com"><fmt:message key='www.linkedin.com'/></option>
	            				<option value="other"><fmt:message key='other'/></option>
	            			</select>
	          			</div>
	        		</div>
					<div style="display:none" id="titleDiv" class="control-group">
	         			<label class="control-label"><fmt:message key="personalSocialMediaInfo.field.title"/></label>
	          			<div class="controls">
	            			<input id="title" placeholder="<fmt:message key='personalSocialMediaInfo.field.title.placeholder'/>" maxlength="55" type="text" name="title" value="" />
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key='personalSocialMediaInfo.field.url'/></label>
	          			<div class="controls">
	            			<input maxlength="200" type="text" name="url" id="url" value="" />
	          			</div>
	        		</div>
	        	</div>
	        	<div class="modal-footer">
					<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
					<button type="submit" id="save" class="btn btn-primary"><fmt:message key='common.save'/></button>
		        </div>
	        </fieldset>
        </form>
	</div>
	<div id="removeModal" tabindex="-1" class="modal hide fade">
		<div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">×</button>
	        <h3><fmt:message key='personalSocialMediaInfo.delete.title'/></h3>
        </div>
        <form method="post" class="form-horizontal no-bmargin" name="remove-workinfo" action="<dhome:url value='/people/${domain}/admin/personal/edit/favorite'/>">
			<input type="hidden" name="func" value="delete">
			<input type="hidden" id="delFavId" name="delFavId" value="">
			<fieldset>
				<div class="modal-body">
					<span><fmt:message key='personalSocialMediaInfo.delete.confirm'/></span>
					<p>
					<div id="removeInfo">
					</div>
				</div>
	        	<div class="modal-footer">
					<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
					<button type="submit" class="btn btn-primary"><fmt:message key='common.confirm.delete'/></button>
		        </div>
	        </fieldset>
        </form>
	</div>
</body>
<jsp:include page="commonheader.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	//根据select 的值初始化各种参数
	function initSelect(){
		var title=$("#favoriteSelect").find('option:selected').text();
		var url=$("#favoriteSelect").val();
		if(url=="other"){
			$('#title').val("");
			$('#url').attr("placeholder","<fmt:message key='personalSocialMediaInfo.field.url.placeholder'/>");
			$('#titleDiv').show();
		}else{
			$('#title').val(title);
			$('#url').attr("placeholder","http://"+url);
			$('#titleDiv').hide();
		}
	}
	
	//为下拉框绑定更换事件
	$("#favoriteSelect").live("change",initSelect);
	
	//根据Id获取一个社交媒体
	function getFavoriteUrl(id){
		var data;
		$.ajax({
			async:false,
			url : "<dhome:url value='/people/${domain}/admin/personal/favorite.json'/>",
			type : "POST",
			data : "id=" + id,
			success : function(rtData){
				data= rtData;
			}
		});
		return data;
	}
	//删除绑定事件
	$('#removeModal').on("show",function(){
		var data=getFavoriteUrl($('#delFavId').val());
		var htmlStr="<b>"+data.title+"\t"+data.url+"</b>";
		$("#removeInfo").html(htmlStr);
		
	})
	//弹出框 增/改 显示绑定事件
	$('#editModal').on("show",function(){
		if($('#oper').val()=='edit'){
			var data=getFavoriteUrl($('#favId').val());
			var isSelectItem=false;
			$("#favoriteSelect option").each(function(index,item){
				if($(item).val()==data.selectMedia){
					isSelectItem=true;
					$(item).attr('selected',true);
					return false;
				}
			})
			if(!isSelectItem){
				$("#favoriteSelect").val("other");
			}
			initSelect();
			$('#title').val(data.title);
			$('#url').val(data.url);
		}else{
			$("#favoriteSelect").val("weibo.com");
			initSelect();
			$('#url').val('');
		}
	});
	$('#editModal').on("hidden",function(){
		$("#favoriteSelect").val("weibo.com");
		$('#url').val('');
		$('#title').val('');
		$('label[class=error]').remove();
	})
	//前台验证
	$("#editFavorite").validate({
		rules:{
			title:{required:true, maxlength:50},
			url:{required:true,urlMine:true}
		},
		messages:{
			title:{
				required:"<fmt:message key='personalSocialMediaInfo.validate.title.required'/>",
				maxlength:"<fmt:message key='personalSocialMediaInfo.validate.title.maxLength'/>"},
			url:{
				required:"<fmt:message key='personalSocialMediaInfo.validate.url.required'/>"
			}
		}
	});
	 $.validator.addMethod("urlMine", function(value, element){
	    	return this.optional(element)||(isURL(value));
	    }, "<fmt:message key='personalSocialMediaInfo.validate.url.regix'/>");
	//自定义url验证规则
	function isURL(str_url){
		var strRegex = "^((https|http|ftp|rtsp|mms)?://)?(([0-9a-zA-Z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?" //ftp的user@
		+ "(([0-9]{1,3}.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
		+ "|" // 允许IP和DOMAIN（域名）
		+ "([0-9a-zA-Z_!~*'()-]+.)*" // 域名- www.
		+ "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]." // 二级域名
		+ "[a-zA-Z]{2,6})" // first level domain- .com or .museum
		+ "(:[0-9]{1,4})?" // 端口- :80
		+ "((/?)|" // a slash isn't required if there is no file name
		+ "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
		var re=new RegExp(strRegex);
		return re.test($.trim(str_url));
	}
		
});
</script>
</html>