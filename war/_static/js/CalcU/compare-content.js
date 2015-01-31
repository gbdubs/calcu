$(function() {
    "use strict";
    
    $(".content-comparison").each(function(){
    	
    	var userId = $(this).data("user");
    	var category = $(this).data("category");
    	var buttonGroup = this;
    	var allUuids = [];
    	$("button", this).each(function(){
    		allUuids.push($(this).data("uuid"));
    	});
    	
    	$("button", this).click(function(){
        	var uuid = $(this).data("uuid");

        	$.ajax({
        		type: "POST",
        		url: "/personalize/content-comparison",
        		data: "userId="+userId+"&preference="+uuid+"&outOf="+allUuids.join(",")
        	});
        	
        	$("button", buttonGroup).removeClass("btn-success").addClass("btn-default");
        	$(this).removeClass("btn-default").addClass("btn-success");
    	});
    });
});