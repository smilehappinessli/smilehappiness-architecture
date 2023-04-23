package cn.smilehappiness.common.page;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * Return results by page 
 * <p/>
 *
 * @author
 * @Date 2021/10/9 20:23
 */
@Data
public class PageResultResponse<T> {

    /**
     * Paging data result list 
     */
    private List<T> dataList;
    /**
     * Current Page 
     */
    private long pageNumber;
    /**
     * Number of pages per page 
     */
    private long pageSize;
    /**
     * Total number of current pages 
     */
    private long pages;
    /**
     * Total quantity 
     */
    private long total;

}
