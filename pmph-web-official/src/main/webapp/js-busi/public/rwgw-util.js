$(function(){
    /**
     * Created by zqr
     */
    var U = window.U || (window.U = {});

    U.tab = function(head,body,opt){
        var _opt= $.extend({
            event:'click',
            filterD:null,
            callback:null,
            defItem:0
        },opt);

        var _hc=$(head).children();
        var _bc=$(body).children();
        var _actIndex=null;

        _hc=_opt.filterD?_hc.not(_opt.filterD):_hc;
        show(_opt.defItem);

        _hc.on(_opt.event,function(e){
            var _id=$(this).index();

            if((!_actIndex&&_actIndex!=0)||_actIndex!=_id){
                show($(this).index());
                _opt.callback&&_opt.callback($(this));
            }
            _actIndex=_id;
            e.preventDefault();

        });
        function show(index){
            _hc.eq(index)
                .addClass('active')
                .siblings().removeClass('active');
            _bc.eq(index).show().siblings().hide();
        }

    };
    
    U.tab('.box-book .tabn','.box-book .tabc');
    U.tab('.order-nav','.order-list');
    $('.order-list .bk-item').live('mouseover', function () {
        $(this).addClass('active').siblings().removeClass('active');
    }).on('mouseleave', function (e) {
        if($(e.relatedTarget).parents('.order-list').size()>0
          &&$(e.relatedTarget).hasClass('.item')){
            $(this).removeClass('active');
        }
    });

  /*var mySwipe2 = Swipe(document.getElementById('mySwipe2'), {});
    $('#page2-prev').click(function(){
        mySwipe2.prev();
    });
    $('#page2-next').click(function(){
        mySwipe2.next();
    }); */
    $('.news-show .news-title').click(function(){
        $('.news-show li').removeClass('active');
        $(this).parents('li').addClass('active');
    });
   
    $(".hz-icon").click(function(){
            $.scrollTo('#webList',1000);
    });
    
    /**
     * 左侧栏的交互事件
    */
    $(".gwfix").click(function(e){
        var efix=$(this).parent("#elefix").find(".elevator");
        if(efix.css('display')=='none'){
            $(this).html('<i class="gwfix-l"></i>');
        }else{
            $(this).html('<i class="gwfix-r"></i>');
        }
        efix.toggle();
        e.preventDefault();
    });
    $("#ewm").click(function(){
          $(this).toggleClass('hover-b');
      })
    
    /**
     * 说明：加入收藏夹
     * ecpaddfavorite  必须的a标签的类
     * 返回值：无
     */
    $(".ecpaddfavorite").live("click", function() {
    	var $item = $(this);
    	var url = window.location;
    	var title = document.title;
    	var Sys = isBrowser();
    	try {
    	    if(Sys.ie){
    	    	window.external.addFavorite(url, title);
    	    }else if(Sys.firefox || Sys.chrome || Sys.opera || Sys.safari){
    	    	$item.attr("rel", "sidebar");
    			$item.attr("title", title);
    			$item.attr("href", url);
    	    }else{
    	    	eDialog.alert("请使用Ctrl+D将本页加入收藏夹！");
    	    }
    		/*if (jQuery.browser.msie) {
    			window.external.addFavorite(url, title);
    		} else if (jQuery.browser.mozilla || jQuery.browser.opera) {
    			$item.attr("rel", "sidebar");
    			$item.attr("title", title);
    			$item.attr("href", url);
    		} else {
    			eDialog.alert("请使用Ctrl+D将本页加入收藏夹！");
    		}*/
    	} catch (e) {
    		eDialog.alert("您的浏览器不支持，请使用Ctrl+D将本页加入收藏夹！");
    	}
    });

    /**
     * 说明：设为首页 
     * ecpsethome 必须的a标签的类 
     * 返回值：无
     */
    $(".ecpsethome").live("click",function(){
    	//var $item = $(this);
    	//var url = "http://localhost:8081/pmph-web-official/main";
    	var url = window.location.href;
    	try {
    		this.style.behavior = 'url(#default#homepage)';
    		this.setHomePage(url);
    	} catch (e) {
    		if (window.netscape) {
    			try {
    				netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
    			} catch (e) {
    				alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将 [signed.applets.codebase_principal_support]的值设置为'true',双击即可。");
    			}
    			var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
    			prefs.setCharPref('browser.startup.homepage', url);
    		} else {
    			alert("您的浏览器不支持，请按照下面步骤操作：1.打开浏览器设置。2.点击设置网页。3.输入：" + url + "点击确定。");
    		}
    	}
    });
    
});



/**
 * 获取浏览器
 * @returns Sys
 */
function isBrowser(){
	
	var Sys = {};
    var ua = navigator.userAgent.toLowerCase();
    var s;
    
    (s = ua.match(/rv:([\d.]+)\) like gecko/)) ? Sys.ie = s[1] :
    (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
    (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
    (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
    (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
    (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
    
    /*if (Sys.ie) $('span').text('IE: ' + Sys.ie);
    if (Sys.firefox) $('span').text('Firefox: ' + Sys.firefox);
    if (Sys.chrome) $('span').text('Chrome: ' + Sys.chrome);
    if (Sys.opera) $('span').text('Opera: ' + Sys.opera);
    if (Sys.safari) $('span').text('Safari: ' + Sys.safari);*/
    
    return Sys;
}
