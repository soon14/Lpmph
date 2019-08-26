/**
 * 基于Bootstrap的表格做的简单封装，用于做简单的数据表格，不需要太复杂的操作
 * 使用时需要为Table添加名为bTable的样式
 */
var bTable = {
	//获得选中行HTML元素
	//tabObj：表格的jquery对象
	getSelectedRowElement : function(tabObj){
		return $('tr.selected',$(tabObj));
	}
};

$.fn.extend({
	//初始化表格对象
	bTable : function(){
		if(!$(this)) return;
		//处理点击后选中行中的单选框
		$('tbody tr',$(this)).on('click',function(){
			$(':radio',$(this)).prop('checked',true);
			$(this).siblings().removeClass('selected');
			$(this).addClass('selected');
		});
	},
	//获得选中行ID
	getSelectedId : function(){
		var id = $('input:radio:checked',$(this)).val();
		if(!id){
			eDialog.error("请选择至少选择一个表格行！");
			return false;
		}else return id;
	},
	//获得选中行的HTML元素，返回结果是一个或多个
	getSelectedRowElement : function(){
		return bTable.getSelectedRowElement($(this));
	},
	//获得选中行所有数据，包含ID，数据结构为：
	//{id:1,column:['111','222','333','444','555']}
	getSelectedRowData : function(){
		var rows = bTable.getSelectedRowElement($(this));
		if(!rows || rows.size()==0) return null;
		
		var data = new Array();
		$.each(rows,function(i,row){
			var rdata = {};
			rdata.id = $(row).attr('id');
			rdata.column = [];
			var content = $('td',$(row)).not('td:has(:radio)').not('td:has(:checkbox)');
			$.each(content,function(j,tds){
				rdata.column.push($(tds).text());
			});
			data.push(rdata);
		});
		return data;
	}
});