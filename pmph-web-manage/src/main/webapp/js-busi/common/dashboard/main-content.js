$(function() {
	var initCount = function(key) {

		$.eAjax({
			url : GLOBAL.WEBROOT + "/reportitem/" + key,
			success : function(returnInfo) {
				$("#" + key).empty();
				if(returnInfo.rate){
					$("#" + key).html(returnInfo.rate);	
				}else{
				if(key == "yestordermoney" || key == "yestsellmoney"){
					returnInfo = (parseFloat(returnInfo)/100).toFixed(2);
					$("#" + key).html(returnInfo);
				}else{
				$("#" + key).html(returnInfo);
				}
				}

			},
			error : function() {
				eDialog.error('浏览器错误！');
			}
		});

	}
	// 紧俏商品数
	initCount("hardtogetgdscount");
	// 缺货商品数
	initCount("lackgdscount");
	// 待审核商品
	initCount("verifygdscount");
	// 待审核入驻
	initCount("auditshopcount");
	// 待审核评价
	initCount("auditevalcount");
	// 待审核促销
	// initCount("auditpromcount");
	// 待审核主题促销
	initCount("auditthemepromcount");
	// 待发货订单
	initCount("waitdeliordercount");
	// 待处理退款订单
	initCount("waitretMordercount");
	// 待处理退货订单
	initCount("waitretordercount");
	// 待审核线下支付
	initCount("waitverifyordercount");

	// 昨日订单额
	initCount("yestordermoney");

	// 昨日销售额
	initCount("yestsellmoney");

	// 昨日订单总量
	initCount("yestordercount");

	// 昨日支付成功订单总数
	initCount("yestpayedordercount");

	// 昨日下单成功率
	initCount("yestsucceeorderrate");

	// 昨日注册会员总数
	initCount("yestregisterstaffcount");

	// 图表初始化
	saleTradeDataChart = echarts.init(document.getElementById('saleTrend'));

	/**
	 * 刷新图表
	 */
	function _drawSaleChart(selector, data) {
		var xAxisData = [];// 日期
		var orderAmountSeries = [];// 订单
		var saledAmountSeries = [];// 销售

		// 处理数据
		for (var i = 0; i < data.length; i++) {
			xAxisData.push(data[i].dataDate);
			orderAmountSeries.push(data[i].compareItem1 / 100);// 分转元
			saledAmountSeries.push(data[i].compareItem2 / 100);// 分转元
		}

		// 指定图表的配置项和数据
		var option = {
//			title : {
//				text : '近30天交易情况',
//				textStyle : {
//					fontSize : 18,
//					fontWeight : 'bolder',
//					color : 'orange'
//				}
//			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : [ '销售额', '订单额' ]
			},
			calculable : true,
			xAxis : {
				// data:
				// ["2016/05/11","2016/05/13","2016/05/15","2016/05/17","2016/05/19","2016/05/21"]
				data : xAxisData
			},
			yAxis : [ {
				type : 'value',
				axisLabel : {
					formatter : '{value} 元'
				}
			} ],
			series : [ {
				name : '销售额',
				type : 'line',
				// data: [5000, 20000, 36000, 10000, 10000, 20000]
				data : saledAmountSeries
			}, {
				name : '订单额',
				type : 'line',
				// data: [19200, 220000, 89000, 210000, 70500, 120900]
				data : orderAmountSeries
			} ]
		};

		// 使用刚指定的配置项和数据显示图表。
		selector.setOption(option);
	}

	// 5. 销售趋势
	$.eAjax({
		url : GLOBAL.WEBROOT + "/reportitem/saleTradeData",
		data : {
			shopId : 100
		},
		success : function(returnInfo) {
			
			if (returnInfo && returnInfo.serviceState == '0000') {// 正常
			// $("#shopORDTrendZW").hide();

				$("#saleTrend").show();
				var list = returnInfo.reportItemRespDTOs;
				// 排序处理
				list.sort(function(a, b) {
					return Number(a.dataDate) - Number(b.dataDate)
				});
				// 无数据判断
				var flag = true;
				// for(var i in list){
				// flag = flag&&(list[i].orderAmount!="0");
				// }
				// if(flag){//有数据刷新图表
				_drawSaleChart(saleTradeDataChart, list);
				// }else{//无数据，展现“无数据”图例
				// $("#shopORDTrend").hide();
				// $("#shopORDTrendZW").show();
				// }
			} else {
				$("#saleTrend").hide();
				// $("#shopORDTrendZW").show();
			}
		}
	});
	
	
	
	// 图表初始化
	orderTradeDataChart = echarts.init(document.getElementById('orderTrend'));

	/**
	 * 刷新图表
	 */
	function _drawOrderChart(selector, data) {
		var xAxisData = [];// 日期
		var orderAmountSeries = [];// 订单
		var saledAmountSeries = [];// 销售

		// 处理数据
		for (var i = 0; i < data.length; i++) {
			xAxisData.push(data[i].dataDate);
			orderAmountSeries.push(data[i].compareItem1 );// 分转元
			saledAmountSeries.push(data[i].compareItem2);// 分转元
		}

		// 指定图表的配置项和数据
		var option = {
//			title : {
//				text : '近30天交易情况',
//				textStyle : {
//					fontSize : 18,
//					fontWeight : 'bolder',
//					color : 'orange'
//				}
//			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : [ '支付成功总量', '订单总量' ]
			},
			calculable : true,
			xAxis : {
				// data:
				// ["2016/05/11","2016/05/13","2016/05/15","2016/05/17","2016/05/19","2016/05/21"]
				data : xAxisData
			},
			yAxis : [ {
				type : 'value',
				axisLabel : {
					formatter : '{value} 单'
				}
			} ],
			series : [ {
				name : '支付成功总量',
				type : 'line',
				// data: [5000, 20000, 36000, 10000, 10000, 20000]
				data : saledAmountSeries
			}, {
				name : '订单总量',
				type : 'line',
				// data: [19200, 220000, 89000, 210000, 70500, 120900]
				data : orderAmountSeries
			} ]
		};

		// 使用刚指定的配置项和数据显示图表。
		selector.setOption(option);
	}

	// 5. 销售趋势
	$.eAjax({
		url : GLOBAL.WEBROOT + "/reportitem/orderTradeData",
		data : {
			shopId : 100
		},
		success : function(returnInfo) {
			
			if (returnInfo && returnInfo.serviceState == '0000') {// 正常
			// $("#shopORDTrendZW").hide();

				$("#saleTrend").show();
				var list = returnInfo.reportItemRespDTOs;
				// 排序处理
				list.sort(function(a, b) {
					return Number(a.dataDate) - Number(b.dataDate)
				});
				// 无数据判断
				var flag = true;
				// for(var i in list){
				// flag = flag&&(list[i].orderAmount!="0");
				// }
				// if(flag){//有数据刷新图表
				_drawOrderChart(orderTradeDataChart, list);
				// }else{//无数据，展现“无数据”图例
				// $("#shopORDTrend").hide();
				// $("#shopORDTrendZW").show();
				// }
			} else {
				$("#saleTrend").hide();
				// $("#shopORDTrendZW").show();
			}
		}
	});
	
	
	
	
	
	
	
	//清空菜单定位参数
	eNav.clearForword();
	eNav.clearSubPageText();
	$('#breadcrumbNavigateBar').hide();
})
