$(function(){
	
	//统计上传成功文件个数
	var successCount = 0;
    $('#fileUploaderUpload').click(function(){
    	if($('div.uploadify-queue-item',$('#attachmentFileQueue')).size()>0){
        	$(this).button('loading');//设置状态按钮的状态为处理中
        	successCount = 0;
        	$("#excelFileInput").eUpload();
    	}else{
    		eDialog.warning("请选择导入文件!");
    	}
    });
    
	//单个文件上传成功后的回调处理，实现上传结果处理等信息均在此处理
    //最终结果可在onQueueComplete中统一显示
    var uploadSuccess = function(file,data,response){
    	eDialog.confirm((data!="ok"?(data+"\n"+"导入失败！"):"导入成功！")+"是否继续导入？", {
			buttons : [{
				caption : '继续导入',
				callback : function(){
				}
			},{
				caption : '返回',
				callback : function(){
					bDialog.closeCurrent();
				}
			}]
		});
	};
	
	
	//更多的参数请参考e.upload.js中的详细参数
	$("#excelFileInput").eUploadInit({
		'uploader' : $webroot + 'custcardsend/importdata',//后台接收文件处理的controller 
		'fileObjName' : 'excelFile',
		'fileTypeDesc' : "*.xls;*.xlsx",  //文件选择类型描述
		'fileTypeExts' : "*.xls;*.xlsx",  //文件选择类型限制
		//'queueID' : "attachmentFileQueue",//队列内容显示元素ID指定默认ID为attachmentFileQueue
		'queueSizeLimit':1,
		'multi' : false,
		'onUploadSuccess' : uploadSuccess,
		'onUploadError': uploadError,	
		'onQueueComplete' : function(queueData){
			//console.log(queueData);
			//console.log(queueData.uploadsSuccessful);
		},
		//回调
		'callback' : function(){
			$('#fileUploaderUpload').button('reset');//设置状态按钮的状态为恢复
		}
	});
	

	
});