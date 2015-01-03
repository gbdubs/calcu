<form action="${param.formPostUrl}" method="post" class="rating-system-horizontal hidden-xs">
	<input type="hidden" value="${param.contentId}" name="contentId">
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
	<input class="btn btn-block btn-default" type="submit">
		Submit
	</input>
</form>

<form action="${param.formSubmitUrl}" method="post" class="rating-system-horizontal small-screen visible-xs">
	<input type="hidden" value="${param.contentId}" name="contentId">
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
	<input class="btn btn-block btn-default" type="submit">
		Submit
	</input>
</form>