<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Admin Console
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-solid">
			<div class="box-header">
				<i class="fa fa-tools fa-karma-score"></i>
				<h3 class="box-title">Achievement Manager</h3>
				<div class="pull-right box-tools">
					<button class="btn btn-primary btn-sm" data-widget="collapse" data-toggle="tooltip" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
				</div>
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
						<input type="text" name="name" class="form-control" placeholder="The funny name of the achievement"/>
						<br/>
						<label for="description">Description</label>
						<input type="text" name="description" class="form-control" placeholder="The humorous description of the achievement"/>
						<br/>
						<label for="qualification">Qualification</label>
						<input type="text" name="qualification" class="form-control" placeholder="The description of how one would get this achievement"/>
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
						<input type="submit" class="btn-full-width btn-block btn-primary" value="Create New Achievement"/>
					</div>
				</form>
				
				<c:if test="${fn:length(allAchievements) > 0}">
				<div class="box box-primary">
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
								<td><div class="${achievement.secondaryColor} achievement-style-preview"><i class="fa fa-fw ${achievement.icon} ${achievement.color}"></i></div></td>
								<td><a class="delete-achievement-button badge bg-red">Delete</a></td>
								<td>${achievement.uuid}</td>
							</tr>
							</c:forEach>
						</tbody>
						</table>
					</div>
				</div>
				</c:if>
			
			</div>	
		</div>
		
		<div class="box box-solid">
			<div class="box-header">
				<i class="fa fa-upload fa-karma-score"></i>
				<h3 class="box-title">Content Creation</h3>
				<div class="pull-right box-tools">
					<button class="btn btn-info btn-sm" data-widget="collapse" data-toggle="tooltip" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<form action="/admin/upload-new-content" method="post" class="box box-info">
					<div class="box-header">
						<i class="fa fa-cloud-upload fa-karma-score"></i>
						<h3 class="box-title">Upload New Content in JSON format</h3>
					</div>
					<div class="box-body">
						<p>
							Depending on the type of data you are trying to upload, the format of your json must have certain parameters.
							Every value must be a string.  Sites do not need to be exact URLs, but solutionLinks should be. Titles, Problems,
							and Solutions should all contain LaTex formatting in place of image tags. The required json properties for each
							type of input are:
							<ul>
								<li>Questions -- 'title', 'problem', 'tags', 'site'</li>
								<li>Practice Problems -- 'title', 'problem', 'solution'*, 'tags', 'solutionLink', 'site'</li>
							</ul>
							 * Note that if a Practice Problem has a solution which is null or "", it will be added like a question.  If any
							 other fields are missing, the upload will fail as a whole. Verify your JSON!
						</p>
						<p>
							<select class="form-control" name="contentType">
								<option value="practiceProblem">Practice Problems</option>
								<option value="question">Questions</option>
								<option value="textContent">Text Based Content</option>
							</select>
						</p>
						<p>	
							<textarea class="textarea no-horizontal-resize" style="width: 100%;height: 200px;font-size: 14px;line-height: 18px;border: 1px solid #dddddd; padding: 10px;" name="jsonDataUpload"></textarea>
						</p>
						<input type="submit" class="btn-full-width btn-block btn-info" value="Upload New Content"/>
					</div>
				</form>
			</div>
		</div>
		
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/admin-console.js"></script>
	</jsp:attribute>
</t:genericpage>