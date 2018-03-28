package cn.homjie.hystrix.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiehong.jh
 * @date 2018/3/28
 */
@SpringBootApplication
@RestController
@EnableCircuitBreaker
@EnableHystrixDashboard
public class Application {

    @Autowired
    private BookService bookService;

    @GetMapping("/to-read")
    public String toRead() {
        return bookService.index();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
