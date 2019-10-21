package com.leyou.api;

import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {
    @GetMapping("spec/groups/{cid}")
    public List<SpecGroup> querySpecByCid(@PathVariable("cid") Long cid);
    @GetMapping("spec/params")
    public List<SpecParams> quertSpecParamBygid(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching" ,required = false) Boolean searching
    );
    @GetMapping("spec/group")
    public List<SpecGroup> queryListByCid(@RequestParam("cid") Long cid);
}
