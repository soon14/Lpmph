/* ===========================================================
 * b.page.js
 * ===========================================================
 * Terry 
 * created : 2015.08.18
 * 
 * 基于bootstrap风格的分页插件
 * 
 * 更新记录：
 * 2015.09.09 - 解决pageSize=undefined的问题
 * 2015.10.8  - 解决每页记录数下拉选中的值的问题；
 *            - 增加部分初始参数从 data- 属性中获取；
 * ========================================================== */
!function ($) {
	"use strict"; // 使用严格模式Javascript
	//内部对象定义
	var bPage =  {
		//绑定事件的名称，使用了bPage的命名空间
		eventName : 'click.bPage',
		//初始化分页
		init : function(e,p){
			var _this = this;
			var htmlstr = '<div class="pagination bPage pagination-right">';
			htmlstr+='<ul>';
			htmlstr+='<li id="bPageList" class="disabled bPageList"><a><label>page size <select id="bPageDropList"></select></label></a></li>';
			var pageInfo = 'page showing ';
			pageInfo += p.pageNumber + '/' + p.totalPage + '';
			pageInfo += '（total '+p.totalRow+' records）';
			htmlstr+='<li id="bPageInfo" class="disabled bPageInfo"><a>'+pageInfo+'</a></li>';
			htmlstr+='<li id="bPageFirstPage"><a href="javascript:void(0);">first</a></li>';
			htmlstr+='<li id="bPagePreviousPage"><a href="javascript:void(0);">«</a></li>';
			htmlstr+='<li id="bPageNextPage"><a href="javascript:void(0);">»</a></li>';
			htmlstr+='<li id="bPageLastPage"><a href="javascript:void(0);">last</a></li>';
			htmlstr+='</ul>';
			htmlstr+='</div>';
			$(e).append(htmlstr);
			//设置分页设置每页显示条数的下拉列表
			if(p.pageSizeMenu && $.isArray(p.pageSizeMenu) && p.pageSizeMenu.length > 0){
				$.each(p.pageSizeMenu,function(i,row){
					if(row == p.pageSize){
						$('#bPageDropList',$(e)).append('<option selected="true">'+row+'</option>');
					} else {
						$('#bPageDropList',$(e)).append('<option>'+row+'</option>');
					}
					
				});
			}
			$('#bPageDropList',$(e)).on('change',function(event){
				var fn = _this.setPageClick(event,p, 1, Number($(this).val()));
				fn();
			});
			
			this.populate(e,p);
		},
		//数据填充
		populate : function(e,p){
			var i, j, _class, _start, _end, _url, _half=Math.floor(p.pageBarSize/2);

			//总页数小于显示页码个数
			if(p.totalPage < p.pageBarSize) {
				_start = 1;
				_end = p.totalPage;
			//当前页码小于显示页码个数的一半
			} else if ( p.pageNumber <= _half ) {
				_start = 1;
				_end = p.pageBarSize;
			//当前页码大于等于总页数减去显示页码个数一半的值
			} else if ( p.pageNumber >= (p.totalPage - _half) ) {
				_start = p.totalPage - p.pageBarSize + 1;
				_end = p.totalPage;
			//常规情况
			} else {
				_start = p.pageNumber - _half;
				_end = _start + p.pageBarSize - 1;
			}
			
			//移除分页控制按钮除外的所有页码
			$('li:not(#bPageList,#bPageInfo,#bPageFirstPage,#bPagePreviousPage,#bPageNextPage,#bPageLastPage)',$(e)).remove();
			
			for ( j=_start ; j<=_end ; j++ ) {
				_class = (j==p.pageNumber) ? 'class="active"' : '';
				//_url = this.setUrl(p,j);
				var curPage = $('<li '+_class+'><a href="javascript:void(0);">'+j+'</a></li>').insertBefore($('#bPageNextPage', $(e)));
				if(j!=p.pageNumber) this.setFunction($(curPage), p, j);
			}
			
			// 处理静态控制按钮样式及链接
			var _fUrl,_pUrl,_nUrl,_lUrl;
			var _fNum,_pNum,_nNum,_lNum;
			if ( p.pageNumber === 1 ) {
				$('#bPageFirstPage,#bPagePreviousPage', $(e)).addClass('disabled');
				_fNum = -1;
				_pNum = -1;
			} else {
				$('#bPageFirstPage,#bPagePreviousPage', $(e)).removeClass('disabled');
				_fNum = 1;
				_pNum = p.pageNumber>1?p.pageNumber-1:1;
			}
			this.setFunction($('#bPageFirstPage a',$(e)), p, _fNum);
			this.setFunction($('#bPagePreviousPage a',$(e)), p, _pNum);

			if ( p.pageNumber === p.totalPage || p.totalPage === 0 ) {
				$('#bPageNextPage,#bPageLastPage', $(e)).addClass('disabled');
				_nNum = -1;
				_lNum = -1;
			} else {
				$('#bPageNextPage,#bPageLastPage', $(e)).removeClass('disabled');
				_nNum = p.pageNumber<p.totalPage?p.pageNumber+1:p.totalPage;
				_lNum = p.totalPage;
			}
			this.setFunction($('#bPageNextPage a',$(e)), p, _nNum);
			this.setFunction($('#bPageLastPage a',$(e)), p, _lNum);
		},
		//设置页码块上的URL
		setUrl : function(p,pageNumber,pageSize){
			var str ='';
			if(p.url){
				var str = p.url + '?1=1';
				if(typeof(pageNumber)!='undefined' && typeof(pageNumber) == 'number'){
					str += '&pageNumber=' + pageNumber;
				}else{
					str += '&pageNumber=' + p.pageNumber;
				}
				if(typeof(pageSize)!='undefined' && typeof(pageSize) == 'number'){
					str += '&pageSize=' + pageSize;
				}else{
					str += '&pageSize=' + p.pageSize;
				}
				//总页数、总记录通常是由后台传递给前台，前台没必要传递给后台
				//str += '&totalRow=' + p.totalRow;
				//str += '&totalPage=' + p.totalPage;
				str += this.getParamsStr(p, pageNumber, pageSize);
			}else{
				str = 'javascript:void(0);';
			}

			return str;
		},
		//获得业务参数的URL字符串
		getParamsStr : function(p,pageNumber,pageSize){
			var str='';
			if(p.params && $.isFunction(p.params)){
				var pa = p.params(),attr;
				if($.isPlainObject(pa)){
					for(attr in pa){
						str += '&' + attr + '=' + pa[attr];
					}					
				}
			}
			return str;
		},
		//设置参数对象，用于在执行ajax请求时执行的内容
		getParams : function(p,pageNumber,pageSize){
			var param = {};
			if(typeof(pageNumber)!='undefined' && typeof(pageNumber) == 'number'){
				param.pageNumber = pageNumber;
			}else{
				param.pageNumber = p.pageNumber;
			}
			if(typeof(pageSize)!='undefined' && typeof(pageSize) == 'number'){
				param.pageSize = pageSize;
			}else{
				param.pageSize = p.pageSize;
			}
			if(p.params && $.isFunction(p.params)){
				var pa = p.params();
				if($.isPlainObject(pa)){
					param = $.extend({},param ,pa);
				}
			}
			return param;
		},
		//设置页面点击事件处理
		//event：事件对象
		//若pageNumber参数为-1，而设置当前页不处理操作
		setPageClick : function(event, p,pageNumber,pageSize){
			if(event){
				event.preventDefault();
			}
			var _this = this;
			if(typeof(pageNumer)!=undefined && typeof(pageNumber)=='number' && pageNumber == -1) return $.noop;
			var fn = function(){
				//异步刷新页面模式
				if(p.asyncLoad){
					var param = _this.getParams(p, pageNumber, pageSize);
					$.eAjax({
						url : p.url,
						data : param,
						dataType : 'text',//使用了html会导致数据读取后，不执行success内部的问题
						success : function(returnData){
							if($(p.asyncTarget).size()>0){
								$(p.asyncTarget).empty().append(returnData);
							}
						}
					});
				//直接跳转模式
				}else{
					window.location.href = _this.setUrl(p, pageNumber, pageSize);
				}
			};
			return fn;
		},
		//设置事件
		setFunction : function(obj,p,pageNumber,pageSize){
			var _this = this;
			$(obj).off(this.eventName).on(this.eventName,function(event){
				var fn = _this.setPageClick(event, p, pageNumber, pageSize);
				fn();
			});
		},
		
		//提供给外面做翻页的动作；
		dopage:function(p){
			var _this = this;
			var fn = _this.setPageClick(false,p);
			fn();
		}
	};
	
	

	//分页插件入口
	$.fn.bPage = function(p){
		return this.each(function(){
			var $this = $(this),
				data = $this.data('bPage'),
				params = $.extend({}, $.fn.bPage.defaults, $this.data(), typeof p == 'object' && p);
			$this.data('bPage', params);
			bPage.init(this, params);
			
		});
	};
	
	$.fn.bPageRefresh = function(p){
			return this.each(function(){
				var $this = $(this);
				var data = $this.data('bPage');
				var params = $.extend({}, $.fn.bPage.defaults, data, typeof p == 'object' && p);
				bPage.dopage(params);
			});
	};
	//默认参数
	$.fn.bPage.defaults = {
		//每页显示记录数
        "pageSize"       : 10,
        //当前页号
        "pageNumber"     : 1,
        //总记录个数
        "totalRow"       : 0,
        //总页数
        "totalPage"      : 1,
        //显示页码个数，建议使用奇数
        "pageBarSize"    : 5,
        //每页显示记录数设置
        "pageSizeMenu"   : [10,20,50,100],
        //业务参数集，参数为function，function的返回值必须为Object格式：{a:1,b:2,……}
        "params"         : false,
        //异步处理分页
        "asyncLoad"      : false,
        //异步处理对象容器，允许使用jquery表达式
        "asyncTarget"    : 'body',
        //分页跳转URL
        "url"            : 'aaaaa'
	};
}(window.jQuery);