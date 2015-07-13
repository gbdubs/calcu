<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Practice Problem Dashboard
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="centered">
			<h2 class="welcome-banner margin-top-100 margin-bottom-50">Creating Content for CalcU</h2>
			<h3 class="welcome-banner-subtitle margin-bottom-50">Read this to become familiar with directions, or click below to jump in!</h3>
			<div class="max-width-at-1000 margin-bottom-50 align-left">
				<p>
					There are three main types of content on CalcU. Questions are asked by our users and answered by other users, they are 
					usually general questions, not focusing on a specific problem.  Practice Problems are submitted by users alongside a 
					solution, they are meant to give people practice with a specific topic in calculus, or to provide general practice.
					Textual content is submitted to provide an explanation of a specific concept or set of concepts, and should come from
					an authoritative source. To begin creation of any of these types, click their respective link above.  You will be able
					to save your work, and revisit it later. Once you are done editing, you can submit your work (either with your name, or 
					anonymously). To continue editing your work, or submit it, you can come back to this page, the 'content dashboard', where 
					a link will be made available. You can also track all of your past submissions from this page, and see how they have done 
					as people have rated and reviewed them. If you have any questions about content creation, please feel free to contact us.
				</p>
			</div>
			<div class="max-width-at-1000 align-left">
				<div class="row create-content-buttons">
					<div class="col-sm-4 col-xs-12">
						<a href="/contribute/question/new">
							<button class="btn btn-block btn-primary">
								<i class="fa fa-plus"></i>
								New Question
							</button>
						</a>
					</div>
					<div class="col-sm-4 col-xs-12">
						<a href="/contribute/practice-problem/new">
							<button class="btn btn-block btn-success">
								<i class="fa fa-plus"></i>
								New Practice Problem
							</button>
						</a>
					</div>				
					<div class="col-sm-4 col-xs-12">
						<a href="/contribute/text-content/new">
							<button class="btn btn-block btn-warning">
								<i class="fa fa-plus"></i>
								New Text Content
							</button>
						</a>
					</div>
				</div>
		
				<div class="box box-primary box-solid">
					<c:if test="${fn:length(submittedQuestions) == 0 || fn:length(unsubmittedQuestions) > 0}">
					<div class="box-header">
						<i class="fa fa-warning hidden-xs"></i>
						<h3 class="box-title">Unfinished/Unsubmitted Questions  <small>To open a question up for editing, or to submit it, click on its link below</small></h3>
					</div>
					<div class="box-body no-padding">
						<table class="table">
							<tbody>
									<tr>
										<td class="hidden-xs">#</td>
										<td>Question Title</td>
										<td>Editor</td>
										<td>Live Preview</td>
										<td>Last Edited</td>
									</tr>
								<c:forEach items="${unsubmittedQuestions}" var="question" varStatus="loop">
									<tr>
										<td class="hidden-xs"><a href="${question.editUrl}"> ${loop.index + 1}. </a></td>
										<td><a href="${question.editUrl}"> ${question.title}</a></td>
										<td><a class="badge bg-aqua" href="${question.editUrl}">Edit</a></td>
										<td><a class="badge bg-blue" href="${question.previewUrl}">Preview</a></td>
										<td>${question.readableTime}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						Total: ${fn:length(unsubmittedQuestions)} Unsubmitted Questions
					</div>
					</c:if>
			
					<c:if test="${fn:length(submittedQuestions) > 0}">
					<div class="box-header">
						<i class="fa fa-check hidden-xs"></i>
						<h3 class="box-title">Submitted Questions  <small>Though these can no longer be edited, you can see them in action!</small></h3>
					</div>
					<c:set var="totalKarma" value="${0}"/>
					<div class="box-body no-padding">
						<table class="table">		
							<tbody>
									<tr>
										<td class="hidden-xs">#</td>
										<td>Question Title</td>
										<td>Karma Earned</td>
										<td>Date Submitted</td>
									</tr>
								<c:forEach items="${submittedQuestions}" var="question" varStatus="loop">
									<tr>
										<td class="hidden-xs"><a href="${question.url}"> ${loop.index + 1}. </a></td>
										<td><a href="${question.url}"> ${question.title} </a></td>
										<td><span class="badge bg-blue">${question.karma} Karma</span></td>
										<td>${question.readableTime}</td>
										<c:set var="totalKarma" value="${totalKarma + question.karma}"/>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						Total: ${fn:length(submittedQuestions)} Submitted Questions, ${totalKarma} Karma
					</div>
					</c:if>
				</div>
		
		
		
				<c:if test="${fn:length(unsubmittedPracticeProblems) + fn:length(submittedPracticeProblems) > 0}">
				<div class="box box-success box-solid">
					<c:if test="${fn:length(unsubmittedPracticeProblems) > 0}">
					<div class="box-header">
						<i class="fa fa-warning hidden-xs"></i>
						<h3 class="box-title">Unfinished/Unsubmitted Problems  <small>To open a problem up for editing, or to submit it, click on its link below</small></h3>
					</div>
					<div class="box-body no-padding">
						<table class="table">
							<tbody>
									<tr>
										<td class="hidden-xs">#</td>
										<td>Problem Title</td>
										<td>Editor</td>
										<td>Live Preview</td>
										<td>Last Edited</td>
									</tr>
								<c:forEach items="${unsubmittedPracticeProblems}" var="practiceProblem" varStatus="loop">
									<tr>
										<td class="hidden-xs"><a href="${practiceProblem.editUrl}"> ${loop.index + 1}. </a></td>
										<td><a href="${practiceProblem.editUrl}"> ${practiceProblem.title}</a></td>
										<td><a class="badge bg-green" href="${practiceProblem.editUrl}">Edit</a></td>
										<td><a class="badge bg-olive" href="${practiceProblem.previewUrl}">Preview</a></td>
										<td>${practiceProblem.readableTime}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						Total: ${fn:length(unsubmittedPracticeProblems)} Unsubmitted Problems
					</div>
					</c:if>

					<c:if test="${fn:length(submittedPracticeProblems) > 0}">
					<div class="box-header">
						<i class="fa fa-check hidden-xs"></i>
						<h3 class="box-title">Submitted Problems  <small>Though these can no longer be edited, you can see them in action!</small></h3>
					</div>
					<c:set var="totalKarma" value="${0}"/>
					<div class="box-body no-padding">
						<table class="table">
							<tbody>
									<tr>
										<td class="hidden-xs">#</td>
										<td>Problem Title</td>
										<td>Karma Earned</td>
										<td>Date Submitted</td>
									</tr>
								<c:forEach items="${submittedPracticeProblems}" var="practiceProblem" varStatus="loop">
									<tr>
										<td class="hidden-xs"><a href="${practiceProblem.url}"> ${loop.index + 1}. </a></td>
										<td><a href="${practiceProblem.url}"> ${practiceProblem.title} </a></td>
										<td><span class="badge bg-olive">${practiceProblem.karma} Karma</span></td>
										<td>${practiceProblem.readableTime}</td>
									</tr>
									<c:set var="totalKarma" value="${totalKarma + practiceProblem.karma}"/>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						Total: ${fn:length(submittedPracticeProblems)} Submitted Problems, ${totalKarma} Karma
					</div>
					</c:if>
				</div>
				</c:if>
		
		
				<c:if test="${fn:length(unsubmittedContent) + fn:length(submittedContent) > 0}">
				<div class="box box-warning box-solid">
					<c:if test="${fn:length(unsubmittedContent) > 0}">
					<div class="box-header">
						<i class="fa fa-warning hidden-xs"></i>
						<h3 class="box-title">Unfinished/Unsubmitted Content  <small>To open up content for editing, or to submit it, click on its link below</small></h3>
					</div>
					<div class="box-body no-padding">
						<table class="table">
							<tbody>
									<tr>
										<td class="hidden-xs">#</td>
										<td>Content Title</td>
										<td>Editor</td>
										<td>Live Preview</td>
										<td>Last Edited</td>
									</tr>
								<c:forEach items="${unsubmittedContent}" var="content" varStatus="loop">
									<tr>
										<td class="hidden-xs"><a href="${content.editUrl}"> ${loop.index + 1}. </a></td>
										<td><a href="${content.editUrl}"> ${content.title}</a></td>
										<td><a class="badge bg-yellow" href="${content.editUrl}">Edit</a></td>
										<td><a class="badge bg-orange" href="${content.previewUrl}">Preview</a></td>
										<td>${content.readableTime}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						Total: ${fn:length(unsubmittedContent)} Pieces of Unsubmitted Content
					</div>
					</c:if>

					<c:if test="${fn:length(submittedContent) > 0}">
					<div class="box-header">
						<i class="fa fa-check hidden-xs"></i>
						<h3 class="box-title">Submitted Content  <small>Though these can no longer be edited, you can see them in action!</small></h3>
					</div>
					<c:set var="totalKarma" value="${0}"/>
					<div class="box-body no-padding">	
						<table class="table">
							<tbody>
									<tr>
										<td class="hidden-xs">#</td>
										<td>Content Title</td>
										<td>Karma Earned</td>
										<td>Date Submitted</td>
									</tr>
								<c:forEach items="${submittedContent}" var="content" varStatus="loop">
									<tr>
										<td class="hidden-xs"><a href="${content.url}"> ${loop.index + 1}. </a></td>
										<td><a href="${content.url}"> ${content.title} </a></td>
										<td><span class="badge bg-orange">${content.karma} Karma</span></td>
										<td>${content.readableTime}</td>
									</tr>
									<c:set var="totalKarma" value="${totalKarma + content.karma}"/>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						Total: ${fn:length(submittedContent)} Pieces of Content Submitted, ${totalKarma} Karma
					</div>
					</c:if>
				</div>
				</c:if>
		
				<c:if test="${fn:length(submittedAnswers) > 0}">
					<div class="box box-danger box-solid">
						<div class="box-header">
							<i class="fa fa-check hidden-xs"></i>
							<h3 class="box-title">Submitted Answers  <small>Though these can no longer be edited, you can see them in action!</small></h3>
						</div>
						<c:set var="totalKarma" value="${0}"/>
						<div class="box-body no-padding">	
							<table class="table">
								<tbody>
									<tr>
										<td class="hidden-xs">#</td>
										<td>Answer Title</td>
										<td>Karma Earned</td>
										<td>Date Submitted</td>
									</tr>
									<c:forEach items="${submittedAnswers}" var="answer" varStatus="loop">
										<tr>
											<td class="hidden-xs"><a href="${answer.url}"> ${loop.index + 1}. </a></td>
											<td><a href="${answer.url}"> ${answer.title} </a></td>
											<td><span class="badge bg-orange">${answer.karma} Karma</span></td>
											<td>${answer.readableTime}</td>
										</tr>
										<c:set var="totalKarma" value="${totalKarma + answer.karma}"/>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="box-footer">
							Total: ${fn:length(submittedAnswers)} Answers Submitted, ${totalKarma} Karma
						</div>
					</div>
				</c:if>
			</div>
		</div>		
	</jsp:attribute>
</t:genericpage>
