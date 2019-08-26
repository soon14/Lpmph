/**
 * 
 */
package com.ai.ecp.pmph.service.busi.impl;

import javax.annotation.Resource;

import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.pmph.dao.mapper.busi.InfOrdMainMapper;
import com.ai.ecp.pmph.dao.model.InfOrdMain;
import com.ai.ecp.pmph.dao.model.InfOrdMainCriteria;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdInfMainSV;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;

/**
 * 
 * Project Name:ecp-services-order <br>
 * Description: <br>
 * Date:2015年8月10日下午2:44:54 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class OrdInfMainSVImpl implements IOrdInfMainSV {

    @Resource
    private InfOrdMainMapper infOrdMainMapper;
    @Resource    
    private IGdsInterfaceGdsRSV gdsInterfaceGdsRSV;
    @Resource
    private ICustManageRSV  custManageRSV;

    private static final String MODULE = OrdInfMainSVImpl.class.getName();

    @Override
    public void saveOrdInfMain(InfOrdMain infOrdMain) {
        Long StaffId=infOrdMain.getStaffId();
        if(StaffId!=null){
	        CustInfoResDTO custInfoResDTO = this.custManageRSV.findCustInfoById(StaffId);
	        if (custInfoResDTO != null) {
	            infOrdMain.setStaffCode(custInfoResDTO.getStaffCode());
	            infOrdMain.setStaffName(custInfoResDTO.getCustName());
	        }
        }
        this.infOrdMainMapper.insert(infOrdMain);
    }

	@Override
	public long queryOrdInfMainNumByOrderId(String orderId) {
		// TODO Auto-generated method stub
		InfOrdMainCriteria criteria = new InfOrdMainCriteria();
		criteria.createCriteria().andIdEqualTo(orderId);
		long num = this.infOrdMainMapper.countByExample(criteria);
		return num;
	}
    
    
}
