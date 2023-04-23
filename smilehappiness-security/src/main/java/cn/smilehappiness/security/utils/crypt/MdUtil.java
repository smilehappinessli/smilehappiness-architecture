package cn.smilehappiness.security.utils.crypt;

import cn.smilehappiness.security.dto.BaseRequest;
import com.alibaba.fastjson.annotation.JSONField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * md util
 * <p/>
 *
 * @author
 * @Date 2023/3/9 10:55
 */
public class MdUtil {

    public static String genOneLineOfField(String FieldName, Class<?> FieldType) throws Exception {
        StringBuffer lineOfField = new StringBuffer();
        if (FieldType.equals(String.class)) {
            lineOfField.append("\n| " + FieldName + " | str | | | | |");
        } else if (FieldType.equals(Integer.class)) {
            lineOfField.append("\n| " + FieldName + " | int | | | | |");
        } else if (FieldType.isMemberClass()) {
            lineOfField.append("\n| " + FieldName + " | obj | | - | | - |");
        } else {

        }
        return lineOfField.toString();
    }

    public static StringBuffer genSectionsOfFields(StringBuffer requestFields, Class<?> memberClass) throws Exception {
        Field[] fields = memberClass.getDeclaredFields();

        //  Include member class
        boolean hasMemberClass = false;
        List<Class<?>> list = new ArrayList<>();
        requestFields.append("\n");
        requestFields.append("\n###### xx xxxxx parameter");
        requestFields.append("\n|  Parameter name | Type | Required | Maximum length | Description | Sample value      	           |");
        requestFields.append("\n| -------------------- | ---- | -------- | -------- | ------------------------------------------------------------------------------- | ------------------------   |");

        for (Field field : fields) {
            if (field.isAnnotationPresent(JSONField.class)) {
                requestFields.append(genOneLineOfField(field.getAnnotation(JSONField.class).name(), field.getType()));
            } else {
                requestFields.append(genOneLineOfField(field.getName(), field.getType()));
            }

            if (field.getType().isMemberClass()) {
                hasMemberClass = true;
                list.add(field.getType());
            }
        }

        if (hasMemberClass) {
            for (Class<?> item : list) {
                requestFields = genSectionsOfFields(requestFields, item);
            }
        }
        return requestFields;
    }

    /**
     * <p>
     * gen request fields
     * <p/>
     *
     * @param requestClassName
     * @return java.lang.String
     * @Date 2023/3/9 10:59
     */
    public static String genRequestFields(BaseRequest<?> requestClassName) throws Exception {
        StringBuffer requestFields = new StringBuffer();
        requestFields.append("\n###### 3  Common request parameters");
        requestFields.append("\n|  Parameter name | Type | Required | Maximum length | Description | Sample value      	           |");
        requestFields.append("\n| -------------------- | ---- | -------- | -------- | ------------------------------------------------------------------------------- | ------------------------   |");
        requestFields.append("\n| app_id               | str  | true     | 20       | APP The number of, which is generated when the application registers on the API open platform          								  |                  |");
        requestFields.append("\n| msg_id               | str  | true     | 40       |  Unique number of message communication, independently generated for each call, unique at APP level  								  |               |");
        requestFields.append("\n| format               | str  | false    | 5        |  Request parameter format, only supportedjson                       							  | json                       |");
        requestFields.append("\n| charset              | str  | false    | 10       |  Character set, default isUTF-8                            							  | UTF-8                      |");
        requestFields.append("\n| encrypt_type         | str  | false    | 5        |  Now only AES is supported, and some interfaces support encryption. If the interface does not need encryption, this field in the parameter does not need to be submitted               | AES 					   |");
        requestFields.append("\n| sign_type            | str  | false    | 5        |  Signature type, CA-issued certificate authentication, RSA-RSAWithSha1, RSA2-RSAWithSha256, default isRSA    | RSA 					   |");
        requestFields.append("\n| sign                 | str  | true     | 300      |  Message signature                                       						   |   |");
        requestFields.append("\n| timestamp            | str  | true     | 19       |  Transaction occurrence timestamp, yyyy-MM-dd HH: mm: ss format        							   |         |");
        requestFields.append("\n| ca                   | str  | false    | 2048     |  When adopting ca authentication mode, the certificate shall be submitted        			 							   |      	   |");
        requestFields.append("\n| biz_content          | obj  | true     | -        |  Collection of request parameters                                 							   |                      	   |");

        requestFields = genSectionsOfFields(requestFields, requestClassName.getBizContent().getClass());

        return requestFields.toString();
    }

}
