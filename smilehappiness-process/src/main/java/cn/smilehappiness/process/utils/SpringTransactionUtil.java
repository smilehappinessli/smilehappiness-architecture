package cn.smilehappiness.process.utils;

import cn.smilehappiness.exception.exceptions.SystemInternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Consumer;

/**
 * <p>
 * Spring Programming transaction tool class 
 * <p/>
 *
 * @author
 * @Date 2021/11/15 20:20
 */
@Slf4j
@Component
public class SpringTransactionUtil {

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * <p>
     * This class returns the transaction execution result of the function by receiving a function-type parameter 
     * <p/>
     *
     * @param consumer
     * @return boolean
     * @Date 2021/11/15 20:21
     */
    public <T> boolean transactionalExecute(Consumer<T> consumer) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            //This is just to execute the service business code, without changing the value of the code, so consumer. accept (null) is enough 
            consumer.accept(null);

            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new SystemInternalException("Service processing exception, transaction rollback ：" + e);
        }
    }

}
