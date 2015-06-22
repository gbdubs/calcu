<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Personalization Complete
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered">
			<div class="box box-success box-solid margin-top-100 max-width-at-1000">
				<div class="box-header">
					<i class="fa fa-flag-checkered"></i>
					<h3 class="box-title">You have Completed the 10 Step Personalization Process!</h3>
				</div>
				<div class="box-body align-left">
					<p>
						Now that you have taken the time to let us get to know you better, we will be able to provide
						you with better recommendations.  Remember, that the more you complete this activity, the higher
						the quality of recommendations we can give you. 
					</p>
					<p>
						In order to thank you for spending the time and energy to go through this exercise, we are giving you
						some Karma! We hope you like it!
					</p>
					<p>
						As we speak, our servers are pounding away, calculating what resources we are going to recommend
						to you next. In a minute or two, when you head over to your <a href="/recommendations">recommendations</a>
						panel, you should see some new results.
					</p>
					<p>
						While you wait, feel free to <a href="explore">Explore</a> the content on the site, <a href="/search">Search</a>
						for something in particular, or <a href="/personalize/landing">Personalize</a> your results further, by completing
						this exercise again.
					</p>
				
					<div class="overflower">
						<div class="pull-right">
							<a class="btn btn-success" href="/personalize/${stepNumber + 1}">
								Return
								<i class="fa fa-arrow-circle-right fa-fw"></i>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>