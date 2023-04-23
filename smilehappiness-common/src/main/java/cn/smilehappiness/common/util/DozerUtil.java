package cn.smilehappiness.common.util;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * dozerTools 
 * <p/>
 *
 * @author
 * @Date 2021/10/2 14:56
 */
public class DozerUtil {

    private static Mapper mapper = new DozerBeanMapper();

    public DozerUtil() {
    }

    public static <T> List<T> transForList(List<?> sources, Class<T> clazz) {
        List<T> list = new ArrayList();
        if (sources == null) {
            return list;
        } else {
            Iterator var3 = sources.iterator();

            while (var3.hasNext()) {
                Object o = var3.next();
                T t = transFor(o, clazz);
                list.add(t);
            }

            return list;
        }
    }

    public static void transFor(Object source, Object target) {
        if (source != null && target != null) {
            mapper.map(source, target);
        }
    }

    public static <T> T transFor(Object source, Class<T> target) {
        return source == null ? null : mapper.map(source, target);
    }
}
