package com.leyou.api;

import com.leyou.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {
    @GetMapping("category/list" )
     List<Category> queryByParentId(@RequestParam(value = "pid",defaultValue = "0") Long pid);
    @GetMapping("category/bid/{id}")
     List<Category> queryCategoryByBrandId(@PathVariable("id")Long bid);
    @GetMapping("category/list/ids")
   List<Category> queryNameById(@RequestParam("ids") List<Long> ids);
}
