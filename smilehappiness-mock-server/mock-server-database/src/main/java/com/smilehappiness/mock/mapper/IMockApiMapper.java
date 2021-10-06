package com.smilehappiness.mock.mapper;

import com.smilehappiness.mock.model.MockApiEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 */
public interface IMockApiMapper {


    List<MockApiEntity> queryMockApi(@Param("url") String url);

    Integer countMockApiByUrl(@Param("url") String url);

    /**
     * <p>
     * 获取所有的mock接口数量
     * <p/>
     *
     * @param
     * @return java.lang.Integer
     * @date 2021-01-13 15:41
     */
    @Select("SELECT count(1) from mock_api d where d.is_delete = 0")
    Integer countMockApi();

    /**
     * <p>
     * 获取所有的mock接口列表
     * <p/>
     *
     * @param
     * @return java.util.List<com.smilehappiness.mock.model.MockApiEntity>
     * @date 2021-01-13 15:55
     */
    @Select("SELECT feign_value AS feignValue,url AS url,response_body AS responseBody,template_engine AS templateEnginee from mock_api d where d.is_delete = 0")
    List<MockApiEntity> queryAllMockList();
}

