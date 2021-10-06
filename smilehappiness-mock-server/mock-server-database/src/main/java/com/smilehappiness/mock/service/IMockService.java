package com.smilehappiness.mock.service;

import com.alibaba.fastjson.JSONObject;
import com.smilehappiness.mock.model.MockApiEntity;

import java.util.List;

public interface IMockService {


    /***
     *
     * mock服务
     *
     * 接口服务即为id
     *
     * 定义返回参数
     *
     * extends 支持 参数匹配
     *
     */
    String queryMockResponse(String feignValue, String url, String methodParam);

    String queryMockResponse(String url, JSONObject requestParam);

    /**
     * <p>
     * 获取所有的mock接口数量
     * <p/>
     *
     * @param
     * @return java.lang.Integer
     * @date 2021-01-13 15:41
     */
    Integer countMockApi();

    /**
     * <p>
     * 通过url，获取url相关的mock接口数量
     * <p/>
     *
     * @param url
     * @return java.lang.Integer
     * @date 2021-01-13 15:41
     */
    Integer countMockApiByUrl(String url);

    /**
     * <p>
     * 获取所有的mock接口列表
     * <p/>
     *
     * @param
     * @return java.util.List<com.smilehappiness.mock.model.MockApiEntity>
     * @date 2021-01-13 15:55
     */
    List<MockApiEntity> queryAllMockList();
}
