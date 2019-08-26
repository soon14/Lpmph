/*
 * File：e.grid.js
 * Function：基于datatables插件进行二次封装的处理
 * Author：Terry
 * Create：2012.11.27 
 * 
 * Change Log:
 * 2014.12.30
 * 修复gridReload方法中不会使用当前参数重载表格的问题
 * 新增保存表格数据状态功能
 * 2014.12.31
 * 完善分页信息栏显示内容，增加当前页，总页数显示
 */
(function($) {
	var eGrid = {
		//默认参数集
		defaults : {
			//自定义属性值
			'pCheck' : 'single', //false:不允许选中行 single:单行选中 multi:多行选中
			'pCheckColumn' : true, //是否显示单选/复选框的列
			'pCheckRow' : true,//点击行是否显示高亮
			'pSingleCheckClean' : true, // 单选模式下，选中行后，再次点击行是否选中行true：清除选中false:不清除选中
			'pColDrag' : false,// 列是否允许被移动
			
			'pTableTools' : false,// 表格工具集，包含复制，打印，导出excel，PDF等功能
			
			'pAutoload' : true,// 是否自动读取数据
			'pLengthMenu' : [ 10, 25, 50, 100 ], //每页显示记录数可选下拉列表设置
			'pJavaModel' : false, //后台参数前缀
			'pRequestType' : 'POST',//请求类型，默认为POST
			'pIdColumn' : 'id',//指定结果集中的ID字段，默认为'id'
			'pTableClass' : 'table table-striped table-bordered dTableR',//表格样式设置，默认为Bootstrap的样式
			'params' : false, //后台查询参数，参数格式：[{name:'a',value:'aaa'},{name:'b',value:'bbb'},{……}]
			
			'pForwardPage' : -1,//目标转向页数
			'pPageSize' : 10,//设置每页显示记录数
			'pSortColumn' : '', //排序字段
			'pSortOrder' : '', //排序顺序
			'pQueryType' : '', //
			'pQueryString' : '', //
			
			'eClick' : $.noop, // 行单击事件
			'eDbClick' : $.noop, // 行双击事件；参数（rowData行数据,rowElement行元素【TR】）
			'eDrawComplete' : $.noop, // 表格数据处理结束后的事件回调（每一次）
			
			//DataTables自带参数修改默认值
			'bRetrieve' : true, //若DataTables对象已存在，是否不进行初始化，直接返回已存在的DataTables对象
			'onSuccess' : false, //DataTables初始化完成回调
	        //国际化支持：中文   当前显示第 1 / 1 页 （共3条记录）
			'oLanguage' : {
				"sLoadingRecords": "努力抓取数据中...",
				"sProcessing": "努力处理数据中...",
				"sLengthMenu": "每页 _MENU_ 条",
				"sZeroRecords": "没有任何数据返回",
				"sInfo": "当前显示第 <strong>_PAGE_</strong> / <strong>_PAGES_</strong> 页 （共<strong>_TOTAL_</strong>条记录）",//共 <strong>_TOTAL_</strong> 条记录
				"sInfoEmpty": "0 到 0 共 0 条记录",
				"sInfoFiltered": "(已从 _MAX_ 条记录中过滤)",
				"sInfoPostFix": "",
				"sSearch": "快速搜索",
				"oPaginate": {
					"sFirst":    "首页",
					"sPrevious": "«",
					"sNext":     "»",
					"sLast":     "尾页"
				}
	            //"sUrl": $webroot + "framework/jsLib/jquery/datatables/language/zh_CN.txt"
	        },
	        'bProcessing' : false //是否显示正在处理中的文本
		},
		//常量
		constants : {
			dataTableObjName : 'dtName',
			checkSingle : 'single',
			checkMulti : 'multi',
			checkIdPrefix : 'dt_rowcheck_',
			selectedClass : 'row_selected',
			selectedTrClass : 'tr.row_selected',
			rowIdColumn : 'DT_RowId',
			rowClassColumn : 'DT_RowClass',
			emptyData : {'totalRow':0,'page':1,'list':[],'iTotalRecords':0,'iTotalDisplayRecords':0}
		},
		//设置表格行选中行为
		//obj：表格对象
		//check：选中类型，设置参考defaults.check的说明
		_setRowCheck : function(obj,p){
			if(p.pCheck){
				var c = eGrid.constants;
				if(p.pCheck==c.checkSingle){
					if(p.pCheckRow){//判断是否允许点击行任意位置使得行被选中
						$("tbody tr",$(obj)).click(function(e){//设置行点击事件
							if($(this).hasClass(c.selectedClass)){
								if(p.pSingleCheckClean){//单选模式下选中行后再次单击行清除选中
									$(this).removeClass(c.selectedClass);
									if(p.pCheckColumn) $(":radio[id^='"+c.checkIdPrefix+"']",$(this)).prop('checked',false);
								}
							} else {
								var dobj = $(obj).data(c.dataTableObjName);
								dobj.$(c.selectedTrClass).removeClass(c.selectedClass);
								$(this).addClass(c.selectedClass);
								if(p.pCheckColumn){
									$(":radio[id^='"+c.checkIdPrefix+"']:checked",$(obj)).prop('checked',false);//清除其它已选中的
									$(":radio[id^='"+c.checkIdPrefix+"']",$(this)).prop('checked',true);
								}
							}
						});
					}

					if(p.pCheckColumn){//设置单选框点击事件
						$(":radio[id^='"+c.checkIdPrefix+"']",$(obj)).click(function(e){
							e.stopPropagation();
							$(":radio[id^='"+c.checkIdPrefix+"']:checked",$(obj)).prop('checked',false);
							$(c.selectedTrClass,$(obj)).removeClass(c.selectedClass);
							$(this).closest('tr').addClass(c.selectedClass);
							$(this).prop('checked',true);
						});
						$("thead th:eq(0)",$(obj)).empty().append('选择');
					}
				}else if(p.pCheck==c.checkMulti){
					var checkCheckAll = function(){
						if($(":checkbox[id^='"+c.checkIdPrefix+"']",$(obj)).size() == $(":checkbox[id^='"+c.checkIdPrefix+"']:checked",$(obj)).size()){
							$("#dt_row_all_check",$(obj)).prop('checked',true);
						}else{
							$("#dt_row_all_check",$(obj)).prop('checked',false);
						}
					};
					if(p.pCheckRow){//判断是否允许点击行任意位置使得行被选中
						$("tbody tr",$(obj)).click(function(e){//设置行点击事件
							$(this).toggleClass(c.selectedClass);
							if(p.pCheckColumn){
								var checkObj = $(":checkbox[id^='"+c.checkIdPrefix+"']",$(this));
								$(checkObj).prop('checked',!$(checkObj).prop('checked'));
								checkCheckAll();
							}
						});
					}
					if(p.pCheckColumn){//设置复选框点击事件
						$(":checkbox[id^='"+c.checkIdPrefix+"']",$(obj)).click(function(e){
							e.stopPropagation();
							$(this).closest('tr').toggleClass(c.selectedClass);
							checkCheckAll();
						});
						$("thead th:eq(0)",$(obj)).empty().append('<input type="checkbox" id="dt_row_all_check" title="全选/全取消">').attr('align','center').css({
							'vertical-align':'middle',
							'text-align' : 'center',
							'padding' : '0px'
						});//清空标题栏
						$("#dt_row_all_check",$(obj)).css('margin','0px').click(function(e){
							if($(this).prop('checked')){
								$("tbody tr",$(obj)).addClass(c.selectedClass);
								$(":checkbox[id^='"+c.checkIdPrefix+"']",$(obj)).prop('checked',true);
							}else{
								$("tbody tr",$(obj)).removeClass(c.selectedClass);
								$(":checkbox[id^='"+c.checkIdPrefix+"']",$(obj)).prop('checked',false);
							}
						});
					}
				}
			}
		},
		// 为表格设置事件绑定
		// obj:表格对象
		// p:参数集
		_bindEvent : function(obj,p){
			if(!obj) return;			
			$("tbody tr",$(obj)).click(function(e){
				if(p.eClick && $.isFunction(p.eClick)){
					var g = $(obj).data(eGrid.constants.dataTableObjName);
					p.eClick(g.fnGetData(this),$(this));
				}
			}).dblclick(function(e){
				if(p.eDbClick && $.isFunction(p.eDbClick)){
					var g = $(obj).data(eGrid.constants.dataTableObjName);
					p.eDbClick(g.fnGetData(this),$(this));
				}
			});
		},
		//获得选中行，一个或多个
		//obj：表格对象(jquery)
		_getSelectedRow : function(obj){
			if(!obj) return ;
			var c = eGrid.constants;
			var dobj = $(obj).data(c.dataTableObjName);
			return dobj.$(c.selectedTrClass);
		},
		//获得所有行
		_getAllRows : function(obj){
			if(!obj) return ;
			var c = eGrid.constants;
			var dobj = $(obj).data(c.dataTableObjName);
			return dobj.$('tbody tr');
		},
		//设置后台查询参数
		_setQueryParams : function(p,oSettings){
        	var prefix = '';
        	if(p.pJavaModel) prefix = p.pJavaModel + '.';
			
        	var cPage = Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength );
			//Grid查询参数
			var params = [{
				name : prefix + 'page', value : !isNaN(cPage)?cPage+1:1 //
			}, {
				name : prefix + 'pageSize', value : oSettings._iDisplayLength //iDisplayLength
			}, {
				name : prefix + 'sortname', value : p.pSortColumn
			}, {
				name : prefix + 'sortorder', value : p.pSortOrder
			}, {
				name : prefix + 'query', value : p.pQueryString
			}, {
				name : prefix + 'qtype', value : p.pQueryType
			}];
			if (p.params)copyProperties(params, p.params);
			return params;
		},
		//更新分页信息
		_updatePageInfo : function(params,oSettings,aoData){
			var prefix = oSettings.oInit.pJavaModel;
			if(prefix) prefix += '.';
			if(typeof(oSettings.oInit.pPageSize)!='undefined' && typeof(oSettings.oInit.pPageSize)=='number'){
				oSettings._iDisplayLength = oSettings.oInit.pPageSize;
			}
			var curPage = Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength );
			//设置指定跳转页
			if(oSettings.oInit.pForwardPage!=-1){
				curPage = oSettings.oInit.pForwardPage -1;
				oSettings._iDisplayStart = (oSettings.oInit.pForwardPage -1) * oSettings._iDisplayLength;
				if(curPage < 0) curPage = 0;
				oSettings.oInit.pForwardPage = -1;
			}
			var sortname ='',sortindex=0,sortorder = '',sColumns;
			if(aoData){
				$.each(aoData,function(i,n){
					if(n.name && n.value && n.name=='sColumns') sColumns = n.value;
					if(n.name && n.value && n.name=='iSortCol_0') sortindex = n.value;
					if(n.name && n.value && n.name=='sSortDir_0') sortorder = n.value;
				});
				
				//处理默认排序
				if(sortindex!=null && sortindex!='' && sortindex!=undefined && sColumns){
					sortname = sColumns.split(',')[sortindex];
				}
				if(sortname && !sortorder) sortorder = 'asc';
			}
			$.each(params,function(i,n){
				if(n.name == prefix + 'pageSize') params[i].value = oSettings._iDisplayLength;//更新每页显示记录数
				if(n.name == prefix + 'page') params[i].value = !isNaN(curPage)?curPage+1:1;//更新当前页
				if(n.name == prefix + 'sortname' && sortname) params[i].value = sortname;//更新排序字段
				if(n.name == prefix + 'sortorder' && sortname) params[i].value = sortorder;//更新排序方式
			});
		},
		//根据URL内容返回当前连接符号应该是?还是&
		_getUrlConnCode:function(url){
			return url.indexOf('?')!=-1 ? '&' : '?';
		},
		//获得跳转式分页的跳转URL
		_getReDirectUrl : function(oSettings){
			var redirect = oSettings.sAjaxSource;
			var initParams = oSettings.initParams;
			var params = eGrid._setQueryParams(initParams, oSettings);
			$.each(params,function(i,row){
				redirect += eGrid._getUrlConnCode(redirect) + row.name + '=' + row.value;
			});
			return redirect;
		},
		//设置遮罩中文字的居中对齐
		_setProcessPadding : function(tb){
			var pDiv = $(tb).prev('.dataTables_processing');
			var pDivHeight = $(pDiv).height();
			alert(pDivHeight);
			var fontsize = $(pDiv).css('font-size');
			$(pDiv).css('padding-top',pDivHeight / 2);
		}
	};
	
	
	
	$.fn.extend({
		//初始化DataTables - Ajax读取数据方式
		initDT : function(p){
			var $this = $(this);
			var c = eGrid.constants;
			p = $.extend({}, eGrid.defaults, p);
			//设置Table样式
			if(!$this.hasClass(p.pTableClass)) $this.addClass(p.pTableClass);
			
			//是否显示选择列
			if(p.pCheckColumn){
				if(!p.aoColumns) p.aoColumns = new Array();
				p.aoColumns.unshift({
					"mData" : "",
					"sDefaultContent" : "",
					"sTitle":"选择",
					"sWidth":"35px",
					"sClass" : "center selectColumn",
					"bSortable" : false,
		            "mRender": function ( data, type, row ) {//自定义拼装列的内容
		            	if(p.pCheck == c.checkMulti){//多选模式
		            		return '<input type="checkbox" id="'+c.checkIdPrefix + row.id + '" />';
		            	}else if(p.pCheck == c.checkSingle){//单选模式
		            		return '<input type="radio" id="'+c.checkIdPrefix + row.id + '" />';
		            	}
		            }
				});
				if(p.aoColumnDefs){
					$.each(p.aoColumnDefs,function(i,n){
						if(n.aTargets && $.isArray(n.aTargets) && n.aTargets.length>0){
							$.each(n.aTargets,function(ii,nn){
								p.aoColumnDefs[i].aTargets[ii] = nn+1;
							});
						}
					});
				}				
			}
			
			/* 设置默认排序  */
			// 排除选择列和隐藏列
			var defaultSortIndex = 0;
			$.each(p.aoColumns,function(i,n){
				if(n.bVisible == undefined || n.bVisible == true){
					if(p.pCheckColumn && i!=0){
						defaultSortIndex = i;
						return false;
					}					
					if(!p.pCheckColumn){
						defaultSortIndex = i;
						return false;
					}
				}
			});
			p.aaSorting = [[defaultSortIndex , 'asc']];
			/* 设置默认排序  */
			
			var sDom = 'rt<lip<"clear">>';			
			
			// 设置表格工具集
			if(p.pTableTools){
				p.tableTools = {
					"aButtons": [
					    {"sExtends":"copy","sButtonText":"复制"},
					    {"sExtends":"print","sButtonText":"打印"},
					    {"sExtends":"xls","sButtonText":"导出Excel"},
					    {"sExtends":"pdf","sButtonText":"导出PDF"}
					],
					"sSwfPath": $webroot+"framework/jsLib/jquery/datatables/extras/TableTools/swf/copy_csv_xls_pdf.swf"
				};
				sDom = 'rt<<"span3"T>lip<"clear">>';
			}
			/* 列拖动 */
			if(p.pColDrag) sDom = 'R' + sDom;
			
			/* 设置每页显示记录数下拉列表 */
			if(p.pLengthMenu && $.isArray(p.pLengthMenu) && p.pLengthMenu.length > 0){
				p.aLengthMenu = p.pLengthMenu;
			}
			
			p = $.extend({}, {
				//处理DataTables服务端数据处理完成后的事件回调
				'fnInitComplete' : function(oSettings,json){
					var pageLengthBar = $(".dataTables_length label",$this.next());
					var pageInfoBar = $(".dataTables_info",$this.next());
					var pageCtrl = $(".dataTables_paginate",$this.next());
					$(pageInfoBar).css('margin-left','0px');
					$("#dt_pageinfo_list a",pageCtrl).append(pageLengthBar).height(20);
					$("#dt_pageinfo_count a",pageCtrl).append(pageInfoBar).height(20);
					if(p.onSuccess && $.isFunction(p.onSuccess)) p.onSuccess(oSettings,json);
		        	//设置提示遮罩内边距高度
		        	if(p.bProcessing) eGrid._setProcessPadding($this);
		        },
		        //当每次渲染完表格后做的回调事件
		        'fnDrawCallback': function( oSettings ) {
		        	var pageCtrl = $(".dataTables_paginate",$this.next());
		        	
		        	var pageCount = Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength );//总记录数
		        	var cPage = Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ) +1;//当前页
		        	
		        	//$("#dt_pageinfo_last a",pageCtrl).text('..' + pageCount);
		        	
		        	//处理首页
		        	//if(cPage > 3) $("#dt_pageinfo_first",pageCtrl).show();
		        	//else $("#dt_pageinfo_first",pageCtrl).hide();
		        	
		        	//处理尾页
		        	//if((cPage > 5) && (cPage > (pageCount -3)) || pageCount < 5) $("#dt_pageinfo_last",pageCtrl).hide();
		        	//else $("#dt_pageinfo_last",pageCtrl).show();
		        	
		        	//$("#dt_info_page_count",$this.next()).text(pageCount);
		        	
		        	
		        	if(p.pCheck && p.sAjaxSource) eGrid._setRowCheck($this, p);//设置行单选复选处理
		        	eGrid._bindEvent($this, p);//设置事件
		        	if(p.eDrawComplete && $.isFunction(p.eDrawComplete)) p.eDrawComplete();
		        },
		        //服务端数据回调处理
		        'fnServerData' : function ( sSource, aoData, fnCallback, oSettings ) {
		        	//var params = oSettings.queryParams ? oSettings.queryParams : eGrid._setQueryParams(p,oSettings);
		        	var params = eGrid._setQueryParams(p,oSettings);
		        	
		        	if(oSettings.queryParams) params = copyProperties(oSettings.queryParams,params);
		        	oSettings.queryParams = params;//缓存更新分页查询参数后的参数集合
		        	
		        	oSettings.initParams = p.params;//缓存初始化数据查询参数（非表格初始化参数）
		        	eGrid._updatePageInfo(params, oSettings, aoData);//更新分页信息
		        	if(!p.pAutoload){
		        		fnCallback(c.emptyData);
		        		p.pAutoload = true;
		        	}else{
			            oSettings.jqXHR = $.eAjax({
							"dataType": 'json',
							"type": p.pRequestType,
							"url": sSource,
							"data": params,
							"success": function(data){
								var result = data.gridResult;
								if($.isPlainObject(result)){
									//result=EBC.jsonEval(result);
									
									//数据每个数据行增加DT_RowId字段，这是datatables默认自动识别的ID字段
									$.each(result.list,function(i,row){
										if(row && row[p.pIdColumn] && row[p.pIdColumn]!==0){
											eval('row.'+c.rowIdColumn+'="'+row[p.pIdColumn]+'"');
										}else{
											eval('row.'+c.rowIdColumn+'="'+ i +'"');
										}
									});
									
									result.iTotalRecords = result.totalRow;
									result.iTotalDisplayRecords = result.totalRow;
								}
								fnCallback(result);
							}
			            });
		        	}

		        },
		        'autoWidth': false,//设置是否自适应宽度，在部分bootstrap主题中宽度不正常的时候试着修改该属性
		        'bServerSide' : true, //是否服务端请求
		        'sAjaxDataProp' : "list", //服务端返回数据的json节点
		        "sPaginationType": "bootstrap_full",//使用bootstrap风格的样式
		        "stateSave" : true,//保存表格状态
		        "sDom": sDom,// 修改界面排版，将每页显示记录数移动到底部分页条
		        //提交方式
		        'sServerMethod' : 'POST'
			}, p);
			return this.each(function(){
				var dobj = $(this).dataTable(p);
				//缓存表格对象
				$(this).data(c.dataTableObjName,dobj);
				$(this).removeClass('no-footer');
			});
		},
		//使用自行渲染Table后，再初始化DataTables的方式
		initDT_DOM : function(p){
			var $this = $(this);
			var c = eGrid.constants;
			p = $.extend({}, eGrid.defaults, p);
			p = $.extend({}, {
				//处理DataTables服务端数据处理完成后的事件回调
				'fnInitComplete' : function(oSettings,json){
					if(p.onSuccess && $.isFuction(p.onSuccess)) p.onSuccess(oSettings,json);
		        },
				"sPaginationType": "bootstrap_redirect"//使用bootstrap风格的样式，这种风格的分页条是自定义的，其中的分页处理是直接跳转的方式
			}, p);
			return this.each(function(){
				//if(p.pCheck && p.pCheck==c.checkMulti && !p.sAjaxSource) eGrid._setRowCheck($(this), p.pCheck); 
				var dobj = $(this).dataTable(p);
				oSettings = dobj.fnSettings();
				//oSettings._iRecordsTotal = 100;
				//oSettings._iRecordsDisplay = 100;
				//dobj.fnDraw();
				//缓存表格对象
				$(this).data(c.dataTableObjName,dobj);
				//if(p.pCheck && p.pCheck==c.checkSingle && !p.sAjaxSource) eGrid._setRowCheck($(this), p.pCheck);
			});
		},
		//根据表单内容查询表格数据(后台)
		gridSearch : function(p){
			return this.each(function() {
				if($.fn.DataTable.fnIsDataTable(this)){
					var g = $(this).data(eGrid.constants.dataTableObjName);
					var setting = g.fnSettings();
					if(setting && setting.queryParams){
						copyProperties(setting.queryParams, p);
						g.fnDraw();
					}
				}
			});
		},
		//刷新表格
		gridReload : function(){
			return this.each(function(){
				if($.fn.DataTable.fnIsDataTable(this)){
					var g = $(this).data(eGrid.constants.dataTableObjName);
					var setting = g.fnSettings();
					g.fnDraw(setting);
				}
			});
		},
		//获得表格的内部对象
		getDTObj : function(){
			return $(this).data(eGrid.constants.dataTableObjName);
		},
		//获得选中行，一个或多个
		getSelectedDOM : function(){
			return eGrid._getSelectedRow($(this));
		},
		//获得选中行数据，一个或多个
		//格式：
		//[{id:1,cell:[xx,xx,...]},
		//{id:2,cell:[xx,xx,...]},
		//{id:3,cell:[xx,xx,...]},
		//{id:4,cell:[xx,xx,...]}]
		getSelectedData : function(){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			var rows = eGrid._getSelectedRow($(this));
			if(rows && rows.size()>0){
				var arr = new Array();
				$.each(rows,function(i,row){
					arr.push(g.fnGetData(row));
				});
				return arr;
			}else{
				return false;
			}
		},
		//获得所有行数据
		//格式：
		//[{id:1,cell:[xx,xx,...]},
		//{id:2,cell:[xx,xx,...]},
		//{id:3,cell:[xx,xx,...]},
		//{id:4,cell:[xx,xx,...]}]
		getAllData : function(){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			var rows = eGrid._getAllRows($(this));
			if(rows && rows.size()>0){
				var arr = new Array();
				$.each(rows,function(i,row){
					arr.push(g.fnGetData(row));
				});
				return arr;
			}else{
				return false;
			}
		},
		//获得当前页号
		getCurrentPage : function(){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			var oSettings = g.fnSettings();
			return Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ) +1;
		},
		//设置下次一查询的指定跳转页数
		setForwardPage : function(page){
			if(typeof(page) == 'number' && page > 0){
				var g = $(this).data(eGrid.constants.dataTableObjName);
				var oSettings = g.fnSettings();
				oSettings.oInit.pForwardPage = page;
			}
		},
		//获得当前设置的每页显示记录数
		getPageSize : function(){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			var oSettings = g.fnSettings();
			return oSettings._iDisplayLength;
		},
		//获得选中行的行下标
		getCheckedRowsIndex : function() {
			var arr = new Array();
			$("tbody tr." + eGrid.constants.selectedClass,$(this)).each(function(i,row){
				arr.push($(this).index());
			});
			return arr;
		},
		//获得选中行的ID数组
		getCheckIds : function(){
			var arr = new Array();
			$("tbody tr." + eGrid.constants.selectedClass,$(this)).each(function(i,row){
				arr.push($(this).attr('id'));
			});
			return arr;
		},
		//手动选中所有行(当前页)
		selectAllRow : function() {
			var $this = $(this);
			var c = eGrid.constants;
			$("tbody tr",$this).addClass(c.selectedClass);
			$(":checkbox[id^='"+c.checkIdPrefix+"']",$this).prop('checked',true);
			$("#dt_row_all_check",$this).prop('checked',true);			
		},
		//取消选中所有行
		unSelectAllRow : function() {
			var $this = $(this);
			var c = eGrid.constants;
			$("tbody tr",$this).removeClass(c.selectedClass);
			$(":checkbox[id^='"+c.checkIdPrefix+"']",$this).prop('checked',false);
			$("#dt_row_all_check",$this).prop('checked',false);
		},
		//下一页
		pageNext : function(){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			g.fnPageChange('next');
		},
		//上一页
		pagePrevious : function(){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			g.fnPageChange('previous');
		},
		//第一页
		pageFirst : function(){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			g.fnPageChange('first');
		},
		//最后一页
		pageLast : function(){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			g.fnPageChange('last');
		},
		// 在页面上添加一行，仅限页面操作   *不可用
		addRow : function(data,id){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			var row = null;
			if(id!=undefined && id!=null && id!=''){
				row = {
					"id" : id,
					"cell" : data,
					"DT_RowId" : id
				};
			}else{ 
				var tid = 'dt_temp_row_id_' + Math.ceil(Math.random() * 100);
				row = {
					"id" : tid,
					"cell" : data,
					"DT_RowId" : tid
				};
			}
			g.fnAddData(row);
		},
		// 在页面上删一行记录，仅限页面操作     *不可用
		removeRow : function(idx){
			var g = $(this).data(eGrid.constants.dataTableObjName);
			if(idx!=undefined && idx!=='' && idx!=null && typeof(idx)=='number' && g){				
				g.fnSettings().oInit.bStaticControl = true;
				g.fnDeleteRow(idx);
				g.fnFilter('a');
				//$("tbody tr:eq("+idx+")",$(this)).remove();
			}
		}
	});
	
	$.extend( $.fn.dataTableExt.oApi, {
		//使用参数集重绘表格
		fnDrawByNewSettings : function(oSettings){
			this.oApi._fnCalculateEnd( oSettings );
			this.oApi._fnDraw( oSettings );
		}
	});
	
	
	/* Bootstrap style pagination control extend by Terry */
	$.extend( $.fn.dataTableExt.oPagination, {
		//在官方提供的分页条基础上，增加首页，尾页的按钮
		"bootstrap_full": {
			"fnInit": function( oSettings, nPaging, fnDraw ) {
				var oLang = oSettings.oLanguage.oPaginate;
				var fnClickHandler = function ( e ) {
					e.preventDefault();
					if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
						fnDraw( oSettings );
					}
				};

				var htmlstr = '<ul>';
				//根据初始化参数处理是否隐藏每页显示记录列表和分页信息框是否显示
				if(oSettings.oFeatures.bLengthChange)
					htmlstr += '<li class="prev disabled" id="dt_pageinfo_list"><a>&nbsp;</a></li>';
				if(oSettings.oFeatures.bInfo)
					htmlstr += '<li class="prev disabled" id="dt_pageinfo_count"><a href="javascript:void(0);">&nbsp</a></li>';
				htmlstr += '<li class="prev disabled" id="dt_pageinfo_first" title="首页"><a href="javascript:void(0);">'+oLang.sFirst+'</a></li>'+
						   '<li class="prev disabled" id="dt_pageinfo_previous"><a href="javascript:void(0);">'+oLang.sPrevious+'</a></li>'+//&larr;
						   '<li class="next disabled" id="dt_pageinfo_next"><a href="javascript:void(0);">'+oLang.sNext+'</a></li>'+//&rarr;
						   '<li class="next disabled" id="dt_pageinfo_last" title="尾页"><a href="javascript:void(0);">'+oLang.sLast+'</a></li>'+//'+oLang.sLast+'
					'</ul>';
				$(nPaging).addClass('pagination').append(htmlstr);
				$('#dt_pageinfo_first',nPaging).bind( 'click.DT', { action: "first" }, fnClickHandler );
				$('#dt_pageinfo_previous',nPaging).bind( 'click.DT', { action: "previous" }, fnClickHandler );
				$('#dt_pageinfo_next',nPaging).bind( 'click.DT', { action: "next" }, fnClickHandler );
				$('#dt_pageinfo_last',nPaging).bind( 'click.DT', { action: "last" }, fnClickHandler );
			},

			"fnUpdate": function ( oSettings, fnDraw ) {
				var iListLength = 5;
				var oPaging = oSettings.oInstance.fnPagingInfo();
				var an = oSettings.aanFeatures.p;
				var i, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);

				if ( oPaging.iTotalPages < iListLength) {
					iStart = 1;
					iEnd = oPaging.iTotalPages;
				} else if ( oPaging.iPage <= iHalf ) {
					iStart = 1;
					iEnd = iListLength;
				} else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
					iStart = oPaging.iTotalPages - iListLength + 1;
					iEnd = oPaging.iTotalPages;
				} else {
					iStart = oPaging.iPage - iHalf + 1;
					iEnd = iStart + iListLength - 1;
				}

				for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
					// Remove the middle elements
					//$('li:gt(0)', an[i]).filter(':not(:last)').remove();
					//$('li:gt(3)', an[i]).filter(':not(#dt_pageinfo_next,#dt_pageinfo_last)').remove();
					$('li:not(#dt_pageinfo_list,#dt_pageinfo_count,#dt_pageinfo_first,#dt_pageinfo_previous,#dt_pageinfo_next,#dt_pageinfo_last)', an[i]).remove();
					// Add the new list items and their event handlers
					for ( j=iStart ; j<=iEnd ; j++ ) {
						sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
						var curPage = $('<li '+sClass+'><a href="javascript:void(0);">'+j+'</a></li>')
						.insertBefore( $('#dt_pageinfo_next', an[i])[0] );
						if(j!=oPaging.iPage+1){
							$(curPage).on('click', function (e) {
								e.preventDefault();
								oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
								fnDraw( oSettings );
							});
						}
					}

					// Add / remove disabled classes from the static elements
					if ( oPaging.iPage === 0 ) {
						$('#dt_pageinfo_first,#dt_pageinfo_previous', an[i]).addClass('disabled');
					} else {
						$('#dt_pageinfo_first,#dt_pageinfo_previous', an[i]).removeClass('disabled');
					}

					if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
						$('#dt_pageinfo_last,#dt_pageinfo_next', an[i]).addClass('disabled');
					} else {
						$('#dt_pageinfo_last,#dt_pageinfo_next', an[i]).removeClass('disabled');
					}
				}
			}
		},
		"bootstrap_redirect": {
			"fnInit": function( oSettings, nPaging, fnDraw ) {
				var oLang = oSettings.oLanguage.oPaginate;
				var fnClickHandler = function ( e ) {
					e.preventDefault();
					if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
						//fnDraw( oSettings );
						window.location.href = eGrid._getReDirectUrl(oSettings);
					}
				};

				$(nPaging).addClass('pagination').append(
					'<ul>'+
						'<li class="prev disabled"><a href="#">&larr; '+oLang.sPrevious+'</a></li>'+
						'<li class="next disabled"><a href="#">'+oLang.sNext+' &rarr; </a></li>'+
					'</ul>'
				);
				var els = $('a', nPaging);
				$(els[0]).bind( 'click.DT', { action: "previous" }, fnClickHandler );
				$(els[1]).bind( 'click.DT', { action: "next" }, fnClickHandler );
			},

			"fnUpdate": function ( oSettings, fnDraw ) {
				var iListLength = 5;
				var oPaging = oSettings.oInstance.fnPagingInfo();
				var an = oSettings.aanFeatures.p;
				var i, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);

				if ( oPaging.iTotalPages < iListLength) {
					iStart = 1;
					iEnd = oPaging.iTotalPages;
				}
				else if ( oPaging.iPage <= iHalf ) {
					iStart = 1;
					iEnd = iListLength;
				} else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
					iStart = oPaging.iTotalPages - iListLength + 1;
					iEnd = oPaging.iTotalPages;
				} else {
					iStart = oPaging.iPage - iHalf + 1;
					iEnd = iStart + iListLength - 1;
				}

				for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
					// Remove the middle elements
					$('li:gt(0)', an[i]).filter(':not(:last)').remove();

					// Add the new list items and their event handlers
					for ( j=iStart ; j<=iEnd ; j++ ) {
						sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
						$('<li '+sClass+'><a href="#">'+j+'</a></li>')
							.insertBefore( $('li:last', an[i])[0] )
							.bind('click', function (e) {
								e.preventDefault();
								oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
								//fnDraw( oSettings );
								window.location.href = eGrid._getReDirectUrl(oSettings);
							} );
					}

					// Add / remove disabled classes from the static elements
					if ( oPaging.iPage === 0 ) {
						$('li:first', an[i]).addClass('disabled');
					} else {
						$('li:first', an[i]).removeClass('disabled');
					}

					if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
						$('li:last', an[i]).addClass('disabled');
					} else {
						$('li:last', an[i]).removeClass('disabled');
					}
				}
			}
		}
	} );
})(jQuery);