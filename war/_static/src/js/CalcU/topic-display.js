$(function() {
    "use strict";
    
    if ($("#page-js").text().indexOf("topic-display") > -1) {
	    
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
		
    }
});