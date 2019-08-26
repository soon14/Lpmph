;
/**
 * 官网首页人卫网站组件   将人卫网站当成广告处理
 * @param factory
 */
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/do-data'], function(adData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".main-web-site").main_web_site();
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

	$main_web_site = {
		/**
		 * @param el
		 * @param datas
		 * @author jiangzh
		 */
		"doData" : function(el, datas,opts) {
			$adList = $(".web-site-list", el);
			$adList.empty();
			if(datas && datas.respList && datas.respList.length > 0){//有数据
				$main_web_site.doAdList($adList,datas.respList,opts);
			}/*else{//无数据
				$(el).addClass("hide");
			}*/
		},
		"control":function(el,opts){
			$AdData.getData({
				"placeId":opts.placeId,
				"placeSize":opts.placeSize,
				"placeWidth":opts.placeWidth,
				"placeHeight":opts.placeHeight,
				"status" : opts.status,
				"callback":function(ads){
					$main_web_site.doData(el,ads,opts);
					$main_web_site.adAction(el);
				}
			});
		},
		"doAdList":function(_obj,dataList,opts){
			_obj.empty();
			var str = "";
			$.each(dataList, function(i, n) {
				str += "<li><div class='web-logo-wrap'>";
				str += "<a href='"+n.linkUrl+"' target='_blank'>"
				str += "<img src='"+n.vfsUrl+"' alt='"+n.advertiseTitle+"'/>";
				str += "</a></div>";
				str += "<a href='"+n.linkUrl+"' target='_blank'>";
				str += "<p class='web-name'>"+n.advertiseTitle+"</p>";
				str += "</a></li>"
			});
			_obj.append(str);
			_obj = null;
		},
		"adAction":function(el){
			var jcarousel = $('.jcarousel',el);
		    jcarousel.on('jcarousel:reload jcarousel:create', function () {
		         var carousel = $(this),
		                 width = carousel.innerWidth();
		         if (width >= 600) {
		             width = width / 8;
		         } else if (width >= 350) {
		             width = width / 2;
		         }
		         carousel.jcarousel('items').css('width', Math.ceil(width) + 'px');
		     })
		     .jcarousel({
		         wrap: 'circular'
		     });

		   $('.swipe-page.prev',el)
		     .jcarouselControl({
		         target: '-=1'
		     });

		   $('.swipe-page.next',el)
		     .jcarouselControl({
		         target: '+=1'
		     });

		}
	};
	$.fn.main_web_site = function() {
		return this.each(function() {
			var status = $(this).data("comStatus");
			if(!status || status == "" || status =="undefined" ||status==undefined || status == "init"){

			} else {
				return $(this);
			}
			$(this).data("comStatus","do");
			
			var opts = $.ecpPlugin.parseOptions(this,defaultOpts);
			if(opts == null || !opts.placeId || !opts.placeSize /*|| !opts.placeWidth || !opts.placeHeight*/ || !opts.status){
				return ;
			}
			$main_web_site.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
