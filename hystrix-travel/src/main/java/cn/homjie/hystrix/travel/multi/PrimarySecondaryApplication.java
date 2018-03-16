package cn.homjie.hystrix.travel.multi;

import com.netflix.config.ConfigurationManager;
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
public class PrimarySecondaryApplication implements CommandLineRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(PrimarySecondaryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            ConfigurationManager.getConfigInstance().setProperty("primarySecondary.usePrimary", true);
            log.info("result = {}", new PrimarySecondaryCommand(20).execute());
        } finally {
            context.shutdown();
            ConfigurationManager.getConfigInstance().clear();
        }
        context = HystrixRequestContext.initializeContext();
        try {
            ConfigurationManager.getConfigInstance().setProperty("primarySecondary.usePrimary", false);
            log.info("result = {}", new PrimarySecondaryCommand(20).execute());
        } finally {
            context.shutdown();
            ConfigurationManager.getConfigInstance().clear();
        }
    }
}
