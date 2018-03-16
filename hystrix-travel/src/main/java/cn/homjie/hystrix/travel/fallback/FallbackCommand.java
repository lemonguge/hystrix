package cn.homjie.hystrix.travel.fallback;

import cn.homjie.hystrix.travel.Operations;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import lombok.extern.slf4j.Slf4j;

import static com.netflix.hystrix.HystrixCommandProperties.Setter;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
public class FallbackCommand extends HystrixCommand<String> {
    private final String name;

    public FallbackCommand(String name) {
        super(
            Setter
                // 命令分组用于对依赖操作分组，便于统计和汇总
                // 在不指定 ThreadPoolKey 的情况下，字面值用于对不同依赖的线程池/信号区分
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("TravelGroup"))
                // 每个 CommandKey 代表一个依赖抽象，相同的依赖要使用相同的CommandKey名称。
                // 依赖隔离的根本就是对相同 CommandKey 的依赖做隔离
                .andCommandKey(HystrixCommandKey.Factory.asKey("Fallback"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("FallbackPool"))
                // 配置依赖超时时间为 500 毫秒
                .andCommandPropertiesDefaults(Setter().withExecutionTimeoutInMilliseconds(500))
        );
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        log.info("{} run command", name);
        Operations.sleep(800).execute();
        return name + " thread:" + Thread.currentThread().getName();
    }

    @Override
    protected String getFallback() {
        // HystrixBadRequestException 用在非法参数或非系统故障异常等不应触发回退逻辑的场景。
        // 除了 HystrixBadRequestException 异常之外，所有从 run() 方法抛出的异常都算作失败
        // 并触发降级 getFallback() 和 断路器逻辑。
        return "execute failed";
    }
}
