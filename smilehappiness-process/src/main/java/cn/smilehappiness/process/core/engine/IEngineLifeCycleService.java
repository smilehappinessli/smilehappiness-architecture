package cn.smilehappiness.process.core.engine;

/**
 * <p>
 * Callbacks in the process lifecycle 
 * <p>
 * A process model can call multiple callbacks for processing various business scenarios 
 * For example, after the process passes, other business systems need to be notified, and when the business terminates abnormally, relevant personnel need to be notified to track the problem 
 * <p/>
 *
 * @author
 * @Date 2021/11/3 17:13
 */
public interface IEngineLifeCycleService {

    /**
     * <p>
     * Process initialization 
     * <p/>
     *
     * @param bizId
     * @param bpmModel
     * @param options
     * @return void
     * @Date 2021/11/3 17:15
     */
    void initProcessCycle(String bizId, String bpmModel, String options);

    /**
     * <p>
     * Callback before process execution 
     * <p/>
     *
     * @param
     * @return boolean
     * @Date 2021/11/3 17:15
     */
    boolean beforeProcessCallBack();

    /**
     * <p>
     * Callback on first execution 
     * <p/>
     *
     * @param
     * @return boolean
     * @Date 2021/11/3 17:16
     */
    boolean firstProcessCallBack();

    /**
     * <p>
     * Process in progress 
     * <p/>
     *
     * @param
     * @return boolean
     * @Date 2021/11/4 15:36
     */
    boolean processProcessingCallBack();

    /**
     * <p>
     * Callback when the process passes 
     * <p/>
     *
     * @param
     * @return boolean
     * @Date 2021/11/3 17:16
     */
    boolean processPassCallBack();

    /**
     * <p>
     * Callback on process rejection 
     * <p/>
     *
     * @param
     * @return boolean
     * @Date 2021/11/3 17:17
     */
    boolean processRejectCallBack();

    /**
     * <p>
     * Cause process termination in case of abnormality 
     * <p/>
     *
     * @param message
     * @return boolean
     * @Date 2021/11/3 17:25
     */
    boolean processBreakCallBack(String message);

    /**
     * <p>
     * Callback when the process is abnormal 
     * <p/>
     *
     * @param message
     * @return boolean
     * @Date 2021/11/3 17:20
     */
    boolean processExceptionCallBack(String message);

    /**
     * <p>
     * Callback when the process is abnormal 
     * <p/>
     *
     * @param exception
     * @param message
     * @return boolean
     * @Date 2021/11/3 17:25
     */
    boolean processExceptionCallBack(Exception exception, String message);

    /**
     * <p>
     * Callback notification processing 
     * <p/>
     *
     * @param
     * @return boolean
     * @Date 2021/11/4 10:22
     */
    boolean processBizNotify();

}
