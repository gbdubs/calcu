<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Search
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="centered">
			<h2 class="welcome-banner margin-top-100">Search By Tag</h2>
			<h3 class="margin-bottom-50 welcome-banner-subtitle">Finds results that have one or more tags. The highest results will match the most tags.</h3>
			<div class="margin-bottom-50 max-width-at-700">
				<form action="/search" method="post">
					<div class="input-group">
						<input name="tagsInput" id="tags-input" value="${tags}" />
						<span class="input-group-btn">
							<input class="btn btn-primary large-input-group-button" type="submit" value="Search!">
						</span>
					</div>
				</form>
				<c:if test="${recommendedTags != null}">
					<label for="recommended-tag-wrapper" class="margin-5">Consider Adding These Tags To Your Search To Get More Results!</label>
					<div id="recommended-tag-wrapper" class="tagsinput">
						<c:forEach var="recTag" items="${recommendedTags}">
							<span class="tag bg-green add-recommended-tag" data-tag="${recTag}">
								<span>${recTag}</span>
							</span>
						</c:forEach>
					</div>
				</c:if>
			</div>
			<br>
			<div class = "max-width-at-1000 align-left ">
			<c:if test="${practiceProblemsNotFound || fn:length(resultPracticeProblems) > 0 }">
			
			<div class="box-body">
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
													<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="remove" data-user="${user.userId}" data-content="${question.uuid}">
														<i class="fa fa-bookmark"></i>
													</button>
												</c:when>
												<c:otherwise>
													<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="${user.userId}" data-content="${question.uuid}">
														<i class="fa fa-bookmark-o"></i>
													</button>
												</c:otherwise>
											</c:choose>
											<a href="${question.url}"><b>${question.title}</b></a>
											<span class="max-height-500">
												${question.abbreviatedBody}
											</span>
										</div>
								</c:forEach>
								</div>
									<c:if test="${numPages > 1}">
										<div class="centered">
											<div class="btn-group no-margin">
												<c:forEach begin="1" end="${numPages}" var="i">
													<button class="result-page-tab btn btn-primary" id="q-result-page-${i}-tab">${i}</button>
												</c:forEach>
											</div>
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
												<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="remove" data-user="${user.userId}" data-content="${practiceProblem.uuid}">
													<i class="fa fa-bookmark"></i>
												</button>
											</c:when>
											<c:otherwise>
												<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="${user.userId}" data-content="${practiceProblem.uuid}">
													<i class="fa fa-bookmark-o"></i>
												</button>
											</c:otherwise>
										</c:choose>
							
										<a href="${practiceProblem.url}"><b>${practiceProblem.title}</b></a>
										<span class="max-height-500">
											${practiceProblem.abbreviatedBody}
										</span>
									</div>
								</c:forEach>
								</div>
								<c:if test="${numPages > 1}">
									<div class="centered">
										<div class="btn-group no-margin">
											<c:forEach begin="1" end="${numPages}" var="i">
												<button class="result-page-tab btn btn-success" id="pp-result-page-${i}-tab">${i}</button>
											</c:forEach>
										</div>
									</div>
								</c:if>
							</div>
						</div>
					</c:if>
		
					<c:if test="${fn:length(resultTextContent) > 0}">
						<div class="box box-warning">
							<div class="box-header">
								<i class="fa fa-newspaper-o fa-karma-score"></i>
								<h3 class="box-title">${fn:length(resultTextContent)} Matching Explanations</h3>
								<div class="box-tools pull-right">
									<button class="btn btn-warning btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
								</div>
							</div>
							<div class="box-body">
								<c:set var="numPages" value="${1}"/>
								<div class="search-result-page" id="pp-result-page-1">
									<c:forEach items="${resultTextContent}" var="textContent" varStatus="loop">
									<c:if test="${loop.index % 5 == 0 && loop.index > 0}">
								</div>
									<c:set var="numPages" value="${numPages + 1}"/>
									<div class="search-result-page hidden" id="tc-result-page-${numPages}">
									</c:if>
										<c:set var="bookmarked" value="false" />
									<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
									  <c:if test="${bookmarkUuid eq textContent.uuid}">
										<c:set var="bookmarked" value="true" />
									  </c:if>
									</c:forEach>

									<div class="alert alert-dismissable alert-warning">
						
										<i class="fa">${textContent.karma}</i>
							
										<c:choose>
											<c:when test="${bookmarked}">
												<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="remove" data-user="${user.userId}" data-content="${textContent.uuid}">
													<i class="fa fa-bookmark"></i>
												</button>
											</c:when>
											<c:otherwise>
												<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="${user.userId}" data-content="${textContent.uuid}">
													<i class="fa fa-bookmark-o"></i>
												</button>
											</c:otherwise>
										</c:choose>
							
										<a href="${textContent.url}"><b>${textContent.title}</b></a>
										<span class="max-height-500">
											${textContent.abbreviatedBody}
										</span>
									</div>
								</c:forEach>
								</div>
								<c:if test="${numPages > 1}">
									<div class="centered">
										<div class="btn-group no-margin">
											<c:forEach begin="1" end="${numPages}" var="i">
												<button class="result-page-tab btn btn-warning" id="tc-result-page-${i}-tab">${i}</button>
											</c:forEach>
										</div>
									</div>
								</c:if>
							</div>
						</div>
					</c:if>
		
					<c:if test="${questionsNotFound && !moreResults}">
						<div class="box box-primary">
							<div class="box-header">
								<i class="fa fa-frown-o fa-karma-score hidden-xs"></i>
								<h3 class="box-title">No Matching Questions <small> Try adding more tags, to broaden your search results </small></h3>
							</div>
						</div>
					</c:if>
		
					<c:if test="${practiceProblemsNotFound && !moreResults}">
						<div class="box box-success">
							<div class="box-header">
								<i class="fa fa-frown-o fa-karma-score hidden-xs"></i>
								<h3 class="box-title">No Matching Practice Problems <small> Try adding more tags, to broaden your search results </small></h3>
							</div>
						</div>
					</c:if>
		
					<c:if test="${textContentNotFound && !moreResults}">
						<div class="box box-warning">
							<div class="box-header">
								<i class="fa fa-frown-o fa-karma-score hidden-xs"></i>
								<h3 class="box-title">No Matching Explanations <small> Try adding more tags, to broaden your search results </small></h3>
							</div>
						</div>
					</c:if>
		
					<c:if test="${moreResults || seed > 1}">

							<div class="overflower">
								<c:if test="${seed > 1}">
									<form action="/search/${tags}" method="get" class="pull-left">
										<input type="hidden" name="seed" value="${seed - 1}"/>
										<button class="btn btn-primary" type="submit">
											<i class="fa fa-arrow-circle-left fa-fw"></i>
											Previous 50 Results
										</button>
									</form>
								</c:if>
								<c:if test="${moreResults}">
									<form action="/search/${tags}" method="get" class="pull-right">
										<input type="hidden" name="seed" value="${seed + 1}"/>
										<button class="btn btn-primary" type="submit">
											Next 50 Results
											<i class="fa fa-arrow-circle-right fa-fw"></i>
										</button>
									</form>
								</c:if>
							</div>

					</c:if>
					</div>
					</c:if>
		
					<c:if test="${practiceProblemsNotFound || questionsNotFound || textContentNotFound}">
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
						<div class= "max-width-at-1000 align-left">
							<p>
								The search by tag feature is a powerful tool for exploring content when you have a good idea of what you are interested in looking at.
								You can choose to search with as many "tags" as you want. Each tag is treated like a plain word, and results with matching tags will
								be displayed. Tags can contain spaces, dashes and apostrophes, but no other characters.  Tags are not case sensitive.
								Note that adding more tags will never decrease the number of results you return, it can only increase them. To type in a
								tag, simply type the word or multi word expression (no commas) into the box above, and press enter. That completes the tag, and you can
								begin to type a new tag. When you have a sequence of tags you want to try, press the search button.
							</p>
							<p>
								After you have completed a search, a box may pop up which contains tags you might want to consider adding to your search. 
								These tags will help you broaden your search, by suggesting tags which will widen the number of results that you get back,
								including ones which might be very close to your
								The other will refine your search, bringing to the top content which matches it along with your current search.
							</p>
							<p>
								Finally, we are always doing our best to respond to the needs and requests of our users. If you think there is another way to explore
								our content that could be helpful, please visit our contact page and let us know about it!
							</p>
						</div>
					</div>
				</div>
	</jsp:attribute>	

	<jsp:attribute name="javascriptDependencies">
		searchByTag
	</jsp:attribute>
</t:genericpage>
	