package com.es.search.rpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Request;

/**
 * search feignClient 配置
 * @author lic
 * @date 2018年8月30日
 * @since v1.0.0
 */
@Configuration
public class SearchConfiguration {
	
	@Value("${search.feign.connectTimeout:5000}")
	private int connectTimeout;
	@Value("${search.feign.readTimeout:5000}")
	private int readTimeout;
	
	@Bean
	public Request.Options options(){
		return new Request.Options(connectTimeout,readTimeout);
	}
}
