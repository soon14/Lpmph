var eUI = {
	//初始化页面对象、控件、jQuery插件等
	init : function(_box){
		var $p = $(_box || document);
		
		//表单校验配置
		$("form.required-validate", $p).each(function() {
			$(this).validate({
				focusInvalid : false,
				focusCleanup : true,
				errorElement : "span",//生成span对象
				success : function(label,ele){
					if(ele){
						$(ele).next("span").remove();
						$(ele).after('<span class="note right"></span>');
					}
				},
				errorPlacement : function(label, ele) {
					if(ele && label.text()){
						$(ele).next("span").remove();
						$(ele).after('<span class="note error">'+label.text()+'</span>');
					}
				},
				errorClass : "ip_error",
				ignore : ".ignore",//设置class中有ignore时，忽略校验
				invalidHandler : function(form, validator) {
//					var errors = validator.numberOfInvalids();
//					if (errors) {
//						var message = DWZ.msg("validateFormError", [errors]);
//						alertMsg.error(message);
//					}
				}
			});
		});
		
		//xheditor富文本编辑器
		if ($.fn.xheditor) {
			$("textarea.editor", $p).each(function(){			
				var $this = $(this);
				var customToolBar = 'Cut,Copy,Paste,Pastetext,|,Blocktag,Fontface,FontSize,Bold,Italic,Underline,Strikethrough,FontColor,BackColor,SelectAll,Removeformat,|,Align,List,Outdent,Indent,|,Link,Unlink,Anchor,Hr,Emot,Table,|,insertImages,insertAttach,|,Source,Preview,Print,Fullscreen';
				var op = {html5Upload:true, skin: 'nostyle',tools:$this.attr("tools") || customToolBar};// $this.attr("tools") || 'full'
				var upAttrs = [
					["upLinkUrl","upLinkExt","zip,rar,txt"],
					["upImgUrl","upImgExt","jpg,jpeg,gif,png"],
					["upFlashUrl","upFlashExt","swf"],
					["upMediaUrl","upMediaExt","avi"]
				];
				
				$(upAttrs).each(function(i){
					var urlAttr = upAttrs[i][0];
					var extAttr = upAttrs[i][1];
					
					if ($this.attr(urlAttr)) {
						op[urlAttr] = $this.attr(urlAttr);
						op[extAttr] = $this.attr(extAttr) || upAttrs[i][2];
					}
				});
				var attachExt = "*.zip;*.rar;";
				if($this.attr('attachExt'))attachExt =$this.attr('attachExt');
				var plugins={
					insertImages:{c:'btnImagesUpload',t:'插入图片',e:function(){
						var _this=this;
						_this.saveBookmark();
						_this.showIframeModal('插入图片管理',ebc.constants.attachURL,function(v){
							_this.loadBookmark();
							_this.pasteHTML(ebcUpload.attachHandle(v));
						},538,404);
					}},
					insertAttach:{c:'btnAttachUpload',t:'插入附件',e:function(){
						var _this=this;
						_this.saveBookmark();
						_this.showIframeModal('插入附件管理',ebc.constants.attachURL+'?fileExt='+attachExt+'&modules=upload,history',function(v){
							_this.loadBookmark();
							_this.pasteHTML(ebcUpload.attachHandle(v));
						},538,404);
					}}
				};
				op.plugins = plugins;
				$this.xheditor(op);
			});
		}
		
		//选项卡
		//$(".idTabs").idTabs();
	}
};


$(function(){
	eUI.init();
});