package net.rokyinfo.ops.monitorman;

import net.rokyinfo.ops.monitorman.service.MonitorManService;
import net.rokyinfo.ops.monitorman.util.CustomThreadPoolExecutor;
import net.rokyinfo.ops.monitorman.util.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yuanzhijian
 */
@SpringBootApplication
@EnableAsync
public class MonitorManApplication implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MonitorManApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MonitorManApplication.class, args);
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    public ThreadPoolExecutor newmanExecThreadPoolExecutor() {
        return new CustomThreadPoolExecutor().getCustomThreadPoolExecutor();
    }


    @Bean
    public Sequence monitorIdSequence() {
        return new Sequence(0, 0);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //在容器加载完毕后根据JSON文件初始化监控列表
        MonitorManService monitorManService = event.getApplicationContext().getBean(MonitorManService.class);
        monitorManService.initMonitorListFromFileData();

    }
}
