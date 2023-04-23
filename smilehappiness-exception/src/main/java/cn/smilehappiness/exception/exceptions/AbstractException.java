package cn.smilehappiness.exception.exceptions;

/**
 * <p>
 * Abstract system internal exception class, non-business system prompts internal exception class
 * <p/>
 *
 * @author
 * @Date 2021/8/30 16:33
 */
public abstract class AbstractException extends RuntimeException {

    public AbstractException() {
    }

    public AbstractException(Throwable ex) {
        super(ex);
    }

    public AbstractException(String message) {
        super(message);
    }

    public AbstractException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * <p>
     * Get the business code corresponding to the exception
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/8/30 16:46
     */
    public abstract String getCode();
}
