package com.leyou.service;

import com.leyou.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> queryByParentId(Long pid);

    List<Category> queryCategoryByBrandId(Long bid);
    List<Category> queryNameById(List<Long> ids);
}
