<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Contribute Text Content
	</jsp:attribute>

	<jsp:attribute name="content">
	
		<div class="box box-info contribute-box">
			<div class="box-header">
				<i class="fa fa-newspaper-o hidden-xs"></i>
				<h3 class="box-title">Welcome to the Text Content Editor! <small>Here are some basic guidelines to keep in mind</small></h3>
				<div class="pull-right box-tools hidden-xs">
					<button class="btn btn-info btn-xs" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				Thank you for taking the time to contribute original content to the project!  There are a few things for you to know. 
				<ul>
					<li>
						You can intermittently save your work by using the save button at the bottom of the page. This does not submit your work, 
						and it will remain private until you submit. Once you submit, you will not be able to edit your work further. 
					</li>
					<li>
						You can insert beautifully typeset mathematical expressions easily, using LaTex syntax, which will be automatically extracted
						and rendered to look amazing.  To find out how to write LaTex expressions, and the wrapping syntax that we have made availible,
						check out the <a href="/contribute/latex-introduction">LaTex Introduction</a>.
					</li>
					<li>
						A limited set of HTML tags are available to you to create simple stylistic things like bold text, italics, and ordered/unordered
						lists. To check out the full set of the tags we allow, or learn how to use them, check out our
						<a href="/contribute/html-introduction">Introduction to HTML formatting</a>.
					</li>
					<li>
						Please only submit content that you have the rights to.  For example, taking explanations word for word out of a textbook 
						may violate copyright law, and we wouldn't want that.  
					</li>
					<li>
						You have the option of submitting your content anonymously, or submitting with your username attached to it.  
						Be careful! You will not receive karma for anonymous submissions.  
					</li>
				</ul>
				Thank you so much for your work and your contribution to the project!
			</div>
		</div>
	
		<form action="/contribute/text-content/" method="post">
			<input type="hidden" value="${textContent.uuid}" name="uuid">
			<div class="box box-success contribute-box">
				<div class="box-header">
					<i class="fa fa-edit hidden-xs"></i>
					<h3 class="box-title">Content Editor <small>Try to provide a narrow explanation of an aspect of calculus for someone who might be struggling with it</small></h3>
					<div class="pull-right box-tools hidden-xs">
						<button class="btn btn-success btn-xs" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
					</div>
				</div>
				<div class="box-body">
					<label for="title">Content Title</label>
					<input type="text" name="title" class="form-control" placeholder="Insert an indicative title" value="${textContent.title}">
				</div>
				<div class="box-body pad">
					<label for="body">Content Body</label>
					<textarea  name="body" class="textarea no-horizontal-resize" placeholder="Thoroughly and completely describe the narrow subject you have chosen to engage in" style="width: 100%; height: 200px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;"
					>${textContent.body}</textarea>
				</div>
				<div class="box-body pad">
					<label for="tags-input">Tags/Categories</label>
					<input name="tagsInput" id="tags-input" value="${textContent.tags}" />
				</div>
			</div>
		
			
			<div class="box box-primary contribute-box">
				<div class="box-header">
					<i class="fa fa-paper-plane hidden-xs"></i>
					<h3 class="box-title">Distribution and Submit<small> Read through the information to make sure you are happy submitting your content</small></h3>
					<div class="pull-right box-tools hidden-xs">
						<button class="btn btn-primary btn-xs" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
					</div>
				</div>
				<div class="box-body">
					<p> 
						legaleze.
					</p>
				
					<div class="submit-buttons-pull-right">
						<input class="btn btn-default submit" name="saveButton" type="submit" value="Save Changes">
						<input class="btn btn-warning submit" name="saveButton" type="submit" value="Save + Preview">
						<a class="btn btn-danger" href="/contribute/dashboard">Discard Changes</a>
						<input class="btn btn-info submit" name="saveButton" type="submit" value="Submit Anonymously">
						<input class="btn btn-primary submit" name="saveButton" type="submit" value="Submit">
					</div>
				</div>
			</div>
		</form>
	</jsp:attribute>
		
	<jsp:attribute name="javascript">
		<script src="/_static/js/plugins/jQuery-Tags-Input-master/jquery.tagsinput.min.js"></script>
		<script src="/_static/js/CalcU/practiceProblemCreation.js"></script>
	</jsp:attribute>
</t:genericpage>