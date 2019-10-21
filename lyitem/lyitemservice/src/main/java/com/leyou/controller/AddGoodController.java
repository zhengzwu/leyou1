package com.leyou.controller;

import com.leyou.pojo.Spu;
import com.leyou.service.AddGoodService;
import com.leyou.service.UpdateGoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AddGoodController {
    @Resource
    private AddGoodService addGoodServiceImpl;
    @Resource
    private UpdateGoodService updateGoodServiceImpl;
    @PostMapping("goods")
    public ResponseEntity<Void> addGoodBySpu(@RequestBody Spu spu){
        System.out.println("增"+spu.getCid1());
        addGoodServiceImpl.addGOodBySpu(spu);
        return ResponseEntity.ok().build();
    }
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoodBySpu(@RequestBody Spu spu){
        System.out.println("改"+spu.getCid1());
        updateGoodServiceImpl.updateGOodBySpu(spu);
        return ResponseEntity.ok().build();
    }

}
