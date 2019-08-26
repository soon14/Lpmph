;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/do-data'], function(gdsData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".friendship-link").friendship_link();
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

	var $friendshipLink = {
		/**
		 * 生成信息；
		 * @param el
		 * @param 
		 * @author huangxm9
		 */
		"doData" : function(el, datas) {
			if(datas){
				var dataList = datas.respList;
				var str ="";//
					//清空内容
					$(el).empty();
					
					if(dataList  && dataList.length > 0){//有数据
						//拼接列表
						$.each(dataList, function(i, n) {
							//处理
							if(i%5 == 0)//5个链接元素为一组
							str += "<p>";
							str += "<a target='_blank' href='"+n.linkUrl+"'>"+n.linkName+"</a>";
							if(i<dataList.length-1 && (i+1)%5!=0)
								str += " | "; 
							
							if((i+1)%5 == 0)
								str += "</p>"; 
						});
						$(el).append(str);
					}
		}else{
			
		}
		},
		/**
		 * 调用获取数据组件
		 */
		"control":function(el,opts){
			
			$linkData.getData({
				"siteId" : opts.siteId,
				"status" : opts.status,
				"callback" : function(links){
					$friendshipLink.doData(el,links);
				}
			});
		
		}
   
	};

	$.fn.friendship_link = function() {
		return this.each(function() {
			var status = $(this).data("comStatus");
			if(!status || status == "" || status =="undefined" ||status==undefined || status == "init"){

			} else {
				return $(this);
			}
			$(this).data("comStatus","do");
			var opts = $.ecpPlugin.parseOptions(this, defaultOpts);
			if(opts == null ){
				return ;
			}
			$friendshipLink.control(this,opts);
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
