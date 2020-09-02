package com.hande.hzero.order;

import io.choerodon.resource.annoation.EnableChoerodonResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableChoerodonResourceServer
public class HzeroOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzeroOrderApplication.class, args);
    }

}
