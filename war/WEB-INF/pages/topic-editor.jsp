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
		
				<form action="action_page.php">
					<input class="topic-editor-title centered margin-top-50" type="text" name="title" value="Title">
				</form>
				
				<h3 class="welcome-banner-subtitle">
				On this page you can submit new tags, 
				content, parent topics, and sub topics, 
				as well as view current content data.
				</h3>
				<h3 class="margin-top-25">UUID = 112</h3>
				
		
				</br>
				<div class="box box-solid">
					<div class="box-header bg-olive">
						<h3 class="box-title">Tags</h3>
					</div>
					<div class="box-body">
						<textarea class="topic-editor-textarea" rows="4" cols="500">Write your tags here, separated by a comma.</textarea>
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
									<td>Title</td>
									<td>Responses</td>
									<td>Topics</td>
								</tr>
						
								<tr>
									<td> 1. </td>
									<td> Data</td>
									<td> Data</td>
									<td> Data </td>
								</tr>
							
						
								<tr>
									<td> 2. </td>
									<td> Data </td>
									<td> Data </td>
									<td> Data </td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
		
				<div class="box box-solid">
					<div class="box-header bg-olive">
						<h3 class="box-title">Add New Content</h3>
					</div>
					<div class="box-body">
						<textarea class="topic-editor-textarea" rows="4" cols="500">Write your content here.</textarea>
					</div>
				</div>
		
				<div class="box box-solid">
					<div class="box-header bg-green">
						<h3 class="box-title">Parent Topics</h3>
					</div>
					<div class="box-body">
						<textarea class="topic-editor-textarea" rows="4" cols="500"></textarea>
					</div>
				</div>
		
				<div class="box box-solid">
					<div class="box-header bg-olive">
						<h3 class="box-title">Sub Topics</h3>
					</div>
					<div class="box-body">
						<textarea class="topic-editor-textarea" rows="4" cols="500"></textarea>
					</div>
				</div>
		
				<div class="submit-buttons-pull-right">
					<input class="btn btn-primary" name="Submit" type="button" value="Submit">
					<input class="btn btn-warning" name="Cancel" type="button" value="Cancel">
					<input class="btn btn-danger" name="Delete" type="button" value="Delete">
				</div>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascriptDependencies">
		tutorial searchByTag
	</jsp:attribute>
</t:genericpage>
	