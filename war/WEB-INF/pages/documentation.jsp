<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Documentation
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered">
			<h2 class="welcome-banner margin-top-100 margin-bottom-50">Documentation</h2>
			<div class="box box-solid max-width-at-1000 align-left">
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
						<button class="btn btn-block btn-primary login-button">Log In</button>
					</c:if>
				</div>
			</div>
		
			<div class="box box-solid box-success max-width-at-1000 align-left" id="karma-explanation">
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
		
			<div class="box box-solid max-width-at-1000 align-left" id="level-explanation">
				<div class="box-header bg-purple">
					<i class="fa fa-level-up"></i>
					<h3 class="box-title">Leveling Up!</h3>
				</div>
				<div class="box-body">
					<p>
						One thing that higher Karma gets you is higher levels! When you reach certain levels of karma, you will be sent a notification
						that informs you that you are a higher level.  Other users can see your level, and it is a good indication of how long someone
						has been using the site. The first few levels are given below. Can you figure out the formula for them?
					</p>
					<ul>
						<li> 0 Karma --> Level 1</li>
						<li> 10 Karma --> Level 2</li>
						<li> 30 Karma --> Level 3</li>
						<li> 60 Karma --> Level 4</li>
						<li> 100 Karma --> Level 5</li>
						<li> How many Karma do you need for Level 100?</li>
					</ul>
					<p>
						Certain levels come with... powers. We will leave it at that, but just know that the more you contribute, the more we want to thank you.
						You will receive notifications about what these are when you get them.
					</p>
					<c:if test="${user == null}">
						<p> 
							<b>BUT!</b> we can only give you Karma (and subsequently level up) when you are logged in! 
						</p>
						<button class="btn btn-block bg-purple login-button">Log In!</button>
					</c:if>
				</div>
			</div>
		
			<div class="box box-solid max-width-at-1000 align-left" id="level-explanation">
				<div class="box-header bg-yellow">
					<i class="fa fa-trophy"></i>
					<h3 class="box-title">Achievements</h3>
				</div>
				<div class="box-body">
					<p>
						Additionally, we have hidden some achievements across the site, which vary from the serious to the absurd. You can view all achievements,
						and what they require for completion, on the Achievements Page. When you receive an achievement, you will be notified in the notifications
						menu. The Achievements that you have completed will be bolded, shown glowing like a beacon, while those that you haven't yet accomplished
						will be incomplete and dim.
					</p>
					<p>
						Some basic achievements include: 
						<ul>
							<li> Create five pieces of Content</li>
							<li> Answer ten questions</li>
							<li> Answer a question that gets more karma than the question itself did</li>
							<li> Be VERY interested in a word or phrase</li>
							<li> Reach a level greater than the length of your username</li>
							<li> Complete the personalization processes five times</li>
							<li> Have a post which gets more Karma than it is long </li>
							<li> Create three pieces of content which contain LaTex formatting</li>
							<li> Create three pieces of content which use HTML formatting </li>
							<li> Reach a streak of 5, 10, 20, or 42 in either Answer Mode</li>
						</ul>
					</p>
					<p>
						Note that you might not see your achievement immediately after you get it. Some of the achievements are difficult to test for, so we only
						test for them every few hours. Don't fret if you don't see one that you think you should have gotten immediately, but do shoot us an email
						if you haven't received it after 24 hours.
					</p>
					<c:if test="${user == null}">
						<p> 
							<b>BUT!</b> None of your progress will be recorded unless you are logged in! 
						</p>
						<button class="btn btn-block bg-yellow login-button">Log In!</button>
					</c:if>
				</div>
			</div>
		</div>
		
	</jsp:attribute>
</t:genericpage>