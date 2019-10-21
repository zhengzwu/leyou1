package com.leyou.serviceimpl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.mapper.SkuMapper;
import com.leyou.mapper.StockMapper;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Stock;
import com.leyou.service.SkuService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkuServiceImpl implements SkuService {
    @Resource
    private SkuMapper skuMapper;
    @Resource
    private StockMapper stockMapper;
    @Override
    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionEnum.SKU_NOT_FOUND);

        }
        for (Sku s:skuList
             ) {
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if(stock==null){
                throw new LyException(ExceptionEnum.STOCK_NOT_FOUND);
            }
            s.setStock(stock.getStock());

        }
        //这里需要进一步查询库存
        return skuList;
    }

    @Override
    public List<Sku> querySkuBySkuIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.SKU_NOT_FOUND);
        }

        //查询库存
        List<Stock> stockList = stockMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(stockList))
            throw new LyException(ExceptionEnum.STOCK_NOT_FOUND);

        //把stock变成一个map，其key：skuId,值：库存值
        Map<Long, Integer> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skus.forEach(s ->s.setStock(stockMap.get(s.getId())));
        return skus;
    }
}
