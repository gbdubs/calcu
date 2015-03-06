<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | ${username}'s Profile
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-primary box-solid">
			<div class="box-header">
				<i class="fa fa-user"></i>
				<h3 class="box-title">Account Settings</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-primary btn-sm" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body padding-20">
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
						<form action="${profilePictureUpload}" method="post" enctype="multipart/form-data" class="no-margin">
							<label for="profile-picture-upload">Profile Picture Upload (Max size 1 MB): </label>
							<br>
							<img src="${profilePictureUrl}" id="profile-picture-editor-display">
							<div class="input-group">
								<input type="hidden" name="userId" value="${user.userId}"/>
								<input type="file" class="form-control bg-blue" id="profile-picture-upload" name="profilePictureUpload" multiple="false"/>
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
		<div class="box box-success box-solid">
			<div class="box-header">
				<i class="fa fa-flag"></i>
				<h3 class="box-title">Notifications</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-success btn-sm" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<c:forEach items="${notificationsMenu}" var="notification">
					<div class="alert notification alert-${notification.color} alert-dismissable">
						<button type="button" class="close remove-notification-button" data-user="${user.userId}" data-uuid="${notification.uuid}" data-dismiss="alert" aria-hidden="true">×</button>
						<img src="${notification.image}" class="img-square" alt="user image"/>
						<h4><a href="${notification.url}">${notification.title}</a></h4>
						<p>${notification.description}</p>
					</div>
				</c:forEach>
				<c:if test="${fn:length(notificationsMenu) == 0}">
					<h4 class="centered">No Notifications To See! Go Contribute in the Community!</h4>
				</c:if>
			</div>
		</div>
		
		<span id="bookmarks" class="page-locator"></span>
		<div class="box box-warning box-solid">
			<div class="box-header">
				<i class="fa fa-bookmark"></i>
				<h3 class="box-title">Bookmarks</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-warning btn-sm" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<c:forEach items="${bookmarksMenu}" var="bookmark">
					<div class="alert bookmark alert-${bookmark.color} alert-dismissable">
						<button type="button" class="close remove-bookmark-button" data-user="${user.userId}" data-content="${bookmark.uuid}" data-action="remove" data-dismiss="alert" aria-hidden="true">×</button>
						<h4><a href="${bookmark.url}">${bookmark.title}</a></h4>
						<p>${bookmark.description}</p>
					</div>
				</c:forEach>
				<c:if test="${fn:length(bookmarksMenu) == 0}">
					<h4 class="centered">No Bookmarks Here! Go Explore the World! Smell the Calculus Roses!</h4>
				</c:if>
			</div>
		</div>
		
		<span id="karma" class="page-locator"></span>
		<div class="box box-danger box-solid">
			<div class="box-header">
				<i class="fa fa-trophy"></i>
				<h3 class="box-title">Karma Profile</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-danger btn-sm" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body padding-20">
				<p>
					You are currently at Level ${userLevel}. In order to reach Level ${userLevel + 1}, you will need to get to ${5 * (userLevel * (userLevel + 1))} karma. 
					
					To find out more about how the karma system works, and the different leveling thresholds, visit the <a href="/tutorial">Tutorial Page</a>.
					
					Displayed below is your "karma breakdown", showing how you have earned Karma by using the site.
				</p>
				<jsp:include page="/WEB-INF/templates/karma-profile.jsp" />
			</div>
		</div>
		
		<span id="content" class="page-locator"></span>
		<a href="/contribute/dashboard" style="cursor:pointer;">
			<div class="box box-info box-solid">
				<div class="box-header" style="border-radius: 3px;">
					<i class="fa fa-pencil"></i>
					<h3 class="box-title">Your Content <small>Click here to see all of the content you have created!</small></h3>
				</div>
			</div>
		</a>
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/profile.js" type="text/javascript"></script>
	</jsp:attribute>
</t:genericpage>