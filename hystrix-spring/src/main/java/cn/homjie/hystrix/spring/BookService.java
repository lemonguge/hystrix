package cn.homjie.hystrix.spring;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author jiehong.jh
 * @date 2018/3/28
 */
@Service
@Slf4j
public class BookService {

    @HystrixCommand(fallbackMethod = "reliable", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "200")
    })
    public String index() {
        int sleep = ThreadLocalRandom.current().nextInt(100, 300);
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
        }
        log.info("get book rt is {}", sleep);
        return "Spring in Action";
    }

    public String reliable() {
        return "Cloud Native Java (O'Reilly)";
    }

}
