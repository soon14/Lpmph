
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
            if(defaults.bindevent){
            	defaults.bindevent();
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
            
            //此处渲染完

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

        _this.iScroll = new $.AMUI.iScroll('#'+element, {
            click: true,
            preventDefault:defaults.preventDefault
        });

        /*document.addEventListener('touchmove', function (e) {
            e.preventDefault();
        }, false);*/
        $(window).on('touchmove.iscroll', function (e) {
            e.preventDefault();
        }, false);


        var myScroll = _this.iScroll;
        if(defaults.isAjax){//存在ajax请求的情况
            initParam();
            this.renderList(this.page);
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
            myScroll.off('scrollEnd');
            myScroll.on('scrollEnd', function() {
                //下拉
                if (pullFormTop && this.directionY === -1 &&$pullDown.size()>=1) {
                   console.log(444444);
                    _this.handlePullDown();
                }
                //上拉
                pullFormTop = false;
                if(this.y == this.maxScrollY && $pullUp.size()>=1){
                    _this.handlePullUp();
                }
            });
        }else{ //非列表的刷新
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
        this.prev = this.next = 1;
        this.renderList(this.prev, 'refresh');
        $pullUpLabel.text("上拉加载更多");
    };

    this.handlePullUp = function() {
        $pullUp.find('.am-icon-spin').show();
        $pullUpLabel.text("正在加载");
        if (this.next < this.total) {
        	this.next =  parseInt(this.next) + parseInt(this.count);
            console.log("this.next = "+this.next);
            this.renderList(this.next, 'load');
        } else {
            $pullUpLabel.text(noDataTxt);
            $pullUp.find('.am-icon-spin').hide();
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
        });
        if(aHi<=$('#'+element).height()){
            $list.height($('#'+element).outerHeight()+20);
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
        defaults= $.extend(defaults,opt);
        initParam();
        this.renderList(this.page);
    };

    this.init();
};

//获取URL
var constants = {
    	CATEGORY_ROOT_URL : GLOBAL.WEBROOT + "/search/asyncData/getRootNodes",     // 分类树正常获取节点URL地址.
    	CATEGORY_NODE_URL : GLOBAL.WEBROOT + "/search/asyncData/getNodes"	 // 分类树搜索功能获取节点URL地址.
    };
