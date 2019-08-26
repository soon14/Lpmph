// 页面初始化模块
$(function() {
	var pInit = function() {
		var init = function() {
			// //绑定提交按钮事件
			$('#btnFormSearch').unbind("click").click(function() {
				var shopId = $("#shopId").val();
				var  p = ebcForm.formParams($('#searchForm'));
				
				var url = GLOBAL.WEBROOT
						+ '/seller/custservice/gridlist?ids=' + shopId;
				$('#stockListDiv').load(url,p);
			});
			
			//使失效
			$('#btn_code_invalid').click(function(){ 
				var ids = $('input[type=radio][name=shopName]:checked');
				if(ids && ids.length==1){
					if (ids.attr('status') == '0') {
						eDialog.alert("您好，该会员状态已为失效，无须操作！");
						return;
					}
					eDialog.confirm("您确认要把该条记录置为失效吗？", {
						buttons : [{
							caption : '确认',
							callback : function(){
								$.eAjax({
									url : GLOBAL.WEBROOT+'/seller/custservice/custinvalid?id=' + ids.val(),
									success : function(returnInfo) {
										if(returnInfo.resultMsg){
											eDialog.alert(returnInfo.resultMsg);
											$('#btnFormSearch').click();//刷新列表
										}
									}
								});
							}
						},{
							caption : '取消',
							callback : $.noop
						}]
					});
				}else if(ids && ids.length>1){
					eDialog.alert('只能选择一条记录进行操作！');
				}else if(!ids || ids.length==0){
					eDialog.alert('请选择至少一条记录进行操作！');
				}
			});
			
			
			//使生效
			$('#btn_code_valid').click(function(){
				var ids = $('input[type=radio][name=shopName]:checked');
				if(ids && ids.length==1){
					if (ids.attr('status') == '1') {
						eDialog.alert("您好，该会员状态已为正常，无须操作！");
						return;
					}
					eDialog.confirm("您确认要把该条记录状态恢复为正常吗？", {
						buttons : [{
							caption : '确认',
							callback : function(){
								$.eAjax({
									url : GLOBAL.WEBROOT+'/seller/custservice/custvalid?id=' + ids.val(),
									success : function(returnInfo) {
										if(returnInfo.resultMsg){
											eDialog.alert(returnInfo.resultMsg);
											$('#btnFormSearch').click();//刷新列表
										}
									}
								});
							}
						},{
							caption : '取消',
							callback : $.noop
						}]
					});
				}else if(ids && ids.length>1){
					eDialog.alert('只能选择一条记录进行操作！');
				}else if(!ids || ids.length==0){
					eDialog.alert('请选择至少一条记录进行操作！');
				}
			});
			//新增
			$('#btn_code_add').click(function(){
				window.location.href = GLOBAL.WEBROOT+'/seller/custservice/add';
			});
			//编辑
			$('#btn_code_edit').click(function(){
				modifyBiz();
			});
			//重置
			$('#btnFormReset').click(function(){
				ebcForm.resetForm('#searchForm');
			}); 
			var modifyBiz = function(){
				var ids = $('input[type=radio][name=shopName]:checked');
				if(ids && ids.length==1){
					$('#id').val(ids.val());
					$('#editForm').submit();
				}else if(ids && ids.length>1){
					eDialog.alert('只能选择一条记录进行操作！');
				}else if(!ids || ids.length==0){
					eDialog.alert('请选择至少一条记录进行操作！');
				}
			};
			
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
					var stockList = new pInit();
					stockList.init();
				}
			});
});
