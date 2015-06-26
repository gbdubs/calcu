$(function(){
	
	if ($("#page-js").text().indexOf("topic-selector") > -1) {

		function clearBoxOfSelectedInformation(element){
			$(".topic-selector-selected-below", element).removeClass("topic-selector-selected-below");
			$(".topic-selector-selected-above", element).removeClass("topic-selector-selected-above");
			$(".topic-selector-selected", element).removeClass("topic-selector-selected").removeClass("btn-success").addClass("btn-default");
		}
		
		function clearSiblingsOfSelectedBelow(element){
			$("a", $(element).parent()).removeClass("topic-selector-selected-below").removeClass("topic-selector-selected-above");
		}
		
		function addSelectedBelowToSiblings(element){
			var found = false;
			$("a", $(element).parent()).each(function(){
				if (!found){
					if (this == element){
						found = true;
					} else {
						$(this).addClass("topic-selector-selected-below");
					}
				} else {
					$(this).addClass("topic-selector-selected-above");
				}
			});
		}
		
		function closeBoxRecursively(element){
			var id = $(element).attr("id") + "-box";
			$("#" + id).addClass("hidden");
			$(".topic-selector-selected", "#" + id).each(function (){
				closeBoxRecursively(this);
			});
			clearBoxOfSelectedInformation($("#" + id));
		}
		
		function unselectSelectedButton(parentBoxElement){
			var currentlySelected = $(".topic-selector-selected", parentBoxElement);
			closeBoxRecursively(currentlySelected);
			$(currentlySelected).removeClass("topic-selector-selected").addClass("btn-default").removeClass("btn-success");
			clearSiblingsOfSelectedBelow(currentlySelected);
			
		}
		
		
		$(".topic-selector-page a.btn").click(function(){
			var id = $(this).attr("id");
			$(".no-dropdown").removeClass("no-dropdown");
			if ($(this).hasClass("topic-selector-selected")){
				unselectSelectedButton($(this).parent());
				$("#" + id + "-info-box").addClass("hidden");
			} else {
				unselectSelectedButton($(this).parent());
				$(this).addClass("topic-selector-selected").removeClass("btn-default").addClass("btn-success");
				addSelectedBelowToSiblings(this);
				$(this).parent().removeClass("topic-selector-selected-above");
				if($("#" + id + "-box").length > 0){
					$("#" + id + "-box").removeClass("hidden");
				} else {
					$(this).parent().addClass("no-dropdown");
				}
				$(".ts-info-box").addClass("hidden");
				$("#" + id + "-info-box").removeClass("hidden");
			}
		});
	}
});