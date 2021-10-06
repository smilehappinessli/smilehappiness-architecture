package com.smilehappiness.cache.enums;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 核心状态枚举类，暂时未使用
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/27 15:34
 */
public abstract class CoreEnum {

    /**
     * 过期时间相关枚举
     */
    public enum ExpireEnum {

        /**
         * 未读消息的有效期为30天
         */
        UNREAD_MSG(30L, TimeUnit.DAYS);

        /**
         * 过期时间
         */
        private Long time;
        /**
         * 时间单位
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
