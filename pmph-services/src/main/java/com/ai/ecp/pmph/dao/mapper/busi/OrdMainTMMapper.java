package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.OrdMainTM;
import com.ai.ecp.pmph.dao.model.OrdMainTMCriteria;

public interface OrdMainTMMapper {
    Long countByExample(OrdMainTMCriteria example) throws DataAccessException;

    int deleteByExample(OrdMainTMCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(String id) throws DataAccessException;

    int insert(OrdMainTM record) throws DataAccessException;

    int insertSelective(OrdMainTM record) throws DataAccessException;

    List<OrdMainTM> selectByExample(OrdMainTMCriteria example) throws DataAccessException;

    OrdMainTM selectByPrimaryKey(String id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") OrdMainTM record, @Param("example") OrdMainTMCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") OrdMainTM record, @Param("example") OrdMainTMCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(OrdMainTM record) throws DataAccessException;

    int updateByPrimaryKey(OrdMainTM record) throws DataAccessException;
}