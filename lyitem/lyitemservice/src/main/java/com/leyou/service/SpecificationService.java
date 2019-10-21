package com.leyou.service;

import com.leyou.pojo.SpecGroup;

import java.util.List;

public interface SpecificationService {
 List<SpecGroup> querySpecByCid(Long cid);

    List<SpecGroup>  queryListByCid(Long cid);
}
