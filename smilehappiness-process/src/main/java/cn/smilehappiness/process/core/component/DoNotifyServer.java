package cn.smilehappiness.process.core.component;

import cn.smilehappiness.process.constants.ProcessConstants;
import cn.smilehappiness.process.core.engine.IEngineLifeCycleService;
import cn.smilehappiness.process.mapper.BpmApplyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * Notification callback service 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 10:18
 */
@Slf4j
@Component
@RefreshScope
public class DoNotifyServer {

    /**
     * Callback switch 
     */
    @Value("${bpm.process.canNotify:true}")
    private boolean canNotify;
    @Value("${bpm.process.notify.sleepTime:100}")
    private long sleepTime;
    @Resource
    private BpmApplyMapper bpmApplyMapper;


    /**
     * <p>
     * Callback notification processing - synchronization 
     * <p/>
     *
     * @param engineLifeCycleService
     * @param bizId
     * @return void
     * @Date 2021/11/4 10:25
     */
    public void processBizNotify(IEngineLifeCycleService engineLifeCycleService, String bizId) {
        if (canNotify && engineLifeCycleService.processBizNotify()) {
            //The callback is successfully executed, and the synchronization business status is Synchronized 
            bpmApplyMapper.updateBpmApplyNotifyResult(ProcessConstants.BPM_RESULT_NOTIFY_SUCCESS, bizId);
        }
    }

    /**
     * <p>
     * Callback notification processing 
     * <p/>
     *
     * @param engineLifeCycleService
     * @param bizId
     * @return void
     * @Date 2021/11/4 10:25
     */
    @Async
    public void processBizNotifyAsync(IEngineLifeCycleService engineLifeCycleService, String bizId) {
        if (canNotify) {
            try {
                //Make asynchronous callback more slow and avoid concurrency problems at the business side 
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
            if (engineLifeCycleService.processBizNotify()) {
                //The callback is successfully executed, and the synchronization business status is Synchronized 
                bpmApplyMapper.updateBpmApplyNotifyResult(ProcessConstants.BPM_RESULT_NOTIFY_SUCCESS, bizId);
            }
        }
    }


}
