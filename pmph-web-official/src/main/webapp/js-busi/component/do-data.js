/**
 * 与广告数据相关的模块； 自执行函数；
 */
$AdData = function(){
	/**
	 * 根据内容位置，获取广告；如果有符合要求的广告信息，则调用数据处理的回调函数(callback);否则直接返回；
	 * 入参：
	 * placeId : 内容位置Id；
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author jiangzh
	 */
	var getData = function(opts){
		$.eAjax({
			url : $webroot + '/leaflet/qryLeafletList',
			data : {
				"placeId" : opts.placeId,
				"placeSize" : opts.placeSize,
				"placeWidth":opts.placeWidth,
				"placeHeight":opts.placeHeight,
				"status" : opts.status
			},
			async : true,
			type : "post",
			dataType : "json",
			success : function(data, textStatus) {
				if (data == null) {
					return;
				} else {
					if(opts.callback && $.isFunction(opts.callback)){
						opts.callback(data);
					}
				}
			},
			error :  function(){
				if(opts.callback && $.isFunction(opts.callback)){
					opts.callback(null);
				}
			}
		});
	};
	/**
	 * 根据内容位置，获取广告；如果有符合要求的广告信息，则调用数据处理的回调函数(callback);否则直接返回；
	 * 入参：
	 * placeId : 内容位置Id；
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author jiangzh
	 */
	var getDataVM = function(opts){
		$.eAjax({
			url : $webroot + '/leaflet/qryLeafletVM',
			data : {
				"placeId" : opts.placeId,
				"placeSize" : opts.placeSize,
				"placeWidth":opts.placeWidth,
				"placeHeight":opts.placeHeight,
				"returnUrl":opts.returnUrl,
				"status" : opts.status
			},
			async : true,
			type : "post",
			dataType : "html",
			success : function(data, textStatus) {
				if (data == null) {
					return;
				} else {
					if(opts.callback && $.isFunction(opts.callback)){
						opts.callback(data);
					}
				}
			},
			error :  function(){
				if(opts.callback && $.isFunction(opts.callback)){
					opts.callback(null);
				}
			}
		});
	};
	return {
		"getData" : getData,
		"getDataVM":getDataVM
	};
}();

/**
 * 网站信息列表； 自执行函数；（页面右侧）
 */
$AboutData = function(){
	/**
	 * 获取（有效）网站信息列表，则调用数据处理的回调函数(callback);否则直接返回；
	 * 入参：
	 * placeId : 内容位置Id；
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author jiangzh
	 */
	var getData = function(opts){
		$.eAjax({
			url : $webroot + '/main/qrySiteInfo',
			data : {
				"status" : opts.status
			},
			async : true,
			type : "post",
			dataType : "json",
			success : function(data, textStatus) {
				if (data == null) {
					return;
				} else {
					if(opts.callback && $.isFunction(opts.callback)){
						opts.callback(data);
					}
				}
			},
			error :  function(){
				if(opts.callback && $.isFunction(opts.callback)){
					opts.callback(null);
				}
			}
		});
	};
	
	return {
		"getData" : getData
	};
}();

/**
 * 栏目； 自执行函数；
 */
$ChannelData = function(){
	/**
	 * 获取（有效）栏目，则调用数据处理的回调函数(callback);否则直接返回；
	 * 入参：
	 * siteId : 站点Id；
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author zhanbh
	 */
	var getData = function(opts){
		$.eAjax({
			url : $webroot + '/main/qrychannel',
			data : {
				"status" : opts.status,
				"siteId" : opts.siteId,
				"platformType" : opts.platformType,
				"channelType" : opts.channelType,
				"returnUrl" : opts.returnUrl
			},
			async : true,
			type : "post",
			dataType : "html",
			success : function(data, textStatus) {
				if (data == null) {
					return;
				} else {
					if(opts.callback && $.isFunction(opts.callback)){
						opts.callback(data);
					}
				}
			},
			error :  function(){
				if(opts.callback && $.isFunction(opts.callback)){
					opts.callback(null);
				}
			}
		});
	};
	
	return {
		"getData" : getData
	};
}();



/**
 * 根据位置Id获取文章列表； 自执行函数；
 */
