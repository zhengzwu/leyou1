package com.leyou.controller;

import com.leyou.pojo.Cart;
import com.leyou.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class CartController {
    @Resource
    private CartService cartService;
    @PostMapping
    private ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }
    @GetMapping("list")
    public ResponseEntity<List<Cart>> queryCart(){
        List<Cart> carts = cartService.queryCart();
        return ResponseEntity.ok(carts);
    }
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("id")Long skuId,@RequestParam("num")Integer num){
        cartService.updateNum(skuId,num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId")Long skuId){
        cartService.deleteCart(skuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
