var status ="0";
$(function(){
	var shopParams = new Object();
	shopParams.shopId = $("#shopId").val();
	$.eAjax({
		url : GLOBAL.WEBROOT + "/gdsmanage/shopStatus",
		data : shopParams,
		success : function(returnInfo) {
		  if(returnInfo.status == '1'){
			  $("#batchOpt").show();


		  }else{
			  $("#batchOpt").hide();

		  }
		}
	});
	var arr = new Array();
	if($("#ifGdsScore").val()=='1'){
		arr = new Array(
				{ "mData": "id", "sTitle":"商品编码","sWidth":"80px","sClass": "center","bSortable":false},
				{ "mData": "gdsName", "sTitle":"商品名称","sWidth":"80px","sClass": "center","bSortable":false,
				  "mRender": function(data,type,row){
					  if(row.gdsStatus == '99'){
							//<a href='javascript:void(0)' onclick='gdsRestart(this)'>重新启用</a>
							return data;
					  }
					  return '<a  target="_blank" href="'+row.gdsDetailUrl+'">'+data+'</a>';
				  }
				},
				{ "mData": "mainCatgName", "sTitle":"主分类名称","sWidth":"80px","sClass": "center","bSortable":false},
				{ "mData": "guidePrice", "sTitle":"商品指导价","sWidth":"80px","sClass": "center","bSortable":false,
					"mRender": function(data,type,row){
						var str = (data/100).toFixed(2) + '';
						var intSum = str.substring(0,str.indexOf("."));//取到整数部分.replace( /\B(?=(?:\d{3})+$)/g, ',' )
						var dot = str.substring(str.length,str.indexOf("."));//取到小数部分
						var ret = intSum + dot;
						return ret;
					}
				},
//				{ "mData": "gdsStatus","sTitle":"单品管理","sWidth":"80px","sClass": "center","bSortable":false,
//					"mRender": function(data,type,row){
//						return "<a href='javascript:void(0)' onclick=\"toSkuManage(this,'"+row.id+"','"+row.gdsStatus+"')\">单品管理</a>";
//					}
//				},
//				{ "mData": "shipTemplateName", "sTitle":"运费模板","sWidth":"80px","sClass": "center","bSortable":false,
//					"bVisible":true,
//					"mRender": function(data,type,row){
//						if(row.gdsStatus=='99'||row.gdsStatus=='11'){
//							if(row.shipTemplateId== -1 || row.shipTemplateId==0 || data==null){
//								return "<a style='color:#ddd' href='javascript:void(0)' disabled>设置运费模板</a>";
//							}else{
//								return data+"</br><a  style='color:#ddd'href='javascript:void(0)' disabled>取消运费模板</a>";
//							}
//						}else{
//							if(row.shipTemplateId== -1 || row.shipTemplateId == 0||data==null){
//								return "<a href='javascript:void(0)' onclick=\"setGdsShiptemp(this,'"+row.id+"','"+row.shopId+"')\">设置运费模板</a>";
//							}else{
//								return data+"</br><a href='javascript:void(0)' onclick=\"cancelGdsShiptemp(this,'"+row.id+"','"+row.shopId+"')\">取消运费模板</a>";
//							}
//						}
//					}
//				},

				{ "mData": "gdsStatusName", "sTitle":"状态","sWidth":"80px","sClass": "center","bSortable":false},
				{"targets": -1,"mData": "gdsStatus","sTitle":"操作","sWidth":"80px","sClass": "center",
					"mRender": function(data,type,row){
						var validShop = '';
						if(row.shopStatus=='0'){
							if(data == '0'){
								return "<a href='javascript:void(0)' "+validShop+">上架</a> | <a href='javascript:void(0)' "+validShop+" >编辑</a> | <a href='javascript:void(0)' "+validShop+" >删除</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
							}else if(data == '11'){
								return "<a href='javascript:void(0)' "+validShop+">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
							}else if(data == '22'){
								return "<a href='javascript:void(0)' "+validShop+">上架</a> | <a href='javascript:void(0)' "+validShop+" >编辑</a> | <a href='javascript:void(0)' "+validShop+" >删除</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
							}else if(data == '99'){
								//<a href='javascript:void(0)' onclick='gdsRestart(this)'>重新启用</a>
								return "-";
							}else {
								return "-";
							}
						}else{
							if(data == '0'){
								return "<a href='javascript:void(0)'  onclick=\"gdsUp(this,'single','"+row.shopId+"')\">上架</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEdit('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"')\">编辑</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsRemove(this,'single','"+row.shopId+"')\">删除</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
							}else if(data == '11'){
								return "<a href='javascript:void(0)'  onclick=\"gdsDown(this,'single','"+row.shopId+"')\">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
							}else if(data == '22'){
								return "<a href='javascript:void(0)' onclick=\"gdsUp(this,'single','"+row.shopId+"')\">上架</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEdit('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"')\">编辑</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsRemove(this,'single','"+row.shopId+"')\">删除</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
							}else if(data == '99'){
								//<a href='javascript:void(0)' onclick='gdsRestart(this)'>重新启用</a>
								return "-";
							}else {
								return "-";
							}
						}
					}
				}
			);
	}else{
		arr = new Array(
				{ "mData": "id", "sTitle":"商品编码","sWidth":"80px","sClass": "center","bSortable":false},
				{ "mData": "gdsName", "sTitle":"商品名称","sWidth":"80px","sClass": "center","bSortable":false,
				  "mRender": function(data,type,row){
					  if(row.gdsStatus == '99'){
							//<a href='javascript:void(0)' onclick='gdsRestart(this)'>重新启用</a>
							return data;
					  }
					  return '<a  target="_blank" href="'+row.gdsDetailUrl+'">'+data+'</a>';
				  }
				},
				{ "mData": "mainCatgName", "sTitle":"主分类名称","sWidth":"80px","sClass": "center","bSortable":false},
				{ "mData": "isbn", "sTitle":"ISBN","sWidth":"80px","sClass": "center","bSortable":false},
				{ "mData": "guidePrice", "sTitle":"商品指导价","sWidth":"80px","sClass": "center","bSortable":false,
					"mRender": function(data,type,row){
						var str = (data/100).toFixed(2) + '';
						var intSum = str.substring(0,str.indexOf("."));//取到整数部分.replace( /\B(?=(?:\d{3})+$)/g, ',' )
						var dot = str.substring(str.length,str.indexOf("."));//取到小数部分
						var ret = intSum + dot;
						return ret;
					}
				},
//				{ "mData": "gdsStatus","sTitle":"单品管理","sWidth":"80px","sClass": "center","bSortable":false,
//					"mRender": function(data,type,row){
//						return "<a href='javascript:void(0)' onclick=\"toSkuManage(this,'"+row.id+"','"+row.gdsStatus+"')\">单品管理</a>";
//					}
//				},
				{ "mData": "shipTemplateName", "sTitle":"运费模板","sWidth":"80px","sClass": "center","bSortable":false,
					"bVisible":true,
					"mRender": function(data,type,row){
						if(row.gdsStatus=='99'||row.gdsStatus=='11'){
							if(row.shipTemplateId== -1 || row.shipTemplateId==0 || data==null){
								return "<a style='color:#ddd' href='javascript:void(0)' disabled>设置运费模板</a>";
							}else{
								return data+"</br><a  style='color:#ddd'href='javascript:void(0)' disabled>取消运费模板</a>";
							}
						}else{
							if(row.shipTemplateId== -1 || row.shipTemplateId == 0||data==null){
								return "<a href='javascript:void(0)' onclick=\"setGdsShiptemp(this,'"+row.id+"','"+row.shopId+"')\">设置运费模板</a>";
							}else{
								return data+"</br><a href='javascript:void(0)' onclick=\"cancelGdsShiptemp(this,'"+row.id+"','"+row.shopId+"')\">取消运费模板</a>";
							}
						}
					}
				},
				{ "mData": "gdsPublishTime", "sTitle":"出版日期","sWidth":"80px","sClass": "center","bSortable":false},
				{ "mData": "gdsAuthor", "sTitle":"作者","sWidth":"80px","sClass": "center","bSortable":false},
				{ "mData": "gdsStatusName", "sTitle":"状态","sWidth":"80px","sClass": "center","bSortable":false},
				{"mData": "gdsStatus","sTitle":"操作","sWidth":"80px","sClass": "center",
					"mRender": function(data,type,row){
						var validShop = '';
						var oper = "";
						if(row.shopStatus=='0'){
							if(data == '0'){
								/*if(row.verifySwitch=='1'){
									oper += "<a href='javascript:void(0)' "+validShop+">提交上架审核</a> | <a href='javascript:void(0)' "+validShop+" >编辑</a> | <a href='javascript:void(0)' "+validShop+" >提交删除审核</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a> | <a href='javascript:void(0)'>审核记录</a>";
								}else{
									oper += "<a href='javascript:void(0)' "+validShop+">上架</a> | <a href='javascript:void(0)' "+validShop+" >编辑</a> | <a href='javascript:void(0)' "+validShop+" >删除</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a>";
								}
								return oper;*/
								return "<a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
							}else if(data == '11'){
								/*if(row.verifySwitch=='1'){
									oper += "<a href='javascript:void(0)' "+validShop+">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a> | <a href='javascript:void(0)'>审核记录</a>";
								}else{
									oper += "<a href='javascript:void(0)' "+validShop+">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a>";
								}
								return oper;*/
								return "-";
							}else if(data == '22'){
								/*if(row.verifySwitch == '1'){
									oper += "<a href='javascript:void(0)' "+validShop+">提交上架审核</a> | <a href='javascript:void(0)' "+validShop+" >编辑</a> | <a href='javascript:void(0)' "+validShop+" >提交删除审核</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a> | <a href='javascript:void(0)'>审核记录</a>";
								}else{
									oper += "<a href='javascript:void(0)' "+validShop+">上架</a> | <a href='javascript:void(0)' "+validShop+" >编辑</a> | <a href='javascript:void(0)' "+validShop+" >删除</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a>";
								}
								return oper;*/
								return "<a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
							}else if(data == '99'){
								//<a href='javascript:void(0)' onclick='gdsRestart(this)'>重新启用</a>.
								/*if(row.verifySwitch == '1'){
									return "<a href='javascript:void(0)'>审核记录</a>";
								}else{
									return "-";
								}*/
								return "-";
							}else {
								return "-";
							}
						}else{
							//商店都处于有效状态
							var paramStr = "gdsId='"+row.id+"' shopId='"+row.shopId+"'";
							//0是待上架
							if(data == '0'){
								if(row.verifySwitch == '1'){
									oper += "<a href='javascript:void(0)' class='commitForVerify' value='11' "+paramStr+">提交上架审核</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEdit('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"')\">编辑</a> | <a href='javascript:void(0)' "+validShop+" class='commitForVerify' value='99' "+paramStr+">提交删除审核</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a> | <a href='javascript:void(0)' onclick=showVeriyRecord('"+row.shopId+"','"+row.id+"')>审核记录</a>";
								}else{
									oper += "<a href='javascript:void(0)'  onclick=\"gdsUp(this,'single','"+row.shopId+"')\">上架</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEdit('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"')\">编辑</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsRemove(this,'single','"+row.shopId+"')\">删除</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
								}
								return oper;
							//11是已上架
							}else if(data == '11'){
								if(row.verifySwitch == '1'){
									if(row.gdsOfflineApprove == '0'){
										oper += "<a href='javascript:void(0)' class='commitForVerify' value='33' "+paramStr+">提交更新审核</a> | <a href='javascript:void(0)'  onclick=\"gdsDown(this,'single','"+row.shopId+"')\">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEditShelved('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"','"+row.gdsStatus+"')\">编辑</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a> | <a href='javascript:void(0)' onclick=showVeriyRecord('"+row.shopId+"','"+row.id+"')>审核记录</a>";
									}else{
										oper += "<a href='javascript:void(0)'  onclick=\"gdsDown(this,'single','"+row.shopId+"')\">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEditShelved('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"','"+row.gdsStatus+"')\">编辑</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"')\">预览</a> | <a href='javascript:void(0)' onclick=showVeriyRecord('"+row.shopId+"','"+row.id+"','"+row.ifScoreGds+"')>审核记录</a>";
									}
									//oper += "<a href='javascript:void(0)'  onclick=\"gdsDown(this,'single','"+row.shopId+"')\">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' onclick=showVeriyRecord('"+row.shopId+"','"+row.id+"')>审核记录</a>";
								}else{
									//if(row.gdsOfflineApprove == '0'){
										//oper += "<a href='javascript:void(0)' class='commitForVerify' value='33' "+paramStr+">提交更新审核</a> | <a href='javascript:void(0)'  onclick=\"gdsDown(this,'single','"+row.shopId+"')\">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEditShelved('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"','"+row.gdsStatus+"')\">编辑</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a //href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
									//}else{
										oper += "<a href='javascript:void(0)'  onclick=\"gdsDown(this,'single','"+row.shopId+"')\">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEditShelvedWithoutVerify('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"','"+row.gdsStatus+"')\">编辑</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a>";
									//}
									//oper += "<a href='javascript:void(0)'  onclick=\"gdsDown(this,'single','"+row.shopId+"')\">下架</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>";
								}
								return oper;
								//22已下架
							}else if(data == '22'){
								if(row.verifySwitch == '1'){
									oper += "<a href='javascript:void(0)' class='commitForVerify' value='11' "+paramStr+">提交上架审核</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEdit('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"')\">编辑</a> | <a href='javascript:void(0)' "+validShop+" class='commitForVerify' value='99' "+paramStr+">提交删除审核</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"')\">预览</a> | <a href='javascript:void(0)' onclick=showVeriyRecord('"+row.shopId+"','"+row.id+"')>审核记录</a>";
								}else{
									oper += "<a href='javascript:void(0)' onclick=\"gdsUp(this,'single','"+row.shopId+"')\">上架</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsEdit('"+row.id+"','"+row.ifScoreGds+"','"+row.shopId+"')\">编辑</a> | <a href='javascript:void(0)' "+validShop+" onclick=\"gdsRemove(this,'single','"+row.shopId+"')\">删除</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsDetail('"+row.id+"','"+row.ifScoreGds+"')\">详情</a>| <a href='javascript:void(0)' "+validShop+" onclick=\"gdsPreview('"+row.id+"','"+row.ifScoreGds+"','"+row.ifScoreGds+"')\">预览</a>";
								}
								return oper;
								//已删除
							}else if(data == '99'){
								//<a href='javascript:void(0)' onclick='gdsRestart(this)'>重新启用</a>
								if(row.verifySwitch == '1'){
									return "<a href='javascript:void(0)' onclick=showVeriyRecord('"+row.shopId+"','"+row.id+"')>审核记录</a>";
								}else{
									return "-";
								}
							}else {
								return "-";
							}
						}
					}
				}
			);
	}
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : true,
        'pCheck' : 'multi',
        "sAjaxSource": GLOBAL.WEBROOT + '/gdsmanage/gridlist',
        //指定列数据位置
        "aoColumns": arr,
        "params" : [{name : 'shopId',value : $("#shopId").val()},
                    {name : 'ifGdsScore',value : $("#ifGdsScore").val()}],
        "createdRow": function ( row, data, index ) {
			$('td', row).eq(2).css('word-break',"break-all");
        }

	});
	GdsManage.initValid();
	//查询
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var param = new Object();
		param.shopId = $("#shopId").val();
		$.eAjax({
			url : GLOBAL.WEBROOT + "/gdsmanage/shopStatus",
			data : param,
			success : function(returnInfo) {
			  if(returnInfo.status == '1'){
				  $("#batchOpt").show();


			  }else{
				  $("#batchOpt").hide();

			  }
			}
		});
		var p = ebcForm.formParams($("#searchForm"));
		p.push({ 'name': 'catgCode','value' : $("#catgCode").attr('catgCode') });
		p.push({ 'name': 'ifGdsScore','value' : $("#ifGdsScore").val()});
		p.push({'name':'isbn','value':$("#isbn").val()});
		$.gridLoading({"messsage":"正在加载中...."});
		try {
			$('#dataGridTable').gridSearch(p);
		} catch (e) {
		}
		$.gridUnLoading();
	});
	//重置
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
		var catgCode=$('#catgCode');
		if(catgCode.attr("catgcode")){
			catgCode.removeAttr("catgcode");
		}
		if($("#ifGdsScore").val()=="1"){
			var scoreShopId = $('#SCORE_SHOP_ID').val();
			$("#shopId option[value='"+scoreShopId+"']").attr('selected','selected');
		}
