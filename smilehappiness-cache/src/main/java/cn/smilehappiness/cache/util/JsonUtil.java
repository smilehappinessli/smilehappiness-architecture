package cn.smilehappiness.cache.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * JSONConversion tool class 
 * <p/>
 *
 * @author
 * @Date 2021/4/18 22:44
 */
public class JsonUtil {
    private static Logger logger =  LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    static {
        //Whether the unknown property throws an exception 
        OBJECTMAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Get the Collection Type of the generic type; For single-column collection, an element type is passed in; In the case of double-column collection, key and value element types are passed in respectively 
     * @param collectionClass generic  Collection
     * @param elementClasses Element class 
     * @return JAVAtype 
     */
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECTMAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static String entity2Json(Object value){
        try {
            return OBJECTMAPPER.writeValueAsString(value);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public static <T> T json2Entity(String jsonData, Class<T> valueType){
        try {
            return OBJECTMAPPER.readValue(jsonData, valueType);
        } catch (Exception e) {
            logger.error("jsonData:{}",jsonData,e);
        }
        return null;
    }

    public static <T> T json2Entity(String jsonData, JavaType valueType){
        try {
            return OBJECTMAPPER.readValue(jsonData, valueType);
        } catch (Exception e) {
            logger.error("jsonData:{}",jsonData,e);
        }
        return null;
    }

    public static <T> T json2Entity(String jsonData, TypeReference<T> valueType){
        try {
            return OBJECTMAPPER.readValue(jsonData, valueType);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public static <T> ArrayList<T> json2List(String json, Class<T> clazz) {
        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        try {
            return OBJECTMAPPER.readValue(json, javaType);
        } catch (Exception e) {
            logger.error("json:{}",json,e);
        }
        return null;
    }

    public static Object json2Collection(String jsonData,Class<?> collectionClass, Class<?>... elementClasses){
        JavaType javaType = getCollectionType(collectionClass, elementClasses);
        try {
            return OBJECTMAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            logger.error("jsonData:{}",jsonData,e);
        }
        return null;
    }

    /**
     * Deserialize json into objects 
     *
     * @param in        InputStream
     * @param <T>       T Generic tag 
     * @return Bean
     */
    public static <T> T parse(InputStream in) {
        try {
            TypeReference<T> typeReference = new TypeReference<T>() {
            };
            return OBJECTMAPPER.readValue(in, typeReference);
        } catch (Exception e) {
            logger.error("JSON util parse InputStream to bean error.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserialize json into objects 
     *
     * @param in        InputStream
     * @param <T>       T Generic tag 
     * @return Bean
     */
    public static <T> T parse(InputStream in, Class<T> clazz) {
        try {
            return OBJECTMAPPER.readValue(in, clazz);
        } catch (Exception e) {
            logger.error("JSON util parse InputStream to bean error.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserialize json into objects 
     *
     * @param in            InputStream
     * @param typeReference generic types  
     * @param <T>           T Generic tag 
     * @return Bean
     */
    public static <T> T parse(InputStream in, TypeReference<T> typeReference) {
        try {
            return OBJECTMAPPER.readValue(in, typeReference);
        } catch (Exception e) {
            logger.error("JSON util parse InputStream to bean error. bytes => [{}], Type => [{}]", typeReference.getType().getTypeName(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param json the json
     * @param <K> the key type
     * @param <V> the value type
     * @return Map<K, V>
     */
    public static <K, V> Map<K, V> parseToMap(String json) {
        try {
            TypeReference<Map<K, V>> typeReference = new TypeReference<Map<K, V>>() {
            };
            return OBJECTMAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            logger.error("JSON util parse json to bean map error. json => [{}]", json, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseToList(String json) {
        try {
            return OBJECTMAPPER.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (Exception e) {
            logger.error("JSON util parse json to bean list error. bytes => [{}]", json, e);
            throw new RuntimeException(e);
        }
    }
}
