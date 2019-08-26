$(function() {

	$("#dataGridTable")
			.initDT(
					{
						'pTableTools' : false,
						'pSingleCheckClean' : true,
						'pCheck' : 'multi',
						"sAjaxSource" : GLOBAL.WEBROOT + '/gdsguess/guesslist',

						// 指定列数据位置
						"aoColumns" : [
								{
									"mData" : "id",
									"sTitle" : "猜你喜欢编码",
									"sWidth" : "80px",
									"bSortable" : true,
									"bVisible" : false
								},
								{
									"mData" : "catgName",
									"sTitle" : "应用分类",
									"sWidth" : "80px",
									"sClass" : "center",
									"bSortable" : true

								},
								{
									"mData" : "gdsId",
									"sTitle" : "商品编码",
									"sWidth" : "80px",
									"sClass" : "center",
									"bSortable" : false
								},
								{
									"mData" : "gdsName",
									"sTitle" : "商品名称",
									"sWidth" : "80px",
									"sClass" : "center",
									"bSortable" : false
								},

								{
									"mData" : "ifDefault",
									"sTitle" : "是否默认配置",
									"sWidth" : "80px",
									"bSortable" : false,
									"bVisible" : false
								},
								{
									"mData" : "ifDefault",
									"sTitle" : "是否默认配置",
									"sWidth" : "80px",
									"sClass" : "center",
									"bSortable" : false,
									"mRender" : function(data, type, row) {
										if (data == "0")
											var optStr = "否";
										else
											var optStr = "是";

										return optStr;
									}
								},
								{
									"mData" : "id",
									"sTitle" : "操作",
									"sWidth" : "80px",
									"sClass" : "center",
									"bSortable" : false,
									"mRender" : function(data, type, row) {
										var optStr = "<span><a href='#' onclick='gdsEdit(this,"
												+ data
												+ ")'>编辑</a>|<a href='#' onclick=\"gdsRemove(this,"
												+ data + ")\">删除</a></span>";

										return optStr;
									}
								}

						]

					});

	// 查询
	$('#btnFormSearch').click(function() {
		if (!$("#searchForm").valid())
			return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	// 重置
	$('#btnFormReset').click(function() {
		ebcForm.resetForm('#searchForm');
	});

	$('#btn_code_add').click(function() {
		eNav.setSubPageText('新增猜你喜欢');
		window.location.href = GLOBAL.WEBROOT + '/gdsguess/guessadd';
	});

	// 批量删除
	$('#btn_code_remove').click(function() {
		GdsGuess.gdsbatchRemove();
	});

});

function gdsRemove(obj, id) {
	// alert(id);
	GdsGuess.gdsRemove(obj, id);
}
function gdsEdit(obj, id) {
	GdsGuess.gdsEdit(obj, id);
}

var GdsGuess = {

	gdsRemove : function(obj, id) { // 删除

		var param = {
			operateId : id
		};
		eDialog.confirm("您确认删除该记录吗？", {
			buttons : [ {
				caption : '确认',
				callback : function() {
					$.eAjax({
						url : GLOBAL.WEBROOT + "/gdsguess/gdsbatchremove",
						type : "POST",
						data : param,
						datatype : 'json',
						success : function(returnInfo) {
							if (returnInfo.resultFlag == 'ok') {
								eDialog.success('删除成功！');
								$('#dataGridTable').gridReload();
							} else {
								eDialog.alert(returnInfo.resultMsg);
							}
						}
					});
				}
			}, {
				caption : '取消',
				callback : $.noop
			} ]
		});
	},
	gdsbatchRemove : function() { // 批量删除
		var ids = $('#dataGridTable').getCheckIds();
		if (!ids || ids.length == 0) {
			eDialog.alert('请选择至少选择一个项目进行操作！');
			return;
		}
		var param = {
			operateId : ids.join(",")
		};
		eDialog.confirm("您确认删除该记录吗？", {
			buttons : [ {
				caption : '确认',
				callback : function() {
					$.eAjax({
						url : GLOBAL.WEBROOT + "/gdsguess/gdsbatchremove",
						type : "POST",
						data : param,
						datatype : 'json',
						success : function(returnInfo) {
							if (returnInfo.resultFlag == 'ok') {
								eDialog.success('删除成功！');
								$('#dataGridTable').gridReload();
							} else {
								eDialog.alert(returnInfo.resultMsg);
							}
						}
					});
				}
			}, {
				caption : '取消',
				callback : $.noop
			} ]
		});
	},

	gdsEdit : function(obj, id) {
		eNav.setSubPageText('编辑猜你喜欢');
		window.location.href = GLOBAL.WEBROOT + "/gdsguess/gdsedit?id=" + id;
	},

	chooseCatg : function() {
		bDialog
				.open({
					title : '分类选择',
					width : 350,
					height : 550,
					url : GLOBAL.WEBROOT
							+ "/goods/category/open/catgselect?catgType=1&catlogId=1",
					callback : function(data) {
						if (data && data.results && data.results.length > 0) {
							var _catgs = data.results[0].catgs;
							for ( var i in _catgs) {

								$("#catgName").val(_catgs[i].catgName);
								$("#catgCode").val(_catgs[i].catgCode);
							}
						}
					}
				});
	},

};