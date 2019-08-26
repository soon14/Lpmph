$(function(){
	var domain = $.pdialog.getCurrent();
	var params = domain.data('selectorparams');
	//当前用户目录
	var loadFolderList = function(){
		var params = [{
			name : 'upfileFolderUM.userUUID',value:$("#fileUploaderUserNO",domain).val()
		}];
		$.eAjax({
			data : params,
			url : $webroot + 'manage/system/loadFolderList.action',
			success : function(returnInfo) {
				//$("#imgUploadFolder",domain).append('<option value="0">默认目录</option>');
				if(returnInfo && returnInfo.folderList){
					$.each(returnInfo.folderList,function(i,d){
						$("#imgUploadFolder",domain).append('<option value="'+d.id+'">'+d.name+'</option>');
					});
				}
			}
		});
	};
	

	//附件上传完成后的处理
	var attachUploadAllComplete = function (queueData) {
		if(queueData){
			var msg = "文件成功上传：<b style='color:green;'>" + queueData.uploadsSuccessful + "</b> 个<br>" +
			  "上传失败：<b style='color:green;'>" + queueData.uploadsErrored + "</b> 个<br>";
			alertMsg.correct(msg,{
				okCall : function(){
					var callback = domain.data('selectorcallback');
					if($.isFunction(callback)){
						callback(domain.data('infoarr'));
						$.pdialog.closeCurrent();
					}
				}
			});
		}
	};
	//单个附件上传后的处理
//	id: SWFUpload_3_0 - 索引: 0 - 文件名: 2.jpg - 文件大小: 5681 - 类型: .jpg - 创建日期: Fri Aug 17 2012 11:56:07 GMT+0800 - 修改日期: Tue Jun 15 2010 11:46:38 GMT+0800 - 文件状态: -4 - 服务器端消息: {
//		 "id": 471,
//		 "fileno": "711acb7f-645b-4c84-90c3-d663f252b7b5",
//		 "type": 1,
//		 "file_name": "8836017a-4ef8-4d18-b2b0-8c9759b75108jpg",
//		 "file_size": 5,
//		 "folderid": 4,
//		 "file_width": 120,
//		 "file_height": 120,
//		 "old_file_name": "2.jpg",
//		 "orgnode_id": 256,
//		 "orgnode_uuid": "2bc4fb3f-e522-4bfa-9656-31187a8bbcf2",
//		 "ext_name": ".jpg",
//		 "path": "testcsa/201209/",
//		 "small_path": "testcsa/201209/",
//		 "small_file_name": "8836017a-4ef8-4d18-b2b0-8c9759b75108jpg",
//		 "userid": 19180,
//		 "userno": "061b687e-0678-421a-8190-191dcab16008",
//		 "simpleImgURL": "http://file.newhxpx.net/testcsa/201209/8836017a-4ef8-4d18-b2b0-8c9759b75108jpg",
//		 "originalImgURL": "http://file.newhxpx.net/testcsa/201209/8836017a-4ef8-4d18-b2b0-8c9759b75108jpg"
//		} - 是否上传成功: true
	var fileUploadSuccess = function(file,data,response){
//		alert( 'id: ' + file.id
//				+ ' - 索引: ' + file.index
//				+ ' - 文件名: ' + file.name
//				+ ' - 文件大小: ' + file.size
//				+ ' - 类型: ' + file.type
//				+ ' - 创建日期: ' + file.creationdate
//				+ ' - 修改日期: ' + file.modificationdate
//				+ ' - 文件状态: ' + file.filestatus
//				+ ' - 服务器端消息: ' + data
//				+ ' - 是否上传成功: ' + response);
		if(response){
			var fileInfo = $.parseJSON(data);
			var infoArr = domain.data('infoarr');
			if(!infoArr || $.isArray(infoArr)) infoArr = new Array();
			infoArr.push(fileInfo);
			domain.data('infoarr',infoArr);
		}
	};
//	var attachUploadAllComplete = function (event, data) {
//		if (data.errors) {
//			var msg = "The total number of files uploaded: " + data.filesUploaded
//					+ "\n" + "The total number of errors while uploading: "
//					+ data.errors + "\n" + "The total number of bytes uploaded: "
//					+ data.allBytesLoaded + "\n"
//					+ "The average speed of all uploaded files: " + data.speed;
//			alertMsg.error("event:" + event + "\n" + msg);
//		}else{
//			var msg = "文件成功上传：<b style='color:green;'>" + data.filesUploaded + "</b> 个<br>" +
//			  "上传失败：<b style='color:green;'>" + data.errors + "</b> 个<br>" +
//			  "总上传字节数：<b style='color:green;'>" + data.allBytesLoaded + "</b><br>" + 
//			  "平均上传速度：<b style='color:green;'>" + data.speed + "</b> kb/s";
//			alertMsg.correct(msg,{
//				okCall : function(){
//					var callback = domain.data('selectorcallback');
//					if($.isFunction(callback)){
//						callback();
//						$.pdialog.closeCurrent();
//					}
//				}
//			});
//		}
//	};
	
	$("#attachmentFileInput",domain).each(function() {
		var $this = $(this);
		var options = {
			'swf' : $webroot + 'framework/jsLib/jquery/uploadify/3.2/uploadify.swf',
			'uploader' : $webroot + 'vc/filesUpload.action',
			//'cancelImg' : $webroot + 'framework/jsLib/jquery/uploadify/3.2/uploadify-cancel.png',
			'queueID' : "attachmentFileQueue",
			'fileObjName' : 'upfiles',
			'fileTypeDesc' : "*.jpg;*.jpeg;*.gif;*.png",
			'fileTypeExts' : "*.jpg;*.jpeg;*.gif;*.png",
			'buttonText' : '选择附件',
			'multi' : params.checktype==-1?false:true,
			'auto' : false,
			'fileSizeLimit':'2048KB',
			'method' : 'post',
			'height' : 25,
			'width' : 100,
			'onUploadError' : uploadifyError,
			'onUploadSuccess' : fileUploadSuccess,
			//onComplete : imgUploadComplete,// 单项目上传完成后的事件
			'onQueueComplete' : attachUploadAllComplete//图片全部上传完后的处理
		};
//		var options = {
//			'uploader' : $webroot + 'framework/jsLib/jquery/uploadify/uploadify.swf',
//			'script' : $webroot + 'vc/filesUpload.action',
//			'cancelImg' : $webroot + 'framework/jsLib/jquery/uploadify/uploadify-cancel.png',
//			'queueID' : "attachmentFileQueue",
//			'fileDataName' : 'upfiles',
//			'fileDesc' : "*.jpg;*.jpeg;*.gif;*.png",
//			'fileExt' : "*.jpg;*.jpeg;*.gif;*.png",
//			'buttonText' : '选择附件',
//			'multi' : true,
//			'auto' : false,
//			'onError' : uploadifyError,
//			//onComplete : imgUploadComplete,// 单项目上传完成后的事件
//			'onAllComplete' : attachUploadAllComplete//图片全部上传完后的处理
//		};
		$this.uploadify(options);
	});

	
	//附件上传按钮事件定义
	$("#fileUploaderUpload",domain).click(function(){
		$("#attachmentFileInput",domain).uploadify('settings','formData',{
			'folderId':$('#imgUploadFolder',domain).val(),
			'time' : new Date().getTime(),
			'tokenParams' : $("#sessionToken",domain).val()
		});
		$("#attachmentFileInput",domain).uploadify('upload','*');
//		$("#attachmentFileInput",domain).uploadifySettings('scriptData',{
//			'folderId':$('#imgUploadFolder',domain).val(),
//			'time' : new Date().getTime(),
//			'tokenParams' : $("#sessionToken",domain).val()
//		});
//		$("#attachmentFileInput",domain).uploadifyUpload();
	});
	
	loadFolderList();
});