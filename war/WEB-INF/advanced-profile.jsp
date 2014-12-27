<!DOCTYPE html>
<html ng-app="">
<head>
    <meta charset="UTF-8">
    <title>CalcU.us Workspace</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- bootstrap wysihtml5 - text editor -->
    <link href="../css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="../css/AdminLTE.css" rel="stylesheet" type="text/css" />

    <!-- AngularJS dependency -->
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js" type="text/javascript"></script>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>

<body class="skin-blue fixed">
<!-- header logo: style can be found in header.less -->

<header class="header">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">

            <div>
                Log in to save your progress, your favorite resources, and contribute to the community!
            </div>
            <!--<div class="pull-left image">
                            <img src="../img/avatar5.png" class="img-square" alt="User Image" />
                        </div>-->

            <!--<div class="pull-left info">
                <p>Hello, Grady</p>
                <div class="progress progress-striped">
                    <div class="progress-bar progress-bar-primary" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                        <span class="sr-only">40% Complete (success)</span>
                    </div>
                </div>
            </div>-->
        </div>
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="active">
                <a href="index.html">
                    <i class="fa fa-dashboard"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li>
                <a href="about.html">
                    <i class="fa fa-cogs"></i>
                    <span>About</span>
                </a>
            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-question-circle"></i>
                    <span>Search</span>
                    <i class="fa fa-angle-right pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="pages/search/questions.html"><i class="fa fa-angle-double-right"></i> Questions + Answers</a></li>
                    <li><a href="pages/search/problems.html"><i class="fa fa-angle-double-right"></i> Problems + Solutions</a></li>
                    <li><a href="pages/search/content.html"><i class="fa fa-angle-double-right"></i> Lectures + Resources</a></li>
                </ul>
            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-graduation-cap"></i>
                    <span>Contribute</span>
                    <i class="fa fa-angle-right pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="pages/contribute/guidelines.html"><i class="fa fa-angle-double-right"></i> Contribution Guidelines</a></li>
                    <li><a href="pages/contribute/answer.html"><i class="fa fa-angle-double-right"></i> Answer Questions</a></li>
                    <li><a href="pages/contribute/submit.html"><i class="fa fa-angle-double-right"></i> Submit Problems</a></li>
                    <li><a href="pages/contribute/solve.html"><i class="fa fa-angle-double-right"></i> Solve Problems</a></li>
                    <li><a href="pages/contribute/teach.html"><i class="fa fa-angle-double-right"></i> Contribute Content</a></li>
                </ul>
            </li>
            <li>
                <a href="pages/progress.html">
                    <i class="fa fa-tasks"></i>
                    <span>Progress</span>
                </a>
            </li>
            <li>
                <a href="pages/achievments.html">
                    <i class="fa fa-trophy"></i>
                    <span>Achievments</span>
                </a>
            </li>
            <li>
                <a href="pages/tutorials.html">
                    <i class="fa fa-university"></i>
                    <span>Tutorials</span>
                </a>
            </li>
            <li>
                <a href="pages/suggestions.html">
                    <i class="fa fa-comments"></i>
                    <span>Suggestions</span>
                </a>
            </li>
            <li>
                <a href="pages/external.html">
                    <i class="fa fa-external-link"></i>
                    <span>Links and Support</span>
                </a>
            </li>
            <li>
                <a href="pages/contact.html">
                    <i class="fa fa-heart"></i>
                    <span>Contact</span>
                </a>
            </li>
        </ul>
    </section>
</header>

<div class="wrapper row-offcanvas row-offcanvas-left">
<!-- Left side column. contains the logo and sidebar -->

