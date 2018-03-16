package cn.homjie.hystrix.travel.cache;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
@SpringBootApplication
public class RequestCacheApplication implements CommandLineRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RequestCacheApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            RequestCacheCommand command2a = new RequestCacheCommand(2);
            RequestCacheCommand command2b = new RequestCacheCommand(2);
            //isResponseFromCache判定是否是在缓存中获取结果
            log.info("command2a result is {}, from cache: {}", command2a.execute(), command2a.isResponseFromCache());
            log.info("command2b result is {}, from cache: {}", command2b.execute(), command2b.isResponseFromCache());
        } finally {
            context.shutdown();
        }
        context = HystrixRequestContext.initializeContext();
        try {
            RequestCacheCommand command3b = new RequestCacheCommand(2);
            log.info("command3b result is {}, from cache: {}", command3b.execute(), command3b.isResponseFromCache());
        } finally {
            context.shutdown();
        }
    }
}
