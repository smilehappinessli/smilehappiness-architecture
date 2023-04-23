package cn.smilehappiness.process.core.engine;


/**
 * <p>
 * Policy scheduling interface (top-level interface )
 * <p/>
 *
 * @author
 * @Date 2021/11/3 19:13
 */
public interface IEngineStrategyService {

    /**
     * <p>
     * Policy scheduling execution 
     * <p/>
     *
     * @param bizId
     * @param userId
     * @return java.lang.String
     * @Date 2021/11/3 19:13
     */
    String executeStrategy(String bizId, Long userId);

}
