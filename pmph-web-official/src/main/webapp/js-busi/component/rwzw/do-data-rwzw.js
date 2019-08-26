/**
 * 网站信息列表； 自执行函数；
 */
$SiteInfoDataRwzw = function(){
	/**
	 * 获取（有效）网站信息列表，则调用数据处理的回调函数(callback);否则直接返回；
	 * 入参：
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author zhanbh
	 */
	var getData = function(opts){
		$.eAjax({
			url : $webroot + '/qrySiteInfo',
			data : {
				"siteId":opts.siteId,
				"status": opts.status,
				"url":opts.url,
				"siteInfoType":opts.siteInfoType
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
					opts.callback("");
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
$ChannelDataRwzw = function(){
	/**
	 * 获取（有效）栏目，则调用数据处理的回调函数(callback);否则直接返回；
	 * 入参：
	 * siteId : 站点Id；
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author huangxm9
	 */
	var getData = function(opts){
		$.eAjax({
			url : $webroot + '/qrychannel',
			data : {
				"status" : opts.status,
				"siteId" : opts.siteId,
				"type" : opts.type,
				"platformType" : opts.platformType,
				"channelType" : opts.channelType
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
 * 微信汇； 自执行函数；
 */
$WeixinData = function(){
	/**
	 * 获取（有效）微信汇，则调用数据处理的回调函数(callback);否则直接返回；
	 * 入参：
	 * siteId : 站点Id；
	 * callback : 回调函数，对于数据处理的回调函数；
	 * @author huangxm9
	 */
	var getData = function(opts){
		$.eAjax({
			url : $webroot + '/qryImageSwList',
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
	
	return {
		"getData" : getData
	};
}();