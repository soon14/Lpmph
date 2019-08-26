$(function(){
	
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//获得父窗口设置的回调函数
	var _callback = bDialog.getDialogCallback(_dlg);
	var _uuid = _param.pdfUUID;
	$(".media").attr('href',GLOBAL.WEBROOT+"/pmph/custsrvs/readpdfbuffer?pdfId="+_uuid);
	document.getElementById("pdf").click();
});