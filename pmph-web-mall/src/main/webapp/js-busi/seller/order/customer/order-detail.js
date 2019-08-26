function updatebill(){
	var orderId = $('#orderId').val();
	bDialog.open({
		title : '修改发票信息',
		width : 680,
		height :500,
		url : GLOBAL.WEBROOT + '/seller/order/customer/billinfo/'+orderId,
		callback:function(data){			
            location.reload();
		}
	});
}

function updateInfo(){
	var orderId = $('#orderId').val();
    bDialog.open({
        title : "修改收货人信息",
        width : 880,
        height : 400,
        url : GLOBAL.WEBROOT+'/seller/order/customer/buyerinfo/'+orderId,
        callback:function(returnInfo){
            location.reload();
        }
    });
}

function updateMsg(){
	var orderId = $('#orderId').val();
    bDialog.open({
        title : "修改买家备注",
        width : 580,
        height : 280,
        url : GLOBAL.WEBROOT+'/seller/order/customer/msginfo/'+orderId,
        callback:function(returnInfo){
            location.reload();
        }
    });
}

function updateSellerMsg(){
	var orderId = $('#orderId').val();
    bDialog.open({
        title : "修改卖家备注",
        width : 580,
        height : 280,
        url : GLOBAL.WEBROOT+'/seller/order/customer/sellerMsginfo/'+orderId,
        callback:function(returnInfo){
            location.reload();
        }
    });
}
function updateDelivery(obj){
	var deliveryId=$(obj).next().val();
    bDialog.open({
        title : "修改物流信息",
        width : 850,
        height : 300,
        url : GLOBAL.WEBROOT+'/seller/order/customer/sellerDeliveryinfo/'+deliveryId,
        callback:function(returnInfo){
            location.reload();
        }
    });
}
