package com.ai.ecp.pmph.busi.seller.util;

public class OrdConstant {

    public static final int ORDER_SESSION_TIME = 1800;
    public static final String ORDER_SESSION_KEY_PAY_REFUND = "ORD_PAY_REFUND";
    
	 public static class PaytStatus
     {
 	  
       public static String PAY_ONLINE = "0";
       public static String PAY_ONLINE_TEXT = "线上支付";
       
       public static String PAY_OFFLINE = "1";
       public static String PAY_OFFLINE_TEXT = "线下支付";
       
       public static String PAY_BYOWN = "2";
       public static String PAY_BYOWN_TEXT = "上门支付";
     }


    public static class ChkOrdStatus
    {
        public static String SEND = "02";

        public static String CHECK = "05";
    }

    public static class InvoiceType
    {
        public static String COMM = "0";
        public static String TAX = "1";
        public static String NO = "2";
    }
}
