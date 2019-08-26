$(function(){
	var url = $('#staticHtml').val();
	$.ajax({
		url : url,
		async : true,
		type : "get",
		dataType : 'jsonp',
		jsonp :'jsonpCallback',//注意此处写死jsonCallback
		success: function (data) {
			$('.text').html(data.result);
	    },
	    error:function(){
	    	$('.text').empty();
	    }
	});

	$('#news').on('click',function(){
		
		$('#moreForm').submit();
		
	});
	
    $('#ad').on('click',function(){
		
		$('#adForm').submit();
		
	});
	
})