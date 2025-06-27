package com.ferticare.ferticareback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class FerticareBackApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FerticareBackApplication.class);
        SpringApplication.run(FerticareBackApplication.class, args);
    }
}
