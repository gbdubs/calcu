<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Home
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="main-page-funky-wrapper">
		<div class="row">
			<div class="col-xs-12">
				<div class="box box-solid">
					<div class="box-content">
						<div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
							<ol class="carousel-indicators">
								<li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
								<li data-target="#carousel-example-generic" data-slide-to="1" class=""></li>
								<li data-target="#carousel-example-generic" data-slide-to="2" class=""></li>
							</ol>
							<div class="carousel-inner">
								<div class="item active">
									<img style="max-width:100%;" src="/_static/img/welcome-banner.png" alt="First slide">
								</div>
								<div class="item">
									<img style="max-width:100%;" src="/_static/img/learning-black.png" alt="First slide">
								</div>
								<div class="item">
									<img style="max-width:100%;" src="/_static/img/scraper-black.png" alt="First slide">
								</div>
							</div>
							<a class="left carousel-control" href="#carousel-example-generic" data-slide="prev">
								<span class="glyphicon glyphicon-chevron-left"></span>
							</a>
							<a class="right carousel-control" href="#carousel-example-generic" data-slide="next">
								<span class="glyphicon glyphicon-chevron-right"></span>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-sm-12 hidden-xs">
				<div class="small-box bg-green">
					<div class="inner">
						<h3>
							New Here? Check out the Tutorial to get started!
						</h3>
						<p>
							Check out the tutorial to get the basic organization of the site, and understand where to start!
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-university"></i>
					</div>
					<a href="/tutorial" class="small-box-footer">
						Lets Get Started! <i class="fa fa-arrow-circle-right"></i>
					</a>
				</div>
			</div>
			<div class="col-sm-12 visible-xs">
				<div class="small-box bg-green">
					<div class="inner">
						<h3>
							New Here? 
						</h3>
						<p>
							Check out the Tutorial to get started!
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-university"></i>
					</div>
					<a href="/tutorial" class="small-box-footer">
						Lets Get Started! <i class="fa fa-arrow-circle-right"></i>
					</a>
				</div>
			</div>
		</div>
		
		<div class="row">
		
			<div class="col-lg-3 col-xs-6">
				<div class="small-box bg-blue">
					<div class="inner">
						<h3>
							Search
						</h3>
						<p>
							Find the Content You Are Looking For
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-search"></i>
					</div>
					<a href="/search" class="small-box-footer">
						Search Now <i class="fa fa-arrow-circle-right"></i>
					</a>
				</div>
			</div>
			
			<div class="col-lg-3 col-xs-6">
				<div class="small-box bg-yellow">
					<div class="inner">
						<h3>
							Explore
						</h3>
						<p>
							Discover new areas of Calculus
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-binoculars"></i>
					</div>
					<a href="/explore" class="small-box-footer">
						Explore Now <i class="fa fa-arrow-circle-right"></i>
					</a>
				</div>
			</div>
			
			<div class="col-lg-3 col-xs-6">
				<div class="small-box bg-red">
					<div class="inner">
						<h3>
							Create
						</h3>
						<p>
							Contribute back to the Community
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-pencil"></i>
					</div>
					<a href="/contribute" class="small-box-footer">
						Create Now <i class="fa fa-arrow-circle-right"></i>
					</a>
				</div>
			</div>
			
			<div class="col-lg-3 col-xs-6">
				<div class="small-box bg-purple">
					<div class="inner">
						<h3>
							Personalize
						</h3>
						<p>
							Get the most out of your Experience
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-street-view"></i>
					</div>
					<a href="/personalize" class="small-box-footer">
						Personalize Now <i class="fa fa-arrow-circle-right"></i>
					</a>
				</div>
			</div>
		
		</div>

		<div class="row">
			<div class="col-sm-6">
				<div class="box box-solid bg-maroon">
					<div class="box-header">
						<h3 class="box-title">A Teacher That Thinks Just Like You</h3>
					</div>
					<div class="box-body">
						<p>
							Just like Pandora and Spotify can guess what kind of music you like without knowing anything about music, 
							we provide you with resources that are adapted to the way that you think about mathematics and calculus.  
							The more that you rate our resources, the better our chances of finding information that will be custom tailored to your style of learning.	
						</p>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="box box-solid bg-olive">
					<div class="box-header">
						<h3 class="box-title">An Active Community</h3>
					</div>
					<div class="box-body">
						<p>
							CalcU aims to bring together a wide range of people, students, teachers, and everyone in between, to share their calculus knowledge, 
							and help others who are in the continuous process of learning.  To single out the best resources, and to give their creators the credit they deserve, 
							we have embedded meaningful reviewing deep into the heart of CalcU. 
						</p>
					</div>
				</div>
			</div>
		</div>
		
		
		
		<div class="row">
			<div class="col-lg-4 col-md-4 col-sm-6 col-xs-6">

				<div class="small-box bg-light-blue">
					<div class="inner">
						<h3>
							25
						</h3>
						<p>
							Users
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-user"></i>
					</div>
				</div>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-6 col-xs-6">
				<div class="small-box bg-orange">
					<div class="inner">
						<h3>
							1,419
						</h3>
						<p>
							Pieces of Content
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-newspaper-o"></i>
					</div>
				</div>
			</div>
			<div class="col-lg-4 col-md-4 hidden-sm hidden-xs">
				<div class="small-box bg-aqua">
					<div class="inner">
						<h3>
							96%
						</h3>
						<p>
							Active Users
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-check"></i>
					</div>
				</div>
			</div>
		</div>
		
		
		
		
		</div>
	</jsp:attribute>
</t:genericpage>