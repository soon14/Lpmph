//***************
// ebc.utils.js
// 常用函数工具类
// 曾海沥[Terry]
//***************


//为Javascript原生的Array对象增加indexOf方法，获得对象在Array中的索引号
/*
if(!Array.prototype.indexOf){
	Array.prototype.indexOf = function(elt){
		var len = this.length;
		var from  = Number(arguments[1]) || 0;
		from  = (from < 0)? Math.ceil(from): Math.floor(from);
		if(from < 0){
			from += len;
		}
		for(; from < len; from++){
			if(from in this && this[from] === elt){
				return from;
			}
		}
		return -1;
	};
}
*/

//为Javascript原生的Array对象增加remove()方法
//网上的做法是直接this.indexOf(obj)这样做，在FF下是正确的，但在IE下会报
//不支持该属性或方法,原因是Array不能够直接indexOf()，使用join('')后，将
//会返回所有内容，且未添加任何分割符
/*
Array.prototype.remove = function(obj){
	var index = this.indexOf(obj);
	if(index >= 0){
		this.splice(index,1);
		return true;
	}
	return false;
};
*/
//+---------------------------------------------------   
//| 判断对象/变量内容是否不为空
//+---------------------------------------------------
function isNotEmpty(obj){	
	if(typeof obj != 'undefined' && obj != null && obj.length != 0){
		if(typeof obj == 'string'){
			return (obj.toUpperCase()!='NULL' && obj.trim()!='') ? true : false;
		}else{
			return true;
		}
	}else{
		return false;
	}
}

//+---------------------------------------------------   
//| 判断对象/变量内容是否为空
//+---------------------------------------------------
function isEmpty(obj){
	return !isNotEmpty(obj);
}

//设置在窗口关闭时自动加载的内存释放的操作
/*
$(window).unload(function(){
	if(isIE()){
		CollectGarbage();
	}
});*/

//查看对象中的详细信息
function objectDetail(obj){
	var attrStr = '';
	attrStr = viewObject(obj);
	alertMsg.warn(attrStr);
}

//查看对象中的详细信息
function viewObject(obj){
	var str = '';
	if(isEmpty(obj)){
		str += '空对象';
	}else{
		var property,value;		
		if(obj instanceof Array){
			str += '<br>Array Object,length:' + obj.length;
			str += '<br>===========================';
			for(var i=0;i<obj.length;i++){				
				str += '<br>Array Object Index ' + i;
				str += '\t' + viewObject(obj[i]);
				str += '<br>------------------------------';
			}
		}else{
			//document.write('<br>' + obj);
			//document.write('<br>' + typeof(obj));
			if(typeof(obj)=='object'){
				for(property in obj){
					value = eval('obj.'+property);
					if(typeof(value) == 'undefined'){
						str += '<br>'+property + ':undefined';
					}else if(typeof(value) == 'object'){
						str += viewObject(value);
					}else{
						str += '<br>'+property + ':' + value;
					}
				}
			}else{
				str += '<br>' + typeof(obj) + ':' + obj;				
			}
		}
	}
	return str;
}
//除法函数，用来得到精确的除法结果
//说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
//调用：accDiv(arg1,arg2)
//返回值：arg1除以arg2的精确结果
function accDiv(arg1,arg2){
	var t1=0,t2=0,r1,r2;
    try{t1=arg1.toString().split(".")[1].length;}catch(e){}
    try{t2=arg2.toString().split(".")[1].length;}catch(e){}
    with(Math){
		r1=Number(arg1.toString().replace(".",""));
        r2=Number(arg2.toString().replace(".",""));
        return (r1/r2)*pow(10,t2-t1);
    }
}

//给Number类型增加一个div方法，调用起来更加方便。
Number.prototype.div = function (arg){
	return accDiv(this, arg);
};

