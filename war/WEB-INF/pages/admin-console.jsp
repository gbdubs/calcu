<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Admin Console
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-solid">
			<div class="box-header">
				<i class="fa fa-tools fa-karma-score"></i>
				<h3 class="box-title">Achievments</h3>
			</div>
			<div class="box-body">
				<form action="/admin/achievement" method="post" class="box box-primary">
					<div class="box-header">
						<i class="fa fa-trophy fa-karma-score"></i>
						<h3 class="box-title">Create A New Achievement</h3>
					</div>
					<div class="box-body">
						<input type="hidden" name="action" value="create"/>
						<label for="name">Name</label>
						<input type="text" name="name" class="form-control" placeholder="The funny name of the achievment"/>
						<br/>
						<label for="description">Description</label>
						<input type="text" name="description" class="form-control" placeholder="The humorous description of the achievment"/>
						<br/>
						<label for="qualification">Qualification</label>
						<input type="text" name="qualification" class="form-control" placeholder="The description of how one would get this achievment"/>
						<br/>
						<label for="icon">Icon</label>
						<input type="text" name="icon" class="form-control" placeholder="Any set of Font Awesome Icon Classes"/>
						<br/>
						<label for="color">Text Color</label>
						<input type="text" name="color" class="form-control" placeholder="Any Classes which specify color"/>
						<br/>
						<label for="secondaryColor">Background Color</label>
						<input type="text" name="secondaryColor" class="form-control" placeholder="Any Classes which specify color"/>
						<br/>
						<input type="submit" class="btn-full-width btn-block btn-primary" value="Create New Achievment"/>
					</div>
				</form>
				<div class="box box-info">
					<div class="box-header">
						<i class="fa fa-database fa-karma-score"></i>
						<h3 class="box-title">Existing Achievements</h3>
					</div>
					<div class="box-body">
						<table class="table centered">
						<tbody>
							<tr>
								<th class="centered">Name</th>
								<th class="centered">Qualification</th>
								<th class="centered">Description</th>
								<th class="centered">Style</th>
								<th class="centered">Delete?</th>
								<th class="centered">UUID</th>
							</tr>
							<c:forEach items="${allAchievements}" var="achievement">
							<tr data-uuid="${achievement.uuid}">
								<td>${achievement.name}</td>
								<td>${achievement.qualification}</td>
								<td>${achievement.description}</td>
								<td class="${achievement.secondaryColor}"><i class="fa fa-fw ${achievement.icon} ${achievement.color}"></i></td>
								<td><a class="delete-achievement-button badge bg-red">Delete</a></td>
								<td>${achievement.uuid}</td>
							</tr>
							</c:forEach>
						</tbody>
						</table>
					</div>
				</form>
			</div>	
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/admin-console.js"></script>
	</jsp:attribute>
</t:genericpage>