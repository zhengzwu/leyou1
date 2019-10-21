package com.leyou.controller;

import com.leyou.pojo.Category;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryServiceImpl;
    @GetMapping("list" )
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam(value = "pid",defaultValue = "0") Long pid){
        System.out.println(pid);
       List<Category> list =  categoryServiceImpl.queryByParentId(pid);
       if(CollectionUtils.isEmpty(list)){
           throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
       }
       return ResponseEntity.ok(list);
    }
    @GetMapping("bid/{id}")
    public ResponseEntity<List<Category>> queryCategoryByBrandId(@PathVariable("id")Long bid){
        System.out.println(bid);
        List<Category> categories = categoryServiceImpl.queryCategoryByBrandId(bid);
        return ResponseEntity.ok(categories);
    }
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryNameById(@RequestParam("ids") List<Long> ids){
        List<Category> categories = categoryServiceImpl.queryNameById(ids);
        return ResponseEntity.ok(categories);
    }
}
