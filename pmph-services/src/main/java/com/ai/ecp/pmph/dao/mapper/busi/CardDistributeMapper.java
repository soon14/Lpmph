package com.ai.ecp.pmph.dao.mapper.busi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.ai.ecp.pmph.dao.model.CardDistribute;
import com.ai.ecp.pmph.dao.model.CardDistributeCriteria;

public interface CardDistributeMapper {
    Long countByExample(CardDistributeCriteria example) throws DataAccessException;

    int deleteByExample(CardDistributeCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(Long id) throws DataAccessException;

    int insert(CardDistribute record) throws DataAccessException;

    int insertSelective(CardDistribute record) throws DataAccessException;

    List<CardDistribute> selectByExample(CardDistributeCriteria example) throws DataAccessException;

    CardDistribute selectByPrimaryKey(Long id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") CardDistribute record, @Param("example") CardDistributeCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") CardDistribute record, @Param("example") CardDistributeCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(CardDistribute record) throws DataAccessException;

    int updateByPrimaryKey(CardDistribute record) throws DataAccessException;
}