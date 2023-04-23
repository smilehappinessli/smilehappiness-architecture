package cn.smilehappiness.process.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Precondition execution result 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 14:17
 */
@Data
public class PrepResponse implements Serializable {

    private static final long serialVersionUID = 7472636425096385231L;

    /**
     * Front information 
     */
    private String message;

    /**
     * Whether the preparation is completed 
     */
    private boolean prep;

    public PrepResponse prepSuccess() {
        this.prep = true;
        this.message = "prepReady ";
        return this;
    }

    public PrepResponse prepSuccess(String message) {
        this.prep = true;
        this.message = message;
        return this;
    }

    public PrepResponse prepFail(String message) {
        this.prep = false;
        this.message = message;
        return this;
    }

}
