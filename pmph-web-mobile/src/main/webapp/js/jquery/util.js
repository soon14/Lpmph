
$(function(){

    /**
     * Created by zqr on 2015/8/19.
     */
    var U = window.U || (window.U = {});

    U.qtip = function(obj,opt){
        var _opt= $.extend({
            style: {
                classes: 'qtip-bootstrap'
            },
            position:{
                my: 'right center',
                at: 'left center'
            }
        },opt);
        var _p={}
        switch(_opt.position)
        {
            case 'top':
                _p={
                    my: 'bottom center',
                    at: 'top center'
                }
                break;
            case 'right':
                _p={
                    my: 'left center',
                    at: 'right center'
                }
                break;
            case 'bottom':
                _p={
                    my: 'top center',
                    at: 'bottom center'
                }
                break;
            default:
                _p={
                    my: 'right center',
                    at: 'left center'
                }
                break;
        }

        _opt.position=_p;
        $(obj).qtip(_opt);

    }
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
    /* 多图轮播 */
    U.jcarousel=function(obj,opt){
        $(obj).each(function(){
            var _this=$(this);
            var _opt=$.extend({},opt);
            var slide=$('.slide',_this);
            $(this).slides({
                preload: true,
                generateNextPrev:true,
                animationComplete:function(item){
                    if(slide.size()>1){
                        if(item<=1){
                            _this.find('.prev').hide();
                            _this.find('.next').show();
                        }
                        if(item>=(slide.size())){
                            _this.find('.next').hide();
                            _this.find('.prev').show();
                        }
                    }
                },
                slidesLoaded:function(){
                    if(slide.size()>1){
                        _this.find('.next').show();
                    }
                }
            },_opt);
        });

    }

    U.alertInfo=function(content,p,_callback){
        var _p= $.extend({
            title : '提示',
            buttons : [
                {
                    caption : '确定',
                    callback : function(){
                        _callback&&_callback();
                    }
                },{
                caption : '取消',
                callback : function(){
                    bDialog.closeCurrent();
                }
            }
          ]
        },p);
        eDialog.info(content,_p);
    }

})


