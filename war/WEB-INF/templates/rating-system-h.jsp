<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div action="/rating/submit" method="post" class="rating-system-horizontal hidden-xs" data-user="${param.userId}" data-uuid="${param.contentUuid}">
	<div class="knob-wrapper">
		<input name="helpfulness" type="text" class="knob" value="70" data-skin="tron" data-thickness="0.1" data-width="70" data-height="70" data-fgcolor="#00a65a">
		<div class="knob-label">Helpfulness</div>
	</div>
	<div class="knob-wrapper">
		<input name="difficulty" type="text" class="knob" value="70" data-skin="tron" data-thickness="0.1" data-width="70" data-height="70" data-fgcolor="#f39c12">
		<div class="knob-label">Difficulty</div>
	</div>
	<div class="knob-wrapper">
		<input name="quality" type="text" class="knob" value="70" data-skin="tron" data-thickness="0.1" data-width="70" data-height="70" data-fgcolor="#0073b7">
		<div class="knob-label">Quality</div>
	</div>
	<c:if test="${empty param.userId}">
		<button class="btn btn-block btn-warning login-button" type="button">Must Login To Rate</button>
	</c:if>
	<c:if test="${param.alreadyRated}">
		<button class="btn btn-block btn-success disabled" type="button">Already Rated</button>
	</c:if>
	<c:if test="${!param.alreadyRated && not empty param.userId}">
		<button class="btn btn-block btn-default submit-rating" type="button">Submit Feedback</button>
	</c:if>
</div>

<div action="/rating/submit" method="post" class="rating-system-horizontal small-screen visible-xs" data-user="${param.userId}" data-uuid="${param.contentUuid}">
	<div class="knob-wrapper">
		<input name="helpfulness" type="text" class="knob" value="70" data-skin="tron" data-thickness="0.1" data-width="70" data-height="70" data-fgcolor="#00a65a">
		<div class="knob-label">Helpfulness</div>
	</div>
	<div class="knob-wrapper">
		<input name="difficulty" type="text" class="knob" value="70" data-skin="tron" data-thickness="0.1" data-width="70" data-height="70" data-fgcolor="#f39c12">
		<div class="knob-label">Difficulty</div>
	</div>
	<div class="knob-wrapper">
		<input name="quality" type="text" class="knob" value="70" data-skin="tron" data-thickness="0.1" data-width="70" data-height="70" data-fgcolor="#0073b7">
		<div class="knob-label">Quality</div>
	</div>
	<c:if test="${empty param.userId}">
		<button class="btn btn-block btn-warning login-button" type="button">Must Login To Rate</button>
	</c:if>
	<c:if test="${param.alreadyRated}">
		<button class="btn btn-block btn-success disabled" type="button">Already Rated</button>
	</c:if>
	<c:if test="${!param.alreadyRated && not empty param.userId}">
		<button class="btn btn-block btn-default submit-rating" type="button">Submit Feedback</button>
	</c:if>
</div>