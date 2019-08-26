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
		//var fileInfo = $.parseJSON(data);
		//var infoArr = domain.data('infoarr');
		//if(!infoArr || $.isArray(infoArr)) infoArr = new Array();
		//infoArr.push(fileInfo);
		//domain.data('infoarr',infoArr);
	}
};
//附件上传完成后的处理
var attachUploadAllComplete = function (queueData) {
	if(queueData){
		var msg = "文件成功上传：<b style='color:green;'>" + queueData.uploadsSuccessful + "</b> 个<br>" +
		  "上传失败：<b style='color:red;'>" + queueData.uploadsErrored + "</b> 个<br>";
		eDialog.success(msg);
	}
};
/**
 * A function that triggers when all file uploads have completed. There is no
 * default event handler.
 * 全部上传任务完成后执行的默认方法
 * @param {Object}
 *            event: The event object.
 * @param {Object}
 *            data: An object containing details about the upload process: -
 *            filesUploaded: The total number of files uploaded - errors: The
 *            total number of errors while uploading - allBytesLoaded: The total
 *            number of bytes uploaded - speed: The average speed of all
 *            uploaded files
 */
function uploadifyAllComplete(event, data) {
	if (data.errors) {
		var msg = "The total number of files uploaded: " + data.filesUploaded
				+ "\n" + "The total number of errors while uploading: "
				+ data.errors + "\n" + "The total number of bytes uploaded: "
				+ data.allBytesLoaded + "\n"
				+ "The average speed of all uploaded files: " + data.speed;
		alert("event:" + event + "\n" + msg);
	}else{
		var msg = "文件成功上传：<b style='color:green;'>" + data.filesUploaded + "</b> 个<br>" +
				  "上传失败：<b style='color:green;'>" + data.errors + "</b> 个<br>" +
				  "总上传字节数：<b style='color:green;'>" + data.allBytesLoaded + "</b><br>" + 
				  "平均上传速度：<b style='color:green;'>" + data.speed + "</b> kb/s";
		alertMsg.correct(msg);
	}
}
/**
 * http://www.uploadify.com/documentation/
 * 单个文件上传后的信息处理
 * @param {Object}
 *            event
 * @param {Object}
 *            queueID
 * @param {Object}
 *            fileObj
 * @param {Object}
 *            response
 * @param {Object}
 *            data
 */
function uploadifyComplete(event, queueId, fileObj, response, data) {
	AKF.ajaxDone(AKF.jsonEval(response));
}

/**
 * http://www.uploadify.com/documentation/
 * 上传错误处理
 * @param {Object}
 *            event
 * @param {Object}
 *            queueID
 * @param {Object}
 *            fileObj
 * @param {Object}
 *            errorObj
 */
function uploadifyError(event, queueId, fileObj, errorObj) {
	alert("event:" + event + "\nqueueId:" + queueId + "\nfileObj.name:"
			+ fileObj.name + "\nerrorObj.type:" + errorObj.type
			+ "\nerrorObj.info:" + errorObj.info);
}

var uploadifyLang = new Array();

uploadifyLang['buttonText'] = '选择文件';
uploadifyLang['fileTypeDesc'] = 'All Files (*.*)';
uploadifyLang['replaceMsg1'] = '文件';
uploadifyLang['replaceMsg2'] = '已经在上传队列中';
uploadifyLang['replaceMsg3'] = '是否继续上传?';
uploadifyLang['existsMsg1'] = '文件';
uploadifyLang['existsMsg2'] = '已经上传服务器';
uploadifyLang['existsMsg3'] = '是否继续上传?';
uploadifyLang['errorMsg1'] = '选择的文件不符合要求:';
uploadifyLang['errorMsg2'] = '上传文件的大小总和超过限制';
uploadifyLang['errorMsg3'] = '上传文件的数目超过限制';
uploadifyLang['errorMsg4'] = '文件';
uploadifyLang['errorMsg5'] = '大小超过设定的最大值';
uploadifyLang['errorMsg6'] = '是空的';
uploadifyLang['errorMsg7'] = '不是允许上传的文件类型';
uploadifyLang['errorString1'] = '异常';
uploadifyLang['errorString2'] = 'HTTP 错误';
uploadifyLang['errorString3'] = '上传的URL地址错误';
uploadifyLang['errorString4'] = 'I/O异常';
uploadifyLang['errorString5'] = '安全异常';
uploadifyLang['errorString6'] = '上传已达到极限';
uploadifyLang['errorString7'] = '超过上传限制';
uploadifyLang['errorString8'] = '失败';
uploadifyLang['errorString9'] = '文件找不到';
uploadifyLang['errorString10'] = '验证错误';
uploadifyLang['errorString11'] = '已结束';
uploadifyLang['errorString12'] = '停止';
uploadifyLang['uploadComplete'] = "完成";