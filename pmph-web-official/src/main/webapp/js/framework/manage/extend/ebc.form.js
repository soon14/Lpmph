//***************
// terry.form.js
// 表单中常用函数工具类
// 曾海沥[Terry]
//***************



var ebcForm = {
	//+---------------------------------------------------   
	//| 下拉列表数据填充
	//| 参数格式：
	//| data:Ajax请求返回的数组型的数据
	//| objId:下拉列表对象的ID集，例：#aaa,#bbb,#ccc，注意，需要带上#号
	//| hasLead:是否需要添加“－－请选择－－”字样
	//|	leadStr:下拉列表首项的项目文本
	//|	selectField:下拉列表控件中设置默认选中项目代码的属性
	//|	selectedStr:设置项目选中的option属性，值是selected="selected"
	//|	keyField:对于后台数据结构为List<xxx>时，需要指定做为KEY的列
	//|	valueField:对于后台数据结构为List<xxx>时，需要指定做为VALUE的列
	//+---------------------------------------------------	
	fillSelect : function(data,opts){
		opts = $.extend({
			objIds:'',
			hasLead:false,
			leadStr:'--请选择--',
			selectField:'default',
			selectedStr:'selected="selected"',
			keyField:'',
			valueField:''
		},opts);
		var objs = $(opts.objIds);
		if(objs.size()>0 && data){
			if($.isArray(data) && data.length>0){//数组格式数据（后台数据格式：List<xxx>）
				var keyStr,valueStr;
				objs.each(function(i,row){
					var cObj = $(this),selected = cObj.attr(opts.selectField),selstr='';
					cObj.empty();
					if(opts.hasLead && opts.leadStr){//默认首项
						cObj.append("<option value=''>"+opts.leadStr+"</option>");
					}
					$.each(data,function(index,dRow){
						keyStr = eval('dRow.'+opts.keyField);
						valueStr = eval('dRow.'+opts.valueField);
						selstr = (selected && selected == keyStr)?opts.selectedStr:'';
						cObj.append('<option value="'+keyStr+'" ' + selstr + ' >'+valueStr+'</option>');
					});
				});
			}else{//单列数据格式（后数据格式：Map<String,Object>）
				objs.each(function(i,row){
					var cObj = $(this),selected = cObj.attr(opts.selectField),selstr='';
					cObj.empty();
					if(opts.hasLead && opts.leadStr){//默认首项
						cObj.append("<option value=''>"+opts.leadStr+"</option>");
					}
					for(key in data){
						selstr = (selected && selected == key.toString())?opts.selectedStr:'';
						cObj.append('<option value="'+key+'" ' + selstr + ' >'+data[key]+'</option>');
					}
				});
			}
		}
	},
	//获得表单内容
	formParams : function(p) {
		if (!p)return;
		//将表单数据放到数组中，本方法配合formParams方法使用
		var putParam = function (_obj,_dataList){
			if($.isArray(_dataList) && _obj && $(_obj).attr("name")){
				_dataList.push({ 'name' : $(_obj).attr("name"),'value' : $(_obj).val() });
			}
		};
		var _pdata = new Array;
		$(p).find(":input").each(function(i, n) {
			var flag = "false";
			flag = !$(n).attr("param") ? "true" : $(n).attr("param");
			if (flag == true || flag == "true") {
				if ($(n).attr('type') == "text" || $(n).attr('type') == "password" || $(n).attr('type') == "hidden" || n.tagName == 'TEXTAREA') {
					putParam(n,_pdata);
				} else if(n.tagName == 'SELECT') {
					if ($.trim($(n).val()) != '') {
						putParam(n,_pdata);
					} else {
						if($(n).attr("name")){
							_pdata.push({ 'name' : $(n).attr("name"), 'value' : '' });
						}
					}
				}
			}
		});

		//检查数组中的对象是否存在指定名称的对象
		var containsKey = function(name,arr){
			var index = -1;
			$.each(arr,function(i,n){
				if(n.name == name) index = i;
			});
			return index == -1 ? false : true;
		};
		//将内容保存到数组中，保证字段不重复
		var putParamOnly = function(name,value,arr){
			if($.isArray(arr) && name){
				var setValue = function(){
					arr.push({'name' : name, 'value' : value});
				};
				if(arr.length>0){
					if(!containsKey(name,arr)) setValue();
				}else{
					setValue();
				}
			}
		};
		//处理checkbox数据
		var selectarray = new Array();
		$(p).find(':checkbox,:radio').each(function(i,n){
			var name = $(n).attr('name');
			if(!containsKey(name,selectarray)){
				var group = $("[name='"+name+"']:checked",$(p));
				if($(group).size()>0){
					var vs = new Array();
					$(group).each(function(i,n){
						vs.push($(this).val());
					});
					putParamOnly(name,vs.toString(),selectarray);
				}else{
					putParamOnly(name,'',selectarray);
				}
			}
		});
		
		if(selectarray.length>0) _pdata = _pdata.concat(selectarray);
		return _pdata;
	},
	//表单数据自动填充，适合于填充Input等输入框
	//formId:表单ID名称
	//dataObj:通过Ajax查询返回的model数据json对象
	//modelName:JSP中控件设置name属性时的model对象名前缀（trLine.lineName为例，此处应传递trLine字符，请注意大小写区分）
	//domain:当前域，通常为一个页面或一个Tab或是一个Dialog，若设置了则在domain范围查找对象
	formDataFill:function (formId,dataObj,modelName,domain){
		var formObj = domain?$("#"+formId,$(domain)):$("#"+formId);
		if(!formObj)return;
		var property,iptName,value;
		for(property in dataObj){
			value = eval('dataObj.'+property);
			if(typeof(value) == 'string' || typeof(value) == 'number' || typeof(value) == 'boolean'){
				iptName = modelName ? modelName + '.' + property : property;
				$("[name='"+iptName+"']",formObj).not(':button,:submit,:reset,:checkbox,:radio').val(value);
				//处理单选框，复选框的选中问题，checkbox的值必须是以,号隔开的串，例：'1,2,5,6'
				var select = $("[name='"+iptName+"']",formObj).filter(':checkbox,:radio');
				if($(select).size()>0){
					var values = value.split(',');
					$.each(values,function(i,n){
						$("[name='"+iptName+"'][value='"+n+"']",formObj).prop('checked',true);
					});
				}
			}
		}
	},
	//表单数据清空
	//formId:表单ID名称
	//modelName:JSP中控件设置name属性时的model对象名前缀（trLine.lineName为例，此处应传递trLine字符，请注意大小写区分）
	//domain:当前域，通常为一个页面或一个Tab或是一个Dialog，若设置了则在domain范围查找对象
	//exceptIds不清空内容的控件ID集,数据格式为Array["name","id",...]
	formDataClean:function(formId,modelName,domain,exceptIds){
		var formObj = domain?$("#"+formId,$(domain)):$("#"+formId);
		if(!formObj)return;
		var ids = '';
		if(exceptIds && $.isArray(exceptIds)){
			var splitStr = '';
			for(var i=0;i<exceptIds.length;i++){
				splitStr = ids.length>0?',':'';
				ids += splitStr + '#'+exceptIds[i];
			}
		}
		var inputs = $("[name*='"+modelName+"']",formObj).not(ids).not(':button,:submit,:reset');
		$(inputs).not(":checkbox,:radio").val('');//清空输入框内容
		$(inputs).find(":checkbox,:radio").prop("checked",false);//清空单选、复选框的选中项
	},
	//清空表单数据
	//formName表单ID
	//exceptIds不清空内容的控件ID集,数据格式为Array["name","id",...]
	resetForm:function(formName,exceptIds){
		var ids = '';
		if(exceptIds){
			var splitStr = '';
			for(var i=0;i<exceptIds.length;i++){
				splitStr = ids.length>0?',':'';
				ids += splitStr + '#'+exceptIds[i];
			}
		}
		$(formName+' input').not(ids).not(':button,:submit,:reset,:checkbox,:radio').val('');
		$(formName+' input').not(ids).filter(':checkbox,:radio').removeProp('checked');//.removeProp('selected')
		$(formName+' textarea').not(ids).val('');
		//$(formName+' select').not(ids).val('');
		$('option:first',$(formName+' select').not(ids)).prop('selected',true);
	},
	//表单详细查看页面数据自动填充，适合Div、Label、Span等文件显示标签
	//formId:表单ID名称
	//dataObj:通过Ajax查询返回的model数据json对象
	//modelName:JSP中控件设置name属性时的model对象名前缀（trLine.lineName为例，此处应传递trLine字符，请注意大小写区分）
	textDataFill:function (boxId,dataObj,modelName,domain){
		var formObj = domain?$("#"+boxId,$(domain)):$("#"+boxId);
		if(!formObj)return;
		var property,iptName,value;
		for(property in dataObj){
			value = dataObj[property];
			if(typeof(value) == 'string' || typeof(value) == 'number' || typeof(value) == 'boolean'){
				iptName = modelName + '.' + property;
				$("[name='"+iptName+"']",formObj).not('select,input').not(':button,:submit,:reset,:checkbox,:radio,:text').text(value);
				$("[name='"+iptName+"']",formObj).filter('select').val(value);
				//处理单选框，复选框的选中问题，checkbox的值必须是以,号隔开的串，例：'1,2,5,6'
				var select = $("[name='"+iptName+"']",formObj).filter(':checkbox,:radio');
				if($(select).size()>0){
					$(select).prop('disabled',true);
					var values = value.split(',');
					$.each(values,function(i,n){
						$("[name='"+iptName+"'][value='"+n+"']",formObj).prop('checked',true);
					});
				}
			}
		}
	},
	//将指定对象内的所有下拉框内容隐藏并添加span显示值，通常用于查看明细页面
	showSelectValue : function(box){
		$("select",box).each(function(i,row){
			$(row).after("<span>" + $("option:selected",row).text() + "</span>").hide();
		});
	},
	//在表单里对输入框录入完内容后，按回车，自动跳到下一个输入框中
	//formObj : 表单对象（jQuery）：例：$("#xxxForm")
	//不适合加入select、textarea、checkbox、radio等对象，在处理上有很多不方便的地方
	//select不能用脚本让列表下拉出来，textarea是大文本编辑器，必须可以使用回车
	enter2next : function(formObj){
		if(!formObj)return;
		var inputs = $(":text",formObj).not(":disabled");
		//若只有一个录入框，浏览器原生会生输入框绑定回车提交表单
		if(inputs.size()<=1)return;
		var donext = function(objs,index){
			if(index+1 < objs.size()){
				objs[index+1].focus();
			}
		};
		$.each(inputs,function(i,obj){
			$(this).keypress(function(evt){
				var key_code = window.event ? window.event.keyCode : evt.which ? evt.which : evt.charCode;
				if(key_code==13)donext(inputs,i);
			});
		});
	},
	//使用ctrl+enter快速提交表单
	quickSubmitForm:function(eventObj){
		//获得当前按键的键值
		var key_code = window.event ? window.event.keyCode : eventObj.which ? eventObj.which : eventObj.charCode;
		if(eventObj.ctrlKey && key_code==13){
			try{
				quickSubmitTempFunction();
			}catch(e){}
		}
	},
	//屏蔽所有输入框
	//formName表单ID
	//exceptIds不清空内容的控件ID集,数据格式为Array["name","id",...]
	disableAllInput : function(formName,exceptIds){
		var ids = '';
		if(exceptIds){
			var splitStr = '';
			for(var i=0;i<exceptIds.length;i++){
				splitStr = ids.length>0?',':'';
				ids += splitStr + '#'+exceptIds[i];
			}
		}
		$(formName+' input').not(ids).not(':button,:submit,:reset,:checkbox,:radio').prop('disabled',true);
		//$('#'+ formName+' input').not(ids).filter(':checkbox,:radio').removeProp('checked');//.removeProp('selected')
		$(formName+' textarea').not(ids).prop('disabled',true);
		$(formName+' select').not(ids).prop('disabled',true);
	},
	//设置表单控件为只读
	setFormReadonly : function(formId,domain){
		var formObj = domain?$(formId,$(domain)):$(formId);
		$(":text,textarea",formObj).prop("readonly",true);
		$("select,:checkbox,:radio,.Wdate",formObj).prop("disabled",true);
	},
	//设置表单控件为只读
	unsetFormReadonly : function(formId,domain){
		var formObj = domain?$(formId,$(domain)):$(formId);
		$(":text,textarea",formObj).prop("readonly",false);
		$("select,:checkbox,:radio,.Wdate",formObj).prop("disabled",false);
	}
};