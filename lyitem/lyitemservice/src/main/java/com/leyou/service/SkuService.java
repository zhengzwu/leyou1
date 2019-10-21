package com.leyou.service;

import com.leyou.pojo.Sku;

import java.util.List;

public interface SkuService {
    List<Sku> querySkuBySpuId(Long spuId);

    List<Sku> querySkuBySkuIds(List<Long> ids);
}
