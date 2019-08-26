$(function(){
	gds_batch_grid.init();
	//批量选择  点击复选框事件
	batchAddObj.bindItem($(".batch-add-item"),"selectingGds");//selectingGds 为选择后数据存储字段。
});
var gds_batch_grid = {
	init : function(){//初始化
//		var siteId = $("#siteId").val();
		//初始化列表
		$("#gds_grid_dataGridTable").initDT({
	        'pTableTools' : false,
	        'pSingleCheckClean' : false,
	        'pCheck' : "multi",
	        'pAutoload' : false,
	        'pCheckColumn' : false, //是否显示单选/复选框的列
	        "sAjaxSource": $webroot + 'common/querypromgds',
	        //指定列数据位置
	        "aoColumns": [
	            { "mData": "selectGds", "sTitle":"选择","sWidth":"40px","bSortable":false,"mRender": function(data,type,row){
	      			return "<input type='checkbox' class='batch-add-item' data-id='"+row.gdsId+"' data-gds-name='"+SearchObj.HTMLEncode(row.gdsName)+"' data-prom-id='"+row.parentId+"' data-prom-name='"+SearchObj.HTMLEncode(row.promTheme)+"' data-is-prom='1'>";
	      		}},
	        	{ "mData": "gdsId", "sTitle":"商品编码","sWidth":"40px","bSortable":false},
				{ "mData": "gdsName", "sTitle":"商品名称","bSortable":false,"mRender": function(data,type,row){
					return SearchObj.HTMLEncode(data);
				}},
				{ "mData": "mainPic", "sTitle":"商品图片","bSortable":false,"mRender": function(data,type,row){
					if(data){
						return "<img src='"+data+"'/>";
					}
				}},
				{ "mData": "status", "sTitle":"商品状态","bSortable":false,"mRender": function(data,type,row){
					if(data=='0'){
						return "待上架";
					}else if(data=='11'){
						return "已上架";
					}else if(data=='22'){
						return "已下架";
					}else if(data == '99'){
						return "已删除";
					}else{
						return data;
					}
				}},
				/*
				{ "mData": "gdsTypeName", "sTitle":"商品类型","sWidth":"100px","bSortable":false},
				{ "mData": "prop1001", "sTitle":"作者","sWidth":"100px","bSortable":false},*/
				{ "mData": "isbn", "sTitle":"ISBN","sWidth":"100px","bSortable":false},
				{ "mData": "shopName", "sTitle":"店铺名称","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return SearchObj.HTMLEncode(data);
				}},
				{ "mData": "parentId", "sTitle":"促销编码","bVisible":false,"bSortable":false},
				{ "mData": "promTheme", "sTitle":"促销名称","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return SearchObj.HTMLEncode(data);
				}},
				{ "mData": "promTypeCode", "sTitle":"促销类别代码","bVisible":false,"sWidth":"100px","bSortable":false},
				{ "mData": "promType", "sTitle":"促销类别","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return SearchObj.HTMLEncode(data);
				}}
	        ],
	        "eDbClick" : function(){
	        	//edit();
	        },
	        "eDrawComplete":function(){
	        	//批量选择
	        	batchAddObj.initCheckBox("selectedGds","selectingGds");//批量增加  指定已选中及正处于选择状态的变量
	        }
		});
		//绑定查询按钮
		$('#gds_grid_btnFormSearch').click(function(){
			gds_batch_grid.search();
		});
		//绑定重置按钮
		$('#gds_grid_btnFormReset').click(function(){
			$("#catgCode").attr('catgCode',""); 
			$("#isbn").val('');
			ebcForm.resetForm('#gds_grid_searchForm');
		});
		//绑定选定按钮
		$('#btn_code_select').click(function(){
			gds_batch_grid.selectGds();
		});
		
		//点击空格enter查询
		$("#gds_grid_gdsName").keydown(function(e){
			if(e.keyCode==13){
				gds_batch_grid.search();
			}
		});
		$("#isbn").keydown(function(e){
			if(e.keyCode==13){
				gds_batch_grid.search();
			}
		});
		
		$("#siteId").live("change",function(){
			// 分类回填
			$('#catgName').mcDropDown({
				backfillCatgName : 'catgName',
				backfillCatgCode : 'catgCode'
			});
			//初始化分类下拉
			$('#catgName').mcDropDown.change();
		});
		
		$("#siteId").trigger("change");
		
		//让分类不能被选中
		$("#catgName").focus(function(){
			$(this).blur();
			});
		
		gds_batch_grid.search();
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
	/**
	 * 根据url打开窗口
	 * @param {} url : 链接路径
	 * @param {} linkType : href,open
	 * @param {} openType : _blank..
	 */
	windowOpenUrl : function (url,linkType,openType){
		if(linkType=="href"){
			window.location.href = url;
		}else if(linkType=="open"){
			window.open(url,openType);
		}
	},
	/**
	 * 判断字数
	 * @param {} thisObj  当前对象
	 * @param {} showObj  要显示提示的对象
	 */
	checkLen : function (thisObj,showObj,maxChars){
		//var maxChars = 200;
		if (thisObj.value.length > maxChars) thisObj.value = thisObj.value.substring(0,maxChars);  
		var curr = maxChars - thisObj.value.length;  
		$("#"+showObj).html(curr.toString());
	}
};

