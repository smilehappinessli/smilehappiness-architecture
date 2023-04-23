package cn.smilehappiness.security.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  User token information storage
 * <p/>
 *
 * @author
 * @Date 2021/10/13 18:04
 */
@Data
public class UserTokenInfo implements Serializable {

    private static final long serialVersionUID = -1720636280667286003L;

    /**
     *  userid
     */
    private Long userId;
    /**
     *  User name
     */
    private String userName;
    /**
     * appMerchant
     */
    private String appMerchant;
    /**
     *  cell-phone number
     */
    private String mobile;
    /**
     *  User type (1 - individual 2 - enterprise）
     */
    private Integer userType;

}
