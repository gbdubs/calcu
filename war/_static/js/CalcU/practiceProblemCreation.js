$(function() {
    "use strict";

    $("#tags-input").tagsInput({
    	'width': '100%',
    	'height': 'auto',
    	'defaultText': 'To Add Tag, Press Enter',
    	'removeWithBackspace': true,
    });
     
    $(".wysihtml5-beam-me-up").wysihtml5();
});