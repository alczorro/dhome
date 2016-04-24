<%@ page language="java" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<dhome:InitLanuage useBrowserLanguage="true" />
<jsp:include page="commonheaderCss.jsp"></jsp:include>
<input type="hidden" id="contextPath"
	value="<%=request.getContextPath()%>" />
	
<form id="editForm"
	action="<dhome:url value="/people/${domain }/admin/p/addPage"/>"
	method="post">
	
	<div>
		<b><fmt:message key='editor.pageTitle' /></b>
		<button type="button" onclick="cancel();"
			class="btn btn-mini float-right">
			<fmt:message key='common.cancel' />
		</button>
		<button type="submit"
			class="saveButton btn btn-primary btn-mini float-right"
			style="margin-right: 10px;">
			<fmt:message key='common.save' />
		</button>
		<div class="clear"></div>
		<input maxlength="50" type="text" placeholder="<fmt:message key="editor.title.placeholder"/>" id="editor_title" class="d-through x-top"
			name="title" value="<c:out value="${page.title }"/>" />
	</div>
	<div class="gray-text small-font">
		<b><fmt:message key='editor.pageUrl' /></b>http://www.escience.cn<%=request.getContextPath()%>/people/${domain
		}/
		<c:choose>
			<c:when test="${page.keyWord!='index' }">
				<input type="text" value="<c:out value='${page.keyWord }'/>"
					id="pageUrl" name="pageUrl" class="d-ss" />
			</c:when>
			<c:otherwise>
			${page.keyWord }
			<input type="hidden" value="<c:out value='${page.keyWord }'/>"
					id="pageUrl" name="pageUrl" class="d-ss" />
			</c:otherwise>
		</c:choose>
		.html
	</div>
	<input type="hidden" name="pid" value="${page.id }" /> 
	<input type="hidden" name="itemId" value="${item.id }" /> 
	<strong class="small-font"><fmt:message key="editor.insert" />
	</strong>
	<a href="javascript:showPaper();" data-toggle="modal" class="block-link icon16"><i class="icon-list-alt"></i><fmt:message key='editor.insertPaper' /></a>
	<a href="javascript:showWork();" data-toggle="modal" class="block-link icon16" id="work"><i class="icon-briefcase"></i><fmt:message key='editor.insertWork' /></a> 
	<a href="javascript:showEdu();" data-toggle="modal" class="block-link icon16" id="edu"><i class="icon-book"></i><fmt:message key='editor.insertEdu' /></a>
	<a href="javascript:$EDITORUI['edui151']._onClick();" class="block-link icon16 maintain"><i class="icon-file"></i><fmt:message key="editor.insertFile"/></a>
	<a href="javascript:$EDITORUI['edui141']._onClick();" class="block-link icon16 maintain"><i class="icon-camera"></i><fmt:message key="editor.insertImg"/></a>
	<a id="insertLatex" class="block-link icon16 maintain"><i class="icon-formula"></i><fmt:message key='editor.formula.insert'/></a>
	
	<textarea  id="myEditor">
	<c:if test="${(page.title=='English version')&&newPage }">
		<div class="page-container" style="margin-top:-2em;">
			<p></p>
			<p><strong><em>1 Full Name:</em></strong></p>
<!-- 			<p>Huang &nbsp; &nbsp;Gang</p><p>(Family) (First) </p> -->
			<p><strong><em>2 Date and Place of Birth:</em></strong></p>
<!-- 			<p> Nov12th, 1971 in Beijing</p> -->
			<p><strong><em>3 Sex:</em></strong></p>
<!-- 			<p> Male</p> -->
			<p><strong><em>4 Present Address:</em></strong></p>
