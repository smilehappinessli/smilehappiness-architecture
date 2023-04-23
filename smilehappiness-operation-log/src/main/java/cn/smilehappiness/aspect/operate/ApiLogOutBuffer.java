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
 * Log buffer -ApiLoggerOut
 * <p/>
 *
 * @author
 * @Date 2022/1/3 18:01
 */
@Slf4j
public class ApiLogOutBuffer {

    private final BufferConsumer consumer = new BufferConsumer();
    private static final BlockingQueue<OperateLogBaseInfo> API_LOGGER_OUT_QUEUE = new LinkedBlockingQueue<>();

    @Value("${logger.apiLoggerOut.beginStore.size:1}")
    private int sizeToSave;

    /**
     * <p>
     * Automatically every 15 seconds 
     * <p/>
     *
     * @param
     * @return
     * @Date 2022/1/3 18:01
     */
    public ApiLogOutBuffer() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("apiLoggerOut-pool-%d").build();
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(2, nameThreadFactory);
        poolExecutor.scheduleAtFixedRate(consumer, 0, 30, TimeUnit.SECONDS);
    }

    public void enqueue(OperateLogBaseInfo logInfo) {
        API_LOGGER_OUT_QUEUE.add(logInfo);

        //How many logs are saved once 
        if (API_LOGGER_OUT_QUEUE.size() >= sizeToSave) {
            consumer.run();
        }
    }

    public static class BufferConsumer implements Runnable {
        @Override
        public void run() {
            List<OperateLogBaseInfo> logList = new ArrayList<>();
            //Get all available data objects from BlockingQueue at one time (you can also specify the number of data to get ）
            //This method can improve the efficiency of data acquisition and does not need to add locks or release locks in batches many times 
            API_LOGGER_OUT_QUEUE.drainTo(logList);

            if (CollectionUtils.isEmpty(logList)) {
                return;
            }

            try {
                IApiLoggerOutStore store = SpringUtil.getBean(IApiLoggerOutStore.class);
                store.store(logList);
            } catch (Exception e) {
                log.error("【ApiLoggerOut】Failed to save operation log, failure reason ：【{}】", e.getMessage());
                log.error("【ApiLoggerOut】Failed to save the operation log. You need to store the log information ：【{}】", JSON.toJSONString(logList));
            }
        }
    }
}
