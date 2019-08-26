;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define([ 'ecp-component/do-data' ], function(adData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".zw-journals-slider").zw_journals_slider();
				}
			};

		});
	} else {
		// 全局模式 ，不使用AMD 规范的时候，使用的插件
		factory(jQuery);
	}
}
		(function($) {
			// / 具体插件的定义；
			var defaultOpts = {
				placeId : ""
			};

			$zw_journals_slider = {
				/**
				 * 页面轮播广告，公用；
				 * 
				 * @param el
				 * @param images
				 */
				"doData" : function(el, datas) {
					if (datas && datas.respList && datas.respList.length > 0) {
						$zw_journals_slider.doAdList(el, datas.respList);
					} else {// 无数据
						$(el).empty();
						$(el)
								.append(
										"<div style='visibility:visible;padding-top:100px;padding-left:45%' class ='pro-empty'>亲，暂无数据</div>");
					}
				},
				"control" : function(el, opts) {

					$AdData.getData({
						"placeId" : opts.placeId,
						"placeSize" : opts.placeSize,
						"placeWidth" : opts.placeWidth,
						"placeHeight" : opts.placeHeight,
						"status" : opts.status,
						"callback" : function(ads) {
							$zw_journals_slider.doData(el, ads);
						}
					});
				},
				"doAdList" : function(el, dataList) {
					$(el).empty();
					img_str = "";
					$
							.each(
									dataList,
									function(i, n) {
										img_str = "<img src='"
												+ n.vfsUrl
												+ "' alt='"
												+ (n.advertiseTitle ? n.advertiseTitle
														: "")
												+ "' class='ql-banner'  style='opacity: 0.1;filter:alpha(opacity=10);' />"
									});
					$(el).append(img_str);
					
				    $('.ql-banner').scrollLoading({
				        callback: function (obj) {
				            $(obj).animate({'opacity': 1}, 1000);
				        }
				    });
				}
			};

			$.fn.zw_journals_slider = function() {
				return this.each(function() {
					var status = $(this).data("comStatus");
					if (!status || status == "" || status == "undefined"
							|| status == undefined || status == "init") {

					} else {
						return $(this);
					}
					$(this).data("comStatus", "do");

					var opts = $.ecpPlugin.parseOptions(this, defaultOpts);
					if (opts == null || !opts.placeId || !opts.placeSize
							|| !opts.placeWidth || !opts.placeHeight
							|| !opts.status) {
						return;
					}
					$zw_journals_slider.control(this, opts);

					$(this).data("comStatus", "end");
					return $(this);
				});
			};
		}));
