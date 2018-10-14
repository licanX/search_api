package com.es.search.biz.es;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.es.search.model.SearchResult;
import com.google.common.collect.Lists;

/**
 * 搜索结果解析
 * 
 * @author lic
 * @date 2018年8月29日
 * @since v1.0.0
 */
public class ResponseFormatter {

	private static final Logger LOG = LoggerFactory.getLogger(ResponseFormatter.class);

	/**
	 * 搜索响应解析为结果对象
	 * 
	 * @param response
	 * @return SearchResult
	 * @author lic
	 * @date 2018年8月29日
	 */
	public static SearchResult formatter(SearchResponse response) {
		SearchResult sr = new SearchResult();
		if (validate(response)) {
			LOG.error("[ResponseFormatter]formatter error,search result is empty,response :{}", response);
			return sr;
		}
		List<Object> results = Lists.newArrayList();
		long total = 0L;
		SearchHits hits = response.getHits();
		total = hits.getTotalHits();
		for (SearchHit hit : hits) {
			Map<String, Object> result = hit.getSourceAsMap();
			results.add(result);
		}
		sr.setResults(results);
		sr.setTotal(total);
		return sr;
	}

	private static boolean validate(SearchResponse response) {
		return response == null || response.getHits().getTotalHits() < 1;
	}
}
