

$(function(){
	
    		
    
    		// BOSH地址 
			var BOSH_SERVICE = $("#boshService").val();
			ChatBoxUtil.BOSH_SERVICE = BOSH_SERVICE;//set value
			// XMPP连接  
			ChatBoxUtil.connection = null;
			// 当前状态是否连接  
			ChatBoxUtil.connected = false;
			var baseH = $('.chat-bottom').outerHeight();
			var winPercentHi=0.85; //页面高度百分比
		    //set value
		    ChatBoxUtil.constants.baseH = baseH;
			ChatBoxUtil.model = ChatBoxUtil.constants.CUST;
		    //登录
		    var uName = $("#uName").val();
		    ChatBoxUtil.ServName = $('#staffCode').val();
		    var PASS = ChatBoxUtil.defPass;
		    var sessionId = $('#sessionId').val();
		    var csaCode = $('#csaCode').val();
		    var shopId = $('#shopId').val();
		    ChatBoxUtil.ServName="人卫商城在线客服";
		    ChatBoxUtil.csaCode = csaCode;
		    ChatBoxUtil.custPic = $('#custPic').val();
		    ChatBoxUtil.servPic = $('#servPic').val();
		    initConnection({uName: uName, uPass: PASS});
		    ChatBoxUtil.wndUnload();
//		    Strophe.log = function(lv, msg){
//		    	console.log(msg);
//		    }
		    
		    FastClick.attach(document.body);  
			//退出
		    $("body").delegate('#logoutBtn','click',function(){
		    	window.history.back(-1);
			    return false;
		    });


});	

