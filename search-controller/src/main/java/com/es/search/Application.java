package com.es.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 * @author lic
 * @date 2018年8月26日
 * @since v1.0.0
 */
//@EnableEurekaServer  注册eureka服务
//@EnableEurekaClient 启用eureka客户端
//@EnableFeignClients(basePackages = {"com.es.search"}) 需要调用的eureka接口包
//@EnableDiscoveryClient 
//@EnableHystrixDashboard
@SpringBootApplication
@ComponentScan(basePackages = {"com.es.search"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
