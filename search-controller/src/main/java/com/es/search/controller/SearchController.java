package com.es.search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.es.search.biz.service.SearchService;
import com.es.search.model.SearchParam;
import com.es.search.model.SearchResult;

/**
 * Http 搜索入口
 * 
 * @author lic
 * @date 2018年8月26日
 * @since v1.0.0
 */
@RestController
@RequestMapping(value = "/search")
public class SearchController {

	private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private SearchService search;

	@RequestMapping(value = "/list/1.0", method = RequestMethod.POST)
	@ResponseBody
	public SearchResult search(@RequestBody SearchParam param) {
		long start = System.currentTimeMillis();
		SearchResult sr = search.search(param);
		LOG.info("[search]cost time : {},param:{}", (System.currentTimeMillis() - start), param);
		return sr;
	}
}