<!-- 			<p style="margin-left:48px;">Key Laboratory of Regional Climate-Envirment for East Asia (RCE-TEA)Institute of Atmospheric Physics, Chinese Academy of Sciences</p> -->
<!-- 			<p style="text-indent:48px">P.O.Box 9804, Beijing100029, P.R.China</p><p style="text-indent:48px">Telephone: +86-10-82995312</p><p style="text-indent:48px">Fax: +86-10-82995135&amp; +86-10-62560390</p><p style="text-indent:48px">E-mail: <a href="mailto:hg@mail.iap.ac.cn">hg@mail.iap.ac.cn</a>, <a href="mailto:hgiap@hotmail.com">hgiap@hotmail.com</a> (permanent)</p> -->
			<p><strong><em>5 Academic Positions</em></strong>:  </p>
		</div>
	</c:if>
	${page.content }</textarea>
	<input type="hidden" id="finalContent" name="content"/>
	<div class="x-top">
		<c:if test="${page.keyWord!='index' }">
			<input name="notRealease" type="checkbox"
				<c:if test="${item.status==0}"></c:if>><fmt:message key='editor.notPublishedYet' />
		</c:if>
	</div>
	<div class="d-top">
		<button type="submit" class="saveButton btn btn-primary">
			<fmt:message key='common.save' />
		</button>
		<button type="button" onclick="cancel();" class="btn">
			<fmt:message key='common.cancel' />
		</button>
	</div>
</form>
<div tabindex="-1" id="myPaperModal" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">×</button>
		<h3>
			<fmt:message key='common.paper.title' />
		</h3>
	</div>
	<div class="modal-body">
		<div class="control-group">
			<div class="controls">
				<div id="divPaperCheck">
					<input id="checkPaperAll" type="checkbox" class="d-sbottom">
					<fmt:message key="common.selectAll" />
				</div>
				<ul id="paperItems" class="striped paper-list">
				</ul>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><fmt:message
				key='common.cancel' /></a> <a href="javascript:insertPaper();"
			class="btn btn-primary"><fmt:message key='common.confirm' /></a>
	</div>
</div>
<div tabindex="-1" id="myWorkModal" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">×</button>
		<h3>
			<fmt:message key='editor.workList' />
		</h3>
	</div>
	<div class="modal-body">
		<div class="control-group">
			<div class="controls">
				<div id="divWorkCheck">
					<input id="checkWorkAll" type="checkbox" class="d-sbottom">
					<fmt:message key='common.selectAll' />
				</div>
				<ul id="workItems" class="striped">
				</ul>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><fmt:message
				key='common.cancel' /></a> <a href="javascript:insertWork();"
			class="btn btn-primary"><fmt:message key='common.confirm' /></a>
	</div>
</div>
<div tabindex="-1" id="insertLatex" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">×</button>
		<h3>
			<fmt:message key='editor.workList' />
		</h3>
	</div>
	<div class="modal-body">
		<div class="control-group">
			<div class="controls">
				<div id="divWorkCheck">
					<input id="checkWorkAll" type="checkbox" class="d-sbottom">
					<fmt:message key='common.selectAll' />
				</div>
				<ul id="workItems" class="striped">
				</ul>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><fmt:message
				key='common.cancel' /></a> <a href="javascript:insertWork();"
			class="btn btn-primary"><fmt:message key='common.confirm' /></a>
	</div>
</div>
<div tabindex="-1" id="myEduModal" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">×</button>
		<h3>
			<fmt:message key='editor.educationList' />
		</h3>
	</div>
	<div class="modal-body">
		<div class="control-group">
			<div class="controls">
				<div id="divEduCheck">
					<input id="checkEduAll" type="checkbox" class="d-sbottom">
					<fmt:message key='common.selectAll' />
				</div>
				<ul id="eduItems" class="striped">
				</ul>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><fmt:message
				key='common.cancel' /></a> <a href="javascript:insertEdu();"
			class="btn btn-primary"><fmt:message key='common.confirm' /></a>
	</div>
</div>

