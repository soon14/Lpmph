;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/do-data' ], function(adData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".main-ad-slider").main_ad_slider();
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

	$main_ad_slider = {
		/**
		 * 页面轮播广告，公用；
		 * @param el
		 * @param images
		 */
		"doData" : function(el, datas) {
			if(datas &&datas.respList && datas.respList.length > 0){
				var $main_ad_slider_num = $(".main-ad-slider-num", el);
				var $main_ad_slider_img = $(".main-ad-slider-img", el);
				$main_ad_slider.doAdList($main_ad_slider_num,$main_ad_slider_img,datas.respList);
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
					$main_ad_slider.doData(el,ads);
				}
			});
		},
		"doAdList":function(_num,_img,dataList){
			_num.empty();
			_img.empty();
			var num_str = "";
			var img_str = "";
			$.each(dataList, function(i, n) {
				var k = i + 1;
				// 拼装轮播广告下列数字  
				num_str += "<li class=\""+((k==1)?"active":"")+" page"+((k < 10)?"0":"")+k+"\"></li>";
			    
				// 拼装轮播广告图片
            img_str += "<div class='item'>";
            img_str += "<div class='img-wrap'>";
            img_str += "<a href='"+(n.linkUrl?n.linkUrl:"javascript:void(0);")+"'><img src='"+n.vfsUrl+"' alt='"+(n.advertiseTitle?n.advertiseTitle:"")+"'></a></div>";
//            img_str += "<div class='banner-tip'><div class='w'><table class='tip-tb'><tr><td class='tip-tt' style='width: 50px'>";
//            img_str += "<p>"+(n.advertiseTitle?n.advertiseTitle:"")+"</p></td>";
//            img_str += "<td class='tip-txt'><p>"+(n.remark?n.remark:"")+"</p></td>";
//            img_str += "</tr></table></div></div>";
            img_str += "</div>"
			});
			_num.append(num_str);
			_num = null;
			
			_img.append(img_str);
			  /* 官网图片轮播begin */
		    var bannerImg=document.getElementById('bannerSwipe');
		    if(bannerImg){
		        var bannerSwipe=Swipe(bannerImg, {
		            // startSlide: 4,
		           // auto: 5000,
		            callback: function(pos) {
		                $('#bannerPage li').eq(pos).addClass('active').siblings().removeClass('active');
		            }
		        });
		        $('#bannerPage li').mouseover(function(){
		            bannerSwipe.slide($(this).index());
		        });
		    }
		    /* 官网图片轮播end */
			_img = null;
		}
	};

	$.fn.main_ad_slider = function() {
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
			$main_ad_slider.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
