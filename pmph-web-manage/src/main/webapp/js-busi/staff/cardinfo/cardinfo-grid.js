$(function(){
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/card/gridlist',
        'pCheck':'multi',
        //指定列数据位置
        "aoColumns": [
			{ "mData": "custCardId", "sTitle":"会员卡号","sWidth":"80px","bSortable":true},
			{ "mData": "custLevelName", "sTitle":"会员等级","sWidth":"80px","bSortable":false},
			{ "mData": "cardStatus", "sTitle":"会员卡状态","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
                         if(data=='01'){
                        	 return "未发实体卡";
                         }else if(data=='02'){
                        	 return "已发实体卡";
                         }else{
                        	 return "已作废";

                         }
				
			}},
			{ "mData": "cardGroup", "sTitle":"所属群组","sWidth":"80px","bSortable":false},
			{ "mData": "createTime", "sTitle":"生成时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}},
			{ "mData": "updateTime", "sTitle":"更新时间","sWidth":"80px","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}}
			/**
			 * ,
			{ "mData": "prop1", "sTitle":"其他属性1","sWidth":"90px","bSortable":false},
			{ "mData": "prop2", "sTitle":"其他属性2","sWidth":"90px","bSortable":false},
			{ "mData": "prop3", "sTitle":"其他属性3","sWidth":"90px","bSortable":false},
			{ "mData": "statusName", "sTitle":"状态","sWidth":"30px","bSortable":false},
			{ "mData": "sortno", "sTitle":"排序","sWidth":"40px","bSortable":false}
			 */
        ],
        "eDbClick" : function(){
        	modifyBiz();
        }
	});
	
	var modifyBiz = function(){
		var val = $('#dataGridTable').getSelectedData();
		if(val && val.length==1){
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
		
		bDialog.open({
			title : '会员卡生成',
			width : 450,
			height : 350,
			scroll : false,
			params : {},
			url : GLOBAL.WEBROOT+'/card/build',
			callback:function(data){
						$('#dataGridTable').gridReload();
				
			}
		});
		
	});
	
	$('#btn_excel_export').click(function(){
		
		$('#custLevelCode1').val($('#custLevelCode').val());
		$('#cardStatus1').val($('#cardStatus').val());
		$('#custCardId1').val($('#custCardId').val());
		$('#cardGroup1').val($('#cardGroup').val());
		$('#excelfrom').submit();
		
	});
	
	$('#btn_excel_update').click(function(evt){
		bDialog.open({
			title : 'excel批量修改',
			width : 850,
			height : 450,
			scroll : true,
			params : {},
			url : GLOBAL.WEBROOT+'/card/excelupdate',
			callback:function(data){
			    $('#dataGridTable').gridReload();
				$.eAjax({
					url : GLOBAL.WEBROOT + "/card/deletetemp",
					datatype:'json',
					success : function(returnInfo) {
						
					}
						}); 
						
					}
				});
			});
	
	
	$('#btn_no').click(function(){
		var val = $('#dataGridTable').getSelectedData();
		if (!val) {
			eDialog.alert('请选择至少一条记录进行操作！');
			return;
		}
		var cardlist = "";
		for(var i=0;i<val.length;i++){
			
			cardlist = cardlist + val[i].custCardId+",";
			
		}
		cardlist = cardlist.substring(0,cardlist.length-1);
		
		$.eAjax({
			url : GLOBAL.WEBROOT + "/card/updatecardstatus",
			data : {'cardlist':cardlist,'status':'01'},
			datatype:'json',
			success : function(returnInfo) {
				eDialog.success('修改成功',{buttons:[{
					caption:"确定",
					callback:function(){
						$('#dataGridTable').gridReload();
			        }
				}]
				}); 
				
			}
		});
		
	});
	
	$('#btn_real').click(function(){
		var val = $('#dataGridTable').getSelectedData();
		if (!val) {
			eDialog.alert('请选择至少一条记录进行操作！');
			return;
		}
		var cardlist = "";
		for(var i=0;i<val.length;i++){
			
			cardlist = cardlist + val[i].custCardId+",";
			
		}
		cardlist = cardlist.substring(0,cardlist.length-1);
		
		$.eAjax({
			url : GLOBAL.WEBROOT + "/card/updatecardstatus",
			data : {'cardlist':cardlist,'status':'02'},
			datatype:'json',
			success : function(returnInfo) {
				eDialog.success('修改成功',{buttons:[{
					caption:"确定",
					callback:function(){
						$('#dataGridTable').gridReload();
			        }
				}]
				}); 
				
			}
		});
		
	});

});