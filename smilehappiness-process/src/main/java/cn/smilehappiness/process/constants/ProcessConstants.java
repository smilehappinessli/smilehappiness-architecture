package cn.smilehappiness.process.constants;

import lombok.Data;

/**
 * <p>
 * Process constant 
 * <p/>
 *
 * @author
 * @Date 2021/11/3 16:34
 */
@Data
public class ProcessConstants {

    public static final String BIZ_EXCEPTION = "Business-level exception ";

    public static final String SYS_EXCEPTION = "System-level exception ";

    /************Node preparation ***************/
    /**
     * Preparation completed 
     */
    public static final String PREP_READY = "Y";
    /**
     * Ready in advance 
     */
    public static final String PREP_UNREADY = "N";

    /******************** Process status  ********************/
    /**
     * Process status: 1-process initialization 
     */
    public static final String PROCESS_STATUS_INIT = "1";

    /**
     * Process status: 2-process template initialization 
     */
    public static final String PROCESS_STATUS_TEMPLATE = "2";

    /**
     * Process status: 3-Process in process 
     */
    public static final String PROCESS_STATUS_PROCESS = "3";

    /**
     * Process status: 4-process processing completed (pass or reject）
     */
    public static final String PROCESS_STATUS_DONE = "4";

    /**
     * Process status: 5 - Process processing exception 
     */
    public static final String PROCESS_STATUS_EXCEPTION = "5";

    /******************** Process results  ********************/
    /**
     * Process initialization -init
     */
    public static final String BPM_RESULT_INIT = "init";

    /**
     * Process initialization 
     */
    public static final String BPM_RESULT_INIT_MSG = "Process initialization ";

    /**
     * In process -process
     */
    public static final String BPM_RESULT_PROCESS = "process";

    /**
     * In process 
     */
    public static final String BPM_RESULT_PROCESS_MSG = "In process ";

    /**
     * Process results -pass
     */
    public static final String BPM_RESULT_PASS = "pass";

    /**
     * Process result description 
     */
    public static final String BPM_RESULT_PASS_MSG = "Process passed ";

    /**
     * Process results -reject
     */
    public static final String BPM_RESULT_REJECT = "reject";

    /**
     * Process result description 
     */
    public static final String BPM_RESULT_REJECT_MSG = "Process Reject ";

    /**
     * Process termination -break
     */
    public static final String BPM_RESULT_BREAK = "break";

    /**
     * Process terminated, please check the process record 
     */
    public static final String BPM_RESULT_BREAK_MSG = "Process terminated, please check the process record ";

    /**
     * Process exception -exception
     */
    public static final String BPM_RESULT_EXCEPTION = "exception";

    /**
     * Process exception 
     */
    public static final String BPM_RESULT_EXCEPTION_MSG = "Process exception ";

    /**
     * cycleProcess suspended in 
     */
    public static final String BPM_RESULT_CYCLE_PROCESS_MSG = "cycleProcess suspended in ";


    /******************** Callback result  ********************/
    /**
     * Callback ID initialization, no callback 
     */
    public static final String BPM_RESULT_NOTIFY_INIT = "N";
    /**
     * Callback succeeded -Y
     */
    public static final String BPM_RESULT_NOTIFY_SUCCESS = "Y";

    public static final String ASYNC_EXECUTE = "async";

    public static final String SYNC_EXECUTE = "sync";

    public static final String DING_CORE_KEY_BILL = "【 bill 】";

    public static final String DING_CORE_KEY_USER = "【 user 】";

    private ProcessConstants() {
        throw new IllegalStateException("Utility class");
    }

}
