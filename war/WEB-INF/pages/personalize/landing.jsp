<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Personalize
	</jsp:attribute>

	<jsp:attribute name="content">
	
		<div class="box box-success box-solid">
			<div class="box-header">
				<i class="fa fa-street-view"></i>
				<h3 class="box-title">Personalize Your CalcU Experience</h3>
			</div>
			<div class="box-body">
				
				<p>
					Using the same kind of algorithms that suggest you music on Pandora and Spotify, in combination
					with the group/personality matching done in many online dating sites, this page is the heart of
					the scientific part of CalcU's mission.
				</p>
				<p>
					The personalization exercise is not a one time event, but something you can do often, to shift
					the kind of resources that you are recommended. They can help us figure out whether we should
					direct you to harder or easier resources.  They give us an idea of what you are currently interested in.
					When you take the time to use this tool, your experience on the site should change, and change
					for the better.
				</p>
				<p>
					Each time you take the personalization exercise, you will be given 15 questions. Some things you
					will likely run in to:
				</p>
				<ul>
					<li>Difficulty Calibration -- We ask you how difficult a certain question is for you. Note you don't have to solve it, but are welcome to.</li>
					<li>Interest Selection -- We will present you with 25 different topics in Calculus, and you tell us which you are interested in.</li>
					<li>Reasoning Calibration -- We present you with two different pieces of Content, you tell us which one makes more sense to you.</li>
				</ul>
				<p>
					We encourage you to keep using this tool until you feel that the resources you are getting directed to
					are helpful, relevant, and appropriately difficult.  As always, thank you for using this tool, and please
					let us know what you think of it!
				</p>			
				<c:if test="${!loggedIn}">
					<span class="btn btn-block btn-warning login-button">
						You must log in to access this feature
					</span>
				</c:if>
				<c:if test="${loggedIn}">
					<a href="/personalize/1" class="btn btn-block btn-success">
						Lets Get Started!
					</a>
				</c:if>
			</div>	
		</div>
	</jsp:attribute>
</t:genericpage>