package com.ai.ecp.pmph.dubbo.constants;

/**
 * Project Name:ecp-services-goods <br>
 * Description: <br>
 * Date:2015年10月30日上午9:41:28  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public final class PmphGdsDataImportErrorConstants {
    
    /**
     * 隐藏实现，防止初始化
     */
    private PmphGdsDataImportErrorConstants() {
    }
    public static class Commons {

        /**
         * 操作模式为空
         */
        public static final String ERROR_PMPH_GOODS_DATAIMPORT_1 = "error.pmph.goods.dataimport.1";
        
        /**
         * 操作模式不在范围之内
         */
        public static final String ERROR_PMPH_GOODS_DATAIMPORT_2 = "error.pmph.goods.dataimport.2";
        
    }
    
    public static class ERP {
        
        /**
         * 商品未设置分类信息（一二三四级分类）
         */
        public static final String ERROR_GOODS_DATAIMPORT_ERP_1 = "error.goods.dataimport.erp.1";
        
    }
    
    public static class ZY {
        
        /**
         * 商品分类ID值为空
         */
        public static final String ERROR_GOODS_DATAIMPORT_ZY_2 = "error.goods.dataimport.zy.2";
        
        /**
         * 商品类型值为空
         */
        public static final String ERROR_GOODS_DATAIMPORT_ZY_4 = "error.goods.dataimport.zy.4";
        
        /**
         * 商品分类类型不合法
         */
        public static final String ERROR_GOODS_DATAIMPORT_ZY_5 = "error.goods.dataimport.zy.5";
        
        
    }
    /**
     * 
     * Project Name:ecp-services-goods-server <br>
     * Description:图片样章导入异常 <br>
     * Date:2015-11-13上午4:27:56  <br>
     * 
     * @version PmphGdsDataImportErrorConstants
     * @since JDK 1.6
     */
    public static class ImageSampleChapterImportError{
    	/**
    	 * 图片或者样章{0}匹配不到商品!
    	 */
    	public static final String ERROR_GOODS_IMAGESAMPLECHAPTERIMPORT = "error.goods.imagesamplechapterimport";		
    }

}

