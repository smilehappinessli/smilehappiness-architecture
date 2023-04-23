package cn.smilehappiness.exception.exceptions;

/**
 * <p>
 * System-level exception
 * <p/>
 *
 * @author
 * @Date 2021/8/30 16:33
 */
public final class SystemInternalException extends AbstractException {

    /**
     * Status code
     */
    private String code;

    public SystemInternalException(Throwable ex) {
        super(ex);
    }

    public SystemInternalException(String message) {
        super(message);
    }

    public SystemInternalException(String message, Throwable ex) {
        super(message, ex);
    }

    public SystemInternalException(String code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
