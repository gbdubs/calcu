<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Tutorial
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-solid">
			<div class="box-header bg-blue">
				<i class="fa fa-bank"></i>
				<h3 class="box-title">Basics on How To Use This Site</h3>
			</div>
			<div class="box-body">
				<p>
					Welcome to the Tutorials Page, here you can find information about Karma, Levels, and Achievements, in addition to
					a brief overview of the site, how it works, and how to best use it. This section focuses on the major features of
					CalcU, and how you can best work with the system.
				</p>
				<p>
					The site is organized around the idea of calculus Content, both user submitted, and scraped from the web. That means
					that most things you will be doing on the site will be in relation to a piece of content that someone else created,
					or that you created. You can create content, you can comment on content, you can answer content, you can rate content,
					and the majority of the site is trying to create a new way for you to experience or interact with, content.
				</p>
				<p>	
					The three types of content that officially supported are <b>Text Content</b>, a free-form way for authors to give an explanation
					of a concept, <b>Question</b>, a way for a user to ask a specific question to the world. We try to provide incentives to 
					people to answer your question well, and when they do, we reward them with extra Karma. Finally <b>Practice Problems</b> where
					an author provides both a question and a solution.  
				</p>
				
				<p>
					The major features of this site are:
					<ul class="margin-10-list">
						<li> <i class="fa fa-search fa-fw"></i> <b>Searching for Content</b> -- You give us a couple of words describing what you are looking for, and we get you content that matches your search.</li>
						<li> <i class="fa fa-binoculars fa-fw"></i> <b>Exploring Content</b> -- WE present you with some of the newest, best, and quickly rising posts from the last few days.</li>
						<li> <i class="fa fa-bookmark fa-fw"></i> <b>Bookmarking Content</b> -- Throughout the site, you will see a little <i class="fa fa-bookmark-o"></i> icon, which tells you you can bookmark the content to come back to later. If you see it filled in, you already have it bookmarked!</li>
						<li> <i class="fa fa-comment fa-fw"></i> <b>Comment/Answer Content</b> -- Create a discussion, answer a question, provide an alternative explanation. Commenting and Answering is meant to provide you with a way to enlarge the quality and scope of conversation in an interactive environment.</li>
						<li> <i class="fa fa-tasks fa-fw"></i> <b>Recommended Content</b> -- Once we get to know you a bit better, we will recommend you problems, questions, and texts that we think can help you in your goals. </li>
						<li> <i class="fa fa-street-view fa-fw"></i> <b>Personalization</b> -- We want to get you the best recommendations possible, but we can only do that if we know enough about you! Check out the Personalize feature to let us get to know you better!</li>
						<li> <i class="fa fa-trophy fa-fw"></i> <b>Achievements</b> -- Learning is tough, so we think it is important to reward you for taking the time to do it well. Check out the <a href="#achievements-explanation">Achievements</a> section for more information. </li>
						<li> <i class="fa fa-line-chart fa-fw"></i> <b>Karma + Levels</b> -- Whenever you contribute to the CalcU community, we try to reward you with some Karma. Check out the <a href="#karma-explanation">Karma</a> section for more information. </li>
						<li> <i class="fa fa-graduation-cap fa-fw"></i> <b>Contributing Content</b> -- In addition, CalcU relies on people contributing content often. Whether it is a great question that you saw in class, or an issue
							that you don't fully understand, or an explanation you found helpful, you can contribute to our ever growing database of content. 
						</li>
						<li> <i class="fa fa-lightbulb-o fa-fw"></i> <b>Answering Questions</b> -- If answering questions is more your forte, we will present you with questions which are in desperate need of answers, and you can provide help to someone struggling to find answers to their question. </li>
					</ul>
					Finally, this site is still very much a work in progress. Please <a href="/contact">contact</a> me with any questions, comments, concerns or suggestions. I want to make this the best
					experience it can possibly be, and feedback from the people who use the site is *PUN* integral *PUN* to the success of this project.
				</p>
				<c:if test="${user == null}">
					<p>
						Though some basic features of CalcU (search, etc) are available without logging in, our experience rests heavily on our attempts
						to understand the way that you think. We would recommend that you try to be logged in whenever you use CalcU, and you don't need
						to create an account or password or anything, just sign in with any google account!
					</p>
					<button class="btn btn-block btn-success login-button">Log In</button>
				</c:if>
			</div>
		</div>
		
		<div class="box box-solid box-success" id="karma-explanation">
			<div class="box-header">
				<i class="fa fa-trophy"></i>
				<h3 class="box-title">Karma Overview/Rules</h3>
			</div>
			<div class="box-body">
				<p>
					Karma is a simple way to reward you for using the site and for helping other people.
					When you interact with the site in positive ways, you get karma. When people use the 
					site in non-positive ways, they lose karma. There are a couple of ways you can earn karma.
				</p>
				<ul>
					<li>Rating other user's work gets you <b>1</b> Karma, regardless of what the rating is.</li>
					<li>Contributing a problem, question, or other content gets you <b>5</b> Karma immediately.</li>
					<li>PLUS, When other users rate your content, you will get <b>between -2 and 4</b> Karma, depending on the quality of their rating.</li>
					<li>Answering a question gives you <b>3</b> Karma immediately, PLUS between <b>-2 and 4</b> Karma per other user's Rating.</li>
					<li>Answering questions in Turbo Mode gets you an additional karma boost of <b>between 1 and 10</b> Karma immediately.
					<li>If your answer becomes an "approved answer" (marked by an admin or the author), you get an additional <b>10</b> Karma immediately.</li>
				</ul>
				<c:if test="${user == null}">
					<p> 
						<b>BUT!</b> we can only give you Karma when you are logged in! 
					</p>
					<button class="btn btn-block btn-success login-button">Log In!</button>
				</c:if>
			</div>
		</div>
		
	</jsp:attribute>
</t:genericpage>