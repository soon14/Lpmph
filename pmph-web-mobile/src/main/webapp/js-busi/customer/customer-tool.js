Strophe.addNamespace('OFFLINEMSG_IQ', "com:ai:of:offlinemsg");
/* TODO ========= ChatBox start ========== */
/**
 * 会话对象
 * 
 * **下划线开头为私有属性，不对外暴露
 * 
 * @param args
 */
function ChatBox(chatBean){
	if(!chatBean) throw new error("chatBean should be initialized!"); 
	this.chatId = chatBean.chatId || "";
	this.box = chatBean.box || null; //box dom实例
	this.tab = chatBean.tab || null; //会话列表tab项
	this.host = chatBean.host || ""; //登录者 JID
	this.from = chatBean.from || ""; //接收消息时 JID
	this.to = chatBean.to || ""; //发送消息时 JID
	this.sessionId = chatBean.sessionId ||"";//当前的会员Id
	this.msgId = chatBean.msgId ||"";  //消息ID
	this.shopId = chatBean.shopId || "";
	this.current = chatBean.current || false; //是否当前窗口
	this.unread = chatBean.unread || 0; //未读消息
    this.isinit = chatBean.isinit || false;
	this.model = chatBean.model || ChatBoxUtil.constants.SERV;  // "serv" "cust" 
	this.reciveTmplId = chatBean.reciveTmplId || "record"; //接收消息展示模板
	this.sendTmplId = chatBean.sendTmplId || "selfRecord"; //发送消息展示模板
	this.msgInput = null; //消息输入区
	
	this.userInfo = chatBean.userInfo || {};
	
	this.lastSendMoment = new Date(); //最后一次发消息的时刻
	this.sendable = true; //消息可否发送
	this._creationTime = new Date();
	//插入时间提示
	this.showTime = 1;
	this.chatnum = 0;
	//存放iscroll new出的对象
	this.ordScroll = null;
	this.gdsScroll = null;
	this.chatScroll = null;
	this.hisScroll = null;
	//初始化
	this._initDom();
	
}

