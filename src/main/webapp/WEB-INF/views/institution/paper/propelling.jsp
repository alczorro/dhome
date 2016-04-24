<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
<dhome:InitLanuage useBrowserLanguage="true"/>
<title>${titleUser.zhName }<fmt:message key="common.dhome"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.css"/>" />
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
			<h3>论文管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="paper" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonPaper.jsp"><jsp:param name="activeItem" value="propelling" /> </jsp:include>
					<form name="search-paper-form" class="form-search form-horizontal" action="<dhome:url value='/people/${domain}/admin/paper/propelling/submit'/>" method="POST">
						<div class="userToAuthor">
							<div class="control-group">
								<label class="control-label">您曾使用过的作者名：</label>
								<div class="controls padding">
									<c:forEach items="${authors }" var="author">
										<c:if test="${author.status==1 }">
											<p class="color-ok">${author.authorName }<c:if test="${!empty author.authorEmail }">[${author.authorEmail }-${author.authorCompany }]</c:if>（<i class="icon icon-user"></i>已认证）</p>
										</c:if>
										<c:if test="${author.status!=1 }">
											<p>${author.authorName }<c:if test="${!empty author.authorEmail }">[${author.authorEmail}-${author.authorCompany } ]</c:if>（未认证）
											<a id="deletePropelling" href="<dhome:url value='/people/${domain}/admin/paper/propelling/delete?authorId=${author.id }'/>" onclick="if(confirm('确定删除?')==false)return false;"> 删除</a>
											</p>
										</c:if>
									</c:forEach>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">添加作者名：</label>
								<div class="controls">
									<input type="text" name="authorSearch" class="autoCompleteSearch register-xlarge" id="authorSearch"/>
									<p class="hint">注：这里不能新建作者名，只能添加已存在的作者，如需新建作者，请到相应的学术成果中添加。
									<br>请勿添加其他人的作者名，以免影响您的推送结果。</p>
									<ul id="authorTable"></ul>
								</div>
							</div>
							<div class="control-group">
								<div class="controls">
									<a class="btn btn-mini btn-primary" id="addAuthor">确定添加</a>
								</div>
							</div>
						</div>
						<input type="hidden" name="authorIds" value="">
				      <input type="hidden" name="paperIds" value="">
				        <fieldset>
				           <div class="control-group">
					          <ul class="striped eighty-per no-border small-font" id="paper-up" style="margin-bottom:0;"></ul>
					          <div id="ctrlBtn" class="form-actions clear-b" style="display:none;padding-left:0; margin-top:0;">
					          	<button id="save-search" class="btn btn-primary btn-set-middle">保存</button>
					          	<a class="btn" href="<dhome:url value='/people/${domain}/admin/paper/propelling'/>">取消</a>
					          </div>
				              <input type="text" name="paper-keyword" class="input-medium input-xlarge" placeholder="请输入论文的名称或者作者">
	      					  <a id="search-paper" class="btn" type="button" class="btn">搜索</a>
	      					  <div class="small-font subText">通过搜索关键词：<b><span id="defaultKeywords"></span></b> 为您找到以下论文，请确认哪些是您发表过的论文。</div>
				           </div>
				          <ul id="paper-down" class="striped eighty-per small-font">
				          	<li class="more-artical" id="loadmore"><a>更多论文</a></li>
				          </ul>
				          <p id="notice" class="more-artical dhome2">正在载入...</p>
				          
				      </fieldset>
				    </form>
		</div>
	</div>
	</div>
</div>

	<jsp:include page="../../commonfooter.jsp"></jsp:include>

