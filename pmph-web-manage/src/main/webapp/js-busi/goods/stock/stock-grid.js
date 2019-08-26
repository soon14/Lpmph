

$(function() {
	var _initShop = $("select[name=shopId]").val();

	$("#dataGridTable").initDT({
		'pCheckColumn' : false,
		'pTableTools' : false,
		'pSingleCheckClean' : false,
		"sAjaxSource" : GLOBAL.WEBROOT + '/goods/stockinfo/listStock',
		"params" : [{
					name : 'shopId',
					value : _initShop
				}],
		// 指定列数据位置
		"aoColumns" : [{
					"mData" : "id",
					"sTitle" : "库存id",
					"sWidth" : "60px",
					"bSortable" : false,
					"bVisible" : false
				}, {
					"mData" : "gdsId",
					"sTitle" : "商品id",
					"sWidth" : "60px",
					"bSortable" : false,
					"bVisible" : false
				}, {
					"mData" : "skuId",
					"sTitle" : "单品编码",
					"sWidth" : "70px",
					"bSortable" : false

				}, {
					"mData" : "gdsName",
					"sTitle" : "商品名称",
					"sWidth" : "70px",
					"bSortable" : false,
					"mRender": function(data,type,row){
						  if(row.skuStatus == '99'){
								//<a href='javascript:void(0)' onclick='gdsRestart(this)'>重新启用</a>
							 return data;
						  }
						  return '<a  target="_blank" href="'+row.gdsDetailUrl+'">'+data+'</a>';
					}
				}, {
					"mData" : "productNo",
					"sTitle" : "ISBN",
					"sWidth" : "80px",
					"bSortable" : false
				}, {
					"mData" : "typeName",
					"sTitle" : "商品类型",
					"sWidth" : "70px",
					"bSortable" : false
				},

				{
					"mData" : "propStr",
					"sTitle" : "单品属性串",
					"sWidth" : "90px",
					"bSortable" : false
				}, {
					"mData" : "repCode",
					"sTitle" : "仓库编码",
					"sWidth" : "70px",
					"bSortable" : false
				}, {
					"mData" : "repType",
					"sTitle" : "仓库类型",
					"sWidth" : "80px",
					"bSortable" : false
				}, {

					"sTitle" : "单品库存量",
					"sWidth" : "90px",
					"bSortable" : false,
					"mRender" : function(data, type, row) {
						return "<div>现有库存量:" + row.realCount + "</div>"
								+ "<div>预占库存量:" + row.preOccupyCount + "</div>"
								+ "<div>可用库存量:" + row.availCount + "</div>"
								+ "<div>已发货库存量:" + row.sendCount + "</div>"
						        + "<div>工厂库存:" + (row.facStock != -1 ? row.facStock : "") + "</div>";

					}
				}, {
					"mData" : "skuStatus",
					"sTitle" : "单品状态",
					"sWidth" : "80px",
					"bSortable" : false
				}, {
					"mData" : "opt",
					"sTitle" : "操作",
					"sWidth" : "80px",
					"bSortable" : false,
					"mRender" : function(data, type, row) {
						return "<span><a href='#' name='stockrow' stockid = '"
								+ row.id + "' gdsid = '" + row.gdsId
								+ "' shopid = '" + row.shopId
								+ "' avaicount = '" + row.availCount
								+ "'  >调整库存</a> </span>";

					}
				}],
         "createdRow": function ( row, data, index ) {
		     $('td', row).eq(1).css('word-break',"break-all");
	     },
	});

	$("a[name='stockrow']").live("click", function(e) {
				var shopId = $(this).attr("shopid");
				var id = $(this).attr("stockid");
				var gdsId = $(this).attr("gdsid");
				var availCount = $(this).attr("avaicount");
				bDialog.open({
							title : '库存调整',
							width : 520,
							height : 250,
							url : GLOBAL.WEBROOT + "/goods/stockinfo/optStock",

							params : {
								"stockId" : id,
								"shopId" : shopId,
								"gdsId" : gdsId,
								"availCount" : availCount
							},
							callback : function(data) {
								var p = ebcForm.formParams($("#searchForm"));
								$('#dataGridTable').gridSearch(p);

							}
						});

			});

	$('#btnFormSearch').click(function() {
				if (!$("#searchForm").valid())
					return false;

				var p = ebcForm.formParams($("#searchForm"));
				p.push({
							'name' : 'catgCode',
							'value' : $("#catgCode").attr('catgCode')
						});
				$.gridLoading({
							"el" : "#gridLoading",
							"messsage" : "正在加载中...."
						});
				$('#dataGridTable').gridSearch(p);
				$.gridUnLoading({
							"el" : "#gridLoading"
						});
			});

	$('#btnFormReset').click(function() {
				ebcForm.resetForm('#searchForm');
				var catgCode = $('#catgCode');
				if (catgCode.attr("catgcode")) {
					catgCode.removeAttr("catgcode");
				}
			});

	$('#btn_code_opt').click(function() {
				window.location.href = GLOBAL.WEBROOT
						+ '/goods/stockrep/optStock';
			});

	$("#catgCode").click(function() {
		
		if(undefined == $('#shopId').val() || "" == $('#shopId').val()){
			 eDialog.alert('店铺必须选择!');
			 return;
		}
		
		bDialog.open({
			title : '分类选择',
			width : 350,
			height : 550,
			params : {multi : false,shopIds:[$('#shopId').val()]},
			url : GLOBAL.WEBROOT + "/goods/category/open/catgselect?catgType=1",
			callback : function(data) {
				if (data && data.results && data.results.length > 0) {
					var _catgs = data.results[0].catgs;
					var size = _catgs.length;
					for (var i = 0; i < size; i++) {
						var obj = _catgs[i];
						$("#catgCode").val(obj.catgName);
						$("#catgCode").attr('catgCode', obj.catgCode);
					}
				}
			}
		});

	});

});