$(function() {
    "use strict";
    
    $(".btn-group.difficulty-rating-buttons").each(function(){
    	
    	var userId = $(this).data("user");
    	var contentUuid = $(this).data("uuid");
    	var buttonGroup = this;
    	
    	$("button", this).click(function(){
        	var difficulty = $(this).data("difficulty");
        	$.ajax({
        		type: "POST",
        		url: "/personalize/difficulty",
        		data: "userId="+userId+"&contentUuid="+contentUuid+"&difficulty="+difficulty
        	});
        	
        	$("button", buttonGroup).removeClass("btn-success").addClass("btn-default");
        	$(this).removeClass("btn-default").addClass("btn-success");
    	});
    	
    });
  
});