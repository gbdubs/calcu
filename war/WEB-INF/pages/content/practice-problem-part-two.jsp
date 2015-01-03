<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Sandbox
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="box box-solid practice-problem">
			<div class="box-header">
				<h3 class="box-title">Practice Problem <small>by <a href="${practiceProblem.author.profileUrl}">${practiceProblem.author.username}</a> on ${practiceProblem.shortReadableTime}</small></h3>
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
						<div class="box-body">
							<jsp:include page="/WEB-INF/templates/rating-system-h.jsp">
								<jsp:param name="contentId" value="${practiceProblem.uuid}" />
								<jsp:param name="formPostUrl" value="${practiceProblem.rateUrl}" />
							</jsp:include>
							${practiceProblem.body}
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
								${practiceProblem.authorSolution}
							</div>
						</div>
					</div>
					<c:forEach items="${practiceProblem.answers}" var="answer" varStatus="loop">
						<div class="panel box ${answer.colorClass}">
							<div class="box-header">
								<i class="fa ${answer.icon} fa-karma-score"><span class="karma-score">  +${answer.karma} </span></i>
								<h4 class="box-title">
									<a data-toggle="collapse" data-parent="#accordion" href="#collapsePracticeProblemAnswer${loop.index}" class="collapsed">
										${answer.title} | by ${answer.author.username}
									</a>
								</h4>
							</div>
							<div id="collapsePracticeProblemAnswer${loop.index}" class="panel-collapse collapse" style="height: 0px;">
								<div class="box-body box-body-with-rating-system">
									<jsp:include page="/WEB-INF/templates/rating-system-h.jsp">
										<jsp:param name="contentId" value="${answer.uuid}" />
										<jsp:param name="formPostUrl" value="${answer.rateUrl}" />
									</jsp:include>
									${answer.body}
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
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>	
</t:genericpage>
	