//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.smilehappiness.common.pagehelper;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * <p>
 * Paging basic parameters ，pagehelper
 * <p/>
 *
 * @author
 * @Date 2021/10/10 20:43
 */
public class PageQueryRequest<T> implements Serializable {

    private static final long serialVersionUID = -1999757275705498526L;

    private T t;
    private PageBean pageBean;

    public PageQueryRequest() {
    }

    @Override
    public String toString() {
        return (new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)).toString();
    }

    public T getT() {
        return this.t;
    }

    public PageBean getPageBean() {
        return this.pageBean;
    }

    public void setT(T t) {
        this.t = t;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }
}
