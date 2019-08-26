;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/do-data' ], function(adData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".zw-app-list").zw_app_list();
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

	$zw_app_list = {
		/**
		 * 页面轮播广告，公用；
		 * @param el
		 * @param images
		 */
		"doData" : function(el, datas) {
			if(datas &&datas.respList && datas.respList.length > 0){
				var $applist = $(".zw-app-list");
				$zw_app_list.doAdList($applist,datas.respList);
			}else{//无数据
				$(el).empty();
				$(el).append("<div style='visibility:visible;padding-top:100px' class ='pro-empty'>亲，暂无数据</div>");
			}
		},
		"control":function(el,opts){
			$(".main-ad-slider-img", el).append("<div class='loading-small'></div>");
			$AdData.getData({
				"placeId":opts.placeId,
				"placeSize":opts.placeSize,
				"placeWidth":opts.placeWidth,
				"placeHeight":opts.placeHeight,
				"status" : opts.status,
				"callback":function(ads){
					$zw_app_list.doData(el,ads);
				}
			});
		},
		"doAdList":function(_list,dataList){
			_list.empty();
			var list_li = "";
			$.each(dataList, function(i, n) {
				list_li = $('<li></li>');
				var list_a = $('<a href="'+(n.linkUrl?n.linkUrl+'" target="_blank"':"javascript:void(0);")+'></a>');
				list_a.append('<img src='+n.vfsUrl+' alt='+(n.advertiseTitle||"")+'>');
				var list_str ='<div class="all all-app">';
				list_str += '<p class="all-tit"><span>'+(n.advertiseTitle||"")+'</span></p>';
				n.remark = n.remark || "";
				if((n.remark).length>60){
					n.remark  = (n.remark).substring(0,60)+"...";
				}
				list_str += '<p class="all-con"><span>'+n.remark+'</span></p>';
				list_str += '</div>';
				list_a.append(list_str);
				list_li.append(list_a);
				_list.append(list_li);
			});
			
		}
	};

	$.fn.zw_app_list = function() {
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
			$zw_app_list.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
