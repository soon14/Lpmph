// 页面初始化模块
$(function() {
	var pInit = function() {
		var init = function() {

			var ajaxFileUpload = function(url, secureuri, fileElementId, type,
					dataType, callback) {
				$.ajaxFileUpload({
							url : url, // 用于文件上传的服务器端请求地址
							secureuri : secureuri, // 一般设置为false
							fileElementId : fileElementId, // 文件上传空间的id属性
															// <input
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
			};
			
			var uploadImage = function(object, path,id) {
				// var Img = new Image();
				// Img.src = object.value;
				// alert(object.height+":"+object.width);
				// return false;
				var filepath = path;
				filepath = (filepath + '').toLowerCase();
				var regex = new RegExp(
						'\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$', 'gi');
				/** 上传图片文件格式验证 */
				if (!filepath || !filepath.match(regex)) {
					eDialog.alert('请选择图片格式为(.jpg,.png,.jpeg,.gif).');
					uploadfile.value = "";
					return;
				}
				var url = GLOBAL.WEBROOT + '/seller/gdsmedia/uploadmedia';
				var callback = function(returnInfo) {
					/** 上传成功，隐藏上传组件，并显示该图片 */
					if (returnInfo.success == "ok") {
//						var id = $(object).parent().parent().find("img").attr("id");
//						alert(id)
						if(id=="id0"){
							$("#id0").attr("src", returnInfo.resultMap.bmpUrl);
							$("#imgid0").val(returnInfo.resultMap.vfsId);
						}else if(id=="id1"){
							$("#id1").attr("src", returnInfo.resultMap.bmpUrl);
							$("#imgid1").val(returnInfo.resultMap.vfsId);
//							alert(returnInfo.resultMap.bmpUrl)
						}else if(id=="id2"){
							$("#id2").attr("src", returnInfo.resultMap.bmpUrl);
							$("#imgid2").val(returnInfo.resultMap.vfsId);
						}else if(id=="id3"){
							$("#id3").attr("src", returnInfo.resultMap.bmpUrl);
							$("#imgid3").val(returnInfo.resultMap.vfsId);
						}else if(id=="id4"){
//							alert(id);
//							alert(returnInfo.resultMap.bmpUrl);
							$("#id4").attr("src", returnInfo.resultMap.bmpUrl);
							$("#imgid4").val(returnInfo.resultMap.vfsId);
						}
						$("#mediaPic").val(returnInfo.resultMap.vfsId);
						$("#pictrueName").val(returnInfo.resultMap.bmpName);
					} else {
						eDialog.error(returnInfo.message);
					}
				};
				ajaxFileUpload(url, false, $(object).attr('id'), "POST",
						"json", callback);
			};

			var saveGdsMedia = function(obj) {
				if (!$("#detailInfoForm").valid())
					return false;
				if(!$("#mediaPic").val()){
					eDialog.error('请上传图片！');
				return false;
				}
				var btn = $(obj);
				btn.button('loading');// 设置按钮为处理状态，并为中读，不允许点击
				$.eAjax({
					url : GLOBAL.WEBROOT + "/seller/gdsmedia/savemedia",
					data : ebcForm.formParams($("#detailInfoForm")),
					success : function(returnInfo) {
						btn.button('reset');
						if (returnInfo.resultFlag == 'ok') {
							eDialog.success('图片新增成功！', {
								onClose : function() {
									$('#mediaSearchBtn').trigger('click');
									bDialog.closeCurrent({"success":true});
								}
							});
						} else {
							eDialog.error('图片保存失败！');
						}
					},
					exception : function() {
						btn.button('reset');
					}
				});
			};
			$("#picture0").live("change", function(o) {
						var path = $(this).val();
						var id="id0";
						uploadImage(this, path,id);
					});
			$("#picture1").live("change", function(o) {
				var path = $(this).val();
				var id="id1";
				uploadImage(this, path,id);
			});
			$("#picture2").live("change", function(o) {
				var path = $(this).val();
				var id="id2";
				uploadImage(this, path,id);
			});
			$("#picture3").live("change", function(o) {
				var path = $(this).val();
				var id="id3";
				uploadImage(this, path,id);
			});
			$("#picture4").live("change", function(o) {
				var path = $(this).val();
				var id="id4";
				uploadImage(this, path,id);
			});

			$("#btn_submit").live("click", function() {
				var applyType = $('#applyType').val();
				var conUrl = null;

				if(applyType == '0'){
					conUrl = GLOBAL.WEBROOT+'/seller/order/refund/confirmRefund';
				} else if(applyType == '1'){
					conUrl = GLOBAL.WEBROOT+'/seller/order/backgds/confirmRefund';
				} else {
					return ;
				}
				var data = ebcForm.formParams($("#backrefundForm"));
//						$.gridLoading({"message":"正在加载中...."});
				$.eAjax({
					url:conUrl,
					data:data,
					success:function(result){
//								$.gridUnLoading();
						if(result&&result.resultFlag=='ok'){
							eDialog.alert('操作成功',function(){
								bDialog.closeCurrent({'result' : 'ok'});
							},'confirmation');
						}else{
							eDialog.alert(result.resultMsg,function(){
								bDialog.closeCurrent({'result' : 'error'});
							},'error');
						}
					},
					failure:function(){
//								$.gridUnLoading();
						bDialog.closeCurrent({'result' : 'error'});
					}
				});
					});
			$("#btn_close").live("click", function() {
					
						bDialog.closeCurrent({'result' : 'close'});
			});
					
		};
		return {
			init : init
		};
	};
	pageConfig.config({
		// 指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : [],
		// 指定页面
		init : function() {
			var mediaAdd = new pInit();
			mediaAdd.init();
		}
	});
});