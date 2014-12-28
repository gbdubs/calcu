<%@tag description="General Page template" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="content" fragment="true" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CalcU.us Workspace</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- bootstrap wysihtml5 - text editor -->
    <link href="/_static/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="/_static/css/AdminLTE.css" rel="stylesheet" type="text/css" />

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

    <!-- LOGO -->
    <a href="index.html" class="logo">
        <!-- Add the class icon to your logo image or logo icon to add the margining -->
        <img src="/_static/img/shield-icons/deriv-w.png"> Calcu.us <img src="/_static/img/shield-icons/integ-w.png">
    </a>

    <!-- Header, Navbar -->
    <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
        <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>
        <div class="navbar-right">
            <ul class="nav navbar-nav">

                <!-- Notifications Menu-->
                <li class="dropdown messages-menu">
                    <jsp:include page="/WEB-INF/templates/notifications-menu.jsp" />
                </li>

                <!-- Bookmarks Menu -->
                <li class="dropdown notifications-menu bookmarks-menu">
                    <jsp:include page="/WEB-INF/templates/bookmarks-menu.jsp" />
                </li>

                <!-- Recommendations Menu -->
                <li class="dropdown tasks-menu recommendations-menu">
                    <jsp:include page="/WEB-INF/templates/recommendations-menu.jsp" />
                </li>

                <!-- User Account + Login/Logout -->
                <li class="dropdown user user-menu">
                    <c:choose>
						<c:when test="${user != null}">
							<jsp:include page="/WEB-INF/templates/user-login-menu-logged-in.jsp" />
						</c:when>
						<c:otherwise>
							<jsp:include page="/WEB-INF/templates/user-login-menu-logged-out.jsp" />
						</c:otherwise>
					</c:choose>
                </li>
            </ul>
        </div>
    </nav>

</header>

<div class="wrapper row-offcanvas row-offcanvas-left">
    <!-- Left side column. contains the logo and sidebar -->

    <aside class="left-side sidebar-offcanvas">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">

            <!-- Sidebar user panel -->
            <div class="user-panel">
                <c:choose>
					<c:when test="${user != null}">
						<jsp:include page="/WEB-INF/templates/user-info-panel-logged-in.jsp" />
					</c:when>
					<c:otherwise>
						<jsp:include page="/WEB-INF/templates/user-info-panel-logged-out.jsp" />
					</c:otherwise>
				</c:choose>
            </div>

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
            <jsp:invoke fragment="content" />
        </section>
        
    </aside>
</div>

<div id="login-modal" class="hidden">
	<div class="opaque-screen"></div>
	
	<div class="form-box" id="login-box">
	    <div class="header">Sign In</div>
	    <form action="" method="post">
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
	            <a href="${loginUrl}" type="submit" class="btn btn-primary btn-block login-action">Log In with Google Account</a>
	            <a href="#" class="btn btn-danger btn-block login-action">Continue as Guest User</a>
	        </div>
	    </form>
	
	    <div class="margin text-center">
	        <span>Coming Soon: Sharing content on these social networks:</span>
	        <br>
	        <button class="btn bg-light-blue btn-circle"><i class="fa fa-facebook"></i></button>
	        <button class="btn bg-aqua btn-circle"><i class="fa fa-twitter"></i></button>
	    </div>
	</div>
</div>

<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js" type="text/javascript"></script>
<script src="//code.jquery.com/ui/1.11.1/jquery-ui.min.js" type="text/javascript"></script>
<!-- Bootstrap WYSIHTML5 -->
<script src="/_static/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>

<!-- AdminLTE App -->
<script src="/_static/js/AdminLTE/app.js" type="text/javascript"></script>
</body>
</html>
