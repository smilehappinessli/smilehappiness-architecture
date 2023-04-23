package cn.smilehappiness.process.dto;

import lombok.Data;


/**
 * <p>
 * Core request parameter class 
 * <p/>
 *
 * @author
 * @Date 2021/11/3 16:34
 */
@Data
public class EngineRequestDto<T> {

    /**
     * The primary key ID may be the application record, and the post-processing logic. The first phase is assigned to the orderApply order id
     */
    private Long id;

    /**
     * User ID, which can be determined based on different business processes (the bill system needs this field). The other three parameters must be passed 
     */
    private Long userId;

    /**
     * business id
     */
    private String bizId;

    /**
     * business Code
     */
    private String bizCode;

    /**
     * Request parameters 
     */
    private T requestParam;

}
