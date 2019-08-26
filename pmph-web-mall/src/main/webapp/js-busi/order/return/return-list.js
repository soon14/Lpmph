//页面初始化模块
$(function(){ 
	//页面业务逻辑处理内容
    var pageInit = function(){
    	
        var init = function(){
        	
        	//controller配置
            var controllConfig = {
            		'01':'moneyList',
            		'02':'returnList'
            };
            
            //常量
        	var constant = {
        			status:$('#status').val()
        	};
        	
            //选项跳转
        	$('#teamTab li').click(function(){
        		var id = $(this).attr('id');
        		window.location.href = GLOBAL.WEBROOT + '/order/return/'+controllConfig[id];
        	});
        	
        	//分页
        	$('#pageControlbar').bPage({
        	    url : GLOBAL.WEBROOT + '/order/return/'+controllConfig[constant.status],
        	    asyncLoad : false,
        	    params : function(){}
        	});
        	
			//退款详情页面
			$('._returnMoney').click(function(){
				var backId = $(this).parents('.order-sub-tb').find('input[name="backId"]').val();
				var orderId = $(this).parents('.order-sub-tb').find('input[name="orderId"]').val();
				var siteId = $(this).parents('.order-sub-tb').find('input[name="siteId"]').val();
				var isCompenstate = $(this).parents('.order-sub-tb').find('input[name="isCompenstate"]').val();
				var url = GLOBAL.WEBROOT+'/order/return/returnMoney';
				if(isCompenstate==0){
					 url = GLOBAL.WEBROOT+'/order/return/returnCompensateMoney';
				}				
				if(siteId == '2'){
					url = GLOBAL.WEBROOT+'/order/point/return/returnMoney/'+backId+'/'+orderId;
					window.open(url);
				}
				
				var $form = $("#listForm");
				$form.attr("action",url);
				$form.attr("method","post");
				var contentHTML="";
				contentHTML = "<input type=\"hidden\" id=\"orderId\" name=\"orderId\" value=\""+ orderId +"\">";
				contentHTML = contentHTML + "<input type=\"hidden\" id=\"backId\" name=\"backId\" value=\""+ backId +"\">";
				$form.html(contentHTML);
				$form.submit();
				
				
			});
			
			//退货详情页面
			$('._returnDetail').click(function(num){
				var backId = $(this).parents('.order-sub-tb').find('input[name="backId"]').val();
				var orderId = $(this).parents('.order-sub-tb').find('input[name="orderId"]').val();
				var siteId = $(this).parents('.order-sub-tb').find('input[name="siteId"]').val();
				var url = GLOBAL.WEBROOT+'/order/return/returnDetail';
				if(siteId == '2'){
					url = GLOBAL.WEBROOT+'/order/point/return/returnDetail/'+backId+'/'+orderId;
					window.open(url);
				}
				
				var $form = $("#listForm");
				$form.attr("action",url);
				$form.attr("method","post");
				var contentHTML="";
				contentHTML = "<input type=\"hidden\" id=\"orderId\" name=\"orderId\" value=\""+ orderId +"\">";
				contentHTML = contentHTML + "<input type=\"hidden\" id=\"backId\" name=\"backId\" value=\""+ backId +"\">";
				$form.html(contentHTML);
				$form.submit();
	
			});
			//订单详情
			$('.return-ord-detail .number').click(function(){
        		detailOrd($(this));
        	});
			//方法定义
            var detailOrd = function(_this){
            	var orderId = _this.parents('.return-ord-detail').find('input[name="orderId"]').val();
        		//$('#detailForm').find('input[name="orderId"]').val(orderId);      		
        		var url = GLOBAL.WEBROOT + "/ord/detail/"+orderId;
        		window.open(url);
        		//$('#detailForm').submit();
            };
			
			//退货发货页面
			$('._returnBack').click(function(num){
				var backId = $(this).parents('.order-sub-tb').find('input[name="backId"]').val();
				var orderId = $(this).closest('.order-sub-tb').find('input[name="orderId"]').val();
				var siteId = $(this).parents('.order-sub-tb').find('input[name="siteId"]').val();
				var isStatus = true;
				var url = GLOBAL.WEBROOT + "/order/return/checkReturn";
	        	$.eAjax({
        			url:url,
        			data:[{name:"backId",value:backId},{name:"orderId",value:orderId},{name:"oper",value:"02"}],
        			async:false,
        			method:'post',
        			success:function(returnInfo){
        				if(returnInfo&&returnInfo.flag==false){
        					isStatus = false;
        					eDialog.alert(returnInfo.msg);
        				}
        			}
        		});
	        	if(isStatus == true){
	        		url = GLOBAL.WEBROOT+'/order/return/returnBack/'+backId+'/'+orderId;
					if(siteId == '2'){
						url = GLOBAL.WEBROOT+'/order/point/return/returnBack/'+backId+'/'+orderId;
					} 
					window.open(url);
        		}
			});  
			 
			//统计当前所有商品的总数量
			var checkItemCount = function(){
				var itemCount = 0; 
				$('#subList :checkbox:checked').each(function(i,row){
					var box = $(this).closest('.item-body');
					var check = $('.itemCheck',$(box));
					if($(check).prop('checked')){
						itemCount ++;
					}
				});
				$('#selectItemCount').text(itemCount);
			};
			
			$('#selectAllItem').on('click',function(e){
				checkItemCount();
			});
		    //退货申请页面默认选中所有商品
			$('#selectAllItem').click();
			//退货申请页面选中商品
			$('#subList .itemCheck').on('click',function(e){
				checkItemCount();
			});
			
			
			//退货发货保存
			$('#btn_send').click(function(){
				if(!$("#gdsSendForm").valid())
					return;
				var backId = $("#backId").val();
				var orderId = $("#orderId").val();
				var express = $.trim($("#express").val());
				var expressNo = $.trim($("#expressNo").val());
				if(express == ""){
					eDialog.alert('请输入物流公司名称！');
				}else if(expressNo == ""){
//					eDialog.alert('请输入快递单号！');
				}else{
					var isStatus = true;
	        		var url = GLOBAL.WEBROOT + "/order/return/checkReturn";
		        	$.eAjax({
	        			url:url,
	        			data:[{name:"backId",value:backId},{name:"orderId",value:orderId},{name:"oper",value:"02"}],
	        			async:false,
	        			method:'post',
	        			success:function(returnInfo){
	        				if(returnInfo&&returnInfo.flag==false){
	        					isStatus = false; 
	        					eDialog.alert(returnInfo.msg);
	        				}
	        			}
	        		}); 
		        	if(isStatus == true){
		        		var url = GLOBAL.WEBROOT + "/order/return/saveBackGdsSend";
						$.eAjax({
							url:url,
							data:ebcForm.formParams($("#gdsSendForm")),
							async:false,
							method:'post',
							success:function(returnInfo){
								if(returnInfo&&returnInfo.resultFlag=='ok'){
									eDialog.alert('退货发货成功',function(){
										window.location.href = GLOBAL.WEBROOT + "/order/return/returnList";
									}); 
								}else{
									eDialog.alert(returnInfo.resultMsg);
								}
								
							}
						}); 
	        		} 
					
				}
		    	
			});
			
			$("#pictrue").live("change", function(o) {
				var url1=$("#image1").attr("src");
				var url2=$("#image2").attr("src");
				var url3=$("#image3").attr("src");
				var url4=$("#image4").attr("src");
				var url5=$("#image5").attr("src");
				if(!url1 || !url2 || !url3 || !url4 || !url5){
					var path = $(this).val();
					uploadImage(this, path);
				}
				
			});
			
			//退货申请提交页面
			$('.apply-btn').click(function(){
				var checkedAll = $('#checkedAll').val();
				if($('.itemCheck:checked').size()==0&&checkedAll==0){
					eDialog.alert('请选择一项商品');
					return;
				}else{		
					
					if(checkedAll==0){
						var backindex = $('.itemCheck:checked').val();
						
						var valid = "true";
						
						var num = $("#num_"+backindex).val();
					
						if(isNaN(num)){
							eDialog.alert('请填写数字！');
							return;
						}
						
						if(num<=0){
							eDialog.alert('退货数量应大于0');
							return;
	              	    }
						if(num-$("#subnum_"+backindex).val()>0){
							eDialog.alert('退货数量不能大于可退货数量');
							return;
						}		
					}
					$.eAjax({
						url:GLOBAL.WEBROOT + '/order/return/saveSessionSub/'+$("#orderId").val(),
						data:ebcForm.formParams($("#childForm")),
						success:function(returnInfo){ 
							if(returnInfo&&returnInfo.resultFlag=='ok'){
								window.location.href = GLOBAL.WEBROOT + '/order/return/returnApplySub/'+$("#orderId").val()+'/'+checkedAll;
							}else{
								eDialog.alert(returnInfo.resultMsg,function(){
									window.location.replace(GLOBAL.WEBROOT + '/order/return/returnChild/'+$("#orderId").val());
								},'error');
							}
						}
					});
				} 
			});
			
			//退款申请保存
			$('#btn_apply').click(function(){
				if(!$("#applyForm").valid()) return false;
				$("#backTypeName").val($("#backType option:selected").text());
				var orderId = $("#orderId").val();
			    var isStatus = true;
	        	var url = GLOBAL.WEBROOT + "/order/return/checkReturn";

	        	$.eAjax({
        			url:url,
        			data:[{name:"backId",value:""},{name:"orderId",value:orderId},{name:"oper",value:"01"}],
        			async:false,
        			method:'post',
        			success:function(returnInfo){
        				if(returnInfo&&returnInfo.flag==false){
        					isStatus = false; 
        					eDialog.alert(returnInfo.msg);
        				}
        				
        			}
        		}); 
	        	
	        	if(isStatus == true){
	        		var url = GLOBAL.WEBROOT + "/order/return/saveBackMoneyApply";
			    	$.eAjax({
						url:url,
						data:ebcForm.formParams($("#applyForm")),
						async:false,
						method:'post',
						success:function(returnInfo){
							if(returnInfo&&returnInfo.resultFlag=='ok'){
								eDialog.alert('退款申请成功',function(){
									window.location.href = GLOBAL.WEBROOT + "/order/return/moneyList";
								}); 
							}else{
								eDialog.alert(returnInfo.resultMsg);
							}
							
						}
					}); 
        		} 
			    
			});
			
			//退货申请保存
			$('#btn_apply_sub').click(function(){
				if(!$("#applySubForm").valid()) return false;
			    $("#backTypeName").val($("#backType option:selected").text());  
			    var orderId = $("#orderId").val();
			    var isStatus = true;
        		var url = GLOBAL.WEBROOT + "/order/return/checkReturn";
	        	$.eAjax({
        			url:url,
        			data:[{name:"backId",value:""},{name:"orderId",value:orderId},{name:"oper",value:"00"}],
        			async:false,
        			method:'post',
        			success:function(returnInfo){
        				if(returnInfo&&returnInfo.flag==false){
        					isStatus = false; 
        					eDialog.alert(returnInfo.msg);
        				}
        			}
        		}); 
	        	if(isStatus == true){
	        		var url = GLOBAL.WEBROOT + "/order/return/saveBackGdsApply";
			    	$.eAjax({
						url:url,
						data:ebcForm.formParams($("#applySubForm")),
						async:false,
						method:'post',
						success:function(returnInfo){
							if(returnInfo&&returnInfo.resultFlag=='ok'){
								eDialog.alert('退货申请成功',function(){
									window.location.href = GLOBAL.WEBROOT + "/order/return/returnList";
								}); 
							}else{
								eDialog.alert(returnInfo.resultMsg);
							}							
						}
					}); 
        		}  
			}); 
			
			//关闭页面
			$("#btnReturn").click(function(){
				window.close();
			});
			
			//关闭图片1
			$("#closeimage1").click(function(){
				for(var i=1;i<5;i++){
					$("#image"+i).attr("src",$("#image"+(i+1)).attr("src"));
					$("#pic"+i).val($("#pic"+(i+1)).val());
					
				}
				$("#image5").attr("src","");
				$("#pic5").val("");
				$(this).hide();
				for(var j=1;j<=5;j++){
					if(!$("#image"+j).attr("src")){
						$("#li"+j).hide();
					}
				}
				
			});
			//关闭图片2
			$("#closeimage2").click(function(){
				for(var i=2;i<5;i++){
					$("#image"+i).attr("src",$("#image"+(i+1)).attr("src"));
					$("#pic"+i).val($("#pic"+(i+1)).val());
				}
				$("#image5").attr("src","");
				$("#pic5").val("");
				$(this).hide();
				debugger;
				for(var j=1;j<=5;j++){
					if(!$("#image"+j).attr("src")){
						$("#li"+j).hide();
					}
				}
			});
			//关闭图片3
			$("#closeimage3").click(function(){
				for(var i=3;i<5;i++){
					$("#image"+i).attr("src",$("#image"+(i+1)).attr("src"));
					$("#pic"+i).val($("#pic"+(i+1)).val());
				}
				$("#image5").attr("src","");
				$("#pic5").val("");
				$(this).hide();
				for(var j=1;j<=5;j++){
					if(!$("#image"+j).attr("src")){
						$("#li"+j).hide();
					}
				}
			});
			//关闭图片4
			$("#closeimage4").click(function(){
				for(var i=4;i<5;i++){
					$("#image"+i).attr("src",$("#image"+(i+1)).attr("src"));
					$("#pic"+i).val($("#pic"+(i+1)).val());
				}
				$("#image5").attr("src","");
				$("#pic5").val("");
				$(this).hide();
				for(var j=1;j<=5;j++){
					if(!$("#image"+j).attr("src")){
						$("#li"+j).hide();
					}
				}
			});
			//关闭图片5
			$("#closeimage5").click(function(){
				$("#image5").attr("src","");
				$("#pic5").val("");
				$(this).hide();
				$("#li5").hide();
			});
			 
			 $(".express>li").mouseover(function(){
				var id = $(this).find("img").attr("id");
				if($("#"+id).attr("src") != ""){
					$("#close"+id).show();
				} 
			});
			 $(".express>li").mouseout(function(){
				var id = $(this).find("img").attr("id");
				if($("#"+id).attr("src") != ""){
					$("#close"+id).hide();
				}
			});
			 
			var uploadImage = function (object, path) {
		    	var filepath = path;
		    	filepath=(filepath+'').toLowerCase();
		    	var regex = new RegExp(
		    			'\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$', 'gi');
		    	/** 上传图片文件格式验证 */
		    	if (!filepath || !filepath.match(regex)) {
		    		eDialog.alert('请选择图片格式为(.jpg,.png,.jpeg,.gif).');
		    		uploadfile.value = "";
		    		return;
		    	}
		    	var url = GLOBAL.WEBROOT + '/order/return/uploadimage';
		    	var callback = function(returnInfo) {
		    		/** 上传成功，隐藏上传组件，并显示该图片 */
		    		if (returnInfo.success == "ok") {
		    			var url1=$("#image1").attr("src");
		    			var url2=$("#image2").attr("src");
		    			var url3=$("#image3").attr("src");
		    			var url4=$("#image4").attr("src");
		    			var url5=$("#image5").attr("src");
		    			
		    			if(!url1){
		    				$("#image1").attr("src",returnInfo.map.imagePath);
							$("#pic1").val(returnInfo.map.vfsId);
							$("#li1").show();
		    			}else if(!url2){
		    				$("#image2").attr("src",returnInfo.map.imagePath);
							$("#pic2").val(returnInfo.map.vfsId);
							$("#li2").show();
		    			}else if(!url3){
		    				$("#image3").attr("src",returnInfo.map.imagePath);
							$("#pic3").val(returnInfo.map.vfsId);
							$("#li3").show();
		    			}else if(!url4){
		    				$("#image4").attr("src",returnInfo.map.imagePath);
							$("#pic4").val(returnInfo.map.vfsId);
							$("#li4").show();
		    			}else if(!url5){
		    				$("#image5").attr("src",returnInfo.map.imagePath);
							$("#pic5").val(returnInfo.map.vfsId);
							$("#li5").show();
		    			}
		    		} else {
		    			eDialog.error(returnInfo.message);
		    		}
		    	};
		    	ajaxFileUpload(url, false, $(object).attr('id'), "POST", "json", callback);
		    };
		    var ajaxFileUpload = function (url, secureuri, fileElementId, type, dataType, callback) {
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
			};
        };

        return {
            init : init
        };
    };
    pageConfig.config({
        //指定需要加载的插件，名称请参考common中定义的插件名称，注意大小写
        plugin : ['bPage'],
        //指定页面
        init : function(){
            var p = new pageInit();
            p.init();
        }
    });

    //是否全部退货绑定事件
    $("input[name='identity']").each(function(){
        $(this).click(function(){
            var discount = $(this).val();
            if(discount=="0"){
               $('.itemCheck').attr("disabled",false);
               $('#checkedAll').val(0);             
               $("input[id^='num']").attr("readOnly",false);              
            }
            if(discount=="1"){
            	 $('.itemCheck').attr("disabled",true);
            	 $('#checkedAll').val(1);
                 $("input[id^='num']").each(function(){
              	   var nums = $(this).prop("id").split('_');
              	   $(this).val($("#subnum_"+nums[1]).val());
                 });
            	 $("input[id^='num']").attr("readOnly",true);
            }
        });
    });
    
    //表格中的radio绑定事件
    $('.itemCheck').each(function(){
    	var index = $(this).val();
    	$(this).click(function(){
    		if ($(this).attr('checked')) {
    			$("#checked_"+index).val("checked");
    		}else{
    			$("#checked_"+index).val("");
    		}
    	 });    
    });
    
    //减少商品数量
    $("a[id^='reduceCount_']").each(function(){
	    $(this).click(function(){
	    	var idvalues = $(this).prop("id").split('_');	    	
	    	reduceCount('down',idvalues[1]);
	    });
    });
    
    //增加商品数量
    $("a[id^='addCount_']").each(function(){
	    $(this).click(function(){
	    	var idvalues = $(this).prop("id").split('_');	
	    	addCount('up',idvalues[1]);
	    });
    });
    
    var addCount = function(str,backIndex){   
    	var value = $("#num_"+backIndex).val();
    	var stock = $("#subnum_"+backIndex).val();
    	var max = $("#num_"+backIndex).attr('max');
    	var checkedAll = $("#checkedAll").val();
    	
    	if(checkedAll==1){
    		$("#num_"+backIndex).val($("#subnum_"+backIndex).val());
    		return;
    	}
    	if(isNaN(value)){
    		$("#num_"+backIndex).val($("#subnum_"+backIndex).val());
    		return;
    	}
    	if(Number(stock)<=Number(value)){
    		return;
    	}
    	if (value != '') {
    		if(++value > 999999999){
    			return;
    		}   		
    		$("#num_"+backIndex).val(value);
    	} else {    		
    		$("#num_"+backIndex).val($("#subnum_"+backIndex).val());
    	}
    };
    
    var reduceCount = function(str,backIndex){
    	var value = $("#num_"+backIndex).val(); 
    	var checkedAll = $("#checkedAll").val();
    	if(checkedAll==1){
    		$("#num_"+backIndex).val($("#subnum_"+backIndex).val());
    		return;
    	}
    	if(isNaN(value)){
    		$("#num_"+backIndex).val($("#subnum_"+backIndex).val());
    		return;
    	}
    	if(Number(value)<=0){
    		return;
    	}
    	if (value != '') {
    		$("#num_"+backIndex).val(--value);
    	} else {
    		$("#num_"+backIndex).val($("#subnum_"+backIndex).val());
    	}
    };
    
    $("input[id^='num_']").each(function(){
    	var checkedAll = $("#checkedAll").val();
    	if(checkedAll==1){
    		 $(this).attr("readOnly",true);
    	}else{
    		$(this).attr("readOnly",false);
    	}
	    $(this).on('keyup',function(){
	    	var idvalues = $(this).prop("id").split('_');	
	    	limitInputNum(idvalues[1]);
	    });
    });
    
	/**
	 * 限制只能输入正整数
	 * @param obj
	 */
	var limitInputNum = function(backIndex){	
		var _amount = $("#num_"+backIndex).val();		
		if(!isNaN(_amount)){			
			var subnum = $("#subnum_"+backIndex).val();
			if(_amount-subnum>0){
				$("#num_"+backIndex).val(subnum);
				return;
			}			
		}
		_amount=_amount.replace(/[^0-9]/g,'');
		if(_amount.trim() == ''){
			_amount=$("#subnum_"+backIndex).val();
		}		
		$("#num_"+backIndex).val(_amount);
	};
});