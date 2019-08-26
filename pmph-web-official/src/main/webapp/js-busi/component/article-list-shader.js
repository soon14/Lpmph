;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define([ 'jquery-powerSwitch','ecp-component/do-data' ], function(slide,adData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".article-list-shader").article_list_shader();
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

	$article_list_shader = {
		/**
		 * 官网首页文章列表渲染
		 * @param el
		 * @param 
		 */
		"doData" : function(el, datas) {
			if(datas){
				$(el).empty();
				$(el).html(datas);
			}/*else{//无数据
				$(el).empty();
			}*/
		},
		"control":function(el,opts){
			$ArtlistData.getData({
				"placeId" : opts.placeId,//位置id
				"placeSize" : opts.placeSize,//获取的文章条数
				"placeWidth":opts.placeWidth,//图片规格  宽
				"placeHeight":opts.placeHeight,//图片规格 长
				"menuType":opts.menuType,//样式类型
				"status" : opts.status, //状态
				"homepageIsShow" : opts.homepageIsShow,//首页是否显示
				"returnUrl":opts.returnUrl,
				"callback":function(articles){
					$article_list_shader.doData(el,articles);
					$article_list_shader.showImg(el);
				}
			});
		},
		"showImg":function(el){//显示文章图片
			$(".article-show-img",el).live("mouseover",function(e){
				//隐藏所有图片
				$("li",el).removeClass("active");
				$(this).parent("li").addClass("active");
			});
		}
	};

	$.fn.article_list_shader = function() {
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
			$article_list_shader.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
