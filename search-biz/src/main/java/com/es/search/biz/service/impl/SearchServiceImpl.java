package com.es.search.biz.service.impl;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.search.biz.es.ResponseFormatter;
import com.es.search.biz.es.SearchProcessor;
import com.es.search.biz.service.SearchService;
import com.es.search.model.SearchParam;
import com.es.search.model.SearchResult;

/**
 * 搜索请求处理
 * 
 * @author lic
 * @date 2018年8月29日
 * @since v1.0.0
 */
@Service
public class SearchServiceImpl implements SearchService {


	@Autowired
	private TransportClient client;
	
	@Override
	public SearchResult search(SearchParam param) {
		SearchResponse response = SearchProcessor.queryResponse(param, client);
		return ResponseFormatter.formatter(response);
	}

}