ChatBox.prototype = {
	constructor: ChatBox,
	getTo: function(){
		if(!this.to) throw new error("to is not found!");
		return this.to;
	},
	getFrom: function(){
		if(!this.from) throw new error("from is not found!");
		return this.from; 
	},
	setFrom: function(from){
		if(!from) throw new error("from is missing!");
		this.from = from;
	},
	setTo: function(to){
		if(!to) throw new error("to is missing!");
		this.to = to;
	},
	isCurrent: function(){
		return this.current;
	},
	setCurrent: function(status){
		status == !!status;
		this.current = status;
	},
	setSessionId : function(sessionId){
		if(!sessionId) throw new error("session is missing!");
		this.sessionId = sessionId;
	},
	
	/**
	 * 关闭会话窗口
	 */
	close: function(){
		//1.窗口移除
		this.box.remove();
		if(this.tab) this.tab.remove();
		//2.ChatBoxUtil.list 移除
		var index = $.inArray(this, ChatBoxUtil.list);
		if(index>=0){
			ChatBoxUtil.list.splice(index,1);
		}
	},
	/**
	 * 展示会话窗口
	 */
	show: function(){
		//1.当前窗口显示
		//2.兄弟窗口隐藏
		var list = ChatBoxUtil.list;
		for(var chat in list){
			var chatBox = list[chat];
			if(chatBox.chatId == this.chatId){
				//chatBox._display();
			}else{
				chatBox._hide();
			}
		}
		
		//tab 
    	this.unread = 0;
    	if(this.tab) this.tab.find(".mNum").html(this.unread).hide();
	},
	/**
	 * 显示当前
	 */
	_display: function(){
		this.current = true;
		//显示窗口
		this.box.show();
		//展示添加滚动条
		 var chatRecord=$('.chat-record',this.box);
			var proPanel=$('.pro-panel',this.box);
			var quePanel=$('.que-panel',this.box);
			var allordPanel=$('.allord-panel',this.box);
			var msgPanelCont=$('.msgPanelCont',this.box);
			var ordPanel=$('.ord-detail-panel',this.box);
			   var scrollClz='mCustomScrollbar';
			if(chatRecord.hasClass(scrollClz)){
				chatRecord.mCustomScrollbar('update');//实时更新滚动条当内容发生变化
				chatRecord.mCustomScrollbar('scrollTo', 'bottom');
			}
			if(proPanel.hasClass(scrollClz)&&proPanel.css('display')=='block'){
				proPanel.mCustomScrollbar('update');
			}
			if(allordPanel.hasClass(scrollClz)&&allordPanel.css('display')=='block'){	
				allordPanel.mCustomScrollbar('update');
			}
			if(ordPanel.hasClass(scrollClz)&&!ordPanel.is(":hidden")){
               	ordPanel.mCustomScrollbar('update');
             }
			if(quePanel.hasClass(scrollClz)&&quePanel.css('display')=='block'){
				quePanel.mCustomScrollbar('update');
			}
			if(msgPanelCont.hasClass(scrollClz)&&msgPanelCont.css('display')=='block'){
				msgPanelCont.mCustomScrollbar('update');
			}
		//选中tab
		if(this.tab) this.tab.addClass("current");
		
		//从cookie中读取ueditor快捷键
	    var ueditorShortKeyIndex=$.cookie('ueditorShortKeyIndex_'+$('#staffCode').val());
    	if(ueditorShortKeyIndex){
    		$('.btn-keysel li',this.box).eq(ueditorShortKeyIndex).addClass('checked').siblings().removeClass('checked');
    	}
	},
	/**
	 * 隐藏当前
	 */
	_hide: function(){
		this.current = false;
		this.box.hide();
		if(this.tab) this.tab.removeClass("current");
	},
	
	
	/**
	 * 展示消息
	 */
	showReceived: function(msgInfo){
		var msg = msgInfo.msg;
		var box= this;
		var chatMain = $('#chatMain'+box.chatId);
		var chatRecord = $(".chat-record", chatMain);
        var chartList = $('.chat-list', chatRecord);
        box.chatnum = box.chatnum + 1;//统计接收消息的数量
        //每8条消息插入一个时间(包括接收和发送)
        if(this.chatnum%8 ==0){
        	var tpltime = {
            		time: ebcDate.dateFormat(new Date(), "hh:mm"),
                }
        	 var htmltime = template('timeRecord', tpltime);
            chartList.append(htmltime);
        }
        
        var tplData = {
    		msg: msg,
    		servPic: ChatBoxUtil.servPic
        }
        var html = template('record', tplData);
        if(msgInfo.messageType=='welcome'){
        	chartList.append(html);
        	ChatBoxUtil.countWinHeight(box);
     		
        }else{
        	//消息到达
        	$.eAjax({
    	        url: GLOBAL.WEBROOT + '/mobilecusthistory/updateMsgStatus',
    	        data: {id:box.msgId},
    	        datatype: 'json',
    	        async: false,
    	        success: function(returnInfo) {
    	        	  chartList.append(html);
    	        	  ChatBoxUtil.countWinHeight(box);
    	        },
    	        exception: function() {
    	            eDialog.alert("消息展示异常，请检查您的网络");
    	            return;
    	        }
            });
        }
        
        //tab
//        var tabItem = this.tab;
//        if(tabItem){
//	        tabItem.find(".tip").html(msg.substring(0,30)+"...");
//	        if(!this.current){
//	        	this.unread++;
//	        	tabItem.find(".mNum").html(this.unread).show();
//	        }
//        }
	},
	/**
	 * 发送消息
	 */
	sendMessage: function(){
		if(ChatBoxUtil.connected) {
			
			//控制发送频率
//            var smDiff = new Date().getTime() - this.lastSendMoment.getTime();
//            if(smDiff < 500){
//            	ChatBoxUtil.showNotice('消息发送过于频繁，请稍事休息！',true);
//            	return;
//            }
            
            //前序消息发送中
            if(!this.sendable){
            	ChatBoxUtil.showNotice('消息发送过于频繁，请稍事休息！',true);
            }
			
			var waitCount = $('#waitCount').val();
			if(waitCount>0){
				ChatBoxUtil.showNotice("您的前面还有"+waitCount+"位正在等待，请稍等",false);
				return false;
			}
            if(!this.to && !this.from) {  
            	//eDialog.alert("没有联系人！");
            	ChatBoxUtil.showNotice('您好，客服还没有上线，请在正常工作时间内联系客服人员.',true);
    			return false;
            }else{
            	var sessionid = $('#sessionId').val();
            	this.setSessionId(sessionid);
            	var flag = true;
                $.eAjax({
        	        url:GLOBAL.WEBROOT + '/mobilecusthistory/getSessionByone',
        	        data: {id:sessionid},
        	        datatype: 'json',
        	        async: false,
        	        success: function(returnInfo) {
        	        
                           if(returnInfo.status!='1'){
                        	   //ChatBoxUtil.showNotice('与当前客服的会话已经结束，重新发起咨询请点击'+'<span style="color:blue" id="resrt">确认</span>'+'按钮',false);
                        	   flag =  false;
                        	   eDialog.alert('客服关闭会话，会话已结束，请您谅解。',function(){
	          	        			 window.history.back(-1);
	          	        			 return false;
	          	        			
	                        	   });
                           }        	        	
        	        
        	            }
                     });
                if(!flag){
                	return false;
                }
            }
            
            // 发送消息 
            // 创建一个<message>元素并发送  
            //var ue = ChatBoxUtil.ue;
            var messageValue = $('.inSend').html();
            
            //messageValue ="<img src='http://192.168.1.104:8080/pmph-web-mall/images/face/i_f04.gif'/>";
            /*
             * 消息处理；1.内容  2.频次
             */
            if(messageValue == "") {
            	
                //ue.execCommand("cleardoc");
            	return;
            }
            this.sendable = false; //消息发送中
            /**
             * 消息uuid生成
             */
            var csa = this.to.split('/');
            var issueType = $('#issueType').val();
            var data = {to:this.to||this.from,
        		    toResource:csa[1],
        		    from:ChatBoxUtil.connection.jid,
        		    body:messageValue,
        		    sessionId:this.sessionId,
        		    messageType:'msg',
        		    contentType:'0',
        		    shopId : this.shopId,
        		    csaCode:ChatBoxUtil.getUserFromJid(this.to||this.from),
        		    userCode:ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid),
        		    issueType:$('#issueType').val(),
        		    gdsId : $('#gdsId').val(),
        		    ordId : $('#ordId').val()}
            $.eAjax({
    	        url: GLOBAL.WEBROOT + '/mobilecusthistory/saveMsgHistory',
    	        data: data,
    	        datatype: 'json',
    	        async: false,
    	        success: function(returnInfo) {
    	        	ChatBoxUtil.message.messagetype = returnInfo.messageType;
    	        	ChatBoxUtil.message.body = returnInfo.body;
    	        	messageValue = returnInfo.body;
    	        	ChatBoxUtil.message.sessionId = returnInfo.sessionId;
    	        	ChatBoxUtil.message.contentType = returnInfo.contentType;
    	        	ChatBoxUtil.message.sendTime = returnInfo.beginDate;
    	        	var str = JSON.stringify(ChatBoxUtil.message); 
    	            var msg = $msg({  
    	                to: returnInfo.to,//发送或回复消息   
    	                from: returnInfo.from, //登录者ID
    	                id : returnInfo.id,
    	               // messagetype : returnInfo.messageType,
    	                type: 'chat'  
    	            }).c("body", null, str)//.c("session", null,returnInfo.sessionId);//带入本次会话的sessionId
    	            ChatBoxUtil.connection.send(msg.tree());  
    	            //用户发消息后，先清除原来的定时任务 
    	            window.clearTimeout(ChatBoxUtil.timeOutId);
    	            window.clearTimeout(ChatBoxUtil.timeOutRId);
    	            
    	            //重新开启定时任务：10分钟后，如果用户没有主动发消息，则调用服务，请求断开链接
    	            ChatBoxUtil.timeOutId = window.setTimeout(ChatBoxUtil.disconnFromServ, ChatBoxUtil.outTime);
    	            ChatBoxUtil.timeOutRId = window.setTimeout(ChatBoxUtil.timeOutRemind, ChatBoxUtil.outTime-120000);
    	        },
    	        exception: function() {
    	            eDialog.alert("消息发送请求异常，请检查网络");
    	            this.sendable = true;
    	            return;
    	        }
            });
            
            // 展示消息
            var winItem = this.box;
    		var chatMain = $('#chatMain'+this.chatId,winItem);
    		var chatRecord = $(".chat-record", chatMain);
            var chartList = $('.chat-list', chatRecord);
            //插入时间提示
            this.chatnum = this.chatnum + 1;
            if(this.showTime==1){
            	var tpltime = {
                		time: ebcDate.dateFormat(ChatBoxUtil.message.sendTime, "hh:mm"),
                    }
            	 var htmltime = template('timeRecord', tpltime);
                chartList.append(htmltime);
                this.showTime = 2 ;
                
            }else if(this.chatnum%8 ==0){
            	var tpltime = {
                		time: ebcDate.dateFormat(ChatBoxUtil.message.sendTime, "hh:mm"),
                    }
            	 var htmltime = template('timeRecord', tpltime);
                chartList.append(htmltime);
            }
            var tplData = {
        		msg: messageValue,
        		uName: ChatBoxUtil.getStaffCodeByUserCode(ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid)),
        		time: ebcDate.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"),
        		custPic: ChatBoxUtil.custPic
            }
            var html = template('selfRecord', tplData);
            chartList.append(html);
            ChatBoxUtil.countWinHeight(this);
            //清空
            $('.inSend').empty();
            this.sendable = true;
        } else {  
        	eDialog.alert("请先登录！");  
        }  
	}, 

	/**
	 * 同步其他平台的消息
	 * @param msg
	 */
	syncSendMessage: function(msgInfo){
		// 展示消息
        var winItem = this.box;
		var chatMain = $('#chatMain'+this.chatId,winItem);
		var chatRecord = $(".chat-record", chatMain);
        var chartList = $('.chat-list', chatRecord);
        var staffCode = ChatBoxUtil.getStaffCodeByUserCode(ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid));
        var tplData,html,msg;
		
        // 消息类型处理
        if(msgInfo.messageType == "msg"){
	        tplData = {
	    		msg: msgInfo.msg,
	    		uName: staffCode,
	    		custPic: ChatBoxUtil.custPic
	        }
	        html = template('selfRecord', tplData);
        }else if(msgInfo.messageType == "gds"){
        	msg = JSON.parse(msgInfo.msg);
        	tplData = {
        		uName: staffCode,
        		custPic : ChatBoxUtil.custPic,
        		url : $("#gdsDetailUrl").val() + msg.gdsId + "-",
        		gdsId : msg.gdsId,
        		gdsImage : msg.gdsImage,
        		gdsName : msg.gdsName,
        		price : msg.price,
        		chatSide : "chat-right"
            }
            html = template('recordGds1', tplData);
        }else if(msgInfo.messageType == "order"){
        	msg = JSON.parse(msgInfo.msg);
        	tplData = {
        		uName: staffCode,
        		custPic : ChatBoxUtil.custPic,
        		ordId : msg.ordId,
        		price : msg.price,
        		ordImage : msg.ordImage,
        		createTime : ebcDate.dateFormat(new Date(Number(msg.createTime)), "yyyy-MM-dd hh:mm:ss"),
        		url : $("#ordDetailUrl").val() + msg.ordId,
        		chatSide : "chat-right"
            }
            html = template('recordOrder1', tplData);
        }
        $.eAjax({
	        url: GLOBAL.WEBROOT + '/mobilecusthistory/updateMsgStatus',
	        data: {id:msgInfo.id},
	        datatype: 'json',
	        async: false,
	        success: function(returnInfo) {
	        	  chartList.append(html);
	        	  //消息置底
	        	  var scrollHi = $(window).height() - $('.am-header').height() - $('#chat-input').height();
	              var that = this;
	          	  chartList.imagesLoaded(function () {
	          		that.chatScroll.refresh();
	          		that.chatScroll.scrollTo(0,-chartList.height()+scrollHi, 100);
	    	       });
	        	  
	        	  
	        },
	        exception: function() {
	            eDialog.alert("消息展示异常，请检查您的网络");
	            return;
	        }
        });
	},
	//保存评价
	sendEvaluate: function(){
		if(ChatBoxUtil.connected) { 
			var satisfyType = "";
			var notSatisfyType = "";
			var notSatisfyReason ="";
			var score = $(".eva-cont").find("input[name='evalscore']").val();
			
			if(score=="1"){
				//对业务流程不满意
	    		notSatisfyType = 1;
	    		satisfyType = 1;
	    		//不满意原因
				var reason = $(".eva-cont").find("input[name='notSatisfyReason']").val();
				notSatisfyReason = $.trim(reason);
				if(notSatisfyReason == ""){
					$("#notSatisfyReason").attr("placeholder","请填写业务流程不满意原因！");
					return false;
				}
	    	}else if (score=="2"){
	    		notSatisfyType = 2;
	    		satisfyType = 2;
	    	}else{
	    		satisfyType = score;
	    	}
			var data = {csaCode:ChatBoxUtil.csaCode,
					shopId:this.shopId,
					ofStaffCode:$("#uNameForLogout").val(),
					sessionId:this.sessionId,
					satisfyType:satisfyType,
					notSatisfyType:notSatisfyType,
					notSatisfyReason:notSatisfyReason}
			$.eAjax({
				url:GLOBAL.WEBROOT + '/customerservice/saveEvaluate',
				data: data,
				datatype: 'json',
		        async: false,
				success:function(returnInfo){
					if(returnInfo&&returnInfo.resultFlag == 'ok'){
						ChatBoxUtil.showNotice('您对'+ChatBoxUtil.ServName+'评价成功！',true);
						$('.customer-evaluate-fixed').hide();
					}else{  
						ChatBoxUtil.showNotice(returnInfo.resultMsg,true);
						$('.customer-evaluate-fixed').hide();
					}
				}
			});   
            
        } else {  
        	eDialog.alert("请先登录！");  
        }  
	}, 
	//检查评价权限
	evaluateCheck: function(){
		if(ChatBoxUtil.connected) {   
			if(this.isinit){
				var data = {csaCode:ChatBoxUtil.csaCode,
						shopId:this.shopId,
						ofStaffCode:$("#uNameForLogout").val(),
						sessionId:this.sessionId,
					}
				$.eAjax({
					url:GLOBAL.WEBROOT + '/customerservice/evaluateCheck',
					data: data,
					datatype: 'json',
			        async: false,
					success:function(returnInfo){
						if(returnInfo&&returnInfo.resultFlag == 'ok'){
							$('.customer-evaluate-fixed').show();
							
						}else{  
							ChatBoxUtil.showNotice(returnInfo.resultMsg,true);
						}
					}
				});  
			} 
        } else {  
        	eDialog.alert("请先登录！");  
        } 
	},
	
	//历史记录信息
	_loadHistory : function(){
		var chatBox = this;
		var staffCode = $('#staffCode').val();
		var userCode = $("#uNameForLogout").val();
		//var csaCode = $("#csaCodeForLogout").val();
		//没有接入客服的时候，防止下拉页面加载数据	
//		if(!chatBox.isinit){
//			return false;
//		}
		var scrollHi = $(window).height() - $('.am-header').height();// 
		var loadDom = $('#hisScorllDiv').height(scrollHi);
		$('.scroll',loadDom).css('min-height',scrollHi+10);//内层高度大于外层高度,浏览器才能滚动
		
		chatBox.hisScroll = new $.AMUI.iScroll('#hisScorllDiv', {
            preventDefault: false
        });
//		chartList.imagesLoaded(function(){
//			chatBox.hisScroll.refresh();
//		})
        var isDownLoad = false;
        var pullStart = 0;
        chatBox.hisScroll.on('scrollStart', function () {
            pullStart = this.y;
            isDownLoad = this.directionY === -1 && pullStart >= 0;
            if (isDownLoad) {
                loadDom.addClass('loading');
            }
        });
        //var pageNum = ChatBoxUtil.historyflag;
        chatBox.hisScroll.on('scrollEnd', function () {
            if (isDownLoad) {
                loadDom.removeClass('loading');
                
                //加载数据
                ChatBoxUtil.historyflag  =ChatBoxUtil.historyflag+1;
                $.eAjax({
        	        url: GLOBAL.WEBROOT + '/mobilecusthistory/getMessageHistory',
        	        data: {
        	        	userCode:userCode,
        	        	pageNumber:ChatBoxUtil.historyflag,
        	        	pageSize:ChatBoxUtil.pageSize,
        	        	shopId : chatBox.shopId,
        	        	status:'20'
        	        },
        	        datatype: 'json',
        	        async: false,
        	        success: function(returnInfo) {
        	        	//returnInfo = returnInfo.reverse();
        	        	var winItem = chatBox.box;
        	      		var chatMain = $('#historyMain'+userCode+'WEB',winItem);
          	    		var chatRecord = $(".chat-record", chatMain);
          	            var chartList = $('.chat-list', chatRecord);
        	        	$.each(returnInfo, function(p1, p2) {
        	        	   if(ChatBoxUtil.getUserFromJid(p2.from) ==  ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid)){
        	        	            var tplData = {
        	        	            		msg: p2.body,
        	        	            		uName: staffCode,
        	        	            		time: ebcDate.dateFormat(p2.beginDate, "yyyy-MM-dd hh:mm:ss"),
        	        	            		custPic: ChatBoxUtil.custPic
        	        	            }
        	        	            var html = template('selfRecord', tplData);
        	        	            if(p2.messageType != "msg"){
        	        	            	var domInfo = {
        	        	            			messageType: p2.messageType,
        	        	            			uName: staffCode,	
        	        	            			custPic: ChatBoxUtil.custPic
        	        	            	};
        	        	            	html = chatBox._createMessageDom(domInfo, p2, "chat-right");
        	        	            }
        	        	            $('.ser-more',chartList).after(html);
        	        	           
        	        		}else{
        	        			
        		                var tplData = {
        		            		msg: p2.body,
        		            		uName:ChatBoxUtil.ServName,
        		            		time: ebcDate.dateFormat(p2.beginDate, "yyyy-MM-dd hh:mm:ss"),
        		            		servPic: ChatBoxUtil.servPic
        		                }
        		                var html = template('record', tplData);
        		                if(p2.messageType != "msg"){
        		                	var domInfo = {
        		                			messageType: p2.messageType,
        		                			uName: ChatBoxUtil.ServName,	
        		                			servPic: ChatBoxUtil.servPic
        		                	};
        		                	html = chatBox._createMessageDom(domInfo, p2, "");
        		                }
        		                $('.ser-more',chartList).after(html);
        	        		}
        	        	   //刷新
        	        	   chartList.imagesLoaded(function () {
        	        		   chatBox.hisScroll.refresh();
        	        		   
   			  	        });
        	        	   
        	        	   
        	        	});
        	        	$('.ser-more',chartList).hide();
        	        },
        	        exception: function() {
        	            eDialog.alert("滚动加载历史消息异常，请检查网络");
        	            return;
        	        }
                });

            }
        });
	},
	
	//商品页面
	_loadGoods : function(){
		var box = this;
		var scrollHi = $(window).height() - $('.am-header').height();
		var loadDom = $('#goodsDiv').height(scrollHi);
		$('.scroll',loadDom).css('min-height',scrollHi+10);//内层高度大于外层高度,浏览器才能滚动
		box.gdsScroll = new $.AMUI.iScroll('#goodsDiv', {
            preventDefault: false
        });
        var isDownLoad = false;
        var pullStart = 0;
        box.gdsScroll.on('scrollStart', function () {
            pullStart = this.y;
            isDownLoad = this.directionY === -1 && pullStart >= 0;
            if (isDownLoad) {
                loadDom.addClass('loading');
            }
        });
        var pageNum = 1;
        box.gdsScroll.on('scrollEnd', function () {
            if (isDownLoad) {
                loadDom.removeClass('loading');
                //加载数据
                pageNum  =pageNum+1;
                var shopId = box.shopId;//var shopId = box.shopId;
        		$.eAjax({
        			url: GLOBAL.WEBROOT + '/customerservice/browsegoodsmsg',
        			data: {
        				pageNumber:pageNum,
        	        	pageSize:ChatBoxUtil.pageSize,
        	        	shopId:shopId,	
        			},
        			datatype: 'json',
        			async: false,
        			success: function(returnInfo) {
        				var winItem = box.box;
        				var chatMain = $('#gdsbody',winItem);
        				var chartList = $("#customer-gds", chatMain);
        				
        				$.each(returnInfo, function(p1, p2) {
        					tplData = {
        							gdsMsg : p2.gdsDesc,
        				    		price : (p2.defaultPrice*0.01).toFixed(2),
        				    		gdsImage : p2.imageUrl,
        				    		gdsId: p2.id,
        				    		gdsName:p2.gdsName
        				        }
        						html = template('recordGds', tplData);
        						$(html).appendTo(chartList);
        						chartList.imagesLoaded(function () {
        							box.gdsScroll.refresh();
        			  	        });
        				});
        			}
        		})

            }
        });
        
	},
	
	//订单页面
	_loadOrder: function(){
		var box = this;
		var scrollHi = $(window).height() - $('.am-header').height();
		var loadDom = $('#orderDiv').height(scrollHi);
		$('.scroll',loadDom).css('min-height',scrollHi+10);//内层高度大于外层高度,浏览器才能滚动
		box.ordScroll = new $.AMUI.iScroll('#orderDiv', {
            preventDefault: false
        });
        var isDownLoad = false;
        var pullStart = 0;
        //防止下滑页面时,头部随动
        document.addEventListener('touchmove', function (e) {
            e.preventDefault();
        }, false);

        box.ordScroll.on('scrollStart', function () {
            pullStart = this.y;
            isDownLoad = this.directionY === -1 && pullStart >= 0;
            if (isDownLoad) {
                loadDom.addClass('loading');
            }
        });
        var pageNum = 1;
        this.ordScroll.on('scrollEnd', function () {
            if (isDownLoad) {
                loadDom.removeClass('loading');
                pageNum  =pageNum+1;
                var shopId = box.shopId;
        		$.eAjax({
        			url: GLOBAL.WEBROOT + '/customerservice/ordermsg',
        			data: {
        				pageNumber:pageNum,
        	        	pageSize:ChatBoxUtil.pageSize,
        	        	shopId:shopId,	
        			},
        			datatype: 'json',
        			async: false,
        			success: function(returnInfo) {
        				var winItem = box.box;
        				var chatMain = $('#orderbody',winItem);
        				var chartList = $("#customer-order", chatMain);
        				
        				$.each(returnInfo, function(p1, p2) {
        					tplData = {
        							createTime: ebcDate.dateFormat(p2.orderTime, "yyyy-MM-dd hh:mm:ss"),
        				    		ordId : p2.orderId,
        				    		price : (p2.realMoney*0.01).toFixed(2),
        				    		ordImage : p2.ord01102Resps[0].picUrl,
        				    		timeDto : p2.orderTime,
        				        }
        						html = template('recordOrder', tplData);
        						$(html).appendTo(chartList);
        						chartList.imagesLoaded(function () {
        							box.ordScroll.refresh();
        			  	        });
        				});
        			}
        		})

            }
        });
	},
	
	//聊天会话
	_loadChat: function(){
		var chatBox = this;
		var winItem = this.box;
		var staffCode = $('#staffCode').val();
		var html = template('showtemp', {});
		var chatMain = $('#chatMain'+this.chatId,this.box);
		chatMain.append(html);//载入聊天显示区域
		var chatRecord = $(".chat-record", chatMain);//接收或者发送的消息区域
		var chartList = $('.chat-list', chatRecord);//ul的class
		
		
		var scrollHi = $(window).height() - $('.am-header').height() - $('#chat-input').height();// 
		var loadDom = $('#chatScorllDiv').height(scrollHi);
		$('.scroll',loadDom).css('min-height',scrollHi+10);//内层高度大于外层高度,浏览器才能滚动

		chatBox.chatScroll = new $.AMUI.iScroll('#chatScorllDiv', {
            preventDefault: false
        });
		
		chartList.imagesLoaded(function(){
			chatBox.chatScroll.refresh();
		})
		
		//没有接入客服的时候，防止下拉页面加载数据	
		if(!chatBox.isinit){
			return false;
		}
		
        var isDownLoad = false;
        var pullStart = 0;
        chatBox.chatScroll.on('scrollStart', function () {
            pullStart = this.y;
            isDownLoad = this.directionY === -1 && pullStart >= 0;
            if (isDownLoad) {
                loadDom.addClass('loading');
            }
        });
        var pageNum = 0;
        var beginScroll = true;
		var userCode = $("#uNameForLogout").val();
		//var csaCode = $("#csaCodeForLogout").val();
        chatBox.chatScroll.on('scrollEnd', function () {
            if (isDownLoad) {
                loadDom.removeClass('loading');
//                if(pageNum==1){
//                	$('#his-tip1',chartList).show();//显示以上是历史消息
//                }
                if(beginScroll){
                	$("#his-tip1").nextAll().remove();
                	beginScroll = false;
                }
                //加载数据
                pageNum  =pageNum+1;
                $.eAjax({
        	        url: GLOBAL.WEBROOT + '/mobilecusthistory/getMessageHistory',
        	        data: {
        	        	userCode:userCode,
        	        	pageNumber:pageNum,
        	        	pageSize:ChatBoxUtil.pageSize,
        	        	shopId:chatBox.shopId,
        	        	status:'20'
        	        },
        	        datatype: 'json',
        	        async: false,
        	        success: function(returnInfo) {
        	        	//returnInfo = returnInfo.reverse();
        	        	var winItem = chatBox.box;
          	    		var chatMain = $('#chatMain'+userCode+'WEB',winItem);
          	    		var chatRecord = $(".chat-record", chatMain);
          	            var chartList = $('.chat-list', chatRecord);
        	        	$.each(returnInfo, function(p1, p2) {
        	        	   if(ChatBoxUtil.getUserFromJid(p2.from) ==  ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid)){
        	        	            var tplData = {
        	        	            		msg: p2.body,
        	        	            		uName: staffCode,
        	        	            		time: ebcDate.dateFormat(p2.beginDate, "yyyy-MM-dd hh:mm:ss"),
        	        	            		custPic: ChatBoxUtil.custPic
        	        	            }
        	        	            var html = template('selfRecord', tplData);
        	        	            if(p2.messageType != "msg"){
        	        	            	var domInfo = {
        	        	            			messageType: p2.messageType,
        	        	            			uName: staffCode,	
        	        	            			custPic: ChatBoxUtil.custPic
        	        	            	};
        	        	            	html = chatBox._createMessageDom(domInfo, p2, "chat-right");
        	        	            }
        	        	            $('.ser-more',chartList).after(html);
        	        	           
        	        		}else{
        	        			
        		                var tplData = {
        		            		msg: p2.body,
        		            		uName:ChatBoxUtil.ServName,
        		            		time: ebcDate.dateFormat(p2.beginDate, "yyyy-MM-dd hh:mm:ss"),
        		            		servPic: ChatBoxUtil.servPic
        		                }
        		                var html = template('record', tplData);
        		                if(p2.messageType != "msg"){
        		                	var domInfo = {
        		                			messageType: p2.messageType,
        		                			uName: ChatBoxUtil.ServName,	
        		                			servPic: ChatBoxUtil.servPic
        		                	};
        		                	html = chatBox._createMessageDom(domInfo, p2, "");
        		                }
        		                $('.ser-more',chartList).after(html);
        	        		}
        	        	   //刷新
        	        	   chartList.imagesLoaded(function () {
        	        		   chatBox.chatScroll.refresh();
   			  	        });
        	        	   
        	        	   
        	        	});
        	        	$('.ser-more',chartList).hide();
        	        },
        	        exception: function() {
        	            eDialog.alert("滚动加载历史消息异常，请检查网络");
        	            return;
        	        }
                });

            }
        });
    
	},
	
	_createBox: function(){
			var chatWin=$('.w');
			
			chatWin.empty();
			var winItem=$('<div class="winItem"></div>');
			this.box = winItem; //设置box
	        chatWin.append(winItem);
	        
	        //1.聊天会话
	        var chatbody=$('<div class="ui-customer-fixBottom" id="chatBody"></div>');
	        winItem.append(chatbody);
	        var html = template('chattemp',{'chatId':this.chatId,'model':ChatBoxUtil.constants.CUST}); 
	        chatbody.append(html);
	        winItem.attr('id','winItem'+this.chatId);
	        //2.订单页面
	        var orderbody=$('<div class="ui-content" id="orderbody" style="display:none;" ></div>');
	        winItem.append(orderbody);
	        var html2 = template('orderlist',{'chatId':this.chatId,'model':ChatBoxUtil.constants.CUST}); 
	        orderbody.append(html2);
	        //3.商品页面
	        var gdsbody=$('<div class="ui-content" id="gdsbody" style="display:none;" ></div>');
	        winItem.append(gdsbody);
	        var html3 = template('gdslist',{'chatId':this.chatId,'model':ChatBoxUtil.constants.CUST}); 
	        gdsbody.append(html3);
	        //4.历史消息
	        var hisbody=$('<div class="ui-customer-fixBottom" id="historyBody" style="display:none;"></div>');
	        winItem.append(hisbody);
	        var html4 = template('histemp',{'chatId':this.chatId,'model':ChatBoxUtil.constants.CUST}); 
	        hisbody.append(html4);
	        
	        this._loadChat();//聊天工作区
	        this._loadOrder();//订单页面
	        this._loadGoods();//商品信息
	        this._loadHistory();//历史信息
        if(this.isinit){
        	   //this._createChatHis();//聊天记录
               //this._createHistoryMsg();//最近会话记录 
               this._createOrderMsg();//订单信息
               this._createGdsMsg();//商品信息
        }
     
        

        //实例化编辑器
        //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
//        var editorId = 'editor'+this.chatId;
//        UE.delEditor(editorId);
//        var ue = UE.getEditor(editorId, {
//            initialFrameHeight: '100',
//            initialFrameWidth: '100%',
//            elementPathEnabled: false,
//            wordCount: false,
//            autoHeightEnabled: false,
//            autoFloatEnabled: false,
//            enableAutoSave: false,
//            serverUrl:GLOBAL.WEBROOT+"/ueditfileupload/upload",
//            toolbars: [
//                ['emotion','simpleupload']
//            ]
//        });
//        ChatBoxUtil.ue = ue; //设置
        
        
        
//        var chatBox = this;
//        ue.addListener("keydown", function(i,e){ 
//        	var enterIndex=$('.btn-keysel li.checked',winItem).index();
//        	if(enterIndex==0){
//            	if(e.ctrlKey && e.which == 13 || e.which == 10) { 
//        			chatBox.sendMessage();
//        		}
//        	}else{
//        		if(e.which == 13 || e.which == 10) { 
//        			chatBox.sendMessage();
//        		}
//        	}
//    	});
	},
	_createTab: function(){
		var chatList=$('.chat-uList');
		var html = template('chatItemTpl',{'uName':this.chatId, 'chatId':this.chatId});
		this.tab = $(html);
		chatList.append(this.tab);
	},
	//初始化
	_initDom: function(){
		if(ChatBoxUtil.hasChatBox(this.chatId)) return;
		//if(this.model == ChatBoxUtil.constants.SERV) this._createTab();
		this._createBox();
		ChatBoxUtil.list.push(this);
		if(this.isinit){
			this._bindEvent();
			ChatBoxUtil.timeshowId = window.setInterval(ChatBoxUtil.chatTimeShow(this), 120000);//接入后启动计时器，每隔两分钟会在发送消息时插入时间在聊天区域
			
		}
		
		
	},
	/**
	 *  绑定事件
	 */
	_bindEvent: function(){
	
		
		var chatBox = this;
		var winItem = this.box;
		//图片放大
        eImage.gallery($('.chat-record', winItem),{
            bigImg:".chat-wrap img",
            ignoreImg:".head-img img"
        });
		var mFun = $('#chat-input').find('.mFun');
	    var emoticon = mFun.siblings();
		if(this.isinit){
			//消息发送	
			$("#sendMsg").click(function(e){
				if(ChatBoxUtil.connected) chatBox.sendMessage();
				$('.head-input .inSend').focus();
			}); 
			//var bfscrolltop = document.body.scrollTop;
			$('.head-input .inSend').focus(function () {
		    	emoticon.hide();
	            mFun.hide();
	            
	            var u = navigator.userAgent;
	            var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	            if(isiOS){
	               $('html, body').animate({scrollTop: $(document).height()}, 500);
	            }
	            
//	            //ios系统虚拟键盘掩盖部分聊天区域解决代码
//	            interval = setInterval(function(){
//	            	document.body.scrollTop = document.body.scrollHeight;
//	            },100)
//	            ChatBoxUtil.countWinHeight(chatBox);
//	            clearInterval(interval);
	        });
			//我的订单
		    $("#myorder").click(function(){
		    	$("#orderbody").show();//显示订单区域
		    	chatBox.ordScroll.refresh();//要先 show 才能  refresh
			    $("#chatBody").hide();//隐藏聊天区域
		    });
		    //浏览商品
		    $("#browseGoods").click(function(){
		    	$("#gdsbody").show();//显示商品区域
		    	chatBox.gdsScroll.refresh();
			    $("#chatBody").hide();//隐藏聊天区域
		    });
		    //打开评论
		    $('#funFont').click(function () {
		    	chatBox.evaluateCheck();
		    });
		    //打开聊天历史记录
		    $("#hismsgbtn").click(function(){
		    	$("#historyBody").show();//显示历史记录区域
		    	//chatBox.hisScroll.refresh();//要先 show 才能  refresh
			    $("#chatBody").hide();//隐藏聊天区域
			    chatBox._createChatHis();
		    });
		    $(window).on('resize.chatScroll',function(){
		    	ChatBoxUtil.countWinHeight(chatBox);
		    });
			
		    //点击+号 打开隐藏区域
		    $('#head-mFun img').click(function () {
		        mFun.show();
		        emoticon.hide();
		        ChatBoxUtil.countWinHeight(chatBox);
		    });
		    $('#head-emoticon img').click(function () {
		        emoticon.show();
		        mFun.hide();
		        
		        var Path = window.parent.parent.document.getElementById("imProjectUrl").value + '/images/';
		        //console.log(Path);
		        var emotionImg = $('.emoticons .am-slides');
	            if(!emotionImg.data('load')){
	               new ChatBoxUtil.EmotionImg({
	                    serverUrl : '/pmph-web-mobile/images/face/',
	                    contanier : emotionImg
	                });
	                emotionImg.data('load', true);
	                $('.am-slider').flexslider(); //图片滑动

	                $('.emoticons i').click(function () {
	                    var imgurl = $(this).data('imgurl');//本地显示的表情地址
	                	//$('.inSend').append('<img src="'+imgurl+'"/>')
	                    var serimgurl = Path+imgurl.substring(imgurl.indexOf('face/'));//客服显示的地址
	                	//$('.saveSend').append('<img src="'+serimgurl+'"/>')
	                	$('.inSend').append('<img src="'+serimgurl+'"/>')
	                });
	            }
	            ChatBoxUtil.countWinHeight(chatBox);
		    });
		};
	
	    //右侧三个点 ... 的显示与隐藏
	    $('.otxt .icon-more').click(function () {
	        $(this).parents('.head-down').find('.down-list').toggle();
	    });
	    //关闭评价
	    $('.customer-evaluate .evaClose').click(function () {
	        $(this).parents('.customer-evaluate-fixed').hide();
	    });
	    
	    //评价等级选择
	    $(".eva-state li").click(function() {  
            $(this).addClass("active").siblings().removeClass("active");
            var scoer =$(this).val();
            $(".eva-cont").find("input[name='evalscore']").val($(this).val());
            if(scoer == "1"){
            	$(".eva-edit").show();
            }else {
            	$(".eva-edit").hide();
            }
        }); 
	    //保存评价
	    $(".eva-btn").click(function() {
	    	
	    	chatBox.sendEvaluate();
	    });
	    //返回聊天界面
	    $("#orderBackBtn").click(function(){
	    	$("#orderbody").hide();//隐藏订单区域
		    $("#chatBody").show();//显示聊天区域
		    chatBox.chatScroll.refresh();
		    return false;
	    });
	   
	    $("#goodsBackBtn").click(function(){
	    	$("#gdsbody").hide();//隐藏商品区域
		    $("#chatBody").show();//显示聊天区域
		    chatBox.chatScroll.refresh();
		    return false;
	    });
	    $("#hisBackBtn").click(function(){
	    	$("#historyBody").hide();//隐藏商品区域
		    $("#chatBody").show();//显示聊天区域
		    chatBox.chatScroll.refresh();
		    return false;
	    });
	    
	    /* ========= 浏览商品 begin ========== */
	     
	     $('#customer-gds').delegate('.goodsitem',"click",function(){
	    	 var box = chatBox;
		    	//console.log(chatBox);
	            var html = $(this).html();
	            
	            $("#dlgCont2").remove();
				var orderList = $("#gdsbody");
		    	var temp = template('gdsWinTemp', {});
		    	orderList.append(temp);
	            
	            $(".goodsdoalog").empty();
	            $(".goodsdoalog").prepend(html);//把选择列表的商品复制一份到弹出框
	           
	            var gdsImage = $(this).find("input[name='gdsImage']").val();
	            var gdsName = $(this).find("input[name='gdsName']").val();
	            var price = $(this).find("input[name='price']").val();
	            var gdsId = $(this).find("input[name='gdsId']").val();
	            var gdsName = $(this).find("input[name='gdsName']").val();
	           
	            var msg = {"gdsId":gdsId,"gdsName":gdsName,"gdsImage":gdsImage,"price":price,"url":""}
	            var msgbody = JSON.stringify(msg); 
		    	bDialog.open({
	                title: '确认发送该商品到聊天页',
	                width: 0.8,
	                height: 200
	            }, $('#dlgCont2'));
		    	$('goodsitem').unbind('click');	
		    	//关闭弹出框
		    	$("#closeBtn2").click(function(){
		   	    	bDialog.closeCurrent();
		   	    });
		   	    //确认
		   	    $("#confimBtn2").click(function(){
		   	    	//console.log(box);
		   	    	var csa = box.to.split('/');
		            var issueType = $('#issueType').val();
		            var data = {to:box.to||box.from,
		        		    toResource:csa[1],
		        		    from:ChatBoxUtil.connection.jid,
		        		    body:msgbody,
		        		    sessionId:box.sessionId,
		        		    messageType:'gds',
		        		    contentType:'2',
		        		    shopId : box.shopId,
		        		    csaCode:ChatBoxUtil.getUserFromJid(box.to||box.from),
		        		    userCode:ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid),
		        		    issueType:$('#issueType').val(),
		        		    gdsId : $('#gdsId').val(),
		        		    ordId : $('#ordId').val()}
		            $.eAjax({
		    	        url: GLOBAL.WEBROOT + '/mobilecusthistory/saveMsgHistory',
		    	        data: data,
		    	        datatype: 'json',
		    	        async: false,
		    	        success: function(returnInfo) {
		    	        	ChatBoxUtil.message.messagetype = returnInfo.messageType;
		    	        	ChatBoxUtil.message.body = returnInfo.body;
		    	        	messageValue = returnInfo.body;
		    	        	ChatBoxUtil.message.sessionId = returnInfo.sessionId;
		    	        	ChatBoxUtil.message.contentType = returnInfo.contentType;
		    	        	ChatBoxUtil.message.sendTime = returnInfo.beginDate;
		    	        	var str = JSON.stringify(ChatBoxUtil.message); 
		    	            var msg = $msg({  
		    	                to: returnInfo.to,//发送或回复消息   
		    	                from: returnInfo.from, //登录者ID
		    	                id : returnInfo.id,
		    	               // messagetype : returnInfo.messageType,
		    	                type: 'chat'  
		    	            }).c("body", null, str)//.c("session", null,returnInfo.sessionId);//带入本次会话的sessionId
		    	            ChatBoxUtil.connection.send(msg.tree());  
		    	            //用户发消息后，先清除原来的定时任务 
		    	            window.clearTimeout(ChatBoxUtil.timeOutId);
		    	            window.clearTimeout(ChatBoxUtil.timeOutRId);
		    	            
		    	            //重新开启定时任务：5分钟后，如果用户没有主动发消息，则调用服务，请求断开链接
		    	            ChatBoxUtil.timeOutId = window.setTimeout(ChatBoxUtil.disconnFromServ, ChatBoxUtil.outTime);
		    	            ChatBoxUtil.timeOutRId = window.setTimeout(ChatBoxUtil.timeOutRemind, ChatBoxUtil.outTime-120000);
		    	            //关闭弹出框
		    	            bDialog.closeCurrent();
		    	            $("#gdsbody").hide();//隐藏商品区域
		    	            $("#chatBody").show();//显示聊天区域
		    	            
		    	        },
		    	        exception: function() {
		    	            eDialog.alert("消息发送请求异常，请检查网络");
		    	            this.sendable = true;
		    	            return;
		    	        }
		            });
		            // 展示消息
		    		//var winItem = box;
		    		var chatMain = $('#chatMain'+box.chatId);
		    		//console.log(chatMain);
		    		var chatRecord = $(".chat-record", chatMain);//接收或者发送的消息区域
		    		var chartList = $('.chat-list', chatRecord);//ul的class
		    		//console.log(chartList);
		            var tplData = {
		            	gdsImage: gdsImage,
		            	gdsName : gdsName,
		        		price:price,
		        		chatSide:"chat-right",
		        		custPic : ChatBoxUtil.custPic
		            }
		            var html = template('recordGds1', tplData);
		            chartList.append(html);
		            ChatBoxUtil.countWinHeight(box);
//		            var scrollHi = $(window).height() - $('.am-header').height() - $('#chat-input').height();
//		            var that = box;
//		          	chartList.imagesLoaded(function () {
//		          		that.chatScroll.refresh();
//		          		that.chatScroll.scrollTo(0,-chartList.height()+scrollHi, 100);
//		    	       });
		            
		   	    }) 
	     });
	    
	    /* ========= 浏览商品 end ========== */
	    
	    
	    /* ========= 订单 begin ========== */
	    //点击订单列表,弹出发送框
	    
	    $('#customer-order').delegate('.odritem',"click",'',function(){
	    	var box = chatBox;
	    	var html = $(this).html();
	    	$("#dlgCont").remove();
			var orderList = $("#orderbody");
	    	var temp = template('orderWinTemp', {});
	    	orderList.append(temp);
	    	//console.log(chatBox);
            $(".orderdoalog").empty();
            $(".orderdoalog").prepend(html);
           
            var ordImage = $(this).find("input[name='ordImage']").val();
            var ordId = $(this).find("input[name='ordId']").val();
            var price = $(this).find("input[name='price']").val();
            var createTime = $(this).find("input[name='timeDto']").val();//非格式化,用于传递
            var time = $(this).find("input[name='time']").val();//格式化,用于展示的时间
            var msg = {"ordId":ordId,"price":price,"ordImage":ordImage,"createTime":createTime,"url":""}
            var msgbody = JSON.stringify(msg); 
	    	bDialog.open({
                title: '确认发送该订单到聊天页',
                width: 0.8,
                height: 200
            }, $('#dlgCont'));
	    	$('.odritem').unbind('click');	
	    	//关闭弹出框
	    	$("#closeBtn").click(function(){
	   	    	bDialog.closeCurrent();
	   	    });
	   	    //确认
	   	    $("#confimBtn").click(function(){
	   	    	//console.log(box);
	   	    	var csa = box.to.split('/');
	            var issueType = $('#issueType').val();
	            var data = {to:box.to||box.from,
	        		    toResource:csa[1],
	        		    from:ChatBoxUtil.connection.jid,
	        		    body:msgbody,
	        		    sessionId:box.sessionId,
	        		    messageType:'order',
	        		    contentType:'2',
	        		    shopId : box.shopId,
	        		    csaCode:ChatBoxUtil.getUserFromJid(box.to||box.from),
	        		    userCode:ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid),
	        		    issueType:$('#issueType').val(),
	        		    gdsId : $('#gdsId').val(),
	        		    ordId : $('#ordId').val()}
	            $.eAjax({
	    	        url: GLOBAL.WEBROOT + '/mobilecusthistory/saveMsgHistory',
	    	        data: data,
	    	        datatype: 'json',
	    	        async: false,
	    	        success: function(returnInfo) {
	    	        	ChatBoxUtil.message.messagetype = returnInfo.messageType;
	    	        	ChatBoxUtil.message.body = returnInfo.body;
	    	        	messageValue = returnInfo.body;
	    	        	ChatBoxUtil.message.sessionId = returnInfo.sessionId;
	    	        	ChatBoxUtil.message.contentType = returnInfo.contentType;
	    	        	ChatBoxUtil.message.sendTime = returnInfo.beginDate;
	    	        	var str = JSON.stringify(ChatBoxUtil.message); 
	    	            var msg = $msg({  
	    	                to: returnInfo.to,//发送或回复消息   
	    	                from: returnInfo.from, //登录者ID
	    	                id : returnInfo.id,
	    	               // messagetype : returnInfo.messageType,
	    	                type: 'chat'  
	    	            }).c("body", null, str)//.c("session", null,returnInfo.sessionId);//带入本次会话的sessionId
	    	            ChatBoxUtil.connection.send(msg.tree());  
	    	            //用户发消息后，先清除原来的定时任务 
	    	            window.clearTimeout(ChatBoxUtil.timeOutId);
	    	            window.clearTimeout(ChatBoxUtil.timeOutRId);
	    	            
	    	            //重新开启定时任务：5分钟后，如果用户没有主动发消息，则调用服务，请求断开链接
	    	            ChatBoxUtil.timeOutId = window.setTimeout(ChatBoxUtil.disconnFromServ, ChatBoxUtil.outTime);
	    	            ChatBoxUtil.timeOutRId = window.setTimeout(ChatBoxUtil.timeOutRemind, ChatBoxUtil.outTime-120000);
	    	            //关闭弹出框
	    	            bDialog.closeCurrent();
	    	            $("#orderbody").hide();//隐藏订单区域
	    	            $("#chatBody").show();//显示聊天区域
	    	            
	    	        },
	    	        exception: function() {
	    	            eDialog.alert("消息发送请求异常，请检查网络");
	    	            this.sendable = true;
	    	            return;
	    	        }
	            });
	            // 展示消息
	           
	    		//var winItem = box;
	    		var chatMain = $('#chatMain'+box.chatId);
	    		//console.log(chatMain);
	    		var chatRecord = $(".chat-record", chatMain);//接收或者发送的消息区域
	    		var chartList = $('.chat-list', chatRecord);//ul的class
	    		//console.log(chartList);
	            var tplData = {
	            	ordImage: ordImage,
	            	ordId: ordId,
	        		createTime: time,
	        		price:price,
	        		chatSide:"chat-right",
	        		custPic : ChatBoxUtil.custPic
	            }
	            var html = template('recordOrder1', tplData);
	            chartList.append(html);
	            ChatBoxUtil.countWinHeight(box);
//	            var scrollHi = $(window).height() - $('.am-header').height() - $('#chat-input').height();
//	            var that = box;
//	          	chartList.imagesLoaded(function () {
//	          		that.chatScroll.refresh();
//	          		that.chatScroll.scrollTo(0,-chartList.height()+scrollHi, 100);
//	    	       });
	            
	   	    })
	     });
	    
	    
	    
	    
