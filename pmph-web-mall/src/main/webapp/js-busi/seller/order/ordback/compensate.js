function addLoadParamsToUrl(_load_url_) {
	var begDate = $('input[name="begDate"]').val();
	if (begDate != "" && begDate != undefined) {
		_load_url_ += '&begDate=' + begDate;
	}
	var endDate = $('input[name="endDate"]').val();
	if (endDate != "" && endDate != undefined) {
		_load_url_ += '&endDate=' + endDate;
	}
	var orderId = $('input[name="orderId4Search"]').val();
	if (orderId != "" && orderId != undefined) {
		_load_url_ += '&orderId=' + orderId;
	}
	var shopId = $('input[name="shopId"]').val();
	if (shopId != "" && shopId != undefined) {
		_load_url_ += '&shopId=' + shopId;
	}
    var date = new Date();
    _load_url_ += '&t='+date.getTime();
	return _load_url_;
}
function toDecimal(x) { 
	   var f = parseFloat(x); 
	   if (isNaN(f)) { 
	    return; 
	   } 
	   f = Math.round(x*100)/100; 
	   return f; 
	}
function selectOrder(orderId,realMoney,staffName){
	$('#orderId').val(orderId);
	$('#orderIdspan').html(orderId);
	$('#realMoney').val(realMoney);
	$('#realMoneyspan').html(realMoney);
	$('#staffName').val(staffName);
	$('#staffNamespan').html(staffName);
}
// 页面初始化模块
$(function() {
	
	// 页面业务逻辑处理内容
	var pInit = function() {

		var init = function() {
			
			var orderId = $('#orderId').val();
			var startdate = $("input[name='begDate']").val();
			var enddate = $("input[name='endDate']").val();
			$("#backMoney").keyup(function(){      
		        $(this).val($(this).val().replace(/^\./g,"").replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3').replace(/[^\d.]/g,''));      
		    }).bind("paste",function(){  
		        $(this).val($(this).val().replace(/^\./g,"").replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3').replace(/[^\d.]/g,''));       
		    }); 
			
			//提交
            $('#btn_submit').click(function(){
        		if ($("#backMoney").val()==""&&$("#memo").val()!=""){
        			$("#backMoney").css({ "border-color": "#C62626" });
        			$("#backMoneyspan").html("必填字段");
        			return false;
        		}
        		if ($("#backMoney").val()!=""&&$("#memo").val()==""){
        			$("#memo").css({ "border-color": "#C62626" });
        			$("#memospan").html("必填字段");
        			return false;
        		}
        		if ($("#memo").val()==""&&$("#backMoney").val()==""){
        			$("#backMoney").css({ "border-color": "#C62626" });
        			$("#memo").css({ "border-color": "#C62626" });
        			$("#backMoneyspan").html("必填字段");
        			$("#memospan").html("必填字段");
        			return false;
        		}
        		var orderId = $('#orderId').val();
        		var backMoney = $('#backMoney').val();
        		var memo = $('#memo').val();
        		var realMoney = $('#realMoney').val();
        		//保留2位小数，避免出现问题
        		backMoney = toDecimal(backMoney*100);
        		realMoney = toDecimal(realMoney*100);

        		if(realMoney<backMoney){
        			eDialog.alert('退款金额不能大于实付金额！');
        			return;
        		}
        		if(orderId==null||orderId==''){
        			return;
        		}
        		if(backMoney==null||backMoney==''){
        			return;
        		}
        		if(memo==null||memo==''){
        			return;
        		}
        		var data = [];
        		data.push({name:'orderId',value:orderId},
        				{name:'backMoney',value:backMoney},
        				{name:'memo',value:memo}
        				);
        		var url =  GLOBAL.WEBROOT + '/seller/refundReview/savecompenstate';
        		$.eAjax({
        			url : url,
        			data :data,
        			async : false,
        			type : "post",
        			dataType : "json",
        			success : function(result) {
        				if(result.resultFlag == "ok"){  //不跳页面
        					//刷新退款列表页面
        					window.location.href=GLOBAL.WEBROOT + '/seller/refundReview/refund1';
        				}else{  //跳页面
        					eDialog.alert(result.message);
        					return;
        				}
        			},
        			failure:function(){
        			}
        		});
        	});
        	$('#btn_return').click(function(){
        		window.location.href=GLOBAL.WEBROOT + '/seller/refundReview/refund1';
        	});
            //查询
			$('#btnFormSearch').click(function() {
				var _load_url_ = '/seller/refundReview/gridordlist?';
            	_load_url_ = addLoadParamsToUrl(_load_url_);
            	$(this).attr("class","active");
            	$('#order-contentDiv').load(GLOBAL.WEBROOT + _load_url_);
            	$('#btnFormSearch').attr("class","sbtn sbtn-blue");
			});
			// 重置
			$('#btnFormReset').click(function(){
				 $("input[name='begDate']").val(startdate);
		    	 $("input[name='endDate']").val(enddate);
		    	 $("input[name='orderId4Search']").val('');
			});
			$('#btnFormSearch').click();
			$('#btnFormSearch').attr("class","sbtn sbtn-blue");
		};
		return {
			init : init
		};
	};
	pageConfig.config({
		// 指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : [ 'bForm', 'bPage' ],
		// 指定页面
		init : function() {
			var p = new pInit();
			p.init();

		}
	});

});
