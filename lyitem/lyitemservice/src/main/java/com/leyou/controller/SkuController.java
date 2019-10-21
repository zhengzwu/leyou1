package com.leyou.controller;

import com.leyou.pojo.Sku;
import com.leyou.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sku")
public class SkuController {
    @Autowired
    private SkuService skuServiceImpl;
    @GetMapping("list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long spuId){
        List<Sku> skus = skuServiceImpl.querySkuBySpuId(spuId);
        return ResponseEntity.ok(skus);
    }
    @GetMapping("list/ids")
    public ResponseEntity<List<Sku>> querySkuBySkuId(@RequestParam("ids")  List<Long> ids){
        List<Sku> skus = skuServiceImpl.querySkuBySkuIds(ids);
        return ResponseEntity.ok(skus);
    }

}
