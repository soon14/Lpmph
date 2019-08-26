package com.ai.ecp.pmph.facade.interfaces.eventual;

/**
 * 
 * Project Name:pmph-services-server <br>
 * Description: 启动人卫e教本版编号通知的主事务<br>
 * Date:2017年8月25日上午9:04:01  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public interface IEEduVNNoticeMainSV {

    /**
     * 启动人卫e教本版编号通知的主事务；versionNumbers 以半角,隔开
     */
    public void  startAsyncEEduVNNoticMain(String versionNumbers);
}

