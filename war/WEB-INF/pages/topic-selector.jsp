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
				<h3 class="margin-bottom-50 welcome-banner-subtitle">Choose a topic you're interested in, and we will find related content, practice problems, and examples for you.</h3>
				<div id="topic-selector">
				
					<div id="ts-columns">
					
						<!-- FIRST COLUMN --> 
						<div id="ts-col-1" class="col-lg-3 col-md-4 col-sm-4">
							<div class="box box-primary box-solid">
								<div class="box-header centered">
									<h3 class="box-title float-none">Topics</h3>
								</div>
								<div class="box-body align-left">
									<c:forEach var="topic" items="${rootTopics}">
										<a id="ts-${topic.uuid}" class="btn btn-block btn-default">
											${topic.title}
										</a>
									</c:forEach>
								</div>
							</div>
						</div>
				
					</div>
		
					
					<div class="hidden" id="ts-data">
						<c:forEach var="topic" items="${allTopics}">
							<div id="ts-${topic.uuid}-data">
								<div class="title">${topic.title}</div>
								<div class="sub-topics">${topic.subTopics}</div>
								<div class="short-desc">${topic.shortDescription}</div>
								<div class="long-desc">${topic.longDescription}</div>
								<div class="tags">${topic.tags}</div>
								<div class="content-size">${fn:length(topic.contentUuids)}</div>
								<c:forEach var="i" begin="${0}" end="${5}">
									<c:if test="${fn:length(topic.contentUuids) > i + 1}">
										<div class="content-${i}">${topic.contentUuids[i]}</div>
									</c:if>
								</c:forEach>
								</div>
						</c:forEach>
					</div>					
					
					
					
					
					
					<div id="templates" class="hidden">
				
						<div id="ts-column-template" class="col-lg-3 col-md-4 col-sm-4">
							
						</div>
								
						<div id="ts-topic-box-template" class="box box-success box-solid topic-box hidden">
							<div class="box-header centered topic-selector-header">
								<h3 class="box-title float-none">[TOPIC_TITLE]</h3>
							</div>
							<div class="box-body align-left">
								
							</div>
						</div>
					
						<a id="ts-topic-box-button" class="btn btn-block btn-default">
							
						</a>
						
						<div id="ts-info-box-template" class="max-width-at-1000 align-left hidden ts-info-box">
							<div class = "box box-primary box-solid">
								<div class="box-header">
									<div class="align-right">
										<h3 class="box-title"></h3>
										<div class="padding-top-4 margin-right-5">
											<input class="btn btn-danger" type="submit" value="Edit Topic">
											<input class="btn btn-success" type="submit" value="See Full Topic">
										</div>
									</div>
								</div>
								<div class="box-body">
									<h4 class="tags"></h4>
									<span class="short-description"></span>
									<div class="margin-left-100 margin-top-10 centered">
										<a class="btn btn-warning large-input-group-button margin-10" href="/topic/[TOPIC_UUID]">See <b></b> Pieces of Related Content</a>
										<a class="btn btn-success large-input-group-button margin-10" href="/topic/[TOPIC_UUID]">See <b></b> Related Practice Problems</a>
										<a class="btn btn-primary large-input-group-button margin-10" href="/topic/[TOPIC_UUID]">See <b></b> Related Questions</a>
									</div>
									<span class="long-description"></span>
								</div>
							</div>
						</div>
					
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