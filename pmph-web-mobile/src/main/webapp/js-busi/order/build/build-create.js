$(function(){
	
	/*
	 * nav
	 * 
	 * 一般事件 start
	 * 
	 * 收货地址 start
	 * 支付方式 start
	 * 优惠券 start
	 * 资金账户 start
	 * 发票信息 start
	 * 配送方式 start
	 * 
	 * 金额修改事件
	 */
	/**
	 * 
	 */
	$('#taxpayer_no').keyup(function(){
		var _value = $(this).val();
		if (/^(\d|[a-zA-Z])+$/.test(_value)){
			
		}else{
			$(this).val('');
		}
	});
	/**
	 * 全局参数
	 */
	var buildObj = {
			currentInvoice: {}, //当前确定开票对象
			shopClass: ".js_build_shop",  //店铺样式关键词
			addrUpper: "10"  //收货地址上限数量
	};   
	
	/**
	 * 一般事件 start ============
	 */
	//折叠面板 ：店铺、资金账户。
	$("span.ui_arrow").click(function(e){
		var self = $(this);
		var aright = "ui-arrow-right";
		var abottom = "ui-arrow-bottom";
		if(self.hasClass(aright)){ //向右时展开
			self.removeClass(aright).addClass(abottom);
			self.closest("div").next("div").show();
		}else if(self.hasClass(abottom)){ //向下时折叠
			self.removeClass(abottom).addClass(aright);
			self.closest("div").next("div").hide();
		}else{
			//DO NOTHING
		}
	});
	//金额计算,店铺订单
	buildObj.updateRealMoney = function(obj){
		//buildObj.shopClass==.js_build_shop
		var _shop = $(obj).closest(buildObj.shopClass);
		
		//固定订单金额
		var _orderMoney = _shop.find('input.js_calc_orderMoney').val();
		
		//资金账户
		var _deduct = 0;
		_shop.find('input[name$=".deductOrderMoney"]').each(function(){
			_deduct = _deduct + Number($(this).val());
		});
		
		//运费
		var _realExpressFees = _shop.find('input.js_calc_realExpressFees').val();
		//单价减免
		var _discountMoney = Number(_shop.find('input.js_calc_discountMoney').val());

		//优惠券
		var _coupShop = _shop.find('input[class="js_build_coupShopValue"]');
//		//优惠券的值获取判断
		var _coupMoney = 0;
		if(_coupShop.length) {
			_coupMoney = Number(_coupShop.val());
			_shop.find('span[id="coupMoney"]').text(ebcUtils.numFormat(_coupMoney/100,2));
		}
		if(_coupMoney == 0){
			$(".coup-money").hide();
		}else{
			$(".coup-money").show();
		}
		//新增代码：author:mwz
		$(".coup-money").show();

		//固定总价
		var oldReal = Number(_orderMoney)+Number(_realExpressFees);
		_shop.find('input.js_calc_realMoney').val(oldReal-_deduct-_coupMoney-_discountMoney);
		
	}
	//计算总价,更新资费
	buildObj.totalCount = function(){
		//店铺总的返现满减统计
		var shopDiscounts = 0;
		$('input.js_calc_shopDiscounts').each(function(){
			shopDiscounts += Number($(this).val());
		});

		//计算真实总金额
		var total = 0;
		$('input.js_calc_realMoney').each(function(){
			total += Number($(this).val());
		});

		//计算总运费
		var real_express_fees = 0;
		$('input.js_calc_realExpressFees').each(function(){
			real_express_fees += Number($(this).val());
		});

		//计算资金账户费用
		var acct_fees = 0;
		$('input[name$=".deductOrderMoney"]').each(function(){
			acct_fees += Number($(this).val());
		});

		//展示金额
		$('.js_calc_allMoney').html("&yen;"+ebcUtils.numFormat(total/100, 2));
		$('#build_realExpressFees').html("&yen;"+ebcUtils.numFormat(real_express_fees/100, 2));

		$("#build_orderMoneys").html(function(index, text){
			var mo = $(this).attr("moneys");
			return "&yen;"+ebcUtils.numFormat(Number(mo)/100, 2);
		});
		$('#build_acct_fees').html("- &yen;"+ebcUtils.numFormat(acct_fees/100, 2));
	}
	//更新相关资费
	buildObj.updateOrds = function(obj){
		buildObj.updateRealMoney(obj);
		buildObj.totalCount();
	}
	//模糊电话 
	buildObj.hidePhone = function(phone){
		var newphone = phone;
		if(phone.length == 11){
			var head = phone.substring(0,3);
			var mid = "****";
			var tail = phone.substring(7,12);
			newphone = head+mid+tail;
		}
		return newphone;
	};
	buildObj.initBuildInvoiceBox = function(inv){
		var scope = $("#build_invoice_box");
		if(!$.isEmptyObject(inv)){//不为空
			//发票抬头
			$("#build_inv_title", scope).val(inv.invoiceTitle);
			//纳税人识别码
			$("#taxpayer_no", scope).val(inv.invoiceTaxpayerNo);
			//附加明细
			$("#build_inv_detail", scope).children("li").each(function(i, el){
				var self = $(this);
				inv.detailFlag == self.attr("value") ? self.addClass("active") : self.removeClass("active");
			});
			//发票内容
			$("#build_inv_content", scope).children("li").each(function(i, el){
				var self = $(this);
				inv.invoiceContent == self.attr("invoice") ? self.addClass("active") : self.removeClass("active");
			});
		}else{
			//置空
			$("#build_inv_title", scope).val("");
			//默认否
			$("#build_inv_detail", scope).children("li").last().addClass("active").prev().removeClass("active");
			//缺省首项
			$("#build_inv_content", scope).children("li").first().addClass("active").siblings("li").removeClass("active");
		}
	}
	//限制只输入数字，调用代码
    //依赖juqery  
    //integer：是否为整数，默认为true
    //positive：是否为正数，默认为true
	buildObj.checkNum = function(inputnum,integer,positive) {
		var int = true,pos = true;
		var dotLen = 0; //小数点数量
		if($.type(integer)!='undefined' && $.type(integer)=='boolean') int = integer;
        if($.type(positive)!='undefined' && $.type(positive)=='boolean') pos = positive;
        
        var arrCode = inputnum.split("");
        inputnum = $($.map(arrCode,function(el, i){
            return  checkcode(i,el,int,pos);
        })).get().join("");
        
        function checkcode(i,key_code,integer,positive){
            
            var code = key_code.replace(/[^\d|\.|-]/g,'');
            console.log(dotLen);
            //限制只允许输入一个小数点符号
            if(code==".") {
                dotLen++;
                if(int){
                    return "";
                }else{
                    if(dotLen>1){
                        return "";
                    }
                }
            }

            //限制只允许输入一个负数符号
            if(code=="-" && positive) {
                return "";
            }else{
                if(code=="-" && i!=0){
                    return "";
                }
            }

            return code;
        }
        
        return inputnum;
    }
	//统计优惠券金额
	buildObj.totalCoupCount = function(){
		var coupCount = 0;
		$('input[class="js_build_coupShopValue"]').each(function(){
			coupCount += Number($(this).val());
		});
		$('#build_coup_fees').html('- &yen;'+ebcUtils.numFormat(coupCount/100,2));
	}
	/**
	 * 一般事件 end ============
	 */
	
	/**
	 * 收货地址 start ============
	 */
	/* -----------------管理----------------------*/
	//关闭地址管理
	$('#canvas_custaddr').click(function(e){
        $('#build_canvas_custaddr').offCanvas('close');
        e.preventDefault();
        e.stopPropagation();
    });
	//打开地址管理
	$('#build_custaddr').click(function(e){
        $('#build_canvas_custaddr').offCanvas('open');
       // window.history.pushState({ title: "购物车" }, "购物车", GLOBAL.WEBROOT+"/order/cart/list");
      //  var json=window.history.state;
		var chkval = $("#js_addrlist").find("input[type='radio']:checked").val();
		var divval = $("#js_addrlist").find("input[type='radio']:checked").parent().parent().parent();
		var firstval = $("#js_addrlist .js_addrunit:first").find("input[type='radio']").val();
		if(chkval != "undefined" && chkval != firstval){
			$("#js_addrlist").prepend(divval);
		}
        $(window).off('resize.offcanvas.amui orientationchange.offcanvas.amui');
        e.preventDefault();
        e.stopPropagation();
    });
	//设置默认地址
	$("#js_addrlist").delegate(".js_build_radioaddr","click",function(e){
        e.stopPropagation();
		var self = $(this);
		var addrId = self.val();
		$.eAjax({
			url : GLOBAL.WEBROOT + "/custaddr/setaddr",
			data : [{name: 'id',value: addrId},{name: "staffId",value: $('#staffId').val()}],
			datatype:'json',
			success : function(returnInfo) {
				if(returnInfo&&returnInfo.resultFlag=='ok'){
					new AmLoad({content:'设置默认地址成功'});
					//变更收货地址
					//1.1设置addrId
					$("#addrId").val(addrId);
					//1.2设置文本
					var bro = self.closest("div.js_addrunit").find("#build_custaddr_bro")
					.append().html();
					$("#build_custaddr").html(bro);
					$("#build_custaddr").find(".adre-w").after('<span class="ui-arrow ui-arrow-right"></span>');
					//1.3样式变更
					self.closest("div.js_addrunit").addClass("odr-check").siblings("div.odr-box").removeClass("odr-check");
					
					addrchange();
				
				}else{
					eDialog.alert("设置失败："+returnInfo.resultMsg);
				}
				
			}
		});
	});
	//阻止radio的事件冒泡
	$("#js_addrlist").delegate("label","click",function(e){
		e.stopPropagation();
	});
	//设置当前收货地址
	$("#js_addrlist").delegate(".js_addrunit","click",function(e){
		var self = $(this);
		var addrId = self.find("input[name='radioaddr']").val();
		//1.样式
		self.addClass("odr-check").siblings("div.odr-box").removeClass("odr-check");
		//2.设置收货地址
		//2.1设置addrId
		$("#addrId").val(addrId);
		//2.2设置文本
		var bro = self.find("#build_custaddr_bro").html();
		$("#build_custaddr").html(bro);
		$("#build_custaddr").find(".adre-w").after('<span class="ui-arrow ui-arrow-right"></span>');
		
		addrchange();
		
	});
	//删除
	$("#js_addrlist").delegate(".js_build_deladdr","click",function(e){
		e.stopPropagation();
		var self = $(this);
		var addrId = self.closest("div").siblings("div").find("input.js_build_radioaddr").val();
		//调用
		eDialog.confirm("确定要删除吗？",function() {
			$.eAjax({
				data: [{name: 'id', value: addrId}, {name: "staffId", value: $('#staffId').val()}],
				url: GLOBAL.WEBROOT + "/custaddr/deladdr",
				success: function (returnInfo) {
					if (returnInfo && returnInfo.resultFlag == 'ok') {
						//判断是否当前“默认”，是则联动前页面
						var isDef = addrId == $("#addrId").val();
						if (isDef) {
							//1.1设置addrId,置空
							$("#addrId").val("");
							//1.2设置文本
							$("#build_custaddr").html("设置收货地址");
						}
						//删除目标
						self.closest("div.js_addrunit").remove();
					} else {
						eDialog.alert('系统异常');
					}
				}
			});
		});
		return false;
	});
	//编辑
	$("#js_addrlist").delegate(".js_build_editaddr","click",function(e){
	    e.stopPropagation();  
		var self = $(this);
		var scope = $(self).closest("div.js_addrunit");
		//初始化数据,填充界面
		//得到数据
		var id = self.closest("div").siblings("div").children("input").val();
		var contactName = $("#addrunit_contactName", scope).val();
		var contactPhone = $("#addrunit_contactPhone", scope).val();
		var province = $("#addrunit_province", scope).val();
		var cityCode = $("#addrunit_cityCode", scope).val();
		var countyCode = $("#addrunit_countyCode", scope).val();
		var chnlAddress = $("#addrunit_chnlAddress", scope).val();
		var postCode = $("#addrunit_postCode", scope).val();
		//初始化
		var dest = $("#build_form_editaddr");
		$("input[name='id']", dest).val(id);
		$("input[name='contactName']", dest).val(contactName);
		$("input[name='contactPhone']", dest).val(contactPhone);
		$("textarea[name='chnlAddress']", dest).val(chnlAddress);
		$("input[name='postCode']", dest).val(postCode);
		resetCustaddrArea(true, province, cityCode, countyCode);//初始化下拉框
		
		$("#build_addr_title").html("编辑");//标题
		openCustaddrEdit();//打开编辑界面
	});
	//新增收货地址
	$("#build_newcustaddr").click(function(e){
		//清空数据
		$(":input:not(select)", $("#build_form_editaddr")).val("");
		resetCustaddrArea(true, "110000");//初始化为 北京
		$("#build_addr_title").html("新增");//标题
		openCustaddrEdit();//打开新增界面
		
	});
	//保存新增收货地址
	$("#build_addr_save").click(function(e){
		//校验容量上限
		if($("#build_canvas_custaddr .js_addrunit").length > buildObj.addrUpper){
			eDialog.alert("地址数量到达上限");
			return;
		}
		if(!$("#build_form_editaddr").valid()) return false;
		var val = ebcForm.formParams($("#build_form_editaddr"));
		$.eAjax({
			url : GLOBAL.WEBROOT + "/order/build/saveaddr",
			data : val,
			datatype:'json',
			success : function(data) {
				if(data&&data.resultFlag == 'ok'){
					//成功
					var scope = $("#build_form_editaddr");
					var id = $("input[name='id']", scope).val();
					var contactName = $("input[name='contactName']", scope).val();
					var contactPhone = $("input[name='contactPhone']", scope).val();
					var chnlAddress = $("textarea[name='chnlAddress']", scope).val();
					var province = $("select[name='province']", scope).find("option:selected").text();
					var provinceValue = $("select[name='province']", scope).val();
					var cityCode = $("select[name='cityCode']", scope).find("option:selected").text();
					var cityCodeValue = $("select[name='cityCode']", scope).val();
					var countyCode = $("select[name='countyCode']", scope).find("option:selected").text();
					var countyCodeValue = $("select[name='countyCode']", scope).val();
					var postCode = $("input[name='postCode']", scope).val();
					//封装
					var item = {};
					item.id = data.id;
					item.contactName = contactName;
					item.contactPhone = contactPhone;
					item.province = provinceValue;
					item.cityCode = cityCodeValue;
					item.countyCode = countyCodeValue;
					item.chnlAddress = chnlAddress;
					item.addrStr = province+" "+cityCode+" "+countyCode+" "+chnlAddress;
					item.postCode = postCode;

					if(id == ""){//新增
						if($("#js_addrlist .js_addrunit").length == 0){
							item.defChecked = "checked";
						}

						$("#js_addrlist").prepend(getHtmlOfAddrunit(item));
						//是否第一个
						if($("#js_addrlist .js_addrunit").length == 1){
							var addr = $("#js_addrlist").find(".js_addrunit").eq(0);
							addr.addClass("odr-check").siblings("div.odr-box").removeClass("odr-check");

							//修改收货地址
							$("#addrId").val(data.id);
							//修改确认界面
							$("#build_custaddr").html($("#js_addrlist .js_addrunit").find("#build_custaddr_bro").html());
							$("#build_custaddr").find(".adre-w").after('<span class="ui-arrow ui-arrow-right"></span>');
						}

					}else{//修改
						var checkVal = "";
						$("#js_addrlist").children("div.js_addrunit").find("input.js_build_radioaddr").each(function(i, el){
							if($(this).attr("checked")){
								checkVal = $(this).val();
							}
						});
						//找到id在js_addrlist中的位置
						$("#js_addrlist").children("div.js_addrunit").find("input.js_build_radioaddr").each(function(i, el){
							var self = $(this);
							if(self.val() == id){//定位到目标
								//替换
								var dest = self.closest("div.js_addrunit");
								// if(id == $("#addrId").val()) item.defChecked = "checked";
								if(checkVal == id){
									item.defChecked = "checked";
								}
								dest.replaceWith(getHtmlOfAddrunit(item));
								// //是否当前使用
								// if(id == $("#addrId").val()){//修改确认界面
								// 	//还原“选中”状态
								// 	self.closest("div.js_addrunit").find("input.js_build_radioaddr").attr("checked", true);
								// 	//修改确认界面
								// 	$("#build_custaddr").html(dest.find(".js_addrunit").html());
								// 	// $("#build_custaddr").find(".adre-w").after('<span class="ui-arrow ui-arrow-right"></span>');
								// }
								//
								return false;//退出
							}
						});
						$("#js_addrlist").children("div.js_addrunit").find("input.js_build_radioaddr").each(function(i, el){
							if($(this).val()  == id){
								var addr = $(this).closest("div.js_addrunit");
								addr.addClass("odr-check").siblings("div.odr-box").removeClass("odr-check");
								//修改收货地址
								$("#addrId").val(data.id);
								//修改确认界面
								$("#build_custaddr").html(addr.find("#build_custaddr_bro").html());
							}
						});
					}
					//返回
					closeCustaddrEdit();
				}else{
					eDialog.alert(data.resultMsg);
				}
			}
		});
	});
	/* -----------------新增、修改----------------------*/
	//关闭地址编辑、新增
	$('#canvas_custaddr_edit').click(function(e){
		$('.am-offcanvas-content').show();
		$('#submitForm').show();
        $('#build_canvas_custaddr_edit').offCanvas('close');
        e.preventDefault();
        e.stopPropagation();
    });
	var closeCustaddrEdit = function(){
		$('.am-offcanvas-content').show();
		$('#submitForm').show();
		$('#build_canvas_custaddr_edit').offCanvas('close');
	}
	//打开地址编辑、新增
	var openCustaddrEdit = function(){
		$('.am-offcanvas:not(#build_canvas_custaddr) .am-offcanvas-content').hide();
		$('#submitForm').hide();
		$('#build_canvas_custaddr_edit .am-offcanvas-content').show();
		$('#build_canvas_custaddr_edit').offCanvas('open');
		$('#build_canvas_custaddr .am-offcanvas-content').hide(500);
		$(window).off('resize.offcanvas.amui orientationchange.offcanvas.amui');
	}
	//重置地区
	//isval true:通过value设置，false:通过text设置
	//省,市,区
	var resetCustaddrArea = function(isval, province, cityCode, countyCode){
		//p-code
		if(province){
			var code = $("#p-code");
			if(isval){
				code.val(province);
			}else{
				code.find("option[text="+province+"]").attr("selected",true);
			}
			onchange(code[0]);
		}
		//city-code
		if(cityCode){
			var code = $("#city-code");
			if(isval){
				code.val(cityCode);
			}else{
				code.find("option[text="+cityCode+"]").attr("selected",true);
			}
			onchange(code[0]);
		}
		//county-code
		if(countyCode){
			var code = $("#county-code");
			if(isval){
				code.val(countyCode);
			}else{
				code.find("option[text="+countyCode+"]").attr("selected",true);
			}
			onchange(code[0]);
		}
		
		function onchange(el){
			if(document.all){  
				el.fireEvent("onchange");  
			}else{  
				var evt = document.createEvent('HTMLEvents');  
				evt.initEvent('change',true,true);  
				el.dispatchEvent(evt);  
			}
		}	
		
	}
	
	//得到html
	var getHtmlOfAddrunit = function(data){
		var html = "";
		html +='<div class="odr-box odr-pad js_addrunit">';
		html +='    <div id="build_custaddr_bro">';
		html +='        <div class="r-address">';
		html +='            <i class="icon icon-name"></i>';
		html +='            <div>'+data.contactName+'<span class="c-orange mlr">|</span> ';
		html +='            '+buildObj.hidePhone(data.contactPhone)+'</div>';
		html +='            <input type="hidden" id="addrunit_contactName" value="'+data.contactName+'" />';
		html +='            <input type="hidden" id="addrunit_contactPhone" value="'+data.contactPhone+'" />';
		html +='        </div>';
		html +='        <div class="r-address">';
		html +='            <i class="icon icon-adr"></i>';
		html +='            <div class="adre">';
		html +='                <span class="adre-w"> '+data.addrStr+' </span>';
		html +='            	<input type="hidden" id="addrunit_province" value="'+data.province+'" />';
		html +='            	<input type="hidden" id="addrunit_cityCode" value="'+data.cityCode+'" />';
		html +='            	<input type="hidden" id="addrunit_countyCode" value="'+data.countyCode+'" />';
		html +='            	<input type="hidden" id="addrunit_chnlAddress" value="'+data.chnlAddress+'" />';
		html +='            	<input type="hidden" id="addrunit_postCode" value="'+data.postCode+'" />';
		html +='            </div>';
		html +='        </div>';
		html +='    </div>';
		html +='    <hr/>';
		html +='    <div class="edit-adr">';
		html +='        <div>';
		html +='            <input type="radio" name="radioaddr" id="radioaddr'+data.id+'" class="ui-checkbox js_build_radioaddr" value="'+data.id+'" '+(data.defChecked||"")+' />';
		html +='            <label for="radioaddr'+data.id+'">默认地址</label>';
		html +='        </div>';
		html +='        <div class="ed-icon">';
		html +='            <span class="js_build_editaddr"><i class="icon icon-e-edit"></i>编辑</span>';
		html +='            <span class="js_build_deladdr"><i class="icon icon-e-del"></i>删除</span>';
		html +='        </div>';
		html +='    </div>';
		html +='</div>';
		return html;
	}
	
	//切换运费
	var addrchange = function() {
		//切换地址运费
		$.eAjax({
			url : GLOBAL.WEBROOT + '/order/build/getExpressFees',
			data : ebcForm.formParams($("#submitForm")),
			success : function(returnInfo){
				$.each(returnInfo,function(n,value){
					$("div.js_build_deliver").each(function(m,value2){
						
						if(m%3 == 0){
							var ul = $(this).closest("ul");
							var shop_id = ul.parent().parent().parent().prev().children(':first').val();
							if(n == shop_id){
								//当前运费
								var cFee = ul.siblings("input[name$='.realExpressFee']");
								//原始运费
								var oFee = ul.siblings("input.js_calc_realExpressFee");
								
								var cFeeNum = Number(value);
								
								//cFee.val(cFeeNum);
								if($(this).parent().parent().next().val() == "2" || $(this).parent().parent().next().val() == "0") {
									cFee.val(0);
								}else{
									cFee.val(cFeeNum);
								}
								//刷新资费
								buildObj.updateOrds(this);
							}
						}
					});
				});
			}
		});
	}
	//刷新页面更新缓存
	addrchange();
	/**
	 * 收货地址 end ============
	 */
	
	/**
	 * 支付方式 start ============
	 */
	
	/**
	 * 支付方式初始化
	 */
	$("div.js_payType").each(function(i,el){
		var el = $(el);
		if(el.hasClass("active")){
			$("#payType").val(el.attr("spcode"));
			return false;
		}
	});
	
	/**
	 * 支付方式选择
	 */
	$("div.js_payType").click(function(e){
		//赋值
		var self = $(this);
		var spcode = self.attr("spcode");
		if(spcode == "" || spcode == undefined){
			eDialog.alert("支付方式不存在！");
			return;
		}
		$("#payType").val(spcode);//赋值
		//切换样式
		self.closest("ul").find("div").each(function(i,el){
			$(el).is(self) ? $(el).addClass("active"):$(el).removeClass("active");
		});
	});
	
	/**
	 * 支付方式 end ============
	 */
	
	/**
	 * 优惠券 start ============
	 */
	//关闭使用优惠券
	$('div.js_canvas_coupon').click(function(e){
		//在关闭使用优惠券之前获取每个子订单的支付方式,按顺序存在deliveryVals数组中
		var deliveryDivs=$("#fixBottom").find("div[id='delivery-div']");
		var deliveryVals=new Array();
		for(var i=0;i<deliveryDivs.length;i++){
			var lis=$(deliveryDivs[i]).find("li");
			for(var j=0;j<lis.length;j++){
				var clazz=$(lis[j]).children().attr("class");
				if(clazz.indexOf("active")!=-1){
					var val=$(lis[j]).children().attr("deliver");
					deliveryVals.push(val);
					break;
				}
			}
		}
        $(this).closest("div#build_canvas_coupon").offCanvas('close');
        e.preventDefault();
        e.stopPropagation();
        //回显客户之前选中的配送方式
		for(var i=0;i<deliveryDivs.length;i++){
			var lis=$(deliveryDivs[i]).find("li");
			var realDeliver=deliveryVals[i];
			for(var j=0;j<lis.length;j++){
				var thisDeliver=$(lis[j]).children().attr("deliver");
				if(thisDeliver==realDeliver){
					$(lis[j]).children().trigger('click');
					break;
				}
			}
		}
		//刷新页面更新缓存
		addrchange();
    });
	//打开使用优惠券
	$('div.js_build_coupon').click(function(e){
		var shopId=$(this).attr("id");
		var coupValues = 0;
		var coupShop = $(this).find('.js_build_coupShopValue');
		$(':checkbox:checked', $(this).find('.js_build_coupItemBean')).each(function () {
			/*$(this).closest('.js_build_coupItem').find('.js_build_coupItemChecked').val("checked");
			$(this).closest('.js_build_coupItem').siblings().find('.js_build_coupItemChecked').val("");
			$('.js_build_coupItemBeanChecked', $(this).closest('js_build_coupItemBean')).val("checked");
			$('.js_build_coupItemBeanChecked', $(this).closest('js_build_coupItemBean')).siblings().val("");*/
			//设置单元优惠
			var coupValue = $(this).val();
			if (isNaN(coupValue)) coupValue = 0;
			//设置单元优惠 每种类型的优惠券
			var noExpress = $(this).nextAll('.js_build_noExpress').val();
			var discountCoup = $(this).nextAll('.js_build_discountCoup').val();
			if(noExpress != '1'){
				//coupValues = Number(coupValues) + Number(coupValue);
				if(discountCoup != '1'){
					coupValues = Number(coupValues) + Number(coupValue);
				}else{
					var orderMoney = $(this).closest('.js_build_coupItems').find('.js_calc_orderMoney').val();
					var skuIds = $(this).parent().find('._skuIds').val();
					skuIds = skuIds.split(",");
					var gdsValues = 0 ;
					for(var i=0;i<skuIds.length;i++){
						var skuId = skuIds[i];
						var amount = Number($("#"+skuId).attr("amount"))||0;
						var basePrice = Number($("#"+skuId).attr("basePrice"))/100||0;
						var disValue = ebcUtils.numFormat((basePrice * Number(coupValue))/10000, 2);
						$("#"+skuId).html("价格：&yen;"+disValue);
						gdsValues = gdsValues + (basePrice-disValue)*amount*100;
					}
					coupValues = Number(coupValues) + Number(gdsValues);
				}
			} 
			
		});
		coupShop.val(coupValues);

		//更新资费信息
		buildObj.updateOrds(this);

		//优惠券合计
		buildObj.totalCoupCount();

        $(this).children('div#build_canvas_coupon').offCanvas('open');
        $(window).off('resize.offcanvas.amui orientationchange.offcanvas.amui');
        e.stopPropagation();
    });
	
	//打开展示的优惠券按钮
	$('#showCoups').click(function(e){
		var coupValues = 0;
		var coupShop = $(this).find('.js_build_coupShopValue');
		$(':checkbox:checked', $(this).find('.js_build_coupItemBean')).each(function () {
			/*$(this).closest('.js_build_coupItem').find('.js_build_coupItemChecked').val("checked");
			$(this).closest('.js_build_coupItem').siblings().find('.js_build_coupItemChecked').val("");
			$('.js_build_coupItemBeanChecked', $(this).closest('js_build_coupItemBean')).val("checked");
			$('.js_build_coupItemBeanChecked', $(this).closest('js_build_coupItemBean')).siblings().val("");*/
			//设置单元优惠
			var coupValue = $(this).val();
			if (isNaN(coupValue)) coupValue = 0;
			//设置单元优惠 每种类型的优惠券
			var noExpress = $(this).nextAll('.js_build_noExpress').val();
			var discountCoup = $(this).nextAll('.js_build_discountCoup').val();
			if(noExpress != '1'){
				//coupValues = Number(coupValues) + Number(coupValue);
				if(discountCoup != '1'){
					coupValues = Number(coupValues) + Number(coupValue);
				}else{
					var orderMoney = $(this).closest('.js_build_coupItems').find('.js_calc_orderMoney').val();
					var skuIds = $(this).parent().find('._skuIds').val();
					skuIds = skuIds.split(",");
					var gdsValues = 0 ;
					for(var i=0;i<skuIds.length;i++){
						var skuId = skuIds[i];
						var amount = Number($("#"+skuId).attr("amount"))||0;
						var basePrice = Number($("#"+skuId).attr("basePrice"))/100||0;
						var disValue = ebcUtils.numFormat((basePrice * Number(coupValue))/10000, 2);
						$("#"+skuId).html("价格：&yen;"+disValue);
						gdsValues = gdsValues + (basePrice-disValue)*amount*100;
					}
					coupValues = Number(coupValues) + Number(gdsValues);
				}
			} 
			
		});
		coupShop.val(coupValues);

		//更新资费信息
		buildObj.updateOrds(this);

		//优惠券合计
		buildObj.totalCoupCount();

        $(this).children('div#build_canvas_coupon').offCanvas('open');
        $(window).off('resize.offcanvas.amui orientationchange.offcanvas.amui');
        e.stopPropagation();
    });
	
	//优惠券是否选择
	var coupItemSelect = function(obj){
		var box = $(obj).closest('.js_build_coupItemBean');
		var hasSelect = false;
		$(box).each(function(){
			if($(':checkbox',$(this)).prop('checked'))
				hasSelect = true;
		});
		return hasSelect;
	};

	var otherCoupItemSelect = function(obj){
		var box = $(obj).closest('.js_build_coupItemBean');
		var hasSelect = false;
		$(box).siblings().each(function(){
			$(':checkbox',$(this)).each(function(){
				var _this = this;
				if($(_this).prop('checked'))
					hasSelect = true;
			});
		});
		return hasSelect;
	};
	//选择优惠券
	$('.js_build_coupItemBean').each(function(){
		var _this = this;
		$(':checkbox',$(_this)).click(function(){
			var hasSelect = otherCoupItemSelect($(this));
			if(hasSelect){
				var dialog = this;
				$(dialog).prop('checked',!$(dialog).prop('checked'));
				eDialog.alert('不同优惠券不能共用',function(){

				},'information');
			}else{
				var checkSelect = $(this).prop('checked');

				//标记选择
				if(checkSelect){
					$(this).closest('.js_build_coupItem').find('.js_build_coupItemChecked').val("checked");
				}else{
					$(this).closest('.js_build_coupItem').find('.js_build_coupItemChecked').val("");
					var skuIds = $(this).parent().find('._skuIds').val();
					skuIds = skuIds.split(",");
					var gdsValues = 0 ;
					for(var i=0;i<skuIds.length;i++){
						var skuId = skuIds[i];
						var value = ebcUtils.numFormat(($("#"+skuId).attr("value"))/100, 2)||0;
						$("#"+skuId).html("价格：&yen;"+value);
					}

				}

				if(checkSelect){
					$('.js_build_coupItemBeanChecked',$(_this)).val("checked");

				}else{
					if(!coupItemSelect(_this))
						$('.js_build_coupItemBeanChecked',$(_this)).val("");
				}//标记选中

				//设置单元优惠
				var coupValue = $(this).val();

				if(isNaN(coupValue)) coupValue = 0;

				var coupShop = $(this).closest('.js_build_coupItems').find('.js_build_coupShopValue');
				var realExpressFee = $(this).closest(buildObj.shopClass).find('.js_calc_realExpressFee');
				var realExpressFees = $(this).closest(buildObj.shopClass).find('.js_calc_realExpressFees');
				//设置单元优惠 每种类型的优惠券
				var coupValues = 0;
				realExpressFees.val(Number(realExpressFee.val()));
				$(':checkbox:checked',$(_this)).each(function(){
					var coupValue = $(this).val();
					var noExpress = $(this).nextAll('.js_build_noExpress').val();
					var discountCoup = $(this).nextAll('.js_build_discountCoup').val();
					if(noExpress != '1'){
						if(discountCoup != '1'){
							coupValues = Number(coupValues) + Number(coupValue);
						}else{
							var orderMoney = $(this).closest('.js_build_coupItems').find('.js_calc_orderMoney').val();
							var skuIds = $(this).parent().find('._skuIds').val();
							skuIds = skuIds.split(",");
							var gdsValues = 0 ;
							for(var i=0;i<skuIds.length;i++){
								var skuId = skuIds[i];
								var amount = Number($("#"+skuId).attr("amount"))||0;
								var value = Number($("#"+skuId).attr("value"))/100||0;
								var disValue = ebcUtils.numFormat((value * Number(coupValue))/10000, 2);
								$("#"+skuId).html("价格：&yen;"+disValue);
								gdsValues = gdsValues + (value-disValue)*amount*100;
							}
							coupValues = Number(coupValues) + Number(gdsValues);
						}
					}else{
						realExpressFees.val(0);
					}
				});

				coupShop.val(0);
				coupShop.val(coupValues);
			}

			//更新资费信息(这里有点问题)
			buildObj.updateOrds(this);
			
			//优惠券合计
			buildObj.totalCoupCount();
			
			//将优惠金额展示在新加的优惠券栏旁边
			var build_coup_fees=$("#build_coup_fees").text();
			$("#yangSpan").text("");
			$("#coupMoneySpan").text(build_coup_fees);
		});
	});
	/**
	 * 优惠券 end ============
	 */
	
	/**
	 * 资金账户 start ============
	 */
	/**
	 * 使用金额输入控制
	 */
	$(".js_build_acctinput").bind("input propertychange", function(e){
		//只能输入数字
		var self = $(this);
		var inputnum = self.val();
		self.val(buildObj.checkNum(inputnum,false));
	});
	$(".js_build_acctinput").on("blur", function(e){
		var self = $(this);
		var max = self.closest("div#build_acctunit").find("input.js_calc_deductOrderMoney").val();
		var num = self.val();
		if(Number(num)>Number(max)/100){
			self.val(ebcUtils.numFormat((Number(max))/100, 2));
		}else{
			self.val(ebcUtils.numFormat(num,2));
		}
	});
	
	/**
	 * 资金账户 end ============
	 */
	
	/**
	 * 发票信息 start ============
	 */
	
	/**
	 * 是否开票
	 */
	$(".js_build_invoice").click(function(e){
		var self = $(this);
		//切换样式
		self.closest("ul").find("div").each(function(i,el){
			$(el).is(self) ? $(el).addClass("active"):$(el).removeClass("active");
		});
	});
	$(".js_build_invoice").click(function(e){
		var self = $(this);
		//hidden scope
		var scope = self.closest("ul").siblings("div#build_rOrdInvoice");
		//不开票  
		if(self.attr("invoice") == "0"){
			//还原
			$("#mainBillType", scope).val("2");
			$("#billType", scope).val("1");
			$("#billTitle", scope).val("");
			$("#taxpayerNo", scope).val("");
			$("#billContent", scope).val("");
			$("#detailFlag", scope).val("0");
		}
		//开票  
		if(self.attr("invoice") == "1"){
			//当前确定开票对象
			buildObj.currentInvoice = scope; 
		    //初始化sub
		    //是否首次编辑
		    var inv = {};
		    if(scope.find("#mainBillType").val() != "2"){//重新编辑,2为不开票
		    	inv.invoiceType = $("#billType", scope).val();  //类型 当前仅“普通”
		    	inv.invoiceTitle = $("#billTitle", scope).val();  //抬头
		    	inv.invoiceTaxpayerNo = $("#taxpayerNo", scope).val();  //纳税人识别码
		    	inv.invoiceContent = $("#billContent", scope).val();  //内容
		    	inv.detailFlag = $("#detailFlag", scope).val();   //明细 
		    }
		    buildObj.initBuildInvoiceBox(inv);  //执行初始化动作
		    //打开发票sub页
		    $('#build_canvas_invoice').offCanvas('open');
		    $(window).off('resize.offcanvas.amui orientationchange.offcanvas.amui');
		}
	});
	//关闭发票sub页
	$("#canvas_invoice").click(function(e){
		//如果未保存发票信息
		//复位
		var dest = buildObj.currentInvoice;//目标店铺发票
		if(dest.find("#mainBillType").attr("value")=="2"){//不开票
			
			dest.siblings("ul").find("div[invoice='0']").trigger("click");
		}
		closeInvoiceEdit(e);//关闭
	});
	//关闭 发票信息修改
	var closeInvoiceEdit = function(e){
		if(e){
			e.preventDefault();
			e.stopPropagation();
		}
		$('#build_canvas_invoice').offCanvas('close');
	}
	//打开设置发票信息
	
	/* ------------ 发票信息修改 ------------- */
	/**
	 * 选择附加明细
	 */
	$("#build_inv_detail .js_build_inv_detail").click(function(e){
		var self = $(this);
		self.addClass("active");
		self.siblings().removeClass("active");
	});
	
	/**
	 * 选择发票内容
	 */
	$("#build_inv_content .js_build_inv_content").click(function(e){
		var self = $(this);
		self.addClass("active");
		self.siblings().removeClass("active");
	});
	/**
	 * 选择公司
	 */
	$("#company").click(function(e){
		var $this = $(this);
		$("#person").removeClass('active');
		$this.addClass('active');
		$("#taxpayer").css('display','block');
		$("#billType").val("1");
		$("#build_inv_title").val("");
		$("#build_inv_title").attr({"disabled":false});
	});
	/**
	 * 选择个人
	 */
	$("#person").click(function(e){
		var $this = $(this);
		$("#company").removeClass('active');
		$this.addClass('active');
		$("#taxpayer").css('display','none');
		$("#billType").val("0");
		$("#build_inv_title").val("个人");
		$("#build_inv_title").attr({"disabled":true});
	});
	
	/**
	 * 保存发票信息
	 */
	$("#build_invoice_save").click(function(e){
		var scope = $("#build_invoice_box");
		var dest = buildObj.currentInvoice;//目标店铺发票
		//验证
		var title = $.trim($("#build_inv_title", scope).val());
		if(title==""){
			eDialog.alert('发票抬头不能为空');
			return;
		}
		//var bType = $("#billType").val();
		if($("#company").hasClass("active")){
			var taxpayerNo = $.trim($("#taxpayer_no", scope).val());
			if(taxpayerNo==""){
				eDialog.alert('纳税人识别号不能为空');
				return;
			}
			var reg = /^[0-9a-zA-Z]*$/g;
			if (!reg.test(taxpayerNo)){
				eDialog.alert('请输入正确的纳税人识别码');
				return;
			}
		}
		//数据验证过完  回填
		var billContent = "";
		var detailFlag = "";
		scope.find("ul#build_inv_detail").children("li").each(function(i, el){
			if($(el).hasClass("active")){
				detailFlag = $(el).attr("value");
				return false;
			}
		});
		scope.find("ul#build_inv_content").children("li").each(function(i, el){
			if($(el).hasClass("active")){
				billContent = $(el).attr("invoice");
				return false;
			}
		});
		$("#billType", dest).val("0");//类型  0:个人，1:公司  2 电子
		$("#mainBillType", dest).val("0");//类型   0:普票 1 为增票  2 为不开发票
		$("#billTitle", dest).val(title);//抬头
		$("#taxpayerNo", dest).val(taxpayerNo);//纳税人识别号
		$("#billContent", dest).val(billContent);//内容
		$("#detailFlag", dest).val(detailFlag);//明细
		//var no = $("#taxpayerNo").val();
		closeInvoiceEdit(e);//关闭
	});
	
	
	
	/**
	 * 发票信息 end ============
	 */
	
	/**
	 * 配送方式 start ============
	 */
	
	/**
	 * 配送方式初始化
	 */
	$("div.js_build_deliver").each(function(i,el){
		var el = $(el);
		if(el.hasClass("active")){
			el.closest("ul").siblings("input[name$='.deliverType']").val(el.attr("deliver"));
			return false;
		}
	});
	
	/**
	 * 选择配送方式
	 */
	$("div.js_build_deliver").click(function(e){
		//赋值
		var self = $(this);
		var deliver = self.attr("deliver");
		self.closest("ul").siblings("input[name$='.deliverType']").val(deliver);//赋值
		//切换样式
		self.closest("ul").find("div").each(function(i,el){
			$(el).is(self) ? $(el).addClass("active"):$(el).removeClass("active");
		});
	});
	
	/**
	 * 配送方式 end ============
	 */
	
	/**
	 * 提交
	 */
	$("#submitOrd").click(function(){
		if($("#addrId").val()==""){
			eDialog.alert('请选择收货地址');
			return;
		}
		$.AMUI.progress.start();//打开进度条
		if(!$(obj).hasClass('mbtn-grad')){
			$.eAjax({
				url:GLOBAL.WEBROOT + '/order/build/submitOrd',
				data:ebcForm.formParams($("#submitForm")),
				success:function(returnInfo){
					$.AMUI.progress.done();//关闭进度条，请求完成
					if(returnInfo&&returnInfo.resultFlag == 'ok'){
						var payType = $("#payType").val();
						var payOrdFormAction = GLOBAL.WEBROOT + '/pay/way';
					if('0' == payType){
							if($(buildObj.shopClass).length > 1){
								window.location.replace(GLOBAL.WEBROOT + '/pay/orderList/'+returnInfo.onlineKey);
							}else{
								window.location.replace(GLOBAL.WEBROOT + '/pay/way?onlineKey='+returnInfo.onlineKey);
							}
						}else{
							window.location.replace(GLOBAL.WEBROOT + '/pay/payWay/'+returnInfo.payTypeKey);
						}
					}else{
						//去掉样式 按钮可点击
						$(obj).removeClass('mbtn-grad');
						eDialog.alert(returnInfo.resultMsg);
					}
				}
			});
		}

		//禁止多点
		//按钮置灰事件失效
		$(obj).addClass('mbtn-grad');
	});
	
	/** ============ 金额修改事件及相关置于底部   ============ **/
	
	/**
	 * 配送方式：运费
	 */
	$("div.js_build_deliver").click(function(e){
		var ul = $(this).closest("ul");
		//当前运费
		var cFee = ul.siblings("input[name$='.realExpressFee']");
		//原始运费
		var oFee = ul.siblings("input.js_calc_realExpressFee");
		
		var cFeeNum = Number(cFee.val());
		var oFeeNum = Number(oFee.val());
		
		//自提，邮政 消减邮费
		//设置运费
		if($(this).attr("deliver") != "1") {
			cFee.val(0);
		}else{
			cFee.val(oFeeNum);
		}
		//刷新资费
		//buildObj.updateOrds(this);
		  addrchange();
	});
	
	/**
	 * 资金账户 使用
	 */
	$(".js_build_acctuse").click(function(e){
		var self = $(this);
		var pdiv = self.closest("div");//父div
		var pdivb = pdiv.siblings("div"); 
		//得到输入金额
		var acctamt = self.siblings("input").val();
		//设置金额
		pdivb.find("span").html(acctamt);
		acctamt = ebcUtils.numFormat(accMul(acctamt,100),0);
		self.closest("div#build_acctunit").find("input.js_calc_acct_deduct").val(acctamt);
		//转换样式
		pdiv.toggle();
		pdivb.toggle();
		//更新资费信息
		buildObj.updateOrds(this);
	});
	
	/**
	 * 资金账户 取消使用
	 */
	$(".js_build_acctundo").click(function(e){
		var self = $(this);
		var pdiv = self.closest("div");//父div
		var pdivb = pdiv.siblings("div"); 
		//设置金额
		self.closest("div#build_acctunit").find("input.js_calc_acct_deduct").val(0);
		//转换样式
		pdiv.toggle();
		pdivb.toggle();
		//更新资费信息
		buildObj.updateOrds(this);
	});
	/**
	 * 展示的优惠券使用点击事件
	 * @author mwz
	 */
	$(".js_build_coupCheck_la_show").click(function(e){
		//查看checkbox的checked值是否为"checked"
		var _this=$(this);
		var checked=_this.prev().is(':checked');
		var id=_this.attr("for");
		var shopId=_this.attr("shopId");
		var flag=true;
		//判断是否可以使用优惠券
		var box = _this.closest('.js_build_coupItemBean');
		var temp = $(box).siblings().has($(':checkbox:checked')).size()>0;
		if(checked){//取消使用优惠券
			_this.prev().prop("checked",false);
			//找到隐藏域中对应的label
		}else{
			//可以使用优惠券
			if(!temp){
				_this.prev().prop("checked",true);
			}
		}
		//找到隐藏域中对应的label
		$("#"+shopId).find("#"+id).find('label.js_build_coupCheck_la').trigger('click');
		//刷新页面更新缓存
		addrchange();
	});
	/**
	 * 关闭隐藏域中的优惠券页面
	 * @author mwz
	 */
	$('div.js_canvas_coupon_show').click(function(e){
		//获取每个子订单的支付方式,按顺序存在deliveryVals数组中
		var deliveryDivs=$("#fixBottom").find("div[id='delivery-div']");
		var deliveryVals=new Array();
		for(var i=0;i<deliveryDivs.length;i++){
			var lis=$(deliveryDivs[i]).find("li");
			for(var j=0;j<lis.length;j++){
				var clazz=$(lis[j]).children().attr("class");
				if(clazz.indexOf("active")!=-1){
					var val=$(lis[j]).children().attr("deliver");
					deliveryVals.push(val);
					break;
				}
			}
		}
		$("#submitCoups").each(function(e){
			$('div.js_canvas_coupon').trigger('click');
		});
		//配送方式
		for(var i=0;i<deliveryDivs.length;i++){
			var lis=$(deliveryDivs[i]).find("li");
			var realDeliver=deliveryVals[i];
			for(var j=0;j<lis.length;j++){
				var thisDeliver=$(lis[j]).children().attr("deliver");
				if(thisDeliver==realDeliver){
					$(lis[j]).children().trigger('click');
					break;
				}
			}
		}
		//刷新页面更新缓存
		addrchange();
    });
});