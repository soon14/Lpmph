package com.ai.ecp.pmph.dubbo.impl.dataexport;

import com.ai.ecp.pmph.dubbo.interfaces.dataexport.IERPGdsInfoExportRSV;
import com.ai.ecp.pmph.service.busi.interfaces.dataexport.IERPGdsInfoExportSV;
import com.ai.paas.utils.LogUtil;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 */
public class ERPGdsInfoExportRSVImpl implements IERPGdsInfoExportRSV {

    private final String KEY_EXPORTTYPE="exportType";

    @Resource
    private IERPGdsInfoExportSV erpGdsInfoExportSV;

    @Override
    public List<Map<String, Object>> fullImport() {
        return erpGdsInfoExportSV.fullImport();
    }

    @Override
    public List<Map<String, Object>> deltaImport() throws Exception {
        return erpGdsInfoExportSV.deltaImport();
    }

    @Override
    public List<Map<String, Object>> export(Map<String, Object> map) {
        if(map!=null&&map.containsKey(KEY_EXPORTTYPE)){
            if(StringUtils.equalsIgnoreCase("1",String.valueOf(map.get(KEY_EXPORTTYPE)))){
                return this.fullImport();
            }else if(StringUtils.equalsIgnoreCase("2",String.valueOf(map.get(KEY_EXPORTTYPE)))){
                try {
                    return this.deltaImport();
                } catch (Exception e) {
                    LogUtil.error(this.getClass().getName(), "获取商品增量数据失败!",e);
                }
            }
        }
        return null;
    }
}
