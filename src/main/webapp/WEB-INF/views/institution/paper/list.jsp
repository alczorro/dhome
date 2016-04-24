<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<!DOCTYPE html>
<html lang="en">
<head>
<dhome:InitLanuage useBrowserLanguage="true"/>
<title>${titleUser.zhName }<fmt:message key="common.dhome"/></title>
<meta name="description" content="dHome" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
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
			<h3><fmt:message key="institution.paper.title"/></h3>
			</div>
			<jsp:include page="../menu.jsp"> 
			<jsp:param name="activeItem" value="paper" /> 
			<jsp:param name="memberItem" value="${flag }" />
			</jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
				<jsp:include page="../../commonPaper.jsp"><jsp:param name="activeItem" value="papers" /> </jsp:include>
				<div class="batch">
					<a class="summary btn btn-mini btn-success" style="font-size:12px;float:right;">高级检索</a>
					<span class="summary-content search" style="display:block">
					<div class="filterDetail">
						<p class="total">论文合计：<span>${page.allSize }</span>
						影响因子累计：<span>${totalIf }</span>
						引用频次累计：<span>${totalCitation }</span>
						</p>
						<div class="leftTitle">年份：</div>
						<div class="rightDetail">
							<ul class="years" id="pubYearsCond">
								<li <c:if test="${condition.year_min==0&&condition.year_max==0 }">class="active"</c:if> data-minyear="0" data-maxyear="0"><a><strong>全部</strong></a></li>
								<li <c:if test="${condition.year_min==year-4&&condition.year_max==year }">class="active"</c:if> data-minyear="${year-4 }" data-maxyear="${year }"><a>近五年(${year-4 }-${year })</a></li>
