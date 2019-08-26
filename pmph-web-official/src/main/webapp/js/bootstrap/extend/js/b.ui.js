var bUI = {
	//初始化页面对象、控件、jQuery插件等
	init : function(_box){
		var $p = $(_box || document);
		
		//表单校验配置
		if(window.bValidate){
			$("form.required-validate", $p).each(function() {
				bValidate.labelValidate(this);
			});
		}
		
		if(window.bForm){
			$("form", $p).each(function(){
				bForm.setHeadNav($(this));
				bForm.setRequiredHead($(this));
			});
		}
		
		//表单后台校验显示区域
		$('div.formValidateMessages' , $p).each(function(){
			if($.trim($(this).text())) $(this).slideDown('fast');
		});
		
		
		$(".Wdate", $p).each(function() {
			$(this).prop('readonly',true).css('cursor','pointer');
		});
		
		//设置输入框的样式为Bdate时，自动为其应用datetimepicker日期插件，该控件为针对Bootstrap这个UI框架特别开发的
		//若在输入框中设置了data-date-format属性来设置日期格式，则优先读取该日期格式，否则默认使用yyyy-mm-dd
		/*
		$("input.bDate", $p).each(function() {
			var userFormat = $(this).attr('data-date-format');
			$(this).css('cursor','pointer');
			$(this).datetimepicker({
		        language:  'zh-CN',
		        weekStart: 7,
		        todayBtn:  1,
				autoclose: 1,
				todayHighlight: 1,
				startView: 2,
				format : userFormat?userFormat:'yyyy-mm-dd',
				minView: 2
		    });
		});
		*/
		
		$('table.bTable',$p).each(function(){
			$(this).bTable();
		});
		
		//选项卡
		//$(".idTabs").idTabs();
		if($.fn.UItoTop){
			//回到页面顶部
			$().UItoTop({
				text : 'Top',
				easingType: 'easeOutElastic',
				scrollSpeed: 200
			});
		}
	}
};


$(function(){
	bUI.init();
});