package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.InfOrdMainHis;
import com.ai.ecp.pmph.dao.model.InfOrdMainHisCriteria;

public interface InfOrdMainHisMapper {
    Long countByExample(InfOrdMainHisCriteria example) throws DataAccessException;

    int deleteByExample(InfOrdMainHisCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(String id) throws DataAccessException;

    int insert(InfOrdMainHis record) throws DataAccessException;

    int insertSelective(InfOrdMainHis record) throws DataAccessException;

    List<InfOrdMainHis> selectByExample(InfOrdMainHisCriteria example) throws DataAccessException;

    InfOrdMainHis selectByPrimaryKey(String id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") InfOrdMainHis record, @Param("example") InfOrdMainHisCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") InfOrdMainHis record, @Param("example") InfOrdMainHisCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(InfOrdMainHis record) throws DataAccessException;

    int updateByPrimaryKey(InfOrdMainHis record) throws DataAccessException;

    void insertBatch(List<InfOrdMainHis> recordLst) throws DataAccessException;
}