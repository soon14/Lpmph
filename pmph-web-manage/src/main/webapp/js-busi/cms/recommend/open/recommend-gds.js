$(function(){
	//设置列表是否多选
	var type = $("#type").val();
//	alert(type);
	var pCheck = "multi";
	if(type == '1'){//当为‘推荐商品’时单选。
		pCheck = "single";
	}
	recommend_gds.init(pCheck);
});

var recommend_gds = {
	init : function(pCheck){//初始化
		//初始化列表
		$("#dataGridTable").initDT({
	        'pTableTools' : false,
	        'pSingleCheckClean' : false,
	        'pCheck' : pCheck,
	        'pAutoload' : false,
//	        'pCheck' : "multi",
	        "sAjaxSource": $webroot + 'common/querygds',
	        //指定列数据位置
	        "aoColumns": [
	        	{ "mData": "id", "sTitle":"商品编码","bVisible":false,"sWidth":"40px","bSortable":false},
				{ "mData": "gdsName", "sTitle":"商品名称","bSortable":false},
				{ "mData": "imageUrl", "sTitle":"图片","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
					return "<img src='"+data+"' width='120' height='50'>";
				}},
				{ "mData": "gdsTypeName", "sTitle":"商品类型","sWidth":"100px","bSortable":false},
				{ "mData": "prop1001", "sTitle":"作者","sWidth":"100px","bSortable":false},
				{ "mData": "prop1002", "sTitle":"ISBN","sWidth":"100px","bSortable":false},
				{ "mData": "prop1005", "sTitle":"出版日期","sWidth":"100px","bSortable":false},
				{ "mData": "updateTime", "sTitle":"上架日期","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd");
				}},
				{ "mData": "prop1010", "sTitle":"版次","sWidth":"100px","bSortable":false},
				//{ "mData": "shopName", "sTitle":"所属店铺","sWidth":"120px","bSortable":false},
				{"targets": -1,"mData": "id","sTitle":"操作","sWidth":"80px","sClass": "left",
					"mRender": function(data,type,row){
						return "<a href='javascript:void(0)' onclick='recommend_gds.selectInfo(\""+(data||'')+"\",\""+(row.gdsName||"")+"\")'>选定</a> ";
					}
				}
	        ],
	        "eDbClick" : function(){
	        	//update();
	        }
		});
		//绑定查询按钮
		$('#btnFormSearch').click(function(){
			recommend_gds.search();
		});
		//绑定重置按钮
		$('#btnFormReset').click(function(){
			$("#catgCode").attr('catgCode',""); 
			$("#isbn").val('');
			ebcForm.resetForm('#searchForm');
		});
		//绑定选定按钮
		$('#btn_code_select').click(function(){
			recommend_gds.selectMoreInfo();
		});
		
		$("#catgCode").click(function(){
			var catlogId = '1';
//			if($("#siteId").val()!='1'){
//				catlogId = '2';
//			}
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
		recommend_gds.search();
	},
	//查询方法
	search : function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		p.push({ 'name': 'catgCode','value' : $("#catgCode").attr('catgCode') });
		p.push({'name':'isbn','value':$("#isbn").val()});
		$('#dataGridTable').gridSearch(p);
	},
	//绑定选定单个
	selectInfo : function (ids,gdsNames){
		bDialog.closeCurrent({
			"gdsIds":ids,
			"gdsNames":gdsNames
		});
	},
	//绑定选定多个
	selectMoreInfo : function (){
		var datas = $('#dataGridTable').getSelectedData();
		if(!datas || datas.length==0){
			eDialog.alert('请选择至少选择一条记录！');
			return;
		}
		var gdsIds = '';
		var gdsNames = '';
//		alert(gdsNames);
		for(var i=0;i<datas.length;i++){
			gdsIds += datas[i].id+"、";
			gdsNames += datas[i].gdsName+"、";
		}
		if(gdsIds != null && gdsIds != ""){
			gdsIds=gdsIds.replace(/、$/,'');//替换最后一个‘、’
		}
		if(gdsNames != null && gdsNames != ""){
			gdsNames=gdsNames.replace(/、$/,'');
		}
		bDialog.closeCurrent({
			"gdsIds":gdsIds,
			"gdsNames":gdsNames
		});
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

