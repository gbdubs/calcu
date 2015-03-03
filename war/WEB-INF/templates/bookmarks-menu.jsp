<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a href="#" class="dropdown-toggle" data-toggle="dropdown" data-user="${user.userId}" data-type="bookmarks">
	<i class="fa fa-bookmark"></i>
	<c:if test="${unreadBookmarks > 0}">
		<span class="label label-warning">${unreadBookmarks}</span>
	</c:if>
</a>
<ul class="dropdown-menu">
	<li class="header">You have ${fn:length(bookmarksMenu)} Bookmarks</li>
	<li>
		<ul class="menu">
			<c:forEach items="${bookmarksMenu}" var="bookmark">
 				<li>
					<a href="${bookmark.url}">
						<i class="fa ${bookmark.icon} ${bookmark.color}"></i> 
						<h4>${bookmark.title}</h4>
						<h5>${bookmark.description}</h5>
					</a>
				</li>
			</c:forEach>
		</ul>
	</li>
	<li class="footer"><a href="${profileUrl}#bookmarks">View All Bookmarks</a></li>
</ul>