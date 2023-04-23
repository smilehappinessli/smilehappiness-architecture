package cn.smilehappiness.common.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * FastJsonConfiguration class 
 * org.springframework.http.converter.json.MappingJackson2HttpMessageConverter transfer into com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter
 * <p/>
 *
 * @author
 * @Date 2021/12/12 14:52
 */
@Configuration
public class FastJsonUnifyConfig {

    /**
     * <p>
     * http、feignCall, the serialization mechanism is changed to FastJson (Feign interface call, ordinary RestController is changed to FastJson serialization ）
     * <p>
     * QuoteFieldNames	Whether to use double quotation marks when outputting key, the default is true
     * UseSingleQuotes	Use single quotation marks instead of double quotation marks. The default is false
     * WriteMapNullValue	Whether to output fields with null values, the default is false
     * WriteEnumUsingToString	EnumOutput name () or original, the default is false
     * WriteEnumUsingName	enumThe value is serialized as its Name, and the default is true
     * UseISO8601DateFormat	DateUse ISO8601 format for output, the default is false
     * WriteNullListAsEmpty	ListIf the field is null, the output is [] instead of null
     * WriteNullStringAsEmpty	If the character type field is null, the output is "" instead of null
     * WriteNullNumberAsZero	If the numeric field is null, the output is 0 instead of null
     * WriteNullBooleanAsFalse	BooleanIf the field is null, the output is false instead of null
     * SkipTransientField	If it is true, the Field corresponding to the Get method in the class is transient, which will be ignored during serialization. Default is true
     * SortField	Output after sorting by field name. Default is false
     * WriteTabAsSpecial	Make  t escape output. The default is false. Not recommended 
     * PrettyFormat	Whether the result is formatted. The default value is false. Not recommended 
     * WriteClassName	Type information is written during serialization. The default is false. Deserialization is not recommended 
     * DisableCircularReferenceDetect	Eliminate the problem of circular reference to the same object. The default value is false. Not recommended 
     * WriteSlashAsSpecial	Escaping slash '/' is not recommended 
     * BrowserCompatible	Serialize all Chinese characters into/uxxx format. The number of bytes will be more, but it is compatible with IE 6. The default is false. It is not recommended 
     * WriteDateUseDateFormat	The global modification date format is false by default. Not recommended 
     * DisableCheckSpecialChar	If there are special characters in the string attribute of an object, such as double quotation marks, it will be converted to json with a backslash transition character. If you do not need to escape, you can use this property. The default is false. Not recommended 
     * NotWriteRootClassName	Meaning not recommended 
     * BeanToArray	Converting objects to array output is not recommended 
     * WriteNonStringKeyAsString	Writing the attribute key as String is not recommended 
     * NotWriteDefaultValue	Don't set default value Not recommended 
     * BrowserSecure		Not recommended 
     * IgnoreNonFieldGetter	Ignoring properties without getter methods is not recommended 
     * WriteNonStringValueAsString	Fields that are not String are written as String
     * IgnoreErrorGetter	Ignore properties with errors in getter methods 
     * WriteBigDecimalAsPlain	Large numbers written as text 
     * MapSortField	Fields are sorted according to TreeMap, default false
     * <p/>
     *
     * @param
     * @return org.springframework.boot.autoconfigure.http.HttpMessageConverters
     * @Date 2021/12/12 14:58
     */
    @Bean
    @Primary
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                //Whether the result is formatted, the default is false
                SerializerFeature.PrettyFormat,
                //Large numbers written as text 
                SerializerFeature.WriteBigDecimalAsPlain,

                // Whether to output a field with a null value. The default value is false. We will open it 
                //SerializerFeature.WriteMapNullValue,
                // Output the field null value of the Collection type field as []
                //SerializerFeature.WriteNullListAsEmpty,
                // Output the null value of the string type field as an empty string (sometimes the writing is not rigorous, str? "": "", you will think that true)
                //SerializerFeature.WriteNullStringAsEmpty,
                // If the null value of a numeric type field is output to 0, it will affect the feign call result as a mapper judgment. If null is returned, the value is assigned to 0, mapper= null and mapper != ''， Will be affected 
                //SerializerFeature.WriteNullNumberAsZero,
                //SerializerFeature.WriteDateUseDateFormat,
                //BooleanIf the field is null, the output is false instead of null
                SerializerFeature.WriteNullBooleanAsFalse,
                //Enumeration field output as enumeration value 
                //SerializerFeature.WriteEnumUsingToString,
                // Disable circular references 
                SerializerFeature.DisableCircularReferenceDetect
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        // Add supported MediaTypes; When not added, it defaults to */*, that is, it supports all by default 
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
        return new HttpMessageConverters(fastConverter);
    }

}
