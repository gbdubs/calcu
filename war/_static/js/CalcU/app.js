$(function() {
    "use strict";
    
    $(".knob").knob();
    
    $("#affirm-consent-modal").each(function(){
    	var modal = this;
    	$(".affirm-consent-button").click(function(){
    		var userId = $(this).data("user");
        	$.ajax({
    			type: "POST",
    			url: "/affirm-consent",
    			data: "userId="+userId
    		});
        	$(modal).remove();
    	});
    });
    
});