package in.sskrishna.gatekeeper.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ExecutorServiceConf {

    @Bean
    @Qualifier("GlobalExecutorService")
    public ExecutorService executorService() {
        int cores = Runtime.getRuntime().availableProcessors();
        log.info("Number of available CPU cores: {}", cores);
        int minThreads = cores;
        int maxThreads = cores * 2;
        int keepAliveTime = 360;
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        ExecutorService executorService = new ThreadPoolExecutor(minThreads, maxThreads, keepAliveTime, TimeUnit.SECONDS, queue);
        return executorService;
    }
}