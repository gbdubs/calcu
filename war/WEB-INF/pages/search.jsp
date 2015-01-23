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
		<c:if test="${fn:length(resultQuestions) > 0}">
			<div class="box box-primary">
				<div class="box-header">
					<i class="fa fa-question fa-karma-score"></i>
					<h3 class="box-title">${fn:length(resultQuestions)} Matching Questions</h3>
					<div class="box-tools pull-right">
						<button class="btn btn-primary btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
					</div>
				</div>
				<div class="box-body">
					<c:set var="numPages" value="${1}"/>
					<div class="search-result-page" id="q-result-page-1">
					<c:forEach items="${resultQuestions}" var="question" varStatus="loop">
						
						<c:if test="${loop.index % 5 == 0 && loop.index > 0}">
							</div>
							<c:set var="numPages" value="${numPages + 1}"/>
							<div class="search-result-page hidden" id="q-result-page-${numPages}">
						</c:if>
						
						<c:set var="bookmarked" value="false" />
						<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
						  <c:if test="${bookmarkUuid eq question.uuid}">
							<c:set var="bookmarked" value="true" />
						  </c:if>
						</c:forEach>

						<div class="alert alert-dismissable alert-info">
						
							<i class="fa">${question.karma}</i>
							
							<c:choose>
								<c:when test="${bookmarked}">
									<button type="button" class="remove-bookmark-button bookmarked-button pull-right buttonless" data-user="${user.userId}" data-content="${question.uuid}">
										<i class="fa fa-bookmark"></i>
									</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="add-bookmark-button pull-right buttonless" data-user="${user.userId}" data-content="${question.uuid}">
										<i class="fa fa-bookmark"></i>
									</button>
								</c:otherwise>
							</c:choose>
							
							<a href="${question.url}"><b>${question.title}</b></a>
							${question.abbreviatedBody}
						</div>
					</c:forEach>
					</div>
					<c:if test="${numPages > 1}">
						<div class="btn-group no-margin">
							<c:forEach begin="1" end="${numPages}" var="i">
								<button class="result-page-tab btn btn-primary" id="q-result-page-${i}-tab">${i}</button>
							</c:forEach>
						</div>
					</c:if>
				</div>
			</div>
		</c:if>	
		
		<c:if test="${fn:length(resultPracticeProblems) > 0}">
			<div class="box box-success">
				<div class="box-header">
					<i class="fa fa-pencil fa-karma-score"></i>
					<h3 class="box-title">${fn:length(resultPracticeProblems)} Matching Practice Problems</h3>
					<div class="box-tools pull-right">
						<button class="btn btn-success btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
					</div>
				</div>
				<div class="box-body">
					<c:set var="numPages" value="${1}"/>
					<div class="search-result-page" id="pp-result-page-1">
					<c:forEach items="${resultPracticeProblems}" var="practiceProblem" varStatus="loop">
						
						<c:if test="${loop.index % 5 == 0 && loop.index > 0}">
							</div>
							<c:set var="numPages" value="${numPages + 1}"/>
							<div class="search-result-page hidden" id="pp-result-page-${numPages}">
						</c:if>
						
						<c:set var="bookmarked" value="false" />
						<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
						  <c:if test="${bookmarkUuid eq practiceProblem.uuid}">
							<c:set var="bookmarked" value="true" />
						  </c:if>
						</c:forEach>

						<div class="alert alert-dismissable alert-success">
						
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
							
							<a href="${practiceProblem.url}"><b>${practiceProblem.title}</b></a>
							${practiceProblem.abbreviatedBody}
						</div>
					</c:forEach>
					</div>
					<c:if test="${numPages > 1}">
						<div class="btn-group no-margin">
							<c:forEach begin="1" end="${numPages}" var="i">
								<button class="result-page-tab btn btn-success" id="pp-result-page-${i}-tab">${i}</button>
							</c:forEach>
						</div>
					</c:if>
				</div>
			</div>
		</c:if>
		
		<c:if test="${questionsNotFound}">
			<div class="box box-primary">
				<div class="box-header">
					<i class="fa fa-frown-o fa-karma-score hidden-xs"></i>
					<h3 class="box-title">No Matching Questions <small> Try adding more tags, to broaden your search results </small></h3>
				</div>
			</div>
		</c:if>
		
		<c:if test="${practiceProblemsNotFound}">
			<div class="box box-success">
				<div class="box-header">
					<i class="fa fa-frown-o fa-karma-score hidden-xs"></i>
					<h3 class="box-title">No Matching Practice Problems <small> Try adding more tags, to broaden your search results </small></h3>
				</div>
			</div>
		</c:if>
		
		<c:if test="${practiceProblemsNotFound || questionsNotFound}">
			<div class="box box-default">
				<div class="box-header">
					<i class="fa fa-question fa-karma-score hidden-xs"></i>
					<h3 class="box-title">Didn't Find What You Were Looking For?</h3>
				</div>
				<div class="box-body">
					If you didn't find what you were looking for in this search, don't despair, you have some great options!  You can <a href="/contribute/question/new">ask the question yourself</a>,
					<a href="/external-resources">check out sites that might have your answer</a>, or try searching again with more tags.  The more tags you include, the more results you will get!
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
	