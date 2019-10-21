package com.search.searching.service;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brands;
import com.leyou.pojo.Category;
import com.leyou.pojo.SpecParams;
import com.search.client.BrandClient;
import com.search.client.CategoryClient;
import com.search.client.SpecificationClient;
import com.search.pojo.Goods;
import com.search.pojo.SearchRequest;
import com.search.pojo.SearchResult;
import com.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchingService {
    @Autowired
    private GoodsRepository goodsRepository;
    @Resource
    private CategoryClient categoryClient;
    @Resource
    private BrandClient brandClient;
    @Resource
    private SpecificationClient specificationClient;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    public SearchResult search(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            return null;
        }
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
       QueryBuilder basicQuery = getBaseQuery(request);
        queryBuilder.withQuery(basicQuery);
        int page = request.getPage();
        int size = request.getSize();
        queryBuilder.withPageable(PageRequest.of(page - 1, size));
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[] {"id","subTitle","skus"},null));
        String categoryAggName = "category";
        String brandAggName = "brand";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        AggregatedPage<Goods> pageInfo = ( AggregatedPage<Goods> )goodsRepository.search(queryBuilder.build());
        //categories聚合
        List<Category> categories = getCategories(pageInfo.getAggregation(categoryAggName));
        //brand聚合
        List<Brands> brands = getBrands(pageInfo.getAggregation(brandAggName));
        Long total  = pageInfo.getTotalElements();
        int totalPage = pageInfo.getTotalPages();
        List<Goods> goodsList = pageInfo.getContent();
        //规格参数聚合
        List<Map<String,Object>> specs = new ArrayList<>();
        if(categories.size()==1){
            specs=getSpecs(categories.get(0).getId(),basicQuery);
        }
        return  new SearchResult(total,totalPage,goodsList,categories,brands,specs);

    }
    private List<Brands> getBrands(Aggregation aggregation){
        LongTerms brandTerms = (LongTerms) aggregation;
        List<Long> bids =new ArrayList<>();
        for (LongTerms.Bucket bucket:brandTerms.getBuckets()
             ) {
            bids.add(bucket.getKeyAsNumber().longValue());
        }
        return brandClient.queryBrandByIds(bids);
    }
    private List<Category> getCategories(Aggregation aggregation){
        LongTerms categoryTerms = (LongTerms) aggregation;
        List<Long> cids =new ArrayList<>();
        for (LongTerms.Bucket bucket:categoryTerms.getBuckets()
                ) {
            cids.add(bucket.getKeyAsNumber().longValue());
        }
        return categoryClient.queryNameById(cids);
    }
    private List<Map<String,Object>> getSpecs(Long cid,QueryBuilder basicQuery){

        List<SpecParams> specParams = specificationClient.quertSpecParamBygid(null, cid, true);
        List<Map<String, Object>> specs = new ArrayList<>();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(basicQuery);
        for (SpecParams s: specParams
             ) {
            String key = s.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(key)
                    .field("specs."+key+".keyword"));
        }
        Map<String, Aggregation> aggregationMap = elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations).asMap();
        for (SpecParams s:specParams
             ) {
            Map<String,Object> spec = new HashMap<>();
            String key = s.getName();
            spec.put("k",key);
            StringTerms terms = (StringTerms)aggregationMap.get(key);
            spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));

            specs.add(spec);
        }
        return specs;
    }
    private QueryBuilder getBaseQuery (SearchRequest request){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("all",request.getKey()));
        Map<String,String> filter = request.getFilter();
        for (Map.Entry<String,String> entry: filter.entrySet()
             ) {
            String key = entry.getKey();
            if (!"cids".equals(key)&&!"brandId".equals(key)){
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }
        return boolQueryBuilder;
    }

}
