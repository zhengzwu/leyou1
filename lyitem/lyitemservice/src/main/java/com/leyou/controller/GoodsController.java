package com.leyou.controller;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.dto.CartDTO;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.GoodService;
import com.leyou.service.SpuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spu")
public class GoodsController {
    @Autowired
    private GoodService goodServiceImpl;
    @Autowired
    private SpuDetailService spuDetailServiceImpl;
    @GetMapping("page")
    public ResponseEntity<PageResult<Spu>> queryGoods(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) Boolean saleable
    ){
      PageResult<Spu> spu =  goodServiceImpl.queryGood(key,page,rows,saleable);

        return ResponseEntity.ok(spu);
    }
    @GetMapping("detail/{spuid}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuid") Long spuId){
        SpuDetail spuDetail = spuDetailServiceImpl.querySpuDetailBySpuId(spuId);
        if(spuDetail==null){
            throw new LyException(ExceptionEnum.SPU_NOT_FOUND);
        }
        return ResponseEntity.ok(spuDetail);
    }
    @GetMapping("{id}")
    public ResponseEntity<Spu> queryById(@PathVariable("id") Long id){
        Spu spu = goodServiceImpl.querySpuById(id);
        return  ResponseEntity.ok(spu);
    }
    @PostMapping("stock/decrease")
    public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDTO> cartDTOS){
        goodServiceImpl.decreaseStock(cartDTOS);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
