<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Links and Support
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="box box-solid box-primary">
			<div class="box-header">
				<h3 class="box-title">Links and Support <small>  Learning Calculus is difficult, but it can always be made easier</small></h3>
			</div>
			<div class="box-body">
				<p>
					What CalcU does is attempt to make learning Calculus as easy as it can be.  That being said, this website isn't for everyone, nor is it able
					to cover the wide range of materials that are available on the web.  This page contains sites, textbooks, practice problems, video lectures,
					online courses, and a whole range of materials that you might find useful on your journey through learning Calculus.  If you have a favorite
					resource that hasn't been listed on this page, please contact us with a link to it, so that other people can profit from the information you 
					have!
				</p>
				<p>
					Finally, remember that there is no correct or incorrect way to learn. That has been one of the formative missions of this site, in that we
					want to make this large subject universally applicable and understandable to people with different levels of interest, experience, skill and
					expertise in Calculus. If you have found this site helpful, that is great! But our main goal is directing people to the resources which will
					best aid them, and it is important to recognize that that won't always be our site.
				</p>
				<p>
					Best of Luck on Your Journey!
				</p>
			</div>
			<div class="box-body">
				<div class="box box-primary">
					<div class="box-header">
						<h3 class="box-title">Online Courses <small>  These free courses are offered by professionals in mathematics education. Many are self 
						paced, and have their content available on your schedule. These are not recommended for those who already have a structured course in 
						their life, but they can be a stellar resource for providing a schedule and order to the information for those who want it.</small></h3>
					</div>
					<div class="box-body">
						<ul>
							<li><b><a href="https://www.edx.org/course/pre-university-calculus-delftx-calc001x#.VMbTgmTF_As">Coursera: Pre-University Calculus</a></b> An online course offered by edX, which is offered on a paced schedule every month.</li>
							<li><b><a href="https://www.coursera.org/learn/calculus1">Coursera: Calculus One</a></b>  An online course offered by MOOC giant coursera, via Ohio State University</li>
							<li><b><a href="https://www.mooculus.osu.edu">Mooculus</a></b>  A flashy new MOOC that takes a new approach on an old subject.</li>
						</ul>
					</div>
				</div>
				
				<div class="box box-info">
					<div class="box-header">
						<h3 class="box-title">Open Source Textbooks <small>  Open Access Publishing is one of the best things to happen in education in the last
						few decades. These are some of the best textbooks available anywhere, and they are yours free as a PDF download.  These can be a great
						asset in augmenting a self paced education.</small></h3>
					</div>
					<div class="box-body">
						<ul>
							<li><b><a href="http://ocw.mit.edu/resources/res-18-001-calculus-online-textbook-spring-2005/textbook/">MIT: Spring 2005 Text</a></b> This text includes an instructors manual and a student study guide.</li>
							<li><b><a href="https://www.math.wisc.edu/~keisler/calc.html">Calculus: An Infinitessimal Approach</a></b>  A comprehensive and elementary approach to a very difficult subject.</li>
						</ul>
					</div>
				</div>
			
				<div class="box box-success">
					<div class="box-header">
						<h3 class="box-title">Video Examples <small>  Often the most helpful thing we can have is someone explain a specific concept to us.
						Luckily, organizations have started to see the value in more example oriented demonstrations of what things mean and how they are used.
						A number of these organizations offer amazing support for all aspects of calculus, and using these resources can be hugely helpful when there
						is a specific area of study you want to hone in on.</small></h3>
					</div>
					<div class="box-body">
						<ul>
							<li><b><a href="https://www.khanacademy.org/math/differential-calculus">Khan Academy: Differential Calculus</a></b> This series of video lessons comes from the world's leading producer of this type of media</li>
							<li><b><a href="https://www.khanacademy.org/math/integral-calculus">Khan Academy: Integral Calculus</a></b>  The second part of this two-subject series</li>
						</ul>
					</div>
				</div>
			
			</div>
		</div>
		
	</jsp:attribute>	
	<jsp:attribute name="javascript">
		<script src="/_static/js/plugins/jQuery-Tags-Input-master/jquery.tagsinput.min.js"></script>
		<script src="/_static/js/CalcU/searchByTag.js"></script>
	</jsp:attribute>
</t:genericpage>
	