/* 初始化js */
$(function(){
    /* 网站顶部内容收缩 */
  /*  $('.dorpdown,.downc').on('mouseover', function () {
        $(this).addClass('active');
    }).on('mouseout', function () {
        $(this).removeClass('active');
    });*/

    //var words = ['boat', 'bear', 'dog', 'drink', 'elephant', 'fruit'];
    if($('#site-search').size()>0){
        $('#site-search').autocomplete({
                //        hints: words,
                placeholder: 'Search',
                width:500,
                height: 28,
                showButton: true,
                buttonText: '搜索',
                onSubmit:function(input){
                    alert(input.val());
                    //window.location.href='https://www.baidu.com/'
                },
                inputSet:function(input,currentProposals,currentSelection){
                    if(currentSelection>-1){
                        var curPro=currentProposals[currentSelection];
                        input.val(curPro.val);
                        input.attr('sub-id',curPro.id);
                    }

                },
                onChange:function(params,input,proposalList,currentProposals){
                    var vals=[{id:'1',val:'衣服'},{id:'2',val:'裤子'},{id:'3',val:'裤子'},{id:'4',val:'裤子'},{id:'5',val:'裤子'}
                        ,{id:'6',val:'裤子'},{id:'7',val:'裤子'},{id:'8',val:'裤子'},{id:'9',val:'裤子'}
                        ,{id:'10',val:'裤子'},{id:'11',val:'裤子'},{id:'12',val:'裤子'}];
                    for(var index in vals){
                        var v=vals[index];
                        currentProposals.push({id: v.id,val:v.val});
                        var element = $('<li></li>')
                            .addClass('proposal')
                            .click(function(){
                                params.inputSet(input,currentProposals,index);
                                proposalList.empty();
                                params.onSubmit(input);
                            })
                            .mouseenter(function() {
                                $(this).addClass('selected');
                            })
                            .mouseleave(function() {
                                $(this).removeClass('selected');
                            });

                        var html= "<span class='search-val'>"+v.val+"</span>" +
                            "<span class='search-num'>搜索约1000个结果</span>";
                        if(index>proposalList.find('li').size()-1){
                            element.html(html);
                            proposalList.append(element);
                        }else{
                            proposalList.find('li').eq(index).html(html);
                        }
                    }
                    if(vals.length<proposalList.size()){
                        proposalList.find('li:gt('+(vals.length-1)+')').remove();
                    }
                }
            }
        );
    }

    if($('#nav-search').size()>0){
        $('#nav-search').autocomplete({
                //        hints: words,
                placeholder: 'Search',
                width:165,
                height: 25,
                showButton: true,
                buttonText: '',
                onSubmit:function(input){
                    alert(input.val());
                    //window.location.href='https://www.baidu.com/'
                },
                inputSet:function(input,currentProposals,currentSelection){
                    if(currentSelection>-1){
                        var curPro=currentProposals[currentSelection];
                        input.val(curPro.val);
                        input.attr('sub-id',curPro.id);
                    }

                },
                onChange:function(params,input,proposalList,currentProposals){
                    var vals=[{id:'1',val:'衣服'},{id:'2',val:'裤子'},{id:'3',val:'裤子'},{id:'4',val:'裤子'},{id:'5',val:'裤子'}
                        ,{id:'6',val:'裤子'},{id:'7',val:'裤子'},{id:'8',val:'裤子'}];
                    for(var index in vals){
                        var v=vals[index];
                        currentProposals.push({id: v.id,val:v.val});
                        var element = $('<li></li>')
                            .addClass('proposal')
                            .click(function(){
                                params.inputSet(input,currentProposals,index);
                                proposalList.empty();
                                params.onSubmit(input);
                            })
                            .mouseenter(function() {
                                $(this).addClass('selected');
                            })
                            .mouseleave(function() {
                                $(this).removeClass('selected');
                            });

                        var html= "<span class='search-val'>"+v.val+"</span>" +
                            "<span class='search-num'>搜索约1000个结果</span>";
                        if(index>proposalList.find('li').size()-1){
                            element.html(html);
                            proposalList.append(element);
                        }else{
                            proposalList.find('li').eq(index).html(html);
                        }
                    }
                    if(vals.length<proposalList.size()){
                        proposalList.find('li:gt('+(vals.length-1)+')').remove();
                    }
                }
            }
        );
    }

    if($('#serList-search').size()>0){
        $('#serList-search').autocomplete({
                //        hints: words,
                placeholder: '输入关键词',
                width:120,
                height: 20,
                showButton: true,
                buttonText: '查询',
                onSubmit:function(input){
                    alert(input.val());
                    //window.location.href='https://www.baidu.com/'
                },
                inputSet:function(input,currentProposals,currentSelection){
                    if(currentSelection>-1){
                        var curPro=currentProposals[currentSelection];
                        input.val(curPro.val);
                        input.attr('sub-id',curPro.id);
                    }

                },
                onChange:function(params,input,proposalList,currentProposals){
                    var vals=[{id:'1',val:'衣服'},{id:'2',val:'裤子'},{id:'3',val:'裤子'},{id:'4',val:'裤子'},{id:'5',val:'裤子'}
                        ,{id:'6',val:'裤子'},{id:'7',val:'裤子'},{id:'8',val:'裤子'}];
                    for(var index in vals){
                        var v=vals[index];
                        currentProposals.push({id: v.id,val:v.val});
                        var element = $('<li></li>')
                            .addClass('proposal')
                            .click(function(){
                                params.inputSet(input,currentProposals,index);
                                proposalList.empty();
                                params.onSubmit(input);
                            })
                            .mouseenter(function() {
                                $(this).addClass('selected');
                            })
                            .mouseleave(function() {
                                $(this).removeClass('selected');
                            });

                        var html= "<span class='search-val'>"+v.val+"</span>" +
                            "<span class='search-num'>搜索约1000个结果</span>";
                        if(index>proposalList.find('li').size()-1){
                            element.html(html);
                            proposalList.append(element);
                        }else{
                            proposalList.find('li').eq(index).html(html);
                        }
                    }
                    if(vals.length<proposalList.size()){
                        proposalList.find('li:gt('+(vals.length-1)+')').remove();
                    }
                }
            }
        );
    }
    /* 头部搜索框 */

    $('.order-switch a').click(function () {
        $(this).siblings().removeClass('checked')
            .find('input[type="radio"]').prop('checked', false);
        $(this).addClass('checked')
            .find('input[type="radio"]').prop('checked', true);
    });

   $('.check-switch label').click(function(){
       $(this).addClass('selected').siblings().removeClass('selected');
   });

    if($('.icheck').size()>0){
        //$('.icheck').iCheck();
    }


    $('.seller-navL .list-c').each(function(){
        var $li=$('li',$(this));
        if($li.size()>0){
            var liNUm=Math.ceil($li.size()/2);
            $(this).outerHeight(($li.eq(0).height())*liNUm+8);
        }
    });
    $('.seller-navL dt').click(function () {
        var listc = $(this).next('.list-c');
        var listcp=listc.parent(),liH=0;
        listcp.toggleClass('active');
        if (listcp.hasClass('active')) {
            if($('li',listc).size()>2){
                liH=$('li',listc).eq(0).height()*2+8;
            }else{
                liH=$('li',listc).eq(0).height()+8;
            }
        } else {
            liH=$(this).next('.list-c').find('ul').outerHeight();
        }
        listc.outerHeight(liH);
    });

})