//	    $('.odritem').click(function () {
//	    	
//        });
	    
	 
	    /* ========= 订单 end ========== */
		//会话列表tab选中
//		if(this.tab){
//			this.tab.click(function(e){
//				chatBox.show();
//			});
//		}
		
	
//        var scrollClz='mCustomScrollbar';
//		  /* 判断 浏览器resize beign*/
//        $(window).on('resize.chatScroll',ChatBoxUtil.debounce(function(){
//            var useListScroll=$('.chat-uContainer');
//            if(useListScroll.hasClass(scrollClz)){
//            	ChatBoxUtil.setBoxHeight(useListScroll);
//                useListScroll.mCustomScrollbar('update');//实时更新滚动条当内容发生变化
//            }
//            $('.winItem','.chat-win').each(function(){
//            	var $win=$(this);
//                var chatRecord=$('.chat-record',$win);
//                var proPanel=$('.pro-panel',$win);
//                var proPanel=$('.ord-panel',$win);
//                var quePanel=$('.que-panel',$win);
//                var ordPanel=$('.ord-detail-panel',$win);
//                var msgPanelCont=$('.msgPanelCont',$win);
//                if(chatRecord.hasClass(scrollClz)&&!chatRecord.is(":hidden")){
//                   ChatBoxUtil.setBoxHeight(chatRecord,$win);
//                   chatRecord.mCustomScrollbar('update');
//                }
//                if(proPanel.hasClass(scrollClz)&&!proPanel.is(":hidden")){
//                	ChatBoxUtil.setBoxHeight(proPanel,$win);
//                    proPanel.mCustomScrollbar('update');
//                }
//                if(quePanel.hasClass(scrollClz)&&!quePanel.is(":hidden")){
//                	ChatBoxUtil.setBoxHeight(quePanel,$win);
//                	quePanel.mCustomScrollbar('update');
//                }
//                if(ordPanel.hasClass(scrollClz)&&!ordPanel.is(":hidden")){
//                	ChatBoxUtil.setBoxHeight(ordPanel,$win);
//                	ordPanel.mCustomScrollbar('update');
//                }
//               
//                msgPanelCont.each(function(){
//                	  if($(this).hasClass(scrollClz)&&!$(this).is(":hidden")){
//                      	ChatBoxUtil.setBoxHeight($(this),$win);
//                      	$(this).mCustomScrollbar('update');
//                      }
//                })
//              
//            });
//       },200));
      /* 判断 浏览器resize end*/

       
	
	},
	/**
	 * domInfo:{messageType,uName,custPic}
	 * record: MessageHistoryRespVO
	 * side: left or right .. chat-right
	 * 
	 * @returns {String}
	 */
	_createMessageDom : function(domInfo, record, side){
		var html,tplData,msg;
		switch(domInfo.messageType){
			case 'gds':
				msg = $.parseJSON(record.body);
				tplData = {
		    		uName: domInfo.uName,
		    		time: ebcDate.dateFormat(record.beginDate, "yyyy-MM-dd hh:mm:ss"),
		    		custPic : ChatBoxUtil.custPic,
		        	url : $("#gdsDetailUrl").val() + msg.gdsId +"-",
		    		gdsId : msg.gdsId,
		    		gdsImage : msg.gdsImage,
		    		gdsName : msg.gdsName,
		    		price : msg.price,
		    		chatSide : side
		        };
				html = template('recordGds1', tplData);
				break;
			case 'order':
				msg = $.parseJSON(record.body);
				tplData = {
		    		uName: domInfo.uName,
		    		time: ebcDate.dateFormat(record.beginDate, "yyyy-MM-dd hh:mm:ss"),
		    		custPic : ChatBoxUtil.custPic,
		    		ordId : msg.ordId,
		    		price : msg.price,
		    		ordImage : msg.ordImage,
		    		createTime : ebcDate.dateFormat(isNaN(Number(msg.createTime))?msg.createTime:new Date(Number(msg.createTime)), "yyyy-MM-dd hh:mm:ss"),
//		    		url : "javascript:orderDetail('"+ msg.ordId +"')"
		    		chatSide : side
		        }
				html = template('recordOrder1', tplData);
				break;
			default:
				html = "";
		}
		return html;
	},
	/**
	 * 
	 * @param p2 MessageHistoryRespVO
	 *
	 * @returns {String}  历史面板中的消息
	 */
	//_createMessageHtml : function(p2){},
	
	//浏览商品
	_createGdsMsg:function(){
		var chatBox = this;
		var shopId = chatBox.shopId;
		$.eAjax({
			url: GLOBAL.WEBROOT + '/customerservice/browsegoodsmsg',
			data: {
				pageNumber:ChatBoxUtil.pageNum,
	        	pageSize:ChatBoxUtil.pageSize,
	        	shopId:shopId,	
			},
			datatype: 'json',
			async: false,
			success: function(returnInfo) {
				var winItem = chatBox.box;
				var chatMain = $('#gdsbody',winItem);
				var chartList = $("#customer-gds", chatMain);
				
				$.each(returnInfo, function(p1, p2) {
					tplData = {
							gdsMsg : p2.gdsDesc,
				    		price : (p2.defaultPrice*0.01).toFixed(2),
				    		gdsImage : p2.imageUrl,
				    		gdsId: p2.id,
				    		gdsName:p2.gdsName
				        }
						html = template('recordGds', tplData);
						$(html).appendTo(chartList);
						chartList.imagesLoaded(function () {
							chatBox.gdsScroll.refresh();
			  	        });
				});
			}
		})
	},
	
	//订单信息
	_createOrderMsg:function(){
		var chatBox = this;
		var shopId = chatBox.shopId;
		$.eAjax({
			url: GLOBAL.WEBROOT + '/customerservice/ordermsg',
			data: {
				pageNumber:ChatBoxUtil.pageNum,
	        	pageSize:ChatBoxUtil.pageSize,
	        	shopId:shopId,	
			},
			datatype: 'json',
			async: false,
			success: function(returnInfo) {
				var winItem = chatBox.box;
				var chatMain = $('#orderbody',winItem);
				var chartList = $("#customer-order", chatMain);
				
				$.each(returnInfo, function(p1, p2) {
					tplData = {
							createTime: ebcDate.dateFormat(p2.orderTime, "yyyy-MM-dd hh:mm:ss"),
				    		ordId : p2.orderId,
				    		price : (p2.realMoney*0.01).toFixed(2),
				    		ordImage : p2.ord01102Resps[0].picUrl,
				    		timeDto : p2.orderTime,
				        }
						html = template('recordOrder', tplData);
						$(html).appendTo(chartList);
						chartList.imagesLoaded(function () {
							chatBox.ordScroll.refresh();
			  	        });
				});
			}
		})
	},
	
	
	//最近会话记录
	_createHistoryMsg:function(){
		var chatBox = this;
		var userCode = $("#uNameForLogout").val();
		var to = chatBox.to;
		var csaCode = ChatBoxUtil.getUserFromJid(to);
		var staffCode = $("#staffCode").val();
		var shopId = this.shopId;
        $.eAjax({
	        url: GLOBAL.WEBROOT + '/mobilecusthistory/getMessageHistory',
	        data: {
	        	userCode:userCode,
	        	csaCode:csaCode,
//	        	messageType:'msg',
	        	pageNumber:ChatBoxUtil.pageNum,
	        	pageSize:ChatBoxUtil.pageSize,
	        	shopId:shopId,
	        	status:'20'
	        },
	        datatype: 'json',
	        async: false,
	        success: function(returnInfo) {
	        	returnInfo = returnInfo.reverse();//after追加的，不需要反转
	        	var winItem = chatBox.box;
  	    		var chatMain = $('#chatMain'+userCode+'WEB',winItem);
  	    		var chatRecord = $(".chat-record", chatMain);
  	            var chartList = $('.chat-list', chatRecord);
	        	$.each(returnInfo, function(p1, p2) {
	        		if(ChatBoxUtil.getUserFromJid(p2.from) == ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid)){
	        	            var tplData = {
	        	            		msg: p2.body,
	        	            		uName: staffCode,
	        	            		time: ebcDate.dateFormat(p2.beginDate, "yyyy-MM-dd hh:mm:ss"),
	        	            		custPic: ChatBoxUtil.custPic
	        	            }
	        	            var html = template('selfRecord', tplData);
	        	            if(p2.messageType != "msg"){
	        	            	var domInfo = {
	        	            			messageType: p2.messageType,
	        	            			uName: staffCode,	
	        	            			custPic: ChatBoxUtil.custPic
	        	            	};
	        	            	html = chatBox._createMessageDom(domInfo, p2, "chat-right");
	        	            }
	        	            $(chartList).append(html);
	        		}else{
	        			
		                var tplData = {
		            		msg: p2.body,
		            		uName:ChatBoxUtil.ServName,
		            		time: ebcDate.dateFormat(p2.beginDate, "yyyy-MM-dd hh:mm:ss"),
		            		servPic: ChatBoxUtil.servPic
		                }
		                var html = template('record', tplData);
		                if(p2.messageType != "msg"){
		                	var domInfo = {
		                			messageType: p2.messageType,
		                			uName: ChatBoxUtil.ServName,	
		                			custPic: ChatBoxUtil.servPic
		                	};
		                	html = chatBox._createMessageDom(domInfo, p2, "");
		                }
		                $(chartList).append(html);
	        		}
	        	});
	        	ChatBoxUtil.countWinHeight(chatBox);
	        	
	        },
	        exception: function() {
	            eDialog.alert("面板历史消息加载异常");
	            return;
	        }
        });
		
		
		
	},
	
	/**
	 * 聊天记录 （历史）
	 */
	_createChatHis: function(){
		var chatBox = this;
		var userCode = $("#uNameForLogout").val();
		var to = chatBox.to;
		var csaCode = ChatBoxUtil.getUserFromJid(to);
		var staffCode = $("#staffCode").val();
		var shopId = chatBox.shopId;
        $.eAjax({
	        url: GLOBAL.WEBROOT + '/mobilecusthistory/getMessageHistory',
	        data: {
	        	userCode:userCode,
	        	//csaCode:csaCode,
//	        	messageType:'msg',
	        	pageNumber:ChatBoxUtil.pageNum,
	        	pageSize:ChatBoxUtil.pageSize,
	        	shopId:shopId,
	        	status:'20'
	        },
	        datatype: 'json',
	        async: false,
	        success: function(returnInfo) {
	        	returnInfo = returnInfo.reverse();
	        	var winItem = chatBox.box;
  	    		var chatMain = $('#historyMain'+userCode+'WEB',winItem);
  	    		var chatRecord = $(".chat-record", chatMain);
  	            var chartList = $('.chat-list', chatRecord);
  	            $(chartList).empty();
  	            var msgbody=$('<li class="his-tip ser-more" style="display:none;">查看更多信息</li>');
  	            chartList.append(msgbody);
  	            ChatBoxUtil.historyflag = 1;
	        	$.each(returnInfo, function(p1, p2) {
	        		if(ChatBoxUtil.getUserFromJid(p2.from) == ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid)){
	        	            var tplData = {
	        	            		msg: p2.body,
	        	            		uName: staffCode,
	        	            		time: ebcDate.dateFormat(p2.beginDate, "yyyy-MM-dd hh:mm:ss"),
	        	            		custPic: ChatBoxUtil.custPic
	        	            }
	        	            var html = template('selfRecord', tplData);
	        	            if(p2.messageType != "msg"){
	        	            	var domInfo = {
	        	            			messageType: p2.messageType,
	        	            			uName: staffCode,	
	        	            			custPic: ChatBoxUtil.custPic
	        	            	};
	        	            	html = chatBox._createMessageDom(domInfo, p2, "chat-right");
	        	            }
	        	            $(html).appendTo(chartList);
	        		}else{
	        			
		                var tplData = {
		            		msg: p2.body,
		            		uName:ChatBoxUtil.ServName,
		            		time: ebcDate.dateFormat(p2.beginDate, "yyyy-MM-dd hh:mm:ss"),
		            		servPic: ChatBoxUtil.servPic
		                }
		                var html = template('record', tplData);
		                if(p2.messageType != "msg"){
		                	var domInfo = {
		                			messageType: p2.messageType,
		                			uName: ChatBoxUtil.ServName,	
		                			custPic: ChatBoxUtil.servPic
		                	};
		                	html = chatBox._createMessageDom(domInfo, p2, "");
		                }
		                $(html).appendTo(chartList);
	        		}
	        	});
	        	chartList.imagesLoaded(function () {
	       	       chatBox.hisScroll.refresh();
	       	       var scrollHi = $(window).height() - $('.am-header').height();// 
	       	       chatBox.hisScroll.scrollTo(0,-chartList.height()+scrollHi, 100);
	  	        });
	        },
	        exception: function() {
	            eDialog.alert("面板历史消息加载异常");
	            return;
	        }
        });
	},
	
	
		
}
/* ========= ChatBox end ========== */

