package com.leyou.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.dto.CartDTO;
import com.leyou.mapper.*;
import com.leyou.pojo.Category;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.GoodService;
import com.leyou.service.SkuService;
import com.leyou.service.SpuDetailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodServiceImpl implements GoodService {
    @Resource
    private SkuService skuServiceImpl;
    @Resource
    private SpuDetailService spuDetailServiceImpl;
    @Resource
    private SpuMapper spuMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private BrandMapper brandMapper;
    @Autowired
    private StockMapper stockMapper;
    @Override
    public PageResult<Spu> queryGood(String key, Integer page, Integer rows, Boolean saleable) {
        PageHelper.startPage(page,rows);

        Example example = new Example(Spu.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("title","%"+key+"%");
        }
        if(saleable!=null){
          example.createCriteria().andEqualTo("saleable",saleable);
        }
        example.setOrderByClause("last_update_time DESC");
        List<Spu> spus= spuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(spus)){
            System.out.println("----500------");
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //解析分类以及品牌的名称
        loadCnameAndBname(spus);
        PageInfo info = new PageInfo(spus);
        return new PageResult<>(info.getTotal(),spus);
    }

    @Override
    public Spu querySpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        List<Sku> skus = skuServiceImpl.querySkuBySpuId(id);
        spu.setSkus(skus);
        SpuDetail spuDetail = spuDetailServiceImpl.querySpuDetailBySpuId(id);
        spu.setSpuDetail(spuDetail);
        return spu;
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOS) {
        for (CartDTO cartDTO : cartDTOS) {
            int count = stockMapper.decreaseStock(cartDTO.getSkuId(), cartDTO.getNum());
            if(count != 1){
                throw new LyException(ExceptionEnum.STOCK_NOT_ENOUGH);
            }
        }
    }

    private void loadCnameAndBname(List<Spu> spus) {
        for (Spu spu:spus) {
            List ids = Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3());
           List<Category> categories =  categoryMapper.selectByIdList(ids);
           List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
           spu.setCname(StringUtils.join(names,"/"));
           spu.setBname(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
        }
        }


}
