$(function(){
	$('.iicon-minuscril').hide();
	
	$('div.accordion-body ul.nav a').click(function(e){
		e.stopPropagation();
		var code = $(this).attr('menuCode');
		var url = $(this).attr('menuUrl');
		if(code) {
			eNav.setForword(code);
			//进行菜单点击时，清除二级菜单节点的cookie
			eNav.clearSubPageText();
			eNav.clearLastParentPath();
		}
		if(url) window.top.location.href = url;
	});
	
	
	$('.iicon-addcril').click(function(e){
		//设置当前访问模块高亮
		$("#side_accordion").find('.accordion-heading').addClass('sdb_h_active');
		$("#side_accordion").find('.accordion-body').addClass('in').css('height','auto');
		$('.iicon-addcril').hide();
		$('.iicon-minuscril').show();
	});
	$('.iicon-minuscril').click(function(e){
		//设置当前访问模块高亮
		$("#side_accordion").find('.accordion-heading').removeClass('sdb_h_active');
		$("#side_accordion").find('.accordion-body').removeClass('in').css('height', '0px');
		$('.iicon-addcril').show();
		$('.iicon-minuscril').hide();
	});
	//处理菜单当前节点高亮及面包屑设置
	//node：当前访问的菜单节点(jQuery对象)
	var doMenuInit = function(node){
		if(!node) return;
		var activeLi = $(node).parent();
		$(activeLi).addClass('active');
		var parentDiv = $(activeLi).closest('.accordion-group');
		//设置当前访问模块高亮
		$(parentDiv).find('.accordion-heading').addClass('sdb_h_active');
		$(parentDiv).find('.accordion-body').addClass('in');
		
		//处理面包屑导航
		var bottomNode = {
			name : $(node).text(),
			url : $(node).attr('menuUrl')
		};
		var parentNode = $(parentDiv).find('.accordion-toggle');
		var moduleNode = {
			name : $(parentNode).text()
		};
		$('#breadcrumbNavigateBar').find('.breadcrumbModule > span:last').text($.trim(moduleNode.name));
		$('#breadcrumbNavigateBar').find('.breadcrumbFunction > span:last').text($.trim(bottomNode.name));
		//$('#breadcrumbNavigateBar .breadcrumbModule,#breadcrumbNavigateBar .breadcrumbFunction').show();
		
		//处理新增、修改、查看等单页路径
		var operationText = eNav.getSubPageText();
		//获得最后被记录的父功能节点的访问路径
		var lastPath = eNav.getLastParentPath();
		if(operationText && lastPath && (lastPath != window.location.pathname)){
			$('#breadcrumbNavigateBar').find('li.breadcrumbOperation > span:last').text($.trim(operationText));
			$('#breadcrumbNavigateBar').find('li.breadcrumbOperation').show();
		}
		
		$('#breadcrumbNavigateBar').show();
	};
	
	//激活当前访问模块的菜单项目及根折叠块	
	var node = eNav.currentVisitNode($('#side_accordion'), 'menuUrl', window.location.pathname);
	if(node){
		doMenuInit(node);
		eNav.setForword(window.location.pathname);
	}else if(eNav.getForwordCode()){
		node = eNav.currentVisitNode($('#side_accordion'), 'menuUrl', eNav.getForwordCode());
		doMenuInit(node);
	}else{
		$('#breadcrumbNavigateBar .breadcrumbModule,#breadcrumbNavigateBar .breadcrumbFunction').hide();
	}
});