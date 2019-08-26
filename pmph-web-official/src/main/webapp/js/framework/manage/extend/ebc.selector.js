//通用选择器插件
var ebcSelector = {
	//弹出窗口的ID，每个选择器须要手动进行递增
	selectorDialogId : 1,
	//弹出附件管理界面
	//窗口处理后传递给回调函数的参数结果集为json数据对象（数组）
	//obj.type - 对象类型，这里仅区别img和other两种类型
	//obj.url - 对象所在服务器的URL
	//*obj.alt - 图片的说明文字
	//*obj.align - 图片的对齐方式
	//*obj.width - 图片的宽度
	//*obj.height - 图片的高度
	//*obj.border - 图片的边框
	//*obj.vspace - 图片的左右内边距
	//*obj.hspace - 图片的上下内边距
	//带*号的是图片类型对象的专用属性
	//访问方式例：obj[0].type
	attach : function(opt,evt){
		if(evt){
			try{
				evt.stopPropagation();
			}catch(e){}	
		}
		opt = $.extend({
			url : ebc.constants.attachURL,
			width : 537,
			height : 404,
			multi : 1,// 批量模式文件上传，默认打开批量模式1:批量2：单选
			modules : 'net,upload',//net:网络图片，upload：本地上传文件，history：历史上传文件
			fileExt : '*.jpg;*.jpeg;*.gif;*.png;',
			returnType : 'url',//url : 返回完整URL路径|fileno：附件服务器的代码
			callback : null//回调函数
		},opt);
		var goUrl = opt.url + '?aa=1';
		if(opt.fileExt) goUrl += '&fileExt=' + opt.fileExt;
		//若模块设置不正常，则默认只打开文件上传功能
		if((opt.modules && opt.modules.indexOf('net')==-1 && opt.modules.indexOf('upload')==-1 && opt.modules.indexOf('history')==-1) || !opt.modules)opt.modules = 'upload';
		if(opt.modules) goUrl += '&modules=' + opt.modules;
		goUrl += '&multi=' + ((opt.multi===1)?'1':'2');
		var _winContent = $('<iframe src="' + goUrl + '" frameborder="0" height="' + opt.height + '" style="border:0;width:100%;">');
		$.modal(_winContent, {
			containerCss:{
				backgroundColor:"#FFFFFF",
				borderColor:"#666666",
				height:opt.height,
				padding:0,
				width:opt.width
			},
			opacity:20
		});
		var modalWin=_winContent[0].contentWindow;
		initModalWin();
		//初始化接口
		function initModalWin(){
			try{
				modalWin.callback=callbackModal;
			}
			catch(ex){}
		}
		//模式窗口回调
		function callbackModal(v){
			modalWin.document.write('');
			$.modal.close();
			if(v!=null && opt.callback)opt.callback(v);	
		}
	},
	//附件上传插件
	uploader : function(opt,evt){
		if(evt){
			try{
				evt.stopPropagation();
			}catch(e){}	
		}
		opt = $.extend({
			url : ebc.constants.uploaderURL,
			width : 600,
			height : 450,
			title : '附件上传',
			checktype : 1,//数据选择模式1：多选；-1：单选模式
			returnType : 'fileno',//url : 返回完整URL路径|fileno：附件服务器的代码
			params : null,//自定义参数集
			domain : null,
			//回调函数function(data)返回的数据结构：
			//grid : [{id:'xx',[{name:'',value:''},{...}]},....]
			//标准数据格式：[{id:'aa',value:'aaa'},{...}]
			callback : null
		},opt);
		if(opt.url && opt.callback && opt.title){
			var dlg = $.pdialog.open(
				opt.url,
				ebc.constants.selectorDialogPrefix + ebcSelector.selectorDialogId,
				opt.title,
				{
					width:opt.width,
					height:opt.height
				}
			);
			ebcSelector.selectorDialogId++;
			$(dlg).data('selectorcallback',opt.callback);
			if(opt.params){
				$(dlg).data('selectorparams',$.extend({checktype:opt.checktype},opt.params));
			}else{
				$(dlg).data('selectorparams',{checktype:opt.checktype});
			}
		}else{
			alertMsg.error("选择器参数传递不正确或参数个数不完整!");
		}
	},
	//选择器
	selector : function(opt,evt){
		if(evt){
			try{
				evt.stopPropagation();
			}catch(e){}	
		}
		opt = $.extend({
			url : '',
			width : 600,
			height : 450,
			title : '选择器',
			dialogId : '',
			checktype : 1,//数据选择模式1：多选；-1：单选模式
			params : null,//自定义参数集
			domain : null,
			//回调函数function(data)返回的数据结构：
			//grid : [{id:'xx',[{name:'',value:''},{...}]},....]
			//标准数据格式：[{id:'aa',value:'aaa'},{...}]
			callback : null
		},opt);
		if(opt.url && opt.callback && opt.title){
			var dlg = $.pdialog.open(
				opt.url,
				ebc.constants.selectorDialogPrefix + ebcSelector.selectorDialogId,
				opt.title,
				{
					width:opt.width,
					height:opt.height
				}
			);
			ebcSelector.selectorDialogId++;
			$(dlg).data('selectorcallback',opt.callback);
			if(opt.params){
				$(dlg).data('selectorparams',$.extend({checktype:opt.checktype},opt.params));
			}else{
				$(dlg).data('selectorparams',{checktype:opt.checktype});
			}
		}else{
			alertMsg.error("选择器参数传递不正确或参数个数不完整!");
		}
	},
	//组织机构选择器
	selectTree : function(opt,evt){
		if(evt){
			try{
				evt.stopPropagation();
			}catch(e){}	
		}
		opt = $.extend({
			title:'选择器',
			size:{width:570,height:400},
			url:null,
			initselect:[],
			nodeDblClick:null,
			nodeClick:null,
			showtype:null,
			openall:false,
			ok:null
		},opt);
		if(!opt.url)return ;
		var dialogopts = {};
		dialogopts.url = opt.url;
		dialogopts.initselect=opt.initselect;
		dialogopts.showbtn = opt.showbtn;
		dialogopts.openall = opt.openall;
		dialogopts.height = opt.size.height;
		dialogopts.width = opt.size.width;
		dialogopts.dealtreedata = opt.dealtreedata;
		dialogopts.leafclick=opt.leafclick;
		//双击
		if(opt.nodeDblClick){
			dialogopts.nodeDblClick=opt.nodeDblClick;
		}
		//点击
		if(opt.nodeClick)dialogopts.nodeClick=opt.nodeClick;
		if(opt.ok)dialogopts.ok=opt.ok;
		dialogopts.showtype = opt.showtype;
		dialogopts.showbtn = opt.showbtn;
		var dialog = $.pdialog.open(
			$webroot+"framework/jsLib/manage/extend/selecttree/orgnSelect.jsp",
			ebc.constants.selectorDialogPrefix + ebcSelector.selectorDialogId,
			opt.title,
			{
				width:opt.size.width,
				height:opt.size.height
			}
		);
		ebcSelector.selectorDialogId++;
		$(dialog).data("stoptions",dialogopts);
	}
};