//		if($("#ifGdsScore").val()=="1"){
//			$("#shopId option[value='101']").attr('selected','selected');
//		}
	});
	//添加商品
	$('#btn_code_add').click(function(){
		var _ifGdsScore = $("#ifGdsScore").val();
		if(_ifGdsScore=='1'){
			window.location.href = GLOBAL.WEBROOT+'/gdspointentry';
		}else{
			window.location.href = GLOBAL.WEBROOT+'/gdsinfoentry';
		}
	});
//	//设置运费模板
//	$('#btn_code_set').click(function(){
//		var ids = $('#dataGridTable').getCheckIds();
//		if(!ids || ids.length==0){
//			eDialog.alert('请选择一条商品记录进行操作！');
//			return ;
//		}else if(ids.length >= 2){
//			eDialog.alert('只能选择一条商品记录进行操作！');
//			return ;
//		}
//		bDialog.open({
//			title : '设置运费模板',
//			width : 860,
//			height : 500,
//			params : {
//				'gdsId': ids[0]
//			},
//			url : GLOBAL.WEBROOT + '/gdsmanage/free-open',
//			callback:function(data){
//				if(data && data.results && data.results.length > 0 ){
//                    var flag = data.results[0].flag;
//                    if(flag == "ok"){
//                    	GdsManage.gridGdsList();
//                    }else{
//                    	eDialog.error('配置模板失败！');
//                    }
//				}
//			}
//		});
//	});
	//批量上架
	$("#btn_code_up").live('click',function(){
		if($("#GDS_VERIFY_SWITCH").val()=='1'){
			GdsManage.batchCommitForVerify('11');
		}else{
			GdsManage.gdsBatchUp();
		}
	});
	//批量下架
	$("#btn_code_down").live('click',function(){
		GdsManage.gdsBatchDown();
	});
	//批量删除
	$('#btn_code_remove').live('click',function(){
		if($("#GDS_VERIFY_SWITCH").val()=='1'){
			GdsManage.batchCommitForVerify('99');
		}else{
			GdsManage.gdsBatchRemove();
		}
	});
	//复制商品
	$("#btn_code_copy").live("click",function(){
		var ids = $('#dataGridTable').getCheckIds();
		if(!ids || ids.length==0){
			eDialog.alert('请选择一条记录进行操作！');
			return ;
		}else if(ids[0]==undefined){
			eDialog.alert('请选择至少选择一条商品记录进行操作！');
			return ;
		}
		if(ids && ids.length >=2){
			eDialog.alert("只能操作一条记录");
			return;
		}
		var _ifGdsScore = $("#ifGdsScore").val();
		if(_ifGdsScore=='1'){
			window.location.href = GLOBAL.WEBROOT +"/gdspointentry/togdsedit?formCopyFlag=1&gdsId="+ids[0];
		}else{
			window.location.href = GLOBAL.WEBROOT +"/gdsinfoentry/togdsedit?formCopyFlag=1&gdsId="+ids[0];
		}

	});

	// 商品新增导入
	$("#btn_add_import").live("click",function(){
		eNav.setSubPageText('商品新增导入');
		window.location.href = GLOBAL.WEBROOT+'/goods/excelImport/pageInit';
	});
	// 商品编辑导入
	$("#btn_edit_import").live("click",function(){
		eNav.setSubPageText('商品编辑导入');
		window.location.href = GLOBAL.WEBROOT+'/goods/excelEditImport/pageInit';
	});
	//导出
	$("#btn_gds_export").click(function(){
		var ids = $('#dataGridTable').getCheckIds();
		if(!ids || ids.length==0){
			eDialog.alert('请至少选择一条商品记录进行操作！');
			return ;
		}else if(ids[0]==undefined){
			eDialog.alert('请至少选择一条商品记录进行操作！');
			return ;
		}
		$('#btnFormSearch').trigger('click');
		var scoreShopVal=$('#scoreShop').val();
		if (typeof(scoreShopVal) == "undefined"||scoreShopVal=="") {
			scoreShopVal=$('#shopId').val();
		}
		eDialog.confirm("导出商品" , {
			buttons: [{
				'caption': '导出',
				'callback': function () {
//					$('#exportIds').val(ids.join(","));
//					$('#exportShopId').val(scoreShopVal);
//					$("#exportForm").submit();

					$.gridLoading({"message": "正在生成...."});//遮罩
			        $.eAjax({
			            url: GLOBAL.WEBROOT + '/gdsmanage/getExportFileId',
						data : {
									"ids" : ids.join(","),
									"shopId" : scoreShopVal
								},
			            success: function (result) {
			                $.gridUnLoading();
			                if (result && result.resultFlag == 'ok') {
			                    window.location.href = GLOBAL.WEBROOT + '/order/export/'+result.fileId;
			                } else {
			                    eDialog.alert(result.resultMsg);
			                }
			            },
			            failure: function () {
			                $.gridUnLoading();
			            }
			        });
				}
			}, {
				'caption': '取消',
				'callback': function () {
				}
			}]
		});
	});

	//Tab换页查询
	$("#myTab").children().each(function(){
		$(this).bind("click",function(){
			var id =$(this).attr("id");
			if(id == "tab1"){
//				$("#btn_code_set").show();
			    $("#btn_code_up").show();
			    $("#btn_code_down").hide();
			    $("#btn_code_remove").show();
			    $("#btn_code_copy").show();
				status ="0";
				GdsManage.gridGdsList();
			    $('#myTab li:eq(0) a').tab('show');

			}else if(id == "tab2"){
				status ="11";
				GdsManage.gridGdsList();
				$('#myTab li:eq(1) a').tab('show');
//				$("#btn_code_set").hide();
			    $("#btn_code_up").hide();
			    $("#btn_code_down").show();
			    $("#btn_code_remove").hide();
			    $("#btn_code_copy").show();
			}else if(id == "tab3"){
				status ="22";
				GdsManage.gridGdsList();
				$('#myTab li:eq(2) a').tab('show');
//				$("#btn_code_set").show();
			    $("#btn_code_up").show();
			    $("#btn_code_down").hide();
			    $("#btn_code_remove").show();
			    $("#btn_code_copy").show();
			}else if(id == "tab4"){
				status ="99";
				GdsManage.gridGdsList();
				$('#myTab li:eq(3) a').tab('show');
//				$("#btn_code_set").hide();
			    $("#btn_code_up").hide();
			    $("#btn_code_down").hide();
			    $("#btn_code_remove").hide();
			    $("#btn_code_copy").hide();
			}

		});
	});
	$("#catgCode").click(function(){
		catlogId = '1';
		catgType = '1';
		if($("#ifGdsScore").val()=='1'){
			catlogId = '2';
		}

		if(undefined == $('#shopId').val() || "" == $('#shopId').val()){
			 eDialog.alert('店铺必须选择!');
			 return;
		}

		bDialog.open({
            title : '分类选择',
            width : 350,
            height : 550,
            params:{multi : false,ignoreDataAuth:'false'},
            url : GLOBAL.WEBROOT+"/goods/category/open/catgselect?catgType="+catgType+"&catlogId="+catlogId,
            callback:function(data){
            	if(data && data.results && data.results.length > 0 ){
                    var _catgs = data.results[0].catgs;
					var size = _catgs.length;
					for(var i =0;i<size;i++){
						var obj = _catgs[i];
						$("#catgCode").val(obj.catgName);
						$("#catgCode").attr('catgCode',obj.catgCode);
					}
				}
            }
        });
	});
	//提交上架审核的绑定事件
	$(".commitForVerify").live('click',function(e){
		var $this = $(this);
		var params = {};
		params.operateType = $this.attr('value');
		params.gdsId = $this.attr('gdsId');
		params.shopId = $this.attr('shopId');
		GdsManage.commitForVerify(params);
		e.preventDefault();
	});
});

