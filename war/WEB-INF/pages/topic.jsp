<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Topics
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000 margin-bottom-50 full-width">
				<h2 class="welcome-banner margin-top-100">${topic.title}</h2>
				<h3 class="welcome-banner-subtitle margin-bottom-50">${topic.shortDescription}</h3>
				<div class="align-left">
					<div class="box-body">
						<div class="box box-warning box-solid">
							<div class="box-header">
								<h3 class="box-title">Text Content</h3>
								<div class="pull-right box-tools">
									<button class="btn btn-warning btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
								</div>
							</div>
							<div class="box-body">
								<c:forEach begin="1" end="5" var="i">
									<div class="alert alert-dismissable alert-warning margin-top-20">
										<i class="fa">${i}</i>
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
										<a href="${question.url}"><b>Pythagorean Theorem</b></a>
										This is a bunch of garbage text used for testing, you should really not read this. This really isn't worth your time man. Just stop here. 
									</div>
								</c:forEach>
								<div class="centered">
									<div class="btn-group margin-bottom-10">
										<c:forEach begin="1" end="5" var="i">
											<button class="result-page-tab btn btn-warning" id="tc-result-page-${i}-tab">${i}</button>
										</c:forEach>
									</div>
								</div>
							</div>	
						</div>
						<div class="box box-success box-solid">
							<div class="box-header">
								<h3 class="box-title">Practice Problems</h3>
								<div class="pull-right box-tools">
									<button class="btn btn-success btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
								</div>
							</div>
							<div class="box-body">
								<c:forEach begin="1" end="5" var="i">
									<div class="alert alert-dismissable alert-success margin-top-20">
										<i class="fa">${i}</i>
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
										<a href="${question.url}"><b>Pythagorean Theorem Practice Problem</b></a>
										This is a bunch of garbage text used for testing, you should really not read this. This really isn't worth your time man. Just stop here. 
									</div>
								</c:forEach>
								<div class="centered">
									<div class="btn-group margin-bottom-10">
										<c:forEach begin="1" end="5" var="i">
											<button class="result-page-tab btn btn-success" id="tc-result-page-${i}-tab">${i}</button>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
						<div class="box box-primary box-solid">
							<div class="box-header">
								<h3 class="box-title">Questions</h3>
								<div class="pull-right box-tools">
									<button class="btn btn-primary btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
								</div>
							</div>
							<div class="box-body">
								<c:forEach begin="1" end="5" var="i">
									<div class="alert alert-dismissable alert-info margin-top-20">
										<i class="fa">${i}</i>
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
										<a href="${question.url}"><b>Pythagorean Theorem Question</b></a>
										This is a bunch of garbage text used for testing, you should really not read this. This really isn't worth your time man. Just stop here. 
									</div>
								</c:forEach>
								<div class="centered">
									<div class="btn-group margin-bottom-10">
										<c:forEach begin="1" end="5" var="i">
											<button class="result-page-tab btn btn-primary" id="tc-result-page-${i}-tab">${i}</button>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
						<div class="box box-info box-solid">
							<div class="box-header">
								<h3 class="box-title">Practice Tests</h3>
								<div class="pull-right box-tools">
									<button class="btn btn-info btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
								</div>
							</div>
							<div class="box-body row">
								<form class="col-sm-12 col-md-12 col-lg-12" action="/topic/${topic.uuid}/generate-test" method="post">
									<input type="hidden" name="userId" value="${user.userId}"/>
									<div class="col-sm-12 col-md-12 col-lg-12 no-padding">
										<label for="difficulty">Difficulty</label>
										<select class="form-control" name="difficulty">
											<option value="very-difficult">Very Difficult</option>
											<option value="difficult">Difficult</option>
											<option value="medium" selected>Medium</option>
											<option value="easy">Easy</option>
											<option value="very-easy">Very Easy</option>
										</select>
									</div>
									<div class="col-sm-12 col-md-12 col-lg-12 no-padding margin-top-10">
										<label for="question-number">Question Number</label>
										<select class="form-control" name="question-number">
											<option value="5">5</option>
											<option value="10" selected>10</option>
											<option value="15">15</option>
											<option value="20">20</option>
										</select>
									</div>
									<div class="col-sm-12 col-md-12 col-lg-12 no-padding margin-top-10">
										<label for="time-limit">Time Limit</label>
										<select class="form-control" name="time-limit">
											<option value="unlimited">Unlimited</option>
											<option value="10-minutes">10 minutes</option>
											<option value="20-minutes">20 minutes</option>
											<option value="30-minutes">30 minutes</option>
										</select>
									</div>
									<input type="submit" value="Generate Practice Test" class="btn btn-primary pull-right margin-top-10"/>
								</form>
							</div>
						</div>
					</div>
					<div class="centered row">
						<div class="col-sm-12 col-md-6">
							<h2 class=" welcome-banner-subtitle centered margin-top-bottom-50"> Related Topics</h2>
							<p>
								<c:forEach begin="1" end="5" var="i">
									<a href="/answer/practice-problem/new" class="btn btn-block btn-warning topic-related-box-display">Something Something ${i}</a>
								</c:forEach>
							</p>
						</div>
						<div class="col-sm-12 col-md-6">
							<h2 class=" welcome-banner-subtitle centered margin-top-bottom-50"> Related Tags</h2>
							<div class="row">
								<c:forEach begin="1" end="20" var="i">
									<a href="/search/" class="btn btn-primary large-input-group-button margin-5-all">topic ${i}</a>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>