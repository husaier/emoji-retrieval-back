package org.bupt.hse.retrieval;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@MapperScan("org.bupt.hse")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
