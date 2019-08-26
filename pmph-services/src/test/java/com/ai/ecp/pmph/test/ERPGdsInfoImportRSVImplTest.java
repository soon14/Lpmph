package com.ai.ecp.pmph.test;

import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IERPGdsInfoImportRSV;
import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IERPGdsStockInfoImportRSV;
import com.ai.ecp.pmph.service.busi.impl.dataimport.ERPGdsInfoImportSVImpl;
import com.ai.ecp.server.test.EcpServicesTest;
import com.ai.paas.utils.LogUtil;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ERPGdsInfoImportRSVImplTest extends EcpServicesTest {

    @Resource
    private IERPGdsInfoImportRSV erpGdsInfoImportRSV;

    @Resource
    private IERPGdsStockInfoImportRSV erpGdsStockInfoImportRSV;

    /**
     * 导入库存
     * 
     * @since JDK 1.6
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testSaveGdsStockInfo() {
        //20 1553  21 37
        //
        for (int i = 1; i <= 1553; i++) {
            Map map = new HashMap();
            map.put("benbanbianhao", "20160320" + i);
            Long l = (long) (100 + i);
            map.put("kucunceshu", l);
            this.erpGdsStockInfoImportRSV.saveGdsStockInfo(map);
        }
    }

    /**
     * 多线程导入商品
     * 
     * @since JDK 1.6
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testSaveGdsInfo() {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(600);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(20, 20, 1, TimeUnit.HOURS, queue,new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 1; i <= 1; i++) {
            Map data = initData(i);
            Task task = new Task(erpGdsInfoImportRSV, data);
            pool.execute(task);
        }
        LogUtil.info("", "shut down");
        try {
            pool.awaitTermination(10,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
//            LogUtil.error("", "error",e);
        }
    }

    class Task extends Thread {
        private IERPGdsInfoImportRSV erpGdsInfoImportRSV;

        private Map data;

        public Task(IERPGdsInfoImportRSV erpGdsInfoImportRSV, Map data) {
            this.erpGdsInfoImportRSV = erpGdsInfoImportRSV;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                erpGdsInfoImportRSV.receive(data);
            } catch (Exception e) {
//                LogUtil.error("", "error",e);
            }
        };
    }

    private Map initData(int i) {
        Map map = new HashMap();
        map.put("leixingbianhao", "2011000016");// 类型编号
        map.put("zhipinleixing", "图书");// 制品类型//“图书”、“音像”等
        map.put("shoubanbianhao", "0000Z28613");// 首版编号---------------------------------
        map.put("benbanbianhao", "20160815-" + i);// 本版编号//图书的唯一id--------------------------------
        map.put("zhipinmingcheng", getRamdonHan(7)+"-20160524---\\" + i);// 书名
        map.put("congshumingcheng", "");// 丛书名
        map.put("zuozhemingcheng", "刘权章主编");// 作者名
        map.put("zhuyifangshi", "");// 著译方式
        map.put("zhipinshuhao", "02175");// 书号
        map.put("zhipinbanci", "1");// 版次
        map.put("zhipindingjia", "" + i);// 定价,以元为单位-----------------------------------
        map.put("yuanshumingcheng", "");// 原书名
        map.put("yuanshuchubandanwei", "");// 原书出版单位
        map.put("yuanshuchubanxinxi", "");// 原书版型
        map.put("yuanshuzuozhe", "");// 原书作者
        map.put("yeshu", "");// 页数
        map.put("zishu", "");// 字数
        map.put("peipanshu", "");// 盘数
        map.put("kaiben", "16");// 开本
        map.put("yinzhang", "");// 印张
        map.put("zhuangzheng", "精装");// 装帧
        map.put("zaitixingshi", "");// 载体
        map.put("yuzhongcongshu", "");// 语种丛书
        map.put("wenzhong", "");// 文种
        map.put("wenzi", "");// 文字
        map.put("biaozhunshuhao", "ISBN 7-117-02175-6/R·2176");// 标准书号
        map.put("tiaoxingmahao", "txm-9787117021753");// 条码号
        map.put("duzheduixiang", "");// 读者对象
        map.put("cehuabumen", "第四编辑部");// 策划部门
        map.put("cehuabianji", "");// 策划编辑
        map.put("zhipinyijifenleibianhao", "");// 一级分类编号
        map.put("zhipinyijifenlei", "");// 一级分类
        map.put("zhipinerjifenleibianhao", "");// 二级分类编号
        map.put("zhipinerjifenlei", "");// 二级分类
        map.put("zhipinsanjifenleibianhao", "");// 三级分类编号
        map.put("zhipinsanjifenlei", "");// 三级分类
        map.put("zhipinsijifenleibianhao", "2015001723");// 四级分类编号
        map.put("zhipinsijifenlei", "");// 四级分类
        map.put("zhipinteshufenlei", "");// 特殊分类
        map.put("neirongtiyao", "");// 内容提要
        map.put("peitaojiaocai", "");// 配套教材
        map.put("peitaojiaofu", "");// 配套教辅
        map.put("chubanqingkuang", "停印");// 出版情况
        map.put("shoushuriqi", "1995/8/28");// 收书日期
        map.put("huojiangxinxi", "");// 获奖情况
        map.put("liulanwangzhi", "");// 网址
        map.put("zuozheziliao", "");// 作者资料
        map.put("kucunqingkuang", "无");// 库存情况
        map.put("bianjibumen", "");// 编辑部门
        map.put("zerenbianji", "");// 责任编辑
        map.put("querenriqi", "2015/4/17  16:16:37");// 确认日期
        map.put("chexiaoriqi", "");// 撤销日期
        map.put("xuanchuanyu", "");// 宣传语
        map.put("xinshuyugao", "");// 新书预告
        map.put("shifouyincang", "");// 是否隐藏
        map.put("wangzhanbeizhu", "新版图书");// 网站备注
        map.put("dianzishudizhi", "");// 电子书地址
        map.put("chehuibeizhu", "");// 撤回备注
        map.put("faburiqi", "2015/4/17  16:16:37");// 发布日期//图书信息发布的日期，作为有效标志
        map.put(ERPGdsInfoImportSVImpl.KEY_SHANGJIABIAOZHI, "0");// 上架标志//正常[0]
                                                                 // 下架[1]---------------------------
        map.put("xuekefenleihao", "");// 学科分类号
        map.put("zhongtufenleihao", "Q");// 中图分类号
        map.put("bofangshijian", "");// 播放时间
        map.put("xiaoshoubeizhu", "");// 销售备注
        map.put("kucunbeizhu", "");// 库存备注
        map.put("qitabeizhu", "");// 其他备注
        map.put("yuanshuchubandi", "");// 原书出版地
        map.put(ERPGdsInfoImportSVImpl.KEY_XINXICAOZUO, "2");// 操作模式//增加[1]删除[2]修改[3]---------------------------
        map.put("caozuoriqi", "2015/9/9  22:47:24");// 操作日期
        return map;
    }
    
    private static Random ran = new Random();
    private final static int delta = 0x9fa5 - 0x4e00 + 1;
      
    public static char getRandomHan() {
        return (char)(0x4e00 + ran.nextInt(delta)); 
    }
    
    public static String getRamdonHan(int size){
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(getRandomHan());
        }
        return sb.toString();
    }

}
