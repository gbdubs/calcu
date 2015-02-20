$(function() {
    "use strict";
    
    var removePreference = function(){
    	$(this).removeClass("btn-success").addClass("btn-default");
    };
    
    var addPreference = function(){
    	$(this).removeClass("btn-default").addClass("btn-success");
    };
    
    
    $(".toggle-preference-button").click(function(){
    	$(".toggle-preference-button").each(removePreference);
    	$(this).each(addPreference);
    });
  
    $("#next-step-button").click(function(){
    	var body = $(this).closest('.box-body');
    	
    	var charSelected = $(".btn-success", body).data("character");
    	var userId = $(this).data("user");
     	
    	$.ajax({
    		type: "POST",
    		url: "/personalize/comparison",
    		data: "userId="+userId+"&preferenceCharacter="+charSelected
    	});
    });
});