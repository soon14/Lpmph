//页面初始化模块
$(function(){	
	//店铺list
	/**
	 * {shopId : shopInfo}
	 */
	var shopListData = {};
	//当前显示的店铺id
	var currentShopId = "";
    //当前显示的店铺全称
	var currentShopName = "";
	//当前店铺的申请提现金额
	var applicationMoney=0;
	//店铺名片鼠标点击事件
	$(".seller-list").delegate("li", "click", function () {
        $(this).addClass("shop-box").siblings().removeClass("shop-box");
        var shopId = $(this).find("input").eq(0).val();
        showShop(shopId);
        //店铺全称
        var shopFullName=$(this).find("input").eq(1).val();
        //修改店铺详情中的店铺全称
        $(".shop-name-txt").text(shopFullName);
    });
	
    $(".seller-list").delegate("li", "mouseover", function () {
        $(this).addClass("shop-box").siblings().removeClass("shop-box");
    });
    $(".seller-list").delegate("li", "mouseleave", function () {
        $(".seller-list li").each(function(i){
        	var self = $(this);
        	if(self.find("input").val()==currentShopId){
        		self.addClass("shop-box").siblings().removeClass("shop-box");
        		return false;
        	}
        });
    });
    
    
    //店铺名片分页鼠标点击事件
    $("#shopSlideList").delegate("li", "click", function () {
    	var self = $(this);
    	if(self.find("a").attr("class")=="active"){//点击当前页不刷新
    		return;
    	}
    	self.find("a").addClass("active")
    	self.siblings().find("a").removeClass("active");
        var list = [];
        var pageno = self.find("a").attr("pageno");
        var pagecount = self.find("a").attr("pagecount");
        var pagesize = 4;
        var start = pageno*pagesize;
        var end = start+pagecount;
        var j=0;
        for(var i in shopListData){
        	if(j>=start && j<end){
        		list.push(shopListData[i]);
        	}
        	j++;
        }
        _removeCardAll();//清空
        _appendCardRequire(list);//刷新名片
    });
    
    
	
	//初始化函数
	var init = function(){
		//1.我的店铺
        
		//查询
		$.eAjax({
  			url : GLOBAL.WEBROOT + "/seller/shopdashboard/myshops",
  			async : true,
  			data : {},
			type : "post",
  			dataType:'json',
  			success : function(data) {
  				//TODO data.length==0
  				var len = data.length;
  				
  				//之前是只有一个店铺，隐藏“我的店铺”
  				/*if(len>1){
  					$("#multShop").show();
  				}
  				*/
  				//只要有店铺就展示我的店铺
  				if(len>1){
  					$("#multShop").show();
  				}
  				//排序id从小到大 -- 保持与shopListData一致
  				data.sort(function(a,b){ return a.id - b.id});
  				if(data){//更新shopListData
  					for(var i in data){
  						shopListData[""+data[i].id+""] = data[i];
  					}
  				}
  				
  				//清空
  				_removeCardAll();
  				_removeSlideAll();
  				//1.处理名片
  				_appendCardRequire(data);
  				//2.处理分页
  				_appendSlideAll(data);
  				
  			}
  		});
		
	};
	
	/**
	 * 初始化
	 */
	init();
	
	/**
	 * 清空名片组
	 */
	function _removeCardAll(){
		$("#shopList").html("");
	}
	
	/**
	 * 刷新名片
	 */
	function _appendCardRequire(data){
		var size = 4;//一页展示数量
		var ul = $("#shopList");
		for(var i=0; i<size; i++){
			if(data[i]){
				ul.append(_createCardLi(data[i], i==0));
			}
		}
		//初始化店铺 首个
		showShop(data[0].id);
		//初始化店铺 首个  全称
		$(".shop-name-txt").text(data[0].shopFullName);
	}
	
	/**
	 * 清空分页滑块
	 */
	function _removeSlideAll(){
		$("#shopSlideList").html("");
	}
	
	/**
	 * 刷新分页
	 */
	function _appendSlideAll(data){
		var count = data.length;
		var size = 4;//分页容器容量上限
		var ul = $("#shopSlideList");
		var pageCount = (count%size==0)?parseInt(count/size):parseInt(count/size+1);
		for(var i=0; i<pageCount; i++){
			var pageinfo = {};
			pageinfo.pageno = i; //当前页
			pageinfo.pagecount = size;  //当前页容量
			if(i==(pageCount-1)) pageinfo.pagecount = count-i*size;//当前页为最后一页时的容量
			
			ul.append(_createSlideLi(pageinfo, i==0));
		}
		
	}
	
	/**
	 * 创建一个名片li
	 */
	function _createCardLi(shop, isShow){
		
		var card = '';
		if(isShow){//完整名片
			card += '<li class="shop-box">';
		}else{//图片
			card += '<li class="">';	
		}
		card += '<input type="hidden" value="'+shop.id+'">';
		card += '<input type="hidden" value="'+shop.shopFullName+'">';
		card += '<img style="width:160px;height:160px" src="' + shop.logoPathURL + '" alt=""/>';
		card += '<div class="imgmargin"><h2 title="'+shop.shopFullName+'">' + shop.shopFullName + '</h2>';
		card += '<p style="text-overflow:ellipsis;overflow:hidden;white-space:nowrap;width:160px;" title="'+shop.linkPerson+'" >联系人：' + shop.linkPerson + '</p>';
		card += '<p style="text-overflow:ellipsis;overflow:hidden;white-space:nowrap;width:160px;">联系电话：' + shop.linkMobilephone + '</p>';
		var cautionMoney = shop.cautionMoneys||"0";
		card += '<p>入店保障资金：' + cautionMoney + '</p>';
		card += '</div>';
		card += '</li>';

		return card;
	}
	
	/**
	 * 创建一个分页滑块li
	 */
	function _createSlideLi(pageinfo, isActive){
		
		
		var slide = '';
		var active = !!isActive?'active':'';
		slide += '<li>';
		slide += '<a class="'+active+'" href="javascript:" pageno='+pageinfo.pageno+' pagecount='+pageinfo.pagecount+'></a>';
		slide += '</li>';

		return slide;
	}
	var showShop = function(shopId){
		if(currentShopId==shopId){//当前店铺不刷新
			return;
		}
		currentShopId = shopId;//当前店铺
		//1.1、店铺账户详情框:店铺账号可用余额
		$.eAjax({
  			url : GLOBAL.WEBROOT + "/seller/shopmgr/withdraw/shopAcctInfo",
  			async : true,
  			data : {shopId : shopId},
			type : "post",
  			dataType:'json',
  			success : function(data) {
  				//在店铺详情框展示店铺账户可用余额
  				var acctBalance=data.acctBalance;
  				$('#shop-acct-acctBalance').html("&yen;"+ebcUtils.numFormat(acctBalance/100, 2));
  			}
  		});
		//2.1、根据店铺id和年份查询该店铺的整年每个月的可提现金额
		showShopAcctBillMonth(shopId)
	}
	var showShopAcctBillMonth=function(shopId){
		//清空申请提现金额
		applicationMoney=0;
		//清空申请提现隐藏域金额
		$("#shop-acct-app-input").val(0);
		//清空提现申请备注
		$("#remark").val("");
		var currentYear=$("#current-year").text();
		$.eAjax({
  			url : GLOBAL.WEBROOT + "/seller/shopmgr/withdraw/shopAcctBillMonth",
  			async : true,
  			data : {shopId : shopId,currentYear : currentYear},
			type : "post",
  			dataType:'json',
  			success : function(data) {
  				for(var i=0;i<data.length;i++){
  					var _this=$("#month"+i);
  					if(data[i]!=null){
  						//清空样式
  						_this.parent("td").removeClass("online-wdr");
  						_this.parent("td").removeClass("over-wdr");
  						_this.parent("td").removeClass("doing-wdr");
  						//清空点击事件
  						_this.parent("td").unbind();
  						_this.next().text("");
  						_this.next().next().text("");
  						_this.next().next().next().removeClass("wdr-money-state-ok");
  						//清空隐藏域的值
  						_this.siblings("input").val(0);
  						//判断状态   0：未对账 1：未提现  2：已提现  3：提线申请流程中
  						//已提现
  						if(data[i].status=="2"){
  							//给对应的月份增加类样式
  							_this.parent("td").addClass("over-wdr");
  							//显示已提现金额
  							var withdrawMoney=data[i].withdrawMoney;
  							_this.next().html("已提现金额：&yen;"+ebcUtils.numFormat(withdrawMoney/100, 2))
  							//状态
  							_this.next().next().html("已提现");
  						}else if(data[i].status=="1"){//未提现（1、可提现；2、未到提现时间不可提现）
  							//该月结订单的可提现时间
  							var canWithdrawTime=data[i].canWithdrawTime;
  							//系统当前时间【后台传的】
  							var systemTime=data[i].systemTime;
  							//如果系统时间大于可提现时间，则该笔订单可以提现
  							if(systemTime>=canWithdrawTime){
  								//给对应的月份增加类样式
  								_this.parent("td").addClass("online-wdr");
  								//可提现金额(有可能是负数)
  								var money=data[i].money;
  								_this.next().html("可提现金额：&yen;"+ebcUtils.numFormat(money/100, 2))
  								//将可提现金额存到对应的隐藏域中
  								_this.siblings("input").val(money);
  								//状态
  	  							_this.next().next().html("可提现");
  								//默认选中
  								_this.next().next().next().addClass("wdr-money-state-ok");
  								//累计该用户可提现金额
  								applicationMoney+=money;
  							}else{
  								//未到提现时间
  								//可提现金额
  								var money=data[i].money;
  								_this.next().html("可提现金额：&yen;"+ebcUtils.numFormat(money/100, 2))
  								//状态
  	  							_this.next().next().html("未到提现日期");
  							}
  						}else if(data[i].status=="0"){
  							//未对账
  							//状态
  							_this.next().next().html("未对账");
  						}else if(data[i].status=="3"){
  							//提现申请流程中
  							//给对应的月份增加类样式
							_this.parent("td").addClass("doing-wdr");
  							//提现金额
							var money=data[i].money;
							_this.next().html("提现金额：&yen;"+ebcUtils.numFormat(money/100, 2))
							//状态
  							_this.next().next().html("提现申请流程中");
  						}
  					}else{
  						//移除所有效果
  						_this.parent("td").removeClass("online-wdr");
  						_this.parent("td").removeClass("over-wdr");
  						_this.parent("td").removeClass("doing-wdr");
  						//清空点击事件
  						_this.parent("td").unbind();
  						_this.next().text("");
  						_this.next().next().text("");
  						_this.next().next().next().removeClass("wdr-money-state-ok");
  						//清空隐藏域的值
  						_this.siblings("input").val(0);
  					}
  				}
  				//店铺账户可提现金额累计
  				$("#shop-acct-application").html("&yen;"+ebcUtils.numFormat(applicationMoney/100, 2));
  				//该店铺可提现金额隐藏域
  				$("#shop-acct-app-input").val(applicationMoney);
  				//给所有可提现的<td>绑定点击事件
  				$("td").each(function(){
  					var clazz_td=$(this).attr("class");
  					if(clazz_td=="online-wdr"){
  						$(this).bind("click",function(){
  							var clazz=$(this).attr("class");
  							//如果未选中点击后可选中
  							if(clazz==null||clazz==""){
  								$(this).addClass("online-wdr");
  								$(this).children("span").addClass("wdr-money-state-ok");
  								//选中后修改该账户申请提现金额
  								//该月可提现金额(不考虑负数问题)
  								var money=$(this).children("input").val();
  								//申请提现金额
  								var appMoney=$("#shop-acct-app-input").val();
  								appMoney=appMoney*1+money*1;
								//修改申请提现金额
								$("#shop-acct-application").html("&yen;"+ebcUtils.numFormat(appMoney/100, 2));
								//同时修改隐藏域的值
								$("#shop-acct-app-input").val(appMoney);
								//修改全局变量applicationMoney的值
								applicationMoney=appMoney
							}else{
								$(this).removeClass("online-wdr");
								$(this).children("span").removeClass("wdr-money-state-ok");
								//取消选中后修改该账户申请提现金额
								//该月可提现金额(有可能是负数)
								var money=$(this).children("input").val();
								//申请提现金额
  								var appMoney=$("#shop-acct-app-input").val();
  								appMoney=appMoney*1-money*1;
								//修改申请提现金额
								$("#shop-acct-application").html("&yen;"+ebcUtils.numFormat(appMoney/100, 2));
								//同时修改隐藏域的值
								$("#shop-acct-app-input").val(appMoney);
								//修改全局变量applicationMoney的值
								applicationMoney=appMoney
							}
  						});
  					}
  				});
  			}
  		});
	}
	$("#btnApply").live("click",function(){
		//先通过js判断申请提现金额是否大于0，如果小于或等于0不允许提现,后面还要通过后台判断.
		if(applicationMoney<=0){
			eDialog.info('申请提现金额必须大于0才可以申请提现！');
			return;
		}
		//判断是否符合提现要求
		//需要的参数
		//shopId
		var shopId=currentShopId;
		//所有选中的月份集合
		//申请提现年份
		var year=$("#current-year").text();
		//月份集合：将所有选中的月份放在一个字符串集合中
		var array="";
		$("td").each(function(){
			var clazz_td=$(this).attr("class");
			if(clazz_td=="online-wdr"){
				array+=year+$(this).children("p").first().attr("month")+",";
			}
		});
		array=array.substring(0,array.length-1);
		if(array==""){
			eDialog.info('申请提现金额必须大于0才可以申请提现！');
			return;
		}
		$.eAjax({
  			url : GLOBAL.WEBROOT + "/seller/shopmgr/withdraw/check",
  			async : true,
  			data : {"shopId":shopId,"billMonths":array,"year":year},
			type : "post",
  			dataType:'json',
  			success : function(result) {
  				var resultFlag=result.resultFlag;
  				var resultMsg=result.resultMsg;
  				if(resultFlag=='ok'){
  					//将店铺ID、结算月集合、传到后台
  					window.location.href = GLOBAL.WEBROOT+'/seller/shopmgr/withdraw/showWithdrawDetail/'+shopId+'/'+array;
  				}else{
  					eDialog.info(resultMsg);
  				}
  			}
		});
	});	
	$("#prev-year").live("click",function(){
		var curYear=parseInt($(".wdr-year-current").html());
		//如果curYear<2017的话
		if(curYear<=2017){
			eDialog.info('查无数据!');
		}else{
			curYear--;
			$(".wdr-year-current").html(curYear);
			showShopAcctBillMonth(currentShopId);
		}
	});
	$("#next-year").live("click",function(){
		var curYear=parseInt($(".wdr-year-current").html());
		if(curYear<new Date().getFullYear()){
			curYear++;
			$(".wdr-year-current").html(curYear);
			showShopAcctBillMonth(currentShopId);
		}
	});
});