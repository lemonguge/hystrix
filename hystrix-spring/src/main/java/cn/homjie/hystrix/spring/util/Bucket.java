package cn.homjie.hystrix.spring.util;

/**
 * @author jiehong.jh
 * @date 2018/4/26
 */
public abstract class Bucket<T> {

    private BucketContainer<T> container;

    public Bucket(BucketContainer<T> container) {
        this.container = container;
    }

    /**
     * @return 下一个bucket
     */
    public abstract Bucket<T> next();

    public final void onAccept(T t) {
        try {
            container.onAccept(t);
        } catch (Exception ex) {
            onAcceptException(t, ex);
        }
    }

    /**
     * 不要抛出异常，否则导致滑动窗口有问题
     *
     * @param t
     * @param ex
     */
    protected void onAcceptException(T t, Exception ex) {
        // 静默处理
    }

    public final void onComplete(long ttl) {
        try {
            container.onComplete(ttl);
        } catch (Exception ex) {
            onCompleteException(ttl, ex);
        }
    }

    /**
     * 不要抛出异常，否则导致滑动窗口有问题
     *
     * @param ttl 当前bucket的存活时间
     * @param ex
     */
    protected void onCompleteException(long ttl, Exception ex) {
        // 静默处理
    }

    public final void onExpire() {
        try {
            container.onExpire();
        } catch (Exception ex) {
            onExpireException(ex);
        }
    }

    /**
     * 不要抛出异常，否则导致滑动窗口有问题
     *
     * @param ex
     */
    protected void onExpireException(Exception ex) {
        // 静默处理
    }
}
