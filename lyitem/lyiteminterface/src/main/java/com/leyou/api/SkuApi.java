package com.leyou.api;

import com.leyou.pojo.Sku;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SkuApi {
    @GetMapping("sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);

    @GetMapping("sku/list/ids")
    public List<Sku> querySkuBySkuId(@RequestParam("ids") List<Long> ids);


}