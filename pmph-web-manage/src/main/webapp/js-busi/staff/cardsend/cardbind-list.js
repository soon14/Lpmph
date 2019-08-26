$(function(){
	$("#bindDataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn':false,
        "sAjaxSource": GLOBAL.WEBROOT + '/card/bindgrid',
        //指定列数据位置
        "aoColumns": [
          	{ "mData": "id", "sTitle":"主键Id","sWidth":"80px","sClass": "center","bSortable":false,"bVisible":false},
            { "mData": "custCardId", "sTitle":"会员卡号","sWidth":"80px","bSortable":false},  
            { "mData": "staffCode", "sTitle":"会员名","sWidth":"80px","bSortable":false},  
            { "mData": "bindType", "sTitle":"绑卡方式","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                if(data == '0'){
                	return "线下发卡";
                } else if(data == '1'){
               	 	return "线上申请";
                } else {
                	return "线下发卡";
                }
	         }},    
	        { "mData": "cardGroup", "sTitle":"所属群组","sWidth":"80px","bSortable":false},
			{ "mData": "contactName", "sTitle":"联系人姓名","sWidth":"80px","bSortable":false},
			{ "mData": "contactPhone", "sTitle":"联系人手机","sWidth":"80px","bSortable":false},
			{ "mData": "contactAddress", "sTitle":"联系人地址","sWidth":"80px","bSortable":false},
			{ "mData": "bindTime", "sTitle":"绑卡时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}}
        ],
        "eDbClick" : function(){
        	//modifyBiz();
        }
	});
	
	$('#bindBtnFormSearch').click(function(){
		if(!$("#bindSearchForm").valid()) return false;
		var p = ebcForm.formParams($("#bindSearchForm"));
		$('#bindDataGridTable').gridSearch(p);
	});
	
	$('#bindBtnFormReset').click(function(){
		ebcForm.resetForm('#bindSearchForm');
	});
	$('#btn_excel_export').click(function(){
		$('#contactName1').val($('#contactName').val());
		$('#contactPhone1').val($('#contactPhone').val());
		$('#bindType1').val($('#bindType').val());
		$('#cardGroup1').val($('#cardGroup').val());
		$('#custCardId1').val($('#custCardId2').val());
		$('#excelBindFrom').submit();
		
	});
});