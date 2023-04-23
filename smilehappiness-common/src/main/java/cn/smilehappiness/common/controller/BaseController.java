package cn.smilehappiness.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.smilehappiness.common.enums.ResultCodeEnum;
import cn.smilehappiness.common.page.PageResultResponse;
import cn.smilehappiness.common.result.ObjectRestResponse;

/**
 * <p>
 * Basics controller
 * <p/>
 *
 * @author
 * @Date 2021/10/10 11:20
 */
public abstract class BaseController<T> {

    protected PageResultResponse<T> toPageResultResponse(IPage<T> iPage) {
        PageResultResponse<T> pageResultResponse = new PageResultResponse<>();
        pageResultResponse.setDataList(iPage.getRecords());
        pageResultResponse.setPageNumber(iPage.getCurrent());
        pageResultResponse.setPageSize(iPage.getSize());
        pageResultResponse.setPages(iPage.getPages());
        pageResultResponse.setTotal(iPage.getTotal());
        return pageResultResponse;
    }

    public ObjectRestResponse<String> success() {
        ObjectRestResponse<String> objectRestResponse = new ObjectRestResponse<>();
        objectRestResponse.setCode(ResultCodeEnum.OK.getCode());
        return objectRestResponse;
    }

    public ObjectRestResponse<PageResultResponse<T>> success(PageResultResponse<T> data) {
        ObjectRestResponse<PageResultResponse<T>> objectRestResponse = new ObjectRestResponse<>();
        objectRestResponse.setCode(ResultCodeEnum.OK.getCode());
        objectRestResponse.setResult(data);
        return objectRestResponse;
    }

    public ObjectRestResponse<String> fail() {
        ObjectRestResponse<String> objectRestResponse = new ObjectRestResponse<>();
        objectRestResponse.setCode(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode());
        return objectRestResponse;
    }

    public ObjectRestResponse<PageResultResponse<T>> fail(PageResultResponse<T> data) {
        ObjectRestResponse<PageResultResponse<T>> objectRestResponse = new ObjectRestResponse<>();
        objectRestResponse.setCode(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode());
        objectRestResponse.setResult(data);
        return objectRestResponse;
    }

}
