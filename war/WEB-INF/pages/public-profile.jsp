<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | ${profileUsername}'s Profile
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-primary box-solid">
			<div class="box-header">
				<i class="fa fa-user"></i>
				<h3 class="box-title">${profileUsername}'s Profile</h3>
			</div>
			<div class="box-body" style="overflow: auto;">
			
				<span style="float:left; padding: 0 10px 0 0;">
					<img style="max-height: 150px;max-width:150px; border-radius:3px;" src="${profileProfilePictureUrl}"/>
				</span>

				<div style="display:inline-block;">
					<p style="display:inline-block;">Username: <b>${profileUsername}</b></p></br>
					<p style="display:inline-block;">Level: <b>${profileLevel}</b></p></br>
					<p style="display:inline-block;">Karma: <b>${profileKarma}</b></p></br>
					<p style="display:inline-block;">Last Submission: <b>${profileLastSubmission}</b></p></br>
					<p style="display:inline-block;">Number of Contributions: <b>${profileNumberContributions}</b></p></br>
				</div>

			</div>
		</div>
		<div class="box box-success box-solid">
			<div class="box-header">
				<i class="fa fa-pencil"></i>
				<h3 class="box-title">${profileUsername}'s Content</h3>
			</div>
			<div class="box-body" style="overflow: auto;">
				<c:forEach items="${profileUserContent}" var="content">
				
					<c:set var="bookmarked" value="false" />
					<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
					  <c:if test="${bookmarkUuid eq content.uuid}">
						<c:set var="bookmarked" value="true" />
					  </c:if>
					</c:forEach>
				
					<div class="alert alert-${content.color} alert-dismissable" style="margin: 5px 0;">
						<c:choose>
							<c:when test="${bookmarked}">
								<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="remove" data-user="${user.userId}" data-content="${content.uuid}">
									<i class="fa fa-bookmark"></i>
								</button>
							</c:when>
							<c:otherwise>
								<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="${user.userId}" data-content="${content.uuid}">
									<i class="fa fa-bookmark-o"></i>
								</button>
							</c:otherwise>
						</c:choose>
						
						<h4><a href="${content.url}">${content.title}</a></h4>
						<p>${content.description}</p>
						
					</div>
				</c:forEach>
				<c:if test="${fn:length(profileUserContent) == 0}">
					<h4 class="centered">This user hasn't created any content yet!</h4>
				</c:if>
			</div>
		</div>
		
		<div class="box box-warning box-solid">
			<div class="box-header">
				<i class="fa fa-trophy"></i>
				<h3 class="box-title">${profileUsername}'s Karma Profile</h3>
			</div>
			<div class="box-body">
				<table class="table"><tbody>
					<tr>
						<th>Source</th>
						<th>Karma</th>
						<th>Proportion</th>
					</tr>
					<tr>
						<td>Contributing Practice Problems</td><td>${karmaFromPracticeProblems}</td>
						<td>
							<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-success" role="progressbar" style="width: ${100 * (karmaFromPracticeProblems + 1) / (karmaTotal +1)}%"></div></div>
						</td>					
					</tr>
					<tr>
						<td>Contributing Questions</td><td>${karmaFromQuestions}</td>
						<td>
							<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-primary" role="progressbar" style="width: ${100 * (karmaFromQuestions + 1) / (karmaTotal +1)}%"></div></div>
						</td>					
					</tr>
					<tr>
						<td>Contributing Text Content</td><td>${karmaFromTextContent}</td>
						<td>
							<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-warning" role="progressbar" style="width: ${100 * (karmaFromTextContent + 1) / (karmaTotal +1)}%"></div></div>
						</td>					
					</tr>
					<tr>
						<td>Answering/Commenting on Content</td><td>${karmaFromAnswers}</td>
						<td>
							<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-danger" role="progressbar" style="width: ${100 * (karmaFromAnswers + 1) / (karmaTotal +1)}%"></div></div>
						</td>					
					</tr>
					<tr>
						<td>Rating Others</td><td>${karmaFromRatingOthers}</td>
						<td>
							<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-info" role="progressbar" style="width: ${100 * (karmaFromRatingOthers + 1) / (karmaTotal +1)}%"></div></div>
						</td>					
					</tr>
					<tr>
						<td>Answering in Answer Mode</td><td>${karmaFromAnswerMode}</td>
						<td>
							<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-primary" role="progressbar" style="width: ${100 * (karmaFromAnswerMode + 1) / (karmaTotal +1)}%"></div></div>
						</td>					
					</tr>
					<tr>
						<td>Your Answers/Comments Quality</td><td>${karmaFromApprovedAnswers}</td>
						<td>
							<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-success" role="progressbar" style="width: ${100 * (karmaFromApprovedAnswers + 1) / (karmaTotal +1)}%"></div></div>
						</td>					
					</tr>
				
				</tbody></table>
				
			</div>
		</div>
				
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/public-profile.js" type="text/javascript"></script>
	</jsp:attribute>
</t:genericpage>