package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.InfOrdSub;
import com.ai.ecp.pmph.dao.model.InfOrdSubCriteria;

public interface InfOrdSubMapper {
    Long countByExample(InfOrdSubCriteria example) throws DataAccessException;

    int deleteByExample(InfOrdSubCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(String id) throws DataAccessException;

    int insert(InfOrdSub record) throws DataAccessException;

    int insertSelective(InfOrdSub record) throws DataAccessException;

    List<InfOrdSub> selectByExample(InfOrdSubCriteria example) throws DataAccessException;

    InfOrdSub selectByPrimaryKey(String id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") InfOrdSub record, @Param("example") InfOrdSubCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") InfOrdSub record, @Param("example") InfOrdSubCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(InfOrdSub record) throws DataAccessException;

    int updateByPrimaryKey(InfOrdSub record) throws DataAccessException;

    void insertBatch(List<InfOrdSub> recordLst) throws DataAccessException;
}