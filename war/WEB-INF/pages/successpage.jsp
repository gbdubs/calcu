<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>

	<jsp:attribute name="notificationsMenu">
		<jsp:include page="/WEB-INF/templates/notifications-menu-default.jsp" />
	</jsp:attribute>
	
	<jsp:attribute name="bookmarksMenu">
		<jsp:include page="/WEB-INF/templates/bookmarks-menu-default.jsp" />
	</jsp:attribute>
	
	<jsp:attribute name="recommendationsMenu">
		<jsp:include page="/WEB-INF/templates/recommendations-menu-default.jsp" />
	</jsp:attribute>
	
	<jsp:attribute name="userLoginPanel">
		<jsp:include page="/WEB-INF/templates/user-login-menu-logged-out.jsp" />
	</jsp:attribute>
	
	<jsp:attribute name="userInfoPanel">
		<jsp:include page="/WEB-INF/templates/user-info-panel-logged-out.jsp" />
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="box box-success">
			<div class="box-header">
				<i class="fa fa-trophy"></i>
				<h3 class="box-title">
					WHAT SUCCESS!!!	
				</h3>
			</div>	
		</div>
	</jsp:attribute>
	
</t:genericpage>