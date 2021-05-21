package com.geekrem.mall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MallApplication {

    public static void main(String[] args) {
        log.debug ("==================项目开始启动===============");
        SpringApplication.run(MallApplication.class, args);
        log.debug ("==================项目启动成功===============");
    }

}
