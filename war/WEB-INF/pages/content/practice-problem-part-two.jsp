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
								<a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" class="collapsed">
									Author's Solution
								</a>
							</h4>
						</div>
						<div id="collapseOne" class="panel-collapse collapse in">
							<div class="box-body box-body-with-rating-system">
								${practiceProblem.authorSolution}
							</div>
						</div>
					</div>
					<c:forEach items="${practiceProblem.answers}" var="answer" varStatus="loop">
						<div class="panel box ${answer.class}">
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
									${answer.body}
								</div>
							</div>
						</div>
					</c:forEach>
					<!--<div class="panel box box-warning">
						<div class="box-header">
							<h4 class="box-title">
								<i class="fa fa-lightbulb-o fa-karma-score"><span class="karma-score">  +22 </span></i>
								<a data-toggle="collapse" data-parent="#accordion" href="#collapseThree" class="collapsed">
									Other Solution
								</a>
							</h4>
						</div>
						<div id="collapseThree" class="panel-collapse collapse" style="height: 0px;">
							<div class="box-body box-body-with-rating-system">
								Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
							</div>
						</div>
					</div>-->
					<form class="panel box box-danger" action="${practiceProblem.newAnswerUploadUrl}" method="post">
						<div class="box-header">
							<h4 class="box-title">
								<i class="fa fa-university fa-karma-score"><span class="karma-score">  +? </span></i>
								<a data-toggle="collapse" data-parent="#accordion" href="#collapseFour" class="collapsed">
									Answer this Problem <small> Contribute your own explanation of the solution to this problem</small>
								</a>
							</h4>
						</div>
						<form action="${practiceProblem.newAnswerUploadUrl}" method="post" id="collapseFour" class="panel-collapse collapse" style="height: 0px;">
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
					</form>
				</div>
			</div>
		</div>
	</jsp:attribute>	
</t:genericpage>
	