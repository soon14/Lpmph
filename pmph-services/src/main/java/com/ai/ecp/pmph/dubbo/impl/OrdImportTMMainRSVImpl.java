package com.ai.ecp.pmph.dubbo.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmImportLogResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdImportTMMainRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportLogSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.impl.datainout.DataImportHandler;
import com.ai.ecp.server.util.DataInoutUtil;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.IStaffUnionRSV;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;

/**
 * Project Name:ecp-services-order <br>
 * Description: <br>
 * Date:2015年8月12日下午8:22:28 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class OrdImportTMMainRSVImpl implements IOrdImportTMMainRSV {

    @Resource
    private ICustManageRSV custManageRSV;

    @Resource
    private IOrdImportLogSV ordImportLogSV;

    @Resource
    private IOrdImportSV ordImportSV;

    @Resource
    private IStaffUnionRSV staffUnionRSV;

    private static final String MODULE = OrdImportTMMainRSVImpl.class.getName();

    @Override
    public List<ROrdTmImportLogResp> importTMMain(final RFileImportRequest info)
            throws BusinessException {
        LogUtil.info(MODULE, "天猫主订单导入dubbo服务开始导入");
        try {
            InputStream inputStream = new ByteArrayInputStream(FileUtil.readFile(info.getFileId()));
            DataInoutUtil.importExcel(
                    new DataImportHandler(inputStream, FileUtil.getFileName(info.getFileId()),
                            "xlsx", info.getModuleName(), info.getStaffId().toString()) {
                        @Override
                        public boolean doCallback(List<List<Object>> list, String s) {
                            if (list.size() > 10000) {
                                LogUtil.error(MODULE, "===天猫订单导入不能超过6000===");
                                throw new BusinessException(
                                        MsgConstants.ServiceMsgCode.ORD_SERVER_310055);
                            }

                            if (CollectionUtils.isEmpty(list)) {
                                return true;
                            }
                            if (list.get(0).size() != 40) {
                                throw new BusinessException(
                                        MsgConstants.ServiceMsgCode.ORD_SERVER_310056);
                            }
                            List<List<Object>> newlist = list.subList(1, list.size());
                            if(CollectionUtils.isEmpty(newlist)){
                                return true;
                            }
                            
                            ThreadPoolExecutor executor = new ThreadPoolExecutor(8,16,1,TimeUnit.MILLISECONDS,
                                    new ArrayBlockingQueue<Runnable>(64));
                            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                            
                            int groupNum = 8;
                            int group = newlist.size() / groupNum + 1;
                            int num = newlist.size()%groupNum;
                            List<Future> fus = new ArrayList<Future>();
                            for (int i = 0; i < group; i++) {
                                List<List<Object>> obs  = null;
                                if(i != group-1){
                                    obs = newlist.subList(i*groupNum, (i+1)*groupNum);
                                } else {
                                    obs = newlist.subList(i*groupNum, i*groupNum+num);
                                }
                                Future f = executor.submit(new DealThread(info, obs));
                                fus.add(f);
                            }
                            for (Future f : fus) {
                                try {
                                    f.get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                            executor.shutdown();
                            
                            // 关闭启动线程
//                            pool.shutdown();
                            return true;
                        }
                    }, 1, 1);
        } catch (BusinessException be) {
            LogUtil.error(MODULE, "===业务异常===", be);
            throw be;
        } catch (Exception e) {
            LogUtil.error(MODULE, "===系统异常===", e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ORD_SERVER_310049);
        }
        return ordImportLogSV.queryFailTmOrdImport(info.getFileId(), "06");
    }

    @Override
    public List<ROrdTmImportLogResp> queryFailTmOrdImport(String fileId, String importType)
            throws BusinessException {

        return ordImportLogSV.queryFailTmOrdImport(fileId, importType);
    }

    public class DealThread extends Thread {
        private RFileImportRequest info;

        private List<List<Object>> group;

        public DealThread(RFileImportRequest info, List<List<Object>> group) {
            this.info = info;
            this.group = group;
        }

        @Override
        public void run() {

            ordImportSV.saveBatchOrdMainTM(info, group);

        }

    }
    public class DealOne extends Thread {
        private RFileImportRequest info;

        private List<Object> row;

        public DealOne(RFileImportRequest info, List<Object> row) {
            this.info = info;
            this.row = row;
        }

        @Override
        public void run() {

            ordImportSV.saveOneOrdMainTM(info, row);

        }

    }
}