</body>
<jsp:include page="../../commonheader.jsp"></jsp:include>
<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
<script type="text/javascript">
	$(document).ready(function(){
		var url = "<dhome:url value='/people/${domain}/admin/paper/propelling/paper'/>";
		var offset = 0;
		var size = 10;
		var curKeyword = "";
		
		var notice_handler = {
				loading: function() {
					$('#loadmore').hide();
					$('#notice').addClass('large').text("正在载入...").fadeIn();
				},
				noMatch: function() {
					$('#loadmore').hide();
					$('#notice').addClass('large').text("系统没有找到您的论文，您可以更改关键词再次搜索。").show();
				},
				noMore: function() { 
					$('#loadmore').hide();
					setTimeout(function(){
						$('#notice').removeClass('large').text("没有更多论文了").show();
					}, 500);
				},
				readyToLoad: function() {
					$('#loadmore').show();
					$('#notice').hide();
				},
				error : function(){
					$('#loadmore').hide();
					$('#notice').removeClass('large').text("请求失败！可能由于以下原因导致此问题：未登录，会话过期或权限不够！").fadeIn();
				}
		};
		
		initPaper();
		function initPaper(){
			var params = {"offset":offset, "size":size, "existPapers":getSelectedPaperIds()};
			notice_handler.loading();
			ajaxRequest(url, params, renderData, errorHandler);
		}
		
		function ajaxRequest(url, params, successCall, errorCall){
			$.ajax({
				url:url,
				type:'POST',
				data: params,
				success:successCall,
				error:errorCall
			});
		}
		
		function renderData(data){
 			var len = data.result.length;
//  			alert(len);
			$("#defaultKeywords").text(data.searchKeywords);
			if(len>0){
				$("#paper-template").tmpl(data.result).insertBefore("#loadmore");
				offset += len;
				if(len<size){
					notice_handler.noMore();
				}else{
					notice_handler.readyToLoad();
				}
			}else{
				if(offset ==0){
					notice_handler.noMatch();
				}else{
					notice_handler.noMore();
				}
			}
		}
		
		function errorHandler(){
			notice_handler.error();
		}
		
		$("button.btn-success").live("click",function(){
			$(this).parent().hide();
			$(this).parent().prev().show();
			$(this).parents("li").clone().appendTo("#paper-up");
			$(this).parents("li").hide();
			$("#paper-up").removeClass("no-border");
			$("#ctrlBtn").show();
			
		});
		
		$("a.not-my-paper").live("click",function(){
			$(this).hide();
			$(this).prev().hide();
			$(this).prev().prev().show();
			$(this).parents("li").find("span").css({"color":"#999","text-decoration":"line-through"});
		});
		
		$("button.undo").live("click",function(){
			$(this).hide();
			$(this).next().show();
			$(this).next().next().show();
			$(this).parents("li").find("span").css({"color":"#000","text-decoration":"none"});
		});
		
		$("button.close").live("click", function(){
			var paperId = $(this).attr("paper_id");
			$(this).parents("li").remove();
			var $liBtn = $("li button[paper_id="+paperId+"]");
			$liBtn.parent().hide();
			$liBtn.parent().next().show();
			$liBtn.parents("li").show();
			if($("#paper-up").html()==''){
				$("#ctrlBtn").hide();
			}
		});
		
		$("#loadmore").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=paper-keyword]").val();
			if(curKeyword!=keyword){
				curKeyword = keyword;
				offset = 0;
			}
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existPapers":getSelectedPaperIds()};
			ajaxRequest(url, params, renderData, errorHandler);
		});
		
		$("#search-paper").click(function(){
			notice_handler.loading();
			var keyword = $("input[name=paper-keyword]").val();
			curKeyword = keyword;
			offset = 0;
			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existPapers":getSelectedPaperIds()};
			ajaxRequest(url, params, renderNewResearch, errorHandler);
		});
		$("#addAuthor").click(function(){
			$("input[name=paperIds]").val(getSelectedPaperIds());
			$("input[name=authorIds]").val(getSelectedAuthorIds());
			$("form[name=search-paper-form]").submit();
// 			notice_handler.loading();
// 			var keyword = $("input[name=paper-keyword]").val();
// 			curKeyword = keyword;
// 			offset = 0;
// 			var params = {"keyword":$.trim(keyword), "offset":offset, "size":size, "existPapers":getSelectedPaperIds()};
// 			ajaxRequest(url, params, renderNewResearch, errorHandler);
// 			$.get("<dhome:url value='/institution/${domain}/backend/paper/propelling/submit?authorIds='/>"+getSelectedAuthorIds()).done(function(data){
// 				alert(data);
// 				if(data.success){
// 					window.location.reload();
// 				}else{
// 					alert(data.desc);
// 				}
// 			});
		});
		
		$("input[name=paper-keyword]").keyup(function(event){
			if(event.which == 13){
				$("#search-paper").click();
				event.preventDefault();
				event.stopPropagation();
			}
		}).keydown(function(event){
			if(event.which == 13){
				event.preventDefault();
				event.stopPropagation();
			}
		});
		
		function renderNewResearch(data){
			$("#paper-down li:not(#loadmore)").remove();
			renderData(data);
		}
		
		function getSelectedPaperIds(){
			var paperIds = new Array();
			$("#paper-up li:not(#loadmore)").each(function(){
				var paperId = $(this).find("button.close[paper_id]").attr("paper_id");
				paperIds.push(paperId);
			});
			return paperIds.toString();
		}
		function getSelectedAuthorIds(){
			var authorIds = new Array();
			$("#authorTable li").each(function(){
				var authorId = $(this).find(".removeAuthor[data-uid]").data('uid');
				authorIds.push(authorId);
			});
			return authorIds.toString();
		}
		
		$("#save-search").click(function(){
			$("input[name=paperIds]").val(getSelectedPaperIds());
			$("input[name=authorIds]").val(getSelectedAuthorIds());
			$("form[name=search-paper-form]").submit();
		});
		
		
		//作者队列
		var author={
				//数据
				data:[],
				//删除
				remove:function(id){
					for(var i in this.data){
						if(this.data[i].id==id){
							this.data.splice(i,1);
							this.renders();
							return;
						}
					}
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
				//插入
				append:function(item){
					if(this.isContain(item)){
						return false;
					}
					this.data.push(item);
					this.renders();
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
				//控制表格显示
				renders:function(){
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
						$('#authorTable').html($('#author-template').tmpl(this.data,{
							judiceBool:function(bool){
								return bool?'true':'false';
							}
						})); 
					}
				}
		};
		
		//添加作者名按钮
		$('#searchAuthor').on('click',function(){
			$('#add-author-popup').modal('show');
// 			$('#addPubForm').get(0).reset();
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
						
// 						if(item.status==-1){
// 							alert("该作者已提交！");
// 						}
						var authors=$.parseJSON('${json}');
						for(var i=0;i<authors.length;i++){
							if(authors[i].id==item.id){
								alert("该作者已在列表！");
								return false;
							}
						}
						if(item.status==1){
							alert("该作者已被其他用户认领！");
							return false;
						}
						var success=author.append(item);
						if(!success){
							alert('请勿重复添加');
						}
						$('#add-author-popup').modal('hide');
					});
		//删除作者
		$('.removeAuthor').live('click',function(){
			if(confirm("确定移除该作者？")){
				var email=$(this).data('uid');
				author.remove(email);
			}
		});
		
	});
