$(function(){
	$("#carderinfodataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/carderinfo/griddata',
        //指定列数据位置
        "aoColumns": [
          	{ "mData": "id", "sTitle":"发卡人编码","sWidth":"80px","sClass": "center","bSortable":false},
            { "mData": "disName", "sTitle":"发卡人姓名","sWidth":"80px","bSortable":false},      
            { "mData": "disMobile", "sTitle":"发卡人电话","sWidth":"80px","bSortable":false},    
			{ "mData": "status", "sTitle":"状态","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				if(data=='1'){
					return '有效';
				}else{	
					return '失效';
				}
			}},			
			{ "mData": "createTime", "sTitle":"创建时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd");
			}},
            { "mData": "createStaffName", "sTitle":"创建人","sWidth":"80px","bSortable":false}

        ],
        "eDbClick" : function(){
        }
	});
	
	$('#p_btnFormSearch').click(function(){
		if(!$("#carderinfosearchForm").valid()) return false;
		var p = ebcForm.formParams($("#carderinfosearchForm"));
		$('#carderinfodataGridTable').gridSearch(p);
	});
	
	$('#p_btnFormReset').click(function(){
		ebcForm.resetForm('#carderinfosearchForm');
	});
	
	//编辑发卡人
	$('#p_btn_code_edit').click(function(){
		var val = $('#carderinfodataGridTable').getSelectedData();
		if(val && val.length==1){
			eNav.setSubPageText("编辑发卡人");
			var recordId = val[0].id;
			$('#p_recordId').val(recordId);
			$('#p_editFrom').submit();
		}else if(val && val.length>1){
			eDialog.alert('只能选择一个项目进行操作！');
		}else if(!val || val.length==0){
			eDialog.alert('请选择至少选择一个项目进行操作！');
		}
		
		//window.location.href = GLOBAL.WEBROOT+'/shop/edit';
	});
	$('#p_btn_code_add').click(function(){
		eNav.setSubPageText("新增发卡人");
		window.location.href = GLOBAL.WEBROOT+'/carderinfo/add';
	});

	$('#p_btn_code_active').click(function(){
		var val = $('#carderinfodataGridTable').getSelectedData();
		if(val && val.length==1){
			if(val[0].shopStatus != 1){
				eDialog.confirm("您确认要生效该发卡人吗？", {
					buttons : [{
						caption : '确认',
						callback : function(){
							eDialog.alert('success','生效成功！');
							
							$.eAjax({
								url : GLOBAL.WEBROOT + "/carderinfo/active",
								data : {'id':val[0].id},
								datatype:'json',
								success : function(returnInfo) {
									eDialog.success('生效成功！',{
										buttons:[{
											caption:"确定",
											callback:function(){
												$('#carderinfodataGridTable').gridReload();
									        }
										}]
									}); 
								}
							});
							
						}
					},{
						caption : '取消',
						callback : $.noop
					}]
				});
		}else
			{eDialog.alert('该发卡人已经是生效状态！');}
		}else if(val && val.length>1){
			eDialog.alert('只能选择一个项目进行操作！');
		}else if(!val || val.length==0){
			eDialog.alert('请选择至少选择一个项目进行操作！');
		}
	});
	$('#p_btn_code_valid').click(function(){
		var val = $('#carderinfodataGridTable').getSelectedData();
		if(val && val.length==1){
			if(val[0].shopStatus != 0){
				eDialog.confirm("您确认要失效该发卡人吗？", {
					buttons : [{
						caption : '确认',
						callback : function(){
							eDialog.alert('success','失效成功！');
							
							$.eAjax({
								url : GLOBAL.WEBROOT + "/carderinfo/valid",
								data : {'id':val[0].id},
								datatype:'json',
								success : function(returnInfo) {
									eDialog.success('失效成功！',{
										buttons:[{
											caption:"确定",
											callback:function(){
												$('#carderinfodataGridTable').gridReload();
									        }
										}]
									}); 
								}
							});
							
						}
					},{
						caption : '取消',
						callback : $.noop
					}]
				});
			}else
			{eDialog.alert('该发卡人已经是失效状态！');}
		}else if(val && val.length>1){
			eDialog.alert('只能选择一个项目进行操作！');
		}else if(!val || val.length==0){
			eDialog.alert('请选择至少选择一个项目进行操作！');
		}
	});
	

	
});