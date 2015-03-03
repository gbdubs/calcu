<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a href="#" class="dropdown-toggle" data-toggle="dropdown">
	<i class="fa fa-road"></i>
	<c:if test="${unreadRecommendations > 0}">
		<span class="label label-danger">${unreadRecommendations}</span>
	</c:if>
</a>
<ul class="dropdown-menu">
	<li class="header">You have ${fn:length(recommendationsMenu)} Recommendations</li>
	<li>
		<ul class="menu">
			<c:forEach items="${recommendationsMenu}" var="recommendation">
 				<li>
					<a href="${recommendation.url}">
						<h3>
							${recommendation.title}
							<small class="pull-right">${recommendation.percentage}</small>
						</h3>
						<div class="progress xs">
							<div class="progress-bar progress-bar-${recommendation.color}" style="width: ${recommendation.percentage}" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
								<span class="sr-only">${recommendation.percentage} Confident</span>
							</div>
						</div>
					</a>
				</li>
			</c:forEach>
		</ul>
	</li>
	<li class="footer">
		<a href="/recommendations">View All Recommendations</a>
	</li>
</ul>