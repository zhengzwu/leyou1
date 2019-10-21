package com.leyou.mapper;

import com.leyou.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category> ,IdListMapper<Category,Long>{
    @Select("select * from tb_category where id in( select" +
            " category_id from tb_category_brand where brand_id = #{bid}   )")
    List<Category> queryCategoryByBrandId(Long bid);
}
