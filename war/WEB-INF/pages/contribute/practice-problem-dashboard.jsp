<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Practice Problem Dashboard
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="box box-info">
			<a href="/contribute/practice-problem/new">
				<div class="box-header">
					<i class="fa fa-plus"></i>
					<h3 class="box-title">Create New Practice Problem  <small>Click here to create a new problem, rather than editing a non-submitted problem</small></h3>
				</div>
			</a>
		</div>
		<div class="box box-warning">
			<div class="box-header">
				<i class="fa fa-warning"></i>
				<h3 class="box-title">Unfinished/Unsubmitted Problems  <small>To open a problem up for editing, or to submit it, click on its link below</small></h3>
			</div>
			<div class="box-body">
				<table class="table">
					<tbody>
						<c:forEach items="${unsubmittedPracticeProblems}" var="practiceProblem" varStatus="loop">
							<tr>
								<td>${loop.index + 1}.</td>
								<td><a href="${practiceProblem.editUrl}"> ${practiceProblem.problemTitle} </a></td>
								<td><span class="badge bg-red">0 Karma</span></td>
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
				<i class="fa fa-check"></i>
				<h3 class="box-title">Submitted Problems  <small>Though these can no longer be edited, you can check out the comments and karma on them by clicking below</small></h3>
			</div>
			<div class="box-body">
				<table class="table">
					<tbody>
						<c:forEach items="${submittedPracticeProblems}" var="practiceProblem" varStatus="loop">
							<tr>
								<td>${loop.index + 1}.</td>
								<td><a href="${practiceProblem.url}"> ${practiceProblem.problemTitle} </a></td>
								<td><span class="badge bg-blue">70 Karma</span></td>
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
