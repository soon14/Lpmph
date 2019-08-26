package com.ai.ecp.pmph.dubbo.util;

public class PmphConstants {
    /**
     * 
     * Project Name:pmph-services-server <br>
     * Description: <br>
     * Date:2016年8月15日下午5:36:29  <br>
     * 
     * @version PmphConstants 
     * @since JDK 1.6
     */
    public static class GdsType{
        /**
         * 商品类型虚拟卡
         */
        public static final Long GDSTYPE_VIRTUAL_CARD = 3L;
        /**
         * 是否是虚拟商品.
         */
        public static final Long GDSTYPE_VIRTUAL_PRODUCT = 2L;
    }

    public static class Score
    {
 
        /**
         * 积分回滚
         */
        public static final String SCORE_ROLLBACK_TYPE = "9999";//积分回滚
        
        /**
         * 订单退款返还积分
         */
        public static final String SCORE_ORDER_REFUND_TYPE = "9998";//订单退款返还积分
        
        /**
         * 订单退货返还积分
         */
        public static final String SCORE_ORDER_BACK_TYPE = "9997";//订单退货返还积分
        
        /**
         * 该条记录已回退
         */
        public static final String SCORE_BACK_FLAG_1 = "1";//该条记录已回退
        
        /**
         * 该条记录未回退
         */
        public static final String SCORE_BACK_FLAG_0 = "0";//该条记录未回退
        
        /**
         * 该条记录为回退记录
         */
        public static final String SCORE_BACK_FLAG_2 = "2";//该条记录为回退记录
        /*
         * 积分来源参数缓存中的key
         */
        public static final String SCORE_ACTION_CACHE_KEY= "SCORE_ACTION_CACHE_KEY";
        public static final String SCORE_TYPE_CACHE_KEY = "SCORE_TYPE_CACHE_KEY";
        public static final String SCORE_FUN_CACHE_KEY = "SCORE_FUN_CACHE_KEY";
        public static final String SCORE_PARA_CACHE_KEY = "SCORE_PARA_CACHE_KEY";
        public static final String SCORE_CACLFUN_CACHE_KEY = "SCORE_CACLFUN_CACHE_KEY";


        /**
         * 积分有效期时间
         */
        public static final int SCORE_ACTIVE_DAY = 365;
        
        /**
         * 积分账户正常状态
         */
        public static final String SCORE_STATUS_NORMAL = "1";
        /**
         * 积分账户无效
         */
        public static final String SCORE_STATUS_INVALID = "0";
        /**
         * 积分账户冻结
         */
        public static final String SCORE_STATUS_FROZEN = "2";
        
        /**
         * 积分函数状态有效
         */
        public static final String SCORE_FUNC_STATUS_ACTIVE = "1";
        /**
         * 积分函数状态无效
         */
        public static final String SCORE_FUNC_STATUS_INVALID = "0";
        
        /**
         * 积分操作类型：收入
         */
        public static final String SCORE_OPT_TYPE_1 = "1";
        
        /**
         * 积分操作类型：支出 
         */
        public static final String SCORE_OPT_TYPE_2 = "2";
        
        /**
         * 积分操作类型：解冻
         */
        public static final String SCORE_OPT_TYPE_3 = "3";
        /**
         * 积分操作类型：冻结
         */
        public static final String SCORE_OPT_TYPE_4 = "4";
        
        /**
         * 积分操作类型：后台调增
         */
        public static final String SCORE_OPT_TYPE_5 = "5";
        
        /**
         * 积分操作类型：后台调减
         */
        public static final String SCORE_OPT_TYPE_6 = "6";
        
        /**
         * 积分调整类型：客户维系
         */
        public static final String SCORE_ADJUST_TYPE_9001 = "9001";
        
        /**
         * 积分调整类型：系统差错
         */
        public static final String SCORE_ADJUST_TYPE_9002 = "9002";
        
        /**
         * 积分操作类型：积分补偿（订单异常回滚、取消订单、退款、退货等）
         */
        public static final String SCORE_OPT_TYPE_7 = "7";
        
        /**
         * 积分操作类型：积分扣减（取消订单、退款、退货等）
         */
        public static final String SCORE_OPT_TYPE_8 = "8";
        
        /**
         * 您的积分不足
         */
        public static final String SCORE_NOT_ENOUGH = "staff.100026";
        
        public static final String USER_SCORE_STATUS_ERROR = "staff.100030";
        
        /**
         * 购物车校验时：您的积分账户处于异常状态
         */
        public static final String SCORE_STATUS_ERROR = "staff.100043";
        
        /**
         * 积分消费扣减方式：1、普通
         */
        public static final String SCORE_EXCHANGE_MODE_1 = "1";
        
        /**
         * 积分消费扣减方式：2、退款，退货
         */
        public static final String SCORE_EXCHANGE_MODE_2 = "2";
        
        /**
         * 积分消费扣减方式：3、后台调减
         */
        public static final String SCORE_EXCHANGE_MODE_3 = "3";
        
