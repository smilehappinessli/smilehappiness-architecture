package com.smilehappiness.aspect.operate;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.smilehappiness.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * <p>
 * 日志缓冲器
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/28 15:20
 */
@Slf4j
public class OperateLogBuffer {

    private BufferConsumer consumer = new BufferConsumer();
    private static BlockingQueue<OperateLogBaseInfo> logQueue = new LinkedBlockingQueue<>();

    /**
     * <p>
     * 每15秒自动执行一次
     * <p/>
     *
     * @param
     * @return
     * @Date 2021/8/28 15:22
     */
    public OperateLogBuffer() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("operateLog-pool-%d").build();
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(2, nameThreadFactory);
        poolExecutor.scheduleAtFixedRate(consumer, 0, 15, TimeUnit.SECONDS);
    }

    public void enqueue(OperateLogBaseInfo logInfo) {
        logQueue.add(logInfo);

        //多少条日志保存一次
        Integer sizeToSave = 5;
        if (logQueue.size() >= sizeToSave) {
            consumer.run();
        }
    }

    public static class BufferConsumer implements Runnable {
        @Override
        public void run() {
            List<OperateLogBaseInfo> logList = new ArrayList<>();
            //一次性从BlockingQueue获取所有可用的数据对象（还可以指定获取数据的个数）
            //通过该方法，可以提升获取数据效率，不需要多次分批加锁或释放锁
            logQueue.drainTo(logList);

            if (CollectionUtils.isEmpty(logList)) {
                return;
            }

            try {
                IOperateLogStore store = SpringUtil.getBean(IOperateLogStore.class);
                store.store(logList);
            } catch (Exception e) {
                log.error("保存操作日志失败，失败原因：{}", e.getMessage());
                log.error("保存操作日志失败，需要存储的日志信息：{}", JSON.toJSONString(logList));
            }
        }
    }
}