<%-- 								<li <c:if test="${condition.year_min==2001&&condition.year_max==2005 }">class="active"</c:if> data-minyear="2001" data-maxyear="2005"><a>2001-2005</a></li> --%>
<%-- 								<li <c:if test="${condition.year_min==2006&&condition.year_max==2010 }">class="active"</c:if> data-minyear="2006" data-maxyear="2010"><a>2006-2010</a></li> --%>
<%-- 								<li <c:if test="${condition.year_min==2011&&condition.year_max==2015 }">class="active"</c:if> data-minyear="2011" data-maxyear="2015"><a>2011-2015</a></li> --%>
							</ul>
							<span class="custom">
								<input type="text" id="minYear" class="price-range" title="最低数" value="${condition.year_min==0?'':condition.year_min }" autocomplete="off" /> <span>-</span> 
								<input type="text" id="maxYear" class="price-range" title="最高数" value="${condition.year_max==0?'':condition.year_max }" autocomplete="off" /><a class="btn btn-mini" id="yearSaveBtn">确定</a>
							</span>
						</div>
						<div class="leftTitle">影响因子：</div>
						<div class="rightDetail">
							<ul class="years" id="ifCond">
								<li <c:if test="${condition.if_min==0&&condition.if_max==0 }">class="active"</c:if> data-minif="0" data-maxif="0"><a><strong>全部</strong></a></li>
								<li <c:if test="${condition.if_min==0&&condition.if_max==1 }">class="active"</c:if> data-minif="0" data-maxif="1"><a><c:out value="IF<=1"></c:out></a></li>
								<li <c:if test="${condition.if_min==1&&condition.if_max==3 }">class="active"</c:if> data-minif="1" data-maxif="3"><a><c:out value="1<IF<=3"></c:out></a></li>
								<li <c:if test="${condition.if_min==3&&condition.if_max==5 }">class="active"</c:if> data-minif="3" data-maxif="5"><a><c:out value="3<IF<=5"></c:out></a></li>
								<li <c:if test="${condition.if_min==5&&condition.if_max==0 }">class="active"</c:if> data-minif="5" data-maxif="0"><a><c:out value="IF>5"></c:out></a></li>
							</ul>
							<span class="custom">
								<input type="text" id="minIf" class="price-range" title="最低数" value="${condition.if_min==0?'':condition.if_min }" autocomplete="off" /> <span>-</span> 
								<input type="text" id="maxIf" class="price-range" title="最高数" value="${condition.if_max==0?'':condition.if_max }" autocomplete="off" /><a class="btn btn-mini" id="ifSaveBtn">确定</a>
							</span>
						</div>
						<div class="leftTitle">引用频次：</div>
						<div class="rightDetail">
							<ul class="years" id="citationCond">
								<li <c:if test="${condition.citation_min==0&&condition.citation_max==0 }">class="active"</c:if> data-mincitation="0" data-maxcitation="0"><a><strong>全部</strong></a></li>
								<li <c:if test="${condition.citation_min==0&&condition.citation_max==100 }">class="active"</c:if> data-mincitation="0" data-maxcitation="100"><a><c:out value="0-100"></c:out></a></li>
								<li <c:if test="${condition.citation_min==101&&condition.citation_max==200 }">class="active"</c:if> data-mincitation="101" data-maxcitation="200"><a><c:out value="101-200"></c:out></a></li>
								<li <c:if test="${condition.citation_min==201&&condition.citation_max==300 }">class="active"</c:if> data-mincitation="201" data-maxcitation="300"><a><c:out value="201-300"></c:out></a></li>
								<li <c:if test="${condition.citation_min==300&&condition.citation_max==0 }">class="active"</c:if> data-mincitation="300" data-maxcitation="0"><a><c:out value="300以上"></c:out></a></li>
							</ul>
							<span class="custom">
								<input type="text" id="minCi" class="price-range" title="最低数" value="${condition.citation_min==0?'':condition.citation_min }" autocomplete="off" /> <span>-</span> 
								<input type="text" id="maxCi" class="price-range" title="最高数" value="${condition.citation_max==0?'':condition.citation_max }" autocomplete="off" /><a class="btn btn-mini" id="ciSaveBtn">确定</a>
							</span>
						</div>
						<div class="leftTitle">排序：</div>
						<div class="rightDetail">
							<div class="filter">
								<ul id="orderCondition">
									<li data-order="2" <c:if test="${condition.order==2||condition.order==0 }">class="active"</c:if>><a>按发表时间排序<i ></i></a></li>
									<li data-order="1" <c:if test="${condition.order==1 }">class="active"</c:if>><a>按第一作者排序<i ></i></a></li>
									<li data-order="3" <c:if test="${condition.order==3 }">class="active"</c:if>><a>按引用频次排序<i ></i></a></li>
									<li data-order="5" <c:if test="${condition.order==5 }">class="active"</c:if>><a>按影响因子排序<i ></i></a></li>
								</ul>
							</div>
						</div>
					</div>
					</span>
				</div>
				<ul class="listShow">
					<li class="title">
						<!-- <span class="check"><input type="checkbox"  id="checkAll"/></span> -->
						<span class="article">标题</span>
						<span class="author">作者</span> 
						<span class="quot">影响因子</span>
						<span class="quot">引用频次</span>
					</li>
					<c:forEach items="${page.datas }" var="data">
						<li>
							<%-- <span class="check"><input data-paper-id="${data.id }" type="checkbox" class="checkItem"/></span> --%>
							<span class="article">
								<span class="title"><c:out value="${data.title }"/></span>
								<span><c:if test="${data.status==1 }"><i class="icon icon-ok"></i></c:if></span>
								<span class="detail">
									<!-- doi -->
									<c:if test="${!empty data.doi }">
										dio:<c:out value="${data.doi }"/>. 
									</c:if>
									<!-- issn -->
									<c:if test="${!empty data.issn }">
										issn:<c:out value="${data.issn }"/>. 
									</c:if>
									<!-- 刊物 -->
									<c:if test="${data.publicationId!=0 }">
										<c:out value="${pubMap[data.publicationId].pubName }"/>
									</c:if>
									<!-- 卷号 -->
									<c:if test="${!empty data.volumeNumber }">
										卷:<c:out value="${data.volumeNumber }"/>. 
									</c:if>
									<!-- 期号 -->
									<c:if test="${!empty data.series }">
										期:<c:out value="${data.series }"/>. 
									</c:if>
									<!-- 开始页~结束页 -->
									<c:if test="${!empty data.publicationPage }">
										页:${data.publicationPage}. 
									</c:if>
