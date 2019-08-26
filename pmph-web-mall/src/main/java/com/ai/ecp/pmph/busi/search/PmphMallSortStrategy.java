package com.ai.ecp.pmph.busi.search;

import com.ai.ecp.busi.search.ISortStrategy;
import com.ai.ecp.search.dubbo.search.SortField;
import com.ai.ecp.search.dubbo.search.util.ESort;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class PmphMallSortStrategy implements ISortStrategy {
    @Override
    public List<SortField> preScoreSort() {
        List<SortField> sortFieldList=new ArrayList<SortField>();
        //无自定义排序，则第一排序字段为商品类型，目的是纸质书拍前面
        sortFieldList.add(new SortField("gdsTypeId", ESort.ASC));
        return sortFieldList;
    }

    @Override
    public List<SortField> afterScoreSort() {
        return null;
    }
}
