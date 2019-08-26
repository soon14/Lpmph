//页面初始化模块
$(function() {
	var pInit = function() {
		var init = function() {
	
			// //绑定提交按钮事件
			 $('#giftSkuSearchBtn').unbind("click").click(function(){
				 
				   if($("input[name='gdsId']")){
						var gdsId=$("input[name='gdsId']").val();
						if(gdsId && gdsId!=""){
							var r = /^[0-9]*$/;
							if(!r.test(gdsId)){
								eDialog.alert('商品编码只能是整数！');
								return false;
							}
						}
					}
				    
				    var  p = ebcForm.formParams($('#giftSkuForm'));
					var shopId = $("#shopId").val();
				    var url = GLOBAL.WEBROOT + '/seller/gift/gridskulist?shopId=' + shopId;
				
				 
					$('#giftSkuListDiv').load(url,p);
			 });
				

			$("#giftSkuResetBtn").unbind("click").click(function() {
				$("#giftSkuForm")[0].reset();
			}); 

		};
		return {
			init : init
		};
	};
	pageConfig.config({
		// 指定需要加载的插件，名称请参考requirejs.common.js中定义的插件名称，注意大小写
		plugin : [ ],
		// 指定页面
		init : function() {
			var giftSkuList = new pInit();
			giftSkuList.init();
		}
	});
});
