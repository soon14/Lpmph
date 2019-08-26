function addLoadParamsToUrl(_load_url_){
	var date = new Date();
	_load_url_ += '&t='+date.getTime();
	var begDate = $('input[name="begDate"]').val();
    if(begDate != "" && begDate != undefined)
    {
    	_load_url_ += '&begDate=' + begDate;	
    }
    var endDate = $('input[name="endDate"]').val();
    if(endDate != "" && endDate != undefined)
    {
    	_load_url_ += '&endDate=' + endDate;	
    }
    var gdsType  = $('select[name="gdsType"]').val();
    if(gdsType != "" && gdsType != undefined)
    {
    	_load_url_ += '&gdsType=' + gdsType;	
    }
    var shopId  = $('select[name="shopId"]').val();
    if(shopId != "" && shopId != undefined)
    {
    	_load_url_ += '&shopId=' + shopId;	
    }   
    var gdsName = $('input[name="gdsName"]').val();
    if(gdsName != "" && gdsName != undefined)
    {
    	_load_url_ += '&gdsName=' + gdsName;
    }
    var isbn = $('input[name="isbn"]').val();
    if(isbn != "" && isbn != undefined)
    {
    	_load_url_ += '&isbn=' + isbn;
    }
    var staffCode = $('input[name="staffCode"]').val();
    if(staffCode != "" && staffCode != undefined)
    {
    	_load_url_ += '&staffCode=' + staffCode;
    }
    var orderId = $('input[name="orderId"]').val();
   
    if(orderId != "" && orderId != undefined)
    {
    	_load_url_ += '&orderId=' + orderId;
    }
    
    var payType = $('select[name="payType"]').val();
    
    if(payType != "" && payType != undefined)
    {
    	_load_url_ += '&payType=' + payType;
    }
    return _load_url_;
}
//页面初始化模块
$(function(){
	
	//页面业务逻辑处理内容
	var pInit = function(){

    	var init = function(){
    		
    		function orderSum(){
    	        $.eAjax({
    	            url : GLOBAL.WEBROOT + '/seller/order/detail/salesum',
    	            data :ebcForm.formParams($("#searchForm")),
    	            async : true,
    	            type : "post",
    	            dataType : "json",
    	            success : function(datas) {
    	                $("#basicMoney").text(ebcUtils.numFormat(accDiv(datas.basicMoney,100),2));
    	                //和列表里的数据同名
    	                $("#realMoney").text(ebcUtils.numFormat(accDiv(datas.realMoney,100),2));
    	                $("#orderNum").text(datas.orderNum);
    	                $("#saleNum").text(datas.saleNum);
    	            }
    	        });
    	    }
    		
    		function queryOrders(){
    			var _load_url_ = '/seller/order/detail/detaillist?t='+new Date().getTime();
            	// _load_url_ = addLoadParamsToUrl(_load_url_);
//            	alert(_load_url_);
            	$(this).attr("class","active");
                $('#order-contentDiv').load(GLOBAL.WEBROOT + _load_url_ , ebcForm.formParams($("#searchForm")));
    		}
    		
    		orderSum();
    		queryOrders();
    		
    		//查询
    		$('#btnFormSearch').click(function(){
    			if(!$('#searchForm').valid())
    				return;
    			orderSum();
        		queryOrders();
    		});	
    		//重置
    		$('#btnFormReset').click(function(){
    			ebcForm.resetForm('#searchForm');
    			$("#begDate").val($("#resetBegDate").val());
    			$("#endDate").val($("#resetEndDate").val());
    		});
    		
    		
    		//导出明细
    		$("#btnFormExport").click(function(){
    			if(!$("#searchForm").valid()) return false;
    			eDialog.confirm("导出订单" , {
    				buttons: [{
    					'caption': '导出',
    					'callback': function () {
    						var p = ebcForm.formParams($('#searchForm'));
    						//导出限制
    						p.push({name:'pageNo',value:1});
    						p.push({name:'pageSize',value:10000});
    						//导出限制
    						$('#exportType').val('getSellerDetail');
    						$('#exportInfo').val(JSON.stringify(p));
    						$("#exportForm").submit();
    					}
    				}, {
    					'caption': '取消',
    					'callback': function () {
    					}
    				}]
    			});
    		});
    	};
    	return {
    		init : init
    	};
	};
	pageConfig.config({
		//指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : ['bForm','bPage'],
		//指定页面
		init : function(){
			var p = new pInit();
			p.init();
			
		}
	});
	
});


