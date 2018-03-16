package cn.homjie.hystrix.travel.semaphore;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import static com.netflix.hystrix.HystrixCommandProperties.Setter;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
public class SemaphoreCommand extends HystrixCommand<String> {
    private final String name;

    public SemaphoreCommand(String name) {
        super(
            Setter.
                withGroupKey(HystrixCommandGroupKey.Factory.asKey("TravelGroup"))
                // 配置信号量隔离方式，默认采用线程池隔离
                .andCommandPropertiesDefaults(
                    Setter().withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE))
        );
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        // 将在被调用的线程执行
        log.info("{} run command", name);
        return "Thread: " + Thread.currentThread().getName();
    }
}
