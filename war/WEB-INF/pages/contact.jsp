<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Contact/Suggestions
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000">
				<form class="box-primary box-solid contact-page-content" action="/submit-contact-form" method="post">
					<div class="box-header centered margin-top-100">
						<h2 class="welcome-banner">Suggestions/Feedback</h2>
					</div>
					<div class="centered margin-top-25">
						<h3 class="welcome-banner-subtitle">
							Please offer suggestions that you would like to see on the site, and feel free to leave your username and number if you would like
							a response. 
						</h3>
					</div>
					<div class="box-body margin-top-25">
						<div class="row">
							<div class="col-sm-6 col-xs-12 contact-page-inputs">
								<div class="input-group margin-top-25">
									<span class="input-group-addon"><i class="fa fa-envelope"></i></span>
									<input type="text" class="form-control" placeholder="Email" name="email">
								</div>
							</div>
							<div class="col-sm-6 col-xs-12 contact-page-inputs margin-top-25">
								<div class="input-group">
									<span class="input-group-addon">@</span>
									<input type="text" class="form-control" placeholder="Username" name="username">
								</div>
							</div>
							<div class="col-sm-12 contact-page-inputs margin-top-25">
								<select class="form-control" name="responsePreference">
									<option value="notNeeded" selected>I am okay without a followup, if it isn't necessary.</option>
									<option value="pleaseFollowUp">Please follow up with me about my comment if you can!</option>
									<option value="doNotContact">Please do not contact me or record my email address.</option>
								</select>
							</div>
						</div>
		
						<div class="form-group margin-top-25 align-left">
							<label>Comments, Concerns, Compaints, Suggestions?</label>
							<textarea class="form-control no-horizontal-resize" rows="6" placeholder="We are excited to hear what you have to say!" name="body"></textarea>
						</div>
						
						<div class="mini-font centered margin-top-25">
							We cannot reply to any of your ideas if you choose to submit anonymously, as we will not record any identifying
							information if you chose that option.
						</div>
		
						<div class="submit-buttons-pull-right margin-top-25">
							<input class="btn btn-info submit" name="anonymousSubmit" type="submit" value="Submit Anonymously">
							<input class="btn btn-primary submit" name="regularSubmit" type="submit" value="Submit">
						</div>
					</div>
				</form>
				<div class="box-success box-solid">
					<div class="box-header">
						<h2 class="secondary-banner">Contact Us</h2>
					</div>
					<div class="box-body align-left margin-bottom-50">
						If you would prefer a less formal way of contact, feel free to contact 
						<a href="mailto:grady.b.ward@gmail.com">Grady</a>, 
						<a href="rsantos@cs.brandeis.edu">Russ</a>, 
						<a href="rlacroix@cs.brandeis.edu">Roger</a>, 
						<a href="dsmyda@cs.brandeis.edu">Danny</a>, or <a href="seaurchi@brandeis.edu">Sofiya</a>  .
						Feel free to reach out to discuss anything regarding ed-tech,
						your thoughts on the site, or how we can better deliver information to those who need it.
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>