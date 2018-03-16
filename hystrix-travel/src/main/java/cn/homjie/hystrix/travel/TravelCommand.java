package cn.homjie.hystrix.travel;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
public class TravelCommand extends HystrixCommand<String> {
    private final String name;
    private Operable operable;

    public TravelCommand(String name) {
        // 最少配置：指定命令组名(CommandGroup)
        super(HystrixCommandGroupKey.Factory.asKey("TravelGroup"));
        this.name = name;
    }

    public void setOperable(Operable operable) {
        this.operable = operable;
    }

    @Override
    protected String run() throws Exception {
        log.info("{} run command", name);
        if (operable != null) {
            operable.execute();
        }
        // 依赖逻辑封装在run()方法中
        return name + ", Thread: " + Thread.currentThread().getName();
    }
}
