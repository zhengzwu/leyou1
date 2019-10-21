package com.search.repository;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Category;
import com.leyou.pojo.Spu;
import com.search.client.CategoryClient;
import com.search.client.GoodsClient;
import com.search.pojo.Goods;
import com.search.service.SearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private SearchService searchService;
    @Resource
    private GoodsClient goodsClient;
    @Test
    public void test(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }
    @Test
    public void load(){

        int page = 1;
        int row = 100;
        int size = 0;
        do { PageResult<Spu> spuPageResult = goodsClient.queryGoods(null, page, row, true);
        List<Spu> spus = spuPageResult.getItems();
            size=spus.size();
        if(CollectionUtils.isEmpty(spus)){
            break;
        }
        List<Goods> goods = spus.stream().map(searchService::buildGoods).collect(Collectors.toList());
        goodsRepository.saveAll(goods);
        page++;

    }while (size ==100);

    }
    @Test
    public void rey(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("all","手机"));
        Page<Goods> search = goodsRepository.search(queryBuilder.build());


    }
}