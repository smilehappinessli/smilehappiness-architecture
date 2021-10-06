package com.smilehappiness.mock.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smilehappiness.mock.config.SnowflakeConfig;
import com.smilehappiness.mock.mapper.IMockApiMapper;
import com.smilehappiness.mock.model.MockApiEntity;
import com.smilehappiness.mock.service.IMockService;
import com.smilehappiness.utils.FreeMarkUtils;
import com.smilehappiness.utils.JsonParseUtils;
import com.smilehappiness.utils.LogicUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("feignMockServer")
public class MockServerImpl implements IMockService {

    @Resource
    private IMockApiMapper mockApiMapper;

    @Resource
    private FreeMarkUtils freeMarkUtils;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private SnowflakeConfig snowflakeConfig;

    /****
     *
     * 数据库中支持两种模式
     *
     * 1. 自定义返回
     *
     * 2. 自定义freeMark返回
     *
     */
    @Override
    public String queryMockResponse(String feignValue, String url, String methodParam) {
        String sql = "SELECT\n" +
                "\tmock.feign_value AS feignValue,\n" +
                "\tmock.url AS url,\n" +
                "\tmock.response_body AS responseBody,\n" +
                "\tmock.template_engine AS templateEnginee \n" +
                "FROM\n" +
                "\tmock_api mock \n" +
                "WHERE\n" +
                "\tmock.is_delete = 0 \n" +
                "\tAND feign_value =? \n" +
                "\tAND url =? \n" +
                "ORDER BY\n" +
                "\tid ASC";
        Map<String, Object> mockServerResponse = jdbcTemplate.queryForMap(sql, feignValue, url);
        return (String) mockServerResponse.get("responseBody");
    }

    @Override
    public String queryMockResponse(String url, JSONObject requestParam) {
        MockApiEntity mockApi = getMockEntity(url, requestParam);
        if (mockApi == null) {
            return "";
        }
        Map<String, Object> templateMap = new HashMap<>(16);
        if (mockApi.getVarMap() != null) {
            templateMap.putAll(mockApi.getVarMap());
        }
        /**根据规则表达路由,默认第一条*/
        templateMap.put("random1", "" + snowflakeConfig.snowflakeId());
        templateMap.put("random2", "" + snowflakeConfig.snowflakeId());
        templateMap.put("random3", "" + snowflakeConfig.snowflakeId());
        return freeMarkUtils.renderByStringTemplate(templateMap, mockApi.getResponseBody());
    }

    private MockApiEntity getMockEntity(String url, JSONObject requestParam) {
        MockApiEntity mockApi = null;

        /***通过url查询组**/
        for (MockApiEntity mock : mockApiMapper.queryMockApi(url)) {
            /**
             * 变量集
             * featureCode=R#featureCode#,serviceProviderCode=R#serviceProviderCode#"
             *
             */
            String varList = mock.getVarList();
            Map<String, String> logicMap = new HashMap<>(16);
            if (StringUtils.isNotBlank(varList)) {
                for (String tempRule : varList.split(",")) {
                    String varName = tempRule.split("=")[0];
                    String varExpress = tempRule.split("=")[1];
                    String varValue = JsonParseUtils.parseValueToString(varExpress, "", requestParam);
                    logicMap.put(varName, varValue);
                    logicMap.put(varName + "_size", "0");
                    if (StringUtils.isNotBlank(varValue)) {
                        logicMap.put(varName + "_size", "" + varValue.length());
                    }
                }
            }

            /**
             *  规则集
             *  featureCode=gs_vehicle_info&&serviceProviderCode=sg_order_submit
             */
            String rule_str = mock.getRuleList();
            if (StringUtils.isNotBlank(rule_str)) {
                /**命中则返回*/
                for (String tempRuleExpress : rule_str.split(",")) {
                    if (LogicUtils.XOR(tempRuleExpress, logicMap)) {
                        mock.setVarMap(logicMap);
                        return mock;
                    }
                }

            }
            mockApi = mock;
        }
        return mockApi;
    }

    /**
     * <p>
     * 获取所有的mock接口数量
     * <p/>
     *
     * @param
     * @return java.lang.Integer
     * @date 2021-01-13 15:41
     */
    @Override
    public Integer countMockApi() {
        return mockApiMapper.countMockApi();
    }

    /**
     * <p>
     * 通过url，获取url相关的mock接口数量
     * <p/>
     *
     * @param
     * @return java.lang.Integer
     * @date 2021-01-13 15:41
     */
    @Override
    public Integer countMockApiByUrl(String url) {
        return mockApiMapper.countMockApiByUrl(url);
    }

    /**
     * <p>
     * 获取所有的mock接口列表
     * <p/>
     *
     * @param
     * @return java.util.List<com.smilehappiness.mock.model.MockApiEntity>
     * @date 2021-01-13 15:55
     */
    @Override
    public List<MockApiEntity> queryAllMockList() {
        return mockApiMapper.queryAllMockList();
    }

    public static void main(String[] args) {
//string1 = '@ad&*jfad张132（www）。。。'
        String p = "@ad&*jfad张132（www）。。。地址是123号*地址吗?<brunk>神奇的地址333";
        System.out.println(p.replaceAll("[^\u4e00-\u9fa5^a-z^A-Z^0-9]", ""));

    }
}
