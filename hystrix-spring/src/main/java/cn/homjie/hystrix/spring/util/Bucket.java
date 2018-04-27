package cn.homjie.hystrix.spring.util;

/**
 * @author jiehong.jh
 * @date 2018/4/26
 */
public abstract class Bucket<T> {

    private BucketListener<T> listener;

    public Bucket(BucketListener<T> listener) {
        this.listener = listener;
    }

    public abstract Bucket<T> next();

    public BucketListener<T> getListener() {
        return listener;
    }
}
