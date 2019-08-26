var basePath = GLOBAL.WEBROOT;
(function($){
	
	$.pageTop = {
			/**
			 * 修改购物车数量
			 * @param options
			 * @returns
			 */
		setOrderCount : function(options){
			var opts = $.extend({},{
				url:"",
			}, options);
			
			$.eAjax({
				url : opts.url,
				type : "post",
				async : true,
				datatype:'json',
				success : function(data) {
				  $('#cartCount').html(data);
				},
				error : function(){//失败
					 $('#cartCount').html('0');
				}
			});
			
		}
	};
	
})(jQuery);