function gdsUp(obj,str,shopId){
	GdsManage.gdsUp(obj,str,shopId);
}
function gdsDown(obj,str,shopId){
	GdsManage.gdsDown(obj,str,shopId);
}
function gdsRemove(obj,str,shopId){
	GdsManage.gdsRemove(obj,str,shopId);
}
function gdsEdit(obj,ifScoreGds,shopId){
	GdsManage.gdsEdit(obj,ifScoreGds,shopId);
}
function gdsEditShelved(obj,ifScoreGds,shopId,gdsStatus){
	GdsManage.gdsEditShelved(obj,ifScoreGds,shopId,gdsStatus);
}
function gdsEditShelvedWithoutVerify(obj,ifScoreGds,shopId,gdsStatus){
	GdsManage.gdsEditShelvedWithoutVerify(obj,ifScoreGds,shopId,gdsStatus);
}
function gdsDetail(obj,ifScoreGds){
	GdsManage.gdsDetail(obj,ifScoreGds);
}
function gdsPreview(obj,ifScoreGds){
	GdsManage.gdsPreview(obj,ifScoreGds);
}
function toSkuManage(obj,gdsId,status){
	GdsManage.toSkuManage(obj,gdsId,status);
}
function setGdsShiptemp(obj,gdsId,shopId){
	GdsManage.setGdsShiptemp(obj,gdsId,shopId);
}
function cancelGdsShiptemp(obj,gdsId,shopId){
	GdsManage.cancelGdsShiptemp(obj,gdsId,shopId);
}
function showVeriyRecord(shopId,gdsId){
	GdsManage.showVeriyRecord(shopId,gdsId);
}
var GdsManage = {
		setGdsShiptemp : function(obj,gdsId,shopId){
//			var ids = $('#dataGridTable').getCheckIds();
//			if(!ids || ids.length==0){
//				eDialog.alert('请选择一条商品记录进行操作！');
//				return ;
//			}else if(ids.length >= 2){
//				eDialog.alert('只能选择一条商品记录进行操作！');
//				return ;
//			}
			bDialog.open({
				title : '设置运费模板',
				width : 860,
				height : 500,
				params : {
					'gdsId': gdsId,
					'shopId':shopId
				},
				url : GLOBAL.WEBROOT + '/gdsmanage/free-open',
				callback:function(data){
					if(data && data.results && data.results.length > 0 ){
	                    var flag = data.results[0].flag;
	                    if(flag == "ok"){
	                    	GdsManage.gridGdsList();
	                    }else{
	                    	eDialog.error('配置模板失败！');
	                    }
					}
				}
			});
		},
		cancelGdsShiptemp : function(obj,gdsId,shopId){
			var param = {};
			param.gdsId = gdsId;
			param.shopId = shopId;
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsmanage/setshiptemp",
				data : param,
				success : function(returnInfo) {
					if(returnInfo.resultFlag == "ok"){
                    	GdsManage.gridGdsList();
                    }else{
                    	eDialog.error('取消配置模板失败！');
                    }
				}
			});
		},
		toSkuManage : function(obj,gdsId,status){
			bDialog.open({
				title : '单品列表',
				width : 860,
				height : 400,
				url : GLOBAL.WEBROOT + '/gdsmanage/sku-open',
				params : {
					'gdsId' : gdsId,
					'status' : status
				},
				callback:function(data){
					GdsManage.gridGdsList();
				}
			});
		},
		gdsUp : function(obj,str,shopId){
			var ids = $(obj).parent().siblings().eq(1).text();
			var param = {
					operateId : ids,
					operateFlag : '1',
					shopId : shopId
			};
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsmanage/gdsupdown",
				data : param,
				success : function(returnInfo) {
					if(returnInfo.resultFlag=='ok'){
						eDialog.success('上架成功！');
						GdsManage.gridGdsList();
					}else{
						eDialog.error('上架失败！,原因是：'+returnInfo.resultMsg);
					}
				}
			});
		},
		gdsDown : function(obj,str,shopId){
			var ids  = $(obj).parent().siblings().eq(1).text();
			var param = {
					operateId : ids,
					operateFlag : '0',
					shopId : shopId
			};
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsmanage/gdsupdown",
				data : param,
				success : function(returnInfo) {
					if(returnInfo.resultFlag=='ok'){
						eDialog.success('下架成功！');
						GdsManage.gridGdsList();
					}else{
						eDialog.error('下架失败！，原因是：'+returnInfo.resultMsg);
					}
				}
			});
		},
		gdsBatchUp : function(){
			var ids = $('#dataGridTable').getCheckIds();
			if(!ids || ids.length==0){
				eDialog.alert('请选择至少选择一条商品记录进行操作！');
				return ;
			}else if(ids[0]==undefined){
				eDialog.alert('请选择至少选择一条商品记录进行操作！');
				return ;
			}
			var param = {
					operateId : ids.join(","),
					operateFlag : '1',
					shopId : $("#shopId").val()
			};
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsmanage/gdsbatchupdown",
				data : param,
				success : function(returnInfo) {
					if(returnInfo.resultFlag=='ok'){
						eDialog.success('批量上架成功！');
						GdsManage.gridGdsList();
					}else{
						eDialog.error('批量上架失败！原因是：'+returnInfo.resultMsg);
						GdsManage.gridGdsList();
					}
				}
			});
		},
		gdsBatchDown : function(){
			var ids = $('#dataGridTable').getCheckIds();
			if(!ids || ids.length==0){
				eDialog.alert('请选择至少选择一条商品记录进行操作！');
				return ;
			}else if(ids[0]==undefined){
				eDialog.alert('请选择至少选择一条商品记录进行操作！');
				return ;
			}
			var param = {
					operateId : ids.join(","),
					operateFlag : '0',
					shopId : $("#shopId").val()

			};
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsmanage/gdsbatchupdown",
				data : param,
				success : function(returnInfo) {
					if(returnInfo.resultFlag=='ok'){
						eDialog.success('批量下架成功！');
						GdsManage.gridGdsList();
					}else{
						eDialog.error('批量下架失败！原因是：'+returnInfo.resultMsg);
						GdsManage.gridGdsList();
					}
				}
			});
		},
		gdsRemove : function(obj,str,shopId){
			var ids  = $(obj).parent().siblings().eq(1).text();
			var param = {
					operateId : ids,
					operateFlag : '9',
					shopId : shopId
			};
			eDialog.confirm("您确认删除该记录吗？", {
			buttons : [ {
				caption : '确认',
				callback : function() {
					$.eAjax({
						url : GLOBAL.WEBROOT + "/gdsmanage/gdsremove",
						data : param,
						success : function(returnInfo) {
							if (returnInfo.resultFlag == 'ok') {
								eDialog.success('删除成功！');
								GdsManage.gridGdsList();
							}else{
								eDialog.error('删除失败！，原因是：'+returnInfo.resultMsg);
							}
						}
					});
				}
			}, {
				caption : '取消',
				callback : $.noop
			} ]
		});
		},
		gdsEdit : function(gdsId,ifScoreGds,shopId){
			if(ifScoreGds=='1'){
				window.location.href= GLOBAL.WEBROOT +"/gdspointentry/togdsedit?gdsId="+gdsId;
			}else{
				if($("#GDS_VERIFY_SWITCH").val()=='1'){
					//审核按钮开启的时候，先判断该商品是否处于提交待审核状态。如果是，则不允许操作
					var params = {
							'gdsId' : gdsId,
							'shopId' : shopId
							};
					GdsManage.whetherCanOperate(params);
				}else{
					window.location.href= GLOBAL.WEBROOT +"/gdsinfoentry/togdsedit?gdsId="+gdsId;
				}

			}
		},
		gdsEditShelved : function(gdsId,ifScoreGds,shopId,gdsStatus){
			var params = {};
			params.gdsId = gdsId;
			params.shopId = shopId;
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsinfoentry/ifverify",
				data : params,
				success : function(returnInfo) {
					if(returnInfo.resultFlag=='ok'){
						if(ifScoreGds=='1'){
							window.location.href= GLOBAL.WEBROOT +"/gdspointentry/togdseditshelved?gdsId="+gdsId+"&shopId="+shopId+"&gdsStatus="+gdsStatus;
						}else{
							/*if($("#GDS_VERIFY_SWITCH").val()=='1'){
								//审核按钮开启的时候，先判断该商品是否处于提交待审核状态。如果是，则不允许操作
								var params = {
										'gdsId' : gdsId,
										'shopId' : shopId
										};
								GdsManage.whetherCanOperate(params);
							}else{*/
								window.location.href= GLOBAL.WEBROOT +"/gdsinfoentry/togdseditshelved?gdsId="+gdsId+"&shopId="+shopId+"&gdsStatus="+gdsStatus;
							//}

						}
					}else{
						eDialog.error(returnInfo.resultMsg);
					}
				}
			});

		},
		gdsEditShelvedWithoutVerify : function(gdsId,ifScoreGds,shopId,gdsStatus){
			if(ifScoreGds=='1'){
				window.location.href= GLOBAL.WEBROOT +"/gdspointentry/togdseditshelved?gdsId="+gdsId+"&shopId="+shopId+"&gdsStatus="+gdsStatus;
			}else{
				window.location.href= GLOBAL.WEBROOT +"/gdsinfoentry/togdseditshelved?gdsId="+gdsId+"&shopId="+shopId+"&gdsStatus="+gdsStatus;
			}
		},
		gdsDetail : function(gdsId,ifScoreGds){
			if(ifScoreGds=='1'){
				window.location.href= GLOBAL.WEBROOT +"/gdspointentry/togdsdetail?gdsId="+gdsId;
			}else{
				window.location.href= GLOBAL.WEBROOT +"/gdsinfoentry/togdsdetail?gdsId="+gdsId;
			}
		},
		gdsPreview : function(gdsId,ifScoreGds){
			if(ifScoreGds=='1'){
				window.location.href= GLOBAL.WEBROOT +"/gdspointentry/togdsdetail?gdsId="+gdsId;
			}else{
				var params = {};
				params.gdsId = gdsId;
				params.ifScoreGds = ifScoreGds;
				bDialog.open({
		            title : '商品预览',
		            width : 800,
		            height : 550,
		            params:params,
		            url : GLOBAL.WEBROOT+"/gdsinfoentry/togdspreviewwindow?gdsId="+gdsId,
		            callback:function(data){
		            	if(data && data.results && data.results.length > 0 ){
		                    //window.location.href = GLOBAL.WEBROOT+ '/gdsverify?shopId='+ data.results[0].param;
		                    window.location.href = GLOBAL.WEBROOT+ '/gdsinfoentry/initTogdspreviewwindow?gdsId='+gdsId;
		            	}
		            }
		        });
			}
		},
		gridGdsList : function(){
			var p = ebcForm.formParams($("#searchForm"));
			p.push({ 'name': 'status','value' : status });
			p.push({ 'name': 'ifGdsScore','value' : $("#ifGdsScore").val()});
			$.gridLoading({"el":"#gridLoading","messsage":"正在加载中...."});
			$('#dataGridTable').gridSearch(p);
			$.gridUnLoading({"el":"#gridLoading"});
		},
		gdsBatchRemove : function(){
			var ids = $('#dataGridTable').getCheckIds();
			if(!ids || ids.length==0){
				eDialog.alert('请选择至少选择一条商品记录进行操作！');
				return ;
			}else if(ids[0]==undefined){
				eDialog.alert('请选择至少选择一条商品记录进行操作！');
				return ;
			}
			var param = {
					operateId : ids.join(","),
					shopId : $("#shopId").val()
			};
			if(ids && ids.length>=1){
				eDialog.confirm("您确认删除选中的商品吗？", {
					buttons : [{
						caption : '确认',
						callback : function(){
							$.eAjax({
								url : GLOBAL.WEBROOT + "/gdsmanage/gdsbatchremove",
								data : param,
								success : function(returnInfo) {
									if(returnInfo.resultFlag=='ok'){
										eDialog.success('批量删除成功！');
										GdsManage.gridGdsList();
									}else{
										eDialog.error('批量删除失败！，原因是：'+returnInfo.resultMsg);
										GdsManage.gridGdsList();
									}
								}
							});
						}
					},{
						caption : '取消',
						callback : $.noop
					}]
				});
			}else if(!ids || ids.length==0){
				eDialog.alert('请选择至少选择一条商品记录进行操作！');
			}
		},
		initValid : function(){
			jQuery.validator.addMethod("compareTime", function(value, element) {
				var endTimeId = element.id;
				var startTime= $("#startTime").val();
				var endTime = $("#" + endTimeId).val();
				if(startTime =="" || endTime ==""){
					return true;
				}
				/**
				 * 解决IE不支持trim问题
				 */
				if (!String.prototype.trim) {
					String.prototype.trim = function() {
						return this.replace(/^\s+|\s+$/g, '');
					};
				}
				if (startTime.trim().length == 10) {
					startTime = startTime + " 00:00:00";
				}
				if (endTime.trim().length == 10) {
					endTime = endTime + " 00:00:00";
				}
				var reg = new RegExp('-', 'g');
				startTime = startTime.replace(reg, '/');// 正则替换
				endTime = endTime.replace(reg, '/');
				startTime = new Date(parseInt(Date.parse(startTime), 10));
				endTime = new Date(parseInt(Date.parse(endTime), 10));
				if (startTime > endTime) {
					return false;
				} else {
					return true;
				}
			}, "<font color='#E47068'>截至时间不能早于起始时间</font>");
			$("#searchForm").validate({
				rules : {
					endTime : {
						compareTime : true
					},
					gdsId : {
						digits : true
					}
				},
				messages : {
					endTime : {
						compareTime : "<span style='color:red'>截至时间不能早于起始时间</span>"
					},
					gdsId : {
						digits : "<span style='color:red'>只能输入整数</span>"
					}
				},
				//	          debug: false,  //如果修改为true则表单不会提交
				submitHandler : function() {
				}
			});
		},
		commitForVerify : function(params){
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsmanage/commitforverify",
				data : params,
				success : function(returnInfo) {
					if(returnInfo.resultFlag=='ok'){
						eDialog.success(returnInfo.resultMsg, {
							buttons : [{
								caption : "确定",
								callback : function() {
									window.location.href = GLOBAL.WEBROOT
											+ '/gdsmanage?shopId='
											+ $("#shopId").val();
								}
							}]
						});
					}else{
						eDialog.error(returnInfo.resultMsg);
					}
				}
			});
		},
		batchCommitForVerify : function(operateType){
			var ids = $('#dataGridTable').getCheckIds();
			if(!ids || ids.length==0){
				eDialog.alert('请选择至少选择一条商品记录进行操作！');
				return ;
			}else if(ids[0]==undefined){
				eDialog.alert('请选择至少选择一条商品记录进行操作！');
				return ;
			}
			var params = {
					operateId : ids.join(","),
					operateType : operateType,
					shopId : $("#shopId").val()
			};
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsmanage/batchcommitforverify",
				data : params,
				success : function(returnInfo) {
					if(returnInfo.resultFlag=='ok'){
						eDialog.success(returnInfo.resultMsg);
					}else{
						eDialog.error(returnInfo.resultMsg);
					}
				}
			});
		},
		showVeriyRecord : function(shopId,gdsId){
			bDialog.open({
				title : '审核记录',
				width : 860,
				height : 500,
				params : {
					'gdsId': gdsId,
					'shopId':shopId
				},
				url : GLOBAL.WEBROOT + '/gdsmanage/gdsverifyrecord',
				callback:function(data){}
			});
		},
		whetherCanOperate : function(params){
			$.eAjax({
				url : GLOBAL.WEBROOT + "/gdsmanage/whethercanoperate",
				data : params,
				success : function(returnInfo) {
					if(returnInfo.resultFlag=='ok'){
						//表示可以进行操作
						window.location.href= GLOBAL.WEBROOT +"/gdsinfoentry/togdsedit?gdsId="+params.gdsId;
					}else{
						//不可进行操作
						eDialog.error(returnInfo.resultMsg);
					}
				}
			});
		}

};
