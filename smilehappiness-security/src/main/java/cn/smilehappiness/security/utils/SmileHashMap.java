package cn.smilehappiness.security.utils;

import cn.smilehappiness.security.constant.SmileConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * <p>
 * str transfer
 * <p/>
 *
 * @author
 * @Date 2023/3/7 10:39
 */
public class SmileHashMap extends HashMap<String, String> {

    public SmileHashMap() {
        super();
    }

    public String put(String key, Object value) {
        String strValue;

        if (value == null) {
            strValue = null;
        } else if (value instanceof String) {
            strValue = (String) value;
        } else if (value instanceof Integer) {
            strValue = ((Integer) value).toString();
        } else if (value instanceof Long) {
            strValue = ((Long) value).toString();
        } else if (value instanceof Float) {
            strValue = ((Float) value).toString();
        } else if (value instanceof Double) {
            strValue = ((Double) value).toString();
        } else if (value instanceof Boolean) {
            strValue = ((Boolean) value).toString();
        } else if (value instanceof Date) {
            DateFormat format = new SimpleDateFormat(SmileConstants.DATE_TIME_FORMAT);
            format.setTimeZone(TimeZone.getTimeZone(SmileConstants.DATE_TIMEZONE));
            strValue = format.format((Date) value);
        } else {
            strValue = value.toString();
        }

        return this.put(key, strValue);
    }

    @Override
    public String put(String key, String value) {
        if (SecurityStringUtil.areNotEmpty(key, value)) {
            return super.put(key, value);
        } else {
            return null;
        }
    }

}
