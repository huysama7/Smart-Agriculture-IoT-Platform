package com.huysama.deviceService;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {
        "com.huysama.deviceService",
        "com.huysama.builderCore.config.iotDB",
        "com.huysama.builderCore.repositories.iot",
})
public class DeviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class, args);
    }

}
