package cn.homjie.hystrix.travel.observe;

import cn.homjie.hystrix.travel.Operations;
import cn.homjie.hystrix.travel.TravelCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rx.Observable;
import rx.Observer;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
@Slf4j
@SpringBootApplication
public class ObserveApplication implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ObserveApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("--> start");
        //注册观察者事件拦截
        subscribe(new TravelCommand("Observer").observe());

        TravelCommand command = new TravelCommand("Exception");
        command.setOperable(Operations.exception());
        try {

            subscribe(command.observe());
        } catch (HystrixRuntimeException ex) {
            // 无法捕获异常，异步
            log.error("can not catch exception", ex);
        }
        // 快速执行
        log.info("--> async quick finish");
        // 防止结束导致看不到结果
        Operations.sleep(1000).execute();
    }

    private void subscribe(Observable<String> fs) {
        //注册结果回调事件
        fs.subscribe(result -> {
            // 对结果做二次处理
            log.info("call result: {}", result);
        });
        //注册完整执行生命周期事件
        fs.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                // 正常完成之后最后回调
                log.info("execute onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                // 当产生异常时回调
                log.info("onError: {}", e.getMessage());
            }

            @Override
            public void onNext(String v) {
                // 获取结果后回调
                log.info("onNext: {}", v);
            }
        });
    }
}
