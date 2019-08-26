$(function() {

	$("#dataGridTable")
			.initDT(
					{
	        'pTableTools' : false,
	        'pSingleCheckClean' : false,
	        'pCheck' : "multi",
	        'pAutoload' : false,
	        'pCheckColumn' : false, //是否显示单选/复选框的列
	        "sAjaxSource": GLOBAL.WEBROOT + '/gdsguess/querygds',
	        //指定列数据位置
	        "aoColumns": [
	        	{ "mData": "id", "sTitle":"商品编码","bVisible":false,"sWidth":"40px","bSortable":false},
				{ "mData": "gdsName", "sTitle":"商品名称","sWidth" : "100px","bSortable":false},
				{ "mData": "imageUrl", "sTitle":"图片","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return "<img src='"+data+"' width='120' height='50'>";
				}},
				{ "mData": "gdsTypeName", "sTitle":"商品类型","sWidth":"100px","bSortable":false},
				{ "mData": "prop1001", "sTitle":"作者","sWidth":"100px","bSortable":false},
				{ "mData": "prop1002", "sTitle":"ISBN","sWidth":"100px","bSortable":false},
				{ "mData": "prop1005", "sTitle":"出版日期","sWidth":"100px","bSortable":false},
				{ "mData": "prop1010", "sTitle":"版次","sWidth":"100px","bSortable":false},
				//{ "mData": "shopName", "sTitle":"所属店铺","sWidth":"120px","bSortable":false},ta,type,row){
				{ "mData": "id","sTitle":"操作","sWidth":"80px","sClass": "left",
					"mRender": function(data,type,row){
										var optStr = "<span><a href='javascript:void(0)' onclick='GdsGuess.gdsSelect(\""
												+ row.id
												+ "\",\""
												+ row.gdsName
												+ "\")'>选择</a></span>";

										return optStr;
									}
				}
	        ],
	        "eDbClick" : function(){
	        	//edit();
	        }
		});

	$('#isDefault').click(function() {
		$('#relationGroup').html("");
	});

	$('#noDefault')
			.click(
					function() {
						$('#relationGroup')
								.html(
										"<div class='formSep'>"
												+ "<div class='control-group' >"
												+ "<label class='control-label'><span style='color:red;'>* </span>关联分类：</label>"
												+ "<div class='controls'>"
												+ "<input type='text' id='relatedCatgName' name='relatedCatgName' class='input-large required' placeholder='选择关联分类' value='' readonly='readonly'/>"
												+ "<input type='hidden' id='relatedCatgCode' name='relatedCatgCode' value=''/>"
												+ "<button type='button' class='btn btn-info' id='catgChoose'><i class='icon-magic'></i>选择</button>"
												+ "<span class='help-inline' style='color: red;'>默认配置不需要选择关联分类，否则关联分类必选</span>"
												+ "</div>" + "</div>"
												+ "</div>");
					});

	$("#catgChoose").live(
			'click',
			function() {
				bDialog.open({
					title : '分类选择',
					width : 350,
					height : 550,
					url : GLOBAL.WEBROOT
							+ "/goods/category/open/catgselect?catgType=1&catlogId=1",
					callback : function(data) {
						if (data && data.results && data.results.length > 0) {
							var _catgs = data.results[0].catgs;
							for ( var i in _catgs) {
								$("#relatedCatgName").val(_catgs[i].catgName);
								$("#relatedCatgCode").val(_catgs[i].catgCode);
							}
						}
					}
				});
			});
	$('#btnFormSave').click(
			function() {
				if (!$("#baseInfoForm").valid())
					return false;

				$.eAjax({
					url : GLOBAL.WEBROOT + "/gdsguess/editsave",
					data : ebcForm.formParams($("#baseInfoForm")),
					success : function(returnInfo) {
						if (returnInfo.resultFlag == 'ok') {
							;
							eDialog.success('保存成功！', {
								buttons : [ {
									caption : "确定",
									callback : function() {
										window.location.href = GLOBAL.WEBROOT
												+ "/gdsguess";
									}
								} ]
							});
						} else {
							eDialog.alert(returnInfo.resultMsg);
						}
					}
				});
			});

	$('#btnFormSearch').click(function() {
		if (!$("#relationForm").valid())
			return false;
		var p = ebcForm.formParams($("#relationForm"));
		$('#dataGridTable').gridSearch(p);
	});

	$('#btnFormReset').click(function() {
		ebcForm.resetForm('#relationForm');
	});

	$('#btnReturn').click(function() {
		window.location.href = GLOBAL.WEBROOT + '/gdsguess';
	});
	
	$("#btnFormSearch").trigger("click");

});

var GdsGuess = {
	gdsSelect : function(Id, gdsName) {

		$("#gdsId").val(Id);
		$("#relatedName").val(gdsName);

	},

	choosePlatCatg : function() {
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
								$("#platCatgs").val(_catgs[i].catgCode);
							}
						}
					}
				});
	}

};
