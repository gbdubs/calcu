<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	
	<jsp:attribute name="pageTitle">
		CalcU | Contribute Practice Problem
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000 align-left">
				<div class="box box-primary contribute-box box-solid">
					<div class="box-header">
						<i class="fa fa-info-circle hidden-xs"></i>
						<h3 class="box-title">Welcome to the Practice Problem Editor! <small>Here are some basic guidelines to keep in mind</small></h3>
						<div class="pull-right box-tools hidden-xs">
							<button class="btn btn-primary btn-xs" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
						</div>
					</div>
					<div class="box-body">
						Thank you for taking the time to contribute a problem to the project!  There are a few things for you to know. 
						<ul>
							<li>
								You can intermittently save your work by using the save button at the bottom of the page. This does not submit the problem, 
								and it will remain private until you submit. Once you submit the problem, you will not be able to edit it further. 
							</li>
							<li>
								You can insert beautifully typeset mathematical expressions easily, using LaTex syntax, which will be automatically extracted
								and rendered to look amazing.  To find out how to write LaTex expressions, and the wrapping syntax that we have made availible,
								check out the <a href="/contribute/latex-introduction">LaTex Introduction</a>.
							</li>
							<li>
								Please only submit questions that you have the rights to.  For example, scraping questions out of a textbook 
								may violate copyright law, and we wouldn't want that.  
							</li>
							<li>
								You have the option of submitting the question anonymously, or submitting with your username attached to it.  
								Note that the username that will be associated with this problem forevermore is the one that you have when you submit.  
								You will not receive karma for anonymous submissions.  
							</li>
						</ul>
						Thank you so much for your work and your contribution to the project!
					</div>
				</div>
	
				<form action="/contribute/practice-problem/" method="post">
					<input type="hidden" value="${practiceProblem.uuid}" name="uuid">
					<div class="box box-warning contribute-box box-solid">
						<div class="box-header">
							<i class="fa fa-question hidden-xs"></i>
							<h3 class="box-title">Problem Editor <small>Construct the information that the student will need to solve the problem in this area</small></h3>
							<div class="pull-right box-tools hidden-xs">
								<button class="btn btn-warning btn-xs" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
							</div>
						</div>
						<div class="box-body">
							<label for="title">Problem Title</label>
							<input type="text" name="title" class="form-control" placeholder="Insert a brief description of your problem" value="${practiceProblem.title}">
						</div>
						<div class="box-body pad">
							<label for="body">Problem Description</label>
							<textarea id="body-editor" name="body" class="textarea wysihtml5-beam-me-up" placeholder="Outline the information nescessary to solve the problem">${practiceProblem.body}</textarea>
						</div>
						<div class="box-body pad">
							<label for="tags-input">Tags/Categories</label>
							<input name="tagsInput" id="tags-input" value="${practiceProblem.tags}" />
						</div>
					</div>
		
					<div class="box box-success contribute-box box-solid">
						<div class="box-header">
						<i class="fa fa-lightbulb-o hidden-xs"></i>
							<h3 class="box-title">Your Solution <small> Explain how to solve the problem, and explicitly state the correct answer in this field</small></h3>
							<div class="pull-right box-tools hidden-xs">
								<button class="btn btn-success btn-xs" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
							</div>
						</div>
						<div class="box-body pad">
							<label for="body">Official Solution</label>
							<textarea id="solution-editor" name="authorSolution" class="textarea wysihtml5-beam-me-up" placeholder="Construct your solution here">${practiceProblem.authorSolution}</textarea>
						</div>
					</div>
			
					<div class="box box-primary contribute-box box-solid">
						<div class="box-header">
							<i class="fa fa-paper-plane hidden-xs"></i>
							<h3 class="box-title">Distribution and Submit<small> Read through the information to make sure you are happy submitting your problem</small></h3>
							<div class="pull-right box-tools hidden-xs">
								<button class="btn btn-primary btn-xs" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-minus"></i></button>
							</div>
						</div>
						<div class="box-body">
							<p> 
								Thank you for taking the time to create this content! Before you submit, please verify a few quick things:
								<ul>
									<li> The work is fully your own, and does not violate the copyright laws of anyone else or any business</li>
									<li> You are okay with us showing this to the world, and if you submit un-anonymously, it will be associated with your username</li>
									<li> After submitting you <b>cannot change any aspect of your content again!</b>  For this reason, it is recommended that you save and preview before submitting.</li>
								</ul>
								Once you agree to the above, press submit (Or Feel Free to Submit Anonymously)! Thank you for your hard work!
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
				<jsp:include page="/WEB-INF/templates/latex-playground.jsp">
					<jsp:param name="hasSolution" value="${true}" />
				</jsp:include>
			</div>
		</div>	
	</jsp:attribute>
	
	<jsp:attribute name="javascriptDependencies">
		practiceProblemCreation lpg
	</jsp:attribute>
	
</t:genericpage>