/* TODO ========= ChatBoxUtil start ========== */
/**
 *  会话窗口工具
 */
var ChatBoxUtil = {
	constants: {
		SERV: "serv",
		CUST: "cust",
		baseH: 0
	},
	message:{
		body:"",
		messagetype:"",
		sessionId:"",
		contentType:"",
		sendTime:""
	},
	ServName:"",
	csaCode : "",
	defPass:"123456",
	ue:null,
	connection: "",
	custPic : "",
	servPic : "",
	clearInterval : "",
	connected: false,
	BOSH_SERVICE: "",
	pageNum : 1,
	pageSize : 10,
	historyflag:0,
	model: "",//界面模型
	list: [],
	first: null,
	last: null,
	timeOutId:"",//定时任务id
	outTime:10*60*1000,//会话超时时间10分钟
	timeOutRId:"",//会话超时提示ID
	timeshowId:"",
	conagain:false,//重新链接
	getSize: function(){
		return this.list.length;
	},
	hasChatBox: function(chatId){
		if(!chatId) throw new error("chatId is missing!");
		for(var chat in this.list){
			if(chat.chatId ==  chatId){
				return true;
			}
		}
		return false;
	},
	getChatIdFromJID: function(jid){
		if(!jid) return null;
		return Strophe.getNodeFromJid(jid) + Strophe.getResourceFromJid(jid);
	},
	getUserFromJid : function(jid){
		if(!jid) return null;
		return Strophe.getNodeFromJid(jid);
	},
	getChatBoxFromJID: function(jid){
		var chatId = this.getChatIdFromJID(jid);
		var list = this.list;
		if(list.length>0){
			for(var chat in list){
				if(list[chat].chatId == chatId){
					return list[chat];
				}
			}
		}
		
		return null;
	},
	getCurrent: function(){
		var list = this.list;
		if(list.length>0){
			for(var chat in list){
				if(list[chat].current){
					return list[chat];
				}
			}
		}
		return null;
	},
	isEmpty: function(){
		return !(this.list.length > 0);
	},
	setBoxHeight:function(domObj,winItem){
		 var calHeight = $(window).height()*.85- ChatBoxUtil.constants.baseH-$('.chat-bar').outerHeight();
		 if(domObj.hasClass('chat-record')){
			 calHeight = calHeight - $('.chat-header',winItem).outerHeight()
                         - $('.chat-in',winItem).outerHeight()
                         - 10;
		 }
		 else if(domObj.hasClass('msgPanelCont')){
			 calHeight = calHeight  - $('.msg-head',winItem).outerHeight()
                         - $('#msgTab',winItem).outerHeight()
                         - 60;
		 }
		 else if(domObj.hasClass('pro-panel')){
			 calHeight = calHeight - $('.chat-side .nav-tabs',winItem).outerHeight() - 30;
		 }
		 else if(domObj.hasClass('ord-detail-panel')){
			 calHeight = calHeight - $('.chat-side .nav-tabs',winItem).outerHeight() - 30;
		 }
		 else if(domObj.hasClass('que-panel')){
			 calHeight = calHeight - $('.chat-side .nav-tabs',winItem).outerHeight() - 30;
		 }
		 else if(domObj.hasClass('chat-uContainer')){
			 calHeight = calHeight - $('.chat-nav .tit').outerHeight() +10;
		 }
		 
		 domObj.height(calHeight);
	},
	debounce:function(func, wait, immediate){
		 var timeout;
         return function () {
             var context = this, args = arguments;
             var later = function () {
                 timeout = null;
                 if (!immediate) func.apply(context, args);
             };
             var callNow = immediate && !timeout;
             clearTimeout(timeout);
             timeout = setTimeout(later, wait);
             if (callNow) func.apply(context, args);
         };
	},
	browserMsgTip: 0,//缺省为0次
	browserMsgTimer: null,
	setBrowserMsgTip: function(num){
		if(!num) num = 20;
		this.browserMsgTip = num;
	},
	/**
	 * 浏览器标签页消息提示
	 */
	enableBrowserMsgTip: function(){
		// 各种浏览器兼容
	    var hidden, state, visibilityChange;
	    if (typeof document.hidden !== "undefined") {
	        hidden = "hidden";
	        visibilityChange = "visibilitychange";
	        state = "visibilityState";
	    } else if (typeof document.mozHidden !== "undefined") {
	        hidden = "mozHidden";
	        visibilityChange = "mozvisibilitychange";
	        state = "mozVisibilityState";
	    } else if (typeof document.msHidden !== "undefined") {
	        hidden = "msHidden";
	        visibilityChange = "msvisibilitychange";
	        state = "msVisibilityState";
	    } else if (typeof document.webkitHidden !== "undefined") {
	        hidden = "webkitHidden";
	        visibilityChange = "webkitvisibilitychange";
	        state = "webkitVisibilityState";
	    } 
	    if(typeof hidden === "undefined") return;//不支持

	    var title = document.title;
	    var self = this;
	    var onVisibilityChange = function(){
	    	if(document[state] == 'hidden'){
	    		self.browserMsgTimer = setInterval(function() {  
		    		if(self.browserMsgTip!=0){
		    			if (self.browserMsgTip % 2 == 0) {  
			                document.title = "【新消息】" + title;
			            } else {  
			                document.title = "【　　　】" + title;
			            };
			            self.browserMsgTip--;
		            }else{
		            	if(document.title != title){
		            		document.title = title;
		            	}
		            }
		        }, 500);
	        }else{
	        	clearInterval(self.browserMsgTimer);
	        	document.title = title;
	        	self.browserMsgTip = 0;
	        }
	    }
	    //监听
	    document.addEventListener(visibilityChange, onVisibilityChange, false); 
	},
	/**
	 * 主动断开与服务器的连接
	 */
	disconnFromServ: function(){
		$.eAjax({
	        url: GLOBAL.WEBROOT + '/customerservice/disconn',
	        data: {csaCode:$("#csaCodeForLogout").val(),shopId:$("#shopIdForLogout").val(),ofStaffCode:$("#uNameForLogout").val(),sessionId:$('#sessionId').val()},
	        datatype: 'json',
	        async: false,
	        success: function(returnInfo) {
	        	
	        	if (returnInfo.resultFlag == "ok") {
	        		/*//通知客服下线
	        		 var messageValue ="该会员已经离开";
	        		 var msg = $msg({  
	        	         to: ChatBoxUtil.csaCode,//发送或回复消息   
	        	         from: ChatBoxUtil.connection.jid, //登录者ID
	        	         id : "inform",
	        	         messagetype : "inform",//通知类型
	        	         type: 'chat'  
	        	     }).c("body", null, messageValue).c("session", {id: "inform"});//带入本次会话的sessionId
	        	     ChatBoxUtil.connection.send(msg.tree());*/
    	        	 var presence = $pres({type: "unavailable"}).c("status").t("Unavailable");//$pres().c("show").t("xa").up().c("status").t("down the rabbit hole!");
    	        	 ChatBoxUtil.connection.send(presence.tree());
    	        	 ChatBoxUtil.connection.flush();
	        		 eDialog.alert('对不起！由于长时间没有发送消息，您已断开与客服的连接。',function(){
	        			 window.history.back(-1);
	        			 return false;
	        			
	        		});
	        	}
	        }
        });
	},
	timeOutRemind:function(){
		
		ChatBoxUtil.showNotice('您已经8分钟未发言，如果继续不发言2分钟后会自动断线。',true);
	},
	chatTimeShow:function(chatBox){
		
		chatBox.showTime = 1 ;
	},
	/**
	 * 排队
	 */
	queueUp : function(){
		$.eAjax({
	        url: GLOBAL.WEBROOT + '/customerservice/getHotlineQueue',
	        data: {businessType:$('#issueType').val(),shopId:$("#shopIdForLogout").val(),ofStaffCode:$("#uNameForLogout").val(),custLevelCode:$('#custLevel').val(),orderId:$('#ordId').val(),goodsId:$('#gdsId').val()},
	        datatype: 'json',
	        async: false,
	        success: function(returnInfo) {
	        	var waitCount = returnInfo.waitCount;
	        	var notice = $('#notice');
	        	
	        	
	       	 if(waitCount=='-99'){
	       		 /*判断是否第一次加载*/
	       	   if($('#waitCount').val()=='-99'){
      		    	ChatBoxUtil.showNotice('对不起，客服还没有上线，请在正常工作时间内联系客服人员.',false);
      		    	setTimeout("ChatBoxUtil.queueUp()",5000);
      		    	return false;
      		    }
	       		    ChatBoxUtil.ServName = "人卫商城在线客服";
	       		    $('#waitCount').val(waitCount);
	       		 
	            	//初始化界面
	            	var chatBean = {
	            		chatId: ChatBoxUtil.getChatIdFromJID(ChatBoxUtil.connection.jid),
	            		model: ChatBoxUtil.constants.CUST,
	            		sessionId : '',
	            		to: '',
	            		isinit : false,
	            		chatScroll: null,
	            		shopId:returnInfo.shopId
	            	}
	            	try {
	            		new ChatBox(chatBean);
	            		ChatBoxUtil.showNotice('对不起，客服还没有上线，请在正常工作时间内联系客服人员.',false);
	            		$('#sendMsg').attr("disabled",true);
	            	} catch(e) {
	            		if(window.console && window.console.log){
	            			console.error(e);
	            		}
	            	}
	            	setTimeout("ChatBoxUtil.queueUp()",5000);
	 			return false;
	 	     }else if(waitCount!=0){
	 	    	 if($('#waitCount').val()>0){
	 	    		    ChatBoxUtil.showNotice("您的前面还有"+waitCount+"位正在等待，请稍等",false);
	      		    	setTimeout("ChatBoxUtil.queueUp()",5000);
	      		    	return false;
	      		    }
	 	    	 $('#waitCount').val(waitCount);
	 	    	   //初始化界面
	            	var chatBean = {
	            		chatId: ChatBoxUtil.getChatIdFromJID(ChatBoxUtil.connection.jid),
	            		model: ChatBoxUtil.constants.CUST,
	            		sessionId : '',
	            		to: '',
	            		isinit : false,
	            		chatScroll: null,
	            		shopId:returnInfo.shopId
	            	}
	            	try {
	            		new ChatBox(chatBean);
	            		ChatBoxUtil.showNotice("您的前面还有"+waitCount+"位正在等待，请稍等",false);
	            		$('#sendMsg').attr("disabled",true);
	            	} catch(e) {
	            		if(window.console && window.console.log){
	            			console.error(e);
	            		}
	            	}
	 	    	   
	    			setTimeout("ChatBoxUtil.queueUp()",5000);
	        	}else{
	        		 $('#waitCount').val('');
	        		 $('#sendMsg').attr("disabled",false);
	        		ChatBoxUtil.csaCode = returnInfo.csaCode;
	        		$("#csaCodeForLogout").val(returnInfo.csaCode);
	        		$('#sessionId').val(returnInfo.sessionId);
	        		  //初始化界面
	        		
	            	var chatBean = {
	            		chatId: ChatBoxUtil.getChatIdFromJID(ChatBoxUtil.connection.jid),
	            		model: ChatBoxUtil.constants.CUST,
	            		sessionId : returnInfo.sessionId,
	            		to: returnInfo.csaCode+returnInfo.ofserver,
	            		isinit : true,
	            		chatScroll: null,
	            		shopId:returnInfo.shopId
	            	}
	            	try {
	            		ChatBoxUtil.ServName = returnInfo.serName;
	            		//ChatBoxUtil.ServName = "人卫商城在线客服";
	            		new ChatBox(chatBean);
	            		/*设置超时时间*/
	            		if($('#sessionTime').val()!=''&&$('#sessionTime').val()!=null){
	            			ChatBoxUtil.outTime = $('#sessionTime').val();
	            		}
	            		//获取离线消息
	            		var chat = ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid);
	                    ChatBoxUtil.fetchHistoryMsg(returnInfo.shopId,returnInfo.csaCode,chat);
	            		inform(returnInfo.sessionId,chatBean.to);//通知客服对接
		        		ChatBoxUtil.showNotice("人民卫生出版社"+ChatBoxUtil.ServName+"将为您服务",true);
	            	} catch(e) {
	            		if(window.console && window.console.log){
	            			console.error(e);
	            		}
	            	}
	            	return waitCount;
	        	}
	        }
		});
	},
	getStaffCodeByUserCode :function(chatId){
		var user = chatId.substring(0,5);
		var csa = chatId.substring(0,4);
		if(user=='user_'){
			return chatId.substring(5,chatId.length);
		}else if(csa == 'csa_'){
			return chatId.substring(4,chatId.length);
		}
		return chatId
	},
	showNotice :function(e,f){
		var notice = $('.notice');
		$(notice).find('p').html(e);
		if(f){
			$(notice).show(300).delay(5000).hide(300); 
		}else{
			$(notice).show();
		}
		
		$(notice).find('#resrt').click(function(){
			ChatBoxUtil.queueUp();
		});
	},
	/**
	 * 获取与相关客服的离线消息
	 * @param csaCode
	 */
	fetchHistoryMsg : function(shopId,csaCode,chatBox){
		if(!shopId) return;
		if(!csaCode) return;
		var chatBox = this.getChatBoxFromJID(ChatBoxUtil.connection.jid) || chatBox;
		if(chatBox){
			var shopId = chatBox.shopId;
			var userCode = ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid);
			var iq = $iq({  
                type: 'get'
            }).c("offlinemsg", {"xmlns":Strophe.NS.OFFLINEMSG_IQ,"shopId":shopId,"userCode":userCode, "csaCode":csaCode}, "");
			ChatBoxUtil.connection.sendIQ(iq.tree(),
					function(e){
		            },
		            function(e){
		            }    
            ); 
		}
	},
	/**
	 * 当前用户退出
	 */
	signout : function(){
    	ChatBoxUtil.connection.disconnect(); 
	},
	wndUnload : function(){
		var self = this;
		window.onbeforeunload = function(){
			self.signout();
		}
//		window.addEventListener("popstate", function(e) {  
//			self.signout();
//	    }, false);
		
	},
	
	uploadCallBack : function(returnInfo) {
		/** 上传成功，聊天框显示该图片,图片url发送到客服端 */
		if (returnInfo.success == "ok") {
			
			var chatBox1 = this.getChatBoxFromJID(ChatBoxUtil.connection.jid);
			var imagePath = returnInfo.map.imagePath;
			var imageName = returnInfo.map.imageName;
			if(ChatBoxUtil.connected) {
				if(!chatBox1.to && !chatBox1.from) {  
	            	//eDialog.alert("没有联系人！");
	            	ChatBoxUtil.showNotice('您好，客服还没有上线，请在正常工作时间内联系客服人员.',true);
	    			return false;
	            }else{
	            	var sessionid = $('#sessionId').val();
	            	chatBox1.setSessionId(sessionid);
	            	var flag = true;
	                $.eAjax({
	        	        url:GLOBAL.WEBROOT + '/mobilecusthistory/getSessionByone',
	        	        data: {id:sessionid},
	        	        datatype: 'json',
	        	        async: false,
	        	        success: function(returnInfo) {
	        	        
	                           if(returnInfo.status!='1'){
	                        	   //ChatBoxUtil.showNotice('与当前客服的会话已结束，重新发起咨询请点击'+'<span style="color:blue" id="resrt">确认</span>'+'按钮',false);
	                        	   flag =  false;
	                        	   eDialog.alert('客服关闭会话，会话已结束，请您谅解。',function(){
	          	        			 window.history.back(-1);
	          	        			 return false;
	          	        			
	                        	   });
	                           }        	        	
	        	        
	        	            }
	                     });
	                if(!flag){
	                	return false;
	                }
	                // 发送消息 
	                var msghtml = "<img src="+imagePath+" alt="+imageName+">";
	                var messageValue = msghtml;
	                
	                chatBox1.sendable = false; //消息发送中
	                /**
	                 * 消息uuid生成
	                 */
	                var csa = chatBox1.to.split('/');
	                var issueType = $('#issueType').val();
	                // 展示消息
	                var winItem = chatBox1.box;
	        		var chatMain = $('#chatMain'+chatBox1.chatId,winItem);
	        		var chatRecord = $(".chat-record", chatMain);
	               // var chartList = $('.chat-list', chatRecord);
	        		var charList = $('#chatScorllDiv').find('.chat-list');
	                var tplData = {
	            		msg: messageValue,
	            		custPic: ChatBoxUtil.custPic
	                }
	                var html = template('selfRecord', tplData);
	                charList.append(html);
	                //刷新上传组件
	                ChatBoxUtil.countWinHeight(chatBox1);
	                //后端保存
	                var data = {to:chatBox1.to||chatBox1.from,
	            		    toResource:csa[1],
	            		    from:ChatBoxUtil.connection.jid,
	            		    body:messageValue,
	            		    sessionId:chatBox1.sessionId,
	            		    messageType:'msg',
	            		    contentType:'1',
	            		    shopId : chatBox1.shopId,
	            		    csaCode:ChatBoxUtil.getUserFromJid(chatBox1.to||chatBox1.from),
	            		    userCode:ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid),
	            		    issueType:$('#issueType').val(),
	            		    gdsId : $('#gdsId').val(),
	            		    ordId : $('#ordId').val()}
	                $.eAjax({
	        	        url: GLOBAL.WEBROOT + '/mobilecusthistory/saveMsgHistory',
	        	        data: data,
	        	        datatype: 'json',
	        	        async: false,
	        	        success: function(returnInfo) {
	        	        	ChatBoxUtil.message.messagetype = returnInfo.messageType;
	        	        	ChatBoxUtil.message.body = returnInfo.body;
	        	        	messageValue = returnInfo.body;
	        	        	ChatBoxUtil.message.sessionId = returnInfo.sessionId;
	        	        	ChatBoxUtil.message.contentType = returnInfo.contentType;
	        	        	ChatBoxUtil.message.sendTime = returnInfo.beginDate;
	        	        	var str = JSON.stringify(ChatBoxUtil.message); 
	        	            var msg = $msg({  
	        	                to: returnInfo.to,//发送或回复消息   
	        	                from: returnInfo.from, //登录者ID
	        	                id : returnInfo.id,
	        	               // messagetype : returnInfo.messageType,
	        	                type: 'chat'  
	        	            }).c("body", null, str)//.c("session", null,returnInfo.sessionId);//带入本次会话的sessionId
	        	            ChatBoxUtil.connection.send(msg.tree());  
	        	            //用户发消息后，先清除原来的定时任务 
	        	            window.clearTimeout(ChatBoxUtil.timeOutId);
	        	            window.clearTimeout(ChatBoxUtil.timeOutRId);
	        	            
	        	            //重新开启定时任务：5分钟后，如果用户没有主动发消息，则调用服务，请求断开链接
	        	            ChatBoxUtil.timeOutId = window.setTimeout(ChatBoxUtil.disconnFromServ, ChatBoxUtil.outTime);
	        	            ChatBoxUtil.timeOutRId = window.setTimeout(ChatBoxUtil.timeOutRemind, ChatBoxUtil.outTime-120000);
	        	        },
	        	        exception: function() {
	        	            eDialog.alert("消息发送请求异常，请检查网络");
	        	            this.sendable = true;
	        	            return;
	        	        }
	                });
	                
	                
	            }
			}else{
				eDialog.alert("请先登录！");
			}
		} else {
			eDialog.error(returnInfo.message);
		} 
	},
	//加载图片的表情
	EmotionImg : function (opt) {
        var defalut = {
            serverUrl: '',
            title: [],
            pageSize: 24,
            length: 47,
            contanier: $('.am-slides')
        };
        var str='';
        var chidContanier = '';
        defalut = $.extend(defalut,opt);
        for(var i = 0; i < defalut.length; i++){
            if( i % defalut.pageSize == 0){
                str? chidContanier.append(str) : '';
                str = '';
                chidContanier = $('<li class="item"></li>');
                defalut.contanier.append(chidContanier);

            }
            var j = i+1;
            var k = j<10? '0'+j:j;
            str += '<i class="em'+k+'" title="'+defalut.title[i]+'" data-imgurl="'+defalut.serverUrl+'i_f'+k+'.gif"></i>';

            if( i == defalut.length-1){
                chidContanier.append(str);
            }
        }
        return this;


    },
    //计算浏览器窗口高度
    countWinHeight : function (chatBox){
    	var box = chatBox;
		var chatMain = $('#chatMain'+box.chatId);
		var chatBody = $('#chatBody');
    	setTimeout(function(){
    		var scrollHi = $(window).height() - $('.am-header',chatBody).outerHeight() - $('#chat-input',chatBody).outerHeight();// 
    		var chatRecord = $(".chat-record", chatMain);//接收或者发送的消息区域
    		var chartList = $('.chat-list', chatRecord);//ul的class
    		chatRecord.height(scrollHi);
    		chartList.imagesLoaded(function(){
    			box.chatScroll.refresh();
    			if(chartList.height()>scrollHi){
    				box.chatScroll.scrollTo(0,-chartList.height()+scrollHi, 100);
    			}
    	  		
    		})
    	},0)
    	
    }
	
}
/* ========= ChatBoxUtil end ========== */

