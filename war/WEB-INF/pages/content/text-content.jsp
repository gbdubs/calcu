<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Text Content
	</jsp:attribute>
	<jsp:attribute name="content">
		<c:if test="${!livePreview && !textContent.viewable}">
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
					<a href="${textContent.editUrl}" class="btn btn-warning pull-right live-preview-return-button">Back To Editor</a>
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
		<c:if test="${difficultyCalibration}">
			<div class="box box-success box-solid">
				<div class="box-header">
					<i class="fa fa-refresh"></i>
					<h3 class="box-title">Difficulty Calibration: Step (${stepNumber}/15)</h3>
				</div>
				<div class="box-body">
					<p>
						How difficult is it to understand this explanation?
					</p>
					<ul>
						<li>"Not Yet" -- I do not have the building blocks that would allow me to understand this content, in any amount of time. </li>
						<li>"Very Difficult" -- The math in this content is significantly more difficult than I have been working with.</li>
						<li>"Difficult" -- I have read and tried to explain passages of this difficulty, with mixed success.</li>
						<li>"Moderate" -- This content is approximately as difficult as what I am looking for.</li>
						<li>"Easy" -- I understand this content without much effort.</li>
						<li>"Very Easy" -- I would be able to write an explanation that was this explanatory.</li>
					</ul>	
					<div class="btn-group difficulty-rating-buttons" data-uuid="${textContent.uuid}" data-user="${user.userId}">
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
		<div class="box box-solid practice-problem">
			<div class="box-header">
				<c:if test="${!textContent.anonymous}">
					<h3 class="box-title">Explanation <small>by <a href="${textContent.author.profileUrl}">${textContent.author.username}</a> on ${textContent.shortReadableTime}</small></h3>
				</c:if>
				<c:if test="${textContent.anonymous}">
					<h3 class="box-title">Explanation <small>by submitted on ${textContent.shortReadableTime}</small></h3>
				</c:if>
				<div class="dropdown pull-right report-menu primary-report-menu">
					<a class="dropdown-toggle btn btn-default" data-toggle="dropdown" href="#">
						Actions <span class="caret"></span>
					</a>
					<c:set var="bookmarked" value="false" />
					<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
					  <c:if test="${bookmarkUuid eq textContent.uuid}">
						<c:set var="bookmarked" value="true" />
					  </c:if>
					</c:forEach>
					
					<ul class="dropdown-menu">
						<c:choose>
							<c:when test="${bookmarked}">
								<li class="remove-bookmark-button" data-user="${user.userId}" data-content="${textContent.uuid}" data-action="remove">
									<a role="menuitem" tabindex="-1" href="#">Un-Bookmark Explanation</a>
								</li>
							</c:when>
							<c:otherwise>
								<li class="add-bookmark-button" data-user="${user.userId}" data-content="${textContent.uuid}" data-action="add">
									<a role="menuitem" tabindex="-1" href="#">Bookmark Explanation</a>
								</li>
							</c:otherwise>
						</c:choose>
						
						
						<li role="presentation" class="divider"></li>
						<c:if test="${! textContent.anonymous}"> 
							<li role="presentation"><a role="menuitem" tabindex="-1" href="${textContent.author.profileUrl}">Go To User Profile</a></li>
							<li role="presentation" class="divider"></li>
						</c:if>
						<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
							<form action="${textContent.reportUrl}" method="post">
								<input class="menuitem normal-menu-item-input" type="submit" name="inappropriateContent" value="Report Inappropriate Content">
							</form>
						</a></li>
						<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
							<form action="${textContent.reportUrl}" method="post">
								<input class="menuitem normal-menu-item-input" type="submit" name="inaccurateContent" value="Report Inaccurate Content">
							</form>
						</a></li>
						<li role="presentation"><a role="menuitem" tabindex="-1" href="#">
							<form action="${textContent.reportUrl}" method="post">
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
				<c:if test="${fn:length(textContent.readableTags) > 0}">
					<span class="indented-header"><b>Tagged As:</b> ${textContent.readableTags}</span>
				</c:if>
			</div>
			<div class="box-body">
				<div class="box-group" id="accordion">
					<div class="box box-primary">
						<div class="box-header">
							<i class="fa fa-newspaper-o fa-karma-score"><span class="karma-score karma-green">  +${textContent.karma} </span></i>
							<h4 class="box-title">
								${textContent.title}
							</h4>
						</div>
						<div class="box-body box-body-with-rating-system" >
							<jsp:include page="/WEB-INF/templates/rating-system-h.jsp">
								<jsp:param name="contentUuid" value="${textContent.uuid}" />
								<jsp:param name="userId" value="${user.userId}" />
								<jsp:param name="userIsAuthor" value="${user.userId == textContent.creatorUserId}" />
								<jsp:param name="alreadyRated" value="${textContent.alreadyRatedByCurrentUser}"/>
							</jsp:include>
							<span class="preserve-line-formatting">${textContent.body}</span>
						</div>
					</div>
					<c:forEach items="${textContent.answers}" var="answer" varStatus="loop">
						<div class="panel box ${answer.colorClass}">
							<div class="box-header">
								<i class="fa ${answer.icon} fa-karma-score"><span class="karma-score">  +${answer.karma} </span></i>
								<h4 class="box-title">
									<a data-toggle="collapse" data-parent="#accordion" href="#collapseTextContentAnswer${loop.index}" class="collapsed">
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
										<c:if test="${user.userId == textContent.creatorUserId}"> 
											<li role="presentation">
												<a role="menuitem" tabindex="-1" href="#">
													<c:if test="${! answer.approved}"> 
														<form method="post" action="${answer.approvedSolutionUrl}">
															<input class="menuitem normal-menu-item-input" value="Mark Approved Explanation" type="submit">
														</form>
													</c:if>
													<c:if test="${answer.approved}"> 
														<form method="post" action="${answer.notApprovedSolutionUrl}">
															<input class="menuitem normal-menu-item-input" value="Unmark as Approved Explanation" type="submit">
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
							<div id="collapseTextContentAnswer${loop.index}" class="panel-collapse collapse" style="height: 0px;">
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
									Comment on this Explanation <small> Contribute your own explanation of this idea, or provide an alternative perspective</small>
								</a>
							</h4>
						</div>
						<c:choose>
							<c:when test="${answerMode}">
								<form action="/answer/text-content/new" method="post" id="collapseSubmitYourAnswer" class="panel-collapse collapse" style="height: 0px;">
									<input type="hidden" name="answerModeStreak" value="${answerModeStreak}"/>
									<input type="hidden" name="parentUuid" value="${textContent.uuid}"/>
									<input type="hidden" name="action" value="done"/>
									<div class="box-body">
										<label for="title">Answer/Response Title</label>
										<input type="text" name="title" class="form-control" placeholder="Insert a brief description of your response" value="RE:${textContent.title}">
									</div>
									<div class="box-body pad">
										<label for="body">Answer Explanation and Response</label>
										<textarea name="body" class="textarea wysihtml5-beam-me-up" placeholder="Describe how you would solve the problem, and the final result of any calculations"></textarea>
										<div class="submit-buttons-pull-right">
											<input class="btn btn-success submit" name="regularSubmit" type="submit" value="Submit + Next">
										</div>
									</div>
								</form>
							</c:when>
							<c:otherwise>
								<form action="${textContent.newAnswerUploadUrl}" method="post" id="collapseSubmitYourAnswer" class="panel-collapse collapse" style="height: 0px;">
									<input type="hidden" name="parentUuid" value="${textContent.uuid}">
									<div class="box-body">
										<label for="title">Answer/Response Title</label>
										<input type="text" name="title" class="form-control" placeholder="Insert a brief description of your response" value="RE:${textContent.title}">
									</div>
									<div class="box-body pad">
										<label for="body">Answer Explanation and Response</label>
										<textarea name="body" class="textarea wysihtml5-beam-me-up" placeholder="Describe how you would solve the problem, and the final result of any calculations"></textarea>
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
		<c:if test="${difficultyCalibration}">
			<script src="/_static/js/CalcU/personalize-difficulty.js"></script>
		</c:if>
		<script src="/_static/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
		<script src="/_static/js/CalcU/rating-system.js"></script>
		<script src="/_static/js/CalcU/question.js"></script>
	</jsp:attribute>
</t:genericpage>
	