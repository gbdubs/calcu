$(function() {
    "use strict";
    
    var toggleInterestOnClick = function(){
    	var interest = $(this).data("interest");
    	var userId = $(this).data("user");
    	
    	if ($(this).hasClass("btn-default")){
    		$(this) .removeClass("btn-default")
    				.addClass("btn-success");
    	} else {
    		$(this) .removeClass("btn-success")
					.addClass("btn-default");
    	}
    };
    
    $(".toggle-interest-button").click(toggleInterestOnClick);
  
    $("#next-step-button").click(function(){
    	var userId = $(this).data("user");
    	var allTags = [];
    	$(".toggle-interest-button.btn-success").each(function(){
    		var interest = $(this).data("interest");
    		allTags.push(interest);
    	});
    	
    	var toRemove = [];
    	$(".original-interest").each(function(){
    		if (! $(this).hasClass("btn-success")){
    			var interest = $(this).data("interest");
        		toRemove.push(interest);
    		}
    	});
    	
    	$.ajax({
    		type: "POST",
    		url: "/personalize/interests",
    		data: "userId="+userId+"&add-interests="+allTags+"&remove-interests="+toRemove
    	});
    });
});