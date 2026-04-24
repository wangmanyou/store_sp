package org.example.store_sp_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.store_sp_backend.mapper")
public class StoreSpBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreSpBackendApplication.class, args);
    }

}
