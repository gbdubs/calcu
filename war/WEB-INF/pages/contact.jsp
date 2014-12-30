<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="content">
		<form class="box box-primary contact-page-content" action="/submit-contact-form" method="post">
			<div class="box-header">
				<i class="fa fa-comments"></i>
				<h3 class="box-title">Suggestions/Feedback</h3>
			</div>
			<div class="box-body">
				Thank you for taking the time to provide us with some feedback!  We allow you to submit feedback whether you are logged in or not.
				Please offer suggestions that you would like to see on the site, and feel free to leave your username and number if you would like
				a response.  Note that I cannot reply to any of your ideas if you choose to submit anonymously, as we will not record any identifying
				information if you chose that option.
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-sm-6 col-xs-12 contact-page-inputs">
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-envelope"></i></span>
							<input type="text" class="form-control" placeholder="Email" name="email">
						</div>
					</div>
					<div class="col-sm-6 col-xs-12 contact-page-inputs">
						<div class="input-group">
							<span class="input-group-addon">@</span>
							<input type="text" class="form-control" placeholder="Username" name="username">
						</div>
					</div>
					<div class="col-sm-12 contact-page-inputs">
						<select class="form-control" name="responsePreference">
							<option value="notNeeded" selected>I am okay without a followup, if it isn't necessary.</option>
							<option value="pleaseFollowUp">Please follow up with me about my comment if you can!</option>
							<option value="doNotContact">Please do not contact me or record my email address.</option>
						</select>
					</div>
				</div>
		
				<div class="form-group">
					<label>Comments, Concerns, Compaints, Suggestions?</label>
					<textarea class="form-control no-vertical-resize" rows="6" placeholder="We are excited to hear what you have to say!" name="body"></textarea>
				</div>
		
				<div class="submit-buttons">
					<input class="btn btn-info submit" name="anonymousSubmit" type="submit" value="Submit Anonymously">
					<input class="btn btn-primary submit" name="regularSubmit" type="submit" value="Submit">
				</div>
			</div>
		</form>
		
		<div class="box box-success">
			<div class="box-header">
				<i class="fa fa-heart"></i>
				<h3 class="box-title">Contact</h3>
			</div>
			<div class="box-body">
				If you would prefer a less formal way of contact, my email is <a href="mailto:grady.b.ward@gmail.com">grady.b.ward@gmail.com</a> . Feel free to reach out to discuss anything ed-tech,
				your thoughts on the site, or how we can better deliver information to those who need it.
			</div>
		</div>
		
	</jsp:attribute>
</t:genericpage>