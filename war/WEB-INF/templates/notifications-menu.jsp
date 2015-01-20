<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<a href="#" class="dropdown-toggle" data-toggle="dropdown">
	<i class="fa fa-flag"></i>
	<span class="label label-success">${fn:length(notificationsMenu)}</span>
</a>
<ul class="dropdown-menu">
	<li class="header">You have ${fn:length(notificationsMenu)} notifications</li>
	<li>
		<ul class="menu">
			<c:forEach items="${notificationsMenu}" var="notification">
 				<li>
					<a href="${notification.url}">
						<div class="pull-left">
							<img src="${notification.image}" class="img-square" alt="user image"/>
						</div>
						<h4>
							${notification.title}
							<small><i class="fa fa-clock-o"></i> ${notification.time}</small>
						</h4>
						<p>${notification.description}</p>
					</a>
				</li>
			</c:forEach>
		</ul>
	</li>
	<li class="footer"><a href="${profileUrl}#notifications">View All Notifications</a></li>
</ul>