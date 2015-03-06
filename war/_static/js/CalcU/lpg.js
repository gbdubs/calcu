$(function(){
	
	var playgroundButton = "<li><button class=\"btn btn-default fa fa-magic lpg-toggle\" type=\"button\"></button></li>";
	
	$(".wysihtml5-toolbar").append(playgroundButton);
	
	$(".lpg-toggle").click(function(){
		$(".lpg-toggle").each(function(){
			if ($(this).hasClass("btn-default")){
				$(this).removeClass("btn-default").addClass("btn-success");
			} else if ($(this).hasClass("btn-success")){
				$(this).removeClass("btn-success").addClass("btn-default");
			}
		});
		
		var coordinates = $(this).offset();
		coordinates.left = coordinates.left + 45;
		$(".latex-playground").toggleClass("hidden").offset(coordinates);
	});
	
	$(".latex-playground").draggable();
	
	$(".lpg-input-box").change(function(){
		var val = "$$ " + $(this).val() + " $$";
		$("#lpg-result-box").empty().text(val);
		MathJax.Hub.Queue(["Typeset",MathJax.Hub,"lpg-result-box"]);
	});
	
	$(".lpg-insert-into-body").click(function(){
		console.log("triggered");
		var iframe = $("#body-editor").parent().find("iframe");
		var editor = $(".wysihtml5-editor", iframe.contents());
		var expression = $(".lpg-input-box").first().val();
		var originalText = $(editor).text();
		if (originalText == $("#body-editor").attr("placeholder")){
			originalText = "";
		}
		var newText = originalText + " " + expression;
		$(editor).text(newText);
	});
});