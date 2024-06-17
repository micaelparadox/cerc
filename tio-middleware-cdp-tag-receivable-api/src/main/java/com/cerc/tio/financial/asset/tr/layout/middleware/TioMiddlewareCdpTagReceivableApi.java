package com.cerc.tio.financial.asset.tr.layout.middleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@ConfigurationPropertiesScan
public class TioMiddlewareCdpTagReceivableApi {

	public static void main(String[] args) {
		SpringApplication.run(TioMiddlewareCdpTagReceivableApi.class, args);
	}
}