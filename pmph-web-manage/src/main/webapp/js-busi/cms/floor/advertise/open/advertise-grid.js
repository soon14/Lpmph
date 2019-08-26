$(function(){
	advertise_grid.init();
});

var advertise_grid = {
	init : function(){//初始化
		//初始化列表
		$("#advertise_grid_dataGridTable").initDT({
	        'pTableTools' : false,
	        'pSingleCheckClean' : false,
	        'pCheck' : "multi",
	        "sAjaxSource": $webroot + 'advertise/gridlist?status=1',
	        //指定列数据位置
	        "aoColumns": [
	        	{ "mData": "id", "sTitle":"id","bVisible":false,"sWidth":"80px","bSortable":true},
				{ "mData": "advertiseTitle", "sTitle":"广告语","sWidth":"100px","bSortable":false},
				{ "mData": "placeName", "sTitle":"内容位置","sWidth":"80px","bSortable":false},
				/*{ "mData": "subSystem", "bVisible":false,"sTitle":"所属系统","sWidth":"80px","bSortable":false},*/
				{ "mData": "subSystemZH", "bVisible":true,"sTitle":"所属系统","sWidth":"80px","bSortable":false},
				{ "mData": "shopName", "sTitle":"店铺","sWidth":"80px","bSortable":false},
				/*{ "mData": "linkType", "bVisible":false,"sTitle":"链接类型","sWidth":"60px","bSortable":false},*/
				{ "mData": "linkTypeZH", "bVisible":true,"sTitle":"链接类型","sWidth":"60px","bSortable":false},
				/*{ "mData": "status", "bVisible":false,"sTitle":"状态","sWidth":"60px","bSortable":false},*/
				{ "mData": "statusZH", "bVisible":true,"sTitle":"状态","sWidth":"60px","bSortable":false},
				{ "mData": "createTime", "sTitle":"创建时间","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
				}},
				/*{
					"targets": -1,"data": null,"sTitle":"操作","sWidth":"80px","sClass": "center","defaultContent": "<a href='#' id='gdsUp' onclick='cli()'>上架</a> | <a href='#'>编辑</a> | <a href='#'>删除</a>","bSortable":false
				}*/
				{"targets": -1,"mData": "id","sTitle":"操作","sWidth":"100px","bSortable":false,"sClass": "left",
					"mRender": function(data,type,row){
						//alert(data+"---"+type+"---"+row);
						return "<a href='javascript:void(0)' onclick='advertise_grid.selectAdvertise(\""+(row.id||"")+"\",\""+(row.advertiseTitle||"")+"\")'>选定</a> ";
					}
				}
	        ],
	        "eDbClick" : function(){
	        	//edit();
	        }
		});
		//绑定查询按钮
		$('#advertise_grid_btnFormSearch').click(function(){
			if(!$("#advertise_grid_searchForm").valid()) return false;
			var p = ebcForm.formParams($("#advertise_grid_searchForm"));
			p.push({ 'name': 'catgCode','value' : $("#catgCode").attr('catgCode') });
			p.push({'name':'isbn','value':$("#isbn").val()});
			$('#advertise_grid_dataGridTable').gridSearch(p);
		});
		//绑定重置按钮
		$('#advertise_grid_btnFormReset').click(function(){
			ebcForm.resetForm('#advertise_grid_searchForm');
		});
		
		$("#catgCode").click(function(){//平台分类
			catlogId = '1';
			if($("#siteId").val()!='1'){
				catlogId = '2';
			}
			bDialog.open({
	            title : '分类选择',
	            width : 300,
	            height : 350,
	            params:{},
	            url : GLOBAL.WEBROOT+"/goods/category/open/catgselect?catgType=1&catlogId="+catlogId,
	            callback:function(data){
	            	if(data && data.results && data.results.length > 0 ){
	                    var _catgs = data.results[0].catgs;
						var size = _catgs.length;
						for(var i =0;i<size;i++){
							var obj = _catgs[i];
							$("#catgCode").val(obj.catgName);
							$("#catgCode").attr('catgCode',obj.catgCode);
						}
					}
	            }
	        });
		});
	},
	//绑定选定
	selectAdvertise : function (advertiseId,advertiseTitle){
		bDialog.closeCurrent({
			"advertiseId":advertiseId,
			"advertiseTitle":advertiseTitle
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

