package com.leyou.serviceimpl;

import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.pojo.Stock;
import com.leyou.mapper.SkuMapper;
import com.leyou.mapper.SpuDetailMapper;
import com.leyou.mapper.SpuMapper;
import com.leyou.mapper.StockMapper;
import com.leyou.service.AddGoodService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AddGoodServiceImpl implements AddGoodService {
    @Resource
    private SpuMapper spuMapper;
    @Resource
    private SpuDetailMapper spuDetailMapper;
    @Resource
    private SkuMapper skuMapper;
    @Resource
    private  StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Override
    @Transactional
    public void addGOodBySpu(Spu spu) {
        spu.setSaleable(true);
        spu.setValid(false);
        int count  = spuMapper.insert(spu);
        if(count!=1){
            System.out.println("spu新增出错");
        }
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        int count1=spuDetailMapper.insert(spuDetail);
        if(count1!=1){
            System.out.println("spuDetail新增出错");
        }
        List<Stock> stocks = new ArrayList();
      List<Sku> skus = spu.getSkus();
        for (Sku sku:skus
             ) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            skuMapper.insert(sku);
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stocks.add(stock);
        }
          stockMapper.insertList(stocks) ;
        amqpTemplate.convertAndSend("leyou.exchange","item.insert",spu.getId());
    }
}
