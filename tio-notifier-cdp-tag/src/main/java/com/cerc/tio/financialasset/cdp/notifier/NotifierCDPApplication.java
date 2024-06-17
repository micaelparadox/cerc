package com.cerc.tio.financialasset.cdp.notifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
@ConfigurationPropertiesScan
public class NotifierCDPApplication {


    public static void main(String[] args) {
        SpringApplication.run(NotifierCDPApplication.class, args);
    }

}
