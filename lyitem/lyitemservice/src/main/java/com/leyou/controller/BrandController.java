package com.leyou.controller;

import com.leyou.pojo.Brands;
import com.leyou.common.vo.PageResult;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandServiceImpl;
    @GetMapping("page")
    public ResponseEntity<PageResult<Brands>> queryBrandsByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "sortBy",required = false)String sortBy
            ){
        PageResult<Brands> brands = brandServiceImpl.queryBrandsByPage(key,page,rows,desc,sortBy);
        return ResponseEntity.ok(brands);
    }
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brands brands,@RequestParam("cids")List<Long> cids){
        brandServiceImpl.saveBrand(brands,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("cid/{id}")
    public ResponseEntity<List<Brands>> queryBrandByCid(@PathVariable("id" )Long cid){
        List<Brands> brands =brandServiceImpl.queryBrandsByCid(cid);
        return ResponseEntity.ok(brands);
    }
    @GetMapping("list")
    public ResponseEntity<List<Brands>> queryBrandByIds (@RequestParam("ids") List<Long> ids){
        List<Brands> brands = brandServiceImpl.queryBrandsByIds(ids);
        return ResponseEntity.ok(brands);
    }
    @GetMapping("id")
    public ResponseEntity<Brands> queryBrandById(@RequestParam("id") Long id){
        Brands brands = brandServiceImpl.queryBrandsById(id);
    return ResponseEntity.ok(brands);
    }

}
