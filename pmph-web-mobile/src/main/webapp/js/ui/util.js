/**
 * Created by zqr on 2016/5/25.
 */
function LoadScroll(element, options) {
    var defaults = $.extend({
        url: '',
        isAjax: false,
        callback: null,
        downCallback: null,
        preventDefault: false,
        params: {
            page: 1,
            count: 1
        }
    }, options);
    var $main = $('#' + element);
    var $list = $main.find('.scrollCont');
    var $pullDown = $main.find('.pull-down');
    var $pullDownLabel = $pullDown.find('.pull-label');
    var $pullUp = $main.find('.pull-up');
    var $pullUpLabel = $pullUp.find('.pull-label');
    var topOffset = -$pullDown.outerHeight();
    var _this = this;
    var loadTxt = "加载完成";
    var noDataTxt = "无数据了";

    this.iScroll = null;

    /*设置url*/
    this.getURL = function (params) {
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
    this.renderList = function (page, type) {
        var $el = $pullDown;
        if (type === 'load') {
            $el = $pullUp;
        }
        $.getJSON(this.URL + page).then(function (data) {
            var values = data.values;
            if (defaults.callback) {
                defaults.callback(_this, data);
            } else {
                _this.total = values.total;
                var html = template(element + '-tpl', data);//Handlebars.compile($('#'+element+"-tpl").html());
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
            var timeClear;
            if (timeClear) {
                clearTimeout(timeClear);
            } else {
                timeClear = setTimeout(function () {
                    _this.iScroll.refresh();
                }, 100);
            }

            //有一些scroll插件表达式不太好处理的，需要加载之后做操作的，放到这个属性
            if(defaults.call){
                defaults.call();
            }

        }, function () {
            calHeight();
            eDialog.alert("加载错误，请检查网络问题!");
        }).always(function () {
            if (type !== 'load') {
                _this.iScroll.scrollTo(0, topOffset, 800, $.AMUI.iScroll.utils.circular);
            } else {

            }
        });

    };

    /* 初始化界面 */
    this.init = function () {
        var $params = defaults.params;
        var _this = this;
        var pullFormTop = false;
        var pullStart;

        _this.iScroll = new $.AMUI.iScroll('#' + element, {
            preventDefault: defaults.preventDefault
        });

        document.addEventListener('touchmove', function (e) {
            e.preventDefault();
        }, false);


        var myScroll = _this.iScroll;
        if (defaults.isAjax) {//存在ajax请求的情况
            initParam();
            this.renderList(this.page);
            var directionY = 0;
            myScroll.off('scrollStart');
            myScroll.on('scrollStart', function () {
                $pullDown.find('.am-icon-spin').hide();
                $pullUp.find('.am-icon-spin').hide();
                if (this.y >= topOffset) {
                    pullFormTop = true;
                }
                pullStart = this.y;
                directionY = this.directionY;
                if (this.directionY === -1) {
                    $pullDownLabel.text("下拉刷新");
                }
                /*  if(this.directionY === 1 && (this.y == this.maxScrollY )){
                 if (_this.next < _this.total) {
                 $pullUpLabel.text("正在加载..");
                 }else{
                 $pullUpLabel.text(noDataTxt);
                 $pullUp.find('.am-icon-spin').hide();
                 }
                 }*/
            });
            $(".scroll", myScroll.wrapper).on("touchmove", function () {
                $pullUpLabel.text("正在加载");
                $pullUp.find('.am-icon-spin').show();
            });
            myScroll.off('scrollEnd');
            myScroll.on('scrollEnd', function () {
                //下拉
                if (pullFormTop && this.directionY === -1 && $pullDown.size() >= 1) {
                    _this.handlePullDown();
                }
                //上拉
                pullFormTop = false;
                if (this.y == this.maxScrollY && $pullUp.size() >= 1) {
                    _this.handlePullUp();
                }
            });
        } else { //非列表的刷新
            if ($pullDown.size() > 0) {
                _this.iScroll.scrollTo(0, topOffset, 800, $.AMUI.iScroll.utils.circular);
            }
            myScroll.off('scrollStart');
            myScroll.on('scrollStart', function () {
                $pullDown.find('.am-icon-spin').hide();
                if (this.y >= topOffset) {
                    pullFormTop = true;
                }
                pullStart = this.y;
                if (this.directionY === -1) {
                    $pullDownLabel.text("下拉刷新");
                }
            });
            myScroll.off('scrollEnd');
            myScroll.on('scrollEnd', function () {
                //下拉
                $pullDown.find('.am-icon-spin').show();
                if (pullFormTop && this.directionY === -1 && $pullDown.size() >= 1) {
                    $pullDownLabel.text("正在加载");
                    if (defaults.downCallback) {
                        defaults.downCallback(_this, afterDownBack);
                    }
                }
            });
        }
    };

    this.handlePullDown = function () {
        $pullDownLabel.text("正在加载");
        $pullDown.find('.am-icon-spin').show();
        this.prev = this.next = 1;
        this.renderList(this.prev, 'refresh');
        $pullUpLabel.text("上拉加载更多");
    };

    this.handlePullUp = function () {
        $pullUp.find('.am-icon-spin').show();
        $pullUpLabel.text("正在加载");
        if (this.next < this.total) {
            this.next += this.count;
            this.renderList(this.next, 'load');
        } else {
            $pullUpLabel.text(noDataTxt);
            $pullUp.find('.am-icon-spin').hide();
        }
    };

    /* 刷新iscroll */
    this.refresh = function () {
        this.iScroll.refresh();
    };

    function afterDownBack() {
        $pullDownLabel.text(loadTxt);
        calHeight();
        _this.iScroll.scrollTo(0, topOffset, 800, $.AMUI.iScroll.utils.circular);
    }

    function calHeight() {
        var aHi = 0;
        $list.children().each(function () {
            aHi += $(this).outerHeight();
        })
        if (aHi <= $('#' + element).height()) {
            $list.height($('#' + element).outerHeight() + 20)
        } else {
            $list.height("auto");
        }
    }

    function initParam() {
        var $params = defaults.params;
        _this.prev = _this.next = _this.page = $params.page ? $params.page : 1;
        _this.count = $params.count ? $params.count : 1;
        _this.total = null;
        _this.URL = _this.getURL(defaults.params);
    }

    /* 刷新页面 */
    this.refreshPage = function (opt) {
        defaults = $.extend(defaults, opt);
        initParam();
        this.renderList(this.page);
    };

    this.init();
};
/* 搜索 begin*/
$(function () {
    var needSerInput = $('#needSearch');
    if (needSerInput.size() > 0) {
        var searchPageWrap = $('<div id="searchPage" class="ui-page-contanier">' +
            '<header class="am-header am-header-return am-header-fixed">' +
            '<div class="am-header-title">' +
            '<div id="site-search">' +
            '<span class="input-clear"></span>' +
            '</div>' +
            '<div class="am-header-left">' +
            '<span id="pageReturn" class="ui-arrow ui-arrow-left"></span>' +
            '</div>' +
            '</div>' +
            '</header>' +
            '</div>');
        $('body').append(searchPageWrap);
        var searchPage = $('#searchPage');
        var isLoad = false;
        needSerInput.on('click', function () {
            searchPage.addClass('page-in');
            $('body').css('overflow', 'hidden');
            if (!isLoad) {
                isLoad = true;
                if ($('#site-search').size() > 0) {
                    $('#site-search').autocomplete({
                            //        hints: words,
                            placeholder: 'Search',
                            width: 300,
                            height: 30,
                            showButton: true,
                            buttonText: '搜索',
                            onSubmit: function (input) {
                                // alert(input.val());
                                //   window.location.href='https://www.baidu.com/'
                            },
                            inputSet: function (input, currentProposals, currentSelection) {
                                if (currentSelection > -1) {
                                    var curPro = currentProposals[currentSelection];
                                    input.val(curPro.val);
                                    input.attr('sub-id', curPro.id);
                                }

                            },
                            onChange: function (params, input, proposalList, currentProposals) {
                                var inputClear = input.parents('#site-search').find('.input-clear')
                                if (input.val() == '') {
                                    inputClear.hide();
                                    proposalList.empty();
                                    return;
                                } else {
                                    inputClear.off().on('click', function (e) {
                                        proposalList.empty();
                                        input.val('');
                                        e.stopPropagation();
                                    });
                                }
                                inputClear.show();
                                var vals = [{id: '1', val: '衣服'}, {id: '2', val: '裤子'}, {id: '3', val: '裤子'}, {
                                    id: '4',
                                    val: '裤子'
                                }, {id: '5', val: '裤子'}
                                    , {id: '6', val: '裤子'}, {id: '7', val: '裤子'}, {id: '8', val: '裤子'}, {
                                        id: '9',
                                        val: '裤子'
                                    }
                                    , {id: '10', val: '裤子'}, {id: '11', val: '裤子'}, {id: '12', val: '裤子'}];
                                for (var index in vals) {
                                    var v = vals[index];
                                    currentProposals.push({id: v.id, val: v.val});
                                    var element = $('<li></li>')
                                        .addClass('proposal')
                                        .click(function () {
                                            params.inputSet(input, currentProposals, index);
                                            proposalList.empty();
                                            params.onSubmit(input);
                                        })
                                        .mouseenter(function () {
                                            $(this).addClass('selected');
                                        })
                                        .mouseleave(function () {
                                            $(this).removeClass('selected');
                                        });

                                    var html = "<span class='search-val'>" + v.val + "</span>";
                                    if (index > proposalList.find('li').size() - 1) {
                                        element.html(html);
                                        proposalList.append(element);
                                    } else {
                                        proposalList.find('li').eq(index).html(html);
                                    }
                                }
                                if (vals.length < proposalList.size()) {
                                    proposalList.find('li:gt(' + (vals.length - 1) + ')').remove();
                                }
                            }
                        }
                    );
                }
                var pageReturn = $('#pageReturn', searchPage);
                pageReturn.on('click', function (e) {
                    // searchPage.offCanvas('close');
                    $('body').css('overflow', 'auto');
                    searchPage.removeClass('page-in');
                    e.preventDefault();
                });
                $.ajax({
                    url: "kwText.html",
                    context: document.body,
                    dataType: 'html',
                    //  async:false,
                    success: function (html) {
                        searchPage.append(html);
                    }
                });
            }

        });

    }
});
/* 搜索 end*/


$(function () {
    $('.am-offcanvas').on('touchmove', function (e) {
        e.preventDefault();
        e.stopPropagation();
    });
});