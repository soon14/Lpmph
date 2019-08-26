;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define([ 'jquery-powerSwitch','ecp-component/do-data' ], function(slide,adData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".channel-shader").channel_shader();
				}
			};

		});
	} else {
		// 全局模式 ，不使用AMD 规范的时候，使用的插件
		factory(jQuery);
	}
}(function($) {
	// / 具体插件的定义；
	var defaultOpts = {
		placeId : ""
	};

	$channel_shader = {
		/**
		 * 官网栏目渲染；
		 * @param el
		 * @param datas：html代码
		 */
		"doData" : function(el, datas) {
			if(datas){
				$(el).empty();
				$(el).html(datas);
			}else{//无数据
				$(el).empty();
			}
		},
		"control":function(el,opts){
			$ChannelData.getData({
				"siteId":opts.siteId,
				"status" : opts.status,
				"platformType" : opts.platformType,
				"channelType" : opts.channelType,
				"returnUrl" : opts.returnUrl,
				"callback":function(channels){
					$channel_shader.doData(el,channels);
				}
			});
		}
	};

	$.fn.channel_shader = function() {
		return this.each(function() {
			var status = $(this).data("comStatus");
			if(!status || status == "" || status =="undefined" ||status==undefined || status == "init"){

			} else {
				return $(this);
			}
			$(this).data("comStatus","do");
			
			var opts = $.ecpPlugin.parseOptions(this,defaultOpts);
			if(opts == null || !opts.siteId || !opts.status){
				return ;
			}
			$channel_shader.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
