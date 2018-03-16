package cn.homjie.hystrix.travel.cache;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
public class RequestCacheCommand extends HystrixCommand<String> {
    private final int id;

    public RequestCacheCommand(int id) {
        super(
            Setter
                // 命令分组用于对依赖操作分组，便于统计和汇总
                // 在不指定 ThreadPoolKey 的情况下，字面值用于对不同依赖的线程池/信号区分
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("TravelGroup"))
                // 每个 CommandKey 代表一个依赖抽象，相同的依赖要使用相同的CommandKey名称。
                // 依赖隔离的根本就是对相同 CommandKey 的依赖做隔离
                .andCommandKey(HystrixCommandKey.Factory.asKey("RequestCache"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RequestCachePool"))
        );
        this.id = id;
    }

    @Override
    protected String run() throws Exception {
        log.info("run command execute id= " + id);
        return "executed= " + id;
    }

    @Override
    protected String getCacheKey() {
        // 实现区分不同请求的逻辑
        return String.valueOf(id);
    }

}
