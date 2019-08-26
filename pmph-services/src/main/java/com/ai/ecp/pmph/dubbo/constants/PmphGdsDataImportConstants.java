package com.ai.ecp.pmph.dubbo.constants;

/**
 * Project Name:ecp-services-goods <br>
 * Description: 数据割接常量类<br>
 * Date:2015年10月26日下午8:06:18 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public final class PmphGdsDataImportConstants {
    /**
     * 隐藏实现，防止初始化
     */
    private PmphGdsDataImportConstants() {
    }
    /**
     * 
     * Project Name:ecp-services-goods <br>
     * Description: <br>
     * Date:2015年8月11日上午10:29:49 <br>
     * 
     * @version
     * @since JDK 1.6
     */
    public static class Commons {


        /**
         * ERP数据来源标志
         */
        public final static String ORIGIN_ERP = "08";

        /**
         * 泽元数据来源标志
         */
        public final static String ORIGIN_ZEYUN = "09";

        /**
         * 人卫目录编码
         */
        public final static Long CATLOG_RENWEI = 1L;

        /**
         * 积分目录编码
         */
        public final static Long CATLOG_JIFEN = 2L;

        /**
         * 默认创建/更新工号编码
         */
        public final static Long STAFF_ID = 1111L;

        /**
         * 默认店铺编码
         */
        public final static Long SHOP_ID = 100L;

        /**
         * 默认Company编码
         */
        public final static Long COMPANY_ID = 1L;

        /**
         * 纸质书其它分类编码
         */
        public final static String CATEGORY_BOOK_OTHER_ID = "1198";

        /**
         * 电子书其它分类编码
         */
        public final static String CATEGORY_EBOOK_OTHER_ID = "1203";
        /**
         * 电子书默认分类编码
         */
        public final static String CATEGORY_EBOOK_ID = "1200";

        /**
         * 数字教材其它分类编码
         */
        public final static String CATEGORY_DBOOK_OTHER_ID = "1204";
        /**
         * 数字教材默认分类编码
         */
        public final static String CATEGORY_DBOOK_ID = "1201";
        
        /**
         * 考试其它分类编码
         */
        public final static String CATEGORY_EXAM_OTHER_ID = "1205";
     
        /**
         * 辅导班默认分类编码。
         */
        public static final String TUTORIUM_CLASS_CATGCODE = "1401";
        
        /**
         * 数字图书馆－〉虚拟卡分类编码
         */
        public static final String VIRTUAL_CARD_CATGCODE = "1207";
        
        /**
         * 辅导班其它分类编码
         */
        public final static String CATEGORY_EXAM_TUTORIALCLASS_OTHER_ID = "1501";
        
        /**
         * 试卷默认分类编码。
         */
        public static final String TEST_PAPER_CATGCODE = "1402";
        
        /**
         * 试卷其它分类编码
         */
        public final static String CATEGORY_EXAM_PAPER_OTHER_ID = "1502";

        /**
         * 实物商品主键
         */
        public final static Long GOODTYPE_ORDINARY_ID = 1L;
        
        /**
         * 实物商品编码
         */
        public final static String GOODTYPE_ORDINARY_CODE = "OrdinaryProduct";

        /**
         * 虚拟商品主键
         */
        public final static Long GOODTYPE_VIRTUAL_ID = 2L;

        /**
         * 虚拟商品编码
         */
        public final static String GOODTYPE_VIRTUAL_CODE = "VirtualProduct";
        
        /**
         * 虚拟卡商品主键
         */
        public final static Long GOODTYPE_VIRTUAL_CARD_ID = 3L;
        
        
        /**
         * 泽元考试网原始编码前缀。
         */
        public static final String ZY_EXAM_ORIGN_CODE_PREFIX = "ZYKS-";
        
        /**
         * 试卷属性值
         */
        public static final String ZY_EXAM_PROP_PAPER = "1";
        
        /**
         * 试卷包属性值
         */
        public static final String ZY_EXAM_PROP_PAPERBAG = "2";
        
        /**
         * 辅导班属性值
         */
        public static final String ZY_EXAM_PROP_TUTORIALCLASS = "3";
        
        /**
         * 人卫出版社名称
         */
        public static final String RENWEI_NAME="人民卫生出版社";
        
        /**
         * 错误信息KEY
         */
        public static final String KEY_FAIL_MESSAGE="failuremessage";
    }
    
    public static class CodePreFix {
        
        /**
         * 泽元电子书、数字教材原始商品编码前缀。
         */
        public static final String ZY_EDBOOK_ORIGN_CODE_PREFIX = "EDBOOK-";
        
        /**
         * 泽元考试网试卷原始商品编码前缀。
         */
        public static final String ZY_EXAM_PAPER_ORIGN_CODE_PREFIX = "EXAMPAPER-";
        
        /**
         * 泽元考试网试卷包原始商品编码前缀。
         */
        public static final String ZY_EXAM_PAPERBAG_ORIGN_CODE_PREFIX = "EXAMPAPERBAG-";
        
        /**
         * 泽元考试网辅导班原始商品编码前缀。
         */
        public static final String ZY_EXAM_TUTORIALCLASS_ORIGN_CODE_PREFIX = "EXAMTUTORIALCLASS-";
        
    }

    /**
	 * 
	 * Project Name:ecp-services-goods-server <br>
	 * Description: 来源系统<br>
	 * Date:2016-2-25上午11:49:14  <br>
	 * 
	 * @version DataImportConstants 
	 * @since JDK 1.6
	 */
	public static class SrcSystem{
		/**
		 * ERP纸质书.
		 */
		public static final String ERP_01 = "ERP-01";
		/**
		 * 泽元-电子书数字教材
		 */
		public static final String ZY_01 = "ZY-01";
		/**
		 * 泽元-考试网
		 */
		public static final String ZY_02 = "ZY-02";
	}
}
