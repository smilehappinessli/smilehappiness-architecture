package com.smilehappiness.mock.rest;

import com.alibaba.fastjson.JSONObject;
import com.smilehappiness.mock.model.MockApiEntity;
import com.smilehappiness.mock.service.IMockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 * MockServer注解
 * <p/>
 *
 * @author smilehappiness
 * @date 2021-01-13 13:16
 */
@Api(value = "MockServerController", tags = "MockServerController服务")
@RestController
@RequestMapping("/mock")
public class MockServerController {

    @Resource
    private IMockService mockService;

    /**
     * <p>
     * 获取所有的mock接口
     * <p/>
     *
     * @param httpServletRequest
     * @return com.alibaba.fastjson.JSONObject
     * @date 2021-01-13 15:16
     */
    @ApiOperation(notes = "获取所有的mock接口", value = "queryAllMock")
    @GetMapping("/queryAllMock")
    public JSONObject queryAllMock(HttpServletRequest httpServletRequest) {
        String uri = httpServletRequest.getRequestURI();
        JSONObject param = JSONObject.parseObject(getSubscribeJson(httpServletRequest));
        return JSONObject.parseObject(mockService.queryMockResponse(uri, param));
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
    @ApiOperation(notes = "获取所有的mock接口列表", value = "queryAllMockList")
    @GetMapping("/queryAllMockList")
    public List<MockApiEntity> queryAllMockList() {
        return mockService.queryAllMockList();
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
    @ApiOperation(notes = "获取所有的mock接口数量", value = "countMockApi")
    @GetMapping("/countMockApi")
    public Integer countMockApi() {
        return mockService.countMockApi();
    }

    /**
     * <p>
     * 通过url，获取url相关的mock接口数量
     * <p/>
     *
     * @param url
     * @return java.lang.Integer
     * @date 2021-01-13 15:41
     */
    @ApiOperation(notes = "通过url，获取url相关的mock接口数量", value = "countMockApiByUrl")
    @GetMapping("/countMockApiByUrl")
    public Integer countMockApiByUrl(@RequestParam("url") String url) {
        return mockService.countMockApiByUrl(url);
    }

    /**
     * 从request中获取json
     *
     * @param request
     * @return
     */
    private String getSubscribeJson(HttpServletRequest request) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
