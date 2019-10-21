package com.leyou.service;

import com.leyou.pojo.Brands;
import com.leyou.common.vo.PageResult;

import java.util.List;

public interface BrandService {
    PageResult<Brands> queryBrandsByPage(String key, Integer page, Integer rows, Boolean desc, String sortBy);

    void saveBrand(Brands brands,List<Long> cids);

    List<Brands> queryBrandsByCid(Long cid);

    List<Brands> queryBrandsByIds(List<Long> ids);

    Brands queryBrandsById(Long id);
}