<div tabindex="-1" id="saveHint" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">×</button>
		<h3>
			<fmt:message key='common.hint' />
		</h3>
	</div>
	<div class="modal-body">
		<fmt:message key='hint.save' />
	</div>
	<div class="modal-footer">
		<a href="#" class="btn btn-primary" data-dismiss="modal"><fmt:message
				key='common.confirm' /></a>
	</div>
</div>

<div tabindex="-1" id="insertLatexDialog" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">×</button>
		<h3>
			<fmt:message key='editor.formula.edit'/>
		</h3>
	</div> 
	<div class="modal-body">
                <div id="formulaMenu" class="formulaMenu">
                </div>
		<textarea id="MathInput" style="width:718px; resize:none; height:13em; border-radius:0; padding:0"></textarea>
		<div id="MathPreview" style="border:1px solid #ccc; padding: 3px 0px; width:718px; margin-top:5px; min-height:3em;"></div>
		<div id="MathBuffer" style="border:1px solid #ccc;  padding: 3px 0px; width:718px; margin-top:5px; min-height:3em; visibility:hidden; position:absolute; top:0; left: 0"></div>
		
	</div>
	<div class="modal-footer">
		<a id="insertLatexOk" class="btn btn-primary" data-dismiss="modal"><fmt:message key='common.confirm' /></a>
		<a href="insertLatexCancel" class="btn" data-dismiss="modal"><fmt:message key='common.cancel'/></a>
	</div>
</div>


<div id="insertHTML" style="display:none"></div>
<dhome:IsMainTain reverse="true">
	<script type="text/javascript" src="<dhome:url value="/resources/third-party/ueditor/editor_config.js"/>"></script>
