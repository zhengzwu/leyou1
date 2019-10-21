package com.leyou.mapper;

import com.leyou.pojo.Brands;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brands>,IdListMapper<Brands,Long> {
    @Insert("insert into tb_category_brand (category_id,brand_id)values (#{cid},#{bid})")
    public int insertCategoryBrand(@Param("cid" ) Long cid,@Param("bid")Long bid);
   @Select("select * from tb_brand where id in (select brand_id from tb_category_brand where category_id = #{cid})")
    public List<Brands> queryBrandsByCid(Long cid);

}