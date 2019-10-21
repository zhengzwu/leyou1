package com.leyou.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.pojo.Brands;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.mapper.BrandMapper;
import com.leyou.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Resource
    private BrandMapper brandMapper;
    @Override
    public PageResult<Brands> queryBrandsByPage(String key, Integer page, Integer rows, Boolean desc, String sortBy) {
        PageHelper.startPage(page,rows);
        Example example = new Example(Brands.class);
        //过滤
        if(StringUtils.isNotBlank(key)){
            //创建过滤条件
            example.createCriteria().orLike("name","%"+key+"%")
                    .orEqualTo("letter",key.toUpperCase());
        }
        //排序
        if(StringUtils.isNotBlank(sortBy)){
            String oderByClause = sortBy+(desc? " DESC":" ASC");
            example.setOrderByClause(oderByClause);//orderByClause 排序语句
        }
      List<Brands> list = brandMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRANDS_NOT_FOUND);
        }
        PageInfo<Brands> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),list);
    }

    @Override
    @Transactional
    public void saveBrand(Brands brands, List<Long> cids) {
      //  brands.setId(null);
        int count = brandMapper.insertSelective(brands);
        for (Long cid: cids) {
            brandMapper.insertCategoryBrand(cid,brands.getId());
        }
    }

    @Override
    public List<Brands> queryBrandsByCid(Long cid) {
        return brandMapper.queryBrandsByCid(cid);
    }

    @Override
    public List<Brands> queryBrandsByIds(List<Long> ids) {
       return brandMapper.selectByIdList(ids);
    }

    @Override
    public Brands queryBrandsById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}