$ArtlistData = function(){
	/**
	 * 获取（有效）文章列表，则调用数据处理的回调函数(callback);否则直接返回；
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author zhanbh
	 */
	var getData = function(opts){
		$.eAjax({
			url : $webroot + '/main/qryArticleListVM',
			data : {
				"placeId":opts.placeId,//位置id
				"placeSize":opts.placeSize,//获取的文章条数
				"placeWidth":opts.placeWidth,//图片规格  宽
				"placeHeight":opts.placeHeight,//图片规格 长
				"menuType":opts.menuType,//样式类型
				"status":opts.status, //状态
				"returnUrl":opts.returnUrl,
				"homepageIsShow":opts.homepageIsShow//首页是否显示
			},
			async : true,
			type : "post",
			dataType : "html",
			success : function(data, textStatus) {
				if (data == null) {
					return;
				} else {
					if(opts.callback && $.isFunction(opts.callback)){
						opts.callback(data);
					}
				}
			},
			error :  function(){
				if(opts.callback && $.isFunction(opts.callback)){
					opts.callback(null);
				}
			}
		});
	};
	
	return {
		"getData" : getData
	};
}();
/**
 * 与  楼层数据相关的模块； 自执行函数；
 */
$floorGdsData = function(){
	/**
	 * 获取 楼层下的信息，如，页签、商品……；如果有符合要求的信息，则调用数据处理的回调函数(callback);否则直接返回；
	 * 入参：
	 * placeId : 内容位置Id；
	 * gdsSize : 位置展示商品个数
	 * tabSize : 页签个数
	 * placeWidth : 图片宽度
	 * placeHeight" : 图片长度
	 * status : 状态
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author jiangzh
	 */
	var getData = function(opts){
		$.eAjax({
			url : $webroot + '/main/qryFloorList',
			data : {
				"placeId" : opts.placeId,
				"gdsSize" : opts.gdsSize,
				"tabSize" : opts.tabSize,
				"placeWidth" : opts.placeWidth,
				"placeHeight" : opts.placeHeight,
				"status" : opts.status
			},
			async : true,
			type : "post",
			dataType : "json",
			success : function(data, textStatus) {
				if (data == null) {
					return;
				} else {
					if(opts.callback && $.isFunction(opts.callback)){
						opts.callback(data);
					}
				}
			},
			error :  function(){
				if(opts.callback && $.isFunction(opts.callback)){
					opts.callback(null);
				}
			}
		});
	};
	var getDataVM = function(opts){
		$.eAjax({
			url : $webroot + '/main/qryFloorVM',
			data : {
				"placeId" : opts.placeId,
				"gdsSize" : opts.gdsSize,
				"tabSize" : opts.tabSize,
				"placeWidth" : opts.placeWidth,
				"placeHeight" : opts.placeHeight,
				"floorImgSize" : opts.floorImgSize,
				"floorImgWidth" : opts.floorImgWidth,
				"floorImgHeight" : opts.floorImgHeight,
				"returnUrl" : opts.returnUrl,
				"status" : opts.status
			},
			async : true,
			type : "post",
			dataType : "html",
			success : function(data, textStatus) {
				if (data == null) {
					return;
				} else {
					if(opts.callback && $.isFunction(opts.callback)){
						opts.callback(data);
					}
				}
			},
			error :  function(){
				if(opts.callback && $.isFunction(opts.callback)){
					opts.callback(null);
				}
			}
		});
	};
	
	return {
		"getData" : getData,
		"getDataVM" : getDataVM
	};
}();

/**
 * 友情链接； 自执行函数；
 */
$linkData = function(){
	/**
	 * 获取（有效）友情链接，则调用数据处理的回调函数(callback);否则直接返回；
	 * 入参：
	 *
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author huangxm9
	 */
	var getData = function(opts){
		$.eAjax({
			url : $webroot + '/link/querylink',
			data : {
				"status" : opts.status,
				"siteId" : opts.siteId
			},
			async : true,
			type : "post",
			dataType : "json",
			success : function(data, textStatus) {
				if (data == null) {
					return;
				} else {
					if(opts.callback && $.isFunction(opts.callback)){
						opts.callback(data);
					}
				}
			},
			error :  function(){
				if(opts.callback && $.isFunction(opts.callback)){
					opts.callback(null);
				}
			}
		});
	};
	
	return {
		"getData" : getData
	};
}();
