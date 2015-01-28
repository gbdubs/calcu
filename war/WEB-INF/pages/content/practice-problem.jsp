<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Practice Problem
	</jsp:attribute>
	<jsp:attribute name="content">
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
			<div class="box box-solid bg-olive-gradient">
				<div class="box-header solid-box-header">
					<i class="fa fa-cube fa-karma-score hidden-xs"></i>
					<h3 class="box-title">Solve Practice Problem Mode (STREAK = ${answerModeStreak})</h3>
					<form action="/answer/practice-problem/new" method="post" class="pull-right box-tools">
						<input type="hidden" name="answerModeStreak" value="${answerModeStreak}"/>
						<input type="hidden" name="parentUuid" value="${practiceProblem.uuid}"/>
						<input type="hidden" name="action" value="skip"/>
						<input type="hidden" name="contentType" value="practice-problem"/>
						<input type="submit" class="btn btn-success" value="Skip" />
					</form>
				</div>
				<div class="box-body">
					You are currently in Problem Solving Turbo Mode. That name isn't real, but here is the deal!
					We will present you with problems we think that you would be the perfect person to solve.
					For most of them, the author's solution hasn't been satisfactory to most viewers.
					If you can solve the problem, please do! You will get a tidy karma bonus for answering the problems we select for you. After you solve the problem, hit the 'Done' button.
					If you don't think that you can answer the question well, or if you think that the answers are already good enough, press the 'Skip' button. Pressing 'Skip' will not change your Answer Streak.
					At any time, you can navigate away from this page, but know that your Answer Streak will be broken! Good Luck, and thank you for your contribution to our project!
				</div>
			</div>
		</c:if>
		<div class="box box-solid practice-problem">
			<div class="box-header">
				<h3 class="box-title">Practice Problem <small>by <a href="${practiceProblem.author.profileUrl}">${practiceProblem.author.username}</a> on ${practiceProblem.shortReadableTime}</small></h3>
				<div class="dropdown pull-right report-menu primary-report-menu">
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
			<div class="box-header">
				<c:if test="${fn:length(practiceProblem.readableTags) > 0}">
					<span class="indented-header"><b>Tagged As:</b> ${practiceProblem.readableTags}</span>
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
								<jsp:param name="contentId" value="${practiceProblem.uuid}" />
								<jsp:param name="formPostUrl" value="${practiceProblem.rateUrl}" />
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
										<jsp:param name="contentId" value="${answer.uuid}" />
										<jsp:param name="formPostUrl" value="${answer.rateUrl}" />
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
										<textarea  name="body" class="textarea no-horizontal-resize" placeholder="Respond to the question asked, explaining, describing, and recommending as nesscessary. Make sure to include calculations, examples, and equations when relevant." style="width: 100%; height: 200px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;"></textarea>
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
										<textarea  name="body" class="textarea no-horizontal-resize" placeholder="Describe how you would solve the problem, and the final result of any calculations" style="width: 100%; height: 200px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;"></textarea>
										<div class="submit-buttons-pull-right">
											<input class="btn btn-info submit" name="anonymousSubmit" type="submit" value="Submit Anonymously">
											<input class="btn btn-primary submit" name="regularSubmit" type="submit" value="Submit">
										</div>
									</div>
								</form>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/practiceProblem.js"></script>
	</jsp:attribute>
</t:genericpage>
	