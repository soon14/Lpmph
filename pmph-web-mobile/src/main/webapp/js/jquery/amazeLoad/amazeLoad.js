/**
 * Created by zqr on 2016/5/25.
 * 基于amaze ui 弹框
 */

function AmLoad(opt){
    var defaults= {
        modal : false,//是否模式窗口
        callback : false,
        type:"1",
        style:'',
        autoCloss:true,
        lazeTime:1000,
        content:''
    };
    var $default = $.extend(defaults,opt);
    var $this=this;

    var tpl=$('<div class="am-modal" tabindex="-1" id="my-modal-loading">'+
            '<div class="am-modal-dialog">'+
            '<div class="am-modal-bd">'+
            '</div>'+
            '</div>'+
            '</div>');

    $(window.top.document.body).append(tpl);
    switch ($default.type){
        case '1' :  //表示成功弹框
            tpl.addClass("modal-sucess").addClass('am-modal-active').show();
            $('.am-modal-bd',tpl).html($default.content);
            break;
        case '2': //表示失败弹框
            tpl.addClass("modal-error").addClass('am-modal-active').show();
            $('.am-modal-bd',tpl).html($default.content);
            break;
        default :
             break;
    }
    if(defaults.autoCloss){
        var t;
        if(!t){
            t= window.setTimeout(function(){
                $this.close();
            },defaults.lazeTime);
        }else{
            window.clearTimeout(t);
            t=null;
        }
    }
    var close=this.close=function(){
        tpl.fadeOut(function(){
            tpl.remove();
        });
    }
}