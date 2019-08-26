/** 
 * File Name:ImageSampleChapterSVImpl.java 
 * Date:2015-10-30下午3:12:35 
 * 
 */
package com.ai.ecp.pmph.service.busi.impl.dataimport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ai.ecp.goods.dao.mapper.busi.GdsGds2MediaMapper;
import com.ai.ecp.goods.dao.mapper.busi.GdsGds2PropMapper;
import com.ai.ecp.goods.dao.mapper.busi.GdsGds2PropPropIdxMapper;
import com.ai.ecp.goods.dao.mapper.busi.GdsSku2PropMapper;
import com.ai.ecp.goods.dao.mapper.busi.GdsSku2PropPropIdxMapper;
import com.ai.ecp.goods.dao.model.GdsGds2Prop;
import com.ai.ecp.goods.dao.model.GdsGds2PropCriteria;
import com.ai.ecp.goods.dao.model.GdsGds2PropPropIdx;
import com.ai.ecp.goods.dao.model.GdsProp;
import com.ai.ecp.goods.dao.model.GdsSku2Prop;
import com.ai.ecp.goods.dao.model.GdsSku2PropCriteria;
import com.ai.ecp.goods.dao.model.GdsSku2PropPropIdx;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.dto.GdsMediaRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.category.GdsCategoryCompareReqDTO;
import com.ai.ecp.goods.dubbo.dto.category.GdsCategoryCompareRespDTO;
import com.ai.ecp.goods.dubbo.dto.category.dataimport.ImageSampleChapterRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2MediaReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsGds2MediaRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfores.GdsSku2PropReqDTO;
import com.ai.ecp.goods.dubbo.util.GdsCacheUtil;
import com.ai.ecp.goods.dubbo.util.GdsUtils;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsInfoManageSV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsInfoQuerySV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsSkuInfoQuerySV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfoidx.IGdsInfoIDXSV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfores.IGdsInfo2MediaSV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfores.IGdsInfo2PropSV;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfores.IGdsSkuInfo2PropSV;
import com.ai.ecp.goods.service.common.impl.AbstractSVImpl;
import com.ai.ecp.goods.service.common.interfaces.IGdsCategorySV;
import com.ai.ecp.goods.service.common.interfaces.IGdsPropSV;
import com.ai.ecp.goods.service.thread.GdsInfoRefreshTask;
import com.ai.ecp.goods.service.thread.GdsRefreshReq;
import com.ai.ecp.goods.service.thread.ThreadPoolExecutorUtil;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportErrorConstants;
import com.ai.ecp.pmph.service.busi.interfaces.dataimport.IImageSampleChapterSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description: 图片样章更新服务。<br>
 * Date:2015-10-30下午3:12:35 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class ImageSampleChapterSVImpl extends AbstractSVImpl implements IImageSampleChapterSV {

    /**
     * 五位ISBN属性ID。
     */
    private static final Long FIVE_ISBN_PROP_ID = 1004L;

    /**
     * 在线试读PDF属性ID。
     */
    private static final Long TRYING_READ_ONLINE_PDF_PROP_ID = 1026L;

    // private static final Pattern PATTERN_FILENAME =
    // Pattern.compile("^(.{5})(-|_?)(\\d*)(\\.)(jpg|JPG|jpeg|JPEG|png|PNG|pdf|PDF)$");

    // 类似00-0002.jpg 两位数字+4位数字.文件后缀名。可以直接跟五位ISBN号匹配。
    // private static final Pattern PATTERN_FILENAME_01 =
    // Pattern.compile("^(\\d{2}-\\d{4})(\\.)(jpg|JPG|jpeg|JPEG|png|PNG|pdf|PDF)$");

    // 00-0002.jpg 格式将提取00-0002 ISBN号去查询纸质书并匹配图片.
    // 06-0042-e.jpg 格式将提取06-0042-e ISBN号去查询纸质书并匹配图片
    // 8089-2371.jpg 格式将提取8089-2371 ISBN号去查询纸质书并匹配图片
    // 14117228.jpg 格式将提取14117228 ISBN号去查询纸质书并匹配图片.
    // 201104-13-e.jpg 格式将提取201104-13-e ISBN号去查询纸质书并匹配图片
    // 201104-6B.jpg格式将提取201104-6B ISBN号去查询纸质书并匹配图片
    // 11111B.jpg

    /**
     * 数字-数字.文件名.
     */
    private static final Pattern PATTERN_01 = Pattern.compile("^(\\d+)-(\\d*)(\\.)(jpg|JPG|jpeg|JPEG|png|PNG|pdf|PDF|bmp|BMP)$");

    /**
     * 全数字.文件名.
     */
    private static final Pattern PATTERN_02 = Pattern.compile("^(\\d+[a-zA-Z]*)(\\.)(jpg|JPG|jpeg|JPEG|png|PNG|pdf|PDF|bmp|BMP)$");

    /**
     * 其他文件命名方式.
     */
    private static final Pattern PATTERN_FILENAME_COMMON = Pattern.compile("^(.*)(\\.)(jpg|JPG|jpeg|JPEG|png|PNG|pdf|PDF|bmp|BMP)$");

    @Resource
    private IGdsCategorySV gdsCategorySV;

    @Resource
    private IGdsSkuInfoQuerySV gdsSkuInfoQuerySV;

    @Resource
    private IGdsSkuInfo2PropSV gdsSkuInfo2PropSV;

    @Resource
    private IGdsInfoQuerySV gdsInfoQuerySV;

    @Resource
    private IGdsInfoManageSV gdsInfoManageSV;

    @Resource
    private IGdsInfoIDXSV gdsInfoIDXSV;

    @Resource
    private GdsGds2PropMapper gds2propMapper;

    @Resource
    private GdsGds2PropPropIdxMapper gds2PropPropIdxMapper;

    @Resource
    private GdsSku2PropPropIdxMapper sku2PropPropIdxMapper;

    @Resource
    private IGdsPropSV gdsPropSV;

    @Resource
    private GdsSku2PropMapper sku2PropMapper;

    @Resource
    private IGdsInfo2PropSV gdsInfo2PropSV;

    @Resource
    private IGdsInfo2MediaSV gdsInfo2MediaSV;

    /**
     * 商品媒体关系表操作Mapper
     */
    @Resource
    private GdsGds2MediaMapper gds2MediaMapper;

    /**
     * 文件名。
     */
    public static final String KEY_FILE_NAME = "filename";

    /**
     * MongoDB返回UUID。
     */
    public static final String KEY_MEDIA_UUID = "fileid";

    @Override
    public void executeUpdate(Map<String, Object> map, String catgCode, Boolean sendIdxMsg) throws BusinessException {
        LogUtil.info(MODULE, "执行图片样章更新操作，入参map：" + map + ";限定分类编码catgCode:" + catgCode);
        if (MapUtils.isEmpty(map)) {
            LogUtil.warn(MODULE, "图片样章更新服务收到空数据集，服务直接返回!");
            return;
        }
        ImageSampleChapterRespDTO iscrdto = parseDataMap(map);
        if (null == iscrdto || StringUtil.isBlank(iscrdto.getIsbn())) {
            LogUtil.warn(MODULE, "数据集数据不合法，服务直接返回!");
            return;
        }
        dealUpdate(iscrdto, catgCode, sendIdxMsg);
    }

    @Override
    public boolean executeUpdateCheck(String fileName, String catgCode) throws BusinessException {
        boolean result = false;
        if (StringUtil.isNotBlank(fileName)) {
            //File file = new File(fileName);
            fileName = getFileName(fileName);
            ImageSampleChapterRespDTO respDTO = new ImageSampleChapterRespDTO();
            respDTO.setFileName(fileName);
            parseIsbn(respDTO);
            return doCheck(respDTO, catgCode);
        }
        return result;
    }
    
    
    private String getFileName(String fileName){
        if(fileName.indexOf("\\") != -1){
            return fileName.substring(fileName.lastIndexOf("\\")+1);
        }else if(fileName.indexOf("/") != -1){
            return fileName.substring(fileName.lastIndexOf("/")+1);
        }else{
            return fileName;
        }
    }

    /*
     * 
     * parseDataMap：解析数据集。<br/>
     * 
     * 
     * @param map
     * 
     * @return
     * 
     * @since JDK 1.6
     */
    private ImageSampleChapterRespDTO parseDataMap(Map<String, Object> map) {
        String fileName = (String) map.get(KEY_FILE_NAME);
        String uuid = (String) map.get(KEY_MEDIA_UUID);
        ImageSampleChapterRespDTO respDTO = null;
        if (StringUtil.isNotBlank(fileName) && StringUtil.isNotBlank(uuid)) {
            respDTO = new ImageSampleChapterRespDTO();
            respDTO.setMediaUuid(uuid);
            respDTO.setFileName(fileName);
            parseIsbn(respDTO);
        }
        return respDTO;
    }

    private void parseIsbn(ImageSampleChapterRespDTO respDTO) {
        String fileName = respDTO.getFileName();
        Matcher m = PATTERN_01.matcher(fileName);
        if (PATTERN_01.matcher(fileName).matches()) {
            m = PATTERN_01.matcher(fileName);
            // 两位数字+4位数字.文件后缀名。可以直接跟五位ISBN号匹配。 前面2位后面4位的的直接跟ISBN号进行匹配
            if (m.matches()) {
                String group01 = m.group(1);
                String group02 = m.group(2);
                String fileExt = m.group(4);
                respDTO.setFileSuffix(fileExt);
                // 2位数字－4位数字
                if (group01.length() == 2 && group02.length() == 4) {
                    respDTO.setIsbn(group01 + "-" + group02);
                } else if (group02.length() <= 2) {
                    respDTO.setIsbn(group01);
                    respDTO.setSortNo(group02);
                } else if (group02.length() <= 3) {
                    respDTO.setIsbn(group01 + "-" + group02);
                } else {
                    respDTO.setIsbn(group01 + "-" + group02);
                }
            }
        } else if (PATTERN_02.matcher(fileName).matches()) {
            m = PATTERN_02.matcher(fileName);
            if (m.matches()) {
                String group01 = m.group(1);
                String fileExt = m.group(3);
                respDTO.setFileSuffix(fileExt);
                respDTO.setIsbn(group01);
            }
        } else {
            m = PATTERN_FILENAME_COMMON.matcher(fileName);
            if (m.matches()) {
                String group01 = m.group(1);
                String fileExt = m.group(3);
                respDTO.setFileSuffix(fileExt);
                respDTO.setIsbn(group01);
            }
        }
    }

    /*
     * 
     * dealUpdate:处理更新. <br/>
     * 
     * 
     * @param iscrdto
     * 
     * @since JDK 1.6
     */
    private void dealUpdate(ImageSampleChapterRespDTO iscrdto, String catgCode, Boolean sendIdxMsg) {

        GdsSku2PropPropIdxReqDTO reqDTO = new GdsSku2PropPropIdxReqDTO();

        paramNullCheck(iscrdto.getIsbn(), "isbn号为空");

        // 设置5位ISBN属性ID。
        reqDTO.setPropId(FIVE_ISBN_PROP_ID);
        // 设置ISBN属性值。
        reqDTO.setPropValue(iscrdto.getIsbn());
        reqDTO.setPageSize(100);
        reqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
        PageResponseDTO<GdsSkuInfoRespDTO> page = gdsSkuInfoQuerySV.queryGdsSkuInfoPagingByProp(reqDTO);

        List<GdsSkuInfoRespDTO> result = page.getResult();

        int affectedRecords = 0;

        if (CollectionUtils.isNotEmpty(result)) {
            // 图片类型执行图片新增或者更新操作
            if (GdsUtils.isImage(iscrdto.getFileSuffix())) {
                if (StringUtil.isBlank(iscrdto.getMediaUuid())) {
                    LogUtil.warn(MODULE, "图片UUID为空，不执行更新操作!ImageSampleChapterRespDTO:" + ToStringBuilder.reflectionToString(iscrdto));
                    return;
                }
                for (GdsSkuInfoRespDTO dto : result) {
                    List<GdsRefreshReq> lst = new ArrayList<>();
                    if (isAllowUpdate(catgCode, dto.getMainCatgs())) {
                        updateMediaId(iscrdto, dto);
                        if (Boolean.TRUE == sendIdxMsg) {
                            lst.add(new GdsRefreshReq(dto.getGdsId(), dto.getCatlogId(), dto.getId()));
                            GdsUtils.sendGdsIndexMsg(null, "T_GDS_INFO", MODULE, dto.getGdsId(), dto.getCatlogId());
                        }
                        ++affectedRecords;
                    }
                    if(CollectionUtils.isNotEmpty(lst)){
                        ThreadPoolExecutorUtil.commitTask(new GdsInfoRefreshTask(lst));
                    }
                }

                if (affectedRecords < 1) {
                    throw new BusinessException(PmphGdsDataImportErrorConstants.ImageSampleChapterImportError.ERROR_GOODS_IMAGESAMPLECHAPTERIMPORT, new String[] { iscrdto.getFileName() });
                }
            } else if (GdsUtils.isPdf(iscrdto.getFileSuffix())) {
                if (StringUtil.isBlank(iscrdto.getMediaUuid())) {
                    LogUtil.warn(MODULE, "样章UUID为空，不执行更新操作！ImageSampleChapterRespDTO:" + ToStringBuilder.reflectionToString(iscrdto));
                    return;
                }
                GdsPropRespDTO propRespDTO = getTryingReadOnlineProp();
                // PDF类型执行样章新增或者更新操作。
                for (GdsSkuInfoRespDTO dto : result) {
                    if (isAllowUpdate(catgCode, dto.getMainCatgs())) {
                        updateSampleChapter(iscrdto, dto, propRespDTO);
                        if (Boolean.TRUE == sendIdxMsg) {
                            GdsUtils.sendGdsIndexMsg(null, "T_GDS_INFO", MODULE, dto.getGdsId(), dto.getCatlogId());
                        }
                        ++affectedRecords;
                    }
                }
                if (affectedRecords < 1) {
                    throw new BusinessException(PmphGdsDataImportErrorConstants.ImageSampleChapterImportError.ERROR_GOODS_IMAGESAMPLECHAPTERIMPORT, new String[] { iscrdto.getFileName() });
                }
            }

        } else {
            LogUtil.warn(MODULE, iscrdto.getFileName() + "匹配不到目标分类为" + catgCode + "商品,略过更新!");
            try {
                LogUtil.info(MODULE, "清理mediaUuid为:" + iscrdto.getMediaUuid() + "的已上传图片信息");
                throw new BusinessException(PmphGdsDataImportErrorConstants.ImageSampleChapterImportError.ERROR_GOODS_IMAGESAMPLECHAPTERIMPORT, new String[] { iscrdto.getFileName() });
            } finally {
                if (StringUtil.isNotBlank(iscrdto.getMediaUuid())) {
                    FileUtil.deleteFile(iscrdto.getMediaUuid());
                }
            }
        }

    }

    /**
     * 
     * doCheck:分类ID.<br/>
     * 
     * @param iscrdto
     * @param catgCode
     * @since JDK 1.6
     */
    private boolean doCheck(ImageSampleChapterRespDTO iscrdto, String catgCode) {
        boolean flag = false;
        paramCheck(new Object[] { iscrdto, catgCode }, new String[] { "iscrdto", "catgCode" });
        paramNullCheck(iscrdto.getIsbn(), "isbn");

        GdsSku2PropPropIdxReqDTO reqDTO = new GdsSku2PropPropIdxReqDTO();
        // 设置5位ISBN属性ID。
        reqDTO.setPropId(FIVE_ISBN_PROP_ID);
        // 设置ISBN属性值。
        reqDTO.setPropValue(iscrdto.getIsbn());
        //reqDTO.setPageSize(Integer.MAX_VALUE);
        reqDTO.setPageSize(100);
        reqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
        PageResponseDTO<GdsSkuInfoRespDTO> page = gdsSkuInfoQuerySV.queryGdsSkuInfoPagingByProp(reqDTO);

        List<GdsSkuInfoRespDTO> result = page.getResult();

        if (CollectionUtils.isNotEmpty(result)) {
            // 图片类型执行图片新增或者更新操作
            if (GdsUtils.isImage(iscrdto.getFileSuffix())) {
                for (GdsSkuInfoRespDTO dto : result) {
                    if (isAllowUpdate(catgCode, dto.getMainCatgs())) {
                        flag = true;
                        break;
                    }
                }
            } else if (GdsUtils.isPdf(iscrdto.getFileSuffix())) {
                // PDF类型执行样章新增或者更新操作。
                for (GdsSkuInfoRespDTO dto : result) {
                    if (isAllowUpdate(catgCode, dto.getMainCatgs())) {
                        flag = true;
                        break;
                    }
                }
            }

        }
        return flag;
    }

    /*
     * 
     * updateMedia:更新图片。<br/>
     * 
     * 
     * @param iscrdto
     * 
     * @param skuInfoRespDTO
     * 
     * @since JDK 1.6
     */
    private void updateMediaId(ImageSampleChapterRespDTO iscrdto, GdsSkuInfoRespDTO skuInfoRespDTO) {

        if (StringUtil.isBlank(iscrdto.getSortNo())) {
            // 排序字段为空，则更新主图。
            GdsMediaRespDTO mainPicRespDTO = gdsInfo2MediaSV.queryGdsMainPicByGdsId(skuInfoRespDTO.getGdsId());
            // 主图为空,则新增主图。
            if (null == mainPicRespDTO) {
                createNewGds2Media(iscrdto, skuInfoRespDTO, GdsConstants.GdsMedia.MEDIA_MAINPIC_SORTNO);
            } else {
                updateGds2Media(iscrdto, mainPicRespDTO, skuInfoRespDTO);
            }
        } else {
            // 排序字段不为空，则更新对应图片。先取出原先uuid,更新成功后删除原uuid对应数据。
            int sortNo = Integer.valueOf(iscrdto.getSortNo());
            // 因主图排序为1，则遇到排序号为1略过不处理。
            if (1 == sortNo) {
                return;
            }
            List<GdsGds2MediaRespDTO> mediaList = gdsInfo2MediaSV.queryGds2MediasByGdsId(skuInfoRespDTO.getGdsId());
            // 根据排序号过滤出商品媒体关联关系。
            GdsGds2MediaRespDTO g2mrd = filterGds2MediaBySortNo(mediaList, sortNo);

            if (null == g2mrd) {
                createNewGds2Media(iscrdto, skuInfoRespDTO, sortNo);
            } else {
                updateGds2Media(iscrdto, g2mrd, skuInfoRespDTO);
            }
        }

    }

    /*
     * updateGds2Media:更新媒体关联关系。<br/>
     * 
     * 
     * @param iscrdto
     * 
     * @param mainPicRespDTO
     * 
     * @since JDK 1.6
     */
    private void updateGds2Media(ImageSampleChapterRespDTO iscrdto, GdsMediaRespDTO mainPicRespDTO, GdsSkuInfoRespDTO skuInfoRespDTO) {
        String oldMediaUuid = mainPicRespDTO.getMediaUuid();
        GdsGds2MediaReqDTO g2mReqDTO = new GdsGds2MediaReqDTO();
        ObjectCopyUtil.copyObjValue(mainPicRespDTO, g2mReqDTO, null, true);
        g2mReqDTO.setMediaUuid(iscrdto.getMediaUuid());
        g2mReqDTO.setGdsId(skuInfoRespDTO.getGdsId());
        g2mReqDTO.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        g2mReqDTO.setOldMediaUuid(oldMediaUuid);
        g2mReqDTO.setStaff(GdsUtils.getStaff(PmphGdsDataImportConstants.Commons.STAFF_ID));
        
        GdsGds2MediaReqDTO query = new GdsGds2MediaReqDTO();
        query.setGdsId(skuInfoRespDTO.getGdsId());
        query.setOldMediaUuid(oldMediaUuid);
        gdsInfo2MediaSV.updateGds2Media(g2mReqDTO,query);
        
        // 删除商品主图缓存
        GdsCacheUtil.delCacheItem(GdsConstants.GdsInfoCacheKey.GDS_MAINPIC_CACHE_KEY_PREFIX + skuInfoRespDTO.getGdsId());

        // 更新成功，删除原先图片uuid以释放mongodb图片存储空间。
        /*
         * if(StringUtil.isNotBlank(oldMediaUuid)){ ImageUtil.deleteImage(oldMediaUuid); }
         */
    }

    private void updateGds2Media(ImageSampleChapterRespDTO iscrdto, GdsGds2MediaRespDTO gds2MediaRespDTO, GdsSkuInfoRespDTO skuInfoRespDTO) {
        String oldMediaUuid = gds2MediaRespDTO.getMediaUuid();
        GdsGds2MediaReqDTO g2mReqDTO = new GdsGds2MediaReqDTO();
        ObjectCopyUtil.copyObjValue(gds2MediaRespDTO, g2mReqDTO, null, true);
        g2mReqDTO.setMediaUuid(iscrdto.getMediaUuid());
        g2mReqDTO.setGdsId(skuInfoRespDTO.getGdsId());
        g2mReqDTO.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        g2mReqDTO.setOldMediaUuid(gds2MediaRespDTO.getMediaUuid());
        g2mReqDTO.setStaff(GdsUtils.getStaff(PmphGdsDataImportConstants.Commons.STAFF_ID));
        
        GdsGds2MediaReqDTO query = new GdsGds2MediaReqDTO();
        query.setGdsId(skuInfoRespDTO.getGdsId());
        query.setOldMediaUuid(oldMediaUuid);
        gdsInfo2MediaSV.updateGds2Media(g2mReqDTO,query);
        // 更新成功，删除原先图片uuid以释放mongodb图片存储空间。
        // 因根据UUID删除图片方法只删除mongodb记录，而没有删除物理硬盘图片信息，所以取消下面那行。
        // if(StringUtil.isNotBlank(oldMediaUuid)){
        // ImageUtil.deleteImage(oldMediaUuid);
        // }
    }

    /*
     * createNewGds2Media:新建媒体关联关系. <br/>
     * 
     * 
     * @param iscrdto
     * 
     * @param skuInfoRespDTO
     * 
     * @since JDK 1.6
     */
    private void createNewGds2Media(ImageSampleChapterRespDTO iscrdto, GdsSkuInfoRespDTO skuInfoRespDTO, int sortNo) {
        GdsGds2MediaReqDTO g2mReqDTO = new GdsGds2MediaReqDTO();
        g2mReqDTO.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
        g2mReqDTO.setGdsId(skuInfoRespDTO.getGdsId());
        g2mReqDTO.setShopId(skuInfoRespDTO.getShopId());
        g2mReqDTO.setMediaUuid(iscrdto.getMediaUuid());
        // 设置为直接上传.
        g2mReqDTO.setMediaRtype("2");
        g2mReqDTO.setSortNo(sortNo);
        g2mReqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
        g2mReqDTO.setMediaType(GdsConstants.GdsMedia.MEDIA_TYPE_PIC);
        g2mReqDTO.setStaff(GdsUtils.getStaff(PmphGdsDataImportConstants.Commons.STAFF_ID));
        gdsInfo2MediaSV.saveGds2Media(g2mReqDTO);
    }

    /*
     * 
     * updateSampleChapter:更新样章。
     * 
     * 
     * @param iscrdto
     * 
     * @param skuInfoRespDTO
     * 
     * @since JDK 1.6
     */
    private void updateSampleChapter(ImageSampleChapterRespDTO iscrdto, GdsSkuInfoRespDTO skuInfoRespDTO, GdsPropRespDTO propRespDTO) {
        String oldMediaUuid = null;

        // =============gds2prop========================================
        // String oldMediaUuid = null;
        // 查询商品属性关联关系。
        GdsGds2Prop gds2prop = gdsInfo2PropSV.queryGds2PropModel(skuInfoRespDTO.getGdsId(), TRYING_READ_ONLINE_PDF_PROP_ID);
        Timestamp now = now();
        // 不存在则新增gds2prop
        if (null == gds2prop) {
            gds2prop = new GdsGds2Prop();
            ObjectCopyUtil.copyObjValue(skuInfoRespDTO, gds2prop, null, false);
            ObjectCopyUtil.copyObjValue(propRespDTO, gds2prop, null, false);
            gds2prop.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
            gds2prop.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
            gds2prop.setCreateTime(now);
            gds2prop.setUpdateTime(now);
            gds2prop.setUpdateStaff(gds2prop.getCreateStaff());
            gds2prop.setPropId(TRYING_READ_ONLINE_PDF_PROP_ID);
            gds2prop.setPropValue(iscrdto.getMediaUuid());
            gds2propMapper.insertSelective(gds2prop);

            GdsGds2PropPropIdx gds2PropPropIdxInsert = new GdsGds2PropPropIdx();
            ObjectCopyUtil.copyObjValue(gds2prop, gds2PropPropIdxInsert, null, true);
            gds2PropPropIdxMapper.insertSelective(gds2PropPropIdxInsert);

        } else {
            // 存在则更新gds2prop。
            oldMediaUuid = gds2prop.getPropValue();
            gds2prop.setPropValue(iscrdto.getMediaUuid());
            gds2prop.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
            gds2prop.setUpdateTime(now());

            GdsGds2PropCriteria example1 = new GdsGds2PropCriteria();
            GdsGds2PropCriteria.Criteria c1 = example1.createCriteria();
            c1.andGdsIdEqualTo(gds2prop.getGdsId());
            c1.andPropIdEqualTo(TRYING_READ_ONLINE_PDF_PROP_ID);
            c1.andStatusEqualTo(GdsConstants.Commons.STATUS_VALID);

            gds2propMapper.updateByExampleSelective(gds2prop, example1);

            // 更新gds2PropPropIdx
            GdsGds2PropPropIdx gds2PropPropIdxCon = new GdsGds2PropPropIdx();
            gds2PropPropIdxCon.setGdsId(skuInfoRespDTO.getGdsId());
            gds2PropPropIdxCon.setPropId(TRYING_READ_ONLINE_PDF_PROP_ID);
            GdsGds2PropPropIdx gds2PropPropIdx = gdsInfoIDXSV.queryGds2PropPropIdx(gds2PropPropIdxCon);
            // 不存在索引则新增索引。
            if (null == gds2PropPropIdx) {
                gds2PropPropIdx = new GdsGds2PropPropIdx();
                ObjectCopyUtil.copyObjValue(gds2prop, gds2PropPropIdx, null, true);
                gds2PropPropIdx.setPropValue(iscrdto.getMediaUuid());
                gds2PropPropIdx.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
                gds2PropPropIdx.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
                gds2PropPropIdx.setCreateTime(now());
                gds2PropPropIdx.setUpdateTime(gds2PropPropIdx.getCreateTime());
                gds2PropPropIdxMapper.insertSelective(gds2PropPropIdx);
            } else {
                // 否则更新现有索引表。
                gds2PropPropIdx.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
                gds2PropPropIdx.setUpdateTime(now());
                gds2PropPropIdx.setPropValue(iscrdto.getMediaUuid());
                gdsInfoIDXSV.updateGds2PropPropIdx(gds2PropPropIdx);
            }
        }

        // =============sku2prop========================================
        GdsSku2PropReqDTO gs2prd = new GdsSku2PropReqDTO();
        gs2prd.setSkuId(skuInfoRespDTO.getId());
        gs2prd.setGdsId(skuInfoRespDTO.getGdsId());
        gs2prd.setPropId(TRYING_READ_ONLINE_PDF_PROP_ID);
        GdsSku2Prop gdsSku2Prop = gdsSkuInfo2PropSV.queryGdsSku2Prop(gs2prd);

        // 不存在则新增sku2prop
        if (null == gdsSku2Prop) {
            gdsSku2Prop = new GdsSku2Prop();
            ObjectCopyUtil.copyObjValue(skuInfoRespDTO, gdsSku2Prop, null, false);
            ObjectCopyUtil.copyObjValue(propRespDTO, gdsSku2Prop, null, false);
            gdsSku2Prop.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
            gdsSku2Prop.setCreateTime(now);
            gdsSku2Prop.setUpdateTime(now);
            gdsSku2Prop.setUpdateStaff(gds2prop.getCreateStaff());
            gdsSku2Prop.setPropId(TRYING_READ_ONLINE_PDF_PROP_ID);
            ObjectCopyUtil.copyObjValue(propRespDTO, gdsSku2Prop, null, true);
            gdsSku2Prop.setSkuId(skuInfoRespDTO.getId());
            gdsSku2Prop.setPropValue(iscrdto.getMediaUuid());
            sku2PropMapper.insertSelective(gdsSku2Prop);

            GdsSku2PropPropIdx gdsSku2PropPropIdxInsert = new GdsSku2PropPropIdx();
            ObjectCopyUtil.copyObjValue(gdsSku2Prop, gdsSku2PropPropIdxInsert, null, false);
            sku2PropPropIdxMapper.insertSelective(gdsSku2PropPropIdxInsert);

        } else {
            // 存在则更新。
            gdsSku2Prop.setPropValue(iscrdto.getMediaUuid());
            gdsSku2Prop.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
            gdsSku2Prop.setUpdateTime(now());

            GdsSku2PropCriteria example1 = new GdsSku2PropCriteria();
            GdsSku2PropCriteria.Criteria c1 = example1.createCriteria();
            c1.andSkuIdEqualTo(gdsSku2Prop.getSkuId());
            c1.andGdsIdEqualTo(gdsSku2Prop.getGdsId());
            c1.andPropIdEqualTo(TRYING_READ_ONLINE_PDF_PROP_ID);
            c1.andStatusEqualTo(GdsConstants.Commons.STATUS_VALID);

            sku2PropMapper.updateByExampleSelective(gdsSku2Prop, example1);

            GdsSku2PropPropIdx sku2PropPropIdxCon = new GdsSku2PropPropIdx();
            sku2PropPropIdxCon.setSkuId(skuInfoRespDTO.getId());
            sku2PropPropIdxCon.setGdsId(skuInfoRespDTO.getGdsId());
            sku2PropPropIdxCon.setPropId(TRYING_READ_ONLINE_PDF_PROP_ID);
            GdsSku2PropPropIdx sku2PropPropIdx = gdsInfoIDXSV.queryGdsSku2PropPropIdx(sku2PropPropIdxCon);
            // 不存在索引则新增索引。
            if (null == sku2PropPropIdx) {
                sku2PropPropIdx = new GdsSku2PropPropIdx();
                ObjectCopyUtil.copyObjValue(gdsSku2Prop, sku2PropPropIdx, null, true);
                sku2PropPropIdx.setPropValue(iscrdto.getMediaUuid());
                sku2PropPropIdx.setCreateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
                sku2PropPropIdx.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
                sku2PropPropIdx.setCreateTime(now());
                sku2PropPropIdx.setUpdateTime(sku2PropPropIdx.getCreateTime());
                sku2PropPropIdxMapper.insertSelective(sku2PropPropIdx);
            } else {
                // 否则更新现有索引表。
                sku2PropPropIdx.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
                sku2PropPropIdx.setUpdateTime(now());
                sku2PropPropIdx.setPropValue(iscrdto.getMediaUuid());
                sku2PropPropIdx.setUpdateStaff(PmphGdsDataImportConstants.Commons.STAFF_ID);
                gdsInfoIDXSV.updateGdsSku2PropPropIdx(sku2PropPropIdx);
            }

        }
        // 更新完成删除旧的mediaUuid的数据记录。
        /*
         * if(StringUtil.isNotBlank(oldMediaUuid)){ ImageUtil.deleteImage(oldMediaUuid); }
         */
    }

    /*
     * private List<GdsSku2PropReqDTO> generateGdsSku2PropReqDTOs( List<GdsGds2PropReqDTO>
     * gds2PropReqDTOs) { List<GdsSku2PropReqDTO> gdsSku2PropReqDTOList = new
     * ArrayList<GdsSku2PropReqDTO>(); for (GdsGds2PropReqDTO gdsGds2PropReqDTO : gds2PropReqDTOs) {
     * GdsSku2PropReqDTO gdsSku2PropReqDTO = new GdsSku2PropReqDTO();
     * gdsSku2PropReqDTO.setPropId(gdsGds2PropReqDTO.getPropId());
     * gdsSku2PropReqDTO.setPropValue(gdsGds2PropReqDTO.getPropValue());
     * gdsSku2PropReqDTO.setPropValueId(gdsGds2PropReqDTO.getPropValueId());
     * gdsSku2PropReqDTOList.add(gdsSku2PropReqDTO); } return gdsSku2PropReqDTOList; }
     * 
     * 
     * 
     * private List<GdsGds2PropReqDTO> generateGdsPropReqDTOs(ImageSampleChapterRespDTO iscrdto,
     * GdsSkuInfoRespDTO skuInfoRespDTO){ List<GdsGds2PropReqDTO> lst = new
     * ArrayList<GdsGds2PropReqDTO>(); GdsGds2PropReqDTO dto = new GdsGds2PropReqDTO();
     * dto.setPropId(TRYING_READ_ONLINE_PDF_PROP_ID); ObjectCopyUtil.copyObjValue(skuInfoRespDTO,
     * dto, null, true); dto.setPropValue(iscrdto.getMediaUuid()); lst.add(dto); return lst; }
     */

    /*
     * 
     * filterGds2MediaBySortNo:根据排序号过滤商品图片关联关系。<br/>
     * 
     * 
     * @param lst
     * 
     * @param sortNo
     * 
     * @return
     * 
     * @since JDK 1.6
     */
    private GdsGds2MediaRespDTO filterGds2MediaBySortNo(List<GdsGds2MediaRespDTO> lst, Integer sortNo) {
        if (CollectionUtils.isNotEmpty(lst)) {
            for (GdsGds2MediaRespDTO g2mrt : lst) {
                if (sortNo.intValue() == g2mrt.getSortNo().intValue()) {
                    return g2mrt;
                }
            }
        }
        return null;
    }

    private GdsPropRespDTO getTryingReadOnlineProp() {
        GdsProp gdsProp = gdsPropSV.queryGdsPropByPK(TRYING_READ_ONLINE_PDF_PROP_ID);
        GdsPropRespDTO gdsPropRespDTO = new GdsPropRespDTO();
        ObjectCopyUtil.copyObjValue(gdsProp, gdsPropRespDTO, null, true);
        return gdsPropRespDTO;
    }

    private boolean isAllowUpdate(String sourceCode, String targetCode) {
        GdsCategoryCompareRespDTO compareRespDTO = comapre(sourceCode, targetCode);
        if (null != compareRespDTO) {
            if (GdsCategoryCompareRespDTO.RESULT_EQUAL == compareRespDTO.getResult() || GdsCategoryCompareRespDTO.RESULT_GREAT_THAN == compareRespDTO.getResult()
                    || GdsCategoryCompareRespDTO.RESULT_LESS_THAN == compareRespDTO.getResult()) {
                return true;
            }
        }
        // 电子书数字教材需要作第二重判断.
        if ("1200".equals(sourceCode)) {
            compareRespDTO = comapre("1201", targetCode);
            if (null != compareRespDTO) {
                if (GdsCategoryCompareRespDTO.RESULT_EQUAL == compareRespDTO.getResult() || GdsCategoryCompareRespDTO.RESULT_GREAT_THAN == compareRespDTO.getResult()
                        || GdsCategoryCompareRespDTO.RESULT_LESS_THAN == compareRespDTO.getResult()) {
                    return true;
                }
            }
        }
        return false;
    }

    private GdsCategoryCompareRespDTO comapre(String sourceCode, String targetCode) {
        GdsCategoryCompareReqDTO compareReqDTO = new GdsCategoryCompareReqDTO();
        compareReqDTO.setSourceCode(sourceCode);
        compareReqDTO.setTargetCode(targetCode);
        return gdsCategorySV.executeCompare(compareReqDTO);
    }

    /*
     * public static void main(String[] args) { String fileName = "11111.pdf";
     * 
     * Map<String, Object> map = new HashMap<String, Object>(); map.put(KEY_FILE_NAME, fileName);
     * map.put(KEY_MEDIA_UUID, "fdafdafa"); ImageSampleChapterSVImpl impl = new
     * ImageSampleChapterSVImpl(); ImageSampleChapterRespDTO respDTO = impl.parseDataMap(map);
     * System.out.println(ToStringBuilder.reflectionToString(respDTO));
     * 
     * }
     */
    
}
