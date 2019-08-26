$(function(){
	//表格数据初始化
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/cardapplication/gridlist',
        //指定列数据位置
        "aoColumns": [
            { "mData": "id", "sTitle":"主键id","sWidth":"80px","bSortable":false,"bVisible":false},
            { "mData": "staffId", "sTitle":"用户ID","sWidth":"80px","bSortable":false,"bVisible":false},
            { "mData": "custLevelCode", "sTitle":"会员等级","sWidth":"80px","bSortable":false,"bVisible":false},
            //这里展示的是登录工号，用的是昵称字段来存的，需要注意
            { "mData": "nickName", "sTitle":"会员名","sWidth":"80px","bSortable":false},      
            { "mData": "custLevelName", "sTitle":"申请等级","sWidth":"60px","bSortable":false},         
			{ "mData": "checkStatus", "sTitle":"审核状态","sWidth":"60px","bSortable":false,"mRender": function(data,type,row){
                if (data == '0') {
               	 	return "待审核";
                } else if (data == '1') {
               	 	return "审核通过";
                } else if (data  == '2') {
                	return "审核不通过";
                } else {
                	return "待审核";
                }
	        }},
	        { "mData": "remark", "sTitle":"备注","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
                if (data == null) {
                	return "";
                } else {
                	return "<span title='"+data+"' style='cursor:default'>"+data+"</span>";
                }
                
	        }},  
	        { "mData": "contactName", "sTitle":"联系人","sWidth":"80px","bSortable":false},  
	        { "mData": "contactPhone", "sTitle":"联系人手机","sWidth":"80px","bSortable":false},  
	        { "mData": "contactAddress", "sTitle":"联系人地址","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                if (data == null) {
                	return "";
                } else {
                	return "<span title='"+data+"' style='cursor:default'>"+data+"</span>";
                }
	        }},  
			{ "mData": "createTime", "sTitle":"申请时间","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
				data = ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
				return "<span title='"+data+"' style='cursor:default'>"+data+"</span>";
			}}
        ],
        "eDbClick" : function(){
        }
	});
	
	
	//查询
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	//重置
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});
	//审核通过
	var check_pass = function(){
		var val = $('#dataGridTable').getSelectedData();
		if(val && val.length==1) {
			if (val[0].checkStatus != '0') {
				eDialog.alert('该申请已经审核过了，无法进行重复审核！');
				return;
			}
		    bDialog.open({
				title : '审核通过（绑定会员卡）',
				width : 900,
				height : 500,
				url : GLOBAL.WEBROOT+'/cardapplication/pass?id=' + val[0].id + '&staffId='+val[0].staffId + '&custLevelCode=' + val[0].custLevelCode,
				callback:function(data){
						$('#dataGridTable').gridReload();
					}
			});
		} else if(val && val.length>1){
			eDialog.alert('只能选择一条记录进行操作！');
		} else if(!val || val.length==0){
			eDialog.alert('请选择至少一条记录进行操作！');
		}
	};
	
	
	//审核不通过
	var check_no_pass = function(){
		var val = $('#dataGridTable').getSelectedData();
		if(val && val.length==1) {
			if (val[0].checkStatus != '0') {
				eDialog.alert('该申请已经审核过了，无法进行重复审核！');
				return;
			}
		    bDialog.open({
				title : '审核意见',
				width : 600,
				height : 250,
				scroll :false,
				url : GLOBAL.WEBROOT+'/cardapplication/nopass?id=' + val[0].id + '&staffId=' + val[0].staffId,
				callback:function(data){
						$('#dataGridTable').gridReload();
					}
			});
		} else if(val && val.length>1){
			eDialog.alert('只能选择一条记录进行操作！');
		} else if(!val || val.length==0){
			eDialog.alert('请选择至少一条记录进行操作！');
		}
	};
	
	//审核通过按钮
	$('#btn_code_pass').click(function(){
		check_pass();
	});
	//审核不通过按钮
	$('#btn_code_no_pass').click(function(){
		check_no_pass();
	});
});