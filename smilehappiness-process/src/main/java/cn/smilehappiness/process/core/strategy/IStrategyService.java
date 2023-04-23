package cn.smilehappiness.process.core.strategy;


/**
 * <p>
 * Routing Layer - Policy Processing 
 * Decision process model through rules 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 11:48
 */
public interface IStrategyService {

    /**
     * <p>
     * Initialization Policy 
     * <p/>
     *
     * @param bizId
     * @param initialWeight        Initial weight value 
     * @param bpmModel             Model 
     * @param bizCode              business code
     * @param strategyOptionConfig Policy configuration 
     * @return void
     * @Date 2021/11/4 11:47
     */
    void initStrategy(String bizId, Integer initialWeight, String bpmModel, String bizCode, String strategyOptionConfig);

    /**
     * <p>
     * Strategy weight=score weight+initial weight 
     * The one with the highest strategic weight score wins 
     * <p/>
     *
     * @param
     * @return java.lang.Integer
     * @Date 2021/11/4 11:46
     */
    Integer getStrategyWeight();

    /**
     * <p>
     * Initialize policy template and process template 
     * <p/>
     *
     * @param userId
     * @return void
     * @Date 2021/11/4 11:46
     */
    void initStrategyTemplate(Long userId);

}
