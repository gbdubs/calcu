<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Topic Selector
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000 topic-selector-page">
				<h2 class="welcome-banner margin-top-100">Topic Selector</h2>
				<h3 class="welcome-banner-subtitle">Choose a topic you're interested in, and we will find related content, practice problems, and examples for you.</h3>
				<div class="topic-explorer-explanation margin-bottom-50">
					<p>
						The topic explorer is a simple way of traversing the hierarchical nature of learning
						mathematics.  When you dive deeper into a subject you are shown some more specific options,
						and find that there are many levels of learning.  Additionally, it is a great way
						to find the kinds of questions you are looking for, even if you can't quite remember
						what the subject is called.
					</p>
					<p> 
						To Begin using the topic selector, simply press on any of the grey buttons in the "topics"
						panel.  When you do that, a new panel, with more specific options, will pop up, and your 
						selection will lead you to it.  To go back, simply click on a previous option or on the 
						currently selected option.  Once you have settled on a topic you want to examine, you can
						click on the button that appears above the topic selector, which will lead you to a page
						with all of the content on that topic.
					</p>
					<p>
						This is just one way of engaging with our database.  You can also 
						<a href="/search">Search</a> for what you are looking for, or use our 
						personalization and recommendation features, which will attempt to deduce
						what you are interested in learning.
					</p>
				</div>
				
				<div class="centered margin-bottom-25">
					<a id="ts-view-topic-button" class="hidden btn btn-success"></a>
				</div>
				<div id="topic-selector">
				
					<div id="ts-columns">
						<!-- FIRST COLUMN, PRE LOADED. --> 
						<div id="ts-col-1" class="col-lg-3 col-md-4 col-sm-4">
							<div class="box box-primary box-solid">
								<div class="box-header centered">
									<h3 class="box-title float-none">Topics</h3>
								</div>
								<div class="box-body align-left">
									<c:forEach var="topic" items="${rootTopics}">
										<c:if test="${topic.upperCaseTitle != 'None'}">
											<a id="ts-${topic.uuid}" class="btn btn-block btn-default">
												${topic.upperCaseTitle}
											</a>
										</c:if>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
			
					<div id="ts-info-box">
					
					
					</div>
			
					<div class="hidden" id="ts-data">
						${allTopicData}
					</div>					

					<div id="templates" class="hidden">
						<div id="ts-column-template" class="col-lg-3 col-md-4 col-sm-4"></div>
								
						<div id="ts-topic-box-template" class="box box-success box-solid topic-box hidden">
							<div class="box-header centered topic-selector-header">
								<h3 class="box-title float-none">[TOPIC_TITLE]</h3>
							</div>
							<div class="box-body align-left"></div>
						</div>
					
						<a id="ts-topic-box-button" class="btn btn-block btn-default"></a>
					
						<div id="ts-info-box-content-template" class="alert alert-success hidden">							
							<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="${user.userId}" data-content="[CONTENT_UUID]">
								<i class="fa fa-bookmark-o"></i>
							</button>
							<a href="/practice-problem/af335f4c-8538-4c46-87cf-62bee879c31b"><b>[CONTENT_TITLE]</b></a>
							[CONTENT_BODY]
						</div>
					</div>
				</div>
			
				
			</div>
		</div>

	</jsp:attribute>
	<jsp:attribute name="javascriptDependencies">
		topic-selector
	</jsp:attribute>
</t:genericpage>