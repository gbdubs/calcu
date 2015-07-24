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
		
		function addTopicSelectorClickToScope(scopeElement){
			$("a.btn", scopeElement).click(function(){
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
					if($(".sub-topics", "#" + id + "-data").text().length > 2){
						if($("#" + id + "-box").length == 0){
							var col = parseInt(($(this).closest(".col-lg-3").attr("id")).substring(7));
							createNewSelectorBox(col+1, id);
						}
						$("#" + id + "-box").removeClass("hidden");
					} else {
						$(this).parent().addClass("no-dropdown");
					}
					$(".ts-info-box").addClass("hidden");
					if ($("#" + id + "-info-box").length == 0){
						createNewInfoBox(id);
					}
					$("#" + id + "-info-box").removeClass("hidden");
				}
				hideOrShowColumns();
			});
		}
		
		addTopicSelectorClickToScope("#topic-selector");
		
		
		function createNewSelectorBox(colNumber, id){
			var data = $("#" + id + "-data");
			var title = $(".title", data).text();
			var subTopics = $(".sub-topics", data).text().replace("[","").replace("]","").split(",");
			var shortDesc = $(".short-desc", data).text();
			var longDesc = $(".long-desc", data).text();
			var tags = $(".tags", data).text();
			
			var template = $("#ts-topic-box-template").clone().attr("id", id + "-box");
			$(".box-title", template).text(title);
			var boxBody = $(".box-body", template);
			
			for (var subTopic in subTopics){
				var newButtonId = subTopics[subTopic].trim();
				var newButtonTitle = $(".title", "#ts-" + newButtonId + "-data").text();
				var newButton = $("#ts-topic-box-button").clone().attr("id", "ts-" + newButtonId).text(newButtonTitle);
				$(boxBody).append(newButton);
			}
			
			addTopicSelectorClickToScope(boxBody);
			
			var column = $("#ts-col-" + colNumber);
			if (column.length == 0){
				column = $("#ts-column-template").clone();
				$(column).attr("id", "ts-col-" + colNumber);
				$(column).removeClass("hidden");
				$("#ts-columns").append(column);
			}
			$(column).append(template);
		}
		
		function createNewInfoBox(id){
			var data = $("#" + id + "-data");
			var title = $(".title", data).text();
			var shortDesc = $(".short-desc", data).text();
			var longDesc = $(".long-desc", data).text();
			var numContent = $(".content-size", data).text();
			var tags = $(".tags", data).text();
			
			var template = $("#ts-info-box-template").clone().attr("id", id + "-info-box");
			$(".box-title", template).text("Currently Selected: " + title);
			$(".short-description", template).text(shortDesc);
			$(".long-description", template).text(longDesc);
			$(".tags", template).text(tags);
			$("b", template).text(numContent);
			
			$("#topic-selector").append(template);
		}
		
		function hideOrShowColumns(){
			$(".ts-stashed-col", "#ts-columns").removeClass("ts-stashed-col").removeClass("ts-last-stashed");
		
			var numShown = 3;
		    if (window.width >= 1200){
		    	numShown = 4;
		    }
		    var lastVisibleCol = 0;
		    $(".col-lg-3", "#ts-columns").each(function(){
		    	var thisOneVis = false;
		    	$(".box", this).each(function(){
		    		if (! $(this).hasClass("hidden")){
		    			thisOneVis = true;
		    		}
		    	});
		    	if (thisOneVis){
		    		lastVisibleCol = lastVisibleCol + 1;
		    	}
		    });
		    
		    if (lastVisibleCol > numShown){
		    	for (var i = 1; i <= lastVisibleCol - numShown; i++){
		    		$("#ts-col-"+i, "#ts-columns").addClass("ts-stashed-col");
		    		if (i == lastVisibleCol - numShown){
		    			$("#ts-col-"+i, "#ts-columns").addClass("ts-last-stashed");
		    		}
		    	}
		    }
		}	
	}
});