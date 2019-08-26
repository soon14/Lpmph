package com.ai.ecp.pmph.test;

import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IZYGdsInfoImportRSV;
import com.ai.ecp.server.test.EcpServicesTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class ZYGdsInfoImportRSVImplTest extends EcpServicesTest {

    @Resource
    private IZYGdsInfoImportRSV zyGdsInfoImportRSV;

    @Test
    public void test(){
        System.out.println("----------------");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testSaveGdsInfo() {

        for(int ii=1;ii<=10;ii++) {

            Map map = new HashMap();

            map.put("tpGoodsId", "ceshi34534ceshi"+ii);// 数字教材/电子书 的商品ID
            map.put("tpGoodsName", "测试5位ISBN号---"+ii);// 商品名称
            map.put("tpPrice", "444");// 商城价格，以元为单位，折扣前的原价
            map.put("tpType", "1");// 商品分类类型//1=电子书，2=数字教材
            map.put("tpGoodTypeId", "1");// 商品分类ID
            // map.put("tpGoodTYpeName", "分类1");//商品分类名称
            map.put("tpISBN", "978-7-117-17214-1");// 标准书号(ISBN)
            map.put("tpEISBN", "978-7-117-17214-1");// 原版纸质书ISBN
            map.put("tpAuthorName", "作者1");// 作者
            map.put("tpPublishDate", "2015-09-30 15:10:02");// 出版日期，格式yyyy-MM-dd hh:mm:ss，如：2015-09-30
            // 15:10:02
            map.put("tpEBookSize", "1");// 文件大小
            map.put("tpIsAcademic", "1");// 是否提供试读，0:不提供试读,1:提供试读
            map.put("tpEBookIntro", "图书简介，HTML格式串");// 图书简介，HTML格式串
            // map.put("tpUpTime", "2015-09-30 15:10:02");//上架时间

            this.zyGdsInfoImportRSV.receive(map);
//        this.zyGdsInfoImportRSV.offShelves(map);
        }
    }

}
