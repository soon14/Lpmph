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
					$(".homepage-ad-top").homepage_ad_top();
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

	$homepage_ad_top = {
		/**
		 * @param el
		 * @param datas
		 * @author jiangzh
		 */
		"doData" : function(el, datas,opts) {
			$adList = $(".ad-top-list", el);
			$adList.empty();
			if(datas && datas.respList && datas.respList.length > 0){//有数据
				$(el).removeClass("hide");
				$homepage_ad_top.doAdList($adList,datas.respList,opts);
			}else{//无数据
				$(el).addClass("hide");
			}
		},
		"control":function(el,opts){
			$AdData.getData({
				"placeId":opts.placeId,
				"placeSize":opts.placeSize,
				"placeWidth":opts.placeWidth,
				"placeHeight":opts.placeHeight,
				"status" : opts.status,
				"callback":function(ads){
					$homepage_ad_top.doData(el,ads,opts);
					$homepage_ad_top.adClose(el);
				}
			});
		},
		"doAdList":function(_obj,dataList,opts){
			_obj.empty();
			//imgstyle = "width : "+ opts.placeWidth +"px;height : "+opts.placeHeight +"px;";
			var str = "";
			$.each(dataList, function(i, n) {
				str += "<a href='"+n.linkUrl+"'><img src='"+n.vfsUrl+"' alt='"+n.advertiseTitle+"'/></a>";
			});
			_obj.append(str);
			_obj = null;
		},
		"adClose":function(el){
			$(".ad-close",el).live("click",function(){
				$(el).addClass("hide");
			});
		}
	};
	$.fn.homepage_ad_top = function() {
		return this.each(function() {
			var status = $(this).data("comStatus");
			if(!status || status == "" || status =="undefined" ||status==undefined || status == "init"){

			} else {
				return $(this);
			}
			$(this).data("comStatus","do");
			
			var opts = $.ecpPlugin.parseOptions(this,defaultOpts);
			if(opts == null || !opts.placeId || !opts.placeSize ){
				return ;
			}
			$homepage_ad_top.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
