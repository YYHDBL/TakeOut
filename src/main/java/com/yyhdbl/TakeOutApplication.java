package com.yyhdbl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan  //扫描过滤器  将扫描到的控制器 过滤器 监听器注入到spring容器中
@EnableTransactionManagement
@Slf4j
public class TakeOutApplication {

    public static void main(String[] args) {
        log.info("http://localhost:8080/backend/page/login/login.html");
        SpringApplication.run(TakeOutApplication.class, args);
    }

}
