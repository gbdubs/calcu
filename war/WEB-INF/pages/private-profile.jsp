<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | ${username}'s Profile
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-primary">
			<div class="box-header">
				<i class="fa fa-user"></i>
				<h3 class="box-title">Account Settings</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-primary btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
		
					<div class="user-profile-settings col-sm-12 col-md-5 col-lg-6">
						<form data-user="${user.userId}">
							<label for="edit-user-name">Username: </label>
							<div class="input-group">
								<input type="text" class="form-control" name="edit-username" id="edit-user-name" value="${username}">
								<span class="input-group-btn">
									<input class="btn btn-primary" type="submit" value="Change Username" id="change-username-button">
								</span>
							</div>
						</form>
						<form action="${profilePictureUpload}" method="post" enctype="multipart/form-data">
							<label for="profile-picture-upload">Profile Picture Upload (Max size 1 MB): </label>
							<br>
							<img src="${profilePictureUrl}" id="profile-picture-editor-display">
							<div class="input-group">
								<input type="hidden" name="userId" value="${user.userId}"/>
								<input type="file" class="form-control" id="profile-picture-upload" name="profilePictureUpload" multiple="false"/>
								<span class="input-group-btn">
									<button class="btn btn-primary" type="submit">Square Crop and Save</button>
								</span>
							</div>
						</form>
					</div>
					
					<form class="user-email-preferences col-sm-12 col-md-7 col-lg-6" data-user="${user.userId}">
						<label for="email-pref-reply">Reply Email Preference</label>
						<select class="form-control" id="email-pref-reply" name="emailReply">
							<option value="every" <c:if test="${emailReply =='every'}">selected</c:if>>EVERY: Email me for every reply to a question of mine.</option>
							<option value="best" <c:if test="${emailReply =='best'}">selected</c:if>>BEST: Only email me a reply to a question if it recieves 5 votes.</option>
							<option value="none" <c:if test="${emailReply =='none'}">selected</c:if>>NONE: Please do not email me for any reply to a question that I ask.</option>
						</select>
						<label for="email-pref-karma">Karma Email Preference</label>
						<select class="form-control" id="email-pref-karma" name="emailKarma">
							<option value="best" <c:if test="${emailKarma =='best'}">selected</c:if>>BEST: Email me when a post of mine gets lots of Karma, or I get a gift.</option>
							<option value="gift" <c:if test="${emailKarma =='gift'}">selected</c:if>>GIFT: Only email me when another user gives me Karma as a gift.</option>
							<option value="none" <c:if test="${emailKarma =='none'}">selected</c:if>>NONE: Please do not email me no matter the amount of karma I am given.</option>
						</select>
						<label for="email-pref-recommend">Recommendation Email Preference</label>
						<select class="form-control" id="email-pref-recommend" name="emailRecommend">
							<option value="daily" <c:if test="${emailRecommend =='daily'}">selected</c:if>>DAILY: Email me daily with new resources you think might be useful to me.</option>
							<option value="weekly"<c:if test="${emailRecommend =='weekly'}">selected</c:if>>WEEKLY: Email me weekly with new resources that might be useful to me.</option>
							<option value="none"  <c:if test="${emailRecommend =='none'}">selected</c:if>>NONE: Please do not send me emails with recommendations.</option>
						</select>
						<input type="submit" value="Save Email Preferences" class="btn btn-primary pull-right" id="email-pref-submit">
					</form>
		
				</div>
			</div>
		</div>
		
		<span id="notifications" class="page-locator"></span>
		<div class="box box-success">
			
			<div class="box-header">
				<i class="fa fa-flag"></i>
				<h3 class="box-title">Notifications</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-success btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<c:forEach items="${notificationsMenu}" var="notification">
					<div class="alert alert-${notification.color} alert-dismissable">
						<button type="button" class="close remove-notification-button" data-user="${user.userId}" data-uuid="${notification.uuid}" data-dismiss="alert" aria-hidden="true">×</button>
						<h4><a href="${notification.url}">${notification.title}</a></h4>
						<p>${notification.description}</p>
					</div>
				</c:forEach>
			</div>
		</div>
		
		<span id="bookmarks" class="page-locator"></span>
		<div class="box box-warning">
			
			<div class="box-header">
				<i class="fa fa-bookmark"></i>
				<h3 class="box-title">Bookmarks</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-warning btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<c:forEach items="${bookmarksMenu}" var="bookmark">
					<div class="alert alert-${bookmark.color} alert-dismissable">
						<button type="button" class="close remove-bookmark-button" data-user="${user.userId}" data-content="${bookmark.uuid}" data-action="remove" data-dismiss="alert" aria-hidden="true">×</button>
						<h4><a href="${bookmark.url}">${bookmark.title}</a></h4>
						<p>${bookmark.description}</p>
					</div>
				</c:forEach>
			</div>
		</div>
		
		<span id="karma" class="page-locator"></span>
		<div class="box box-danger">
			<div class="box-header">
				<i class="fa fa-trophy"></i>
				<h3 class="box-title">Karma Breakdown</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-danger btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<ul class="timeline">
					<!-- timeline time label -->
					<li class="time-label">
						<span class="bg-red">10 Feb. 2014</span>
					</li>
					<!-- /.timeline-label -->
					<!-- timeline item -->
					<li>
					<span class="timeline-bubble bg-blue">
						<i class="fa fa-question"></i>
						<b class="timeline-score">30</b>
					</span>
		
						<div class="timeline-item">
							<span class="time"><i class="fa fa-clock-o"></i> 12:05</span>
							<h3 class="timeline-header"><a href="#">What is this whole dx thing anyway?</a></h3>
							<div class="timeline-body">
								Etsy doostang zoodles disqus groupon greplin oooj voxy zoodles,
								weebly ning heekya handango imeem plugg dopplr jibjab, movity
								jajah plickers sifteo edmodo ifttt zimbra. Babblely odeo kaboodle
								quora plaxo ideeli hulu weebly balihoo...
							</div>
							<div class="timeline-footer">
								<a class="btn btn-primary btn-xs">View All Answers</a>
								<a class="btn btn-danger btn-xs">Delete Question</a>
							</div>
						</div>
					</li>
					<!-- END timeline item -->
					<!-- timeline item -->
					<li>
					<span class="timeline-bubble bg-purple">
						<i class="fa fa-gift"></i>
						<b class="timeline-score">200</b>
					</span>
						<div class="timeline-item">
							<span class="time"><i class="fa fa-clock-o"></i> 5 mins ago</span>
							<h3 class="timeline-header no-border"><a href="#">Sarah Young</a> Gifted you 200 Karma!</h3>
						</div>
					</li>
					<!-- END timeline item -->
					<!-- timeline item -->
					<li>
					<span class="timeline-bubble bg-yellow">
						<i class="fa fa-bank"></i>
						<b class="timeline-score">0</b>
					</span>
						<div class="timeline-item">
							<span class="time"><i class="fa fa-clock-o"></i> 27 mins ago</span>
							<h3 class="timeline-header">You answered <a href="#">Jay White's question</a></h3>
							<div class="timeline-body">
								That is a great question Jay, the longer answer is much more complicated, but rather than looking as to exactly why this is the only solution the partial differential equation, usually we spend our time looking for solutions and their verification...
							</div>
							<div class="timeline-footer">
								<a class="btn btn-warning btn-xs">View Question</a>
								<a class="btn btn-danger btn-xs">Delete Answer</a>
							</div>
						</div>
					</li>
					<!-- END timeline item -->
					<!-- timeline time label -->
					<li class="time-label">
						<span class="bg-green">3 Jan. 2014</span>
					</li>
					<!-- /.timeline-label -->
					<!-- timeline item -->
					<li>
						<i class="fa fa-trophy bg-green"></i>
						<div class="timeline-item">
							<span class="time"><i class="fa fa-clock-o"></i> 2 days ago</span>
							<h3 class="timeline-header no-border">You achieved Level 22, with 2000 karma! Nice work!</h3>
						</div>
					</li>
					<!-- END timeline item -->
					<li>
						<i class="fa fa-clock-o"></i>
					</li>
				</ul>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/profile.js" type="text/javascript"></script>
	</jsp:attribute>
</t:genericpage>