<!-- 									开始页~结束页 -->
<%-- 									<c:if test="${!empty data.startPage||!empty data.endPage }"> --%>
<%-- 										页:${data.startPage}~${data.endPage==0?'无':data.endPage}.  --%>
<%-- 									</c:if> --%>
									<!-- 发表时间 年.月 -->
									出版年:${data.publicationYear}<c:if test="${data.publicationMonth!=0 }">.${data.publicationMonth}</c:if>. 
								</span>
									<!-- 原文链接 -->
									<c:if test="${!empty data.originalLink }">
										<a href="<c:out value="${data.originalLink }"/>" target="_blank">原文链接</a>
									</c:if>
									<!-- 原文下载 -->
									<c:if test="${data.clbId!=0 }">
										<a href="<dhome:url value="/system/file?fileId=${data.clbId }"/>" target="_blank">原文下载</a>
									</c:if>
									<!-- 摘要 -->
									<c:if test="${!empty data.summary}">
										<a data-paper-id="${data.id }" class="showSummary">查看摘要</a>
									</c:if>
									<span class="manage">
										<a class="label label-success" href="<dhome:url value="../detail/${data.id }?returnPage=${page.currentPage }"/>">查看</a>
										<a class="label label-danger deletePaper" href="<dhome:url value="../delete?paperId[]=${data.id }&page=${page.currentPage }"/>">删除</a>
<%-- 										<c:if test="${papersMap[data.id].status!=1 }"> --%>
<%-- 										<a class="label label-success" href="<dhome:url value="/people/${domain }/admin/paper/update/${data.id }?returnPage=${page.currentPage }"/>">编辑</a> --%>
<%-- 										</c:if> --%>
									</span>
									<div id="summary_${data.id }" class="summaryDiv" style="display:none"><c:out value="${data.summary }"/></div>
							</span>
							<span class="author">
								<c:choose>
									<c:when test="${empty authorMap[data.id]}">
										--
									</c:when>
									<c:otherwise>
										<ul>
										<c:forEach items="${ authorMap[data.id]}" var="author">
											<li >
											<a class="authorDetailPopover"  data-paper-id="${data.id }" data-author-id="${author.id }" ><c:out value="${author.authorName }"/></a>
												<sup>[${author.subscriptIndex }]</sup>
											</li>
										</c:forEach>
										</ul>
										<c:if test="${!empty authorMap[data.id] && data.authorAmount>fn:length(authorMap[data.id]) }"><span style="color:#999; float:left; font-size:11px; margin-top:5px;">等</span></c:if>
									</c:otherwise>
								</c:choose>
							</span>
							<span class="quot">
								<c:if test="${empty pubMap[data.publicationId].ifs}">--</c:if>
								<c:if test="${!empty pubMap[data.publicationId].ifs}">${pubMap[data.publicationId].ifs}</c:if>
							</span>
							<span class="quot">
						        <c:choose>
							       <c:when test="${data.citation == '-1' || empty data.citation}">
											--   
									</c:when> 
									<c:otherwise>
										<c:out value="${data.citation}"/>
									</c:otherwise>
								</c:choose>
							</span>
						</li> 
					</c:forEach>
				</ul>
				<c:choose>
					<c:when test="${page.maxPage!=0 }">	
						<div class="pages">
							<a class="first">首页</a>
							<c:if test="${page.currentPage!=1 }">
								<a class="prev"><i class="prev-triggle"></i>上一页</a>
							</c:if>
							<span id="pageNav"> </span>
							<c:if test="${page.currentPage!=page.maxPage }">
								<a class="next" >下一页<i class="next-triggle"></i></a>
							</c:if>
							<a class="last">尾页</a>
						</div>
					</c:when>
					<c:otherwise>
						<p class="msg">没有匹配记录</p>
					</c:otherwise>
				</c:choose>
			</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<jsp:include page="../../commonfooter.jsp"></jsp:include>

