/**
 * gxq
 */
$(function(){
	//页面业务逻辑处理内容
    var pageInit = function(){
        var init = function(){
           var _FLAG = "1";
        	//简介
        	var getCkeditValue = function(obj,url){
        		if(url && url != ""){
	        		$.ajax({
	    				url : url,
	    				async : true,
	    				dataType : 'jsonp',
	    				jsonp : 'jsonpCallback',// 注意此处写死jsonCallback
	    				success : function(data) {
	    					obj.html(data.result);
	    				}
	    			 });
        		}
        	};
        	if($("#gdsDesc").val()!=""){
        		$("#gdsDetailDescHead").html("产品详情");
        		$("#gdsDetailDesc").html('');
        		getCkeditValue($("#gdsDetailDesc"),$("#gdsDesc").val());
        	}
//        	getCkeditValue($("gdsDetailPartlist"),$("#gdsPartlist").val());
        	$(".moreEditorParse").each(function(){
        		var _this = $(this);
        		getCkeditValue(_this,_this.attr('value'));
        	});
        	//获取分类路径=======start.
        	$root_nav = $("#root-nav");
        	$.eAjax({
    			url : GLOBAL.WEBROOT + '/gdsdetail/querycatgcodelist',
    			data : {
    				"mainCatgs" : $("#mainCatgs").val()
    			},
    			async : true,
    			type : "post",
    			dataType : "json",
    			success : function(datas) {
    				$root_nav.html('');
    				if(datas.digitsBook==true){
    					//是数字教材
    					$("#gdsName-extends-desc").text('（数字教材）');
    				}
    				if(datas.ebook==true){
    					//是电子书
    					$("#gdsName-extends-desc").text('（电子书）');
    				}
    				if($("#offerReadOnline").val()=='1'&&datas.ifReadOnline=='1'){
    					$("#shareMoreFunction").append("<a href='javascript:void(0);' id='readOnline'><i class='micon micon-page'></i>在线试读</a>");
    				}
    				var HTML = "<ul class='breadcrumb'><li><a href='"+GLOBAL.WEBROOT+"/homepage'>首页</a></li>";
    				var cateroot = "<ul class='breadcrumb' style='padding: 0px 0px;background-color: #fff;color:#0043c5'>";
    				$.each(datas.list, function(i, n) {
    					HTML +="<li>" +
    							" <span class='divider'>></span>" +
    							"<a href='"+GLOBAL.WEBROOT+"/search?category="+n.catgCode+"'>"+n.catgName+"</a> " +
    							"</li>";
    					cateroot += "<li>" ;
    					if(i == 0){
    					}else{
    						cateroot += " <span class='divider' style='color:#0043c5'>></span>";
    					}
    					cateroot += "<a style='color:#0043c5' href='"+GLOBAL.WEBROOT+"/search?category="+n.catgCode+"'>"+n.catgName+"</a> " +
						"</li>";
    				});
    				HTML += "</ul>";
    				$("#cate-root-list").html(cateroot);
    				$root_nav.html(HTML);
    			}
    		});
        	//获取分类路径=======end
        	//获取店铺信息
        	$.eAjax({
    			url : GLOBAL.WEBROOT + '/shopdetail/queryShopInfo',
    			data : {
    				"id" : $("#shopId").val()
    			},
    			async : true,
    			type : "post",
    			dataType : "json",
    			success : function(shopInfo) {
    				$("#shopLogo").attr("src",shopInfo.logoPathURL);
    				$("#gdsDetailShopFullName").html(shopInfo.shopName);
    			}
    		});
        	//显示关注人数
        	$.eAjax({
    			url : GLOBAL.WEBROOT + '/shopdetail/countCollectByShopId',
    			data : {
    				"id" : $("#shopId").val()
    			},
    			async : true,
    			type : "post",
    			dataType : "json",
    			success : function(count) {
    				$("#gdsDetailCollectCount").html(count);
    			}
    		});
        	//显示该店铺有多少商品
        	$.eAjax({
    			url : GLOBAL.WEBROOT + '/shopdetail/countGdsNum',
    			data : {
    				"id" : $("#shopId").val()
    			},
    			async : true,
    			type : "post",
    			dataType : "json",
    			success : function(gdsNum) {
    				$("#gdsDetailGdsNum").html(gdsNum);
    			}
    		});
        	//显示该店铺好评率
        	$.eAjax({
    			url : GLOBAL.WEBROOT + '/shopdetail/calGoodEvalRate',
    			data : {
    				"id" : $("#shopId").val()
    			},
    			async : true,
    			type : "post",
    			dataType : "json",
    			success : function(evalRate) {
    				$("#gdsDetailEvalRate").html(evalRate+"%");
    			}
    		});
        	//根据标准的ISBN获取商品，判断除了当前商品外是否还有电子书或者纸质书=====start
        	var querygdsbyisbn = function(){
        		$.eAjax({
	    			url : GLOBAL.WEBROOT + '/gdsdetail/querygdsbyisbn',
	    			data : {
	    				"biazhunisbn" : $("#BIAOZHUNISBN").val(),
	    				"catgCode" : $("#mainCatgs").val(),
	    				"skuId" : $("#skuId").val()
	    			},
	    			async : true,
	    			type : "post",
	    			dataType : "json",
	    			success : function(datas) {
	    				if(datas.list.length<=1){
	    					return;
	    				}
	    				var $buyWayForAlone = $("#buyWayForAlone");
	    				var HTML = "<label>选择版本</label>";
	    				$.each(datas.list, function(i, n) {
	    					HTML +="<div class='check-switch'>";
	    					if(n.checked=="1"){
	    						HTML += "<label class='selected'>";
	    					}else{
	    						HTML += "<label>";
	    					}
	    					HTML +="<input type='checkbox' name='buyWayForAlone' catgCode='"+n.catgCode+"' gdsId='"+n.gdsId+"' skuId='"+n.skuId+"'/>"+
	    					""+n.catgName+""+
	    					"</label>"+
	    					"</div>";
	    				});
	    				$buyWayForAlone.html(HTML);
	    				$("input[name='buyWayForAlone']").each(function(){
	    	    			var $this = $(this);
	    	    			$this.click(function(){
	    	    				window.location.href=GLOBAL.WEBROOT+"/gdsdetail/"+$this.attr('gdsId')+"-"+$this.attr('skuId');
	    	    			});
	    	    		});
	    			}
        		});
        	};
        	//根据标准的ISBN获取商品，判断除了当前商品外是否还有电子书或者纸质书=====end
        	if( $("#BIAOZHUNISBN").val()!="" && $("#gdsStatus").val()=='11'){
        		querygdsbyisbn();
        	}
        	
        	//获取销售列表=======start
        	var querySaleList = function(){
        		var $container = $("#saleList");
            	$.eAjax({
        			url : GLOBAL.WEBROOT + '/gdsdetail/querysalelist',
        			data : {
        				"gdsId" : $("#gdsId").val(),
        				"shopId" : $("#shopId").val(),
        				"skuId" : $("#skuId").val(),
        				"realPrice" : $("#price").val(),
        				"discountPrice" : $("#discountPrice").val(),
        				"gdsName" : $("#gdsName").val(),
        				"shopName" : $("#shopName").val()
        			},
        			async : true,
        			type : "post",
        			dataType : "json",
        			success : function(datas) {
        				$container.html('');
        				var _count = 0;
        				var htmlContext = "<label>促销信息</label><div class='promotion-ground'>";
        				
        			    var seckillStart = false;
        			    var existSeckill = false;
        				
        				// 处理秒杀时间倒数计时开始.
        				if(datas.saleList != null && datas.saleList.promList != null){
        					
        					var promList = datas.saleList.promList;
        					var seckill = datas.saleList.seckill;
        					var seckillProm = null;
        					
        					if(null != seckill){
        						seckillProm = seckill.seckillProm;
        					}
        					var seckillCountDown = $('#seckill-countDown');
        					
        					if(seckill !=null && seckill.exist==true && seckill.start==true){
        						existSeckill = true;
        						$('#promTitle').html(seckillProm.promTypeName);
        						$('#prompt').html('距结束');
        						seckillStart = true;
        						countDown(seckill.seckillProm.endTime,"#time-box",datas.sysdate);
        						seckillCountDown.show();
        					}else if(seckill !=null && seckill.exist==true && seckill.start==false ){
        						existSeckill = true;
        						$('#promTitle').html(seckillProm.promTypeName);
        						$('#prompt').html('距开始');
        						countDown(seckill.seckillProm.startTime,"#time-box",datas.sysdate);
        						seckillCountDown.show();
        					}
        					
        					if(!seckillStart){
        						seckillCountDown.css('background-color','#31c99a');
        					}
        					// 处理秒杀倒数计时结束.
        					if(existSeckill){
        						htmlContext = "<label>促销信息</label><div class='promotion-ground seckill-info'>";
        					}
        					
        					if(promList.length>3){
        						htmlContext += "<a href='javascript:;' class='prom-more'>更多+</a>";
        					}
	        				$.each(promList, function(i, n) {
	        					if(i==0){
	        						if(n.promSkuPriceRespDTO && n.promSkuPriceRespDTO !=null){
	        							var beforePrice=$("#realPrice").html().replace(/[^0-9]/ig, ""); 
	        							var flag=true;
	        							if(beforePrice+"" == n.promSkuPriceRespDTO.discountFinalPrice+""){
	        								flag=false;
	        							}
	        							if(n.promSkuPriceRespDTO.discountFinalPrice+"" !="" && n.promSkuPriceRespDTO.discountFinalPrice != null && flag){
	        								$("#realPrice").html("&yen;"+parseMoney(n.promSkuPriceRespDTO.discountFinalPrice));
	        							}
	        							if(n.promSkuPriceRespDTO.discountCaclPrice+"" !="" && n.promSkuPriceRespDTO.discountCaclPrice != null && flag){
	        								$("#guidePrice").html("&yen;"+parseMoney(n.promSkuPriceRespDTO.discountCaclPrice));
	        							}
	        						}
	        					}
	        					_count ++;
	        					if(i<=2){
	        						htmlContext += "<div class='prom-item'>";
	        					}else{
	        						htmlContext +="<div class='prom-item hide'>";
	        					}
	        					
	        					// css样式类.
	        					var spanCls = "";
	        					
	        					if(existSeckill){
	        						spanCls = "active-info active-info-color";
	        						if(i == 0 && seckillStart){
		        						spanCls = "active-info ";
		        					}
	        					}else{
	        						spanCls = "promotion";
	        					}
	        					
	        					
	        					
	        					
	        					
	        					htmlContext += "<span class='"+spanCls+"'>"+n.promInfoDTO.promTypeName+"</span>"+
	                                "<span class='sitxt'>"+n.promInfoDTO.promTheme+"</span>"+
	                                "<a href='#' class='promotion-a'> 详情>></a>"+
	                                "<div class='prom-c'>"+
	                                    ""+n.promInfoDTO.promContent+""+
	                                "</div>"+
	                            "</div>";
	        					
	        					
	        					
	        				});
        				}
        				htmlContext += "</div>";
        				if(_count >0){
        					$container.html(htmlContext);
            				//促销信息绑定事件
            	        	$('.promotion-a').click(function(e){
            	                var prc= $(this).parent().find('.prom-c');
            	                if(prc.css('display')=='none'){
            	                    $(this).text('收起');
            	                }else{
            	                    $(this).text('详情>>');
            	                }
            	                prc.toggle();
            	                e.preventDefault();
            	            });
            	           $('.prom-more').click(function(e){
            	               var prom=$(this).parents('.promotion-ground').find('.prom-item:gt(2)');
            	               if(prom.eq(0).css('display')=='none'){
            	                   $(this).text('更多 -');
            	               }else{
            	                   $(this).text('更多+');
            	               }
            	               prom.toggle();
            	               e.preventDefault();
            	           });
        				}
        			}
        		});
        	};
        	if($("#gdsStatus").val()=='11'){
        		querySaleList();
        	}
        	
        	//获取销售列表=======end
            /*产品详情*/
            U.tab('.pro-info .tabn','.pro-info .tabc',{
                defItem:0
            });
            /*推荐*/
//            U.tab('.pro-tjs .tabn','.pro-tjs .tabc');
            $(".pro-tjs .tabn li").live("mouseover",function(e){
				$(".pro-tjs .tabn li").removeClass("active");
				$(".pro-tjs .citem").removeClass("active");
				var $this = $(this);
				var hoverId = $this.children("a").attr('value');
				$this.addClass("active");
				$("#"+hoverId).addClass("active");
            });
        	$("input[name='skuprop']").each(function(){
        		$this = $(this);
        		$this.bind('click',function(){
        			_FLAG = '0';
        			var skuParam = "[";
        			$("input[name='skuprop']").each(function(){
        				$obj = $(this);
        				if($obj.parent().hasClass('selected')){
        					var propId = $obj.attr('propId');
                			var propName = $obj.attr('propName');
                			var value = $obj.attr('value');
                			var valueId = $obj.attr('valueId');
                			skuParam = skuParam + "{propId:'"+propId+"',propName:'"+propName+"',value:'"+value+"',valueId:'"+valueId+"'},";
        				}
        			});
        			skuParam += "]";
        			var param = {};
        			param.skuPropParam = skuParam;
        			param.gdsId = $("#gdsId").val();
        			param.shopId = $("#shopId").val();
//        			$.gridLoading({"messsage":"正在保存中...."});
        			$.eAjax({
        				url : GLOBAL.WEBROOT + "/gdsdetail/getskudetailinfo",
        				data : param,
        				success : function(returnInfo) {
        					_FLAG = '1';
        					var _this = $(".gdsPictrueSlide");
        					_this.attr('data-sku-id',returnInfo.respDto.skuInfo.id);
        					var _realAmount = returnInfo.respDto.skuInfo.realAmount;
        					$("#realPrice").html("&yen;"+parseMoney(returnInfo.respDto.skuInfo.discountPrice));
        					$("#guidePrice").html("&yen;"+parseMoney(returnInfo.respDto.skuInfo.guidePrice));
        					$("#price").val(returnInfo.respDto.skuInfo.realPrice);
        					$("#discountPrice").val(returnInfo.respDto.skuInfo.discountPrice);
        					$("#gdsStatus").val(returnInfo.respDto.skuInfo.gdsStatus);
        					$("#realAmount").html(_realAmount);
        					$("#skuId").val(returnInfo.respDto.skuInfo.id);
        					$("#appSpecPrice").html("&yen;"+parseMoney(returnInfo.respDto.skuInfo.appSpecPrice));
        					var $purchaseAmount = $("#purchaseAmount");
        	        		if($purchaseAmount.attr("gdsTypeId")=="2"){
        	        			purchaseAmount = $purchaseAmount.text();
        	        		}else{
        	        			$("#purchaseAmount").attr('max',_realAmount);
            					$("#purchaseAmount").val('1');
        	        		}
        					$("#skuProps").val(returnInfo.respDto.skuInfo.skuProps);
        					var str = "";
        					var _span = $("span[name='addToCart']");
        					var _page = $("span[name='addPageToCart']");
        					var _ifPage = $("#ifPage").val();
        					
        					if(returnInfo.respDto.gdsTypeRespDTO.ifNeedstock=="1"){//需要库存
	        					if(returnInfo.stockStatus == '00'){
	        						str = "缺货";
	        						if(_ifPage=='1' && Number(returnInfo.digitsPrinPrice)>0){
	        							$("#realAmount").text('999999999');
	            						$("#purchaseAmount").attr('max','999999999');
	        						}else{
	        							$("#realAmount").text(_realAmount);
	            						$("#purchaseAmount").attr('max',_realAmount);
	        						}
	        						if(_span.hasClass('mbtn-orange')){
	        							_span.attr("disabled",'true');
	        							_span.removeClass('mbtn-orange');
	        							_span.addClass('mbtn-disable');
	        						}
	        						if(_page.hasClass('mbtn-disable')){
	        							_page.removeAttr("disabled");
	        							_page.addClass('mbtn-orange');
	        							_page.removeClass('mbtn-disable');
	        							if(Number(returnInfo.digitsPrinPrice)>0){
	        								$("#realPrice").html("&yen;"+returnInfo.digitsPrinPrice);
	        							}
	        						}
	        					}else if(returnInfo.stockStatus == '01'){
	        						str = "紧张";
	        						if(_span.hasClass('mbtn-disable')){
	        							_span.removeAttr('disabled');
	        							_span.removeClass('mbtn-disable');
	        							_span.addClass('mbtn-orange');
	        						}
	        						if(_page.hasClass('mbtn-orange')){
	        							_page.attr('disabled','true');
	        							_page.addClass('mbtn-disable');
	        							_page.removeClass('mbtn-orange');
	        						}
	        					}else if(returnInfo.stockStatus == '02'){
	        						str = "充足";
	        						if(_span.hasClass('mbtn-disable')){
	        							_span.removeAttr('disabled');
	        							_span.removeClass('mbtn-disable');
	        							_span.addClass('mbtn-orange');
	        						}
	        						if(_page.hasClass('mbtn-orange')){
	        							_page.attr('disabled','true');
	        							_page.addClass('mbtn-disable');
	        							_page.removeClass('mbtn-orange');
	        						}
	        					}
        					}else{
        						str = "充足";
        						$("#realAmount").text('999999999');
        						$("#purchaseAmount").attr('max','999999999');
        					}
        					$("#showStockStatus").text(str);
        					
        					$gdsPictrueData.getData({
        						"pictrueNum" : _this.attr('data-pictrue-num'),
        						"pictrueHeight" : _this.attr('data-pictrue-height'),
        						"pictrueWidth" : _this.attr('data-pictrue-width'),
        						"pictrueMoreHeight" : _this.attr('data-pictrue-more-height'),
        						"pictrueMoreWidth" : _this.attr('data-pictrue-more-width'),
        						"gdsId" : _this.attr('data-gds-id'),
        						"skuId" : returnInfo.respDto.skuInfo.id,
        						"callback":function(ads){
        							$gdsPictrue.getData(_this,ads);
        						}
        					});
        					querySaleList();
        					queryautocombine();
//        					$.gridUnLoading();
        				},
        				exception : function(){
//        					$.gridUnLoading();
        				}
        			});
        		});
        	});
        	var parseMoney = function(data){
        		var str = (data/100).toFixed(2) + '';
				var intSum = str.substring(0,str.indexOf("."));//取到整数部分.replace( /\B(?=(?:\d{3})+$)/g, ',' )
				var dot = str.substring(str.length,str.indexOf("."));//取到小数部分
				return ret = intSum + dot;
        	};
        	//加入购物车
        	var getaddToCartParam = function(obj,cartType,shopId,ordCartItemList){
        		var param = {
        				cartType : cartType,
        				shopId : shopId,
        				ordCartItemList	: JSON.stringify(ordCartItemList)
        		};
        		$.eAjax({
    				url : GLOBAL.WEBROOT + "/order/cart/add",
    				data : param,
    				plugin : ['ePageTop'],
    				success : function(returnInfo) {
    					if(returnInfo.ecpBaseResponseVO.resultFlag == "ok"){
			              //  var addcar = $(this);
			                var startOffset = $(obj).offset();
			                var endOffset = $("#carGus").offset();
			                var img = $('.pcimg').attr('src');
			                var flyer = $('<img class="u-flyer"  src="'+img+'">');
			                flyer.fly({
								speed : 1.4,
			                    start: {
			                        left: startOffset.left,
			                        top: startOffset.top
			                    },
			                    end: {
			                        left: endOffset.left+50,
			                        top: endOffset.top+10,
			                        width: 0,
			                        height: 0
			                    },
			                    onEnd: function(){
			                        this.destory();
			                    }
			                });
    						$.pageTop.setOrderCount({url:GLOBAL.WEBROOT + '/order/getcartcount'}); 
    						//window.location.href = GLOBAL.WEBROOT+"/order/cart/list";
                        }else if(returnInfo.ecpBaseResponseVO.resultFlag == "fail"){
                        	eDialog.alert(returnInfo.ecpBaseResponseVO.resultMsg);
                        }else{
                        	eDialog.error(returnInfo.ecpBaseResponseVO.resultMsg);
                        }
    					$(obj).bind('click');
    				},
    				exception : function(){
						$(obj).bind('click');
					}
    			});
        	};
        	//加入购物车事件绑定====================start
        	$("#addToCart").live('click',function(){
        		var $this = this;
        		$(this).unbind('click');
        		if(_FLAG == '0'){
        			$(this).bind('click');
        			return;
        		}
        		if($(this).attr('disabled')=='disabled'){
        			$(this).bind('click');
        			return;
        		}
        		var buyFlag = 0;
        		if($("#ifNeedstock").val()=='1'){
        			var STOCK_LACK_THRESHOLD = Number($("#STOCK_LACK_THRESHOLD").val());
            		var purchaseAmount = Number($("#purchaseAmount").val());
            		var max = Number($("#purchaseAmount").attr('max'));
            		var _orderAlert = $("#orderAlert");
            		if(purchaseAmount>(max-STOCK_LACK_THRESHOLD)){
            			_orderAlert.show();
            			_orderAlert.html('购买量已超过实际库存量');
            			$(this).bind('click');
            			return;
            		}else{
            			_orderAlert.hide();
            		}
        		}

				if($("#ifBuyonce").val()=='0'){
        			//虚拟产品，则判断是否已购买
        			$.eAjax({
    					url : GLOBAL.WEBROOT + "/gdsdetail/wetherbuyed",
    					data : {skuId:$("#skuId").val()},
    					async : false,
    					success : function(returnInfo) {
    						if(returnInfo.resultFlag=="ok"){
    							if(returnInfo.buyedFlag==true){
    								//已购买过咯
    								buyFlag = 1;
    							}
    						}
    					}
        			});
        		}
        		if(buyFlag == 1){
        			eDialog.alert("抱歉，该商品只允许购买一次！"); 
        			return;
        		}
        		//判断是是否是数字教材
        		$.eAjax({
					url : GLOBAL.WEBROOT + "/gdsdetail/ifebookordigitsbook",
					data : {mainCatgs:$("#mainCatgs").val()},
					async : false,
					success : function(returnInfo) {
						if (returnInfo.digitsBook == true || returnInfo.ebook == true) {
							eDialog.confirm("您加入购物车的商品是【数字产品-"+returnInfo.catgCodeName+"】", {
			        			buttons : [ {
			        				caption : '确定加入购物车',
			        				callback : function() {
			        	        		//购物车类型
			        	        		var cartType = "01";
			        	        		//店铺id
			        	        		var shopId = $("#shopId").val();
			        	        		//是否数字印刷
			        	        		var prnFlag = '0';
			        	        		var ordCartItemList = new Array();
			        	        		commonPriceAddToCart(prnFlag,cartType,shopId,ordCartItemList);
			        	        		getaddToCartParam($this,cartType,shopId,ordCartItemList);
			        				}
			        			}, {
			        				caption : '返回',
			        				callback : $.noop
			        			} ]
			        		});
						}else{
        	        		//购物车类型
        	        		var cartType = "01";
        	        		//店铺id
        	        		var shopId = $("#shopId").val();
        	        		//是否数字印刷
        	        		var prnFlag = '0';
        	        		var ordCartItemList = new Array();
        	        		commonPriceAddToCart(prnFlag,cartType,shopId,ordCartItemList);
        	        		getaddToCartParam($this,cartType,shopId,ordCartItemList);
						}
					}
				});
        	});
        	
        	$("#addPageToCart").live('click',function(){
        		if(_FLAG == '0'){
        			return;
        		}
        		if($(this).attr('disabled')=='disabled'){
        			return;
        		}
        		//购物车类型
        		var cartType = "01";
        		//店铺id
        		var shopId = $("#shopId").val();
        		var prnFlag = '1';
        		var ordCartItemList = new Array();
        		commonPriceAddToCart(prnFlag,cartType,shopId,ordCartItemList);
        		getaddToCartParam(this,cartType,shopId,ordCartItemList);
        		
        	});
        	
        	//普通价格商品加入购物车
        	var commonPriceAddToCart = function(prnFlag,cartType,shopId,ordCartItemList){
        		//购买数量
        		var purchaseAmount = 0;
        		var $purchaseAmount = $("#purchaseAmount");
        		if($purchaseAmount.attr("gdsTypeId")=="2"){
        			purchaseAmount = $purchaseAmount.text();
        		}else{
        			purchaseAmount = $purchaseAmount.val();
        		}
        		//商品id
        		var gdsId = $("#gdsId").val();
        		//商品名称
        		var gdsName = $("#gdsName").val();
        		//单品id
        		var skuId = $("#skuId").val();
        		//单品属性串
        		var skuProps = $("#skuProps").val();
        		//商品主分类
        		var mainCatgs = $("#mainCatgs").val();
        		//组合类型
        		var groupType = "0";
        		//组合明细 	默认为skuId，组合搭配的话，为主skuId:次skuId:次skuId	1	
        		var groupDetail	= $("#skuId").val();
        		//商品类型id
        		var gdsTypeId = $("#gdsTypeId").val();
        		var ordCartItemListObj = {
        				cartType : cartType,
        				orderAmount : purchaseAmount,
        				shopId : shopId,
        				gdsId : gdsId,
        				gdsName : gdsName,
        				skuId : skuId,
        				mainCatgs : mainCatgs,
        				skuInfo : skuProps,
        				groupDetail : groupDetail,
        				groupType : groupType,
        				promId : "",
        				gdsType : gdsTypeId,
        				prnFlag : prnFlag,
        				scoreTypeId : ''
        		};
        		ordCartItemList.push(ordCartItemListObj);
        	};
        	//阶梯价格商品加入购物车
        	var ladderPriceAddToCart = function(ordCartItemList){
        		
        	};
        	//加入购物车事件绑定end==================
        	
        	
        	//加入购物车减少商品数量
            $("#reduceCount").click(function(){
            	reduceCount('down');
            });
            //加入购物车增加商品数量
            $("#addCount").click(function(){
            	addCount('up');
            });
            var addCount = function(str){
            	var stock = $("#realAmount").text();
            	var value = $("#purchaseAmount").val();
            	var STOCK_LACK_THRESHOLD = $("#STOCK_LACK_THRESHOLD").val();
            	var max = $("#purchaseAmount").attr('max');
            	if(Number(stock)<=Number(STOCK_LACK_THRESHOLD)){
            		return;
            	}
            	if (value != '') {
//            		if(++value>Number(max)-Number(STOCK_LACK_THRESHOLD)){
//            			return;
//            		}
            		if(++value > 9999999){
            			return;
            		}
            		
            		$("#purchaseAmount").val(value);
        			$("#realAmount").text(--stock);
            	} else {
            		$("#purchaseAmount").val(1);
            		$("#realAmount").text(--stock);
            	}
            };
            var reduceCount = function(str){
            	var stock = $("#realAmount").text();
            	var value = $("#purchaseAmount").val();
            	if(Number(value)<=1){
            		return;
            	}
            	if (value != '') {
            		$("#purchaseAmount").val(--value);
        			$("#realAmount").text(++stock);
            	} else {
            		$("#purchaseAmount").val(1);
            		$("#realAmount").text(++stock);
            	}
            };
            $("#purchaseAmount").on('keyup',function(){
            	ladderLimitInputNum(this);
            });
            /**
        	 * 限制输入框输入的数字只能为正整数
        	 * @param obj
        	 */
        	var ladderLimitInputNum = function(obj){
        		var input=$(obj);
        		if($("#gdsTypeId").val()=='2'){
        			input.val(1);
        			return;
        		}
        		limitInputNum(obj);
        		var num=0;
        		var STOCK_LACK_THRESHOLD = $("#STOCK_LACK_THRESHOLD").val();
        		var maxAttr=input.attr("max");
        		var max=0;
        		if(maxAttr){
        			if(Number(maxAttr) > Number(STOCK_LACK_THRESHOLD)){
        				max=Number(maxAttr)- Number(STOCK_LACK_THRESHOLD);
        			}else{
        				max = 1;
        			}
        		}
//        		if(Number(input.val())>max){
//        			input.val(max);
//        			num=max;
//        			$("#realAmount").text(max-num+Number(STOCK_LACK_THRESHOLD));
//        		}else{
//        			num=input.val();
//        			$("#realAmount").text(max-num+Number(STOCK_LACK_THRESHOLD));
//        		}
        		if(Number(input.val())>9999999){
        			input.val(9999999);
        		}
        	};
        	/**
        	 * 限制只能输入正整数
        	 * @param obj
        	 */
        	var limitInputNum = function(obj){
        		var _amount = $(obj).val();
        		_amount=_amount.replace(/[^0-9]/g,'');
        		if(_amount.trim() == '' || _amount=='0'){
        			_amount=1;
        		}
        		$(obj).val(_amount);
        	};
        	/**
        	 * 添加收藏
        	 */
        	var _collect = 0;
        	$("#addCollect").click(function(){
        		if(_collect == 1){
        			return;
        		}
        		_collect = 1;
        		$.eAjax({
        			url : GLOBAL.WEBROOT + "/gdsdetail/add",
        			data : {
        				"skuId" : $("#skuId").val()
        			},
        			success : function(returnInfo) {
        				if (returnInfo.resultFlag == 'ok') {
							eDialog.success("您已成功收藏该商品！");
						} else {
							eDialog.error(returnInfo.resultMsg);
						}
        				_collect = 0;
        			}
        		});
        	});
        	/**
        	 * 添加店铺收藏
        	 */
        	var _shopCollect = 0;
        	$("#addShopCollect").click(function(){
        		if(_shopCollect == 1){
        			return;
        		}
        		_shopCollect = 1;
        		$.eAjax({
        			url : GLOBAL.WEBROOT + "/shopdetail/addShopCollect",
        			data : {
        				"id" : $("#shopId").val()
        			},
        			success : function(returnInfo) {
        				if (returnInfo.resultFlag == 'ok') {
							eDialog.success("您已成功收藏该店铺！");
						} else {
							eDialog.error(returnInfo.resultMsg);
						}
        				_shopCollect = 0;
        			}
        		});
        	});
        	var fixedCombineBuyBtn = function(){
        		//固定搭配套餐组合立即购买按钮事件绑定
	        	$("a[name='fixedCombineBuyBtn']").each(function(){
	        		$(this).click(function(){
	        			var _statusC = 0;
	        			var _notOnsaleInfo = "您好！</br>商品：";
	        			var _this = $(this);
    	        		var _autoObj = _this.parents(".jitem");
    	        		//购物车类型
    	        		var cartType = "01";
    	        		//组合类型，自由购买
    	        		var groupType = "1";
    	        		//购买数量
    	        		var purchaseAmount = 1;
    	        		//商品名称
    	        		var gdsName = _autoObj.find("#fixedFixedGdsName").val();
    	        		//店铺id
    	        		var shopId = _autoObj.find("#fixedFixedShopId").val();
    	        		//店铺名称
//    	        		var shopName = _autoObj.find("#fixedFixedShopName").val();
    	        		//组合明细 	默认为skuId，组合搭配的话，为主skuId:次skuId:次skuId	1	
    	        		var ordCartItemList = new Array();
    	        		//固定商品的单品编码
    	        		var skuId  = _autoObj.find("#fixedFixedSkuId").val();
    	        		var fixedIndex = _autoObj.find("#fixedIndex").val();
    	        		var arr = new Array();
    	        		arr[fixedIndex] = skuId;
    	        		var tempGdsName=gdsName;
    	        		var fixedStatus = _autoObj.find("#fixedGdsStatus").val();
    	        		if(fixedStatus!="11"){
    	        			_statusC ++;
    	        			_notOnsaleInfo += tempGdsName;
    	        		}
    	        		if(_statusC >0){
    	        			eDialog.alert(_notOnsaleInfo+" 卖家还没有上架，您暂时还不能购买该组合！");
    	        			return ;
    	        		}
    	        		_autoObj.find('input[name="fixedCombineCheck"]').each(function(){
    	        			var $this = $(this);
    	        			//单品编码
	    	        		var skuIds  = $this.attr("skuId");
    	    	        	var autoSkuIndex = $this.attr("index");
    	    	        	var gdsStatus = $this.attr("gdsStatus");
    	    	        	var tempGdsName=$this.attr("gdsName");
    	    	        	if(gdsStatus!="11"){
    	    	        		_statusC ++;
    	    	        		_notOnsaleInfo += tempGdsName;
    	    	        	}
    	    	        	arr[autoSkuIndex] = skuIds;
    	        		});
    	        		if(_statusC >0){
    	        			eDialog.alert(_notOnsaleInfo+" 卖家还没有上架，您暂时还不能购买该组合！");
    	        			return ;
    	        		}
    	        		var groupDetail = "";
    	        		for(var i = 0;i < arr.length;i++){
    	        			//组合明细 	默认为skuId，组合搭配的话，为主skuId:次skuId:次skuId	1	
    	        			if(arr[i]!=undefined){
    	        				groupDetail  = groupDetail+":"+arr[i];
    	        			}
    	        		}
    	        		_autoObj.find('input[name="fixedCombineCheck"]').each(function(){
    	        			var $this = $(this);
    	        			//商品id
	    	        		var gdsId = $this.attr("gdsId");
	    	        		//商品名称
	    	        		var gdsName = $this.attr("gdsName");
	    	        		//商品主分类
	    	        		var mainCatgs = $this.attr("mainCatgs");
	    	        		//选择的单品编码
	    	        		var skuId  = $this.attr("skuId");
	    	        		//单品属性串
	    	        		var skuProps = $this.attr("skuProps");
	    	        		//组合明细 	默认为skuId，组合搭配的话，为主skuId:次skuId:次skuId	1	
	    	        		//促销活动ID
	    	        		var promId = $this.attr("promId");
	    	        		//商品类型id
	    	        		var gdsTypeId = $this.attr("gdsTypeId");
	    	        		var ordCartItemListObj = {
	    	        				cartType : cartType,
	    	        				orderAmount : purchaseAmount,
	    	        				shopId : shopId,
	    	        				gdsId : gdsId,
	    	        				gdsName : gdsName,
	    	        				skuId : skuId,
	    	        				mainCatgs : mainCatgs,
	    	        				skuInfo : skuProps,
	    	        				groupType : groupType,
	    	        				groupDetail : groupDetail,
	    	        				promId : promId,
	    	        				gdsType : gdsTypeId,
	    	        				prnFlag : '0',
	    	        				scoreTypeId : ''
	    	        		};
	    	        		ordCartItemList.push(ordCartItemListObj);
    	        		});
    	        		//商品id
    	        		var gdsId = _autoObj.find("#fixedFixedGdsId").val();
    	        		//商品主分类
    	        		var mainCatgs = _autoObj.find("#fixedFixedMainCatgs").val();
    	        		//商品名称
    	        		var gdsName = _autoObj.find("#fixedFixedGdsName").val();
    	        		//单品属性串
    	        		var skuProps = _autoObj.find("#fixedFixedSkuProps").val();
    	        		//促销活动ID
    	        		var promId = _autoObj.find("#fixedFixedPromId").val();
    	        		//商品类型id
    	        		var gdsTypeId = _autoObj.find("#fixedFixedGdsTypeId").val();
    	        		var ordCartItemListObj = {
    	        				cartType : cartType,
    	        				orderAmount : purchaseAmount,
    	        				shopId : shopId,
    	        				gdsId : gdsId,
    	        				gdsName : gdsName,
    	        				skuId : skuId,
    	        				mainCatgs : mainCatgs,
    	        				skuInfo : skuProps,
    	        				groupType : groupType,
    	        				groupDetail : groupDetail,
    	        				promId : promId,
    	        				gdsType : gdsTypeId,
    	        				prnFlag : '0',
    	        				scoreTypeId : ''
    	        		};
    	        		ordCartItemList.push(ordCartItemListObj);
    	        		getaddToCartParam(this,cartType,shopId,ordCartItemList);
	        		});
	        	});
	        	/*自由搭配选择绑定事件================end*/
        	};
        	
        	var autoCombineBuyBtn = function(){
        		//立即购买
	        	$("a[name='autoCombineBuyBtn']").each(function(){
	        		$(this).click(function(){
	        			var _statusC = 0;
	        			var _notOnsaleInfo = "您好！";
	        			var _this = $(this);
    	        		var _autoObj = _this.parent().parent().parent().parent().parent();
    	        		//购物车类型
    	        		var cartType = "01";
    	        		//组合类型，自由购买
    	        		var groupType = "1";
    	        		//购买数量
    	        		var purchaseAmount = 1;
    	        		//固定商品的单品编码
    	        		var skuId  = _autoObj.find("#fixedSkuId").val();
    	        		//店铺id
    	        		var shopId = _autoObj.find("#fixedShopId").val();
    	        		//商品名称
    	        		var gdsName = _autoObj.find("#fixedGdsName").val();
    	        		//店铺名称
//    	        		var shopName = _autoObj.find("#fixedShopName").val();
    	        		//组合明细 	默认为skuId，组合搭配的话，为主skuId:次skuId:次skuId	1	
    	        		var ordCartItemList = new Array();
    	        		var _count = 0;
    	        		//循环里的标识符，用来进行groupDetail的单品排序用（顺序按照后场取回来的数据的顺序不变）
    	        		var fixedIndex = _autoObj.find("#fixedIndex").val();
    	        		var arr = new Array();
    	        		arr[fixedIndex] = skuId;
    	        		var tempGdsName=gdsName;
    	        		var fixedStatus = _autoObj.find("#fixedGdsStatus").val();
    	        		if(fixedStatus!="11"){
    	        			_statusC ++;
    	        			_notOnsaleInfo += tempGdsName;
    	        		}
    	        		if(_statusC >0){
    	        			eDialog.alert(_notOnsaleInfo+" 卖家还没有上架，您暂时还不能购买该组合！");
    	        			return ;
    	        		}
    	        		_autoObj.find('input[name="combineCheck"]').each(function(){
    	        			var $this = $(this);
    	        			if($this.attr('checked')=="checked"){
	    	        			//单品编码
    	    	        		var skuIds  = $this.attr("skuId");
    	    	        		var autoSkuIndex = $this.attr("index");
    	    	        		arr[autoSkuIndex] = skuIds;
    	    	        		var gdsStatus = $this.attr("gdsStatus");
    	    	        		var tempGdsName=$this.attr("gdsName");
        	    	        	if(gdsStatus!="11"){
        	    	        		_statusC ++;
        	    	        		_notOnsaleInfo += tempGdsName;
        	    	        	}
    	        			}
    	        		});
    	        		if(_statusC >0){
    	        			eDialog.alert(_notOnsaleInfo+" 卖家还没有上架，您暂时还不能购买该组合！");
    	        			return ;
    	        		}
    	        		var groupDetail = "";
    	        		for(var i = 0;i < arr.length;i++){
    	        			//组合明细 	默认为skuId，组合搭配的话，为主skuId:次skuId:次skuId	1	
    	        			if(arr[i]!=undefined){
    	        				groupDetail  = groupDetail+":"+arr[i];
    	        			}
    	        		}
    	        		_autoObj.find('input[name="combineCheck"]').each(function(){
    	        			var $this = $(this);
    	        			if($this.attr('checked')=="checked"){
    	        				_count ++;
    	    	        		//商品id
    	    	        		var gdsId = $this.attr("gdsId");
    	    	        		//商品名称
    	    	        		var gdsName = $this.attr("gdsName");
    	    	        		//商品主分类
    	    	        		var mainCatgs = $this.attr("mainCatgs");
    	    	        		//选择的单品编码
    	    	        		var skuId  = $this.attr("skuId");
    	    	        		//单品属性串
    	    	        		var skuProps = $this.attr("skuProps");
    	    	        		
    	    	        		//促销活动ID
    	    	        		var promId = $this.attr("promId");
    	    	        		//商品类型id
    	    	        		var gdsTypeId = $this.attr("gdsTypeId");
    	    	        		var ordCartItemListObj = {
    	    	        				cartType : cartType,
    	    	        				orderAmount : purchaseAmount,
    	    	        				shopId : shopId,
    	    	        			
    	    	        				gdsId : gdsId,
    	    	        				gdsName : gdsName,
    	    	        				skuId : skuId,
    	    	        				mainCatgs : mainCatgs,
    	    	        				skuInfo : skuProps,
    	    	        				groupType : groupType,
    	    	        				groupDetail : groupDetail,
    	    	        				promId : promId,
    	    	        				gdsType : gdsTypeId,
    	    	        				prnFlag : '0',
    	    	        				scoreTypeId : ''
    	    	        		};
    	    	        		ordCartItemList.push(ordCartItemListObj);
    	        			}
    	        		});
    	        		if(_count == 0){
    	        			//eDialog.alert("至少选择一个搭配商品！");
    	        			eDialog.alert("至少要选择一件搭配商品才能参加活动!");
    	        			return ;
    	        		}
    	        		//商品id
    	        		var gdsId = _autoObj.find("#fixedGdsId").val();
    	        		//商品主分类
    	        		var mainCatgs = _autoObj.find("#fixedMainCatgs").val();
    	        		//单品属性串
    	        		var skuProps = _autoObj.find("#fixedSkuProps").val();
    	        		//商品名称
    	        		var gdsName = _autoObj.find("#fixedGdsName").val();
    	        		//促销活动ID
    	        		var promId = _autoObj.find("#fixedPromId").val();
    	        		//商品类型id
    	        		var gdsTypeId = _autoObj.find("#fixedGdsTypeId").val();
    	        		var ordCartItemListObj = {
    	        				cartType : cartType,
    	        				orderAmount : purchaseAmount,
    	        				shopId : shopId,
    	        				gdsId : gdsId,
    	        				gdsName : gdsName,
    	        				skuId : skuId,
    	        				mainCatgs : mainCatgs,
    	        				skuInfo : skuProps,
    	        				groupType : groupType,
    	        				groupDetail : groupDetail,
    	        				promId : promId,
    	        				gdsType : gdsTypeId,
    	        				prnFlag : '0',
    	        				scoreTypeId : ''
    	        		};
    	        		ordCartItemList.push(ordCartItemListObj);
    	        		getaddToCartParam(this,cartType,shopId,ordCartItemList);
	        		});
	        	});
        	};
        	//异步加载自由搭配===========start
        	var queryautocombine = function(){
        		$.eAjax({
        			url : GLOBAL.WEBROOT + "/gdsdetail/queryautocombine",
        			data : {
        				"gdsId" : $("#gdsId").val(),
        				"shopId" : $("#shopId").val(),
        				"skuId" : $("#skuId").val()
        			},
        			dataType : "html",
        			success : function(returnInfo) {
        				$("#auto-combine").empty();
        				$("#auto-combine").html(returnInfo);
        				/*组合*/
        	            U.tab('.pro-group .tabn','.pro-group .tabc');


        	            /*自由组合*/
        	            U.tab('#zyTeam .jn','#zyTeam .jc');

        	            /*组合推荐*/
        	            U.tab('.pro-teamj .jn','.pro-teamj .jc');
        	            var _autoLengt = $(".slide-trigger").length;
        	            for(var i = 1;i<_autoLengt + 1;i++){
        	            	$("#jcarouseln-"+i+" a").powerSwitch({
            	                number: 4,
            	                container: $("#jcarouselc-"+i+"")
            	            });
        	            }
        				/*自由搭配选择绑定事件================start*/
        	        	$("input[name='combineCheck']").each(function(){
        	        		var _this = $(this);
        	        		_this.live('click',function(){
        	        			var _countPriceAfter = 0;
        	    				var _countPrice = 0;
        	    				var _object = _this.parent().parent().parent().parent().parent().parent();
        	    				_countPriceAfter = Number(_object.find('#fixedGdsPriceAfter').text());
        						_countPrice = Number(_object.find('#fixedGdsPrice').text());
        						_object.find("input[name='combineCheck']").each(function(){
        	    					if($(this).attr('checked')=="checked"){
        	    						var _this2 = $(this).parent();
        	    						var _1 = Number(_this2.find('font[name="combinePriceAfter"]').text());
        	    						var _2 = Number(_this2.find('font[name="combinePrice"]').text());
        	        					_countPriceAfter = Number((_countPriceAfter + _1).toFixed(2));
        	        					_countPrice =  Number((_countPrice + _2).toFixed(2));
        	    					}
        	    				});
        	    				var _afterCut =  Number((_countPrice-_countPriceAfter).toFixed(2));
        	    				_object.find('#combineAllPriceAfter').html("&yen;"+_countPriceAfter);
        	    				_object.find('#combineAllPriceAfterCut').html("&yen;"+_afterCut+"元");
        	    				_object.find('#combineAllPrice').html(_countPrice+"元");
        	        		});
        	        	});
        	        	//自由搭配购买事件绑定
        	        	autoCombineBuyBtn();
        	        	//固定搭配购买事件绑定
        	        	fixedCombineBuyBtn();
        			}
        		});
        	};
        	queryautocombine();
        	//异步加载自由搭配===========end
        	var strToPrice = function(data) {
        		var str = (data / 100).toFixed(2) + '';
        		var intSum = str.substring(0, str.indexOf("."));// 取到整数部分.replace(
        														// /\B(?=(?:\d{3})+$)/g,
        														// ',' )
        		var dot = str.substring(str.length, str.indexOf("."));// 取到小数部分
        		var ret = intSum + dot;
        		return ret;
        	};
        	//异步加载重磅推荐===========start
        	var param = {
        			"placeId" : "6",
     				"gdsSize" : "5",
     				"tabSize" : "0",
     				"placeWidth" : "150",
     				"placeHeight" : "150",
     				"status" : "1"
        	};
        	$.eAjax({
    			url : GLOBAL.WEBROOT + "/homepage/qryFloorList",
    			data : param,
    			async : true,
    			type : "post",
    			dataType : "json",
    			success : function(returnInfo) {
    				if(null == returnInfo || null == returnInfo.gdsList){
    					$("#mainRecommond").html("<div class='pro-empty'>「暂无推荐商品」</div>");
    				}else{
    					var str = "<ul class='glist clearfix'>";
        				var _cnt = 0;
        				$.each(returnInfo.gdsList, function(i, n) {
        					try{
        						//获取作者属性
            					var authorValue = doGdsProp(n,"1001");
            					if(!authorValue){
            						authorValue="";
            					}
            					str += "<li>"+
            			        "<div class='p-img'>"+
            			            "<a href='"+GLOBAL.WEBROOT+"/gdsdetail/"+n.skuInfo.gdsId+"-"+n.skuInfo.id +"'>" +
            			            	"<img width='150' height='150' src='"+n.mainPic.url+"' alt=''>"+
            						"</a>"+
            					"</div>"+
            			        "<p class='p-name'>"+
            			            "<a href='"+GLOBAL.WEBROOT+"/gdsdetail/"+n.skuInfo.gdsId+"-"+n.skuInfo.id+"'>"+
            								""+n.gdsName+""+
            			            "</a>"+
            			        "</p>"+
            			        "<p class='p-actor c-gray'>"+authorValue+""+
            			        "</p>"+
            			       " <p class='p-pri'>"+
            			            "<b class='c-red'>"+
            								"&yen;"+strToPrice(n.skuInfo.discountPrice)+""+
            			            "</b>"+
            			            "<b class='c-gray t-mline'>"+
            								"&yen;"+strToPrice(n.guidePrice)+""+
            			            "</b>"+
            			        "</p>"+
            				"</li>";
            					_cnt ++;
        					}catch(err){
        						$("#mainRecommond").html("<div class='pro-empty'>「暂无推荐商品」</div>");
        					}
        				});
        				str += "</ul>";
        				if(_cnt >0){
        					$("#mainRecommond").html(str);
        				}else{
        					$("#mainRecommond").html("<div class='pro-empty'>「暂无推荐商品」</div>");
        				}
    				}
    			}
        	});
        	//异步加载重磅推荐===========end
        	
        	var querysomevalue = function(){
        		//异步加载相同作者推荐===========start
//        		$("#commondAuthor").append("<div class='loading-small'></div>");
            	$.eAjax({
        			url : GLOBAL.WEBROOT + "/pmph/custsrvs/querysomevalue",
        			data : {
        				"propId" : $("#authorPropId").val(),
        				"propValue" : $("#authorValue").val(),
        				"gdsId" : $("#gdsId").val()
        			},
        			dataType : "html",
        			success : function(returnInfo) {
        				$("#commondAuthor").empty();
        				$("#commondAuthor").html(returnInfo);
        			}
            	});
            	//异步加载相同作者推荐===========end
        	};
        	querysomevalue();
        	//异步加载商品评论===========start
        	$.eAjax({
    			url : GLOBAL.WEBROOT + "/gdsdetail/querygdseval",
    			data : {
    				"gdsId" : $("#gdsId").val()
    			},
    			dataType : "html",
    			success : function(returnInfo) {
    				$("#evaluationList").empty();
    				$("#evaluationList").html(returnInfo);
    			}
        	});
        	//异步加载商品评论===========end
        	var querycommondcat = function(){
        		//异步加载相同分类推荐===========start
//        		$("#commondCat").append("<div class='loading-small'></div>");
            	$.eAjax({
        			url : GLOBAL.WEBROOT + "/gdsdetail/querycommondcat",
        			data : {
        				"categCode" : $("#mainCatgs").val(),
        				"gdsId" : $("#gdsId").val(),
        				"skuId" : $("#skuId").val()
        			},
        			dataType : "html",
        			success : function(returnInfo) {
        				$("#commondCat").empty();
        				$("#commondCat").html(returnInfo);
        			}
            	});
            	//异步加载相同分类推荐===========end
        	};
        	querycommondcat();
        	
            var shopId = $("#shopId").val()
            
            $.eAjax({
    				url : GLOBAL.WEBROOT + "/gdsdetail/shopInfo",
    				data : {"shopId":shopId},
    				dataType:"html",
    				success : function(returnInfo) {
    					$("#shopInfo").html(returnInfo);
    				}
    			});
    			
    		  $('#colShop').die("click").live("click",function () {
		if($('#isLogin').val() != 'true'){
				window.location.href = GLOBAL.WEBROOT+'/login';
				return;
		}
		var isCollect=$('#isCollect').val();
		var url=GLOBAL.WEBROOT + "/shopIndex/collectShop/"+$('#shopId').val()
		if(isCollect== 'true'){
			url=GLOBAL.WEBROOT + "/shopIndex/deleteShop/"+$('#shopId').val()
		}
		$.eAjax({
				url :url ,
				data : {},
				async : false,
				success : function(returnInfo) {
					if(returnInfo.resultFlag=="ok"){
						eDialog.success(returnInfo.resultMsg);
					}
					if(isCollect== 'true'){
						$('#isCollect').val('false');
						$('#colShop').html("收藏店铺");
					}else{
						$('#isCollect').val('true');
						$('#colShop').html("取消店铺收藏");
					}
				},
				exception : function() {
					eDialog.error(returnInfo.resultMsg);
				}
			});
      });	
    		  
    		//绑定【立即购买】按键事件
  			$("#buyBtn").live('click',function(){
  				if(_FLAG == '0'){
  					return;
  				}
  				if($(this).attr('disabled')=='disabled'){
  					return;
  				}
  				//购物车类型
  				var cartType = "02";
  				//店铺id
  				var shopId = $("#shopId").val();
  				var prnFlag = '0';
  				var ordCartItemList = new Array();
  				var buyFlag = 0;
  				if($("#ifBuyonce").val()=='0'){
  					//虚拟产品，则判断是否已购买
  					$.eAjax({
  						url : GLOBAL.WEBROOT + "/gdsdetail/wetherbuyed",
  						data : {skuId:$("input[id='skuId']").val(),gdsTypeId: $("input[id='gdsTypeId']").val()},
  						async : false,
  						success : function(returnInfo) {
  							if(returnInfo.resultFlag=="ok"){
  								if(returnInfo.buyedFlag==true){
  									//表示可以购买多个
  									buyFlag = 1;
  								}
  							}
  							if(returnInfo.resultFlag=='fail'){
  								buyFlag = 2;
  							}
  						}
  					});
  				}
  				if(buyFlag == 1){
  					eDialog.alert("抱歉，该商品只允许购买一次！");
  					return;
  				}
  				commonPriceAddToCart(prnFlag,cartType,shopId,ordCartItemList);
  				buyGds(this,cartType,shopId,ordCartItemList);

  			});

  			//立即购买
  			var buyGds = function(obj,cartType,shopId,ordCartItemList){

  				var param = {
  					cartType : cartType,
  					shopId : shopId,
  					ordCartItemList	: JSON.stringify(ordCartItemList)
  				};

  				$.eAjax({
  					url : GLOBAL.WEBROOT + "/order/cart/easy/add",
  					data : param,
  					async : false,
  					success : function(returnInfo) {
  						if(returnInfo.resultFlag == "ok" && returnInfo.key){
  							window.location.href = GLOBAL.WEBROOT + "/order/build/preord/"+returnInfo.key;
  						}else{
  							eDialog.error(returnInfo.resultMsg);
  							$(obj).bind('click');
  						}
  					},
  					exception : function(){
  						$(obj).bind('click');
  					}
  				});
  			};
            
        };
        
        return {
            "init" : init
        };
    };
    pageConfig.config({
        //指定需要加载的插件，名称请参考common中定义的插件名称，注意大小写
        plugin : ['ePageTop'],
        //指定页面
        init : function(){
            var p = pageInit();
            p.init();
        }
    });
});