//乘法函数，用来得到精确的乘法结果
//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
//调用：accMul(arg1,arg2)
//返回值：arg1乘以arg2的精确结果
function accMul(arg1,arg2){
	var m=0,s1=arg1.toString(),s2=arg2.toString();
	try{m+=s1.split(".")[1].length;}catch(e){}
	try{m+=s2.split(".")[1].length;}catch(e){}
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
}

//给Number类型增加一个mul方法，调用起来更加方便。
Number.prototype.mul = function (arg){
	return accMul(arg, this);
};

//加法函数，用来得到精确的加法结果
//说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
//调用：accAdd(arg1,arg2)
//返回值：arg1加上arg2的精确结果
function accAdd(arg1,arg2){
	var r1,r2,m;
	try{r1=arg1.toString().split(".")[1].length;}catch(e){r1=0;}
	try{r2=arg2.toString().split(".")[1].length;}catch(e){r2=0;}
	m=Math.pow(10,Math.max(r1,r2));
	return (Number(arg1)*m + Number(arg2)*m)/m;
}

//给Number类型增加一个add方法，调用起来更加方便。
Number.prototype.add = function (arg){
	return accAdd(arg,this);
};

//减法函数，用来得到精确的加法结果
//说明：javascript的减法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的减法结果。
//调用：accSub(arg1,arg2)
//返回值：arg1减上arg2的精确结果
function accSub(arg1,arg2){
	return accAdd(arg1,-arg2);
}

//给Number类型增加一个sub方法，调用起来更加方便。
Number.prototype.sub = function (arg){
	return accSub(this,arg);
};

function Cookies() {  
    this.get = function(key) {  
        var cookie = document.cookie;  
        var cookieArray = cookie.split(';');  
        var val = "";  
        for (var i = 0; i < cookieArray.length; i++) {  
            if (cookieArray[i].trim().substr(0, key.length) == key) {  
                val = cookieArray[i].trim().substr(key.length + 1);  
                break;  
            }  
        }  
        return unescape(val);  
    };  
    this.getChild = function(key, childKey) {  
        var child = this.get(key);  
        var childs = child.split('&');  
        var val = "";  
  
        for (var i = 0; i < childs.length; i++) {  
            if (childs[i].trim().substr(0, childKey.length) == childKey) {  
                val = childs[i].trim().substr(childKey.length + 1);  
                break;  
            }  
        }  
        return val;  
    };  
    this.set = function(key, value) {  
        var cookie = "";  
        if (!!key && !!value)  
            cookie += key + "=" + escape(value) + ";";  
        if (!!arguments[2])  
            cookie += "expires=" + arguments[2].toGMTString() + ";";  
        if (!!arguments[3])  
            cookie += "domain=" + arguments[3] + ";";  
        if (!!arguments[4])  
            cookie += "path=" + arguments[4] + ";";  
        document.cookie = cookie;  
    };  
    this.remove = function(key) {  
        var date = new Date();  
        date.setFullYear(date.getFullYear() - 1);  
        var cookie = " " + key + "=;expires=" + date + ";";
        document.cookie = cookie;  
    };
}  


















