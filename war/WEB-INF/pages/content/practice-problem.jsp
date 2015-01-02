<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Practice Problem
	</jsp:attribute>    
    
    <jsp:attribute name="content">
		<div class="secondary-response-box box box-primary box-question">
			<div class="box-header">
				<div class="response-box-icon-and-score">
					<i class="fa fa-question-circle"></i>
					<span>+${practiceProblem.karma}</span>
				</div>
				<h3 class="box-title">${practiceProblem.title}</h3>
			</div>
		
			<div class="rating-system-vertical">
				<div class="rating-system-vertical-title">
					<span>Evaluate</span>
				</div>
				<div class="knob-wrapper">
					<input type="text" class="knob" value="80" data-skin="tron" data-thickness="0.1" data-width="60" data-height="60" data-fgcolor="#00a65a">
					<div class="knob-label">Helpfulness</div>
				</div>
				<div class="knob-wrapper">
					<input type="text" class="knob" value="80" data-skin="tron" data-thickness="0.1" data-width="60" data-height="60" data-fgcolor="#f39c12">
					<div class="knob-label">Difficulty</div>
				</div>
				<div class="knob-wrapper">
					<input type="text" class="knob" value="80" data-skin="tron" data-thickness="0.1" data-width="60" data-height="60" data-fgcolor="#0073b7">
					<div class="knob-label">Quality</div>
				</div>
				<div class="btn btn-sm btn-default">
					Submit
				</div>
			</div>
			<div class="secondary-response-response">
				<div class="user-information box box-primary">
					<img src="${practiceProblem.author.profilePictureUrl}" class="user-img">
					<a class="user-name" href="${practiceProblem.author.profileUrl}">${practiceProblem.author.username}</a><br/>
					<span class="user-stat">${practiceProblem.author.readableKarma}</span><br/>
					<span class="user-date">Submitted ${practiceProblem.shortReadableTime}</span>
				</div>
				${practiceProblem.body}
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>