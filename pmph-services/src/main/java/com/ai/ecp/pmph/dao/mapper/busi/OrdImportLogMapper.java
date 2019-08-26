package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.OrdImportLog;
import com.ai.ecp.pmph.dao.model.OrdImportLogCriteria;

public interface OrdImportLogMapper {
    Long countByExample(OrdImportLogCriteria example) throws DataAccessException;

    int deleteByExample(OrdImportLogCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(String id) throws DataAccessException;

    int insert(OrdImportLog record) throws DataAccessException;

    int insertSelective(OrdImportLog record) throws DataAccessException;

    List<OrdImportLog> selectByExample(OrdImportLogCriteria example) throws DataAccessException;

    OrdImportLog selectByPrimaryKey(String id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") OrdImportLog record, @Param("example") OrdImportLogCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") OrdImportLog record, @Param("example") OrdImportLogCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(OrdImportLog record) throws DataAccessException;

    int updateByPrimaryKey(OrdImportLog record) throws DataAccessException;
}