package cn.smilehappiness.process.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.utils.LogicUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * JSONTools 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 11:04
 */
@Slf4j
public class JSONUtils {


    /**
     * <p>
     * R#Parsing expression at the beginning （ R#result.Field）
     * <p/>
     *
     * @param rule
     * @param defaultValue
     * @param data
     * @return T
     * @Date 2021/11/4 11:07
     */
    public static <T> T parseValue(String rule, T defaultValue, JSONObject data) {
        if (StringUtils.isBlank(rule)) {
            return defaultValue;
        }
        rule = rule.trim();
        /** Non-general parsing rules, custom parser  **/
        if (!rule.startsWith("R#")) {
            return defaultValue;
        }

        //rule example： R#engineLifeCycle.lifeBean#
        rule = rule.replace("R#", "");
        JSONObject current = data;

        T value = null;
        String[] ruleList = rule.split("\\.");
        if (ArrayUtils.isEmpty(ruleList)) {
            return defaultValue;
        }

        for (String key : ruleList) {
            if (key.contains("[")) {
                /**Find matching values in the array **/
                // Get the key
                String arrayKey = key.split("\\[")[0];
                // jsonArray object 
                JSONArray arrayList = current.getJSONArray(arrayKey);
                if (CollectionUtils.isEmpty(arrayList)) {
                    return defaultValue;
                }

                // [key=i_cnt_partner_Loan_all_90day&&abc=i_cnt_partner_Loan_all_90day].kkkk
                String arrayChildKeys = key.split("\\[")[1].replace("]", "");
                for (int i = 0; i < arrayList.size(); i++) {
                    JSONObject tmpJsonObj = arrayList.getJSONObject(i);
                    Map<String, String> processMap = transferMap(tmpJsonObj);
                    if (LogicUtils.XOR(arrayChildKeys, processMap)) {
                        current = tmpJsonObj;
                        break;
                    }
                }
                continue;
            }

            // numerical value 
            if (key.contains("#")) {
                key = key.split("#")[0];
                value = (T) current.get(key);
                if (value == null) {
                    return defaultValue;
                }
                return value;
            }

            current = current.getJSONObject(key);

            /** Return the default value when the object is empty  **/
            if (current == null) {
                return defaultValue;
            }
        }

        return defaultValue;
    }

    /**
     * Convert all keys in json to map
     */

    private static Map<String, String> transferMap(JSONObject jsonObject) {
        Map<String, String> returnMap = new HashMap<>(16);
        if (jsonObject != null) {
            for (JSONObject.Entry<String, Object> entry : jsonObject.entrySet()) {
                returnMap.put(entry.getKey(), entry.getValue() == null ? "null" : entry.getValue().toString());
            }
        }
        return returnMap;
    }


    public static void main(String[] args) {
        //JSONObject param = JSON.parseObject("");
        //System.out.println(parseValue("R#data.data[cardType=22&&cardState=1].cardID#String", "", param));
    }
}
