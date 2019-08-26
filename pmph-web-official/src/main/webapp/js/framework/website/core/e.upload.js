/* ===========================================================
 * e.upload.js
 * ===========================================================
 * Terry 
 * created : 2015.08.28
 * 
 * 基于uploadify插件进行封装的上传功能插件
 * 
 * 更新记录：
 */
!function ($) {
	"use strict"; // 使用严格模式Javascript
	
	
	//初始化上插件
	$.fn.eUploadInit = function(p){
		return this.each(function(){
			var _this = $(this);
			var params = $.extend({}, $.fn.eUpload.defaults, p);
			params.onQueueComplete = function(queueData){
				if(typeof(p.onQueueComplete)!=undefined && $.isFunction(p.onQueueComplete)){
					p.onQueueComplete(queueData);
				}else{
					$.fn.eUpload.queueComplete(queueData);
				}
				params.callback();
			};
			_this.uploadify(params);
		});
	};
	//执行文件上传
	//params:传递到后台的参数集，格式为{}JSON对象
	$.fn.eUpload = function(p){
		return this.each(function(){
			var params = {};
			if(p && p.params) params = p.params;
			$(this).uploadify('settings','formData',params);
			$(this).uploadify('upload','*');
		});
	};
	//上传错误的处理
	$.fn.eUpload.uploadError = function(event, queueId, fileObj, errorObj){
		alert("event:" + event + "\nqueueId:" + queueId + "\nfileObj.name:"
				+ fileObj.name + "\nerrorObj.type:" + errorObj.type
				+ "\nerrorObj.info:" + errorObj.info);
	};
	//所有上传队列完成后的处理
	$.fn.eUpload.queueComplete = function(queueData){
		if(queueData){
			var msg = "文件成功上传：<b style='color:green;'>" + queueData.uploadsSuccessful + "</b> 个<br>" +
			  "上传失败：<b style='color:red;'>" + queueData.uploadsErrored + "</b> 个<br>";
			if(window.eDialog){
				eDialog.alert(msg,$.noop,'confirmation');
			}else{
				alert(msg);
			}
		}
	};
	//默认参数
	$.fn.eUpload.defaults = {
		'swf' : $webroot + 'js/jquery/uploadify/uploadify.swf',
		'uploader' : '',
		'fileObjName' : 'uploadFileObj',//传递到后台，用于接收上传文件的对象名
		'queueID' : "attachmentFileQueue",//队列内容显示元素ID指定
		'buttonText' : '选择上传文件',  //按钮显示文本
		'multi' : false,               //是否为批量上传
		'auto' : false,                //是否选择完文件后自动开始上传
		'queueSizeLimit' : 20,          //文件待上传队列大小，默认设置20个
		'fileSizeLimit':'10240KB',      //上传文件大小限制
		'method' : 'post',              //上传方式
		'height' : 25,                  //Flash按钮高度
		'width' : 100,                  //Flash按钮宽度
		'onUploadError' : $.fn.eUpload.uploadError,
		'onUploadSuccess' : $.noop,     //单个文件上传成功的回调
		'callback' : $.noop             //上传完成后用户处理回调
	};
}(window.jQuery);