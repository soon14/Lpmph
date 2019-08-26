package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.CardApplication;
import com.ai.ecp.pmph.dao.model.CardApplicationCriteria;

public interface CardApplicationMapper {
    Long countByExample(CardApplicationCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(Long id) throws DataAccessException;

    int insert(CardApplication record) throws DataAccessException;

    int insertSelective(CardApplication record) throws DataAccessException;

    List<CardApplication> selectByExample(CardApplicationCriteria example) throws DataAccessException;

    CardApplication selectByPrimaryKey(Long id) throws DataAccessException;

    int updateByPrimaryKeySelective(CardApplication record) throws DataAccessException;

    int updateByPrimaryKey(CardApplication record) throws DataAccessException;
}