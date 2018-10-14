package com.es.search.biz.es;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.es.search.constant.FilterType;
import com.es.search.constant.SortType;
import com.es.search.model.SearchParam;

/**
 * 搜索查询封装
 * 
 * @author lic
 * @date 2018年8月29日
 * @since
 */
public class SearchProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchProcessor.class);

	/**
	 * 通过请求参数得到response
	 *
	 * @param param
	 * @return 2017年9月18日
	 */
	public static SearchResponse queryResponse(SearchParam param, TransportClient client) {
		if (null != param) {
			SearchRequestBuilder builder = client.prepareSearch(param.getCat()).setTypes(param.getCat())
					.setSearchType(SearchType.QUERY_THEN_FETCH).setExplain(false);
			buildFlcontext(param, builder);
			buildSort(param, builder);
			buildFilter(param, builder);
			buildSize(param, builder);
			LOGGER.info(builder.toString());
			return builder.get();
		}
		return null;
	}

	/**
	 * 构造查询词
	 *
	 * @param input
	 * @param output 2017年11月21日
	 */
	private static void buildFlcontext(SearchParam input, SearchRequestBuilder output) {
		String flcontext = input.getFlcontext();
		if (null != flcontext) {
			String[] fields = flcontext.split(",");
			output.setSource(SearchSourceBuilder.searchSource().fetchSource(fields, new String[] {}));
		}
	}

	/**
	 * 构造filterQuery
	 *
	 * @param input
	 * @param output 2017年9月19日
	 */
	@SuppressWarnings(value = "unchecked")
	private static void buildFilter(SearchParam input, SearchRequestBuilder output) {
		Map<String, Object> filterMap = input.getFilter();
		if (null == filterMap || filterMap.isEmpty()) {
			return;
		}
		Map<String, Object> filters = input.getFilter();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		/** filter */
		for (Entry<String, Object> entry : filters.entrySet()) {
			String[] fields = entry.getKey().split(",");
			Object value = entry.getValue();
			if (value instanceof Map) {
				boolQueryBuilder
						.must(buildFieldQueryFilter((Map<String, List<Object>>) entry.getValue(), entry.getKey()));
			} else if (value instanceof String) {
				String valueStr = (String) value;
				if (valueStr.indexOf(".*") > -1) { /** 模糊查询 */
					BoolQueryBuilder fieldsBoolQuery = QueryBuilders.boolQuery();
					for (String field : fields) {
						fieldsBoolQuery.should(QueryBuilders.regexpQuery(field, valueStr));
					}
					boolQueryBuilder.must(fieldsBoolQuery);
				} else { /** 多字段查询 */
					boolQueryBuilder.must(QueryBuilders.multiMatchQuery(valueStr, fields).operator(Operator.AND));
				}
			} else {
				boolQueryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
			}
		}
		/** query */
		output.setQuery(boolQueryBuilder);
	}

	/**
	 * 构造排序，可传多字段排序
	 *
	 * @param input
	 * @param output 2017年9月19日
	 */
	private static void buildSort(SearchParam input, SearchRequestBuilder output) {
		Map<String, String> sortMap = input.getSort();
		if (null == sortMap || sortMap.isEmpty()) {
			return;
		}
		for (Entry<String, String> entry : sortMap.entrySet()) {
			switch (entry.getValue()) {
			case SortType.DESC:
				output.addSort(entry.getKey(), SortOrder.DESC);
				break;
			case SortType.ASC:
				output.addSort(entry.getKey(), SortOrder.ASC);
				break;
			default:
				output.addSort("update_time", SortOrder.DESC);
				break;
			}
		}
	}

	private static void buildSize(SearchParam input, SearchRequestBuilder output) {
		int from = input.getPage() * input.getSize();
		output.setFrom(from).setSize(input.getSize());
	}

	/**
	 * 按入参FilterType判断过滤类型
	 * 
	 * <如果需要按字符串类型字段排序，请在建索引时，指定字段为不分词>
	 *
	 * @param fieldFilter
	 * @param field
	 * @return 2017年9月22日
	 */
	private static BoolQueryBuilder buildFieldQueryFilter(Map<String, List<Object>> fieldFilter, String field) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		if (null != fieldFilter && !fieldFilter.isEmpty()) {
			for (Entry<String, List<Object>> filter : fieldFilter.entrySet()) {
				List<Object> values = filter.getValue();
				if (null != values && !values.isEmpty()) {
					switch (filter.getKey()) {
					case FilterType.GT:
						boolQuery.must(QueryBuilders.rangeQuery(field).gt(values.get(0)).lt(Long.MAX_VALUE));
						break;
					case FilterType.GTE:
						boolQuery.must(QueryBuilders.rangeQuery(field).gte(values.get(0)).lt(Long.MAX_VALUE));
						break;
					case FilterType.LT:
						boolQuery.must(QueryBuilders.rangeQuery(field).gte(0).lt(values.get(0)));
						break;
					case FilterType.LTE:
						boolQuery.must(QueryBuilders.rangeQuery(field).gte(0).lte(values.get(0)));
						break;
					case FilterType.RANGE:
						boolQuery.must(buildRangeQuery(filter, field));
						break;
					case FilterType.AND:
						boolQuery.must(buildAndBoolQuery(filter, field));
						break;
					case FilterType.OR:
						boolQuery.must(buildOrBoolQuery(filter, field));
						break;
					case FilterType.NOT:
						boolQuery.must(buildNotBoolQuery(filter, field));
						break;
					default:
						break;
					}
				}
			}
		}
		return boolQuery;
	}

	/* 通过已知filter构造or boolquery */
	private static BoolQueryBuilder buildOrBoolQuery(Entry<String, List<Object>> filter, String field) {
		BoolQueryBuilder orQuery = QueryBuilders.boolQuery();
		for (Object obj : filter.getValue()) {
			if (obj instanceof String) {
				String value = (String) obj;
				if (value.indexOf(".*") > -1) {
					orQuery.should(QueryBuilders.regexpQuery(field, value));
				} else {
					orQuery.should(QueryBuilders.multiMatchQuery(obj, field));
				}
			} else {
				orQuery.should(QueryBuilders.multiMatchQuery(obj, field));
			}
		}
		return orQuery;
	}

	/* 通过已知filter构造not boolquery */
	private static BoolQueryBuilder buildNotBoolQuery(Entry<String, List<Object>> filter, String field) {
		BoolQueryBuilder notQuery = QueryBuilders.boolQuery();
		for (Object obj : filter.getValue()) {
			notQuery.mustNot(QueryBuilders.multiMatchQuery(obj, field));
		}
		return notQuery;
	}

	/* 通过已知filter构造and boolquery */
	private static BoolQueryBuilder buildAndBoolQuery(Entry<String, List<Object>> filter, String field) {
		BoolQueryBuilder andQuery = QueryBuilders.boolQuery();
		for (Object obj : filter.getValue()) {
			andQuery.must(QueryBuilders.multiMatchQuery(obj, field));
		}
		return andQuery;
	}

	/* 通过已知filter构造rangeQuery */
	private static RangeQueryBuilder buildRangeQuery(Entry<String, List<Object>> filter, String field) {
		RangeQueryBuilder rangQuery = QueryBuilders.rangeQuery(field);
		try {
			rangQuery.gte(filter.getValue().get(0)).lte(filter.getValue().get(1));
		} catch (Exception e) {
			LOGGER.error("[buildRangeQuery]build range query error,filter:{},field:{}", filter, field, e);
		}
		return rangQuery;
	}
}
