package com.leyou.api;

import com.leyou.common.vo.PageResult;
import com.leyou.dto.CartDTO;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {
    @GetMapping("spu/page")
    public PageResult<Spu> queryGoods(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) Boolean saleable
    );
    @GetMapping("spu/detail/{spuid}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuid") Long spuId);
    @PostMapping("goods")
    public Void addGoodBySpu(@RequestBody Spu spu);
    @PutMapping("goods")
    public Void updateGoodBySpu(@RequestBody Spu spu);
    @GetMapping("spu/{id}")
    public Spu queryById(@PathVariable("id") Long id);
    @PostMapping("spu/stock/decrease")
    void decreaseStock(@RequestBody List<CartDTO> cartDTOS);
}
