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
				<i class="fa fa-gears fa-karma-score"></i>
				<h3 class="box-title">Achievement Manager</h3>
				<div class="pull-right box-tools">
					<button class="btn btn-primary btn-sm" data-widget="collapse" data-toggle="tooltip" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<form action="/admin/achievement" method="post" class="box box-solid box-primary collapsed-box">
					<input type="hidden" name="uploadMethod" value="single" />
					<div class="box-header">
						<i class="fa fa-trophy fa-karma-score"></i>
						<h3 class="box-title">Create A New Achievement</h3>
						<div class="pull-right box-tools">
							<button class="btn btn-primary btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" data-original-title="Collapse"><i class="fa fa-plus"></i></button>
						</div>
					</div>
					<div class="box-body" style="display:none;">
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
						<input type="submit" class="btn btn-full-width btn-block btn-primary" value="Create New Achievement"/>
					</div>
				</form>
				
				<div class="box box-primary box-solid collapsed-box">
					<div class="box-header">
						<i class="fa fa-cloud-upload fa-karma-score"></i>
						<h3 class="box-title" >Upload Achievements in JSON Format</h3>
						<div class="pull-right box-tools">
							<button class="btn btn-primary btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" data-original-title="Collapse"><i class="fa fa-plus"></i></button>
						</div>
					</div>
					<form class="box-body" style="display:none;" action="/admin/achievement" method="post">
						<input type="hidden" name="uploadMethod" value="bulk" />
						<p>
							Uploading achievements in a JSON format is a stellar way to build a collection of them while not fearing for their
							destruction or loss in case of a datastore purge. The format expected for each entry in the JSON text is:
						</p
						<ul>
							<li>'name' - The title of the achievement</li>
							<li>'description' - A funny or anecdotal description of what the person has now achieved</li>
							<li>'qualification' - A description of how you go about getting the achievement</li>
							<li>'icon - the Font Awesome icon class to display. Using FA-4.0, so not all things are on the table</li>
							<li>
								'textColor' - The CSS class which determines the primary color of the icon and border of the achievement. 
								text-{color} classes are recommended, as they modify the color property with important qualification
							</li>
							<li>
								'backgroundColor' - The color that will be the backdrop for the background. Should not be very light, as
								achievements text is displayed in WHITE. The CSS class for this should modify the color property of the object.
								text-{color} classes are recommended because they use !important
							</li>
						</ul>
						<p>
							Once you have uploaded all of this stuff, it will all be viewable and delete-able on this page, in the next section.
						</p>
						<p>
							<select class="form-control" name="bulkAddInstructions">
								<option value="purge" selected>Delete All Achievements, and Add These</option>
								<option value="append">Add these to Existing Achievements</option>
							</select>
						</p>
						<p>
							<textarea class="textarea no-horizontal-resize" style="width: 100%;height: 200px;font-size: 14px;line-height: 18px;border: 1px solid #dddddd; padding: 10px;" name="jsonDataUpload"></textarea>
						</p>
						<input type="submit" class="btn btn-full-width btn-block btn-primary" value="Upload New Achievements"/>
					</form>
				</div>
				
				<c:if test="${fn:length(allAchievements) > 0}">
				<div class="box box-primary box-solid no-margin collapsed-box">
					<div class="box-header">
						<i class="fa fa-database fa-karma-score"></i>
						<h3 class="box-title">Existing Achievements</h3>
						<div class="pull-right box-tools">
							<button class="btn btn-primary btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" data-original-title="Collapse"><i class="fa fa-plus"></i></button>
						</div>
					</div>
					<div class="box-body" style="display:none;">
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
				<form action="/admin/upload-new-content" method="post" class="box box-info box-solid no-margin">
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
						<input type="submit" class="btn btn-full-width btn-block btn-info" value="Upload New Content"/>
					</div>
				</form>
			</div>
		</div>
		
		<div class="box box-solid">
			<div class="box-header">
				<i class="fa fa-ban fa-karma-score"></i>
				<h3 class="box-title">Reported Content</h3>
				<div class="pull-right box-tools">
					<button class="btn btn-danger btn-sm" data-widget="collapse" data-toggle="tooltip" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<div class="box box-danger box-solid no-margin">
					<div class="box-header">
						<i class="fa fa-crosshairs fa-karma-score"></i>
						<h3 class="box-title">View and respond to reported content</h3>
					</div>
					<div class="box-body">
						<p>
							When a report comes in, the content is automatically made invisible to users if the content is
							"offensive" or "irrelevant" or "Proprietary".  If the complaint has to do with accuracy, it takes
							two reports to make it automatically invisible.  Shown in the table below is all of the content which
							has currently been marked as invisible.  It is broken up into each type of content, Practice Problems
							and Questions (TextContent to be added later).  It is sorted by the date that it was made invisible, with
							the oldest reports being shown first.  You have three options when given a report:
						</p>
						<ul>
							<li>Delete the Content -- Permanently deletes the reported Content.</li>
							<li>Delete Report -- Does not delete content, and does not flag the reporter as an abuser</li>
							<li>Ignore the Report with Consequences -- Does not delete content, but does flag the reporter for abuse of the system </li>
							<li>
								Email the Reporter -- Keeps the content hidden, and does not remove it from the current list of problems that have been reported,
								Rather, opens a MailTo which allows you to email the reporter, likely to discuss liability concerns.
							</li>
						</ul>
						<p>
							None of these options are reversible, so take care in making them well!
						</p>
						<c:if test="${fn:length(reportedContent) > 0}">
							<table class="table">
								<tbody>
									<tr>
										<th>Report Name</th>
										<th>Reported On</th>
										<th>Reporter</th>
										<th>Report Type</th>
										<th>Delete Report</th>
										<th>Delete Content</th>
										<th>Flag Reporter</th>
										<th>Email Reporter</th>
									</tr>
									<c:forEach items="${reportedContent}" var="report">
										<tr data-uuid="${report.reportUuid}">
											<td><a href="${report.contentUrl}">${report.contentTitle}</a></td>
											<td>${report.reportedOn}</td>
											<td>${report.reporterUsername}</td>
											<td>${report.reportType}</td>
											<td><a class="delete-report-button badge bg-green">Delete Report</a></td>
											<td><a class="delete-reported-content-button badge bg-red">Delete Content</a></td>
											<td><a class="flag-reporter-button badge bg-yellow">Flag Reporter</a></td>
											<td><a class="badge bg-blue" href="mailto:${report.reporterEmail}?Subject=Reported Content On ${report.reportedOn}">Email Reporter</a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>
						<c:if test="${fn:length(reportedContent) == 0}">
							<h3 class="centered">No Reported Content! Celebrate!</h2>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/admin-console.js"></script>
	</jsp:attribute>
</t:genericpage>