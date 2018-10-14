package com.es.search.rpc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.search.model.SearchParam;
import com.es.search.model.SearchResult;
import com.netflix.hystrix.contrib.javanica.annotation.*;

@Service
public class SearchRpcService {

//	@Autowired
//	SearchFeignClient searchClient;
//	
////	@HystrixCommand
//	public SearchResult search(SearchParam param) {
//		return searchClient.search(param);
//	}
}
