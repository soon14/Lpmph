/**
 * Created by wang on 16/1/13.
 */
$(function(){
    orderSum();

    $("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        'pCheckColumn' : false,
        'pCheckRow' : false,
        'pIdColumn' : 'orderId',
        'scrollX' :true,
        'pLengthMenu' : [25,10,50,100],
        "sAjaxSource": GLOBAL.WEBROOT + '/ordersub/goodsalelist',
        'params':ebcForm.formParams($("#searchForm")),
        //指定列数据位置
        "aoColumns": [
            //订单编号、商品名称、ISBN号、书号、产品一级分类、产品二级分类、购买单价、购买数量、购买总金额、是否活动商品、购买日期、会员名、联系人、联系电话、联系地址
            { "mData": "orderId", "sTitle":"订单编号","bSortable":false, "sClass": "center", "mRender": function(data,type,row){
                return '<a href="javascript:void(0)" onclick="oUtil.getOrdDetail(\''+data+'\')">'+data+'</a>';
            }},
            { "mData": "gdsName", "sTitle":"商品名称","bSortable":false, "sClass": "center"},
            //{ "mData": "isbn", "sTitle":"ISBN号","bSortable":false, "sClass": "center"},
            { "mData": "zsCode", "sTitle":"书号", "bSortable":false, "sClass": "center"},
            { "mData": "category01", "sTitle":"一级分类","bSortable":false, "sClass": "center"},
            { "mData": "category02", "sTitle":"二级分类","bSortable":false, "sClass": "center"},
            { "mData": "category03", "sTitle":"三级分类","bSortable":false, "sClass": "center"},
            { "mData": "category04", "sTitle":"四级分类","bSortable":false, "sClass": "center"},
            { "mData": "discountPrice", "sTitle":"购买单价","bSortable":false,  "sClass": "center","mRender": function(data,type,row){
                var money = data == null? 0:data;
                return ebcUtils.numFormat(accDiv(money,100),2);
            }},
            { "mData": "orderAmount", "sTitle":"购买数量", "bSortable":false, "sClass": "center"},
            { "mData": "realMoney", "sTitle":"购买总金额","bSortable":false, "sClass": "center","mRender": function(data,type,row){
                var money = data == null? 0:data;
                return ebcUtils.numFormat(accDiv(money,100),2);
            }},
            { "mData": "payType", "sTitle":"支付方式", "sClass":"center", "bSortable":false,  "mRender": function(data,type,row){
				var val = oUtil.constant.pay[data];
				if(val == null){
					val = '';
				}
				return val;
			}},
            { "mData": "isProm", "sTitle":"活动商品","bSortable":false,  "sClass": "center","mRender": function(data,type,row){
                if(data){
                    return "是";
                }
                return "否";
            }},
            { "mData": "orderTime", "sTitle":"购买日期","bSortable":false, "sClass": "center","mRender": function(data,type,row){
                return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
            }},
            { "mData": "staffCode", "sTitle":"会员名","bSortable":false, "sClass": "center"},
            { "mData": "contactName", "sTitle":"联系人","bSortable":false, "sClass": "center"},
            { "mData": "contactPhone", "sTitle":"联系电话","bSortable":false, "sClass": "center"},
            { "mData": "chnlAddress", "sTitle":"联系地址","bSortable":false, "sClass": "center"}
        ]
    });

    var modifyBiz = function(){
        var ids = $('#dataGridTable').getCheckIds();
        if(ids && ids.length==1){
            window.location.href = GLOBAL.WEBROOT+'/demo/edit';
        }else if(ids && ids.length>1){
            eDialog.alert('只能选择一个项目进行操作！');
        }else if(!ids || ids.length==0){
            eDialog.alert('请选择至少选择一个项目进行操作！');
        }
    };

    $('#btnFormSearch').click(function(){
        if(!$("#searchForm").valid()) return false;
        var isbn = $.trim($("#isbn").val());
        if(isbn != ""){
        	if(isbn.length != 5){
        		eDialog.alert('请输入5位ISBN号！');
        		return false;
        	} 
        }
        var p = ebcForm.formParams($("#searchForm"));
        orderSum();
        $('#dataGridTable').gridSearch(p);
    });

    $('#btnFormReset').click(function(){
        ebcForm.resetForm('#searchForm');
        $('#endDate').val(ebcDate.dateCalc(new Date(),0));
        $('#begDate').val(ebcDate.yearCalc(new Date(),-1));
        $('#btnFormSearch').trigger('click');
    });

    $('#btn_code_add').click(function(){
        window.location.href = GLOBAL.WEBROOT+'/demo/edit';
    });

    $('#btn_code_modify').click(function(){
        modifyBiz();
    });

    $("#btn_code_more").on("click",function(){
        window.location.href = GLOBAL.WEBROOT+'/demo/more';
    });


    $("#btnFormExport").on("click",function(){
        $('#btnFormSearch').trigger('click');

        if(!$("#searchForm").valid()) return false;
        var isbn = $.trim($("#isbn").val());
        if(isbn != ""){
        	if(isbn.length != 5){
        		eDialog.alert('请输入5位ISBN号！');
        		return false;
        	} 
        }
        eDialog.confirm("导出销售情况" , {
            buttons: [{
                'caption': '导出',
                'callback': function () {
                    var p = ebcForm.formParams($('#searchForm'));
                    //导出限制
                    p.push({name:'pageNo',value:1});
                    p.push({name:'pageSize',value:10000});
                    //导出限制

                    $('#exportType').val('getSaleFileId');
                    $('#exportInfo').val(JSON.stringify(p));
                    $("#exportForm").submit();

                }
            }, {
                'caption': '取消',
                'callback': function () {

                }
            }]
        });
    });

    function orderSum(){
        $.eAjax({
            url : GLOBAL.WEBROOT + '/ordersub/salesum',
            data :ebcForm.formParams($("#searchForm")),
            async : true,
            type : "post",
            dataType : "json",
            success : function(datas) {
//				console.info(datas);
                $("#basicMoney").text(ebcUtils.numFormat(accDiv(datas.basicMoney,100),2));
                //和列表里的数据同名
                $("#realMoney").text(ebcUtils.numFormat(accDiv(datas.realMoney,100),2));
                $("#orderNum").text(datas.orderNum);
                $("#saleNum").text(datas.saleNum);
                //if(datas.orderCount == 0){
                    //$("#payedRate").text("0.00");
                //} else {
                    //$("#payedRate").text(ebcUtils.numFormat(accMul(accDiv(datas.payedCount,datas.orderCount),100),2));
                //}
            }
        });
    }

});