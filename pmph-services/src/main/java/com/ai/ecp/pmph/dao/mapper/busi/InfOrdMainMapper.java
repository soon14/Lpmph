package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.InfOrdMain;
import com.ai.ecp.pmph.dao.model.InfOrdMainCriteria;

public interface InfOrdMainMapper {
    Long countByExample(InfOrdMainCriteria example) throws DataAccessException;

    int deleteByExample(InfOrdMainCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(String id) throws DataAccessException;

    int insert(InfOrdMain record) throws DataAccessException;

    int insertSelective(InfOrdMain record) throws DataAccessException;

    List<InfOrdMain> selectByExample(InfOrdMainCriteria example) throws DataAccessException;

    InfOrdMain selectByPrimaryKey(String id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") InfOrdMain record, @Param("example") InfOrdMainCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") InfOrdMain record, @Param("example") InfOrdMainCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(InfOrdMain record) throws DataAccessException;

    int updateByPrimaryKey(InfOrdMain record) throws DataAccessException;

    void insertBatch(List<InfOrdMain> recordLst) throws DataAccessException;
}