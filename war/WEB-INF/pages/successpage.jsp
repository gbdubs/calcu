<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
		<c:choose>
			<c:when test="${user != null}">
				<jsp:include page="/WEB-INF/templates/user-login-menu-logged-in.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include page="/WEB-INF/templates/user-login-menu-logged-out.jsp" />
			</c:otherwise>
		</c:choose>
	</jsp:attribute>
	
	<jsp:attribute name="userInfoPanel">
		<c:choose>
			<c:when test="${user != null}">
				<jsp:include page="/WEB-INF/templates/user-info-panel-logged-in.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include page="/WEB-INF/templates/user-info-panel-logged-out.jsp" />
			</c:otherwise>
		</c:choose>
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