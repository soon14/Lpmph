/** 
 * File Name:DemoReq.java 
 * Date:2016-2-22下午6:52:57 
 * 
 */ 
package com.ai.ecp.app.req;

import com.ailk.butterfly.app.model.IBody;

/**
 * Project Name:ecp-web-mall <br>
 * Description: 用户修改密码入参<br>
 * Date:2016-2-22下午6:52:57  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class Pmphstaff004Req extends IBody {
    
    private String oldPwd;
    
    private String newPwd;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

}

