package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.CardBind;
import com.ai.ecp.pmph.dao.model.CardBindCriteria;

public interface CardBindMapper {
    Long countByExample(CardBindCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(Long id) throws DataAccessException;

    int insert(CardBind record) throws DataAccessException;

    int insertSelective(CardBind record) throws DataAccessException;

    List<CardBind> selectByExample(CardBindCriteria example) throws DataAccessException;

    CardBind selectByPrimaryKey(Long id) throws DataAccessException;

    int updateByPrimaryKeySelective(CardBind record) throws DataAccessException;

    int updateByPrimaryKey(CardBind record) throws DataAccessException;
}