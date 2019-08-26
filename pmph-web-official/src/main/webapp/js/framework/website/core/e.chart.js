/**
 * 基于HighChart做的基础封装
 */
var eChart = {
	/**
	 * 默认参数
	 * 数据表达式说明：
	 * Point系列
	 * point.name当前节点描述，通常对应源数据每行下标为0的数据
	 * point.x数据下标
	 * point.y数据实体值，通常对应数据源每行下标为1的数据
	 * point.percentage当前节点数据值占总数据的百分比
	 * point.total所有数据的总和
	 * Series系列
	 * series.name数据源中指定的数据描述
	 */
	defaults :{
    	credits: { enabled : false },//关闭网站/版本信息
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,//图表边框，设置null为无边框
            plotShadow: false
        }
	},
	/**
	 * 饼图
	 * 默认使用随机颜色
	 * 默认使用数量比例来展示数据
	 * @param obj 需要放置图表的对象
	 * @param p
	 */
	pie : function(obj,p){
		if(!obj) return;
		p = $.extend({},eChart.defaults,p);
		p.tooltip = p.tooltip ? p.tooltip : {
			pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		};
		p.plotOptions = p.plotOptions?p.plotOptions:{
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                    	//设置边框和颜色，若能读取到指定块的颜色，则将边框的颜色设置为块的颜色
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        };
        var defaultPieSeries = {
        	type : 'pie',
        	name : '数据比例',
        	data : []
        };
        if(p.series){
        	$.each(p.series,function(i,row){
        		p.series[i] = $.extend({},defaultPieSeries,row);
        	});
        }else{
        	p.series = [defaultPieSeries];
        }
        $(obj).highcharts(p);
	}
};