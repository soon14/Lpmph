/**
 * Created by wang on 16/8/5.
 */
$(function(){



    //===================方法定义===============
    //全局事件定义
    //折叠面板 ：店铺、资金账户。
    $("span.ui-arrow").click(function(e){
        var self = $(this).parent().parent();
        var aright = "ui-arrow-right";
        var abottom = "ui-arrow-bottom";
        if($(this).hasClass(aright)){ //向右时展开
            $(this).removeClass(aright).addClass(abottom);
            self.find("div").show();
        }else if($(this).hasClass(abottom)){ //向下时折叠
            $(this).removeClass(abottom).addClass(aright);
            self.find("div").hide();
        }else{
            //DO NOTHING
        }
    });

    $('#invoice').click(function(){
        $('#order_detail_invoice').offCanvas('open');
    });

    $('#order_detail_invoice').click(function(e){
        e.stopPropagation();
        $(this).offCanvas('close');
    });

    $('#gopay').click(function(e){
        e.stopPropagation();
        $('form',$(this).closest('#payfooter')).submit();
    });

    $('#cancel').click(function(e){
        e.stopPropagation();
        var _this = this;
        eDialog.confirm("确认取消" , function(){
            var orderId = $('form',$(_this).closest('#payfooter')).attr('id');
            var url = GLOBAL.WEBROOT + "/order/my/cancel/confirmord";
            $.eAjax({
                url:url,
                data:[{name:"orderId",value:orderId},{name:"oper",value:"04"}],
                method:'post',
                success:function(returnInfo){
                    if(returnInfo&&returnInfo.resultFlag=='true'){
                        window.location.href = GLOBAL.WEBROOT + "/order/my";
                    }else{
                        eDialog.alert(returnInfo.resultMsg,function(){
                            window.location.reload();
                        });
                    }
                }
            });
        });
    });
    
    //买家收货确认
	$('.confirmord').click(function(e){
        e.stopPropagation();
        var _this = this;
        
        eDialog.confirm("收货确认" , function(){
        	 var orderId = $("#orderId").val();
             var url = GLOBAL.WEBROOT + "/order/my/recept/confirmord";
             $.eAjax({
                 url:url,
                 data:[{name:"orderId",value:orderId},{name:"oper",value:"03"}],
                 method:'post',
                 success:function(returnInfo){
                     if(returnInfo&&returnInfo.resultFlag=='true'){
                     	window.location.href = GLOBAL.WEBROOT + "/order/member/recept";
                     }else{
                         eDialog.alert(returnInfo.resultMsg,function(){
                             window.location.reload();
                         });
                     }
                 }
             });
        	
        }); 
    });
	//咨询客服
	$('.cstmrBtn').click(function() {
		var shopId=$("#shopId").val();
		var issueType=$("#issueType").val();
		var gsdandOrdId=$("#ordId").val();
		window.location.href = GLOBAL.WEBROOT + "/customerservice/index/"+shopId+"-"+issueType+"-"+gsdandOrdId;
		//window.location.href = GLOBAL.WEBROOT + "/customerservice/index?shopId="+shopId +"&issueType="+issueType+"&ordId="+ordId;
	});
	
});