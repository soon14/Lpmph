$(function(){
	gds_grid.init();
	
	//批量选择  点击复选框事件
	batchAddObj.bindItem($(".batch-add-item"),"selectingGds");//selectingGds 为选择后数据存储字段。
});

var gds_grid = {
	init : function(){//初始化
		var siteId = $("#siteId").val();
		//初始化列表
		$("#gds_grid_dataGridTable").initDT({
	        'pTableTools' : false,
	        'pSingleCheckClean' : false,
	        'pCheck' : "multi",
	        'pAutoload' : false,
	        'pCheckColumn' : false, //是否显示单选/复选框的列
	        "sAjaxSource": $webroot + 'common/querygds?siteId='+siteId,
	        //指定列数据位置
	        "aoColumns": [
	            { "mData": "selectGds", "sTitle":"选择","sWidth":"40px","bSortable":false,"mRender": function(data,type,row){
					return "<input type='checkbox' class='batch-add-item' data-id='"+row.id+"' data-gds-name='"+SearchObj.HTMLEncode(row.gdsName)+"' data-is-prom='0'>";
				}},
	        	{ "mData": "id", "sTitle":"商品编码","bVisible":false,"sWidth":"40px","bSortable":false},
				{ "mData": "gdsName", "sTitle":"商品名称","bSortable":false,"mRender": function(data,type,row){
					return SearchObj.HTMLEncode(data);
				}},
				{ "mData": "imageUrl", "sTitle":"图片","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return "<img src='"+data+"' width='120' height='50'>";
				}},
				{ "mData": "gdsTypeName", "sTitle":"商品类型","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return SearchObj.HTMLEncode(data);
				}},
				{ "mData": "prop1001", "sTitle":"作者","sWidth":"100px","bSortable":false},
				{ "mData": "prop1002", "sTitle":"ISBN","sWidth":"100px","bSortable":false},
				{ "mData": "prop1005", "sTitle":"出版日期","sWidth":"100px","bSortable":false},
				{ "mData": "updateTime", "sTitle":"上架日期","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd");
				}},
				{ "mData": "prop1010", "sTitle":"版次","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return SearchObj.HTMLEncode(data);
				}}
				//{ "mData": "shopName", "sTitle":"所属店铺","sWidth":"120px","bSortable":false},ta,type,row){
				
	        ],
	        "eClick" : function(rowData,rowElement){
	        },
	        "eDrawComplete":function(){
	        	//批量选择
	        	batchAddObj.initCheckBox("selectedGds","selectingGds");//批量增加  指定已选中及正处于选择状态的变量
	        }
		});
		//绑定查询按钮
		$('#gds_grid_btnFormSearch').click(function(){
			gds_grid.search();
		});
		//绑定重置按钮
		$('#gds_grid_btnFormReset').click(function(){
			$("#catgCode").attr('catgCode',""); 
			$("#isbn").val('');
			ebcForm.resetForm('#gds_grid_searchForm');
		});
		//绑定选定按钮
		$('#btn_code_select').click(function(){
			gds_grid.selectGds();
		});
		
		//点击空格enter查询
		$("#gds_grid_gdsName").keydown(function(e){
			if(e.keyCode==13){
				gds_grid.search();
			}
		});
		$("#isbn").keydown(function(e){
			if(e.keyCode==13){
				gds_grid.search();
			}
		});
		// 分类回填
		$('#catgName').mcDropDown({
			backfillCatgName : 'catgName',
			backfillCatgCode : 'catgCode'
		});
		//初始化分类下拉
		$('#catgName').mcDropDown.change();
		
		//让分类不能被选中
		$("#catgName").focus(function(){
			$(this).blur();
			});
		
		gds_grid.search();
	},
	//查询
	search : function (){
		if(!$("#gds_grid_searchForm").valid()) return false;
		var p = ebcForm.formParams($("#gds_grid_searchForm"));
		$('#gds_grid_dataGridTable').gridSearch(p);
	},
	//绑定选定
	selectGds : function (){
		var selecting = batchAddObj.getParam("selectingGds");
		bDialog.closeCurrent({
			"selectingGds":selecting || {}
		});
	},
};

