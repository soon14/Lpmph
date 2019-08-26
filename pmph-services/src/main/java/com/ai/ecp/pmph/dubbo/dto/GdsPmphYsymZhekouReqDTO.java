package com.ai.ecp.pmph.dubbo.dto;

import com.ai.ecp.server.front.dto.BaseInfo;

import java.io.Serializable;
import java.sql.Timestamp;

public class GdsPmphYsymZhekouReqDTO extends BaseInfo {
    private Long id;

    private Long pastecardid;

    private String cardno;

    private String memo;

    private String prop1;

    private String prop2;

    private String prop3;

    private String prop4;

    private String adduser;

    private Timestamp addtime;

    private String modifyuser;

    private Timestamp modifytime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPastecardid() {
        return pastecardid;
    }

    public void setPastecardid(Long pastecardid) {
        this.pastecardid = pastecardid;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno == null ? null : cardno.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1 == null ? null : prop1.trim();
    }

    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2 == null ? null : prop2.trim();
    }

    public String getProp3() {
        return prop3;
    }

    public void setProp3(String prop3) {
        this.prop3 = prop3 == null ? null : prop3.trim();
    }

    public String getProp4() {
        return prop4;
    }

    public void setProp4(String prop4) {
        this.prop4 = prop4 == null ? null : prop4.trim();
    }

    public String getAdduser() {
        return adduser;
    }

    public void setAdduser(String adduser) {
        this.adduser = adduser == null ? null : adduser.trim();
    }

    public Timestamp getAddtime() {
        return addtime;
    }

    public void setAddtime(Timestamp addtime) {
        this.addtime = addtime;
    }

    public String getModifyuser() {
        return modifyuser;
    }

    public void setModifyuser(String modifyuser) {
        this.modifyuser = modifyuser == null ? null : modifyuser.trim();
    }

    public Timestamp getModifytime() {
        return modifytime;
    }

    public void setModifytime(Timestamp modifytime) {
        this.modifytime = modifytime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pastecardid=").append(pastecardid);
        sb.append(", cardno=").append(cardno);
        sb.append(", memo=").append(memo);
        sb.append(", prop1=").append(prop1);
        sb.append(", prop2=").append(prop2);
        sb.append(", prop3=").append(prop3);
        sb.append(", prop4=").append(prop4);
        sb.append(", adduser=").append(adduser);
        sb.append(", addtime=").append(addtime);
        sb.append(", modifyuser=").append(modifyuser);
        sb.append(", modifytime=").append(modifytime);
        sb.append("]");
        return sb.toString();
    }
}