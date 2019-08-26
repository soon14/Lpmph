;
(function(factory) {
	if (typeof define === "function" && define.amd) {
		// AMD模式
		define(['ecp-component/do-data'], function(floorData) {
			factory(jQuery);
			// /返回对象，要求，其中必须要有一个 componentInit的方法；
			return {
				componentInit : function() {
					$(".homepage-strong-rcmd").homepage_strong_rcmd();
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

	var $homepage_strong_rcmd = {
		/**
		 * 生成重磅推荐  商品信息；
		 * @param el
		 * @param images
		 * @author jiangzh
		 */
		"doData" : function(el, datas) {
			var $gdsdiv = $(".homepage-floor-gds", el);
			if(datas && $.isPlainObject(datas)){
				//1 获取楼层数据，填充楼层名称
				var floorRespDTO =datas.floorRespDTO;
				var $tabdiv = $(".homepage-floor-tab", el);
				if(floorRespDTO && floorRespDTO.id){
					var $floorName = $(".floor-name", el);
					$floorName.find("span").text(floorRespDTO.floorName);
					$floorName.removeClass("hide");
					$floorName.attr('id','floor-'+floorRespDTO.id);
					
					//2 获取楼层页签数据，填充楼层页签
					var floorTabList =datas.floorTabList;
					var gdsList = datas.gdsList;
					if(floorTabList && floorTabList.length >0){
						$homepage_strong_rcmd.doTabList($tabdiv,$gdsdiv,floorTabList);
					}
					
					//3 获取楼层商品数据，填充商品数据
					$homepage_strong_rcmd.doGdsList($gdsdiv,gdsList,0);
				}else{//没有楼层的处理
					$gdsdiv.empty();
					$gdsdiv.append("<div class ='pro-empty'>亲，这家伙太懒，暂未配置数据！</div>");
				}
				
			}else{//错误数据
				$gdsdiv.empty();
				$gdsdiv.append("<div class ='pro-empty'>亲，出错啦！</div>");
			}
		},
		/**
		 * 调用获取数据组件
		 */
		"control":function(el,opts){
			var $container = $(".homepage-floor-gds", el);
		    	$container.empty();
			    $homepage_strong_rcmd.loadContanier($container,-1);
			$floorGdsData.getData({
				"placeId" : opts.placeId,
				"gdsSize" : opts.gdsSize,
				"tabSize" : opts.tabSize,
				"placeWidth" : opts.placeWidth,
				"placeHeight" : opts.placeHeight,
				"status" : opts.status,
				"callback":function(gds){
					$homepage_strong_rcmd.doData(el,gds);
					$homepage_strong_rcmd.readPdf(el);
				}
			});
		},
		"doGdsList":function(_obj,dataList2,index){
			
            var _clist=_obj.children().eq(index);
            _clist.empty();
            	
            if(dataList2 && dataList2.length > 0){//有数据
            	var dataList=U.dealData(dataList2);
				//	_obj.empty();
				var str = "";
				$.each(dataList, function(i, n) {
					//获取作者属性
					var authorValue = doGdsProp(n,"1001");
					//在线试读pdf
					var readPDF = doGdsProp(n,"1026");
					//是否支持在线试读
					var ifReadPDF = doGdsProp(n,"1031");
					var introduction =  doGdsProp(n,"1020");//内容简介
					if(!introduction){
						introduction = n.gdsDesc;
					}
					authorValue=authorValue?(authorValue+" 著"):'';
					if(i+1 == dataList.length){
						str += "<li>";
					}else{
						str += "<li class=\"recon-right-border\">";
					}
					str += "<div class=\"img-p\"><a "+$homepage_strong_rcmd.getTarget(n.url)+" href =\""+(n.url?(GLOBAL.WEBROOT+n.url):'javascript:void(0)')+"\"><img src=\""+n.mainPic.url+"\"></a>";
					str += "<p class=\"img-p-top\">";
					str += "<span class=\"price-z\">"+(n.skuInfo.discountPrice?("&yen;"+ebcUtils.numFormat(accDiv(n.skuInfo.discountPrice,100),2)):'')+"</span><span class=\"price-l\">"+(n.guidePrice?("&yen;"+ebcUtils.numFormat(accDiv(n.guidePrice,100),2)):'')+"</span>";
					str += "</p></div>";
					str += "<div class=\"recon-about\">";
					str += "<h3><a "+$homepage_strong_rcmd.getTarget(n.url)+" href=\""+(n.url?(GLOBAL.WEBROOT+n.url):'javascript:void(0)')+"\" "+(n.gdsName?(" title=\""+n.gdsName+"\""):"")+"> "+(n.gdsName?cmsDoSubString(n.gdsName,24):"")+"</a></h3>";	
					str += "<div class=\"star-box\">";
					str += "<span class=\"grade-star\">"; 
	                str += "<a href=\"javascript:void(0)\" class=\"star5 active\"></a>";
                    str += "</span>";
                    //str += "<span>（67）</span>";
					str += "</div>";
					str += "<p class=\"author\">"+authorValue+"</p>";
					str += "<p class=\"about\""+(introduction?("title = \""+introduction+"\""):"")+">"+(introduction?cmsDoSubString(introduction,120):"")+"</p>";
                    
                    var buttonType = " read-pdf ";
                    //var buttonType = " recom-btn-red";//修改春节版进行样式调整
                    if(!readPDF /*|| n.gdsStatus != '11' || ifReadPDF != '是'*/){//不支持在线阅读
                    	buttonType = " mbtn-disable ";
                    }
                    str += "<div><button class=\"recom-btn "+buttonType+"\" data-read-pdf =\""+(readPDF?readPDF:'')+"\">立即阅读</button></div>";
                    str += "</div>";
					str += "</li>";
				});
				_clist.append(str);
					
            }else{//没有数据
            	_clist.append("<div class ='pro-empty'>亲，暂无数据！</div></div>");
            }
			_obj == null;
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
		"doTabList":function(_obj,_gdsdiv,dataList){
			var str = "<ul class=\"nav-two homepage-floor-ui-tab\">";
			
			var strc="";
			$.each(dataList, function(i, n) {
				str += "<li>";
				str += "<a "+$homepage_strong_rcmd.getTarget(n.linkUrl)+" href='"+$homepage_strong_rcmd.getHref(n.linkUrl)+"' tabId="+n.id; 
				strc +='<ul ';
				if(i == 0){
					str +=" class='active'";
				}
				if(i > 0){
					strc+= 'class="hide"';
				}
				str +=">"+n.tabName+"</a></li>";
				strc +='><div class="loading"></div></ul>';
			});
			str += "</ul>";
			_obj.append(str);
			//tab对应的内容
			_gdsdiv.empty().append(strc);
			//第一次加载标志
			_obj.find('.homepage-floor-ui-tab').children().eq(0).data('load',true);
		},
		"loadContanier":function($container,index){
			if(index==-1){
				$container.append('<ul><div class="loading"></div></ul>');
				index=0;
			}else{
				$container.children().hide().eq(index).show();
			}
	
		},
		"readPdf" : function(el){
			$(".read-pdf",el).live("click",function(e){
        		bDialog.open({
                    title : '试读章节',
                    width : '70%',
                    height : '85%',
                    params:{
                    	pdfUUID : $(this).attr("data-read-pdf")
                    		},
                    url : GLOBAL.WEBROOT+"/gdsdetail/readpdf",
                    callback:function(data){
                    	
                    }
                });
        	});

		},
		"queryGdsByTabId":function(el,opts,index){
			 var $container = $(".homepage-floor-gds",el);
			 $homepage_strong_rcmd.loadContanier($container,index);
		    	$.eAjax({
				url : $webroot + '/homepage/queryGdsByTabId',
				data : {
					"tabId" : opts.tabId,
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
					if(datas && $.isPlainObject(datas)){
						$homepage_strong_rcmd.doGdsList($container,datas.gdsList,index);
					}
				}
			});
		},
		"bindTabChange":function(el){
			//绑定tab页切换
			$(".homepage-floor-tab a",el).live("mouseover",function(e){
				//获取楼层商品数据
				$(".homepage-floor-tab a",el).removeClass("active");
				
				$(this).addClass("active");
			
				var opts = $(el).data();
				var tabId =$(this).attr("tabId");
				opts.tabId = tabId;
				var $index=$(this).parent().index();
				if(!$(this).parent().data('load')){
					$(this).parent().data('load',true);
					$homepage_strong_rcmd.queryGdsByTabId(el,opts,$index);
				}else{
					$(".homepage-floor-gds",el).children().hide().eq($index).show();
				}
	
		
			});
		}
	};

	$.fn.homepage_strong_rcmd = function() {
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
			$homepage_strong_rcmd.control(this,opts);
			
			$(this).data("comStatus","end");
			return $(this);
		});
	};
}));
