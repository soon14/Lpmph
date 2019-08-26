package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.CardInfo;
import com.ai.ecp.pmph.dao.model.CardInfoCriteria;

public interface CardInfoMapper {
    Long countByExample(CardInfoCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(String custCardId) throws DataAccessException;

    int insert(CardInfo record) throws DataAccessException;

    int insertSelective(CardInfo record) throws DataAccessException;

    List<CardInfo> selectByExample(CardInfoCriteria example) throws DataAccessException;

    CardInfo selectByPrimaryKey(String custCardId) throws DataAccessException;

    int updateByPrimaryKeySelective(CardInfo record) throws DataAccessException;

    int updateByPrimaryKey(CardInfo record) throws DataAccessException;
}