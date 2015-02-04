$(function() {
    "use strict";

    $("#email-pref-submit").click(function(){
    	$(this).attr('disabled', 'disabled');
    	
    	var parent = $(this).closest("form");
    	
    	var reply = $("#email-pref-reply", parent).val();
    	var karma = $("#email-pref-karma", parent).val();
    	var recommend = $("#email-pref-recommend", parent).val();
    	var userId = $(parent).data("user");
    	
    	$.ajax({
    		type: "POST",
    		url: "/change-email-preferences",
    		data: "userId="+userId+"&emailReply="+reply+"&emailKarma="+karma+"&emailRecommend="+recommend
    	});
    	
    	$(this).val("Changed!").addClass("btn-success").removeClass("btn-primary");
    });
    
    $("#change-username-button").click(function(){
    	$(this).attr('disabled', 'disabled');
    	
    	var parent = $(this).closest("form");
    	
    	var newName = $("#edit-user-name", parent).val();
    	var userId = $(parent).data("user");
    	
    	$.ajax({
    		type: "POST",
    		url: "/change-user-name",
    		data: "userId="+userId+"&edit-username="+newName
    	});
    	
    	$(this).val("Changed!").addClass("btn-success").removeClass("btn-primary");
    });
    
    $(".remove-bookmark-button").click(function(){
    	var userId = $(this).data("user");
    	var content = $(this).data("content");
    	var action = "remove";
    	
    	$.ajax({
    		type: "POST",
    		url: "/bookmark",
    		data: "userId="+userId+"&contentUuid="+content+"&action="+action
    	});
    });
    
    $(".remove-notification-button").click(function(){
    	var userId = $(this).data("user");
    	var uuid = $(this).data("uuid");
    	var action = "remove";
    	
    	$.ajax({
    		type: "POST",
    		url: "/remove-notification",
    		data: "userId="+userId+"&uuid="+uuid
    	});
    });
    
});