        /**
         * 积分最多扣为0，不能扣为负
         */
        public static final String SUBTRACT_TO_ZERO = "0";
        
        /**
         * 积分可扣为负数
         */
        public static final String SUBTRACT_BELOW_ZERO = "1";
        
        /**
         * 通过ISBN号查询不到商品
         */
        public static final String ISBN_NOT_FOUNT = "staff.100057";
        

        /**
         * 书码已经赠送过积分
         */
		public static final String BOOK_CODE_EXIST_FOR_SCORE = "staff.100058";
		
		/**
		 * 通过本版编号找不到商品
		 */
		public static final String BBCODE_NOT_FOUNT = "staff.100060";
        
    }
    
    public static class Card{
        
        /**
         * 会员卡状态redis Key
         */
        public static final String CARD_STATUS_KEY="STAFF_CARD_STATUS";
        
        /**
         * 待审核
         */
        public static final String CHECK_STATUS_0 = "0";
        
        /**
         * 审核通过
         */
        public static final String CHECK_STATUS_1 = "1";
        
        /**
         * 审核不通过
         */
        public static final String CHECK_STATUS_2 = "2";
        
        /**
         * 会员卡状态:01未绑定
         */
        public static final String CUST_CARD_NO_SEND = "01";
        
        /**
         * 会员卡绑定方式:0 线下发卡
         */
        public static final String BIND_TYPE_0 = "0";
        
        /**
         * 会员卡绑定方式:1 线上申请
         */
        public static final String BIND_TYPE_1 = "1";
        
        /**
         * 会员卡状态:02已绑定
         */
        public static final String CUST_CARD_SEND = "02";
        
        /**
         * 生成会员卡号异常编码
         */
        public static final String BULID_CARD_ID_ERROR = "staff.100038";
        
        /**
         * 会员卡已经被绑定异常
         */
        public static final String CARD_IS_BINDED_EVER = "staff.100039";
        
        /**
         * 会员有存在已申请的会员卡记录
         */
        public static final String CARD_EXIST_APPLY_RECORD = "staff.100040";
        
        /**
         * 该会员卡号不存在
         */
        public static final String CARD_BIND_NOEXIST_ERROR = "staff.100041";
        
        /**
         * 审核时，用户会员等级与所申请等级不一致
         */
        public static final String CUST_LEVEL_CHANGE = "staff.100044";

        /**
         * 该会员卡等级低于会员自身的等级
         */
        public static final String CARD_LEVEL_LOW_ERROR = "staff.100045";

        /**
         * 该会员卡已有发卡记录
         */
        public static final String CARD_DISTRIBUTE_EXITS = "staff.100051";

    }
    
    /**
     * 天猫订单()
     */
    public static final String TMALL_ORDER_TYPE = "04"; 
    
    /**
     * 来源类型（一书一码激活）
     */
    public static final String ISBN_ACTIVE_TYPE = "06";
    
    public static class OrderTwoStatus {

        // 订单二级状态--- 提交订单
        public static String STATUS_SUBMIT = "0100";

        // 订单二级状态--- 线上支付完成
        public static String STATUS_PAID_ONLINE = "0200";

        // 订单二级状态--- 线下支付申请
        public static String STATUS_PAID_OFFLINE_APPLY = "0201";

        // 订单二级状态--- 线下支付审核通过
        public static String STATUS_PAID_OFFLINE_VERIFY_PASS = "0202";

        // 订单二级状态--- 线下支付审核不通过
        public static String STATUS_PAID_OFFLINE_VERIFY_NOT = "9902";

        // 订单二级状态--- 部分发货
        public static String STATUS_SEND_SECTION = "0400";

        // 订单二级状态--- 全部发货
        public static String STATUS_SEND_ALL = "0500";

        // 订单二级状态--- 买家收货
        public static String STATUS_RECEPT_BUYER = "0600";

        // 订单二级状态--- 自动收货
        public static String STATUS_RECEPT_SYSTEM = "0601";
        
        // 订单二级状态--- 退货流程结束
        public static String STATUS_BACKGDS_NO ="0700";
        
        // 订单二级状态--- 退货流程中
        public static String STATUS_BACKGDS_YES ="0701";
        
        // 订单二级状态--- 退款流程结束
        public static String STATUS_REFUND_NO ="0800";
        
        // 订单二级状态--- 退款流程中
        public static String STATUS_REFUND_YES ="0801";

        // 订单二级状态--- 关闭
        public static String STATUS_CLOSE = "8000";

        // 订单二级状态--- 买家取消
        public static String STATUS_CANCLE_BUYER = "9901";

        // 订单二级状态--- 系统取消
        public static String STATUS_CANCLE_SYSTEM = "9902";
        
        // 订单二级状态--- 全部退货取消
        public static String STATUS_CANCLE_BACKGDS = "9903";
        
        // 订单二级状态--- 退款取消
        public static String STATUS_CANCLE_REFUND = "9904";
        
        // 订单二级状态--- 补偿性退款取消
        public static String STATUS_CANCLE_CREFUND = "9905";

    }
    
}

