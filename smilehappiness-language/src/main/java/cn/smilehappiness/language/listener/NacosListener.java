//package cn.smilehappiness.language.listener;
//
//
//import com.purgeteam.dynamic.config.starter.event.ActionConfigEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author
// * @date ：Created in 16/12/21 14:04:08
// * @description：TODO：Optimize later 
// * @modified By：
// * @Version ：1.0
// */
//
//@Slf4j
//@Component
//class NacosListener implements ApplicationListener<ActionConfigEvent> {
//    @Override
//    public void onApplicationEvent(ActionConfigEvent environment) {
//        Map<String, HashMap> map = environment.getPropertyMap();
//        for (Map.Entry<String, HashMap> entry : map.entrySet()) {
//            String key = entry.getKey();
//            Map changeMap = entry.getValue();
//            String before = String.valueOf(changeMap.get("before"));
//            String after = String.valueOf(changeMap.get("after"));
//            if(log.isInfoEnabled()){
//                log.info("The configuration [key: {}] is changed, before the change: {}, after the change after：{}",key,before,after);
//            }
//            // Record of falling table 
//        }
//    }
//}