</dhome:IsMainTain>
<dhome:IsMainTain>
<script type="text/javascript" src="<dhome:url value="/resources/third-party/ueditor/editor_config_maintain.js"/>"></script>
</dhome:IsMainTain>
<script type="text/javascript" src="http://www.escience.cn/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
<script type="text/javascript" src="<dhome:url value="/resources/third-party/ueditor/editor_all.js"/>"></script>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/latex.config.js"/>"></script>
<script>
$(document).ready(function(){
	//init latex select
	for(var i=0;i<latexConfig.length;i++){
		var title=latexConfig[i][0];
		var display=latexConfig[i][1]
		var html="";
		if(title==='|'||display==='|'){ 
			html='<span class="divider">|</span>';
		}else{
			html='<span class="formulaBtn" title="'+title+'">'+display+'</span>';
		}
		$('#formulaMenu').append(html);
	}
	
	$('#formulaMenu').append('<a class="btn btn-link btn-small" id="revert"><fmt:message key="editor.formula.revocation"/></a>');
	$('#revert').on('click',function(){
		var latex=cache.pop();
		var textarea=$('#MathInput');
		if(latex==textarea.val()){
			latex=cache.pop();
		}
		textarea.val(latex);
		Preview.Update();
	});
	$('#MathInput').on('keyup',function(){
		Preview.Update();
		cache.push($(this).val());
	});
	jQuery.extend({ 
	    /**
	     * 清除当前选择内容
	     */ 
	    unselectContents: function(){ 
	        if(window.getSelection) 
	            window.getSelection().removeAllRanges(); 
	        else if(document.selection) 
	            document.selection.empty(); 
	    } 
	}); 
	jQuery.fn.extend({ 
	    /**
	     * 选中内容
	     */ 
	    selectContents: function(){ 
	        $(this).each(function(i){ 
	            var node = this; 
	            var selection, range, doc, win; 
	            if ((doc = node.ownerDocument) && 
	                (win = doc.defaultView) && 
	                typeof win.getSelection != 'undefined' && 
	                typeof doc.createRange != 'undefined' && 
	                (selection = window.getSelection()) && 
	                typeof selection.removeAllRanges != 'undefined') 
	            { 
	                range = doc.createRange(); 
	                range.selectNode(node); 
	                if(i == 0){ 
	                    selection.removeAllRanges(); 
	                } 
	                selection.addRange(range); 
	            } 
	            else if (document.body && 
	                typeof document.body.createTextRange != 'undefined' && 
	                (range = document.body.createTextRange())) 
	            { 
	                range.moveToElementText(node); 
	                range.select(); 
	            } 
	        }); 
	    }, 
	    /**
	     * 初始化对象以支持光标处插入内容
	     */ 
	    setCaret: function(){ 
	        if(!$.browser.msie) return; 
	        var initSetCaret = function(){ 
	            var textObj = $(this).get(0); 
	            textObj.caretPos = document.selection.createRange().duplicate(); 
	        }; 
	        $(this) 
	            .click(initSetCaret) 
	            .select(initSetCaret) 
	            .keyup(initSetCaret); 
	    }, 
	    /**
	     * 在当前对象光标处插入指定的内容
	     */ 
	    insertAtCaret: function(textFeildValue){ 
	        var textObj = $(this).get(0); 
	        if(document.all && textObj.createTextRange && textObj.caretPos){ 
	            var caretPos=textObj.caretPos; 
	            caretPos.text = caretPos.text.charAt(caretPos.text.length-1) == '' ? 
	                textFeildValue+'' : textFeildValue; 
	        } 
	        else if(textObj.setSelectionRange){ 
	            var rangeStart=textObj.selectionStart; 
	            var rangeEnd=textObj.selectionEnd; 
	            var tempStr1=textObj.value.substring(0,rangeStart); 
	            var tempStr2=textObj.value.substring(rangeEnd); 
	            textObj.value=tempStr1+textFeildValue+tempStr2; 
	            textObj.focus(); 
	            var len=textFeildValue.length; 
	            textObj.setSelectionRange(rangeStart+len,rangeStart+len); 
	            textObj.blur(); 
	        } 
	        else { 
	            textObj.value+=textFeildValue; 
	        } 
	    } 
	});
	 $("#MathInput").setCaret();
	 $('#insertLatex').click(function(){
			$('#insertLatexDialog').modal('show');
		});
	$('.formulaBtn').on('click',function(){
			var latex=$(this).attr("title");
			var textarea=$('#MathInput');
			textarea.insertAtCaret(latex); 
			Preview.Update();
			cache.push(textarea.val());  
	});
	$('#insertLatexOk').click(function(){
		editor.execCommand('insertHtml',"<span class='formula'>\\["+$("#MathInput").val()+"\\]</span>");
	}); 
	$('#insertLatexDialog').on('hidden',function(){
		$('#mathsymbols').val("");
		$('#greeksymbols').val("");
		$('#MathInput').val('');
		Preview.Update();
	});
});
</script>
<link rel="stylesheet" href="<dhome:url value="/resources/third-party/ueditor/themes/default/ueditor.css"/>">
<script type="text/javascript">
var cache=[];
var Preview = {
		  delay: 150,        // delay after keystroke before updating
		  preview: null,     // filled in by Init below
		  buffer: null,      // filled in by Init below

		  timeout: null,     // store setTimout id
		  mjRunning: false,  // true when MathJax is processing
		  oldText: null,     // used to check if an update is needed

		  //
		  //  Get the preview and buffer DIV's
		  //
		  Init: function () {
		    this.preview = document.getElementById("MathPreview");
		    this.buffer = document.getElementById("MathBuffer");
		  },

		  //
		  //  Switch the buffer and preview, and display the right one.
		  //  (We use visibility:hidden rather than display:none since
		  //  the results of running MathJax are more accurate that way.)
		  //
		  SwapBuffers: function () {
		    var buffer = this.preview, preview = this.buffer;
		    this.buffer = buffer; this.preview = preview;
		    buffer.style.visibility = "hidden"; buffer.style.position = "absolute";
		    preview.style.position = ""; preview.style.visibility = "";
		  },

		  //
		  //  This gets called when a key is pressed in the textarea.
		  //  We check if there is already a pending update and clear it if so.
		  //  Then set up an update to occur after a small delay (so if more keys
		  //    are pressed, the update won't occur until after there has been 
		  //    a pause in the typing).
		  //  The callback function is set up below, after the Preview object is set up.
		  //
		  Update: function () {
		    this.callback();
		  },

		  //
		  //  Creates the preview and runs MathJax on it.
		  //  If MathJax is already trying to render the code, return
		  //  If the text hasn't changed, return
		  //  Otherwise, indicate that MathJax is running, and start the
		  //    typesetting.  After it is done, call PreviewDone.
		  //  
		  CreatePreview: function () {
			var latex=$("#MathInput").val();
		    Preview.timeout = null;
		    if (this.mjRunning) return;
		    var text ="\\["+latex+"\\]";
		    if (text === this.oldtext) return;
		    this.buffer.innerHTML = this.oldtext = text;
		    this.mjRunning = true;
		    MathJax.Hub.Queue(
		      ["Typeset",MathJax.Hub,this.buffer],
		      ["PreviewDone",this]
		    );  
		  },   

		  //
		  //  Indicate that MathJax is no longer running,
		  //  and swap the buffers to show the results.
		  //
		  PreviewDone: function () {
		    this.mjRunning = false;
		    this.SwapBuffers();
		  }

		};

		//
		//  Cache a callback to the CreatePreview action
		//
		Preview.callback = MathJax.Callback(["CreatePreview",Preview]);
		Preview.callback.autoReset = true;  // make sure it can run more than once

	// make sure it can run more than once
	Preview.Init();

	$('#viewMyPage,.config-link').click(function(){
		alert("<fmt:message key='hint.save'/>")
		return false;
	});
	$("#checkPaperAll").click(function(){
		var ckValue=$(this).attr("checked");
		if(ckValue=="checked"){
			$("input[name='paperChecked']").attr("checked","checked");
		}else{
			$("input[name='paperChecked']").removeAttr("checked");
		}
	});
	
	$("#checkEduAll").click(function(){
		var ckValue=$(this).attr("checked");
		if(ckValue=="checked"){
			$("input[name='eduChecked']").attr("checked","checked");
		}else{
			$("input[name='eduChecked']").removeAttr("checked");
		}
	});
	$("#checkWorkAll").click(function(){
		var ckValue=$(this).attr("checked");
		if(ckValue=="checked"){
			$("input[name='workChecked']").attr("checked","checked");
		}else{
			$("input[name='workChecked']").removeAttr("checked");
		}  
	});
    var editor = new baidu.editor.ui.Editor();
    editor.render("myEditor");
    var isSource=false;
    //编辑器切换源码监听
    editor.addListener("sourceModeChanged",function(type,mode){
   		isSource=mode;
	});
    
    function showPaper() {
    	$('#myPaperModal').modal('show');
    	$("#paperItems").html("");
    	$('#checkPaperAll').removeAttr("checked");
    	$.ajax({
			type : "get",
			url : "<dhome:url value='/people/${domain}/admin/paper/getPapers.json'/>",
			data : {"t":new Date()},
			success:function(items){
				if(items!=''&&items!=null&&items.length>0){
					$("#divPaperCheck").show();
					$("#paper-template").render(items).appendTo("#paperItems");
					$("#paper-insert-template").render(items).appendTo("#insertHTML");
					$(".paper-source").each(function(i,n){
						if($(n).html()==':'){
							$(n).remove();
						}
					})
				}else{
					$("#divPaperCheck").hide();
					$("#paperItems").html("<div class='some-lpad'><fmt:message key='editor.noPaper'/></div>")
					.append("<div class='some-lpad'><a target='_blank' href='<dhome:url value='/people/${domain }/admin/paper/edit'/>'><fmt:message key='editor.addPaper'/></a></div>");
				}
				
			}
		});
    };
    function showEdu() {
    	$('#myEduModal').modal('show');
    	$("#eduItems").html("");
    	$('#checkEduAll').removeAttr("checked");
		getEdusJson(function(items){
			if(items!=''&&items.length>0){
				$("#divEduCheck").show();
				$(items).each(function(i,n){
					convertDate(n);
				});
				$("#edu-template").render(items).appendTo("#eduItems");
				$("#edu-insert-template").render(items).appendTo("#insertHTML");
			}else{
				$("#divEduCheck").hide();
				$("#eduItems").html("<div class='some-lpad'><fmt:message key='editor.noEducation'/></div>")
				.append("<div class='some-lpad'><a target='_blank' href='<dhome:url value='/people/${domain}/admin/personal/education'/>'><fmt:message key='editor.addEducation'/></a></div>");
			}
		});
    };
    function getWorksJson(callback){
    	$.ajax({
			type : "get",
			url : "<dhome:url value='/people/${domain}/admin/p/getWorks.json'/>",
			data : {"t":new Date()},
			success:callback
		});
    }
    function getEdusJson(callback){
    	$.ajax({
			type : "get",
			url : "<dhome:url value='/people/${domain}/admin/p/getEdus.json'/>",
			data : {"t":new Date()},
			success:callback
		});
    }
     function showWork() {
    	$('#myWorkModal').modal('show');
    	$("#workItems").html("");
    	$('#checkWorkAll').removeAttr("checked");
    	getWorksJson(function(items){
			if(items!=''&&items.length>0){
				$("#divWorkCheck").show();
				$(items).each(function(i,n){
					convertDate(n);
				})
				$("#work-template").render(items).appendTo("#workItems");
				$("#work-insert-template").render(items).appendTo("#insertHTML");
			}else{
				$("#divWorkCheck").hide();
				$("#workItems").html("<div class='some-lpad'><fmt:message key='editor.noWork'/></div>")
				.append("<div class='some-lpad'><a target='_blank' href='<dhome:url value='/people/${domain}/admin/personal/work'/>'><fmt:message key='editor.addWork'/></a></div>");
			}
			
		});
    	
    };
    
    function insertPaper() {
    	var insertHtml="";
    	$('input[name="paperChecked"]').each(function(n,i){
    		if(($(i).attr("checked")=='checked')){
    			var paper_id=$(i).attr("paper_id");
    			var source=$("#paper_insert_source_"+paper_id);
    			if(source.html()==':'){
    				source.remove();	
    			}
    			insertHtml+=$('#paper_insert_'+paper_id).html();
    		}	
    	});
    	editor.execCommand('insertHtml', '<p><h4><fmt:message key="editor.insertPaper" /></h4>' + '<ol>'  + insertHtml + '</ol></p>');
    	$('#paperItems').html("");
    	$("#checkPaperAll").removeAttr("checked");
    	$('#myPaperModal').modal('hide');
    };
	 function insertEdu() {
    	var insertHtml="";
    	$('input[name="eduChecked"]').each(function(n,i){
    		if(($(i).attr("checked")=='checked')){
    			var edu_id=$(i).attr("edu_id");
    			insertHtml+=$('#edu_insert_'+edu_id).html();
    		}	
    	});
    	editor.execCommand('insertHtml', '<p><h4><fmt:message key="editor.insertEdu" /></h4>' + '<ul>'  + insertHtml + '</ul></p>');
    	$('#eduItems').html("");
    	$("#checkEduAll").removeAttr("checked");
    	$('#myEduModal').modal('hide');
    };
	 function insertWork() {
    	var insertHtml="";
    	$('input[name="workChecked"]').each(function(n,i){
    		if(($(i).attr("checked")=='checked')){
    			var work_id=$(i).attr("work_id");
    			insertHtml+=$('#work_insert_'+work_id).html();
    		}
    	});
    	editor.execCommand('insertHtml', '<p><h4><fmt:message key="editor.insertWork" /></h4>' + '<ul>'  + insertHtml + '</ul></p>');
    	$('#workItems').html("");
    	$("#checkWorkAll").removeAttr("checked");
    	$('#myWorkModal').modal('hide');
    };
    function cancel(){
    	var flag=confirm("<fmt:message key='hint.cancel'/>");
    	if(flag){
			window.location.href='<dhome:url value="/people/${domain }/admin/p/${page.keyWord }/cancelPage?isNew="/>'+isNew;
    	}
    }
    function convertDate(obj){
    	var beginTimeOrg=obj.beginTime;
    	var beginTime=new Date(obj.beginTime);
    	var endTime=new Date(obj.endTime)
    	obj.beginTime=beginTime.getFullYear()+"."+(beginTime.getMonth()+1);//+"."+beginTime.getDate();
    	obj.endTime=endTime.getFullYear()+"."+(endTime.getMonth()+1);//+"."+endTime.getDate();
    	//这里正常来说不会是空，除非是注册时候带进来的
    	if(beginTimeOrg==null){
    		obj.beginTime='';
    	}
    	//这里是3000的情况只有一个，就是至今
    	if(obj.endTime=='3000.1'){
    		obj.endTime='<fmt:message key="personalWorkInfo.untilnow"/>';
    	}
    	
    }
    $(document).ready(function(){	
    $.validator.addMethod("contentLength", function(value, element){
    	return this.optional(element)||(editor.getContentTxt().length<=10000);
    }, "");
    
    $.validator.addMethod("urlRegex", function(value, element){
    	var regex = /^[a-zA-Z0-9\-_]+$/;
    	return this.optional(element)||(regex.test(value));
    }, "<fmt:message key='editor.check.lettersandNum'/>");
    
    var validator =$("#editForm").validate({
    	submitHandler:function(form){
	    	 if(isSource){
	    		 editor.execCommand("source");
	    	 }
	    	$('#finalContent').val(editor.getContent());
	    	form.submit();
	      },
  	  rules: {
  	  	title: {required:true},
  	  	pageUrl: {
  	   		 required: true,
  	   		 remote:{
  	   			type: "GET",
  				url: '<dhome:url value="/people/${domain}/isUrlUsed.json"/>?pid=${page.id}',
  	   		 },
  	  		 urlRegex: true
  	   	  },
  	   	content:{
  	   		contentLength:true
  	  	}
  	  },
  	   messages: {
  		 title: {
  			   required:"<fmt:message key='editor.check.required.pageTitle'/>"},
  		 pageUrl: {
  		  	   required: "<fmt:message key='editor.check.required.pageUrl'/>",
  		       remote:"<fmt:message key='editor.check.pageUrlInUse'/>"
  		   }
  		 }

  	  });
  //work education init content
    if(${newPage}&&'${page.title}'=='<fmt:message key="adminCommonLeft.addNewPage.defaultTitle1"/>'){
  	  getWorksJson(function(items){
  			if(items!=''&&items.length>0){
  				$(items).each(function(i,n){
  					convertDate(n);
  				});
  				$("#work-insert-template").render(items).appendTo("#insertHTML");
  				var html="";
  				$('#insertHTML div').each(function(index,item){
  					html+=$(item).html();
  				});
  				editor.execCommand('insertHtml', '<ul>'  + html + '</ul>');
  				$('#insertHTML').html("");
  			}
  	  })	
    }else if(${newPage}&&'${page.title}'=='<fmt:message key="adminCommonLeft.addNewPage.defaultTitle2"/>'){
  	  getEdusJson(function(items){
  			if(items!=''&&items.length>0){
  				$(items).each(function(i,n){
  					convertDate(n);
  				});
  				$("#edu-insert-template").render(items).appendTo("#insertHTML");
  				var html="";
  				$('#insertHTML div').each(function(index,item){
  					html+=$(item).html();
  				});
  				editor.execCommand('insertHtml',  '<ul>'  + html + '</ul>');
  				$('#insertHTML').html("");
  			}
  	  })
    }
    });
    function updateAllCheck(obj){
    	var flag=true;
    	var s="";
    	var b="";
    	if(obj=='work'){
    		s='work';
    		b='Work';
    	}else if(obj=='paper'){
    		s='paper';
    		b='Paper';
    	}else if(obj='edu'){
    		s='edu';
    		b='Edu';
    	}
    	$('input[name="'+s+'Checked"]').each(function(n,i){
    		flag&=($(i).attr("checked")=='checked');
    	});
    	if(flag){
    		$('#check'+b+'All').attr('checked','checked');
    	}else{
    		$('#check'+b+'All').removeAttr('checked');
    	}
    }
  
  //new logical - new page none save botton
	var isNew=${empty page.title};
	if(isNew){
		$('#pageUrl').val("");
		$('#editor_title').blur(function(){
			$.ajax({
				type : "get",
				url : "<dhome:url value='/people/${domain}/admin/p/getPinYin'/>",
				data : {"title":$('#editor_title').val(),"pid":${page.id}},
				success:function(pageUrl){
					if($('#pageUrl').val()==''){
						$('#pageUrl').val(pageUrl);
					}
				}
			});
		});
	}
	
	
	//end
