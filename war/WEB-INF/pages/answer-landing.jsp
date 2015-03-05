<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Answer Questions
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="box box-solid">
			<div class="box-header solid-box-header">
				<i class="fa fa-cube fa-karma-score hidden-xs"></i>
				<h3 class="box-title">Welcome to Question Answering Mode(s)!</h3>
			</div>
			<div class="box-body">
				These two modes provide you with the ability to help as many people as you can, in as little time as possible!
				Here is how it works.  We pick a problem/question that we think you are the right person to provide an answer to.
				If you think it is a good fit for you, you create a beautiful answer, and then submit it.  Once you submit, you
				will be ushered on to the next piece of content. We keep track of how many you answer in a given session (we call
				it your Answer Streak), and we reward you with more automatic karma the higher your streak gets! Your posts still
				get karma from the votes of your peers, but they also get an immediate boost because you are answering the questions
				we really need answered.  Thank you for trying out this feature, and if you have any comments or concerns, please
				contact us with them!
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="box box-solid bg-blue-gradient">
					<div class="box-header solid-box-header">
						<i class="fa fa-question fa-karma-score hidden-xs"></i>
						<h3 class="box-title">Answer User Questions</h3>
					</div>
					<div class="box-body">
						<p>
						In this mode, you will be answering User Questions.  Since User Questions often get fewer views than practice problems,
						they tend to yield lower levels of Karma just because of the way they are structured.  To make up for that, there is a 
						higher immediate karma return for your answer in Answer Mode, so that we can try and get Users the answers to their problems
						as soon as possible. Thanks for trying it out!
						</p>
						<c:choose>
							<c:when test="${user != null}">
								<a href="/answer/question/new" class="btn btn-block btn-primary">Get Started</a>
							</c:when>
							<c:otherwise>
								<button class="btn btn-block btn-primary login-button">Login to Get Started</button>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="box box-solid bg-olive-gradient">
					<div class="box-header solid-box-header">
						<i class="fa fa-pencil fa-karma-score hidden-xs"></i>
						<h3 class="box-title">Solve Practice Problems</h3>
					</div>
					<div class="box-body">
					    <p>
						We have all seen that problem in that math book where it isn't quite clear where the author got their answer.
						We have all been there.  Some times, despite an Author's best efforts, they don't do a great job describing
						their solution.  Sometimes, they don't provide it in the most accessible way. We have an algorithm that detects
						frustrated students in need of a clear solution, and we are sending you their way! Thank you!
						</p>
						<c:choose>
							<c:when test="${user != null}">
								<a href="/answer/practice-problem/new" class="btn btn-block btn-success">Get Started</a>
							</c:when>
							<c:otherwise>
								<button class="btn btn-block btn-success login-button">Login to Get Started</button>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
		<div class="box box-solid box-purple-gradient">
			<div class="box-header solid-box-header">
				<i class="fa fa-cubes fa-karma-score hidden-xs"></i>
				<h3 class="box-title">Other Ways to Get Involved</h3>
			</div>
			<div class="box-body">
				Though answering other peoples questions is a great way to reinforce your own knowledge while helping others, on this site
				there are many other opportunities to do the same thing!  If you are interested in creating some practice problems of your
				own, or you have a question you aren't sure about asking, or if you want to create a text resource to help others out with a
				difficult subject, you can head on over to your <a href="/contribute/dashboard">Contribute Dashboard</a> to see how you can
				get involved.
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>