<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<script type="text/javascript" src="<dhome:url value="/resources/scripts/json2/json2-min.js" />"></script>
<script src="<dhome:url value="/resources/scripts/jquery/1.7.2/jquery-1.7.2.min.js"/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/scripts/jquery/jquery.validate.min.js"/>" type="text/javascript" ></script>
<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.min.js'/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/third-party/bootstrap/js/bootstrap.min.js"/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/scripts/base.min.js"/>" type="text/javascript" ></script>
<script src="<dhome:url value="/resources/scripts/jquery/jquery-ui-1.8.16.custom.min.js"/>" type="text/javascript"></script>  
<script src="<dhome:url value="/resources/scripts/upload/fileuploader.min.js"/>" type="text/javascript"></script>
<script src="<dhome:url value="/resources/scripts/jquery/jquery.autocomplete.min.js"/>" type="text/javascript"></script>
<script src="<dhome:url value="/resources/scripts/commonBanner.js"/>" type="text/javascript" id="bannerJs" data="url=<dhome:url value='/discover'/>"></script>
<script src="<dhome:url value="/resources/scripts/jquery/search-jQuery.js"/>" type="text/javascript" ></script>
<link href="http://www.escience.cn/dface/css/dface.banner.css" rel="stylesheet" type="text/css"/>
<script src="http://www.escience.cn/dface/js/dface.banner.js" type="text/javascript" ></script>

<script >
$(document).ready(function(){
	/**
	maintain
	*/
	<dhome:IsMainTain>
		$('.maintain').each(function(i,n){
			$(n).hide();
		})
	</dhome:IsMainTain>
	/* GLOBAL SEARCH */
	var globalSearch = new SearchBox("globalSearch", "<fmt:message key='common.searchDefaut'/>", false, false, false);
	globalSearch.setPullDown("<fmt:message key='searchBox.noResult'/>", "<fmt:message key='searchBox.error'/>", '350px');
	globalSearch.searchInput.focus(function(){
	globalSearch.container.addClass('loaded');
	globalSearch.container.parent().addClass('loaded');
	});
	globalSearch.searchInput.blur(function(){
	if (globalSearch.searchResultState==false) {
	globalSearch.container.removeClass('loaded');
	}
	globalSearch.container.parent().removeClass('loaded');
	}); 
	
	/*bind enter key press event to search input
	 */
	globalSearch.doSearch=function(keyWord){
		$('#keyword').val(keyWord);
	 	$('#searchForm').submit();
	}
	//bind goto login
	$('.gotoLogin').attr("href",'${context}/system/login?state='+encodeURIComponent(window.location.href));
	//bind goto Msg Board
	$('.gotoMsgBoard').attr('href','${context}/people/${domain}/admin/msgboard');  
});
</script>
<link href="http://www.escience.cn/dface/css/dface.simple.footer.css" rel="stylesheet" type="text/css"/>
<script src="http://www.escience.cn/dface/js/dface.simple.footer.js" type="text/javascript" ></script>
<script type="text/javascript">
$(document).ready(function(){
	$(".dface.footer p span#app-version").html("(dHome <f:version/>)");
});
</script>
<div style="display:none;">
	<!-- <script src="http://s13.cnzz.com/stat.php?id=4852745&web_id=4852745&show=pic" language="JavaScript"></script> -->
</div>