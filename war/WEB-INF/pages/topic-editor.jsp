<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Topic Editor
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000">
		
				<form action="/topic-editor" method="POST">
					<input type="hidden" name="uuid" value="${topic.uuid}"/>
					
					<input class="topic-editor-title centered margin-top-50" type="text" name="title" value="${topic.title}">
		
					<h3 class="welcome-banner-subtitle">
						On this page you can submit new tags, 
						content, parent topics, and sub topics, 
						as well as view current content data.
					</h3>
				
					<h3 class="margin-top-25">UUID = ${topic.uuid}</h3>
				
					</br>
					
					<div class="box box-solid">
						<div class="box-header bg-olive">
							<h3 class="box-title">Difficulty</h3>
						</div>
						<div class="box-body">
							<input type="number" name="difficulty" value="${topic.difficulty}"/>
						</div>
					</div>
					
					<div class="box box-solid">
						<div class="box-header bg-olive">
							<h3 class="box-title">Tags</h3>
						</div>
						<div class="box-body">
							<textarea class="topic-editor-textarea" rows="4" cols="500" name="tags">${topic.tags}</textarea>
						</div>
					</div>
		
					<div class="box box-success box-solid margin-top-75">
						<div class="box-header">
							<h3 class="box-title"> Current Content Data </h3>
						</div>
						<div class="box-body no-padding">
							<table class="table">
								<tbody>
									<tr>
										<td>#</td>
										<td>Link</td>
									</tr>
									<c:forEach var="contentUuid" items="${topic.contentUuids}" varStatus="index">
										<tr>
											<td>${index.count}.</td>
											<td><a href="/content/${contentUuid}">${contentUuid}</a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<!--
						<div class="box box-solid">
							<div class="box-header bg-olive">
								<h3 class="box-title">Add Or Modify Content</h3>
							</div>
							<div class="box-body">
								<textarea class="topic-editor-textarea" rows="4" cols="500">${topic.contentUuids}</textarea>
							</div>
						</div>
					-->
					<div class="box box-solid">
						<div class="box-header bg-green">
							<h3 class="box-title">Parent Topics</h3>
						</div>
						<div class="box-body">
							<textarea class="topic-editor-textarea" rows="4" cols="500" name="parentTopics">${topic.parentTopics}</textarea>
						</div>
					</div>
		
					<div class="box box-solid">
						<div class="box-header bg-olive">
							<h3 class="box-title">Sub Topics</h3>
						</div>
						<div class="box-body">
							<textarea class="topic-editor-textarea" rows="4" cols="500" name="subTopics">${topic.subTopics}</textarea>
						</div>
					</div>
					
					<div class="box box-solid">
						<div class="box-header bg-green">
							<h3 class="box-title">Short Description</h3>
						</div>
						<div class="box-body">
							<textarea class="topic-editor-textarea" rows="4" cols="500" name="shortDescription">${topic.shortDescription}</textarea>
						</div>
					</div>
		
					<div class="box box-solid">
						<div class="box-header bg-olive">
							<h3 class="box-title">Long Description</h3>
						</div>
						<div class="box-body">
							<textarea class="topic-editor-textarea" rows="4" cols="500" name="longDescription">${topic.longDescription}</textarea>
						</div>
					</div>
		
					<div class="submit-buttons-pull-right">
						<input class="btn btn-primary" name="action" type="submit" value="Save">
						<a class="btn btn-warning" href="/topic/${topic.uuid}">Cancel</a>
						<input class="btn btn-danger" name="action" type="submit" value="Delete">
					</div>
				</form>
				<form action="/topic-editor/merge" method="POST">
					<input type="hidden" name="action" value="merge"/>
					<input type="hidden" name="sourceTopic" value="${topic.uuid}"/>
					<input type="text" name="targetTopic" value="To Merge With"/>
					<input type="submit" class="btn btn-success" value="Merge Into Topic"/>
				</form>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascriptDependencies">
		tutorial searchByTag
	</jsp:attribute>
</t:genericpage>
	