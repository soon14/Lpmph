;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/do-data' ], function(adData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".zw-weix-slider").zw_weix_slider();
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

	$zw_weix_slider = {
		/**
		 * 微信汇重点推荐，公用；
		 * @param el
		 * @param images
		 */
		"doData" : function(el, datas) {
			if(datas &&datas.respList && datas.respList.length > 0){
				$(el).empty();
				$.each(datas.respList, function(i, n) {
				$_li = $('<li class="span4 clearfix"></li>');
				$_div = $('<div class="por-left"></div>');
				$_onePic = "<img src='"+n.onePicUrl+"' alt='"+(n.imName?n.imName:"")+"'>";
				$_hidediv = '<div class="pro-trans"><img src='+n.twicePicUrl+' alt='+(n.imName?n.imName:"")+'/></div>';
				$_div.append($_onePic);
				$_div.append($_hidediv);
				
				$_divcon = $('<div class="con-w"></div>');
				$_name = '<h3>'+n.imName+'</h3>';
				$_des = '<p class="wxh">'+n.describeInfo+'</p>';
				$_remark ='<p class="w-name">'+(n.remark?n.remark:"")+'</p>';
				$_divcon.append($_name);
				$_divcon.append($_des);
				$_divcon.append($_remark);
				
				$_li.append($_div);
				$_li.append($_divcon);
				$(el).append($_li);
				});
				
			}else{//无数据
				$(el).empty();
				$(el).append("<div style='visibility:visible;padding-top:100px;padding-left:45%' class ='pro-empty'>亲，暂无数据</div>");
			}
		},
		"control":function(el,opts){
			$(".main-ad-slider-img", el).append("<div class='loading-small'></div>");
			$WeixinData.getData({
				"placeId":opts.placeId,
				"placeSize":opts.placeSize,
				"placeWidth":opts.placeWidth,
				"placeHeight":opts.placeHeight,
				"status" : opts.status,
				"callback":function(ads){
					$zw_weix_slider.doData(el,ads);
				}
			});
		}
	};

	$.fn.zw_weix_slider = function() {
		return this.each(function() {
			var status = $(this).data("comStatus");
			if(!status || status == "" || status =="undefined" ||status==undefined || status == "init"){

			} else {
				return $(this);
			}
			$(this).data("comStatus","do");
			
			var opts = $.ecpPlugin.parseOptions(this,defaultOpts);
			if(opts == null || !opts.placeId || !opts.placeSize || !opts.placeWidth || !opts.placeHeight || !opts.status){
				return ;
			}
			$zw_weix_slider.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
