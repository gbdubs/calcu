$(function() {
    "use strict";

    $("#tags-input").tagsInput({
    	'width': '100%',
    	'height': 'auto',
    	'defaultText': 'Type Tags Here',
    	'removeWithBackspace': true,
    });
    
    var toggleBookmarkButton = function(){
    	var userId = $(this).data("user");
		var content = $(this).data("content");
		var action = $(this).data("action");
		
    	$.ajax({
			type: "POST",
			url: "/bookmark",
			data: "userId="+userId+"&contentUuid="+content+"&action="+action
		});
    	
    	if (action === "add") {
    		$(this).data("action","remove");
    		$('i', this).removeClass("fa-bookmark-o").addClass("fa-bookmark");
    	} else {
    		$(this).data("action","add");
    		$('i', this).removeClass("fa-bookmark").addClass("fa-bookmark-o");
    	}
    };
	
	$(".toggle-bookmark-button").click(toggleBookmarkButton);
    
    $(".search-result-page").each(function(){
    	var parent = $(this).parent(".box-body");
    	var id = "#" + $(this).attr("id");
    	var tabId = id + "-tab";
    	$(tabId, parent).click(function(){
    		$(".search-result-page", parent).each(function(){
    			$(this).addClass("hidden");
    		});
    		$(id, parent).removeClass("hidden");
    		$(".result-page-tab", parent).each(function(){
    			$(this).removeClass("selected")
    		});
    		$(this).addClass("selected");
    	});
    });
    
    $(".add-recommended-tag").click(function(){
    	var tag = $(this).data("tag");
    	var input = $("#tags-input");
    
    	var newTag = "<span class=\"tag\"><span>"+ tag +"&nbsp;&nbsp;</span><a href=\"#\" title=\"Removing tag\"></a></span>"
    	
    	$(input).val($(input).val() + "," + tag);
    	$(newTag).insertBefore("#tags-input_tag");

    	$(this).remove();
    });
});