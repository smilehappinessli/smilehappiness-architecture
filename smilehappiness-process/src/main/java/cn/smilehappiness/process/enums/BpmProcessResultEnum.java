package cn.smilehappiness.process.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * <p>
 * Process processing result enumeration class 
 * none/process/pass/reject  Empty/Processing/Pass/Reject 
 * <p/>
 *
 * @author
 * @Date 2021/12/17 17:28
 */
@Getter
@RequiredArgsConstructor
public enum BpmProcessResultEnum {

    /**
     * 1-Process initialization 
     */
    PROCESS_RESULT_INIT("1", "init", "Process initialization "),

    /**
     * 2-Process template initialization 
     */
    PROCESS_RESULT_TEMPLATE_INIT("2", "templateInit", "Process template initialization "),

    /**
     * 3-In process 
     */
    PROCESS_RESULT_PROCESS("3", "process", "In process "),

    /**
     * 4-Process processing completed (pass or reject）
     */
    PROCESS_RESULT_DONE("4", "done", "Process processing completed (pass or reject）"),

    /**
     * 5-Process handling exception 
     */
    PROCESS_RESULT_EXCEPTION("5", "exception", "Process handling exception "),

    /**
     * 6-No relevant orders were obtained 
     */
    PROCESS_RESULT_NONE("6", "none", "No relevant orders were obtained ");

    private final String key;
    private final String value;
    private final String message;

    /**
     * <p>
     * Return the corresponding description information through key 
     * <p/>
     *
     * @param key
     * @return java.lang.String
     * @Date 2021/12/17 17:28
     */
    public static String getProcessApproveResult(String key) {
        Optional<BpmProcessResultEnum> optional = Arrays.stream(BpmProcessResultEnum.values()).filter(item -> StringUtils.equals(item.getKey(), key)).findFirst();
        if (optional.isPresent()) {
            return optional.get().getValue();
        }

        return "process";
    }

}
