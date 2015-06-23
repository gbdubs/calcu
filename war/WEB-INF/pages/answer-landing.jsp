<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Answer Questions
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="centered">
			<h2 class="welcome-banner margin-top-100">Answering Mode</h2>
			<h3 class="welcome-banner-subtitle max-width-at-1000 margin-bottom-50">In this mode, we provide you with questions matched to your skill level. Every consecutive question you
				answer correctly will contribute to your answer streak, which will boost your karma.</h3>
			<div class="row max-width-at-1000">
				<div class="col-sm-12 max">
					<div class="box box-solid bg-blue-gradient">
						<div class="box-header solid-box-header">
							<i class="fa fa-question fa-karma-score hidden-xs"></i>
							<h3 class="box-title">Answer User Questions</h3>
						</div>
						<div class="box-body align-left margin-bottom-50">
							<p>
								In this mode, you will be answering User Questions.  Since User Questions often get fewer views than practice problems,
								they tend to yield lower levels of Karma just because of the way they are structured.  To make up for that, there is a 
								higher immediate karma return for your answer in Answer Mode, so that we can try and get Users the answers to their problems
								as soon as possible. Thanks for trying it out!
							</p>
							<div class="centered">
								<c:choose>
									<c:when test="${user != null}">
										<a href="/answer/question/new" class="btn btn-primary display-inline">Get Started</a>
									</c:when>
									<c:otherwise>
										<button class="btn btn-warning login-button display-inline">Login to Get Started</button>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
				<div class="col-sm-12">
					<div class="box box-solid bg-olive-gradient">
						<div class="box-header solid-box-header">
							<i class="fa fa-pencil fa-karma-score hidden-xs"></i>
							<h3 class="box-title">Solve Practice Problems</h3>
						</div>
						<div class="box-body align-left margin-bottom-25">
							<p>
							We have all seen that problem in that math book where it isn't quite clear where the author got their answer.
							We have all been there.  Some times, despite an Author's best efforts, they don't do a great job describing
							their solution.  Sometimes, they don't provide it in the most accessible way. We have an algorithm that detects
							frustrated students in need of a clear solution, and we are sending you their way! Thank you!
							</p>
							<div class="centered">
								<c:choose>
									<c:when test="${user != null}">
										<a href="/answer/practice-problem/new" class="btn btn-success display-inline">Get Started</a>
									</c:when>
									<c:otherwise>
										<button class="btn btn-warning login-button display-inline">Login to Get Started</button>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="max-width-at-1000">
				<div class="box-header solid-box-header">
					<h3 class="box-title">Other Ways to Get Involved</h3>
				</div>
				<div class="box-body align-left margin-bottom-50">
					Though answering other peoples questions is a great way to reinforce your own knowledge while helping others, on this site
					there are many other opportunities to do the same thing!  If you are interested in creating some practice problems of your
					own, or you have a question you aren't sure about asking, or if you want to create a text resource to help others out with a
					difficult subject, you can head on over to your <a href="/contribute/dashboard">Contribute Dashboard</a> to see how you can
					get involved.
				</div>
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>