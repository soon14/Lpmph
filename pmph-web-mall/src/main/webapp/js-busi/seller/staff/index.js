$(function(){

	$("#shopId", "#searchForm").change(function(){
		var self = $(this);
		//重载树
		createZtree();
		//初始化表单
		var shopId = self.val();
		if(!shopId){
			return;
		}
		var shopName = self.find("option:selected").text();
		$("#shopId","#groupdetail").val(shopId);
		$("#shopName","#groupdetail").val(shopName);
		_clearForm();
	});
	
	/**
	 * 添加分组  form展示
	 */
	$("#btn_dir_add").click(function(){
		_clearForm();
		showGroupdetail();
	});
	
	/**
	 * 调用保存
	 */
	$("#btn_group_save").click(function(){
		if(!$("#groupdetail").valid())return false;
		var data = ebcForm.formParams($("#groupdetail"));
		data.push({"name":"groupClass","value":"10"});
		var id = $("#groupdetail").find("#id").val();
		$.eAjax({
	        url: GLOBAL.WEBROOT + '/seller/customer/custmanage/group/save',
	        data: data,
	        datatype: 'json',
	        success: function(returnInfo) {
	        	if(id){
	        		eDialog.alert("修改成功！");
	        	}else {
	        		eDialog.alert("添加成功！");
	        	}
	        	//回填
	        	createZtree();
	        },
	        exception: function() {
	            eDialog.alert("常用语业务调用失败！");
	            return;
	        }
        });
	});
	
	/**
	 * 添加常用语 form展示
	 */
	$("#btn_add").click(function(){
		var selectedNode = _getZTreeObj().getSelectedNodes();
		//选中的一个节点
		if(!(selectedNode && selectedNode.length==1 && _hasChildren(selectedNode))){
			eDialog.alert("请选择一个分组！");  
			return;
		}
		_clearForm();
		showItemdetail();
	});
	
	function showItemdetail(){
		$("#itemdetail").show();
		$("#groupdetail").hide();
	}
	
	function showGroupdetail(){
		$("#groupdetail").show();
		$("#itemdetail").hide();
	}
	
	/**
	 * 调用保存
	 */
	$("#btn_item_save").click(function(){
		if(!$("#itemdetail").valid())return false;
		var data = ebcForm.formParams($("#itemdetail"));
		var groupIdBelong = "";
		var selectedNode = _getZTreeObj().getSelectedNodes();
		//选中的一个节点
		if(selectedNode && selectedNode.length==1){
			if(_hasChildren(selectedNode)){
				groupIdBelong = selectedNode[0].id;
			}else{
				var pn = selectedNode[0].getParentNode();
				groupIdBelong = pn.id;
			}
		}
		
		if(!groupIdBelong){
			eDialog.alert("请选择一个分组！");  
			return;
		}
		var id = $("#itemdetail").find("#id").val();
		var groupId = {name:"groupId",value:groupIdBelong};
		data.push(groupId);
		$.eAjax({
	        url: GLOBAL.WEBROOT + '/seller/customer/custmanage/item/save',
	        data: data,
	        datatype: 'json',
	        success: function(returnInfo) {
	        	if(id){
	        		eDialog.alert("修改成功！");
	        	}else{
	        		eDialog.alert("添加成功！");
	        	}
	        	createZtree();
	        },
	        exception: function() {
	            eDialog.alert("常用语业务调用失败！");
	            return;
	        }
        });
	});
	
	/**
	 * 删除
	 */
	$("#btn_del").click(function(){
		//当前选中的节点
		var selectedNode = _getZTreeObj().getSelectedNodes();
		//选中的一个节点
		if(!(selectedNode && selectedNode.length==1)){
			eDialog.alert("请选择一个节点！");
			return;
		}
		var url = GLOBAL.WEBROOT + '/seller/customer/custmanage/item/del';
		if(_hasChildren(selectedNode)){//拥有子节点，为分组
			url = GLOBAL.WEBROOT + '/seller/customer/custmanage/group/del';
		}
		var id = selectedNode[0].id;
		var data = {id:id};
		
		eDialog.confirm("是否确定删除？", {
			buttons : [{
				caption : '删除',
				callback : function(){
					$.eAjax({
				        url: url,
				        data: data,
				        datatype: 'json',
				        success: function(returnInfo) {
				        	eDialog.alert("删除成功！");
				        	createZtree();
				        },
				        exception: function() {
				            eDialog.alert("常用语业务调用失败！");
				            return;
				        }
			        });
				}
			},{
				caption : '取消',
				callback : function(){
					bDialog.closeCurrent();
				}
			}]
		});
	});
	
	/******************** 树开始 ********************/
	var setting = {
		callback:{
			onClick: onClick,
			onAsyncSuccess: onAsyncSuccess
		},
		view: {
			selectedMulti: false,
			addDiyDom: addDiyDom
		}
	};
	
	/**
	 * 节点样式
	 */
	function getFont(treeId, node) {
		var normalCss = {color:"#333", "font-weight":"normal"};
		if(!!node.highlight){
			return {color:"#e66828", "font-weight":"bold"};
		}else{
			var res = node.font ? _isNullObj(node.font) ? normalCss : node.font : normalCss;
			return res;
		}
	}
	
	/**
	 * 自定义展现
	 */
	function addDiyDom(treeId, treeNode) {
        var spantxt = $("#" + treeNode.tId + "_span").html();
        if (spantxt.length > 17) {
            spantxt = spantxt.substring(0, 17) + "...";
            $("#" + treeNode.tId + "_span").html(spantxt);
        }
    }
	
	/**
	 * 是否是空对象
	 */
	function _isNullObj(obj){
	    for(var i in obj){
	        if(obj.hasOwnProperty(i)){
	            return false;
	        }
	    }
	    return true;
	}

	/**
	 * 节点单击事件
	 */
	function onClick(event, treeId, treeNode, clickFlag) {
		if(clickFlag==1){
			var parentNode = treeNode.getParentNode();
			if(treeNode.isParent){//分组
				_fillGroupDetail({
					id: treeNode.id,
					groupName: treeNode.name,
					sortNo: treeNode.sortNo
				});
				showGroupdetail();
			}else{//常用语
				_fillItemDetail({
					id: treeNode.id,
					itemText: treeNode.name,
					sortNo: treeNode.sortNo,
					groupId: parentNode.id
				});
				showItemdetail();
			}
		}else{
			_clearForm();
		}
	}
	
	
	/**
	 * 加载树成功后初始化
	 */
	function onAsyncSuccess(event, treeId, treeNode, msg){
		_clearForm();
	}
	
	/**
	 * 填充表单数据
	 */
	function _fillForm(node){
		if(node){
		}
	}
	
	/**
	 * 填充分组表单
	 */
	function _fillGroupDetail(node){
		if($.isArray(node) && node.length>0) node = node[0];
		var form = $("#groupdetail");
		form.find("#id").val(node.id);
		form.find("#groupName").val(node.groupName);
		form.find("#sortNo").val(node.sortNo);
	}
	
	/**
	 * 填充常用语表单
	 */
	function _fillItemDetail(node){
		if($.isArray(node) && node.length>0) node = node[0];
		var form = $("#itemdetail");
		form.find("#id").val(node.id);
		form.find("#itemText").val(node.itemText);
		form.find("#sortNo").val(node.sortNo);
	}
	
	/**
	 * 清空表单
	 */
	function _clearForm(){
		//清空表单
		$("#itemdetail,#groupdetail").find(":input").each(function(i){
			var self = $(this);
			var id = self.attr("id");
			if(id=="shopId"||id=="shopName"){
				return true;
			}
			self.val("");
		});
	}
	
	//得到当前树对象
	function _getZTreeObj(){
		return $.fn.zTree.getZTreeObj("treeBase");
	}
	
	//是否有子集
	function _hasChildren(node){
		if($.isArray(node) && node.length>0) node = node[0];
		for(var key in node){
			if(key=="children"){
				return true;
			}
		}
		return false;
	}
	
	
	/**初始化树**/
	function loadZTreeObj(zNodes){
		$.fn.zTree.destroy("treeBase");//销毁
		$.fn.zTree.init($("#treeBase"), setting, zNodes);
	}
	
	/**
	 * 生成树
	 */
	function createZtree(){
		if(!$("#shopId").val()){
			return;
		}
		$.ajax({
	        url: GLOBAL.WEBROOT + '/seller/customer/custmanage/tree',
	        data: {
	        	shopId:$("#shopId").val(),
	        	//IE第二次请求Ajax如果参数或者URL不变，会直接取缓存的数据，这里需要传一个随机函数-----------------------------------
	        	dataTime: Math.random()
	        	},
	        success: function(returnInfo) {
	        	returnInfo = $.parseJSON(returnInfo);
	        	loadZTreeObj(returnInfo);
	        },
	        exception: function() {
	            eDialog.alert("常用语业务调用失败！");
	            return;
	        }
        });
	}
	/******************** 树结束 ********************/
	
	
	$("#shopId", "#searchForm").trigger("change");//初始化树
});