<aside class="left-side sidebar-offcanvas">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">

            <div>
                Log in to save your progress, your favorite resources, and contribute to the community!
            </div>

        </div>
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="active">
                <a href="index.html">
                    <i class="fa fa-dashboard"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li>
                <a href="about.html">
                    <i class="fa fa-cogs"></i>
                    <span>About</span>
                </a>
            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-question-circle"></i>
                    <span>Search</span>
                    <i class="fa fa-angle-right pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="pages/search/questions.html"><i class="fa fa-angle-double-right"></i> Questions + Answers</a></li>
                    <li><a href="pages/search/problems.html"><i class="fa fa-angle-double-right"></i> Problems + Solutions</a></li>
                    <li><a href="pages/search/content.html"><i class="fa fa-angle-double-right"></i> Lectures + Resources</a></li>
                </ul>
            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-graduation-cap"></i>
                    <span>Contribute</span>
                    <i class="fa fa-angle-right pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="pages/contribute/guidelines.html"><i class="fa fa-angle-double-right"></i> Contribution Guidelines</a></li>
                    <li><a href="pages/contribute/answer.html"><i class="fa fa-angle-double-right"></i> Answer Questions</a></li>
                    <li><a href="pages/contribute/submit.html"><i class="fa fa-angle-double-right"></i> Submit Problems</a></li>
                    <li><a href="pages/contribute/solve.html"><i class="fa fa-angle-double-right"></i> Solve Problems</a></li>
                    <li><a href="pages/contribute/teach.html"><i class="fa fa-angle-double-right"></i> Contribute Content</a></li>
                </ul>
            </li>
            <li>
                <a href="pages/progress.html">
                    <i class="fa fa-tasks"></i>
                    <span>Progress</span>
                </a>
            </li>
            <li>
                <a href="pages/achievments.html">
                    <i class="fa fa-trophy"></i>
                    <span>Achievments</span>
                </a>
            </li>
            <li>
                <a href="pages/tutorials.html">
                    <i class="fa fa-university"></i>
                    <span>Tutorials</span>
                </a>
            </li>
            <li>
                <a href="pages/suggestions.html">
                    <i class="fa fa-comments"></i>
                    <span>Suggestions</span>
                </a>
            </li>
            <li>
                <a href="pages/external.html">
                    <i class="fa fa-external-link"></i>
                    <span>Links and Support</span>
                </a>
            </li>
            <li>
                <a href="pages/contact.html">
                    <i class="fa fa-heart"></i>
                    <span>Contact</span>
                </a>
            </li>
        </ul>
    </section>
</aside>

<aside class="right-side">

<!-- Main content -->
<section class="content">

<div class="box box-primary">
    <div class="box-header">
        <i class="fa fa-user"></i>
        <h3 class="box-title">Account Settings</h3>
        <div class="box-tools pull-right">
            <button class="btn btn-primary btn-xs" data-widget="collapse"><i class="fa fa-minus"></i></button>
        </div>
    </div><!-- /.box-header -->
    <div class="box-body">
        <div class="row">

            <div class="user-profile-settings col-sm-12 col-md-5 col-lg-6">
                <form>
                    <label for="edit-user-name">Username: </label>
                    <div class="input-group">
                        <input type="text" class="form-control" name="edit-user-name" id="edit-user-name" placeholder="BooToBacon">
                            <span class="input-group-btn">
                                <button class="btn btn-primary" type="button">Change Username </button>
                            </span>
                    </div>
                </form>
                <form>
                    <label for="profile-picture-upload">Profile Picture: </label>
                    <br>
                    <img src="../img/user-bg.png" id="profile-picture-editor-display">
                    <div class="input-group">
                        <input type="file" class="form-control" id="profile-picture-upload">
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
</section><!-- right col -->
</aside><!-- /.right-side -->
</div><!-- ./wrapper -->


<!--

<div class="opaque-screen"></div>

<div class="form-box" id="login-box">
    <div class="header">Sign In</div>
    <form action="../../index.html" method="post">
        <div class="body bg-gray">
            <p class="modal-paragraph">
                <span class="fa-stack fa-lg pull-left fa-fw google-paragraph-indent">
                    <i class="fa fa-square-o fa-stack-2x"></i>
                    <i class="fa fa-google fa-stack-1x"></i>
                </span>
                To readily serve you while protecting your data effectively, CalcU.us uses authorization through a Google account.  These accounts are secure, reliable, and free, and a new one can be created <a href="https://accounts.google.com/SignUp">here</a>.  We are considering expanding this service to other OpenAuth providers based on demand.  If you would like to see another supported, please <a href="contact.html">contact</a> us.
            </p>
        </div>
        <div class="footer">
            <button type="submit" class="btn btn-primary btn-block">Log In with Google Account</button>
            <button type="submit" class="btn btn-danger btn-block">Continue as Guest User</button>
        </div>
    </form>

    <div class="margin text-center">
        <span>Coming Soon: Sharing content on these social networks:</span>
        <br>
        <button class="btn bg-light-blue btn-circle"><i class="fa fa-facebook"></i></button>
        <button class="btn bg-aqua btn-circle"><i class="fa fa-twitter"></i></button>
    </div>
</div>

-->


<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js" type="text/javascript"></script>
<script src="//code.jquery.com/ui/1.11.1/jquery-ui.min.js" type="text/javascript"></script>
<!-- Bootstrap WYSIHTML5 -->
<script src="../js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
<!-- AngularJS dependency -->
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js" type="text/javascript"></script>

<!-- AdminLTE App -->
<script src="../js/AdminLTE/app.js" type="text/javascript"></script>

</body>
</html>
