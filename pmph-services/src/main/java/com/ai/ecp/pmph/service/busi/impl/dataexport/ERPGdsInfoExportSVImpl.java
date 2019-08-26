package com.ai.ecp.pmph.service.busi.impl.dataexport;

import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.goods.dao.model.GdsInterfaceGdsGidx;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption;
import com.ai.ecp.goods.dubbo.dto.GdsMediaRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsInterfaceGdsGidxReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdslog.GdsLogReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdslog.GdsLogRespDTO;
import com.ai.ecp.goods.dubbo.util.GdsUtils;
import com.ai.ecp.goods.service.busi.interfaces.IGdsInterfaceGdsSV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsInfoQuerySV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsSkuInfoQuerySV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfores.IGdsSkuInfo2MediaSV;
import com.ai.ecp.goods.service.busi.interfaces.gdslog.IGdsLogSV;
import com.ai.ecp.goods.service.busi.interfaces.gdslog.OperationType;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.service.busi.interfaces.dataexport.IERPGdsInfoExportSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.sys.dubbo.dto.SysCfgReqDTO;
import com.ai.ecp.sys.dubbo.dto.SysCfgResDTO;
import com.ai.ecp.sys.service.common.interfaces.ISysCfgSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class ERPGdsInfoExportSVImpl implements IERPGdsInfoExportSV {

    private final String MODULE=getClass().getName();

    private final Long shopId=PmphGdsDataImportConstants.Commons.SHOP_ID;
    //private final long shopId=3005l;

    @Resource
    private IGdsSkuInfoQuerySV gdsSkuInfoQuerySV;

    @Resource
    private IGdsInfoQuerySV gdsInfoQuerySV;

    @Resource
    private IGdsLogSV gdsLogSV;

    @Resource
    private ISysCfgSV sysCfgSV;

    @Resource
    protected IGdsInterfaceGdsSV gdsInterfaceGdsSV;

    @Resource
    private IGdsSkuInfo2MediaSV gdsSkuInfo2MediaSV;

    public String getGdsDetailSiteUrl(Long gdsId,Long skuId){
        String detailUrl = CmsCacheUtil.getCmsSiteCache(1l).getSiteUrl()+GdsUtils.getGdsUrl(gdsId, skuId, null);
        if(detailUrl==null){
            detailUrl="";
        }
        return detailUrl;
    }

    @Override
    public List<Map<String,Object>> fullImport() {

        Integer pageSize= Integer.parseInt(SysCfgUtil.fetchSysCfg("GDS_DELTA_PAGESIZE").getParaValue());
        Integer pageNo= 0;
        String isFullFinished="1";

        SysCfgReqDTO sysCfgReqDTO=new SysCfgReqDTO();
        sysCfgReqDTO.setParaCode("GDS_FULL_PAGENO");
        SysCfgResDTO sysCfgResDTO=sysCfgSV.fetchByparaCode(sysCfgReqDTO);

        if(sysCfgResDTO!=null){
            pageNo=Integer.parseInt(sysCfgResDTO.getParaValue());
        }

        sysCfgReqDTO.setParaCode("GDS_FULL_FINISH");
        sysCfgResDTO=sysCfgSV.fetchByparaCode(sysCfgReqDTO);

        if(sysCfgResDTO!=null){
            isFullFinished=sysCfgResDTO.getParaValue();
        }

        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();

        //全量未结束
        if(!StringUtils.equals(GdsConstants.Commons.STATUS_VALID,isFullFinished)&&(pageNo!=0)){

            //把当前系统点记录系统参数（GDS_DELTA_STARTTIME），以作为下一个增量起始时间点。
            if(pageNo==1){
                sysCfgReqDTO.setParaCode("GDS_DELTA_STARTTIME");
                sysCfgReqDTO.setSearchParams("GDS_DELTA_STARTTIME");
                sysCfgReqDTO.setParaValue(DateUtil.getSysDate().getTime()+"");
                sysCfgSV.updateSysCfg(sysCfgReqDTO);
            }

            GdsSkuInfoReqDTO gdsSkuInfoReqDTO=new GdsSkuInfoReqDTO();
            gdsSkuInfoReqDTO.setPageSize(pageSize);
            gdsSkuInfoReqDTO.setPageNo(pageNo);
            gdsSkuInfoReqDTO.setGdsTypeId(1l);
            gdsSkuInfoReqDTO.setIfScoreGds(GdsConstants.Commons.STATUS_INVALID);
            List<String> sortRule=new ArrayList<String>();
            //必须是id升序
            sortRule.add("SKU_ID,asc");
            gdsSkuInfoReqDTO.setSortRule(sortRule);
            gdsSkuInfoReqDTO.setShopId(shopId);

            GdsOption.SkuQueryOption[] skuQuery = new GdsOption.SkuQueryOption[] { GdsOption.SkuQueryOption.BASIC,
                    GdsOption.SkuQueryOption.MAINPIC};

            LogUtil.error(MODULE, "准备进行全量分页查询！当前页码pageNo是："+pageNo);
            LogUtil.error(MODULE, "准备进行全量分页查询！分页大小pageSize是："+pageSize);
            LogUtil.error(MODULE, "准备进行全量分页查询！店铺编码shopId是："+shopId);

            PageResponseDTO<GdsSkuInfoRespDTO> pageResponseDTO= this.gdsSkuInfoQuerySV.queryGdsSkuInfoPageListResp(gdsSkuInfoReqDTO,skuQuery);

            if(pageResponseDTO!=null){

                if(pageResponseDTO.getResult()!=null&&CollectionUtils.isNotEmpty(pageResponseDTO.getResult())){
                    for(GdsSkuInfoRespDTO gdsSkuInfoRespDTO:pageResponseDTO.getResult()){

                        if(gdsSkuInfoRespDTO==null){
                            LogUtil.error(MODULE, "单品-店铺索引表分页后（内部再查询单品主表信息），返回的单品对象为空！原因可能是：内部根据单品编码查询单品信息返回对象为空（主表或缓存）！");
                            continue;
                        }

                        Map<String,Object> map=new HashMap<String,Object>();
                        map.put("ID",gdsSkuInfoRespDTO.getId());
                        map.put("GDS_ID",gdsSkuInfoRespDTO.getGdsId());
                        map.put("GDS_NAME",gdsSkuInfoRespDTO.getGdsName()==null?"":gdsSkuInfoRespDTO.getGdsName());
                        map.put("GDS_SUB_HEAD",gdsSkuInfoRespDTO.getGdsSubHead()==null?"":gdsSkuInfoRespDTO.getGdsSubHead());
                        map.put("GUIDE_PRICE",gdsSkuInfoRespDTO.getGuidePrice());
                        map.put("GDS_STATUS",gdsSkuInfoRespDTO.getGdsStatus()==null?"":gdsSkuInfoRespDTO.getGdsStatus());
                        map.put("URL",getGdsDetailSiteUrl(gdsSkuInfoRespDTO.getGdsId(),gdsSkuInfoRespDTO.getId()));
                        if(gdsSkuInfoRespDTO.getMainPic()!=null){
                            String imageUrl=ImageUtil.getImageUrl(gdsSkuInfoRespDTO.getMainPic().getMediaUuid()+ "_220x220!");
                            map.put("IMAGE_URL",imageUrl==null?"":imageUrl);
                        }else{
                            map.put("IMAGE_URL","");
                        }
                        map.put("ISBN",gdsSkuInfoRespDTO.getIsbn()==null?"":gdsSkuInfoRespDTO.getIsbn());
                        map.put("OPER_TYPE","1");
                        /*map.put("CREATE_TIME",gdsSkuInfoRespDTO.getCreateTime());
                        map.put("CREATE_STAFF",gdsSkuInfoRespDTO.getCreateStaff());
                        map.put("UPDATE_TIME",gdsSkuInfoRespDTO.getUpdateTime());
                        map.put("UPDATE_STAFF",gdsSkuInfoRespDTO.getUpdateStaff());*/

                        GdsInterfaceGdsGidxReqDTO gdsInterfaceGdsReqDTO = new GdsInterfaceGdsGidxReqDTO();
                        gdsInterfaceGdsReqDTO.setGdsId(gdsSkuInfoRespDTO.getGdsId());
                        gdsInterfaceGdsReqDTO.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
                        GdsInterfaceGdsGidx gdsInterfaceGds = this.gdsInterfaceGdsSV
                                .queryGdsInterfaceGdsGidxByEcpGdsId(gdsInterfaceGdsReqDTO);

                        //ECP侧创建商品，该字段为空
                        if(gdsInterfaceGds!=null){
                            map.put("BENBANBIANHAO",gdsInterfaceGds.getOriginGdsId()==null?"":gdsInterfaceGds.getOriginGdsId());
                        }else{
                            map.put("BENBANBIANHAO","");
                        }
                        list.add(map);

                    }

                    if(pageResponseDTO.getPageCount()>pageNo&&pageResponseDTO.getResult().size()!=pageNo){
                        LogUtil.error(MODULE, "，当前页不是最后一页，但是分页大小pageSize不等于分页返回记录总数！");
                    }
                }

                //分页结束
                if(pageResponseDTO.getPageCount()==pageNo){
                    sysCfgReqDTO.setParaCode("GDS_FULL_PAGENO");
                    sysCfgReqDTO.setSearchParams("GDS_FULL_PAGENO");
                    sysCfgReqDTO.setParaValue("0");
                    sysCfgSV.updateSysCfg(sysCfgReqDTO);

                    sysCfgReqDTO.setParaCode("GDS_FULL_FINISH");
                    sysCfgReqDTO.setSearchParams("GDS_FULL_FINISH");
                    sysCfgReqDTO.setParaValue("1");
                    sysCfgSV.updateSysCfg(sysCfgReqDTO);
                }else{
                    sysCfgReqDTO.setParaCode("GDS_FULL_PAGENO");
                    sysCfgReqDTO.setSearchParams("GDS_FULL_PAGENO");
                    sysCfgReqDTO.setParaValue((++pageNo)+"");
                    sysCfgSV.updateSysCfg(sysCfgReqDTO);
                }

            }else{
                LogUtil.error(MODULE, "分页返回对象pageResponseDTO为空，全量同步提前结束！");
                sysCfgReqDTO.setParaCode("GDS_FULL_PAGENO");
                sysCfgReqDTO.setSearchParams("GDS_FULL_PAGENO");
                sysCfgReqDTO.setParaValue("0");
                sysCfgSV.updateSysCfg(sysCfgReqDTO);

                sysCfgReqDTO.setParaCode("GDS_FULL_FINISH");
                sysCfgReqDTO.setSearchParams("GDS_FULL_FINISH");
                sysCfgReqDTO.setParaValue("1");
                sysCfgSV.updateSysCfg(sysCfgReqDTO);
            }
        }else{
            LogUtil.error(MODULE, "全量同步已结束！");
        }

         return list;

    }

    @Override
    public List<Map<String,Object>> deltaImport() throws Exception {
        Integer pageSize= Integer.parseInt(SysCfgUtil.fetchSysCfg("GDS_DELTA_PAGESIZE").getParaValue());
        Integer pageNo= 1;
        Timestamp tsStartTS=null;

        String isFullFinished="0";

        SysCfgReqDTO sysCfgReqDTO=new SysCfgReqDTO();
        sysCfgReqDTO.setParaCode("GDS_DELTA_PAGENO");
        SysCfgResDTO sysCfgResDTO=sysCfgSV.fetchByparaCode(sysCfgReqDTO);

        if(sysCfgResDTO!=null){
            pageNo=Integer.parseInt(sysCfgResDTO.getParaValue());
        }

        sysCfgReqDTO.setParaCode("GDS_FULL_FINISH");
        sysCfgResDTO=sysCfgSV.fetchByparaCode(sysCfgReqDTO);

        if(sysCfgResDTO!=null){
            isFullFinished=sysCfgResDTO.getParaValue();
        }

        sysCfgReqDTO.setParaCode("GDS_DELTA_STARTTIME");
        sysCfgResDTO=sysCfgSV.fetchByparaCode(sysCfgReqDTO);

        if(sysCfgResDTO!=null){
            if(org.apache.commons.lang.StringUtils.isNotBlank(sysCfgResDTO.getParaValue())&&!org.apache.commons.lang.StringUtils.equals(sysCfgResDTO.getParaValue(),"0")){
                tsStartTS=new Timestamp(Long.parseLong(sysCfgResDTO.getParaValue()));
            }
        }

        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();

        //需要先做全量或全量未结束
        if(tsStartTS!=null&&StringUtils.equals(GdsConstants.Commons.STATUS_VALID,isFullFinished)){

            GdsLogReqDTO gdsLogReqDTO=new GdsLogReqDTO();
            gdsLogReqDTO.setPageSize(pageSize);
            gdsLogReqDTO.setPageNo(pageNo);
            gdsLogReqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
            List<String> sortRule=new ArrayList<String>();
            //必须是id升序
            sortRule.add("log_id,asc");
            gdsLogReqDTO.setSortRule(sortRule);
            gdsLogReqDTO.setBegUpdateTime(tsStartTS);
            PageResponseDTO<GdsLogRespDTO> pageResponseDTO= this.gdsLogSV.queryGdsLogRespDTOPaging(gdsLogReqDTO);

            if(pageResponseDTO!=null&&pageResponseDTO.getResult()!=null&&CollectionUtils.isNotEmpty(pageResponseDTO.getResult())){

                for(GdsLogRespDTO gdsLogRespDTO:pageResponseDTO.getResult()){

                    Short operResult=gdsLogRespDTO.getOperResult();
                    String operType=gdsLogRespDTO.getOperType();
                    String operParam=gdsLogRespDTO.getOperParam();

                    LogUtil.error(MODULE, "处理到增量日志：【logId:"+gdsLogRespDTO.getLogId()+",operType:"+operType+",operResult:"+operResult+",operParam:"+operParam+"】");

                    if(operResult==1&& StringUtils.isNotBlank(operType)&& StringUtils.isNotBlank(operParam)){
                        JSONObject jso=JSON.parseObject(operParam);
                        //默认操作类型是新增
                        String type="1";
                        List<String> gdsIdList=new ArrayList<String>();

                        //TODO 审核类型商品操作类型暂时需要通过商品状态字段获取
                        List<String> verifyIdList=new ArrayList<String>();

                        //1新增、2修改、0删除、3上架、4下架。
                        if(OperationType.GDS_ADD.getName().equals(operType)){//商品新增
                            type="1";
                            if(jso.containsKey("id")){
                                gdsIdList.add(String.valueOf(jso.get("id")));
                            }
                        }else if(OperationType.GDS_EDIT.getName().equals(operType)){//商品修改
                            type="2";
                            if(jso.containsKey("id")){
                                gdsIdList.add(String.valueOf(jso.get("id")));
                            }else if(jso.containsKey("gdsInfoReqDTO.id")){
                                gdsIdList.add(String.valueOf(jso.get("gdsInfoReqDTO.id")));
                            }
                        }else if(OperationType.GDS_DELETE.getName().equals(operType)){//商品删除
                            type="0";
                            if(jso.containsKey("id")){
                                gdsIdList.add(String.valueOf(jso.get("id")));
                            }
                        }else if(OperationType.GDS_BATCH_DELETE.getName().equals(operType)){//商品批量删除
                            type="0";
                            if(jso.containsKey("ids")){
                                JSONArray jsoa=jso.getJSONArray("ids");
                                if(jsoa.size()>0){
                                    for(int i=0;i<jsoa.size();i++){
                                        gdsIdList.add(String.valueOf(jsoa.get(i)));
                                    }
                                }
                            }
                        }else if(OperationType.GDS_ON_SHELVES.getName().equals(operType)){//商品上架
                            type="3";
                            if(jso.containsKey("id")){
                                gdsIdList.add(String.valueOf(jso.get("id")));
                            }
                        }else if(OperationType.GDS_BATCH_ON_SHELVES.getName().equals(operType)){//商品批量上架
                            type="3";
                            if(jso.containsKey("ids")){
                                JSONArray jsoa=jso.getJSONArray("ids");
                                if(jsoa.size()>0){
                                    for(int i=0;i<jsoa.size();i++){
                                        gdsIdList.add(String.valueOf(jsoa.get(i)));
                                    }
                                }
                            }
                        }else if(OperationType.GDS_OFF_SHELVES.getName().equals(operType)){//商品下架
                            type="4";
                            if(jso.containsKey("id")){
                                gdsIdList.add(String.valueOf(jso.get("id")));
                            }
                        }else if(OperationType.GDS_BATCH_OFF_SHELVES.getName().equals(operType)){//商品批量下架
                            type="4";
                            if(jso.containsKey("ids")){
                                JSONArray jsoa=jso.getJSONArray("ids");
                                if(jsoa.size()>0){
                                    for(int i=0;i<jsoa.size();i++){
                                        gdsIdList.add(String.valueOf(jsoa.get(i)));
                                    }
                                }
                            }
                        }else if(OperationType.GDS_INFO_VERIFY.getName().equals(operType)){//商品审核//触发审核的动作。而不是提交动作。
                            //TODO 需要区分审核类型
                            //TODO 需要知道审核结果
                            type="2";
                            if(jso.containsKey("ids")){
                                JSONArray jsoa=jso.getJSONArray("ids");
                                if(jsoa.size()>0){
                                    for(int i=0;i<jsoa.size();i++){
                                        gdsIdList.add(String.valueOf(jsoa.get(i)));
                                        verifyIdList.add(String.valueOf(jsoa.get(i)));
                                    }
                                }
                            }
                        }

                        if(gdsIdList.size()==0){
                            continue;
                        }

                        for(String s:gdsIdList){
                            List<Long> skuId = gdsInfoQuerySV.querySkuIdsGdsId(Long.parseLong(s));
                            if (CollectionUtils.isEmpty(skuId)) {
                                LogUtil.error(MODULE, "根据商品编码查找不到单品编码列表！商品编码："+s);
                                continue;
                            }

                            Map<String,Object> map=new HashMap<String,Object>();
                            map.put("ID",skuId.get(0));
                            map.put("GDS_ID",Long.parseLong(s));
                            map.put("OPER_TYPE",type);

                            GdsSkuInfoReqDTO gdsSkuInfoReqDTO=new GdsSkuInfoReqDTO();
                            gdsSkuInfoReqDTO.setId(skuId.get(0));
                            GdsSkuInfoRespDTO gdsSkuInfo=gdsSkuInfoQuerySV.queryGdsSkuInfoResp(gdsSkuInfoReqDTO);

                            if(gdsSkuInfo==null){
                                LogUtil.error(MODULE, "根据单品编码查找不到单品主表信息！单品编码："+skuId.get(0));
                                continue;
                            }

                            if(gdsSkuInfo.getShopId().longValue()!=shopId){
                                continue;
                            }

                            //纸质书//积分商品
                            if(gdsSkuInfo.getGdsTypeId()!=1l||StringUtils.equals(gdsSkuInfo.getIfScoreGds(),GdsConstants.Commons.STATUS_VALID)){
                                continue;
                            }

                            //TODO 审核类型商品操作类型暂时需要通过商品状态字段获取
                            if(verifyIdList.contains(s)){
                                if(StringUtils.equals("0",gdsSkuInfo.getGdsStatus())){//待上架
                                    type="1";
                                }else if(GdsUtils.isOnShelves(gdsSkuInfo.getGdsStatus())){//已上架
                                    type="3";
                                }else if(GdsUtils.isOffShelves(gdsSkuInfo.getGdsStatus())){//已下架
                                    type="4";
                                }else if(GdsUtils.isDelete(gdsSkuInfo.getGdsStatus())){//删除
                                    type="0";
                                }

                                //1新增、2修改、0删除、3上架、4下架。
                                map.put("OPER_TYPE",type);
                            }

                            //查询媒体信息
                            GdsMediaRespDTO mainPic = gdsSkuInfo2MediaSV.querySkuMainPicBySkuId(gdsSkuInfo.getId(), gdsSkuInfo.getGdsId());
                            gdsSkuInfo.setMainPic(mainPic);

                            map.put("GDS_NAME",gdsSkuInfo.getGdsName()==null?"":gdsSkuInfo.getGdsName());
                            map.put("GDS_SUB_HEAD",gdsSkuInfo.getGdsSubHead()==null?"":gdsSkuInfo.getGdsSubHead());
                            map.put("GUIDE_PRICE",gdsSkuInfo.getGuidePrice());
                            map.put("GDS_STATUS",gdsSkuInfo.getGdsStatus()==null?"":gdsSkuInfo.getGdsStatus());
                            map.put("URL",getGdsDetailSiteUrl(gdsSkuInfo.getGdsId(),gdsSkuInfo.getId()));
                            if(gdsSkuInfo.getMainPic()!=null){
                                String imageUrl=ImageUtil.getImageUrl(gdsSkuInfo.getMainPic().getMediaUuid()+ "_220x220!");
                                map.put("IMAGE_URL",imageUrl==null?"":imageUrl);
                            }else{
                                map.put("IMAGE_URL","");
                            }
                            map.put("ISBN",gdsSkuInfo.getIsbn()==null?"":gdsSkuInfo.getIsbn());
                           /* map.put("CREATE_TIME",gdsSkuInfo.getCreateTime());
                            map.put("CREATE_STAFF",gdsSkuInfo.getCreateStaff());
                            map.put("UPDATE_TIME",gdsSkuInfo.getUpdateTime());
                            map.put("UPDATE_STAFF",gdsSkuInfo.getUpdateStaff());*/

                            GdsInterfaceGdsGidxReqDTO gdsInterfaceGdsReqDTO = new GdsInterfaceGdsGidxReqDTO();
                            gdsInterfaceGdsReqDTO.setGdsId(gdsSkuInfo.getGdsId());
                            gdsInterfaceGdsReqDTO.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
                            GdsInterfaceGdsGidx gdsInterfaceGds = this.gdsInterfaceGdsSV
                                    .queryGdsInterfaceGdsGidxByEcpGdsId(gdsInterfaceGdsReqDTO);

                            //ECP侧创建商品，该字段为空
                            if(gdsInterfaceGds!=null){
                                map.put("BENBANBIANHAO",gdsInterfaceGds.getOriginGdsId()==null?"":gdsInterfaceGds.getOriginGdsId());
                            }else{
                                map.put("BENBANBIANHAO","");
                            }
                            list.add(map);
                        }
                    }
                }

                //分页标识和全量分页是否结束标识更新
                if(pageResponseDTO.getResult().size()<pageSize){
                    sysCfgReqDTO.setParaCode("GDS_DELTA_PAGENO");
                    sysCfgReqDTO.setSearchParams("GDS_DELTA_PAGENO");
                    sysCfgReqDTO.setParaValue("1");
                    sysCfgSV.updateSysCfg(sysCfgReqDTO);

                    //以最后一个增量商品的更新时间作为下一个增量起始时间点。
                    GdsLogRespDTO lastGdsLogRespDTO=pageResponseDTO.getResult().get(pageResponseDTO.getResult().size()-1);
                    sysCfgReqDTO.setParaCode("GDS_DELTA_STARTTIME");
                    sysCfgReqDTO.setSearchParams("GDS_DELTA_STARTTIME");
                    long start=lastGdsLogRespDTO.getUpdateTime().getTime()+1;
                    sysCfgReqDTO.setParaValue(start+"");
                    sysCfgSV.updateSysCfg(sysCfgReqDTO);
                }else{
                    sysCfgReqDTO.setParaCode("GDS_DELTA_PAGENO");
                    sysCfgReqDTO.setSearchParams("GDS_DELTA_PAGENO");
                    sysCfgReqDTO.setParaValue((++pageNo)+"");
                    sysCfgSV.updateSysCfg(sysCfgReqDTO);
                }
            }
        }else{
            LogUtil.error(MODULE, "请先做全量同步或等待全量同步结束！");
        }
        Collections.reverse(list);
        //LogUtil.info(MODULE,"输入同步数据："+list.toString());
        return list;
    }
}
