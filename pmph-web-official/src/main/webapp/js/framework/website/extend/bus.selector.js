/* ===========================================================
 * bus.selector.js
 * ===========================================================
 * Terry 
 * 依赖：
 * bus.selector.js
 * 
 * created : 2015.09.03
 * 
 * 公用选择器
 * 
 * 更新记录：
 */

var busSelector = {
	//全局文件上传组件
	uploader : function(p,evt){
		if(evt){
			try{
				evt.stopPropagation();
			}catch(e){}	
		}
		p = $.extend({
			url : $webroot + 'ecpupload/uploadSelector',
			uploadUrl : '', //自定义上传后台处理路径
			uploadFileObjName : '',//自定义上传后台接收对象名称
			width : 600,
			height : 450,
			title : '文件上传',
			fileSizeLimit : '10MB',//默认设置10MB
			fileTypeExts : '*.jpg;*.gif;*.bmp;*.png',//限制上传文件的文件类型
			checktype : "multi",//数据选择模式multi：多选；single：单选模式
			//回调函数function(data)返回的数据结构：
			//grid : [{id:'xx',[{name:'',value:''},{...}]},....]
			//标准数据格式：[{id:'aa',value:'aaa'},{...}]
			callback : $.noop
		},p);
		//设置传递参数
		var params = {
			checktype : p.checktype,
			fileTypeExts : p.fileTypeExts,
			uploadUrl : p.uploadUrl,
			uploadFileObjName : p.uploadFileObjName,
			fileSizeLimit : p.fileSizeLimit
		};
		bDialog.open({
			title : p.title,
			width : p.width,
			height : p.height,
			url : p.url,
			params : params,
			callback : p.callback
		});
	}
};