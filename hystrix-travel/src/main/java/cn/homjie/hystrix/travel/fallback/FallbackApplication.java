package cn.homjie.hystrix.travel.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
@SpringBootApplication
public class FallbackApplication implements CommandLineRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(FallbackApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 触发降级回退
        log.info("result = {}", new FallbackCommand("Timeout").execute());

        FallbackCommand command = new FallbackCommand("Async");
        Future<String> future = command.queue();
        try {
            // 并不会触发降级
            log.info("result = {}", future.get(300, TimeUnit.MILLISECONDS));
        } catch (TimeoutException ex) {
            log.error("not trigger fallback {}", ex.getMessage());
        }
    }
}
