//ebc.date.js
//日期工具类

var ebcDate = {
	//+-------------------------------------	
	//|日期格式化的总调方法
	//|dateObj：日期对象或字符串型日期内容
	//|formatter：
	//+-------------------------------------
	dateFormat : function(dateObj,formatter){
		if(!dateObj)return '';
		var tmpDate = null;
		if(typeof(dateObj) === 'string'){
			tmpDate = ebcDate.parse(dateObj);
		}else if(typeof(dateObj) === 'number'){
			var d = new Date();
			d.setTime(dateObj);
			tmpDate = d;
		}else if(dateObj instanceof Date){
			tmpDate = dateObj;
		}
		return ebcDate.format(tmpDate,formatter);
	},
	// 将日期字符串转换为yyyy-MM-dd的标准格式
	// 曾海沥
	format:function(date,formatter){
		if(date==null){return null;};
		if(formatter==null){formatter = "yyyy-MM-dd";};
		var year = date.getFullYear().toString();
		var month = (date.getMonth() + 1).toString();
		var day = date.getDate().toString();
		var hours = date.getHours().toString();
		var minutes = date.getMinutes().toString();
		var seconds = date.getSeconds().toString();
		var yearMarker = formatter.replace(/[^y]/g,'');
		var monthMarker = formatter.replace(/[^M]/g,'');
		var dayMarker = formatter.replace(/[^d]/g,'');
		var hoursMarker = formatter.replace(/[^h]/g,'');
		var minutesMarker = formatter.replace(/[^m]/g,'');
		var secondsMarker = formatter.replace(/[^s]/g,'');
		if(yearMarker.length == 2){year = year.substring(2,4);};
		if(monthMarker.length > 1 && month.length==1){month = "0" + month;};
		if(dayMarker.length > 1 && day.length==1){day = "0" + day;};
		if(hoursMarker.length > 1 && hours.length==1){hours = "0" + hours;};
		if(minutesMarker.length > 1 && minutes.length==1){minutes = "0" + minutes;};
		if(secondsMarker.length > 1 && seconds.length==1){seconds = "0" + seconds;};
		if(yearMarker.length>0){formatter = formatter.replace(yearMarker,year);}
		if(monthMarker.length>0){formatter = formatter.replace(monthMarker,month);};
		if(dayMarker.length>0){formatter = formatter.replace(dayMarker,day);};
		if(hoursMarker.length>0){formatter = formatter.replace(hoursMarker,hours);};
		if(minutesMarker.length>0){formatter = formatter.replace(minutesMarker,minutes);};
		if(secondsMarker.length>0){formatter = formatter.replace(secondsMarker,seconds);};
		return formatter;
	},
	//日期字符串转转换为指定格式
	parse : function(str){  
		if(typeof str == 'string'){
			///处理CST时间； Fri Aug 14 20:51:57 CST 2015
			if(str.toUpperCase().indexOf("CST") != -1){
				//默认的CST格式时间使用的时区是美国中部时间UT-6:00
				//而所需要的是中国标准时间UT+8:00
				//所以默认使用CST格式时间进行初始化日期对象后，实际时间比中国标准时间早了14个小时
				//在日期转换时需要进行相减
				var tmpd = new Date(str);
				tmpd.setHours(tmpd.getHours()-14);
				return tmpd;
			}
			if(str.toUpperCase().indexOf("GMT") != -1){
				return new Date(str);
			}
			var results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) *$/);
			if(results && results.length>3)
				return new Date(parseInt(results[1]),parseInt(results[2]) -1,parseInt(results[3]));
			results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2}) *$/);
			if(results && results.length>6)
				return new Date(parseInt(results[1]),parseInt(results[2]) -1,parseInt(results[3]),parseInt(results[4]),parseInt(results[5]),parseInt(results[6]));
			results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2})\.(\d{1,9}) *$/);
			if(results && results.length>7)
				return new Date(parseInt(results[1]),parseInt(results[2]) -1,parseInt(results[3]),parseInt(results[4]),parseInt(results[5]),parseInt(results[6]),parseInt(results[7]));
		}
		return null;  
	},
	//+---------------------------------------------------   
	//| 比较两个日期之间相差天数
	//| 格式 MM/dd/YYYY MM-dd-YYYY YYYY/MM/dd YYYY-MM-dd
	//+---------------------------------------------------
	differenceDate:function(date1,date2){
		var s1 = date1;
		var s2 = date2;
		s1 = s1.replace(/-/g,"/");
		s2 = s2.replace(/-/g,"/");
		s1 = new Date(s1);
		s2 = new Date(s2);
		var time = s1.getTime() - s2.getTime();
		var days = Math.abs(parseInt(time / (1000 * 60 * 60 * 24)));
		return days;
	},
	//+---------------------------------------------------   
	//| 比较日期1是否大于日期2
	//| 格式 MM/dd/YYYY MM-dd-YYYY YYYY/MM/dd YYYY-MM-dd
	//| compareDate('2007-12-31','2007-01-01')返回true
	//+---------------------------------------------------
	compareDate:function(date1,date2){
		var s1 = date1;
		var s2 = date2;
		s1 = s1.replace(/-/g,"/");
		s2 = s2.replace(/-/g,"/");
		s1 = new Date(s1);
		s2 = new Date(s2);
		return (s1.getTime() > s2.getTime()) ? true : false;
	},
	//+---------------------------------------------------   
	//| 从JSON格式的详细日期中获得yyyy-MM-dd格式的日期
	//+---------------------------------------------------
	simpleDateFromJson:function(json){
		if(isNotEmpty(json)){
			var year,month,day;
			year = 1900+json.year;
			month=json.month<9?'0'+(json.month+1):(json.month+1);
			day = json.date<10?'0'+json.date:json.date;
			return year + '-' + month + '-' + day;
		}else{
			return '';
		}
	},
	//+---------------------------------------------------   
	//| 根据传入日期，获得年份
	//| 日期格式：XXXX-XX-XX 
	//+---------------------------------------------------
	getYearByDate:function(vDate){
		if(isEmpty(vDate)){return '';}
		var tempDate = vDate;
		if(typeof(vDate)=='string'){
			tempDate = vDate.replace("-", "/").replace("-", "/");
			tempDate = new Date(tempDate);//若用户传递字符串格式，则进行转换
		}
		return tempDate.getFullYear();
	},
	//+---------------------------------------------------   
	//| 根据传入日期，要处理的天数，进行加/减计算(num：加使用正数，减时使用负数)
	//| 日期格式：XXXX年XX月XX日 
	//+---------------------------------------------------
	dateCalc:function(dateParameter, num){
		var newDate, dateString = "", monthString = "", dayString = "";
		if(typeof(dateParameter) != 'undefined' && dateParameter instanceof Date){
			newDate = dateParameter;
		}else{
			var translateDate = dateParameter.replace("-", "/").replace("-", "/");
			newDate = new Date(translateDate);
		}
		newDate = newDate.valueOf();  
		newDate = newDate + num * 24 * 60 * 60 * 1000;  
		newDate = new Date(newDate);  
	   
		//如果月份长度少于2，则前加 0 补位  
		if ((newDate.getMonth() + 1).toString().length == 1) {  
			monthString = 0 + "" + (newDate.getMonth() + 1).toString();  
		} else {  
			monthString = (newDate.getMonth() + 1).toString();  
		}
		//如果天数长度少于2，则前加 0 补位  
		if (newDate.getDate().toString().length == 1) {  
			dayString = 0 + "" + newDate.getDate().toString();  
		} else {  
			dayString = newDate.getDate().toString();  
		}
		dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString;  
		return dateString;  
	},
	//+---------------------------------------------------   
	//| 根据传入日期，要处理的年份，进行加/减计算(num：加使用正数，减时使用负数)
	//| 日期格式：XXXX年XX月XX日 
	//+---------------------------------------------------
	yearCalc:function(dateParameter, num) {  
		var newDate, dateString = "", monthString = "", dayString = "";
		if(typeof(dateParameter) != 'undefined' && dateParameter instanceof Date){
			newDate = dateParameter;
		}else{
			var translateDate = dateParameter.replace("-", "/").replace("-", "/");
			newDate = new Date(translateDate);
		}
	   
		//如果月份长度少于2，则前加 0 补位  
		if ((newDate.getMonth() + 1).toString().length == 1) {  
			monthString = 0 + "" + (newDate.getMonth() + 1).toString();  
		} else {  
			monthString = (newDate.getMonth() + 1).toString();  
		}
		//如果天数长度少于2，则前加 0 补位  
		if (newDate.getDate().toString().length == 1) {  
			dayString = 0 + "" + newDate.getDate().toString();  
		} else {  
			dayString = newDate.getDate().toString();  
		}
		dateString = (newDate.getFullYear()+num) + "-" + monthString + "-" + dayString;  
		return dateString;  
	},
	//+---------------------------------------------------   
	//| 以标准格式(yyyy-MM-dd)获得当前日期
	//+---------------------------------------------------
	getToday:function(){
		return getStandardDate(new Date());
	},
	//+---------------------------------------------------   
	//| 根据日期类型数据返回'yyyy-MM-dd'的格式
	//| vDate:日期格式的对象
	//+---------------------------------------------------
	getStandardDate:function(vDate){
		if(!vDate){return '';}
		var tempDate = vDate;
		if(typeof(vDate)=='string'){
			vDate = new Date(vDate);//若用户传递字符串格式，则进行转换
			if(isNaN(vDate))vDate = new Date(Date.parse(tempDate.replace(/-/g,"/")));
			if(isNaN(vDate) && tempDate.length==8){//处理yyyyMMdd这样的连在一级的格式
				var tmp = tempDate.substr(0,4) + '/' + tempDate.substr(4,2) + '/' + tempDate.substr(6,2);
				vDate = new Date(Date.parse(tmp));
			}
		}
		var dateStr = '';
		dateStr = vDate.getFullYear() + '-';
		if(vDate.getMonth()<9){
			dateStr += '0' + (vDate.getMonth()+1) + '-';
		}else{
			dateStr += (vDate.getMonth()+1) + '-';
		}
		if(vDate.getDate()<10){
			dateStr += '0' + vDate.getDate();
		}else{
			dateStr += vDate.getDate();
		}
		return dateStr;
	}
};