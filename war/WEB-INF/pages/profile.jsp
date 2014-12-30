<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
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
						<form action="/change-user-name" method="post">
							<label for="edit-user-name">Username: </label>
							<div class="input-group">
								<input type="text" class="form-control" name="edit-username" id="edit-user-name" placeholder="${username}">
								<span class="input-group-btn">
									<input class="btn btn-primary" type="submit"  value="Change Username">
								</span>
							</div>
						</form>
						<form action="" method="post" enctype="multipart/form-data">
							<label for="profile-picture-upload">Profile Picture: </label>
							<br>
							<img src="${profilePictureUrl}" id="profile-picture-editor-display">
							<div class="input-group">
								<input type="file" class="form-control" id="profile-picture-upload" name="profilePicture">
									<span class="input-group-btn">
										<button class="btn btn-primary" type="button">Save</button>
									</span>
							</div>
						</form>
					</div>
		
					<div class="user-email-preferences col-sm-12 col-md-7 col-lg-6">
						<label for="email-pref-reply">Reply Email Preference</label>
						<select class="form-control" id="email-pref-reply">
							<option>EVERY: Email me for every reply to a question of mine.</option>
							<option>BEST: Only email me a reply to a question if it recieves 5 votes.</option>
							<option>NONE: Please do not email me for any reply to a question that I ask.</option>
						</select>
						<label for="email-pref-karma">Karma Email Preference</label>
						<select class="form-control" id="email-pref-karma">
							<option>BEST: Email me when a post of mine gets lots of Karma, or I get a gift.</option>
							<option>GIFT: Only email me when another user gives me Karma as a gift.</option>
							<option>NONE: Please do not email me no matter the amount of karma I am given.</option>
						</select>
						<label for="email-pref-recommend">Recommendation Email Preference</label>
						<select class="form-control" id="email-pref-recommend">
							<option>DAILY: Email me daily with new resources you think might be useful to me.</option>
							<option>WEEKLY: Email me weekly with new resources that might be useful to me.</option>
							<option>NONE: Please do not send me emails with recommendations.</option>
						</select>
						<input type="submit" value="Save Email Preferences" class="btn btn-primary pull-right">
					</div>
		
				</div>
			</div>
		</div>
		
		<div class="box box-success">
			<div class="box-header">
				<i class="fa fa-flag"></i>
				<h3 class="box-title">Notifications</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-success btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div><!-- /.box-header -->
			<div class="box-body">
				<div class="alert alert-danger alert-dismissable">
					<i class="fa fa-ban"></i>
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
					<b>Your Post was Flagged for Review</b> Danger alert preview. This alert is dismissable. A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart.
				</div>
				<div class="alert alert-info alert-dismissable">
					<i class="fa fa-info"></i>
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
					<b>New Karma Policy!</b> For more information on the change in Karma Policy, please click <a>here</a>.
				</div>
				<div class="alert alert-warning alert-dismissable">
					<i class="fa fa-warning"></i>
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
					<b>Provide Feedback!</b> Your question "What is dx?" was answered yesterday! Please take the time to <a>provide some feedback on it</a>!
				</div>
				<div class="alert alert-success alert-dismissable">
					<i class="fa fa-check"></i>
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
					<b>Congratulations!</b> You have now mastered the "Integration Basics, Part 1" Skill!
				</div>
			</div>
		</div>
		
		<div class="box box-warning">
			<div class="box-header">
				<i class="fa fa-bookmark"></i>
				<h3 class="box-title">Bookmarks</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-warning btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
					<h4><a>What is the dx thing I keep hearing about?</a></h4>
					<p>There is a problem that we need to fix. A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart.</p>
				</div>
				<div class="alert alert-info alert-dismissable">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
					<h4><a>What do you do about nested integrals?</a></h4>
					<p>There is a problem that we need to fix. A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart.</p>
				</div>
				<div class="alert alert-warning alert-dismissable">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
					<h4><a>How do we treat constants when we integrate?</a></h4>
					<p>There is a problem that we need to fix. A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart.</p>
				</div>
				<div class="alert alert-success alert-dismissable">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
					<h4><a>Does every function have a derivative?  Does every function have an integral?</a></h4>
					<p>There is a problem that we need to fix. A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart.</p>
				</div>
			</div>
		</div>
		
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
</t:genericpage>