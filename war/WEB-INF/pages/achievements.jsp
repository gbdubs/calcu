<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Achievements
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered bg-black full-content">			
			<div class="max-width-at-1000">
				<h2 class="welcome-banner margin-top-100">Achievements</h2>
				<h3 class="welcome-banner-subtitle margin-bottom-50">
					Gotta' Get 'Em All! To learn about how to earn Achievements, Karma, 
					and Leveling Up, visit the <a href="/tutorial" class="text-white">Tutorial</a> page.
				</h3>
				<div class="row">
					<c:forEach items="${achievements}" var="a">
					<div class="col-xs-12 col-sm-4 col-md-4 col-lg-3">
						<div class="achievement-box <c:if test="${a.value}">completed</c:if> box box-solid ${a.key.secondaryColor}">
							<div class="achievement-badge ${a.key.color}">
								<div class="hexagon outer-hexagon"></div>
								<div class="achievement-badge-insides ${a.key.secondaryColor}">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw ${a.key.icon} fa-5x badge-icon"></i>
							</div>
							<h3>${a.key.name}</h3>
							<h5><i>${a.key.qualification}</i></h5>
							<p>${a.key.description}</p>
						</div>
					</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>