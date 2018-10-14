package com.es.search.biz.service;

import com.es.search.model.SearchParam;
import com.es.search.model.SearchResult;

/**
 * 搜索请求处理
 * 
 * @author lic
 * @date 2018年8月29日
 * @since v1.0.0
 */
public interface SearchService {

	public SearchResult search(SearchParam param);
}
