$(function(){   	
	//选项跳转
//	$('#teamTab li a').click(function(){
//		alert("click");
//	});
	
	/*//选项卡被选中
	$('#teamTab li').click(function(){
		$(this).siblings().removeClass('active');
		$(this).addClass('active');	
	});*/
	
	 //页面业务逻辑处理内容
    var pageInit = function(){
    	
        var init = function(){
        	
        	//controller配置
            var controllConfig = {
					'00':'all',
            		'01':'pay',
            		'02':'send',
            		'04':'partSend',
            		'05':'allSend',
            		'99':'cancel'
            };
        	
            //title配置
            var titleConfig = {
					'00':'全部订单',
            		'01':'待支付订单',
            		'02':'待发货订单',
            		'04':'部分发货订单',
            		'05':'全部发货订单',
            		'99':'已取消订单'
            };
            
            //常量
        	var constant = {
        			status:$('#status').val(),
        			begDate:ebcDate.dateFormat(ebcDate.yearCalc(new Date(),-1),'yyyy-MM-dd'),
        			endDate:ebcDate.dateFormat(ebcDate.dateCalc(new Date(),1),'yyyy-MM-dd')
        	};
        	
        	//=================-------================
        	//方法定义
            var detailOrd = function(_this){
            	var orderId = _this.parents('.order-tb').find('input[name="orderId"]').val();
        		//$('#detailForm').find('input[name="orderId"]').val(orderId);
        		
        		window.open(GLOBAL.WEBROOT + "/ord/detail/"+orderId);
        		//$('#detailForm').submit();
            };
            
        	 //选项跳转
        	$('#teamTab li a').click(function(){
        		var id = $(this).parent().attr('id');
        		window.location.href = GLOBAL.WEBROOT + '/pubord/'+controllConfig[id];
        	});
        	
        	//选项卡被选中
        	$('#teamTab li').each(function(){
        		if($(this).attr('id')==constant.status){
        			$(this).addClass('active');
            		$(this).siblings().removeClass('active');
        		}
        	});
            
        	//分页
        	$('#pageControlbar').bPage({
        		
        	    url : GLOBAL.WEBROOT + '/pubord/'+controllConfig[constant.status],
        	    asyncLoad : false,
        	    params : function(){
        	    	var begDate = $('input[name="begDate"]').val();
        	    	var endDate = $('input[name="endDate"]').val();
        	    	return {
        	    		begDate: begDate?begDate:constant.begDate,
        	    		endDate: endDate?endDate:constant.endDate
        	    	}
        	    }
        	});
            
        	//订单详情
        	$('.signOrder-detail').click(function(){
        		detailOrd($(this));
        	});
            
        	//取消订单
        	$('#cancel').click(function(){
        		var _this = this;
        		eDialog.confirm("您确定要取消该征订单吗" , {
        		    buttons : [{
        		        'caption' : '确定',
        		        'callback' : function(){
        		        	var pubOrderId = $(_this).closest('.sign-list-detail').find('input[name="pubOrderId"]').val();
        		        	var url = GLOBAL.WEBROOT + "/puborder/cancel";
        		        	$.eAjax({
        	        			url:url,
        	        			data:[{name:"pubOrderId",value:pubOrderId},{name:"oper",value:"04"}],
        	        			method:'post',
        	        			success:function(returnInfo){

        	        				if(returnInfo&&returnInfo.resultFlag=='true'){
        	        					window.location.reload(true);
        	        					//window.location.href = GLOBAL.WEBROOT + "/order/pay";
        	        				}else{
        	        					eDialog.alert(returnInfo.resultMsg,function(){
        	        						//window.location.href = GLOBAL.WEBROOT + "/order/pay";
        	        						window.location.reload(true);
        	        					});
        	        				}
        	        				
        	        			}
        	        		}); 
        		        }
        		    },{
        		        'caption' : '取消',
        		        'callback' : function(){
        		           
        		        }
        		    }]
        		});
        		
        		//$('#cancelForm').submit();
        	});
            
        };
        return {
        	init : init
        };
    };
    pageConfig.config({
        //指定需要加载的插件，名称请参考common中定义的插件名称，注意大小写
        plugin : ['bForm','bPage'],
        //指定页面
        init : function(){
            var p = new pageInit();
            p.init();
        }
    });
});