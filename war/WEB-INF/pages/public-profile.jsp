<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | ${username}'s Profile
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-primary">
			<div class="box-header">
				<i class="fa fa-user"></i>
				<h3 class="box-title">${profileUsername}'s Profile</h3>
			</div>
			<div class="box-body centered">
				<div>
					<img style="max-height: 300px;max-width:300px;" src="${profileProfilePictureUrl}"/>
				</div>
				<h4 class="centered">${profileKarma}</h4>
			</div>
		</div>
				
	</jsp:attribute>
</t:genericpage>