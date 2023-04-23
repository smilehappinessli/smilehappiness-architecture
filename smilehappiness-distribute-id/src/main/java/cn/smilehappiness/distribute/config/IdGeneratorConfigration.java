package cn.smilehappiness.distribute.config;

import cn.smilehappiness.distribute.service.impl.CachedUidGenerator;
import cn.smilehappiness.distribute.service.impl.DisposableWorkerIdAssigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfigration {

    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    public CachedUidGenerator cachedUidGenerator(DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        return cachedUidGenerator;
    }

//    @Bean(name = "uidGenerator")
//    public CachedUidGenerator uidGenerator(DisposableWorkerIdAssigner disposableWorkerIdAssigner){
//        CachedUidGenerator cachedUidGenerator=new CachedUidGenerator();
//        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
//        return cachedUidGenerator;
//    }
}
