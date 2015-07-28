<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a href="#" class="dropdown-toggle small-header-icon-padding" data-toggle="dropdown" data-user="${user.userId}" data-type="recommendations">
	<i class="fa fa-search"></i>
</a>
<ul class="dropdown-menu dropdown-search">
	<li class="header">
		<form action="/search" method="post">
			<div class="input-group">
				<input name="tagsInput" id="tags-input-bar" value="${tags}" />
				<span class="input-group-btn">
					<input class="btn btn-primary large-input-group-button" type="submit" value="Search!">
					<input class="btn btn-info large-input-group-button" type="submit" value="Search!">
				</span>
			</div>
		</form>
	</li>
</ul>