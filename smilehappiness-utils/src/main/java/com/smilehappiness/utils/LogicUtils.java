package com.smilehappiness.utils;

import java.math.BigDecimal;
import java.util.Map;

/***
 *
 * XOR 逻辑计算
 * if 逻辑
 * 支持 && || 级联运算
 * 支持 =   注: 表示字符串 equal , 如果是数值计算 ,请用 ==
 * 支持 == 表示数值计算是否相等
 * 支持 >  >=
 * 支持 <  <=  逻辑计算
 *
 */
public class LogicUtils {

    /**
     * 异或逻辑 计算
     */
    public static boolean XOR(String expressValue, Map<String, String> processMap) {
        /**先计算 or 逻辑 **/
        for (String express : expressValue.split("\\|\\|")) {
            /** 最终由 AND 逻辑组成 **/
            if (AND(express, processMap)) {
                /**只要一个true,则为true, or 逻辑 **/
                return true;
            }
        }
        return false;
    }

    /**
     * AND逻辑计算
     **/
    private static boolean AND(String expressValue, Map<String, String> processMap) {
        for (String express : expressValue.split("&&")) {
            /**只要1个false, 都为false */
            if (!unitCal(express, processMap)) {
                return false;
            }
        }
        return true;
    }

    private static boolean unitCal(String expressValue, Map<String, String> processMap) {
        /** 单元运算 **/
        /**暂时只支持 = 运算**/
        /**code=SUCCESS*/

        /** >= 运算 **/
        if (expressValue.indexOf(">") > 0) {
            String reg = ">";
            if (expressValue.indexOf("=") > 0) {
                reg = ">=";
            }


            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            /** >  或  >=计算*/
            if (reg.indexOf("=") > 0 ? compare(actVal, value) >= 0 : compare(actVal, value) > 0) {

                return true;
            }
            return false;
        }

        /** <= 运算 **/
        if (expressValue.indexOf("<") > 0) {
            String reg = "<";
            if (expressValue.indexOf("=") > 0) {
                reg = "<=";
            }

            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            /** <  或  <= 计算*/
            if (reg.indexOf("=") > 0 ? compare(actVal, value) <= 0 : compare(actVal, value) < 0) {
                return true;
            }
            return false;
        }


        /** ! 不等逻辑 ,先执行, 否则会被 == 逻辑截获*/
        if (expressValue.indexOf("!==") > 0) {
            String reg = "!==";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            /** <  或  <= 计算*/
            if (compare(actVal, value) != 0) {
                return true;
            }
            return false;
        }

        /** == 运算  表示数值计算 **/
        if (expressValue.indexOf("==") > 0) {
            String reg = "==";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            /** <  或  <= 计算*/
            if (compare(actVal, value) == 0) {
                return true;
            }
            return false;
        }


        /** ! 不等逻辑*/
        if (expressValue.indexOf("!=") > 0) {
            String reg = "!=";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (!value.equals(actVal)) {
                return true;
            }
            return false;
        }
        /** = 运算 表示字符串是否相同 **/
        if (expressValue.indexOf("=") > 0) {
            String reg = "=";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (value.equals(actVal)) {
                return true;
            }
            return false;
        }


        /** "#eq#" 运算 表示字符串是否相同 **/
        if (expressValue.indexOf("#eq#") > 0) {
            String reg = "#eq#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (value.equals(actVal)) {
                return true;
            }
            return false;
        }

