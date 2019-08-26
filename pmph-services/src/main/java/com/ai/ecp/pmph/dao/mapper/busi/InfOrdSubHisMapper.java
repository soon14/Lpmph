package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.InfOrdSubHis;
import com.ai.ecp.pmph.dao.model.InfOrdSubHisCriteria;

public interface InfOrdSubHisMapper {
    Long countByExample(InfOrdSubHisCriteria example) throws DataAccessException;

    int deleteByExample(InfOrdSubHisCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(String id) throws DataAccessException;

    int insert(InfOrdSubHis record) throws DataAccessException;

    int insertSelective(InfOrdSubHis record) throws DataAccessException;

    List<InfOrdSubHis> selectByExample(InfOrdSubHisCriteria example) throws DataAccessException;

    InfOrdSubHis selectByPrimaryKey(String id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") InfOrdSubHis record, @Param("example") InfOrdSubHisCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") InfOrdSubHis record, @Param("example") InfOrdSubHisCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(InfOrdSubHis record) throws DataAccessException;

    int updateByPrimaryKey(InfOrdSubHis record) throws DataAccessException;

    void insertBatch(List<InfOrdSubHis> recordLst) throws DataAccessException;
}