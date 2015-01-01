<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Practice Problem Dashboard
	</jsp:attribute>
	<jsp:attribute name="content">
	
		<div class="row create-content-buttons">
			<div class="col-sm-4 col-xs-12">
				<button class="btn btn-block btn-primary">
					<i class="fa fa-plus"></i>
					New Question
				</button>
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
				<button class="btn btn-block btn-warning">
					<i class="fa fa-plus"></i>
					New Text Content
				</button>
			</div>
		</div>
	
		<div class="box box-info">
			<div class="box-header">
				<i class="fa fa-info-circle hidden-xs"></i>
				<h3 class="box-title">Creating Content for CalcU  <small>Read this to become familiar with directions, or click above to jump in!</small></h3>
			</div>
			<div class="box-body">
				There are three main types of content on CalcU.  Questions are asked by our users and answered by other users, they are 
				usually general questions, not focusing on a specific problem.  Practice Problems are submitted by users alongside a 
				solution, they are meant to give people practice with a specific topic in calculus, or to provide general practice.
				Textual content is submitted to provide an explanation of a specific concept or set of concepts, and should come from
				an authoritative source. To begin creation of any of these types, click their respective link above.  You will be able
				to save your work, and revisit it later. Once you are done editing, you can submit your work (either with your name, or 
				anonymously). To continue editing your work, or submit it, you can come back to this page, the 'content dashboard', where 
				a link will be made available. You can also track all of your past submissions from this page, and see how they have done 
				as people have rated and reviewed them. If you have any questions about content creation, please feel free to contact us.
			</div>
		</div>
	
		<div class="box box-warning">
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
								<td>Karma Earned</td>
								<td>Last Edited</td>
							</tr>
						<c:forEach items="${unsubmittedPracticeProblems}" var="practiceProblem" varStatus="loop">
							<tr>
								<td class="hidden-xs">${loop.index + 1}.</td>
								<td><a href="${practiceProblem.editUrl}"> ${practiceProblem.title} </a></td>
								<td><span class="badge bg-red">0 Karma</span></td>
								<td>${practiceProblem.readableTime}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="box-footer">
				Total: ${fn:length(unsubmittedPracticeProblems)} Unsubmitted Problems
			</div>
		</div>
		
		<div class="box box-success">
			<div class="box-header">
				<i class="fa fa-check hidden-xs"></i>
				<h3 class="box-title">Submitted Problems  <small>Though these can no longer be edited, you can check out the comments and karma on them by clicking below</small></h3>
			</div>
			<div class="box-body no-padding">
				<table class="table">
					<tbody>
							<tr>
								<td class="hidden-xs">#</td>
								<td>Problem Title</td>
								<td>Karma Earned</td>
								<td>Last Edited</td>
							</tr>
						<c:forEach items="${submittedPracticeProblems}" var="practiceProblem" varStatus="loop">
							<tr>
								<td class="hidden-xs">${loop.index + 1}.</td>
								<td><a href="${practiceProblem.url}"> ${practiceProblem.title} </a></td>
								<td><span class="badge bg-blue">70 Karma</span></td>
								<td>${practiceProblem.readableTime}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="box-footer">
				Total: ${fn:length(submittedPracticeProblems)} Submitted Problems, 100,000 Karma
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>
