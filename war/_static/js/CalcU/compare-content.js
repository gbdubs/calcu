$(function() {
    "use strict";
    
    var togglePreferenceOnClick = function(){    	
    	if ($(this).hasClass("btn-default")){
    		$(this) .removeClass("btn-default")
    				.addClass("btn-success");
    	} else {
    		$(this) .removeClass("btn-success")
					.addClass("btn-default");
    	}
    };
    
    $(".toggle-preference-button").click(togglePreferenceOnClick);
  
    $("#next-step-button").click(function(){
    	var body = $(this).closest('.box-body');
    	
    	var charSelected = $(".btn-success", body).data("character");
    	
    	var userId = $(this).data("user");
     	
    	$.ajax({
    		type: "POST",
    		url: "/personalize/comparison",
    		data: "userId="+userId+"&charSelected="+charSelected
    	});
    });
});