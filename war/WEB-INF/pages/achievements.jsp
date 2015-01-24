<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Practice Problem Dashboard
	</jsp:attribute>
	<jsp:attribute name="css">
		<link href="/_static/css/CalcU/badge.css" rel="stylesheet" type="text/css" />
	</jsp:attribute>
	<jsp:attribute name="content">
	
		<div class="box box-solid bg-olive-gradient">
			<div class="box-header">
				<i class="fa fa-leaf"></i>
				<h3 class="box-title">Incomplete Achievements</h3>
			</div>
			<div class="box-body">
				<div class="row">
					<c:forEach items="${unfinishedAchievements}" var="a">
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
						<div class="achievement-box box box-solid ${a.secondaryColor}">
							<div class="achievement-badge ${a.color}">
								<div class="hexagon outer-hexagon"></div>
								<div class="achievement-badge-insides ${a.secondaryColor}">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw ${a.icon} fa-5x badge-icon"></i>
							</div>
							<h3>${a.name}</h3>
							<i>${a.qualification}</i>
							<p>${a.description}</p>
						</div>
					</div>
					</c:forEach>
				</div>
			 
				<p>
					You haven't done this shit yet
				</p>
			</div>
		</div>
	
	</jsp:attribute>
</t:genericpage>