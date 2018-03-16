package cn.homjie.hystrix.travel.run;

import cn.homjie.hystrix.travel.Operations;
import cn.homjie.hystrix.travel.TravelCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
@SpringBootApplication
public class ExecuteApplication implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ExecuteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("--> start");
        TravelCommand travelCommand = new TravelCommand("Synchronous");
        // 同步调用
        String result = travelCommand.execute();
        log.info("result= " + result);
        try {
            //每个Command对象只能调用一次,不可以重复调用,
            travelCommand.execute();
        } catch (HystrixRuntimeException ex) {
            log.error("exception by {}", ex.getMessage());
        }

        travelCommand = new TravelCommand("Asynchronous");
        // 异步调用
        Future<String> future = travelCommand.queue();
        // 不能超过 command 定义的超时时间，默认:1秒
        result = future.get(100, TimeUnit.MILLISECONDS);
        log.info("result= " + result);

        travelCommand = new TravelCommand("Timeout");
        travelCommand.setOperable(Operations.sleep(1500));
        try {
            // 超时
            travelCommand.execute();
        } catch (HystrixRuntimeException ex) {
            log.error("exception by {}", ex.getMessage());
        }
        log.info("--> finish");
    }
}
