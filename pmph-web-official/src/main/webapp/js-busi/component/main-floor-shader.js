;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/do-data'], function(gdsData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".main-floor-shader").main_floor_shader();
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

	var $mainFloorShader = {
		/**
		 * 生成信息；
		 * @param el
		 * @param 
		 * @author zhanbh
		 */
		"doData" : function(el, datas) {
			var $container = $(".gds-info", el);
			if(datas && $.isPlainObject(datas)){
				//获取楼层数据
				var floorRespDTO =datas.floorRespDTO;
				//获取楼层页签数据
				var floorTabList =datas.floorTabList;
				//获取楼层商品数据
				var gdsList =datas.gdsList;
				var $floorTabs = null;
//				var $floorName = $floorName = $(".floor-name", el);
				var $fistTab = null;
				if(floorRespDTO && floorRespDTO.id){//有楼层处理
					if(floorTabList && floorTabList.length>0){//有tab页
						 $floorTabs = $(".floor-tabs", el);
						 $fistTab = floorTabList[0].id ? floorTabList[0].id : 'error';
					}else{//无tab页
						$fistTab = 'notab';
					}
					$mainFloorShader.doTabList($floorTabs,$container,floorRespDTO,floorTabList);
					$mainFloorShader.doGdsList($fistTab,$container,gdsList,el);
				}else{//没有楼层的处理
					$container.empty();
					$container.append("<div class ='pro-empty'>亲，暂无数据！</div>");
				}
			}else{
				$container.empty();
				$container.append("<div class ='pro-empty'>亲，数据丢失了！</div>");
			}
		},
		/**
		 * 调用获取数据组件
		 */
		"control":function(el,opts){
			//加加载动态图
			var $container = $(".gds-info", el);
			$container.empty();
			$container.append("<div class='loading-small'></div>");
			//加载数据
			$floorGdsData.getData({
				"placeId" : opts.placeId,
				"gdsSize" : opts.gdsSize,
				"tabSize" : opts.tabSize,
				"placeWidth" : opts.placeWidth,
				"placeHeight" : opts.placeHeight,
				"status" : opts.status,
				"callback":function(ads){
					$mainFloorShader.doData(el,ads);
					$mainFloorShader.bindTabChange(el);
					$mainFloorShader.bindShowItem(el);
				}
			});
		},
		"doGdsList":function(_tabId,_obj,dataList,el){
   
			var $contain = _obj.find("[tabid="+_tabId+"]");
			var str ="<ul class='clearfix book-list'>";//商品渲染后字符串
			if(_tabId != 'error'){//正常的tab  包括无tab页     _tabId ->  'notab'
				//清空内容
				$contain.empty();
				if(dataList  && dataList.length > 0){//有数据
					//拼接列表
					var gdsdesc = null;
					var gdsdescValue ="";
					var allPropMaps =null;
					var propObj = null;
					$.each(dataList, function(i, n) {
						//处理
						if(!(n.mainPic) ){
							n.mainPic = {};
						}
						if(!(n.skuInfo) ){
							n.skuInfo = {};
						}
						//获取内容简介
						gdsdesc = null;
					    propObj = null;
					    gdsdescValue = '';
					    allPropMaps = n.skuInfo.allPropMaps;
						if(allPropMaps && $.isPlainObject(allPropMaps)){
						    propObj = allPropMaps['1020'];
							if(propObj){
								gdsdesc = propObj.values;
								if(gdsdesc && gdsdesc.length > 0){
								  gdsdescValue = gdsdesc[0].propValue;
								}
							}
						}
						
						str += "<li>";
						str += "<div class='book-img'>";
						str += "<a  "+$mainFloorShader.getTarget(n.url)+" href='"+$mainFloorShader.getHref(n.url)+"'> <img src='"+( n.mainPic.url ? n.mainPic.url : '' )+"' alt='"+(n.gdsName ? n.gdsName : '')+"' /></a>";
						str += "</div>";
						str += "<div title='"+(n.gdsName ? n.gdsName : '')+"' class='book-name'>";
						str += "<a  "+$mainFloorShader.getTarget(n.url)+" href='"+$mainFloorShader.getHref(n.url)+"'>"+(n.gdsName ? n.gdsName : '')+"</a>";
						str += "</div>";
						str += "<div title='"+(gdsdescValue?gdsdescValue:"")+"' class='book-dec'>"+(gdsdescValue?gdsdescValue:"")+"</div>";
						str += "</li>";
					});
					str += "</ul>"
					$contain.append(str);
				}else{//没有数据
					$contain.append("<div class ='pro-empty'>亲，暂无数据！</div>");
				}
			}
			
		},
		"doTabList":function(_floorTabs,_gdsInfo,respDTO,tabsList){
			//拼接tab 以及tab数据div
			var gdsInfoDivs = "";
			if(!(tabsList && tabsList.length)){//没有tab页
				gdsInfoDivs += "<div  class='item ecp-gds-info-tab' tabId = 'notab'></div>";
			}else{//有tab页
				if(_floorTabs){
					_floorTabs.empty();
					
					var strTabs = "<ul class=\"tabn book-nav\">";
					$.each(tabsList, function(i, n) {
						if(i == 0){
							strTabs += "<li tabId='"+(n.id?n.id:'error')+"' class=\"active\" tabdata = \"already\"><a "+$mainFloorShader.getTarget(n.linkUrl)+" href='"+$mainFloorShader.getHref(n.linkUrl)+"'  >"+n.tabName+"<i class=\"l-icon\"></i></a>";
							strTabs += "</li>";
						}else{
							strTabs += "<li  tabId='"+(n.id?n.id:'error')+"' tabdata=''>";
							strTabs += "<a  "+$mainFloorShader.getTarget(n.linkUrl)+" href='"+$mainFloorShader.getHref(n.linkUrl)+"' >"+n.tabName+"<i class=\"l-icon\"></i></a>";
							strTabs += "</li>";
						}
						if(n.id){
							if(i == 0){
								gdsInfoDivs += "<div  class='item ecp-gds-info-tab' tabId = '"+n.id+"'><div class='loading-small'></div></div>";	
							}else{
								gdsInfoDivs += "<div  class='item ecp-gds-info-tab' style='display:none;' tabId = '"+n.id+"'><div class='loading-small'></div></div>";
							}
							
						}
						
					});//end of each
					strTabs += "</ul>";
					_floorTabs.empty();
					_floorTabs.append(strTabs);
					}
			}
			//添加错误tab的div
			gdsInfoDivs +="<div  class='item ecp-gds-info-tab pro-empty' style='display:none;' tabId = 'error' >亲，数据被搞丢了！</div>";
			
			//添加数据
			_gdsInfo.empty();
			_gdsInfo.append(gdsInfoDivs);
			_floorTabs=null;
			_gdsInfo=null;
			
		},
		"getHref":function(href){
			if(href){
				return href;
			}
			return 'javascript:void(0)';
		},
		"getTarget":function(target){
			if(target){
				return ' target="_blank" ';
			}
			return '';
		},
		"bindShowItem" : function(el){
			$('.new-c-list .n-item',el).live('mouseover', function () {
                $(this).addClass('active').siblings().removeClass('active');
            }).live('mouseleave', function (e) {
                if($(e.relatedTarget).parents('.new-c-list').size()>0){
                    $(this).removeClass('active');
                }
            });
		},
		"queryGdsByTabId":function(el,opts,tabId){
			$.eAjax({
				url : $webroot + '/main/queryGdsByTabId',
				data : {
					"tabId" : tabId,
					"gdsSize" : opts.gdsSize,
					"tabSize" : opts.tabSize,
					"placeWidth" : opts.placeWidth,
					"placeHeight" : opts.placeHeight,
					"status" : opts.status
				},
				async : true,
				type : "post",
				dataType : "json",
				success : function(datas, textStatus) {
					var dataList = null;
					if(datas && $.isPlainObject(datas)){
						dataList = datas.gdsList;
					}
					$mainFloorShader.doGdsList(tabId,$(".gds-info",el),dataList,el);
				},
				error :  function(){
					$mainFloorShader.doGdsList(tabId,$(".gds-info",el),null,el);
				}
			});
		},
		"bindTabChange":function(el){
			//绑定tab页切换
			$(".floor-tabs li",el).live("mouseover",function(e){
				
				//隐藏所有tab页
				$(".floor-tabs li",el).removeClass("active");
				//隐藏所有gds-info div
				$(".ecp-gds-info-tab",el).attr("style","display:none;");
				//获取当前tab
				var $thisTab = $(this);
				//获取参数
				var opts = $(el).data();
				var tabId =$thisTab.attr("tabId");
				opts.tabId = tabId;
				//获取当前tab 的 gds-info div
				var $thisGdsDiv = $(".gds-info",el).find("[tabid="+tabId+"]");
				
				$thisTab.addClass("active");
				$thisGdsDiv.attr("style","");
				
				var tabdata = $thisTab.attr("tabdata")
				if (tabdata != "already") {
					$thisTab.attr("tabdata", "already");
					$mainFloorShader.queryGdsByTabId(el, opts,tabId);
				}
			});
		}

	};

	$.fn.main_floor_shader = function() {
		return this.each(function() {
			var status = $(this).data("comStatus");
			if(!status || status == "" || status =="undefined" ||status==undefined || status == "init"){

			} else {
				return $(this);
			}
			$(this).data("comStatus","do");
			var opts = $.ecpPlugin.parseOptions(this, defaultOpts);
			if(opts == null || opts.placeId =="" || opts.placeId=="undefined"){
				return ;
			}
			$mainFloorShader.control(this,opts);
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
