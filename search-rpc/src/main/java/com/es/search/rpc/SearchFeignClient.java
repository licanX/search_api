package com.es.search.rpc;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.es.search.model.SearchParam;
import com.es.search.model.SearchResult;

/**
 * search feignClient
 * 
 * @author lic
 * @date 2018年8月30日
 * @since v1.0.0
 */
//@FeignClient(value = "${search.feign.client.value}", configuration = SearchConfiguration.class)
//public interface SearchFeignClient {
//
//	@RequestMapping(value="/search/list/1.0",method=RequestMethod.POST)
//	SearchResult search(@RequestBody SearchParam param);
//}
