package cn.smilehappiness.cache.enums;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Core state enumeration class, not used temporarily 
 * <p/>
 *
 * @author
 * @Date 2021/8/27 15:34
 */
public abstract class CoreEnum {

    /**
     * Expiration time related enumeration 
     */
    public enum ExpireEnum {

        /**
         * Unread messages are valid for 30 days 
         */
        UNREAD_MSG(30L, TimeUnit.DAYS);

        /**
         * Expiration time 
         */
        private Long time;
        /**
         * Time unit 
         */
        private TimeUnit timeUnit;

        ExpireEnum(Long time, TimeUnit timeUnit) {
            this.time = time;
            this.timeUnit = timeUnit;
        }

        public Long getTime() {
            return time;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }
}
