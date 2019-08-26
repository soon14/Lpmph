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
        autoClose:true,
        lazeTime:2000,
        shade:false,//是否遮罩
        content:'loading..'
    };
    var $default = $.extend(defaults,opt);
    var $this=this;

    var tpl=$('<div class="am-modal" tabindex="-1" id="my-modal-loading">'+
            '<div class="am-modal-dialog">'+
            '<div class="am-modal-bd">'+
            '</div>'+
            '</div>'+
            '</div>');
    var shadeHtml=$('<div class="modal-shade"></div>');

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
        case '3': //loading
            tpl.addClass("modal-loading").addClass('am-modal-active').show();
            break;
        default :
         //   tpl.addClass("modal-loading").addClass('am-modal-active').show();
            //$('.am-modal-bd',tpl).html($default.content);
            break;
    }

    if(defaults.shade){
        $(window.top.document.body).append(shadeHtml);
    }
    if(defaults.autoClose){
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
        tpl.fadeOut("slow",function(){
            tpl.remove();
            shadeHtml.remove();
        });
    }
}