package cn.smilehappiness.process.core.engine;


/**
 * <p>
 * Policy, process, callback job service class 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 11:37
 */
public interface IEngineJobService {

    /**
     * <p>
     * Batch execution of policies job
     * <p/>
     *
     * @param strategyBeanName
     * @return void
     * @Date 2021/11/4 11:36
     */
    void batchStrategyJob(String strategyBeanName);

    /**
     * <p>
     * Process batch execution job
     * <p/>
     *
     * @param processStatus If there are more than one status, comma separation 
     * @param bizCode
     * @return void
     * @Date 2021/11/4 11:36
     */
    void batchInvokeEngineJob(String processStatus, String bizCode);

    /**
     * <p>
     * Trigger process through bizId -- single process processing 
     * <p/>
     *
     * @param bizId
     * @return void
     * @Date 2021/11/17 17:10
     */
    void singleInvokeEngineJob(String bizId);

    /**
     * <p>
     * Notification callback batch execution job
     * <p/>
     *
     * @param
     * @return void
     * @Date 2021/11/3 18:10
     */
    void batchEngineNotifyJob();

}
