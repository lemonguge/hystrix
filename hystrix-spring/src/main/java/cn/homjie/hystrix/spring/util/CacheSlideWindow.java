package cn.homjie.hystrix.spring.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jiehong.jh
 * @date 2018/4/27
 */
public class CacheSlideWindow<T> extends CountSlideWindow<T> {

    private final long time;
    private final TimeUnit unit;
    private final long window;

    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);

    public CacheSlideWindow(int length, Bucket<T> head, int max, long time, TimeUnit unit) {
        super(length, head, max);
        this.time = time;
        this.unit = unit;
        window = unit.toMillis(time);
        executor.schedule(this::keepNext, time, unit);
    }

    private void keepNext() {
        wLock.lock();
        try {
            if (System.currentTimeMillis() >= window + liveTime) {
                count.set(0);
                unsafeKeepNext();
            }
        } finally {
            wLock.unlock();
        }
    }

    @Override
    protected void onUnsafeNext() {
        executor.schedule(this::keepNext, time, unit);
    }
}
