//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.smilehappiness.common.pagehelper;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>
 * pagehelper bean
 * <p/>
 *
 * @author
 * @Date 2021/10/10 20:02
 */
public class PageBean {

    private Integer pageNumber;
    private Integer pageSize;
    private String orderBy;

    public PageBean() {
    }

    public String toString() {
        return (new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)).toString();
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public String getOrderBy() {
        return this.orderBy;
    }
}
