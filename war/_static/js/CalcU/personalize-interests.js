$(function() {
    "use strict";
    
    var toggleInterestOnClick = function(){
    	var interest = $(this).data("interest");
    	var userId = $(this).data("user");
    	var action = $(this).data("action");

    	$.ajax({
    		type: "POST",
    		url: "/personalize/interest",
    		data: "userId="+userId+"&interest="+interest+"&action="+action
    	});
    	
    	if (action === "add"){
    		$(this) .removeClass("btn-default")
    				.addClass("btn-success")
    				.data("action", "remove");
    	} else {
    		$(this) .removeClass("btn-success")
					.addClass("btn-default")
					.data("action", "add");
    	}
    };
 
    
    $(".toggle-interest-button").click(toggleInterestOnClick);
  
});