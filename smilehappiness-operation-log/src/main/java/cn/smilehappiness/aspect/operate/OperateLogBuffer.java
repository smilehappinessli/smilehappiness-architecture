package cn.smilehappiness.aspect.operate;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cn.smilehappiness.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * <p>
 * Log buffer 
 * <p/>
 *
 * @author
 * @Date 2021/8/28 15:20
 */
@Slf4j
public class OperateLogBuffer {

    private final BufferConsumer consumer = new BufferConsumer();
    private static final BlockingQueue<OperateLogBaseInfo> LOG_QUEUE = new LinkedBlockingQueue<>();

    @Value("${logger.apiLogger.beginStore.size:1}")
    private int sizeToSave;

    /**
     * <p>
     * Automatically every 15 seconds 
     * <p/>
     *
     * @param
     * @return
     * @Date 2021/8/28 15:22
     */
    public OperateLogBuffer() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("operateLog-pool-%d").build();
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(2, nameThreadFactory);
        poolExecutor.scheduleAtFixedRate(consumer, 0, 30, TimeUnit.SECONDS);
    }

    public void enqueue(OperateLogBaseInfo logInfo) {
        LOG_QUEUE.add(logInfo);

        //How many logs are saved once 
        if (LOG_QUEUE.size() >= sizeToSave) {
            consumer.run();
        }
    }

    public static class BufferConsumer implements Runnable {
        @Override
        public void run() {
            List<OperateLogBaseInfo> logList = new ArrayList<>();
            //Get all available data objects from BlockingQueue at one time (you can also specify the number of data to get ）
            //This method can improve the efficiency of data acquisition and does not need to add locks or release locks in batches many times 
            LOG_QUEUE.drainTo(logList);

            if (CollectionUtils.isEmpty(logList)) {
                return;
            }

            try {
                IOperateLogStore store = SpringUtil.getBean(IOperateLogStore.class);
                store.store(logList);
            } catch (Exception e) {
                log.error("Failed to save operation log, failure reason ：【{}】", e.getMessage());
                log.error("Failed to save the operation log. You need to store the log information ：【{}】", JSON.toJSONString(logList));
            }
        }
    }
}