var ebcUtils = {
	//+---------------------------------------------------   
	//| 获得对象本身的HTML代码
	//| obj：JQuery对象
	//+---------------------------------------------------
	getSelfHtml : function(obj){
		return $('<div>').append(obj.clone()).remove().html();
	},
	//序列化json对象为表单提交的格式
	//obj：json数据对象
	//nameArr：前缀数组，正常使用不需要传递，仅为递归时使用
	serializeObject : function(obj,nameArr){
		if(!obj) return null;
		var result = new Array();
		var namePre = '';
		if(nameArr && $.isArray(nameArr) && nameArr.length > 0 ){
			$.each(nameArr,function(i,row){
				if(row && $.isPlainObject(row)){
					if(row.index == -1){//不设置下标
						namePre += row.name + '.';
					}else{//设置下标
						namePre += row.name + '[' + row.index + '].';
					}					
				}
			});
		}
		$.each(obj,function(i,row){
			if($.type(row) == 'object'){//处理子对象
				var tmpArr = (nameArr && $.isArray(nameArr) && nameArr.length > 0)? nameArr : new Array();
				tmpArr = tmpArr.concat([{name : i,index : -1}]);
				result = result.concat(ebcUtils.serializeObject(row,tmpArr));
			}else if($.isArray(row)){//处理数组
				if(row.length > 0){
					$.each(row,function(arrIdx,arrRow){
						var tmpArr = (nameArr && $.isArray(nameArr) && nameArr.length > 0)? nameArr : new Array();
						tmpArr = tmpArr.concat([{name : i,index : arrIdx}]);
						result = result.concat(ebcUtils.serializeObject(arrRow,tmpArr));
					});
				}
			}else{//处理对象
				var itemObj = {};
				itemObj.name = namePre + i;
				itemObj.value = row;
				result.push(itemObj);
			}
		});
		return result;
	},
	//数字格式化
	//num :源数字，支持数字或数字字符串 
	//n   :保留小数点倍数，默认为2位小数
	numFormat : function(num,n){
		var temp = 1;
		var temp2 = "";
		if($.type(n)!='number' && !n) n=2;
		for(var i=0;i<n;i++){
			temp = temp * 10;
			temp2 = temp2 + "0";
		}
		if(num==0){
			if(n==0) return "0";
			else return "0."+temp2;
		}
		if(isNaN(num) || num==""){return "";}
		var result = (Math.round(parseFloat(num)*temp)/temp).toString();
		if(result.indexOf(".")==-1){
			if(n==0) return result;
			else return result + "." + temp2;
		}
		else return (result+temp2).substr(0,1 + parseInt(n) + parseInt((result+temp2).indexOf(".")));
	},
	//为数字添加千位符，返回字符串结果
	thousandSeparator : function(num,separator){
		var scode = separator?separator:',';
		if(!/^(\+|-)?(\d+)(\.\d+)?$/.test(num)){  
			return num;  
		}  
		var a = RegExp.$1,b = RegExp.$2,c = RegExp.$3;  
		var re = new RegExp();
		re.compile("(\\d)(\\d{3})("+scode+"|$)");
		while(re.test(b)){
			b = b.replace(re,"$1"+scode+"$2$3");  
		}  
		return a +""+ b +""+ c;  
	},
	//去除千位符
	unThousandSeparator : function(num,separator){
		var result='';
		var scode = separator?separator:',';
		if(num && typeof(num)=='string'){
			result = this.replaceAll(num, scode, '');
		}
		return result;
	},
	replaceAll : function(srcStr,substr,newstr){
		if(!srcStr || !typeof(srcStr)=='string') return;
		return srcStr.replace(new RegExp(substr,"gm"),newstr);
	},
	//+---------------------------------------------------   
	//| 将JSON对象指定列的所有行数据转换为Array对象
	//| jsonObj:JSON格式变量(该对象须是Array类型)
	//| columnName:JSON对象的属性名
	//+---------------------------------------------------
	convertJsonToArray:function(jsonObj,columnName){
		var array = new Array(jsonObj.lenght);
		for (var i = 0; i < jsonObj.length; i++) {
			array[i] = eval('jsonObj[i].'+columnName);
		}
		return array;
	},
	//+---------------------------------------------------   
	//| 将后台查询返回的JSON数据转换为Javascript能识别的Json格式
	//| obj:JSON格式数据内容
	//+---------------------------------------------------
	parseJsonObject:function(obj){
		return isNotEmpty(obj)?eval('('+obj+')'):'';
	},
	//+---------------------------------------------------   
	//| 将数字字符串转换为数字
	//+---------------------------------------------------
	parseToNum:function(str){
		return isNotEmpty(str) ? parseFloat(str) : 0;
	},
	//+---------------------------------------------------   
	//| 将空内容转换为空字符串
	//+---------------------------------------------------
	nullToEmpty:function(str){
		return isNotEmpty(str) ? str : '';
	},
	//限制只输入数字，调用代码为：[ onkeypress="javascript:return checkNum(event,this);"]
	//目前无法解决中文输入的问题，若要解决可以使用onpropertychange来解决，FF下是onInput的事件来解决
	//eventObj：事件对象
	//obj：控件对象自身，通常传递this即可
	//integer：是否为整数，默认为true
	//positive：是否为正数，默认为true
	checkNum:function(eventObj,obj,integer,positive) {
		//获得当前按键的键值
		var key_code = window.event ? window.event.keyCode : eventObj.which ? eventObj.which : eventObj.charCode;
		var int = true,pos = true;
		if($.type(integer)!='undefined' && $.type(integer)=='boolean') int = integer;
		if($.type(positive)!='undefined' && $.type(positive)=='boolean') pos = positive;
		if (!(((key_code >= 48) && (key_code <= 57)) || 
			   (key_code == 13) || 
			   (key_code == 8) || 
			   (key_code == 46 && !int) || 
			   (key_code == 45 && !pos))) {
			try{
				window.event.keyCode = 0 ;
			}catch(e){
				eventObj.cancelBubble = true;
			}		
			return false;
		}
		//限制只允许输入一个小数点符号
		if (key_code == 46 && $(obj).val().indexOf(".") != -1) {
			try{
				window.event.keyCode = 0 ;
			}catch(e){
				eventObj.cancelBubble = true;
			}
			return false;
		}

		//限制只允许输入一个负数符号
		if (key_code == 45 && $(obj).val().indexOf("-") != -1) {
			try{
				window.event.keyCode = 0 ;
			}catch(e){
				eventObj.cancelBubble = true;
			}
			return false;
		}

		return true;
	},
	//通过调用该方法防止事件冒泡
	blockEventUp:function(event){
		try{
			event.stopPropagation();
		}catch(e){
			//event.cancelBubble = true;
			window.event.cancelBubble=true;
		}
	},
	//将一个数组对象的所有值转换成"aa,bb,cc"格式的字符串
	//若只需要转换成'aaa,bbb,ccc'此类以','分割的字符串时，只需要使用array.toString()就可以达到效果
	arrayToString:function(array,splitKey){
		var key = isNotEmpty(splitKey)?splitKey:',';
		return isNotEmpty(array)?array.join(key):'';
	},
	//将"aa,bb,cc"格式的字符串转换为数组对象
	//splitKey:分割关键字符，若用户未设置，则默认使用','
	stringToArray:function(str,splitKey){
		var key = isNotEmpty(splitKey)?splitKey:',';
		if(isNotEmpty(str)){
			return str.indexOf(key)!=-1 ? str.split(key) : new Array(str); 
		}else{
			return new Array();
		}
	},
	//根据URL内容返回当前连接符号应该是?还是&
	getUrlConnCode:function(url){
		return url.indexOf('?')!=-1 ? '&' : '?';
	},
	//在指定秒数后清空指定对象内的内容
	delayClearText:function(objId,second){
		if(isNotEmpty(objId) && isNotEmpty(second)){
			var tTime;
			try{
				tTime = parseInt(second);
			}catch(e){
				tTime = 3;
			}
			window.setTimeout("$(\"#"+objId+"\").html('')", tTime * 1000);
		}
	},
	//返回Grid指定列数据到指定的父页面对象中
	//gridName页面中flexiGrid的ID
	//p:JSON格式的配置内容[{inputId:'aa',colIndex:'2'},{...}]
	//注意：colIndex若设置为-1则为id列
	returnGridValues:function(gridName,p){
	    var gridObj = $("#"+gridName);
	    var ids = gridObj.getCheckedIds();
	    if (ids.length == 0) {
	        $.alert('请选择要返回的项目！');
	        return;
	    }
	    p = $.decode(p);
	    var parentObj = getParentDoc();

	    if(p && $.isArray(p)){
	    	for(var i=0;i<p.length;i++){
	    		var temparr = p[i],tempValue;
	    		tempValue = temparr.colIndex == -1?tempValue = ids[0]:tempValue = gridObj.getCheckedObjects()[0].cell[temparr.colIndex].value;
	    		$('#'+temparr.inputId , parentObj).val(tempValue);
	    	}
	    }
	    try{
	        //手动调用父页面中的方法，若有需要在选择器选择完后执行部分自定义代码
	    	//调用者需要在页面中自定义一个returnValue()方法
	        window.parent.returnValue();
	    }catch(e){}
	    $.closeWindow();
	},
	//获得父窗口文档对象
	getParentDoc:function(){
		var relation = $.getRelationDocument();	
		return relation?relation:window.parent.document;
	},
	//最大化窗口
	maxWindow:function(){
		setTimeout('top.moveTo(0,0)',1);
		setTimeout('top.resizeTo(screen.availWidth,screen.availHeight)',1);
	},
	//关闭当前窗口
	closeWindow:function(){
		window.opener=null;
		//window.open("","_self");
		window.close();
	},
	//判断当前浏览器是否为IE
	isIE:function(){
	    var ua = navigator.userAgent.toLowerCase(),
	    check = function(r){
	        return r.test(ua);
	    },
	    //isStrict = document.compatMode == "CSS1Compat",
	    isOpera = check(/opera/),
	    isChrome = check(/chrome/),
	    //isWebKit = check(/webkit/),
	    isSafari = !isChrome && check(/safari/),
	    isSafari3 = isSafari && check(/version\/3/),
	    isSafari4 = isSafari && check(/version\/4/),
	    isIE = !isOpera && check(/msie/),
	    isIE7 = isIE && check(/msie 7/),
	    isIE8 = isIE && check(/msie 8/);
	    //isGecko = !isWebKit && check(/gecko/),
	    //isGecko3 = isGecko && check(/rv:1\.9/),
	    //isBorderBox = isIE && !isStrict,
	    //isWindows = check(/windows|win32/),
	    //isMac = check(/macintosh|mac os x/),
	    //isAir = check(/adobeair/),
	    //isLinux = check(/linux/),
	    //isSecure = /^https/i.test(window.location.protocol);
	    return isIE;
	},
	//将对象转换为boolean型数据
	obj2boolean:function(obj){
		if(typeof(obj)=='string'){
			if(obj.toUpperCase()=="TRUE"){
				return true;
			}else if(obj.toUpperCase()=="FALSE"){
				return false;
			}
		}
		return Boolean(obj);
	},
	//获得键盘事件的按键码，兼容IE，FF
	getKeyCode : function (event){
		event = (event) ? event : ((window.event) ? window.event : ""); //兼容IE和Firefox获得keyBoardEvent对象
		return event.keyCode ? event.keyCode : event.which; //兼容IE和Firefox获得keyBoardEvent对象的键值
	},
	//URL传递参数中有中文内容时需要进行编码
	encode : function(str){
		return encodeURIComponent(str);
	},
	//对编码进行解码
	decode : function(str){
		return decodeURIComponent(str);
	},
	//数字转字母
	//下标从1开始
	number2letter : function(num){
		if(typeof(num)=='number' && num > 0 && num <= 26){
			var str = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
			return str.substring(num-1, num);
		}
	},
	//对象内容标准化，用于直接alert输出直接调试查看，也用于格式化需要提交到后台的参数
	//JSON对象若报错，则是浏览器默认没有提供JSON对象，通常是IE8以前的浏览器，这时需要手动添加
	objStringify : function(obj){
		return JSON.stringify(id);
	},
	//获得服务器虚拟路径
	getRootPath : function(){
		var strFullPath=window.document.location.href;
		var strPath=window.document.location.pathname;
		var pos=strFullPath.indexOf(strPath);
		var prePath=strFullPath.substring(0,pos);
		var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
		return(prePath+postPath);
	}
};