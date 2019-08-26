;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define([ 'jquery-powerSwitch','ecp-component/do-data' ], function(slide,adData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".main-service-slider").main_service_slider();
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

	$main_service_slider = {
		/**
		 * 页面轮播广告，公用；
		 * @param el
		 * @param images
		 */
		"doData" : function(el, datas) {
			if(datas && datas.respList && datas.respList.length > 0){
				var $main_service_slider_num = $(".main-service-slider-num", el);
				var $main_service_slider_img = $(".main-service-slider-img", el);
				$main_service_slider.doAdList($main_service_slider_num,$main_service_slider_img,datas.respList);
			}else{//无数据
				$(el).empty();
				$(el).append("<div style='visibility:visible;' class ='pro-empty'>亲，暂无数据</div>");
			}
		},
		"control":function(el,opts){
			$(".main-service-slider-img", el).append("<div class='loading-small'></div>");
			$AdData.getData({
				"placeId":opts.placeId,
				"placeSize":opts.placeSize,
				"placeWidth":opts.placeWidth,
				"placeHeight":opts.placeHeight,
				"status" : opts.status,
				"callback":function(ads){
					$main_service_slider.doData(el,ads);
				}
			});
		},
		"doAdList":function(_num,_img,dataList){
			_num.empty();
			_img.empty();
			var num_str = "";
			var img_str = "";
			var i = 0;//用于循环
			// 拼装轮播广告下列数字 
			var itemCount = parseInt(dataList.length / 4);
			var itemover = dataList.length % 4;
			if(itemover > 0){
				itemCount++;
			}
			for(i=0;i < itemCount ; i++){
			   num_str += "<li class=\""+((i==0)?"active":"")+"\"></li>";
			}
			
			// 拼装轮播广告图片
			for(i = 0 ; i < dataList.length ; i++){
                if((i % 4) == 0 ){
                	img_str += "<div class='item'>";
                	img_str += "<ul class='service-list'>";
                	img_str += "<li><a href='"+(dataList[i].linkUrl?dataList[i].linkUrl:"javascript:void(0);")+"'><img src='"+dataList[i].vfsUrl+"' alt='"+(dataList[i].advertiseTitle?dataList[i].advertiseTitle:"")+"'></a></li>";
                }else if((i % 4) == 3){
                	img_str += "<li class='last'>";
                	img_str += "<a href='"+(dataList[i].linkUrl?dataList[i].linkUrl:"javascript:void(0);")+"'><img src='"+dataList[i].vfsUrl+"' alt='"+(dataList[i].advertiseTitle?dataList[i].advertiseTitle:"")+"'></a></li>";
                	img_str += "</ul></div>";
                }else{
                	img_str += "<li><a href='"+(dataList[i].linkUrl?dataList[i].linkUrl:"javascript:void(0);")+"'><img src='"+dataList[i].vfsUrl+"' alt='"+(dataList[i].advertiseTitle?dataList[i].advertiseTitle:"")+"'></a></li>";
                }				
			}
			if(itemover > 0){
				img_str += "</ul></div>";
			}
			_num.append(num_str);
			_num = null;
			
			_img.append(img_str);

	        var $swipe = document.getElementById('mySwipe');
	        if($swipe){
	            var bullets = $('#swipePage li');
	            var mySwipe = Swipe($swipe, {
	                // startSlide: 4,
	                auto: 5000,
	                callback: function(pos) {
	                    bullets.eq(pos).addClass('active').siblings().removeClass('active');
	                }
	            });
	            $('#swipePage li').click(function(){
	                mySwipe.slide($(this).index());
	            });
	        }
			_img = null;
		}
	};

	$.fn.main_service_slider = function() {
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
			$main_service_slider.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
