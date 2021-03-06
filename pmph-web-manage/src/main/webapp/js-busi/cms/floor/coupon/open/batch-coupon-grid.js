$(function(){
	coupon_batch_grid.init();
	//批量选择  点击复选框事件
	batchAddObj.bindItem($(".batch-add-item"),"selectingCp");//selectingGds 为选择后数据存储字段。
});

var coupon_batch_grid = {
	init : function(){//初始化
		var siteId = $("#siteId").val();
		//初始化列表
		$("#coupon_grid_dataGridTable").initDT({
	        'pTableTools' : false,
	        'pSingleCheckClean' : false,
	        'pCheck' : "multi",
	        'pAutoload' : false,
	        'pCheckColumn' : false, //是否显示单选/复选框的列
	        "sAjaxSource": $webroot + 'common/querycoupon?siteId='+siteId,
	        //指定列数据位置
	        "aoColumns": [
				{ "mData": "selectGds", "sTitle":"选择","sWidth":"40px","bSortable":false,"mRender": function(data,type,row){
					return "<input type='checkbox' class='batch-add-item' data-id='"+row.id+"' data-coup-name='"+row.coupName+"' data-coup-value='"+(row.coupValue <= 0 ? "抵用券":row.coupValue/100)+"' data-coup-type-name='"+row.coupTypeName+"'>";
				}},
	        	{ "mData": "id", "sTitle":"优惠券ID","bVisible":false,"sWidth":"40px","bSortable":false},
	        	{ "mData": "siteName", "sTitle":"站点","bSortable":false},
	        	{ "mData": "shopName", "sTitle":"所属店铺","bSortable":false},
	        	{ "mData": "coupName", "sTitle":"优惠券名称","bSortable":false},
				{ "mData": "coupTypeName", "sTitle":"优惠券类型","bSortable":false},
				{ "mData": "coupValue", "sTitle":"额度","sWidth":"90px","bSortable":false,"mRender":function(data,type,row){ 
					  if(data<=0){
						  return "抵用券";
					  }else{
						  return data/100;
					  }
				}},
				{ "mData": "coupNum", "sTitle":"发行总量","bSortable":false},
				{ "mData": "getNum", "sTitle":"已领取总量","bSortable":false},
				{ "mData": "effTypeName", "sTitle":"日期类型","sWidth":"80px","bSortable":false},
				{ "mData": "activeTime", "sTitle":"生效时间","bSortable":false,"sWidth":"120px","mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
				}},
				{ "mData": "inactiveTime", "sTitle":"失效时间","bSortable":false,"sWidth":"120px","mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
				}},
	        ],
	        "eDbClick" : function(){
	        	//edit();
	        },
	        "eDrawComplete":function(){
	        	//批量选择
	        	batchAddObj.initCheckBox("selectedCp","selectingCp");//批量增加  指定已选中及正处于选择状态的变量
	        }
		});
		//绑定查询按钮
		$('#coupon_grid_btnFormSearch').click(function(){
			coupon_batch_grid.search();
		});
		//绑定重置按钮
		$('#coupon_grid_btnFormReset').click(function(){
			$("#catgCode").attr('catgCode',""); 
			$("#isbn").val('');
			ebcForm.resetForm('#coupon_grid_searchForm');
		});
		//绑定选定按钮
		$('#btn_code_select').click(function(){
			coupon_batch_grid.selectCoupon();
		});
		
		//点击空格enter查询
		$("#coupon_grid_gdsName").keydown(function(e){
			if(e.keyCode==13){
				coupon_batch_grid.search();
			}
		});
		$("#isbn").keydown(function(e){
			if(e.keyCode==13){
				coupon_batch_grid.search();
			}
		});
		
		$("#catgCode").click(function(){
			var catlogId = '2';
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
		});
		
		coupon_batch_grid.search();
		
		//固定时间选择
		$('#effType').change(function(){
			var p1=$(this).children('option:selected').val();
			$('.queryDateCls').hide();
			//清空
			$('#activeTime').val('');
			$('#inactiveTime').val('');
			if(p1==1){
				$('.queryDateCls').show();
			}
		});
	},
	//查询
	search : function (){
		if(!$("#coupon_grid_searchForm").valid()) return false;
		var p = ebcForm.formParams($("#coupon_grid_searchForm"));
		p.push({ 'name': 'catgCode','value' : $("#catgCode").attr('catgCode') });
		p.push({'name':'isbn','value':$("#isbn").val()});
		$('#coupon_grid_dataGridTable').gridSearch(p);
	},
	//绑定选定
	selectCoupon : function (couponId,couponName){
		var selecting = batchAddObj.getParam("selectingCp");
		bDialog.closeCurrent({
			"selectingCp":selecting || {}
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

