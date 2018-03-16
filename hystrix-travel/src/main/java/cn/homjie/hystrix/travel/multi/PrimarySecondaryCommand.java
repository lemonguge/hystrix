package cn.homjie.hystrix.travel.multi;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import lombok.extern.slf4j.Slf4j;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import static com.netflix.hystrix.HystrixCommandProperties.Setter;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
public class PrimarySecondaryCommand extends HystrixCommand<String> {

    private final static DynamicBooleanProperty usePrimary = DynamicPropertyFactory.getInstance().getBooleanProperty(
        "primarySecondary.usePrimary", true);
    private final int id;

    public PrimarySecondaryCommand(int id) {
        super(
            Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("TravelGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("PrimarySecondary"))
                .andCommandPropertiesDefaults(
                    Setter().withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)));
        this.id = id;
    }

    @Override
    protected String run() {
        log.info("run command execute id= " + id);
        if (usePrimary.get()) {
            return new PrimaryCommand(id).execute();
        } else {
            return new SecondaryCommand(id).execute();
        }
    }

    @Override
    protected String getFallback() {
        return "static-fallback-" + id;
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }

    private static class PrimaryCommand extends HystrixCommand<String> {
        private final int id;

        private PrimaryCommand(int id) {
            super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("TravelGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("Primary"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("PrimaryPool"))
                .andCommandPropertiesDefaults(
                    // we default to a 600ms timeout for primary
                    Setter().withExecutionTimeoutInMilliseconds(600)));
            this.id = id;
        }

        @Override
        protected String run() {
            // perform expensive 'primary' service call
            return "response from Primary-" + id;
        }
    }

    private static class SecondaryCommand extends HystrixCommand<String> {
        private final int id;

        private SecondaryCommand(int id) {
            super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("TravelGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("Secondary"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("SecondaryPool"))
                .andCommandPropertiesDefaults(
                    // we default to a 100ms timeout for secondary
                    Setter().withExecutionTimeoutInMilliseconds(100)));
            this.id = id;
        }

        @Override
        protected String run() {
            // perform fast 'secondary' service call
            return "response from Secondary-" + id;
        }
    }
}
