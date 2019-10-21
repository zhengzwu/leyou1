package com.leyou.serviceimpl;

import com.leyou.pojo.Category;
import com.leyou.mapper.CategoryMapper;
import com.leyou.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryByParentId(Long pid) {
        Category t = new Category();
        t.setParentId(pid);
        return categoryMapper.select(t);
    }

    @Override
    public List<Category> queryCategoryByBrandId(Long bid) {

        return categoryMapper.queryCategoryByBrandId(bid);
    }

    @Override
    public List<Category> queryNameById(List<Long> ids) {
        return categoryMapper.selectByIdList(ids);
    }
}