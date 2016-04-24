<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<%--请在下列标签中查询每个Menu的数量 --%>
<dhome:institutionLeftMenuCount domain="${domain }"/>
<%-- 
memberCount-员工总数
paperCount-论文数量
topicCount-课题数量
periodicalCount-期刊任职数量
copyrightCount-软件著作权数量
patentCount-专利数量
trainingCount-人才培养数量
 --%>
<div class="ins_backend_leftMenu">
	<ul>
		<li id="pandectMenu" >
			<a href="<dhome:url value="/institution/${domain}/backend/pandect/index"/>">
				<span class="title">总览</span>
			</a>
		</li>
		<li id="memberMenu" >
			<a href="<dhome:url value="/institution/${domain}/backend/member/list/1"/>">
				<span class="title">员工</span>
				<span class="count">${memberCount }</span>
			</a>
		</li>
		<li id="paperMenu">
			<a href="<dhome:url value="/institution/${domain }/backend/paper/list/1"/>">
				<span class="title">论文</span>
				<span class="count">${paperCount }</span>
			</a>
		</li>
		<li id="treatiseMenu">
			<a href="<dhome:url value="/institution/${domain }/backend/treatise/list/1"/>">
				<span class="title">论著</span>
				<span class="count">${treatiseCount}</span>
			</a>
		</li>
		<li id="awardMenu">
			<a href="<dhome:url value="/institution/${domain }/backend/award/list/1"/>">
				<span class="title">奖励</span>
				<span class="count">${awardCount}</span>
			</a>
		</li>
		<li id="copyrightMenu">
			<a href="<dhome:url value="/institution/${domain }/backend/copyright/list/1"/>">
				<span class="title">软件著作权</span>
				<span class="count">${copyrightCount }</span>
			</a>
		</li>
		<li id="patentMenu">
			<a href="<dhome:url value="/institution/${domain }/backend/patent/list/1"/>">
				<span class="title">专利</span>
				<span class="count">${patentCount }</span>
			</a>
		</li>
		<li id="topicMenu">
			<a href="<dhome:url value="/institution/${domain}/backend/topic/list/1"/>">
				<span class="title">课题</span>
				<span class="count">${topicCount }</span>
			</a>
		</li>
		<li id="academicMenu">
			<a href="<dhome:url value="/institution/${domain }/backend/academic/list/1"/>">
				<span class="title">学术任职</span>
				<span class="count">${academicCount }</span>
			</a>
		</li>
		<li id="periodicalMenu">
			<a href="<dhome:url value="/institution/${domain }/backend/periodical/list/1"/>">
				<span class="title">期刊任职</span>
				<span class="count">${periodicalCount }</span>
			</a>
		</li>
		<li id="trainingMenu">
			<a href="<dhome:url value="/institution/${domain }/backend/training/list/1"/>">
				<span class="title">人才培养</span>
				<span class="count">${trainingCount }</span>
			</a>
		</li>
		<li id="settingMenu">
			<a href="<dhome:url value="/institution/${domain }/backend/setting/1"/>">
				<span class="title">设置</span>
			</a>
		</li>
	</ul>
</div>