//UUID
var vfsId = '';

//dataJson
var dataJsonArr = [];

$(function(){
	
	$.eAjax({
		url : GLOBAL.WEBROOT + '/puborder/build/initdata',
		async : false,
		type : "post",
		dataType : "json",
		success : function(data) {
			if(data){
				var dataCacheImpList = data.dataCacheImpList;
				//表格数据选项
				var importdata = '';
				for(var count in dataCacheImpList){
					var dataCacheImp = JSON.parse(dataCacheImpList[count]);
					importdata = importdata + '<tr class="add-table-border">'
												+ '<td width="">'+dataCacheImp.fileId+'</td>'
												+ '<td width=""> <a href="'+GLOBAL.WEBROOT+'/puborder/build/allzddownload/'+dataCacheImp.fileId+'" class="sign-order-xls">'+dataCacheImp.fileName+'</a></td>'
												+ '<td width=""><a href="javascript:void(0);" class="sign-import-success">'+dataCacheImp.successCount+'</a></td>'
												+ '<td width=""><a href="'+dataCacheImp.failUrl+'" class="sign-import-fail">'+dataCacheImp.failCount+'</a></td>'
												+ '<td width="">'+dataCacheImp.createTime+'</td>'
											+ '</tr>';
				}
				
				var importTable = '<table id="importShowTable" class="add-table">'
					+'<thead>'
						+'<tr class="add-table-border">'
							+'<th>导入批次</th>'
							+'<th>导入文档</th>'
							+'<th>成功导入</th>'
							+'<th>失败导入</th>'
							+'<th>导入时间</th>'
						+'</tr>'
					+'</thead>'
					+ importdata
				+'</table>';
				//刷新导入元素
				if($('#importShowTable').length==0){
					$('#attributeForm').append(importTable);
				}
				
				if(data.zDPreOrd!=null){
					$("#zDRedisHash").val(data.zDRedisHash);
					init(data);
				}
			}
		}
	});
	
	$("#uploadFileTXT").on( "change", function () {
    	$("#file1").val($(this).val().substring($(this).val().lastIndexOf("\\")+1));
    });
    $("#signOrderSub").on("click",function(){
    	if($(".signOrder-list-detail").length!=0){
    		$(this).css('border','1px solid white');
        	$(this).css('background-color','#dddddd');
        	$(this).css('color','#666');
    		var buyerMsgMap = {};
        	$('.buyerMsg').each(function(n,val){
        		buyerMsgMap[$(this).attr('name')] = $(this).val();
        		/*if(n == ($('.buyerMsg').length-1)){
        			buyerMsgs = buyerMsgs + '{"' + $(this).attr('name') + '":"' + $(this).val() + '"}';
        		}else{
            		buyerMsgs = buyerMsgs + '{"' + $(this).attr('name') + '":"' + $(this).val() + '"},';
        		}*/
        	});
        	setTimeout(function(){
        		$.eAjax({
            		url : GLOBAL.WEBROOT+"/puborder/build/submitOrd/"+$("#zDRedisHash").val(),
            		data : {"buyerMsgMap":JSON.stringify(buyerMsgMap)},
            		async : false,
            		type : "post",
            		dataType : "json",
            		success : function(data) {
            			if(data.mapforRedisKeys!=null){
							if(data.mapforRedisKeys.ordMainMapsFailedRedisKey!=null && data.mapforRedisKeys.rOrdZDResponseRedisKey != null){
								if(data.mapforRedisKeys.ordMainMapsFailedRedisKey == '0'){
									// 订单提交成功
									window.location.href =GLOBAL.WEBROOT+"/puborder/build/create-success/"+data.mapforRedisKeys.rOrdZDResponseRedisKey+"/"+data.mapforRedisKeys.ordMainMapsFailedRedisKey;
								} else {
									eDialog.alert(data.msg);
								}
							} else {
								$("#signOrderSub").css('border','1px solid #ff6633');
								$("#signOrderSub").css('background-color','#FF6633');
								$("#signOrderSub").css('color','#ffffff');
								eDialog.alert(data.msg);
							}
							
						} else {
							$("#signOrderSub").css('border','1px solid #ff6633');
							$("#signOrderSub").css('background-color','#FF6633');
							$("#signOrderSub").css('color','#ffffff');
							eDialog.alert(data);
						}
            			
            		}
            		
            	});
        	}, 1);
    	}
    	
    });
    
    //绑定订单级文件属性的上传事件===========start
	 $('.uoloadFile').each(function(){
    	$(this).live('change',function(e){
    		var fileSize =  this.files[0].size;
    		if(fileSize/1048576 >10){
    			eDialog.warning("文件最大不能超过10M");
    			return;
    		}
    		var path = $(this).val();
    		var filesuffix = $('#file1').val().split('.');
    		if(filesuffix[1] != 'xls' && filesuffix[1] != 'xlsx'){
    			eDialog.warning("文件不是excel格式");
    			return;
    		}
    		
    		fileInfo.uploadFile(this, path);
			e.preventDefault();
    	});
    });
	//页面业务逻辑处理内容
	$("#myZdOrd").click(function(){
		window.location.href = GLOBAL.WEBROOT+'/pubord/all';
	});
	$("#signSuccessBack").click(function(){
		history.go(-1);
	});
    //统计上传成功文件个数
    var successCount = 0;
    
 });
