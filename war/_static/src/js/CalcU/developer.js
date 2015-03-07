$(function(){
	var contentApprovalActionButtons = $(".content-approval-action-buttons");
	
	$(".content-approval-action-button").click(function(){
		var action = $(this).data("action");
		var contentType = $(this).data("type");
		var uuid = $(this).parent().data("uuid");
	
		$.ajax({
			type: "POST",
			url: "/admin/content-approval",
			data: "uuid="+uuid+"&action="+action+"&contentType="+contentType
		});
	
		var color = "";
		if ($(this).hasClass("btn-success")){
			color = "green";
		} else if ($(this).hasClass("btn-warning")){
			color = "yellow";
		} else if ($(this).hasClass("btn-primary")){
			color = "light-blue";
		} else if ($(this).hasClass("btn-info")){
			color = "aqua";
		} else if ($(this).hasClass("btn-danger")){
			color = "red";
		}
		
		$(this).closest(".content-approval-action-buttons").children("button")
			.removeClass().addClass("btn").addClass("btn-default").addClass("disabled");
		$(this).removeClass("btn-default").addClass("bg-" + color).addClass("disabled")
		
		var id = $(this).closest(".content-approval-content-display").attr("id");
		
		var newId = "#" + id.substring(0, id.length - 7);
		console.log(newId);
		$(newId).addClass("bg-" + color);
	});
	
});