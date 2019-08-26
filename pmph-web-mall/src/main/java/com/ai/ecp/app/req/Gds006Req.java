package com.ai.ecp.app.req;

import java.util.List;

import com.ai.ecp.app.req.gds.SearchPropReqInfo;
import com.ailk.butterfly.app.model.IBody;

public class Gds006Req extends IBody {

    private String shopId;

   private String keyword;
   
   private String category;
   
   private int pageSize;
   
   private int pageNumber;
   
   private String field;
   
   private String sort;
   
   private String priceStart;
   
   private String priceEnd;
   
   private String ifSelfOperate;
   
   /**
    * 单个过滤和多个属性过滤都使用属性值组统一处理
    */
   private List<SearchPropReqInfo> propertyGroup;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getKeyword() {
    return keyword;
}

public void setKeyword(String keyWord) {
    this.keyword = keyWord;
}

public String getCategory() {
    return category;
}

public void setCategory(String category) {
    this.category = category;
}

public int getPageSize() {
    return pageSize;
}

public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
}

public int getPageNumber() {
    return pageNumber;
}

public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
}

public String getField() {
    return field;
}

public void setField(String field) {
    this.field = field;
}

public String getSort() {
    return sort;
}

public void setSort(String sort) {
    this.sort = sort;
}

public String getPriceStart() {
	return priceStart;
}

public void setPriceStart(String priceStart) {
	this.priceStart = priceStart;
}

public String getPriceEnd() {
	return priceEnd;
}

public void setPriceEnd(String priceEnd) {
	this.priceEnd = priceEnd;
}



public void setPropertyGroup(List<SearchPropReqInfo> propertyGroup) {
	this.propertyGroup = propertyGroup;
}

public List<SearchPropReqInfo> getPropertyGroup() {
	return propertyGroup;
}

public String getIfSelfOperate() {
	return ifSelfOperate;
}

public void setIfSelfOperate(String ifSelfOperate) {
	this.ifSelfOperate = ifSelfOperate;
}

  
}

