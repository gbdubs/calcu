<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Practice Problem
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000 inline-block align-left margin-top-50">
				<c:if test="${contentApprovalMode}">
					<form class="content-approval-buttons" action="/content-approval" method="POST">
						<input type="submit" class="btn btn-success" value="Approved" name="approved"/>
						<input type="submit" class="btn btn-danger" value="Not Approved" name="not-approved"/>
						<input type="hidden" value="${practiceProblem.uuid}" name="contentUuid"/>
					</form>
				</c:if>
			
				<c:if test="${!livePreview && !practiceProblem.viewable}">
					<div class="box box-solid bg-maroon-gradient">
						<div class="box-header solid-box-header">
							<i class="fa fa-eye-slash fa-karma-score hidden-xs"></i>
							<h3 class="box-title">This Content Is Currently Invisible</h3>
						</div>
					</div>
				</c:if>
				<c:if test="${livePreview}">
					<div class="box box-solid bg-yellow-gradient">
						<div class="box-header solid-box-header">
							<i class="fa fa-photo fa-karma-score hidden-xs"></i>
							<h3 class="box-title">Live Preview Mode</h3>
							<a href="${practiceProblem.editUrl}" class="btn btn-warning pull-right live-preview-return-button">Back To Editor</a>
						</div>
						<div class="box-body">
							You are currently in live preview mode. To switch back to the edit view, hit the 'Back to Editor' button.
							Nothing on this page is visible to other users, for now. 
							Live Preview enables you to make sure that your formatting looks the way you want it to before you submit your content.
							Everything on the page is as you can expect it once you submit, except for this box.
							To continue to edit your content, before you submit, you can hit the edit button in this panel.
						</div>
					</div>			
				</c:if>
				<c:if test="${answerMode}">
					<div class="box box-solid bg-olive">
						<div class="box-header solid-box-header">
							<i class="fa fa-cube fa-karma-score hidden-xs"></i>
							<h3 class="box-title">Solve Practice Problem Mode</h3>
							<form action="/answer/practice-problem/new" method="post" class="pull-right box-tools">
								<input type="hidden" name="answerModeStreak" value="${answerModeStreak}"/>
								<input type="hidden" name="parentUuid" value="${practiceProblem.uuid}"/>
								<input type="hidden" name="action" value="skip"/>
								<input type="hidden" name="contentType" value="practice-problem"/>
								<input type="submit" class="btn btn-success" value="Skip" />
							</form>
						</div>
						<div class="centered practice-problem-streak">
							<h2>Streak</h2>
							<h2>${answerModeStreak}</h2>
						</div>
						<div class="box-body">
							<p>
							You are currently in Problem Solving Turbo Mode. That name isn't real, but here is the deal!
							We will present you with problems we think that you would be the perfect person to solve.
							For most of them, the author's solution hasn't been satisfactory to most viewers.
							If you can solve the problem, please do! You will get a tidy karma bonus for answering the problems we select for you. After you solve the problem, hit the 'Done' button.
							If you don't think that you can answer the question well, or if you think that the answers are already good enough, press the 'Skip' button. Pressing 'Skip' will not change your Answer Streak.
							At any time, you can navigate away from this page, but know that your Answer Streak will be broken! Good Luck, and thank you for your contribution to our project!
							</p>
							<c:if test="${practiceProblem == null}">
								<div class="box box-solid bg-yellow-gradient no-margin">
									<div class="box-header">
										<h3 class="box-title">You have answered (or authored) all available Practice Problems! Go outside! Take a break! Wait for your medal! </h3>
									</div>
								</div>
							</c:if>
						</div>
					</div>
				</c:if>
				
				<c:if test="${difficultyCalibration}">
					<div class="box box-success box-solid">
						<div class="box-header">
							<i class="fa fa-refresh"></i>
							<h3 class="box-title">Difficulty Calibration: Step (${stepNumber}/10)</h3>
						</div>
						<div class="box-body">
							<p>
								How difficult would it be for you to solve this problem?
							</p>
							<ul>
								<li>"Not Yet" -- I am not yet able to answer this question, in any amount of time. </li>
								<li>"Very Difficult" -- The math in this example is significantly more difficult than I have been working with.</li>
								<li>"Difficult" -- I have worked with a few problems this difficult, with mixed success.</li>
								<li>"Moderate" -- This question is approximately as difficult as the problems I am looking for.</li>
								<li>"Easy" -- I know how to solve this question, and I am confident I could do it.</li>
								<li>"Very Easy" -- I have progressed beyond this point in my Calculus curriculum, and I am very confident in this area of calculus.</li>
							</ul>	
							<div class="btn-group difficulty-rating-buttons" data-uuid="${practiceProblem.uuid}" data-user="${user.userId}">
								<button class="btn btn-default" data-difficulty="not-yet">Not Yet</button>
								<button class="btn btn-default" data-difficulty="very-difficult">Very Difficult</button>
								<button class="btn btn-default" data-difficulty="difficult">Difficult</button>
								<button class="btn btn-default" data-difficulty="moderate">Moderate</button>
								<button class="btn btn-default" data-difficulty="easy">Easy</button>
								<button class="btn btn-default" data-difficulty="very-easy">Very Easy</button>
							</div>
							<div class="overflower">
								<div class="pull-right">
									<a class="btn btn-success" href="/personalize/${stepNumber + 1}">
										Next Step
										<i class="fa fa-arrow-circle-right fa-fw"></i>
									</a>
								</div>
							</div>
						</div>
					</div>
				</c:if>
				
				<div class="box-solid practice-problem">
					<div class="box-header">
						<c:if test="${! practiceProblem.anonymous}">
							<h3 class="box-title">Practice Problem <small>by <a href="${practiceProblem.author.profileUrl}">${practiceProblem.author.username}</a> on ${practiceProblem.shortReadableTime}</small></h3>
						</c:if>
						<c:if test="${practiceProblem.anonymous}">
							<h3 class="box-title">Practice Problem <small>submitted on ${practiceProblem.shortReadableTime}</small></h3>
						</c:if>
						<div class="dropdown pull-right report-menu primary-report-menu top-negative-45">
							<a class="dropdown-toggle btn btn-default" data-toggle="dropdown" href="#">
								Actions <span class="caret"></span>
							</a>
							<c:set var="bookmarked" value="false" />
							<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
							  <c:if test="${bookmarkUuid eq practiceProblem.uuid}">
								<c:set var="bookmarked" value="true" />
							  </c:if>
							</c:forEach>
					
							<ul class="dropdown-menu">
								<c:choose>
									<c:when test="${bookmarked}">
										<li class="remove-bookmark-button" data-user="${user.userId}" data-content="${practiceProblem.uuid}" data-action="remove">
											<a role="menuitem" tabindex="-1" href="#">Un-Bookmark Problem</a>
										</li>
									</c:when>
									<c:otherwise>
										<li class="add-bookmark-button" data-user="${user.userId}" data-content="${practiceProblem.uuid}" data-action="add">
											<a role="menuitem" tabindex="-1" href="#">Bookmark Problem</a>
										</li>
									</c:otherwise>
								</c:choose>
						
						
								<li role="presentation" class="divider"></li>
								<c:if test="${! practiceProblem.anonymous}"> 
									<li role="presentation"><a role="menuitem" tabindex="-1" href="${practiceProblem.author.profileUrl}">Go To User Profile</a></li>
									<li role="presentation" class="divider"></li>
								</c:if>
								<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
									<form action="${practiceProblem.reportUrl}" method="post">
										<input class="menuitem normal-menu-item-input" type="submit" name="inappropriateContent" value="Report Inappropriate Content">
									</form>
								</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
									<form action="${practiceProblem.reportUrl}" method="post">
										<input class="menuitem normal-menu-item-input" type="submit" name="inaccurateContent" value="Report Inaccurate Content">
									</form>
								</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
									<form action="${practiceProblem.reportUrl}" method="post">
										<input class="menuitem normal-menu-item-input" type="submit" name="irrelevantContent" value="Report Irrelevant Content">
									</form>
								</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
									<form action="${practiceProblem.reportUrl}" method="post">
										<input class="menuitem normal-menu-item-input" type="submit" name="proprietaryContent" value="Report Proprietary Content">
									</form>
								</a></li>
							</ul>
						</div>
					</div>
					<div class="box-header margin-bottom-25">
						<c:if test="${fn:length(practiceProblem.readableTags) > 0}">
							<span><b>Tagged As:</b> ${practiceProblem.readableTags}</span>
						</c:if>
					</div>
					<div class="box-body">
						<div class="box-group" id="accordion">
							<div class="box box-primary">
								<div class="box-header">
									<i class="fa fa-question fa-karma-score"><span class="karma-score karma-green">  +${practiceProblem.karma} </span></i>
									<h4 class="box-title">
										${practiceProblem.title}
									</h4>
								</div>
								<div class="box-body box-body-with-rating-system" >
									<jsp:include page="/WEB-INF/templates/rating-system-h.jsp">
										<jsp:param name="contentUuid" value="${practiceProblem.uuid}" />
										<jsp:param name="userId" value="${user.userId}" />
										<jsp:param name="userIsAuthor" value="${user.userId == practiceProblem.creatorUserId}" />
										<jsp:param name="alreadyRated" value="${practiceProblem.alreadyRatedByCurrentUser}"/>
									</jsp:include>
									<span class="preserve-line-formatting">${practiceProblem.body}</span>
								</div>
							</div>
							<div class="panel box box-primary">				
								<div class="box-header">
									<i class="fa fa-bullseye fa-karma-score"><span class="karma-score">  +${practiceProblem.karma} </span></i>
									<h4 class="box-title">
										<a data-toggle="collapse" data-parent="#accordion" href="#collapseAuthorSolution" class="collapsed">
											Author's Solution
										</a>
									</h4>
									<div class="dropdown pull-right padding-5">
										<button class="btn btn-default peek-button">Peek</button>
									</div>
								</div>
								<div id="collapseAuthorSolution" class="panel-collapse collapse">
									<div class="box-body box-body-with-rating-system">
										<span class="preserve-line-formatting">${practiceProblem.authorSolution}</span>
									</div>
								</div>
							</div>
							<c:forEach items="${practiceProblem.answers}" var="answer" varStatus="loop">
								<div class="panel box ${answer.colorClass}">
									<div class="box-header">
										<i class="fa ${answer.icon} fa-karma-score"><span class="karma-score">  +${answer.karma} </span></i>
										<h4 class="box-title">
											<a data-toggle="collapse" data-parent="#accordion" href="#collapsePracticeProblemAnswer${loop.index}" class="collapsed">
												<c:if test="${!answer.viewable}">
													<i class="fa fa-eye-slash text-red"></i>
												</c:if>
												<c:choose>
													<c:when test="${answer.anonymous}">${answer.title} <small> written on ${answer.shortReadableTime}</small></c:when>
													<c:otherwise>${answer.title} <small> by ${answer.author.username} on ${answer.shortReadableTime}</small></c:otherwise>
												</c:choose>
											</a>
										</h4>
										<div class="dropdown pull-right report-menu">
											<a class="dropdown-toggle" data-toggle="dropdown" href="#">
												Actions <span class="caret"></span>
											</a>
											<ul class="dropdown-menu">
												<c:if test="${user.userId == practiceProblem.creatorUserId}"> 
													<li role="presentation">
														<a role="menuitem" tabindex="-1" href="#">
															<c:if test="${! answer.approved}"> 
																<form method="post" action="${answer.approvedSolutionUrl}">
																	<input class="menuitem normal-menu-item-input" value="Mark Approved Solution" type="submit">
																</form>
															</c:if>
															<c:if test="${answer.approved}"> 
																<form method="post" action="${answer.notApprovedSolutionUrl}">
																	<input class="menuitem normal-menu-item-input" value="Unmark as Approved Solution" type="submit">
																</form>
															</c:if>
														</a>
													</li>
													<li role="presentation" class="divider"></li>
												</c:if>
												<c:if test="${! answer.anonymous}"> 	
													<li role="presentation"><a role="menuitem" tabindex="-1" href="${answer.author.profileUrl}">Go To User Profile</a></li>
													<li role="presentation" class="divider"></li>
												</c:if>
										
												<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
													<form action="${answer.reportUrl}" method="post">
														<input class="menuitem normal-menu-item-input" type="submit" name="inappropriateContent" value="Report Inappropriate Content">
													</form>
												</a></li>
												<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
													<form action="${answer.reportUrl}" method="post">
														<input class="menuitem normal-menu-item-input" type="submit" name="inaccurateContent" value="Report Inaccurate Content">
													</form>
												</a></li>
												<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
													<form action="${answer.reportUrl}" method="post">
														<input class="menuitem normal-menu-item-input" type="submit" name="irrelevantContent" value="Report Irrelevant Content">
													</form>
												</a></li>
											</ul>
										</div>
									</div>
									<div id="collapsePracticeProblemAnswer${loop.index}" class="panel-collapse collapse" style="height: 0px;">
										<div class="box-body box-body-with-rating-system">
											<jsp:include page="/WEB-INF/templates/rating-system-h.jsp">
												<jsp:param name="contentUuid" value="${answer.uuid}" />
												<jsp:param name="userId" value="${user.userId}" />
												<jsp:param name="userIsAuthor" value="${user.userId == answer.creatorUserId}" />
												<jsp:param name="alreadyRated" value="${answer.alreadyRatedByCurrentUser}"/>
											</jsp:include>
											<span class="preserve-line-formatting">${answer.body}</span>
										</div>
									</div>
								</div>
							</c:forEach>
							<div class="panel box box-danger">
								<div class="box-header">
									<h4 class="box-title">
										<i class="fa fa-university fa-karma-score"><span class="karma-score">  +? </span></i>
										<a data-toggle="collapse" data-parent="#accordion" href="#collapseSubmitYourAnswer" class="collapsed">
											Answer this Problem <small> Contribute your own explanation of the solution to this problem</small>
										</a>
									</h4>
								</div>
								<c:choose>
									<c:when test="${answerMode}">
										<form action="/answer/practice-problem/new" method="post" id="collapseSubmitYourAnswer" class="panel-collapse collapse" style="height: 0px;">
											<input type="hidden" name="answerModeStreak" value="${answerModeStreak}"/>
											<input type="hidden" name="parentUuid" value="${practiceProblem.uuid}"/>
											<input type="hidden" name="action" value="done"/>
											<div class="box-body">
												<label for="title">Answer/Response Title</label>
												<input type="text" name="title" class="form-control" placeholder="Insert a brief description of your response" value="RE:${practiceProblem.title}">
											</div>
											<div class="box-body pad">
												<label for="body">Answer Explanation and Response</label>
												<textarea id="body-editor" name="body" class="textarea wysihtml5-beam-me-up" placeholder="Describe how you would solve the problem, and the final result of any calculations"></textarea>
												<div class="submit-buttons-pull-right">
													<input class="btn btn-success submit" name="regularSubmit" type="submit" value="Submit + Next">
												</div>
											</div>
										</form>
									</c:when>
									<c:otherwise>
										<form action="${practiceProblem.newAnswerUploadUrl}" method="post" id="collapseSubmitYourAnswer" class="panel-collapse collapse" style="height: 0px;">
											<input type="hidden" name="parentUuid" value="${practiceProblem.uuid}">
											<div class="box-body">
												<label for="title">Answer Title</label>
												<input type="text" name="title" class="form-control" placeholder="Insert a brief description of your problem" value="RE:${practiceProblem.title}">
											</div>
											<div class="box-body pad">
												<label for="body">Answer Explanation and Result</label>
												<textarea id="body-editor" name="body" class="textarea wysihtml5-beam-me-up" placeholder="Describe how you would solve the problem, and the final result of any calculations"></textarea>
												<div class="submit-buttons-pull-right">
													<c:if test="${user != null}">
														<input class="btn btn-info submit" name="saveButton" type="submit" value="Submit Anonymously">
														<input class="btn btn-primary submit" name="saveButton" type="submit" value="Submit">
													</c:if>
													<c:if test="${user == null}">
														<div class="btn login-button btn-warning">Log In To Answer Questions</div>
													</c:if>
												</div>
											</div>
										</form>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="/WEB-INF/templates/latex-playground.jsp">
			<jsp:param name="hasSolution" value="${false}" />
		</jsp:include>
	</jsp:attribute>
	
	<jsp:attribute name="javascriptDependencies">
		<c:if test="${difficultyCalibration}">
			personalize-difficulty 
		</c:if>
		practiceProblem rating-system lpg
	</jsp:attribute>
</t:genericpage>
	