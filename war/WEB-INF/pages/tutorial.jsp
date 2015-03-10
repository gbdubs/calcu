<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Tutorial
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="centered">
		
		<div class="tutorial-item-wrapper">
			<h2 class="welcome-banner">Welcome To CalcU</h2>
			<h3>Six Steps to get you started.</h3>
		</div>
		<div class="tutorial-item-wrapper">
			<span class="looming-number hidden-xs">1</span>
			<h2 class="tutorial-item-title">CalcU Brings together Three types of Content</h2>
			<div class="row">
				<div class="col-xs-12 col-sm-4 bg-green-gradient">
					<h4 class="no-margin padding-20">Practice Problems</h4>
					<p>
						Some are Scraped from the web, others come from users like you. Practice Problems have an author's answer, user answers, and admin approved answers.
					</p>
				</div>
				<div class="col-xs-12 col-sm-4 bg-blue-gradient">
					<h4 class="no-margin padding-20">Questions and Answers</h4>
					<p>
						Whether you have a question, or want to help others answer theirs, questions are at the heart of the community of CalcU. Most are answered within a week.
					</p>
				</div>
				<div class="col-xs-12 col-sm-4 bg-yellow-gradient">
					<h4 class="no-margin padding-20">Explanations / Lectures</h4>
					<p >
						When you find you want more basic explanations, or want to be formally taught a subject, we offer explanations from around the web and from our users.
					</p>
				</div>
			</div>
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
					<input name="tagsInput" id="tags-input" value="derivative,related rates" style="display: none;"><div id="tags-input_tagsinput" class="tagsinput" style="width: 100%; height: auto;"><div id="tags-input_addTag"><input id="tags-input_tag" value="" data-default="Type Tags Here" style="color: rgb(102, 102, 102); width: 143px;"></div><div class="tags_clear"></div></div>
					<span class="input-group-btn">
						<input class="btn btn-primary large-input-group-button" type="submit" value="Search!">
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
				</p>
				<i class="fa fa-arrow-right second"></i>
				<i class="fa fa-road"></i>
			</div>
		</div>
	</jsp:attribute>
	
	<jsp:attribute name="javascriptDependencies">
		tutorial
	</jsp:attribute>
</t:genericpage>