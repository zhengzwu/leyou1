package com.leyou.api;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brands;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrandsApi {
    @GetMapping("brand/page")
    public PageResult<Brands> queryBrandsByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "sortBy",required = false)String sortBy
    );
    @PostMapping("brand")
    public Void saveBrand(Brands brands,@RequestParam("cids")List<Long> cids);

    @GetMapping("brand/cid/{id}")
    public List<Brands> queryBrandByCid(@PathVariable("id" )Long cid);

    @GetMapping("brand/list")
    public List<Brands> queryBrandByIds (@RequestParam("ids") List<Long> ids);
    @GetMapping("brand/id")
    public Brands queryBrandById(@RequestParam("id") Long id);
}