/**
 * 开始连接
 */
var initConnection = function(userInfo){
		
			ChatBoxUtil.connection = new Strophe.Connection(ChatBoxUtil.BOSH_SERVICE);
			ChatBoxUtil.connection.connect(userInfo.uName,userInfo.uPass,onConnect);//登录
			
		}

/**
 * 连接
 */
var onConnect = function(status) {
	switch (status) {
	case Strophe.Status.CONNECTING:
		break;
	case Strophe.Status.CONNFAIL:
	case Strophe.Status.AUTHFAIL:
	case Strophe.Status.ERROR:
		
		console.log(status+" ERROR");
//		eDialog.alert('连接失败，当前咨询已失效， 请点击确定重新咨询!',function(){
//			location.reload();
//		});
		break;
	case Strophe.Status.CONNECTED:
		if(ChatBoxUtil.conagain){
			connectAgainSuccess();//DISCONNECTED后再次链接
		}else{
			connectSuccess();
		}
		
		console.log(status+" CONNECTED");
		ChatBoxUtil.connected = true;
		break;
	case Strophe.Status.DISCONNECTED:
		
		console.log(status+" DISCONNECTED");
//		eDialog.alert('会话已断开，当前咨询已失效， 请点击确定重新咨询!',function(){
//			location.reload();
//		});
		
		
		// XMPP连接  
		ChatBoxUtil.connection = null;
		// 当前状态是否连接  
		ChatBoxUtil.connected = false;
		//重新链接标志
		ChatBoxUtil.conagain = true;
		 var uName2 = $("#uName").val();
		 var PASS2 = ChatBoxUtil.defPass;
		 initConnection({uName: uName2, uPass: PASS2});
		break;
	case Strophe.Status.DISCONNECTING:
		break;
	}
}
/**
 * 连接成功
 */