</script>
<script type="text/html" id="paper-template">
<li>
	<span class="d-long">
		<span style="display:inline-block; margin-right:1em"><strong>{{= title}}</strong></span>
		<span style="display:inline-block; margin-right:1em" title="{{= author}}">{{= author}}</span>
		<span style="display:inline-block; margin-right:1em">{{= publicationName}}</span>
		<span style="display:inline-block; margin-right:1em">{{= publicationYear}}</span>
	</span>
	<span class="d-short paper-up" style="display:none">
		<button type="button" class="close" paper_id="{{= id}}">×</button>
	</span>
	<span class="d-short paper-down">
		<button type="button" style="display:none" class="btn d-sbtn undo" paper_id="{{= id}}">撤销</button>
		<button type="button" class="btn btn-success d-sbtn" paper_id="{{= id}}">是</button>
		<a class="not-my-paper" paper_id="{{= id}}">不是</a>
	</span>
	<div class="clear"></div>
</li>
</script>
<script type="text/x-jquery-tmpl" id="author-template">
<li>
	<span>{{= authorName}}</span>
	<span>{{= authorEmail}}</span>
	<span>{{= authorCompany}}</span>
	{{if status!=1}}<span><a class="removeAuthor" data-uid="{{= id}}"><i class="icon icon-trash"></i></a></span>{{/if}}
</li>
</script>
</html>