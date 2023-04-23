//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.smilehappiness.common.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.smilehappiness.common.page.PageResultResponse;
import cn.smilehappiness.common.pagehelper.PageQueryRequest;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * <p>
 * pagehelperPaging tool class 
 * <p/>
 *
 * @author
 * @Date 2021/10/10 19:37
 */
public class PageUtil {

    private static final Integer PAGE_NUM = 1;
    private static final Integer PAGE_SIZE = 10;

    public PageUtil() {
    }

    public static <T> Page<T> startPage(PageQueryRequest<T> request) {
        if (request.getPageBean() == null) {
            return null;
        }

        Integer vPageNum = request.getPageBean().getPageNumber() == null ? PAGE_NUM : request.getPageBean().getPageNumber();
        Integer vPageSize = request.getPageBean().getPageSize() == null ? PAGE_SIZE : request.getPageBean().getPageSize();
        return PageHelper.startPage(vPageNum, vPageSize);
    }

    public static <T> PageResultResponse<T> toPageResponse(List<?> list, Class<T> clz) {
        PageResultResponse<T> pageResultResponse = new PageResultResponse<>();
        if (CollectionUtils.isEmpty(list)) {
            return pageResultResponse;
        }

        List<T> dataList = DozerUtil.transForList(list, clz);
        pageResultResponse.setDataList(dataList);

        if (list instanceof Page) {
            Page page = (Page) list;
            pageResultResponse.setTotal(page.getTotal());
            pageResultResponse.setPageNumber(page.getPageNum());
            pageResultResponse.setPages(page.getPages());
            pageResultResponse.setPageSize(page.getPageSize());
        }

        return pageResultResponse;
    }

    public static <T> PageResultResponse<T> toPageResponse(PageInfo<T> pageInfo) {
        PageResultResponse<T> pageResultResponse = new PageResultResponse<>();
        if (pageInfo == null) {
            return pageResultResponse;
        }

        pageResultResponse.setDataList(pageInfo.getList());
        pageResultResponse.setTotal(pageInfo.getTotal());
        pageResultResponse.setPageNumber(pageInfo.getPageNum());
        pageResultResponse.setPages(pageInfo.getPages());
        pageResultResponse.setPageSize(pageInfo.getPageSize());
        return pageResultResponse;
    }

}
