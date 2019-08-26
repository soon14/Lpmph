$(function(){
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//返回的数组
	var fileResults = new Array();
	
	//统计上传成功文件个数
	var successCount = 0;
    $('#btnFileUploaderUpload').click(function(){
    	$(this).button('loading');//设置状态按钮的状态为处理中
    	successCount = 0;
		$("#attachmentFileInput").eUpload();
    });
    
    //单个文件上传成功后的回调处理，实现上传结果处理等信息均在此处理
    //最终结果可在onQueueComplete中统一显示
    var uploadSuccess = function(file,data,response){
		if(response){
			if(data){//data为后台返回的JSON内容
				var tmp = JSON.parse(data);
				var fileInfo = {};
				if(tmp && typeof(tmp.fileId) != 'undefined' && typeof(tmp.url) != 'undefined' ){
					fileInfo.fileId = tmp.fileId;
					fileInfo.url = tmp.url;
					fileResults.push(fileInfo);
				}
			}
		}
	};
    
	//更多的参数请参考e.upload.js中的详细参数
	$("#attachmentFileInput").eUploadInit({
		'uploader' : _param.uploadUrl ? _param.uploadUrl : $webroot + 'ecpupload/publicFileUpload',//后台接收文件处理的controller
		'fileObjName' : _param.uploadFileObjName ? _param.uploadFileObjName : 'uploadFileObj',//后台接收文件的对象名称
		'fileTypeDesc' : _param.fileTypeExts,  //文件选择类型描述
		'fileTypeExts' : _param.fileTypeExts,  //文件选择类型限制
		'fileSizeLimit' : _param.fileSizeLimit, //文件大小限制
		'multi' : _param.checktype=='multi'?true:false,  //是否批量上传
		'queueID' : "attachmentFileQueue",//队列内容显示元素ID指定默认ID为attachmentFileQueue
		'onUploadSuccess' : uploadSuccess,
		//回调
		'callback' : function(){
			$('#btnFileUploaderUpload').button('reset');//设置状态按钮的状态为恢复
		}
	});
	
	//完成上传，关闭窗口，返回数据
	$('#btnUploadDone').click(function(){
		bDialog.closeCurrent(fileResults);
	});
});