var connectSuccess = function(){
	ChatBoxUtil.connection.addHandler(onMessage, null, 'message', null, null, null); // 消息
	var presence = $pres().c("show").t("chat").up().c("status").t("I am online!");
	ChatBoxUtil.connection.send(presence.tree()); // 发送在线请求
	//等待人数
	waitCountError();
	
}
//再次链接
var connectAgainSuccess = function(){
	var userCode = $("#uNameForLogout").val();
	var shopId = $("#shopId").val();
	var sessionId = $("#sessionId").val();
	ChatBoxUtil.connection.addHandler(onMessage, null, 'message', null, null, null); // 消息
	var presence = $pres().c("show").t("chat").up().c("status").t("I am online!").up().c("userCode").t(userCode).up().c("shopId").t(shopId).up().c("sessionId").t(sessionId);
	ChatBoxUtil.connection.send(presence.tree()); // 发送在线请求
}

var waitCountError = function(){
	 
	   waitCount = ChatBoxUtil.queueUp();
	}

var onMessage = function(msg){
	// 解析出<message>的from、type属性，以及body子元素  
    var from = msg.getAttribute('from');  
    var to = msg.getAttribute('to')
    var type = msg.getAttribute('type');  
    var id = msg.getAttribute('id');  
    var elems = msg.getElementsByTagName('body');
    var body = "";
    if (type == "chat" && elems.length > 0) {
    	body = elems[0];
    	body = Strophe.getText(body);
    	body = HtmlUtil.htmlDecode(body);
    	var json = $.parseJSON(body);
    	var msgInfo = {
    		from: from,
    		to: to,
    		id: id,
    		type: type,
    		msg: json.body,
    		messageType:json.messagetype,
    		contentType:json.contentType,
    		sessionId: json.sessionId
    	};
    	handleMessage(msgInfo);
    }else if(type="headline" && elems.length > 0){
    	body = elems[0];
    	body = Strophe.getText(body);
    	body = HtmlUtil.htmlDecode(body);
    	var json = $.parseJSON(body);
    	var msgInfo = {
    		from: from,
    		to: to,
    		id: id,
    		type: type,
    		bizcode: json.bizcode,
    		body:json
    	};
    	handleHeadline(msgInfo);
    }
    
    return true;
}

