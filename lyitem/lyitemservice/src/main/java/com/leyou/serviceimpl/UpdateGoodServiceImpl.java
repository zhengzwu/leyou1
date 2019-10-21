package com.leyou.serviceimpl;

import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.Stock;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.mapper.SkuMapper;
import com.leyou.mapper.SpuDetailMapper;
import com.leyou.mapper.SpuMapper;
import com.leyou.mapper.StockMapper;
import com.leyou.service.UpdateGoodService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateGoodServiceImpl implements UpdateGoodService {
    @Resource
    private SpuMapper spuapper;
    @Resource
    private SkuMapper skuMapper;
    @Resource
    private SpuDetailMapper spuDetailMapper;
    @Resource
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Override
    @Transactional
    public void updateGOodBySpu(Spu spu) {
        if(spu.getId()==null){
            throw  new LyException(ExceptionEnum.SPU_NOT_FOUND);
        }

        //先删除sku  stock 再增加
        //删除sku
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        if(!CollectionUtils.isEmpty(skuList)) {
            //删除sku
            skuMapper.delete(sku);

            //删除stock
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }
         spu.setValid(false);
         spu.setSaleable(true);
         spu.setCreateTime(null);
         spu.setLastUpdateTime(new Date());
         int count = spuapper.updateByPrimaryKey(spu);
         if(count!=1){
             throw new LyException(ExceptionEnum.UPDATE_SPU_ERROR);
         }
          int index = spuDetailMapper.updateByPrimaryKey(spu.getSpuDetail());
         if(index!=1){
             throw new LyException(ExceptionEnum.UPDATE_SPU_ERROR);

         }
        List<Stock> stocks = new ArrayList();
        List<Sku> skus = spu.getSkus();
        for (Sku s:skus
                ) {
            s.setCreateTime(new Date());
            s.setLastUpdateTime(s.getCreateTime());
            s.setSpuId(spu.getId());
            skuMapper.insert(s);
            Stock stock = new Stock();
            stock.setSkuId(s.getId());
            stock.setStock(s.getStock());
            stocks.add(stock);
        }
        stockMapper.insertList(stocks) ;
        amqpTemplate.convertAndSend("leyou.exchange","item.update",spu.getId());
    }
}
