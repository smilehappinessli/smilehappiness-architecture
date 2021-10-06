package com.smilehappiness.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * JSONUtils
 * <p/>
 *
 * @author smilehappiness
 * @Date 2020/8/15 16:59
 */
@Slf4j
public class JsonParseUtils {

    /***
     *
     * R#开头的解析表达式
     * 非数组表达式需要什么点什么
     * R#result.etcCardStateFlag
     * 数组类表达式 ,确认数组中的唯一
     * ETC_PARSE_RISK_MODEL_1.0	etcBillOverdueFlag	R#result.etcBillOverdueFlag
     * @param rule
     * @param defaultValue
     * @param data
     * @return
     */
    public static String parseValueToString(String rule, String defaultValue, JSONObject data) {
        if (StringUtils.isBlank(rule)) {
            return defaultValue;
        }
        rule = rule.trim();
        /** 非通用解析规则 , 自定义解析器 **/
        if (!rule.startsWith("R#")) {

            return defaultValue;
        }
        if (data == null || data.size() == 0) {
            return defaultValue;
        }

        rule = rule.replace("R#", "");
        JSONObject current = data;

        String value = null;
        for (String key : rule.split("\\.")) {
            if (key.contains("[")) {
                /**数组中寻找符合数值**/
                // 获取jsonArray的key
                String array_key = key.split("\\[")[0];
                // jsonArray 对象
                JSONArray array_list = current.getJSONArray(array_key);

                // key=i_cnt_partner_Loan_all_90day;abc=i_cnt_partner_Loan_all_90day
                String array_child_keys = key.split("\\[")[1].replace("]", "");

                for (int i = 0; i < array_list.size(); i++) {
                    JSONObject tmp_json_obj = array_list.getJSONObject(i);
                    // "key": "i_is_per_all_all_courtdefault_all_all",
                    String tmp_child_key = array_child_keys.split("=")[0];
                    // i_is_per_all_all_courtdefault_all_all
                    String tmp_child_Value = array_child_keys.split("=")[1];
                    /** 获得匹配的对象 */
                    if (tmp_json_obj.getString(tmp_child_key).equalsIgnoreCase(tmp_child_Value)) {
                        current = tmp_json_obj;
                        break;
                    }
                }
                continue;
            }

            // 数值
            if (key.contains("#")) {
//                String type = key.split("#")[1];
                key = key.split("#")[0];
                value = current.getString(key);
                if (value == null) {
                    return defaultValue;
                }
                return value;
            }

            current = current.getJSONObject(key);

            /** 对象为空 返回缺省值 **/
            if (current == null) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
