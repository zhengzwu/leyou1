package com.leyou.service;

import com.leyou.dto.CartDTO;
import com.leyou.pojo.Spu;
import com.leyou.common.vo.PageResult;

import java.util.List;


public interface GoodService {
  PageResult<Spu> queryGood(String key, Integer page, Integer rows, Boolean saleable);

    Spu querySpuById(Long id);

    void decreaseStock(List<CartDTO> cartDTOS);
}