</body>
<jsp:include page="../../commonheader.jsp"></jsp:include>
<script src="<dhome:url value="/resources/scripts/nav.js"/>"></script>
	<script src="<dhome:url value="/resources/scripts/check.util.js"/>"></script>
	<script>
		$(document).ready(function(){
			$("#viewMyPage").attr("href","<dhome:url value='/people/${domain}?isIAP="+2+"'/>");
			
			var orderType=parseInt('${condition.orderType}');
			var order=getOrder();
			if(order!=0){
				var orderTypeClass=['icon-arrow-up','icon-arrow-down'];
				$('#orderCondition li.active a i').attr('class',orderTypeClass[orderType-1]);
			}
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
			$('.deletePaper').on('click',function(){
				return(confirm("确认删除该论文？"));
			});
			$('#batchBtn').on('click',function(){
				var operation=$('#batchOperation').val();
				if(operation=='batchDelete'){
					var url='';
					$('.checkItem:checked').each(function(i,n){
						url+="&paperId[]="+$(n).data('paperId');
					});
					if(url==''){
						alert('请选择要删除的论文');
						return;
					}
					$.get("<dhome:url value='/people/${domain}/admin/paper/delete?page=${page.currentPage}'/>"+url).done(function(data){
						if(data.success){
							window.location.reload();
						}else{
							alert(data.desc);
						}
					});
					
				}else{
					alert('请选择批量操作'); 
				}
			});
			//点击作者查看详情
			$('.authorDetailPopover').on('click',function(){
				$('.authorDetailPopover').popover('destroy');
				var authorId=$(this).data('authorId');
				if($('#popover_'+authorId).size()==0){
					var $self=$(this);
					var data=$(this).data();
					var paperId=data.paperId;
					var authorId=data.authorId;
					$.get('<dhome:url value="/people/${domain}/admin/paper/author/"/>'+paperId+"/"+authorId).done(function(result){
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
			//查看摘要
			$('.showSummary').on('click',function(){
				var paperId=$(this).data('paperId');
				$('#summary_'+paperId).toggle();
			});
			//用第几个查询的order？
			function getOrder(){
				var $selectedOrderCond=$('#orderCondition li.active');
				if($selectedOrderCond.size()==0){
					return 2;
				} 
				return $selectedOrderCond.data('order');
			};
			//根据查询条件查询
			function search(baseUrl){
				if((baseUrl+'').indexOf('?')==-1){
					baseUrl+="?_=";
				}
// 				if(!$('#pubYearsCond li.active')[0]){
// 					alert("请选择论文！");
// 					return false;
// 				}else{
// 					baseUrl+="&publicationYear="+$('#pubYearsCond li.active').data('years');
// 				}
				baseUrl+="&year_min="+($('#minYear').val()==''?0:$('#minYear').val());
				baseUrl+="&year_max="+($('#maxYear').val()==''?0:$('#maxYear').val());
				
				baseUrl+="&if_min="+($('#minIf').val()==''?0:$('#minIf').val());
				baseUrl+="&if_max="+($('#maxIf').val()==''?0:$('#maxIf').val());
				
				baseUrl+="&citation_min="+($('#minCi').val()==''?0:$('#minCi').val());
				baseUrl+="&citation_max="+($('#maxCi').val()==''?0:$('#maxCi').val());
				
				baseUrl+="&order="+getOrder();
				baseUrl+="&orderType="+orderType;
// 				if($('#searchType').val()==1){
// 					baseUrl+="&keyword="+encodeURIComponent($.trim($('#paperKeyword').val()));
// 				}
// 				if($('#searchType').val()==2){
// 					baseUrl+="&keyword2="+encodeURIComponent($.trim($('#paperKeyword').val()));
// 				}
// 				if($('#searchType').val()==3){
// 					baseUrl+="&keyword3="+encodeURIComponent($.trim($('#paperKeyword').val()));
// 				}
				window.location.href=baseUrl;
			}
			
			checkAllBox('checkAll','checkItem');
			
			var pageNav=new PageNav(parseInt('${page.currentPage}'),parseInt('${page.maxPage}'),'pageNav');
			pageNav.setToPage(function(page){
				window.location.href=page;
			});
			
			$('#pubYearsCond li').on('click',function(){
 				$('#pubYearsCond li').removeClass('active');
// 				$(this).addClass('active');
				$(this).addClass('active');
				$('#minYear').val($('#pubYearsCond li.active').data('minyear')==0?'':$('#pubYearsCond li.active').data('minyear'));
				$('#maxYear').val($('#pubYearsCond li.active').data('maxyear')==0?'':$('#pubYearsCond li.active').data('maxyear'));
				search(1);
			});
			$('#ifCond li').on('click',function(){
				$('#ifCond li').removeClass('active');
				$(this).addClass('active');
				$('#minIf').val($('#ifCond li.active').data('minif')==0?'':$('#ifCond li.active').data('minif'));
				$('#maxIf').val($('#ifCond li.active').data('maxif')==0?'':$('#ifCond li.active').data('maxif'));
				search(1);
			});
			$('#citationCond li').on('click',function(){
				$('#ifCond li').removeClass('active');
				$(this).addClass('active');
				$('#minCi').val($(this).data('mincitation')==0?'':$(this).data('mincitation'));
				$('#maxCi').val($(this).data('maxcitation')==0?'':$(this).data('maxcitation'));
				search(1);
			});
			$('#minYear').blur(function(){
				var min=$('#minYear').val();
				if(!(parseInt(min)==min)){
					$('#minYear').val('');
				}
				});
			$('#maxYear').blur(function(){
				var max=$('#maxYear').val();
				if(!(parseInt(max)==max)){
					$('#maxYear').val('');
				}
				});
			$('#yearSaveBtn').on('click',function(){
				var min=$('#minYear').val();
				var max=$('#maxYear').val();
				if(min>max&&max!=''){
					$('#minYear').val(max);
					$('#maxYear').val(min);
				}
				search(1);
			});
			$('#minIf').blur(function(){
				var min=$('#minIf').val();
				if(!(parseInt(min)==min)){
					$('#minIf').val('');
				}
				});
			$('#maxIf').blur(function(){
				var max=$('#maxIf').val();
				if(!(parseInt(max)==max)){
					$('#maxIf').val('');
				}
				});
			$('#ifSaveBtn').on('click',function(){
				var min=$('#minIf').val();
				var max=$('#maxIf').val();
				if(min>max&&max!=''){
					$('#minIf').val(max);
					$('#maxIf').val(min);
				}
				search(1);
			});
			$('#minCi').blur(function(){
				var min=$('#minCi').val();
				if(!(parseInt(min)==min)){
					$('#minCi').val('');
				}
				});
			$('#maxCi').blur(function(){
				var max=$('#maxCi').val();
				if(!(parseInt(max)==max)){
					$('#maxCi').val('');
				}
				});
			$('#ciSaveBtn').on('click',function(){
				var min=$('#minCi').val();
				var max=$('#maxCi').val();
				if(min>max&&max!=''){
					$('#minCi').val(max);
					$('#maxCi').val(min);
				}
				search(1);
			});
			$('#orderCondition li').on('click',function(){
				$('#orderCondition li').removeClass('active');
				$(this).addClass('active');
				if(!$(this).find('a i').attr('class')){
					orderType=1;
				}else{
					orderType=(orderType==2?1:orderType+1);
				}  
				search(1);
			});
			$('#searchPaperBtn').on('click',function(){
				search(1);
			});
			$('#paperKeyword').enter(function(){
				search(1);
			});
			
			$("a.summary").toggle(function(event){
				$(this).parent().children(".summary-content").slideUp();
				event.preventDefault();
			}, function(event){
				$(this).parent().children(".summary-content").slideDown();
				event.preventDefault();
			});
			
			$('#paperMenu').addClass('active');
		}); 
	</script>
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<li>姓名：{{= author.authorName}}</li>
		<li>邮箱：{{= author.authorEmail}}</li>
		<li>单位：{{= author.authorCompany}}</li>
		{{if author.communicationAuthor}}
			<li>通讯作者：是</li>
		{{/if}}
		{{if author.authorStudent}}
			<li>学生在读：是</li>
		{{/if}}
		{{if author.authorTeacher}}
			<li>第一作者导师：是</li>
		{{/if}}
		{{if home}}
			<li><a href="<dhome:url value="/people/{{= home}}"/>" target="_blank">个人主页</a></li>
		{{/if}}
	</script>

</html>