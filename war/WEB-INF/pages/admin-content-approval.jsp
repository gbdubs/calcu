<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Approve Content
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="box box-solid">
			<div class="box-header solid-box-header">
				<i class="fa fa-cube fa-karma-score hidden-xs"></i>
				<h3 class="box-title">Welcome to Content Approval Mode!</h3>
			</div>
			<div class="box-body">
				<p>
					Here is a pane to take auto uploaded/scraped problems, and review them before they are put into circulation.
					The pane on your left shows each outstanding issue. Once you have reviewed a piece of potential content, you have five options:
					<ul>
						<li> You can create new content from the Potential Content, in either the Practice Problem, Question, or Text Content flavor.</li>
						<li> You can flag the content for editing, and you will be able to edit it before you pass it on to be accepted content.</li>
						<li> Or you can simply delete the potential content, and give up on its possibility.</li>
					</ul>
					Once you have marked content for editing, it will be available in the editing page.
				</p>
				<div class="content-approval-window">
					<div class="content-approval-sidebar">
						<div class="header">
							<b>Select a Proposed Piece Of Content To Begin Editing</b>
						</div>
						<ul>
							<c:forEach var="pc" items="${potentialContent}" varStatus="loop">
								<li id="potential-content-${loop.index}">
									<h5><b>${pc.title}</b></h5>
									<i>${pc.createdAtDate}</i>
								</li>
							</c:forEach>
						</ul>
					</div>
					<div class="content-approval-viewer">
						<c:forEach var="pc" items="${potentialContent}" varStatus="loop">
							<c:choose>
								<c:when test="${loop.index > 0}">
									<div class="content-approval-content-display hidden" id="potential-content-${loop.index}-viewer">
								</c:when>
								<c:otherwise>
									<div class="content-approval-content-display" id="potential-content-${loop.index}-viewer">
								</c:otherwise>
							</c:choose>
								<div class="content-approval-action-buttons btn-group" data-uuid="${pc.uuid}">
									<button data-action="create" data-type="practiceProblem" class="btn btn-success content-approval-action-button" type="button"><i class="fa fa-plus"></i>  Practice Problem</button>
									<button data-action="create" data-type="question" class="btn btn-primary content-approval-action-button" type="button"><i class="fa fa-plus"></i>  Question</button>
									<button data-action="create" data-type="textContent" class="btn btn-warning content-approval-action-button" type="button"><i class="fa fa-plus"></i>  TextContent</button>
									<button data-action="flag" class="btn btn-info content-approval-action-button" type="button">Flag for Editing</button>
									<button data-action="delete" class="btn btn-danger content-approval-action-button" type="button">Delete</button>
								</div>
								<label>Title</label>
								<input name="title" type="text" class="content-approval-title form-control" value="${pc.title}"/>
								<div>
									<label for="body">Problem Body</label>
									<textarea name="body" class="textarea wysihtml5-beam-me-up" placeholder="Construct your question here">${pc.body}</textarea>
								</div>
								<div>
									<label for="body">Official Solution</label>
									<textarea name="authorSolution" class="textarea wysihtml5-beam-me-up" placeholder="Construct your solution here">${pc.solution}</textarea>
								</div>
								<label>Tags</label>
								<input name="tagsInput" type="text" class="content-approval-tags form-control" value="${pc.tagsAsString}"/>
								
								<div class="content-approval-rendered">
									<h3>${pc.title}</h3>
									<label>Body</label>
									<div>${pc.body}</div>
									<label>Solution</label>
									<div>${pc.solution}</div>
									<label>Tags</label>
									<div>${pc.tagsAsString}</div>
								</div>
							</div>
							
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
		
	</jsp:attribute>
	<jsp:attribute name="javascriptDependencies">
		content-approval
	</jsp:attribute>
</t:genericpage>