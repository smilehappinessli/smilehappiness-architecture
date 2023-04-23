package cn.smilehappiness.security.dto;

import lombok.Data;

/**
 * <p>
 *  Signature universaldto
 * <p/>
 *
 * @author
 * @Date 2023/3/10 10:40
 */
@Data
public class SignCommonDto {

    /**
     * appId
     */
    private String appId;

    /**
     *  Method name
     */
    private String methodName;

    /**
     *  Channel No
     */
    private String channelNo;

}
