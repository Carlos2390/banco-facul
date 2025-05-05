package com.cgp.banco;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BancoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BancoApplication.class)
                .run(args);
    }

}
