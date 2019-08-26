/**
*wangbh
*图片压缩上传功能
**/
$(function(){
	$('.w').on('change','#upfile',function(){
		var filepath = $(this).val();
		filepath=(filepath+'').toLowerCase();
    	var regex = new RegExp(
    			'\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$|\\.(bmp)$', 'gi');
    	/** 上传图片文件格式验证 */
    	if (!filepath || !filepath.match(regex)) {
    		eDialog.alert('请选择图片格式为(.jpg,.png,.jpeg,.gif,.bmp)');
    		uploadfile.value = "";
    		return;
    	}
		lrz(this.files[0], {width: 500}).then(function (rst) {
            // 把处理的好的图片给用户看看呗（可选）
            var img = new Image();
            img.src = rst.base64; //base64字符串
            return rst;
        }).then(function (rst) {
            // 这里该上传给后端啦
            /* ==================================================== */
        	uploadImg(rst.base64);
            /* ==================================================== */

            return rst;
        }).always(function () {
            // 不管是成功失败，这里都会执行
        	$('#upfile').val('');
        });
		
	});
	
	var uploadImg = function(base64){
		 $.eAjax({
  	        url: GLOBAL.WEBROOT + '/mobilecusthistory/uploadimg',
  	        data: {img:base64},
  	        datatype: 'json',
  	        async: true,
  	        success: function(returnInfo) {
  	        	ChatBoxUtil.uploadCallBack(returnInfo);
  	        }
          });
	}
});