function importFile(obj){
	
	$('#uploadFileTXT').trigger('click');
	
}
//删除订单缓存信息
function delOrdMain(obj){
	$.eAjax({
		url : GLOBAL.WEBROOT + '/puborder/build/delordmain',
		data : {
			"zDRedisHash" : $("#zDRedisHash").val(),
			"preOrdMainId" : $(obj).prev().val()
		},
		async : true,
		type : "post",
		dataType : "json",
		success : function(data) {
			$("#zDRedisHash").val(data.zDRedisHash);
			init(data);
		}
	});
}
//初始刷新或数据加载
function init(data){
	//订单元素变量
	var ordMainDivs = '<div class="signOrder-list-head">征订单列表</div>';
	//定义totalAmount
	var totalAmount = 0;
	//订单遍历
	var ordMainsList = data.zDPreOrd.ordMainMap;
	
	for(var count in ordMainsList){
		var ordMainsMap = ordMainsList[count];
		for(var key in ordMainsMap){
			
			if(ordMainsMap[key].preOrdMainList!=null){
				var mainOrder = ordMainsMap[key].preOrdMainList;
				var invoiceTitle = "";
				var taxpayerNo = "";
				var invoiceMoney = "";
				if(mainOrder[0].rOrdInvoiceCommRequest!=null){
					invoiceTitle = mainOrder[0].rOrdInvoiceCommRequest.invoiceTitle;
					taxpayerNo = mainOrder[0].rOrdInvoiceCommRequest.taxpayerNo;
					invoiceMoney = mainOrder[0].rOrdInvoiceCommRequest.invoiceMoney;
				}
				var ordSubDivs = '';
				var ordSubList = mainOrder[0].preOrdSubList;
				for(var count in ordSubList){
					ordSubDivs = ordSubDivs
						+ '<div class="signOrder-txt">'
							+ '<div class="goods-item" id="" style="width: 220px;">'
								+ '<div class="p-img">'
									+ '<a href="'+(GLOBAL.WEBROOT+ordSubList[count].gdsUrl)+'" target="_blank">'
										+ '<img class="" src="'+ordSubList[count].picUrl+'" title="" width="60" height="60">'
									+ '</a>'
								+ '</div>'
								+ '<div class="p-msg">'
									+ '<div class="p-name">'
										+ '<a href="'+(GLOBAL.WEBROOT+ordSubList[count].gdsUrl)+'" class="a-link" target="_blank" title="">'+ordSubList[count].gdsName+'</a>'
									+ '</div>'
								+ '</div>'
							+ '</div>'
							+ '<div class="goods-number" style="width: 50px;"> *'+ordSubList[count].orderAmount+' </div>'
							+ '<div class="goods-repair" style="width: 50px;">'
								+ '<a href="'+(GLOBAL.WEBROOT+ordSubList[count].gdsUrl)+'">'+ebcUtils.numFormat(accDiv(ordSubList[count].buyPrice,100),2)+'</a>'
							+ '</div>'
						+ '</div>'
				}
				var expressCharge = ''; 
				if(mainOrder[0].realExpressFee!='0'){
					expressCharge = '5.00';
				}else{
					expressCharge = '0.00';
				}
				totalAmount = totalAmount + parseInt(mainOrder[0].orderAmount);
				ordMainDivs = ordMainDivs
					+ '<div class="signOrder-list-detail">'
						+ '<table class="order-tb mt20" style="width: 95%;margin: 15px auto;">'
							+ '<tbody>'
								+ '<tr class="tr-th">'
									+ '<td colspan="4" style="height:35px;padding: 0 15px;box-sizing: border-box;">'
										+ '<span>收货人：</span>'
										+ '<span class="signOrder-address">'+mainOrder[0].receiver+' '+mainOrder[0].address+'</span>'
										+ '<span class="signOrder-phoneNum">'+mainOrder[0].phone+'</span>'
										+ '<span class="signOrder-warningMsg"></span>'
									+ '</td>'
								+ '</tr>'
								+ '<tr class="tr-bd">'
									+ '<td  style="padding:10px 0;">'
										+ ordSubDivs
									+ '</td>'
									+ '<td rowspan="1" class="txtStyle">'
										+ '<div class="amount">'
											+ '<strong><em class="rmb">¥</em>'+ebcUtils.numFormat(accDiv(mainOrder[0].orderMoney+mainOrder[0].realExpressFee,100),2)+'</strong> <br>'
											+ '<span class="t-gray">（快递'+expressCharge+'元）</span><br>'
										+ '</div>'
									+ '</td>'
									+ '<td rowspan="1" style="text-align: left;padding-left: 5px;box-sizing: border-box;">'
										+ '<div class="fapiaoMsg">'
											+ '<span class="c-gray-fa">发票信息</span><br>'
											+ '<span class="c-gray">抬头：</span>'+invoiceTitle+'<br>'
											+ '<span class="c-gray">纳税人识别号：</span>'+taxpayerNo+'<br>'
											//+ '<span class="c-gray">发票金额：</span>'+invoiceMoney+'<br>'
											+ '<span class="c-gray">发票金额：</span>'+ebcUtils.numFormat(accDiv(invoiceMoney,100),2)+'<br>'
										+ '</div>'
									+ '</td>'
									+ '<td rowspan="1" class="txtStyle">'
										+ '<div class="operate">'
											+ '<input type="hidden" value="'+key+'" />'
											+ '<a href="javascript:void(0)" class="signOrder-delete" onclick="delOrdMain(this)">删除</a><br>'
										+ '</div>'
									+ '</td>'
								+ '</tr>'
							+ '</tbody>'
						+ '</table>'
						+ '<div style="width: 95%;margin: 15px auto;">'
							+ '<label style="display: inline-block;">备注：</label>'
							+ '<textarea class="buyerMsg" name="'+key+'" rows="3" style="width: 90%;vertical-align:top">'+mainOrder[0].remark+'</textarea>'
						+ '</div>'
					+ '</div>';
			}
		}
	}
	
	$('.signOrder-list').children().remove();
	$('.signOrder-list').append(ordMainDivs);
	//手机号重复警告提示
	checkRepeatPhoneNO();
	//设置征订单总信息
	$('#totalOrderAmount').html(totalAmount);
	$('#totalOrderMoney').html('￥'+ebcUtils.numFormat(accDiv(data.zDPreOrd.baseMoney,100),2));
	$('#totalExpressFee').html('￥'+ebcUtils.numFormat(accDiv(data.zDPreOrd.realExpressFee,100),2));
	$('#totalDiscountMoney').html('-￥'+ebcUtils.numFormat(accDiv(data.zDPreOrd.discountMoney,100),2));
	$('#totalRealMoney').html('￥'+ebcUtils.numFormat(accDiv(data.zDPreOrd.realMoney,100),2));
}

