/**
 * 生成会员卡号
 */
$(function(){
	
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//获得父窗口设置的回调函数
	var _callback = bDialog.getDialogCallback(_dlg);

	
	//确定导入excel进行批量修改
	$('#btnFormSave').click(function(evt){
		var excelFileId = $('#excelFileId').val();
		if (excelFileId == '') {
			eDialog.alert('请先上传excel文件 ！');
			return;
		}
		
		$.eAjax({
			url : GLOBAL.WEBROOT + "/card/excelfile",
			data : {'excelFileId':excelFileId,'fileName':$('#fileName').val()},
			success : function(returnInfo) {
				$('#importId').val(returnInfo.resultMsg);
				eDialog.alert('操作成功！');
				var p = ebcForm.formParams($("#searchForm"));
        		$('#dataGridTable').gridSearch(p);
				//eDialog.success('操作成功！',{
					//onClose:function(){
					//	bDialog.closeCurrent();
						
					//}
				//}); 
			}
		});
	});
	
	//上传excel文件 到服务器
	$('#fileUploadBtn').eUploadBaseInit({
		fileTypeExts: ['xls','xlsx'],  //文件选择类型限制
		callback: function(fileInfo){
			$('#fileShowDiv').css('display','');
			$('#excelFileId').val(fileInfo.fileId);
			$('#fileShow').attr('href',fileInfo.url);
			$('#fileShow').html(fileInfo.fileName);
			$('#fileName').val(fileInfo.fileName);
		}
		
	});
	
	$('#uploadFileButton11').click(function(evt){
		busSelector.uploader({
			'fileTypeExts' : ['xls','xlsx'],  //文件选择类型限制
			'fileSizeLimit': '5MB',
			checktype : 'single',
			callback : function(fileInfo){
				$('#fileShowDiv').css('display','');
				$('#excelFileId').val(fileInfo.fileId);
				$('#fileShow').attr('href',fileInfo.url);
				$('#fileShow').html(fileInfo.fileName);
			}
		}, evt);
	});
	$('#btn_code_back').click(function(){
		
		bDialog.closeCurrent({});
	})
	
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pCheckColumn' : false,
        'pSingleCheckClean' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/card/templist',
        //指定列数据位置
        "aoColumns": [
			{ "mData": "custCardId", "sTitle":"会员卡号","sWidth":"40px","bSortable":true},
			{ "mData": "errorMess", "sTitle":"错误信息","sWidth":"40px","bSortable":false},
		
			{ "mData": "createTime", "sTitle":"生成时间","sWidth":"40px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}}
        ]
	});
	
	
	
	
})