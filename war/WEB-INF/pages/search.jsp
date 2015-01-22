<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Search
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="box box-solid">
			<div class="box-header">
				<h3 class="box-title">Search By Tag <small>  Finds results that have one or more tags. The highest results will match the most tags.</small></h3>
			</div>
			<div class="box-body">
				<label for="tags-input">Tags/Categories To Search</label>
				<form action="/search" method="post">
					<div class="input-group">
						<input name="tagsInput" id="tags-input" value="${tags}" />
						<span class="input-group-btn">
							<input class="btn btn-primary large-input-group-button" type="submit" value="Search!">
						</span>
					</div>
				</form>
			</div>
		</div>	
		<c:if test="${fn:length(resultPracticeProblems) > 0}">
			<div class="box box-success">
				<div class="box-header">
					<i class="fa fa-pencil fa-karma-score"></i>
					<h3 class="box-title">${fn:length(resultPracticeProblems)} Matching Practice Problems</h3>
				</div>
				<div class="box-body">
					<c:forEach items="${resultPracticeProblems}" var="practiceProblem" varStatus="loop">
					
						<c:set var="bookmarked" value="false" />
						<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
						  <c:if test="${bookmarkUuid eq practiceProblem.uuid}">
							<c:set var="bookmarked" value="true" />
						  </c:if>
						</c:forEach>

						<c:choose>
							<c:when test="${bookmarked}"> <div class="alert alert-dismissable alert-success"> </c:when>
							<c:otherwise>                 <div class="alert alert-dismissable alert-info"> </c:otherwise>
						</c:choose>								
						
							<i class="fa">${practiceProblem.karma}</i>
							
							<c:choose>
								<c:when test="${bookmarked}">
									<button type="button" class="remove-bookmark-button bookmarked-button pull-right buttonless" data-user="${user.userId}" data-content="${practiceProblem.uuid}">
										<i class="fa fa-bookmark"></i>
									</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="add-bookmark-button pull-right buttonless" data-user="${user.userId}" data-content="${practiceProblem.uuid}">
										<i class="fa fa-bookmark"></i>
									</button>
								</c:otherwise>
							</c:choose>
							
							<a href="${practiceProblem.url}">
								<b>${practiceProblem.title}</b> ${practiceProblem.abbreviatedBody}
							</a>
						</div>
					</c:forEach>
				</div>
			</div>
		</c:if>
		<c:if test="${practiceProblemsNotFound}">
			<div class="box box-warning">
				<duv class="box-header">
					<i class="fa fa-frown-o fa-karma-score"></i>
					<h3 class="box-title">No Matching Practice Problems <small> Try adding more tags, to broaden your search results </small></h3>
				</div>
			</div>
		</c:if>
	</jsp:attribute>	
	<jsp:attribute name="javascript">
		<script src="/_static/js/plugins/jQuery-Tags-Input-master/jquery.tagsinput.min.js"></script>
		<link rel="stylesheet" type="text/css" href="jquery.tagsinput.css" />
		<script src="/_static/js/CalcU/searchByTag.js"></script>
	</jsp:attribute>
</t:genericpage>
	