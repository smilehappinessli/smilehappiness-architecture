package cn.smilehappiness.common.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * Paging request basic parameters -mybatis-plus
 * <p/>
 *
 * @author
 * @Date 2021/10/10 9:23
 */
@ApiModel(value = "Paging request basic parameter ", description=" Paging request basic parameter ")
public abstract class PageRequestDto<T> extends Page<T> {

    private static final long serialVersionUID = -8824017318196056592L;

    /**
     * The number of displayed items per page is 10 by default. You can directly use the properties of the parent class 
     */
    @Deprecated
    @ApiModelProperty(value = "Number of displayed items per page, default  10")
    protected long size = 10;

    /**
     * Current page - the default is 1. You can directly use the properties of the parent class 
     */
    @Deprecated
    @ApiModelProperty(value = "Current Page - Default 1")
    protected long current = 1;

    /**
     * Sort type (true indicates ascending order, false indicates descending order ）
     */
    @ApiModelProperty(value = "Sort type (true indicates ascending order, false indicates descending order ）")
    private boolean isAsc;

    /**
     * Sort fields, if there are more than one, separated by commas 
     */
    @ApiModelProperty(value = "Sort fields, if there are more than one, separated by commas ")
    private String sortFields;

    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        this.sortFields = sortFields;
    }
}