/**
 * handleHeadline 处理业务通知 
 * body中的对象包含 "bizcode"
 */
function handleHeadline(msgInfo){
	switch(msgInfo.bizcode){
		case "transferuser"://用户待接收
			var sessionId = msgInfo.body.sessionId;
			var csaCode = msgInfo.body.csaCode;
			var hotlinePerson = msgInfo.body.hotlinePerson;
			var greeting = msgInfo.body.greeting;
			
			var chatBox = ChatBoxUtil.getChatBoxFromJID(ChatBoxUtil.connection.jid);
			var refCsaCode = chatBox.getFrom();
			var to = csaCode+refCsaCode.substring(refCsaCode.indexOf("@"));
			//1.更新chatBox
			chatBox.setSessionId(sessionId);
			chatBox.setFrom(to);
			chatBox.setTo(to);
			ChatBoxUtil.csaCode = csaCode;
			$("#csaCodeForLogout").val(csaCode);
			
			$('#sessionId').val(sessionId);
			//2.更新boxutil
			ChatBoxUtil.ServName = hotlinePerson;
			//3.更新html
			//$("#hotlineSerName", chatBox.box).html("-"+hotlinePerson);
			//4.问候语
			// 展示消息
			ChatBoxUtil.showNotice(greeting,true,chatBox.box);
			
			break;
		default:
			//do nothing
			break;
	}
}