//新增警告提示
function checkRepeatPhoneNO(){
	if($('.signOrder-warningMsg').length>1){
		var cellphones = [];
		$.each($('.signOrder-warningMsg'), function(n, val){
			if(cellphones.length>0){
				for(var count in cellphones){
					if(cellphones[count]==$(this).prev().html()){
						if($('.signOrder-warningMsg').eq(count).html()==''){							
							$('.signOrder-warningMsg').eq(count).html("提醒：手机号码有重复，请核实是否有重复订单");
						}
						$(this).html("提醒：手机号码有重复，请核实是否有重复订单");
					}
				}
			}
			cellphones.push($(this).prev().html());
		});
	};
}

var fileInfo = {
		uploadFile : function(object1, path){
			var filepath = path;
	    	filepath=(filepath+'').toLowerCase(); 
	    	var url = GLOBAL.WEBROOT + '/puborder/build/uploadfile';
	    	var propId = $(object1).attr('propId');
	    	var callback = function(data, status) {
	    		/** 上传成功，隐藏上传组件，并显示该图片 */
	    		if (data.success == "ok") {
	    			var _this = $("#file"+propId);
	    			_this.attr('disabled',true);
	    			vfsId = data.resultMap.vfsId;
	    			_this.val(filepath.substring(filepath.lastIndexOf("\\")+1));
	    			
	    			if(vfsId!=''){
	    		    	$("#excelFileInput").button('导入中');//设置状态按钮的状态为处理中
	    		    	//$.gridLoading({"el":"#importdata_id", "messsage":"正在加载中...."});//增加遮罩
	    		    	successCount = 0;
	    		    	//$("#uploadFileTXT").eUpload(uploadInitParams);
	    		    	//合并用户传递参数与默认参数
	    		    	var url = GLOBAL.WEBROOT + '/puborder/build/pubOrdImport';
	    		    	//var file = $.trim($("#file").val());
	    		    	$("#fileId").val(vfsId);
	    				$("#moduleName").val("证明单明细");
	    		    	$.eAjax({
	    		    		url : url,
	    		    		data :ebcForm.formParams($("#attributeForm")),
	    		    		async : false,
	    		    		type : "post",
	    		    		dataType : "json",
	    		    		success : function(data) {
	    		    			if(data.flag == '0'){
	    		    				eDialog.alert('征订单导入失败!');
	    		        			isStatus = false;
	    		    			}else if(data.flag == '1'){
	    		    				eDialog.alert('征订单导入完成!');
	    		    				isStatus = true;
	    		    				//zDRedisHash
	    		    				$("#zDRedisHash").val(data.zDRedisHash);
	    		    				var date = new Date();
	    		    				var month = date.getMonth()+1; var day = date.getDate();
	    		    				var hour = date.getHours(); var min = date.getMinutes(); var sec = date.getSeconds();
	    		    				if(date.getMonth()+1<10){
	    		    					month = '0'+(date.getMonth()+1);
	    		    				}
	    		    				if(date.getDate()<10){
	    		    					day = '0'+date.getDate();
	    		    				}
	    		    				if(date.getHours()<10){	    		    					
	    		    					hour = '0'+date.getHours();
	    		    				}
	    		    				if(date.getMinutes()<10){	    		    					
	    		    					min = '0'+date.getMinutes();
	    		    				}
	    		    				if(date.getSeconds()<10){	    		    					
	    		    					sec = '0'+date.getSeconds();
	    		    				}
	    		    				var failUrl = '';
	    		    				if(data.failCount==0){
	    		    					failUrl = '#';
	    		    				}else{
	    		    					failUrl = GLOBAL.WEBROOT + '/puborder/build/allzddownload/' + data.errorFileId;
	    		    				}
	    		    				var importdata = '<tr class="add-table-border">'
	    		    									+'<td width="">'+data.fileId+'</td>'
	    		    									+'<td width=""> <a href="'+GLOBAL.WEBROOT+'/puborder/build/allzddownload/'+data.fileId+'" class="sign-order-xls">'+filepath.substring(filepath.lastIndexOf("\\")+1)+'</a></td>'
	    		    									+'<td width=""><a href="javascript:void(0);" class="sign-import-success">'+data.successCount+'</a></td>'
	    		    									+'<td width=""><a href="'+failUrl+'" class="sign-import-fail">'+data.failCount+'</a></td>'
	    		    									+'<td width="">'+date.getFullYear()+'-'+month+'-'+day+' '+hour+':'+min+':'+sec+'</td>'
	    		    								+'</tr>';
	    		    				var importTable = '<table id="importShowTable" class="add-table">'
	    		    										+'<thead>'
	    		    											+'<tr class="add-table-border">'
	    		    												+'<th>导入批次</th>'
	    		    												+'<th>导入文档</th>'
	    		    												+'<th>成功导入</th>'
	    		    												+'<th>失败导入</th>'
	    		    												+'<th>导入时间</th>'
	    		    											+'</tr>'
	    		    										+'</thead>'
	    		    										+ importdata
	    		    									+'</table>';
	    		    				
	    		    				if($('#importShowTable').length>0){   
	    		    					$('#importShowTable thead').after(importdata);
	    		    					
	    		    				}else{
	    		    					$('#attributeForm').append(importTable);
	    		    					
	    		    				}
	    		    				
	    		    				init(data);
	    		    				
	    		    				var dataJson = {"fileId":data.fileId,"fileName":filepath.substring(filepath.lastIndexOf("\\")+1),"successCount":data.successCount,"failUrl":failUrl,"failCount":data.failCount,"createTime":date.getFullYear()+'-'+month+'-'+day+' '+hour+':'+min+':'+sec};
	    		    				dataJsonArr.push(dataJson);
	    		    				//把导入数据缓存起来 方便刷新调用
	    		    				$.eAjax({
	    		    		    		url : GLOBAL.WEBROOT+'/puborder/build/cacheImpLog',
	    		    		    		data :{"dataJsonArr":JSON.stringify(dataJsonArr)},
	    		    		    		async : false,
	    		    		    		type : "post",
	    		    		    		dataType : "json",
	    		    		    		success : function() {
	    		    		    			
	    		    		    		}
	    		    				});
	    		    			}else if(data.flag == '2'){
	    		    				eDialog.alert(data.msg);
	    		        			isStatus = false;
	    		    			} 
	    		    		}
	    		    	});
	    		    	
	    		    	$("#excelFileInput").button('reset');
	    			}else{
	    				eDialog.warning("请选择导入文件!");
	    			}
	    			
	    			//eDialog.alert('文件上传成功!');
	    		} else {
	    			eDialog.error(data.message);
	    		}
	    		fileInfo.bindFileUpload();
	    		//$("#attributeForm").valid();
	    	};
	    	fileInfo.ajaxFileUpload(url, false, $(object1).attr('id'), "POST", "json", callback);
		},
		bindFileUpload : function(){
			 $('.uoloadFile').each(function(){
			    	$(this).live('change',function(e){
			    		var fileSize =  this.files[0].size;
			    		if(fileSize/1048576 >10){
			    			eDialog.warning("文件最大不能超过10M");
			    			return;
			    		}
			    		var path = $(this).val();
			    		
			    		var filesuffix = $('#file1').val().split('.');
			    		if(filesuffix[1] != 'xls' && filesuffix[1] != 'xlsx'){
			    			eDialog.warning("文件不是excel格式");
			    			return;
			    		}
			    		
			    		fileInfo.uploadFile(this, path);
						e.preventDefault();
			    	});
			    });
		},
		ajaxFileUpload : function(url, secureuri, fileElementId, type, dataType,
				callback) {
			$.ajaxFileUpload({
						url : url, // 用于文件上传的服务器端请求地址
						secureuri : secureuri, // 一般设置为false
						fileElementId : fileElementId, // 文件上传空间的id属性 <input
						// type="file" id="imageFile"
						// name="imageFile" />
						type : type, // get 或 post
						dataType : dataType, // 返回值类型
						success : callback, // 服务器成功响应处理函数
						error : function(data, status, e) // 服务器响应失败处理函数
						{
							alert(e);
						}
					});
		}
}