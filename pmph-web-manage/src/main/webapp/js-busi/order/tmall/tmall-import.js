$(function() {
	//绑定订单级文件属性的上传事件===========start
	 $('.upFileTyle').each(function(){
	    	$(this).live('change',function(e){
	    		var fileSize =  this.files[0].size;
	    		if(fileSize/1048576 >10){
	    			eDialog.alert("文件最大不能超过10M");
	    			return;
	    		}
	    		var path = $(this).val();
	    		ordFileInfo.uploadFile(this, path);
				e.preventDefault();
	    	});
	    }); 
	 
	//绑定订单明细文件属性的上传事件===========start
	 $('.upFileTyle2').each(function(){
	    	$(this).live('change',function(e){
	    		var fileSize =  this.files[0].size;
	    		if(fileSize/1048576 >10){
	    			eDialog.alert("文件最大不能超过10M");
	    			return;
	    		}
	    		var path = $(this).val();
	    		ordDetailFileInfo.uploadFile(this, path);
				e.preventDefault();
	    	});
	    }); 
	
}); 
function cancelFile(obj){
	$(obj).parent().find('input[type="text"]').removeAttr('disabled');
	$(obj).parent().find('input[type="text"]').val('');
	$(obj).parent().find('.file-wrap').show();
	$(obj).hide();
} 
function importFile(obj){
	$("#detailSpan1").hide();
	$("#detailSpan2").hide();
	var id = $(obj).attr('id');
	var url = "";
	if(id=='import1'){
		url = GLOBAL.WEBROOT + '/order/tmall/ordImport';
		var file1 = $.trim($("#file1").val());
		if(file1 == ''){
			eDialog.alert("请选择上传文件");
			return;
		}
		$("#fileId").val(file1);
		$("#moduleName").val("天猫订单");
	}else if(id=='import2'){
		url = GLOBAL.WEBROOT + '/order/tmall/ordDetailImport';
		var file2 = $.trim($("#file2").val());
		if(file2 == ''){
			eDialog.alert("请选择上传文件");
			return;
		}
		$("#fileId").val(file2);
		$("#moduleName").val("天猫订单明细");
	}
	var ary = new Array(); 
	var isStatus = true;
	$.eAjax({
		url : url,
		data :ebcForm.formParams($("#importForm")),
		async : false,
		type : "post",
		dataType : "json",
		success : function(data) {
			if(data.flag == '0'){
				eDialog.alert("导入成功");
    			isStatus = false;
			}else if(data.flag == '1'){
				isStatus = true;
			}else if(data.flag == '2'){
				eDialog.alert(data.msg);
    			isStatus = false;
			} 
		}
	});
	 
	if(isStatus){
		if(id=='import1'){
			$("#detailSpan1").show();
			$("#detail1").attr("href",GLOBAL.WEBROOT+'/order/tmall/importTMFailure/'+$.trim($("#file1").val()));
		}else if(id=='import2'){
			$("#detailSpan2").show();
			$("#detail2").attr("href",GLOBAL.WEBROOT+'/order/tmall/importTMDetailFailure/'+$.trim($("#file2").val()));
		}  
	}
	
}
//订单级文件
var ordFileInfo = {
		uploadFile : function(object1, path){
			var filepath = path;
	    	filepath=(filepath+'').toLowerCase(); 
	    	var url = GLOBAL.WEBROOT + '/order/tmall/uploadfile';
	    	var propId = $(object1).attr('propId');
	    	var callback = function(data, status) {
	    		/** 上传成功，隐藏上传组件，并显示该图片 */
	    		if (data.success == "ok") {
	    			var _this = $("#file"+propId);
	    			_this.val(data.resultMap.vfsId);
	    			_this.attr('disabled',true);
	    			_this.parent().find('.file-wrap').hide();
	    			_this.parent().find('button').show();
	    		} else {
	    			eDialog.error(data.message);
	    		}
	    		ordFileInfo.bindFileUpload();
	    		$("#importForm").valid();
	    	};
	    	ordFileInfo.ajaxFileUpload(url, false, $(object1).attr('id'), "POST", "json", callback);
		},
		bindFileUpload : function(){
			 $('.upFileTyle').each(function(){
			    	$(this).live('change',function(e){
			    		var fileSize =  this.files[0].size;
			    		if(fileSize/1048576 >10){
			    			eDialog.alert("文件最大不能超过10M");
			    			return;
			    		}
			    		var path = $(this).val();
			    		ordFileInfo.uploadFile(this, path);
						e.preventDefault();
			    	});
			    });
		},
		ajaxFileUpload : function(url, secureuri, fileElementId, type, dataType,
				callback) {
			$.ajaxFileUpload({
						url : url, // 用于文件上传的服务器端请求地址
						secureuri : secureuri, // 一般设置为false
						fileElementId : fileElementId, // 文件上传空间的id属性 <input
						// type="file" id="imageFile"
						// name="imageFile" />
						type : type, // get 或 post
						dataType : dataType, // 返回值类型
						success : callback, // 服务器成功响应处理函数
						error : function(data, status, e) // 服务器响应失败处理函数
						{
							eDialog.alert(e);
						}
					});
		}
}; 
//订单明细文件
var ordDetailFileInfo = {
		uploadFile : function(object1, path){
			var filepath = path;
	    	filepath=(filepath+'').toLowerCase(); 
	    	var url = GLOBAL.WEBROOT + '/order/tmall/uploadfile2';
	    	var propId = $(object1).attr('propId');
	    	var callback = function(data, status) {
	    		/** 上传成功，隐藏上传组件，并显示该图片 */
	    		if (data.success == "ok") {
	    			var _this = $("#file"+propId);
	    			_this.val(data.resultMap.vfsId);
	    			_this.attr('disabled',true);
	    			_this.parent().find('.file-wrap').hide();
	    			_this.parent().find('button').show();
	    		} else {
	    			eDialog.error(data.message);
	    		}
	    		ordDetailFileInfo.bindFileUpload();
	    		$("#importForm").valid();
	    	};
	    	ordDetailFileInfo.ajaxFileUpload(url, false, $(object1).attr('id'), "POST", "json", callback);
		},
		bindFileUpload : function(){
			 $('.upFileTyle2').each(function(){
			    	$(this).live('change',function(e){
			    		var fileSize =  this.files[0].size;
			    		if(fileSize/1048576 >10){
			    			eDialog.alert("文件最大不能超过10M");
			    			return;
			    		}
			    		var path = $(this).val();
			    		ordDetailFileInfo.uploadFile(this, path);
						e.preventDefault();
			    	});
			    });
		},
		ajaxFileUpload : function(url, secureuri, fileElementId, type, dataType,
				callback) {
			$.ajaxFileUpload({
						url : url, // 用于文件上传的服务器端请求地址
						secureuri : secureuri, // 一般设置为false
						fileElementId : fileElementId, // 文件上传空间的id属性 <input
						// type="file" id="imageFile"
						// name="imageFile" />
						type : type, // get 或 post
						dataType : dataType, // 返回值类型
						success : callback, // 服务器成功响应处理函数
						error : function(data, status, e) // 服务器响应失败处理函数
						{
							alert(e);
						}
					});
		}
}