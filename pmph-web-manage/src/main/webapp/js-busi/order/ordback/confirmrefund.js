$(function(){

	//返回的文件信息
	/*var fileInfo = {};
	//单个文件上传成功后的回调处理，实现上传结果处理等信息均在此处理
	//最终结果可在onQueueComplete中统一显示
	var uploadSuccess = function(file,data,response){
		if(response){
			if(data){//data为后台返回的JSON内容
				var tmp = JSON.parse(data);
				//var fileInfo = {};
				//检查文件是否上传成功
				if($.fn.eUpload.onUploadSuccessCheck(tmp)){
					if(tmp && $.isPlainObject(tmp) && typeof(tmp.fileId) != 'undefined' && typeof(tmp.url) != 'undefined' ){
						fileInfo.fileId = tmp.fileId;
						fileInfo.url = tmp.url;
						//fileResults.push(fileInfo);
					}
				}
			}
		}
	};*/

	//更多的参数请参考e.upload.js中的详细参数
	$("div.fileInputBtn").each(function(i,n){
		var _this = this;
		var li = $(_this).closest('li');
		$(this).eUploadBaseInit({
			//回调
			'callback' : function(fileInfo){
				if(fileInfo){
					$('img',$(li)).attr('src',fileInfo.url);
					var id = $('img',$(li)).attr("id");
					var imgid = "#img"+id;
					$(imgid).val(fileInfo.fileId);
				}
			}
		});
	});

	$('#btn_submit').click(function(){
		
		var applyType = $('#applyType').val();
		var conUrl = null;
		if(applyType == '0'){
			conUrl = GLOBAL.WEBROOT+'/ordrefund/confirmRefund';
		} else if(applyType == '1'){
			conUrl = GLOBAL.WEBROOT+'/ordback/confirmRefund';
		} else {
			return ;
		}
		var data = ebcForm.formParams($("#backrefundForm"));
		$.gridLoading({"message":"正在加载中...."});
		$.eAjax({
			url:conUrl,
			data:data,
			success:function(result){
				$.gridUnLoading();
				if(result&&result.resultFlag=='ok'){
					eDialog.alert('操作成功',function(){
						bDialog.closeCurrent({result:'ok'});
					},'confirmation');					
				}else{
					eDialog.alert(result.resultMsg,function(){
						bDialog.closeCurrent();
					},'error');
					
				}
			},
			failure:function(){
				$.gridUnLoading();
				bDialog.closeCurrent();
			}
		});
	});
	$('#btn_close').click(function(){
		bDialog.closeCurrent({result:'ok'});
	});
});