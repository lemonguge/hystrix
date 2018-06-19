package cn.homjie.hystrix.spring.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 时间 滑动窗口
 *
 * @author jiehong.jh
 * @date 2018/4/27
 */
public class TimerSlideWindow<T> extends SlideWindow<T> {

    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);

    /**
     * @param length bucket个数
     * @param head
     * @param time   每个bucket存活时间
     * @param unit
     */
    public TimerSlideWindow(int length, Bucket<T> head, long time, TimeUnit unit) {
        super(length, head);
        executor.scheduleAtFixedRate(this::safeKeepNext, time, time, unit);
    }

    @Override
    public void put(T t) {
        rLock.lock();
        arr[index].onAccept(t);
        rLock.unlock();
    }
}
