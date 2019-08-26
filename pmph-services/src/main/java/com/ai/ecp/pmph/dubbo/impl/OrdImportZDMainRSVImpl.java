package com.ai.ecp.pmph.dubbo.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdZDResponse;
import com.ai.ecp.order.dubbo.util.MsgConstants;
import com.ai.ecp.pmph.dubbo.dto.ROrdZDImportLogResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdImportZDMainRSV;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportLogSV;
import com.ai.ecp.pmph.service.busi.interfaces.IZDOrdImportSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.server.util.DataInoutUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.PubUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.PubUserInfoRespDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.IPubRSV;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ExcelFileUtil;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

public class OrdImportZDMainRSVImpl implements IOrdImportZDMainRSV {
	
	private static final String MODULE = OrdImportTMMainRSVImpl.class.getName(); 
	
	//线程池大小
    private static final int FORK_JOIN_THREADS = 4*8;
	
	@Resource
    private IZDOrdImportSV zDOrdImportSV;
	
	@Resource
    private IOrdImportLogSV ordImportLogSV;
	
	@Resource
    private ICustManageRSV custManageRSV;
	
	@Resource IPubRSV pubRSV;
	
	/*@Override
	public List<ROrdZDImportLogResp> importZDMain(final RFileImportRequest info) throws BusinessException {
		
		LogUtil.info(MODULE, "征订单导入dubbo服务开始导入");
		try {
			InputStream inputStream = new ByteArrayInputStream(FileUtil.readFile(info.getFileId()));
			DataInoutUtil.importExcel(
					new DataImportHandler(inputStream, FileUtil.getFileName(info.getFileId()),
							"xlsx", info.getModuleName(), info.getStaffId().toString()) {

						@Override
						public boolean doCallback(List<List<Object>> list, String s) {
							if (list.size() > 10000) {
                                LogUtil.error(MODULE, "===征订单导入不能超过6000===");
                                throw new BusinessException(
                                        MsgConstants.ServiceMsgCode.ZDORD_SERVER_360002);
                            }
							
							if (CollectionUtils.isEmpty(list)) {
                                return true;
                            }
							
							if (list.get(0).size() != 50) {
                                throw new BusinessException(
                                        MsgConstants.ServiceMsgCode.ZDORD_SERVER_360003);
                            }
							List<List<Object>> newlist = list.subList(1, list.size()-14);
							if(CollectionUtils.isEmpty(newlist)){
                                return true;
                            }
							
							ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()/2,
									Runtime.getRuntime().availableProcessors()/2+1,1,TimeUnit.MILLISECONDS,
									new ArrayBlockingQueue<Runnable>(Runtime.getRuntime().availableProcessors()*2));
							executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
							
							int groupNum = Runtime.getRuntime().availableProcessors()/2+1;
							int group = newlist.size() / groupNum + 1;
							int num = newlist.size()%groupNum;
							
							List<Future<ConcurrentHashMap<String, RPreOrdMainsResponse>>> fus = new ArrayList<Future<ConcurrentHashMap<String, RPreOrdMainsResponse>>>();
							
							//添加一个计数器
							AtomicInteger countTool = new AtomicInteger(0);
							for (int i = 0; i < group; i++) {
								List<List<Object>> obs  = null;
								if(i != group-1){
									obs = newlist.subList(i*groupNum, (i+1)*groupNum);
								} else {
									obs = newlist.subList(i*groupNum, i*groupNum+num);
								}
								Future<ConcurrentHashMap<String, RPreOrdMainsResponse>> f = executor.submit(new PubDealThread(info, obs, countTool, new ConcurrentHashMap<String, RPreOrdMainsResponse>()));
								fus.add(f);
							}
							List<ConcurrentHashMap<String, RPreOrdMainsResponse>> resultMapList = new ArrayList<ConcurrentHashMap<String, RPreOrdMainsResponse>>();
							for (Future<ConcurrentHashMap<String, RPreOrdMainsResponse>> f : fus) {
								try {
									ConcurrentHashMap<String, RPreOrdMainsResponse> rPreOrdMains = f.get();
									resultMapList.add(rPreOrdMains);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
							}
							executor.shutdown();
							
							Long baseMoney = 0L;
							Long orderMoney = 0L;
							Long realMoney = 0L;
							Long discountMoney = 0L;
							Long realExpressFee = 0L;
							
							//把最终结果放入缓存
							RPreOrdZDResponse rPreOrdZDResponse = new RPreOrdZDResponse();
							//遍历resultMapList计算出orderMoney, realMoney, discountMoney, realExpressFee
							for (ConcurrentHashMap<String, RPreOrdMainsResponse> concurrentHashMap : resultMapList) {
								for (Map.Entry<String, RPreOrdMainsResponse> rPreMainEntry: concurrentHashMap.entrySet()) {
									if(rPreMainEntry.getValue().getPreOrdMainList()!=null){
										orderMoney = orderMoney + rPreMainEntry.getValue().getPreOrdMainList().get(0).getOrderMoney();
										discountMoney = discountMoney + rPreMainEntry.getValue().getPreOrdMainList().get(0).getDiscountMoney();
										realExpressFee = realExpressFee + rPreMainEntry.getValue().getPreOrdMainList().get(0).getRealExpressFee();
										baseMoney = baseMoney + rPreMainEntry.getValue().getPreOrdMainList().get(0).getBasicMoney();
									}
								}
							}
							//status先不在这里做处理
							//rPreOrdZDResponse.setStatus("01");
							//查询会员名 和 征订单ID
							CustInfoResDTO custInfoResDTO = custManageRSV.findCustInfoById(info.getStaffId());
							PubUserInfoReqDTO pubUserInfoReqDTO = new PubUserInfoReqDTO();
							pubUserInfoReqDTO.setStaffCode(custInfoResDTO.getStaffCode());
							PageResponseDTO<PubUserInfoRespDTO> pubUserInfos = pubRSV.queryPubUserInfoPage(pubUserInfoReqDTO);
							
							rPreOrdZDResponse.setStaffCode(custInfoResDTO.getStaffCode());
							rPreOrdZDResponse.setStaffId(pubUserInfos.getResult().get(0).getId());
							
							realMoney = baseMoney + realExpressFee - discountMoney;
							rPreOrdZDResponse.setBaseMoney(baseMoney);
							rPreOrdZDResponse.setOrderMoney(orderMoney);
							rPreOrdZDResponse.setRealMoney(realMoney);
							rPreOrdZDResponse.setDiscountMoney(discountMoney);
							rPreOrdZDResponse.setRealExpressFee(realExpressFee);
							rPreOrdZDResponse.setOrdMainMap(resultMapList);
							String redisKey = md5key(new StringBuilder().append(info.getStaffId()).append(DateUtil.getCurrentTimeMillis()).toString());
							CacheUtil.addItem(new StringBuilder().append(redisKey).toString(), rPreOrdZDResponse,1800);
							//把redisKey也放入缓存
							CacheUtil.addItem(new StringBuilder().append(rPreOrdZDResponse.getStaffCode()).toString(), redisKey, 1800);
							
							//重新组合新的元素导入
							//第一行抬头
							List<List<Object>> titleList = list.subList(0, 1);
							List<Object> titleRows = titleList.get(0);
							List<String> newtitleRows = new ArrayList<String>();
							newtitleRows.add("错误原因");
							for(int i=0; i<titleRows.size(); i++){
								newtitleRows.add(StringUtil.toString(titleRows.get(i)));
	                		}
							//错误内容信息
							@SuppressWarnings("unchecked")
							List<List<Object>> errorList = (List<List<Object>>)CacheUtil.getItem(new StringBuilder("FAIL_PUB_ORD_MAIN_LIST").append(info.getFileId()).toString());
							
							//组合
							List<List<Object>> newErrorList = new ArrayList<List<Object>>();
							//错误内容
							if(errorList!=null){
								for(int i=0; i<errorList.size(); i++){
									newErrorList.add(errorList.get(i));
								}
								
								String errorFileId = DataInoutUtil.exportExcel(newErrorList, newtitleRows,
										"errorTemplate", "xlsx", "征订单导入错误信息", rPreOrdZDResponse.getStaffCode());
								
								CacheUtil.addItem(new StringBuilder("errorFileId").append(rPreOrdZDResponse.getStaffCode()).toString(), errorFileId, 1800);
								
								//清除缓存
								CacheUtil.delItem(new StringBuilder("FAIL_PUB_ORD_MAIN_LIST").append(info.getFileId()).toString());
							}
							
							return true;
						}
						
					}, 1, 1);
		} catch (BusinessException be){
			LogUtil.error(MODULE, "===业务异常===", be);
            throw be;
		} catch (Exception e){
			LogUtil.error(MODULE, "===系统异常===", e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ZDORD_SERVER_360001);
		}
		return queryFailZDOrdImport(info.getFileId(), "08");
	}
	*/
	
