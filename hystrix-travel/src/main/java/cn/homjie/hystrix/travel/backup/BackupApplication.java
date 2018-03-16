package cn.homjie.hystrix.travel.backup;

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
public class BackupApplication implements CommandLineRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(BackupApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("result is {}", new RemoteCommand("key").execute());
    }
}
