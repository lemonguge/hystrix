package cn.homjie.hystrix.spring.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 滑动窗口（线程安全）
 *
 * @author jiehong.jh
 * @date 2018/4/26
 */
public abstract class SlideWindow<T> {

    protected final Bucket<T>[] arr;
    // bucket个数
    protected final int length;
    private final ReentrantReadWriteLock bucketLock = new ReentrantReadWriteLock();
    protected final Lock rLock = bucketLock.readLock();
    protected final Lock wLock = bucketLock.writeLock();
    // 当前bucket索引
    protected volatile int index;

    // 创建当前bucket的时间
    protected long liveTime;

    /**
     * @param length bucket个数
     */
    @SuppressWarnings("unchecked")
    public SlideWindow(int length, Bucket<T> head) {
        this.length = length;
        arr = (Bucket<T>[])new Bucket[length];
        arr[0] = head;
        liveTime = System.currentTimeMillis();
    }

    /**
     * @param t 保证只被当前bucket处理
     */
    public abstract void put(T t);

    /**
     * 当前bucket切换，保持滑动窗口连续
     */
    protected void safeKeepNext() {
        wLock.lock();
        try {
            Bucket<T> bucket = arr[index];
            if (index == length - 1) {
                index = 0;
            } else {
                index++;
            }
            arr[index] = bucket.next();
            long now = System.currentTimeMillis();
            long ttl = now - liveTime;
            liveTime = now;
            onSafeNext();
            bucket.onComplete(ttl);
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 切换新的bucket时触发
     */
    protected void onSafeNext() {
    }

}