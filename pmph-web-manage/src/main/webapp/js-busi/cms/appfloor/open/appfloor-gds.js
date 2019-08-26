$(function(){
	advertise_gds.init();
});

var advertise_gds = {
	
	init : function(){//初始化
		var siteId = $("#siteId").val();
		//初始化列表
		$("#dataGridTable").initDT({
	        'pTableTools' : false,
	        'pSingleCheckClean' : false,
	        'pCheck' : "multi",
	        'pAutoload' : false,
	        'pCheckColumn' : false, //是否显示单选/复选框的列
	        "sAjaxSource": $webroot + 'common/querygds?siteId='+siteId,
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
				{ "mData": "prop1010", "sTitle":"版次","sWidth":"80px","bSortable":false},
				//{ "mData": "shopName", "sTitle":"所属店铺","sWidth":"120px","bSortable":false},
				{"targets": -1,"mData": "id","sTitle":"操作","sWidth":"80px","sClass": "left",
					"mRender": function(data,type,row){
						return "<a href='javascript:void(0)' onclick='advertise_gds.selectInfo(\""+(row.id||"")+"\",\""+(row.gdsName||"")+"\")'>选定</a> ";
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
			advertise_gds.search();
		});
		//绑定重置按钮
		$('#btnFormReset').click(function(){
			$("#catgCode").attr('catgCode',""); 
			$("#isbn").val('');
			ebcForm.resetForm('#searchForm');
			$('#shopId').val(shopId);
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
		
		advertise_gds.search();
	},
	//查询方法
	search : function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		/*p.push({ 'name': 'catgCode','value' : $("#catgCode").attr('catgCode') });
		p.push({'name':'isbn','value':$("#isbn").val()});*/
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

