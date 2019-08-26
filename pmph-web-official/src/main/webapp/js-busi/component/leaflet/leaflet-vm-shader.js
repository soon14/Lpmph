;
/**
 * 首页顶部广告
 * @param factory
 */
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/do-data' ], function(adData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".leaflet-vm-shader").leaflet_vm_shader();
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

	$leaflet_vm_shader = {
		/**
		 * @param el
		 * @param datas
		 * @author zhanbh
		 */
		"doData" : function(el, datas,opts) {
			if(datas){
				$(el).empty();
				$(el).html(datas);
			}/*else{//无数据
				$(el).empty();
			}*/
		},
		"control":function(el,opts){
			$AdData.getDataVM({
				"placeId":opts.placeId,
				"placeSize":opts.placeSize,
				"placeWidth":opts.placeWidth,
				"placeHeight":opts.placeHeight,
				"returnUrl":opts.returnUrl,
				"status" : opts.status,
				"callback":function(datas){
					$leaflet_vm_shader.doData(el,datas,opts);
				}
			});
		}
	};
	$.fn.leaflet_vm_shader = function() {
		return this.each(function() {
			var status = $(this).data("comStatus");
			if(!status || status == "" || status =="undefined" ||status==undefined || status == "init"){

			} else {
				return $(this);
			}
			$(this).data("comStatus","do");
			
			var opts = $.ecpPlugin.parseOptions(this,defaultOpts);
			if(opts == null || !opts.placeId || !opts.placeSize){
				return ;
			}
			$leaflet_vm_shader.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
