package com.leyou.controller;

import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParams;
import com.leyou.service.SpecParamService;
import com.leyou.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationServiceImpl;
    @Autowired
    private SpecParamService specParamServiceImpl;
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> list = specificationServiceImpl.querySpecByCid(cid);
        return ResponseEntity.ok(list);
    }
    @GetMapping("params")
    public ResponseEntity<List<SpecParams>> quertSpecParamBygid(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching" ,required = false) Boolean searching
    ){
        List<SpecParams> specParams = specParamServiceImpl.querySpecParamsBygid(gid,cid,searching);
        return ResponseEntity.ok(specParams);
    }
    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid){
        return ResponseEntity.ok(specificationServiceImpl.queryListByCid(cid));
    }
}
