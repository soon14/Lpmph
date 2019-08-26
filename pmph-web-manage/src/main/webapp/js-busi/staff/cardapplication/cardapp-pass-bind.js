$(function(){
	var custLevel = $('#custLevelCode').val();
	//表格数据初始化
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/cardapplication/bindlist',
        "params" : [{name:"custLevelCode",value:$('#custLevelCode').val()}],
        //指定列数据位置
        "aoColumns": [
            { "mData": "custCardId", "sTitle":"会员卡编码","sWidth":"150","bSortable":false},      
            { "mData": "custLevelName", "sTitle":"会员卡等级","sWidth":"50","bSortable":false},      
			{ "mData": "cardStatus", "sTitle":"会员卡状态","sWidth":"50","bSortable":false,"mRender": function(data,type,row){
                if (data == '01') {
               	 	return "未发实体卡";
                } else if (data == '02') {
               	 	return "已发实体卡";
                } else if (data  == '03') {
                	return "作废";
                } else {
                	return "未发实体卡";
                }
	        }}
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	//查询
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	//绑定
	$('#btnFormBind').click(function(){
		var val = $('#dataGridTable').getSelectedData();
		if(val && val.length==1){
			$.eAjax({
				url : GLOBAL.WEBROOT + "/cardapplication/passbind/save",
				data : {'custCardId':val[0].custCardId,'staffId':$('#staffId').val(),
						'custLevelCode':$('#custLevelCode').val(),'id':$('#cardAppId').val()},
				datatype:'json',
				success : function(returnInfo) {
					eDialog.success(returnInfo.resultMsg,{
						buttons:[{
							caption:"确定",
							callback:function(){
								$('#dataGridTable').gridReload();
								bDialog.closeCurrent();
					        }
						}]
					}); 
				}
			});
			
		}else if(val && val.length>1){
			eDialog.alert('只能选择一条记录进行操作！');
		}else if(!val || val.length==0){
			eDialog.alert('请选择至少一条记录进行操作！');
		}
	});
});