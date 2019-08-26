
(function($){
	if ($.validator) {
		$.validator.addMethod("alphanumeric", function(value, element) {
			return this.optional(element) || /^\w+$/i.test(value);
		}, "Letters, numbers or underscores only please");
		
		$.validator.addMethod("lettersonly", function(value, element) {
			return this.optional(element) || /^[a-z]+$/i.test(value);
		}, "Letters only please"); 
		
		$.validator.addMethod("phone", function(value, element) {
		    var fixedReg =/^(0[0-9]{2,3}\-?)?([2-9][0-9]{6,7})$/; 
		    //var mobileReg=/^1[34578]\d{9}$/;	    
		    /*if((fixedReg.test(value))||(mobileReg.test(value))){ 
		        return true; 
		    } 
		    else{
		    	return false;
		    }    */
			return this.optional(element) || fixedReg.test(value);
		}, "请按照xxxx-xxxxxxxx格式填写固定电话");
		
		$.validator.addMethod("postcode", function(value, element) {
			return this.optional(element) || /^[0-9 A-Za-z]{5,20}$/.test(value);
		}, "Please specify a valid postcode");
		
		$.validator.addMethod("date", function(value, element) {
			value = value.replace(/\s+/g, "");
			if (String.prototype.parseDate){
				var $input = $(element);
				var pattern = $input.attr('format') || 'yyyy-MM-dd';
	
				return !$input.val() || $input.val().parseDate(pattern);
			} else {
				return this.optional(element) || value.match(/^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/);
			}
		}, "Please enter a valid date.");
		//自定义身份证验证
		$.validator.addMethod("txtIdCard", function(arg) {
			/** 
			 * 身份证15位编码规则：dddddd yymmdd xx p  
			 * dddddd：地区码  
			 * yymmdd: 出生年月日  
			 * xx: 顺序类编码，无法确定  
			 * p: 性别，奇数为男，偶数为女 
			 * <p /> 
			 * 身份证18位编码规则：dddddd yyyymmdd xxx y  
			 * dddddd：地区码  
			 * yyyymmdd: 出生年月日  
			 * xxx:顺序类编码，无法确定，奇数为男，偶数为女  
			 * y: 校验码，该位数值可通过前17位计算获得 
			 * <p /> 
			 * 18位号码加权因子为(从右到左) Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2,1 ] 
			 * 验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]  
			 * 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )  
			 * i为身份证号码从右往左数的 2...18 位; Y_P为脚丫校验码所在校验码数组位置 
			 *  
			 */
			//调用验证方法
			if(arg == "" || checkIdcard(arg) == "验证通过!"){
				return true;
			}else{
				return false;
			} 
			function checkIdcard(idcard){
				var Errors=new Array(
				"验证通过!",
				"身份证号码位数不对!",
				"身份证号码出生日期超出范围或含有非法字符!",
				"身份证号码校验错误!",
				"身份证地区非法!"
				);
				var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
				var retflag=false;
				var idcard,Y,JYM;
				var S,M;
				var idcard_array = new Array();
				idcard_array = idcard.split("");
				//地区检验
				if(area[parseInt(idcard.substr(0,2))]==null){
					return Errors[4];
				} 
				//身份号码位数及格式检验
				switch(idcard.length){
					case 15:
						if ( (parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )){
							ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
						} else {
							ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
						}
						if(ereg.test(idcard)){
							return Errors[0];
						}else {
						 	return Errors[2];
						}
						break;
					case 18:
						//18位身份号码检测
						//出生日期的合法性检查 
						//闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
						//平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
						if ( parseInt(idcard.substr(6,4)) % 4 == 0 || (parseInt(idcard.substr(6,4)) % 100 == 0 && parseInt(idcard.substr(6,4))%4 == 0 )){
							ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
						} else {
							ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
						}
						if(ereg.test(idcard)){//测试出生日期的合法性
							//计算校验位
							S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
							+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
							+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
							+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
							+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
							+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
							+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
							+ parseInt(idcard_array[7]) * 1 
							+ parseInt(idcard_array[8]) * 6
							+ parseInt(idcard_array[9]) * 3 ;
							Y = S % 11;
							M = "F";
							JYM = "10X98765432";
							M = JYM.substr(Y,1);//判断校验位
							if(M == idcard_array[17]){
								return Errors[0]; //检测ID的校验位
							}else{
								return Errors[3];
							}
						}else {
							return Errors[2];
						}
						break;
					default:
						return Errors[1];
						break;
				}
			}
		});
		//验证电话号码
		$.validator.addMethod("checkPhone",function(phoneNumber, element){
			if(phoneNumber == ""){
				return true;
			}
			var valid = false;
			var phoneReg = /(^((0[1-9]\d{1,2})(-)?)?(\d{7,8})$)|(^\(0[1-9]\d{1,2}\)\d{7,8}$)/;
			var mobileReg = /^(13|15|18|17|14)[0-9]{9}$/;
			var phoneFlag = phoneReg.test(phoneNumber);
			var mobileFlag = mobileReg.test(phoneNumber);
			valid = phoneFlag || mobileFlag;
			return this.optional(element) || valid;
		});
		$.validator.addMethod("mobilePhone", function(value, element) {
		    var fixedReg =/^(13|15|18|17|14)[0-9]{9}$/; 
			return this.optional(element) || fixedReg.test(value);
		}, "手机号码格式不正确");
		//只能是数字或字母
		$.validator.addMethod("numberLetterOnly",function(value, element){
			var strExp=/^[A-Za-z0-9]+$/;
			return this.optional(element) || strExp.test(value);
		});
		/**
		 * 校验URL
		 * 支持IP形式的网址
		 * 支持FTP、http、https协议
		 */
		$.validator.addMethod("fullTypeUrl",function(value, element){
			var strExp=/^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i;
			return this.optional(element) || strExp.test(value);
		});
		
		
		$.validator.addClassRules({
			date: {date: true},
			alphanumeric: { alphanumeric: true },
			lettersonly: { lettersonly: true },
			phone: { phone: true },
			postcode: {postcode: true}
		});
		/**
		 * add by gongxq。用于价格的校验，精确到分位
		 * 价格格式 到分位
		 */
		$.validator.addMethod("priceNumber",function(value, element){
			var decimal = /^\d+(\.\d{1,2})?$/;
			return decimal.test(value)|| value=="";
		});
		/**
		 * add by huangjx。用于订单金额的校验，精确到分位
		 * 金额格式 到分位
		 */
		$.validator.addMethod("moneyNumber",function(value, element){
			var decimal = /^\d+(\.\d{1,2})?$/;
			return decimal.test(value)|| value=="";
		});
		/**
		 * add by gongxq。用于价格长度的限定校验
		 */
		$.validator.addMethod("priceLength",function(value, element){
			if(value.indexOf(".")>=0 && value.length>12){
				return false;
			}else if(value.indexOf(".")==-1 && value.length>9){
				return false;
			}else{
				return true;
			}
		});
		/**
		 * add by tongkai。用于折扣率的校验，精确到分位
		 * 折扣率格式 到分位
		 */
		$.validator.addMethod("checkDisc",function(value){
			var decimal = /^\s*([1-9]\d?(\.\d{1,2})?|0\.\d{1,2}|100)\s*$/;
			return decimal.test(value)|| value=="";
		});
		
		/**
		 *  by huangxm9。
		 *用于输入1-100数字的校验，左右可以有空格
		 */
		$.validator.addMethod("checkInteger",function(value){
			var decimal = /^\s*([0]*[1-9][0-9]?|100)\s*$/;
			return decimal.test(value)|| value=="";
		});
		
		/**
		 *  by tongkai。
		 *用于排序的校验，左右可以有空格
		 */
		$.validator.addMethod("checkSortNo",function(value){
			var decimal = /^\s*(\d{1,5})\s*$/;
			return decimal.test(value)|| value=="";
		});
		
		/**
		 * by wangbh
		 * 校验会员登录账号
		 */
		$.validator.addMethod("checkStaffCode",function(value,element){
			var strExp=/^[a-zA-Z0-9~!@#$%^&*()_-]{4,18}$/;
			return this.optional(element) || strExp.test(value);
		},'会员账号支持由数字、字母、一些特殊字符如~!@#$%^&*()_-组成，用户名长度为4～18个字符');
		/**
		 * by wangbh
		 * 校验会员登录账号密码
		 */
		$.validator.addMethod("checkPassword",function(value,element){
			var strExp=/^[a-zA-Z0-9~!@#$%^&*()_-]{6,16}$/;
			return this.optional(element) || strExp.test(value);
		},'长度6-16位，由数字、字母、一些特殊字符如~!@#$%^&*()_-自由组合而成。');
		
		$.validator.addMethod("positiveInteger",function(value,element){
			var strExp=/^[1-9]*[1-9][0-9]*$/;
			return this.optional(element) || strExp.test(value);
		},'只能是正整数');
		/**
		 * by wangbh
		 */
		$.validator.addMethod("email",function(value,element){
//			var strExp=/^[A-Za-zd]+([-_.][A-Za-zd]+)*@([A-Za-zd]+[-.])+[A-Za-zd]{2,5}$/;
			var strExp=/^(?:\w+\.?)*\w+@(?:\w+\.)*\w+$/;
			return this.optional(element) || strExp.test(value);
		},'请正确填写邮箱');
		$.validator.setDefaults({errorElement:"span"});
		$.validator.autoCreateRanges = true;
		
	}
})(jQuery);