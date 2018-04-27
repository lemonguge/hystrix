package cn.homjie.hystrix.spring.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiehong.jh
 * @date 2018/4/27
 */
public class CountSlideWindow<T> extends SlideWindow<T> {

    private final int max;
    protected AtomicInteger count = new AtomicInteger();

    public CountSlideWindow(int length, Bucket<T> head, int max) {
        super(length, head);
        this.max = max;
    }

    @Override
    public void put(T t) {
        rLock.lock();
        if (count.getAndIncrement() < max) {
            arr[index].getListener().onAccept(t);
            rLock.unlock();
        } else {
            rLock.unlock();
            wLock.lock();
            try {
                if (count.get() >= max) {
                    count.set(0);
                    unsafeKeepNext();
                }
                count.getAndIncrement();
                arr[index].getListener().onAccept(t);
            } finally {
                wLock.unlock();
            }
        }
    }

}
