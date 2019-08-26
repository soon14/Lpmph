/**
 * gxq
 */
$(function(){
	//页面业务逻辑处理内容
    var pageInit = function(){
        var init = function(){
                  	
        	//详情、包装清单============end
        	$("#readPdf").click(function(){
        		bDialog.open({
                    title : '试读章节',
                    width : '70%',
                    height : '85%',
                    params:{
                    	pdfUUID : $(this).attr('value')
                    	},
                    url : GLOBAL.WEBROOT+"/pmph/custsrvs/readpdf",
                    callback:function(data){
                    	
                    }
                });
        	});
        	
        	var parseMoney = function(data){
        		var str = (data/100).toFixed(2) + '';
				var intSum = str.substring(0,str.indexOf("."));//取到整数部分.replace( /\B(?=(?:\d{3})+$)/g, ',' )
				var dot = str.substring(str.length,str.indexOf("."));//取到小数部分
				return ret = intSum + dot;
        	};
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
        	
        	var strToPrice = function(data) {
        		var str = (data / 100).toFixed(2) + '';
        		var intSum = str.substring(0, str.indexOf("."));// 取到整数部分.replace(
        														// /\B(?=(?:\d{3})+$)/g,
        														// ',' )
        		var dot = str.substring(str.length, str.indexOf("."));// 取到小数部分
        		var ret = intSum + dot;
        		return ret;
        	};
        	
        	//异步加载网络增值服务========start
        	//有网络增值服务的时候请求
        	if($("#ifNetWork").val()=='1'){
        		var isbn = $("#ISBN").val();
        		isbn = isbn.substr(5,17);
        		$.eAjax({
        			url : GLOBAL.WEBROOT + "/pmph/custsrvs/querynetwordservice",
        			data : {
//        				"isbn" : $("#ISBN").val()
        				"isbn" : isbn,
        				"cnt" : '10'
        			},
        			dataType : "html",
        			success : function(returnInfo) {
//        				返回html格式数据 472 注释
        				$("#netWorkServiceList").empty();
        				$("#netWorkServiceList").html(returnInfo);

        			}
            	});
        	}
        	//异步加载网络增值服务========end
        	
            //在线试读绑定事件==============start
            $("#readOnline").live('click',function(){
            	$.eAjax({
    				url : GLOBAL.WEBROOT + "/pmph/custsrvs/readonline",
    				data : {gdsId:$("#gdsId").val(),catgCode:$("#mainCatgs").val()},
    				success : function(returnInfo) {
    					if(returnInfo.resultFlag=='ok'){
    						eDialog.success(returnInfo.resultMsg); 
    					}else{
    						eDialog.error(returnInfo.resultMsg);
    					}
    				}
    			});
            });
            //在线试读绑定事件==============end
        };
        
        return {
            "init" : init
        };
    };
    pageConfig.config({
        //指定需要加载的插件，名称请参考common中定义的插件名称，注意大小写
        //plugin : ['ePageTop'],
        //指定页面
        init : function(){
            var p = pageInit();
            p.init();
        }
    });
});