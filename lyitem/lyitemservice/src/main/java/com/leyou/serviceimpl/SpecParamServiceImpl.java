package com.leyou.serviceimpl;

import com.leyou.pojo.SpecParams;
import com.leyou.mapper.SpecParamsMapper;
import com.leyou.service.SpecParamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SpecParamServiceImpl implements SpecParamService {
    @Resource
    private SpecParamsMapper specParamsMapper;
    @Override
    public List<SpecParams> querySpecParamsBygid(Long gid, Long cid,Boolean searching) {
        SpecParams specParams = new SpecParams();
        specParams.setGroupId(gid);
        specParams.setCid(cid);
        specParams.setSearching(searching);
        return specParamsMapper.select(specParams);
    }
}
