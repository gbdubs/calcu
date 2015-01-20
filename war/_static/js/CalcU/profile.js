$(function() {
    "use strict";

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
});