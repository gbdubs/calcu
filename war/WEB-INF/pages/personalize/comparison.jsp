<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Personalize Content
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-success box-solid">
			<div class="box-header">
				<i class="fa fa-flag-checkered"></i>
				<h3 class="box-title">Reasoning Calibration: Step (${stepNumber}/15)</h3>
			</div>
			<div class="box-body">
				<p>
					We want to provide you with resources that lay out ideas in the same way that you would
					naturally think of them, and meets you at your level of current understanding. To accomplish
					this, we are presenting you with two different explanations for the same phenomenon in
					Calculus.  Try to think broadly about what you like and dislike about these explanations.
				</p>
				<p>
					We will ask you to decide which piece of content you prefer on a number of Criteria.
					Each criterion gives us an idea of what aspect of each item you found helpful or difficult.
				</p>
				
				<p class="content-comparison centered" data-category="overall" data-user="${user.userId}">
					<button class="btn btn-default" data-uuid="${textContent1.uuid}">Explanation 1</button>
					<label> Which did you prefer overall?</label>
					<button class="btn btn-default" data-uuid="${textContent2.uuid}">Explanation 2</button>
				</p>
				
				<div class="row">
					<div class="col-sm-6 col-xs-12">
						<div class="box box-success box-solid">
							<div class="box-header">
								<i class="fa fa-flag-o"></i>
								<h3 class="box-title">Explanation 1: ${textContent1.title}</h3>
							</div>
							<div class="box-body">
								<span class="preserve-line-formatting">${textContent1.body}</span>
							</div>
						</div>
					</div>
					<div class="col-sm-6 col-xs-12">
						<div class="box box-success box-solid">
							<div class="box-header">
								<i class="fa fa-flag"></i>
								<h3 class="box-title">Explanation 2: ${textContent2.title}</h3>
							</div>
							<div class="box-body">
								<span class="preserve-line-formatting">${textContent2.body}</span>
							</div>
						</div>
					</div>
				</div>
				<div class="overflower">
					<div class="pull-right">
						<a class="btn btn-success" href="/personalize/${stepNumber + 1}">
							Next Step
							<i class="fa fa-arrow-circle-right fa-fw"></i>
						</a>
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/compare-content.js"></script>
	</jsp:attribute>
</t:genericpage>