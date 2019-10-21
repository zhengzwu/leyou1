package com.leyou.service;

import com.leyou.pojo.SpecParams;

import java.util.List;

public interface SpecParamService {
    List<SpecParams> querySpecParamsBygid(Long gid, Long cid,Boolean searching);

}
