package com.search.mq;

import com.leyou.pojo.Spu;
import com.search.client.GoodsClient;
import com.search.pojo.Goods;
import com.search.repository.GoodsRepository;
import com.search.searching.service.SearchingService;
import com.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SearchListener {
    @Resource
    private SearchService searchService;
    @Autowired
    private GoodsRepository goodsRepository;
    @Resource
    private GoodsClient goodsClient;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.create.index.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public  void  listen( Long spuId){
        Spu spu = goodsClient.queryById(spuId);
        Goods goods = searchService.buildGoods(spu);
        System.out.print(goods.getAll());
        goodsRepository.save(goods);
    }
}
