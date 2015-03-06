$(function(){
	
	 if ($("#page-js").text().indexOf("lpg") > -1) {
	
		var playgroundButton = "<li><button class=\"btn btn-default fa fa-magic lpg-toggle\" title=\"Insert Math Equations\" type=\"button\"></button></li>";
		
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
		
		var inputBox = $(".lpg-input-box");
		
		var updateResultBox = function(newText){
			var lineChoice = $("#lpg-latex-line-select").val();
			if (lineChoice == "inline"){
				newText = "\\(" + newText + "\\)";
			} else {
				newText = "$$" + newText + "$$";
			}
			$("#lpg-result-box").empty().text(newText);
			MathJax.Hub.Queue(["Typeset",MathJax.Hub,"lpg-result-box"]);
		}
		
		$(".lpg-button").click(function(){
			var tex = $(this).data("tex");
			var newText = $(inputBox).val() + tex
			$(inputBox).val(newText);
			updateResultBox(newText);
		});
		
		$(".lpg-input-box").change(function(){
			var newText = $(this).val();
			updateResultBox(newText);
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
			var lineChoice = $("#lpg-latex-line-select").val();
			if (lineChoice == "inline"){
				expression = " \\( " + expression + " \\) ";
			} else {
				expression = " $$ " + expression + " $$ ";
			}
			var newText = originalText + " " + expression;
			$(editor).text(newText);
		});
	
		$(".lpg-insert-into-solution").click(function(){
			console.log("triggered");
			var iframe = $("#solution-editor").parent().find("iframe");
			var editor = $(".wysihtml5-editor", iframe.contents());
			var expression = $(".lpg-input-box").first().val();
			var originalText = $(editor).text();
			if (originalText == $("#solution-editor").attr("placeholder")){
				originalText = "";
			}
			var lineChoice = $("#lpg-latex-line-select").val();
			if (lineChoice == "inline"){
				expression = " \\( " + expression + " \\) ";
			} else {
				expression = " $$ " + expression + " $$ ";
			}
			var newText = originalText + " " + expression;
			$(editor).text(newText);
		});
		
		$(".lpg-instructions-toggle").click(function(){
			if ($(this).hasClass("btn-default")){
				$(this).addClass("btn-primary").removeClass("btn-default");
				$(".lpg-instructions").removeClass("hidden");
			} else {
				$(this).addClass("btn-default").removeClass("btn-primary");
				$(".lpg-instructions").addClass("hidden");
			}
		});
	}
});