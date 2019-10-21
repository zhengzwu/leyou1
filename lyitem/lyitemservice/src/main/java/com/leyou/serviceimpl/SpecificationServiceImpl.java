package com.leyou.serviceimpl;

import com.leyou.pojo.SpecGroup;
import com.leyou.mapper.SpecificationMapper;
import com.leyou.pojo.SpecParams;
import com.leyou.service.SpecParamService;
import com.leyou.service.SpecificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationServiceImpl implements SpecificationService {
    @Resource
    private SpecParamService specParamServiceImpl;
    @Resource
    private SpecificationMapper specificationMapper;
    @Override
    public List<SpecGroup> querySpecByCid(Long cid) {
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        List<SpecGroup> list= specificationMapper.select(group);
        return  list;
    }

    @Override
    public List<SpecGroup> queryListByCid(Long cid) {
        List<SpecGroup> specGroups = querySpecByCid(cid);
        List<SpecParams> specParams = specParamServiceImpl.querySpecParamsBygid(null, cid, null);
        //map 中 key 是组id  value包含一个组下所有参数
        Map<Long, List<SpecParams>> map = new HashMap<>();
        //按参数遍历循环
        for (SpecParams param : specParams) {
            if(!map.containsKey(param.getGroupId())){
                // 如果不包含组Id，说明是第一次出现,因此要新增一个List
                map.put(param.getGroupId(),new ArrayList<>());
            }
            // 如果groupId已经存在 也要将param添加到value的list当中

            List<SpecParams> list = map.get(param.getGroupId());//这里的list 是map中value值list
            list.add(param);
        }

        // 填充param到group中
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }

        return specGroups;
    }
}
