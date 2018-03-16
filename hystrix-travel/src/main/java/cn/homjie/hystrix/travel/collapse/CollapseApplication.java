package cn.homjie.hystrix.travel.collapse;

import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.hystrix.HystrixRequestLog;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
@SpringBootApplication
public class CollapseApplication implements CommandLineRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(CollapseApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            Future<String> f1 = new SingleCommand(1).queue();
            Future<String> f2 = new SingleCommand(2).queue();
            Future<String> f3 = new SingleCommand(3).queue();
            Future<String> f4 = new SingleCommand(4).queue();
            log.info(f1.get());
            log.info(f2.get());
            log.info(f3.get());
            log.info(f4.get());
            Collection<HystrixInvokableInfo<?>> hystrixInvokableInfoCollection = HystrixRequestLog.getCurrentRequest()
                .getAllExecutedCommands();
            log.info("request times is {}", hystrixInvokableInfoCollection.size());
            HystrixInvokableInfo<?> command = hystrixInvokableInfoCollection.toArray(new HystrixInvokableInfo[0])[0];
            log.info("commandKey is {}, eventType is {}, collapse is {}",
                command.getCommandKey().name(),
                command.getExecutionEvents().contains(HystrixEventType.COLLAPSED),
                command.getExecutionEvents().contains(HystrixEventType.SUCCESS));
        } finally {
            context.shutdown();
        }
    }
}
