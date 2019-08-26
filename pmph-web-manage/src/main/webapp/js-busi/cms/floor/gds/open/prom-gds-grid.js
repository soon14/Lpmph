$(function(){
	gds_grid.init();
});
var gds_grid = {
	init : function(){//初始化
		
		//获取已选过的数据，从编辑页面传递过来
		var selected = batchAddObj.getParam("selectedGds")||"";
		selected=","+selected+",";
		
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
				}},
				/*{ "mData": "prop1005", "sTitle":"出版日期","sWidth":"100px","bSortable":false},
				{ "mData": "updateTime", "sTitle":"上架日期","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd");
				}},
				{ "mData": "prop1010", "sTitle":"版次","sWidth":"100px","bSortable":false},*/
				//{ "mData": "shopName", "sTitle":"所属店铺","sWidth":"120px","bSortable":false},ta,type,row){
				{"targets": -1,"mData": "gdsId","sTitle":"操作","sWidth":"80px","sClass": "left",
					"mRender": function(data,type,row){
						if(selected.indexOf(","+row.gdsId+",") >= 0){
							return "已选";
						}
						return "<a href='javascript:void(0)' onclick='gds_grid.selectGds(\""+(row.gdsId||"")+"\",\""+SearchObj.HTMLEncode((row.gdsName||""),true)+"\",\""+(row.parentId||"")+"\")'>选定</a> ";
					}
				}
	        ],
	        "eDbClick" : function(){
	        	//edit();
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
			gds_grid.selectMoreInfo();
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
		
		/*$("#catgCode").click(function(){
			catlogId = '2';
			if($("#siteId").val()!='2'){
				catlogId = '1';
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
		});*/
		
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
		
		gds_grid.search();
	},
	//查询
	search : function (){
		if(!$("#gds_grid_searchForm").valid()) return false;
		var p = ebcForm.formParams($("#gds_grid_searchForm"));
		$('#gds_grid_dataGridTable').gridSearch(p);
	},
	//绑定选定
	selectGds : function (gdsId,gdsName,parentId){
		bDialog.closeCurrent({
			"gdsIds":gdsId,
			"gdsNames":gdsName,
			"promIds" : parentId
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
		var promIds = '';
//		alert(gdsNames);
		for(var i=0;i<datas.length;i++){
			gdsIds += datas[i].gdsId+"、";
			promIds += datas[i].parentId+"、";
			gdsNames += datas[i].gdsName+"、";
		}
		if(gdsIds != null && gdsIds != ""){
			gdsIds=gdsIds.replace(/、$/,'');//替换最后一个‘、’
		}
		if(gdsNames != null && gdsNames != ""){
			gdsNames=gdsNames.replace(/、$/,'');
		}
		if(gdsNames != null && gdsNames != ""){
			promIds=promIds.replace(/、$/,'');
		}
		bDialog.closeCurrent({
			"gdsIds":gdsIds,
			"promIds":promIds,
			"gdsNames":gdsNames
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

