package org.laokouyun.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.laokouyun.**")
public class ServerDemoApplication {


    static void main(String[] args) {
        SpringApplication.run(ServerDemoApplication.class, args);
    }
}
