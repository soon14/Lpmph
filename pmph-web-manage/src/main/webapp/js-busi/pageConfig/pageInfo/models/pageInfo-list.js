$(function(){
	pageinfo_list.init();
});

var pageinfo_list = {
	
	init : function(){//初始化
		var siteId = $("#siteId").val();
		//初始化列表
		$("#dataGridTable").initDT({
	        'pTableTools' : false,
	        'pSingleCheckClean' : false,
	        'pCheck' : "multi",
	        'pAutoload' : false,
	        'pCheckColumn' : false, //是否显示单选/复选框的列
	        "sAjaxSource": $webroot + 'pageInfo/querypageinfo?siteId='+siteId,
	        //指定列数据位置
	        "aoColumns": [
	        	{ "mData": "id", "sTitle":"ID","bVisible":true,"sWidth":"50px","bSortable":true},
	        	{ "mData": "pageName", "sTitle":"页面名称","sWidth":"120px","bSortable":false},	
	        	{ "mData": "platformTypeZH", "sTitle":"平台类型","sWidth":"100px","bSortable":false},	
				{ "mData": "pageTypeZH", "sTitle":"页面类型","sWidth":"100px","bVisible":true,"bSortable":false},
				{ "mData": "siteUrl", "sTitle":"路径","bVisible":true,"bSortable":false},
				{ "mData": "statusZH", "sTitle":"状态","sWidth":"100px","bVisible":true,"bSortable":false},
				{ "mData": "createTime", "sTitle":"创建时间","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd");
				}},
				{"targets": -1,"data": "id","sTitle":"操作","sWidth":"80px","sClass": "left",
					"mRender": function(data,type,row){
						return "<a href='javascript:void(0)' onclick='pageinfo_list.selectInfo(\""+row.id+"\",\""+row.pageName+"\")'>选定</a> ";
					}
				}
	        ],
	        "eDbClick" : function(){
	        	//update();
	        }
		});
		var shopId = $('#shopId').val();
		//绑定查询按钮
		$('#btnFormSearch').click(function(){
			pageinfo_list.search();
		});
		//绑定重置按钮
		$('#btnFormReset').click(function(){
			ebcForm.resetForm('#searchForm');
		});
		
		pageinfo_list.search();
	},
	//查询方法
	search : function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		p.push({ 'name': 'catgCode','value' : $("#catgCode").attr('catgCode') });
		p.push({'name':'isbn','value':$("#isbn").val()});
		$('#dataGridTable').gridSearch(p);
	},
	//绑定选定
	selectInfo : function (id,title){
		bDialog.closeCurrent({
			"linkUrl": id,
			"infoTitle":title
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