//分类对象
var GdsCategory = {
	initCategory : function(){
		var category = $(".ser-aType");
		category.append(GdsCategory.getULHtml("root"));
	},
	getULHtml : function(id){
		var url = "";
		if(id == "root"){
			url = constants.CATEGORY_ROOT_URL;
		}else{
			url = constants.CATEGORY_NODE_URL;
		}
		var param = {};
		param.catgType = 1;
		param.catlogId = "";
		param.id = id;
		var html = "<ul>";
		var nodes = "";
		$.eAjax({
			url:url,
			dataType : "text",
			async : false,
			data : param,
			success : function(data) {
				if(data != "" && data != null){
					nodes = eval(data);
				}
			},
			error: function(){
				alert("error");
			}
		});
		if(nodes != ""){
			for(var i in nodes){
				var node = nodes[i];
				html += "<li><div id="+node.id+">"+node.name+"</div></li>";
			}
		}
		html += "</ul>";
		return html;
	},
	setChildUL : function(id){
		var $this = $("#"+id);
		if(!$this.hasClass("loaded")){
			$this.addClass("loaded")
			var UlHtml = GdsCategory.getULHtml(id);
			$this.after(UlHtml);
		}
	}
}
/* 搜索 begin*/
$(function(){
	GdsCategory.initCategory();
    var needSerInput=$('#needSearch');
    if(needSerInput.size()>0){
    	var searchShopInner = $("#searchShopInner").val();
    	var html = "";
    	if(searchShopInner=="true"){
        	html = '<div id="searchPage" class="ui-page-contanier">' +
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
            '<div class="am-header-left  am-header-nav">' +
            //'<a href="javascript:" onclick="history.go(-1); ">'+
            '<i class="public-arrowleft" id="pageReturn"></i>'+
            //'</a>'+
            //'<span id="pageReturn" class="ui-arrow ui-arrow-left"></span>' +
            '</div>' +
            '</div>' +
            '</header>' +
            '</div>';
    	}else{
        	html = '<div id="searchPage" class="ui-page-contanier">' +
            '<header class="am-header am-header-return am-header-fixed">' +
            '<div class="am-header-title tit-hidden">' +
            '<div id="site-search" class="site-input">' +
            '<span class="input-clear"></span>' +
            '<div class="sel-search">' +
            '<span class="am-icon-search" searchType="1">' +
            '<em>&nbsp商&nbsp品</em>' +
            '</span>';
        	/*if($("#SHOP_SHOW_LOCK").val()=='1'){//开启店铺搜索
        		var shopHtml =  '<span class="am-icon-search" searchType="2">' +
                '<em>店铺</em>' +
                ' <i class="search-arrow"></i>' +
                '</span>' +
                '<ul class="search-list">' +
                '<li searchType="1"><i class="iconfont icon-pro"></i> 商品</li>' +
                '<li searchType="2"><i class="iconfont icon-shop"></i> 店铺</li>'+
                '</ul>' ;
        		if($("#searchType").val()=="2"){
        			html += shopHtml;
        		}else{
	              	 html +=  '<span class="am-icon-search" searchType="1">' +
	                   '<em>商品</em>' +
	                   ' <i class="search-arrow"></i>' +
	                   '</span>' +
	                   '<ul class="search-list">' +
	                   '<li searchType="1"><i class="iconfont icon-pro"></i> 商品</li>' +
	                   '<li searchType="2"><i class="iconfont icon-shop"></i> 店铺</li>' +
	                   '</ul>' ;
        		}
        	}else{
        		 html +=  '<span class="am-icon-search" searchType="1">' ;
        	}*/
            html += '</div>' +
            '</div>' +
            '<div class="am-header-left  am-header-nav">' +
            //'<a href="javascript:" onclick="history.go(-1); ">'+
            '<i class="public-arrowleft" id="pageReturn"></i>'+
            //'</a>'+
            //'<span id="pageReturn" class="ui-arrow ui-arrow-left"></span>' +
            '</div>' +
            '</div>' +
            '</header>' +
            '</div>';
    	}
        $('body').append(searchPageWrap);
        $('.am-icon-search').click(function (e) {
            $(this).parents('.sel-search').toggleClass('open');
            e.stopPropagation();
        });
        $('.search-list>li').click(function (e) {
            $('.sel-search>.am-icon-search>em').html($(this).text());
            $('.sel-search>.am-icon-search').attr('searchType',$(this).attr('searchType'));
            if($(this).attr('searchType')=="2"){
            	if($("#shopId").val()!=""){
            		$("#searchShopInner").val("true");
            	}
            	$("#searchType").val("2");
            }else{
            	if($("#shopId").val()!=""){
            		$("#searchShopInner").val("false");
            	}
            	$("#searchType").val("1");
            }
            $(".proposal-list").empty();
            $('.sel-search').removeClass('open');
            e.stopPropagation();
        });
    	
	    var searchPageWrap = $(html);
        $('body').append(searchPageWrap);
        $('.am-icon-search').click(function (e) {
            $(this).parents('.sel-search').toggleClass('open');
            e.stopPropagation();
        });
        $('.search-list>li').click(function (e) {
            $('.sel-search>.am-icon-search>em').html($(this).text());
            $('.sel-search>.am-icon-search').attr('searchType',$(this).attr('searchType'));
            if($(this).attr('searchType')=="2"){
            	if($("#shopId").val()!=""){
            		$("#searchShopInner").val("true");
            	}
            	$("#searchType").val("2");
            }else{
            	if($("#shopId").val()!=""){
            		$("#searchShopInner").val("false");
            	}
            	$("#searchType").val("1");
            }
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
                var a = Math.random();
                var size = Math.ceil(a)*10+2
                if($("#searchShopInner").val()!="true" && $("#searchType").val()=="1"){
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
                            	 var size_temp = $('#size').val();
                                 var a = Math.random();
                                 var size = Math.ceil(a*10)+2;
                                 if(size!=size_temp){
                                	 $('#size').val(size);
                                 }else{
                                	 $('#size').val(size+1);
                                 }
                                 $.ajax({
                                     url: GLOBAL.WEBROOT + "/search/hotkeyword/exchange",
                                     data :{
                                    	 'pageNo':$('#kw-pageNo').val(),
                                    	 'size' : $('#size').val()
                                    	 },
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
                }
                $('#searchTxt').val($('#needSearch').val());
            }

        });

    }
});
/* 搜索 end*/

$(function(){
   $('#allType').on("click",function(){
       $('#allTypeCont').offCanvas('open');
   });
    $('#searchBtn').click(function () {
        search($('#needSearch').val());
    });
    
    $('.ser-aType div').on("click",function(){
        var aType=$(this).parents('.ser-aType');
        var pLi=$(this).parent('li');
        var id = $(this).attr("id");
        GdsCategory.setChildUL(id);
        aType.find('.selected').removeClass('selected');
        aType.parents('li').siblings('li').find('.open').removeClass('open');
        $(this).addClass('selected');
        if( pLi.find('ul').size()>0){
            pLi.toggleClass('open');
        }
    })
});

/**
 * 搜索
 */
var search=function(keyword){
    keyword=$.trim(keyword);
    var url = "";
    if($("#searchShopInner").val()=="true"){
    	 url=encodeURI(GLOBAL.WEBROOT+"/search?shopId="+$("#shopId").val()+"&keyword="+keyword+"&searchShopInner="+$("#searchShopInner").val());
    }else{
    	var searchType = $('.sel-search>.am-icon-search').attr('searchType');
        url=encodeURI(GLOBAL.WEBROOT+"/search?keyword="+keyword);
        if(searchType=="1"||$("#searchType").val()!="2"){//商品搜索
        	url=encodeURI(GLOBAL.WEBROOT+"/search?keyword="+keyword);
        }else if(searchType=="2" || $("#searchType").val()=="2"){//店铺搜索
        	url=encodeURI(GLOBAL.WEBROOT+"/shopsearch?keyword="+keyword);
        }
    }
    window.location.href = url;
};

