$(function(){
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/custcardsend/griddata',
        //指定列数据位置
        "aoColumns": [
          	{ "mData": "id", "sTitle":"主键Id","sWidth":"80px","sClass": "center","bSortable":false,"bVisible":false},
          	{ "mData": "disId", "sTitle":"发卡人编码","sWidth":"80px","sClass": "center","bSortable":false,"bVisible":false},
            { "mData": "disName", "sTitle":"发卡人姓名","sWidth":"80px","bSortable":false},      
            { "mData": "custCardId", "sTitle":"会员卡号","sWidth":"80px","bSortable":false},    
			{ "mData": "custLevelCodeName", "sTitle":"会员卡等级","sWidth":"80px","bSortable":false},
			{ "mData": "remark", "sTitle":"发卡原因","sWidth":"80px","bSortable":false},
			{ "mData": "sendCard", "sTitle":"发卡时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd");
			}}
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});
	
	$('#btn_code_edit').click(function(){
//		modifyBiz();
		var val = $('#dataGridTable').getSelectedData();
		if(val && val.length==1){
			eNav.setSubPageText("编辑发卡记录");
			var recordId = val[0].id;
			var disId = val[0].disId;

			$('#recordId').val(recordId);
			$('#recordDisId').val(disId);
			$('#editFrom').submit();
		}else if(val && val.length>1){
			eDialog.alert('只能选择一个项目进行操作！');
		}else if(!val || val.length==0){
			eDialog.alert('请选择至少选择一个项目进行操作！');
		}
		
		//window.location.href = GLOBAL.WEBROOT+'/shop/edit';
	});
	$('#btn_code_add').click(function(){
		eNav.setSubPageText("新增发卡记录");
		window.location.href = GLOBAL.WEBROOT+'/custcardsend/add';
	});


	$('#importExcel').click(function(evt){
		busSelector.uploader({
			'fileTypeDesc' : "*.xls;*.xlsx",  //文件选择类型描述
			'fileTypeExts' : "*.xls;*.xlsx",  //文件选择类型限制
			callback : function(data){
				if(data && data.results && data.results.length > 0){
					$.ajax({
						url : $webroot + 'custcardsend/importdata',
						data : {'fileId':data.results[0].fileId},
						success : function(returnInfo) {
							eDialog.alert(returnInfo);
							if (returnInfo == '导入成功！') {
								$("#dataGridTable").gridReload();
							}
						}
					});
				}
			}
		}, evt);
	});
});