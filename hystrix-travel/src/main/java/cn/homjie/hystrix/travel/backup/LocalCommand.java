package cn.homjie.hystrix.travel.backup;

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
public class LocalCommand extends HystrixCommand<String> {
    private final String name;

    public LocalCommand(String name) {
        super(
            Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("TravelGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("Local"))
                // 使用不同的线程池做隔离，防止上层线程池跑满，影响降级逻辑.
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("LocalPool")));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        log.info("{} run command", name);
        return "from local";
    }

    @Override
    protected String getFallback() {
        return null;
    }
}
