var stock_switch = "";
var price_switch = "";
var multi_switch = "";
var _number = /^([0-9]+)$/;
var _price = /^\d+(\.\d{1,2})?$/;
$(function(){
	/**
	 * 去掉101积分店铺（暂时硬编码）
	 */
	//$("#shopId").find("option[value='101']").remove();
	GdsInfoEntry.initValid();
	GdsInfoEntry.validSkuParam();
	stock_switch = $("#stock-switch").val();
	price_switch = $("#price-switch").val();
	multi_switch = $("#multi-switch").val();
	$("#gdsCombine").live('click',function(){
		var param = $("#combineGds").val();
		if(param ==""){
			param = "[]";
		}
		bDialog.open({
            title : "选择组合产品",
            width : 900,
            height : 850,
            params : {
            	'param' : param
            },
            url : GLOBAL.WEBROOT + "/gdsinfoentry/togdscombine",
            callback:function(data){
            	if(data && data.results && data.results.length > 0 ){
            		$("#combineGds").val(data.results[0].param);
            	}
            }
        });
	});
	var _maxCount = $("#GDS_CATEGORY_MAX_COUNT").val();
	$('#selptPlat').live('click',function(){
		bDialog.open({
            title : '平台分类选择',
            width : 350,
            height : 550,
            params:{shopIds:[$('#shopId').val()]},
            url : GLOBAL.WEBROOT+"/goods/category/open/catgselect?catgType=1",
            callback:function(data){
            	if(data && data.results && data.results.length > 0 ){
                    var _catgs = data.results[0].catgs;
					var size = _catgs.length;
					var length = $("#catagoryList").find('tr').length;
					if((size + length) > _maxCount){
						eDialog.error("商品最多只能归属"+_maxCount+"平台分类！");
						return ;
					}
					for(var i =0;i<size;i++){
						var count = 0;
						var obj = _catgs[i];
						$("#catagoryList").find('tr').each(function(){
							$this = $(this);
							catgCode = $this.children('td').eq(0).attr('id');
								if(catgCode == obj.catgCode){
									count ++;
								}
						});
						if(count == 0){
							$("#catagoryList").append('<tr><td id="'+obj.catgCode+'">'+obj.catgName+'</td><td id="2">副分类</td><td><a href="javascript:void(0)" onclick="deleteCatPlat(this)">删除</a></td></tr>');
						}
					}
				}
            }
        });
    });
	$('#seldpShop').live('click',function(){
		bDialog.open({
            title : '店铺分类选择',
            width : 350,
            height : 550,
            params:{shopIds:[$('#shopId').val()]},
            url : GLOBAL.WEBROOT+"/goods/category/open/catgselect?catgType=2",
            callback:function(data){
            	if(data && data.results && data.results.length > 0 ){
                    var _catgs = data.results[0].catgs;
					var size = _catgs.length;
					var length = $("#shopCatagoryList").find('tr').length;
					if((size + length) > _maxCount){
						eDialog.error("商品最多只能归属"+_maxCount+"个平台分类！");
						return ;
					}
					for(var i =0;i<size;i++){
						var count = 0;
						var obj = _catgs[i];
						$("#shopCatagoryList").find('tr').each(function(){
							$this = $(this);
							catgCode = $this.children('td').eq(0).attr('id');
								if(catgCode == obj.catgCode){
									count ++;
								}
						});
						if(count == 0){
							$("#shopCatagoryList").append('<tr><td id="'+obj.catgCode+'">'+obj.catgName+'</td><td><a href="javascript:void(0)" onclick="deleteCatPlat(this)">删除</a></td></tr>');
						}
					}
				}
            }
        });
    });
	
   $("#openGdsShiptemp").live('click',function(){
       bDialog.open({
           title : "运费模板管理",
           width : 800,
           height : 700,
           params : {
        	   'shopId' : $("#shopId").val()
           },
           url : GLOBAL.WEBROOT + "/gdsinfoentry/toshiptemp",
           callback:function(data){
        	   if(data && data.results && data.results.length > 0 ){
        		   $("#shipTemplateId").val(data.results[0].tempName);
				   $("#shipTemplateId").attr('tempId',data.results[0].tempId);
				}
           }
       });
   });
    $(".switch-button").find('.btn').live('click',function(e){
    	$this_btn = $(this);
		$this = $this_btn.closest('.btns_state');
//		if($this_btn.hasClass('active')) {
			if("multi-open" == $this_btn.val()){
				multi_switch = "1";
				$("#setting-price-label").hide();
				$("#setting-price-input").hide();
				$("#param-senior-button").find('button[value=multi-open]').addClass('active');
				$("#param-senior-button").find('button[value=multi-close]').removeClass('active');
				$("#gds-skuParam").show();
				$("#param-senior-button").show();
				$("#price-senior-button").hide();
				//$("#gds-priceSetting").hide();
			}else if('multi-close' == $this_btn.val()){
				multi_switch = "0";
				$("#setting-price-label").show();
				$("#setting-price-input").show();
				$("#price-senior-button").find('button[value=multi-close]').addClass('active');
				$("#price-senior-button").find('button[value=multi-open]').removeClass('active');
				$("#gds-skuParam").hide();
				$("#param-senior-button").hide();
				$("#price-senior-button").show();
				$("#gds-priceSetting").show();
			}
			if("price-open" == $this_btn.val()){
				GdsInfoEntry.buttonOpen('open','senior-price');
			}else if('price-close' == $this_btn.val()){
				GdsInfoEntry.buttonOpen('close','senior-price');
			}
			if("stock-open" == $this_btn.val()){
				GdsInfoEntry.buttonOpen('open','senior-stock');
			}else if('stock-close' == $this_btn.val()){
				GdsInfoEntry.buttonOpen('close','senior-stock');
			}
//		} else {
//			if("multi-open" == $this_btn.val()){
//				$("#gds-priceSetting").show();
//			}else if('multi-close' == $this_btn.val()){
//				$("#gds-priceSetting").hide();
//			}
//		}
		e.preventDefault();
    });
    $("#topSave").click(function(){
    	$("#saveGdsInfo").trigger('click');
    });
    $("#topback").click(function(){
    	$("#backGdsManage").trigger('click');
    });
    $("#saveGdsInfo").click(function(){
    	if(!$("#baseInfoForm").valid()){
    		$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
    				: $('body'))
    				: $('html,body');
    		$body.animate({
    			scrollTop : $("#gds-category").offset().top
    		}, "fast");
    		return ;
    	}
    	if(!$("#priceSettingForm").valid()){
    		$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
    				: $('body'))
    				: $('html,body');
    		$body.animate({
    			scrollTop : $("#gds-skuParam").offset().top
    		}, "fast");
    		return;
    	} 
    	if(!$("#gdsSkuForm").valid()){
    		$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
    				: $('body'))
    				: $('html,body');
    		$body.animate({
    			scrollTop : $("#gds-skuParam").offset().top
    		}, "fast");
    		return ;
    	} 
    	if(!$("#attributeForm").valid()){
    		$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
    				: $('body'))
    				: $('html,body');
    		$body.animate({
    			scrollTop : $("#gds-pictrue").offset().top
    		}, "fast");
    		return;
    	}
    	if(!$("#settingForm").valid()){
    		$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
    				: $('body'))
    				: $('html,body');
    		$body.animate({
    			scrollTop : $("#gds-attr").offset().top
    		}, "fast");
    		return;
    	} 
    	if(!$("#decriptionForm").valid()){
    		$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
    				: $('body'))
    				: $('html,body');
    		$body.animate({
    			scrollTop : $("#gdecription").offset().top
    		}, "fast");
    		return;
    	}
    	if(!GdsInfoEntry.saveGdsInfoValidator()){
    		$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
    				: $('body'))
    				: $('html,body');
    		$body.animate({
    			scrollTop : $("#gds-skuParam").offset().top
    		}, "fast");
    		return;
    	}
    	
		GdsInfoEntry.saveGdsInfo(this);
    });
    $("#backGdsManage").click(function(){
    	window.location.href =  GLOBAL.WEBROOT +"/gdsmanage?shopId="+$("#shopId").val();
    });
    
    $(".add-lader-price").live('click',function(){
    	var $thisInput = $("#lader-price-table").find('tr').last().find("#order-amount");
    	if($("#lader-price-table").find('tr').length >= 4){
    		return;
    	}
    	$thisInput.siblings('br').remove();
		$thisInput.siblings('span').remove();
    	var htmlContext = "<tr>"+
        "<td>≥<input type='text' class='input-mini required' id='order-amount' name='order-amount' value='' onblur='validateLaderAmount(this)' maxlength='9'/></td>"+
        "<td><input type='text' class='input-mini' name='lader-price' value='' onblur='validateLaderPrice(this)' maxlength='9'/></td>"+
        "<td><a href='javascript:void(0);' onclick='delteLaderPrice(this)'>删除</a></td></tr>";
    	$("#lader-price-table").append(htmlContext);
    	if($("#lader-price-table").find('tr').length >= 4){
    		$(".add-lader-price").attr('style','color:#999');
    		return;
    	}
    });
    var more = 1;
    $("#more-pictrue").click(function(){
    	$this = $(this);
    	if(more == 1){
    		$this.find('i').removeClass('icon-caret-down');
    		$this.find('i').addClass('icon-caret-up');
    		$this.find('span').text('收起');
    		more = -1;
    		$("#picture-block").show();
    		GdsInfoEntry.queryMediaList();
    	}else{
    		$this.find('i').removeClass('icon-caret-up');
    		$this.find('i').addClass('icon-caret-down');
    		$this.find('span').text('更多');
    		more = 1;
    		$("#picture-block").hide();
    		
    	}
    });
    $("#gds-lader-price-open").click(function(){
    	var $this = $(this);
    	if($this.attr('checked') == 'checked'){
    		$("#lader-price-block").show();
    	}else{
    		$("#lader-price-block").hide();
    	}
    });

    $('.com_input').each(function(){
    	$(this).bind("change", function(e) {
			var path = $(this).val();
			GdsInfoEntry.uploadImage(this, path);
			e.preventDefault();
		});
    	
    });
    //绑定文件属性的上传事件===========start
    $('.upFileTyle').each(function(){
    	$(this).live('change',function(e){
    		var fileSize =  this.files[0].size;
    		var fileSize =  this.files[0].size;
    		if(fileSize/1048576 >10){
    			eDialog.alert("文件最大不能超过10M");
    			return;
    		}
    		var path = $(this).val();
			GdsInfoEntry.uploadFile(this, path);
			e.preventDefault();
    	});
    });
    //绑定文件属性的上传事件===========end
    //选择图片的
    $(".imgcont").click(function(){
	  	$(".imgcont").each(function(){
	  		 $(this).removeAttr("style");
		});
	    $(this).attr('style',"border-color: red");
	});	  
    //===========多选事件绑定start
        $.fn.mutiselect=function(){
            return this.each(function(){
                var _this=$(this);
                var _seldrop=$('.sel-drop', _this);
                $(_this).hover(function(){
                    _this.find('.body').fadeToggle();
                    _seldrop.addClass('icon-chevron-up').removeClass('icon-chevron-down');
                },function(){
                	_this.find('.body').fadeToggle();
				   	_seldrop.removeClass('icon-chevron-up').addClass('icon-chevron-down');
				});
                _this.find('label').on('click',function(){
                    selCheckbox();
                });
                selCheckbox();
                function selCheckbox(){
                    var _hdhtml='';
                    var _c = 0;
                    _this.find('.selcheckbox').each(function(){
                        if($(this).is(":checked")){
                            _hdhtml+=$(this).val()+',';
                            _c ++;
                        }
                    });
                    $('.head',_this).val(_hdhtml);
                    if(_c > 0 ){
                    	$('.head',_this).valid();
                    }
                }
            });
        };
        $('.mutiselect').mutiselect();
		$('.sel-drop').click(function(){
		    $('.mutiselect').trigger('click');
		});
		//===========多选事件绑定end
		//是否免邮事件绑定===========start
		$("#ifFree").click(function(){
			var _this = $(this);
			if(_this.attr('checked')=='checked'){
				$("#gdsShiptempSetting").hide();
			}else{
				$("#gdsShiptempSetting").show();
			}
		});
		//是否免邮事件绑定===========end
		
});
/** 页面属性点击顺序 */
var sort = new Array();
/** 需要动态添加的html */
var addHtml = "";
/** 动态表格组件Id */
var tagIdCount = 0;
/** ckeditor */
function checkSkuParameter(propId, propName, propValueId, propValue,currentIndex,indexCount,obj){
	GdsInfoEntry.checkSkuParameter(propId, propName, propValueId, propValue,currentIndex,indexCount,obj);
}
function textCounter(id,limitNum,showArea){
	GdsInfoEntry.textCounter(id,limitNum,showArea);
}
function choseGdsCat(obj){
	GdsInfoEntry.choseGdsCat(obj);
}
function validateLaderPrice(obj){
	GdsInfoEntry.validateLaderPrice(obj,'price');
}
function validateLaderAmount(obj){
	GdsInfoEntry.validateLaderPrice(obj,'amount');
}
function delteLaderPrice(obj){
	GdsInfoEntry.delteLaderPrice(obj);
}
function openStockWin(obj){
	GdsInfoEntry.openStockWin(obj);
}
function openPriceWin(obj){
	GdsInfoEntry.openPriceWin(obj);
}
function openSkuPictrueWindow(obj){
	GdsInfoEntry.openSkuPictrueWindow(obj);
}
function deleteCatPlat(obj){
	GdsInfoEntry.deleteCatPlat(obj);
}
function searchMedia(){
	GdsInfoEntry.queryMediaList();
}
function validatorSku(obj,str){
	GdsInfoEntry.validatorSku(obj,str);
}
function cancelFile(obj){
	$(obj).parent().find('input[type="text"]').removeAttr('disabled');
	$(obj).parent().find('input[type="text"]').val('');
	$(obj).parent().find('.file-wrap').show();
	$(obj).hide();
}
function paperChange(obj,str){
	var _value = $(obj).val();
	var _paper = $("#paperChange1029");
	if(_value=="306"){
		_paper.removeClass("required");
		_paper.addClass("required");
	}else{
		_paper.removeClass("required");
	}
	if(str=="attr"){
		$("#attributeForm").valid();
	}else{
		$("#baseInfoForm").valid();
	}
	
}
var GdsInfoEntry = {
		bindPicUpload : function(){
			$('.com_input').each(function(){
		    	$(this).bind("change", function(e) {
					var path = $(this).val();
					GdsInfoEntry.uploadImage(this, path);
					e.preventDefault();
				});
		    	
	    	});
		},
		bindFileUpload : function(){
			 $('.upFileTyle').each(function(){
			    	$(this).live('change',function(e){
			    		var fileSize =  this.files[0].size;
			    		if(fileSize/1048576 >10){
			    			eDialog.alert("文件最大不能超过10M");
			    			return;
			    		}
			    		var path = $(this).val();
						GdsInfoEntry.uploadFile(this, path);
						e.preventDefault();
			    	});
			    });
		},
		initValid : function(){
			jQuery.validator.addMethod("compareTime", function(value, element) {
				var endTimeId = element.id;
				var startTime= $("#putonTime").val();
				var endTime = $("#" + endTimeId).val();

				if (endTime == "" || startTime == "") {
					return true;
				}
				/**
				 * 解决IE不支持trim问题
				 */
				if (!String.prototype.trim) {
					String.prototype.trim = function() {
						return this.replace(/^\s+|\s+$/g, '');
					};
				}
				if (startTime.trim().length == 10) {
					startTime = startTime + " 00:00:00";
				}
				if (endTime.trim().length == 10) {
					endTime = endTime + " 00:00:00";
				}
				var reg = new RegExp('-', 'g');
				startTime = startTime.replace(reg, '/');// 正则替换
				endTime = endTime.replace(reg, '/');
				startTime = new Date(parseInt(Date.parse(startTime), 10));
				endTime = new Date(parseInt(Date.parse(endTime), 10));
				if (startTime > endTime) {
					return false;
				} else {
					return true;
				}
			}, "<font color='#E47068'>自动下架时间不能早于自动上架时间</font>");
			
			$("#settingForm").validate({
				rules : {
					mainPicVfsId : {
						gdsMainMedia : true
					},
					putoffTime : {
						compareTime : true
					}
				},
				messages : {
					mainPicVfsId : {
						gdsMainMedia : "<span style='color:red'>请上传主图图片</span>"
					},
					putoffTime : {
						compareTime : "<span style='color:red'>自动下架时间不能早于自动上架时间</span>"
					}
				},
				//	          debug: false,  //如果修改为true则表单不会提交  
				submitHandler : function() {
				}
			});
		},
		validSkuParam : function(){
			jQuery.validator.addMethod("price_param",  //addMethod第1个参数:方法名称  
		        function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）  
				var decimal = /^\d+(\.\d{1,2})?$/;
				return (decimal.test(value)|| $.trim(value)=="");
		        },  
		        "价格格式错误");
			jQuery.validator.addMethod("stock_param",  //addMethod第1个参数:方法名称  
		        function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）  
				var decimal = /^\d+(\.\d{1,2})?$/;
				return (decimal.test(value)|| $.trim(value)=="");
		        },  
		        "<b style='color:red'>请输入大于等于0的整数</b>");
			$("#gdsSkuForm").validate({
				rules : {
//					price_param : {
//						price_param : true
//					},
//					stock_param : {
//						stock_param : true
//					}
				},
				messages : {
//					price_param : {
//						price_param : "<b style='color:red'>价格格式错误</b>"
//					},
//					stock_param : {
//						stock_param : "<b style='color:red'>只能输入整数</b>",
//					}
				},
				//	          debug: false,  //如果修改为true则表单不会提交  
				submitHandler : function() {
				},
				errorPlacement : function(error, element) {
					error.appendTo(element.parent());
				}
			});
		},
		buttonOpen : function(str,button){
			if('senior-price' == button){
				if('open' == str){
					price_switch = "1";
					$("#price-senior-button").find('button[value=price-open]').addClass('active');
					$("#param-senior-button").find('button[value=price-open]').addClass('active');
					$("#price-senior-button").find('button[value=price-close]').removeClass('active');
					$("#param-senior-button").find('button[value=price-close]').removeClass('active');
					$("#open-ladder-price").html('<a class="required priceNumber gjpri" href="javascript:void(0)" value="[]" id="productPrice" onclick="openPriceWin(this)">高级价格</a>');
				}else{
					price_switch = "0";
					$("#price-senior-button").find('button[value=price-close]').addClass('active');
					$("#param-senior-button").find('button[value=price-close]').addClass('active');
					$("#price-senior-button").find('button[value=price-open]').removeClass('active');
					$("#param-senior-button").find('button[value=price-open]').removeClass('active');
					$("#open-ladder-price").html('<input type="text" class="required priceNumber" onblur="validatorSku(this,\'price\')" name="productPrice" id="productPrice" maxlength="9"/>');
				}
			}else if('senior-stock' == button){
				if('open' == str){
					stock_switch = "1";
					$("#price-senior-button").find('button[value=stock-open]').addClass('active');
					$("#param-senior-button").find('button[value=stock-open]').addClass('active');
					$("#price-senior-button").find('button[value=stock-close]').removeClass('active');
					$("#param-senior-button").find('button[value=stock-close]').removeClass('active');
					$("#open-ladder-stock").html('<a class="gjkc" href="javascript:void(0)" id="productStock" id="productStock" value="[]" onclick="openStockWin(this)">高级库存</a>');
				}else{
					stock_switch = "0";
					$("#price-senior-button").find('button[value=stock-close]').addClass('active');
					$("#param-senior-button").find('button[value=stock-close]').addClass('active');
					$("#price-senior-button").find('button[value=stock-open]').removeClass('active');
					$("#param-senior-button").find('button[value=stock-open]').removeClass('active');
					$("#open-ladder-stock").html('<input type="text" class="required digits" name="productStock" onblur=\"validatorSku(this,\'stock\')\" id="productStock" maxlength="9"/>');
				}
			}
			$("#sku-param-table").find('tr').each(function(e){
				var $this = $(this);
				if('senior-price' == button){
					if('open' == str){
						$this.find('#senior-price').html('<a class="gjpri" href="javascript:void(0)" id="productPrice" value="[]" onclick="openPriceWin(this)">高级价格</a>');
					}else{
						$this.find('#senior-price').html('<input type="text" class="input-small" onblur=\"validatorSku(this,\'price\')\" name="price_param" maxlength="9"/>');
					}
				}else if('senior-stock' == button){
					if('open' == str){
						$this.find('#senior-stock').html('<a class="gjkc" href="javascript:void(0)" id="productStock" value="[]" onclick="openStockWin(this)">高级库存</a>');
					}else{
						$this.find('#senior-stock').html('<input type="text" class="input-small" onblur=\"validatorSku(this,\'stock\')\" name="stock_param" maxlength="9"/>');
					}
				}
			});
		},
		textCounter : function(id,limitNum,showArea)   {			
			if   ($("#"+id).val().length  > limitNum)        
				//如果元素区字符数大于最大字符数，按照最大字符数截断；        
				$("#"+id).val($("#"+id).val().substring(0, limitNum));      
			else      
				//在记数区文本框内显示剩余的字符数；        
				$("#"+showArea).text( limitNum - $("#"+id).val().length);     
		},
		choseGdsCat : function(obj){
			var $this = $(obj);
			if($this.val()=='3'){
				$this.after('<span class="help-inline"><a href="javascript:void(0)" id="gdsCombine">*选择组合产品</a></span>');
			}else{
				$this.next().remove();
			}
		},
		checkSkuParameter : function (propId, propName, propValueId, propValue,currentIndex,indexCount,obj) {
			if($("#sku-param-table").find('tr').length==0){
				var stockhtml = "";
				var pricehtml = "";
			    var appPriceInputHtml = "";
				if(stock_switch=='1'){
					stockhtml +="<a class='gjkc' href='javascript:void(0)' value='[]' onclick='openPriceWin(this)'>高级价格</a";
				}else{
					stockhtml +="<input type='text' name='price_param'  class='input-small' maxlength='9'/>";
				}
				if(price_switch=='1'){
					pricehtml +="<a class='gjkc' href='javascript:void(0)' value='[]' onclick='openStockWin(this)'>高级库存</a>";
				}else{
					pricehtml +="<input type='text' name='stock_param' class='input-small' maxlength='9'/>";
				}
				appPriceInputHtml += "<input type='text' name='appPrice_param'  class='input-small' maxlength='9'/>";
				var html = "<thead>"+
                "<tr>"+
                    "<th id='getPriceTh'><span style='color: red'></span>价格</th>"+
                    "<th id='appPrice'><span style='color: red'></span>手机专享价</th>"+
                    "<th id='realCountTh'><span style='color: red'></span>库存数量</th>"+
                    "<th>操作</th>"+
                "</tr>"+
                "</thead>"+
                "<tbody>"+
                "<tr id='esp'>"+
                    "<td id='senior-price'>"+pricehtml+"</td>"+
                    "<td id='senior-appPrice'>"+appPriceInputHtml+"</td>"+
                    "<td id='senior-stock'>"+stockhtml+"</td>"+
                    "<td><a href='javascript:void(0)' value='[]' onclick='openSkuPictrueWindow(this)'>图片管理</a></td>"+
                "</tr>"+
                "</tbody>";
				$("#sku-param-table").append(html);
			}
			
	    	addHtml = "";
	    	currentIndex = parseInt($(obj).attr('count'));
	    	indexCount = parseInt($(obj).attr('size'));
	    	var childIndex = $(obj).attr('i');
	    	childIndex = parseInt(childIndex) - 1;
	    	var first = $(".check_" + propId).eq(childIndex).prop("checked");
	    	var flag = false;
	    	var count = 0;
	    	var arr = new Array();
	    	// 查看当前选中的值，以及其他行被选中的值的个数
	    	for (var i = 1; i < indexCount + 1; i++) {
	    		var temp = false;
	    		var countFlag = Number(0);
	    		var propIdTemp = $("#propId_" + i).val();
	    		var arrChecked = new Array();
	    		arrChecked[countFlag] = propIdTemp;
	    		// 遍历改行属性，查看是否有属性被选中
	    		$(".check_" + propIdTemp).each(function(index, element) {
	    					var checkBox = $(".check_" + propIdTemp).eq(index);
	    					var checked = checkBox.prop("checked");
	    					if (checked) {
	    						temp = true;
	    						countFlag++;
	    						if (i == currentIndex) {
	    							arrChecked[1] = propValueId;
	    							flag = true;
	    						} else {
	    							arrChecked[countFlag] = checkBox.val();
	    						}
	    					}
	    				});
	    		if (temp) {
	    			arr[count] = arrChecked;
	    			count++;
	    		}
	    	}
	    	if (first) {// 选中操作
	    		var addTd = false;
	    		// 当前操作行，是否有选中判断
	    		if (flag) {
	    			// 如果该行属性有被选中，列不存在 ，则添加列
	    			if ($("#th_" + propId).length == 0) {
	    				addTd = true;
	    				$("#getPriceTh").before("<th id='th_" + propId + "'>" + propName
	    						+ "</th>");
	    				$(".gdsNameTd").before("<td class='td_" + propId
	    						+ "'  propId='td_" + propId + "_" + propValueId
	    						+ "'><input type='hidden'  class='input-small property' ifHaveto='"+$(obj).attr('ifHaveto')+"' ifBasic='"+$(obj).attr('ifBasic')+"'  propId='"
	    						+ propId + "' valueId='" + propValueId + "' value='"+ propValue + "' >" + propValue + "</td>");
	    				sort[sort.length] = propId;
	    			}

	    		} else {
	    			// 如果该行属性没有一个被选中，列存在则删除列
	    			if ($("#th_" + propId).length != 0) {
	    				$("#th_" + propId).remove();
	    				$(".td_" + propId).remove();
	    				var index = GdsInfoEntry.getIndex(sort, propId);
	    				if (index != null) {
	    					sort[index] = null;
	    				}
	    			}
	    		}
	    		// 针对列排序:
	    		var afterSort = GdsInfoEntry.getMin(arr, sort);
	    		var tableLens = $("#sku-param-table").find("tr").length;
	    		if(tableLens == 2 && $("#esp")){
	    			$("#esp").remove();
	    		}
	    		var tableLen = $("#sku-param-table").find("tr").length;
	    		if (!addTd || tableLen == 1) {
	    			GdsInfoEntry.listAllPro(afterSort, 0, afterSort.length, "",obj);
	    			var htmls = (addHtml + "").split("|||||");
	    			var afterHtml = GdsInfoEntry.getPramaListTdHtml();
	    			for (var i = 0; i < htmls.length; i++) {
	    				var html = htmls[i] + "";
	    				/**
	    				 * 解决IE不支持trim问题
	    				 */
	    				if (!String.prototype.trim) {
	    					String.prototype.trim = function() {
	    						return this.replace(/^\s+|\s+$/g, '');
	    					};
	    				}
	    				if (html.trim() != "") {
	    					$("#sku-param-table").append("<tr class='oneAsSku'>" + html + afterHtml);
	    				}

	    			}
	    		}
	    	} else {
	    		if (!flag) {
	    			// 如果该行属性没有一个被选中，列存在则删除列
	    			if ($("#th_" + propId).length != 0) {
	    				$("#th_" + propId).remove();
	    				$(".td_" + propId).remove();
	    				var index = GdsInfoEntry.getIndex(sort, propId);
	    				if (index != null) {
	    					sort[index] = null;
	    				}
	    				var stockInputHtml = "";
	    				var priceInputHtml = "";
	    				var appPriceInputHtml = "";
	    				if(price_switch=="1"){
	    					priceInputHtml += '<a class="gjpri" href="javascript:void(0)" value="[]" onclick="openPriceWin(this)">高级价格</a>';
	    				}else{
	    					priceInputHtml += "<input type='text' name='price_param' class='input-small' maxlength='9'/>";
	    				}
	    				if(stock_switch=="1"){
	    					stockInputHtml += '<a class="gjkc" href="javascript:void(0)" value="[]" onclick="openStockWin(this)">高级库存</a>';
	    				}else{
	    					stockInputHtml += "<input type='text' name='stock_param' class='input-small' maxlength='9'/>";
	    				}
	    				appPriceInputHtml += "<input type='text' name='appPrice_param'  class='input-small' maxlength='9'/>";
	    				if($(".input-small").length==0){
			    			var htmlMe = "<tr id='esp'> <td class='senior-price'>"+priceInputHtml+"</td>" +
			    			         "<td id='senior-appPrice'>"+appPriceInputHtml+"</td>"+
			    					" <td class='senior-stock'>"+stockInputHtml+"</td>" +
			    					"<td><a href='javascript:void(0)' value='[]' onclick='openSkuPictrueWindow(this)'>图片管理</a></td>";
			    			if ($("#sku-param-table").find("tr").length == 1) {
			    				$("#sku-param-table").append(htmlMe);
			    			};
	    				}
	    			}
	    		} else {
	    			$("[propId='td_" + propId + "_" + propValueId + "']").each(
	    					function(index, element) {
	    						$(this).parent().remove();
	    					});
	    		}
	    	}
	    	var size = $("#proSize").val();
	    	var count = 0;
	    	for (var i = 1; i < size + 1; i++) {
				var propIdTemp = $("#propId_" + i).val();
				// 遍历改行属性，查看是否有属性被选中
				
				$(".check_" + propIdTemp).each(function(index, element) {
					var checkBox = $(".check_" + propIdTemp).eq(index);
					var checked = checkBox.prop("checked");
					if (checked) {
						count ++;
					}
				});
			}
	    	if(count ==0){
	    		$("#sku-param-table").html('');
	    	}
	    	
	    },

	    /**
	     * 对遍历得到的选中值，按照勾选顺序进行排序
	     * 
	     * @param arr
	     *            属性从第一行，到最后一行的选中数组
	     * @param sort
	     *            单品属性行的点击顺序保存数据
	     * @returns {Array}
	     */
	    getMin : function (arr, sort) {
	    	var result = new Array();
	    	for (var i = 0; i < arr.length; i++) {
	    		var min = null;
	    		var minIndex = null;
	    		for (var j = 0; j < arr.length; j++) {
	    			if (arr[j] != null && arr[j].length > 0) {
	    				var tdId = arr[j][0];
	    				if (tdId != null) {
	    					var index = GdsInfoEntry.getIndex(sort, tdId);
	    					if (min == null) {
	    						min = index;
	    						minIndex = j;
	    					} else {
	    						if (Number(index) < Number(min)) {
	    							min = index;
	    							minIndex = j;
	    						}
	    					}
	    				}
	    			}
	    		}
	    		result[i] = arr[minIndex];
	    		arr[minIndex] = null;
	    	}
	    	return result;
	    },
	    /**
	     * 获取某个属性id在 属性选中顺序数组sort中的下标
	     * 
	     * @param arr
	     * @param propId
	     * @returns
	     */
	    getIndex : function (arr, propId) {
	    	for (var i = 0; i < arr.length; i++) {
	    		if (arr[i] == propId) {
	    			return i;
	    		}
	    	}
	    	return null;
	    },
	    getPramaListTdHtml : function () { 
	    	tagIdCount++;
	    	var tdHtml = "<td class='gdsNameTd' id='senior-price'>" + GdsInfoEntry.getInputHtml("price", tagIdCount);
	    	tdHtml += "</td>" + "<td id='senior-appPrice'>" + GdsInfoEntry.getInputHtml("appPrice", tagIdCount);
	    	tdHtml += "</td>" + "<td id='senior-stock'>" + GdsInfoEntry.getInputHtml("stock", tagIdCount);
	    	tdHtml += "</td>" + "<td>" + GdsInfoEntry.getInputHtml("pictrue", tagIdCount);
	    	tdHtml += "</td>" + "</tr>";
	    	return tdHtml;
	    },
	    getInputHtml : function (name, tagIdCount) {
	    	var tagName = "";
	    	if(name == 'price'){
	    		tagName = "price";
	    	}else if(name == "stock"){
	    		tagName = "stock";
	    	}else if(name == "appPrice"){
	    		tagName = "appPrice";
	    	}
	    	var html= "";
	    	if(name == 'pictrue'){
	    		html +="<a href='javascript:void(0)' value='[]' onclick='openSkuPictrueWindow(this)'>图片管理</a>";
	    	}else if(name =='stock'){
	    		if(stock_switch=='1'){
	    			html +="<a class='gjpri' href='javascript:void(0)' value='[]' onclick='openStockWin(this)'>高级库存</a>";
	    		}else{
	    			html +="<input name='"+name+"_param' id='" + name + "_" + tagIdCount+"'onblur=\"validatorSku(this,'"+tagName+"')\"   ";
	    			if(name == "price"){
	    	    		html=html+"' begId='guidePrice_" + tagIdCount+"'    ";
	    	    	}
	    	        html=html+"  class='input-small'  type='text' maxlength='9'/>";
	    		}
	    		
	    	}else if(name =='price'){
	    		if(price_switch=='1'){
	    			html +="<a class='gjpri' href='javascript:void(0)' value='[]' onclick='openPriceWin(this)'>高级价格</a>";
	    		}else{
	    			html +="<input name='"+name+"_param' id='" + name + "_" + tagIdCount+"'onblur=\"validatorSku(this,'"+tagName+"')\"   ";
	    			if(name == "price"){
	    	    		html=html+"' begId='guidePrice_" + tagIdCount+"'    ";
	    	    	}
	    	        html=html+"  class='input-small'  type='text' maxlength='9'/>";
	    		}
	    	}else if(name =='appPrice'){
	    		html += "<input type='text' name='appPrice_param' id='" + name + "_" + tagIdCount+"' onblur=\"validatorSku(this,'"+tagName+"')\"  class='input-small priceNumber' maxlength='9'/>";
	    	}
//	    	else{
//	    		html +="<input name='"+name+"_param' id='" + name + "_" + tagIdCount+"'onblur=\"validatorSku(this,'"+tagName+"')\"   ";
//	    		if(name == "price"){
//		    		html=html+"' begId='guidePrice_" + tagIdCount+"'    ";
//		    	}
//		        html=html+"  class='input-small'  type='text' />";
//	    	}
	    	return html;

	    },
	    listAllPro : function (arr, index, size, html,obj) {
	    	if (Number(index) == Number(size)) {
	    		addHtml = addHtml + html + "|||||";
	    		return "";
	    	}
	    	var temp = arr[index];
	    	var propId = temp[0];
	    	index++;
	    	for (var i = 1; i < temp.length; i++) {
	    		var propValueId = temp[i];
	    		var value = $("#propValue_" + propId + "_" + propValueId).val();
	    		var temphtml = html;
	    		temphtml = temphtml + "<td class='td_" + propId + "' propId='td_"
	    				+ propId + "_" + propValueId + "'>"
	    				+ "<input type='hidden'  class='input-small property' property' ifHaveto='"+$(obj).attr('ifHaveto')+"' ifBasic='"+$(obj).attr('ifBasic')+"'   propId='" + propId
	    				+ "' valueId='" + propValueId + "' value='" + value + "' >"
	    				+ value + "</td>";
	    		GdsInfoEntry.listAllPro(arr, index, size, temphtml,obj);
	    	}
	    	return html;
	    },
	    validateLaderPrice : function(obj,str){
	    	var $this = $(obj);
	    	var $thisValue = Number($.trim($this.val()));
	    	var flag = true;
	    	var $sibling = $this.parent().parent().prevAll();
	    	var $nextAll = $this.parent().parent().nextAll();
	    	var fin = "";
	    	var error = "";
	    	if(str=='amount'){
	    		fin = "order-amount";
	    		error = "起订量要从小到大";
	    		if(!_number.test($thisValue)){
	    			$("#laderPrice-error").text('起订量只能输入整数');
	    		}else{
	    			$("#laderPrice-error").text('');
	    		}
	    	}else if(str=='price'){
	    		fin  = "lader-price";
	    		error = "销售价格要从大到小";
	    		if(!_price.test($thisValue)){
	    			$("#laderPrice-error").text('销售价格格式不合法');
	    		}else{
	    			$("#laderPrice-error").text('');
	    		}
	    	}
//	    	if($nextAll.length >= 1){
	    		$nextAll.each(function(){
		    		var $obj = $(this);
		    		if(str=='amount'){
		    			if(Number($.trim($obj.find('input[name="'+fin+'"]').val())) <= $thisValue){
			    			flag = false;
			    		}
		    		}else{
		    			if(Number($.trim($obj.find('input[name="'+fin+'"]').val())) >= $thisValue){
			    			flag = false;
			    		}
		    		}
		    	});
//	    	}else{
	    		$sibling.each(function(){
	    			var $obj = $(this);
		    		if(str=='amount'){
		    			if(Number($.trim($obj.find('input[name="'+fin+'"]').val())) >= $thisValue){
			    			flag = false;
			    		}
		    		}else{
		    			if(Number($.trim($obj.find('input[name="'+fin+'"]').val())) <= $thisValue){
			    			flag = false;
			    		}
		    		}
	    		});
//	    	}
	    	if(!flag){
	    		$("#laderPrice-error").text(error);
	    	}else{
	    		$("#laderPrice-error").text('');
	    	}
	    },
	    delteLaderPrice : function(obj){
	    	if($("#lader-price-table").find('tr').length <= 4){
	    		$(".add-lader-price").removeAttr('style');
	    	}
	    	$this = $(obj);
	    	$this.parent().parent().remove();
	    	$("#laderPrice-error").text('');
	    },
	    openStockWin : function(obj){
    	        bDialog.open({
    	            title : "高级库存",
    	            width : 680,
    	            height : 400,
    	            url :  GLOBAL.WEBROOT + "/gdsinfoentry/toseniorstock?shopId="+$("#shopId").val()+"&&editFlag='0'",
    	            params : {
    	            	'param' : $(obj).attr('value')
    	            },
    	            callback:function(data){
    	            	if(data && data.results && data.results.length > 0 ){
    						$(obj).attr('value',data.results[0].param);
    					}
    	            }
    	        });
	    },
	    openPriceWin : function(obj){
    	        bDialog.open({
    	            title : "高级价格",
    	            width : 760,
    	            height : 400,
    	            params : {
    	            	'param' : $(obj).attr('value')
    	            },
    	            url : GLOBAL.WEBROOT + "/gdsinfoentry/toseniorprice?shopId="+$("#shopId").val(),
    	            callback:function(data){
    	            	if(data && data.results && data.results.length > 0 ){
    						$(obj).attr('value',data.results[0].param);
    					}
    	            }
    	        });
	    },
	    openSkuPictrueWindow : function(obj){
	        bDialog.open({
	            title : "图片管理",
	            width : 860,
	            height : 550,
	            params : {
					'pictrueList' : $(obj).attr('value'),
					'shopId' : $("#shopId").val()
				},
	            url : GLOBAL.WEBROOT + "/gdsinfoentry/toskupictrues?shopId="+$("#shopId").val(),
	            callback:function(data){
	            	if(data && data.results && data.results.length > 0 ){
						$(obj).attr('value',data.results[0].param);
					}
	            }
	        });
	    },
	    uploadImage : function (object, path) {
	    	var filepath = path;
	    	filepath=(filepath+'').toLowerCase();
	    	var regex = new RegExp(
	    			'\\.(jpg)$|\\.(png)$|\\.(jpeg)$|\\.(gif)$|\\.(bmp)$', 'gi');
	    	/** 上传图片文件格式验证 */
	    	if (!filepath || !filepath.match(regex)) {
	    		eDialog.alert('请选择图片文件(.jpg,.png,.jpeg,.gif,.bmp).');
	    		$(object).value = "";
	    		return;
	    	}
	    	var url = GLOBAL.WEBROOT + '/gdsinfoentry/uploadimage';
	    	var callback = function(data, status) {
	    		/** 上传成功，隐藏上传组件，并显示该图片 */
	    		if (data.success == "ok") {
	    			var index = $(object).attr('index');
					$("#image"+index).attr("src",data.map.imagePath);
					$("#picVfsId"+index).val(data.map.vfsId);
					$("#picVfsId"+index).attr('picName',data.map.imageName);
					$("#picVfsId"+index).attr('mediaRtype','2');
					$('#picVfsId'+index).attr('mediaType','1');
					if(index==99){
						$('#4000').next('div').children('input').val(data.map.vfsId);
					}
					if(index==100){
						$('#4001').next('div').children('input').val(data.map.vfsId);
					}
	    		} else {
	    			eDialog.error(data.message);
	    		}
	    		GdsInfoEntry.bindPicUpload();
	    	};
	    	GdsInfoEntry.ajaxFileUpload(url, false, $(object).attr('id'), "POST", "json", callback);
	    },
	    uploadFile : function(object1, path){
	    	var filepath = path;
	    	filepath=(filepath+'').toLowerCase();
//	    	var regex = new RegExp(
//	    			'\\.(pdf)$|\\.(doc)$|\\.(txt)$', 'gi');
//	    	/** 上传文件格式验证 */
//	    	if (!filepath || !filepath.match(regex)) {
//	    		eDialog.alert('请选择文件(.pdf,).');
//	    		$(object).value = "";
//	    		return;
//	    	}
	    	var url = GLOBAL.WEBROOT + '/gdsinfoentry/uploadfile';
	    	var propId = $(object1).attr('propId');
	    	var callback = function(data, status) {
	    		/** 上传成功，隐藏上传组件，并显示该图片 */
	    		if (data.success == "ok") {
	    			var _this = $("#file"+propId);
	    			_this.val(data.resultMap.vfsId);
	    			_this.attr('disabled',true);
	    			_this.parent().find('.file-wrap').hide();
	    			_this.parent().find('button').show();
	    		} else {
	    			eDialog.error(data.message);
	    		}
	    		$("#attributeForm").valid();
	    		GdsInfoEntry.bindFileUpload();
	    	};
	    	GdsInfoEntry.ajaxFileUpload(url, false, $(object1).attr('id'), "POST", "json", callback);
	    },
	    ajaxFileUpload : function (url, secureuri, fileElementId, type, dataType, callback) {
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
		},
		//新增保存商品
		saveGdsInfo : function(obj){
			if(GdsInfoEntry.initSaveGdsInfoParam()==""){
				return;
			}
			var param = GdsInfoEntry.initSaveGdsInfoParam();
			$.gridLoading({"messsage":"正在保存中...."});
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsinfoentry/savegdsinfo",
				data :param,
				success : function(returnInfo) {
					$.gridUnLoading();
					if(returnInfo.ecpBaseResponseVO.resultFlag=='ok'){
						eDialog.success('保存成功！',{
							buttons:[{
								caption:"确定",
								callback:function(){
									window.location.href = GLOBAL.WEBROOT + '/gdsmanage?shopId='+$("#shopId").val();
						        }
							}]
						}); 
					}else{
						eDialog.error('保存失败！');
					}
				},
				exception : function(){
					$.gridUnLoading();
				}
			});
		},
		//获取商品保存的参数
		initSaveGdsInfoParam : function(){
			var param = {};
			//店铺编码
			param.shopId = $("#shopId").val();
			//多功能开关
			param.multiSwitch = multi_switch;
			// 平台分类
			param.getCategoryParam = GdsInfoEntry.getCategoryParam();
//			// 店铺分类
//			param.getShopCategoryParam = GdsInfoEntry.getShopCategoryParam();
			// 获取自动加载的参数
			param.autoParam = GdsInfoEntry.getAutoParam();
			// 获取单品参数
			param.skuParam = GdsInfoEntry.getSkuParam();
			//获取阶梯价
			param.ladderPriceParam = GdsInfoEntry.getLaderPriceParam();
			// 获取商品图片
			if(GdsInfoEntry.getPictrueParam()==""){
				return "";
			}
			param.pictrueParam = GdsInfoEntry.getPictrueParam();
			// 商品名称
			param.gdsName = $("#gdsName").val();
			// 商品副标题
			param.gdsSubHead = $("#gdsSubHead").val();
			// 商品知道价(产品价格)
			param.guidePrice = $("#guidePrice").val();
			// 商品描述
			param.gdsDesc = $("#gdsDesc").val();
			// 包装清单
			param.gdsPartlist = $("#gdsPartlist").val();
			// 商品类型
			param.gdsTypeId = $("#gdsTypeId").val();
			// 自动上架时间
			param.putonTime = $("#putonTime").val();
			// 自动下架时间
			param.putoffTime = $("#putoffTime").val();
			// 是否赠送积分
			var ifSendscore = "0";
			if ($("#ifSendscore").attr('checked') == 'checked') {
				ifSendscore = "1";
			}
			param.ifSendscore = ifSendscore;
			// 是否单独销售
			var ifSalealone  = "0";
			if ($("#ifSalealone").attr('checked') == 'checked') {
				ifSalealone = "1";
			}
			param.ifSalealone = ifSalealone;
			// 是否推荐
			var ifRecomm = "0";
			if ($("#ifRecomm").attr('checked') == 'checked') {
				ifRecomm = "1";
			}
			param.ifRecomm = ifRecomm;
			// 是否开启存量过低提醒
			var ifStocknotice = "0";
			if ($("#ifStocknotice").attr('checked') == 'checked') {
				ifStocknotice = "1";
			}
			param.ifStocknotice = ifStocknotice;
			// 是否免邮
			var ifFree = "0";
			if ($("#ifFree").attr('checked') == 'checked') {
				ifFree = "1";
			}else{
				// 运费模板编码
				param.shipTemplateId = $("#shipTemplateId").attr('tempId');
			}
			param.ifFree = ifFree;
			// 高级库存是否开启
			var ifDisperseStock = "0";
			if (stock_switch == 1) {
				ifDisperseStock = "1";
			}
			param.ifDisperseStock = ifDisperseStock;
			//是否开启高级价格
			param.ifSeniorPrice = price_switch;
			// 是否阶梯价
			var ifLadderPrice = "0";
			if ($("#gds-lader-price-open").attr('checked') == 'checked') {
				ifLadderPrice = "1";
				
			}
			param.ifLadderPrice = ifLadderPrice;
//			param.ifEntityCode = $("#ifEntityCode").val();
			param.checkedParam = GdsInfoEntry.getCheckedParam();
			return param;
		},
		//获取阶梯价
		getLaderPriceParam : function(){
			if ($("#gds-lader-price-open").attr('checked') != 'checked') {
				return "[]";
			}
			var laderPriceParam = "[";
			$("#lader-price-table").find("tr").each(function(){
				var $this = $(this);
				var amount = $.trim($this.find("input[name='order-amount']").val());
				var price = $.trim($this.find("input[name='lader-price']").val());
				laderPriceParam += "{amount:'"+amount+"',ladderPrice:'"+price+"'},";
			});
			laderPriceParam += "]";
			return laderPriceParam;
		},
		// 获取平台分类
		getCategoryParam : function(){
			var categoryParam = "[";
			$("#catagoryList").find('tr').each(function(){
				$this = $(this);
				var catgCode = $this.children().eq(0).attr('id');
				var gds2catgType = $this.children().eq(1).attr('id');
				categoryParam += "{catgCode:'" + catgCode + "',gds2catgType:'"
					+ gds2catgType + "',catType:'1'},";
			});
			$("#shopCatagoryList").find('tr').each(function(){
				$this = $(this);
				var catgCode = $this.children().eq(0).attr('id');
				categoryParam += "{catgCode:'" + catgCode + "',gds2catgType:'',catType:'2'},";
			});
			categoryParam += "]";
			return categoryParam;
		},
		//获取单品参数
		getSkuParam : function (){
			if(multi_switch=='0'){
				var skuStockList = "";
				var skuPriceList = "";
				//价格
				
				if(price_switch == '0'){
					var skuPrice = $("#productPrice").val();
					if (skuPrice == "") {
						skuPrice = "0";
					}
					
					// 手机专享价.
					var appSpecPrice = $('#appSpecPrice').val();
					if (appSpecPrice == "") {
						appSpecPrice = "0";
					}
					skuPriceList += "[{skuPrice:'"+skuPrice+"',switch:'"+price_switch+"',priceTypeCode:'',priceTypeId:'',priceTarget:'',defaultPrice:'',appSpecPrice:'"+appSpecPrice+"'}]";
				}else{
					//[{skuPrice:'',priceType:'',priceTarget:'',defaultPrice:'',switch:''},{skuPrice:'',priceType:'',priceTarget:'',defaultPrice:''}]
					var skuPrice = $("#productPrice").attr('value');
					if (skuPrice == "") {
						skuPrice = "0";
					}
					skuPriceList += skuPrice;
				}
				
				console.log(skuPriceList);
				//库存
				if(stock_switch == '0'){	
					var skuStock = $("#productStock").val();
					skuStockList +="[{skuStock:'"+skuStock+"',repType:'01',repCode:'',switch:'"+stock_switch+"',areaList:[]}]";
				}else{
					//[{skuStock:'',repCode:'',switch:''},{skuStock:'',repCode:'',switch:''}]
					var skuStock = $("#productStock").attr('value');
					skuStockList += skuStock;
				}
				
				var defaultSku = "[{ifHaveto:'',ifBasic:'',propId:'',propValueId:'',propName:'',propValue:''" +
						",skuPriceList:"+skuPriceList+",skuStockList:"+skuStockList+",skuPictrueList:[],skuAmount:'1',skuId:''}]";
				return defaultSku;
			}
			// 初始化单品参数
			var skuParam = "[";
			var skuAmount = $("#sku-param-table").find("tr[class='oneAsSku']").length;
			$("#sku-param-table").find("tr").each(function(index, element) {
				if (index == 0) {

				} else {
					var $this = $(this);
					var a = $(this).find(".property");
					var skuPriceList = "";
					var skuStockList = "";
					//[{picId:'',meidaRtype:'',mediaType:'',sortNo:''}]
					var skuPictrueList = $this.children().last().find('a').attr('value');
					//价格
					if(price_switch == '0'){
						var skuPrice = $this.find('input[name="price_param"]').val();
						if (skuPrice == "") {
							skuPrice = "0";
						}
						
						// 手机专享价.
						var appSpecPrice = $this.find('input[name="appPrice_param"]').val();
						if (appSpecPrice == "") {
							appSpecPrice = "0";
						}
						skuPriceList += "[{skuPrice:'"+skuPrice+"',switch:'"+price_switch+"',priceTypeCode:'',priceTypeId:'',priceTarget:'',defaultPrice:'',appSpecPrice:'"+appSpecPrice+"'}]";
					}else{
						//[{skuPrice:'',priceType:'',priceTarget:'',defaultPrice:'',switch:''},{skuPrice:'',priceType:'',priceTarget:'',defaultPrice:''}]
						var skuPrice = $this.find('td[id="senior-price"]').find('a').attr('value');
						if (skuPrice == "") {
							skuPrice = "0";
						}
						skuPriceList += skuPrice;
					}
					
					//库存
					if(stock_switch == '0'){	
						var skuStock = $this.find('input[name="stock_param"]').val();
						skuStockList +="[{skuStock:'"+skuStock+"',repType:'01',repCode:'',switch:'"+stock_switch+"',areaList:[]}]";
					}else{
						//[{skuStock:'',repCode:'',switch:''},{skuStock:'',repCode:'',switch:''}]
						var skuStock = skuPrice = $this.find('td[id="senior-stock"]').find('a').attr('value');
						skuStockList += skuStock;
					}
					var count  = 0;
					if(skuStockList ==""){
						skuStockList +="[]";
					}
					if(skuPriceList ==""){
						skuPriceList +="[]";
					}
					$(this).find(".property").each(function(index, element) {
					
						var obj = $(this);
						var propId = obj.attr("propId");
						var valueId = obj.attr("valueId");
						var propName = $("#propName_" + propId).val();
						var ifHaveto = obj.attr('ifHaveto');
						var ifBasic = obj.attr('ifBasic');
						var gdsValue = obj.attr("value");
						skuParam = skuParam + "{ifHaveto:'"+ifHaveto+"',ifBasic:'"+ifBasic+"',propId:'" + propId + "',propValueId:'"
								+ valueId + "',propName:'" + propName + "',propValue:'"
								+ gdsValue + "',skuPriceList:"+skuPriceList+",skuStockList:"+skuStockList+",skuPictrueList:"+skuPictrueList+",skuAmount:'"+skuAmount+"',skuId:''},";
						count ++;
					});
					if(count == 0){
						skuParam = skuParam + "{propId:'',propValueId:'',propName:'',propValue:'',skuPriceList:"+skuPriceList+",skuStockList:"+skuStockList+",skuPictrueList:"+skuPictrueList+",skuAmount:'1',skuId:''},";
					}
				}
			});
			skuParam = skuParam + "]";
			return skuParam;
		},
		//获取商品图片参数
		getPictrueParam : function(){
			var pictrueParam = "[";
			var length = $("input[name='mainPicVfsId']").length;
			var picCount = 0;
			var imgAry = $("input[name='mainPicVfsId']");
			for(var i = 1 ;i<length + 1;i++){
				
				var sortNo = $(imgAry[i - 1]).attr("main");
				
				if($("#picVfsId1").val() == ""){
					eDialog.error("请上传商品主图！");
					return "";
				}
				/*if($("#picVfsId6").val() == ""){
					eDialog.error("产品版权页图片！");
					return "";
				}
				if($("#picVfsId7").val() == ""){
					eDialog.error("产品包装条形码特写图片！");
					return "";
				}*/
				var vfsId = $("#picVfsId"+sortNo).val();
				if(vfsId == undefined || vfsId==""){
					continue;
				}
				var picName = $("#picVfsId"+sortNo).attr('picName');
				var mediaRtype = $("#picVfsId"+sortNo).attr('mediaRtype');
				var mediaType = $("#picVfsId"+sortNo).attr('mediaType');
				var mediaId = $("#picVfsId"+sortNo).attr('mediaId');

				//var sortNo = $("#picVfsId"+sortNo).attr('main');
				var url = $("#picVfsId"+sortNo).parent().parent().find('img').attr('src');
				
				if($("#picVfsId"+sortNo).val() != ""){
					//meidaRtype 1 为媒体库引用，2 直接上传 。 mediaType 1位图片，2位视频 3为音频
					pictrueParam += "{picVfsId"+picCount+":'"+vfsId+"',picName:'"+picName+"',mediaRtype:'"+mediaRtype+"',mediaType:'"+mediaType+"',sortNo:'"+sortNo+"',url:'"+url+"',mediaId:'"+mediaId+"'},";
					picCount ++;
				}
			}
			pictrueParam += "]";
			return pictrueParam;
		},
		getAutoParam : function(){
			var PROP_VALUE_TYPE_1 = $("#PROP_VALUE_TYPE_1").val();
			var PROP_VALUE_TYPE_2 = $("#PROP_VALUE_TYPE_2").val();
			var PROP_VALUE_TYPE_3 = $("#PROP_VALUE_TYPE_3").val();
			var PROP_VALUE_TYPE_4 = $("#PROP_VALUE_TYPE_4").val();
			// 获取规格参数
			var detailParam = "[";
			$(".autoParam").each(function(index, element) {
				var obj = $(this);
				var ifmulti = obj.attr('ifmulti');
				var propId = obj.attr("propId");
				var propName = obj.attr("propName");
				var propType = obj.attr("propType");
				var ifBasic = obj.attr("ifBasic");
				if(ifmulti =='single'){//单选值的
					if (propId != undefined) {
						var value = $.trim(obj.val());
						if ("" != value) {
							valueId = value;
							gdsValue = escape(obj.find("option:selected").text());
							detailParam = detailParam + "{propType:'"+propType+"',propValueType:'"+PROP_VALUE_TYPE_2+"',propId:'"
							+ propId + "',valueId:'" + value + "',propName:'"
							+ propName + "',gdsValue:'" + gdsValue + "',ifBasic:'"+ifBasic+"',editor:'0'},";
						}
					}
				}else if(ifmulti =='input'){//手动录入的
					if (propId != undefined) {
//						var propName = obj.attr("propname");
						if (true) {
							valueId =  obj.attr('valueId');
							html =  escape(obj.val());
							detailParam = detailParam + "{propType:'"+propType+"',propValueType:'"+PROP_VALUE_TYPE_1+"',propId:'"
							+ propId + "',valueId:'" + valueId + "',propName:'"
							+ propName + "',gdsValue:'" + html + "',ifBasic:'"+ifBasic+"',editor:'0'},";
						}
					}
				}else if(ifmulti == 'multi'){//多选值的
					var arr = new Array;
					obj.parent().find(".selcheckbox").each(function(){
						$this = $(this);
						if($this.attr('checked')=='checked'){
							arr.push($this.attr('id')+":"+$this.val());
						}
					});
					if(arr.length>=1){
						for(var i =0;i<arr.length;i++){
							detailParam = detailParam + "{propType:'"+propType+"',propValueType:'"+PROP_VALUE_TYPE_3+"',propId:'"
							+ propId + "',valueId:'" + arr[i].split(':')[0] + "',propName:'"
							+ propName + "',gdsValue:'" + escape(arr[i].split(':')[1]) + "',ifBasic:'"+ifBasic+"',editor:'0'},";
						}
					}else{
						detailParam = detailParam + "{propType:'"+propType+"',propValueType:'"+PROP_VALUE_TYPE_3+"',propId:'"
						+ propId + "',valueId:'',propName:'"
						+ propName + "',gdsValue:'',ifBasic:'"+ifBasic+"',editor:'0'},";
					}
					
				}
			});
			//获取富文本的
			$("textarea[nameValue='editor']").each(function(){
				var _obj = $(this);
				var _value = escape(_obj.val());
				var _propId = _obj.attr("propId");
				var _propName = _obj.attr("propName");
				var _propType = _obj.attr("propType");
				var _ifBasic = _obj.attr("ifBasic");
				detailParam = detailParam + "{propType:'"+_propType+"',propValueType:'"+PROP_VALUE_TYPE_1+"',propId:'"
				+ _propId + "',valueId:'',propName:'"
				+ _propName + "',gdsValue:'"+_value+"',ifBasic:'"+_ifBasic+"',editor:'1'},";
			});
			//获取附件的
			$(".autoParamFile").each(function(){
				var _obj = $(this);
				var _value = escape(_obj.val());
				var _propId = _obj.attr("propId");
				var _propName = _obj.attr("propName");
				var _propType = _obj.attr("propType");
				var _ifBasic = _obj.attr("ifBasic");
				detailParam = detailParam + "{propType:'"+_propType+"',propValueType:'"+PROP_VALUE_TYPE_1+"',propId:'"
				+ _propId + "',valueId:'',propName:'"
				+ _propName + "',gdsValue:'"+_value+"',ifBasic:'"+_ifBasic+"',editor:'4'},";
				//editor 0 不是富文本  1 是富文本 4 是附件文件
			});
			detailParam = detailParam + "]";
			return detailParam;
		},
		getCheckedParam : function(){
			var PROP_VALUE_TYPE_3 = $("#PROP_VALUE_TYPE_3").val();
			var param = "[";
			// 遍历改行属性，查看是否有属性被选中
			var size = $("#proSize").val();
			for (var i = 1; i < size + 1; i++) {
				var propIdTemp = $("#propId_" + i).val();
				// 遍历改行属性，查看是否有属性被选中
				$(".check_" + propIdTemp).each(function(index, element) {
					var checkBox = $(".check_" + propIdTemp).eq(index);
					var checked = checkBox.prop("checked");
					var valueId = checkBox.val();
					var propName = $("#propName_" + propIdTemp).val();
					var gdsValue = $("#propValue_" + propIdTemp + "_" + valueId).val();
					var _ifBasic = checkBox.attr('ifBasic');
					if (checked) {
						param = param + "{propType:'1',propValueType:'"+PROP_VALUE_TYPE_3+"',propId:'" + propIdTemp
								+ "',valueId:'" + valueId + "',propName:'" + propName
								+ "',gdsValue:'" + gdsValue + "',ifBasic:'"+_ifBasic+"'},";
					}
				});
			}
			param += "]";
			return param;
		},
		queryMediaList : function(){
			var param = {
					shopId : $("#shopId").val(),
					mediaName : $("#mediaName").val(),
					picCatgCode : $("#picCatgCode").val()
					
			};
			$.gridLoading({"el":"#mediaList","messsage":"正在加载中...."});
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsinfoentry/gridmedialist",
				dataType : "html",
				data : param,
				async : true,
				success : function(returnInfo) {
					$.gridUnLoading({"el":"#mediaList"});
					$("#mediaList").empty();
					$("#mediaList").html(returnInfo);
					//双击选中
//					$(".dbchoose").each(function(){
//						$(this).dblclick(function(){
//				    		var $dbthis = $(this);
//				        	$(".imgcont").each(function(){
//				    			var $this = $(this);
//				    			if($this.attr('style') != undefined){
//				    				$this.children('img').attr('src',$dbthis.children('img').attr('src'));
//				    				$this.parent().find("input[name='mainPicVfsId']").val($dbthis.children('img').attr('mediaId'));
//				    				$this.parent().find("input[name='mainPicVfsId']").attr('mediaRtype','1');
//				    				$this.parent().find("input[name='mainPicVfsId']").attr('mediaType','1');
//				    				$this.removeAttr("style");
//				    			}
//				        	});
//				    	});
//					});
				}
			});
		},
		deleteCatPlat : function(obj){
			$(obj).parent().parent().remove();
		},
		saveGdsInfoValidator : function(){
			//校验多功能开启时候，必选属性
			if(multi_switch==1){
				var size = $("#sku-param-table").find("tr").length;
				if(size<=0){
					eDialog.confirm("请至少选择一个规格属性！", {
						buttons : [ {
							caption : '确认',
							callback : function() {
								$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
										: $('body'))
										: $('html,body');
								$body.animate({
									scrollTop : $("#shopId").offset().top
								}, "fast");
							}
						}]});
					return false;
				}
				// 校验单品价格和单品库存
				var _c = 0;
				$("#sku-param-table").find("tr").each(function(index, element) {
					if (index == 0) {
		
					} else {
						var $this = $(this);
						// [{picId:'',meidaRtype:'',mediaType:'',sortNo:''}]
						// 价格
						if (price_switch == '0') {
							var _skuPriceObj = $this.find('input[name="price_param"]');
							var skuPrice = _skuPriceObj.val();
							if (skuPrice != "" && !_price.test(skuPrice)) {
								_skuPriceObj.nextAll().remove();
								_skuPriceObj.after("</br><b style='color:#C62626'>价格格式不合法</b>");
								_c++;
							}
						}
						// 库存
						if (stock_switch == '0') {
							var _skuStockObj = $this.find('input[name="stock_param"]');
							var skuStock = _skuStockObj.val();
							if (skuStock != "" && !_number.test(skuStock)) {
								_skuStockObj.nextAll().remove();
								_skuStockObj.after("</br><b style='color:#C62626'>只能输入整数</b>");
								_c++;
							}
						}
					}
				});
				if (_c >= 1) {
					$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
							: $('body'))
							: $('html,body');
					$body.animate({
						scrollTop : $("#gds-skuParam").offset().top
					}, "fast");
					return false;
				}
			}
			
			// 校验阶梯价
			var _ladderCount = 0;
			if ($("#gds-lader-price-open").attr('checked') == 'checked') {
				var amoutText = "";
				var priceText = "";
				$("#lader-price-table").find("tr").each(
						function() {
							var $this = $(this);
							var amount = $.trim($this.find(
									"input[name='order-amount']").val());
							var price = $.trim($this.find(
									"input[name='lader-price']").val());
							if (!_number.test(amount)) {
								amoutText = "起订量只能输入整数。";
								_ladderCount++;
							}
							if (!_price.test(price)) {
								priceText = "销售价格格式不合法";
								_ladderCount++;
							}
	
							var fin = "";
							var error = "";
							var str = $this.find("input[name='order-amount']")
									.attr('name');
							var str1 = $this.find("input[name='lader-price']")
									.attr('name');
							if (str == 'order-amount') {
								var $sibling = $this.find(
										"input[name='order-amount']").parent()
										.parent().prevAll();
								var $nextAll = $this.find(
										"input[name='order-amount']").parent()
										.parent().nextAll();
								fin = "order-amount";
								error = "起订量要从小到大";
								var flag = true;
								if (!_number.test(amount)) {
									$("#laderPrice-error").text('起订量只能输入整数');
									_ladderCount ++;
								} else {
									$("#laderPrice-error").text('');
								}
								$nextAll
										.each(function() {
											var $obj = $(this);
											if (Number($.trim($obj.find(
													'input[name="' + fin + '"]')
													.val())) <= amount) {
												_ladderCount ++;
												flag = false;
											}
										});
								$sibling
										.each(function() {
											var $obj = $(this);
											if (Number($.trim($obj.find(
													'input[name="' + fin + '"]')
													.val())) >= amount) {
												_ladderCount ++;
												flag = false;
											}
										});
								if (!flag) {
									$("#laderPrice-error").text(error);
									return false;
								}
							}
							if (str1 == 'lader-price') {
								var $sibling = $this.find(
										"input[name='lader-price']").parent()
										.parent().prevAll();
								var $nextAll = $this.find(
										"input[name='lader-price']").parent()
										.parent().nextAll();
								fin = "lader-price";
								error = "销售价格要从大到小";
								var flag = true;
								if (!_price.test(price)) {
									$("#laderPrice-error").text('销售价格格式不合法');
									_ladderCount ++;
								} else {
									$("#laderPrice-error").text('');
								}
								$nextAll
										.each(function() {
											var $obj = $(this);
											if (Number($.trim($obj.find(
													'input[name="' + fin + '"]')
													.val())) >= price) {
												_ladderCount ++;
												flag = false;
											}
										});
								$sibling
										.each(function() {
											var $obj = $(this);
											if (Number($.trim($obj.find(
													'input[name="' + fin + '"]')
													.val())) <= price) {
												_ladderCount ++;
												flag = false;
											}
										});
								if (!flag) {
									$("#laderPrice-error").text(error);
									return false;
								}
							}
	
						});
				if (_ladderCount > 0) {
					if(amoutText != "" || priceText !=""){
						$("#laderPrice-error").text(amoutText + "" + priceText);
					}
					$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
							: $('body'))
							: $('html,body');
					$body.animate({
						scrollTop : $("#gds-priceSetting").offset().top
					}, "fast");
					return false;
				}
			}
			return true;
		},
		validatorSku : function(obj,str){
			var value = $.trim($(obj).val());
			var skuPriceObj = $(obj);
			if(skuPriceObj.attr('name')=='price_param'){
				if(value !="" && !_price.test(value)){
					skuPriceObj.nextAll().remove();
					skuPriceObj.parent().append("</br><b name='error' style='color:#C62626'>价格格式不合法</b>");
				}else{
					skuPriceObj.nextAll().remove();
				}
			}else if(skuPriceObj.attr('name')=='stock_param'){
				if(value !="" && !_number.test(value)){
					skuPriceObj.nextAll().remove();
					skuPriceObj.parent().append("</br><b name='error' style='color:#C62626'>只能输入整数</b>");
				}else{
					skuPriceObj.nextAll().remove();
				}
			}
		}
		
};

