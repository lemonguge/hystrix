package cn.homjie.hystrix.spring.util;

/**
 * 保证无状态
 *
 * @author jiehong.jh
 * @date 2018/4/27
 */
public interface BucketListener<T> {

    void onAccept(T t);

    void onComplete();
}
