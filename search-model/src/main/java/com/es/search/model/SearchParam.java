package com.es.search.model;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 搜索请求参数
 * 
 * @author lic
 * @date 2018年8月29日
 * @since v1.0.0
 */
public class SearchParam {

	private String cat;
	private String query;
	private int page = 0;
	private int size = 1;
	private Map<String, Object> filter;
	private Map<String, String> sort;
	private String facet;
	private String flcontext;

	public SearchParam() {
	}

	public SearchParam(SearchParam param) {
		this.cat = param.getCat();
		this.query = param.getQuery();
		this.page = param.getPage();
		this.size = param.getSize();
		this.filter = param.getFilter();
		this.sort = param.getSort();
		this.facet = param.getFacet();
		this.flcontext = param.getFlcontext();
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public Map<String, String> getSort() {
		return sort;
	}

	public void setSort(Map<String, String> sort) {
		this.sort = sort;
	}

	public String getFacet() {
		return facet;
	}

	public void setFacet(String facet) {
		this.facet = facet;
	}

	public String getFlcontext() {
		return flcontext;
	}

	public void setFlcontext(String flcontext) {
		this.flcontext = flcontext;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
