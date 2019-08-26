//require(['ZebraDialog']);
	

/**
 * 网站前台弹出窗口
 * 基于Amaze UI
 */
var bDialog={
    template : '<div class="am-modal am-modal-no-btn" tabindex="-1" id="ebcModal">'+
        '<div class="am-modal-dialog" id="ebcModalMain">'+
        '<div class="am-modal-hd" id="ebcModalHeader"><div class="am-modal-tit"></div>'+
        '<a href="javascript: void(0)" class="am-close am-close-spin" id="ebcModalCloseButton" data-am-modal-close>&times;</a>'+
        '</div>'+
        '<div class="am-modal-bd" id="ebcModalBody">'+
        '</div>'+
        '</div>'+
        '</div>',
		defaults : {
            'backdrop' : 'static',//静态模式窗口，鼠标点击背景不关闭窗口，false：不显示背景遮罩，true，显示背景遮罩，但鼠标点击遮罩会关闭窗口
            'title' : '对话框',
            'width' : 0.8,
            'height' : 300,
            'animation' : false, //动画效果，默认关闭
            'closeButton' : false,//对话框底部的关闭窗口按钮
            'url' : false,
            'isHidePage':false,
            'isHideClose':false,
            'gdsUnion': false,
            'param':'',
            'onShow' : $.noop,     //显示对话前执行的回调
            'onShowed' : $.noop,   //显示完成对话框后执行的回调
            'onHide' : $.noop,     //关闭/隐藏对话框前执行的回调
            'onHidden' : $.noop,   //关闭/隐藏对话框后执行的回调
            'callback' : $.noop    //窗口回调函数，参数1：回调后返回的数据(callback(data))
		},
    //打开对话框
    //p:参数集
    //obj:jquery对象，用于网页片断式的显示内容，若设置了URL方式打开窗口，则不需要设置该参数
    open : function(p,obj){
        //合并参数
        var ebcModal = $("#ebcModal");
        var ebcModalMain=$("#ebcModalMain",ebcModal);
        var ebcModalBody=$("#ebcModalBody",ebcModal);
		if(ebcModal.size()<1){
            $("body").append(bDialog.template);
            ebcModal = $("#ebcModal");
            ebcModalMain=$("#ebcModalMain",ebcModal);
            ebcModalBody=$("#ebcModalBody",ebcModal);
		}
        p = $.extend({},bDialog.defaults,p);
        $(".am-modal-tit",ebcModal).html(p.title);//设置标题
        if(p.animation) ebcModal.addClass('fade');//设置动画效果
        ebcModalBody.empty();
        var _init = function(){
            //事件绑定
            if(p.onShow && $.isFunction(p.onShow)) ebcModal.on('show',p.onShow);
            if(p.onShowed && $.isFunction(p.onShowed)) ebcModal.on('shown',p.onShowed);
            if(p.onHide && $.isFunction(p.onHide)) ebcModal.on('hide',p.onHide);
            if(p.onHidden && $.isFunction(p.onHidden)) ebcModal.on('hidden',function(){
                ebcModal.removeClass('dialogInActive');
                //var data = $(main).data('dataReturn');
                p.onHidden();
                //if(p.callback && $.isFunction(p.callback)) p.callback(data);
            });
            ebcModal.modal({
                backdrop : p.backdrop
            });
            ebcModalMain.css({
                'width' : p.width<1?p.width*$(window).width():p.width,
                'height' : p.height<1?p.height*$(window).height():p.height,
            }).removeClass('hide');
        };
        if(obj){//页面片断模式
            ebcModalBody.append($(obj).show());
            _init();
        }else if(p.url) {//iframe模式
            _init();
            ebcModalBody.append('<iframe id="ebcModalBodyFrame" frameborder="0" scrolling="no" style="width:100%;border:0px;" src="'+p.url+'"></iframe>');
        }

        // ****************************处理回调及参数******************
        //对数据传递进行格式封装
        var _callback = null;
        if(p.callback && $.isFunction(p.callback)){
            _callback = function(data){
                if(data){
                    if($.isArray(data)){
                        p.callback({"results" : data});
                        return ;
                    }else{
                        p.callback({"results" : [data]});
                        return ;
                    }
                }else{
                    p.callback({"results" : null});
                    return ;
                }
            };
        }
        //设置回调
        if(p.callback) ebcModal[0].callback = _callback;
        //设置参数
        if(p.params) ebcModal[0].params = p.params; else ebcModal[0].params = {};


        // ****************************处理样式************************
        var head = $("#ebcModalHeader",ebcModal).outerHeight(true);
        //高度减去30 是去掉上下内边距各15，一共30
        var bodyPaddingTop = parseFloat(ebcModalBody.css('padding-top'));
        var bodyPaddingBottom = parseFloat(ebcModalBody.css('padding-bottom'));
        var newBodyHeight = ebcModalMain.innerHeight() - head  - bodyPaddingTop - bodyPaddingBottom;
        var bodyCss = { 'height' : newBodyHeight };
        if(newBodyHeight>400) bodyCss.maxHeight = newBodyHeight;
        ebcModalBody.css(bodyCss);

        //若是iFrame模式则设置iFrame高度等样式
        if(!obj){
            var newBodyFrameHeight = newBodyHeight - bodyPaddingTop - bodyPaddingBottom;
            var bodyFrameCss = { 'height' : newBodyFrameHeight };
            if(newBodyFrameHeight>(400 - bodyPaddingTop - bodyPaddingBottom)) bodyFrameCss.maxHeight = newBodyFrameHeight;
            $("#ebcModalBodyFrame",ebcModal).css(bodyFrameCss);
        }
        return ebcModal;
    },
    //关闭当前弹出窗口
    closeCurrent : function(){
        var _hideDialog = function(dlg){
            if(dlg && $(dlg).hasClass('am-modal-active')){
                //清除参数
                dlg[0].callback = null;
                dlg[0].selectorparams = null;
                $("#ebcModalCloseButton",dlg).click();
                return true;
            }
            return false;
        };
        var dialog = $("#ebcModal");
        if(_hideDialog(dialog)) return;

        //关闭父层窗口
        dialog = $("#ebcModal",$(window.parent.document));
        _hideDialog(dialog);
    },
    //获得弹出窗口对象
    getDialog : function(){
        var dlg = $("#ebcModal");
        if(dlg && $(dlg).hasClass('am-modal-active')) return dlg;
        return $("#ebcModal",$(window.parent.document));
    },
    // 获得选择器中的传递参数
    getDialogParams : function(dlg){
        if(dlg) return dlg[0].params;
        return null;
    },
    // 获得选择器中的回调函数
    getDialogCallback : function(dlg){
        if(dlg) return dlg[0].callback;
        return null;
    }
}
