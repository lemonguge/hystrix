package cn.homjie.hystrix.travel.semaphore;

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
public class SemaphoreApplication implements CommandLineRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SemaphoreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SemaphoreCommand command = new SemaphoreCommand("semaphore");
        String result = command.execute();
        log.info("result: " + result);
    }
}
