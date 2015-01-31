$(function() {
    "use strict";
    
    $(".content-rotating").each(function () {
    	console.log("here");
    	var contentRotating = this;
    	var buttonGroup = $('.btn-group', this);
    	var defaultButton = $('.btn', buttonGroup);
    	var i = 1;
    	$(".row", this).each( function () {
    		var panel = this;
    		var id = $(this).attr('id');
    		var newId = id + "-button";
    		var newButton = $(defaultButton).clone().appendTo(buttonGroup);
    		$(newButton).attr('id', newId).removeClass('hidden').text('' + i);
    		i = i + 1;
    		
    		$(newButton).click( function () {
    			$('.row', contentRotating).addClass("hidden");
    			$(panel).removeClass("hidden");
    			$(".btn", buttonGroup).removeClass('btn-primary').addClass('btn-default');
    			$(this).addClass('btn-primary').removeClass('btn-default');
    		});
    	});
    });
    
    
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

});