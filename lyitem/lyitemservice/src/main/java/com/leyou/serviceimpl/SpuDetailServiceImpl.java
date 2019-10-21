package com.leyou.serviceimpl;

import com.leyou.pojo.SpuDetail;
import com.leyou.mapper.SpuDetailMapper;
import com.leyou.service.SpuDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SpuDetailServiceImpl implements SpuDetailService {
    @Resource
    private SpuDetailMapper spuDetailMapper;
    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spuId);
        return spuDetailMapper.selectOne(spuDetail);
    }
}