</script>
<script id="paper-template" type="text/html">
<li paper_id="{{= id}}">
	<span class="d-short">
		<input onclick="updateAllCheck(\'paper\');" paper_id="{{= id}}" name="paperChecked" type="checkbox" class="d-sbottom"/>
	</span>
	<span class="d-long nofloat">
		<span class="paper-title">{{= title}}</span>
		<span class="paper-author">{{= authors}}.</span>
		<span class="paper-source">{{= source}}:</span>
		<span class="paper-time">{{= publishedTime}}</span>
	</span>
	<div class="clear"></div>
</li>
</script>
<script id="paper-insert-template" type="text/html">
<div id="paper_insert_{{= id}}"><li><span style="font-weight:bold; display:block; font-size:15px;">{{= title}}</span><span style="display:block; margin:0.3em 0;">{{= authors}}</span><span id="paper_insert_source_{{= id}}">{{= source}}:</span><span>{{= publishedTime}}</span></li></div>
</script>
<script id="work-template" type="text/html">
<li work_id="{{= id}}">
	<span class="d-short">
		<input onclick="updateAllCheck(\'work\');" work_id="{{= id}}" name="workChecked" type="checkbox" class="d-sbottom"/>
	</span>
	<span class="d-long">
		<span class="institutionZhName">{{= aliasInstitutionName}}</span>
		<span class="department">{{= department}},</span>
		<span class="position">{{= position}} | </span>
		<span class="beginTime"> &nbsp;{{= beginTime}} - </span>	
		<span class="endTime"> &nbsp;{{= endTime}}</span>	
	</span>
	<div class="clear"></div>
</li>
</script>
<script id="work-insert-template" type="text/html">
<div id="work_insert_{{= id}}"><li><strong>{{= aliasInstitutionName}}</strong>  {{= department}},{{= position}} | {{= beginTime}}-{{= endTime}}</li></div>
</script>
<script id="edu-template" type="text/html">
<li edu_id="{{= id}}">
	<span class="d-short">
		<input onclick="updateAllCheck(\'edu\');" edu_id="{{= id}}" name="eduChecked" type="checkbox"/>
	</span>
	<span class="d-long">
		<span class="institutionZhName">{{= aliasInstitutionName}}</span>
		<span class="department">{{= department}},</span>
		<span class="degree">{{= degree}} | </span>
		<span class="beginTime">  &nbsp;{{= beginTime}} -</span>
		<span class="endTime"> {{= endTime}}</span>
	</span>
	<div class="clear"></div>
</li>
</script>
<script id="edu-insert-template" type="text/html">
<div id="edu_insert_{{= id}}"><li><strong>{{= aliasInstitutionName}}</strong>  {{= department}},{{= degree}} | {{= beginTime}}-{{= endTime}}</li></div>
</script>
