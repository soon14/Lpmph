$(function() {
	// 页面业务逻辑处理内容
	var pageInit = function() {

		var init = function() {
			//获得当前弹出窗口对象
			var _dlg = bDialog.getDialog();
			//获得父窗口传递给弹出窗口的参数集
			var _param = bDialog.getDialogParams(_dlg);
			//获得父窗口设置的回调函数
			//var _callback = bDialog.getDialogCallback(_dlg);
	        	
			$('#btnFormReset').click(function(){
				ebcForm.resetForm('#searchForm');
			});

			$('#btnFormSearch').unbind("click");
			//绑定提交按钮事件
			$('#btnFormSearch').click(function() {
				var shopId = $("#shopId").val();
				var staffCode = $("#staffCode").val();
				$('#stockListDiv').load(GLOBAL.WEBROOT + '/seller/custservice/subacctlist',
						{
				     "shopId":shopId,
				     "staffCode":staffCode
					});
			});
			//初始化加载load数据
			$('#btnFormSearch').click();
			
			var modifyBiz = function(){
				var val = $('input[type=radio][name=shopName]:checked');
				if(val && val.length==1){ 
					bDialog.closeCurrent(val.val());
				}else if(!val || val.length==0){
					eDialog.alert('请至少选择一个客户进行操作！');
				}
			};
			
			$('#btnFormCheck').click(function(){
				modifyBiz();
			});
			
			$("#shopId").change(function(){
				//初始化加载load数据
				$('#btnFormSearch').click();
			});
		};
		return {
			init : init
		};
	};
	pageConfig.config({
		// 指定需要加载的插件，名称请参考common中定义的插件名称，注意大小写
		plugin : ['bForm'],
		// 指定页面
		init : function() {
			var p = new pageInit();
			p.init();
		}
	});
});

