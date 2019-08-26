package com.ai.ecp.pmph.dao.mapper.common;

import com.ai.ecp.pmph.dao.model.ZEResourceActivation;
import com.ai.ecp.pmph.dao.model.ZEResourceActivationCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

public interface ZEResourceActivationMapper {
    Long countByExample(ZEResourceActivationCriteria example) throws DataAccessException;

    int deleteByExample(ZEResourceActivationCriteria example) throws DataAccessException;

    int deleteByPrimaryKey(Long id) throws DataAccessException;

    int insert(ZEResourceActivation record) throws DataAccessException;

    int insertSelective(ZEResourceActivation record) throws DataAccessException;

    List<ZEResourceActivation> selectByExample(ZEResourceActivationCriteria example) throws DataAccessException;

    ZEResourceActivation selectByPrimaryKey(Long id) throws DataAccessException;

    int updateByExampleSelective(@Param("record") ZEResourceActivation record, @Param("example") ZEResourceActivationCriteria example) throws DataAccessException;

    int updateByExample(@Param("record") ZEResourceActivation record, @Param("example") ZEResourceActivationCriteria example) throws DataAccessException;

    int updateByPrimaryKeySelective(ZEResourceActivation record) throws DataAccessException;

    int updateByPrimaryKey(ZEResourceActivation record) throws DataAccessException;
}