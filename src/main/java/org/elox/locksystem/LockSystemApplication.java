package org.elox.locksystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LockSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LockSystemApplication.class, args);

    }

}
