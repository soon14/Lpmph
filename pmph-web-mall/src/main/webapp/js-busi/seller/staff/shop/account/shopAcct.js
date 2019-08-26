//页面初始化模块
$(function(){
	//店铺list
	/**
	 * {shopId : shopInfo}
	 */
	var shopListData = {};
	//当前显示的店铺id
	var currentShopId = "";
	var myDate = new Date();  
	var dateMonth = myDate.getMonth()+1;
	//名片鼠标事件
	$(".seller-list").delegate("li", "click", function () {
        $(this).addClass("shop-box").siblings().removeClass("shop-box");
        var shopId = $(this).find("input").eq(0).val();
        showShop(shopId);
        /*//店铺全称
        var shopFullName = $(this).find("input").eq(1).val();
        $("#shopName").text(shopFullName);*/
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
    
    
    //分页鼠标事件
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
  				
  				//只有一个店铺，隐藏“我的店铺”
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
	//强制保留两位小数
    function changeTwoDecimal(f_x) {
        var s_x = f_x.toString();
    var pos_decimal = s_x.indexOf('.');
    if (pos_decimal < 0)
    {
    pos_decimal = s_x.length;
    s_x += '.';
    }
    while (s_x.length <= pos_decimal + 2)
    {
    s_x += '0';
    }
    return s_x;
    }
	/**
	 * 店铺展现
	 */
	var showShop = function(shopId){
		if(currentShopId==shopId){//当前店铺不刷新
			return;
		}
		//店铺装饰不采用事件来打开新页面了。直接用a标签的href属性。防止打开新页面，shopId传的不对  原:third/sys/shopfishing
		$("#shopDecoration").attr('href', $("#shopDecoration").attr('url')+"/union/login/shopfishing?shopId="+shopId)
		currentShopId = shopId;//当前店铺
		$("#shopId").val(shopId);
		//我的资产
		$.eAjax({
  			url : GLOBAL.WEBROOT + "/seller/shopAccount/gridlist",
  			async : true,
  			data : {shopId : shopId},
			type : "post",
  			dataType:'json',
  			success : function(data) {
  				var c = parseInt(data.acctBalance) * 100 /parseInt(data.acctTotal);
  				var per = parseInt(c);
  				$("#total").text(data.acctTotals);
  				$("#balance").text(data.acctBalances);
  				$("#balance2").text(data.acctBalances);
  				$("#frozen").text(data.acctFrozens);
  				if(data.acctTotal!=0){
  					$("#per").text(per+"%");
  				}else{
  					$("#per").text(0+"%");
  				}
  			}
  		});
		//收支明细
		$.eAjax({
  			url : GLOBAL.WEBROOT + "/seller/shopAccount/balanceList",
  			async : true,
  			data : {shopId : shopId},
			type : "post",
  			dataType:'json',
  			success : function(data) {
  				var s;
  				var s1;
  				var date;
  				var s5="";
  				var s6;
  				var sub_string1;
  				var sub_string2;
  				var sub_string3;
  				$.each(data, function(key,value){
  					s = value.billDay;
    				m =	value.inMoneys;
    				b = value.backMoneys;
  					s1 = JSON.stringify(s);
  					sub_string1 = s1.substring(0,4);
  					sub_string2 = s1.substring(4,6);
  					sub_string3 = s1.substring(6,8);
  					date = sub_string1+"-"+sub_string2+"-"+sub_string3;
  					if(key==(data.length-1)){
  						s5+="{"+"date:'"+date+"',"+"data:'"+"收：￥"+m+","+"支：￥"+b+"'}";
  					}else{
  						s5+="{"+"date:'"+date+"',"+"data:'"+"收：￥"+m+","+"支：￥"+b+"'},";
  					}
  					});
	  				s6 = "["+s5+"]";
	  				var s7 = eval('(' + s6 + ')');
	  				$('.calendar-box').html("");
	  				$('.calendar-box').calendar({
	  			        ele : '.demo-box', //依附
	  			        title : '收支明细',
	  			        // beginDate : '2018-5-01',
	  			        // endDate : '2018-12-04',
	  			        //清拼接此格式
	  			        //data : s4,
	  			        data :s7
	  			    });
  			}
  		});
		//收支明细结算月账单
		$.eAjax({
  			url : GLOBAL.WEBROOT + "/seller/shopAccount/billMonthList",
  			async : true,
  			data : {shopId : $("#shopId").val(),year:myDate.getFullYear(),month:dateMonth},
			type : "post",
  			dataType:'json',
  			success : function(data) {
  				$("#money").text('￥'+data.money);
  				$("#lastMonth").text('￥'+data.lastMonth);
  			}
  		});
		//申请提现
		$.eAjax({
  			url : GLOBAL.WEBROOT + "/seller/shopAccount/withdrawApply",
  			async : true,
  			data : {shopId : $("#shopId").val()},
			type : "post",
  			dataType:'json',
  			success : function(data) {
  				var applyId = data[0].id;
  				var status = data[0].status;
  				var acctId = data[0].acctId;
  				if(applyId!=0){
  					$("#button1").attr("class","wdr-btn-no");
  					$("#button2").css("display","inline-block");
  					$("#button2").click(function(){
  						var url = GLOBAL.WEBROOT+"/seller/shopmgr/shopAcctDetail/withdrawApplyDetail/"+ applyId;
  			        	window.open(url);
  					});
  					if(status!=0){
  					$("#button3").css("display","inline-block");
  					$("#button3").click(function(){
  						eDialog.confirm("您确认要撤销吗？", {
  							buttons : [{
  								caption : '确认',
  								callback : function(){
  									$.eAjax({
  										url : GLOBAL.WEBROOT + "/seller/shopAccount/updateWithdrawApply",
  										data : {"shopId" : $("#shopId").val(),
  												"id":applyId,
  												"companyId":acctId
  												},
  										async : true,
  										type : "post",
  										datatype:'json',
  										success : function(returnInfo) {
  											var status = JSON.stringify(returnInfo.resultFlag);
  											if(status=='"fail"'){
  												eDialog.success('申请单状态已改变，无法撤销！',{
  	  												buttons:[{
  	  													caption:"确定",
  	  													callback:function(data){
  	  														location.reload();
  	  											        }
  	  												}]
  	  											});
  											}else{
  												eDialog.success('已撤销！',{
  													buttons:[{
  														caption:"确定",
  														callback:function(data){
  															location.reload();
  														}
  													}]
  												}); 
  											}
  										}
  									});
  								}
  							},{
  								caption : '取消',
  								callback : $.noop
  							}]
  						});
  					});
  					}
  				}else{
  					$("#button1").attr("class","wdr-btn-ok");
  					$("#button1").click(function(){
  						window.location.href = GLOBAL.WEBROOT+"/seller/shopmgr/withdraw";
  					});
  					$("#button2").css("display","none");
  					$("#button3").css("display","none");
  				}
  			}
  		});
	}
});
