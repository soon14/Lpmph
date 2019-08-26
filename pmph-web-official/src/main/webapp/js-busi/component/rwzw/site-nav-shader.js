;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/rwzw/do-data-rwzw' ], function(siteInfoData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".site-nav-shader").site_nav_shader();
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
		status : "1"
	};

	$site_nav_shader = {
		/**
		 * 网站信息渲染；
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
			$SiteInfoDataRwzw.getData({
				"siteId":opts.siteId,
				"status": opts.status,
				"url":opts.url,
				"siteInfoType":opts.siteInfoType,
				"callback":function(siteInfos){
					$site_nav_shader.doData(el,siteInfos);
				}
			});
		}
	};

	$.fn.site_nav_shader = function() {
		return this.each(function() {
			var status = $(this).data("comStatus");
			if(!status || status == "" || status =="undefined" ||status==undefined || status == "init"){

			} else {
				return $(this);
			}
			$(this).data("comStatus","do");
			
			var opts = $.ecpPlugin.parseOptions(this,defaultOpts);
			if(opts == null || !opts.siteId || !opts.url){
				return ;
			}
			$site_nav_shader.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
