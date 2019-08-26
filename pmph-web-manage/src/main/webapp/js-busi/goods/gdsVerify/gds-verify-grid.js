$(function(){
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : true,
        'pCheck' : 'multi', //multi  single
        'pCheckColumn' : true, //是否显示单选/复选框的列
        "sAjaxSource": GLOBAL.WEBROOT + '/gdsverify/gridlist',
        //指定列数据位置
        "aoColumns": [
			{ "mData": "gdsId", "sTitle":"商品编码","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "gdsName", "sTitle":"商品名称","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "catgName", "sTitle":"主分类名称","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "isbn", "sTitle":"ISBN","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "guidePrice", "sTitle":"商品指导价","sWidth":"80px","sClass": "center","bSortable":false,
				"mRender": function(data,type,row){
					var str = (data/100).toFixed(2) + '';
					var intSum = str.substring(0,str.indexOf("."));//取到整数部分.replace( /\B(?=(?:\d{3})+$)/g, ',' )
					var dot = str.substring(str.length,str.indexOf("."));//取到小数部分
					var ret = intSum + dot;
					return ret;
				}
			},
			{ "mData": "publictionDate", "sTitle":"出版日期","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "author", "sTitle":"作者","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "operateStaffCode", "sTitle":"操作人","sWidth":"80px","sClass": "center","bSortable":false},
			{ "mData": "operateTime", "sTitle":"操作时间","sWidth":"80px","sClass": "center","bSortable":false,
				"mRender": function(data,type,row){
					return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
				}
			},
			{ "mData": "operateType", "sTitle":"操作类型","sWidth":"80px","sClass": "center","bSortable":false,
				"mRender": function(data,type,row){
					if(data=='11'){
						return "上架提交";
					}else if(data=='99'){
						return "删除提交";
					}else if(data=='33'){
						return "更新提交";
					}
				}
			},
			{ "mData": "verifyStatus", "sTitle":"审核状态","sWidth":"80px","sClass": "center","bSortable":false,
				"mRender": function(data,type,row){
					if(data=='03'){ 
						return "提交待复审";
					}else if(data=='01'){
						return "审核通过";
					}else if(data=='02'){
						return "审核拒绝";
					}
				}
			},
			{"targets": -1,"data": "shopId","sTitle":"操作","sWidth":"80px","sClass": "center",
				"mRender": function(data,type,row){
					if(row.verifyStatus=='03'){
						return "<a href='javascript:void(0);' class='verify' gdsId='"+row.gdsId+"'>审核</a>";
					}else{
						return "-";
					}
				}
			}
        ],
        "params" : [{name : 'shopId',value : $("#shopId").val()},{name : 'verifyStatus',value : '03'}]
	});
	GdsVerify.initValid();
	//查询
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		p.push({
			'name' : 'catgCode',
			'value' : $("#catgCode").attr('catgCode')
		});
		$('#dataGridTable').gridSearch(p);
	});
	//重置
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
		$("#catgCode").attr('catgCode','');
	});
	//Tab换页查询
	$("#myTab").children().each(function(){
		$(this).bind("click",function(){
			var id =$(this).attr("id");
			if(id == "tab1"){
				verifyStatus ="03";
				$("#btn_verify_gds").show();
				GdsVerify.gridList(verifyStatus);
			    $('#myTab li:eq(0) a').tab('show');
			}else if(id == "tab2"){
				verifyStatus ="01";
				$("#btn_verify_gds").hide();
				GdsVerify.gridList(verifyStatus);
				$('#myTab li:eq(1) a').tab('show');
			}else if(id == "tab3"){
				verifyStatus ="02";
				$("#btn_verify_gds").hide();
				GdsVerify.gridList(verifyStatus);
				$('#myTab li:eq(2) a').tab('show');
			}
		});
	});
	/**
	 * 审核事件绑定
	 */
	$(".verify").live('click',function(e){
		eNav.setSubPageText('审核商品');
		window.location.href = GLOBAL.WEBROOT +"/gdsinfoentry/togdsdetail?gdsVerifyFlag=1&gdsId="+$(this).attr('gdsId');
		e.preventDefault();
	});
	$("#catgCode").click(function(){
		catlogId = '1';
		if($("#ifGdsScore").val()=='1'){
			catlogId = '2';
		}
		bDialog.open({
            title : '分类选择',
            width : 350,
            height : 550,
            params:{multi : false},
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
	//批量审核
	$("#btn_verify_gds").click(function(){
		var ids = $('#dataGridTable').getCheckIds();
		if(!ids || ids.length==0){
			eDialog.alert('请选择至少选择一条商品记录进行操作！');
			return ;
		}else if(ids[0]==undefined){
			eDialog.alert('请选择至少选择一条商品记录进行操作！');
			return ;
		}	
		var params = {};
		params.shopId = $("#shopId").val();
		params.operateId = ids.join(",");
		bDialog.open({
            title : '商品审核',
            width : 550,
            height : 350,
            params:params,
            url : GLOBAL.WEBROOT+"/gdsverify/togdsverifywindow",
            callback:function(data){
            	if(data && data.results && data.results.length > 0 ){
                    window.location.href = GLOBAL.WEBROOT+ '/gdsverify?shopId='+ data.results[0].param;
            	}
            }
        });
	});
});
var GdsVerify = {
		gridList : function(verifyStatus){
			var p = ebcForm.formParams($("#searchForm"));
			p.push({ 'name': 'verifyStatus','value' : verifyStatus });
			p.push({
				'name' : 'catgCode',
				'value' : $("#catgCode").attr('catgCode')
			});
			$.gridLoading({"el":"#gridLoading","messsage":"正在加载中...."});
			$('#dataGridTable').gridSearch(p);
			$.gridUnLoading({"el":"#gridLoading"});
		},
		initValid : function(){
			jQuery.validator.addMethod("compareTime", function(value, element) {
				var endTimeId = element.id;
				var startTime= $("#startTime").val();
				var endTime = $("#" + endTimeId).val();
				if(startTime =="" || endTime ==""){
					return true;
				}
				if ($.trim(startTime).length == 10) {
					startTime = startTime + " 00:00:00";
				}
				if ($.trim(endTime).length == 10) {
					endTime = endTime + " 00:00:00";
				}
				var reg = new RegExp('-', 'g');
				startTime = startTime.replace(reg, '/');// 正则替换
				endTime = endTime.replace(reg, '/');
				startTime = new Date(parseInt(Date.parse(startTime), 10));
				endTime = new Date(parseInt(Date.parse(endTime), 10));
				if (startTime > endTime) {
					return false;
				} else {
					return true;
				}
			}, "<font color='#E47068'>截至时间不能早于起始时间</font>");
			$("#searchForm").validate({
				rules : {
					endTime : {
						compareTime : true
					},
					gdsId : {
						digits : true
					}
				},
				messages : {
					endTime : {
						compareTime : "<span style='color:red'>截至时间不能早于起始时间</span>"
					},
					gdsId : {
						digits : "<span style='color:red'>只能输入整数</span>"
					}
				},
				//	          debug: false,  //如果修改为true则表单不会提交  
				submitHandler : function() {
				}
			});
		}
};