        if (expressValue.indexOf("#!eq#") > 0) {
            String reg = "#!eq#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (!value.equals(actVal)) {
                return true;
            }
            return false;
        }

        /** "#eqI#" 运算 表示字符串是否相同 - 忽略大小写 **/
        if (expressValue.indexOf("#eqI#") > 0) {
            String reg = "#eqI#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (value.equalsIgnoreCase(actVal)) {
                return true;
            }
            return false;
        }

        if (expressValue.indexOf("#!eqI#") > 0) {
            String reg = "#!eqI#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (!value.equalsIgnoreCase(actVal)) {
                return true;
            }
            return false;
        }

        /** "#contains#" 运算 表示字符串是否包含 **/
        if (expressValue.indexOf("#contains#") > 0) {
            String reg = "#contains#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (actVal.contains(value)) {
                return true;
            }
            return false;
        }

        if (expressValue.indexOf("#!contains#") > 0) {
            String reg = "#!contains#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (!actVal.contains(value)) {
                return true;
            }
            return false;
        }

        /** "#startWith#" 运算 表示字符串是否包含 **/
        if (expressValue.indexOf("#startWith#") > 0) {
            String reg = "#startWith#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (actVal.startsWith(value)) {
                return true;
            }
            return false;
        }

        if (expressValue.indexOf("#!startWith#") > 0) {
            String reg = "#!startWith#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (!actVal.startsWith(value)) {
                return true;
            }
            return false;
        }

        /** "#endWith#" 运算 表示字符串是否包含 **/
        if (expressValue.indexOf("#endWith#") > 0) {
            String reg = "#endWith#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (actVal.endsWith(value)) {
                return true;
            }
            return false;
        }

        if (expressValue.indexOf("#!endWith#") > 0) {
            String reg = "#!endWith#";
            String key = getKey(expressValue, reg);
            String value = getValue(expressValue, reg);
            String actVal = getMapValue(key, processMap);
            if (!actVal.endsWith(value)) {
                return true;
            }
            return false;
        }

        /** "#subStr(1,2)#" 运算 表示字符串是否包含 **/
        if (expressValue.indexOf("#subStr(") > 0) {
            String reg = "#" + expressValue.split("#")[1] + "#";
            String key = expressValue.split("#")[0];
            String value = expressValue.split("#")[2].trim().replaceAll("'", "");
            String actVal = getMapValue(key, processMap);
            /**
             * subStr(4)
             * subStr(1,4)
             * check subStr return Value is not null
             */
            String[] regValue = reg.replaceAll("#", "").replaceAll("subStr\\(", "").replaceAll("\\)", "").split(",");
            if (regValue.length > 1) {
                actVal = subStr(actVal, regValue[0], regValue[1]);
            } else {
                actVal = subStr(actVal, regValue[0]);
            }
            if (actVal.equals(value)) {
                return true;
            }
            return false;
        }

        if (expressValue.indexOf("#!subStr(") > 0) {
            String reg = "#" + expressValue.split("#")[1] + "#";
            String key = expressValue.split("#")[0];
            String value = expressValue.split("#")[2].trim().replaceAll("'", "");
            String actVal = getMapValue(key, processMap);
            /**
             * subStr(4)
             * subStr(1,4)
             * check subStr return Value is not null
             */
            String[] regValue = reg.replaceAll("#", "").replaceAll("!subStr\\(", "").replaceAll("\\)", "").split(",");
            if (regValue.length > 1) {
                actVal = subStr(actVal, regValue[0], regValue[1]);
            } else {
                actVal = subStr(actVal, regValue[0]);
            }
            if (!actVal.equals(value)) {
                return true;
            }
            return false;
        }
        // 默认运算
        return true;
    }


    /**
     * 分割后的key
     */
    private static String getKey(String expressValue, String reg) {
        return expressValue.split(reg)[0].trim();
    }

    /**
     * 分割后的Value
     */
    private static String getValue(String expressValue, String reg) {
        if (expressValue.split(reg).length > 1) {
            return expressValue.split(reg)[1].trim().replaceAll("'", "");
        }
        return "null";
    }

    /**
     * 获取map中的value
     */
    private static String getMapValue(String key, Map<String, String> map) {
        return map.get(key) == null ? "null" : map.get(key);
    }

    private static double compare(String var1, String var2) {
        BigDecimal var1BigDecimal = new BigDecimal(var1);
        BigDecimal var2BigDecimal = new BigDecimal(var2);
        return var1BigDecimal.subtract(var2BigDecimal).doubleValue();
    }

    private static String subStr(String filed, String index) {
        try {
            return filed.substring(Integer.parseInt(index));
        } catch (Exception e) {
            return null;
        }
    }

    private static String subStr(String filed, String index, String end) {
        try {
            return filed.substring(Integer.parseInt(index), Integer.parseInt(end));
        } catch (Exception e) {
            return "";
        }
    }

    public static void printXOR(String logic, Map<String, String> processMap) {
        System.out.println(logic);
        System.out.println(XOR(logic, processMap));
    }

    public static void main(String[] args) {
//        processMap.put("name", "");
//        processMap.put("age", "18");
//        System.out.println(JSONObject.toJSONString(processMap));
//
//        printXOR("age>='18'",processMap);
//
//        printXOR("name2#eq#",processMap);
//        printXOR("name=null",processMap);
//        printXOR("name!=null",processMap);
//        printXOR("name#eq#null",processMap);
//        printXOR("name#eq#'null'",processMap);
//        printXOR("name#eq#'zhangsan'",processMap);
//
//        printXOR("name#eqI#   zhangsan  ",processMap);
//
//        printXOR("name#contains#'San'",processMap);
//
//        printXOR("name#contains#San",processMap);
//
//        printXOR("age!=='18'",processMap);
//        printXOR("age=='18'",processMap);
//
//        printXOR("age!==18",processMap);
//        printXOR("name#startWith#",processMap);
//        printXOR("name#startWith#''",processMap);
    }
}
