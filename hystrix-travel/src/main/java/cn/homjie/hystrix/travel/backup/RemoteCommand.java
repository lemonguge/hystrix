package cn.homjie.hystrix.travel.backup;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
public class RemoteCommand extends HystrixCommand<String> {
    private final String name;

    public RemoteCommand(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TravelGroup"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("Remote")));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        log.info("{} run command", name);
        throw new RuntimeException("remote connect failed");
    }

    @Override
    protected String getFallback() {
        return new LocalCommand(name).execute();
    }
}
