package kr.bitegove.restful.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by jerry on 2016-09-27.
 */
@SpringBootApplication
public class RESTfulApplication {
    public static void main(String[] args) {
        SpringApplication.run(RESTfulApplication.class, args);
    }

}
