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


   function isPC(){
        var userAgentInfo = navigator.userAgent;
        var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");
        var flag = true;
        for (var v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }
        }
        return flag;
    }

    var navBars=$('.nav-bar').children();
    var navBarsChild=$('.nav-bar-child').children();
    var $active=navBars.filter('.active').addClass('hover');
    var $index=$active.index();
    if($index>=0){
        navBarsChild.eq($active.index()).addClass('active childBg');
    }
    /* 导航条begin */
    $('.nav-bar li').on('mouseover touchstart',function(e){
        $(this).addClass('hover').siblings().not('.active').removeClass('hover');
        navBarsChild.not('.active').removeClass('childBg');
        navBarsChild.eq($(this).index()).addClass('childBg');
        $('.menuBg-child').show();
    });
    $('.menuBg').on('mouseleave',function(e){
        $('.menuBg-child').hide();
        navBars.not('.active').removeClass('hover');
    });
    $('.nav-bar-child li').on('mouseover touchstart',function(){
        $(this).addClass('childBg').siblings().not('.active').removeClass('childBg');
        navBars.not('.active').removeClass('hover');
        navBars.eq($(this).index()).addClass('hover');
     });

     /* 首页图片轮播 begin */
     var $swipe = document.getElementById('mySwipe');
     if($swipe){
        var bullets = $('#swipePage li');
        window.mySwipe = Swipe($swipe, {
            // startSlide: 4,
            auto: 5000,
            callback: function(pos) {
                bullets.eq(pos).addClass('active').siblings().removeClass('active');
            }
        });
        $('#swipePage li').click(function(){
            mySwipe.slide($(this).index());
        });
     }

    /* 首页图片轮播 end */

    //针对手机页面
    if(!isPC()){
       // $('body').append('<link href="../css/phone.css" rel=stylesheet>');
     /*   var headLh=0;
        $('.nav-bar > li').each(function() {
            headLh += $(this).outerWidth();
        });
        $('.menu-content').width(headLh+5);
        $('.menuBg-child').width(headLh);

        $('.nav-bar li').on('touchstart',function(e){
            $('.menuBg-child').show();
            $('.menu-wrap').height('215px');
            $('.swipe').css('margin-top','-170px');
        });

        $('body').on('touchstart',function(e){
            if(e.target&&!$(e.target).hasClass('menuBg')
                &&$(e.target).parents('.menuBg').size()<=0){
                $('.menu-wrap').height('auto');
                $('.swipe').css('margin-top','0px');
            }
        });
        var scrollObj = new IScroll(".menu-wrap", {
            scrollX: true,
            scrollY: false,
            preventDefaultException: {
                tagName: /^(DIV|LI)$/
            }
        });*/
    }

    $('.btn-navbar').click(function(){
      //  var ntarget=$(this).attr('nav-bar-phone');
        $('.nav-bar-phone').toggle();
    });

    /* 导航条begin */
});