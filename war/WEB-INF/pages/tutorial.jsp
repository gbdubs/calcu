<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Tutorial
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000">
		
				<div class="tutorial-item-wrapper">
					<h2 class="welcome-banner">Welcome To CalcU</h2>
					<h3 class="welcome-banner-subtitle">Five Steps to get you started.</h3>
				</div>
				<div class="tutorial-item-wrapper tutorial-content-explanation">
					<span class="looming-number hidden-xs">1</span>
					<h2 class="tutorial-item-title">CalcU Brings together Three types of Content</h2>
					<table class="hidden-xs tutorial-content-box">
						<tr>
							<td class="bg-green">
								<h3 class="no-margin padding-20">Practice Problems</h3>
								<p>
									Some are Scraped from the web, others come from users like you. Practice Problems have an author's answer, user answers, and admin approved answers.
								</p>
							</td>
							<td class="bg-blue">
								<h3 class="no-margin padding-20">Questions + Answers</h3>
								<p>
									Whether you have a question, or want to help others answer theirs, questions are at the heart of the community of CalcU. Most are answered within a week.
								</p>
							</td>
							<td class="bg-yellow">
								<h3 class="no-margin padding-20">Explanations</h3>
								<p >
									When you find you want more basic explanations, or want to be formally taught a subject, we offer explanations from around the web and from our users.
								</p>
							</td>
						</tr>
					</table>
					<table class="visible-xs">
						<tr>
							<td class="bg-green">
								<h3 class="no-margin padding-20">Practice Problems</h3>
								<p>
									Some are Scraped from the web, others come from users like you. Practice Problems have an author's answer, user answers, and admin approved answers.
								</p>
							</td>
						</tr>
						<tr>
							<td class="bg-blue">
								<h3 class="no-margin padding-20">Questions + Answers</h3>
								<p>
									Whether you have a question, or want to help others answer theirs, questions are at the heart of the community of CalcU. Most are answered within a week.
								</p>
							</td>
						</tr>
						<tr>
							<td class="bg-yellow">
								<h3 class="no-margin padding-20">Explanations</h3>
								<p >
									When you find you want more basic explanations, or want to be formally taught a subject, we offer explanations from around the web and from our users.
								</p>
							</td>
						</tr>
					</table>
				</div>
				<div class="tutorial-item-wrapper search-by-tag-explanation">
					<span class="looming-number hidden-xs">2</span>
					<h2 class="tutorial-item-title">Search By Tag</h2>
					<div class="search-explanation">
						<p>
							We use Tagging much like it is used in social media, and in music.  All content has tags associated with it based on what it is.
							Most pieces of content have many tags, so to find the most relevant search results, enter all the tags you are interested in finding.
							To create a new tag, type it in then press enter. 
						</p>
					</div>
					<div class="search-bar-wrapper">
						<div class="input-group rounded">
							<i class="fa fa-search"></i>
							<input name="tagsInput" id="tags-input" value="derivatives,related rates" style="display: none;"></input>
							<span class="input-group-btn">
								<button class="btn btn-primary large-input-group-button search-by-tab-button">Search!</button>
							</span>
						</div>
					</div>
				</div>
				<div class="tutorial-item-wrapper">
					<span class="looming-number hidden-xs">3</span>
					<h2 class="tutorial-item-title">Personalization + Recommendation</h2>
					<div class="recommendation-explanation">
						<i class="fa fa-street-view"></i>
						<i class="fa fa-arrow-right first"></i>
						<i class="fa fa-gears"></i>
						<p>
							We use a state of the art recommendation system that bases its choices off of information that you provide us about your interests.
							In order to get the best out of the recommendation system, it is essential to complete the personalization activity every so often.
							Go to the recommendations feature to see your recommendations at any point in time, and rate the ones you like and dislike to see 
							better ones each time. 
							<c:if test="${user == null}">
								<b>But you can only record your progress when you are <a class="login-button">Logged In</a>!</b>
							</c:if>
						</p>
						<i class="fa fa-arrow-right second"></i>
						<i class="fa fa-road"></i>
					</div>
				</div>
				<div class="tutorial-item-wrapper">
					<span class="looming-number hidden-xs">4</span>
					<h2 class="tutorial-item-title">Achievements + Karma</h2>
					<div class="achievements-explanation">
						<div class="achievement-badge-wrapper left">
							<div class="achievement-badge text-red">
								<div class="hexagon outer-hexagon"></div>
								<div class="achievement-badge-insides text-blue bg-blue">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw fa-level-up fa-5x badge-icon"></i>
							</div>
						</div>
						<div class="achievement-badge-wrapper right">
							<div class="achievement-badge completed text-yellow">
								<div class="hexagon outer-hexagon"></div>
								<div class="achievement-badge-insides text-olive bg-olive">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw fa-trophy fa-5x badge-icon"></i>
							</div>
						</div>
						In order to reward people for using the site well, there are achievements hidden throughout the site, which look like hexagonal shields.
						You can check them out <a href="/achievements">here</a>. Additionally, any time you help out your fellow learners, you will receive
						karma, our way of saying thank you. You can find the specifics on how you get karma <a href="/tutorial/documentation">on our documentation page</a>.
						<c:if test="${user == null}">
							<b>But you can only record your progress when you are <a class="login-button">Logged In</a>!</b>
						</c:if>
					</div>
				</div>
				<div class="tutorial-item-wrapper">
					<span class="looming-number hidden-xs">5</span>
					<h2 class="tutorial-item-title">Tools for Learning, Contributing, and Communicating</h2>
					<div>
						<p>
							CalcU has detailed Tools for creating stunning mathematical content and discussion.
						</p>
						<p class="large-icon-bullet-point">
							<i class="fa fa-bookmark-o toggle-me"></i>
							All Content can be bookmarked with a simple click of this button, anywhere on the site.  Filled in means that it is already bookmarked.
						</p>
						<p class="large-icon-bullet-point">
							<i class="fa fa-magic"></i>
							The entire website supports LaTex, the standard method for writing mathematical expressions. Whenever you see the magic wand, you can
							use it to summon a popup which will help you create your own LaTex, while teaching you the ropes.
						</p>
						<p class="large-icon-bullet-point">
							<i class="fa fa-circle-o-notch"></i>
							Rating wheels provide us with insight as to how you view the content, and its quality and difficulty.  Regular rating of others' work lets us know what you like to see
							and gives us an idea of how we can better serve you in the future.
						</p>
						<p class="large-icon-bullet-point">
							<i class="fa fa-comment-o"></i>
							All Content can be commented on, allowing brilliant user generated discussions. The best posts float to the top, and can be marked as "approved" by the author.
						</p>
					</div>
				</div>
				<div class="tutorial-footer row full-width">
					<div class="col-xs-12 bg-white-gradient">
						<h3>Now that you know the basics, where do you want to go from here?<h3>
					</div>
					<div class="centered full-width margin-bottom-50">
					<div class="hexagon-table">
						<div class="hexagon-row centered">
							<a href="/search" class="hexagon-button">
								<div class="text-blue">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<i class="fa fa-fw fa-search fa-5x badge-icon"></i>
									<h4 class="hexagon-title">Start with a Search</h4>
								</div>
							</a>
							<br class="visible-xs">
							<br class="visible-xs">
							<c:if test="${user!=null}">
								<a href="/user/me" class="hexagon-button">
							</c:if>
							<c:if test="${user==null}">
								<a class="hexagon-button login-button">
							</c:if>
								<div class="text-yellow">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<i class="fa fa-fw fa-user fa-5x badge-icon"></i>
									<h4 class="hexagon-title">Visit your Profile</h4>
								</div>
							</a>
							<br class="visible-xs">
							<br class="visible-xs">
					
				
							<a href="/explore" class="hexagon-button">
								<div class="text-green">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<i class="fa fa-fw fa-binoculars fa-5x badge-icon"></i>
									<h4 class="hexagon-title">Explore the Site</h4>
								</div>
							</a>
						</div>
					</div>
				</div>
				<a href="/tutorial/documentation" class="full-width">
					<h3>
						Or Learn more about Karma, Levels, and Rules
						<i class="fa fa-arrow-circle-right"></i>
					</h3>
				</a>
			</div>
		</div>
	</jsp:attribute>
	
	<jsp:attribute name="javascriptDependencies">
		tutorial searchByTag
	</jsp:attribute>
</t:genericpage>