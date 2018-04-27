package cn.homjie.hystrix.spring.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jiehong.jh
 * @date 2018/4/27
 */
public class TimerSlideWindow<T> extends SlideWindow<T> {

    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);

    public TimerSlideWindow(int length, Bucket<T> head, long time, TimeUnit unit) {
        super(length, head);
        executor.scheduleAtFixedRate(this::unsafeKeepNext, time, time, unit);
    }

    @Override
    public void put(T t) {
        rLock.lock();
        arr[index].getListener().onAccept(t);
        rLock.unlock();
    }
}
