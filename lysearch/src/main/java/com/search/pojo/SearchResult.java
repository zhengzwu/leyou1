package com.search.pojo;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brands;
import com.leyou.pojo.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchResult extends PageResult<Goods>{
    private List<Category> categories;
    private List<Brands> brands;
    private List<Map<String,Object>> specs;
    public SearchResult(Long total, Integer totalPage, List<Goods> items,
                        List<Category> categories, List<Brands> brands,List<Map<String,Object>>specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs=specs;
    }
}
