package cn.smilehappiness.cache.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import cn.smilehappiness.exception.enums.BaseExceptionEnum;
import cn.smilehappiness.exception.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Set;
import java.util.TreeMap;

/**
 * Weight removal tool 
 * Created by lzh
 * createdTime: 2022/7/6 15:32
 */
@Slf4j
@Component
public class ReqDedupUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @param reqJSON The parameters of the request, usually JSON
     * @return MD5 summary of removal parameters 
     */
    public String dedupParamMD5(String reqJSON) {
        TreeMap paramTreeMap = JSON.parseObject(reqJSON, TreeMap.class);
        String paramTreeMapJSON = JSON.toJSONString(paramTreeMap);
        return jdkMD5(paramTreeMapJSON);
    }

    private static String jdkMD5(String src) {
        String res = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdBytes = messageDigest.digest(src.getBytes());
            res = DatatypeConverter.printHexBinary(mdBytes);
        } catch (Exception e) {
            log.error("", e);
        }
        return res;
    }

    public void checkReqDup(Object object, Long userId, String method, Long expireTime, BaseExceptionEnum baseExceptionEnum, String... excludeKeys) {
        //Filter Fields 
        String reqJson = objectToJson(object, excludeKeys);
        String dedupMD5 = dedupParamMD5(reqJson);//Calculate the request parameter summary, in which the interference of the request time is eliminated 
        String KEY = "dedup:U=" + userId + "M=" + method + "P=" + dedupMD5;
        long expireAt = System.currentTimeMillis() + expireTime;
        String val = "expireAt@" + expireAt;
        //Direct SETNX does not support with expiration time, so setting+expiration is not an atomic operation. In extreme cases, it may not expire if it is set. Later, the same request may be mistaken for the need to de-duplicate. Therefore, the underlying API is used here to ensure that SETNX+expiration time is an atomic operation 
        Boolean firstSet = redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set(KEY.getBytes(), val.getBytes(), Expiration.milliseconds(expireTime),
                RedisStringCommands.SetOption.SET_IF_ABSENT));
        if (firstSet != null && firstSet) {
            //Set successfully, indicating non-repeated request 
        } else {
            //Setting failed, indicating that the request is repeated 
            throw new BusinessException(baseExceptionEnum);
        }
    }


    public String objectToJson(Object obj, String[] excludeKeys) {
        //Attribute Filter Object 
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        //Attributes exclude collections, emphasizing that some attributes do not need or must not be serialized 
        Set<String> excludes = filter.getExcludes();
        //Attributes contain collections. It is emphasized that only some attributes need to be serialized. Which one to use depends on the actual situation. Here I use the former 
        //Set<String> includes = filter.getIncludes();
        //Exclude properties that do not need serialization 
        if (excludeKeys != null && excludeKeys.length > 0) {
            for (String string : excludeKeys) {
                excludes.add(string);
            }
        }
        //Call the method of fastJson, and the object is transferred to json,
        //Parameter 1: object to be serialized 
        //Parameter 2: Filter for filtering attributes 
        //Parameter 3: Close circular reference. If this is not added, the page cannot display duplicate attribute values 
        String string = JSON.toJSONString(obj, filter, SerializerFeature.DisableCircularReferenceDetect);
        return string;
    }

}