	@Override
	public List<ROrdZDImportLogResp> importZDMain(final RFileImportRequest info) throws BusinessException {
		
		LogUtil.info(MODULE, "征订单导入dubbo服务开始导入");
		
		//通过fileId获得excel文件
        //excel错误提醒  文件无数据  文件数据格式不对 文件数据重复 文件填写不对等
        byte[] byteFile = FileUtil.readFile(info.getFileId());//根据文件id得到文件
        InputStream inputs = new ByteArrayInputStream(byteFile);
        
        List<ConcurrentHashMap<String, RPreOrdMainsResponse>> resultMapList = new ArrayList<ConcurrentHashMap<String, RPreOrdMainsResponse>>();

        //直接用excel文件解析
        Object[] object=ExcelFileUtil.importExcel(inputs, 1, 1, info.getFileName(), "xlsx");
        //判断文件大小
        List<List<Object>> datasList = (List<List<Object>>)object[0];

		if (CollectionUtils.isEmpty(datasList)) {
            return null;
        }
		
		if (datasList.get(0).size() != 50) {
            throw new BusinessException(
                    MsgConstants.ServiceMsgCode.ZDORD_SERVER_360003);
        }
		
		List<List<Object>> newlist = datasList.subList(1, datasList.size()-14);
		if(CollectionUtils.isEmpty(newlist)){
            return null;
        }
		BaseSysCfgRespDTO syscfg = SysCfgUtil.fetchSysCfg("ZD_ORDER_IMPORT_NUMBER_LIMIT");
		if(newlist.size() > Long.valueOf(syscfg.getParaValue()))
		{
			LogUtil.error(MODULE, "===征订单导入不能超过"+syscfg.getParaValue()+"===");
			throw new BusinessException("征订单导入不能超过"+syscfg.getParaValue());
		}
        
        OrdImportDistributeDataFJTask mainTask = new OrdImportDistributeDataFJTask(info.getFileId(), info, newlist);
        
        
        ForkJoinPool forkjoinpool = new ForkJoinPool(FORK_JOIN_THREADS);
        Future<List<ConcurrentHashMap<String, RPreOrdMainsResponse>>> future = forkjoinpool.submit(mainTask);
        
        try {
        	resultMapList = future.get();
        	Long baseMoney = 0L;
			Long orderMoney = 0L;
			Long realMoney = 0L;
			Long discountMoney = 0L;
			Long realExpressFee = 0L;
			
			//把最终结果放入缓存
			RPreOrdZDResponse rPreOrdZDResponse = new RPreOrdZDResponse();
			//遍历resultMapList计算出orderMoney, realMoney, discountMoney, realExpressFee
			for (ConcurrentHashMap<String, RPreOrdMainsResponse> concurrentHashMap : resultMapList) {
				for (Map.Entry<String, RPreOrdMainsResponse> rPreMainEntry: concurrentHashMap.entrySet()) {
					if(rPreMainEntry.getValue().getPreOrdMainList()!=null){
						orderMoney = orderMoney + rPreMainEntry.getValue().getPreOrdMainList().get(0).getOrderMoney();
						discountMoney = discountMoney + rPreMainEntry.getValue().getPreOrdMainList().get(0).getDiscountMoney();
						realExpressFee = realExpressFee + rPreMainEntry.getValue().getPreOrdMainList().get(0).getRealExpressFee();
						baseMoney = baseMoney + rPreMainEntry.getValue().getPreOrdMainList().get(0).getBasicMoney();
					}
				}
			}
			
			//status先不在这里做处理
			//rPreOrdZDResponse.setStatus("01");
			//查询会员名 和 征订单ID
			CustInfoResDTO custInfoResDTO = custManageRSV.findCustInfoById(info.getStaffId());
			PubUserInfoReqDTO pubUserInfoReqDTO = new PubUserInfoReqDTO();
			pubUserInfoReqDTO.setStaffCode(custInfoResDTO.getStaffCode());
			PageResponseDTO<PubUserInfoRespDTO> pubUserInfos = pubRSV.queryPubUserInfoPage(pubUserInfoReqDTO);
			
			rPreOrdZDResponse.setStaffCode(custInfoResDTO.getStaffCode());
			rPreOrdZDResponse.setStaffId(pubUserInfos.getResult().get(0).getId());
			
			realMoney = baseMoney + realExpressFee - discountMoney;
			rPreOrdZDResponse.setBaseMoney(baseMoney);
			rPreOrdZDResponse.setOrderMoney(orderMoney);
			rPreOrdZDResponse.setRealMoney(realMoney);
			rPreOrdZDResponse.setDiscountMoney(discountMoney);
			rPreOrdZDResponse.setRealExpressFee(realExpressFee);
			rPreOrdZDResponse.setOrdMainMap(resultMapList);
			String redisKey = md5key(new StringBuilder().append(info.getStaffId()).append(DateUtil.getCurrentTimeMillis()).toString());
			CacheUtil.addItem(new StringBuilder().append(redisKey).toString(), rPreOrdZDResponse,1800);
			//把redisKey也放入缓存
			CacheUtil.addItem(new StringBuilder().append(rPreOrdZDResponse.getStaffCode()).toString(), redisKey, 1800);
			
			//重新组合新的元素导入
			//第一行抬头
			List<List<Object>> titleList = datasList.subList(0, 1);
			List<Object> titleRows = titleList.get(0);
			List<String> newtitleRows = new ArrayList<String>();
			newtitleRows.add("错误原因");
			for(int i=0; i<titleRows.size(); i++){
				newtitleRows.add(StringUtil.toString(titleRows.get(i)));
    		}
			//错误内容信息
			@SuppressWarnings("unchecked")
			List<List<Object>> errorList = (List<List<Object>>)CacheUtil.getItem(new StringBuilder("FAIL_PUB_ORD_MAIN_LIST").append(info.getFileId()).toString());
			
			//组合
			List<List<Object>> newErrorList = new ArrayList<List<Object>>();
			//错误内容
			if(errorList!=null){
				for(int i=0; i<errorList.size(); i++){
					newErrorList.add(errorList.get(i));
				}
				
				String errorFileId = DataInoutUtil.exportExcel(newErrorList, newtitleRows,
						"errorTemplate", "xlsx", "征订单导入错误信息", rPreOrdZDResponse.getStaffCode());
				
				CacheUtil.addItem(new StringBuilder("errorFileId").append(rPreOrdZDResponse.getStaffCode()).toString(), errorFileId, 1800);
				
				//清除缓存
				CacheUtil.delItem(new StringBuilder("FAIL_PUB_ORD_MAIN_LIST").append(info.getFileId()).toString());
			}
		} catch (BusinessException be){
			LogUtil.error(MODULE, "===业务异常===", be);
            throw be;
		} catch (Exception e){
			LogUtil.error(MODULE, "===系统异常===", e);
            throw new BusinessException(MsgConstants.ServiceMsgCode.ZDORD_SERVER_360001);
		}
		return queryFailZDOrdImport(info.getFileId(), "08");
	}
	
	
	
	public class PubDealThread implements Callable<ConcurrentHashMap<String, RPreOrdMainsResponse>> {
		private RFileImportRequest info;
		
		private List<List<Object>> group;
		
		private AtomicInteger countTool;
		
		private ConcurrentHashMap<String, RPreOrdMainsResponse> resultMap;
		
		public PubDealThread(RFileImportRequest info, List<List<Object>> group, AtomicInteger countTool, ConcurrentHashMap<String, RPreOrdMainsResponse> resultMap) {
            this.info = info;
            this.group = group;
            this.countTool = countTool;
            this.resultMap = resultMap;
        }

		@Override
		public ConcurrentHashMap<String, RPreOrdMainsResponse> call() throws Exception {
			return zDOrdImportSV.importZDPreOrdData(info, group, countTool, resultMap);
			
		}
		
	}
	
	/**
     * 
     * md5key:MD5加密. <br/> 
     * @param str
     * @return 
     * @since JDK 1.6
     */
    private String md5key(String str){
        return DigestUtils.md5Hex(str);
    }
	
	@Override
	public List<ROrdZDImportLogResp> queryFailZDOrdImport(String fileId, String importType) {
		return ordImportLogSV.queryFailZDOrdImport(fileId, importType);
	}
}
