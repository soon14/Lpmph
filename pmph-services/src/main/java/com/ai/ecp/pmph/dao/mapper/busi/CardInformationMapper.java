package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.CardInformation;
import com.ai.ecp.pmph.dao.model.CardInformationCriteria;

public interface CardInformationMapper {
    Long countByExample(CardInformationCriteria example) throws DataAccessException;

    int deleteByExample(CardInformationCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(Long id) throws DataAccessException;

    int insert(CardInformation record) throws DataAccessException;

    int insertSelective(CardInformation record) throws DataAccessException;

    List<CardInformation> selectByExample(CardInformationCriteria example) throws DataAccessException;

    CardInformation selectByPrimaryKey(Long id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") CardInformation record, @Param("example") CardInformationCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") CardInformation record, @Param("example") CardInformationCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(CardInformation record) throws DataAccessException;

    int updateByPrimaryKey(CardInformation record) throws DataAccessException;
}