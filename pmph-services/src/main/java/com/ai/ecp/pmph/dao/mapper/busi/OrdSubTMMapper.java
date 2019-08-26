package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.OrdSubTM;
import com.ai.ecp.pmph.dao.model.OrdSubTMCriteria;

public interface OrdSubTMMapper {
    Long countByExample(OrdSubTMCriteria example) throws DataAccessException;

    int deleteByExample(OrdSubTMCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(String id) throws DataAccessException;

    int insert(OrdSubTM record) throws DataAccessException;

    int insertSelective(OrdSubTM record) throws DataAccessException;

    List<OrdSubTM> selectByExample(OrdSubTMCriteria example) throws DataAccessException;

    OrdSubTM selectByPrimaryKey(String id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") OrdSubTM record, @Param("example") OrdSubTMCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") OrdSubTM record, @Param("example") OrdSubTMCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(OrdSubTM record) throws DataAccessException;

    int updateByPrimaryKey(OrdSubTM record) throws DataAccessException;
}