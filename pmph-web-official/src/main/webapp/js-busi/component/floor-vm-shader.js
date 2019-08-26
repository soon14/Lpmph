;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/do-data'], function(gdsData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".floor-vm-shader").floor_vm_shader();
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

	var $FloorVMShader = {
		/**
		 * 生成信息；
		 * @param el
		 * @param 
		 * @author zhanbh
		 */
		"doData" : function(el, datas) {
			if(datas){
				$(el).empty();
				$(el).html(datas);
			}/*else{//无数据
				$(el).empty();
			}*/
		},
		/**
		 * 调用获取数据组件
		 */
		"control":function(el,opts){
			//加载数据
			$floorGdsData.getDataVM({
				"placeId" : opts.placeId,
				"gdsSize" : opts.gdsSize,
				"tabSize" : opts.tabSize,
				"placeWidth" : opts.placeWidth,
				"placeHeight" : opts.placeHeight,
				"floorImgSize" : opts.floorImgSize,
				"floorImgWidth" : opts.floorImgWidth,
				"floorImgHeight" : opts.floorImgHeight,
				"returnUrl" : opts.returnUrl,
				"status" : opts.status,
				"callback":function(ads){
					$FloorVMShader.doData(el,ads);
				}
			});
		}

	};

	$.fn.floor_vm_shader = function() {
		return this.each(function() {
			var status = $(this).data("comStatus");
			if(!status || status == "" || status =="undefined" ||status==undefined || status == "init"){

			} else {
				return $(this);
			}
			$(this).data("comStatus","do");
			var opts = $.ecpPlugin.parseOptions(this, defaultOpts);
			if(opts == null || opts.placeId =="" || opts.placeId=="undefined"){
				return ;
			}
			$FloorVMShader.control(this,opts);
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
