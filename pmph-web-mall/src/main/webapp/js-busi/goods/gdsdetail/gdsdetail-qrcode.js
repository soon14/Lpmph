$(function(){
	var gdsId = $("#gdsId").val();
	var skuId = $("#skuId").val();
	var qrCodeURL = $("#qrCodeURL").val();
	var qrCode = qrCodeURL+gdsId+"-"+skuId;
	$("#qrcode").qrcode({
        render : "canvas",    //设置渲染方式，有table和canvas，使用canvas方式渲染性能相对来说比较好
        text : qrCode,
        width : "120",               //二维码的宽度
        height : "120",              //二维码的高度
        background : "#ffffff",       //二维码的后景色
        foreground : "#000000",        //二维码的前景色
    });
});