var handleMessage = function(msgInfo){
	var chatBox = null;
	//消息同步
//	if(msgInfo.from == ChatBoxUtil.connection.jid){
//		chatBox = ChatBoxUtil.getChatBoxFromJID(ChatBoxUtil.connection.jid);
//		if(chatBox){
//			chatBox.syncSendMessage(msgInfo);
//		}
//		
//		return;
//	}
	
	if(ChatBoxUtil.model == ChatBoxUtil.constants.SERV){
		chatBox = ChatBoxUtil.getChatBoxFromJID(msgInfo.from);
		if(!chatBox){
			var chatBean = {
				chatId: ChatBoxUtil.getChatIdFromJID(msgInfo.from),
				model: ChatBoxUtil.constants.SERV,
				sessionId: msgInfo.sessionId,
	            msgId : msgInfo.id
			}
			try {
				chatBox = new ChatBox(chatBean);
			} catch(e) {
				if(window.console && window.console.log){
					console.error(e);
				}
			}
		}
	}else if(ChatBoxUtil.model == ChatBoxUtil.constants.CUST){
		chatBox = ChatBoxUtil.getChatBoxFromJID(ChatBoxUtil.connection.jid);
		chatBox.msgId = msgInfo.id;
	}
	if(chatBox && !chatBox.from){
		chatBox.setFrom(msgInfo.from);
	}
	if(chatBox){
		chatBox.showReceived(msgInfo);
		ChatBoxUtil.setBrowserMsgTip();//收到消息 有效消息进行提示
	}
}
/**
 * 通知客服对接
 */
var inform = function(sessionId,to){
	
	     var userCode = ChatBoxUtil.getUserFromJid(ChatBoxUtil.connection.jid);
	     $.eAjax({
		        url: GLOBAL.WEBROOT + '/mobilecusthistory/getSessionCount',
		        data: {userCode:userCode},
		        datatype: 'json',
		        async: true,
		        success: function(returnInfo) {
		        	var count = returnInfo.count;
		        	var csaCode = returnInfo.csaCode;
		        	var sessonTime = returnInfo.sessionTime;
		        	var messageValue = "";
		        	count = count -1;
		        	if(count=='0'){
		        		 messageValue = "用户"+ChatBoxUtil.getStaffCodeByUserCode(userCode)+"第"+(count+1)+"次接入";
		        	}else{
		        		 var s = ChatBoxUtil.getUserFromJid(csaCode);
		        		 messageValue = "用户"+ChatBoxUtil.getStaffCodeByUserCode(userCode)+"第"+(count+1)+"次接入,上次由客服"+ ChatBoxUtil.getStaffCodeByUserCode(s)+"接入于"+ebcDate.dateFormat(sessonTime, "yyyy-MM-dd hh:mm:ss");
		        	}
		   	     //通知客服人员买家接入
		         var sessionId = $('#sessionId').val();
		         ChatBoxUtil.message.messagetype = "inform";
		         ChatBoxUtil.message.body = messageValue;
		         ChatBoxUtil.message.sessionId = sessionId;
		         var str = JSON.stringify(ChatBoxUtil.message); 
				 var msg = $msg({  
			         to: to,//发送或回复消息   
			         from: ChatBoxUtil.connection.jid, //登录者ID
			         id : "inform",
			       //  messagetype : "inform",//通知类型
			         type: 'chat'  
			     }).c("body", null, str)//.c("session", null,sessionId);//带入本次会话的sessionId
			     ChatBoxUtil.connection.send(msg.tree());
				  //用户发消息后，先清除原来的定时任务 
 	            window.clearTimeout(ChatBoxUtil.timeOutId);
 	            window.clearTimeout(ChatBoxUtil.timeOutRId);
			     //启动定时任务，监听用户5分钟之内是否有发送消息，未发送消息，则断开与服务器的连接
			     ChatBoxUtil.timeOutId = window.setTimeout(ChatBoxUtil.disconnFromServ,ChatBoxUtil.outTime);
			     ChatBoxUtil.timeOutRId = window.setTimeout(ChatBoxUtil.timeOutRemind, ChatBoxUtil.outTime-120000);
		           }
		        });
		 
	 

}