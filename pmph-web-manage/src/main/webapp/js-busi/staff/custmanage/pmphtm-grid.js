$(function(){
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/cust/gridlist',
        "params":[{name:"companyId",value:$('#ids').val()}],
        //指定列数据位置
        "aoColumns": [
			{ "mData": "staffCode", "sTitle":"会员名","sWidth":"50px","bSortable":true},
			{ "mData": "nickname", "sTitle":"昵称","sWidth":"50px","bSortable":false},
			{ "mData": "custType", "sTitle":"客户类型","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                         if(data=='10'){
                        	 return "普通会员";
                         }else if(data=='20'){
                        	 return "企业会员";
                         }else if(data=='30'){
                        	 return "企业管理员";
                         }else{
                        	 return "";
                         }
				
			}},
			{ "mData": "custLevelName", "sTitle":"会员等级","sWidth":"80px","bSortable":false},
			{ "mData": "serialNumber", "sTitle":"手机号码","sWidth":"80px","bSortable":false},
			{ "mData": "pccString", "sTitle":"所在区域","sWidth":"80px","bSortable":false},
			{ "mData": "companyName", "sTitle":"所属企业","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                if (data == null) {
                	return "";
                } else {
                	return "<span title='"+data+"' style='cursor:default'>"+data+"</span>";
                }
                
	        }}, 
			{ "mData": "custShopFlag", "sTitle":"是否开店","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                if(data=='0'){
               	 return "否";
                }else if(data=='1'){
               	 return "是";
                } else {
                	return "否";
                }
	         }},
	        { "mData": "shopName", "sTitle":"店铺名称","sWidth":"80px","bSortable":false},
	        { "mData": "status", "sTitle":"状态","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                if (data == '1') {
               	 	return "正常";
                } else if (data == '2') {
               	 	return "锁定";
                } else if (data  == '3') {
                	return "失效";
                } else if (data  == '4') {
                	return "临时冻结";
                } else {
                	return "失效";
                }
	        }},
	        { "mData": "createTime", "sTitle":"注册时间","sWidth":"100px","bSortable":false,"mRender": function(data,type,row){
				data = ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
				return "<span title='"+data+"' style='cursor:default'>"+data+"</span>";
			}},
			{ "mData": "thirdCode", "sTitle":"淘宝会员号","sWidth":"80px","bSortable":false}
			
        ],
        "eDbClick" : function(){
        	modifyBiz();
        }
	});
	
	var modifyBiz = function(){
		var val = $('#dataGridTable').getSelectedData();
		if(val && val.length==1){
			eNav.setSubPageText("编辑用户信息");
			$('#staffId').val(val[0].id);
			$('#editForm').submit();
		}else if(val && val.length>1){
			eDialog.alert('只能选择一条记录进行操作！');
		}else if(!val || val.length==0){
			eDialog.alert('请选择至少一条记录进行操作！');
		}
	};
	
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});
	
	$('#btn_code_add').click(function(){
		eNav.setSubPageText("新增用户信息");
		window.location.href = GLOBAL.WEBROOT+'/cust/more';
	});
	
	$('#btn_code_modify').click(function(){
		modifyBiz();
	});
	
	$('#btn_third_code').click(function(){
		var val = $('#dataGridTable').getSelectedData();
		if(val && val.length==1){
			bDialog.open({
				title : '绑定淘宝会员号',
				width : 400,
				height : 260,
				scroll : false,
				params : {
				'Id':val[0].id	
				},
				url : GLOBAL.WEBROOT+'/custtm/bindpage',
				callback:function(data){
					$('#dataGridTable').gridReload();
				}
			});
		}else if(val && val.length>1){
			eDialog.alert('只能选择一条记录进行操作！');
		}else if(!val || val.length==0){
			eDialog.alert('请选择至少一条记录进行操作！');
		}
	
	});
	
	//使失效
	$('#btn_code_invalid').click(function(){
		var ids = $('#dataGridTable').getCheckIds();
		var rowData = $('#dataGridTable').getSelectedData();
		if(ids && ids.length==1){
			if (rowData[0].status == '3') {
				eDialog.alert("您好，该会员状态已为失效，无须操作！");
				return;
			}
			eDialog.confirm("您确认要把该条记录置为失效吗？", {
				buttons : [{
					caption : '确认',
					callback : function(){
						$.eAjax({
							url : $webroot + 'cust/gridlist/custinvalid?staffId=' + ids[0],
							success : function(returnInfo) {
								if(returnInfo.resultMsg){
									eDialog.alert(returnInfo.resultMsg);
									//window.location.reload();
									$('#btnFormSearch').click();//刷新列表
								}
							}
						});
					}
				},{
					caption : '取消',
					callback : $.noop
				}]
			});
		}else if(ids && ids.length>1){
			eDialog.alert('只能选择一条记录进行操作！');
		}else if(!ids || ids.length==0){
			eDialog.alert('请选择至少一条记录进行操作！');
		}
	});
	//使生效
	$('#btn_code_valid').click(function(){
		var ids = $('#dataGridTable').getCheckIds();
		var rowData = $('#dataGridTable').getSelectedData();
		if(ids && ids.length==1){
			if (rowData[0].status == '1') {
				eDialog.alert("您好，该会员状态已为正常，无须操作！");
				return;
			}
			eDialog.confirm("您确认要把该条记录状态恢复为正常吗？", {
				buttons : [{
					caption : '确认',
					callback : function(){
						$.eAjax({
							url : $webroot + 'cust/gridlist/custvalid?staffId=' + ids[0],
							success : function(returnInfo) {
								if(returnInfo.resultMsg){
									eDialog.alert(returnInfo.resultMsg);
									$('#btnFormSearch').click();//刷新列表
								}
							}
						});
					}
				},{
					caption : '取消',
					callback : $.noop
				}]
			});
		}else if(ids && ids.length>1){
			eDialog.alert('只能选择一条记录进行操作！');
		}else if(!ids || ids.length==0){
			eDialog.alert('请选择至少一条记录进行操作！');
		}
	});
	
	//账户加锁
	$('#btn_code_lock').click(function(){
		var ids = $('#dataGridTable').getCheckIds();
		var rowData = $('#dataGridTable').getSelectedData();
		if(ids && ids.length==1){
			if (rowData[0].status == '2') {
				eDialog.alert("您好，该会员状态已为锁定，无须操作！");
				return;
			}
			eDialog.confirm("您确认要把该条记录置为锁定吗？", {
				buttons : [{
					caption : '确认',
					callback : function(){
						$.eAjax({
							url : $webroot + 'cust/gridlist/custlock?staffId=' + ids[0],
							success : function(returnInfo) {
								if(returnInfo.resultMsg){
									eDialog.alert(returnInfo.resultMsg);
									$('#btnFormSearch').click();//刷新列表
								}
							}
						});
					}
				},{
					caption : '取消',
					callback : $.noop
				}]
			});
		}else if(ids && ids.length>1){
			eDialog.alert('只能选择一条记录进行操作！');
		}else if(!ids || ids.length==0){
			eDialog.alert('请选择至少一条记录进行操作！');
		}
	});
	
	//密码重置
	$('#btn_code_pwd_reset').click(function(){
		var ids = $('#dataGridTable').getCheckIds();
		var rowData = $('#dataGridTable').getSelectedData();
		if(ids && ids.length==1){
			eDialog.confirm("您确认要把该用户的密码重置吗？", {
				buttons : [{
					caption : '确认',
					callback : function(){
						$.eAjax({
							url : $webroot + 'cust/gridlist/pwdreset?staffId=' + ids[0],
							success : function(returnInfo) {
								if(returnInfo.resultMsg){
									eDialog.alert(returnInfo.resultMsg);
									$('#btnFormSearch').click();//刷新列表
								}
							}
						});
					}
				},{
					caption : '取消',
					callback : $.noop
				}]
			});
		}else if(ids && ids.length>1){
			eDialog.alert('只能选择一条记录进行操作！');
		}else if(!ids || ids.length==0){
			eDialog.alert('请选择至少一条记录进行操作！');
		}
	});
});