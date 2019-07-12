package in.sskrishna.convoy.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ExecutorServiceConf {

    @Bean
    @Qualifier("GlobalExecutorService")
    public ExecutorService executorService() {
        int minThreads = 2;
        int maxThreads = 4;
        int keepAliveTime = 360;
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        ExecutorService executorService = new ThreadPoolExecutor(minThreads, maxThreads, keepAliveTime, TimeUnit.SECONDS, queue);
        return executorService;
    }
}