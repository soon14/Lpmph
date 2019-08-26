package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.CardImportTemp;
import com.ai.ecp.pmph.dao.model.CardImportTempCriteria;

public interface CardImportTempMapper {
    Long countByExample(CardImportTempCriteria example) throws DataAccessException;

    int deleteByExample(CardImportTempCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(Long importId) throws DataAccessException;

    int insert(CardImportTemp record) throws DataAccessException;

    int insertSelective(CardImportTemp record) throws DataAccessException;

    List<CardImportTemp> selectByExample(CardImportTempCriteria example) throws DataAccessException;

    CardImportTemp selectByPrimaryKey(Long importId) throws DataAccessException;

    int updateByExampleSelective(@Param("record") CardImportTemp record, @Param("example") CardImportTempCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") CardImportTemp record, @Param("example") CardImportTempCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(CardImportTemp record) throws DataAccessException;

    int updateByPrimaryKey(CardImportTemp record) throws DataAccessException;
}