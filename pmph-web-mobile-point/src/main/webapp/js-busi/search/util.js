/**
 * Created by zqr on 2016/5/25.
 */

function LoadScroll(element, options) {
    var defaults= $.extend({
            url:'',
            isAjax:false,
            callback: null,
            downCallback:null,
            preventDefault:false,
            params:{
                page:1,
                count:1
            }
       },options);
    var $main = $('#'+element);
    var $list = $main.find('.scrollCont');
    var $pullDown = $main.find('.pull-down');
    var $pullDownLabel = $pullDown.find('.pull-label');
    var $pullUp = $main.find('.pull-up');
    var $pullUpLabel = $pullUp.find('.pull-label');
    var topOffset = -$pullDown.outerHeight();
    var _this = this;
    var loadTxt="加载完成";
    var noDataTxt="无数据了";

    this.iScroll=null;

    /*设置url*/
    this.getURL = function(params) {
        var queries = [];
        for (var key in  params) {
            if (key !== 'page') {
                queries.push(key + '=' + params[key]);
            }
        }
        queries.push('page=');
        return defaults.url + '?' + queries.join('&');
    };

    /* ajax 渲染界面 */
    this.renderList = function(page, type) {
        var $el = $pullDown;
        console.log("渲染界面页面号="+page);
        if (type === 'load') {
            $el = $pullUp;
        }
        $.getJSON(this.URL + page).then(function(data) {
            var values=data.values;
            if(defaults.callback){
                defaults.callback(_this,data);
            }else{
                _this.total = values.total;
                var html = template(element+'-tpl', data);//Handlebars.compile($('#'+element+"-tpl").html());
                if (type === 'refresh') {
                    $list.empty().append(html);
                } else if (type === 'load') {
                    $list.append(html);
                } else {
                    $list.html(html);
                }
            }
            if (type === 'refresh') {
                $pullDownLabel.text(loadTxt);
            } else if (type === 'load') {
                $pullUpLabel.text("上拉加载更多");
            }
            // refresh iScoll
            calHeight();
            setTimeout(function() {
                _this.iScroll.refresh();
            }, 100);

        }, function() {
            calHeight();
            eDialog.alert("加载错误，请检查网络问题!");
        }).always(function() {
            if (type !== 'load') {
                _this.iScroll.scrollTo(0, topOffset, 800, $.AMUI.iScroll.utils.circular);
            }else{

            }
        });

    };

    /* 初始化界面 */
    this.init = function() {
        var $params=defaults.params;
        var _this = this;
        var pullFormTop = false;
        var pullStart;
        console.log("初始化界面");
        _this.iScroll = new $.AMUI.iScroll('#'+element, {
            click: true,
            preventDefault:defaults.preventDefault
        });

        document.addEventListener('touchmove', function (e) {
            e.preventDefault();
        }, false);


        var myScroll = _this.iScroll;
        if(defaults.isAjax){//存在ajax请求的情况
        	console.log("存在ajax请求的情况.....");
            initParam();
            this.renderList(this.page);
            console.log("11111111");
            myScroll.off('scrollStart');
            myScroll.on('scrollStart', function() {
                $pullDown.find('.am-icon-spin').hide();
                $pullUp.find('.am-icon-spin').hide();
                if (this.y >= topOffset) {
                    pullFormTop = true;
                }
                pullStart = this.y;
                if(this.directionY === -1){
                    $pullDownLabel.text("下拉刷新");
                }
                if(this.directionY === 1 && (this.y == this.maxScrollY )){
                    if (_this.next < _this.total) {
                        $pullUpLabel.text("上拉加载更多");
                    }else{
                        $pullUpLabel.text(noDataTxt);
                    }
                }
            });
            console.log("222222222222");
            myScroll.off('scrollEnd');
            myScroll.on('scrollEnd', function() {
                //下拉
                if (pullFormTop && this.directionY === -1 &&$pullDown.size()>=1) {
                   console.log(444444)
                    _this.handlePullDown();
                }
                //上拉
                pullFormTop = false;
                if(this.y == this.maxScrollY && $pullUp.size()>=1){
                	console.log("上拉滑动。。")
                    _this.handlePullUp();
                }
            });
        }else{ //非列表的刷新
        	console.log("非列表的刷新.....");
            if($pullDown.size()>0){
                _this.iScroll.scrollTo(0, topOffset, 800, $.AMUI.iScroll.utils.circular);
            }
            myScroll.off('scrollStart');
            myScroll.on('scrollStart', function() {
                $pullDown.find('.am-icon-spin').hide();
                if (this.y >= topOffset) {
                    pullFormTop = true;
                }
                pullStart = this.y;
                if(this.directionY === -1){
                    $pullDownLabel.text("下拉刷新");
                }
            });
            myScroll.off('scrollEnd');
            myScroll.on('scrollEnd', function() {
                //下拉
                $pullDown.find('.am-icon-spin').show();
                if (pullFormTop && this.directionY === -1 &&$pullDown.size()>=1) {
                    $pullDownLabel.text("正在加载");
                    if(defaults.downCallback){
                        defaults.downCallback(_this,afterDownBack);
                    }
                  }
            });
        }
    };

    this.handlePullDown = function() {
        $pullDownLabel.text("正在加载");
        console.log(333333333);
        $pullDown.find('.am-icon-spin').show();
         this.prev = 1;
        this.renderList(this.prev, 'refresh');
        $pullUpLabel.text("上拉加载更多");
    };

    this.handlePullUp = function() {
        $pullUp.find('.am-icon-spin').show();
        $pullUpLabel.text("正在加载");
        console.log("上拉操作。。this.next="+this.next+"this.count="+this.count+"this.total="+this.total);
        if (this.next < this.total) {
        	this.next =  parseInt(this.next) + parseInt(this.count);
        	//this.next +=  parseInt(this.count);
            console.log("this.next = "+this.next);
            this.renderList(this.next, 'load');
            //this.next += this.count;          原有代码此处有bug,将做字符串拼接了
            //this.renderList(this.next, 'load');
        } else {
            $pullUpLabel.text(noDataTxt);
        }
    };

    /* 刷新iscroll */
    this.refresh= function(){
        this.iScroll.refresh();
    };

    function afterDownBack(){
        $pullDownLabel.text(loadTxt);
        calHeight();
        _this.iScroll.scrollTo(0, topOffset, 800, $.AMUI.iScroll.utils.circular);
    }

    function calHeight(){
        var aHi=0;
        $list.children().each(function(){
            aHi+=$(this).outerHeight();
        })
        if(aHi<=$('#'+element).height()){
            $list.height($('#'+element).outerHeight()+20)
        }else{
            $list.height("auto");
        }
    }

    function initParam(){
        var $params=defaults.params;
        _this.prev = _this.next = _this.page =$params.page?$params.page:1;
        _this.count=$params.count?$params.count:1;
        _this.total = null;
        _this.URL = _this.getURL(defaults.params);
    }
    /* 刷新页面 */
    this.refreshPage= function(opt){
    	console.log("刷新页面");
        defaults= $.extend(defaults,opt);
        initParam();
        this.renderList(this.page);
    };
    this.init();
};
/* 搜索 begin*/
$(function(){
    var needSerInput=$('#needSearch');
    if(needSerInput.size()>0){
//    	var searchShopInner = $("#searchShopInner").val();
    	var searchShopInner = "true";             //积分商城中，只检索商品，将店铺检索功能屏蔽
    	var html = "";
    
    	if(searchShopInner=="true"){
        	html = '<div id="searchPage" class="ui-page-contanier" >' +
            '<header class="am-header am-header-return am-header-fixed">' +
            '<div class="am-header-title tit-hidden">' +
            '<div id="site-search">' +
            '<span class="input-clear"></span>' +
            '<div class="sel-search">'+
            '<span class="am-icon-search" searchType="1">' +
//             '<em>商品</em>' +
            ' <i class="search-arrow"></i>' +
            '</span>' +
            '</div>' +
            '<div class="am-header-left am-header-nav">' +
            '<a href="javascript:" onclick="history.go(-1); ">'+
            '<i class="public-arrowleft"></i>'+
            '</a>',
            '<span id="pageReturn" class="ui-arrow ui-arrow-left"></span>' +
            '</div>' +
            '</div>' +
            '</header>' +
            '</div>';
    	}else{
    		var shopHtml =  '<span class="am-icon-search" searchType="2">' +
            '<em>店铺</em>' +
            ' <i class="search-arrow"></i>' +
            '</span>' +
            '<ul class="search-list">' +
            '<li searchType="1"><i class="iconfont icon-pro"></i> 商品</li>' +
            '<li searchType="2"><i class="iconfont icon-shop"></i> 店铺</li>' ;
        	html = '<div id="searchPage" class="ui-page-contanier" style="min-height:600px;">' +
            '<header class="am-header am-header-return am-header-fixed">' +
            '<div class="am-header-title tit-hidden">' +
            '<div id="site-search" class="site-input">' +
            '<span class="input-clear"></span>' +
            '<div class="sel-search">' ;
            if($("#searchType").val()=="2"){
            	html += shopHtml;
             }else{
            	 html +=  '<span class="am-icon-search" searchType="1">' +
                 '<em>商品</em>' +
                 ' <i class="search-arrow"></i>' +
                 '</span>' +
                 '<ul class="search-list">' +
                 '<li searchType="1"><i class="iconfont icon-pro"></i> 商品</li>' +
                 '<li searchType="2"><i class="iconfont icon-shop"></i> 店铺</li>' ;
             }
            html +='</ul>' +
            '</div>' +
            '</div>' +
            '<div class="am-header-left">' +
            '<span id="pageReturn" class="ui-arrow ui-arrow-left"></span>' +
            '</div>' +
            '</div>' +
            '</header>' +
            '</div>';
    	}
	    var searchPageWrap = $(html);
        $('body').append(searchPageWrap);
        $('.am-icon-search').click(function (e) {
            $(this).parents('.sel-search').toggleClass('open');
            e.stopPropagation();
        });
        $('.search-list>li').click(function (e) {
            $('.sel-search>.am-icon-search>em').html($(this).text());
            $('.sel-search>.am-icon-search').attr('searchType',$(this).attr('searchType'));
            $(".proposal-list").empty();
            $('.sel-search').removeClass('open');
            e.stopPropagation();
        });
        $('body').append(searchPageWrap);
        var searchPage=$('#searchPage');
        var isLoad=false;
        needSerInput.on('click',function(){
            searchPage.addClass('page-in');
            if(!isLoad){
                isLoad=true;
                if($('#site-search').size()>0){
                	var buttonText = "搜索";
                	if($("#searchShopInner").val()=="true"){
                		buttonText = "搜本店";
                	}
                    $('#site-search').autocomplete({
                            //        hints: words,
                            placeholder: '输入关键字搜索',
                            width:300,
                            height:30,
                            showButton: true,
                            buttonText: buttonText,
                            onSubmit:function(input){
                                search(input.val());
                            },
                            inputSet:function(input,currentProposals,currentSelection){
                                if(currentSelection>-1){
                                    var curPro=currentProposals[currentSelection];
                                    input.val(curPro.collationQueryString);
                                }
                            },
                            onChange:function(params,input,proposalList,currentProposals){
                                var inputClear=input.parents('#site-search').find('.input-clear');
                                if(input.val()==''){
                                    inputClear.hide();
                                    proposalList.empty();
                                    return;
                                }else{
                                    inputClear.off().on('click',function(e){
                                        proposalList.empty();
                                        input.val('');
                                        e.stopPropagation();
                                    });
                                }
                                inputClear.show();

                                var reqparams={'keyword':input.val()};
                                var searchType = $('.sel-search>.am-icon-search').attr('searchType');
                                var _url = GLOBAL.WEBROOT + "/search/suggest?searchType="+searchType;
                                $.eAjax({
                                    url : _url,
                                    data :reqparams,
                                    success : function(json) {
                                        var vals=json;
                                        if(vals.length==0){
                                            proposalList.empty();
                                        }else{
                                            for(var index in vals){
                                                var v=vals[index];
                                                currentProposals.push({collationQueryString: v.collationQueryString,numberOfHits:v.numberOfHits});
                                                var element = $('<li></li>')
                                                    .addClass('proposal')
                                                    .click(function(){
                                                        input.val($(this).text());
                                                        proposalList.empty();
                                                        params.onSubmit(input);
                                                    })
                                                    .mouseenter(function() {
                                                        $(this).addClass('selected');
                                                    })
                                                    .mouseleave(function() {
                                                        $(this).removeClass('selected');
                                                    });

                                                var html= "<span class='search-val'>"+v.collationQueryString+"</span>";
                                                if(index>proposalList.find('li').size()-1){
                                                    element.html(html);
                                                    proposalList.append(element);
                                                }else{
                                                    proposalList.find('li').eq(index).html(html);
                                                }
                                            }
                                            if(vals.length<proposalList.find('li').size()){
                                                proposalList.find('li:gt('+(vals.length-1)+')').remove();
                                            }
                                        }
                                    }
                                });

                            }
                        }
                    );
                    window.setTimeout(function(){
                        $('.autocomplete-input','#site-search').focus();
                    },100);
                }
                var pageReturn=$('#pageReturn',searchPage);
                pageReturn.on('click',function(e){
                    // searchPage.offCanvas('close');
                    searchPage.removeClass('page-in');
                    e.preventDefault();
                });
                $.ajax({
                    url: GLOBAL.WEBROOT + "/search/hotkeyword",
                    context: document.body,
                    dataType:'html',
                  //  async:false,
                    success: function(html){
                        searchPage.append(html);
                        $('#hot-keyword').delegate('li','click',function () {
                            search($(this).text());
                        });

                        $('#kw-head').delegate('#exchange','click',function () {
                            $.ajax({
                                url: GLOBAL.WEBROOT + "/search/hotkeyword/exchange",
                                data :{'pageNo':$('#kw-pageNo').val()},
                                dataType:'json',
                                success: function(vals){
                                    if(vals&&vals.values&&vals.values.length>0){
                                        $('#hot-keyword').children().remove();

                                        var lis="";
                                        for(var index in vals.values){
                                            var val=vals.values[index];
                                            lis=lis+"<li>"+val+"</li>";
                                        }

                                        $('#hot-keyword').append(lis);
                                        $('#kw-pageNo').val(1+parseInt($('#kw-pageNo').val()));
                                    }
                                }
                            });
                        });
                    }
                });
                $('#searchTxt').val($('#needSearch').val());
                $('#searchTxt').attr("placeholder"," ");
            }

        });

    }
});
/* 搜索 end*/

$(function(){
    $('#searchBtn').click(function () {
        search($('#needSearch').val());
    });
});

/**
 * 搜索
 */
var search=function(keyword){
    keyword=$.trim(keyword);
    var url = encodeURI(GLOBAL.WEBROOT+"/search?keyword="+keyword);
    window.location.href = url;
};
